package online.kingdomkeys.kingdomkeys.client.gui.menu;

import java.util.UUID;

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
import online.kingdomkeys.kingdomkeys.lib.Strings;
import online.kingdomkeys.kingdomkeys.lib.Utils;
import online.kingdomkeys.kingdomkeys.network.PacketHandler;
import online.kingdomkeys.kingdomkeys.network.cts.CSPartyCreate;

public class GuiMenu_Party extends GuiMenu_Background {

	boolean priv = false;
	TextFieldWidget tfName;
	Button togglePriv, accept, cancel;
	GuiMenuButton back, create, join, invite, kick, disband, leave;
	GuiMenuButton[] players = new GuiMenuButton[minecraft.world.getPlayers().size()];
		
	final IPlayerCapabilities props = ModCapabilities.get(minecraft.player);
	final ExtendedWorldData worldData = ExtendedWorldData.get(minecraft.world);
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
	}

	protected void action(String string) {
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
			break;
		case "togglePriv":
			priv = !priv;
			break;
		case "accept":
			if(phase == 1) { //Accept Party creation
				Party localParty = new Party(tfName.getMessage(), minecraft.player.getUniqueID(), minecraft.player.getName().getFormattedText(), priv);
				PacketHandler.sendToServer(new CSPartyCreate(localParty));
			}
			break;
		}
		updateButtons();
	}

	private void updateButtons() {
		//IPlayerCapabilities props = ModCapabilities.get(minecraft.player);
		switch(phase) {
		case 0:
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
			back.y = getY(2);
			break;
		case 1:
			togglePriv.setMessage(priv ? "Private" : "Public");
			//TBName
			togglePriv.visible = true;
			accept.visible = true;
			cancel.visible = true;
			tfName.visible = true;
			back.y = getY(0);
			
			create.visible = false;
			join.visible = false;
			cancel.visible = false;
			break;
		case 2:
			break;
		case 3:
			invite.visible = true;
			togglePriv.visible = true;
			kick.visible = true;
			disband.visible = true;
			
			back.y = getY(4);
			
			create.visible = false;
			join.visible = false;
			togglePriv.visible = false;
			accept.visible = false;
			cancel.visible = false;
			tfName.visible = false;
			leave.visible = false;
			break;
		case 4:
			break;
		case 5:
			break;
		case 6:
			break;
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
		float subButtonPosX = buttonPosX + 10;

		float buttonWidth = ((float) width * 0.1744F) - 20;
		float subButtonWidth = buttonWidth - 10;


		float dataWidth = ((float) width * 0.1744F) - 10;

		int col1X = (int) (subButtonPosX + buttonWidth + 40), col2X=(int) (col1X + dataWidth * 2)+10 ;

		//int i = 0;
		addButton(create = new GuiMenuButton((int) buttonPosX, button_statsY + (0 * 18), (int) buttonWidth, Utils.translateToLocal("Create"), ButtonType.BUTTON, (e) -> { action("create"); }));
		addButton(join = new GuiMenuButton((int) buttonPosX, button_statsY + (1 * 18), (int) buttonWidth, Utils.translateToLocal("Join"), ButtonType.BUTTON, (e) -> { action("join"); }));
		addButton(invite = new GuiMenuButton((int) buttonPosX, button_statsY + (0 * 18), (int) buttonWidth, Utils.translateToLocal("Invite"), ButtonType.BUTTON, (e) -> { action("invite"); }));
		addButton(togglePriv = new Button((int) (width*0.25)-2, button_statsY + (3 * 18), 100, 20, Utils.translateToLocal("Private/public"), (e) -> { action("togglePriv"); }));
		addButton(kick = new GuiMenuButton((int) buttonPosX, button_statsY + (2 * 18), (int) buttonWidth, Utils.translateToLocal("Kick"), ButtonType.BUTTON, (e) -> { action("kick"); }));
		addButton(disband = new GuiMenuButton((int) buttonPosX, button_statsY + (3 * 18), (int) buttonWidth, Utils.translateToLocal("Disband"), ButtonType.BUTTON, (e) -> { action("disband"); }));
		addButton(leave = new GuiMenuButton((int) buttonPosX, button_statsY + (0 * 18), (int) buttonWidth, Utils.translateToLocal("Leave"), ButtonType.BUTTON, (e) -> { action("leave"); }));
		addButton(accept = new Button((int) (width*0.25)-2, button_statsY + (5 * 18), (int) buttonWidth, 20, Utils.translateToLocal("Accept"), (e) -> { action("accept"); }));
		addButton(cancel = new Button((int) buttonPosX, button_statsY + (6 * 18), (int) buttonWidth, 20, Utils.translateToLocal("Cancel"), (e) -> { action("cancel"); }));
		addButton(back = new GuiMenuButton((int) buttonPosX, button_statsY + (0 * 18), (int) buttonWidth, Utils.translateToLocal(Strings.Gui_Menu_Status_Button_Back), ButtonType.BUTTON, (e) -> { action("back"); }));
		
		addButton(tfName = new TextFieldWidget(minecraft.fontRenderer, (int)(width*0.25), (int)(height*0.25), 100, 15, "asd"));

		updateButtons();
		if(party == null) {
			phase = 0;
		} else {
			if(party.getLeader().getUUID().equals(minecraft.player.getUniqueID())){
				phase = 3;
			} else {
				phase = 4;
			}
		}

		updateButtons();
	}

	@Override
	public void render(int mouseX, int mouseY, float partialTicks) {
		//fill(125, ((-140 / 16) + 75) + 10, 200, ((-140 / 16) + 75) + 20, 0xFFFFFF);
		super.render(mouseX, mouseY, partialTicks);
		
		int buttonX = (int)(width*0.25);
		
		if(phase == 3 || phase == 4) {
			RenderSystem.pushMatrix();
			{
				RenderSystem.scaled(1.5,1.5, 1);
				drawString(minecraft.fontRenderer, party.getName(), (int) (topLeftBarWidth + topGap) + 5, 10, 0xFF9900);
			}
			RenderSystem.popMatrix();
		}
		
		if(phase == 1 || phase == 2) {
			if(phase == 1) {
				drawString(minecraft.fontRenderer, "Party Name", buttonX, (int)(height*0.2), 0xFFFFFF);
				drawString(minecraft.fontRenderer, "Accessibility", buttonX, (int)(height*0.35), 0xFFFFFF);
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
			
		}
		drawParty();
	}
	
	public void drawParty() {
		if(phase == 3 || phase == 4 || phase == 0) {
			ExtendedWorldData worldData = ExtendedWorldData.get(minecraft.world);
			Party party = worldData.getPartyFromMember(minecraft.player.getUniqueID());
			if(party != null) {
				for(int i=0;i<party.getMembers().size();i++) {
					UUID memberUUID = party.getMembers().get(i).getUUID();
					drawPlayer(i,minecraft.world.getPlayerByUuid(memberUUID));
				}
			} else {
				drawPlayer(0, minecraft.player);
			}
		}
	}
	
	public void drawPlayer(int order, PlayerEntity player) {
		float playerWidth = width * 0.10F;
		float playerHeight = height * 0.4481F;
		float playerPosX = 150F+ (0.18F * (order) * width);
		float playerPosY = height * 0.7518F;
		RenderSystem.pushMatrix();
		{
			RenderSystem.pushMatrix();
			{
				RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
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
				int infoBoxPosY = (int) (height * 0.6F);
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
				
				IPlayerCapabilities props = ModCapabilities.get(player);
				if (props != null) {
					RenderSystem.pushMatrix();
					{
						RenderSystem.translatef((int) infoBoxPosX + 8, (int) infoBoxPosY + ((22 / 2) - (minecraft.fontRenderer.FONT_HEIGHT / 2)), 1);
						// RenderSystem.scale(0.75F, 0.75F, 1);
						drawString(minecraft.fontRenderer, player.getDisplayName().getFormattedText(), 0, 0, 0xFFFFFF);
					}
					RenderSystem.popMatrix();
					drawString(minecraft.fontRenderer, "LV: " + props.getLevel(), (int) infoBoxPosX + 4, (int) (infoBoxPosY + 26), 0xFFD900);
					drawString(minecraft.fontRenderer, "HP: " + (int) player.getHealth() + "/" + (int) player.getMaxHealth(), (int) infoBoxPosX + 4, (int) (infoBoxPosY + 26) + minecraft.fontRenderer.FONT_HEIGHT, 0x00FF00);
					drawString(minecraft.fontRenderer, "MP: " + (int) props.getMP() + "/" + (int) props.getMaxMP(), (int) infoBoxPosX + 4, (int) (infoBoxPosY + 26) + (minecraft.fontRenderer.FONT_HEIGHT * 2), 0x4444FF);
				}
			}
			RenderSystem.popMatrix();
		}
		RenderSystem.popMatrix();
	}
	
}
