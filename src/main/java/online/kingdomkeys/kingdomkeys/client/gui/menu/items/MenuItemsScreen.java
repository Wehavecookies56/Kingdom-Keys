package online.kingdomkeys.kingdomkeys.client.gui.menu.items;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import online.kingdomkeys.kingdomkeys.client.gui.elements.MenuBackground;
import online.kingdomkeys.kingdomkeys.client.gui.elements.buttons.MenuButton;
import online.kingdomkeys.kingdomkeys.client.gui.menu.items.equipment.MenuEquipmentScreen;
import online.kingdomkeys.kingdomkeys.data.PlayerData;
import online.kingdomkeys.kingdomkeys.lib.Strings;
import online.kingdomkeys.kingdomkeys.network.PacketHandler;
import online.kingdomkeys.kingdomkeys.network.cts.CSOpenMenu;
import org.jetbrains.annotations.NotNull;

import java.awt.*;

public class MenuItemsScreen extends MenuBackground {

    Button items_player, items_melding, items_stock, items_back;

    public MenuItemsScreen() {
		super(Strings.Gui_Menu_Items, new Color(0,0,255));
        minecraft = Minecraft.getInstance();
    }
    @Override
    public void init () {
        super.init();
        this.renderables.clear();
        Minecraft mc = Minecraft.getInstance();

        int i = 0;
        addRenderableWidget(items_player = new MenuButton((int) buttonPosX, buttonPosY + i++ * 18, (int) buttonWidth, Strings.Gui_Menu_Items_Equipment, MenuButton.ButtonType.BUTTON, true, b -> mc.setScreen(new MenuEquipmentScreen())));
        addRenderableWidget(items_melding = new MenuButton((int) buttonPosX, buttonPosY + i++ * 18, (int) buttonWidth, Strings.Gui_Melding, MenuButton.ButtonType.BUTTON, true, b -> mc.setScreen(new MeldingScreen())));
        addRenderableWidget(items_stock = new MenuButton((int) buttonPosX, buttonPosY + i++ * 18, (int) buttonWidth, Strings.Gui_Menu_Items_Stock, MenuButton.ButtonType.BUTTON, true, b -> mc.setScreen(new MenuStockScreen())));
        addRenderableWidget(items_back = new MenuButton((int) buttonPosX, buttonPosY + i++ * 18, (int) buttonWidth, Strings.Gui_Menu_Back, MenuButton.ButtonType.BUTTON, true, b -> PacketHandler.sendToServer(new CSOpenMenu())));
    }

    @Override
    public void render(@NotNull GuiGraphics gui, int mouseX, int mouseY, float partialTicks) {
        super.render(gui, mouseX, mouseY, partialTicks);
    }
}
