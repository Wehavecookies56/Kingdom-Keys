package online.kingdomkeys.kingdomkeys.client.render.entity.drops;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Matrix3f;
import net.minecraft.util.math.vector.Matrix4f;
import net.minecraft.util.math.vector.Vector3f;
import online.kingdomkeys.kingdomkeys.entity.ItemDropEntity;

public class EntityItemDropRenderer extends EntityRenderer<ItemDropEntity> {

	ResourceLocation texture;

	public EntityItemDropRenderer(EntityRendererManager renderManagerIn) {
		super(renderManagerIn);
		this.shadowSize = 0.15F;
		this.shadowOpaque = 0.75F;
	}

	@Override
	protected int getBlockLight(ItemDropEntity entityIn, BlockPos blockPos) {
		return MathHelper.clamp(super.getBlockLight(entityIn, blockPos) + 7, 0, 15);
	}

	public void render(ItemDropEntity entityIn, float entityYaw, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn) {
		matrixStackIn.push();
		float f = 0;
		float f1 = 1F;
		float f2 = 0;
		float f3 = 1F;

		matrixStackIn.translate(0.0D, (double) 0.1F, 0.0D);
		matrixStackIn.rotate(this.renderManager.getCameraOrientation());
		matrixStackIn.rotate(Vector3f.YP.rotationDegrees(180.0F));
		matrixStackIn.scale(0.3F, 0.3F, 0.3F);
		IVertexBuilder ivertexbuilder = bufferIn.getBuffer(RenderType.getEntityTranslucent(getEntityTexture(entityIn)));
		MatrixStack.Entry matrixstack$entry = matrixStackIn.getLast();
		Matrix4f matrix4f = matrixstack$entry.getMatrix();
		Matrix3f matrix3f = matrixstack$entry.getNormal();
		vertex(ivertexbuilder, matrix4f, matrix3f, -0.5F, -0.25F, 255, 255, 255, f, f3, packedLightIn);
		vertex(ivertexbuilder, matrix4f, matrix3f, 0.5F, -0.25F, 255, 255, 255, f1, f3, packedLightIn);
		vertex(ivertexbuilder, matrix4f, matrix3f, 0.5F, 0.75F, 255, 255, 255, f1, f2, packedLightIn);
		vertex(ivertexbuilder, matrix4f, matrix3f, -0.5F, 0.75F, 255, 255, 255, f, f2, packedLightIn);
		matrixStackIn.pop();
		super.render(entityIn, entityYaw, partialTicks, matrixStackIn, bufferIn, packedLightIn);
	}

	private static void vertex(IVertexBuilder bufferIn, Matrix4f matrixIn, Matrix3f matrixNormalIn, float x, float y, int red, int green, int blue, float texU, float texV, int packedLight) {
		bufferIn.pos(matrixIn, x, y, 0.0F).color(red, green, blue, 255).tex(texU, texV).overlay(OverlayTexture.NO_OVERLAY).lightmap(packedLight).normal(matrixNormalIn, 0.0F, 1.0F, 0.0F).endVertex();
	}

	/**
	 * Returns the location of an entity's texture.
	 */
	public ResourceLocation getEntityTexture(ItemDropEntity entity) {
		return texture;
	}
}
