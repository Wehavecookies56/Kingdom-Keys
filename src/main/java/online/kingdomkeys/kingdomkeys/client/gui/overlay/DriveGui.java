package online.kingdomkeys.kingdomkeys.client.gui.overlay;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.data.ModData;
import online.kingdomkeys.kingdomkeys.config.ModConfigs;
import online.kingdomkeys.kingdomkeys.data.PlayerData;
import online.kingdomkeys.kingdomkeys.driveform.DriveForm;
import online.kingdomkeys.kingdomkeys.lib.Strings;
import online.kingdomkeys.kingdomkeys.util.Utils;
import online.kingdomkeys.kingdomkeys.util.Utils.OrgMember;

import java.awt.*;

public class DriveGui extends OverlayBase {
	
	ResourceLocation texture = ResourceLocation.fromNamespaceAndPath(KingdomKeys.MODID, "textures/gui/drivebar.png");

	public static final DriveGui INSTANCE = new DriveGui();

	private DriveGui() {
		super();
	}

	int maxDrive = 1000;
	int maxLength = 100;
	int maxBars = 9;
	double guiLength = 47D;
	double oneValue = (guiLength / 100D);
	double currDrive;
	double currForm;

	static final int R = 0, G = 1, B = 2;
	int[] colors = { 255, 255, 255 };
	static final float CONS = 0.005F;
	float decimalColor = 0F;
	float previousPartialTick = 0;

	/**
	 * Gets the bar currently in
	 * 
	 * @param dp    drive points
	 * @param level max level unlocked (the one which increases when leveling the drive forms up to level 7
	 * @return
	 */
	public int getCurrBar(double dp, int level) {
		int bar = (int) dp / 100;
		if (bar > level)
			bar = level;
		return bar;
	}

