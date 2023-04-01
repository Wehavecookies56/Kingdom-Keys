package online.kingdomkeys.kingdomkeys.client.gui.menu.items.equipment;

import java.awt.Color;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.capability.IPlayerCapabilities;
import online.kingdomkeys.kingdomkeys.capability.ModCapabilities;
import online.kingdomkeys.kingdomkeys.client.gui.elements.MenuBackground;
import online.kingdomkeys.kingdomkeys.client.gui.elements.MenuBox;
import online.kingdomkeys.kingdomkeys.client.gui.elements.MenuColourBox;
import online.kingdomkeys.kingdomkeys.client.gui.elements.buttons.MenuButton;
import online.kingdomkeys.kingdomkeys.client.gui.elements.buttons.MenuSelectShotlockButton;
import online.kingdomkeys.kingdomkeys.lib.Strings;
import online.kingdomkeys.kingdomkeys.shotlock.ModShotlocks;
import online.kingdomkeys.kingdomkeys.shotlock.Shotlock;
import online.kingdomkeys.kingdomkeys.util.Utils;

public class MenuShotlockSelectorScreen extends MenuBackground {

	MenuBox keyblades;//, details;
    Button back;

	int buttonColour;
	Color colour;

	public MenuShotlockSelectorScreen(Color colour, int buttonColour) {
		super(Strings.Gui_Menu_Items_Equipment_Weapon, new Color(0,0,255));
		drawSeparately = true;
		minecraft = Minecraft.getInstance();
		this.colour = colour;
		this.buttonColour = buttonColour;
	}	

	@Override
	public void init() {
		super.init();
        buttonWidth = ((float)width * 0.07F);
		float keybladesX = width * 0.2432F;
		float keybladesY = height * 0.175F;
		float keybladesWidth = width * 0.5F;
		float keybladesHeight = height * 0.5972F;
		float detailsX = width * 0.675F;
		float detailsWidth = width * 0.1817F;
		float listX = width * 0.2546F;
		float listY = height * 0.2546F;


        addRenderableWidget(back = new MenuButton((int)buttonPosX, buttonPosY, (int)buttonWidth, Component.translatable(Strings.Gui_Menu_Back).getString(), MenuButton.ButtonType.BUTTON, false, b -> minecraft.setScreen(new MenuEquipmentScreen())));

		int itemHeight = 15;

		
		int pos = 0;
		IPlayerCapabilities playerData = ModCapabilities.getPlayer(minecraft.player);
		Shotlock equippedShotlock = ModShotlocks.registry.get().getValue(new ResourceLocation(playerData.getEquippedShotlock()));//playerData.getEquippedKeychain(form);
		//If the equipped keychain is a keychain get the keyblade's translation key, otherwise ---
		String equippedShotlockName = equippedShotlock == null ? "---" : equippedShotlock.getTranslationKey();
		
		//Adds the form current keychain (base too as it's DriveForm.NONE)
		addRenderableWidget(new MenuColourBox((int) listX, (int) listY + (itemHeight * (pos-1)), (int) (keybladesWidth - (listX - keybladesX)*2), Utils.translateToLocal(equippedShotlockName),equippedShotlock == null ? "N/A" : "Max: "+equippedShotlock.getMaxLocks(), buttonColour));
		
		if(equippedShotlock != null)
			addRenderableWidget(new MenuSelectShotlockButton("", (int) listX, (int) listY + (itemHeight * pos++), 150, this, buttonColour));

		for(String sName : Utils.getSortedShotlocks(playerData.getShotlockList())) {
			if(equippedShotlock == null || !sName.equals(equippedShotlock.getName())) {
				addRenderableWidget(new MenuSelectShotlockButton(sName, (int) listX, (int) listY + (itemHeight * pos++), 150, this, buttonColour));
			}
		}

		keyblades = new MenuBox((int) keybladesX, (int) keybladesY, (int) keybladesWidth, (int) keybladesHeight, colour);
		//details = new MenuBox((int) detailsX, (int) keybladesY, (int) detailsWidth, (int) keybladesHeight, colour);
	}

	@Override
	public void render(PoseStack matrixStack, int mouseX, int mouseY, float partialTicks) {
		drawMenuBackground(matrixStack, mouseX, mouseY, partialTicks);
		keyblades.renderWidget(matrixStack, mouseX, mouseY, partialTicks);
		//details.draw(matrixStack);
		super.render(matrixStack, mouseX, mouseY, partialTicks);
		RenderSystem.setShaderTexture(0, new ResourceLocation(KingdomKeys.MODID, "textures/gui/menu/menu_button.png"));
	}
}
