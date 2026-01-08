package online.kingdomkeys.kingdomkeys.api.event.client;

import net.neoforged.bus.api.Event;
import online.kingdomkeys.kingdomkeys.client.gui.elements.buttons.MenuButton;
import online.kingdomkeys.kingdomkeys.client.gui.menu.MenuScreen;

import java.util.ArrayList;

public class MenuButtonRegisterEvent extends Event {

    private final MenuScreen screen;
    private final ArrayList<MenuButton> buttons;

    public MenuButtonRegisterEvent(MenuScreen screen, ArrayList<MenuButton> buttons) {
        this.screen = screen;
        this.buttons = buttons;
    }

    public MenuScreen getScreen() {
        return screen;
    }

    public ArrayList<MenuButton> getButtons() {
        return buttons;
    }
}
