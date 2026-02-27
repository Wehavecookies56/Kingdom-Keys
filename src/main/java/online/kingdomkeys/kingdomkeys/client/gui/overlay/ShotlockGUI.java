package online.kingdomkeys.kingdomkeys.client.gui.overlay;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import com.mojang.math.Axis;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.client.ClientSetup;
import online.kingdomkeys.kingdomkeys.config.ModConfigs;
import online.kingdomkeys.kingdomkeys.data.ModData;
import online.kingdomkeys.kingdomkeys.data.PlayerData;
import online.kingdomkeys.kingdomkeys.handler.ClientEvents;
import online.kingdomkeys.kingdomkeys.lib.Constants;
import online.kingdomkeys.kingdomkeys.shotlock.Shotlock;
import online.kingdomkeys.kingdomkeys.util.Utils;
import org.joml.Matrix4f;

//TODO cleanup + comments
public class ShotlockGUI extends OverlayBase {

	public static final ShotlockGUI INSTANCE = new ShotlockGUI();
	float focusBarWidth;
    int barWidth = 260;
    int barHeight = 172;
    int barX = 0;
    int barY = 0;
	PlayerData playerData;

	public ResourceLocation focusBar = ResourceLocation.fromNamespaceAndPath(KingdomKeys.MODID, "textures/gui/focusbar.png");
	private ShotlockGUI() {
		super();
	}

	@Override
	public void render(GuiGraphics guiGraphics, DeltaTracker deltaTracker) {
		super.render(guiGraphics, deltaTracker);

		Player player = minecraft.player;
		int screenWidth = minecraft.getWindow().getGuiScaledWidth();
		int screenHeight = minecraft.getWindow().getGuiScaledHeight();

		float rawScale = 1f;
		switch (minecraft.options.guiScale().get()) {
			case Constants.SCALE_AUTO:
				rawScale = 0.85F;
				break;
		}
		float scaleX = rawScale * ModConfigs.focusXScale/100F * 0.35F;
		float scaleY = rawScale * ModConfigs.focusYScale/100F * 0.35F;

		playerData = PlayerData.get(player);
		if(playerData == null || playerData.getMaxFocus() <= 0)
			return;

		focusBarWidth = (int) (playerData.getFocus());

		PoseStack poseStack = guiGraphics.pose();

        poseStack.pushPose();
        {
            poseStack.translate(screenWidth-0, screenHeight-40, 0);
            poseStack.translate(-barWidth * scaleX, -barHeight * scaleY, 0);
            poseStack.scale(scaleX, scaleY, 1);

            drawBackground(poseStack);
            drawRedBar(poseStack);
            drawOrangeBar(poseStack);
        }
        poseStack.popPose();

        poseStack.pushPose();
        {
			if(ClientEvents.focusing) { //GUI itself
				int guiWidth = 256;
				int guiHeight = 256;

				float focusScale = 400/100F;
				float size = 6;

				poseStack.pushPose();
				{
					poseStack.pushPose();
					{
						Shotlock shotlock = Utils.getPlayerShotlock(minecraft.player);
						playerData = PlayerData.get(minecraft.player);
						if(playerData == null)
							return;

						poseStack.translate((screenWidth / 2) - (guiWidth / 2) * focusScale / size - 0.5F, (screenHeight / 2) - (guiHeight / 2) * focusScale / size - 0.5F, 0);
						poseStack.scale(focusScale / size, focusScale / size, focusScale / size);
						if(ClientEvents.focusGaugeTemp<= 0)
							RenderSystem.setShaderColor(1, 0, 0, 1);
						this.blit(guiGraphics, ResourceLocation.fromNamespaceAndPath(KingdomKeys.MODID, "textures/gui/focus.png"), 0, 0, 0, 0, guiWidth, guiHeight);
						poseStack.pushPose();
						{
							poseStack.scale(2,2,2);
							this.drawString(guiGraphics, minecraft.font, playerData.getShotlockEnemies().size() + "/" + shotlock.getMaxLocks(), guiWidth/2, guiHeight / 4 - minecraft.font.lineHeight / 2, 0x88CC33);
						}
						poseStack.popPose();
						if(ClientEvents.focusGaugeTemp> 0) {
							double max = playerData.getFocus();
							double actual = ClientEvents.focusGaugeTemp;
							int topOffset = 25;
							int botOffset = 31;

							int realGuiHeight = (guiHeight-botOffset) - topOffset;
							int n = (int)(actual * realGuiHeight / max);
							blit(guiGraphics, ResourceLocation.fromNamespaceAndPath(KingdomKeys.MODID, "textures/gui/focus2.png"), 0, (guiHeight-botOffset)-n, 0, (guiHeight-botOffset ) - n, guiWidth, n);
						}
						RenderSystem.setShaderColor(1, 1, 1, 1);

					}
					poseStack.popPose();
				}
				poseStack.popPose();
			}
			RenderSystem.disableBlend();
		}
		poseStack.popPose();
	}

