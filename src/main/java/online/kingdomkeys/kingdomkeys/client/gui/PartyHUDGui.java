package online.kingdomkeys.kingdomkeys.client.gui;

import java.util.ArrayList;
import java.util.List;

import com.mojang.blaze3d.systems.RenderSystem;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.network.play.NetworkPlayerInfo;
import net.minecraft.client.resources.DefaultPlayerSkin;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.capability.IPlayerCapabilities;
import online.kingdomkeys.kingdomkeys.capability.IWorldCapabilities;
import online.kingdomkeys.kingdomkeys.capability.ModCapabilities;
import online.kingdomkeys.kingdomkeys.lib.Party;
import online.kingdomkeys.kingdomkeys.lib.Party.Member;

//TODO cleanup + comments
public class PartyHUDGui extends Screen {
	int hpBarWidth;
	int guiHeight = 10;

	int counter = 0;

	public PartyHUDGui() {
		super(new TranslationTextComponent(""));
		minecraft = Minecraft.getInstance();
	}
	
	public ResourceLocation getLocationSkin(PlayerEntity player) {
		NetworkPlayerInfo networkplayerinfo = Minecraft.getInstance().getConnection().getPlayerInfo(player.getUniqueID());
		return networkplayerinfo == null ? DefaultPlayerSkin.getDefaultSkin(player.getUniqueID()) : networkplayerinfo.getLocationSkin();
	}

	@SubscribeEvent
	public void onRenderOverlayPost(RenderGameOverlayEvent event) {
		PlayerEntity player = minecraft.player;
		if (event.getType().equals(RenderGameOverlayEvent.ElementType.HEALTH) && event.isCancelable()) {
			// if (!MainConfig.client.hud.EnableHeartsOnHUD)
			// event.setCanceled(true);
		}
		
		if (event.getType() == RenderGameOverlayEvent.ElementType.TEXT) {
			int screenWidth = minecraft.getMainWindow().getScaledWidth();
			int screenHeight = minecraft.getMainWindow().getScaledHeight();

			float scale = 0.5f;
			
			IWorldCapabilities worldData = ModCapabilities.getWorld(minecraft.world);
			Party p = worldData.getPartyFromMember(player.getUniqueID());
			if(p == null) {
				return;
			}
			
			List<Member> allies = new ArrayList<Member>();
			allies.clear();
			for(Member m : p.getMembers()) {
				if(!m.getUUID().equals(player.getUniqueID())) {
					allies.add(m);
				}
			}
			
			for(int i=0;i<allies.size();i++) {
				Member ally = allies.get(i);
				if(player.world.getPlayerByUuid(ally.getUUID()) != null){
					PlayerEntity playerAlly = player.world.getPlayerByUuid(ally.getUUID());
					renderFace(playerAlly,screenWidth, screenHeight, scale, i);
				} else {
					// When player in party but not loaded into this client
					renderFace(null,screenWidth, screenHeight, scale, i);
				}
			}
		}
	}
	
