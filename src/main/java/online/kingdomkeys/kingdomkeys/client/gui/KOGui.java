package online.kingdomkeys.kingdomkeys.client.gui;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.ChatScreen;
import net.minecraft.client.gui.screens.GenericMessageScreen;
import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraft.network.chat.Component;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.client.gui.elements.buttons.MenuButton;
import online.kingdomkeys.kingdomkeys.effects.ModMobEffects;
import online.kingdomkeys.kingdomkeys.lib.Strings;
import online.kingdomkeys.kingdomkeys.network.PacketHandler;
import online.kingdomkeys.kingdomkeys.network.cts.CSGiveUpKO;
import online.kingdomkeys.kingdomkeys.util.Utils;

public class KOGui extends ChatScreen {
    private boolean allowChatSend = true;

    Button giveUp, exit;

    public KOGui() {
        super(Component.EMPTY.getString());
    }

    @Override
    protected void init() {
        //GLFW.glfwSetInputMode(minecraft.getWindow().getWindow(), GLFW.GLFW_CURSOR, GLFW.GLFW_CURSOR_DISABLED);
        super.init();
        int cx = width / 2;
        int cy = (int) (height * 0.3F);

        addRenderableWidget(giveUp = new MenuButton(cx - 40, cy, 40, Utils.translateToLocal(Strings.Gui_KO_Die), MenuButton.ButtonType.BUTTON, (e) -> action("giveup")));
        addRenderableWidget(exit = new MenuButton(cx - 40, cy + 18, 40, Utils.translateToLocal(Strings.Gui_KO_Quit), MenuButton.ButtonType.BUTTON, (e) -> action("exit")));
    }


    @Override
    public void onClose() {
        allowChatSend = false;
        super.onClose();
    }

    @Override
    public void handleChatInput(String message, boolean addToRecentChat) {
        if(allowChatSend)
            super.handleChatInput(message, addToRecentChat);
        else
            KingdomKeys.LOGGER.debug("Prevented message being sent while KO: ["+message+"]");
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if(keyCode == 257 || keyCode == 335) {//enter
            if(input.getValue().trim().isEmpty()){
                return false;
            } else {
                this.handleChatInput(this.input.getValue(), true);
                this.input.setValue("");
                this.minecraft.gui.getChat().resetChatScroll();
                return true;
            }
        }
        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    private void action(String string) {
        switch (string) {
            case "giveup" -> {
                //Prevents screen flickering
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

     //   System.out.println(input.getValue());

       /* Minecraft mc = Minecraft.getInstance();
        int width = mc.getWindow().getGuiScaledWidth();
        int height = mc.getWindow().getGuiScaledHeight();

        int layers = 50;
        int max = 140;

        for (int i = 0; i < layers; i++) {
            float t = (float)i / (layers - 1);
            float alpha = (float)Math.pow(t, 2.2);

            int a = (int)(alpha * 180) << 24;  // opacidad máxima

            int thickness = (int)((1f - t) * max);

            gui.fill(0, 0, width, thickness, a);
            gui.fill(0, height - thickness, width, height, a);
            gui.fill(0, thickness, thickness, height - thickness, a);
            gui.fill(width - thickness, thickness, width, height - thickness, a);
        }*/
        super.render(gui, mouseX, mouseY, partialTicks);
    }
}
