package online.kingdomkeys.kingdomkeys.client.gui.overlay;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.client.ClientUtils;
import online.kingdomkeys.kingdomkeys.data.PlayerData;

public class MPGui extends OverlayBase {
	public static final MPGui INSTANCE = new MPGui();
	private static final ResourceLocation texture = ResourceLocation.fromNamespaceAndPath(KingdomKeys.MODID, "textures/gui/mpbar.png");
	private PlayerData playerData;

	private MPGui() {
		super();
	}

	@Override
	public void render(GuiGraphics guiGraphics, DeltaTracker deltaTracker) {
		super.render(guiGraphics, deltaTracker);

		Player player = minecraft.player;
		if(player == null)
			return;

		playerData = PlayerData.get(player);
		if(playerData == null || playerData.getMaxMP() <= 0)
			return;

		int screenWidth = minecraft.getWindow().getGuiScaledWidth();
		int screenHeight = minecraft.getWindow().getGuiScaledHeight();

		RenderSystem.setShaderColor(1,1,1,1);

		int mpBarWidth = (int)(playerData.getMP());
		int mpBarMaxWidth = (int)(playerData.getMaxMP());

		PoseStack poseStack = guiGraphics.pose();
		poseStack.pushPose();
		{
			ClientUtils.MP_ELEMENT.applyTransform(guiGraphics, screenWidth, screenHeight);
			RenderSystem.enableBlend();

			guiGraphics.pose().translate(ClientUtils.MP_ELEMENT.width - mpBarMaxWidth-23.4,0,0);
			drawMPBarBack(guiGraphics, mpBarMaxWidth);
			drawMPBarTop(guiGraphics, mpBarWidth);

			RenderSystem.disableBlend();
			ClientUtils.MP_ELEMENT.endTransform(guiGraphics);
		}
		poseStack.popPose();
	}

	public void drawMPBarBack(GuiGraphics gui, int width) {
		PoseStack matrixStack = gui.pose();
		matrixStack.pushPose();
		{
			// Left Margin
			matrixStack.pushPose();
			{
				blit(gui, texture, 0, 0, 0, 0, 2, 12);
			}
			matrixStack.popPose();

			// Background
			matrixStack.pushPose();
			{
				matrixStack.translate(2, 0, 0);
				matrixStack.scale(width, 1, 0);

				int v = playerData.getRecharge() ? 8 : 2;

				blit(gui, texture, 0, 0, v, 0, 1, 12);
			}
			matrixStack.popPose();

			// Right Margin
			matrixStack.pushPose();
			{
				matrixStack.translate(2 + width, 0, 0);
				blit(gui, texture, 0, 0, 3, 0, 2, 12);
			}
			matrixStack.popPose();

			// MP Icon
			matrixStack.pushPose();
			{
				int v = playerData.getRecharge() ? 45 : 32;

				matrixStack.translate( 2 + width + 1, 0, 0);
				matrixStack.scale(0.8F, 1, 1);

				blit(gui, texture, 0, 0, 0, v, 23, 12);
			}
			matrixStack.popPose();
		}
		matrixStack.popPose();
	}

	public void drawMPBarTop(GuiGraphics gui, int width) {
		PoseStack matrixStack = gui.pose();
		matrixStack.pushPose();
		{
			int maxWidth = (int)playerData.getMaxMP();
			int offset = maxWidth - width;

			matrixStack.translate(2 + offset, 2, 0);
			matrixStack.scale(width, 1, 0);

			int v = playerData.getRecharge() ? 22 : 12;

			blit(gui, texture, 0, 0, 2, v, 1, 8);
		}
		matrixStack.popPose();
	}
}