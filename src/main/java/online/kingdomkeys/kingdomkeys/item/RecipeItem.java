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
import net.minecraft.world.entity.Entity;
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

	public RecipeItem(Properties properties) {
		super(properties);
	}

	@Override
	public InteractionResultHolder<ItemStack> use(Level world, Player player, InteractionHand hand) {
		if (hand == InteractionHand.MAIN_HAND) {
			if (!world.isClientSide) {
				ItemStack stack = player.getMainHandItem();
				if (stack.hasTag()) {
					String[] recipes = { stack.getTag().getString("recipe1"), stack.getTag().getString("recipe2"), stack.getTag().getString("recipe3") };
					IPlayerCapabilities playerData = ModCapabilities.getPlayer(player);
					// /give Abelatox kingdomkeys:recipe{recipe1:"kingdomkeys:oathkeeper",recipe2:"kingdomkeys:diamond"} 1

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
						player.getMainHandItem().shrink(1);
					} else {
						shuffleRecipes(stack, player);
					}
				} else {
					player.displayClientMessage(new TranslatableComponent("Opened recipe"), true);
					shuffleRecipes(stack, (Player) player);
				}
			}
		}
		return super.use(world, player, hand);
	}

	public void shuffleRecipes(ItemStack stack, Player player) {
		IPlayerCapabilities playerData = ModCapabilities.getPlayer(player);

		ResourceLocation recipe1=null, recipe2=null, recipe3=null;
		
		List<ResourceLocation> list = new ArrayList<ResourceLocation>();
		for(Recipe r : RecipeRegistry.getInstance().getValues()) {
			if(!playerData.hasKnownRecipe(r.getRegistryName())) {
				if(r.getType().equals("keyblade")) {
					list.add(r.getRegistryName());
				}
			}
		}
		
		
		

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

		stack.setTag(new CompoundTag());
		if(recipe1 != null)
			stack.getTag().putString("recipe1", recipe1.toString());
		if(recipe2 != null)
			stack.getTag().putString("recipe2", recipe2.toString());
		if(recipe3 != null)
			stack.getTag().putString("recipe3", recipe3.toString());
	}

	@Override
	public void inventoryTick(ItemStack stack, Level worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
		/*if (!stack.hasTag()) {
			shuffleRecipes(stack, (PlayerEntity) entityIn);
		}*/
		super.inventoryTick(stack, worldIn, entityIn, itemSlot, isSelected);
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
							KeychainItem item = (KeychainItem) recipe.getResult().getItem();
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
