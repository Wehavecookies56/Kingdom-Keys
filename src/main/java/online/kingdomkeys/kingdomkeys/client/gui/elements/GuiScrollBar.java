package online.kingdomkeys.kingdomkeys.client.gui.elements;

import com.mojang.blaze3d.systems.RenderSystem;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.util.ResourceLocation;
import online.kingdomkeys.kingdomkeys.KingdomKeys;

public class GuiScrollBar extends Button {

	int clickX, clickY, startX, startY, top, bottom;

	public GuiScrollBar(int x, int y, int widthIn, String buttonText, Button.IPressable onPress) {
		super(x, y, 22 + widthIn, 20, buttonText, onPress);
		height = 10;
		width = 14;
		
	}

	@Override
	public void render(int mouseX, int mouseY, float partialTicks) {
		if (visible) {
			Minecraft.getInstance().textureManager.bindTexture(new ResourceLocation(KingdomKeys.MODID, "textures/gui/menu/menu_button.png"));
			RenderSystem.pushMatrix();
			RenderSystem.translated(x, y, 0);
			RenderSystem.scaled(0.5F, 0.5F, 1);
			blit(0, -9, 41, 29, 14, 9);
			RenderSystem.popMatrix();
			RenderSystem.pushMatrix();
			RenderSystem.translated(x, y, 0);
			RenderSystem.scaled(0.5F, 0.5F, 1);
			blit(0, height, 41, 41, 14, 9);
			RenderSystem.popMatrix();
			for (int i = 0; i < height; i++) {
				RenderSystem.pushMatrix();
				RenderSystem.translated(x, y, 0);
				RenderSystem.scaled(0.5F, 0.5F, 0);
				blit(0, i, 41, 39, 14, 1);
				RenderSystem.popMatrix();
			}
		}
	}

	@Override
	public boolean mouseDragged(double p_mouseDragged_1_, double mouseX, int mouseY, double p_mouseDragged_6_, double p_mouseDragged_8_) {
		if (clickX >= x && clickX <= x + width) {
			if (startY - (clickY - mouseY) >= top - 1 && startY - (clickY - mouseY) <= bottom - height && active) {
				this.y = startY - (clickY - mouseY);
				return true;
			}
		}
		return false;
	}
	@Override
	public boolean mouseClicked(double mouseX, double mouseY, int p_mouseClicked_5_) {
		clickX = (int) mouseX;
		clickY = (int) mouseY;
		startX = x;
		startY = y;
		return false;
	}
	

}
