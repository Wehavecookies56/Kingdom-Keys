package online.kingdomkeys.kingdomkeys.client.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import online.kingdomkeys.kingdomkeys.entity.magic.BlizzardEntity;

public class BlizzardModel extends EntityModel<BlizzardEntity> {
	public ModelPart fist;

	public BlizzardModel() {
		this.texWidth = 32;
		this.texHeight = 32;
		this.fist = new ModelPart(this, 0, 0);
		this.fist.setPos(0.0F, 0.0F, 0.0F);
		this.fist.addBox(-2.0F, -2.0F, -2.0F, 4, 4, 4, 0.0F);
	}

	/**
	 * This is a helper function from Tabula to set the rotation of model parts
	 */
	public void setRotateAngle(ModelPart modelRenderer, float x, float y, float z) {
		modelRenderer.getXRot() = x;
		modelRenderer.getYRot() = y;
		modelRenderer.zRot = z;
	}
	
	@Override
	public void setupAnim(BlizzardEntity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void renderToBuffer(PoseStack matrixStackIn, VertexConsumer bufferIn, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha) {
		matrixStackIn.pushPose();
		
		matrixStackIn.translate(0, 0.1, 0);
		//matrixStackIn.rotate(Vector3f.YP.rotationDegrees());
		//rotate(new Vector3f(0,0.707,0.707).rotationDegrees(15)) 
		fist.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn,red,green,blue,alpha);
		matrixStackIn.popPose();
	}

}