	public void renderFace(PlayerEntity playerAlly, float screenWidth, float screenHeight, float scale, int i) {
		ResourceLocation skin;
		if(playerAlly != null) {
			skin = getLocationSkin(playerAlly);
		} else {
			skin = new ResourceLocation("minecraft","textures/entity/steve.png");
		}
		minecraft.getTextureManager().bindTexture(skin);

		RenderSystem.pushMatrix();
		{
			RenderSystem.translatef(-16, -screenHeight/4*(i+1), 0);

			// HEAD
			int headWidth = 32;
			int headHeight = 32;
			float headPosX = 0;
			float headPosY = 0;
			float scaledHeadPosX = headPosX * scale;
			float scaledHeadPosY = headPosY * scale;

			RenderSystem.pushMatrix();
			{
				RenderSystem.translatef((screenWidth - headWidth * scale) - scaledHeadPosX, (screenHeight - headHeight * scale) - scaledHeadPosY, 0);
				RenderSystem.scalef(scale, scale, scale);
				if(playerAlly == null)
					RenderSystem.color4f(0.2F,0.2F,0.2F,1F);

				this.blit(0, 0, 32, 32, headWidth, headHeight);
			}
			RenderSystem.popMatrix();

			// HAT
			int hatWidth = 32;
			int hatHeight = 32;
			float hatPosX = 0;
			float hatPosY = 0;
			float scaledHatPosX = hatPosX * scale;
			float scaledHatPosY = hatPosY * scale;

			RenderSystem.pushMatrix();
			{
				RenderSystem.translatef((screenWidth - hatWidth * scale) - scaledHatPosX, (screenHeight - hatHeight * scale) - scaledHatPosY, 0);
				RenderSystem.scalef(scale, scale, scale);
				this.blit(0, 0, 160, 32, hatWidth, hatHeight);
			}
			RenderSystem.popMatrix();
			
				RenderSystem.pushMatrix();
				{
					RenderSystem.translatef((screenWidth - hatWidth * scale) - scaledHatPosX, (screenHeight - hatHeight * scale) - scaledHatPosY, 0);
					RenderSystem.scalef(scale, scale, scale);
					String name = playerAlly == null ? "Out of range" : playerAlly.getDisplayName().getFormattedText();
					drawCenteredString(minecraft.fontRenderer, name, 16, -10, 0xFFFFFF);
				}
				RenderSystem.popMatrix();
				
			if(playerAlly != null) {
				RenderSystem.translatef((screenWidth - headWidth * scale) - scaledHeadPosX, (screenHeight - headHeight * scale) - scaledHeadPosY, 0);
				//HP
				float val = playerAlly.getHealth();
				float max = playerAlly.getMaxHealth();
				minecraft.textureManager.bindTexture(new ResourceLocation(KingdomKeys.MODID, "textures/gui/hpbar.png"));
				RenderSystem.translatef(-4, 0, 1);
				//Top
				RenderSystem.pushMatrix();
				{
					RenderSystem.scalef(scale/3*2, scale, 1);
					this.blit(0, 0, 0, 71, 12, 2);
				}
				RenderSystem.popMatrix();
				//Middle
				RenderSystem.pushMatrix();
				{
					RenderSystem.translatef(0, 1, 1);
					RenderSystem.scalef(scale/3*2, scale*28, 1);
					this.blit(0, 0, 0, 73, 12, 1);
				}
				RenderSystem.popMatrix();
				//Bottom
				RenderSystem.pushMatrix();
				{
					RenderSystem.translatef(0, 30, 1);
					RenderSystem.scalef(scale/3*2, scale, 1);
					this.blit(0, -30, 0, 71, 12, 2);
				}
				RenderSystem.popMatrix();
			
				//Bar
				RenderSystem.pushMatrix();
				{
					RenderSystem.rotatef(180, 0, 0, 1);
					RenderSystem.translatef(-4, -15, 1);
					RenderSystem.scalef(scale/3*2, (scale*28) * val/max, 1);
					this.blit(0, 0, 0, 77, 12, 1);
				}
				RenderSystem.popMatrix();
				
				//MP
				IPlayerCapabilities playerData = ModCapabilities.getPlayer(playerAlly);
				if(playerData != null) {
					val = (float) playerData.getMP();
					max = (float) playerData.getMaxMP();
					minecraft.textureManager.bindTexture(new ResourceLocation(KingdomKeys.MODID, "textures/gui/mpbar.png"));
					RenderSystem.translatef(20, 0, 1);
					//Top
					RenderSystem.pushMatrix();
					{
						RenderSystem.scalef(scale/3*2, scale, 1);
						this.blit(0, 0, 0, 58, 12, 2);
					}
					RenderSystem.popMatrix();
					//Middle
					RenderSystem.pushMatrix();
					{
						RenderSystem.translatef(0, 1, 1);
						RenderSystem.scalef(scale/3*2, scale*28, 1);
						this.blit(0, 0, 0, 60, 12, 1);
					}
					RenderSystem.popMatrix();
					//Bottom
					RenderSystem.pushMatrix();
					{
						RenderSystem.translatef(0, 30, 1);
						RenderSystem.scalef(scale/3*2, scale, 1);
						this.blit(0, -30, 0, 58, 12, 2);
					}
					RenderSystem.popMatrix();
				
					//Bar
					RenderSystem.pushMatrix();
					{
						RenderSystem.rotatef(180, 0, 0, 1);
						RenderSystem.translatef(-4, -15, 1);
						RenderSystem.scalef(scale/3*2, (scale*28) * val/max, 1);
						this.blit(0, 0, 0, 64, 12, 1);
					}
					RenderSystem.popMatrix();
				}
			}
		}
		RenderSystem.popMatrix();
	}

}
