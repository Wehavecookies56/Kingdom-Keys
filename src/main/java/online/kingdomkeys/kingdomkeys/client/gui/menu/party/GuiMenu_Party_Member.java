package online.kingdomkeys.kingdomkeys.client.gui.menu.party;

import java.awt.Color;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;

import net.minecraft.client.gui.screen.inventory.InventoryScreen;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.capability.IPlayerCapabilities;
import online.kingdomkeys.kingdomkeys.capability.IWorldCapabilities;
import online.kingdomkeys.kingdomkeys.capability.ModCapabilities;
import online.kingdomkeys.kingdomkeys.client.gui.GuiHelper;
import online.kingdomkeys.kingdomkeys.client.gui.elements.MenuBackground;
import online.kingdomkeys.kingdomkeys.client.gui.elements.buttons.MenuButton;
import online.kingdomkeys.kingdomkeys.client.gui.elements.buttons.MenuButton.ButtonType;
import online.kingdomkeys.kingdomkeys.client.sound.ModSounds;
import online.kingdomkeys.kingdomkeys.lib.Party;
import online.kingdomkeys.kingdomkeys.lib.Party.Member;
import online.kingdomkeys.kingdomkeys.lib.Strings;
import online.kingdomkeys.kingdomkeys.network.PacketHandler;
import online.kingdomkeys.kingdomkeys.network.cts.CSPartyLeave;
import online.kingdomkeys.kingdomkeys.util.Utils;

public class GuiMenu_Party_Member extends MenuBackground {

	MenuButton back, leave;
		
	final IPlayerCapabilities playerData = ModCapabilities.getPlayer(minecraft.player);
	IWorldCapabilities worldData;
	Party party;
	
	public GuiMenu_Party_Member() {
		super(Strings.Gui_Menu_Party, new Color(0,0,255));
		drawPlayerInfo = true;
		worldData = ModCapabilities.getWorld(minecraft.world);
		party = worldData.getPartyFromMember(minecraft.player.getUniqueID());
	}

	protected void action(String string) {
		switch(string) {
		case "back":
			GuiHelper.openMenu();			
			break;		
		case "leave":
			PacketHandler.sendToServer(new CSPartyLeave(party, minecraft.player.getUniqueID()));
			minecraft.world.playSound(minecraft.player, minecraft.player.getPosition(), ModSounds.menu_in.get(), SoundCategory.MASTER, 1.0f, 1.0f);
			minecraft.displayGuiScreen(new GuiMenu_Party_None());
			break;		
		}
		
		updateButtons();
	}

	private void updateButtons() {
		leave.visible = true;
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

		addButton(leave = new MenuButton((int) buttonPosX, button_statsY + (0 * 18), (int) buttonWidth, Utils.translateToLocal(Strings.Gui_Menu_Party_Member_Leave), ButtonType.BUTTON, (e) -> { action("leave"); }));
		addButton(back = new MenuButton((int) buttonPosX, button_statsY + (1 * 18), (int) buttonWidth, Utils.translateToLocal(Strings.Gui_Menu_Back), ButtonType.BUTTON, (e) -> { action("back"); }));
		
		updateButtons();
	}

	@Override
	public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
		//System.out.println("Member 1: "+party);
		super.render(matrixStack, mouseX, mouseY, partialTicks);
		worldData = ModCapabilities.getWorld(minecraft.world);
		party = worldData.getPartyFromMember(minecraft.player.getUniqueID());

