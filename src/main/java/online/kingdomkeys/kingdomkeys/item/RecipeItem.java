package online.kingdomkeys.kingdomkeys.item;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import online.kingdomkeys.kingdomkeys.api.item.IItemCategory;
import online.kingdomkeys.kingdomkeys.api.item.ItemCategory;
import online.kingdomkeys.kingdomkeys.capability.IPlayerCapabilities;
import online.kingdomkeys.kingdomkeys.capability.ModCapabilities;
import online.kingdomkeys.kingdomkeys.network.PacketHandler;
import online.kingdomkeys.kingdomkeys.network.stc.SCSyncCapabilityPacket;
import online.kingdomkeys.kingdomkeys.synthesis.recipe.Recipe;
import online.kingdomkeys.kingdomkeys.synthesis.recipe.RecipeRegistry;
import online.kingdomkeys.kingdomkeys.util.Utils;

public class RecipeItem extends Item implements IItemCategory {
	int tier=0;
	
	public RecipeItem(int tier,Properties properties) {
		super(properties);
		this.tier = tier;
	}

	@Override
	public InteractionResultHolder<ItemStack> use(Level world, Player player, InteractionHand hand) {
		if (hand == InteractionHand.MAIN_HAND) {
			if (!world.isClientSide) {
				ItemStack stack = player.getMainHandItem();

				//Allow recipes to be given with pre-set keyblades
				//If a recipe already has a tag, it will try learn those
				//If the player already has learnt them, the recipe item will be refreshed to try get new recipes.
				IPlayerCapabilities playerData = ModCapabilities.getPlayer(player);
				if (stack.hasTag()) {
					System.out.println(tier+" "+playerData.getSynthLevel());
					if(tier <= playerData.getSynthLevel())
						learnRecipes(player, stack);
					else
						player.displayClientMessage(new TranslatableComponent("You can't learn that recipe yet"), true);
				} else {
					System.out.println(tier);
					List<ResourceLocation> missingKeyblades = getMissingRecipes(playerData, "keyblade", tier);
					List<ResourceLocation> missingItems = getMissingRecipes(playerData, "item", tier);
					
					List<String> types = new ArrayList<String>();
					types.add("keyblade");
					types.add("item");
					if(missingKeyblades.size() == 0) {
						types.remove("keyblade");
					}
					if(missingItems.size() == 0) {
						types.remove("item");
					}
					
					String type = "";
					if(types.size() > 1) {
						int num = world.random.nextInt(types.size());
						type = types.get(num);
					} else if(types.size() == 1){
						type = types.get(0);
					} else {
						player.displayClientMessage(new TranslatableComponent("No more recipes to learn"), true);
						return super.use(world, player, hand);
					}
					
					player.displayClientMessage(new TranslatableComponent("Opened "+type+" recipe"), true);

					//Set up the recipe item with the given type
					//We get here if there are recipes still available to learn.
					shuffleRecipes(stack, player, type);
				}
			}
		}
		return super.use(world, player, hand);
	}

	private void learnRecipes(Player player, ItemStack stack) {
		final CompoundTag stackTag = stack.getTag();
		String[] recipes = { stackTag.getString("recipe1"), stackTag.getString("recipe2"), stackTag.getString("recipe3") };
		IPlayerCapabilities playerData = ModCapabilities.getPlayer(player);
		// /give Dev kingdomkeys:recipe{type:"keyblade",recipe1:"kingdomkeys:oathkeeper",recipe2:"kingdomkeys:fenrir"} 16

		System.out.println(tier+" "+playerData.getSynthLevel());
		if(tier > playerData.getSynthLevel()) {
			player.displayClientMessage(new TranslatableComponent("You can't learn that recipe yet"), true);
			return;
		}
		boolean consume = false;
		for (String recipe : recipes) {
			ResourceLocation rl = new ResourceLocation(recipe);
			if (RecipeRegistry.getInstance().containsKey(rl)) {
				ItemStack outputStack = new ItemStack(RecipeRegistry.getInstance().getValue(rl).getResult());
				if (recipe == null || !RecipeRegistry.getInstance().containsKey(rl)) { // If recipe is not valid
					String message = "ERROR: Recipe for " + Utils.translateToLocal(rl.toString()) + " was not learnt because it is not a valid recipe, Report this to a dev";
					player.sendMessage(new TranslatableComponent(ChatFormatting.RED + message), Util.NIL_UUID);
				} else if (playerData.hasKnownRecipe(rl)) { // If recipe already known
					String message = "Recipe for " + Utils.translateToLocal(outputStack.getDescriptionId()) + " already learnt";
					player.sendMessage(new TranslatableComponent(ChatFormatting.YELLOW + message), Util.NIL_UUID);
				} else { // If recipe is not known, learn it
					playerData.addKnownRecipe(rl);
					consume = true;
					String message = "Recipe " + Utils.translateToLocal(outputStack.getDescriptionId()) + " learnt successfully";
					player.sendMessage(new TranslatableComponent(ChatFormatting.GREEN + message), Util.NIL_UUID);
					PacketHandler.sendTo(new SCSyncCapabilityPacket(playerData), (ServerPlayer) player);
				}
			}
		}

		if (consume) {
			//remove all child tags so we don't contaminate the stack
			//This will set the stack's tag field to null once all are removed.
			stack.removeTagKey("recipe1");
			stack.removeTagKey("recipe2");
			stack.removeTagKey("recipe3");
			stack.removeTagKey("type");
			//reduce stack size by one.
			player.getMainHandItem().shrink(1);
		} else {
			//try for fresh recipes, based on what type this stack was set to. No swapping from keyblade to item recipes etc.
			//will fail successfully if none left.
			shuffleRecipes(stack, player, stackTag.getString("type"));
		}
	}

