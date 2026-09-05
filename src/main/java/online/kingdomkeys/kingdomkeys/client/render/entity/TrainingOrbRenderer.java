package online.kingdomkeys.kingdomkeys.client.render.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import online.kingdomkeys.kingdomkeys.entity.mob.TrainingOrbEntity;
import org.joml.Matrix4f;

public class TrainingOrbRenderer<T extends TrainingOrbEntity> extends EntityRenderer<T> {
	private static final float SHROUD_SPIN = 34F;
	private static final float SHROUD_COUNTER_SPIN = -19F;
	private static final float SHROUD_SPREAD = 1.16F;
	private static final float SHROUD_DEPTH = -0.01F;

	private final ResourceLocation texture;
	private final boolean shrouded;

	public TrainingOrbRenderer(EntityRendererProvider.Context context, ResourceLocation texture, boolean shrouded) {
		super(context);
		this.texture = texture;
		this.shrouded = shrouded;
		this.shadowRadius = 0.3F;
		this.shadowStrength = 0.4F;
	}

	@Override
	public void render(T orb, float entityYaw, float partialTicks, PoseStack pose, MultiBufferSource buffers, int packedLight) {
		float age = orb.tickCount + partialTicks;
		float phase = orb.hoverPhase(partialTicks);

		pose.pushPose();
		{
			// Middle of the body
			pose.translate(0.0D, orb.getBbHeight() * 0.5F + Mth.sin(phase) * 0.06F, 0.0D);

			float pulse = orb.isCharging() ? 1.0F + Mth.sin(phase * 3F) * 0.12F : 1.0F;
			float size = orb.getBbWidth() * 1.25F * pulse;
			pose.scale(size, size, size);

			pose.mulPose(this.entityRenderDispatcher.cameraOrientation());
			pose.mulPose(Axis.YP.rotationDegrees(180.0F));
			int red = 255;
			int green = 255;
			int blue = 255 - orb.hurtTime * 8; // hurtTime = 10 on hit, goes yellow and fades back to white

			VertexConsumer builder = buffers.getBuffer(RenderType.entityTranslucentEmissive(texture));
			quad(builder, pose, cellStart(0), cellEnd(0), 1.0F, 0.0F, red, green, blue, 255);

			if (shrouded) {
				shroudPass(builder, pose, age * (SHROUD_SPIN / 20F), 1.0F, SHROUD_DEPTH * 2F, 255, green, blue, 255);
				shroudPass(builder, pose, age * (SHROUD_COUNTER_SPIN / 20F), SHROUD_SPREAD + Mth.sin(phase * 0.7F) * 0.05F, SHROUD_DEPTH, 255, green, blue, 145);
			}
		}
		pose.popPose();
		super.render(orb, entityYaw, partialTicks, pose, buffers, LightTexture.FULL_BRIGHT);
	}

	private void shroudPass(VertexConsumer builder, PoseStack pose, float degrees, float spread, float depth, int red, int green, int blue, int alpha) {
		pose.pushPose();
		{
			pose.mulPose(Axis.ZP.rotationDegrees(degrees));
			quad(builder, pose, cellStart(1), cellEnd(1), spread, depth, red, green, blue, alpha);
		}
		pose.popPose();
	}

	private void quad(VertexConsumer builder, PoseStack pose, float u0, float u1, float spread, float depth, int red, int green, int blue, int alpha) {
		Matrix4f matrix = pose.last().pose();
		float half = 0.5F * spread;
		vertex(builder, matrix, -half, -half, depth, red, green, blue, alpha, u0, 1F);
		vertex(builder, matrix, half, -half, depth, red, green, blue, alpha, u1, 1F);
		vertex(builder, matrix, half, half, depth, red, green, blue, alpha, u1, 0F);
		vertex(builder, matrix, -half, half, depth, red, green, blue, alpha, u0, 0F);
	}

	private float cellStart(int cell) {
		return shrouded ? cell * 0.5F : 0F;
	}

	private float cellEnd(int cell) {
		return shrouded ? (cell + 1) * 0.5F : 1F;
	}

	private static void vertex(VertexConsumer builder, Matrix4f matrix, float x, float y, float z, int red, int green, int blue, int alpha, float u, float v) {
		builder.addVertex(matrix, x, y, z).setColor(red, green, blue, alpha).setUv(u, v).setOverlay(OverlayTexture.NO_OVERLAY).setLight(LightTexture.FULL_BRIGHT).setNormal(0.0F, 1.0F, 0.0F);
	}

	@Override
	public ResourceLocation getTextureLocation(T orb) {
		return texture;
	}
}
