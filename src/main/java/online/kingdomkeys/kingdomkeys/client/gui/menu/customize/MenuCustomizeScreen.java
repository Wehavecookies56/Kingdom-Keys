package online.kingdomkeys.kingdomkeys.client.gui.menu.customize;

import java.awt.Color;

import org.jetbrains.annotations.NotNull;

import net.minecraft.client.gui.GuiGraphics;
import online.kingdomkeys.kingdomkeys.client.gui.GuiHelper;
import online.kingdomkeys.kingdomkeys.client.gui.elements.MenuBackground;
import online.kingdomkeys.kingdomkeys.client.gui.elements.MenuBox;
import online.kingdomkeys.kingdomkeys.client.gui.elements.buttons.MenuButton;
import online.kingdomkeys.kingdomkeys.lib.Strings;
import online.kingdomkeys.kingdomkeys.network.PacketHandler;
import online.kingdomkeys.kingdomkeys.network.cts.CSOpenMagicCustomize;
import online.kingdomkeys.kingdomkeys.network.cts.CSOpenShortcutsCustomize;
import online.kingdomkeys.kingdomkeys.util.Utils;

public class MenuCustomizeScreen extends MenuBackground {

    MenuBox box;

    MenuButton shortcuts, magic, back;

    int buttonsX = 0;

    public MenuCustomizeScreen() {
        super(Strings.Gui_Menu_Customize, new Color(0,0,255));
        drawPlayerInfo = false;
    }

    protected void action(String string) {
        switch(string) {
            case "shortcuts":
                PacketHandler.sendToServer(new CSOpenShortcutsCustomize());
                break;
            case "magic":
                PacketHandler.sendToServer(new CSOpenMagicCustomize());
                break;
            case "back":
                GuiHelper.openMenu();
                break;
        }
    }

    @Override
    public void init() {
        float boxPosX = (float) width * 0.25F;
        float topBarHeight = (float) height * 0.17F;
        float boxWidth = (float) width * 0.67F;
        float middleHeight = (float) height * 0.6F;
        
        buttonPosY = (int) topBarHeight + 5;

        box = new MenuBox((int) boxPosX, (int) topBarHeight, (int) boxWidth, (int) middleHeight, new Color(4, 4, 68));
        buttonsX = box.getX() + 10;

        super.init();
        this.renderables.clear();

        addRenderableWidget(shortcuts = new MenuButton((int) buttonPosX, (int) buttonPosY, (int) buttonWidth, Utils.translateToLocal(Strings.Gui_Menu_Customize_Shortcuts), MenuButton.ButtonType.BUTTON, (e) -> action("shortcuts")));
        addRenderableWidget(magic = new MenuButton((int) buttonPosX, (int) buttonPosY + (1 * 18), (int) buttonWidth, Utils.translateToLocal(Strings.Gui_Menu_Customize_Magic), MenuButton.ButtonType.BUTTON, (e) -> action("magic")));

        addRenderableWidget(back = new MenuButton((int) buttonPosX, (int) buttonPosY + (2 * 18), (int) buttonWidth, Utils.translateToLocal(Strings.Gui_Menu_Back), MenuButton.ButtonType.BUTTON, (e) -> action("back")));

    }

    @Override
    public void render(@NotNull GuiGraphics gui, int mouseX, int mouseY, float partialTicks) {
        super.render(gui, mouseX, mouseY, partialTicks);
    }
}
