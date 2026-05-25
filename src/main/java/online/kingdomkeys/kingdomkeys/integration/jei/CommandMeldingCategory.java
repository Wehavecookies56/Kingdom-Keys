package online.kingdomkeys.kingdomkeys.integration.jei;

import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.world.item.ItemStack;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.item.MagicSpellItem;
import online.kingdomkeys.kingdomkeys.item.ModItems;
import online.kingdomkeys.kingdomkeys.lib.Strings;
import online.kingdomkeys.kingdomkeys.synthesis.melding.Melding;
import online.kingdomkeys.kingdomkeys.util.Utils;

public class CommandMeldingCategory implements IRecipeCategory<Melding> {

	public static final RecipeType<Melding> TYPE = RecipeType.create(KingdomKeys.MODID, "melding", Melding.class);
	private final IDrawable icon;
	private final IDrawable background;

	public CommandMeldingCategory(IGuiHelper guiHelper) {
		icon = guiHelper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(ModItems.fireSpell.get()));
		background = guiHelper.drawableBuilder(ResourceLocation.fromNamespaceAndPath(KingdomKeys.MODID, "textures/gui/melding_recipe_background.png"), 0, 0, 114, 56).build();
	}

	@Override
	public RecipeType<Melding> getRecipeType() {
		return TYPE;
	}

	@Override
	public Component getTitle() {
		return Component.translatable("jei.category.kingdomkeys.melding");
	}

	@Override
	public IDrawable getBackground() {
		return background;
	}

	@Override
	public IDrawable getIcon() {
		return icon;
	}

	@Override
	public void draw(Melding data, IRecipeSlotsView recipeSlotsView, GuiGraphics guiGraphics, double mouseX, double mouseY) {
		Minecraft mc = Minecraft.getInstance();
		Component title = new ItemStack(data.getResult()).getHoverName();
		FormattedCharSequence formattedcharsequence = title.getVisualOrderText();
		guiGraphics.drawString(Minecraft.getInstance().font, formattedcharsequence, background.getWidth()/2 - Minecraft.getInstance().font.width(formattedcharsequence) / 2, -15, 0x555555, false);
		guiGraphics.drawString(mc.font, Utils.translateToLocal(Strings.Gui_Shop_Buy_Cost)+" " + data.getCost(), 0, 30, 0xFFFF55, false);
		guiGraphics.drawString(mc.font, Utils.translateToLocal(Strings.Gui_Shop_Tier)+" " + Utils.getTierFromInt(data.getTier()), 0, 40, 0x2255FF, false);
	}

	@Override
	public void setRecipe(IRecipeLayoutBuilder builder, Melding data, IFocusGroup focuses) {
		ItemStack ingredient1;
		ItemStack ingredient2;

		if(data.getIngredient1() != null && data.getIngredient1() instanceof MagicSpellItem ing1) {
			ingredient1 = new ItemStack(ing1);
			ing1.setExp(ingredient1, ing1.getMaxExp());
		} else {
			ingredient1 = new ItemStack(data.getIngredient1());
		}

		if(data.getIngredient2() != null && data.getIngredient2() instanceof MagicSpellItem ing) {
			ingredient2 = new ItemStack(ing);
			ing.setExp(ingredient2, ing.getMaxExp());
		} else {
			ingredient2 = new ItemStack(data.getIngredient2());
		}


		builder.addSlot(RecipeIngredientRole.INPUT, 13, 4).addItemStack(ingredient1);
		builder.addSlot(RecipeIngredientRole.INPUT, 86, 4).addItemStack(ingredient2);

		ItemStack output = new ItemStack(data.getResult(), data.getAmount());
		builder.addSlot(RecipeIngredientRole.OUTPUT, 90, 33).addItemStack(output);
	}
}