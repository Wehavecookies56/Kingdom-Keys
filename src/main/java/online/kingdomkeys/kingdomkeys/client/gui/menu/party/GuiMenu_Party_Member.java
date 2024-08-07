package online.kingdomkeys.kingdomkeys.client.gui.menu.party;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
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
import org.jetbrains.annotations.NotNull;

import java.awt.*;

public class GuiMenu_Party_Member extends MenuBackground {

	MenuButton back, leave;
		
	final IPlayerCapabilities playerData = ModCapabilities.getPlayer(minecraft.player);
	IWorldCapabilities worldData;
	Party party;

	final ResourceLocation texture = new ResourceLocation(KingdomKeys.MODID, "textures/gui/menu/menu_button.png");
	
	public GuiMenu_Party_Member() {
		super(Strings.Gui_Menu_Party, new Color(0,0,255));
		drawPlayerInfo = true;
		worldData = ModCapabilities.getWorld(minecraft.level);
		party = worldData.getPartyFromMember(minecraft.player.getUUID());
	}

	protected void action(String string) {
		switch(string) {
		case "back":
			GuiHelper.openMenu();
			break;		
		case "leave":
			PacketHandler.sendToServer(new CSPartyLeave(party, minecraft.player.getUUID()));
			minecraft.level.playSound(minecraft.player, minecraft.player.blockPosition(), ModSounds.menu_in.get(), SoundSource.MASTER, 1.0f, 1.0f);
			minecraft.setScreen(new GuiMenu_Party_None());
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
		this.renderables.clear();
		
		party = worldData.getPartyFromMember(minecraft.player.getUUID());
		if(party == null) {
			GuiHelper.openMenu();
		} else {			
			if(party.getMember(minecraft.player.getUUID()).isLeader()) {
				minecraft.setScreen(new GuiMenu_Party_Leader());
				return;
			}
		}
		
		float topBarHeight = (float) height * 0.17F;
		int button_statsY = (int) topBarHeight + 5;
		float buttonPosX = (float) width * 0.03F;
		float buttonWidth = ((float) width * 0.1744F) - 20;

		addRenderableWidget(leave = new MenuButton((int) buttonPosX, button_statsY + (0 * 18), (int) buttonWidth, Utils.translateToLocal(Strings.Gui_Menu_Party_Member_Leave), ButtonType.BUTTON, (e) -> { action("leave"); }));
		addRenderableWidget(back = new MenuButton((int) buttonPosX, button_statsY + (1 * 18), (int) buttonWidth, Utils.translateToLocal(Strings.Gui_Menu_Back), ButtonType.BUTTON, (e) -> { action("back"); }));
		
		updateButtons();
	}

	@Override
	public void render(@NotNull GuiGraphics gui, int mouseX, int mouseY, float partialTicks) {
		PoseStack matrixStack = gui.pose();
		super.render(gui, mouseX, mouseY, partialTicks);
		worldData = ModCapabilities.getWorld(minecraft.level);
		party = worldData.getPartyFromMember(minecraft.player.getUUID());
		
		if(party == null) {
			GuiHelper.openMenu();
			return;
		} else {			
			if(party.getMember(minecraft.player.getUUID()).isLeader()) {
				minecraft.setScreen(new GuiMenu_Party_Leader());
				return;
			}
			
			matrixStack.pushPose();
			{
				matrixStack.scale(1.5F,1.5F, 1);
				gui.drawString(minecraft.font, "["+party.getMembers().size()+"/"+party.getSize()+"] "+party.getName(), (int) (topLeftBarWidth + topGap) + 5, 10, 0xFF9900);
			}
			matrixStack.popPose();
			drawParty(gui);
		}
	}
	
	public void drawParty(GuiGraphics gui) {
		gui.pose().pushPose();
		{
			for(int i=0;i<party.getMembers().size();i++) {
				Member member = party.getMembers().get(i);
				drawPlayer(gui, i, member);
			}
			
		}
		gui.pose().popPose();
	}
	
	public void drawPlayer(GuiGraphics gui, int order, Member member) {
		
		PoseStack matrixStack = gui.pose();
		byte partySize = (byte)ModCapabilities.getWorld(Minecraft.getInstance().player.level()).getPartyFromMember(member.getUUID()).getMembers().size();
		
		float space = 0.18F;
			space *= 2F/partySize;
		
		float playerHeight = height * 0.45F;

		float playerPosX = 140F+ (space *(order)*1000);
		float playerPosY = height * 0.7F;
		
		Player player = Utils.getPlayerByName(minecraft.level, member.getUsername());
		
				
		matrixStack.pushPose();
		{
			matrixStack.translate(playerPosX,playerPosY,0);

			if(partySize > 4) {
				matrixStack.translate(0,-2.5F,0);

				matrixStack.scale(5F/partySize,5F/partySize,1F);
			}
			
	
			matrixStack.pushPose();
			{
				RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
				if(member != null && player != null) {
					matrixStack.pushPose();
				    InventoryScreen.renderEntityInInventoryFollowsMouse(gui, 0,0, (int) playerHeight / 2, 0,0, player);
					matrixStack.popPose();
				}
				RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 0.75F);
			}
			matrixStack.popPose();
			matrixStack.pushPose();
				RenderSystem.setShaderColor(1, 1, 1, 1);
				matrixStack.translate(-playerPosX, -playerPosY, 100);
				
				RenderSystem.enableBlend();
				int infoBoxWidth = (int) ((width * 0.1385F) - 14); // This might be wrong cuz I had to convert from float to int
				int infoBoxPosX = (int) (105F+ (space * (order) * 1000));
				int infoBoxPosY = (int) (height * 0.54F);
				gui.blit(texture, infoBoxPosX, infoBoxPosY, 123, 67, 12, 22);
				for (int i = 0; i < infoBoxWidth; i++) {
					gui.blit(texture, infoBoxPosX + 10 + i, infoBoxPosY, 136, 67, 2, 22);
				}
				gui.blit(texture, infoBoxPosX + 10 + infoBoxWidth, infoBoxPosY, 137, 67, 3, 22);
				gui.blit(texture, infoBoxPosX, infoBoxPosY + 22, 123, 90, 4, 35);
				for (int i = 0; i < infoBoxWidth + 8; i++) {
					gui.blit(texture, infoBoxPosX + 2 + i, infoBoxPosY + 22, 128, 90, 2, 35);
				}
				gui.blit(texture, infoBoxPosX + 2 + infoBoxWidth + 8, infoBoxPosY + 22, 129, 90, 3, 35);
				
				RenderSystem.disableBlend();
			matrixStack.popPose();
			matrixStack.pushPose();
			{
				matrixStack.translate(-playerPosX, -playerPosY, 100);
				
				matrixStack.pushPose();
				{
					matrixStack.translate((int) infoBoxPosX + 8, (int) infoBoxPosY + ((22 / 2) - (minecraft.font.lineHeight / 2)), 1);
					// matrixStack.scale(0.75F, 0.75F, 1);
					gui.drawString(minecraft.font, member.getUsername(), 0, 0, 0xFFFFFF);
				}
				matrixStack.popPose();
				if(player != null) {
					IPlayerCapabilities playerData = ModCapabilities.getPlayer(player);
					if (playerData != null) {
						gui.drawString(minecraft.font, Utils.translateToLocal(Strings.Gui_Menu_Status_Level)+": " + playerData.getLevel(), (int) infoBoxPosX + 4, (int) (infoBoxPosY + 26), 0xFFD900);
						gui.drawString(minecraft.font, Utils.translateToLocal(Strings.Gui_Menu_Status_HP)+": " + (int) player.getHealth() + "/" + (int) player.getMaxHealth(), (int) infoBoxPosX + 4, (int) (infoBoxPosY + 26) + minecraft.font.lineHeight, 0x00FF00);
						gui.drawString(minecraft.font, Utils.translateToLocal(Strings.Gui_Menu_Status_MP)+": " + (int) playerData.getMP() + "/" + (int) playerData.getMaxMP(), (int) infoBoxPosX + 4, (int) (infoBoxPosY + 26) + (minecraft.font.lineHeight * 2), 0x4444FF);
					}
				}
			}
			matrixStack.popPose();
		}
		matrixStack.popPose();
	}
	
}
