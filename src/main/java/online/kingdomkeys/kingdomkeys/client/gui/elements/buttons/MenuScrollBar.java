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
	public int startX, startY, handleY, handleYMax, localHandleY, localHandleYMax, visibleHeight;
	float scrollPercent;
	private int contentHeight, handleHeight;

	public float scrollOffset;

	//The top and bottom part of the handle that stick out touch the top and bottom of the bar 3 pixels from the middle part so this is so it stops before overlapping with the top/bottom texture
	final int handleEndOffset = 3;

	ResourceLocation texture = new ResourceLocation(KingdomKeys.MODID, "textures/gui/menu/menu_button.png");

	public MenuScrollBar(int x, int y, int height, int visibleHeight, int contentHeight) {
		super(new Builder(Component.empty(),button -> {}).bounds(x, y, 14, height));
		this.visibleHeight = visibleHeight;
		//The highest point on the scroll bar for the handle
		this.handleYMax = getY() + handleEndOffset + barTopBotDims.Y;
		int handleBottom = height - handleEndOffset - barTopBotDims.Y;
		handleY = handleYMax;
		localHandleYMax = handleBottom - handleYMax + 1;
		setContentHeight(contentHeight);
	}

	public void setHandleHeight(int height) {
		this.handleHeight = height;
	}

	public int getHandleBottom() {
		return height - handleEndOffset - barTopBotDims.Y - handleHeight;
	}

	/**
	 * Set the height of the content that is being scrolled
	 * Resizes the handle
	 */
	public void setContentHeight(int contentHeight) {
		this.contentHeight = contentHeight;
		//Calculate what percentage of the scrollable content is visible
		float visiblePercentage = ((float) visibleHeight / contentHeight) * 100;
		//KingdomKeys.LOGGER.debug("{}/{} = {}%", visibleHeight, contentHeight, visiblePercentage);
		//Handle height to same percentage as the visible area of the scroll bar height
		setHandleHeight((int) (localHandleYMax * (visiblePercentage / 100)));
	}

	record Vec2(int X, int Y){}

	final Vec2 barTopUV = new Vec2(26, 29);
	final Vec2 barBottomUV = new Vec2(26, 47);
	final Vec2 handleTopUV = new Vec2(41, 29);
	final Vec2 handleMiddleUV = new Vec2(41, 39);
	final Vec2 handleBottomUV = new Vec2(41, 41);
	final Vec2 barTopBotDims = new Vec2(14, 17);
	final Vec2 handleDims = new Vec2(14, 9);

	@Override
	public void render(@NotNull GuiGraphics gui, int mouseX, int mouseY, float partialTicks) {
		if (visible && contentHeight > visibleHeight) {
			//Bar background
			RenderSystem.enableBlend();
			RenderSystem.setShaderColor(1, 1, 1, 0.5F);
			//box that is transparent offset by 7 so it doesn't stick out the triangle parts of the top and bottom textures
			final int backgroundOffset = 7;
			gui.fill(getX(), getY() + backgroundOffset, getX() + width, height - backgroundOffset, new Color(0, 0, 0, 0.5F).hashCode());
			RenderSystem.disableBlend();
			RenderSystem.setShaderColor(1, 1, 1, 1);

			//Top of bar
			ClientUtils.blitScaled(texture, gui, getX(), getY(), barTopUV.X, barTopUV.Y, barTopBotDims.X, barTopBotDims.Y, 1);

			//Handle top
			ClientUtils.blitScaled(texture, gui, getX(), handleY - handleDims.Y, handleTopUV.X, handleTopUV.Y, handleDims.X, handleDims.Y, 1);

			//Handle middle
			for (int i = 0; i < handleHeight; i++) {
				ClientUtils.blitScaled(texture, gui, getX(), handleY, handleMiddleUV.X, handleMiddleUV.Y, handleDims.X, 1, 1, handleHeight);
			}

			//Handle bottom
			ClientUtils.blitScaled(texture, gui, getX(), handleY + handleHeight, handleBottomUV.X, handleBottomUV.Y, handleDims.X, handleDims.Y, 1);

			//Bottom of bar
			ClientUtils.blitScaled(texture, gui, getX(), height - barTopBotDims.Y, barBottomUV.X, barBottomUV.Y, barTopBotDims.X, barTopBotDims.Y, 1);
		}
	}

	public void updateScroll() {
		//get the local handle position so 0 is at the top of the scroll bar
		localHandleY = handleY - handleYMax;
		//percentage of how far down the bar the handle is
		scrollPercent = ((float) localHandleY / (localHandleYMax - handleHeight)) * 100;
		int totalScroll = contentHeight - visibleHeight;
		scrollOffset = totalScroll * (scrollPercent/100);
		//KingdomKeys.LOGGER.debug("{}/{} = {}%, offset {}", localHandleY, (localHandleYMax - handleHeight), scrollPercent, scrollOffset);
	}

	@Override
	public boolean mouseDragged(double mouseX, double mouseY, int button, double dragX, double dragY) {
		if (visible && contentHeight > visibleHeight) {
			if (clickX >= getX() && clickX <= getX() + width) {
				updateScroll();
				if (active) {
					if (startY - (clickY - mouseY) >= getHandleBottom() + 1) {
						handleY = getHandleBottom() + 1;
					} else if (startY - (clickY - mouseY) <= handleYMax) {
						handleY = handleYMax;
					} else {
						handleY = (int) (startY - (clickY - mouseY));
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
		startY = handleY;
		if (clickY >= getY() && clickY <= getY() + height && clickX >= getX() && clickX <= getX() + width && visible) {
			playDownSound(Minecraft.getInstance().getSoundManager());
		}
		return false;
	}

	@Override
	public boolean mouseScrolled(double mouseX, double mouseY, double scrollDelta) {
		if (visible && contentHeight > visibleHeight) {
			int scrollFactor = 5;
			int oldY = handleY;
			if (scrollDelta > 0) {
				handleY = (int) Math.max(handleY - (scrollDelta * scrollFactor), handleYMax);
			}
			if (scrollDelta < 0) {
				handleY = (int) Math.min(handleY - (scrollDelta * scrollFactor), getHandleBottom()+1);
			}
			if(oldY != handleY) {
				Minecraft.getInstance().player.playSound(ModSounds.menu_move.get(), 1, 1);
			}
			updateScroll();
		}
		return super.mouseScrolled(mouseX, mouseY, scrollDelta);
	}

}
