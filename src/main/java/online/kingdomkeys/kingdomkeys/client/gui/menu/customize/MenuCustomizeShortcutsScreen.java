package online.kingdomkeys.kingdomkeys.client.gui.menu.customize;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import online.kingdomkeys.kingdomkeys.data.ModData;
import online.kingdomkeys.kingdomkeys.client.gui.elements.MenuBackground;
import online.kingdomkeys.kingdomkeys.client.gui.elements.MenuBox;
import online.kingdomkeys.kingdomkeys.client.gui.elements.buttons.MenuButton;
import online.kingdomkeys.kingdomkeys.client.gui.elements.buttons.MenuButton.ButtonType;
import online.kingdomkeys.kingdomkeys.client.gui.elements.buttons.MenuScrollBar;
import online.kingdomkeys.kingdomkeys.data.PlayerData;
import online.kingdomkeys.kingdomkeys.lib.Strings;
import online.kingdomkeys.kingdomkeys.magic.Magic;
import online.kingdomkeys.kingdomkeys.magic.ModMagic;
import online.kingdomkeys.kingdomkeys.network.PacketHandler;
import online.kingdomkeys.kingdomkeys.network.cts.CSSetShortcutPacket;
import online.kingdomkeys.kingdomkeys.util.Utils;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map.Entry;

public class MenuCustomizeShortcutsScreen extends MenuBackground {
		
	MenuBox box;

	MenuButton back;
	MenuButton[] shortcuts = new MenuButton[9];
	MenuButton unequip;
	MenuScrollBar scrollBar;

	List<MenuButton> magics = new ArrayList<>();

	int buttonsX = 0;

	private int selectedShortcut = 0;

    LinkedHashSet<ResourceLocation> allMagic;
	
