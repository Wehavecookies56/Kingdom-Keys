package online.kingdomkeys.kingdomkeys.world.dimension.realm_of_darkness;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import com.mojang.math.Axis;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.DimensionSpecialEffects;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterDimensionSpecialEffectsEvent;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix4f;

@EventBusSubscriber(value = Dist.CLIENT, bus = EventBusSubscriber.Bus.MOD)
public class RealmOfDarknessEffects extends DimensionSpecialEffects {

    static final int r = 100, g = 100, b = 160;

    public RealmOfDarknessEffects(float pCloudLevel, boolean pHasGround, SkyType pSkyType, boolean pForceBrightLightmap, boolean pConstantAmbientLight) {
        super(pCloudLevel, pHasGround, pSkyType, pForceBrightLightmap, pConstantAmbientLight);
    }

    @SubscribeEvent
    public static void specialEffects(RegisterDimensionSpecialEffectsEvent event) {
        event.register(ResourceLocation.fromNamespaceAndPath(KingdomKeys.MODID, "realm_of_darkness"), new RealmOfDarknessEffects(Float.NaN, true, SkyType.NONE, false, true));
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
    public boolean renderSky(ClientLevel level, int ticks, float partialTick, Matrix4f modelViewMatrix, Camera camera, Matrix4f projectionMatrix, boolean isFoggy, Runnable setupFog) {
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.depthMask(false);
        RenderSystem.setShader(GameRenderer::getPositionTexColorShader);
        //RenderSystem.setShaderTexture(0, new ResourceLocation(KingdomKeys.MODID, "textures/environment/sky.png"));
        Tesselator tesselator = Tesselator.getInstance();
        Matrix4f matrix4f = modelViewMatrix;

        for(int i = 0; i < 6; ++i) {
            RenderSystem.setShaderTexture(0, ResourceLocation.fromNamespaceAndPath(KingdomKeys.MODID, "textures/environment/skybox_" + i + ".png"));
            //0 = down

            //north
            if (i == 1) {
                matrix4f.rotate(Axis.XP.rotationDegrees(90.0F));
            }

            //south
            if (i == 2) {
                matrix4f.rotate(Axis.ZP.rotationDegrees(180.0F));
                matrix4f.rotate(Axis.XP.rotationDegrees(-90.0F));
            }

            //up
            if (i == 3) {
                matrix4f.rotate(Axis.XP.rotationDegrees(180.0F));
                matrix4f.rotate(Axis.YP.rotationDegrees(-90.0F));
            }

            //east
            if (i == 4) {
                matrix4f.rotate(Axis.XP.rotationDegrees(90.0F));
                matrix4f.rotate(Axis.ZP.rotationDegrees(90.0F));
            }

            //west
            if (i == 5) {
                matrix4f.rotate(Axis.XP.rotationDegrees(90.0F));
                matrix4f.rotate(Axis.ZP.rotationDegrees(-90.0F));
            }

            BufferBuilder bufferbuilder = tesselator.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX_COLOR);
            bufferbuilder.addVertex(matrix4f, -100.0F, -100.0F, -100.0F).setUv(0.0F, 0.0F).setColor(r, g, b, 255);
            bufferbuilder.addVertex(matrix4f, -100.0F, -100.0F, 100.0F).setUv(0.0F, 1.0F).setColor(r, g, b, 255);
            bufferbuilder.addVertex(matrix4f, 100.0F, -100.0F, 100.0F).setUv(1.0F, 1.0F).setColor(r, g, b, 255);
            bufferbuilder.addVertex(matrix4f, 100.0F, -100.0F, -100.0F).setUv(1.0F, 0.0F).setColor(r, g, b, 255);
            BufferUploader.drawWithShader(bufferbuilder.build());
        }
        RenderSystem.blendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        float f11 = 1.0F - level.getRainLevel(partialTick);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, f11);
        matrix4f.rotate(Axis.YP.rotationDegrees(-90.0F));
        matrix4f.rotate(Axis.XP.rotationDegrees(level.getTimeOfDay(partialTick) * 360.0F));
        Matrix4f matrix4f1 = modelViewMatrix;
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        float f12 = 20.0F;
        RenderSystem.setShaderTexture(0, ResourceLocation.withDefaultNamespace("textures/environment/moon_phases.png"));
        int k = 0; //moon phase
        int l = k % 4;
        int i1 = k / 4 % 2;
        float f13 = (float)(l + 0) / 4.0F;
        float f14 = (float)(i1 + 0) / 2.0F;
        float f15 = (float)(l + 1) / 4.0F;
        float f16 = (float)(i1 + 1) / 2.0F;
        BufferBuilder bufferbuilder = tesselator.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
        bufferbuilder.addVertex(matrix4f1, -f12, -100.0F, f12).setUv(f15, f16);
        bufferbuilder.addVertex(matrix4f1, f12, -100.0F, f12).setUv(f13, f16);
        bufferbuilder.addVertex(matrix4f1, f12, -100.0F, -f12).setUv(f13, f14);
        bufferbuilder.addVertex(matrix4f1, -f12, -100.0F, -f12).setUv(f15, f14);
        BufferUploader.drawWithShader(bufferbuilder.build());

        RenderSystem.depthMask(true);
        RenderSystem.disableBlend();
        return true;
    }

    @Nullable
    @Override
    public float[] getSunriseColor(float pTimeOfDay, float pPartialTicks) {
        return null;
    }
}
