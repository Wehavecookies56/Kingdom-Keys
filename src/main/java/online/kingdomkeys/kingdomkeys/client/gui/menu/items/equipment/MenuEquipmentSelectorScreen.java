package online.kingdomkeys.kingdomkeys.client.gui.menu.items.equipment;

import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.capability.IPlayerCapabilities;
import online.kingdomkeys.kingdomkeys.capability.ModCapabilities;
import online.kingdomkeys.kingdomkeys.client.gui.elements.MenuBox;
import online.kingdomkeys.kingdomkeys.client.gui.elements.MenuBackground;
import online.kingdomkeys.kingdomkeys.client.gui.elements.buttons.MenuSelectEquipmentButton;
import online.kingdomkeys.kingdomkeys.item.KeychainItem;

import java.awt.Color;

public class MenuEquipmentSelectorScreen extends MenuBackground {

	MenuBox keyblades, details;

	public ResourceLocation form;

	int buttonColour;
	Color colour;

	public MenuEquipmentSelectorScreen(ResourceLocation form, Color colour, int buttonColour) {
		super("Keychain", new Color(0,0,255));
		drawSeparately = true;
		minecraft = Minecraft.getInstance();
		this.form = form;
		this.colour = colour;
		this.buttonColour = buttonColour;
	}	

	@Override
	public void init() {
		super.init();
		float keybladesX = width * 0.1432F;
		float keybladesY = height * 0.175F;
		float keybladesWidth = width * 0.5317F;
		float keybladesHeight = height * 0.5972F;
		float detailsX = width * 0.675F;
		float detailsWidth = width * 0.1817F;
		float listX = width * 0.1546F;
		float listY = height * 0.2546F;

		int itemHeight = 14;

		int pos = 0;
		IPlayerCapabilities playerData = ModCapabilities.getPlayer(minecraft.player);
		addButton(new MenuSelectEquipmentButton(playerData.getEquippedKeychain(form), -1, (int) listX, (int) listY + (itemHeight * pos++), 150, this, buttonColour));
		if (!ItemStack.areItemStacksEqual(playerData.getEquippedKeychain(form), ItemStack.EMPTY)) {
			if (minecraft.player.inventory.getFirstEmptyStack() > -1)
				addButton(new MenuSelectEquipmentButton(ItemStack.EMPTY, minecraft.player.inventory.getFirstEmptyStack(), (int) listX, (int) listY + (itemHeight * pos++), 150, this, buttonColour));
		}
		for (int i = 0; i < minecraft.player.inventory.getSizeInventory(); i++) {
			if (!ItemStack.areItemStacksEqual(minecraft.player.inventory.getStackInSlot(i), ItemStack.EMPTY)) {
				if (minecraft.player.inventory.getStackInSlot(i).getItem() instanceof KeychainItem) {
					addButton(new MenuSelectEquipmentButton(minecraft.player.inventory.getStackInSlot(i), i, (int) listX, (int) listY + (itemHeight * pos++), 150, this, buttonColour));
				}
			}

		}
		keyblades = new MenuBox((int) keybladesX, (int) keybladesY, (int) keybladesWidth, (int) keybladesHeight, colour);
		details = new MenuBox((int) detailsX, (int) keybladesY, (int) detailsWidth, (int) keybladesHeight, colour);
	}

	@Override
	public void render(int mouseX, int mouseY, float partialTicks) {
		drawMenuBackground(mouseX, mouseY, partialTicks);
		keyblades.draw();
		details.draw();
		super.render(mouseX, mouseY, partialTicks);
		minecraft.textureManager.bindTexture(new ResourceLocation(KingdomKeys.MODID, "textures/gui/menu/menu_button.png"));
	}
}
