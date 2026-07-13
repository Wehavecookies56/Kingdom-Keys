package online.kingdomkeys.kingdomkeys.client.gui.menu.items.equipment;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import online.kingdomkeys.kingdomkeys.ability.Ability;
import online.kingdomkeys.kingdomkeys.ability.ModAbilities;
import online.kingdomkeys.kingdomkeys.api.item.IKeychain;
import online.kingdomkeys.kingdomkeys.client.gui.elements.MenuBackground;
import online.kingdomkeys.kingdomkeys.client.gui.elements.MenuBox;
import online.kingdomkeys.kingdomkeys.client.gui.elements.MenuColourBox;
import online.kingdomkeys.kingdomkeys.client.gui.elements.buttons.MenuButton;
import online.kingdomkeys.kingdomkeys.client.gui.elements.buttons.MenuScrollBar;
import online.kingdomkeys.kingdomkeys.client.gui.elements.buttons.MenuSelectEquipmentButton;
import online.kingdomkeys.kingdomkeys.data.PlayerData;
import online.kingdomkeys.kingdomkeys.item.KeychainItem;
import online.kingdomkeys.kingdomkeys.lib.Strings;
import online.kingdomkeys.kingdomkeys.util.Utils;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class MenuEquipmentSelectorScreen extends MenuBackground {
	public MenuScrollBar scrollBar;
	MenuBox boxL, boxR;
    Button back;
	MenuColourBox equipped;

	List<MenuSelectEquipmentButton> widgets = new ArrayList<>();

	public ResourceLocation form;

	int buttonColour;
	Color colour;

	public MenuEquipmentSelectorScreen(ResourceLocation form, Color colour, int buttonColour) {
		super(Strings.Gui_Menu_Items_Equipment_Weapon, new Color(0,0,255));
		minecraft = Minecraft.getInstance();
		this.form = form;
		this.colour = colour;
		this.buttonColour = buttonColour;
	}

	@Override
	public void init() {
		super.init();
        buttonWidth = ((float)width * 0.07F);
		float keybladesX = width * 0.1432F;
		float keybladesY = height * 0.175F;
		float keybladesWidth = width * 0.5317F;
		float keybladesHeight = height * 0.5972F;
		float detailsX = width * 0.675F;
		float detailsWidth = width * 0.2F;
		float listX = width * 0.1546F;
		float listY = keybladesY + 18;

		widgets.clear();
        addRenderableWidget(back = new MenuButton((int)buttonPosX, buttonPosY, (int)buttonWidth, Component.translatable(Strings.Gui_Menu_Back).getString(), MenuButton.ButtonType.BUTTON, false, b -> minecraft.setScreen(new MenuEquipmentScreen())));

		int itemHeight = 15;
		
		int pos = 0;
		PlayerData playerData = PlayerData.get(minecraft.player);
		ItemStack equippedKeychain = playerData.getEquippedKeychain(form);
		//If the equipped keychain is a keychain get the keyblade's translation key, otherwise ---
		String equippedKeychainName = (equippedKeychain != null && equippedKeychain.getItem() instanceof KeychainItem) ?  ((KeychainItem) equippedKeychain.getItem()).getKeyblade().getDescriptionId() : "---";

		//Adds the form current keychain (base too as it's DriveForm.NONE)
		String ability = "N/A";
		if(!ItemStack.matches(equippedKeychain, ItemStack.EMPTY)) {
			List<ResourceLocation> abilities = Utils.getKeybladeAbilitiesAtLevel(equippedKeychain.getItem(), ((IKeychain) equippedKeychain.getItem()).toSummon().getKeybladeLevel(equippedKeychain));
			if(!abilities.isEmpty()) {
				Ability a = ModAbilities.registry.get(abilities.get(0));
				ability = Utils.translateToLocal(a.getTranslationKey());
			}
		}

		equipped = new MenuColourBox((int) listX, (int) listY + (itemHeight * (pos-1)), (int) (keybladesWidth - (listX - keybladesX)*2), Utils.translateToLocal(equippedKeychainName),ability, buttonColour);

		if(form != null) {
			if (!ItemStack.matches(playerData.getEquippedKeychain(form), ItemStack.EMPTY)) {// If the form doesn't have an empty slot add it, otherwise it has already been added
				if (minecraft.player.getInventory().getFreeSlot() > -1)
					widgets.add(new MenuSelectEquipmentButton(ItemStack.EMPTY, minecraft.player.getInventory().getFreeSlot(), (int) listX, (int) listY + (itemHeight * pos++), (int) keybladesWidth-17, this, buttonColour));
			}

			for (int i = 0; i < minecraft.player.getInventory().getContainerSize(); i++) {
				if (!ItemStack.matches(minecraft.player.getInventory().getItem(i), ItemStack.EMPTY)) {
					if (minecraft.player.getInventory().getItem(i).getItem() instanceof KeychainItem keychainItem) {
						if (keychainItem.getKeyblade() != null) {
							widgets.add(new MenuSelectEquipmentButton(minecraft.player.getInventory().getItem(i), i, (int) listX, (int) listY + (itemHeight * pos++), (int) keybladesWidth-17, this, buttonColour));
						}
					}
				}
			}

		}
		widgets.forEach(this::addWidget);

		boxL = new MenuBox((int) keybladesX, (int) keybladesY, (int) keybladesWidth, (int) keybladesHeight,0.9F, colour);
		boxR = new MenuBox((int) detailsX, (int) keybladesY, (int) detailsWidth, (int) keybladesHeight,0.9F, colour);

		int scrollYPos = (int)listY;
		int listHeight = 0;

		if(!widgets.isEmpty())
			listHeight = (widgets.get(widgets.size()-1).getY()+itemHeight+equipped.getHeight()) - widgets.get(0).getY()+3;

		scrollBar = new MenuScrollBar(boxL.getX() + boxL.getWidth() - 17, scrollYPos, scrollYPos + (int) keybladesHeight - itemHeight - 8, (int) keybladesHeight - 6,listHeight, true);
		if (scrollBar.isVisible()) {
			widgets.forEach(menuSelectEquipmentButton -> {
				menuSelectEquipmentButton.setWidth((int) keybladesWidth-10-scrollBar.getWidth());
			});
		}
		addRenderableWidget(scrollBar);
	}

	@Override
	public void render(@NotNull GuiGraphics gui, int mouseX, int mouseY, float partialTicks) {
		super.render(gui, mouseX, mouseY, partialTicks);
		boxL.renderWidget(gui, mouseX, mouseY, partialTicks);
		boxR.renderWidget(gui, mouseX, mouseY, partialTicks);
		equipped.render(gui, mouseX, mouseY, partialTicks);
		scrollBar.render(gui, mouseX, mouseY, partialTicks);

		for(MenuSelectEquipmentButton renderable : widgets){
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