	public MenuCustomizeShortcutsScreen(LinkedHashMap<String, int[]> knownMagic) {
		super(Strings.Gui_Menu_Customize_Shortcuts, new Color(0,0,255));
		
		allMagic = new LinkedHashSet<>();
        knownMagic.forEach((s, ints) -> {
            if (ModMagic.registry.containsKey(ResourceLocation.parse(s))) {
                allMagic.add(ResourceLocation.parse(s));
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
		super.init();
		init(0, 0);
	}

	public void init(float scrollOffset, int handleY) {
		drawSeparately = true;
		float boxPosX = (float) width * 0.25F;
		float boxWidth = (float) width * 0.67F;

		buttonPosY = (int) topBarHeight + 5;
		
		box = new MenuBox((int) boxPosX, (int) topBarHeight, (int) boxWidth, (int) middleHeight, new Color(4, 4, 68));
		buttonsX = box.getX() + 10;

		this.renderables.clear();
		this.children().clear();
		this.magics.clear();

		for(int i = 0; i< shortcuts.length;i++) {
			int j = i;
			addRenderableWidget(shortcuts[i] = new MenuButton((int) buttonPosX, buttonPosY +  (i * 18), (int) buttonWidth, Utils.translateToLocal("gui.menu.customize.shortcut")+" "+(i+1), ButtonType.BUTTON, (e) -> { selectedShortcut = j; init(scrollBar.scrollOffset, scrollBar.handleY);}));
		}		
		
		PlayerData playerData = PlayerData.get(minecraft.player);
		int totalMagics = 0;
		int magicLine = 0;
		addRenderableWidget(unequip = new MenuButton((int) buttonPosX, buttonPosY - 18, (int) (buttonWidth), Utils.translateToLocal("gui.menu.customize.unequip"), ButtonType.BUTTON, (e) -> { select(null,0); }));

		for (ResourceLocation entry : allMagic) {
			Magic magic = ModMagic.registry.get(entry);
			if(magic != null) {
				int level = playerData.getMagicLevel(entry);
				while(level >= 0) {
					int lvl = level;
					MenuButton button = new MenuButton((int) ((int) (width * 0.32F) + (level * (buttonWidth + 5))), buttonPosY +  (magicLine * 18), (int) (buttonWidth * 0.8), Utils.translateToLocal(magic.getTranslationKey(level)), ButtonType.SUBBUTTON, (e) -> { select(magic,lvl); });
					magics.add(button);
					addRenderableWidget(button);
					magics.get(totalMagics).setData(magic.getRegistryName().toString()+","+level);
					level--;
					totalMagics++;
				}
				magicLine++;
			}
		}

		int contentHeight = !magics.isEmpty() ? magics.get(magics.size()-1).getY() - magics.get(0).getY() + 28 : 0;
		addRenderableWidget(scrollBar = new MenuScrollBar((int) (boxPosX + boxWidth) - MenuScrollBar.WIDTH - 4, (int) topBarHeight + 2, (int) (topBarHeight + middleHeight)-2, (int) middleHeight, contentHeight));
		scrollBar.scrollOffset = scrollOffset;
		scrollBar.setHandleY(handleY);

        for (MenuButton magic : magics) {
            if (magic != null) {
                magic.active = !isMagicAlreadyEquipped(magic.getData());
                magic.offsetY = (int) scrollBar.scrollOffset;
            }
        }

		addRenderableWidget(back = new MenuButton((int) buttonPosX, buttonPosY + (9 * 18), (int) buttonWidth, Utils.translateToLocal(Strings.Gui_Menu_Back), ButtonType.BUTTON, (e) -> action("back")));
	}

	@Override
	public void render(@NotNull GuiGraphics gui, int mouseX, int mouseY, float partialTicks) {
		box.render(gui, mouseX, mouseY, partialTicks);
		drawMenuBackground(gui, mouseX, mouseY, partialTicks);
		gui.enableScissor(box.getX(), box.getY(), box.getX() + box.getWidth(), box.getY() + box.getHeight());
		magics.forEach(menuButton -> menuButton.render(gui, mouseX, mouseY, partialTicks));
		gui.disableScissor();

		for(int i = 0; i < shortcuts.length; i++) {
			shortcuts[i].render(gui, mouseX, mouseY, partialTicks);
			shortcuts[i].active = i != selectedShortcut;
		}
		back.render(gui, mouseX, mouseY, partialTicks);
		unequip.render(gui, mouseX, mouseY, partialTicks);
		scrollBar.render(gui, mouseX, mouseY, partialTicks);
		
		for(int i = 0; i < magics.size(); i++) {
			if(magics.get(i) != null) {
				magics.get(i).active = !isMagicAlreadyEquipped(magics.get(i).getData());
				magics.get(i).offsetY = (int) scrollBar.scrollOffset;
			}
		}
	}

	private boolean isMagicAlreadyEquipped(String string) {
		PlayerData playerData = PlayerData.get(minecraft.player);
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
			init(scrollBar.scrollOffset, scrollBar.handleY);
		}
	}

	public void updateScroll() {
		for(int i = 0; i < magics.size(); i++) {
			if(magics.get(i) != null) {
				magics.get(i).offsetY = (int) scrollBar.scrollOffset;
			}
		}
	}

	@Override
	public boolean mouseClicked(double pMouseX, double pMouseY, int pButton) {
		scrollBar.mouseClicked(pMouseX, pMouseY, pButton);
		return super.mouseClicked(pMouseX, pMouseY, pButton);
	}

	@Override
	public boolean mouseReleased(double pMouseX, double pMouseY, int pButton) {
		scrollBar.mouseReleased(pMouseX, pMouseY, pButton);
		return super.mouseReleased(pMouseX, pMouseY, pButton);
	}

	@Override
	public boolean mouseDragged(double pMouseX, double pMouseY, int pButton, double pDragX, double pDragY) {
		scrollBar.mouseDragged(pMouseX, pMouseY, pButton, pDragX, pDragY);
		updateScroll();
		return super.mouseDragged(pMouseX, pMouseY, pButton, pDragX, pDragY);
	}

	@Override
	public boolean mouseScrolled(double pMouseX, double pMouseY, double deltaX, double deltaY) {
		scrollBar.mouseScrolled(pMouseX, pMouseY, deltaX, deltaY);
		updateScroll();
		return super.mouseScrolled(pMouseX, pMouseY, deltaX, deltaY);
	}
}
