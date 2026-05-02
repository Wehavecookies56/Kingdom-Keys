package online.kingdomkeys.kingdomkeys.client.gui.menu.customize;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import online.kingdomkeys.kingdomkeys.client.gui.elements.MenuBackground;
import online.kingdomkeys.kingdomkeys.client.gui.elements.MenuBox;
import online.kingdomkeys.kingdomkeys.client.gui.elements.buttons.MenuButton;
import online.kingdomkeys.kingdomkeys.client.gui.elements.buttons.MenuButton.ButtonType;
import online.kingdomkeys.kingdomkeys.client.gui.elements.buttons.MenuScrollBar;
import online.kingdomkeys.kingdomkeys.data.PlayerData;
import online.kingdomkeys.kingdomkeys.item.MagicSpellItem;
import online.kingdomkeys.kingdomkeys.lib.Strings;
import online.kingdomkeys.kingdomkeys.magic.Magic;
import online.kingdomkeys.kingdomkeys.magic.ModMagic;
import online.kingdomkeys.kingdomkeys.network.PacketHandler;
import online.kingdomkeys.kingdomkeys.network.cts.CSSetShortcutPacket;
import online.kingdomkeys.kingdomkeys.util.Utils;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MenuCustomizeShortcutsScreen extends MenuBackground {
		
	MenuBox box;

	MenuButton back;
	MenuButton[] shortcuts = new MenuButton[9];
	MenuButton unequip;
	MenuScrollBar scrollBar;

	List<MenuButton> magics = new ArrayList<>();

	int buttonsX = 0;

	private int selectedShortcut = 0;
	
	public MenuCustomizeShortcutsScreen() {
		super(Strings.Gui_Menu_Customize_Shortcuts, new Color(0,0,255));

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
		
		box = new MenuBox((int) boxPosX, (int) topBarHeight, (int) boxWidth, (int) middleHeight, 0.6F,new Color(4, 4, 68));
		buttonsX = box.getX() + 10;

		this.renderables.clear();
		this.children().clear();
		this.magics.clear();

		for(int i = 0; i< shortcuts.length;i++) {
			int j = i;
			addRenderableWidget(shortcuts[i] = new MenuButton((int) buttonPosX, buttonPosY +  (i * 18), (int) buttonWidth, Utils.translateToLocal("gui.menu.customize.shortcut")+" "+(i+1), ButtonType.BUTTON, (e) -> { selectedShortcut = j; init(scrollBar.scrollOffset, scrollBar.handleY);}));
		}		
		
		PlayerData playerData = PlayerData.get(minecraft.player);
		addRenderableWidget(unequip = new MenuButton((int) buttonPosX, buttonPosY - 18, (int) (buttonWidth), Utils.translateToLocal("gui.menu.customize.unequip"), ButtonType.BUTTON, (e) -> { select(-1); }));

		playerData.getEquippedMagics().entrySet().stream()
				.sorted(Map.Entry.comparingByKey())
				.forEach(entry -> {
					int slot = entry.getKey();
					ItemStack stack = entry.getValue();

					if (stack.isEmpty() || !(stack.getItem() instanceof MagicSpellItem spell)) return;

					int level = spell.getLevel();
					Magic magic = ModMagic.registry.get(ResourceLocation.parse(spell.getMagic()));

					MenuButton button = new MenuButton(
							(int) (width * 0.32F),
							buttonPosY + (magics.size() * 18),
							(int) (buttonWidth * 0.8),
							Utils.translateToLocal(magic.getTranslationKey(level)),
							ButtonType.SUBBUTTON,
							(e) -> select(slot)
					);


					button.setData(String.valueOf(slot));
					magics.add(button);
					addRenderableWidget(button);
				});

		int contentHeight = !magics.isEmpty() ? magics.get(magics.size()-1).getY() - magics.get(0).getY() + 28 : 0;
		addRenderableWidget(scrollBar = new MenuScrollBar((int) (boxPosX + boxWidth) - MenuScrollBar.WIDTH - 4, (int) topBarHeight + 2, (int) (topBarHeight + middleHeight)-2, (int) middleHeight, contentHeight, true));
		scrollBar.scrollOffset = scrollOffset;
		scrollBar.setHandleY(handleY);

		for (MenuButton magic : magics) {
			if (magic != null) {
				int slot = Integer.parseInt(magic.getData());
				magic.active = !isMagicAlreadyEquipped(slot);
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

		for (MenuButton magic : magics) {
			if (magic != null) {
				int slot = Integer.parseInt(magic.getData()); // recuperar slot
				magic.active = !isMagicAlreadyEquipped(slot);
				magic.offsetY = (int) scrollBar.scrollOffset;
			}
		}
	}

	private boolean isMagicAlreadyEquipped(int slot) {
		PlayerData playerData = PlayerData.get(minecraft.player);
		for (int usedSlot : playerData.getShortcutsMap().values()) {
			if (usedSlot == slot) {
				return true;
			}
		}
		return false;
	}


	private void select(int slot) {
		if (slot < 0) {
			PacketHandler.sendToServer(new CSSetShortcutPacket(selectedShortcut, -1));
		} else {
			PacketHandler.sendToServer(new CSSetShortcutPacket(selectedShortcut, slot));
		}

		if (selectedShortcut < 8) {
			selectedShortcut++;
			init(scrollBar.scrollOffset, scrollBar.handleY);
		}
	}

	public void updateScroll() {
		for (MenuButton magic : magics) {
			if (magic != null) {
				magic.offsetY = (int) scrollBar.scrollOffset;
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
