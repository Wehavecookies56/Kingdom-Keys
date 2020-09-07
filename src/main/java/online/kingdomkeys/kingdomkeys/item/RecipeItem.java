package online.kingdomkeys.kingdomkeys.item;

import java.util.Collections;
import java.util.List;
import java.util.Random;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import online.kingdomkeys.kingdomkeys.api.item.IItemCategory;
import online.kingdomkeys.kingdomkeys.api.item.ItemCategory;
import online.kingdomkeys.kingdomkeys.capability.IPlayerCapabilities;
import online.kingdomkeys.kingdomkeys.capability.ModCapabilities;
import online.kingdomkeys.kingdomkeys.lib.Lists;
import online.kingdomkeys.kingdomkeys.network.PacketHandler;
import online.kingdomkeys.kingdomkeys.network.stc.SCSyncCapabilityPacket;
import online.kingdomkeys.kingdomkeys.util.Utils;

public class RecipeItem extends Item implements IItemCategory {

	public RecipeItem(Properties properties) {
		super(properties);
	}
	
	@Override
	public ActionResult<ItemStack> onItemRightClick(World world, PlayerEntity player, Hand hand) {
		if (hand == Hand.MAIN_HAND) {
			if (!world.isRemote) {
				ItemStack stack = player.getHeldItemMainhand();
				if (stack.hasTag()) {
					String[] recipes = { stack.getTag().getString("recipe1"), stack.getTag().getString("recipe2"), stack.getTag().getString("recipe3") };
					IPlayerCapabilities playerData = ModCapabilities.getPlayer(player);

					boolean consume = false;
					for (String recipe : recipes) {
						System.out.println(recipe+":"+Lists.keybladeRecipes.get(0));
						if (recipe == null || !Lists.keybladeRecipes.contains(new ResourceLocation(recipe))) { // If recipe is not valid
							String message = "ERROR: Recipe for " + Utils.translateToLocal(recipe) + " was not learnt because it is not a valid recipe, Report this to a dev";
							player.sendMessage(new TranslationTextComponent(TextFormatting.RED + message));
						} else if (playerData.hasKnownRecipe(new ResourceLocation(recipe))) { // If recipe already known
							String message = "Recipe for " + Utils.translateToLocal(recipe) + " already learnt";
							player.sendMessage(new TranslationTextComponent(TextFormatting.YELLOW + message));
						} else { // If recipe is not known, learn it
							playerData.addKnownRecipe(new ResourceLocation(recipe));
							consume = true;
							String message = "Recipe " + Utils.translateToLocal(recipe) + " learnt successfully";
							player.sendMessage(new TranslationTextComponent(TextFormatting.GREEN + message));
							PacketHandler.sendTo(new SCSyncCapabilityPacket(playerData), (ServerPlayerEntity) player);
						}
					}
	
					if (consume) {
						player.getHeldItemMainhand().shrink(1);
					} else {
						shuffleRecipes(player.getHeldItemMainhand(), player);
					}
				}
			}
		}
		return super.onItemRightClick(world, player, hand);
	}

	public void shuffleRecipes(ItemStack stack, PlayerEntity player) {
		IPlayerCapabilities playerData = ModCapabilities.getPlayer(player);

		long seed = System.nanoTime();
		// Shuffles the list of recipe to increase randomness
		Collections.shuffle(Lists.keybladeRecipes, new Random(seed));
		
		ResourceLocation Recipe1, Recipe2, Recipe3;
		
		Recipe1 = Lists.keybladeRecipes.get(Utils.randomWithRange(0, Lists.keybladeRecipes.size() - 1));
		if (playerData.getKnownRecipeList().size() < Lists.keybladeRecipes.size() - 2) {
			while (playerData.hasKnownRecipe(Recipe1)) {
				Recipe1 = Lists.keybladeRecipes.get(Utils.randomWithRange(0, Lists.keybladeRecipes.size() - 1));
			}
		}
		
		Recipe2 = Lists.keybladeRecipes.get(Utils.randomWithRange(0, Lists.keybladeRecipes.size() - 1));
		if (playerData.getKnownRecipeList().size() < Lists.keybladeRecipes.size() - 1) {
			while (Recipe2.equals(Recipe1) || playerData.hasKnownRecipe(Recipe2)) {
				Recipe2 = Lists.keybladeRecipes.get(Utils.randomWithRange(0, Lists.keybladeRecipes.size() - 1));
			}
		}
		
		Recipe3 = Lists.keybladeRecipes.get(Utils.randomWithRange(0, Lists.keybladeRecipes.size() - 1));
		if (playerData.getKnownRecipeList().size() < Lists.keybladeRecipes.size()) {
			while ((Recipe3.equals(Recipe2) || Recipe3.equals(Recipe1)) || playerData.hasKnownRecipe(Recipe3)) {
				Recipe3 = Lists.keybladeRecipes.get(Utils.randomWithRange(0, Lists.keybladeRecipes.size() - 1));
			}
		}

		stack.setTag(new CompoundNBT());
		stack.getTag().putString("recipe1", Recipe1.toString());
		stack.getTag().putString("recipe2", Recipe2.toString());
		stack.getTag().putString("recipe3", Recipe3.toString());
		
		//if(!player.world.isRemote)
			//System.out.println("Took "+(System.nanoTime()/1000000F - seed/1000000F)+"ms to shuffle");
	}

	@Override
	public void inventoryTick(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
		if (!stack.hasTag()) {
			shuffleRecipes(stack, (PlayerEntity) entityIn);
		}
		super.inventoryTick(stack, worldIn, entityIn, itemSlot, isSelected);
	}
	
	@Override
	public void addInformation(ItemStack stack, World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
		if (stack.hasTag()) {
			String recipe1 = stack.getTag().getString("recipe1");
			String recipe2 = stack.getTag().getString("recipe2");
			String recipe3 = stack.getTag().getString("recipe3");

			tooltip.add(new TranslationTextComponent(Utils.translateToLocal(recipe1)));
			tooltip.add(new TranslationTextComponent(Utils.translateToLocal(recipe2)));
			tooltip.add(new TranslationTextComponent(Utils.translateToLocal(recipe3)));
		}
	}

	@Override
	public ItemCategory getCategory() {
		return ItemCategory.MISC;
	}
}
