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
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.data.PlayerData;
import online.kingdomkeys.kingdomkeys.item.MagicSpellItem;
import online.kingdomkeys.kingdomkeys.item.ModItems;
import online.kingdomkeys.kingdomkeys.lib.Strings;
import online.kingdomkeys.kingdomkeys.magic.Magic;
import online.kingdomkeys.kingdomkeys.magic.ModMagic;
import online.kingdomkeys.kingdomkeys.synthesis.melding.Melding;
import online.kingdomkeys.kingdomkeys.util.Utils;

public class CommandMeldingCategory implements IRecipeCategory<Melding> {

	public static final RecipeType<Melding> TYPE = RecipeType.create(KingdomKeys.MODID, "melding", Melding.class);
	private final IDrawable icon;
	private IDrawable background;

	public CommandMeldingCategory(IGuiHelper guiHelper) {
		icon = guiHelper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(ModItems.fireSpell.get()));
		background = guiHelper.drawableBuilder(KingdomKeys.rl("textures/gui/melding_recipe_bonus_background.png"), 0, 0, 114, 64).build();
	}

	private static String getDisplayName(Item item) {
		if (item instanceof MagicSpellItem spell) {
			Magic magic = ModMagic.registry.get(spell.getMagic());
			return Utils.translateToLocal(magic.getTranslationKey());
		}

		return new ItemStack(item).getHoverName().getString();
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
	public int getWidth() {
		return background.getWidth();
	}

	@Override
	public int getHeight() {
		return background.getHeight();
	}

	@Override
	public IDrawable getIcon() {
		return icon;
	}

	@Override
	public void draw(Melding data, IRecipeSlotsView recipeSlotsView, GuiGraphics guiGraphics, double mouseX, double mouseY) {
		background.draw(guiGraphics);

		Minecraft mc = Minecraft.getInstance();

		String resultName = getDisplayName(data.getResult());

		String bonusText = "";
		int bonusChance = 0;

		if (data.hasBonus()) {
			bonusChance = data.getBonusChance();
			guiGraphics.drawString(mc.font, ChatFormatting.BLUE + String.valueOf(bonusChance) + "%", 38, 25, 0x555555, false);
			bonusText = "[" + ChatFormatting.BLUE + getDisplayName(data.getBonusResult()) + ChatFormatting.DARK_GRAY + "] ";
		} else {
			guiGraphics.fill(4, 22, 56, 54, 0xFFC6C6C6);
		}

		String title = bonusText + resultName;
		guiGraphics.drawString(mc.font, 100 - bonusChance + "%", 60, 25, 0x555555, false);
		guiGraphics.drawString(mc.font, title, background.getWidth() / 2 - mc.font.width(title) / 2, -12, 0x555555, false);

		PlayerData playerData = PlayerData.get(mc.player);
		guiGraphics.drawString(mc.font, Utils.translateToLocal(Strings.Gui_Shop_Buy_Cost) + " " + data.getCost(), 0, 57, 0xFFFF55, true);
		String tierText = Utils.translateToLocal(Strings.Gui_Shop_Tier) + " " + Utils.getTierFromInt(data.getTier());
		guiGraphics.drawString(mc.font, tierText, getWidth() - mc.font.width(tierText), 57, playerData.getSynthLevel() >= data.getTier() ? 0x00FF00 : 0xFF0000, true);
	}

	@Override
	public void setRecipe(IRecipeLayoutBuilder builder, Melding data, IFocusGroup focuses) {
		ItemStack ingredient1;
		ItemStack ingredient2;

		if (data.getIngredient1() != null && data.getIngredient1() instanceof MagicSpellItem ing1) {
			ingredient1 = new ItemStack(ing1);
			ing1.setExp(ingredient1, ing1.getMaxExp());
		} else {
			ingredient1 = new ItemStack(data.getIngredient1());
		}

		if (data.getIngredient2() != null && data.getIngredient2() instanceof MagicSpellItem ing) {
			ingredient2 = new ItemStack(ing);
			ing.setExp(ingredient2, ing.getMaxExp());
		} else {
			ingredient2 = new ItemStack(data.getIngredient2());
		}


		builder.addSlot(RecipeIngredientRole.INPUT, 13, 4).addItemStack(ingredient1);
		builder.addSlot(RecipeIngredientRole.INPUT, 86, 4).addItemStack(ingredient2);

		ItemStack output = new ItemStack(data.getResult(), data.getAmount());
		builder.addSlot(RecipeIngredientRole.OUTPUT, 90, 33).addItemStack(output);

		if (data.hasBonus()) {
			ItemStack bonus = new ItemStack(data.getBonusResult(), data.getBonusAmount());
			builder.addSlot(RecipeIngredientRole.OUTPUT, 9, 33).addItemStack(bonus);
		}

	}
}