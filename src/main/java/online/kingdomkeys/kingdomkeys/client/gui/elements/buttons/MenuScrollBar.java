package online.kingdomkeys.kingdomkeys.client.gui.elements.buttons;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.client.ClientUtils;
import online.kingdomkeys.kingdomkeys.client.sound.ModSounds;

import java.awt.*;

public class MenuScrollBar extends Button {

	double clickX, clickY;
	public int startX, startY, top, bottom;
	int scrollBarHeight;
	int minHeight, maxHeight;

	public MenuScrollBar(int x, int y, int widthIn, int minHeight, int top, int bottom) {
		super(x, y, widthIn, minHeight, Component.empty(), button -> {});
		this.top = top;
		this.minHeight = minHeight;
		this.scrollBarHeight = minHeight;
		this.bottom = bottom;
		this.maxHeight = bottom - top;
	}

	public int getBottom() {
		return bottom + scrollBarHeight;
	}

	record Vec2(int X, int Y){}

	final Vec2 barTopUV = new Vec2(26, 29);
	final Vec2 barBottomUV = new Vec2(26, 47);
	final Vec2 buttonTopUV = new Vec2(41, 29);
	final Vec2 buttonMiddleUV = new Vec2(41, 39);
	final Vec2 buttonBottomUV = new Vec2(41, 41);
	final Vec2 barTopBotDims = new Vec2(14, 17);
	final Vec2 buttonDims = new Vec2(14, 9);


	final int renderOffset = 20;

	@Override
	public void render(PoseStack stack, int mouseX, int mouseY, float partialTicks) {
		if (visible) {
			//Bar background
			RenderSystem.enableBlend();
			RenderSystem.setShaderColor(1, 1, 1, 0.5F);
			fill(stack, x, top-(barTopBotDims.Y/2), x + width, getBottom()+buttonDims.Y, new Color(0, 0, 0, 0.5F).hashCode());
			RenderSystem.disableBlend();
			RenderSystem.setShaderColor(1, 1, 1, 1);

			RenderSystem.setShaderTexture(0, new ResourceLocation(KingdomKeys.MODID, "textures/gui/menu/menu_button.png"));

			//Top of bar
			ClientUtils.blitScaled(stack, this, x, top-renderOffset, barTopUV.X, barTopUV.Y, barTopBotDims.X, barTopBotDims.Y, 1);

			//Button top
			ClientUtils.blitScaled(stack, this, x, y-buttonDims.Y, buttonTopUV.X, buttonTopUV.Y, buttonDims.X, buttonDims.Y, 1);

			//Button middle
			//for (int i = 0; i < scrollBarHeight; i++) {
			ClientUtils.blitScaled(stack, this, x, y, buttonMiddleUV.X, buttonMiddleUV.Y, buttonDims.X, 1, 1, scrollBarHeight);
			//}

			//Button bottom
			ClientUtils.blitScaled(stack, this, x, y+scrollBarHeight, buttonBottomUV.X, buttonBottomUV.Y, buttonDims.X, buttonDims.Y, 1);

			//Bottom of bar
			ClientUtils.blitScaled(stack, this, x, getBottom()+3, barBottomUV.X, barBottomUV.Y, barTopBotDims.X, barTopBotDims.Y, 1);
		}
	}

	public void setScrollBarHeight(int height) {
		this.scrollBarHeight = Math.max(this.scrollBarHeight, height);

	}

	@Override
	public boolean mouseDragged(double mouseX, double mouseY, int button, double dragX, double dragY) {
		if (clickX >= x && clickX <= x + width) {
			if (active) {
				if (startY - (clickY - mouseY) >= bottom) {
					this.y = bottom;
				} else if (startY - (clickY - mouseY) <= top) {
					this.y = top;
				} else {
					this.y = (int) (startY - (clickY - mouseY));
				}
			}

		}
		return super.mouseDragged(mouseX, mouseY, button, dragX, dragY);
	}

	@Override
	public boolean mouseReleased(double p_231048_1_, double p_231048_3_, int p_231048_5_) {
		return true;
	}

	@Override
	public boolean mouseClicked(double mouseX, double mouseY, int button) {
		clickX = mouseX;
		clickY = mouseY;
		startX = x;
		startY = y;
		if (clickX >= x && clickX <= x + width && visible) {
			playDownSound(Minecraft.getInstance().getSoundManager());
		}
		return false;
	}

	@Override
	public boolean mouseScrolled(double mouseX, double mouseY, double scrollDelta) {
		if (visible) {
			int scrollFactor = 5;
			int oldY = y;
			if (scrollDelta > 0) {
				y = (int) Math.max(y - (scrollDelta * scrollFactor), top);
			}
			if (scrollDelta < 0) {
				y = (int) Math.min(y - (scrollDelta * scrollFactor), bottom);
			}
			if(oldY != y) {
				Minecraft.getInstance().player.playSound(ModSounds.menu_move.get(), 1, 1);
			}
		}
		return super.mouseScrolled(mouseX, mouseY, scrollDelta);
	}

}
