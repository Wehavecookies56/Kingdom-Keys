package online.kingdomkeys.kingdomkeys.client.gui.elements.buttons;

import java.awt.Color;

import org.jetbrains.annotations.NotNull;

import com.mojang.blaze3d.systems.RenderSystem;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.client.ClientUtils;
import online.kingdomkeys.kingdomkeys.client.sound.ModSounds;

public class MenuScrollBar extends Button {

	double clickX, clickY;
	public int startX, startY, bottom, scrollY, scrollTop, localScrollY, localScrollMax, visibleHeight;
	float scrollPercent;
	private int contentHeight, scrollBarHeight;

	public float scrollOffset;

	ResourceLocation texture = new ResourceLocation(KingdomKeys.MODID, "textures/gui/menu/menu_button.png");

	public MenuScrollBar(int x, int y, int bottom, int visibleHeight, int contentHeight) {
		super(new Builder(Component.empty(),button -> {}).bounds(x, y, 14, bottom));
		this.visibleHeight = visibleHeight;
		this.bottom = bottom;
		this.scrollTop = getY() + 3 + barTopBotDims.Y;
		int scrollBottom = getHeight() - 3 - barTopBotDims.Y - 1;
		scrollY = scrollTop;
		localScrollMax = scrollBottom - scrollTop + 1;
		setContentHeight(contentHeight);
	}

	public void setScrollHeight(int height) {
		this.scrollBarHeight = height;
	}

	public int getScrollBottom() {
		return bottom - 3 - barTopBotDims.Y - scrollBarHeight;
	}

	public void setContentHeight(int contentHeight) {
		this.contentHeight = contentHeight;
		float visiblePercentage = ((float) visibleHeight / contentHeight) * 100;
		KingdomKeys.LOGGER.debug("{}/{} = {}%", visibleHeight, contentHeight, visiblePercentage);
		setScrollHeight((int) (localScrollMax * (visiblePercentage / 100)));
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
	public void render(@NotNull GuiGraphics gui, int mouseX, int mouseY, float partialTicks) {
		if (visible && contentHeight > visibleHeight) {
			//Bar background
			RenderSystem.enableBlend();
			RenderSystem.setShaderColor(1, 1, 1, 0.5F);
			gui.fill(getX(), getY() + 7, getX() + width, bottom - 7, new Color(0, 0, 0, 0.5F).hashCode());
			RenderSystem.disableBlend();
			RenderSystem.setShaderColor(1, 1, 1, 1);

			//Top of bar
			ClientUtils.blitScaled(texture, gui, getX(), getY(), barTopUV.X, barTopUV.Y, barTopBotDims.X, barTopBotDims.Y, 1);

			//Button top
			ClientUtils.blitScaled(texture, gui, getX(), scrollY - buttonDims.Y, buttonTopUV.X, buttonTopUV.Y, buttonDims.X, buttonDims.Y, 1);

			//Button middle
			for (int i = 0; i < scrollBarHeight; i++) {
				ClientUtils.blitScaled(texture, gui, getX(), scrollY, buttonMiddleUV.X, buttonMiddleUV.Y, buttonDims.X, 1, 1, scrollBarHeight);
			}

			//Button bottom
			ClientUtils.blitScaled(texture, gui, getX(), scrollY + scrollBarHeight, buttonBottomUV.X, buttonBottomUV.Y, buttonDims.X, buttonDims.Y, 1);

			//Bottom of bar
			ClientUtils.blitScaled(texture, gui, getX(), bottom - barTopBotDims.Y, barBottomUV.X, barBottomUV.Y, barTopBotDims.X, barTopBotDims.Y, 1);
		}
	}

	public void setScrollBarHeight(int height) {
		this.scrollBarHeight = Math.max(this.scrollBarHeight, height);

	}

	public void updateScroll() {
		localScrollY = scrollY - scrollTop;
		scrollPercent = ((float) localScrollY / (localScrollMax - scrollBarHeight+1)) * 100;
		int totalScroll = contentHeight - visibleHeight;
		scrollOffset = totalScroll * (scrollPercent/100);
		KingdomKeys.LOGGER.debug("{}/{} = {}%, offset {}", localScrollY, (localScrollMax - scrollBarHeight+1), scrollPercent, scrollOffset);
	}

	@Override
	public boolean mouseDragged(double mouseX, double mouseY, int button, double dragX, double dragY) {
		if (visible && contentHeight > visibleHeight) {
			if (clickX >= getX() && clickX <= getX() + width) {
				updateScroll();
				if (active) {
					if (startY - (clickY - mouseY) >= getScrollBottom() + 1) {
						scrollY = getScrollBottom() + 1;
					} else if (startY - (clickY - mouseY) <= scrollTop) {
						scrollY = scrollTop;
					} else {
						scrollY = (int) (startY - (clickY - mouseY));
					}
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
		startX = getX();
		startY = scrollY;
		if (clickX >= getX() && clickX <= getX() + width && visible) {
			playDownSound(Minecraft.getInstance().getSoundManager());
		}
		return false;
	}

	@Override
	public boolean mouseScrolled(double mouseX, double mouseY, double scrollDelta) {
		if (visible && contentHeight > visibleHeight) {
			updateScroll();
			int scrollFactor = 5;
			int oldY = scrollY;
			if (scrollDelta > 0) {
				scrollY = (int) Math.max(scrollY - (scrollDelta * scrollFactor), scrollTop);
			}
			if (scrollDelta < 0) {
				scrollY = (int) Math.min(scrollY - (scrollDelta * scrollFactor), getScrollBottom()+1);
			}
			if(oldY != scrollY) {
				Minecraft.getInstance().player.playSound(ModSounds.menu_move.get(), 1, 1);
			}
		}
		return super.mouseScrolled(mouseX, mouseY, scrollDelta);
	}

}
