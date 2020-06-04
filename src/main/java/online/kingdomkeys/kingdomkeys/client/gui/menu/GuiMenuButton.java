package online.kingdomkeys.kingdomkeys.client.gui.menu;

import java.awt.Color;

import javax.annotation.ParametersAreNonnullByDefault;

import com.mojang.blaze3d.systems.RenderSystem;

import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.SimpleSound;
import net.minecraft.client.audio.SoundHandler;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.gui.widget.button.Button.IPressable;
import net.minecraft.util.ResourceLocation;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.client.sound.ModSounds;

public class GuiMenuButton extends Button{

	private ResourceLocation texture = new ResourceLocation(KingdomKeys.MODID, "textures/gui/menu/menu_button.png");
	private int endWidth = 11;

	private int leftU = 0, middleU = 12, rightU = 14;
	private int vPos = 0;
	private int selectedVPos = 20;

	private int middleWidth;

	private boolean selected;

	public GuiMenuButton(int x, int y, int widthIn, String buttonText, Button.IPressable onPress) {
		super(x, y, 22 + widthIn, 20, buttonText, onPress);
		middleWidth = widthIn;
	}

	@ParametersAreNonnullByDefault
	@Override
	public void render(int mouseX, int mouseY, float partialTicks) {
		isHovered = mouseX > x+1 && mouseY >= y+1 && mouseX < x + width-1 && mouseY < y + height-1;
		if (visible) {
			RenderSystem.pushMatrix();
			RenderSystem.color3f(1, 1, 1);
			// RenderSystem.enableAlpha();
			RenderSystem.enableBlend();
			Minecraft.getInstance().textureManager.bindTexture(texture);
			if (isHovered && active) { //Hovered button
				x += 10;
				drawButton(true);

				drawString(Minecraft.getInstance().fontRenderer, getMessage(), x + 12, y + 6, new Color(255, 255, 255).hashCode());
				x -= 10;
			} else {
				if(active) {//Not hovered but fully visible
					drawButton(false);

					drawString(Minecraft.getInstance().fontRenderer, getMessage(), x + 12, y + 6, new Color(255, 255, 255).hashCode());
				} else {//Not hovered and selected (not fully visible)
					drawButton(false);

					drawString(Minecraft.getInstance().fontRenderer, getMessage(), x + 12, y + 6, new Color(100,100,100).hashCode());
				}
			}
			RenderSystem.popMatrix();
		}
		
	}
	
	private void drawButton(boolean hovered) {
		int vPos = hovered ? selectedVPos : this.vPos;
		
		blit(x, y, leftU, vPos, endWidth, height);
		for (int i = 0; i < middleWidth; i++)
			blit(x + i + endWidth, y, middleU, vPos, 1, height);
		blit(x + endWidth + middleWidth, y, rightU, vPos, endWidth, height);
		
	}

	@Override
	public boolean mouseClicked(double p_mouseClicked_1_, double p_mouseClicked_3_, int p_mouseClicked_5_) {
		if(isHovered)
			return super.mouseClicked(p_mouseClicked_1_, p_mouseClicked_3_, p_mouseClicked_5_);
		else 
			return false;
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
		soundHandlerIn.play(SimpleSound.master(ModSounds.menu_select.get(), 1.0F, 1.0F));
	}

}
