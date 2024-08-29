package online.kingdomkeys.kingdomkeys.client.render.entity.drops;

import org.joml.Matrix3f;
import org.joml.Matrix4f;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;

import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import online.kingdomkeys.kingdomkeys.entity.ItemDropEntity;
import online.kingdomkeys.kingdomkeys.util.Utils;

public class EntityItemDropRenderer extends EntityRenderer<ItemDropEntity> {

	ResourceLocation texture;

	public EntityItemDropRenderer(EntityRendererProvider.Context context) {
		super(context);
		this.shadowRadius = 0.15F;
		this.shadowStrength = 0.75F;
	}

	@Override
	protected int getBlockLightLevel(ItemDropEntity entityIn, BlockPos blockPos) {
		return Mth.clamp(super.getBlockLightLevel(entityIn, blockPos) + 7, 0, 15);
	}

	public void render(ItemDropEntity entityIn, float entityYaw, float partialTicks, PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn) {
		matrixStackIn.pushPose();
		{
			float f = 0;
			float f1 = 1F;
			float f2 = 0;
			float f3 = 1F;
			float value = entityIn.getValue()/10F;
			value = Mth.clamp(Utils.map(value, 1, 35, 0.5F, 3),0.5F,3F);
			matrixStackIn.scale(value,value,value);
	
			matrixStackIn.translate(0.0D, (double) 0.1F, 0.0D);
			matrixStackIn.mulPose(this.entityRenderDispatcher.cameraOrientation());
			matrixStackIn.mulPose(Axis.YP.rotationDegrees(180.0F));
			matrixStackIn.scale(0.3F, 0.3F, 0.3F);
			VertexConsumer ivertexbuilder = bufferIn.getBuffer(RenderType.entityTranslucent(getTextureLocation(entityIn)));
			PoseStack.Pose matrixstack$entry = matrixStackIn.last();
			Matrix4f matrix4f = matrixstack$entry.pose();
			Matrix3f matrix3f = matrixstack$entry.normal();
			vertex(ivertexbuilder, matrix4f, matrix3f, -0.5F, -0.25F, 255, 255, 255, f, f3, packedLightIn);
			vertex(ivertexbuilder, matrix4f, matrix3f, 0.5F, -0.25F, 255, 255, 255, f1, f3, packedLightIn);
			vertex(ivertexbuilder, matrix4f, matrix3f, 0.5F, 0.75F, 255, 255, 255, f1, f2, packedLightIn);
			vertex(ivertexbuilder, matrix4f, matrix3f, -0.5F, 0.75F, 255, 255, 255, f, f2, packedLightIn);
		}
		matrixStackIn.popPose();
		super.render(entityIn, entityYaw, partialTicks, matrixStackIn, bufferIn, packedLightIn);
	}

	private static void vertex(VertexConsumer bufferIn, Matrix4f matrixIn, Matrix3f matrixNormalIn, float x, float y, int red, int green, int blue, float texU, float texV, int packedLight) {
		bufferIn.addVertex(matrixIn, x, y, 0.0F).setColor(red, green, blue, 255).setUv(texU, texV).setOverlay(OverlayTexture.NO_OVERLAY).setLight(packedLight).setNormal(0.0F, 1.0F, 0.0F);
	}

	/**
	 * Returns the location of an entity's texture.
	 */
	public ResourceLocation getTextureLocation(ItemDropEntity entity) {
		return texture;
	}
}
