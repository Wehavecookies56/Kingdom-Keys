package online.kingdomkeys.kingdomkeys.client.gui.menu.party;

import java.awt.Color;
import java.util.List;

import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.sounds.SoundSource;
import online.kingdomkeys.kingdomkeys.capability.IPlayerCapabilities;
import online.kingdomkeys.kingdomkeys.capability.IWorldCapabilities;
import online.kingdomkeys.kingdomkeys.capability.ModCapabilities;
import online.kingdomkeys.kingdomkeys.client.gui.elements.MenuBackground;
import online.kingdomkeys.kingdomkeys.client.gui.elements.buttons.MenuButton;
import online.kingdomkeys.kingdomkeys.client.gui.elements.buttons.MenuButton.ButtonType;
import online.kingdomkeys.kingdomkeys.client.sound.ModSounds;
import online.kingdomkeys.kingdomkeys.lib.Party;
import online.kingdomkeys.kingdomkeys.lib.Strings;
import online.kingdomkeys.kingdomkeys.network.PacketHandler;
import online.kingdomkeys.kingdomkeys.network.cts.CSPartyAddMember;
import online.kingdomkeys.kingdomkeys.util.Utils;

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
		worldData = ModCapabilities.getWorld(minecraft.level);
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
				} else {
					
				}
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
		
		worldData = ModCapabilities.getWorld(minecraft.level);

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
		int j = 0;
		for(j = 0;j<privateParties.size();j++) {
			Party p = worldData.getPartyFromName(privateParties.get(j));
			if(p != null) {
				addRenderableWidget(parties[c++] = new MenuButton((int)(width * 0.3F), button_statsY + (j * 18), (int)(buttonWidth * 2), "(P) ["+p.getMembers().size()+"/"+p.getSize()+"] "+p.getName(), ButtonType.BUTTON, (e) -> { action("party:"+e.getMessage().getString()); }));
			}
		}
		//Show the buttons to join public parties
		List<Party> partiesList = worldData.getParties();
		for(int i=0;i<partiesList.size();i++) {
			if(partiesList.get(i) != null && !partiesList.get(i).getPriv()) {
				Party p = partiesList.get(i);
				if(!privateParties.contains(p.getName())){//TODO test this xD
					addRenderableWidget(parties[c++] = new MenuButton((int)(width * 0.3F), button_statsY + ((i+j) * 18), (int)(buttonWidth * 2), "["+p.getMembers().size()+"/"+p.getSize()+"] "+p.getName(), ButtonType.BUTTON, (e) -> { action("party:"+e.getMessage().getString()); }));
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
	public void render(PoseStack matrixStack, int mouseX, int mouseY, float partialTicks) {
		super.render(matrixStack, mouseX, mouseY, partialTicks);
		worldData = ModCapabilities.getWorld(minecraft.level);
		refreshParties();
	}
	
	
}
