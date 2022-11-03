package online.kingdomkeys.kingdomkeys.integration.jei;

import com.mojang.blaze3d.platform.InputConstants;
import com.mojang.blaze3d.vertex.PoseStack;
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
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.ForgeRegistries;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.block.ModBlocks;
import online.kingdomkeys.kingdomkeys.capability.ModCapabilities;
import online.kingdomkeys.kingdomkeys.synthesis.material.Material;
import online.kingdomkeys.kingdomkeys.synthesis.recipe.Recipe;

import java.util.List;
import java.util.Map;

public class SynthesisRecipeCategory implements IRecipeCategory<Recipe> {

    IDrawable icon;
    IDrawable background;
    IDrawable munny;

    public static final RecipeType<Recipe> TYPE = RecipeType.create(KingdomKeys.MODID, "synthesis", Recipe.class);

    public SynthesisRecipeCategory(IGuiHelper guiHelper) {
        icon = guiHelper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(ModBlocks.moogleProjector.get()));
        background = guiHelper.drawableBuilder(new ResourceLocation(KingdomKeys.MODID, "textures/gui/synthesis_recipe_background.png"), 0, 0, 170, 86).build();
        munny = guiHelper.drawableBuilder(new ResourceLocation(KingdomKeys.MODID, "textures/entity/munny.png"), 0, 0, 16, 16).setTextureSize(16, 16).build();
    }

    @Override
    public List<Component> getTooltipStrings(Recipe recipe, IRecipeSlotsView recipeSlotsView, double mouseX, double mouseY) {
        return IRecipeCategory.super.getTooltipStrings(recipe, recipeSlotsView, mouseX, mouseY);
    }

    @Override
    public boolean handleInput(Recipe recipe, double mouseX, double mouseY, InputConstants.Key input) {
        return IRecipeCategory.super.handleInput(recipe, mouseX, mouseY, input);
    }

    @Override
    public void draw(Recipe recipe, IRecipeSlotsView recipeSlotsView, PoseStack stack, double mouseX, double mouseY) {
        if (recipe.getCost() > 0) {
            munny.draw(stack, 5, 57);
            new TextDrawable(new TranslatableComponent(String.valueOf(recipe.getCost())), 0xFFFF55).draw(stack, 5, 57);
        }
        Minecraft.getInstance().player.getCapability(ModCapabilities.PLAYER_CAPABILITIES).ifPresent(cap -> {
            if (cap.hasKnownRecipe(recipe.getRegistryName())) {
                new TextDrawable(new TranslatableComponent("jei.category.kingdomkeys.synthesis.unlocked"), 0x55FF55).draw(stack, 5, 72);
            } else {
                new TextDrawable(new TranslatableComponent("jei.category.kingdomkeys.synthesis.locked"), 0xFF5555).draw(stack, 5, 72);
            }
        });
    }

    @Override
    public Component getTitle() {
        return new TranslatableComponent("jei.category.kingdomkeys.synthesis");
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
    public void setRecipe(IRecipeLayoutBuilder builder, Recipe recipe, IFocusGroup focuses) {
        builder.addSlot(RecipeIngredientRole.OUTPUT, 145, 62).addItemStack(new ItemStack(recipe.getResult())).setSlotName("result");
        int startX = 5;
        int startY = 4;
        int currentX = startX;
        int currentY = startY;
        for (Map.Entry<Material, Integer> ingredient : recipe.getMaterials().entrySet()) {
            TextDrawable quantityOverlay = new TextDrawable(new TranslatableComponent(ingredient.getValue().toString()));
            builder.addSlot(RecipeIngredientRole.INPUT, currentX, currentY)
                    .addItemStack(new ItemStack(ForgeRegistries.ITEMS.getValue(new ResourceLocation(ingredient.getKey().getMaterialName()))))
                    .setSlotName(ingredient.getKey().getMaterialName())
                    .setOverlay(quantityOverlay, 16 - quantityOverlay.getWidth(), 16 - quantityOverlay.getHeight());
            currentX += 16;
            if (currentX > 164) {
                currentY += 16;
                currentX = startX;
            }
        }
    }

    @Override
    public ResourceLocation getUid() {
        return new ResourceLocation(KingdomKeys.MODID, "synthesis");
    }

    @Override
    public Class<? extends Recipe> getRecipeClass() {
        return Recipe.class;
    }

    @Override
    public RecipeType<Recipe> getRecipeType() {
        return TYPE;
    }
}
