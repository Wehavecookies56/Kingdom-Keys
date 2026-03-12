package online.kingdomkeys.kingdomkeys.client.gui.overlay;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.client.ClientUtils;
import online.kingdomkeys.kingdomkeys.data.PlayerData;
import online.kingdomkeys.kingdomkeys.driveform.DriveForm;
import online.kingdomkeys.kingdomkeys.lib.Strings;
import online.kingdomkeys.kingdomkeys.util.Utils;
import online.kingdomkeys.kingdomkeys.util.Utils.OrgMember;

import java.awt.*;

public class DriveGui extends OverlayBase {

	private static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(KingdomKeys.MODID, "textures/gui/drivebar.png");

	public static final DriveGui INSTANCE = new DriveGui();


	private static final double GUI_LENGTH = 47D;
	private static final double ONE_VALUE = GUI_LENGTH / 100D;

	private double currDrive;
	private double currForm;

	private float decimalColor;

	public static float maxDriveTicks = 0;
	public static float prevMaxDriveTicks = 0;

	private DriveGui() {}

	private int getCurrBar(double value, int level) {
		int bar = (int) value / 100;
		return Math.min(bar, level);
	}

	@Override
	public void render(GuiGraphics guiGraphics, DeltaTracker deltaTracker) {
		PlayerData playerData = PlayerData.get(minecraft.player);
		if (playerData == null)
			return;
		double dp = playerData.getDP();
		double fp = playerData.getFP();

		currDrive = (ONE_VALUE * dp) - getCurrBar(dp, (int) playerData.getMaxDP() / 100) * GUI_LENGTH;

		if (playerData.getDriveFormMap() != null &&
				playerData.getActiveDriveForm() != null &&
				!playerData.getActiveDriveForm().equals(DriveForm.NONE.toString())) {

			if (playerData.getActiveDriveForm().equals(Strings.Form_Anti)) {
				currForm = (ONE_VALUE * fp) - getCurrBar(fp, 1000) * GUI_LENGTH;
			} else {
				currForm = (ONE_VALUE * fp) - getCurrBar(fp, 300 + (playerData.getDriveFormMap().get(playerData.getActiveDriveForm())[0] * 100)) * GUI_LENGTH;
			}
		}

		if (dp == playerData.getMaxDP()) {
			currDrive = GUI_LENGTH;
		}

		PoseStack pose = guiGraphics.pose();

		pose.pushPose();

		int screenWidth = minecraft.getWindow().getGuiScaledWidth();
		int screenHeight = minecraft.getWindow().getGuiScaledHeight();

		ClientUtils.DRIVE_ELEMENT.applyTransform(guiGraphics,screenWidth,screenHeight);

		RenderSystem.enableBlend();
		RenderSystem.setShaderColor(1,1,1,1);

		renderDriveBar(guiGraphics, deltaTracker, playerData, dp, fp);

		RenderSystem.disableBlend();

		ClientUtils.DRIVE_ELEMENT.endTransform(guiGraphics);
	}

	private void renderDriveBar(GuiGraphics guiGraphics, DeltaTracker deltaTracker, PlayerData playerData, double dp, double fp) {
		int guiWidth = 95;
		int guiHeight = 18;
		int guiBarWidth = 83;

		// Background
		if (playerData.getAlignment() == OrgMember.NONE) {

			if (playerData.getActiveDriveForm().equals(DriveForm.NONE.toString())) {
				blit(guiGraphics, TEXTURE, 0, 0, 0, 0, guiWidth, guiHeight);
			} else {
				blit(guiGraphics, TEXTURE, 0, 0, 98, 0, guiWidth, guiHeight);
			}

		} else {
			blit(guiGraphics, TEXTURE, 0, 0, 0, 68, guiWidth, guiHeight);
		}

		// Yellow meter
		int meterWidth;

		if (playerData.getAlignment() == OrgMember.NONE) {
			if (playerData.getActiveDriveForm().equals(DriveForm.NONE.toString())) {
				meterWidth = (int) currDrive;
				blit(guiGraphics, TEXTURE, 35, -2, 0, 18, meterWidth, guiHeight);
			} else {
				meterWidth = (int) currForm;
				blit(guiGraphics, TEXTURE, 35, -2, 98, 18, meterWidth, guiHeight);
			}

		} else {
			meterWidth = (int) currDrive;
			blit(guiGraphics, TEXTURE, 14, 0, 0, 86, meterWidth, guiHeight);
		}

		// Level number
		int numPos;

		if (playerData.getActiveDriveForm().equals(DriveForm.NONE.toString())) {
			numPos = getCurrBar(dp == 1000 ? 900 : dp, (int) playerData.getMaxDP() / 100) * 10;
		} else {
			numPos = 100 + getCurrBar(fp, Utils.getDriveFormLevel(playerData.getDriveFormMap(), playerData.getActiveDriveForm()) + 2) * 10;
		}

		if (playerData.getAlignment() == OrgMember.NONE) {
			blit(guiGraphics, TEXTURE, 84, -2, numPos, 38, 10, guiHeight);
		} else {
			blit(guiGraphics, TEXTURE, 84, -2, numPos, 106, 10, guiHeight);
		}

		// MAX icon
		if (playerData.getDP() >= playerData.getMaxDP() && playerData.getActiveDriveForm().equals(DriveForm.NONE.toString())) {

			decimalColor = prevMaxDriveTicks + (maxDriveTicks - prevMaxDriveTicks) * deltaTracker.getGameTimeDeltaPartialTick(true);

			Color c = Color.getHSBColor(decimalColor, 1F, 1F);
			RenderSystem.setShaderColor(c.getRed() / 255F, c.getGreen() / 255F, c.getBlue() / 255F, 1);
			blit(guiGraphics, TEXTURE, 44, 3, 0, 57, 30, guiHeight);

			RenderSystem.setShaderColor(1,1,1,1);
		}
	}

	@SubscribeEvent
	public void clientTick(ClientTickEvent.Post event) {
		if (maxDriveTicks >= 1)
			maxDriveTicks = 0;

		prevMaxDriveTicks = maxDriveTicks;
		maxDriveTicks += 0.02;
	}
}