package online.kingdomkeys.kingdomkeys.client.gui.menu.items;

import java.awt.Color;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.util.text.TranslationTextComponent;
import online.kingdomkeys.kingdomkeys.client.gui.GuiHelper;
import online.kingdomkeys.kingdomkeys.client.gui.elements.MenuBackground;
import online.kingdomkeys.kingdomkeys.client.gui.elements.buttons.MenuButton;
import online.kingdomkeys.kingdomkeys.client.gui.menu.MenuScreen;
import online.kingdomkeys.kingdomkeys.client.gui.menu.items.equipment.MenuEquipmentScreen;
import online.kingdomkeys.kingdomkeys.lib.Strings;

public class MenuItemsScreen extends MenuBackground {

    Button items_player, items_stock, items_back;

    public MenuItemsScreen() {
		super(Strings.Gui_Menu_Items, new Color(0,0,255));
        minecraft = Minecraft.getInstance();
    }
    @Override
    public void init () {
		super.init();
		this.buttons.clear();

        int button_items_playerY = buttonPosY;
        int button_items_stockY = button_items_playerY + 18 * 1;
        int button_items_backY = button_items_playerY + 18 * 2;

        Minecraft mc = Minecraft.getInstance();

        addButton(items_player = new MenuButton((int)buttonPosX, button_items_playerY, (int)buttonWidth, new TranslationTextComponent(Strings.Gui_Menu_Items_Equipment).getFormattedText(), MenuButton.ButtonType.BUTTON, b -> mc.displayGuiScreen(new MenuEquipmentScreen())));
        addButton(items_stock = new MenuButton((int)buttonPosX, button_items_stockY, (int)buttonWidth, new TranslationTextComponent(Strings.Gui_Menu_Items_Stock).getFormattedText(), MenuButton.ButtonType.BUTTON, b -> mc.displayGuiScreen(new MenuStockScreen())));
        addButton(items_back = new MenuButton((int)buttonPosX, button_items_backY, (int)buttonWidth, new TranslationTextComponent(Strings.Gui_Menu_Back).getFormattedText(), MenuButton.ButtonType.BUTTON, b -> GuiHelper.openMenu()));

}

    @Override
    public void render(int mouseX, int mouseY, float partialTicks) {
        super.render(mouseX, mouseY, partialTicks);
    }
}
