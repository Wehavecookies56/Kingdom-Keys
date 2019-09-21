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

public class GuiMenuButton extends GuiButton {

	private ResourceLocation texture = new ResourceLocation(Reference.MODID, "textures/gui/menu/menu_button.png");
	private int endWidth = 11;

	private int leftU = 0, middleU = 12, rightU = 14;
	private int vPos = 0;
	private int selectedVPos = 20;

	private int middleWidth;

	private boolean selected;

	public GuiMenuButton(int buttonId, int x, int y, int widthIn, String buttonText) {
		super(buttonId, x, y, 22 + widthIn, 20, buttonText);
		middleWidth = widthIn;
	}

	@ParametersAreNonnullByDefault
	@Override
	public void drawButton(Minecraft mc, int mouseX, int mouseY, float partialTicks) {
		hovered = mouseX > x && mouseY >= y && mouseX < x + width && mouseY < y + height;
		if (visible) {
			GlStateManager.pushMatrix();
			GlStateManager.color(1, 1, 1);
			GlStateManager.enableAlpha();
			GlStateManager.enableBlend();
			mc.renderEngine.bindTexture(texture);
			if (hovered && enabled) {
				x += 10;
				drawTexturedModalRect(x, y, leftU, selectedVPos, endWidth, height);
				for (int i = 0; i < middleWidth; i++)
					drawTexturedModalRect(x + i + endWidth, y, middleU, selectedVPos, 1, height);
				drawTexturedModalRect(x + endWidth + middleWidth, y, rightU, selectedVPos, endWidth, height);
				drawString(mc.fontRenderer, displayString, x + 12, y + 6, new Color(255, 255, 255).hashCode());
				x -= 10;
			} else {
				drawTexturedModalRect(x, y, leftU, vPos, endWidth, height);
				for (int i = 0; i < middleWidth; i++)
					drawTexturedModalRect(x + i + endWidth, y, middleU, vPos, 1, height);
				drawTexturedModalRect(x + endWidth + middleWidth, y, rightU, vPos, endWidth, height);
				drawString(mc.fontRenderer, displayString, x + 12, y + 6, new Color(255, 255, 255).hashCode());
			}
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
