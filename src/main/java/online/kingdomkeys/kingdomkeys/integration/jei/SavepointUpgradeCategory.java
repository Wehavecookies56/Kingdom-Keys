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
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.block.ModBlocks;
import online.kingdomkeys.kingdomkeys.item.ModComponents;
import online.kingdomkeys.kingdomkeys.savepoint.SavePointData;

public class SavepointUpgradeCategory implements IRecipeCategory<SavePointData> {

    private final IDrawable icon;
    private final IDrawable background;

    public static final RecipeType<SavePointData> TYPE = RecipeType.create(KingdomKeys.MODID, "savepoint_upgrades", SavePointData.class);

    public SavepointUpgradeCategory(IGuiHelper guiHelper) {
        icon = guiHelper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(ModBlocks.savepoint.get()));
        background = guiHelper.drawableBuilder(ResourceLocation.fromNamespaceAndPath(KingdomKeys.MODID, "textures/gui/synthesis_recipe_background.png"), 0, 0, 170, 86).build();
    }

    @Override
    public RecipeType<SavePointData> getRecipeType() {
        return TYPE;
    }

    @Override
    public Component getTitle() {
        return Component.translatable("jei.category.kingdomkeys.savepoints");
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
    public void draw(SavePointData data, IRecipeSlotsView recipeSlotsView, GuiGraphics guiGraphics, double mouseX, double mouseY) {
        Component text = Component.literal(data.getName().substring(data.getName().indexOf(":") + 1).toUpperCase());

        FormattedCharSequence formattedcharsequence = text.getVisualOrderText();
        guiGraphics.drawString(Minecraft.getInstance().font, formattedcharsequence, 87 - Minecraft.getInstance().font.width(formattedcharsequence) / 2, -40, 0x555555, false);


        int startX = 5;
        int startY = 11;

        int x = startX;
        int y = startY;

        int col = 0;

        for (SavePointData.SavePointStat stat : SavePointData.SavePointStat.values()) {
            int color = data.restores(stat) || (stat == SavePointData.SavePointStat.TIER && !data.getName().equals(KingdomKeys.MODID+":warp")) ? 0x55FF55 : 0xCC3333;

            guiGraphics.drawString(Minecraft.getInstance().font, Component.literal(stat.name()), x + 18, y + 4, color, false);
            col++;
            x += 50;

            if (col % 3 == 0) {
                x = startX;
                y += 20;
            }
        }
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, SavePointData data, IFocusGroup focuses) {
        int startX = 5;
        int startY = 10;

        int x = startX;
        int y = startY;

        int col = 0;

        for (SavePointData.SavePointStat stat : SavePointData.SavePointStat.values()) {
            ResourceLocation rl = data.getMaterials().get(stat);
            if (rl == null)
                continue;

            Item item = BuiltInRegistries.ITEM.get(rl);
            builder.addSlot(RecipeIngredientRole.INPUT, x, y).addItemStack(new ItemStack(item));


            x += 50;
            col++;

            if (col % 3 == 0) {
                x = startX;
                y += 20;
            }
        }

        ItemStack output = new ItemStack(ModBlocks.savepoint.get());
        output.set(ModComponents.SAVE_POINT_TIER, data.getName().toUpperCase());

        builder.addSlot(RecipeIngredientRole.OUTPUT, 145, 62)
                .addItemStack(output);
    }
}