	@Override
	public void render(GuiGraphics guiGraphics, DeltaTracker deltaTracker) {
		super.render(guiGraphics, deltaTracker);
		/*
		 * if (!MainConfig.displayGUI()) return; if
		 * (!mc.player.getCapability(ModCapabilities.PLAYER_STATS, null).getHudMode())
		 * return;
		 */
		PlayerData playerData = PlayerData.get(minecraft.player);
		if (playerData != null) {
			double dp = playerData.getDP();
			double fp = playerData.getFP();

			currDrive = (float) ((oneValue * dp) - getCurrBar(dp, (int) playerData.getMaxDP() / 100) * guiLength);

			if (playerData.getDriveFormMap() != null && playerData.getActiveDriveForm() != null && !playerData.getActiveDriveForm().equals(DriveForm.NONE.toString())) {
				if (playerData.getActiveDriveForm().equals(Strings.Form_Anti)) {//Antiform FP calculation
					currForm = (float) ((oneValue * fp) - getCurrBar(fp, 1000) * guiLength);
				} else {
					currForm = (float) ((oneValue * fp) - getCurrBar(fp, 300 + (playerData.getDriveFormMap().get(playerData.getActiveDriveForm())[0] * 100)) * guiLength);
				}
			}

			if (dp == playerData.getMaxDP()) {
				currDrive = guiLength;
			}

			int guiWidth = 95;
			int guiBarWidth = 83;
			int guiHeight = 18;
			int screenWidth = minecraft.getWindow().getGuiScaledWidth();
			int screenHeight = minecraft.getWindow().getGuiScaledHeight();

			float rawScale = 0.85f;
			float scaleX = rawScale * ModConfigs.dpXScale/100F;
			float scaleY = rawScale * ModConfigs.dpYScale/100F;
			float posX = 52 * scaleX;
			float posY = 20 * scaleY + 2;

			PoseStack poseStack = guiGraphics.pose();

			poseStack.pushPose();
			{
				RenderSystem.setShaderColor(1, 1, 1, 1);
				RenderSystem.enableBlend();
				poseStack.translate(-27 + ModConfigs.dpXPos, -2 + ModConfigs.dpYPos, 1);

				// Background
				poseStack.pushPose();
				{
					poseStack.translate((screenWidth - guiWidth * scaleX) - posX, (screenHeight - guiHeight * scaleY) - posY, 0);
					poseStack.scale(scaleX, scaleY, 1);

					if(playerData.getAlignment() == OrgMember.NONE) {
						if (playerData.getActiveDriveForm().equals(DriveForm.NONE.toString())) {
							this.blit(guiGraphics, texture, 15, 6, 0, 0, guiWidth, guiHeight);
						} else {
							this.blit(guiGraphics, texture, 15, 6, 98, 0, guiWidth, guiHeight);
						}
					} else {
						this.blit(guiGraphics, texture, 15, 6, 0, 68, guiWidth, guiHeight);
					}
				}
				poseStack.popPose();

				poseStack.pushPose();
				{
					// Yellow meter
					poseStack.translate((screenWidth - guiWidth * scaleX) + (guiWidth - guiBarWidth) * scaleX + (24 * scaleX) - posX, (screenHeight - guiHeight * scaleY) - (2 * scaleY) - posY, 0);
					poseStack.scale(scaleX, scaleY, 1);
					if(playerData.getAlignment() == OrgMember.NONE) {
						if (playerData.getActiveDriveForm().equals(DriveForm.NONE.toString())) {
							this.blit(guiGraphics, texture, 14, 6, 0, 18, (int) currDrive, guiHeight);
						} else {
							this.blit(guiGraphics, texture, 14, 6, 98, 18, (int) currForm, guiHeight);
						}
					} else {
						this.blit(guiGraphics, texture, 14, 6, 0, 86, (int) currDrive, guiHeight);
					}

					poseStack.popPose();

					// Level Number
					poseStack.pushPose();
					{
						poseStack.translate((screenWidth - guiWidth * scaleX) + (85 * scaleX) - posX, (screenHeight - guiHeight * scaleY) - (2 * scaleY) - posY, 0);
						poseStack.scale(scaleX, scaleY, 1);

						int numPos = playerData.getActiveDriveForm().equals(DriveForm.NONE.toString()) ? getCurrBar(dp == 1000 ? 900 : dp, (int) playerData.getMaxDP() / 100) * 10 : 100 + getCurrBar(fp, Utils.getDriveFormLevel(playerData.getDriveFormMap(), playerData.getActiveDriveForm()) + 2) * 10;//(getCurrBar(fp, playerData.getFormGaugeLevel(playerData.getActiveDriveForm())) * 10);
						// int numPos = getCurrBar(dp, 9) * 10;
						if(playerData.getAlignment() == OrgMember.NONE) {
							blit(guiGraphics, texture, 14, 6, numPos, 38, 10, guiHeight);
						} else {
							blit(guiGraphics, texture, 14, 6, numPos, 106, 10, guiHeight);
						}

					}
					poseStack.popPose();

					// MAX Icon
					if (playerData.getDP() >= playerData.getMaxDP() && playerData.getActiveDriveForm().equals(DriveForm.NONE.toString())) {
						poseStack.pushPose();
						{
							
							decimalColor = prevMaxDriveTicks + (maxDriveTicks - prevMaxDriveTicks) * deltaTracker.getGameTimeDeltaPartialTick(true);
							
							Color c = Color.getHSBColor(decimalColor, 1F, 1F);
							RenderSystem.setShaderColor(c.getRed() / 255F, c.getGreen() / 255F, c.getBlue() / 255F, 1);

							poseStack.translate(((screenWidth - guiWidth * scaleX) + (10 * scaleX)), ((screenHeight - guiHeight * scaleY) - (10 * scaleY)), 0);
							poseStack.scale(scaleX, scaleY, 1);
							blit(guiGraphics, texture, 0, -3, 0, 57, 30, guiHeight);
							RenderSystem.setShaderColor(1, 1, 1, 1);
						}
						poseStack.popPose();
					}
				}
				poseStack.popPose();
				RenderSystem.disableBlend();
			}
		}
	}
	
	public static float maxDriveTicks = 0;
	public static float prevMaxDriveTicks = 0;
	
	@SubscribeEvent
	public void ClientTick(ClientTickEvent.Post event) {
		if (maxDriveTicks >= 1)
			maxDriveTicks = 0;

		prevMaxDriveTicks = maxDriveTicks;
		maxDriveTicks += 0.02;
	}
}
