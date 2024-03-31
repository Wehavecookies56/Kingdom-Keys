package online.kingdomkeys.kingdomkeys.client.gui.menu;

import java.awt.Color;

import org.jetbrains.annotations.NotNull;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.capability.IPlayerCapabilities;
import online.kingdomkeys.kingdomkeys.capability.ModCapabilities;
import online.kingdomkeys.kingdomkeys.client.ClientUtils;
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
import online.kingdomkeys.kingdomkeys.client.gui.menu.styles.StylesMenu;
import online.kingdomkeys.kingdomkeys.lib.Party;
import online.kingdomkeys.kingdomkeys.lib.Party.Member;
import online.kingdomkeys.kingdomkeys.lib.Strings;
import online.kingdomkeys.kingdomkeys.util.Utils;

public class MenuScreen extends MenuBackground {

	public MenuScreen() {
		super("Menu", new Color(0,0,255));
		minecraft = Minecraft.getInstance();
	}
	

	public enum buttons {
		ITEMS, ABILITIES, CUSTOMIZE, PARTY, STATUS, JOURNAL, CONFIG, STYLES;
	}

	MenuButton items, abilities, customize, party, status, journal, config, style;

	final ResourceLocation texture = new ResourceLocation(KingdomKeys.MODID, "textures/gui/menu/menu_button.png");

	protected void action(buttons buttonID) {
		switch (buttonID) {
			case ITEMS -> minecraft.setScreen(new MenuItemsScreen());
			case ABILITIES -> minecraft.setScreen(new MenuAbilitiesScreen());
			case PARTY -> {
				Party p = ModCapabilities.getWorld(minecraft.level).getPartyFromMember(minecraft.player.getUUID());
				if (p == null) {
					minecraft.setScreen(new GuiMenu_Party_None());
				} else {
					boolean isLeader = false;
					for(Member leader : p.getLeaders()) {
						if(leader.getUUID().equals(minecraft.player.getUUID())) {
							isLeader = true;
							break;
						}
					}
					if(isLeader) {
						minecraft.setScreen(new GuiMenu_Party_Leader());
					} else {
						minecraft.setScreen(new GuiMenu_Party_Member());
					}
				}
			}
			case STATUS -> minecraft.setScreen(new MenuStatusScreen());
			case CUSTOMIZE -> minecraft.setScreen(new MenuCustomizeScreen());
			case JOURNAL -> minecraft.setScreen(new MenuJournalScreen());
			case CONFIG -> minecraft.setScreen(new MenuConfigScreen());
			case STYLES -> minecraft.setScreen(new StylesMenu());
		}
		updateButtons();
	}

