package online.kingdomkeys.kingdomkeys.client.gui.overlay;

import com.mojang.authlib.GameProfile;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.multiplayer.PlayerInfo;
import net.minecraft.client.player.RemotePlayer;
import net.minecraft.client.resources.DefaultPlayerSkin;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.client.ClientUtils;
import online.kingdomkeys.kingdomkeys.config.ModConfigs;
import online.kingdomkeys.kingdomkeys.data.PlayerData;
import online.kingdomkeys.kingdomkeys.data.WorldData;
import online.kingdomkeys.kingdomkeys.lib.Party;
import online.kingdomkeys.kingdomkeys.lib.Party.Member;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

//TODO cleanup + comments
public class PartyHUDGui extends OverlayBase {

	public static final PartyHUDGui INSTANCE = new PartyHUDGui();

	private PartyHUDGui() {
		super();
	}

	public ResourceLocation getLocationSkin(Player player) {
		PlayerInfo networkplayerinfo = Minecraft.getInstance().getConnection().getPlayerInfo(player.getUUID());
		return networkplayerinfo == null ? DefaultPlayerSkin.get(player.getUUID()).texture() : networkplayerinfo.getSkin().texture();
	}

	@Override
	public void render(GuiGraphics guiGraphics, DeltaTracker deltaTracker) {
		super.render(guiGraphics, deltaTracker);
		Player player = minecraft.player;

		int screenWidth = minecraft.getWindow().getGuiScaledWidth();
		int screenHeight = minecraft.getWindow().getGuiScaledHeight();

		float scale = 0.5f;

		WorldData worldData = WorldData.getClient();
		Party p = worldData.getPartyFromMember(player.getUUID());
		if (p == null) {
			return;
		}

		List<Member> allies = new ArrayList<>();
		allies.clear();
		for (Member m : p.getMembers()) {
			if (!m.getUUID().equals(player.getUUID())) {
				allies.add(m);
			}
		}

		ClientUtils.PARTY_ELEMENT.applyTransform(guiGraphics, screenWidth, screenHeight);
		for (int i = 0; i < allies.size(); i++) {
			Member member = allies.get(i);
			Player playerAlly = player.level().getPlayerByUUID(member.getUUID());
			renderFace(guiGraphics, playerAlly, member, scale, i);
		}
		ClientUtils.PARTY_ELEMENT.endTransform(guiGraphics);
	}

	public void renderFace(GuiGraphics gui, Player playerAlly, Member member, float scale, int i) {
		UUID uuid = member.getUUID();
		String name = member.getUsername();

		GameProfile profile = new GameProfile(uuid, name);
		RemotePlayer fakePlayer = new RemotePlayer(Minecraft.getInstance().level, profile);

		ResourceLocation skin = fakePlayer.getSkin().texture();

		PoseStack pose = gui.pose();

		pose.pushPose();
		{
			float spacing = 40 * (ModConfigs.partyYDistance / 100f);
			pose.translate(4, ClientUtils.PARTY_ELEMENT.height - 20 + -i * spacing, 0);
			pose.pushPose();
			{
				pose.scale(scale, scale, 1);

				if (playerAlly == null)
					RenderSystem.setShaderColor(0.2F, 0.2F, 0.2F, 1F);
				else
					RenderSystem.setShaderColor(1F, 1F, 1F, 1F);

				this.blit(gui, skin, 0, 0, 32, 32, 32, 32);
				RenderSystem.setShaderColor(1F, 1F, 1F, 1F);
			}
			pose.popPose();

			pose.pushPose();
			{
				pose.scale(scale, scale, 1);
				this.blit(gui, skin, 0, 0, 160, 32, 32, 32);
			}
			pose.popPose();

			pose.pushPose();
			{
				float center = 16 * scale * 2;
				pose.scale(scale,scale,scale);
				if (playerAlly != null && minecraft.player.distanceTo(playerAlly) >= ModConfigs.SERVER.partyRangeLimit.get()) {
					drawCenteredString(gui, minecraft.font, "Out of range", (int) center, -20, 0xFFFFFF);
				}

				drawCenteredString(gui, minecraft.font, name, (int) center, -10, 0xFFFFFF);
			}
			pose.popPose();

			if (playerAlly != null) {
				//HP
				float barScaleX = scale * 0.66f;
				float barHeight = scale * 28;

				pose.translate(-4, 0, 1);

				float val = playerAlly.getHealth();
				float max = playerAlly.getMaxHealth();

				ResourceLocation hptexture = KingdomKeys.rl("textures/gui/hpbar.png");

				// top
				pose.pushPose();
				{
					pose.scale(barScaleX, scale, 1);
					this.blit(gui, hptexture, 0, 0, 0, 72, 12, 2);
				}
				pose.popPose();

				// middle
				pose.pushPose();
				{
					pose.translate(0, 1, 1);
					pose.scale(barScaleX, barHeight, 1);
					this.blit(gui, hptexture, 0, 0, 0, 74, 12, 1);
				}
				pose.popPose();

				// bottom
				pose.pushPose();
				{
					pose.translate(0, 30, 1);
					pose.scale(barScaleX, scale, 1);
					this.blit(gui, hptexture, 0, -30, 0, 72, 12, 2);
				}
				pose.popPose();

				// HP fill
				pose.pushPose();
				{
					pose.mulPose(Axis.ZP.rotationDegrees(180));
					pose.translate(-4, -15, 1);
					pose.scale(barScaleX, barHeight * val / max, 1);
					this.blit(gui, hptexture, 0, 0, 0, 78, 12, 1);
				}
				pose.popPose();

				//MP
				PlayerData playerData = PlayerData.get(playerAlly);
				if (playerData != null) {

					val = (float) playerData.getMP();
					max = (float) playerData.getMaxMP();

					ResourceLocation mptexture =
							KingdomKeys.rl("textures/gui/mpbar.png");

					pose.translate(20, 0, 1);

					// top
					pose.pushPose();
					{
						pose.scale(barScaleX, scale, 1);
						this.blit(gui, mptexture, 0, 0, 0, 58, 12, 2);
					}
					pose.popPose();

					// middle
					pose.pushPose();
					{
						pose.translate(0, 1, 1);
						pose.scale(barScaleX, barHeight, 1);
						this.blit(gui, mptexture, 0, 0, 0, 60, 12, 1);
					}
					pose.popPose();

					// bottom
					pose.pushPose();
					{
						pose.translate(0, 30, 1);
						pose.scale(barScaleX, scale, 1);
						this.blit(gui, mptexture, 0, -30, 0, 58, 12, 2);
					}
					pose.popPose();

					// MP fill
					pose.pushPose();
					{
						pose.mulPose(Axis.ZP.rotationDegrees(180));
						pose.translate(-4, -15, 1);
						pose.scale(barScaleX, barHeight * val / max, 1);
						this.blit(gui, mptexture, 0, 0, 0, 64, 12, 1);
					}
					pose.popPose();
				}
			}

		}
		pose.popPose();
	}

}
