package online.kingdomkeys.kingdomkeys.client.gui.menu;

import java.awt.Color;

import org.joml.Quaternionf;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.capability.IPlayerCapabilities;
import online.kingdomkeys.kingdomkeys.capability.ModCapabilities;
import online.kingdomkeys.kingdomkeys.client.gui.elements.MenuBackground;
import online.kingdomkeys.kingdomkeys.client.gui.elements.buttons.MenuButton;
import online.kingdomkeys.kingdomkeys.client.gui.elements.buttons.MenuButton.ButtonType;
import online.kingdomkeys.kingdomkeys.client.gui.menu.abilities.MenuAbilitiesScreen;
import online.kingdomkeys.kingdomkeys.client.gui.menu.config.MenuConfigScreen;
import online.kingdomkeys.kingdomkeys.client.gui.menu.customize.MenuCustomizeScreen;
import online.kingdomkeys.kingdomkeys.client.gui.menu.items.MenuItemsScreen;
import online.kingdomkeys.kingdomkeys.client.gui.menu.journal.MenuJournalScreen;
import online.kingdomkeys.kingdomkeys.client.gui.menu.party.GuiMenu_Party_Leader;
import online.kingdomkeys.kingdomkeys.client.gui.menu.party.GuiMenu_Party_Member;
import online.kingdomkeys.kingdomkeys.client.gui.menu.party.GuiMenu_Party_None;
import online.kingdomkeys.kingdomkeys.client.gui.menu.status.MenuStatusScreen;
import online.kingdomkeys.kingdomkeys.lib.Party;
import online.kingdomkeys.kingdomkeys.lib.Strings;
import online.kingdomkeys.kingdomkeys.util.Utils;

public class MenuScreen extends MenuBackground {

	public MenuScreen() {
		super("Menu", new Color(0,0,255));
		minecraft = Minecraft.getInstance();
	}

	final int ITEMS = 0, ABILITIES = 1, CUSTOMIZE = 2, PARTY = 3, STATUS = 4, JOURNAL = 5, CONFIG = 6;
	final int SUBMENU_MAIN = 0, SUBMENU_ITEMS = 1;

	MenuButton items, abilities, customize, party, status, journal, config;

