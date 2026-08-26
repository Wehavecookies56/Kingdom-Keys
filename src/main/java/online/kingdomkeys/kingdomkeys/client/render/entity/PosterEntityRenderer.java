package online.kingdomkeys.kingdomkeys.client.render.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.entity.PosterEntity;
import org.joml.Matrix4f;

public class PosterEntityRenderer extends EntityRenderer<PosterEntity> {
	private static final ResourceLocation TEXTURE = KingdomKeys.rl("textures/posters/struggle.png");

	public PosterEntityRenderer(EntityRendererProvider.Context context) {
		super(context);
	}

	@Override
	public ResourceLocation getTextureLocation(PosterEntity entity) {
		return TEXTURE;
	}

	@Override
	public void render(PosterEntity entity, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
		poseStack.pushPose();
		{
			poseStack.mulPose(Axis.YP.rotationDegrees(entity.getDirection().toYRot()));

			float halfX = 0.4F;
			float halfY = 0.5F;
			float depth = -0.031F;

			boolean flip = entity.getDirection().getAxis() == Direction.Axis.X;
			float uLeft = flip ? 1F : 0F;
			float uRight = flip ? 0F : 1F;
			depth *= flip ? -1 : 1;

			VertexConsumer consumer = buffer.getBuffer(RenderType.entityCutoutNoCull(getTextureLocation(entity)));
			Matrix4f matrix = poseStack.last().pose();
			vertex(consumer, matrix, -halfX, -halfY, depth, uLeft, 1F, packedLight);
			vertex(consumer, matrix, halfX, -halfY, depth, uRight, 1F, packedLight);
			vertex(consumer, matrix, halfX, halfY, depth, uRight, 0F, packedLight);
			vertex(consumer, matrix, -halfX, halfY, depth, uLeft, 0F, packedLight);
		}
		poseStack.popPose();
		super.render(entity, entityYaw, partialTicks, poseStack, buffer, packedLight);
	}

	private static void vertex(VertexConsumer buffer, Matrix4f matrix, float x, float y, float z, float u, float v, int packedLight) {
		buffer.addVertex(matrix, x, y, z).setColor(255, 255, 255, 255).setUv(u, v).setOverlay(OverlayTexture.NO_OVERLAY).setLight(packedLight).setNormal(0.0F, 0.0F, 1.0F);
	}
}