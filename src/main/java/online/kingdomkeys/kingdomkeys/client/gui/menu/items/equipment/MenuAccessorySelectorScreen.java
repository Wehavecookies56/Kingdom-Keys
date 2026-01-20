package online.kingdomkeys.kingdomkeys.client.gui.menu.items.equipment;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import online.kingdomkeys.kingdomkeys.client.gui.elements.MenuBackground;
import online.kingdomkeys.kingdomkeys.client.gui.elements.MenuBox;
import online.kingdomkeys.kingdomkeys.client.gui.elements.MenuColourBox;
import online.kingdomkeys.kingdomkeys.client.gui.elements.buttons.MenuButton;
import online.kingdomkeys.kingdomkeys.client.gui.elements.buttons.MenuScrollBar;
import online.kingdomkeys.kingdomkeys.client.gui.elements.buttons.MenuSelectAccessoryButton;
import online.kingdomkeys.kingdomkeys.data.PlayerData;
import online.kingdomkeys.kingdomkeys.item.KKAccessoryItem;
import online.kingdomkeys.kingdomkeys.lib.Strings;
import online.kingdomkeys.kingdomkeys.util.Utils;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MenuAccessorySelectorScreen extends MenuBackground {
	public MenuScrollBar scrollBar;
	MenuBox boxL, boxR;
    Button back;
	MenuColourBox equipped;

	List<MenuSelectAccessoryButton> widgets = new ArrayList<>();

	int buttonColour;
	Color colour;
	public int slot = -1;

	public Map<KKAccessoryItem,Integer> addedAccessoriesList = new HashMap<KKAccessoryItem, Integer>();
	
	public MenuAccessorySelectorScreen(int slot, Color colour, int buttonColour) {
		super(Strings.Gui_Menu_Items_Equipment_Accessories, new Color(0,0,255));
		drawSeparately = true;
		minecraft = Minecraft.getInstance();
		this.slot = slot;
		this.colour = colour;
		this.buttonColour = buttonColour;
	}	

	@Override
	public void init() {
		super.init();
        buttonWidth = ((float)width * 0.07F);
		float keybladesX = width * 0.2F;
		float keybladesY = height * 0.175F;
		float keybladesWidth = width * 0.35F;
		float keybladesHeight = height * 0.5972F;
		float detailsX = width * 0.55F;
		float detailsWidth = width * 0.2F;
		float listX = width * 0.21F;
		float listY = height * 0.2546F;

		widgets.clear();
		addedAccessoriesList.clear();

        addRenderableWidget(back = new MenuButton((int)buttonPosX, buttonPosY, (int)buttonWidth, Component.translatable(Strings.Gui_Menu_Back).getString(), MenuButton.ButtonType.BUTTON, false, b -> minecraft.setScreen(new MenuEquipmentScreen())));

		int itemHeight = 15;

		int pos = 0;
		PlayerData playerData = PlayerData.get(minecraft.player);
		ItemStack equippedAccessory = playerData.getEquippedAccessory(slot);
		//If the equipped item is an item get the translation key, otherwise ---
		String equippedAccessoryName = (equippedAccessory != null && equippedAccessory.getItem() instanceof KKAccessoryItem) ? equippedAccessory.getItem().getDescriptionId() : "---";
		
		//Adds the form current keychain (base too as it's DriveForm.NONE)
		equipped = new MenuColourBox((int) listX, (int) listY + (itemHeight * (pos-1)), (int) (keybladesWidth - (listX - keybladesX)*2), Utils.translateToLocal(equippedAccessoryName),"", buttonColour);
		if(slot >= 0) {
			if(!ItemStack.matches(equippedAccessory, ItemStack.EMPTY)) {
				if (minecraft.player.getInventory().getFreeSlot() > -1) {
					widgets.add(new MenuSelectAccessoryButton(ItemStack.EMPTY, minecraft.player.getInventory().getFreeSlot(), (int) listX, (int) listY + (itemHeight * pos++), (int) keybladesWidth-17, this, buttonColour));
				}
			}
			
			for (int i = 0; i < minecraft.player.getInventory().getContainerSize(); i++) {
				if (!ItemStack.matches(minecraft.player.getInventory().getItem(i), ItemStack.EMPTY)) {
					if (minecraft.player.getInventory().getItem(i).getItem() instanceof KKAccessoryItem accessory) {
                        if(addedAccessoriesList.containsKey(accessory)) {
							int amount = addedAccessoriesList.get(accessory);
							addedAccessoriesList.replace(accessory, amount+1);
						} else {
							widgets.add(new MenuSelectAccessoryButton(minecraft.player.getInventory().getItem(i), i, (int) listX, (int) listY + (itemHeight * pos++), (int) keybladesWidth-17, this, buttonColour));
							addedAccessoriesList.put((KKAccessoryItem) minecraft.player.getInventory().getItem(i).getItem(), 1);
						}
					}
				}
			}
		}
		widgets.forEach(this::addWidget);

		boxL = new MenuBox((int) keybladesX, (int) keybladesY, (int) keybladesWidth, (int) keybladesHeight, 0.6F,colour);
		boxR = new MenuBox((int) detailsX, (int) keybladesY, (int) detailsWidth, (int) keybladesHeight,0.6F, colour);

		int scrollYPos = (int)listY;
		int listHeight = 0;

		if(!widgets.isEmpty())
			listHeight = (widgets.get(widgets.size()-1).getY()+itemHeight+equipped.getHeight()) - widgets.get(0).getY()+3;

		scrollBar = new MenuScrollBar(boxL.getX() + boxL.getWidth() - 17, scrollYPos, scrollYPos + (int) keybladesHeight - itemHeight - 8, (int) keybladesHeight - 6,listHeight);
		if (scrollBar.isVisible()) {
			widgets.forEach(menuSelectEquipmentButton -> {
				menuSelectEquipmentButton.setWidth((int) keybladesWidth-10-scrollBar.getWidth());
			});
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
		for(MenuSelectAccessoryButton renderable : widgets){
			gui.enableScissor(boxL.getX()+2,scrollBar.getY(), boxL.getX()+ boxL.getWidth(),scrollBar.getBottom()+1); //Arbitrary number to hide the cut one
			renderable.render(gui,mouseX,mouseY,partialTicks);
			gui.disableScissor();
			renderable.renderData(gui,mouseX,mouseY,partialTicks);
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
		widgets.forEach(button -> {
			button.offsetY = (int) scrollBar.scrollOffset;
		});
	}

	@Override
	public boolean mouseScrolled(double mouseX, double mouseY, double deltaX, double deltaY) {
		if(mouseX >= boxL.getX() && mouseX <= scrollBar.getX()+ scrollBar.getWidth())
			scrollBar.mouseScrolled(mouseX, mouseY, deltaX, deltaY);

		updateScroll();
		return false;
	}
}