		//System.out.println("Member 2: "+party);
		if(party == null) {
			GuiHelper.openMenu();
			updateButtons();
		} else {
			matrixStack.push();
			{
				matrixStack.scale(1.5F,1.5F, 1);
				drawString(matrixStack, minecraft.fontRenderer, "["+party.getMembers().size()+"/"+party.getSize()+"] "+party.getName(), (int) (topLeftBarWidth + topGap) + 5, 10, 0xFF9900);
			}
			matrixStack.pop();
			drawParty(matrixStack);
		}
	}
	
	public void drawParty(MatrixStack matrixStack) {
		for(int i=0;i<party.getMembers().size();i++) {
			Member member = party.getMembers().get(i);
			drawPlayer(matrixStack, i,member);
		}
	}
	
	public void drawPlayer(MatrixStack matrixStack, int order, Member member) {
		float playerHeight = height * 0.45F;
		float playerPosX = 140F+ (0.18F * (order) * width);
		float playerPosY = height * 0.7F;
		
		PlayerEntity player = Utils.getPlayerByName(minecraft.world, member.getUsername());
		
		matrixStack.push();
		{
			matrixStack.push();
			{
				RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
				if(member != null && player != null) {
					RenderSystem.pushMatrix();
					RenderSystem.scalef(0.9F, 0.9F, 1.0F);

					InventoryScreen.drawEntityOnScreen((int) playerPosX, (int) playerPosY, (int) playerHeight / 2, 0, 0, player);
					RenderSystem.popMatrix();
				}
				RenderSystem.color4f(1.0F, 1.0F, 1.0F, 0.75F);
			}
			matrixStack.pop();
			matrixStack.push();
			matrixStack.scale(0.9F, 0.9F, 1);
				RenderSystem.color3f(1, 1, 1);
				matrixStack.translate(1, 20, 100);
				RenderSystem.enableAlphaTest();
				RenderSystem.enableBlend();
				minecraft.getRenderManager().textureManager.bindTexture(new ResourceLocation(KingdomKeys.MODID, "textures/gui/menu/menu_button.png"));
				int infoBoxWidth = (int) ((width * 0.1385F) - 14); // This might be wrong cuz I had to convert from float to int
				int infoBoxPosX = (int) (105F+ (0.18F * (order) * width));
				int infoBoxPosY = (int) (height * 0.54F);
				blit(matrixStack, infoBoxPosX, infoBoxPosY, 123, 67, 12, 22);
				for (int i = 0; i < infoBoxWidth; i++) {
					blit(matrixStack, infoBoxPosX + 11 + i, infoBoxPosY, 135, 67, 2, 22);
				}
				blit(matrixStack, infoBoxPosX + 11 + infoBoxWidth, infoBoxPosY, 137, 67, 3, 22);
				blit(matrixStack, infoBoxPosX, infoBoxPosY + 22, 123, 90, 4, 35);
				for (int i = 0; i < infoBoxWidth + 8; i++) {
					blit(matrixStack, infoBoxPosX + 3 + i, infoBoxPosY + 22, 127, 90, 2, 35);
				}
				blit(matrixStack, infoBoxPosX + 3 + infoBoxWidth + 8, infoBoxPosY + 22, 129, 90, 3, 35);
				RenderSystem.disableAlphaTest();
				RenderSystem.disableBlend();
			matrixStack.pop();
			matrixStack.push();
			{
				matrixStack.scale(0.9F, 0.9F, 1);
				matrixStack.translate(2, 20, 100);
				
				matrixStack.push();
				{
					matrixStack.translate((int) infoBoxPosX + 8, (int) infoBoxPosY + ((22 / 2) - (minecraft.fontRenderer.FONT_HEIGHT / 2)), 1);
					// matrixStack.scale(0.75F, 0.75F, 1);
					drawString(matrixStack, minecraft.fontRenderer, member.getUsername(), 0, 0, 0xFFFFFF);
				}
				matrixStack.pop();
				if(player != null) {
					IPlayerCapabilities playerData = ModCapabilities.getPlayer(player);
					if (playerData != null) {
						drawString(matrixStack, minecraft.fontRenderer, Utils.translateToLocal(Strings.Gui_Menu_Status_Level)+": " + playerData.getLevel(), (int) infoBoxPosX + 4, (int) (infoBoxPosY + 26), 0xFFD900);
						drawString(matrixStack, minecraft.fontRenderer, Utils.translateToLocal(Strings.Gui_Menu_Status_HP)+": " + (int) player.getHealth() + "/" + (int) player.getMaxHealth(), (int) infoBoxPosX + 4, (int) (infoBoxPosY + 26) + minecraft.fontRenderer.FONT_HEIGHT, 0x00FF00);
						drawString(matrixStack, minecraft.fontRenderer, Utils.translateToLocal(Strings.Gui_Menu_Status_MP)+": " + (int) playerData.getMP() + "/" + (int) playerData.getMaxMP(), (int) infoBoxPosX + 4, (int) (infoBoxPosY + 26) + (minecraft.fontRenderer.FONT_HEIGHT * 2), 0x4444FF);
					}
				}
			}
			matrixStack.pop();
		}
		matrixStack.pop();
	}
	
}
