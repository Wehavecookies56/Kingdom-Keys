package online.kingdomkeys.kingdomkeys.client.render;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import com.mojang.math.Axis;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.GameRenderer;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.util.Utils;
import org.joml.Matrix4f;

public class KingdomHeartsSkyRenderer {

    public static void renderSky(ClientLevel level, Matrix4f frustumMatrix, Matrix4f projectionMatrix, float partialTick, Camera camera, Runnable skyFogSetup) {
        if (Utils.isAprilFools()) {
            PoseStack poseStack = new PoseStack();
            poseStack.mulPose(frustumMatrix);
            poseStack.pushPose();
            RenderSystem.enableBlend();
            //RenderSystem.blendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
            RenderSystem.depthMask(false);
            Tesselator tesselator = Tesselator.getInstance();
            float f11 = 1.0F - level.getRainLevel(partialTick);
            RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, f11);
            poseStack.mulPose(Axis.YP.rotationDegrees(0.0F));
            poseStack.mulPose(Axis.XP.rotationDegrees(150.0F));
            Matrix4f matrix4f1 = poseStack.last().pose();
            RenderSystem.setShader(GameRenderer::getPositionTexShader);
            float f12 = 50.0F;
            RenderSystem.setShaderTexture(0, KingdomKeys.rl("textures/environment/kingdom_hearts.png"));
            int k = 0; //moon phase
            int l = k % 4;
            int i1 = k / 4 % 2;
            float f13 = (float) (l + 1);
            float f14 = (float) (i1 + 0);
            float f15 = (float) (l + 0);
            float f16 = (float) (i1 + 1);
            RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
            BufferBuilder bufferbuilder = tesselator.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
            bufferbuilder.addVertex(matrix4f1, -f12, -100.0F, f12).setUv(f15, f16);
            bufferbuilder.addVertex(matrix4f1, f12, -100.0F, f12).setUv(f13, f16);
            bufferbuilder.addVertex(matrix4f1, f12, -100.0F, -f12).setUv(f13, f14);
            bufferbuilder.addVertex(matrix4f1, -f12, -100.0F, -f12).setUv(f15, f14);
            BufferUploader.drawWithShader(bufferbuilder.buildOrThrow());

            poseStack.popPose();

            RenderSystem.depthMask(true);
            RenderSystem.disableBlend();
            RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        }
    }

}
