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
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.item.KeychainItem;
import online.kingdomkeys.kingdomkeys.item.ModItems;

public class KeybladeSummonRecipeCategory implements IRecipeCategory<KeychainItem> {
    IDrawable background;
    IDrawable icon;

    public static final RecipeType<KeychainItem> TYPE = RecipeType.create(KingdomKeys.MODID, "keyblade_summon", KeychainItem.class);

    public KeybladeSummonRecipeCategory(IGuiHelper guiHelper) {
        background = guiHelper.drawableBuilder(new ResourceLocation(KingdomKeys.MODID, "textures/gui/keyblade_summon_background.png"), 0, 0, 120, 58).build();
        icon = guiHelper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(ModItems.kingdomKeyChain.get()));
    }

    @Override
    public Component getTitle() {
        return Component.translatable("jei.category.kingdomkeys.keyblade_summon");
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
    public RecipeType<KeychainItem> getRecipeType() {
        return TYPE;
    }

    @Override
    public void draw(KeychainItem recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics guiGraphics, double mouseX, double mouseY) {
        new TextDrawable(Component.translatable("jei.category.kingdomkeys.keyblade_summon.info")).draw(guiGraphics, 0, 32);
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, KeychainItem recipe, IFocusGroup focuses) {
        builder.addSlot(RecipeIngredientRole.INPUT, 9, 9).addItemStack(new ItemStack(recipe));
        builder.addSlot(RecipeIngredientRole.OUTPUT, 95, 9).addItemStack(new ItemStack(recipe.getKeyblade()));
    }
}
