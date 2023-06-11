package online.kingdomkeys.kingdomkeys.world.dimension.realm_of_darkness;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import com.mojang.math.Matrix4f;
import com.mojang.math.Vector3f;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.DimensionSpecialEffects;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterDimensionSpecialEffectsEvent;
import net.minecraftforge.client.extensions.IForgeDimensionSpecialEffects;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import org.jetbrains.annotations.Nullable;

@Mod.EventBusSubscriber(value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class RealmOfDarknessEffects extends DimensionSpecialEffects {

    public RealmOfDarknessEffects(float pCloudLevel, boolean pHasGround, SkyType pSkyType, boolean pForceBrightLightmap, boolean pConstantAmbientLight) {
        super(pCloudLevel, pHasGround, pSkyType, pForceBrightLightmap, pConstantAmbientLight);
    }

    @SubscribeEvent
    public static void specialEffects(RegisterDimensionSpecialEffectsEvent event) {
        event.register(new ResourceLocation(KingdomKeys.MODID, "realm_of_darkness"), new RealmOfDarknessEffects(Float.NaN, true, SkyType.NONE, false, true));
    }

    @Override
    public Vec3 getBrightnessDependentFogColor(Vec3 pFogColor, float pBrightness) {
         return new Vec3(0, 0, 0);
    }

    @Override
    public boolean isFoggyAt(int pX, int pY) {
        return true;
    }

    @Override
    public boolean renderSky(ClientLevel level, int ticks, float partialTick, PoseStack poseStack, Camera camera, Matrix4f projectionMatrix, boolean isFoggy, Runnable setupFog) {
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.depthMask(false);
        RenderSystem.setShader(GameRenderer::getPositionTexColorShader);
        //RenderSystem.setShaderTexture(0, new ResourceLocation(KingdomKeys.MODID, "textures/environment/sky.png"));
        Tesselator tesselator = Tesselator.getInstance();
        BufferBuilder bufferbuilder = tesselator.getBuilder();

        for(int i = 0; i < 6; ++i) {
            poseStack.pushPose();
            RenderSystem.setShaderTexture(0, new ResourceLocation(KingdomKeys.MODID, "textures/environment/skybox_" + i + ".png"));
            //0 = down

            //north
            if (i == 1) {
                poseStack.mulPose(Vector3f.XP.rotationDegrees(90.0F));
            }

            //south
            if (i == 2) {
                poseStack.mulPose(Vector3f.ZP.rotationDegrees(180.0F));
                poseStack.mulPose(Vector3f.XP.rotationDegrees(-90.0F));
            }

            //up
            if (i == 3) {
                poseStack.mulPose(Vector3f.XP.rotationDegrees(180.0F));
                poseStack.mulPose(Vector3f.YP.rotationDegrees(-90.0F));
            }

            //east
            if (i == 4) {
                poseStack.mulPose(Vector3f.XP.rotationDegrees(90.0F));
                poseStack.mulPose(Vector3f.ZP.rotationDegrees(90.0F));
            }

            //west
            if (i == 5) {
                poseStack.mulPose(Vector3f.XP.rotationDegrees(90.0F));
                poseStack.mulPose(Vector3f.ZP.rotationDegrees(-90.0F));
            }

            Matrix4f matrix4f = poseStack.last().pose();
            int r = 100, g = 100, b = 160;
            bufferbuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX_COLOR);
            bufferbuilder.vertex(matrix4f, -100.0F, -100.0F, -100.0F).uv(0.0F, 0.0F).color(r, g, b, 255).endVertex();
            bufferbuilder.vertex(matrix4f, -100.0F, -100.0F, 100.0F).uv(0.0F, 1.0F).color(r, g, b, 255).endVertex();
            bufferbuilder.vertex(matrix4f, 100.0F, -100.0F, 100.0F).uv(1.0F, 1.0F).color(r, g, b, 255).endVertex();
            bufferbuilder.vertex(matrix4f, 100.0F, -100.0F, -100.0F).uv(1.0F, 0.0F).color(r, g, b, 255).endVertex();
            tesselator.end();
            poseStack.popPose();
        }
        RenderSystem.enableTexture();
        RenderSystem.blendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        poseStack.pushPose();
        float f11 = 1.0F - level.getRainLevel(partialTick);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, f11);
        poseStack.mulPose(Vector3f.YP.rotationDegrees(-90.0F));
        poseStack.mulPose(Vector3f.XP.rotationDegrees(level.getTimeOfDay(partialTick) * 360.0F));
        Matrix4f matrix4f1 = poseStack.last().pose();
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        float f12 = 20.0F;
        RenderSystem.setShaderTexture(0, new ResourceLocation("textures/environment/moon_phases.png"));
        int k = 0; //moon phase
        int l = k % 4;
        int i1 = k / 4 % 2;
        float f13 = (float)(l + 0) / 4.0F;
        float f14 = (float)(i1 + 0) / 2.0F;
        float f15 = (float)(l + 1) / 4.0F;
        float f16 = (float)(i1 + 1) / 2.0F;
        bufferbuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
        bufferbuilder.vertex(matrix4f1, -f12, -100.0F, f12).uv(f15, f16).endVertex();
        bufferbuilder.vertex(matrix4f1, f12, -100.0F, f12).uv(f13, f16).endVertex();
        bufferbuilder.vertex(matrix4f1, f12, -100.0F, -f12).uv(f13, f14).endVertex();
        bufferbuilder.vertex(matrix4f1, -f12, -100.0F, -f12).uv(f15, f14).endVertex();
        BufferUploader.drawWithShader(bufferbuilder.end());
        RenderSystem.disableTexture();

        poseStack.popPose();

        RenderSystem.depthMask(true);
        RenderSystem.enableTexture();
        RenderSystem.disableBlend();
        return true;
    }

    @Nullable
    @Override
    public float[] getSunriseColor(float pTimeOfDay, float pPartialTicks) {
        return null;
    }
}
