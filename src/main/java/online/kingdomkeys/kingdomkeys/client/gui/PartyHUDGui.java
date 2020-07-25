package online.kingdomkeys.kingdomkeys.client.gui;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
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
import online.kingdomkeys.kingdomkeys.capability.ExtendedWorldData;
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
			/*switch (minecraft.gameSettings.guiScale) {
			case Constants.SCALE_AUTO:
				scale = 0.85f;
				break;
			case Constants.SCALE_NORMAL:
				scale = 0.85f;
				break;
			default:
				scale = 0.65f;
				break;
			}*/
			ExtendedWorldData worldData = ExtendedWorldData.get(player.world);
			Party p = worldData.getPartyFromMember(player.getUniqueID());
			if(p == null) {
				return;
			}
			
			List<Member> allies = new ArrayList<Member>();
			allies.clear();
			for(Member m : p.getMembers()) {
				System.out.println(m.getUsername());
				if(!m.getUUID().equals(player.getUniqueID())) {
					allies.add(m);
				}
			}
			for(int i=0;i<allies.size();i++) {
				Member ally = allies.get(i);
				//GameProfile gp = player.world.getPlayerByUuid(ally.getUUID()).getGameProfile();
	            //GameProfile gp = new GameProfile(ally.getUUID(), ally.getUsername());
	          /*  Collection<Property> test = gp.getProperties().get("textures");
	            Iterator<Property> it = test.iterator();
	            while(it.hasNext()) {
	            	//System.out.println(it.next().getValue());
	            }*/
			//	String skinURL = "http://media.discordapp.net/attachments/182573160313389058/736353607196344430/5ddb42d4d6b174f319ae0eaeb288cceb96428d2e10b7853d419b6afed6ea0069.png";// "http://textures.minecraft.net/texture/5ddb42d4d6b174f319ae0eaeb288cceb96428d2e10b7853d419b6afed6ea0069";

				ResourceLocation skin = getLocationSkin(minecraft.world.getPlayerByUuid(ally.getUUID()));
				//ResourceLocation skin = new ResourceLocation(skinURL);
				minecraft.getTextureManager().bindTexture(skin);
				RenderSystem.pushMatrix();
				{
					RenderSystem.translatef(-10, -screenHeight/4*1, 0);
	
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
	
				}
				RenderSystem.popMatrix();
			}
		}
	}

}