    private void drawOrangeBar(PoseStack poseStack) {
        float cost = (float) ClientEvents.focusGaugeTemp;
        float maxFocus = (float) playerData.getMaxFocus();
        float costPercentage = cost / maxFocus;

        poseStack.pushPose();
        {
            RenderSystem.enableBlend();
            RenderSystem.defaultBlendFunc();

            RenderSystem.setShaderTexture(0, ResourceLocation.fromNamespaceAndPath(KingdomKeys.MODID, "textures/gui/focus_bar_fill.png"));
            RenderSystem.setShaderTexture(1, ResourceLocation.fromNamespaceAndPath(KingdomKeys.MODID, "textures/gui/focus_bar_mask.png"));

            ClientSetup.focusShader.setSampler("Sampler0", 0);
            ClientSetup.focusShader.setSampler("Sampler1", 1);
            ClientSetup.focusShader.safeGetUniform("FocusPercentage").set(costPercentage);
            ClientSetup.focusShader.apply();
            RenderSystem.setShader(() -> ClientSetup.focusShader);

            Matrix4f matrix = poseStack.last().pose();
            Tesselator tesselator = Tesselator.getInstance();
            BufferBuilder buffer = tesselator.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);

            buffer.addVertex(matrix, barX, barY + barHeight, 0).setUv(0, 1);
            buffer.addVertex(matrix, barX + barWidth, barY + barHeight, 0).setUv(1, 1);
            buffer.addVertex(matrix, barX + barWidth, barY, 0).setUv(1, 0);
            buffer.addVertex(matrix, barX, barY, 0).setUv(0, 0);
            BufferUploader.drawWithShader(buffer.buildOrThrow());
        }
        poseStack.popPose();
    }

    private void drawRedBar(PoseStack poseStack) {
        float focus = (float) playerData.getFocus();
        float maxFocus = (float) playerData.getMaxFocus();
        float focusPercentage = focus / maxFocus;

        poseStack.pushPose();
        {
            RenderSystem.enableBlend();
            RenderSystem.defaultBlendFunc();
            RenderSystem.setShaderTexture(0, ResourceLocation.fromNamespaceAndPath(KingdomKeys.MODID, "textures/gui/focus_cost_fill.png"));
            RenderSystem.setShaderTexture(1, ResourceLocation.fromNamespaceAndPath(KingdomKeys.MODID, "textures/gui/focus_bar_mask.png"));
            ClientSetup.focusShader.setSampler("Sampler0", 0);
            ClientSetup.focusShader.setSampler("Sampler1", 1);
            ClientSetup.focusShader.safeGetUniform("FocusPercentage").set(focusPercentage);
            ClientSetup.focusShader.apply();
            RenderSystem.setShader(() -> ClientSetup.focusShader);

            Matrix4f matrix = poseStack.last().pose();
            Tesselator tesselator = Tesselator.getInstance();
            BufferBuilder buffer = tesselator.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);

            buffer.addVertex(matrix, barX, barY + barHeight, 0).setUv(0, 1);
            buffer.addVertex(matrix, barX + barWidth, barY + barHeight, 0).setUv(1, 1);
            buffer.addVertex(matrix, barX + barWidth, barY, 0).setUv(1, 0);
            buffer.addVertex(matrix, barX, barY, 0).setUv(0, 0);
            BufferUploader.drawWithShader(buffer.buildOrThrow());

            RenderSystem.disableBlend();
            RenderSystem.setShader(GameRenderer::getPositionTexShader);
        }
        poseStack.popPose();
    }

    private void drawBackground(PoseStack poseStack) {
        poseStack.pushPose();
        {
            RenderSystem.enableBlend();
            RenderSystem.setShader(GameRenderer::getPositionTexShader);
            RenderSystem.setShaderTexture(0, ResourceLocation.fromNamespaceAndPath(KingdomKeys.MODID, "textures/gui/focus_bar_outline.png"));

            Matrix4f matrix = poseStack.last().pose();
            Tesselator tesselator = Tesselator.getInstance();
            BufferBuilder buffer = tesselator.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);

            buffer.addVertex(matrix, barX, barY + barHeight, 0).setUv(0, 1);
            buffer.addVertex(matrix, barX + barWidth, barY + barHeight, 0).setUv(1, 1);
            buffer.addVertex(matrix, barX + barWidth, barY, 0).setUv(1, 0);
            buffer.addVertex(matrix, barX, barY, 0).setUv(0, 0);
            BufferUploader.drawWithShader(buffer.buildOrThrow());
        }
        poseStack.popPose();
    }


}
