package online.kingdomkeys.kingdomkeys.client.render.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.entity.GummiImpactEntity;
import org.joml.Matrix4f;
import org.joml.Quaternionf;

import javax.annotation.Nullable;

@OnlyIn(Dist.CLIENT)
public class GummiImpactEntityRender extends EntityRenderer<GummiImpactEntity> {

	public GummiImpactEntityRender(EntityRendererProvider.Context context) {
		super(context);
		this.shadowRadius = 0F;
	}

    @Override
    public void render(GummiImpactEntity entity, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource bufferIn, int packedLightIn) {
        if (entity.tickCount < 1)
            return;

        poseStack.pushPose();
        {
            poseStack.translate(0, 0.05, 0);
            Camera camera = Minecraft.getInstance().gameRenderer.getMainCamera();
            Quaternionf q = new Quaternionf(camera.rotation());
            poseStack.mulPose(q);
            float scale = 0.3F;

            VertexConsumer vertex = bufferIn.getBuffer(RenderType.entityCutout(getTextureLocation(entity)));

            Matrix4f matrix = poseStack.last().pose();
            int overlay = OverlayTexture.NO_OVERLAY;

            matrix.scale(scale,scale,scale);

            vertex.addVertex(matrix, -0.5f, -0.5f, 0.0f)
                    .setColor(1f, 1f, 1f, 1f)
                    .setUv(0.0f, 1.0f)
                    .setUv1(overlay & 0xFFFF, overlay >> 16)
                    .setLight(packedLightIn)
                    .setNormal(0, 0, 1);

            vertex.addVertex(matrix, 0.5f, -0.5f, 0.0f)
                    .setColor(1f, 1f, 1f, 1f)
                    .setUv(1.0f, 1.0f)
                    .setUv1(overlay & 0xFFFF, overlay >> 16)
                    .setLight(packedLightIn)
                    .setNormal(0, 0, 1);

            vertex.addVertex(matrix, 0.5f, 0.5f, 0.0f)
                    .setColor(1f, 1f, 1f, 1f)
                    .setUv(1.0f, 0.0f)
                    .setUv1(overlay & 0xFFFF, overlay >> 16)
                    .setLight(packedLightIn)
                    .setNormal(0, 0, 1);

            vertex.addVertex(matrix, -0.5f, 0.5f, 0.0f)
                    .setColor(1f, 1f, 1f, 1f)
                    .setUv(0.0f, 0.0f)
                    .setUv1(overlay & 0xFFFF, overlay >> 16)
                    .setLight(packedLightIn)
                    .setNormal(0, 0, 1);
        }
        poseStack.popPose();
        super.render(entity, entityYaw, partialTicks, poseStack, bufferIn, packedLightIn);
    }

    @Nullable
	@Override
	public ResourceLocation getTextureLocation(GummiImpactEntity entity) {
        return KingdomKeys.rl("textures/entity/gummi_water.png");
	}
}