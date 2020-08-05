package online.kingdomkeys.kingdomkeys.client.gui.menu;

import java.awt.Color;
import java.io.IOException;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.widget.button.Button;
import online.kingdomkeys.kingdomkeys.client.gui.GuiHelper;

public class GuiMenu_Items extends GuiMenu_Background {

    final int ITEMS_PLAYER = 1, ITEMS_STOCK = 2, ITEMS_BACK = 3;

    Button items_player, items_stock, items_back;

    GuiMenu_Background background;

	private Minecraft mc;

    public GuiMenu_Items() {
		super("Items", new Color(0,0,255));

        mc = Minecraft.getInstance();
    }

    protected void action (int buttonID){
        switch (buttonID) {
            /*case ITEMS_PLAYER:
                GuiHelper.openMenu_Items_Player();
                break;
            case ITEMS_BACK:
                GuiHelper.openMenu();
                break;
            case ITEMS_STOCK:
                mc.displayGuiScreen(new GuiStock());
                break;*/
        }
        //updateButtons();
    }

    /*private void updateButtons () {
        updateScreen();
    }*/

    @Override
    public void init () {
    	super.width = width;
		super.height = height;
		super.init();
		this.buttons.clear();
        float topBarHeight = (float)height * 0.17F;
        int button_itemsY = (int)topBarHeight+5;
        float buttonPosX = (float)width * 0.1526F;
        float buttonWidth = ((float)width * 0.1744F)-22;

        int button_items_playerY = button_itemsY;
        int button_items_stockY = button_items_playerY + 22;
        int button_items_backY = button_items_stockY + 22;

        /*buttons.add(items_player = new GuiMenuButton(ITEMS_PLAYER, (int)buttonPosX, button_items_playerY, (int)buttonWidth, mc.player.getDisplayNameString()));
        buttons.add(items_stock = new GuiMenuButton(ITEMS_STOCK, (int)buttonPosX, button_items_stockY, (int)buttonWidth, Utils.translateToLocal(Strings.Gui_Menu_Items_Button_Stock)));
        buttons.add(items_back = new GuiMenuButton(ITEMS_BACK, (int)buttonPosX, button_items_backY, (int)buttonWidth, Utils.translateToLocal(Strings.Gui_Menu_Items_Button_Back)));

    updateButtons();*/
}

    @Override
    public void render(int mouseX, int mouseY, float partialTicks) {
        background.drawBars();
        background.drawMunnyTime();
        background.drawBiomeDim();
        super.render(mouseX, mouseY, partialTicks);
    }
    
    //@Override
	protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
		if (mouseButton == 1) {
			GuiHelper.openMenu();
		}
		super.mouseClicked(mouseX, mouseY, mouseButton);
	}
}
