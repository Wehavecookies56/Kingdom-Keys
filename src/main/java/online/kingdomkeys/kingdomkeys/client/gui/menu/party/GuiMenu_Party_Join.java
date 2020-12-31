package online.kingdomkeys.kingdomkeys.client.gui.menu.party;

import java.awt.Color;
import java.util.List;

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
import online.kingdomkeys.kingdomkeys.network.cts.CSPartyAddMember;

public class GuiMenu_Party_Join extends MenuBackground {

	boolean priv = false;
	byte pSize = Party.PARTY_LIMIT;
	
	MenuButton back;
		
	IPlayerCapabilities playerData = ModCapabilities.getPlayer(minecraft.player);
	IWorldCapabilities worldData;
	
	MenuButton[] parties = new MenuButton[100];
	
	public GuiMenu_Party_Join() {
		super(Strings.Gui_Menu_Party_Join, new Color(0,0,255));
		drawPlayerInfo = true;
		worldData = ModCapabilities.getWorld(minecraft.world);
	}

	protected void action(String string) {
		//Clear list as it should never be seen unless in phase 2
		for(int i=0;i<parties.length;i++) {
			if(parties[i] != null) {
				parties[i].visible = false;
			}
		}
		
		switch(string) {
		case "back":
			minecraft.world.playSound(minecraft.player, minecraft.player.getPosition(), ModSounds.menu_in.get(), SoundCategory.MASTER, 1.0f, 1.0f);
			minecraft.displayGuiScreen(new GuiMenu_Party_None());			
			break;
		}
		
		if(string.startsWith("party:")) {
			String[] data = string.split(":");
			String partyName = data[1].substring(data[1].indexOf("]")+2);
			Party p = worldData.getPartyFromName(partyName);
			if(p.getMembers().size() < p.getSize()) {
				PacketHandler.sendToServer(new CSPartyAddMember(p, minecraft.player));
				p.addMember(minecraft.player.getUniqueID(), minecraft.player.getDisplayName().getFormattedText());

				minecraft.world.playSound(minecraft.player, minecraft.player.getPosition(), ModSounds.menu_in.get(), SoundCategory.MASTER, 1.0f, 1.0f);
				minecraft.displayGuiScreen(new GuiMenu_Party_Member());
			} else {
				System.out.println("Full");
			}
		}
		updateButtons();
	}

	private void updateButtons() {
		refreshParties();
	}

	private void refreshParties() {
		playerData = ModCapabilities.getPlayer(minecraft.player);
		List<String> privateParties = playerData.getPartiesInvited();
		
		worldData = ModCapabilities.getWorld(minecraft.world);

		float topBarHeight = (float) height * 0.17F;
		int button_statsY = (int) topBarHeight + 5;
		float buttonWidth = ((float) width * 0.1744F) - 20;

		for(int i = 0;i<buttons.size();i++) {
			if(buttons.get(i).getMessage().startsWith("[") || buttons.get(i).getMessage().startsWith("(P) [")) {
				buttons.remove(i);
			}
		}
		
		//Show private parties
		int j = 0;
		for(j = 0;j<privateParties.size();j++) {
			Party p = worldData.getPartyFromName(privateParties.get(j));
			if(p != null)
				addButton(parties[j] = new MenuButton((int)(width * 0.3F), button_statsY + (j * 18), (int)(buttonWidth * 2), "(P) ["+p.getMembers().size()+"/"+p.getSize()+"] "+p.getName(), ButtonType.BUTTON, (e) -> { action("party:"+e.getMessage()); }));
		}
		//Show the buttons to join public parties
		List<Party> partiesList = worldData.getParties();
		for(int i=0;i<partiesList.size();i++) {
			if(partiesList.get(i) != null && !partiesList.get(i).getPriv()) {
				Party p = partiesList.get(i);
				if(!privateParties.contains(p.getName())){//TODO test this xD
					addButton(parties[i+j] = new MenuButton((int)(width * 0.3F), button_statsY + ((i+j) * 18), (int)(buttonWidth * 2), "["+p.getMembers().size()+"/"+p.getSize()+"] "+p.getName(), ButtonType.BUTTON, (e) -> { action("party:"+e.getMessage()); }));
				}
			}
		}
	}	

	@Override
	public void init() {
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
	public void render(int mouseX, int mouseY, float partialTicks) {
		super.render(mouseX, mouseY, partialTicks);
		worldData = ModCapabilities.getWorld(minecraft.world);
		refreshParties();
	}
	
	
}
