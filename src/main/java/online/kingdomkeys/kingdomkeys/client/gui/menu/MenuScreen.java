package online.kingdomkeys.kingdomkeys.client.gui.menu;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.neoforged.neoforge.common.NeoForge;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.api.event.client.MenuButtonRegisterEvent;
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
import online.kingdomkeys.kingdomkeys.data.PlayerData;
import online.kingdomkeys.kingdomkeys.data.WorldData;
import online.kingdomkeys.kingdomkeys.lib.Party;
import online.kingdomkeys.kingdomkeys.lib.Party.Member;
import online.kingdomkeys.kingdomkeys.lib.Strings;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.util.ArrayList;

public class MenuScreen extends MenuBackground {

	public MenuScreen(PlayerData playerData) {
		super(Strings.Gui_Menu_Main_Title, new Color(0,0,255));
		minecraft = Minecraft.getInstance();
		this.playerData = playerData;
	}

    private final ArrayList<MenuButton> menuButtons = new ArrayList<>();

    public enum buttons {
		ITEMS, ABILITIES, CUSTOMIZE, PARTY, STATUS, JOURNAL, CONFIG, STYLES
    }

	PlayerData playerData;

	MenuButton items, abilities, customize, party, status, journal, config, style;

	protected void action(buttons buttonID) {
		switch (buttonID) {
			case ITEMS -> minecraft.setScreen(new MenuItemsScreen());
			case ABILITIES -> minecraft.setScreen(new MenuAbilitiesScreen());
			case PARTY -> {
				Party p = WorldData.getClient().getPartyFromMember(minecraft.player.getUUID());
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
			case JOURNAL -> {
				if (KingdomKeys.patchouliLoaded) {
					online.kingdomkeys.kingdomkeys.integration.patchouli.PatchouliIntegration.openJournal();
				} else {
					minecraft.setScreen(new MenuJournalScreen());
				}
			}

			case CONFIG -> minecraft.setScreen(new MenuConfigScreen());
			case STYLES -> minecraft.setScreen(new StylesMenu(playerData));
		}
		updateButtons();
	}

	@Override
	public void init() {
		super.init();
		float topBarHeight = (float) height * 0.17F;
		int startY = (int)topBarHeight + 5;
		int pos = 0;

		float buttonX = (float) width * 0.03F;
		float buttonWidth = ((float) width * 0.1744F) - 22;
        menuButtons.clear();
        menuButtons.add(items = new MenuButton((int) buttonX, startY + 18 * pos++, (int) buttonWidth, Strings.Gui_Menu_Main_Button_Items, ButtonType.BUTTON, true, e -> action(buttons.ITEMS)));
        menuButtons.add(abilities = new MenuButton((int) buttonX, startY + 18 * pos++, (int) buttonWidth, Strings.Gui_Menu_Main_Button_Abilities, ButtonType.BUTTON, true, e -> action(buttons.ABILITIES)));
        menuButtons.add(customize = new MenuButton((int) buttonX, startY + 18 * pos++, (int) buttonWidth, Strings.Gui_Menu_Main_Button_Customize, ButtonType.BUTTON, true, e -> action(buttons.CUSTOMIZE)));
        menuButtons.add(party = new MenuButton((int) buttonX, startY + 18 * pos++, (int) buttonWidth, Strings.Gui_Menu_Main_Button_Party, ButtonType.BUTTON, true, e -> action(buttons.PARTY)));
        menuButtons.add(status = new MenuButton((int) buttonX, startY + 18 * pos++, (int) buttonWidth, Strings.Gui_Menu_Main_Button_Status, ButtonType.BUTTON, true, e -> action(buttons.STATUS)));
        menuButtons.add(journal = new MenuButton((int) buttonX, startY + 18 * pos++, (int) buttonWidth, Strings.Gui_Menu_Main_Button_Journal, ButtonType.BUTTON, true, e -> action(buttons.JOURNAL)));

        if (KingdomKeys.efmLoaded) {
            menuButtons.add(style = new MenuButton((int) buttonX, startY + 18 * pos++, (int) buttonWidth, Strings.Gui_Menu_Main_Button_Style, ButtonType.BUTTON, true, e -> action(buttons.STYLES)));
        }

        menuButtons.add(config = new MenuButton((int) buttonX, startY + 18 * pos++, (int) buttonWidth, Strings.Gui_Menu_Main_Button_Config, ButtonType.BUTTON, true, e -> action(buttons.CONFIG)));

        NeoForge.EVENT_BUS.post(new MenuButtonRegisterEvent(this, menuButtons));

        for (MenuButton button : menuButtons) {
            addRenderableWidget(button);
        }

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
		if(KingdomKeys.efmLoaded)
			style.visible = true;
		customize.active = true;
		journal.active = true;
	}

	@Override
	public void render(@NotNull GuiGraphics gui, int mouseX, int mouseY, float partialTicks) {
		super.render(gui, mouseX, mouseY, partialTicks);

		PoseStack pose = gui.pose();

		pose.pushPose();

		// reset cualquier transformación heredada
		pose.translate(0, 0, 0);
		Party.Member m = new Party.Member(minecraft.player.getUUID(), minecraft.player.getDisplayName().getString());
		drawPlayer(gui, null,0, m);

		pose.popPose();	}
}
