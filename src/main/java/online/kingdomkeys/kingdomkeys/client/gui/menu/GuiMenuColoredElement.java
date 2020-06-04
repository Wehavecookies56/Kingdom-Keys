package online.kingdomkeys.kingdomkeys.client.gui.menu;

import java.awt.Color;

import javax.annotation.ParametersAreNonnullByDefault;

import com.mojang.blaze3d.systems.RenderSystem;

import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.SoundHandler;
import net.minecraft.client.gui.widget.Widget;
import net.minecraft.util.ResourceLocation;
import online.kingdomkeys.kingdomkeys.KingdomKeys;

public class GuiMenuColoredElement extends Widget {

	private ResourceLocation texture = new ResourceLocation(KingdomKeys.MODID, "textures/gui/menu/menu_button.png");

	private int u = 215;
	private int vPos = 0;

	private int middleWidth;

	private boolean selected;

	String key, value;
	Color color;

	public GuiMenuColoredElement(int x, int y, int widthIn, String key, String value, int color) {
		super(x, y, widthIn, 14, key);
		this.key = key;
		this.value = value;
		middleWidth = widthIn;
		this.color = new Color(color);
	}

	@ParametersAreNonnullByDefault
	@Override
	public void render(int mouseX, int mouseY, float partialTicks) {
		// isHovered = mouseX > x && mouseY >= y && mouseX < x + width && mouseY < y +
		// height;
		if (visible) {
			RenderSystem.pushMatrix();
			RenderSystem.color3f(color.getRed()/255F, color.getGreen()/255F, color.getBlue()/255F);
			// RenderSystem.enableAlpha();
			RenderSystem.enableBlend();
			Minecraft.getInstance().textureManager.bindTexture(texture);

			for (int i = 0; i < middleWidth; i++) {
				blit(x + i, y, u, vPos, 1, height);
			}
			drawString(Minecraft.getInstance().fontRenderer, key, x + 4, y + 4, new Color(255, 255, 255).hashCode());
			drawString(Minecraft.getInstance().fontRenderer, value, x + width - Minecraft.getInstance().fontRenderer.getStringWidth(value) - 4, y + 4, new Color(255, 255, 0).hashCode());
			RenderSystem.disableBlend();
			RenderSystem.popMatrix();
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
	public void playDownSound(SoundHandler soundHandlerIn) {
		// soundHandlerIn.play(SimpleSound.master(ModSounds.menu_select.get(), 1.0F,
		// 1.0F));
	}

}