	@Override
	public void init() {
		super.width = width;
		super.height = height;
		super.init();
		float topBarHeight = (float) height * 0.17F;
		int start = (int)topBarHeight + 5;
		int pos = 0;

		float buttonPosX = (float) width * 0.03F;
		float buttonWidth = ((float) width * 0.1744F) - 22;

		addRenderableWidget(items = new MenuButton((int) buttonPosX, start, (int) buttonWidth, (Strings.Gui_Menu_Main_Button_Items), ButtonType.BUTTON, true, (e) -> {
			action(buttons.ITEMS);
		}));
		addRenderableWidget(abilities = new MenuButton((int) buttonPosX, start + 18 * ++pos, (int) buttonWidth, (Strings.Gui_Menu_Main_Button_Abilities), ButtonType.BUTTON, true, (e) -> {
			action(buttons.ABILITIES);
		}));
		addRenderableWidget(customize = new MenuButton((int) buttonPosX, start + 18 * ++pos, (int) buttonWidth, (Strings.Gui_Menu_Main_Button_Customize), ButtonType.BUTTON, true, (e) -> {
			action(buttons.CUSTOMIZE);
		}));
		addRenderableWidget(party = new MenuButton((int) buttonPosX, start + 18 * ++pos, (int) buttonWidth, (Strings.Gui_Menu_Main_Button_Party), ButtonType.BUTTON, true, (e) -> {
			action(buttons.PARTY);
		}));
		addRenderableWidget(status = new MenuButton((int) buttonPosX, start + 18 * ++pos, (int) buttonWidth, (Strings.Gui_Menu_Main_Button_Status), ButtonType.BUTTON, true, (e) -> {
			action(buttons.STATUS);
		}));
		addRenderableWidget(journal = new MenuButton((int) buttonPosX, start + 18 * ++pos, (int) buttonWidth, (Strings.Gui_Menu_Main_Button_Journal), ButtonType.BUTTON, true, (e) -> {
			action(buttons.JOURNAL);
		}));
		if (KingdomKeys.efmLoaded)
			addRenderableWidget(style = new MenuButton((int) buttonPosX, start + 18 * ++pos, (int) buttonWidth, (Strings.Gui_Menu_Main_Button_Style), ButtonType.BUTTON, true, e -> action(buttons.STYLES)));
		addRenderableWidget(config = new MenuButton((int) buttonPosX, start + 18 * ++pos, (int) buttonWidth, (Strings.Gui_Menu_Main_Button_Config), ButtonType.BUTTON, true, (e) -> {
			action(buttons.CONFIG);
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
		if (KingdomKeys.efmLoaded)
			style.visible = true;
		customize.active = true;
		journal.active = true;
	}

	@Override
	public void render(@NotNull GuiGraphics gui, int mouseX, int mouseY, float partialTicks) {
		super.render(gui, mouseX, mouseY, partialTicks);
		drawPlayer(gui);
	}
	
	public void drawPlayer(GuiGraphics gui) {
		PoseStack matrixStack = gui.pose();
		//PoseStack ps2 = matrixStack;
		float playerHeight = height * 0.45F;
		float playerPosX = width * 0.5229F;
		float playerPosY = height * 0.7F;
		IPlayerCapabilities playerData = ModCapabilities.getPlayer(minecraft.player);
		if (playerData != null) {
			matrixStack.pushPose();
			{
				Player player = minecraft.player;
				ClientUtils.renderPlayerNoAnims(matrixStack, (int) playerPosX, (int) playerPosY, (int) playerHeight / 2, 0, 0, player);
			}
			matrixStack.popPose();
			matrixStack.pushPose();
			RenderSystem.setShaderColor(1, 1, 1, 1);
			matrixStack.translate(1, 1, 100);

			RenderSystem.enableBlend();
			int infoBoxWidth = (int) ((width * 0.1385F) - 14); // This might be wrong cuz I had to convert from float to int
			int infoBoxPosX = (int) (width * 0.4354F);
			int infoBoxPosY = (int) (height * 0.54F);
			gui.blit(texture, infoBoxPosX, infoBoxPosY, 123, 67, 11, 22);
			for (int i = 0; i < infoBoxWidth; i++) {
				gui.blit(texture, infoBoxPosX + 11 + i, infoBoxPosY, 135, 67, 1, 22);
			}
			gui.blit(texture, infoBoxPosX + 11 + infoBoxWidth, infoBoxPosY, 137, 67, 3, 22);
			gui.blit(texture, infoBoxPosX, infoBoxPosY + 22, 123, 90, 3, 35);
			for (int i = 0; i < infoBoxWidth + 8; i++) {
				gui.blit(texture, infoBoxPosX + 3 + i, infoBoxPosY + 22, 127, 90, 1, 35);
			}
			gui.blit(texture, infoBoxPosX + 3 + infoBoxWidth + 8, infoBoxPosY + 22, 129, 90, 3, 35);

			RenderSystem.disableBlend();
			matrixStack.popPose();
			matrixStack.pushPose();
			{
				matrixStack.translate(2, 2, 100);

				matrixStack.pushPose();
				{
					matrixStack.translate((int) infoBoxPosX + 8, (int) infoBoxPosY + ((22 / 2) - (minecraft.font.lineHeight / 2)), 1);
					// matrixStack.scale(0.75F, 0.75F, 1);
					gui.drawString(minecraft.font, minecraft.player.getDisplayName().getString(), 0, 0, 0xFFFFFF);
				}
				matrixStack.popPose();

				gui.drawString(minecraft.font, Utils.translateToLocal(Strings.Gui_Menu_Status_Level) + ": " + playerData.getLevel(), (int) infoBoxPosX + 4, (int) (infoBoxPosY + 26), 0xFFD900);
				gui.drawString(minecraft.font, Utils.translateToLocal(Strings.Gui_Menu_Status_HP) + ": " + (int) minecraft.player.getHealth() + "/" + (int) minecraft.player.getMaxHealth(), (int) infoBoxPosX + 4, (int) (infoBoxPosY + 26) + minecraft.font.lineHeight, 0x00FF00);
				gui.drawString(minecraft.font, Utils.translateToLocal(Strings.Gui_Menu_Status_MP) + ": " + (int) playerData.getMP() + "/" + (int) playerData.getMaxMP(), (int) infoBoxPosX + 4, (int) (infoBoxPosY + 26) + (minecraft.font.lineHeight * 2), 0x4444FF);

			}
			matrixStack.popPose();
		}
	}

}
