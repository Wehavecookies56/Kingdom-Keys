package online.kingdomkeys.kingdomkeys.client.gui;

import java.util.ArrayList;
import java.util.List;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.multiplayer.PlayerInfo;
import net.minecraft.client.resources.DefaultPlayerSkin;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.capability.IPlayerCapabilities;
import online.kingdomkeys.kingdomkeys.capability.IWorldCapabilities;
import online.kingdomkeys.kingdomkeys.capability.ModCapabilities;
import online.kingdomkeys.kingdomkeys.config.ModConfigs;
import online.kingdomkeys.kingdomkeys.lib.Party;
import online.kingdomkeys.kingdomkeys.lib.Party.Member;

//TODO cleanup + comments
public class PartyHUDGui extends Screen {
	int hpBarWidth;
	int guiHeight = 10;

	int counter = 0;

	public PartyHUDGui() {
		super(new TranslatableComponent(""));
		minecraft = Minecraft.getInstance();
	}

	public ResourceLocation getLocationSkin(Player player) {
		PlayerInfo networkplayerinfo = Minecraft.getInstance().getConnection().getPlayerInfo(player.getUUID());
		return networkplayerinfo == null ? DefaultPlayerSkin.getDefaultSkin(player.getUUID()) : networkplayerinfo.getSkinLocation();
	}

	@SubscribeEvent
	public void onRenderOverlayPost(RenderGameOverlayEvent event) {
		Player player = minecraft.player;
		if (event.getType().equals(RenderGameOverlayEvent.ElementType.HEALTH) && event.isCancelable()) {
			// if (!MainConfig.client.hud.EnableHeartsOnHUD)
			// event.setCanceled(true);
		}

		PoseStack matrixStack = event.getMatrixStack();
		if (event.getType() == RenderGameOverlayEvent.ElementType.TEXT) {
			int screenWidth = minecraft.getWindow().getGuiScaledWidth();
			int screenHeight = minecraft.getWindow().getGuiScaledHeight();

			float scale = 0.5f;

			IWorldCapabilities worldData = ModCapabilities.getWorld(minecraft.level);
			Party p = worldData.getPartyFromMember(player.getUUID());
			if (p == null) {
				return;
			}

			List<Member> allies = new ArrayList<Member>();
			allies.clear();
			for (Member m : p.getMembers()) {
				if (!m.getUUID().equals(player.getUUID())) {
					allies.add(m);
				}
			}

			matrixStack.pushPose();
			{
				matrixStack.translate(ModConfigs.partyXPos, ModConfigs.partyYPos - 100, 0);
				for (int i = 0; i < allies.size(); i++) {
					Member member = allies.get(i);
					Player playerAlly = player.level.getPlayerByUUID(member.getUUID());
					renderFace(matrixStack, playerAlly, screenWidth, screenHeight, scale, i);
				}
			}
			matrixStack.popPose();
		}
	}