	static int munny;
	protected void action(int buttonID) {
		switch (buttonID) {
		case ITEMS:
			minecraft.setScreen(new MenuItemsScreen());
			break;
		case ABILITIES:
			minecraft.setScreen(new MenuAbilitiesScreen());
			break;
		case PARTY:
			Party p = ModCapabilities.getWorld(minecraft.level).getPartyFromMember(minecraft.player.getUUID());
			if(p == null) {
				minecraft.setScreen(new GuiMenu_Party_None());
			} else {
				if(p.getLeader().getUUID().equals(minecraft.player.getUUID())){
					minecraft.setScreen(new GuiMenu_Party_Leader());
				} else {
					minecraft.setScreen(new GuiMenu_Party_Member());
				}
			}
			break;
		case STATUS:
			minecraft.setScreen(new MenuStatusScreen());
			break;

		case CUSTOMIZE:
			minecraft.setScreen(new MenuCustomizeScreen());
			break;

		case JOURNAL:
			minecraft.setScreen(new MenuJournalScreen());
			break;
			
		case CONFIG:
			minecraft.setScreen(new MenuConfigScreen());
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
		//int button_partyY = button_abilitiesY + 18;
		int button_statusY = button_partyY + 18;
		int button_journalY = button_statusY + 18;
		int button_configY = button_journalY + 18;
		float buttonPosX = (float) width * 0.03F;
		float buttonWidth = ((float) width * 0.1744F) - 22;

		addRenderableWidget(items = new MenuButton((int) buttonPosX, button_itemsY, (int) buttonWidth, (Strings.Gui_Menu_Main_Button_Items), ButtonType.BUTTON, true, (e) -> {
			action(ITEMS);
		}));
		addRenderableWidget(abilities = new MenuButton((int) buttonPosX, button_abilitiesY, (int) buttonWidth, (Strings.Gui_Menu_Main_Button_Abilities), ButtonType.BUTTON, true, (e) -> {
			action(ABILITIES);
		}));
		addRenderableWidget(customize = new MenuButton((int) buttonPosX, button_customizeY, (int) buttonWidth, (Strings.Gui_Menu_Main_Button_Customize), ButtonType.BUTTON, true, (e) -> {
			action(CUSTOMIZE);
		}));
		addRenderableWidget(party = new MenuButton((int) buttonPosX, button_partyY, (int) buttonWidth, (Strings.Gui_Menu_Main_Button_Party), ButtonType.BUTTON, true, (e) -> {
			action(PARTY);
		}));
		addRenderableWidget(status = new MenuButton((int) buttonPosX, button_statusY, (int) buttonWidth, (Strings.Gui_Menu_Main_Button_Status), ButtonType.BUTTON, true, (e) -> {
			action(STATUS);
		}));
		addRenderableWidget(journal = new MenuButton((int) buttonPosX, button_journalY, (int) buttonWidth, (Strings.Gui_Menu_Main_Button_Journal), ButtonType.BUTTON, true, (e) -> {
			action(JOURNAL);
		}));
		addRenderableWidget(config = new MenuButton((int) buttonPosX, button_configY, (int) buttonWidth, (Strings.Gui_Menu_Main_Button_Config), ButtonType.BUTTON, true, (e) -> {
			action(CONFIG);
		}));

		updateButtons();
	}

	private void updateButtons() {
		items.visible = true;
		abilities.visible = true;
		customize.visible = true;
		party.visible = true;
		status.visible = true;
		journal.visible = true;
		config.visible = true;
		
		customize.active = true;
		journal.active = true;
	}

	@Override
	public void render(PoseStack matrixStack, int mouseX, int mouseY, float partialTicks) {
		super.render(matrixStack, mouseX, mouseY, partialTicks);
		drawPlayer(matrixStack);
	}

	public void drawPlayer(PoseStack matrixStack) {
		float playerHeight = height * 0.45F;
		float playerPosX = width * 0.5229F;
		float playerPosY = height * 0.7F;
		matrixStack.pushPose();
		{
			Player player = minecraft.player;
			// player.getSwingProgress(1);
			RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
			InventoryScreen.renderEntityInInventory(matrixStack, (int) playerPosX, (int) playerPosY, (int) playerHeight / 2, new Quaternionf(), new Quaternionf(), player);
			RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 0.75F);
		}
		matrixStack.popPose();
		matrixStack.pushPose();
		
		RenderSystem.setShaderColor(1, 1, 1, 1);
			matrixStack.translate(1, 1, 100);
			
			RenderSystem.enableBlend();
			RenderSystem.setShaderTexture(0, new ResourceLocation(KingdomKeys.MODID, "textures/gui/menu/menu_button.png"));
			int infoBoxWidth = (int) ((width * 0.1385F) - 14); // This might be wrong cuz I had to convert from float to int
			int infoBoxPosX = (int) (width * 0.4354F);
			int infoBoxPosY = (int) (height * 0.54F);
			blit(matrixStack, infoBoxPosX, infoBoxPosY, 123, 67, 11, 22);
			for (int i = 0; i < infoBoxWidth; i++) {
				blit(matrixStack, infoBoxPosX + 11 + i, infoBoxPosY, 135, 67, 1, 22);
			}
			blit(matrixStack, infoBoxPosX + 11 + infoBoxWidth, infoBoxPosY, 137, 67, 3, 22);
			blit(matrixStack, infoBoxPosX, infoBoxPosY + 22, 123, 90, 3, 35);
			for (int i = 0; i < infoBoxWidth + 8; i++) {
				blit(matrixStack, infoBoxPosX + 3 + i, infoBoxPosY + 22, 127, 90, 1, 35);
			}
			blit(matrixStack, infoBoxPosX + 3 + infoBoxWidth + 8, infoBoxPosY + 22, 129, 90, 3, 35);
			
			RenderSystem.disableBlend();
		matrixStack.popPose();
		matrixStack.pushPose();
		{
			matrixStack.translate(2, 2, 100);
			IPlayerCapabilities playerData = ModCapabilities.getPlayer(minecraft.player);
			if (playerData != null) {
				matrixStack.pushPose();
				{
					matrixStack.translate((int) infoBoxPosX + 8, (int) infoBoxPosY + ((22 / 2) - (minecraft.font.lineHeight / 2)), 1);
					// matrixStack.scale(0.75F, 0.75F, 1);
					drawString(matrixStack, minecraft.font, minecraft.player.getDisplayName().getString(), 0, 0, 0xFFFFFF);
				}
				matrixStack.popPose();
				
				drawString(matrixStack,minecraft.font, Utils.translateToLocal(Strings.Gui_Menu_Status_Level)+": " + playerData.getLevel(), (int) infoBoxPosX + 4, (int) (infoBoxPosY + 26), 0xFFD900);
				drawString(matrixStack, minecraft.font, Utils.translateToLocal(Strings.Gui_Menu_Status_HP)+": " + (int) minecraft.player.getHealth() + "/" + (int) minecraft.player.getMaxHealth(), (int) infoBoxPosX + 4, (int) (infoBoxPosY + 26) + minecraft.font.lineHeight, 0x00FF00);
				drawString(matrixStack, minecraft.font, Utils.translateToLocal(Strings.Gui_Menu_Status_MP)+": " + (int) playerData.getMP() + "/" + (int) playerData.getMaxMP(), (int) infoBoxPosX + 4, (int) (infoBoxPosY + 26) + (minecraft.font.lineHeight * 2), 0x4444FF);
			}
		}
		matrixStack.popPose();
	}

}
