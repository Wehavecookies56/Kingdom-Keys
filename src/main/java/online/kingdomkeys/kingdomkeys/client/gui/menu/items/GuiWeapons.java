package online.kingdomkeys.kingdomkeys.client.gui.menu.items;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.widget.Widget;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.client.gui.GuiHelper;
import online.kingdomkeys.kingdomkeys.client.gui.menu.GuiMenuAbilitiesButton;
import online.kingdomkeys.kingdomkeys.client.gui.menu.GuiMenu_Background;
import online.kingdomkeys.kingdomkeys.item.KeychainItem;

public class GuiWeapons extends GuiMenu_Background {

	GuiElementBox keyblades, details;

	//List<GuiKeychainListItem> keychains = new ArrayList<>();
	GuiKeychainListItem[] keychains = new GuiKeychainListItem[36];


	int chainSlot, colour, buttonColour;

	public GuiWeapons(int chainSlot, int colour, int buttonColour) {
		super("Keychain", new java.awt.Color(0xFF0000));
		minecraft = Minecraft.getInstance();
		this.chainSlot = chainSlot;
		this.colour = colour;
		this.buttonColour = buttonColour;
	}	

	@Override
	public void init() {
		//keychains.clear();
		super.width = width;
		super.height = height;
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

		//BiMap<Integer, ItemStack> keychainItems = HashBiMap.create();
		
		/*if(minecraft.player.inventory.getFirstEmptyStack() > -1){
			keychainItems.put(minecraft.player.inventory.getFirstEmptyStack(), minecraft.player.inventory.getStackInSlot(minecraft.player.inventory.getFirstEmptyStack()));
		}*/
		int pos = 0;
		for (int i = 0; i < minecraft.player.inventory.getSizeInventory(); i++) {
			if (!ItemStack.areItemStacksEqual(minecraft.player.inventory.getStackInSlot(i), ItemStack.EMPTY)) {
				if (minecraft.player.inventory.getStackInSlot(i).getItem() instanceof KeychainItem) {
				//	keychainItems.put(i, minecraft.player.inventory.getStackInSlot(i)); //Add keychain to list
					addButton(keychains[i] = new GuiKeychainListItem(minecraft.player.inventory.getStackInSlot(i), i, (int) listX, (int) listY + (itemHeight * pos++), 150, this, buttonColour, (e) -> {action("");}));
				}
			}

		}
		/*for (int i = 0; i < new ArrayList<>(keychainItems.keySet()).size(); i++) {
			addButton(new GuiKeychainListItem(new ArrayList<>(keychainItems.values()).get(i), new ArrayList<>(keychainItems.keySet()).get(i), (int) listX, (int) listY + i + (itemHeight * i), 100, this, buttonColour, (e) -> {action("");}));
		}*/
		keyblades = new GuiElementBox((int) keybladesX, (int) keybladesY, (int) keybladesWidth, (int) keybladesHeight, colour);
		details = new GuiElementBox((int) detailsX, (int) keybladesY, (int) detailsWidth, (int) keybladesHeight, colour);
		//super.init();
	}

	private void action(String string) {
		System.out.println("T2");
		
	}

	@Override
	public void render(int mouseX, int mouseY, float partialTicks) {
		super.render(mouseX, mouseY, partialTicks);
		//background.drawBiomeDim();
		//background.drawMunnyTime();
		keyblades.draw();
		details.draw();
		for (Widget i : buttons) {
			i.render(mouseX, mouseY, partialTicks);
		}
		minecraft.textureManager.bindTexture(new ResourceLocation(KingdomKeys.MODID, "textures/gui/menu/menu_button.png"));

		//super.drawScreen(mouseX, mouseY, partialTicks);
	}

	/*@Override
	protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
		if (mouseButton == 0) {
			for (GuiKeychainListItem i : keychains) {
				i.mousePressed(mc, mouseX, mouseY);
			}
		} else {
			GuiHelper.openMenu_Items_Player();
		}
		super.mouseClicked(mouseX, mouseY, mouseButton);
	}*/
}
