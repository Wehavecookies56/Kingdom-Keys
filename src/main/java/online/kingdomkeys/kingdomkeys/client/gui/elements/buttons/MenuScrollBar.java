package online.kingdomkeys.kingdomkeys.client.gui.elements.buttons;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import online.kingdomkeys.kingdomkeys.KingdomKeys;

public class MenuScrollBar extends Button {

	int clickX, clickY, startX, startY, top, bottom;

	public MenuScrollBar(int x, int y, int widthIn, String buttonText, Button.OnPress onPress) {
		super(x, y, 22 + widthIn, 20, new TranslatableComponent(buttonText), onPress);
		height = 10;
		width = 14;
		
	}

	@Override
	public void render(PoseStack matrixStack, int mouseX, int mouseY, float partialTicks) {
		if (visible) {
			RenderSystem.setShaderTexture(0, new ResourceLocation(KingdomKeys.MODID, "textures/gui/menu/menu_button.png"));
			matrixStack.pushPose();
			matrixStack.translate(x, y, 0);
			matrixStack.scale(0.5F, 0.5F, 1);
			blit(matrixStack, 0, -9, 41, 29, 14, 9);
			matrixStack.popPose();
			matrixStack.pushPose();
			matrixStack.translate(x, y, 0);
			matrixStack.scale(0.5F, 0.5F, 1);
			blit(matrixStack, 0, height, 41, 41, 14, 9);
			matrixStack.popPose();
			for (int i = 0; i < height; i++) {
				matrixStack.pushPose();
				matrixStack.translate(x, y, 0);
				matrixStack.scale(0.5F, 0.5F, 0);
				blit(matrixStack, 0, i, 41, 39, 14, 1);
				matrixStack.popPose();
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
