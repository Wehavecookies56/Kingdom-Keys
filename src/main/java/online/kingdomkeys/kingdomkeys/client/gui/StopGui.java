package online.kingdomkeys.kingdomkeys.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import online.kingdomkeys.kingdomkeys.data.GlobalData;
import online.kingdomkeys.kingdomkeys.effects.ModMobEffects;
import org.jetbrains.annotations.NotNull;
import org.lwjgl.glfw.GLFW;

public class StopGui extends Screen {

	public StopGui() {
		super(Component.literal(""));
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
	public void render(@NotNull GuiGraphics gui, int p_render_1_, int p_render_2_, float p_render_3_) {
		if(minecraft.player != null) {
			if(!minecraft.player.hasEffect(ModMobEffects.STOP)) {
				onClose();
			}

		}
	}

	@Override
	public void onClose() {
		GLFW.glfwSetInputMode(minecraft.getWindow().getWindow(), GLFW.GLFW_CURSOR, GLFW.GLFW_CURSOR_NORMAL);
		super.onClose();
	}
}