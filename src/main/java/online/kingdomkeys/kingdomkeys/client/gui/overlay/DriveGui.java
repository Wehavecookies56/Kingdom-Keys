package online.kingdomkeys.kingdomkeys.client.gui.overlay;

import java.awt.Color;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.gui.ForgeIngameGui;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.capability.IPlayerCapabilities;
import online.kingdomkeys.kingdomkeys.capability.ModCapabilities;
import online.kingdomkeys.kingdomkeys.config.ModConfigs;
import online.kingdomkeys.kingdomkeys.driveform.DriveForm;
import online.kingdomkeys.kingdomkeys.lib.Constants;
import online.kingdomkeys.kingdomkeys.lib.Strings;
import online.kingdomkeys.kingdomkeys.util.Utils;
import online.kingdomkeys.kingdomkeys.util.Utils.OrgMember;

public class DriveGui extends OverlayBase {
	
	ResourceLocation texture = new ResourceLocation(KingdomKeys.MODID, "textures/gui/drivebar.png");

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

	private boolean doChange = false;

	private int timeLastChange = (int) System.currentTimeMillis();

	@Override
	public void render(ForgeIngameGui gui, PoseStack poseStack, float partialTick, int width, int height) {
		super.render(gui, poseStack, partialTick, width, height);
		/*
		 * if (!MainConfig.displayGUI()) return; if
		 * (!mc.player.getCapability(ModCapabilities.PLAYER_STATS, null).getHudMode())
		 * return;
		 */
		IPlayerCapabilities playerData = ModCapabilities.getPlayer(minecraft.player);
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

			RenderSystem.setShaderTexture(0, texture);

			float scale = 0.65f;
			switch (minecraft.options.guiScale) {
				case Constants.SCALE_AUTO:
					scale = 0.85f;
					break;
				case Constants.SCALE_NORMAL:
					scale = 0.85f;
					break;
				default:
					scale = 0.65f;
					break;
			}
			float posX = 52 * scale;
			float posY = 20 * scale + 2;

			poseStack.pushPose();
			{
				RenderSystem.setShaderColor(1, 1, 1, 1);
				RenderSystem.enableBlend();
				poseStack.translate(-20.3F + ModConfigs.dpXPos, -2 + ModConfigs.dpYPos, 1);

				// Background
				poseStack.pushPose();
				{
					poseStack.translate((screenWidth - guiWidth * scale) - posX, (screenHeight - guiHeight * scale) - posY, 0);
					poseStack.scale(scale, scale, scale);

					if(playerData.getAlignment() == OrgMember.NONE) {
						if (playerData.getActiveDriveForm().equals(DriveForm.NONE.toString())) {
							this.blit(poseStack, 15, 6, 0, 0, guiWidth, guiHeight);
						} else {
							this.blit(poseStack, 15, 6, 98, 0, guiWidth, guiHeight);
						}
					} else {
						this.blit(poseStack, 15, 6, 0, 68, guiWidth, guiHeight);
					}
				}
				poseStack.popPose();

				poseStack.pushPose();
				{
					// Yellow meter
					poseStack.translate((screenWidth - guiWidth * scale) + (guiWidth - guiBarWidth) * scale + (24 * scale) - posX, (screenHeight - guiHeight * scale) - (2 * scale) - posY, 0);
					poseStack.scale(scale, scale, scale);
					if(playerData.getAlignment() == OrgMember.NONE) {
						if (playerData.getActiveDriveForm().equals(DriveForm.NONE.toString())) {
							this.blit(poseStack, 14, 6, 0, 18, (int) currDrive, guiHeight);
						} else {
							this.blit(poseStack, 14, 6, 98, 18, (int) currForm, guiHeight);
						}
					} else {
						this.blit(poseStack, 14, 6, 0, 86, (int) currDrive, guiHeight);
					}

					poseStack.popPose();

					// Level Number
					poseStack.pushPose();
					{
						poseStack.translate((screenWidth - guiWidth * scale) + (85 * scale) - posX, (screenHeight - guiHeight * scale) - (2 * scale) - posY, 0);
						poseStack.scale(scale, scale, scale);

						int numPos = playerData.getActiveDriveForm().equals(DriveForm.NONE.toString()) ? getCurrBar(dp == 1000 ? 900 : dp, (int) playerData.getMaxDP() / 100) * 10 : 100 + getCurrBar(fp, Utils.getDriveFormLevel(playerData.getDriveFormMap(), playerData.getActiveDriveForm()) + 2) * 10;//(getCurrBar(fp, playerData.getFormGaugeLevel(playerData.getActiveDriveForm())) * 10);
						// int numPos = getCurrBar(dp, 9) * 10;
						if(playerData.getAlignment() == OrgMember.NONE) {
							blit(poseStack, 14, 6, numPos, 38, 10, guiHeight);
						} else {
							blit(poseStack, 14, 6, numPos, 106, 10, guiHeight);
						}

						/*
						matrixStack.translate(20, 16, 0);
						matrixStack.rotate(Vector3f.ZN.rotationDegrees(minecraft.player.ticksExisted*6 % 360));

						//matrixStack.translate(-10, 0, 0);
						blit(matrixStack, 10, 0, 0, 29, 3, 3);
*/
					}
					poseStack.popPose();

					// MAX Icon
					if (playerData.getDP() >= playerData.getMaxDP() && playerData.getActiveDriveForm().equals(DriveForm.NONE.toString())) {
						poseStack.pushPose();
						{
							if (doChange) {
								decimalColor += CONS;
								if (decimalColor >= 1)
									decimalColor = 0;
								Color c = Color.getHSBColor(decimalColor, 1F, 1F);
								RenderSystem.setShaderColor(c.getRed() / 255F, c.getGreen() / 255F, c.getBlue() / 255F, 1);

								timeLastChange = (int) System.currentTimeMillis();
								doChange = false;
							} else {
								if (timeLastChange + 1 < (int) System.currentTimeMillis()) {
									doChange = true;
								}
							}

							poseStack.translate(((screenWidth - guiWidth * scale) + (10 * scale)), ((screenHeight - guiHeight * scale) - (10 * scale)), 0);
							poseStack.scale(scale, scale, scale);
							blit(poseStack, 0, -3, 0, 57, 30, guiHeight);
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
}
