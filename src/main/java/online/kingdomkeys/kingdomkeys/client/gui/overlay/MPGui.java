package online.kingdomkeys.kingdomkeys.client.gui.overlay;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.data.ModData;
import online.kingdomkeys.kingdomkeys.config.ModConfigs;
import online.kingdomkeys.kingdomkeys.data.PlayerData;
import online.kingdomkeys.kingdomkeys.lib.Constants;

//TODO cleanup + comments
public class MPGui extends OverlayBase {

	public static final MPGui INSTANCE = new MPGui();
	int guiWidth = 173;
	int mpBarWidth;
	int guiHeight = 10;
	int noborderguiwidth = 171;
	PlayerData playerData;
	int counter = 0;

	private MPGui() {
		super();
	}

	@Override
	public void render(GuiGraphics guiGraphics, DeltaTracker deltaTracker) {
		super.render(guiGraphics, deltaTracker);
		// if (!MainConfig.displayGUI() || !player.getCapability(ModCapabilities.PLAYER_STATS, null).getHudMode())
		// return;
		Player player = minecraft.player;

		int screenWidth = minecraft.getWindow().getGuiScaledWidth();
		int screenHeight = minecraft.getWindow().getGuiScaledHeight();
		RenderSystem.setShaderColor(1, 1, 1, 1);

		float scale = 1f;
		switch (minecraft.options.guiScale().get()) {
			case Constants.SCALE_AUTO:
				scale = 0.85F;
				break;
		}
		float scaleactor = 1F * ModConfigs.mpXScale/100F;
		playerData = PlayerData.get(player);
		if(playerData == null || playerData.getMaxMP() <= 0)
			return;

		mpBarWidth = (int) (playerData.getMP() * scaleactor);
		int mpBarMaxWidth = (int) (playerData.getMaxMP() * scaleactor);

		PoseStack poseStack = guiGraphics.pose();

		poseStack.pushPose();// MP Background
		{
			RenderSystem.enableBlend();
			poseStack.translate(ModConfigs.mpXPos-7, ModConfigs.mpYPos, 0);

			poseStack.pushPose();// MP Background
			{
				poseStack.translate((screenWidth - mpBarMaxWidth * scale) - 80 * scale, (screenHeight - guiHeight * scale) - 9 * scale, 0);
				poseStack.scale(scale, scale / 1.3F, scale);
				drawMPBarBack(guiGraphics, 0, 0, mpBarMaxWidth, scale);
			}
			poseStack.popPose();

			poseStack.pushPose();// MP Bar
			{
				poseStack.translate((screenWidth - ((int) mpBarWidth) * scale) - 80 * scale, (screenHeight - (guiHeight) * scale) - 9 * scale, 0);
				poseStack.scale(scale, scale / 1.3F, scale);
				drawMPBarTop(guiGraphics, 0, 0, (int) Math.ceil(mpBarWidth), scale);
			}
			poseStack.popPose();
			RenderSystem.disableBlend();
		}
		poseStack.popPose();
	}

	public void drawMPBarBack(GuiGraphics gui, int posX, int posY, int width, float scale) {
		PoseStack matrixStack = gui.pose();
		matrixStack.pushPose();
		{
			// Left Margin
			matrixStack.pushPose();
			{
				matrixStack.translate(scale * posX, scale * posY, 0);
				matrixStack.scale(scale, scale, 0);
				blit(gui, ResourceLocation.fromNamespaceAndPath(KingdomKeys.MODID, "textures/gui/mpbar.png"), 0, 0, 0, 0, 2, 12);
			}
			matrixStack.popPose();

			// Background
			matrixStack.pushPose();
			{
				matrixStack.translate((posX + 2) * scale, posY * scale, 0);
				matrixStack.scale(width, scale, 0);
				int v = playerData.getRecharge() ? 8 : 2;
				blit(gui, ResourceLocation.fromNamespaceAndPath(KingdomKeys.MODID, "textures/gui/mpbar.png"), 0, 0, v, 0, 1, 12);

			}
			matrixStack.popPose();

			// Right Margin
			matrixStack.pushPose();
			{
				matrixStack.translate((posX + 2) * scale + width, scale * posY, 0);
				matrixStack.scale(scale, scale, 0);
				blit(gui, ResourceLocation.fromNamespaceAndPath(KingdomKeys.MODID, "textures/gui/mpbar.png"), 0, 0, 3, 0, 2, 12);
			}
			matrixStack.popPose();

			// MP Icon
			matrixStack.pushPose();
			{
				int v = playerData.getRecharge() ? 45 : 32;
				matrixStack.translate((posX + 2) * scale + width + 1, scale * posY, 0);
				matrixStack.scale(scale * 0.8F, scale, 1);
				blit(gui, ResourceLocation.fromNamespaceAndPath(KingdomKeys.MODID, "textures/gui/mpbar.png"), 0, 0, 0, v, 23, 12);
			}
			matrixStack.popPose();
		}
		matrixStack.popPose();

	}

	public void drawMPBarTop(GuiGraphics gui, int posX, int posY, int width, float scale) {
		PoseStack matrixStack = gui.pose();
		matrixStack.pushPose();
		{
			matrixStack.translate((posX + 2) * scale, (posY + 2) * scale, 0);
			matrixStack.scale(width, scale, 0);
			int v = playerData.getRecharge() ? 22 : 12;
			blit(gui, ResourceLocation.fromNamespaceAndPath(KingdomKeys.MODID, "textures/gui/mpbar.png"), 0, 0, 2, v, 1, 8);
		}
		matrixStack.popPose();

	}
}
