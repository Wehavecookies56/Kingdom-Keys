package online.kingdomkeys.kingdomkeys.client.gui.menu;

import java.awt.Color;

import com.mojang.blaze3d.systems.RenderSystem;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.inventory.InventoryScreen;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ResourceLocation;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.capability.IPlayerCapabilities;
import online.kingdomkeys.kingdomkeys.capability.ModCapabilities;
import online.kingdomkeys.kingdomkeys.client.gui.menu.GuiMenuButton.ButtonType;
import online.kingdomkeys.kingdomkeys.client.gui.menu.party.GuiMenu_Party_Leader;
import online.kingdomkeys.kingdomkeys.client.gui.menu.party.GuiMenu_Party_Member;
import online.kingdomkeys.kingdomkeys.client.gui.menu.party.GuiMenu_Party_None;
import online.kingdomkeys.kingdomkeys.client.gui.menu.synthesis.GuiMenu_Synthesis;
import online.kingdomkeys.kingdomkeys.lib.Party;
import online.kingdomkeys.kingdomkeys.lib.Strings;
import online.kingdomkeys.kingdomkeys.lib.Utils;

public class GuiMenu extends GuiMenu_Background {

	public GuiMenu() {
		super("Menu", new Color(0,0,255));
		minecraft = Minecraft.getInstance();
	}

	final int ITEMS = 0, ABILITIES = 1, CUSTOMIZE = 2, PARTY = 3, STATUS = 4, JOURNAL = 5, CONFIG = 6;
	final int SUBMENU_MAIN = 0, SUBMENU_ITEMS = 1;

	GuiMenuButton items, abilities, customize, party, status, journal, config;

	static int munny;

	int submenuIndex = SUBMENU_MAIN;

	protected void action(int buttonID) {
		switch (buttonID) {
		case ITEMS:
			minecraft.displayGuiScreen(new GuiMenu_Items());
			break;
		case ABILITIES:
			minecraft.displayGuiScreen(new GuiMenu_Abilities());
			break;
		case PARTY:
			Party p = ModCapabilities.getWorld(minecraft.world).getPartyFromMember(minecraft.player.getUniqueID());
			if(p == null) {
				minecraft.displayGuiScreen(new GuiMenu_Party_None());
			} else {
				if(p.getLeader().getUUID().equals(minecraft.player.getUniqueID())){
					minecraft.displayGuiScreen(new GuiMenu_Party_Leader());
				} else {
					minecraft.displayGuiScreen(new GuiMenu_Party_Member());
				}
			}
			//minecraft.displayGuiScreen(new GuiMenu_Party_Join("Party"));
			break;
		case STATUS:
			minecraft.displayGuiScreen(new GuiMenu_Status());
			break;
		/*
		 * case CUSTOMIZE: GuiHelper.openCustomize(); break; case STATUS:
		 * GuiHelper.openStatus(); break; case JOURNAL: GuiHelper.openReports(); break;*/
		  case CONFIG:
			minecraft.displayGuiScreen(new GuiMenu_Synthesis()); 
			break;
		 
		}
		updateButtons();
	}

	@Override
	public void init() {
		super.width = width;
		super.height = height;
		super.init();
		float topBarHeight = (float) height * 0.17F;
		int button_itemsY = (int) topBarHeight + 5;
		int button_abilitiesY = button_itemsY + 18;
		int button_customizeY = button_abilitiesY + 18;
		int button_partyY = button_customizeY + 18;
		int button_statusY = button_partyY + 18;
		int button_journalY = button_statusY + 18;
		int button_configY = button_journalY + 18;
		float buttonPosX = (float) width * 0.0526F;
		float buttonWidth = ((float) width * 0.1744F) - 22;

		addButton(items = new GuiMenuButton((int) buttonPosX, button_itemsY, (int) buttonWidth, Utils.translateToLocal(Strings.Gui_Menu_Main_Button_Items), ButtonType.BUTTON, (e) -> {
			action(ITEMS);
		}));
		addButton(abilities = new GuiMenuButton((int) buttonPosX, button_abilitiesY, (int) buttonWidth, Utils.translateToLocal(Strings.Gui_Menu_Main_Button_Abilities), ButtonType.BUTTON, (e) -> {
			action(ABILITIES);
		}));
		addButton(customize = new GuiMenuButton((int) buttonPosX, button_customizeY, (int) buttonWidth, Utils.translateToLocal(Strings.Gui_Menu_Main_Button_Customize), ButtonType.BUTTON, (e) -> {
			action(CUSTOMIZE);
		}));
		addButton(party = new GuiMenuButton((int) buttonPosX, button_partyY, (int) buttonWidth, Utils.translateToLocal(Strings.Gui_Menu_Main_Button_Party), ButtonType.BUTTON, (e) -> {
			action(PARTY);
		}));
		addButton(status = new GuiMenuButton((int) buttonPosX, button_statusY, (int) buttonWidth, Utils.translateToLocal(Strings.Gui_Menu_Main_Button_Status), ButtonType.BUTTON, (e) -> {
			action(STATUS);
		}));
		addButton(journal = new GuiMenuButton((int) buttonPosX, button_journalY, (int) buttonWidth, Utils.translateToLocal(Strings.Gui_Menu_Main_Button_Journal), ButtonType.BUTTON, (e) -> {
			action(JOURNAL);
		}));
		addButton(config = new GuiMenuButton((int) buttonPosX, button_configY, (int) buttonWidth, Utils.translateToLocal(Strings.Gui_Menu_Main_Button_Config), ButtonType.BUTTON, (e) -> {
			action(CONFIG);
		}));
		
		items.setTip("Equip your Keyblade, potions and accessories");
		abilities.setTip("Equip or unequip your abilities");
		customize.setTip("Change the order of your magics");
		party.setTip("Create or check your party");
		status.setTip("Check your stats");
		journal.setTip("You will not find anything here as it won't be done probably");

		updateButtons();
	}

