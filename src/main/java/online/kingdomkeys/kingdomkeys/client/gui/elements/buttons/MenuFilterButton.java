package online.kingdomkeys.kingdomkeys.client.gui.elements.buttons;

import com.mojang.blaze3d.matrix.MatrixStack;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TranslationTextComponent;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.api.item.ItemCategory;
import online.kingdomkeys.kingdomkeys.client.gui.elements.MenuFilterBar;
import online.kingdomkeys.kingdomkeys.util.Utils;

public class MenuFilterButton extends Button {

    public ItemCategory category;
    int iconSize = 20;
    MenuFilterBar parent;


    public MenuFilterButton(MenuFilterBar parent, int x, int y, ItemCategory category) {
        super(x, y, 26, 15, new TranslationTextComponent(""), b -> parent.onClickFilter(category));
        this.parent = parent;
        this.category = category;
    }

    public MenuFilterButton(MenuFilterBar parent, int x, int y, String text) {
        this(parent, x, y, (ItemCategory) null);
        this.setMessage(new TranslationTextComponent(text));
    }

    @Override
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        Minecraft mc = Minecraft.getInstance();
        FontRenderer fr = mc.fontRenderer;
        float scale = 0.5F;
        isHovered = mouseX > x && mouseY >= y && mouseX < x + width && mouseY < y + height;
        if (visible) {
            float centreX = x + ((width - (iconSize / 2F)) * scale);
            float centreY = y + ((height -(iconSize / 2F)) * scale);
            mc.getTextureManager().bindTexture(new ResourceLocation(KingdomKeys.MODID, "textures/gui/menu/menu_button.png"));

            Utils.blitScaled(matrixStack, this, x, y, 66, 0, 52, 30, scale);
            if (getMessage().getString().isEmpty() && category != null) {
                Utils.blitScaled(matrixStack, this, centreX, centreY, category.getU(), category.getV(), iconSize, iconSize, scale);
            } else {
                float textCentreX = x + ((width * scale) - ((fr.getStringPropertyWidth(getMessage()) * 0.75F) / 2));
                float textCentreY = y + ((height * scale) - ((fr.FONT_HEIGHT * 0.75F) / 2));
                Utils.drawStringScaled(matrixStack, this, textCentreX, textCentreY, getMessage().getString(), 0xFFFFFF, 0.75F);
                mc.getTextureManager().bindTexture(new ResourceLocation(KingdomKeys.MODID, "textures/gui/menu/menu_button.png"));
            }
        }
    }
}
