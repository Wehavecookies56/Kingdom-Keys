package uk.co.wehavecookies56.kk.client.gui.redesign;

import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.audio.SoundHandler;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import uk.co.wehavecookies56.kk.client.sound.ModSounds;
import uk.co.wehavecookies56.kk.api.menu.ItemCategory;
import uk.co.wehavecookies56.kk.common.lib.Reference;
import uk.co.wehavecookies56.kk.common.util.Utils;

public class GuiButtonFilter extends GuiButton {

    ItemCategory category;
    int icon_dimensions = 20;
    GuiFilterBar parent;

    public GuiButtonFilter(GuiFilterBar parent, int buttonId, int x, int y, ItemCategory cat) {
        super(buttonId, x, y, 26, 15, "");
        this.category = cat;
        this.parent = parent;
    }

    public GuiButtonFilter(GuiFilterBar parent, int buttonId, int x, int y, String buttonText) {
        super(buttonId, x, y, 26, 15, buttonText);
        this.category = null;
        this.parent = parent;
    }

    @Override
    public void drawButton(Minecraft mc, int mouseX, int mouseY, float partialTicks) {
        float scale = 0.5F;
        hovered = mouseX > x && mouseY >= y && mouseX < x + width && mouseY < y + height;
        if (visible) {
            float centreX = x + (((width) - (icon_dimensions / 2)) * scale);
            float centreY = y + (((height) - (icon_dimensions / 2)) * scale);
            mc.renderEngine.bindTexture(new ResourceLocation(Reference.MODID, "textures/gui/menu/menu_button.png"));

            Utils.drawScaledModalRect(this, x, y, 66, 0, 52, 30, scale, scale);
            if (displayString.isEmpty()) {
                Utils.drawScaledModalRect(this, centreX, centreY, category.getU(), category.getV(), icon_dimensions, icon_dimensions, scale, scale);
            } else {
                float textCentreX = x + (((width * scale)) - ((mc.fontRenderer.getStringWidth(displayString) * 0.75F) / 2));
                float textCentreY = y + (((height * scale)) - ((mc.fontRenderer.FONT_HEIGHT * 0.75F) / 2));
                GlStateManager.pushMatrix();
                GlStateManager.translate(textCentreX, textCentreY, 0);
                GlStateManager.scale(0.75F, 0.75F, 1);
                drawString(mc.fontRenderer, displayString, 0, 0, 0xFFFFFF);
                GlStateManager.popMatrix();
                mc.renderEngine.bindTexture(new ResourceLocation(Reference.MODID, "textures/gui/menu/menu_button.png"));
            }
            if (hovered) {
                //Utils.drawScaledModalRect(this, x-3, y-3, 66, 30, 58, 36, 0.5F, 0.5F);
            }
        }
    }

    @Override
    public boolean isMouseOver() {
        return super.isMouseOver();
    }

    @Override
    public boolean mousePressed(Minecraft mc, int mouseX, int mouseY) {
        return super.mousePressed(mc, mouseX, mouseY);
    }

    @Override
    public void playPressSound(SoundHandler soundHandlerIn) {
        soundHandlerIn.playSound(PositionedSoundRecord.getMasterRecord(ModSounds.select, 1.0F));
    }
}
