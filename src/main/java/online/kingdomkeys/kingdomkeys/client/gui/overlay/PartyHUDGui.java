package online.kingdomkeys.kingdomkeys.client.gui.overlay;

import java.util.ArrayList;
import java.util.List;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.multiplayer.PlayerInfo;
import net.minecraft.client.resources.DefaultPlayerSkin;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.client.gui.overlay.ForgeGui;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.capability.IPlayerCapabilities;
import online.kingdomkeys.kingdomkeys.capability.IWorldCapabilities;
import online.kingdomkeys.kingdomkeys.capability.ModCapabilities;
import online.kingdomkeys.kingdomkeys.config.ModConfigs;
import online.kingdomkeys.kingdomkeys.lib.Party;
import online.kingdomkeys.kingdomkeys.lib.Party.Member;

//TODO cleanup + comments
public class PartyHUDGui extends OverlayBase {

	public static final PartyHUDGui INSTANCE = new PartyHUDGui();
	int hpBarWidth;
	int guiHeight = 10;

	int counter = 0;

	private PartyHUDGui() {
		super();
	}

	public ResourceLocation getLocationSkin(Player player) {
		PlayerInfo networkplayerinfo = Minecraft.getInstance().getConnection().getPlayerInfo(player.getUUID());
		return networkplayerinfo == null ? DefaultPlayerSkin.getDefaultSkin(player.getUUID()) : networkplayerinfo.getSkinLocation();
	}

	@Override
	public void render(ForgeGui gui, GuiGraphics guiGraphics, float partialTick, int width, int height) {
		super.render(gui, guiGraphics, partialTick, width, height);
		Player player = minecraft.player;

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

		PoseStack poseStack = guiGraphics.pose();

		poseStack.pushPose();
		{
			poseStack.translate(ModConfigs.partyXPos, ModConfigs.partyYPos - 100, 0);
			for (int i = 0; i < allies.size(); i++) {
				Member member = allies.get(i);
				Player playerAlly = player.level().getPlayerByUUID(member.getUUID());
				renderFace(guiGraphics, playerAlly, screenWidth, screenHeight, scale, i);
			}
		}
		poseStack.popPose();
	}

	public void renderFace(GuiGraphics gui, Player playerAlly, float screenWidth, float screenHeight, float scale, int i) {
		ResourceLocation skin;
		if (playerAlly != null) {
			skin = getLocationSkin(playerAlly);
		} else {
			skin = new ResourceLocation("minecraft", "textures/entity/player/wide/steve.png");
		}

		PoseStack matrixStack = gui.pose();

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
					RenderSystem.setShaderColor(0.2F, 0.2F, 0.2F, 1F);
				} else {
					RenderSystem.setShaderColor(1F, 1F, 1F, 1F);
				}

				this.blit(gui, skin, 0, 0, 32, 32, headWidth, headHeight);
				RenderSystem.setShaderColor(1F, 1F, 1F, 1F);

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
				this.blit(gui, skin, 0, 0, 160, 32, hatWidth, hatHeight);
			}
			matrixStack.popPose();

			matrixStack.pushPose();
			{
				matrixStack.translate((screenWidth - hatWidth * scale) - scaledHatPosX, (screenHeight - hatHeight * scale) - scaledHatPosY, 0);
				matrixStack.scale(scale, scale, scale);
				String name = playerAlly == null ? "Out of range" : playerAlly.getDisplayName().getString();
				if (playerAlly != null && minecraft.player.distanceTo(playerAlly) >= ModConfigs.partyRangeLimit)
					drawCenteredString(gui, minecraft.font, "Out of range", 16, -20, 0xFFFFFF);
				drawCenteredString(gui, minecraft.font, name, 16, -10, 0xFFFFFF);
			}
			matrixStack.popPose();

			if (playerAlly != null) {
				matrixStack.translate((screenWidth - headWidth * scale) - scaledHeadPosX, (screenHeight - headHeight * scale) - scaledHeadPosY, 0);
				// HP
				float val = playerAlly.getHealth();
				float max = playerAlly.getMaxHealth();
				ResourceLocation hptexture = new ResourceLocation(KingdomKeys.MODID, "textures/gui/hpbar.png");
				matrixStack.translate(-4, 0, 1);
				// Top
				matrixStack.pushPose();
				{
					matrixStack.scale(scale / 3 * 2, scale, 1);
					this.blit(gui, hptexture, 0, 0, 0, 72, 12, 2);
				}
				matrixStack.popPose();
				// Middle
				matrixStack.pushPose();
				{
					matrixStack.translate(0, 1, 1);
					matrixStack.scale(scale / 3 * 2, scale * 28, 1);
					this.blit(gui, hptexture, 0, 0, 0, 74, 12, 1);
				}
				matrixStack.popPose();
				// Bottom
				matrixStack.pushPose();
				{
					matrixStack.translate(0, 30, 1);
					matrixStack.scale(scale / 3 * 2, scale, 1);
					this.blit(gui, hptexture, 0, -30, 0, 72, 12, 2);
				}
				matrixStack.popPose();

				// Bar
				matrixStack.pushPose();
				{
					matrixStack.mulPose(Axis.ZP.rotationDegrees(180));
					matrixStack.translate(-4, -15, 1);
					matrixStack.scale(scale * 0.66F, (scale * 28) * val / max, 1);
					this.blit(gui, hptexture, 0, 0, 0, 78, 12, 1);
				}
				matrixStack.popPose();

				// MP
				IPlayerCapabilities playerData = ModCapabilities.getPlayer(playerAlly);
				if (playerData != null) {
					val = (float) playerData.getMP();
					max = (float) playerData.getMaxMP();
					ResourceLocation mptexture = new ResourceLocation(KingdomKeys.MODID, "textures/gui/mpbar.png");
					matrixStack.translate(20, 0, 1);
					// Top
					matrixStack.pushPose();
					{
						matrixStack.scale(scale / 3 * 2, scale, 1);
						this.blit(gui, mptexture, 0, 0, 0, 58, 12, 2);
					}
					matrixStack.popPose();
					// Middle
					matrixStack.pushPose();
					{
						matrixStack.translate(0, 1, 1);
						matrixStack.scale(scale / 3 * 2, scale * 28, 1);
						this.blit(gui, mptexture, 0, 0, 0, 60, 12, 1);
					}
					matrixStack.popPose();
					// Bottom
					matrixStack.pushPose();
					{
						matrixStack.translate(0, 30, 1);
						matrixStack.scale(scale / 3 * 2, scale, 1);
						this.blit(gui, mptexture, 0, -30, 0, 58, 12, 2);
					}
					matrixStack.popPose();

					// Bar
					matrixStack.pushPose();
					{
						matrixStack.mulPose(Axis.ZP.rotationDegrees(180));
						matrixStack.translate(-4, -15, 1);
						matrixStack.scale(scale / 3 * 2, (scale * 28) * val / max, 1);
						this.blit(gui, mptexture, 0, 0, 0, 64, 12, 1);
					}
					matrixStack.popPose();
				}
			}
		}
		matrixStack.popPose();
	}

}
