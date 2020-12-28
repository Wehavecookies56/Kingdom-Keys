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
import online.kingdomkeys.kingdomkeys.synthesis.recipe.Recipe;
import online.kingdomkeys.kingdomkeys.synthesis.recipe.RecipeRegistry;
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
					// /give Abelatox kingdomkeys:recipe{recipe1:"kingdomkeys:oathkeeper",recipe2:"kingdomkeys:diamond"} 1
					RecipeRegistry.getInstance().getValues().forEach(r -> System.out.println(r.getType()));

					boolean consume = false;
					for (String recipe : recipes) {
						ResourceLocation rl = new ResourceLocation(recipe);
						if (RecipeRegistry.getInstance().containsKey(rl)) {
							ItemStack outputStack = new ItemStack(RecipeRegistry.getInstance().getValue(rl).getResult());							
							if (recipe == null || !RecipeRegistry.getInstance().containsKey(rl)) { // If recipe is not valid
								String message = "ERROR: Recipe for " + Utils.translateToLocal(rl.toString()) + " was not learnt because it is not a valid recipe, Report this to a dev";
								player.sendMessage(new TranslationTextComponent(TextFormatting.RED + message));
							} else if (playerData.hasKnownRecipe(rl)) { // If recipe already known
								String message = "Recipe for " + Utils.translateToLocal(outputStack.getTranslationKey()) + " already learnt";
								player.sendMessage(new TranslationTextComponent(TextFormatting.YELLOW + message));
							} else { // If recipe is not known, learn it
								playerData.addKnownRecipe(rl);
								consume = true;
								String message = "Recipe " + Utils.translateToLocal(outputStack.getTranslationKey()) + " learnt successfully";
								player.sendMessage(new TranslationTextComponent(TextFormatting.GREEN + message));
								PacketHandler.sendTo(new SCSyncCapabilityPacket(playerData), (ServerPlayerEntity) player);
							}
						}
					}

					for (int i = 0; i < playerData.getKnownRecipeList().size(); i++) {
						ResourceLocation thing = playerData.getKnownRecipeList().get(i);
						System.out.println(i + " " + thing.toString());
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

		ResourceLocation recipe1, recipe2, recipe3;

		recipe1 = Lists.keybladeRecipes.get(Utils.randomWithRange(0, Lists.keybladeRecipes.size() - 1));
		if (playerData.getKnownRecipeList().size() < Lists.keybladeRecipes.size() - 2) {
			while (playerData.hasKnownRecipe(recipe1)) {
				recipe1 = Lists.keybladeRecipes.get(Utils.randomWithRange(0, Lists.keybladeRecipes.size() - 1));
			}
		}

		recipe2 = Lists.keybladeRecipes.get(Utils.randomWithRange(0, Lists.keybladeRecipes.size() - 1));
		if (playerData.getKnownRecipeList().size() < Lists.keybladeRecipes.size() - 1) {
			while (recipe2.equals(recipe1) || playerData.hasKnownRecipe(recipe2)) {
				recipe2 = Lists.keybladeRecipes.get(Utils.randomWithRange(0, Lists.keybladeRecipes.size() - 1));
			}
		}

		recipe3 = Lists.keybladeRecipes.get(Utils.randomWithRange(0, Lists.keybladeRecipes.size() - 1));
		if (playerData.getKnownRecipeList().size() < Lists.keybladeRecipes.size()) {
			while ((recipe3.equals(recipe2) || recipe3.equals(recipe1)) || playerData.hasKnownRecipe(recipe3)) {
				recipe3 = Lists.keybladeRecipes.get(Utils.randomWithRange(0, Lists.keybladeRecipes.size() - 1));
			}
		}

		stack.setTag(new CompoundNBT());
		stack.getTag().putString("recipe1", recipe1.toString());
		stack.getTag().putString("recipe2", recipe2.toString());
		stack.getTag().putString("recipe3", recipe3.toString());

		// if(!player.world.isRemote)
		// System.out.println("Took "+(System.nanoTime()/1000000F - seed/1000000F)+"ms
		// to shuffle");
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
			for (int i = 1; i <= 3; i++) {
				String recipeName = stack.getTag().getString("recipe" + i);
				if(RecipeRegistry.getInstance().containsKey(new ResourceLocation(recipeName))) {
					Recipe recipe = RecipeRegistry.getInstance().getValue(new ResourceLocation(recipeName));
					if (recipe != null) {
						String name;
						if(recipe.getType().equals("keyblade")) {
							KeychainItem item = (KeychainItem) recipe.getResult().getItem();
							name = new ItemStack(item.keyblade).getTranslationKey();
						} else {
							name = new ItemStack(recipe.getResult()).getTranslationKey();
						}
						tooltip.add(new TranslationTextComponent(Utils.translateToLocal(name)));
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
