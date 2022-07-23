package online.kingdomkeys.kingdomkeys.client.gui;

import org.lwjgl.glfw.GLFW;

import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.TranslatableComponent;
import online.kingdomkeys.kingdomkeys.capability.ModCapabilities;

public class StopGui extends Screen {

	public StopGui() {
		super(new TranslatableComponent(""));
		minecraft = Minecraft.getInstance();
	}

	@Override
	protected void init() {
		GLFW.glfwSetInputMode(minecraft.getWindow().getWindow(), GLFW.GLFW_CURSOR, GLFW.GLFW_CURSOR_DISABLED);

		super.init();
	}
	@Override
	public boolean shouldCloseOnEsc() {
		return false;
	}
	@Override
	public boolean isPauseScreen() {
		return false;
	}

	@Override
	public void render(PoseStack matrixStack, int p_render_1_, int p_render_2_, float p_render_3_) {
		if(minecraft.player != null) {

			if(ModCapabilities.getGlobal(minecraft.player) != null) {
	            //InputConstants.grabOrReleaseMouse(this.minecraft.getWindow().getWindow(), 212993, 0, 0);
				if(ModCapabilities.getGlobal(minecraft.player).getStoppedTicks() <= 0) {
					onClose();
				}
				
			}
		}
		//super.render(matrixStack, p_render_1_, p_render_2_, p_render_3_);
	}
}