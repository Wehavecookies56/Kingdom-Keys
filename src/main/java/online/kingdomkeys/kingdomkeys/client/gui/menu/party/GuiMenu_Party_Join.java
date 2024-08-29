package online.kingdomkeys.kingdomkeys.client.gui.menu.party;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.sounds.SoundSource;
import online.kingdomkeys.kingdomkeys.data.ModData;
import online.kingdomkeys.kingdomkeys.client.gui.elements.MenuBackground;
import online.kingdomkeys.kingdomkeys.client.gui.elements.buttons.MenuButton;
import online.kingdomkeys.kingdomkeys.client.gui.elements.buttons.MenuButton.ButtonType;
import online.kingdomkeys.kingdomkeys.client.sound.ModSounds;
import online.kingdomkeys.kingdomkeys.lib.Party;
import online.kingdomkeys.kingdomkeys.lib.Strings;
import online.kingdomkeys.kingdomkeys.network.PacketHandler;
import online.kingdomkeys.kingdomkeys.network.cts.CSPartyAddMember;
import online.kingdomkeys.kingdomkeys.util.Utils;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.util.List;

public class GuiMenu_Party_Join extends MenuBackground {
	MenuButton back;
		
	IPlayerData playerData = ModData.getPlayer(minecraft.player);
	IWorldCapabilities worldData;
	
	MenuButton[] parties = new MenuButton[100];
	
	public GuiMenu_Party_Join() {
		super(Strings.Gui_Menu_Party_Join, new Color(0,0,255));
		drawPlayerInfo = true;
		worldData = ModData.getWorld(minecraft.level);
	}

	protected void action(String string) {
		//Clear list as it should never be seen unless in phase 2
        for (MenuButton party : parties) {
            if (party != null) {
                party.visible = false;
            }
        }
		
		switch(string) {
		case "back":
			minecraft.level.playSound(minecraft.player, minecraft.player.blockPosition(), ModSounds.menu_in.get(), SoundSource.MASTER, 1.0f, 1.0f);
			minecraft.setScreen(new GuiMenu_Party_None());			
			break;
		}
		
		if(string.startsWith("party:")) {
			String[] data = string.split(":");
			String partyName = data[1].substring(data[1].indexOf("]")+2);
			Party p = worldData.getPartyFromName(partyName);
			if(p != null) {
				if(p.getMembers().size() < p.getSize()) {
					PacketHandler.sendToServer(new CSPartyAddMember(p, minecraft.player));
					p.addMember(minecraft.player.getUUID(), minecraft.player.getDisplayName().getString());
	
					minecraft.level.playSound(minecraft.player, minecraft.player.blockPosition(), ModSounds.menu_in.get(), SoundSource.MASTER, 1.0f, 1.0f);
					minecraft.setScreen(new GuiMenu_Party_Member());
				}
			}
		}
		updateButtons();
	}

	private void updateButtons() {
		refreshParties();
	}

	private void refreshParties() {
		playerData = ModData.getPlayer(minecraft.player);
		List<String> privateParties = playerData.getPartiesInvited();
		
		worldData = ModData.getWorld(minecraft.level);

		float topBarHeight = (float) height * 0.17F;
		int button_statsY = (int) topBarHeight + 5;
		float buttonWidth = ((float) width * 0.1744F) - 20;

		for(int i = 0;i<renderables.size();i++) {
			if(((AbstractWidget)renderables.get(i)).getMessage().getString().startsWith("[") || ((AbstractWidget)renderables.get(i)).getMessage().getString().startsWith("(P) [")) {
				renderables.remove(i);
			}
		}
		
		//Show private parties
		int c = 0;
        for (String privateParty : privateParties) {
            Party p = worldData.getPartyFromName(privateParty);
            if (p != null) {
                addRenderableWidget(parties[c] = new MenuButton((int) (width * 0.3F), button_statsY + (c++ * 18), (int) (buttonWidth * 2), "(P) [" + p.getMembers().size() + "/" + p.getSize() + "] " + p.getName(), ButtonType.BUTTON, (e) -> {
                    action("party:" + e.getMessage().getString());
                }));
            }
        }

		//Show the buttons to join public parties
		List<Party> partiesList = worldData.getParties();
        for (Party p : partiesList) {
            if (p != null && !p.getPriv()) {
                if (!privateParties.contains(p.getName())) {//TODO test this xD
                    addRenderableWidget(parties[c] = new MenuButton((int) (width * 0.3F), button_statsY + (c++ * 18), (int) (buttonWidth * 2), "[" + p.getMembers().size() + "/" + p.getSize() + "] " + p.getName(), ButtonType.BUTTON, (e) -> {
                        action("party:" + e.getMessage().getString());
                    }));
                }
            }
        }
	}

	@Override
	public void init() {
		super.width = width;
		super.height = height;
		super.init();
		this.renderables.clear();
				
		float topBarHeight = (float) height * 0.17F;
		int button_statsY = (int) topBarHeight + 5;
		float buttonPosX = (float) width * 0.03F;
		float buttonWidth = ((float) width * 0.1744F) - 20;

		addRenderableWidget(back = new MenuButton((int) buttonPosX, button_statsY + (0 * 18), (int) buttonWidth, Utils.translateToLocal(Strings.Gui_Menu_Back), ButtonType.BUTTON, (e) -> { action("back"); }));
		
		updateButtons();
	}

	@Override
	public void render(@NotNull GuiGraphics gui, int mouseX, int mouseY, float partialTicks) {
		super.render(gui, mouseX, mouseY, partialTicks);
		worldData = ModData.getWorld(minecraft.level);
		refreshParties();
	}
	
	
}
