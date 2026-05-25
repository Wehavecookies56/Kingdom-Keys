package online.kingdomkeys.kingdomkeys.client.gui.menu.items.equipment;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.capabilities.Capabilities;
import online.kingdomkeys.kingdomkeys.client.gui.elements.MenuBackground;
import online.kingdomkeys.kingdomkeys.client.gui.elements.MenuBox;
import online.kingdomkeys.kingdomkeys.client.gui.elements.MenuColourBox;
import online.kingdomkeys.kingdomkeys.client.gui.elements.buttons.MenuButton;
import online.kingdomkeys.kingdomkeys.client.gui.elements.buttons.MenuScrollBar;
import online.kingdomkeys.kingdomkeys.client.gui.elements.buttons.MenuSelectMagicButton;
import online.kingdomkeys.kingdomkeys.data.PlayerData;
import online.kingdomkeys.kingdomkeys.item.MagicSpellItem;
import online.kingdomkeys.kingdomkeys.item.ModItems;
import online.kingdomkeys.kingdomkeys.lib.Strings;
import online.kingdomkeys.kingdomkeys.menu.BagInventory;
import online.kingdomkeys.kingdomkeys.util.Utils;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class MenuMagicSelectorScreen extends MenuBackground {

	public static final int EQUIPPED_OFFSET = -1000;
	public static final int BAG_OFFSET = -2000;

	public MenuScrollBar scrollBar;
	public int slot = -1;
	MenuBox boxL, boxR;
	Button back;
	MenuColourBox equipped;
	List<MenuSelectMagicButton> widgets = new ArrayList<>();
	int buttonColour;
	Color colour;

	public MenuMagicSelectorScreen(int slot, Color colour, int buttonColour) {

		super(Strings.Gui_Menu_Items_Equipment_Magic, new Color(0, 0, 255));

		drawSeparately = true;

		minecraft = Minecraft.getInstance();

		this.slot = slot;
		this.colour = colour;
		this.buttonColour = buttonColour;
	}

	@Override
	public void init() {

		super.init();

		buttonWidth = ((float) width * 0.07F);

		float keybladesX = width * 0.2F;
		float keybladesY = height * 0.175F;

		float keybladesWidth = width * 0.35F;
		float keybladesHeight = height * 0.5972F;

		float detailsX = width * 0.55F;
		float detailsWidth = width * 0.2F;

		float listX = width * 0.21F;
		float listY = height * 0.2546F;

		widgets.clear();

		addRenderableWidget(back = new MenuButton((int) buttonPosX, buttonPosY, (int) buttonWidth, Component.translatable(Strings.Gui_Menu_Back).getString(), MenuButton.ButtonType.BUTTON, false, b -> minecraft.setScreen(new MenuEquipmentScreen())));

		int itemHeight = 15;

		int pos = 0;

		PlayerData playerData = PlayerData.get(minecraft.player);

		ItemStack equippedMagic = playerData.getEquippedMagic(slot);

		String equippedMagicName = (equippedMagic != null && equippedMagic.getItem() instanceof MagicSpellItem) ? equippedMagic.getItem().getDescriptionId() : "---";

		equipped = new MenuColourBox((int) listX, (int) listY + (itemHeight * (pos - 1)), (int) (keybladesWidth - (listX - keybladesX) * 2), Utils.translateToLocal(equippedMagicName), "", buttonColour);

		if (slot >= 0) {

			// Unequip option
			if (!ItemStack.matches(equippedMagic, ItemStack.EMPTY)) {

				if (minecraft.player.getInventory().getFreeSlot() > -1) {

					widgets.add(new MenuSelectMagicButton(ItemStack.EMPTY, minecraft.player.getInventory().getFreeSlot(), (int) listX, (int) listY + (itemHeight * pos++), (int) keybladesWidth - 25, this, buttonColour));
				}
			}

			/*
			 * NORMAL INVENTORY
			 */
			for (int i = 0; i < minecraft.player.getInventory().getContainerSize(); i++) {

				ItemStack stack = minecraft.player.getInventory().getItem(i);

				if (stack.isEmpty()) continue;

				if (!(stack.getItem() instanceof MagicSpellItem)) continue;

				widgets.add(new MenuSelectMagicButton(stack, i, (int) listX, (int) listY + (itemHeight * pos++), (int) keybladesWidth - 25, this, buttonColour));
			}


			/*
			 * MAGIC BAG
			 */
			ItemStack magicBag = ItemStack.EMPTY;

			for (ItemStack stack : minecraft.player.getInventory().items) {
				if (stack.getItem() == ModItems.magicsBag.get()) {
					magicBag = stack;
					break;
				}
			}
			if (!magicBag.isEmpty()) {

				if (magicBag.getCapability(Capabilities.ItemHandler.ITEM) instanceof BagInventory bagInv) {

					for (int i = 0; i < bagInv.getSlots(); i++) {

						ItemStack stack = bagInv.getStackInSlot(i);

						if (stack.isEmpty()) continue;

						if (!(stack.getItem() instanceof MagicSpellItem)) continue;

						int virtualSlot = BAG_OFFSET - i;

						widgets.add(new MenuSelectMagicButton(stack, virtualSlot, (int) listX, (int) listY + (itemHeight * pos++), (int) keybladesWidth - 25, this, buttonColour));
					}
				}
			}
		}

		widgets.forEach(this::addWidget);

		boxL = new MenuBox((int) keybladesX, (int) keybladesY, (int) keybladesWidth, (int) keybladesHeight, 0.6F, colour);

		boxR = new MenuBox((int) detailsX, (int) keybladesY, (int) detailsWidth, (int) keybladesHeight, 0.6F, colour);

		int scrollYPos = (int) listY;

		int listHeight = 0;

		if (!widgets.isEmpty()) {

			listHeight = (widgets.get(widgets.size() - 1).getY() + itemHeight + equipped.getHeight()) - widgets.get(0).getY() + 3;
		}

		scrollBar = new MenuScrollBar(boxL.getX() + boxL.getWidth() - 17, scrollYPos, scrollYPos + (int) keybladesHeight - itemHeight - 8, (int) keybladesHeight - 6, listHeight, true);

		if (scrollBar.isVisible()) {

			widgets.forEach(menuSelectEquipmentButton -> menuSelectEquipmentButton.setWidth((int) keybladesWidth - 10 - scrollBar.getWidth()));
		}

		addRenderableWidget(scrollBar);
	}

	@Override
	public void render(@NotNull GuiGraphics gui, int mouseX, int mouseY, float partialTicks) {

		drawMenuBackground(gui, mouseX, mouseY, partialTicks);

		boxL.renderWidget(gui, mouseX, mouseY, partialTicks);

		boxR.renderWidget(gui, mouseX, mouseY, partialTicks);

		equipped.render(gui, mouseX, mouseY, partialTicks);

		scrollBar.render(gui, mouseX, mouseY, partialTicks);

		back.render(gui, mouseX, mouseY, partialTicks);

		for (MenuSelectMagicButton renderable : widgets) {

			gui.enableScissor(boxL.getX() + 2, scrollBar.getY(), boxL.getX() + boxL.getWidth(), scrollBar.getBottom() + 1);

			renderable.render(gui, mouseX, mouseY, partialTicks);

			gui.disableScissor();

			renderable.renderData(gui, mouseX, mouseY, partialTicks);
		}
	}

	@Override
	public boolean mouseClicked(double mouseX, double mouseY, int mouseButton) {

		scrollBar.mouseClicked(mouseX, mouseY, mouseButton);

		return super.mouseClicked(mouseX, mouseY, mouseButton);
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

	public void updateScroll() {

		widgets.forEach(button -> button.offsetY = (int) scrollBar.scrollOffset);
	}

	@Override
	public boolean mouseScrolled(double mouseX, double mouseY, double deltaX, double deltaY) {

		if (mouseX >= boxL.getX() && mouseX <= scrollBar.getX() + scrollBar.getWidth()) {

			scrollBar.mouseScrolled(mouseX, mouseY, deltaX, deltaY);
		}

		updateScroll();

		return false;
	}
}