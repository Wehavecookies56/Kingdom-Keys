package uk.co.wehavecookies56.kk.client.gui.redesign;

import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.audio.SoundHandler;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.ResourceLocation;
import uk.co.wehavecookies56.kk.client.sound.ModSounds;
import uk.co.wehavecookies56.kk.common.lib.Reference;

import javax.annotation.ParametersAreNonnullByDefault;
import java.awt.*;

public class GuiMenuButtonMove extends GuiButton {

    private ResourceLocation texture = new ResourceLocation(Reference.MODID, "textures/gui/menu/menu_button.png");
    private int endWidth = 11;

    private int uPos = 133;
    private int upV = 90, downV = 100;
    private int selectedVUp = 110;
    private int selectedVDown = 120;

    private boolean selected;
    private int v, selectedV;

    public GuiMenuButtonMove(int buttonId, int x, int y, String direction) {
        super(buttonId, x, y, 9, 9, "");
        if(direction.equals("up")) {
        	selectedV = selectedVUp;
        	v = upV;
        } else {
        	selectedV = selectedVDown;
        	v = downV;
        }
    }

    @ParametersAreNonnullByDefault
    @Override
    public void drawButton(Minecraft mc, int mouseX, int mouseY, float partialTicks) {
        hovered = mouseX > x && mouseY >= y && mouseX < x + width && mouseY < y + height;
        if (visible) {
            GlStateManager.pushMatrix();
            GlStateManager.color(1,1,1);
            GlStateManager.enableAlpha();
            GlStateManager.enableBlend();
            mc.renderEngine.bindTexture(texture);
            if (hovered && enabled) {            	
                drawTexturedModalRect(x, y, uPos, selectedV, endWidth, height);
            } else {
                drawTexturedModalRect(x, y, uPos, v, endWidth, height);
            }
            drawString(mc.fontRenderer, displayString, x+12, y+6, new Color(255, 255, 255).hashCode());
            GlStateManager.popMatrix();
        }
    }
    
    public boolean isHovered() {
    	return hovered;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    @Override
    public boolean mousePressed(Minecraft mc, int mouseX, int mouseY) {
        return super.mousePressed(mc, mouseX, mouseY);
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY) {
        super.mouseReleased(mouseX, mouseY);
    }

    @Override
    public boolean isMouseOver() {
        return super.isMouseOver();
    }

    @Override
    public void playPressSound(SoundHandler soundHandlerIn) {
        soundHandlerIn.playSound(PositionedSoundRecord.getMasterRecord(ModSounds.select, 1.0F));
    }

    @Override
    protected int getHoverState(boolean mouseOver) {
        return super.getHoverState(mouseOver);
    }
}
