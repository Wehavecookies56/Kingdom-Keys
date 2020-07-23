package online.kingdomkeys.kingdomkeys.client.gui.menu;

import java.util.List;

import com.mojang.blaze3d.systems.RenderSystem;

import net.minecraft.client.gui.screen.inventory.InventoryScreen;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ResourceLocation;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.capability.ExtendedWorldData;
import online.kingdomkeys.kingdomkeys.capability.IPlayerCapabilities;
import online.kingdomkeys.kingdomkeys.capability.ModCapabilities;
import online.kingdomkeys.kingdomkeys.client.gui.GuiHelper;
import online.kingdomkeys.kingdomkeys.client.gui.menu.GuiMenuButton.ButtonType;
import online.kingdomkeys.kingdomkeys.lib.Party;
import online.kingdomkeys.kingdomkeys.lib.Party.Member;
import online.kingdomkeys.kingdomkeys.lib.Strings;
import online.kingdomkeys.kingdomkeys.lib.Utils;
import online.kingdomkeys.kingdomkeys.network.PacketHandler;
import online.kingdomkeys.kingdomkeys.network.cts.CSPartyAddMember;
import online.kingdomkeys.kingdomkeys.network.cts.CSPartyCreate;
import online.kingdomkeys.kingdomkeys.network.cts.CSPartyLeave;
import online.kingdomkeys.kingdomkeys.network.cts.CSPartySettings;
import online.kingdomkeys.kingdomkeys.network.cts.CSPartyRemove;

public class GuiMenu_Party extends GuiMenu_Background {

	boolean priv = false;
	byte pSize = Party.PARTY_LIMIT;
	
	TextFieldWidget tfName;
	Button togglePriv, accept, cancel, size;
	GuiMenuButton back, create, join, invite, kick, disband, leave, togglePriv2, refresh;
		
	final IPlayerCapabilities props = ModCapabilities.get(minecraft.player);
	ExtendedWorldData worldData;
	
	GuiMenuButton[] players = new GuiMenuButton[minecraft.world.getPlayers().size()];
	GuiMenuButton[] parties = new GuiMenuButton[100];

	Party party;
	
	//Not in party
	//0 = not in party
	//1 = creating (create)
	//2 = Looking for party (join)
	//In party
	//3 = Leader view
	//4 = Member view
	int phase = 0;
	
	public GuiMenu_Party(String name) {
		super(name);
		drawPlayerInfo = true;
		worldData = ExtendedWorldData.get(minecraft.world);
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
			switch(phase) {
			case 0:
			case 3:
			case 4:
				GuiHelper.openMenu();
				break;
			case 1:
			case 2:
				phase = 0;
				break;				
			}
			
			break;
		case "create":
			phase = 1;
			break;
		case "join":
			phase = 2;
			worldData = ExtendedWorldData.get(minecraft.world);
			break;
		case "togglePriv":
			priv = !priv;
			if(phase == 3) { //If it's in phase 3 (leader view) sync it (as you are not going to create it so it wouldn't save otherwise)
				party.setPriv(priv);
				PacketHandler.sendToServer(new CSPartySettings(party));
			}
			break;
		case "accept":
			if(phase == 1 && !tfName.getText().equals("")) { //Accept Party creation
				Party localParty = new Party(tfName.getText(), minecraft.player.getUniqueID(), minecraft.player.getName().getFormattedText(), priv, Byte.parseByte(size.getMessage()));
				PacketHandler.sendToServer(new CSPartyCreate(localParty));
				GuiHelper.openMenu();
			}
			break;
		case "disband":
			PacketHandler.sendToServer(new CSPartyRemove(party));
			GuiHelper.openMenu();
			break;
		case "refresh":
			refreshParties();
			break;
		case "leave":
			PacketHandler.sendToServer(new CSPartyLeave(party, minecraft.player.getUniqueID()));
			party = null;
			break;
		case "size":
			if(pSize == Party.PARTY_LIMIT) {
				pSize = 1;
			} else {
				pSize++;
			}
			size.setMessage(pSize+"");
			
			if(phase == 3) { //If it's in phase 3 (leader view) sync it (as you are not going to create it so it wouldn't save otherwise)
				party.setSize(pSize);
				PacketHandler.sendToServer(new CSPartySettings(party));
			}
			break;
		}
		
