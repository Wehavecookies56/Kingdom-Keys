package online.kingdomkeys.kingdomkeys.client.gui.menu.party;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundSource;
import online.kingdomkeys.kingdomkeys.client.ClientUtils;
import online.kingdomkeys.kingdomkeys.client.gui.elements.MenuBackground;
import online.kingdomkeys.kingdomkeys.client.gui.elements.MenuBox;
import online.kingdomkeys.kingdomkeys.client.gui.elements.buttons.MenuButton;
import online.kingdomkeys.kingdomkeys.client.gui.elements.buttons.MenuButton.ButtonType;
import online.kingdomkeys.kingdomkeys.client.sound.ModSounds;
import online.kingdomkeys.kingdomkeys.config.ModConfigs;
import online.kingdomkeys.kingdomkeys.data.PlayerData;
import online.kingdomkeys.kingdomkeys.data.WorldData;
import online.kingdomkeys.kingdomkeys.lib.Party;
import online.kingdomkeys.kingdomkeys.lib.Party.Member;
import online.kingdomkeys.kingdomkeys.lib.Strings;
import online.kingdomkeys.kingdomkeys.network.PacketHandler;
import online.kingdomkeys.kingdomkeys.network.cts.CSOpenMenu;
import online.kingdomkeys.kingdomkeys.network.cts.CSPartyPromote;
import online.kingdomkeys.kingdomkeys.util.Utils;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.awt.*;

public class GuiMenu_Party_Promote extends MenuBackground {
	MenuBox box;
	MenuButton back;
		
	WorldData worldData;
	Party party;
	
	MenuButton[] players = new MenuButton[ModConfigs.SERVER.partyMembersLimit.get()];
	
	public GuiMenu_Party_Promote() {
		super(Strings.Gui_Menu_Party_Leader_Promote, new Color(0,0,255));
		drawPlayerInfo = true;
		worldData = WorldData.getClient();
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
		worldData = WorldData.getClient();
		
		float topBarHeight = (float) height * 0.17F;
		int button_statsY = (int) topBarHeight + 5;
		float buttonWidth = (box.getWidth() - 40);

		for(int i = 1;i<renderables.size();i++) {
			
			if(!((AbstractWidget)renderables.get(i)).getMessage().getString().startsWith("Refresh")) {
				renderables.remove(i);
			}
		}
		
		//Show the buttons to join public parties
		party = worldData.getPartyFromMember(minecraft.player.getUUID());
		if(party == null) {
			PacketHandler.sendToServer(new CSOpenMenu());
        } else {
			if(!party.getMember(minecraft.player.getUUID()).isLeader()) {
				minecraft.setScreen(new GuiMenu_Party_Member());
				return;
			}

			for(int i = 0; i < party.getMembers().size(); i++) {
				addRenderableWidget(players[i] = new MenuButton(box.getX()+10, button_statsY + (i * 18), (int)(buttonWidth), party.getMembers().get(i).getUsername(), ButtonType.ROUNDBUTTON, (e) -> { action("member:"+e.getMessage().getString()); }));
				if(gui != null) {
					if(party.getMembers().get(i).isLeader()) {
						gui.drawString(minecraft.font, "Leader", box.getX()+box.getWidth()-minecraft.font.width("Leader")-15,  button_statsY + (i * 18)+6, 0xFF9900);
					}
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
		party = worldData.getPartyFromMember(minecraft.player.getUUID());

		gui.pose().pushPose();
		{
			float scale = 1.5F;
			gui.pose().scale(scale, scale, 1);
			gui.drawString(minecraft.font, Component.literal("["+party.getMembers().size()+"/"+party.getSize()+"] "+party.getName()).withStyle(ClientUtils.KK_Font_EXP), (int) (topLeftBar.getWidth() / scale + topGap) + 5, 10, 0xFF9900);
		}
		gui.pose().popPose();

		refreshMembers(gui);
	}
}
