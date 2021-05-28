package online.kingdomkeys.kingdomkeys.client.gui.menu.customize;

import java.awt.Color;
import java.util.Map.Entry;

import net.minecraft.util.ResourceLocation;
import online.kingdomkeys.kingdomkeys.capability.IPlayerCapabilities;
import online.kingdomkeys.kingdomkeys.capability.ModCapabilities;
import online.kingdomkeys.kingdomkeys.client.gui.GuiHelper;
import online.kingdomkeys.kingdomkeys.client.gui.elements.MenuBackground;
import online.kingdomkeys.kingdomkeys.client.gui.elements.MenuBox;
import online.kingdomkeys.kingdomkeys.client.gui.elements.buttons.MenuButton;
import online.kingdomkeys.kingdomkeys.client.gui.elements.buttons.MenuButton.ButtonType;
import online.kingdomkeys.kingdomkeys.lib.Strings;
import online.kingdomkeys.kingdomkeys.magic.Magic;
import online.kingdomkeys.kingdomkeys.magic.ModMagic;
import online.kingdomkeys.kingdomkeys.network.PacketHandler;
import online.kingdomkeys.kingdomkeys.network.cts.CSSetShortcutPacket;
import online.kingdomkeys.kingdomkeys.util.Utils;

public class MenuCustomizeScreen extends MenuBackground {
		
	MenuBox box;

	MenuButton back;
	MenuButton[] shortcuts = new MenuButton[9];
	MenuButton[] magics = new MenuButton[100];

	int buttonsX = 0;

	private int selectedShortcut = -1;
	
	public MenuCustomizeScreen() {
		super(Strings.Gui_Menu_Customize, new Color(0,0,255));
		drawPlayerInfo = false;
	}

	
	protected void action(String string) {
		switch(string) {
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
		
		box = new MenuBox((int) boxPosX, (int) topBarHeight, (int) boxWidth, (int) middleHeight, new Color(4, 4, 68));
		buttonsX = box.x + 10;
		
		super.init();
		this.buttons.clear();
		
		for(int i = 0; i< shortcuts.length;i++) {
			int j = i;
			addButton(shortcuts[i] = new MenuButton((int) buttonPosX, (int) topBarHeight +  (i * 18), (int) buttonWidth, "Shortcut "+(i+1), ButtonType.BUTTON, (e) -> { selectedShortcut = j; init();}));
		}		
		
		if(selectedShortcut > -1) {
			IPlayerCapabilities playerData = ModCapabilities.getPlayer(minecraft.player);
			int totalMagics = 0;
			int magicType = 0;
			for (Entry<String, int[]> entry : Utils.getSortedMagics(playerData.getMagicsMap()).entrySet()) {
				
				int level = entry.getValue()[0];
				Magic magic = ModMagic.registry.getValue(new ResourceLocation(entry.getKey()));
				while(level >= 0) {
					int lvl = level;
					addButton(magics[totalMagics] = new MenuButton((int) (width * 0.32F) + (level * 80), (int) topBarHeight + (magicType * 18), (int) (buttonWidth * 0.8), Utils.translateToLocal(magic.getTranslationKey(level)), ButtonType.BUTTON, (e) -> { select(magic,lvl); }));
					totalMagics++;
					System.out.println(Utils.translateToLocal(magic.getTranslationKey(level)) + " " + level--);
				}
				magicType++;
			}
		}

		addButton(back = new MenuButton((int) buttonPosX, (int) topBarHeight + (9 * 18), (int) buttonWidth, Utils.translateToLocal(Strings.Gui_Menu_Back), ButtonType.BUTTON, (e) -> { GuiHelper.openMenu(); }));
		
		for(int i = 0; i < shortcuts.length; i++) {
			shortcuts[i].active = i != selectedShortcut;
		}
	}

	private void select(Magic magic, int level) {
		System.out.println("SLOT "+selectedShortcut+": "+ magic.getTranslationKey(level));
		PacketHandler.sendToServer(new CSSetShortcutPacket(selectedShortcut, level, magic.getRegistryName().toString()));
	}	
}
