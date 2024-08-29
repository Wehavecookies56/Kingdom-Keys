package online.kingdomkeys.kingdomkeys.client.gui.elements;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.sounds.SoundManager;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import online.kingdomkeys.kingdomkeys.KingdomKeys;

import javax.annotation.ParametersAreNonnullByDefault;
import java.awt.*;

public class MenuColourBox extends AbstractWidget {

	private ResourceLocation texture = ResourceLocation.fromNamespaceAndPath(KingdomKeys.MODID, "textures/gui/menu/menu_button.png");

	private int u = 215;
	private int vPos = 0;

	private int middleWidth;

	private boolean selected;

	String key, value;
	Color color;
	Minecraft minecraft;

	public MenuColourBox(int x, int y, int widthIn, String key, String value, int color) {
		super(x, y, widthIn, 14, Component.translatable(key));
		this.key = key;
		this.value = value;
		middleWidth = widthIn;
		this.color = new Color(color);
		minecraft = Minecraft.getInstance();
	}

	@ParametersAreNonnullByDefault
	@Override
	public void renderWidget(GuiGraphics gui, int mouseX, int mouseY, float partialTicks) {
		// isHovered = mouseX > x && mouseY >= y && mouseX < x + width && mouseY < y +
		// height;
		if (visible) {
			gui.pose().pushPose();
			RenderSystem.setShaderColor(color.getRed()/255F, color.getGreen()/255F, color.getBlue()/255F, 1.0F);
			// RenderSystem.enableAlpha();
			RenderSystem.enableBlend();

			gui.blit(texture, getX(), getY(), middleWidth, height, u, vPos, 1, height, 256, 256);
			RenderSystem.setShaderColor(1,1,1,1);
			gui.drawString(minecraft.font, key, getX() + 4, getY() + 4, new Color(255, 255, 255).hashCode());
			gui.drawString(minecraft.font, value, getX() + width - minecraft.font.width(value) - 4, getY() + 4, new Color(255, 255, 0).hashCode());
			RenderSystem.disableBlend();
			gui.pose().popPose();
		}
	}

	public void setKey(String key) {
		this.key = key;
	}
	
	public void setValue(String val) {
		this.value = val;
	}
	public boolean isHovered() {
		return isHovered;
	}

	public boolean isSelected() {
		return selected;
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
	}

	@Override
	public void playDownSound(SoundManager soundHandlerIn) {
		// soundHandlerIn.play(SimpleSound.master(ModSounds.menu_select.get(), 1.0F,
		// 1.0F));
	}

	@Override
	public void updateWidgetNarration(NarrationElementOutput pNarrationElementOutput) {

	}
}
