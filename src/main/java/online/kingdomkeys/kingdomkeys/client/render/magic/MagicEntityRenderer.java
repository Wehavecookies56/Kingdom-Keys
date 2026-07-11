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
import net.minecraft.world.entity.projectile.ThrowableProjectile;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.entity.magic.*;

@OnlyIn(Dist.CLIENT)
public class MagicEntityRenderer extends EntityRenderer<ThrowableProjectile> {
	private static final ResourceLocation FIRE_TEXTURE = ResourceLocation.fromNamespaceAndPath(KingdomKeys.MODID, "textures/entity/models/fire.png");
	private static final ResourceLocation DARKFIRE_TEXTURE = ResourceLocation.fromNamespaceAndPath(KingdomKeys.MODID, "textures/entity/models/darkfire.png");
	private static final ResourceLocation THUNDAGASHOT_TEXTURE = ResourceLocation.fromNamespaceAndPath(KingdomKeys.MODID, "textures/entity/models/thundagashot.png");

	private static final int FRAME_COUNT = 4;

	public MagicEntityRenderer(EntityRendererProvider.Context context) {
		super(context);
		this.shadowRadius = 0F;
	}

	@Override
	public void render(ThrowableProjectile entity, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
		float targetScale = switch (entity) {
			case FireEntity fire -> 0.5F;
			case FiraEntity fira -> 0.8F;
			case FiragaEntity firaga -> 1.2F;
			case FirazaEntity firaza -> 2.0F;
			case FiragaBurstControllerEntity firagaBurstController -> 4.0F;
			case ThundagaShotEntity thundagaShotEntity -> 0.8F;
			default -> 1.0F;
		};

		float growTime = 5F;
		float growth = Math.min((entity.tickCount + partialTicks) / growTime, 1F);
		float scale = targetScale * growth;

		poseStack.pushPose();
		{
			poseStack.translate(0, scale / 2.5F, 0);
			poseStack.mulPose(entityRenderDispatcher.cameraOrientation());

			//float spin = (entity.tickCount + partialTicks) * 0.6F;
			//poseStack.mulPose(com.mojang.math.Axis.ZP.rotationDegrees(spin));

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
			vertex(consumer, pose, -size, -size, 0F, v1);
			vertex(consumer, pose, size, -size, 1F, v1);
			vertex(consumer, pose, size, size, 1F, v0);
			vertex(consumer, pose, -size, size, 0F, v0);

			if (entity instanceof ThundagaShotEntity && entity.tickCount > 1) {
				renderElectricArcs(entity, poseStack, buffer);
			}
		}
		poseStack.popPose();


		super.render(entity, entityYaw, partialTicks, poseStack, buffer, packedLight);
	}

	private void vertex(VertexConsumer consumer, PoseStack.Pose pose, float x, float y, float u, float v) {
		vertex(consumer, pose, x, y, u, v, 255, 255, 255);
	}

	private void vertex(VertexConsumer consumer, PoseStack.Pose pose, float x, float y, float u, float v, int r, int g, int b) {
		consumer.addVertex(pose, x, y, 0F).setColor(r, g, b, 255).setUv(u, v).setOverlay(OverlayTexture.NO_OVERLAY).setLight(0xF000F0).setNormal(pose, 0F, 1F, 0F);
	}

	private void renderElectricArcs(ThrowableProjectile entity, PoseStack poseStack, MultiBufferSource buffer) {
		VertexConsumer consumer = buffer.getBuffer(RenderType.lines());
		poseStack.pushPose();
		{
			PoseStack.Pose pose = poseStack.last();

			float size = 1.1F;
			for (int i = 0; i < 12; i++) {
				float x = (entity.level().random.nextFloat() - 0.5F) * size;
				float y = (entity.level().random.nextFloat() - 0.5F) * size;
				float z = (entity.level().random.nextFloat() - 0.5F) * size;

				lightningLine(consumer, pose, x, y, z);
			}
		}
		poseStack.popPose();
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

	@Override
	protected int getBlockLightLevel(ThrowableProjectile entity, BlockPos pos) {
		return 15;
	}

	@Override
	public ResourceLocation getTextureLocation(ThrowableProjectile entity) {
		if (entity instanceof DarkFiragaEntity) {
			return DARKFIRE_TEXTURE;
		} else if (entity instanceof ThundagaShotEntity) {
			return THUNDAGASHOT_TEXTURE;
		}
		return FIRE_TEXTURE;
	}
}