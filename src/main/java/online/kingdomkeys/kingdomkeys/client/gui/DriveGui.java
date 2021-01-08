package online.kingdomkeys.kingdomkeys.client.gui;

import java.awt.Color;

import online.kingdomkeys.kingdomkeys.driveform.DriveForm;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.capability.IPlayerCapabilities;
import online.kingdomkeys.kingdomkeys.capability.ModCapabilities;
import online.kingdomkeys.kingdomkeys.lib.Constants;
import online.kingdomkeys.kingdomkeys.lib.Strings;
import online.kingdomkeys.kingdomkeys.util.Utils;

public class DriveGui extends Screen {
	
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

	public DriveGui() {
		super(new TranslationTextComponent(""));
		minecraft = Minecraft.getInstance();
	}

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

	@SubscribeEvent
	public void onRenderOverlayPost(RenderGameOverlayEvent event) {
		/*
		 * if (!MainConfig.displayGUI()) return; if
		 * (!mc.player.getCapability(ModCapabilities.PLAYER_STATS, null).getHudMode())
		 * return;
		 */
		IPlayerCapabilities playerData = ModCapabilities.getPlayer(minecraft.player);
		if (playerData != null) {
			MatrixStack matrixStack = event.getMatrixStack();
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

			/*
			 * if (event.getType() == RenderGameOverlayEvent.ElementType.HOTBAR) { if
			 * (!STATE.getActiveDriveName().equals("none") &&
			 * !STATE.getActiveDriveName().equals(Strings.Form_Anti)) {
			 * event.setCanceled(true); } }
			 */
			if (event.getType() == RenderGameOverlayEvent.ElementType.TEXT) {
				int guiWidth = 95;
				int guiBarWidth = 83;
				int guiHeight = 18;
				int screenWidth = minecraft.getMainWindow().getScaledWidth();
				int screenHeight = minecraft.getMainWindow().getScaledHeight();
				PlayerEntity player = minecraft.player;

				minecraft.textureManager.bindTexture(texture);

				float scale = 0.65f;
				switch (minecraft.gameSettings.guiScale) {
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

				matrixStack.push();
				{
					matrixStack.translate(-20.3F, -2, 1);

					// Background
					matrixStack.push();
					{
						matrixStack.translate((screenWidth - guiWidth * scale) - posX, (screenHeight - guiHeight * scale) - posY, 0);
						matrixStack.scale(scale, scale, scale);

						if (playerData.getActiveDriveForm().equals(DriveForm.NONE.toString())) {
							blit(matrixStack, 15, 6, 0, 0, guiWidth, guiHeight);
						} else {
							blit(matrixStack, 15, 6, 98, 0, guiWidth, guiHeight);
						}
					}
					matrixStack.pop();

					matrixStack.push();
					{
						// Yellow meter
						matrixStack.translate((screenWidth - guiWidth * scale) + (guiWidth - guiBarWidth) * scale + (24 * scale) - posX, (screenHeight - guiHeight * scale) - (2 * scale) - posY, 0);
						matrixStack.scale(scale, scale, scale);
						if (playerData.getActiveDriveForm().equals(DriveForm.NONE.toString())) {
							blit(matrixStack, 14, 6, 0, 18, (int) currDrive, guiHeight);
						} else {
							blit(matrixStack, 14, 6, 98, 18, (int) currForm, guiHeight);
						}
						
						matrixStack.pop();

						// Level Number
						matrixStack.push();
						{
							matrixStack.translate((screenWidth - guiWidth * scale) + (85 * scale) - posX, (screenHeight - guiHeight * scale) - (2 * scale) - posY, 0);
							matrixStack.scale(scale, scale, scale);

							int numPos = playerData.getActiveDriveForm().equals(DriveForm.NONE.toString()) ? getCurrBar(dp, (int) playerData.getMaxDP() / 100) * 10 : 100 + getCurrBar(fp, Utils.getDriveFormLevel(playerData.getDriveFormMap(), playerData.getActiveDriveForm()) + 2) * 10;//(getCurrBar(fp, playerData.getFormGaugeLevel(playerData.getActiveDriveForm())) * 10);
							// int numPos = getCurrBar(dp, 9) * 10;
							blit(matrixStack, 14, 6, numPos, 38, 10, guiHeight);
						}
						matrixStack.pop();

						// MAX Icon
						if (playerData.getDP() >= playerData.getMaxDP() && playerData.getActiveDriveForm().equals(DriveForm.NONE.toString())) {
							matrixStack.push();
							{
								if (doChange) {
									decimalColor += CONS;
									if (decimalColor >= 1)
										decimalColor = 0;
									Color c = Color.getHSBColor(decimalColor, 1F, 1F);
									RenderSystem.color3f(c.getRed() / 255F, c.getGreen() / 255F, c.getBlue() / 255F);

									timeLastChange = (int) System.currentTimeMillis();
									doChange = false;
								} else {
									if (timeLastChange + 1 < (int) System.currentTimeMillis()) {
										doChange = true;
									}
								}

								matrixStack.translate(((screenWidth - guiWidth * scale) + (10 * scale)), ((screenHeight - guiHeight * scale) - (10 * scale)), 0);
								matrixStack.scale(scale, scale, scale);
								blit(matrixStack, 0, -3, 0, 57, 30, guiHeight);
								RenderSystem.color3f(1, 1, 1);
							}
							matrixStack.pop();
						}
					}
					matrixStack.pop();
				}
			}
		}
	}
}
