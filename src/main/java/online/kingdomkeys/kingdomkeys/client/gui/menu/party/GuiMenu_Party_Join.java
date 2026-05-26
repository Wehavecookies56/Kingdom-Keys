package online.kingdomkeys.kingdomkeys.client.gui.menu.party;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.sounds.SoundSource;
import online.kingdomkeys.kingdomkeys.client.gui.elements.MenuBackground;
import online.kingdomkeys.kingdomkeys.client.gui.elements.MenuBox;
import online.kingdomkeys.kingdomkeys.client.gui.elements.buttons.MenuButton;
import online.kingdomkeys.kingdomkeys.client.gui.elements.buttons.MenuButton.ButtonType;
import online.kingdomkeys.kingdomkeys.client.sound.ModSounds;
import online.kingdomkeys.kingdomkeys.data.PlayerData;
import online.kingdomkeys.kingdomkeys.data.WorldData;
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
	MenuBox box;
	PlayerData playerData = PlayerData.get(minecraft.player);
	WorldData worldData;
	
	MenuButton[] parties = new MenuButton[100];
	
	public GuiMenu_Party_Join() {
		super(Strings.Gui_Menu_Party_Join, new Color(0,0,255));
		drawPlayerInfo = true;
		worldData = WorldData.getClient();
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
					p.addMember(minecraft.player.getUUID(), minecraft.player.getGameProfile().getName());
	
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
		playerData = PlayerData.get(minecraft.player);
		List<String> privateParties = playerData.getPartiesInvited();
		
		worldData = WorldData.getClient();

		float topBarHeight = (float) height * 0.17F;
		int button_statsY = (int) topBarHeight + 5;
		float buttonWidth = (box.getWidth() - 40);

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
                addRenderableWidget(parties[c] = new MenuButton(box.getX() + 10, button_statsY + (c++ * 18), (int) (buttonWidth), "(P) [" + p.getMembers().size() + "/" + p.getSize() + "] " + p.getName(), ButtonType.ROUNDBUTTON, (e) -> {
                    action("party:" + e.getMessage().getString());
                }));
            }
        }

		//Show the buttons to join public parties
		List<Party> partiesList = worldData.getParties();
        for (Party p : partiesList) {
            if (p != null && !p.getPriv()) {
                if (!privateParties.contains(p.getName())) {
                    addRenderableWidget(parties[c] = new MenuButton(box.getX() + 10, button_statsY + (c++ * 18), (int) (buttonWidth), "[" + p.getMembers().size() + "/" + p.getSize() + "] " + p.getName(), ButtonType.ROUNDBUTTON, (e) -> {
                        action("party:" + e.getMessage().getString());
                    }));
                }
            }
        }
	}

	@Override
	public void init() {
		super.init();
		this.renderables.clear();
				
		float topBarHeight = (float) height * 0.17F;
		int button_statsY = (int) topBarHeight + 5;
		float buttonPosX = (float) width * 0.03F;
		float buttonWidth = ((float) width * 0.1744F) - 20;
		box = new MenuBox((int)(width*0.25F), (int)topBarHeight, (int)(width*0.3F), (int) middleHeight,0.8F, new Color(255,128,255));
		addRenderableWidget(back = new MenuButton((int) buttonPosX, button_statsY, (int) buttonWidth, Utils.translateToLocal(Strings.Gui_Menu_Back), ButtonType.BUTTON, (e) -> { action("back"); }));
		
		updateButtons();
	}

	@Override
	public void render(@NotNull GuiGraphics gui, int mouseX, int mouseY, float partialTicks) {
		box.render(gui, mouseX, mouseY, partialTicks);
		super.render(gui, mouseX, mouseY, partialTicks);
		worldData = WorldData.getClient();
		refreshParties();
	}
	
	
}
