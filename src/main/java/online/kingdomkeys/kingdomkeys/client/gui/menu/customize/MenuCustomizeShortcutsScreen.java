package online.kingdomkeys.kingdomkeys.client.gui.menu.customize;

import java.awt.Color;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map.Entry;

import org.jetbrains.annotations.NotNull;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import online.kingdomkeys.kingdomkeys.capability.IPlayerCapabilities;
import online.kingdomkeys.kingdomkeys.capability.ModCapabilities;
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

public class MenuCustomizeShortcutsScreen extends MenuBackground {
		
	MenuBox box;

	MenuButton back;
	MenuButton[] shortcuts = new MenuButton[9];
	MenuButton unequip;

	MenuButton[] magics = new MenuButton[100];

	int buttonsX = 0;

	private int selectedShortcut = 0;

    LinkedHashSet<ResourceLocation> allMagic;
	
	public MenuCustomizeShortcutsScreen(LinkedHashMap<String, int[]> knownMagic) {
		super(Strings.Gui_Menu_Customize_Shortcuts, new Color(0,0,255));
		
		allMagic = new LinkedHashSet<>();
        knownMagic.forEach((s, ints) -> {
        	System.out.println(ints[0]);
            if (ModMagic.registry.get().containsKey(new ResourceLocation(s))) {
                allMagic.add(new ResourceLocation(s));
            }
        });
		drawPlayerInfo = false;
	}
	
	protected void action(String string) {
		switch(string) {
		case "back":
			Minecraft.getInstance().setScreen(new MenuCustomizeScreen());
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
		magics = new MenuButton[100];
		
		for(int i = 0; i< shortcuts.length;i++) {
			int j = i;
			addRenderableWidget(shortcuts[i] = new MenuButton((int) buttonPosX, buttonPosY +  (i * 18), (int) buttonWidth, Utils.translateToLocal("gui.menu.customize.shortcut")+" "+(i+1), ButtonType.BUTTON, (e) -> { selectedShortcut = j; init();}));
		}		
		
		//if(selectedShortcut > -1) {
			IPlayerCapabilities playerData = ModCapabilities.getPlayer(minecraft.player);
			int totalMagics = 0;
			int magicType = 0;
			addRenderableWidget(unequip = new MenuButton((int) (width * 0.32F) + (80), buttonPosY, (int) (buttonWidth * 0.8), Utils.translateToLocal("gui.menu.customize.unequip"), ButtonType.BUTTON, (e) -> { select(null,0); }));

			//for (Entry<String, int[]> entry : Utils.getSortedMagics(playerData.getMagicsMap()).entrySet()) {
			for (ResourceLocation entry : allMagic) {
				Magic magic = ModMagic.registry.get().getValue(entry);
				if(magic != null) {
					int level = playerData.getMagicLevel(entry);
					while(level >= 0) {
						int lvl = level;
						addRenderableWidget(magics[totalMagics] = new MenuButton((int) ((int) (width * 0.32F) + (level * (buttonWidth + 5))), buttonPosY + (18*2) + (magicType * 18), (int) (buttonWidth * 0.8), Utils.translateToLocal(magic.getTranslationKey(level)), ButtonType.BUTTON, (e) -> { select(magic,lvl); }));
						magics[totalMagics].setData(magic.getRegistryName().toString()+","+level);
						level--;
						totalMagics++;
					}
					magicType++;
				}
			}
			
			for(int i = 0; i < magics.length; i++) {
				if(magics[i] != null) {
					magics[i].active = !isMagicAlreadyEquipped(magics[i].getData());
				}
			}
		//}

		addRenderableWidget(back = new MenuButton((int) buttonPosX, buttonPosY + (9 * 18), (int) buttonWidth, Utils.translateToLocal(Strings.Gui_Menu_Back), ButtonType.BUTTON, (e) -> action("back")));
		
	}

	@Override
	public void render(@NotNull GuiGraphics gui, int mouseX, int mouseY, float partialTicks) {
		super.render(gui, mouseX, mouseY, partialTicks);

		for(int i = 0; i < shortcuts.length; i++) {
			shortcuts[i].active = i != selectedShortcut;
		}
		
		for(int i = 0; i < magics.length; i++) {
			if(magics[i] != null) {
				magics[i].active = !isMagicAlreadyEquipped(magics[i].getData());
			}
		}
	}

	private boolean isMagicAlreadyEquipped(String string) {
		IPlayerCapabilities playerData = ModCapabilities.getPlayer(minecraft.player);
		//System.out.println("1: "+string);
		for (Entry<Integer, String> entry : playerData.getShortcutsMap().entrySet()) {
			//System.out.println("2: "+entry.getValue());
			if(entry.getValue().equals(string)) {
				return true;
			}
		}
		return false;
	}


	private void select(Magic magic, int level) {
		if(magic == null) {
			PacketHandler.sendToServer(new CSSetShortcutPacket(selectedShortcut, level, ""));
		} else {
			PacketHandler.sendToServer(new CSSetShortcutPacket(selectedShortcut, level, magic.getRegistryName().toString()));
		}
		if(selectedShortcut < 8) {
			selectedShortcut++;
			init();
		}
	}	
}