	private void updateButtons() {
		switch (submenuIndex) {
		case SUBMENU_MAIN:
			items.visible = true;
			abilities.visible = true;
			customize.visible = true;
			party.visible = true;
			status.visible = true;
			journal.visible = true;
			config.visible = true;
			party.active = true;
			journal.active = true;
			
			customize.active = false;
			journal.active = false;
			break;
		case SUBMENU_ITEMS:
			items.visible = false;
			abilities.visible = false;
			customize.visible = false;
			party.visible = false;
			status.visible = false;
			journal.visible = false;
			config.visible = false;
			break;
		}
	}

	@Override
	public void render(int mouseX, int mouseY, float partialTicks) {
		super.render(mouseX, mouseY, partialTicks);
		drawPlayer();
	}

	public void drawPlayer() {
		float playerHeight = height * 0.45F;
		float playerPosX = width * 0.5229F;
		float playerPosY = height * 0.7F;
		RenderSystem.pushMatrix();
		{
			PlayerEntity player = minecraft.player;
			// player.getSwingProgress(1);
			RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
			InventoryScreen.drawEntityOnScreen((int) playerPosX, (int) playerPosY, (int) playerHeight / 2, 0, 0, player);
			RenderSystem.color4f(1.0F, 1.0F, 1.0F, 0.75F);
		}
		RenderSystem.popMatrix();
		RenderSystem.pushMatrix();
		
			RenderSystem.color3f(1, 1, 1);
			RenderSystem.translatef(1, 1, 100);
			RenderSystem.enableAlphaTest();
			RenderSystem.enableBlend();
			minecraft.getRenderManager().textureManager.bindTexture(new ResourceLocation(KingdomKeys.MODID, "textures/gui/menu/menu_button.png"));
			int infoBoxWidth = (int) ((width * 0.1385F) - 14); // This might be wrong cuz I had to convert from float to int
			int infoBoxPosX = (int) (width * 0.4354F);
			int infoBoxPosY = (int) (height * 0.54F);
			blit(infoBoxPosX, infoBoxPosY, 123, 67, 11, 22);
			for (int i = 0; i < infoBoxWidth; i++) {
				blit(infoBoxPosX + 11 + i, infoBoxPosY, 135, 67, 1, 22);
			}
			blit(infoBoxPosX + 11 + infoBoxWidth, infoBoxPosY, 137, 67, 3, 22);
			blit(infoBoxPosX, infoBoxPosY + 22, 123, 90, 3, 35);
			for (int i = 0; i < infoBoxWidth + 8; i++) {
				blit(infoBoxPosX + 3 + i, infoBoxPosY + 22, 127, 90, 1, 35);
			}
			blit(infoBoxPosX + 3 + infoBoxWidth + 8, infoBoxPosY + 22, 129, 90, 3, 35);
			RenderSystem.disableAlphaTest();
			RenderSystem.disableBlend();
		RenderSystem.popMatrix();
		RenderSystem.pushMatrix();
		{
			RenderSystem.translatef(2, 2, 100);
			IPlayerCapabilities playerData = ModCapabilities.getPlayer(minecraft.player);
			if (playerData != null) {
				RenderSystem.pushMatrix();
				{
					RenderSystem.translatef((int) infoBoxPosX + 8, (int) infoBoxPosY + ((22 / 2) - (minecraft.fontRenderer.FONT_HEIGHT / 2)), 1);
					// RenderSystem.scale(0.75F, 0.75F, 1);
					drawString(minecraft.fontRenderer, minecraft.player.getDisplayName().getFormattedText(), 0, 0, 0xFFFFFF);
				}
				RenderSystem.popMatrix();
				drawString(minecraft.fontRenderer, "LV: " + playerData.getLevel(), (int) infoBoxPosX + 4, (int) (infoBoxPosY + 26), 0xFFD900);
				drawString(minecraft.fontRenderer, "HP: " + (int) minecraft.player.getHealth() + "/" + (int) minecraft.player.getMaxHealth(), (int) infoBoxPosX + 4, (int) (infoBoxPosY + 26) + minecraft.fontRenderer.FONT_HEIGHT, 0x00FF00);
				drawString(minecraft.fontRenderer, "MP: " + (int) playerData.getMP() + "/" + (int) playerData.getMaxMP(), (int) infoBoxPosX + 4, (int) (infoBoxPosY + 26) + (minecraft.fontRenderer.FONT_HEIGHT * 2), 0x4444FF);
			}
		}
		RenderSystem.popMatrix();
	}

}