	public void renderFace(PoseStack matrixStack, Player playerAlly, float screenWidth, float screenHeight, float scale, int i) {
		ResourceLocation skin;
		if (playerAlly != null) {
			skin = getLocationSkin(playerAlly);
		} else {
			skin = new ResourceLocation("minecraft", "textures/entity/steve.png");
		}
		minecraft.getTextureManager().bindForSetup(skin);

		matrixStack.pushPose();
		{			
			
			matrixStack.translate(-16, -screenHeight / 4 * (i * ModConfigs.partyYDistance / 100f), 0);

			// HEAD
			int headWidth = 32;
			int headHeight = 32;
			float headPosX = 0;
			float headPosY = 0;
			float scaledHeadPosX = headPosX * scale;
			float scaledHeadPosY = headPosY * scale;

			matrixStack.pushPose();
			{
				matrixStack.translate((screenWidth - headWidth * scale) - scaledHeadPosX, (screenHeight - headHeight * scale) - scaledHeadPosY, 0);
				matrixStack.scale(scale, scale, scale);
				if (playerAlly == null) {
					RenderSystem.color4f(0.2F, 0.2F, 0.2F, 1F);
				} else {
					RenderSystem.color4f(1F, 1F, 1F, 1F);
				}

				this.blit(matrixStack, 0, 0, 32, 32, headWidth, headHeight);
			}
			matrixStack.popPose();

			// HAT
			int hatWidth = 32;
			int hatHeight = 32;
			float hatPosX = 0;
			float hatPosY = 0;
			float scaledHatPosX = hatPosX * scale;
			float scaledHatPosY = hatPosY * scale;

			matrixStack.pushPose();
			{
				matrixStack.translate((screenWidth - hatWidth * scale) - scaledHatPosX, (screenHeight - hatHeight * scale) - scaledHatPosY, 0);
				matrixStack.scale(scale, scale, scale);
				this.blit(matrixStack, 0, 0, 160, 32, hatWidth, hatHeight);
			}
			matrixStack.popPose();

			matrixStack.pushPose();
			{
				matrixStack.translate((screenWidth - hatWidth * scale) - scaledHatPosX, (screenHeight - hatHeight * scale) - scaledHatPosY, 0);
				matrixStack.scale(scale, scale, scale);
				String name = playerAlly == null ? "Out of range" : playerAlly.getDisplayName().getString();
				if (playerAlly != null && minecraft.player.distanceTo(playerAlly) >= ModConfigs.partyRangeLimit)
					drawCenteredString(matrixStack, minecraft.font, "Out of range", 16, -20, 0xFFFFFF);
				drawCenteredString(matrixStack, minecraft.font, name, 16, -10, 0xFFFFFF);
			}
			matrixStack.popPose();

			if (playerAlly != null) {
				matrixStack.translate((screenWidth - headWidth * scale) - scaledHeadPosX, (screenHeight - headHeight * scale) - scaledHeadPosY, 0);
				// HP
				float val = playerAlly.getHealth();
				float max = playerAlly.getMaxHealth();
				minecraft.textureManager.bindForSetup(new ResourceLocation(KingdomKeys.MODID, "textures/gui/hpbar.png"));
				matrixStack.translate(-4, 0, 1);
				// Top
				matrixStack.pushPose();
				{
					matrixStack.scale(scale / 3 * 2, scale, 1);
					this.blit(matrixStack, 0, 0, 0, 72, 12, 2);
				}
				matrixStack.popPose();
				// Middle
				matrixStack.pushPose();
				{
					matrixStack.translate(0, 1, 1);
					matrixStack.scale(scale / 3 * 2, scale * 28, 1);
					this.blit(matrixStack, 0, 0, 0, 74, 12, 1);
				}
				matrixStack.popPose();
				// Bottom
				matrixStack.pushPose();
				{
					matrixStack.translate(0, 30, 1);
					matrixStack.scale(scale / 3 * 2, scale, 1);
					this.blit(matrixStack, 0, -30, 0, 72, 12, 2);
				}
				matrixStack.popPose();

				// Bar
				matrixStack.pushPose();
				{
					matrixStack.mulPose(Vector3f.ZP.rotation((float) Math.toRadians(180)));
					matrixStack.translate(-4, -15, 1);
					matrixStack.scale(scale * 0.66F, (scale * 28) * val / max, 1);
					this.blit(matrixStack, 0, 0, 0, 78, 12, 1);
				}
				matrixStack.popPose();

				// MP
				IPlayerCapabilities playerData = ModCapabilities.getPlayer(playerAlly);
				if (playerData != null) {
					val = (float) playerData.getMP();
					max = (float) playerData.getMaxMP();
					minecraft.textureManager.bindForSetup(new ResourceLocation(KingdomKeys.MODID, "textures/gui/mpbar.png"));
					matrixStack.translate(20, 0, 1);
					// Top
					matrixStack.pushPose();
					{
						matrixStack.scale(scale / 3 * 2, scale, 1);
						this.blit(matrixStack, 0, 0, 0, 58, 12, 2);
					}
					matrixStack.popPose();
					// Middle
					matrixStack.pushPose();
					{
						matrixStack.translate(0, 1, 1);
						matrixStack.scale(scale / 3 * 2, scale * 28, 1);
						this.blit(matrixStack, 0, 0, 0, 60, 12, 1);
					}
					matrixStack.popPose();
					// Bottom
					matrixStack.pushPose();
					{
						matrixStack.translate(0, 30, 1);
						matrixStack.scale(scale / 3 * 2, scale, 1);
						this.blit(matrixStack, 0, -30, 0, 58, 12, 2);
					}
					matrixStack.popPose();

					// Bar
					matrixStack.pushPose();
					{
						matrixStack.mulPose(Vector3f.ZP.rotation((float) Math.toRadians(180)));
						matrixStack.translate(-4, -15, 1);
						matrixStack.scale(scale / 3 * 2, (scale * 28) * val / max, 1);
						this.blit(matrixStack, 0, 0, 0, 64, 12, 1);
					}
					matrixStack.popPose();
				}
			}
		}
		matrixStack.popPose();
	}

}
