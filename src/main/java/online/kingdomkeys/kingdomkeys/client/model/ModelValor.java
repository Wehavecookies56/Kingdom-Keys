package online.kingdomkeys.kingdomkeys.client.model;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;

import net.minecraft.client.renderer.Vector3f;
import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.Pose;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;
import online.kingdomkeys.kingdomkeys.capability.ILevelCapabilities;
import online.kingdomkeys.kingdomkeys.capability.ModCapabilities;
import online.kingdomkeys.kingdomkeys.item.ModItems;

public class ModelValor extends BipedModel {



	public ModelValor(float size) {
		super(size);
		this.textureWidth = 64;
        this.textureHeight = 32;
       
	}

	boolean isSwimming = false;
	boolean isSleeping = false;
	boolean isGliding = false;

	float armRotation = 0;
	float hairRotation = 0;
	int punchLevel = 0;


	String driveForm = "";
	
	float yaw = 0;
	float pitch = 0;

	@Override
	public void setRotationAngles(LivingEntity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, 1, entityIn);
		ILevelCapabilities props = ModCapabilities.get((PlayerEntity) entityIn);

		driveForm = props.getDriveForm();

		yaw = entityIn.prevRenderYawOffset;
		pitch = headPitch;

		super.setRotationAngles(entityIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
	}

	@Override
	public void render(MatrixStack matrixStackIn, IVertexBuilder builderIn, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha) {
		matrixStackIn.push();
		{
			if (isSwimming) {
				matrixStackIn.translate(0, 1.3, 0);
				matrixStackIn.rotate(Vector3f.XP.rotationDegrees(90));
			} else if (isSleeping) {
				matrixStackIn.translate(0, 0, -1.5);
				matrixStackIn.rotate(Vector3f.XN.rotationDegrees(90));
				matrixStackIn.rotate(Vector3f.ZP.rotationDegrees(180));
			} else {
				matrixStackIn.rotate(Vector3f.YN.rotationDegrees(yaw));
			}
			
			//matrixStackIn.scale(1);
			matrixStackIn.translate(0, 1.5, 0);
			matrixStackIn.rotate(Vector3f.YP.rotationDegrees(180));
			matrixStackIn.rotate(Vector3f.ZP.rotationDegrees(180));


			this.bipedHead.render(matrixStackIn, builderIn, packedLightIn, OverlayTexture.NO_OVERLAY, 1F, 1F, 1F, 1F);
			this.bipedBody.render(matrixStackIn, builderIn, packedLightIn, OverlayTexture.NO_OVERLAY, 1F, 1F, 1F, 1F);
			this.bipedLeftArm.render(matrixStackIn, builderIn, packedLightIn, OverlayTexture.NO_OVERLAY, 1F, 1F, 1F, 1F);
			this.bipedRightArm.render(matrixStackIn, builderIn, packedLightIn, OverlayTexture.NO_OVERLAY, 1F, 1F, 1F, 1F);
			this.bipedLeftLeg.render(matrixStackIn, builderIn, packedLightIn, OverlayTexture.NO_OVERLAY, 1F, 1F, 1F, 1F);
			this.bipedRightLeg.render(matrixStackIn, builderIn, packedLightIn, OverlayTexture.NO_OVERLAY, 1F, 1F, 1F, 1F);

			
			/*this.rightArm.render(matrixStackIn, builderIn, packedLightIn, OverlayTexture.NO_OVERLAY, 1F,1F,1F, 1F);
			this.leftArm.render(matrixStackIn, builderIn, packedLightIn, OverlayTexture.NO_OVERLAY, 1F,1F,1F, 1F);
			this.body.render(matrixStackIn, builderIn, packedLightIn, OverlayTexture.NO_OVERLAY, 1F, 1F, 1F, 1F);
			this.leftLeg.render(matrixStackIn, builderIn, packedLightIn, OverlayTexture.NO_OVERLAY, 1F, 1F, 1F, 1F);
			this.rightLeg.render(matrixStackIn, builderIn, packedLightIn, OverlayTexture.NO_OVERLAY, 1F, 1F, 1F, 1F);*/
		}

		// super.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red,
		// green, blue, alpha);
		matrixStackIn.pop();

	}

	public void setRotateAngle(ModelRenderer ModelRenderer, float x, float y, float z) {
		ModelRenderer.rotateAngleX = x;
		ModelRenderer.rotateAngleY = y;
		ModelRenderer.rotateAngleZ = z;
	}

	public ModelRenderer getHandRenderer() {
		return this.bipedRightArm;
	}

