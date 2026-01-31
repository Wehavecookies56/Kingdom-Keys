package online.kingdomkeys.kingdomkeys.client.gui;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.ChatScreen;
import net.minecraft.client.gui.screens.GenericMessageScreen;
import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraft.network.chat.Component;
import online.kingdomkeys.kingdomkeys.client.gui.elements.buttons.MenuButton;
import online.kingdomkeys.kingdomkeys.effects.ModMobEffects;
import online.kingdomkeys.kingdomkeys.lib.Strings;
import online.kingdomkeys.kingdomkeys.network.PacketHandler;
import online.kingdomkeys.kingdomkeys.network.cts.CSGiveUpKO;
import online.kingdomkeys.kingdomkeys.util.Utils;
import org.lwjgl.glfw.GLFW;

public class KOGui extends ChatScreen {

    Button giveUp, exit;

    public KOGui() {
        super(Component.EMPTY.getString());
    }

    @Override
    protected void init() {
        //GLFW.glfwSetInputMode(minecraft.getWindow().getWindow(), GLFW.GLFW_CURSOR, GLFW.GLFW_CURSOR_DISABLED);
        super.init();

 //      this.input.setFocused(true);

        int cx = width / 2;
        int cy = (int) (height * 0.6F);

        addRenderableWidget(giveUp = new MenuButton(cx - 40, cy, 40, Utils.translateToLocal(Strings.Gui_KO_Die), MenuButton.ButtonType.BUTTON, (e) -> action("giveup")));

        addRenderableWidget(exit = new MenuButton(cx - 40, cy + 18, 40, Utils.translateToLocal(Strings.Gui_KO_Quit), MenuButton.ButtonType.BUTTON, (e) -> action("exit")));
    }

    private void action(String string) {
        switch (string) {
            case "giveup" -> {
                if (minecraft.player != null) {
                    minecraft.player.removeEffect(ModMobEffects.KO);
                }
                PacketHandler.sendToServer(new CSGiveUpKO());
                onClose();
            }
            case "exit" -> {
                if (this.minecraft.level != null) {
                    this.minecraft.level.disconnect();
                }
                this.minecraft.clearClientLevel(new GenericMessageScreen(Component.translatable("menu.savingLevel")));
                this.minecraft.setScreen(new TitleScreen());
            }
        }
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
    public void render(GuiGraphics gui, int mouseX, int mouseY, float partialTicks) {
        // If revives or dies close it
        if (minecraft.player != null) {
            if(minecraft.player.isDeadOrDying()) {
                onClose();
                return;
            }
            if(!minecraft.player.hasEffect(ModMobEffects.KO)){
                onClose();
                return;
            }
        }

        super.render(gui, mouseX, mouseY, partialTicks);
    }
}
