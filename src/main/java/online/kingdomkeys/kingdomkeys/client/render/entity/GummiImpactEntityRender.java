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
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.client.TrailRenderer;
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

	private static final float WAVE_WIDTH = 0.45F;

	private static final float FADE_TICKS = 6F;

	private static final float[][] WAVE_COLORS = {{0.55F, 0.85F, 1F}, {0.25F, 0.6F, 0.95F}, {0.55F, 0.85F, 1F}, {0.25F, 0.6F, 0.95F}};

    @Override
    public void render(GummiImpactEntity entity, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource bufferIn, int packedLightIn) {
        if (entity.tickCount < 1)
            return;

        drawWave(entity, partialTicks, poseStack, bufferIn);

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

	private void drawWave(GummiImpactEntity entity, float partialTicks, PoseStack poseStack, MultiBufferSource bufferSource) {
		GummiImpactEntity partner = entity.otherPart();

		// Only the lower of the pair draws it, the same rule the damage goes by, or it would be drawn twice
		if (partner == null || partner.isRemoved() || entity.getId() > partner.getId()) {
			return;
		}

		float age = entity.tickCount + partialTicks;
		float remaining = entity.getMaxTicks() - age;

		if (remaining <= 0) {
			return;
		}

		float alpha = Math.min(1F, remaining / FADE_TICKS);

		Vec3 from = entity.getPosition(partialTicks);
		Vec3 to = partner.getPosition(partialTicks);

		Vec3[] arc = GummiImpactEntity.arc(from, to, entity.origin());

		Vec3 renderOrigin = entity.getPosition(partialTicks);

		TrailRenderer.render(arc, renderOrigin, poseStack.last().pose(), bufferSource.getBuffer(RenderType.debugQuads()), WAVE_COLORS, WAVE_WIDTH, alpha);
	}

    @Nullable
	@Override
	public ResourceLocation getTextureLocation(GummiImpactEntity entity) {
        return KingdomKeys.rl("textures/entity/gummi_water.png");
	}
}