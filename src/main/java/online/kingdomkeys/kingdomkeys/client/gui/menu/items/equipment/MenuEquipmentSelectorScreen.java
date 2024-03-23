package online.kingdomkeys.kingdomkeys.client.gui.menu.items.equipment;

import java.awt.Color;
import java.util.List;

import org.jetbrains.annotations.NotNull;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import online.kingdomkeys.kingdomkeys.ability.Ability;
import online.kingdomkeys.kingdomkeys.ability.ModAbilities;
import online.kingdomkeys.kingdomkeys.api.item.IKeychain;
import online.kingdomkeys.kingdomkeys.capability.IPlayerCapabilities;
import online.kingdomkeys.kingdomkeys.capability.ModCapabilities;
import online.kingdomkeys.kingdomkeys.client.gui.elements.MenuBackground;
import online.kingdomkeys.kingdomkeys.client.gui.elements.MenuBox;
import online.kingdomkeys.kingdomkeys.client.gui.elements.MenuColourBox;
import online.kingdomkeys.kingdomkeys.client.gui.elements.buttons.MenuButton;
import online.kingdomkeys.kingdomkeys.client.gui.elements.buttons.MenuSelectEquipmentButton;
import online.kingdomkeys.kingdomkeys.item.KeychainItem;
import online.kingdomkeys.kingdomkeys.lib.Strings;
import online.kingdomkeys.kingdomkeys.util.Utils;

public class MenuEquipmentSelectorScreen extends MenuBackground {

	MenuBox keyblades, details;
    Button back;

	public ResourceLocation form;

	int buttonColour;
	Color colour;

	public MenuEquipmentSelectorScreen(ResourceLocation form, Color colour, int buttonColour) {
		super(Strings.Gui_Menu_Items_Equipment_Weapon, new Color(0,0,255));
		drawSeparately = true;
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
		float listY = height * 0.2546F;

        addRenderableWidget(back = new MenuButton((int)buttonPosX, buttonPosY, (int)buttonWidth, Component.translatable(Strings.Gui_Menu_Back).getString(), MenuButton.ButtonType.BUTTON, false, b -> minecraft.setScreen(new MenuEquipmentScreen())));

		int itemHeight = 15;
		
		int pos = 0;
		IPlayerCapabilities playerData = ModCapabilities.getPlayer(minecraft.player);
		ItemStack equippedKeychain = playerData.getEquippedKeychain(form);
		//If the equipped keychain is a keychain get the keyblade's translation key, otherwise ---
		String equippedKeychainName = (equippedKeychain != null && equippedKeychain.getItem() instanceof KeychainItem) ?  ((KeychainItem) equippedKeychain.getItem()).getKeyblade().getDescriptionId() : "---";
		
		//Adds the form current keychain (base too as it's DriveForm.NONE)
		String ability = "N/A";
		if(!ItemStack.matches(equippedKeychain, ItemStack.EMPTY)) {
			List<String> abilities = Utils.getKeybladeAbilitiesAtLevel(equippedKeychain.getItem(), ((IKeychain) equippedKeychain.getItem()).toSummon().getKeybladeLevel(equippedKeychain));
			if(abilities.size() > 0) {
				Ability a = ModAbilities.registry.get().getValue(new ResourceLocation(abilities.get(0)));
				ability = Utils.translateToLocal(a.getTranslationKey());
			}
		}

		addRenderableWidget(new MenuColourBox((int) listX, (int) listY + (itemHeight * (pos-1)), (int) (keybladesWidth - (listX - keybladesX)*2), Utils.translateToLocal(equippedKeychainName),ability, buttonColour));
		if(form != null) {
			if (!ItemStack.matches(playerData.getEquippedKeychain(form), ItemStack.EMPTY)) {// If the form doesn't have an empty slot add it, otherwise it has already been added
				if (minecraft.player.getInventory().getFreeSlot() > -1)
					addRenderableWidget(new MenuSelectEquipmentButton(ItemStack.EMPTY, minecraft.player.getInventory().getFreeSlot(), (int) listX, (int) listY + (itemHeight * pos++), 150, this, buttonColour));
			}
			for (int i = 0; i < minecraft.player.getInventory().getContainerSize(); i++) {
				if (!ItemStack.matches(minecraft.player.getInventory().getItem(i), ItemStack.EMPTY)) {
					if (minecraft.player.getInventory().getItem(i).getItem() instanceof KeychainItem keychainItem) {
						if (keychainItem.getKeyblade() != null) {
							addRenderableWidget(new MenuSelectEquipmentButton(minecraft.player.getInventory().getItem(i), i, (int) listX, (int) listY + (itemHeight * pos++), 150, this, buttonColour));
						}
					}
				}
			}
		}
		keyblades = new MenuBox((int) keybladesX, (int) keybladesY, (int) keybladesWidth, (int) keybladesHeight, colour);
		details = new MenuBox((int) detailsX, (int) keybladesY, (int) detailsWidth, (int) keybladesHeight, colour);
	}

	@Override
	public void render(@NotNull GuiGraphics gui, int mouseX, int mouseY, float partialTicks) {
		drawMenuBackground(gui, mouseX, mouseY, partialTicks);
		keyblades.renderWidget(gui, mouseX, mouseY, partialTicks);
		details.renderWidget(gui, mouseX, mouseY, partialTicks);
		super.render(gui, mouseX, mouseY, partialTicks);
	}
}
