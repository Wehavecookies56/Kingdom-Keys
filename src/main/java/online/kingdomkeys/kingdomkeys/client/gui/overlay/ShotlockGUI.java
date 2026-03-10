package online.kingdomkeys.kingdomkeys.client.gui.overlay;

import ca.weblite.objc.Client;
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
import online.kingdomkeys.kingdomkeys.client.gui.elements.HUDElement;
import online.kingdomkeys.kingdomkeys.config.ClientConfig;
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
    public static final HUDElement ELEMENT = new HUDElement("Focus");

	public static final ShotlockGUI INSTANCE = new ShotlockGUI();
	float focusBarWidth;
    int barWidth = 260;
    int barHeight = 172;
    int barX = 0;
    int barY = 0;
	PlayerData playerData;

	private ShotlockGUI() {
		super();
	}

	@Override
	public void render(GuiGraphics guiGraphics, DeltaTracker deltaTracker) {
		super.render(guiGraphics, deltaTracker);
		Player player = minecraft.player;
		int screenWidth = minecraft.getWindow().getGuiScaledWidth();
		int screenHeight = minecraft.getWindow().getGuiScaledHeight();

		float rawScale = 0.28f;

		float scaleX = rawScale * 0.9F;
		float scaleY = rawScale * 0.8F;

		playerData = PlayerData.get(player);
		if(playerData == null || playerData.getMaxFocus() <= 0)
			return;

		focusBarWidth = (int) (playerData.getFocus());

		PoseStack poseStack = guiGraphics.pose();

        poseStack.pushPose();
        {
            ELEMENT.applyTransform(guiGraphics,screenWidth,screenHeight,1);
            poseStack.scale(scaleX, scaleY, 1);

            drawBackground(poseStack);
            drawRedBar(poseStack);
            drawOrangeBar(poseStack);
            ELEMENT.endTransform(guiGraphics);
        }
        poseStack.popPose();

        poseStack.pushPose();
        {
            if (ClientEvents.focusing) {
                Shotlock shotlock = Utils.getPlayerShotlock(minecraft.player);
                playerData = PlayerData.get(minecraft.player);
                if(playerData == null || shotlock == null)
                    return;

                float scale = 0.6F;
                int guiWidth = 256;
                int guiHeight = 256;
                float x = (screenWidth - guiWidth * scale) / 2f -1;
                float y = (screenHeight - guiHeight * scale) / 2f;

                poseStack.translate(x, y, 0);
                poseStack.scale(scale, scale, scale);

                float shotlockPercentage = (float) (ClientEvents.focusGaugeTemp / playerData.getFocus());

                int barX = 0;
                int barY = 0;

                RenderSystem.enableBlend();
                RenderSystem.defaultBlendFunc();

                if (ClientEvents.focusGaugeTemp <= 0)
                    RenderSystem.setShaderColor(1, 0, 0, 1);
                else
                    RenderSystem.setShaderColor(1, 1, 1, 1);

                // Base
                this.blit(guiGraphics, ResourceLocation.fromNamespaceAndPath(KingdomKeys.MODID, "textures/gui/focus.png"), 0, 0,0, 0, guiWidth, guiHeight);
                RenderSystem.setShaderColor(1, 1, 1, 1);

                RenderSystem.setShaderTexture(0, ResourceLocation.fromNamespaceAndPath(KingdomKeys.MODID, "textures/gui/focus_fill.png"));
                RenderSystem.setShaderTexture(1, ResourceLocation.fromNamespaceAndPath(KingdomKeys.MODID, "textures/gui/focus_mask.png"));

                ClientSetup.shotlockShader.setSampler("Sampler0", 0);
                ClientSetup.shotlockShader.setSampler("Sampler1", 1);
                ClientSetup.shotlockShader.safeGetUniform("ShotlockPercentage").set(shotlockPercentage);
                ClientSetup.shotlockShader.apply();
                RenderSystem.setShader(() -> ClientSetup.shotlockShader);

                Matrix4f matrix = poseStack.last().pose();
                Tesselator tesselator = Tesselator.getInstance();
                BufferBuilder buffer = tesselator.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);

                buffer.addVertex(matrix, barX, barY + guiHeight, 0).setUv(0, 1);
                buffer.addVertex(matrix, barX + guiWidth, barY + guiHeight, 0).setUv(1, 1);
                buffer.addVertex(matrix, barX + guiWidth, barY, 0).setUv(1, 0);
                buffer.addVertex(matrix, barX, barY, 0).setUv(0, 0);

                BufferUploader.drawWithShader(buffer.buildOrThrow());

                RenderSystem.disableBlend();
                RenderSystem.setShader(GameRenderer::getPositionTexShader);

                poseStack.pushPose();
                {
                    poseStack.scale(2,2,2);
                    this.drawString(guiGraphics, minecraft.font, playerData.getShotlockEnemies().size() + "/" + shotlock.getMaxLocks(), guiWidth/2, guiHeight / 4 - minecraft.font.lineHeight / 2, 0x88CC33);
                }
                poseStack.popPose();
            }
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
