package online.kingdomkeys.kingdomkeys.item;

import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
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
import online.kingdomkeys.kingdomkeys.data.PlayerData;
import online.kingdomkeys.kingdomkeys.network.PacketHandler;
import online.kingdomkeys.kingdomkeys.network.stc.SCSyncPlayerData;
import online.kingdomkeys.kingdomkeys.synthesis.recipe.Recipe;
import online.kingdomkeys.kingdomkeys.synthesis.recipe.RecipeRegistry;
import online.kingdomkeys.kingdomkeys.util.Utils;

import java.util.ArrayList;
import java.util.List;

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
				//If the player already has learnt them, the recipe item will be refreshed to try get new recipes.
				PlayerData playerData = PlayerData.get(player);
				if(tier <= playerData.getSynthLevel()) { //If the player has the right tier
					if (stack.hasTag()) { //If the recipe has been generated learn it
						learnRecipes(player, stack);
					} else { //Otherwise generate it
						List<ResourceLocation> missingKeyblades = getMissingRecipes(playerData, "keyblade", tier);
						List<ResourceLocation> missingItems = getMissingRecipes(playerData, "item", tier);

						List<String> types = new ArrayList<String>();
						types.add("keyblade");
						types.add("item");
						if (missingKeyblades.size() == 0) {
							types.remove("keyblade");
						}
						if (missingItems.size() == 0) {
							types.remove("item");
						}

						String type = "";
						if (types.size() > 1) {
							int num = world.random.nextInt(types.size());
							type = types.get(num);
						} else if (types.size() == 1) {
							type = types.get(0);
						} else {
							player.displayClientMessage(Component.translatable("message.recipe.no_more_to_learn"), true);
							return super.use(world, player, hand);
						}

						//Set up the recipe item with the given type
						//We get here if there are recipes still available to learn.
						shuffleRecipes(stack, player, type);
					}
				} else { //If the player tier is not enough don't even try to generate it
					player.displayClientMessage(Component.translatable("message.recipe.cant_learn_yet"), true);
				}
			}
		}
		return super.use(world, player, hand);
	}

	private void learnRecipes(Player player, ItemStack stack) {
		final CompoundTag stackTag = stack.getTag();
		String[] recipes = { stackTag.getString("recipe1"), stackTag.getString("recipe2"), stackTag.getString("recipe3") };
		PlayerData playerData = PlayerData.get(player);
		// /give Dev kingdomkeys:recipe{type:"keyblade",recipe1:"kingdomkeys:oathkeeper",recipe2:"kingdomkeys:fenrir"} 16

		if(tier > playerData.getSynthLevel()) {
			player.displayClientMessage(Component.translatable("message.recipe.cant_learn_yet"), true);
			return;
		}
		boolean consume = false;
		for (String recipe : recipes) {
			ResourceLocation rl = ResourceLocation.parse(recipe);
			if (RecipeRegistry.getInstance().containsKey(rl)) {
				ItemStack outputStack = new ItemStack(RecipeRegistry.getInstance().getValue(rl).getResult());
				if (recipe == null || !RecipeRegistry.getInstance().containsKey(rl)) { // If recipe is not valid
					String message = "ERROR: Recipe for " + Utils.translateToLocal(rl.toString()) + " was not learnt because it is not a valid recipe, Report this to a dev";
					player.sendSystemMessage(Component.translatable(ChatFormatting.RED + message));
				} else if (playerData.hasKnownRecipe(rl)) { // If recipe already known
					player.sendSystemMessage(Component.translatable(Utils.translateToLocal("message.recipe.already_learnt"),ChatFormatting.YELLOW+Utils.translateToLocal(outputStack.getDescriptionId())));
				} else { // If recipe is not known, learn it
					playerData.addKnownRecipe(rl);
					consume = true;
					player.sendSystemMessage(Component.translatable(Utils.translateToLocal("message.recipe.learnt"), ChatFormatting.GREEN+Utils.translateToLocal(outputStack.getDescriptionId())));
					PacketHandler.sendTo(new SCSyncPlayerData(player), (ServerPlayer) player);
				}
			}
		}

		if (consume) {
			//remove all child tags so we don't contaminate the stack, this will set the stack's tag field to null once all are removed.
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
		PlayerData playerData = PlayerData.get(player);

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

	private List<ResourceLocation> getMissingRecipes(PlayerData playerData, String type, int tier) {
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
	public void appendHoverText(ItemStack stack, TooltipContext tooltipContext, List<Component> tooltip, TooltipFlag flagIn) {
		if (stack.hasTag()) {
			for (int i = 1; i <= 3; i++) {
				String recipeName = stack.getTag().getString("recipe" + i);
				if(RecipeRegistry.getInstance().containsKey(ResourceLocation.parse(recipeName))) {
					Recipe recipe = RecipeRegistry.getInstance().getValue(ResourceLocation.parse(recipeName));
					if (recipe != null) {
						String name;
						if(recipe.getType().equals("keyblade")) {
							KeychainItem item = (KeychainItem) recipe.getResult();
							name = new ItemStack(item.keyblade).getDescriptionId();
						} else {
							name = new ItemStack(recipe.getResult()).getDescriptionId();
						}
						tooltip.add(Component.translatable(Utils.translateToLocal(name)));
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
