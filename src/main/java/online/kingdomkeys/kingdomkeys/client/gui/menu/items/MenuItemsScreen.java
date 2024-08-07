package online.kingdomkeys.kingdomkeys.client.gui.menu.items;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import online.kingdomkeys.kingdomkeys.capability.IPlayerCapabilities;
import online.kingdomkeys.kingdomkeys.capability.ModCapabilities;
import online.kingdomkeys.kingdomkeys.client.gui.GuiHelper;
import online.kingdomkeys.kingdomkeys.client.gui.elements.MenuBackground;
import online.kingdomkeys.kingdomkeys.client.gui.elements.buttons.MenuButton;
import online.kingdomkeys.kingdomkeys.client.gui.menu.items.equipment.MenuEquipmentScreen;
import online.kingdomkeys.kingdomkeys.lib.Strings;
import org.jetbrains.annotations.NotNull;

import java.awt.*;

public class MenuItemsScreen extends MenuBackground {

    Button items_player, items_stock, items_back;

    public MenuItemsScreen() {
		super(Strings.Gui_Menu_Items, new Color(0,0,255));
        minecraft = Minecraft.getInstance();
    }
    @Override
    public void init () {
		super.init();
		this.renderables.clear();

        int button_items_playerY = buttonPosY;
        int button_items_stockY = button_items_playerY + 18 * 1;
        int button_items_backY = button_items_playerY + 18 * 2;

        Minecraft mc = Minecraft.getInstance();

        IPlayerCapabilities playerData = ModCapabilities.getPlayer(mc.player);

        addRenderableWidget(items_player = new MenuButton((int)buttonPosX, button_items_playerY, (int)buttonWidth, Strings.Gui_Menu_Items_Equipment, MenuButton.ButtonType.BUTTON, true, b -> openItems(playerData)));
        addRenderableWidget(items_stock = new MenuButton((int)buttonPosX, button_items_stockY, (int)buttonWidth, Strings.Gui_Menu_Items_Stock, MenuButton.ButtonType.BUTTON, true, b -> mc.setScreen(new MenuStockScreen())));
        addRenderableWidget(items_back = new MenuButton((int)buttonPosX, button_items_backY, (int)buttonWidth, Strings.Gui_Menu_Back, MenuButton.ButtonType.BUTTON, true, b -> GuiHelper.openMenu()));

    }

    public void openItems(IPlayerCapabilities playerData) {
        //if (playerData.getAlignment() == Utils.OrgMember.NONE) {
            Minecraft.getInstance().setScreen(new MenuEquipmentScreen());
       // } else {
        //    Minecraft.getInstance().displayGuiScreen(new WeaponTreeSelectionScreen(playerData.getAlignment()));
       // }
    }

    @Override
    public void render(@NotNull GuiGraphics gui, int mouseX, int mouseY, float partialTicks) {
        super.render(gui, mouseX, mouseY, partialTicks);
    }
}
