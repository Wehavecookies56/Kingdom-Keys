package online.kingdomkeys.kingdomkeys.client.gui.menu;

import java.util.UUID;

import com.mojang.blaze3d.systems.RenderSystem;

import net.minecraft.client.gui.screen.inventory.InventoryScreen;
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

public class GuiMenu_Party extends GuiMenu_Background {

	Button party_back;
	GuiMenuButton[] players = new GuiMenuButton[minecraft.world.getPlayers().size()];
	
	final IPlayerCapabilities props = ModCapabilities.get(minecraft.player);
	final ExtendedWorldData worldData = ExtendedWorldData.get(minecraft.world);
	
	public GuiMenu_Party(String name) {
		super(name);
		drawPlayerInfo = true;
	}

	protected void action(String string) {
		if (string.equals("back"))
			GuiHelper.openMenu();
		//else
		//	form = string;

		updateButtons();
	}

	private void updateButtons() {
		//IPlayerCapabilities props = ModCapabilities.get(minecraft.player);
		
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
		int button_stats_playerY = button_statsY;
		int button_stats_formsY = button_stats_playerY + 18;

		float buttonPosX = (float) width * 0.03F;
		float subButtonPosX = buttonPosX + 10;

		float buttonWidth = ((float) width * 0.1744F)- 20;
		float subButtonWidth = buttonWidth - 10;


		float dataWidth = ((float) width * 0.1744F)-10;

		int col1X = (int) (subButtonPosX + buttonWidth + 40), col2X=(int) (col1X + dataWidth * 2)+10 ;

		int i = 0;
		for(i = 0; i<minecraft.world.getPlayers().size();i++) {//Player selector
			PlayerEntity player = minecraft.world.getPlayers().get(i);
			addButton(players[i] = new GuiMenuButton((int) subButtonPosX, button_stats_formsY + (i * 18), (int) subButtonWidth, player.getName().getFormattedText(), ButtonType.SUBBUTTON, (e) -> { action(player.getName().getFormattedText()); }));
		}
		addButton(party_back = new GuiMenuButton((int) buttonPosX, button_stats_formsY + (i * 18), (int) buttonWidth, Utils.translateToLocal(Strings.Gui_Menu_Status_Button_Back), ButtonType.BUTTON, (e) -> { action("back"); }));
		
		//Stats
		int c = 0;
		int spacer = 14;
		
		
		updateButtons();
	}

	@Override
	public void render(int mouseX, int mouseY, float partialTicks) {
		fill(125, ((-140 / 16) + 75) + 10, 200, ((-140 / 16) + 75) + 20, 0xFFFFFF);
		super.render(mouseX, mouseY, partialTicks);
		/*for(int i = 0; i<minecraft.world.getPlayers().size();i++) {//Player selector
			drawPlayer(0, minecraft.world.getPlayers().get(i));
			drawPlayer(1, minecraft.world.getPlayers().get(i));
			drawPlayer(2, minecraft.world.getPlayers().get(i));
		}*/
		drawParty();
	}
	
	public void drawParty() {
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
	
	public void drawPlayer(int order, PlayerEntity player) {
		float playerWidth = width * 0.10F;
		float playerHeight = height * 0.4481F;
		float playerPosX = 30F+ (0.25F * (order+1) * width);
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
				int infoBoxPosX = (int) (-12F+ (0.25F * (order+1) * width));
				int infoBoxPosY = (int) (height * 0.624F);
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