	public void shuffleRecipes(ItemStack stack, Player player, String type) {
		IPlayerCapabilities playerData = ModCapabilities.getPlayer(player);

		ResourceLocation recipe1=null, recipe2=null, recipe3=null;
		
		List<ResourceLocation> list;
		switch(type) {
		case "keyblade":
			list = getMissingRecipes(playerData, "keyblade", tier);
			
			if(list.size() == 0)
				return;
			
			if(list.size() > 0) {
				recipe1 = list.get(Utils.randomWithRange(0, list.size() - 1));
			}
			
			if(list.size() > 1) {
				do {
					recipe2 = list.get(Utils.randomWithRange(0, list.size() - 1));
				} while(recipe2 == recipe1);
			}
			
			if(list.size() > 2) {
				do {
					recipe3 = list.get(Utils.randomWithRange(0, list.size() - 1));
				} while(recipe3 == recipe1 || recipe3 == recipe2);

			}

			break;
		case "item":
			list = getMissingRecipes(playerData, "item", tier);
			if(list.size() > 0) {
				recipe1 = list.get(Utils.randomWithRange(0, list.size() - 1));
			}
			break;
		}

		stack.getOrCreateTag().putString("type", type);

		//if any recipes are on this stack, such as already learned ones, they should get overwritten
		if(recipe1 != null)
			stack.getOrCreateTag().putString("recipe1", recipe1.toString());
		if(recipe2 != null)
			stack.getOrCreateTag().putString("recipe2", recipe2.toString());
		if(recipe3 != null)
			stack.getOrCreateTag().putString("recipe3", recipe3.toString());

		//Call learn recipes immediately.
		//This will remove all child tags and then reduce stack size by one
		//recipe1 is not null if any recipes exist to learn
		if (recipe1 != null) {
			learnRecipes(player, stack);
		}
	}

	private List<ResourceLocation> getMissingRecipes(IPlayerCapabilities playerData, String type, int tier) {
		List<ResourceLocation> list = new ArrayList<ResourceLocation>();
			for(Recipe r : RecipeRegistry.getInstance().getValues()) {
				if(!playerData.hasKnownRecipe(r.getRegistryName())) {
					if(r.getType().equals(type)) {
						if(tier == 0) {
							list.add(r.getRegistryName());
						} else {
							if(r.getTier() == tier) {
								list.add(r.getRegistryName());	
							}
						}
					}
				}
			}
		return list;
	}

	@Override
	public void appendHoverText(ItemStack stack, Level worldIn, List<Component> tooltip, TooltipFlag flagIn) {
		if (stack.hasTag()) {
			for (int i = 1; i <= 3; i++) {
				String recipeName = stack.getTag().getString("recipe" + i);
				if(RecipeRegistry.getInstance().containsKey(new ResourceLocation(recipeName))) {
					Recipe recipe = RecipeRegistry.getInstance().getValue(new ResourceLocation(recipeName));
					if (recipe != null) {
						String name;
						if(recipe.getType().equals("keyblade")) {
							KeychainItem item = (KeychainItem) recipe.getResult();
							name = new ItemStack(item.keyblade).getDescriptionId();
						} else {
							name = new ItemStack(recipe.getResult()).getDescriptionId();
						}
						tooltip.add(new TranslatableComponent(Utils.translateToLocal(name)));
					}
				}
			}
		}
	}

	@Override
	public ItemCategory getCategory() {
		return ItemCategory.MISC;
	}
}
