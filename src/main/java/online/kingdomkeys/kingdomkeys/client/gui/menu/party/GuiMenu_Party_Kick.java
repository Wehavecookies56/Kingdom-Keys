package online.kingdomkeys.kingdomkeys.client.gui.menu.party;

import java.awt.Color;

import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.sounds.SoundSource;
import online.kingdomkeys.kingdomkeys.capability.IPlayerCapabilities;
import online.kingdomkeys.kingdomkeys.capability.IWorldCapabilities;
import online.kingdomkeys.kingdomkeys.capability.ModCapabilities;
import online.kingdomkeys.kingdomkeys.client.gui.elements.MenuBackground;
import online.kingdomkeys.kingdomkeys.client.gui.elements.buttons.MenuButton;
import online.kingdomkeys.kingdomkeys.client.gui.elements.buttons.MenuButtonBase;
import online.kingdomkeys.kingdomkeys.client.gui.elements.buttons.MenuButton.ButtonType;
import online.kingdomkeys.kingdomkeys.client.sound.ModSounds;
import online.kingdomkeys.kingdomkeys.lib.Party;
import online.kingdomkeys.kingdomkeys.lib.Party.Member;
import online.kingdomkeys.kingdomkeys.lib.Strings;
import online.kingdomkeys.kingdomkeys.network.PacketHandler;
import online.kingdomkeys.kingdomkeys.network.cts.CSPartyLeave;
import online.kingdomkeys.kingdomkeys.util.Utils;

public class GuiMenu_Party_Kick extends MenuBackground {
	
	MenuButton back;
		
	final IPlayerCapabilities playerData = ModCapabilities.getPlayer(minecraft.player);
	IWorldCapabilities worldData;
	Party party;
	
	MenuButton[] players = new MenuButton[Party.PARTY_LIMIT];
	
	public GuiMenu_Party_Kick() {
		super(Strings.Gui_Menu_Party_Leader_Kick, new Color(0,0,255));
		drawPlayerInfo = true;
		worldData = ModCapabilities.getWorld(minecraft.level);
	}

	protected void action(String string) {
		//Clear list as it should never be seen unless in phase 2
		for(int i=0;i<players.length;i++) {
			if(players[i] != null) {
				players[i].visible = false;
			}
		}
		
		switch(string) {
		case "back":
			minecraft.level.playSound(minecraft.player, minecraft.player.blockPosition(), ModSounds.menu_in.get(), SoundSource.MASTER, 1.0f, 1.0f);
			minecraft.setScreen(new GuiMenu_Party_Leader());			
			break;
		case "refresh":
			refreshMembers();
			break;
		}
		
		if(string.startsWith("member:")) {
			String[] data = string.split(":");
			String name = data[1];
			Member member = null;
			for(Member m : party.getMembers()) {
				if(m.getUsername().equals(name)){
					member = m;
				}
			}
			if(member != null) {
				PacketHandler.sendToServer(new CSPartyLeave(party, member.getUUID()));
				party.removeMember(member.getUUID());
			}
			refreshMembers();

			minecraft.level.playSound(minecraft.player, minecraft.player.blockPosition(), ModSounds.menu_in.get(), SoundSource.MASTER, 1.0f, 1.0f);
			//minecraft.displayGuiScreen(new GuiMenu_Party_Member("Party Member"));
			
		}
		updateButtons();
	}

	private void updateButtons() {
		refreshMembers();
	}

	private void refreshMembers() {
		worldData = ModCapabilities.getWorld(minecraft.level);
		
		float topBarHeight = (float) height * 0.17F;
		int button_statsY = (int) topBarHeight + 5;
		float buttonWidth = ((float) width * 0.1744F) - 20;

		for(int i = 1;i<children().size();i++) {
			if(children().get(i) instanceof MenuButtonBase) {
				if(!((MenuButtonBase)children().get(i)).getMessage().getString().startsWith("Refresh")) {
					children().remove(i);
				}
			}
		}
		
		//Show the buttons to join public parties
		party = worldData.getPartyFromMember(minecraft.player.getUUID());
		for(int i = 1; i < party.getMembers().size(); i++) {
			addWidget(players[i] = new MenuButton((int)(width * 0.3F), button_statsY + ((i-1) * 18), (int)(buttonWidth * 2), party.getMembers().get(i).getUsername(), ButtonType.BUTTON, (e) -> { action("member:"+e.getMessage().getString()); }));
		}
	
	}	

	@Override
	public void init() {
		//TODO request packet to sync other players data
		super.width = width;
		super.height = height;
		super.init();
		this.clearWidgets();
				
		float topBarHeight = (float) height * 0.17F;
		int button_statsY = (int) topBarHeight + 5;
		float buttonPosX = (float) width * 0.03F;
		float buttonWidth = ((float) width * 0.1744F) - 20;

		addWidget(back = new MenuButton((int) buttonPosX, button_statsY + (0 * 18), (int) buttonWidth, Utils.translateToLocal(Strings.Gui_Menu_Back), ButtonType.BUTTON, (e) -> { action("back"); }));
		
		updateButtons();
	}

	@Override
	public void render(PoseStack matrixStack, int mouseX, int mouseY, float partialTicks) {
		super.render(matrixStack, mouseX, mouseY, partialTicks);
		worldData = ModCapabilities.getWorld(minecraft.level);
		party = worldData.getPartyFromMember(minecraft.player.getUUID());
		refreshMembers();
	}
}
