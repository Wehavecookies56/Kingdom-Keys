package online.kingdomkeys.kingdomkeys.client.gui.menu.party;

import java.awt.Color;
import java.util.UUID;

import com.mojang.blaze3d.matrix.MatrixStack;

import net.minecraft.util.SoundCategory;
import online.kingdomkeys.kingdomkeys.capability.IPlayerCapabilities;
import online.kingdomkeys.kingdomkeys.capability.IWorldCapabilities;
import online.kingdomkeys.kingdomkeys.capability.ModCapabilities;
import online.kingdomkeys.kingdomkeys.client.gui.elements.buttons.MenuButton;
import online.kingdomkeys.kingdomkeys.client.gui.elements.MenuBackground;
import online.kingdomkeys.kingdomkeys.client.gui.elements.buttons.MenuButton.ButtonType;
import online.kingdomkeys.kingdomkeys.client.sound.ModSounds;
import online.kingdomkeys.kingdomkeys.lib.Party;
import online.kingdomkeys.kingdomkeys.lib.Strings;
import online.kingdomkeys.kingdomkeys.util.Utils;
import online.kingdomkeys.kingdomkeys.network.PacketHandler;
import online.kingdomkeys.kingdomkeys.network.cts.CSPartyInvite;

public class GuiMenu_Party_Invite extends MenuBackground {

	boolean priv = false;
	byte pSize = Party.PARTY_LIMIT;
	
	MenuButton back;
		
	final IPlayerCapabilities playerData = ModCapabilities.getPlayer(minecraft.player);
	IWorldCapabilities worldData;
	Party party;
	
	MenuButton[] players = new MenuButton[100];
	
	public GuiMenu_Party_Invite() {
		super(Strings.Gui_Menu_Party_Leader_Invite, new Color(0,0,255));
		drawPlayerInfo = true;
		worldData = ModCapabilities.getWorld(minecraft.world);
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
			minecraft.world.playSound(minecraft.player, minecraft.player.getPosition(), ModSounds.menu_in.get(), SoundCategory.MASTER, 1.0f, 1.0f);
			minecraft.displayGuiScreen(new GuiMenu_Party_Leader());			
			break;
		case "refresh":
			refreshMembers();
			break;
		}
		
		if(string.startsWith("member:")) {
			String[] data = string.split(":");
			String name = data[1];
			
			UUID targetUUID = Utils.getPlayerByName(minecraft.world, name).getUniqueID();
			PacketHandler.sendToServer(new CSPartyInvite(party, targetUUID));
			//party.removeMember(targetUUID);
			refreshMembers();

			minecraft.world.playSound(minecraft.player, minecraft.player.getPosition(), ModSounds.menu_in.get(), SoundCategory.MASTER, 1.0f, 1.0f);
			//minecraft.displayGuiScreen(new GuiMenu_Party_Member("Party Member"));
			
		}
		updateButtons();
	}

	private void updateButtons() {
		refreshMembers();
	}

	private void refreshMembers() {
		worldData = ModCapabilities.getWorld(minecraft.world);

		float topBarHeight = (float) height * 0.17F;
		int button_statsY = (int) topBarHeight + 5;
		float buttonWidth = ((float) width * 0.1744F) - 20;

		for(int i = 0;i<buttons.size();i++) {
			if(!buttons.get(i).getMessage().getString().startsWith("Back")) {
				buttons.remove(i);
			}
		}
		
		int c = 0;
		//Show the buttons to join public parties
		party = worldData.getPartyFromMember(minecraft.player.getUniqueID());
		for(int i = 1; i < minecraft.world.getPlayers().size(); i++) {
			if(worldData.getPartyFromMember(minecraft.world.getPlayers().get(i).getUniqueID()) != party) {
				addButton(players[i] = new MenuButton((int)(width * 0.3F), button_statsY + ((c++) * 18), (int)(buttonWidth * 2), minecraft.world.getPlayers().get(i).getDisplayName().getString(), ButtonType.BUTTON, (e) -> { action("member:"+e.getMessage().getString()); }));
			}
		}
	
	}	

	@Override
	public void init() {
		//TODO request packet to sync other players data
		super.width = width;
		super.height = height;
		super.init();
		this.buttons.clear();
				
		float topBarHeight = (float) height * 0.17F;
		int button_statsY = (int) topBarHeight + 5;
		float buttonPosX = (float) width * 0.03F;
		float buttonWidth = ((float) width * 0.1744F) - 20;

		addButton(back = new MenuButton((int) buttonPosX, button_statsY + (0 * 18), (int) buttonWidth, Utils.translateToLocal(Strings.Gui_Menu_Back), ButtonType.BUTTON, (e) -> { action("back"); }));
		
		updateButtons();
	}

	@Override
	public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
		super.render(matrixStack, mouseX, mouseY, partialTicks);
		worldData = ModCapabilities.getWorld(minecraft.world);
		party = worldData.getPartyFromMember(minecraft.player.getUniqueID());
		refreshMembers();
	}	
}
