package online.kingdomkeys.kingdomkeys.client.gui.menu.party;

import java.awt.Color;

import javax.annotation.Nullable;

import org.jetbrains.annotations.NotNull;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundSource;
import online.kingdomkeys.kingdomkeys.capability.IPlayerCapabilities;
import online.kingdomkeys.kingdomkeys.capability.IWorldCapabilities;
import online.kingdomkeys.kingdomkeys.capability.ModCapabilities;
import online.kingdomkeys.kingdomkeys.client.gui.GuiHelper;
import online.kingdomkeys.kingdomkeys.client.gui.elements.MenuBackground;
import online.kingdomkeys.kingdomkeys.client.gui.elements.buttons.MenuButton;
import online.kingdomkeys.kingdomkeys.client.gui.elements.buttons.MenuButton.ButtonType;
import online.kingdomkeys.kingdomkeys.client.sound.ModSounds;
import online.kingdomkeys.kingdomkeys.config.ModConfigs;
import online.kingdomkeys.kingdomkeys.lib.Party;
import online.kingdomkeys.kingdomkeys.lib.Party.Member;
import online.kingdomkeys.kingdomkeys.lib.Strings;
import online.kingdomkeys.kingdomkeys.network.PacketHandler;
import online.kingdomkeys.kingdomkeys.network.cts.CSPartyPromote;
import online.kingdomkeys.kingdomkeys.util.Utils;

public class GuiMenu_Party_Promote extends MenuBackground {
	
	MenuButton back;
		
	final IPlayerCapabilities playerData = ModCapabilities.getPlayer(minecraft.player);
	IWorldCapabilities worldData;
	Party party;
	
	MenuButton[] players = new MenuButton[ModConfigs.partyMembersLimit];
	
	public GuiMenu_Party_Promote() {
		super(Strings.Gui_Menu_Party_Leader_Promote, new Color(0,0,255));
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
			refreshMembers(null);
			break;
		}
		
		if(string.startsWith("member:")) {
			String[] data = string.split(":");
			String name = data[1];
			if(name.equals(minecraft.player.getDisplayName().getString())) {
				minecraft.player.playSound(ModSounds.error.get());
				refreshMembers(null);
				return;
			}
			Member member = null;
			for(Member m : party.getMembers()) {
				if(m.getUsername().equals(name)){
					member = m;
				}
			}
			if(member != null) {
				PacketHandler.sendToServer(new CSPartyPromote(party, member.getUUID()));
				member.setIsLeader(!member.isLeader());
			}
			refreshMembers(null);

			minecraft.level.playSound(minecraft.player, minecraft.player.blockPosition(), ModSounds.menu_in.get(), SoundSource.MASTER, 1.0f, 1.0f);
			
		}
		updateButtons();
	}

	private void updateButtons() {
		refreshMembers(null);
	}

	private void refreshMembers(@Nullable GuiGraphics gui) {
		worldData = ModCapabilities.getWorld(minecraft.level);
		
		float topBarHeight = (float) height * 0.17F;
		int button_statsY = (int) topBarHeight + 5;
		float buttonWidth = ((float) width * 0.1744F) - 20;

		for(int i = 1;i<renderables.size();i++) {
			
			if(!((AbstractWidget)renderables.get(i)).getMessage().getString().startsWith("Refresh")) {
				renderables.remove(i);
			}
		}
		
		//Show the buttons to join public parties
		party = worldData.getPartyFromMember(minecraft.player.getUUID());
		if(party == null) {
			GuiHelper.openMenu();
			return;
		} else {			
			if(!party.getMember(minecraft.player.getUUID()).isLeader()) {
				minecraft.setScreen(new GuiMenu_Party_Member());
				return;
			}
			
			for(int i = 0; i < party.getMembers().size(); i++) {
				addRenderableWidget(players[i] = new MenuButton((int)(width * 0.3F), button_statsY + (i * 18), (int)(buttonWidth * 2), party.getMembers().get(i).getUsername(), ButtonType.BUTTON, (e) -> { action("member:"+e.getMessage().getString()); }));
				if(gui != null) {
					if(party.getMembers().get(i).isLeader()) {
					gui.drawString(minecraft.font, "Leader", (int)(width * 0.3F+buttonWidth*2)+30,  button_statsY + (i * 18)+6, 0xFF9900);
					}
				}
			}
		}
		
		
	
	}	

	@Override
	public void init() {
		//TODO request packet to sync other players data
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
		worldData = ModCapabilities.getWorld(minecraft.level);
		party = worldData.getPartyFromMember(minecraft.player.getUUID());
		refreshMembers(gui);

	}
}
