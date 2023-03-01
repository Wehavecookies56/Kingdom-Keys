package online.kingdomkeys.kingdomkeys.client.gui.elements;

import java.awt.Color;

import javax.annotation.ParametersAreNonnullByDefault;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.sounds.SoundManager;
import net.minecraft.resources.ResourceLocation;
import online.kingdomkeys.kingdomkeys.KingdomKeys;

public class MenuColourBox extends AbstractWidget {

	private ResourceLocation texture = new ResourceLocation(KingdomKeys.MODID, "textures/gui/menu/menu_button.png");

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
	public void render(PoseStack matrixStack, int mouseX, int mouseY, float partialTicks) {
		// isHovered = mouseX > x && mouseY >= y && mouseX < x + width && mouseY < y +
		// height;
		if (visible) {
			matrixStack.pushPose();
			RenderSystem.setShaderColor(color.getRed()/255F, color.getGreen()/255F, color.getBlue()/255F, 1.0F);
			// RenderSystem.enableAlpha();
			RenderSystem.enableBlend();
			RenderSystem.setShaderTexture(0, texture);

			for (int i = 0; i < middleWidth; i++) {
				blit(matrixStack, x + i, y, u, vPos, 1, height);
			}
			drawString(matrixStack, minecraft.font, key, x + 4, y + 4, new Color(255, 255, 255).hashCode());
			drawString(matrixStack, minecraft.font, value, x + width - minecraft.font.width(value) - 4, y + 4, new Color(255, 255, 0).hashCode());
			RenderSystem.disableBlend();
			matrixStack.popPose();
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
	public void updateNarration(NarrationElementOutput pNarrationElementOutput) {

	}
}
