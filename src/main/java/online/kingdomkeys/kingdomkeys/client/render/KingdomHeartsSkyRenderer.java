package online.kingdomkeys.kingdomkeys.client.render;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import com.mojang.math.Axis;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.DimensionSpecialEffects;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.FogType;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.util.Utils;
import org.joml.Matrix4f;

import java.util.Calendar;

public class KingdomHeartsSkyRenderer {

    //called in LevelRendererMixin, using separate class for hotswap purposes
    public static void renderSky(ClientLevel level, PoseStack pPoseStack, Matrix4f pProjectionMatrix, float pPartialTick, Camera pCamera, Runnable pSkyFogSetup) {
        if (Utils.isAprilFools()) {
            pPoseStack.pushPose();
            RenderSystem.enableBlend();
            //RenderSystem.blendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
            RenderSystem.depthMask(false);
            BufferBuilder bufferbuilder = Tesselator.getInstance().getBuilder();
            float f11 = 1.0F - level.getRainLevel(pPartialTick);
            RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, f11);
            pPoseStack.mulPose(Axis.YP.rotationDegrees(0.0F));
            pPoseStack.mulPose(Axis.XP.rotationDegrees(150.0F));
            Matrix4f matrix4f1 = pPoseStack.last().pose();
            RenderSystem.setShader(GameRenderer::getPositionTexShader);
            float f12 = 50.0F;
            RenderSystem.setShaderTexture(0, new ResourceLocation(KingdomKeys.MODID, "textures/environment/kingdom_hearts.png"));
            int k = 0; //moon phase
            int l = k % 4;
            int i1 = k / 4 % 2;
            float f13 = (float) (l + 1);
            float f14 = (float) (i1 + 0);
            float f15 = (float) (l + 0);
            float f16 = (float) (i1 + 1);
            RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
            bufferbuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
            bufferbuilder.vertex(matrix4f1, -f12, -100.0F, f12).uv(f15, f16).endVertex();
            bufferbuilder.vertex(matrix4f1, f12, -100.0F, f12).uv(f13, f16).endVertex();
            bufferbuilder.vertex(matrix4f1, f12, -100.0F, -f12).uv(f13, f14).endVertex();
            bufferbuilder.vertex(matrix4f1, -f12, -100.0F, -f12).uv(f15, f14).endVertex();
            BufferUploader.drawWithShader(bufferbuilder.end());

            pPoseStack.popPose();

            RenderSystem.depthMask(true);
            RenderSystem.disableBlend();
            RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        }
    }

}
