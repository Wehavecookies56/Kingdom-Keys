package online.kingdomkeys.kingdomkeys.client.gui.menu.items.equipment;

import java.awt.Color;
import java.util.List;

import com.mojang.blaze3d.matrix.MatrixStack;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TranslationTextComponent;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
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
		float detailsWidth = width * 0.1817F;
		float listX = width * 0.1546F;
		float listY = height * 0.2546F;


        addButton(back = new MenuButton((int)buttonPosX, buttonPosY, (int)buttonWidth, new TranslationTextComponent(Strings.Gui_Menu_Back).getString(), MenuButton.ButtonType.BUTTON, false, b -> minecraft.displayGuiScreen(new MenuEquipmentScreen())));

		int itemHeight = 15;
		
		int pos = 0;
		IPlayerCapabilities playerData = ModCapabilities.getPlayer(minecraft.player);
		ItemStack equippedKeychain = playerData.getEquippedKeychain(form);
		//If the equipped keychain is a keychain get the keyblade's translation key, otherwise ---
		String equippedKeychainName = (equippedKeychain != null && equippedKeychain.getItem() instanceof KeychainItem) ?  ((KeychainItem) equippedKeychain.getItem()).getKeyblade().getTranslationKey() : "---";
		
		//Adds the form current keychain (base too as it's DriveForm.NONE)
		List<String> abilities = Utils.getKeybladeAbilitiesAtLevel(equippedKeychain.getItem(), ((IKeychain) equippedKeychain.getItem()).toSummon().getKeybladeLevel(equippedKeychain));
		String ability = "N/A";
		if(abilities.size() > 0) {
			Ability a = ModAbilities.registry.getValue(new ResourceLocation(abilities.get(0)));
			ability = Utils.translateToLocal(a.getTranslationKey());
		}
		addButton(new MenuColourBox((int) listX, (int) listY + (itemHeight * (pos-1)), (int) (keybladesWidth - (listX - keybladesX)*2), Utils.translateToLocal(equippedKeychainName),ability, buttonColour));
		if(form != null) {
			if (!ItemStack.areItemStacksEqual(playerData.getEquippedKeychain(form), ItemStack.EMPTY)) {// If the form doesn't have an empty slot add it, otherwise it has already been added
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
		}
		keyblades = new MenuBox((int) keybladesX, (int) keybladesY, (int) keybladesWidth, (int) keybladesHeight, colour);
		details = new MenuBox((int) detailsX, (int) keybladesY, (int) detailsWidth, (int) keybladesHeight, colour);
	}

	@Override
	public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
		drawMenuBackground(matrixStack, mouseX, mouseY, partialTicks);
		keyblades.draw(matrixStack);
		details.draw(matrixStack);
		super.render(matrixStack, mouseX, mouseY, partialTicks);
		minecraft.textureManager.bindTexture(new ResourceLocation(KingdomKeys.MODID, "textures/gui/menu/menu_button.png"));
	}
}
