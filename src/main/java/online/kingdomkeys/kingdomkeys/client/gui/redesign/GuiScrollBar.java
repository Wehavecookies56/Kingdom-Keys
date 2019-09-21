package uk.co.wehavecookies56.kk.client.gui.redesign;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import uk.co.wehavecookies56.kk.common.lib.Reference;

public class GuiScrollBar extends GuiButton {

    int clickX, clickY, startX, startY, top, bottom;

    public GuiScrollBar(int buttonId, int x, int y, int top, int bottom) {
        super(buttonId, x, y, "");
        this.top = top;
        this.bottom = bottom;
        height = 10;
        width = 14;
    }

    @Override
    public void drawButton(Minecraft mc, int mouseX, int mouseY, float partialTicks) {
        if (visible) {
            mc.renderEngine.bindTexture(new ResourceLocation(Reference.MODID, "textures/gui/menu/menu_button.png"));
            GlStateManager.pushMatrix();
            GlStateManager.translate(x, y, 0);
            GlStateManager.scale(0.5F, 0.5F, 1);
            drawTexturedModalRect(0, -9, 41, 29, 14, 9);
            GlStateManager.popMatrix();
            GlStateManager.pushMatrix();
            GlStateManager.translate(x, y, 0);
            GlStateManager.scale(0.5F, 0.5F, 1);
            drawTexturedModalRect(0, height, 41, 41, 14, 9);
            GlStateManager.popMatrix();
            for (int i = 0; i < height; i++) {
                GlStateManager.pushMatrix();
                GlStateManager.translate(x, y, 0);
                GlStateManager.scale(0.5F, 0.5F, 0);
                drawTexturedModalRect(0, i, 41, 39, 14, 1);
                GlStateManager.popMatrix();
            }
        }
    }

    @Override
    public void mouseDragged(Minecraft mc, int mouseX, int mouseY) {
        if (clickX >= x && clickX <= x + width) {
            if (startY - (clickY - mouseY) >= top - 1 && startY - (clickY - mouseY) <= bottom - height && enabled) {
                this.y = startY - (clickY - mouseY);
            }
        }
    }

    @Override
    public boolean mousePressed(Minecraft mc, int mouseX, int mouseY) {
        clickX = mouseX;
        clickY = mouseY;
        startX = x;
        startY = y;
        return false;
    }

}
