package online.kingdomkeys.kingdomkeys.client.render.magic;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.projectile.ThrowableProjectile;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.client.model.SparkModel;
import online.kingdomkeys.kingdomkeys.entity.magic.SparkEntity;

import javax.annotation.Nullable;

@OnlyIn(Dist.CLIENT)
public class SparkEntityRenderer extends EntityRenderer<SparkEntity> {
	private static final ResourceLocation THUNDAGASHOT_TEXTURE = KingdomKeys.rl("textures/entity/models/thundagashot.png");
	private static final int FRAME_COUNT = 4;
	SparkModel<Entity> sparkModel;

	public SparkEntityRenderer(EntityRendererProvider.Context context) {
		super(context);
		this.shadowRadius = 0.0F;
		sparkModel = new SparkModel<>(context.bakeLayer(SparkModel.LAYER_LOCATION));
	}

	@Override
	public void render(SparkEntity entity, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
		float targetScale = 0.35F;

		float growTime = 5F;
		float growth = Math.min((entity.tickCount + partialTicks) / growTime, 1F);
		float scale = targetScale * growth;

		renderSparkTrail(entity, poseStack, buffer);

		poseStack.pushPose();
		{
			poseStack.translate(0, scale / 2.5F, 0);
			poseStack.mulPose(entityRenderDispatcher.cameraOrientation());

			poseStack.scale(scale, scale, scale);
			PoseStack.Pose pose = poseStack.last();

			VertexConsumer consumer = buffer.getBuffer(RenderType.entityTranslucent(getTextureLocation(entity)));

			int[] frames = {0, 1, 2, 3, 2, 1};
			int index = (entity.tickCount / 2) % frames.length;
			int frame = frames[index];

			float frameHeight = 1F / FRAME_COUNT;

			float v0 = frame * frameHeight;
			float v1 = v0 + frameHeight;

			float size = 0.5F;

			if (entity instanceof SparkEntity spark) {
				vertex(consumer, pose, -size, -size, 0F, v1, 155, 20, 255);
				vertex(consumer, pose, size, -size, 1F, v1, 155, 20, 255);
				vertex(consumer, pose, size, size, 1F, v0, 155, 20, 255);
				vertex(consumer, pose, -size, size, 0F, v0, 155, 20, 255);
			}
			if (entity instanceof SparkEntity && entity.tickCount > 1) {
				renderSparkElectricity(entity, poseStack, buffer);
			}
		}
		poseStack.popPose();

		super.render(entity, entityYaw, partialTicks, poseStack, buffer, packedLight);
	}

	private void vertex(VertexConsumer consumer, PoseStack.Pose pose, float x, float y, float u, float v, int r, int g, int b) {
		consumer.addVertex(pose, x, y, 0F).setColor(r, g, b, 255).setUv(u, v).setOverlay(OverlayTexture.NO_OVERLAY).setLight(0xF000F0).setNormal(pose, 0F, 1F, 0F);
	}

	private void lightningLine(VertexConsumer consumer, PoseStack.Pose pose, float x, float y, float z) {
		float px = 0;
		float py = 0;
		float pz = 0;

		int segments = 5;

		for (int i = 1; i <= segments; i++) {
			float t = i / (float) segments;

			float nx = x * t;
			float ny = y * t;
			float nz = z * t;

			if (i < segments) {
				nx += (float) ((Math.random() - 0.5) * 0.15);
				ny += (float) ((Math.random() - 0.5) * 0.15);
				nz += (float) ((Math.random() - 0.5) * 0.15);
			}

			consumer.addVertex(pose, px, py, pz).setColor(255, 255, 255, 255).setNormal(pose, 0, 1, 0);
			consumer.addVertex(pose, nx, ny, nz).setColor(255, 235, 0, 255).setNormal(pose, 0, 1, 0);

			px = nx;
			py = ny;
			pz = nz;
		}
	}

	private int[] getSparkColor(SparkEntity spark) {
		return switch (spark.getIndex() % 4) {
			case 0 -> new int[]{255, 225, 0};
			case 1 -> new int[]{255, 0, 0};
			case 2 -> new int[]{0, 225, 255};
			default -> new int[]{255, 0, 255};
		};
	}

	private void renderSparkElectricity(ThrowableProjectile entity, PoseStack poseStack, MultiBufferSource buffer) {
		VertexConsumer consumer = buffer.getBuffer(RenderType.lines());

		poseStack.pushPose();
		{
			PoseStack.Pose pose = poseStack.last();
			for (int i = 0; i < 4; i++) {
				float x = (entity.level().random.nextFloat() - 0.5F) * 0.4F;
				float y = (entity.level().random.nextFloat() - 0.5F) * 0.4F;
				float z = (entity.level().random.nextFloat() - 0.5F) * 0.4F;
				lightningLine(consumer, pose, x, y, z);
			}
		}
		poseStack.popPose();
	}

	private void renderSparkTrail(SparkEntity spark, PoseStack poseStack, MultiBufferSource buffer) {
		VertexConsumer consumer = buffer.getBuffer(RenderType.lines());
		poseStack.pushPose();
		{
			double rx = spark.getX();
			double ry = spark.getY();
			double rz = spark.getZ();
			poseStack.translate(0, -0.5F, 0);
			PoseStack.Pose pose = poseStack.last();

			for (int i = 0; i < spark.trailPositions.length - 1; i++) {
				Vec3 p1 = spark.trailPositions[i];
				Vec3 p2 = spark.trailPositions[i + 1];

				if (p1 == null || p2 == null) continue;

				float alpha = 1.0F - (i / (float) spark.trailPositions.length);

				int[] color = getSparkColor(spark);

				float r = color[0];
				float g = color[1];
				float b = color[2];

				consumer.addVertex(pose, (float) (p1.x - rx), (float) (p1.y - ry + 0.6F), (float) (p1.z - rz)).setColor((int) r, (int) g, (int) b, (int) (alpha * 255)).setNormal(pose, 0, 1, 0);
				consumer.addVertex(pose, (float) (p2.x - rx), (float) (p2.y - ry + 0.6F), (float) (p2.z - rz)).setColor((int) r, (int) g, (int) b, (int) (alpha * 255)).setNormal(pose, 0, 1, 0);
			}
		}
		poseStack.popPose();
	}

	@Override
	protected int getBlockLightLevel(SparkEntity entity, BlockPos pos) {
		return 15;
	}

	@Nullable
	@Override
	public ResourceLocation getTextureLocation(SparkEntity entity) {
		return THUNDAGASHOT_TEXTURE;
	}
}