		if(string.startsWith("party:")) {
			String[] data = string.split(":");
			String partyName = data[1].substring(6);
			Party p = worldData.getPartyFromName(partyName);
			PacketHandler.sendToServer(new CSPartyAddMember(p, minecraft.player));
			phase = 4;
			party = p;
		}
		updateButtons();
	}

	private void updateButtons() {
		//IPlayerCapabilities props = ModCapabilities.get(minecraft.player);
		togglePriv.setMessage(priv ? "Private" : "Public");
		togglePriv2.setMessage(priv ? "Private" : "Public");

		switch(phase) {
		case 0://No party
			create.visible = true;
			join.visible = true;
			
			invite.visible = false;
			togglePriv.visible = false;
			kick.visible = false;
			disband.visible = false;
			leave.visible = false;
			accept.visible = false;
			cancel.visible = false;
			tfName.visible = false;
			togglePriv2.visible = false;
			refresh.visible = false;
			size.visible = false;

			back.y = getY(2);
			break;
		case 1://Creating party
			//TBName
			togglePriv.visible = true;
			accept.visible = true;
			cancel.visible = true;
			tfName.visible = true;
			size.visible = true;
			
			back.y = getY(0);
			
			create.visible = false;
			join.visible = false;
			cancel.visible = false;
			togglePriv2.visible = false;
			refresh.visible = false;

			break;
		case 2://Joining party
			refresh.visible = true;
			
			refresh.y = getY(0);
			back.y = getY(1);
			
			create.visible = false;
			join.visible = false;
			cancel.visible = false;
			togglePriv2.visible = false;

			refreshParties();
			break;
		case 3://Party leader
			invite.visible = true;
			togglePriv.visible = true;
			kick.visible = true;
			disband.visible = true;
			togglePriv2.visible = true;
			
			back.y = getY(4);
			
			create.visible = false;
			join.visible = false;
			togglePriv.visible = false;
			accept.visible = false;
			cancel.visible = false;
			tfName.visible = false;
			leave.visible = false;
			break;
		case 4://Party member
			leave.visible = true;
			
			back.y=getY(1);
			disband.visible = false;
			kick.visible = false;
			refresh.visible = false;
			create.visible = false;
			join.visible = false;
			togglePriv.visible = false;
			accept.visible = false;
			cancel.visible = false;
			tfName.visible = false;
			togglePriv2.visible = false;
			invite.visible = false;
			break;
		case 5:
			break;
		case 6:
			break;
		}
	}

	private void refreshParties() {
		worldData = ExtendedWorldData.get(minecraft.world);

		float topBarHeight = (float) height * 0.17F;
		int button_statsY = (int) topBarHeight + 5;
		float buttonWidth = ((float) width * 0.1744F) - 20;

		for(int i = 0;i<buttons.size();i++) {
			if(buttons.get(i).getMessage().startsWith("[")) {
				buttons.remove(i);
			}
		}
		
		//Show the buttons to join public parties
		List<Party> partiesList = worldData.getParties();
		for(int i=0;i<partiesList.size();i++) {
			if(partiesList.get(i) != null && !partiesList.get(i).getPriv()) {
				Party p = partiesList.get(i);
				addButton(parties[i] = new GuiMenuButton((int)(width * 0.3F), button_statsY + (i * 18), (int)(buttonWidth * 2), "["+p.getMembers().size()+"/"+p.getSize()+"] "+p.getName(), ButtonType.BUTTON, (e) -> { action("party:"+e.getMessage()); }));
			}
		}
	}

	private int getY(int i) {
		int button_statsY = (int) topBarHeight + 5;
		return button_statsY + (i * 18);
	}

	@Override
	public void init() {
		//TODO request packet to sync other players data
		super.width = width;
		super.height = height;
		super.init();
		this.buttons.clear();
		
		party = worldData.getPartyFromMember(minecraft.player.getUniqueID());
		
		float topBarHeight = (float) height * 0.17F;
		int button_statsY = (int) topBarHeight + 5;
		float buttonPosX = (float) width * 0.03F;
		float buttonWidth = ((float) width * 0.1744F) - 20;


		addButton(create = new GuiMenuButton((int) buttonPosX, button_statsY + (0 * 18), (int) buttonWidth, Utils.translateToLocal("Create"), ButtonType.BUTTON, (e) -> { action("create"); }));
		addButton(join = new GuiMenuButton((int) buttonPosX, button_statsY + (1 * 18), (int) buttonWidth, Utils.translateToLocal("Join"), ButtonType.BUTTON, (e) -> { action("join"); }));
		addButton(invite = new GuiMenuButton((int) buttonPosX, button_statsY + (0 * 18), (int) buttonWidth, Utils.translateToLocal("Invite"), ButtonType.BUTTON, (e) -> { action("invite"); }));
		addButton(togglePriv = new Button((int) (width*0.25)-2, button_statsY + (3 * 18), 100, 20, Utils.translateToLocal("Private/public"), (e) -> { action("togglePriv"); }));
		addButton(togglePriv2 = new GuiMenuButton((int) buttonPosX, button_statsY + (1 * 18), (int) buttonWidth, Utils.translateToLocal("Private/public"), ButtonType.BUTTON, (e) -> { action("togglePriv"); }));
		addButton(kick = new GuiMenuButton((int) buttonPosX, button_statsY + (2 * 18), (int) buttonWidth, Utils.translateToLocal("Kick"), ButtonType.BUTTON, (e) -> { action("kick"); }));
		addButton(disband = new GuiMenuButton((int) buttonPosX, button_statsY + (3 * 18), (int) buttonWidth, Utils.translateToLocal("Disband"), ButtonType.BUTTON, (e) -> { action("disband"); }));
		addButton(leave = new GuiMenuButton((int) buttonPosX, button_statsY + (0 * 18), (int) buttonWidth, Utils.translateToLocal("Leave"), ButtonType.BUTTON, (e) -> { action("leave"); }));
		addButton(accept = new Button((int) (width*0.25)-2, button_statsY + (5 * 18), (int) 100, 20, Utils.translateToLocal("Accept"), (e) -> { action("accept"); }));
		addButton(cancel = new Button((int) buttonPosX, button_statsY + (6 * 18), (int) buttonWidth, 20, Utils.translateToLocal("Cancel"), (e) -> { action("cancel"); }));
		addButton(back = new GuiMenuButton((int) buttonPosX, button_statsY + (0 * 18), (int) buttonWidth, Utils.translateToLocal(Strings.Gui_Menu_Status_Button_Back), ButtonType.BUTTON, (e) -> { action("back"); }));
		addButton(refresh = new GuiMenuButton((int) buttonPosX, button_statsY + (1 * 18), (int) buttonWidth, Utils.translateToLocal("Refresh"), ButtonType.BUTTON, (e) -> { action("refresh"); }));
		addButton(size = new Button((int) (width * 0.25 - 2 + 100 + 4), button_statsY + (3 * 18), (int) 20, 20, Party.PARTY_LIMIT+"", (e) -> { action("size"); }));
		
		addButton(tfName = new TextFieldWidget(minecraft.fontRenderer, (int)(width*0.25), (int)(height*0.25), 100, 15, ""));
		
		
		updateButtons();
		if(party == null) {
			phase = 0;
		} else {
			if(party.getLeader().getUUID().equals(minecraft.player.getUniqueID())){
				phase = 3;
				priv = party.getPriv();
			} else {
				phase = 4;
			}
		}

		updateButtons();
	}

	@Override
	public void render(int mouseX, int mouseY, float partialTicks) {
		//System.out.println(phase);
		//fill(125, ((-140 / 16) + 75) + 10, 200, ((-140 / 16) + 75) + 20, 0xFFFFFF);
		super.render(mouseX, mouseY, partialTicks);
		worldData = ExtendedWorldData.get(minecraft.world);
		party = worldData.getPartyFromMember(minecraft.player.getUniqueID());

		if(party == null && phase == 4) {
			phase = 0;
			updateButtons();
		}
		//System.out.println(priv);
		
		int buttonX = (int)(width*0.25);
		
		if(phase == 3 || phase == 4) {
			RenderSystem.pushMatrix();
			{
				RenderSystem.scaled(1.5,1.5, 1);
				drawString(minecraft.fontRenderer, "["+party.getMembers().size()+"/"+party.getSize()+"] "+party.getName(), (int) (topLeftBarWidth + topGap) + 5, 10, 0xFF9900);
			}
			RenderSystem.popMatrix();
		}
		System.out.println(phase);
		if(phase == 1 || phase == 2) {
			if(phase == 1) {
				drawString(minecraft.fontRenderer, "Party Name", buttonX, (int)(height * 0.2), 0xFFFFFF);
				drawString(minecraft.fontRenderer, "Accessibility and limit", buttonX, (int)(height * 0.35), 0xFFFFFF);
			}
			/*minecraft.getRenderManager().textureManager.bindTexture(new ResourceLocation(KingdomKeys.MODID, "textures/gui/menu/menu_button.png"));
			int infoBoxPosX = (int) (width * 0.3F);
			int infoBoxPosY = (int) (height * 0.2F);
			blit(infoBoxPosX, infoBoxPosY, 22, 67, 23, 23);
			RenderSystem.pushMatrix();
			{
				RenderSystem.scaled(10, 1, 1);
				blit(infoBoxPosX+23, infoBoxPosY, 47, 67, 1, 23);
			}
			RenderSystem.popMatrix();*/

			/*blit(infoBoxPosX, infoBoxPosY, 22, 67, 23, 23);
			blit(infoBoxPosX, infoBoxPosY, 22, 67, 23, 23);
			blit(infoBoxPosX, infoBoxPosY, 22, 67, 23, 23);*/
			
		} else {
			drawParty();
		}
	}
	
	public void drawParty() {
		if(phase == 3 || phase == 4 || phase == 0) {
			party = worldData.getPartyFromMember(minecraft.player.getUniqueID());
			if(party != null) {
				for(int i=0;i<party.getMembers().size();i++) {
					Member member = party.getMembers().get(i);
					drawPlayer(i,member);
				}
			} else {
				Member m = new Member(minecraft.player.getUniqueID(), minecraft.player.getDisplayName().getFormattedText());
				drawPlayer(0, m);
			}
		}
	}
	
	public void drawPlayer(int order, Member member) {
		float playerHeight = height * 0.45F;
		float playerPosX = 150F+ (0.18F * (order) * width);
		float playerPosY = height * 0.7F;
		
		PlayerEntity player = Utils.getPlayerByName(minecraft.world, member.getUsername());
		
		RenderSystem.pushMatrix();
		{
			RenderSystem.pushMatrix();
			{
				RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
				if(member != null && player != null)
					InventoryScreen.drawEntityOnScreen((int) playerPosX, (int) playerPosY, (int) playerHeight / 2, 0, 0, player);
				RenderSystem.color4f(1.0F, 1.0F, 1.0F, 0.75F);
			}
			RenderSystem.popMatrix();
			RenderSystem.pushMatrix();
			
				RenderSystem.color3f(1, 1, 1);
				RenderSystem.translatef(9, 1, 100);
				RenderSystem.enableAlphaTest();
				RenderSystem.enableBlend();
				minecraft.getRenderManager().textureManager.bindTexture(new ResourceLocation(KingdomKeys.MODID, "textures/gui/menu/menu_button.png"));
				int infoBoxWidth = (int) ((width * 0.1385F) - 14); // This might be wrong cuz I had to convert from float to int
				int infoBoxPosX = (int) (105F+ (0.18F * (order) * width));
				int infoBoxPosY = (int) (height * 0.54F);
				blit(infoBoxPosX, infoBoxPosY, 123, 67, 11, 22);
				for (int i = 0; i < infoBoxWidth; i++) {
					blit(infoBoxPosX + 11 + i, infoBoxPosY, 135, 67, 1, 22);
				}
				blit(infoBoxPosX + 11 + infoBoxWidth, infoBoxPosY, 137, 67, 3, 22);
				blit(infoBoxPosX, infoBoxPosY + 22, 123, 90, 3, 35);
				for (int i = 0; i < infoBoxWidth + 8; i++) {
					blit(infoBoxPosX + 3 + i, infoBoxPosY + 22, 127, 90, 1, 35);
				}
				blit(infoBoxPosX + 3 + infoBoxWidth + 8, infoBoxPosY + 22, 129, 90, 3, 35);
				RenderSystem.disableAlphaTest();
				RenderSystem.disableBlend();
			RenderSystem.popMatrix();
			RenderSystem.pushMatrix();
			{
				RenderSystem.translatef(10, 2, 100);
				
				RenderSystem.pushMatrix();
				{
					RenderSystem.translatef((int) infoBoxPosX + 8, (int) infoBoxPosY + ((22 / 2) - (minecraft.fontRenderer.FONT_HEIGHT / 2)), 1);
					// RenderSystem.scale(0.75F, 0.75F, 1);
					drawString(minecraft.fontRenderer, member.getUsername(), 0, 0, 0xFFFFFF);
				}
				RenderSystem.popMatrix();
				if(player != null) {
					IPlayerCapabilities props = ModCapabilities.get(player);
					if (props != null) {
						drawString(minecraft.fontRenderer, "LV: " + props.getLevel(), (int) infoBoxPosX + 4, (int) (infoBoxPosY + 26), 0xFFD900);
						drawString(minecraft.fontRenderer, "HP: " + (int) player.getHealth() + "/" + (int) player.getMaxHealth(), (int) infoBoxPosX + 4, (int) (infoBoxPosY + 26) + minecraft.fontRenderer.FONT_HEIGHT, 0x00FF00);
						drawString(minecraft.fontRenderer, "MP: " + (int) props.getMP() + "/" + (int) props.getMaxMP(), (int) infoBoxPosX + 4, (int) (infoBoxPosY + 26) + (minecraft.fontRenderer.FONT_HEIGHT * 2), 0x4444FF);
					}
				}
			}
			RenderSystem.popMatrix();
		}
		RenderSystem.popMatrix();
	}
	
}