	public void setLivingAnimations(LivingEntity entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTickTime) {
		this.swimAnimation = entitylivingbaseIn.getSwimAnimation(partialTickTime);
		super.setLivingAnimations(entitylivingbaseIn, limbSwing, limbSwingAmount, partialTickTime);
	}

	public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float headYaw, float headPitch, float scaleFactor, Entity entityIn) {

		LivingEntity entity = ((LivingEntity) entityIn);

		//this.head.rotateAngleY = headYaw / (180F / (float) Math.PI);
		//this.head.rotateAngleX = headPitch / (180F / (float) Math.PI);

		this.bipedLeftLeg.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F) * 0.8F * limbSwingAmount;
		this.bipedRightLeg.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F + (float) Math.PI) * 0.8F * limbSwingAmount;

		this.bipedRightArm.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F) * 0.4F * limbSwingAmount;
		this.bipedLeftArm.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F + (float) Math.PI) * 0.4F * limbSwingAmount;

		if (entity.isSwingInProgress) {
			this.bipedRightArm.rotateAngleX = MathHelper.sin(entity.swingProgress * 3.0F + (float) Math.PI) * 1.2F;
			this.bipedRightArm.rotateAngleY = MathHelper.sin(entity.swingProgress * 3.0F + (float) Math.PI) * -0.2F;
			this.bipedRightArm.rotateAngleZ = -MathHelper.cos(entity.swingProgress * 4.0F + (float) Math.PI) * 0.5F;
		}

		if (entityIn.getDistanceSq(entityIn.prevPosX, entityIn.prevPosY, entityIn.prevPosZ) <= 0.05F && !entity.isSwingInProgress) {
			// this.rightArm.rotateAngleX = 0;
			this.bipedRightArm.rotateAngleY = 0;
			this.bipedRightArm.rotateAngleZ = 0F;
		} else if (!entity.isSwingInProgress && entityIn.getDistanceSq(entityIn.prevPosX, entityIn.prevPosY, entityIn.prevPosZ) > 0) {
			this.bipedRightArm.rotateAngleY = 0;
			this.bipedRightArm.rotateAngleZ = 0F;
		}

		switch (leftArmPose) {
		case EMPTY:
			bipedLeftArm.rotateAngleY = 0.0F;
			break;
		case BLOCK:
			bipedLeftArm.rotateAngleX = bipedLeftArm.rotateAngleX * 0.5F - 0.9424779F;
			bipedLeftArm.rotateAngleY = ((float) Math.PI / 6F);
			break;
		case ITEM:
			bipedLeftArm.rotateAngleX = bipedLeftArm.rotateAngleX * 0.5F - ((float) Math.PI / 10F);
			bipedLeftArm.rotateAngleY = 0.0F;
		}

		switch (rightArmPose) {
		case EMPTY:
			bipedRightArm.rotateAngleY = 0.0F;
			break;
		case BLOCK:
			bipedRightArm.rotateAngleX = bipedRightArm.rotateAngleX * 0.5F - 0.9424779F;
			bipedRightArm.rotateAngleY = (-(float) Math.PI / 6F);
			break;
		case ITEM:
			bipedRightArm.rotateAngleX = bipedRightArm.rotateAngleX * 0.5F - ((float) Math.PI / 10F);
			bipedRightArm.rotateAngleY = 0.0F;
			break;
		case THROW_SPEAR:
			bipedRightArm.rotateAngleX = bipedRightArm.rotateAngleX * 0.5F - (float) Math.PI;
			bipedRightArm.rotateAngleY = 0.0F;
		}

		if (entityIn.isCrouching()) {
			bipedBody.rotateAngleX = 0.5F;
			bipedRightArm.rotateAngleX += 0.4F;
			bipedLeftArm.rotateAngleX += 0.4F;
			bipedRightLeg.rotationPointZ = 4.0F;
			bipedLeftLeg.rotationPointZ = 4.0F;
			bipedRightLeg.rotationPointY = 9.0F;
			bipedLeftLeg.rotationPointY = 9.0F;
			//head.rotationPointY = 1.0F;
		} else {
			bipedBody.rotateAngleX = 0.0F;
			bipedRightLeg.rotationPointZ = 0.1F;
			bipedLeftLeg.rotationPointZ = 0.1F;
			bipedRightLeg.rotationPointY = 12.0F;
			bipedLeftLeg.rotationPointY = 12.0F;
			//head.rotationPointY = 0.0F;
		}
	}
}