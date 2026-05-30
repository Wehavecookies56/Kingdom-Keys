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
import net.minecraft.util.Mth;
import net.minecraft.world.entity.projectile.ThrowableProjectile;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.entity.magic.*;

@OnlyIn(Dist.CLIENT)
public class FireEntityRenderer extends EntityRenderer<ThrowableProjectile> {

	private static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(KingdomKeys.MODID, "textures/entity/models/fire.png");

	private static final int FRAME_COUNT = 4;

	public FireEntityRenderer(EntityRendererProvider.Context context) {
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

			VertexConsumer consumer = buffer.getBuffer(RenderType.entityTranslucentEmissive(getTextureLocation(entity)));

			int[] frames = {0, 1, 2, 3, 2, 1};
			float speed = 0.005F;

			int index = (int) ((entity.tickCount + partialTicks) / speed) % frames.length;
			int frame = frames[index];

			float frameHeight = 1F / FRAME_COUNT;

			float v0 = frame * frameHeight;
			float v1 = v0 + frameHeight;

			float size = 0.5F;

			vertex(consumer, pose, -size, -size, 0F, v1);
			vertex(consumer, pose, size, -size, 1F, v1);
			vertex(consumer, pose, size, size, 1F, v0);
			vertex(consumer, pose, -size, size, 0F, v0);
		}
		poseStack.popPose();

		super.render(entity, entityYaw, partialTicks, poseStack, buffer, packedLight);
	}

	private void vertex(VertexConsumer consumer, PoseStack.Pose pose, float x, float y, float u, float v) {
		consumer.addVertex(pose, x, y, 0F).setColor(255, 255, 255, 255).setUv(u, v).setOverlay(OverlayTexture.NO_OVERLAY).setLight(0xF000F0).setNormal(pose, 0F, 1F, 0F);
	}

	@Override
	protected int getBlockLightLevel(ThrowableProjectile entity, BlockPos pos) {
		return 15;
	}

	@Override
	public ResourceLocation getTextureLocation(ThrowableProjectile entity) {
		if(entity instanceof DarkFiragaEntity) {
			return ResourceLocation.fromNamespaceAndPath(KingdomKeys.MODID, "textures/entity/models/darkfire.png");
		}
		return TEXTURE;
	}
}