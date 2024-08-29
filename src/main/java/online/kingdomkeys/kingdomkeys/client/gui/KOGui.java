package online.kingdomkeys.kingdomkeys.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.ChatScreen;
import net.minecraft.client.gui.screens.GenericMessageScreen;
import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraft.network.chat.Component;
import online.kingdomkeys.kingdomkeys.data.ModData;
import online.kingdomkeys.kingdomkeys.client.gui.elements.buttons.MenuButton;
import online.kingdomkeys.kingdomkeys.lib.Strings;
import online.kingdomkeys.kingdomkeys.network.PacketHandler;
import online.kingdomkeys.kingdomkeys.network.cts.CSGiveUpKO;
import online.kingdomkeys.kingdomkeys.util.Utils;
import org.jetbrains.annotations.NotNull;

public class KOGui extends ChatScreen {

	Button giveUp, exit;
	public KOGui() {
		super("");
		minecraft = Minecraft.getInstance();
	}

	private void action(String string) {
		switch(string){
			case "giveup" -> {
				PacketHandler.sendToServer(new CSGiveUpKO());
				this.minecraft.setScreen(null);
				ModData.getGlobal(minecraft.player).setKO(false);
			}
			case "exit" -> {
				if (this.minecraft.level != null) {
					this.minecraft.level.disconnect();
				}

				this.minecraft.clearClientLevel(new GenericMessageScreen(Component.translatable("menu.savingLevel")));
				this.minecraft.setScreen(new TitleScreen());}
            default -> System.out.println("Unexpected value: " + string);
        }
	}

	@Override
	protected void init() {
		//GLFW.glfwSetInputMode(minecraft.getWindow().getWindow(), GLFW.GLFW_CURSOR, GLFW.GLFW_CURSOR_DISABLED);
		super.init();
		addRenderableWidget(giveUp = new MenuButton((int) (width/2F) - 40, (int)(height * 0.6F), (int) 40, Utils.translateToLocal(Strings.Gui_KO_Die), MenuButton.ButtonType.BUTTON, (e) -> { action("giveup"); }));
		addRenderableWidget(exit = new MenuButton((int) (width/2F) - 40, (int)(height * 0.6F) + 18, (int) 40, Utils.translateToLocal(Strings.Gui_KO_Quit), MenuButton.ButtonType.BUTTON, (e) -> { action("exit"); }));
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
        if(minecraft != null && minecraft.player != null) {
			if(minecraft.player.getHealth() <= 0) {
				onClose();
			}
			IGlobalCapabilities globalData = ModData.getGlobal(minecraft.player);
			if(globalData != null){
				if(!globalData.isKO()){
					onClose();
				}
			}
		}
		super.render(gui, p_render_1_, p_render_2_, p_render_3_);
	}
}