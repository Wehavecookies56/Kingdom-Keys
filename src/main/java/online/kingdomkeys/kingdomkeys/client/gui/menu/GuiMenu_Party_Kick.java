package online.kingdomkeys.kingdomkeys.client.gui.menu;

import java.awt.Color;
import java.util.UUID;

import com.mojang.blaze3d.systems.RenderSystem;

import net.minecraft.util.SoundCategory;
import online.kingdomkeys.kingdomkeys.capability.IPlayerCapabilities;
import online.kingdomkeys.kingdomkeys.capability.IWorldCapabilities;
import online.kingdomkeys.kingdomkeys.capability.ModCapabilities;
import online.kingdomkeys.kingdomkeys.client.gui.menu.GuiMenuButton.ButtonType;
import online.kingdomkeys.kingdomkeys.client.sound.ModSounds;
import online.kingdomkeys.kingdomkeys.lib.Party;
import online.kingdomkeys.kingdomkeys.lib.Strings;
import online.kingdomkeys.kingdomkeys.lib.Utils;
import online.kingdomkeys.kingdomkeys.network.PacketHandler;
import online.kingdomkeys.kingdomkeys.network.cts.CSPartyLeave;

public class GuiMenu_Party_Kick extends GuiMenu_Background {
	
	GuiMenuButton back;
		
	final IPlayerCapabilities playerData = ModCapabilities.getPlayer(minecraft.player);
	IWorldCapabilities worldData;
	Party party;
	
	GuiMenuButton[] players = new GuiMenuButton[4];
	
	public GuiMenu_Party_Kick(String name) {
		super(name, new Color(0,0,255));
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
			PacketHandler.sendToServer(new CSPartyLeave(party, targetUUID));
			party.removeMember(targetUUID);
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
			if(!buttons.get(i).getMessage().startsWith("Refresh") && !buttons.get(i).getMessage().startsWith("Back")) {
				buttons.remove(i);
			}
		}
		
		//Show the buttons to join public parties
		party = worldData.getPartyFromMember(minecraft.player.getUniqueID());
		for(int i = 1; i < party.getMembers().size(); i++) {
			addButton(players[i] = new GuiMenuButton((int)(width * 0.3F), button_statsY + ((i-1) * 18), (int)(buttonWidth * 2), party.getMembers().get(i).getUsername(), ButtonType.BUTTON, (e) -> { action("member:"+e.getMessage()); }));
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

		addButton(back = new GuiMenuButton((int) buttonPosX, button_statsY + (0 * 18), (int) buttonWidth, Utils.translateToLocal(Strings.Gui_Menu_Status_Button_Back), ButtonType.BUTTON, (e) -> { action("back"); }));
		
		updateButtons();
	}

	@Override
	public void render(int mouseX, int mouseY, float partialTicks) {
		super.render(mouseX, mouseY, partialTicks);
		worldData = ModCapabilities.getWorld(minecraft.world);
		party = worldData.getPartyFromMember(minecraft.player.getUniqueID());
		refreshMembers();

		RenderSystem.pushMatrix();
		{
			RenderSystem.scaled(1.5,1.5, 1);
			drawString(minecraft.fontRenderer, "JOIN", 2, 10, 0xFF9900);
		}
		RenderSystem.popMatrix();
	}
	
	
}
