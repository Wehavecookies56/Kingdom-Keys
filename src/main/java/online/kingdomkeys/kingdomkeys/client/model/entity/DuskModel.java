package online.kingdomkeys.kingdomkeys.client.model.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;

public class DuskModel<T extends Entity> extends EntityModel<T> {
    public ModelPart Body1;
    public ModelPart RightLegUpper;
    public ModelPart LeftLegUpper;
    public ModelPart RightArmShoulder;
    public ModelPart LeftArmShoulder;
    public ModelPart HeadUpper;
    public ModelPart LowerBody1;
    public ModelPart UpperBody1;
    public ModelPart RightLegMiddle;
    public ModelPart RightLegThigh;
    public ModelPart RightLegLower;
    public ModelPart RightLegFeet1;
    public ModelPart RightLegFeet2;
    public ModelPart LeftLegMiddle;
    public ModelPart LeftLegThigh;
    public ModelPart LeftLegLower;
    public ModelPart LeftLegFeet1;
    public ModelPart LeftLegFeet2;
    public ModelPart RightArm;
    public ModelPart RightHandUpper;
    public ModelPart RightHandLower;
    public ModelPart LeftArm;
    public ModelPart LeftHandUpper;
    public ModelPart LeftHandLower;
    public ModelPart HeadLower;
    public ModelPart HeadThroat;
    public ModelPart HeadPoint1;
    public ModelPart HeadThroatDetail1;
    public ModelPart HeadThroatDetail2;
    public ModelPart HeadThroatDetail3;
    public ModelPart HeadPoint2;

    private int cycleIndex;
    private double totalDistance;
    private boolean legRotation = false;

    public DuskModel() {
        this.texWidth = 64;
        this.texHeight = 64;
        this.HeadPoint1 = new ModelPart(this, 31, 15);
        this.HeadPoint1.setPos(0.0F, 0.0F, 0.0F);
        this.HeadPoint1.addBox(-1.5F, -5.5F, -3.0F, 3, 2, 1, 0.0F);
        this.LeftLegFeet2 = new ModelPart(this, 0, 34);
        this.LeftLegFeet2.setPos(0.0F, 0.0F, 0.0F);
        this.LeftLegFeet2.addBox(-0.5F, 0.5F, -3.5F, 1, 1, 2, 0.0F);
        this.setRotateAngle(LeftLegFeet2, -0.2617993877991494F, 0.0F, 0.0F);
        this.LowerBody1 = new ModelPart(this, 0, 6);
        this.LowerBody1.setPos(0.0F, 0.0F, 0.0F);
        this.LowerBody1.addBox(-1.5F, 3.0F, -1.5F, 3, 3, 2, 0.0F);
        this.RightLegUpper = new ModelPart(this, 0, 12);
        this.RightLegUpper.setPos(0.0F, 12.5F, 0.0F);
        this.RightLegUpper.addBox(-2.5F, 0.0F, -1.5F, 2, 5, 2, 0.0F);
        this.setRotateAngle(RightLegUpper, -0.12217304763960307F, 0.0F, 0.0F);
        this.RightArmShoulder = new ModelPart(this, 15, 13);
        this.RightArmShoulder.setPos(-2.0F, 6.0F, -0.5F);
        this.RightArmShoulder.addBox(-2.0F, -1.0F, -1.0F, 2, 2, 2, 0.0F);
        this.RightLegFeet2 = new ModelPart(this, 0, 34);
        this.RightLegFeet2.setPos(0.0F, 0.0F, 0.0F);
        this.RightLegFeet2.addBox(-0.5F, 0.5F, -3.5F, 1, 1, 2, 0.0F);
        this.setRotateAngle(RightLegFeet2, -0.2617993877991494F, 0.0F, 0.0F);
        this.HeadLower = new ModelPart(this, 23, 7);
        this.HeadLower.setPos(0.0F, 0.0F, 0.0F);
        this.HeadLower.addBox(-2.0F, -1.0F, -2.0F, 4, 1, 4, 0.0F);
        this.LeftHandUpper = new ModelPart(this, 15, 26);
        this.LeftHandUpper.setPos(0.5F, 6.0F, 0.0F);
        this.LeftHandUpper.addBox(-1.0F, 0.0F, -0.5F, 2, 4, 1, 0.0F);
        this.RightLegThigh = new ModelPart(this, 0, 38);
        this.RightLegThigh.setPos(0.0F, 0.0F, 0.0F);
        this.RightLegThigh.addBox(-2.2F, -0.9F, -1.5F, 2, 2, 2, 0.0F);
        this.setRotateAngle(RightLegThigh, 0.0F, 0.0F, 0.4553564018453205F);
        this.RightHandUpper = new ModelPart(this, 15, 26);
        this.RightHandUpper.setPos(-1.5F, 6.0F, 0.0F);
        this.RightHandUpper.addBox(-1.0F, 0.0F, -0.5F, 2, 4, 1, 0.0F);
        this.RightArm = new ModelPart(this, 15, 18);
        this.RightArm.setPos(0.0F, 0.0F, 0.0F);
        this.RightArm.addBox(-2.0F, 1.0F, -0.5F, 1, 5, 1, 0.0F);
        this.LeftHandLower = new ModelPart(this, 15, 32);
        this.LeftHandLower.setPos(0.0F, 0.0F, 0.0F);
        this.LeftHandLower.addBox(-0.5F, 4.0F, -0.5F, 1, 2, 1, 0.0F);
        this.LeftLegThigh = new ModelPart(this, 0, 38);
        this.LeftLegThigh.setPos(-1.8F, 0.6F, 0.0F);
        this.LeftLegThigh.addBox(-1.8F, -1.4F, -1.5F, 2, 2, 2, 0.0F);
        this.setRotateAngle(LeftLegThigh, 0.0F, 0.0F, 1.0471975511965976F);
        this.RightHandLower = new ModelPart(this, 15, 32);
        this.RightHandLower.setPos(0.0F, 0.0F, 0.0F);
        this.RightHandLower.addBox(-0.5F, 4.0F, -0.5F, 1, 2, 1, 0.0F);
        this.HeadThroatDetail1 = new ModelPart(this, 26, 23);
        this.HeadThroatDetail1.setPos(0.0F, 0.0F, 0.0F);
        this.HeadThroatDetail1.addBox(-2.0F, -2.8F, -2.0F, 4, 2, 0, 0.0F);
        this.LeftLegLower = new ModelPart(this, 0, 23);
        this.LeftLegLower.setPos(0.0F, 0.7F, 0.7F);
        this.LeftLegLower.addBox(-0.5F, -0.5F, -5.0F, 1, 1, 5, 0.0F);
        this.setRotateAngle(LeftLegLower, 1.730144887501979F, 0.0F, 0.0F);
        this.RightLegFeet1 = new ModelPart(this, 0, 29);
        this.RightLegFeet1.setPos(0.0F, 0.5F, -4.5F);
        this.RightLegFeet1.addBox(-0.5F, 0.0F, -2.0F, 1, 1, 3, 0.0F);
        this.setRotateAngle(RightLegFeet1, -1.2740903539558606F, 0.0F, 0.0F);
        this.LeftArmShoulder = new ModelPart(this, 15, 13);
        this.LeftArmShoulder.setPos(2.0F, 6.0F, -0.5F);
        this.LeftArmShoulder.addBox(0.0F, -1.0F, -1.0F, 2, 2, 2, 0.0F);
        this.HeadPoint2 = new ModelPart(this, 32, 20);
        this.HeadPoint2.setPos(0.0F, 0.0F, 0.0F);
        this.HeadPoint2.addBox(-1.0F, -5.5F, -3.9F, 2, 1, 1, 0.0F);
        this.HeadUpper = new ModelPart(this, 22, 0);
        this.HeadUpper.setPos(0.0F, 5.0F, -0.5F);
        this.HeadUpper.addBox(-2.0F, -5.5F, -2.0F, 4, 3, 4, 0.0F);
        this.LeftLegUpper = new ModelPart(this, 0, 12);
        this.LeftLegUpper.setPos(3.0F, 12.5F, 0.0F);
        this.LeftLegUpper.addBox(-2.5F, 0.0F, -1.5F, 2, 5, 2, 0.0F);
        this.setRotateAngle(LeftLegUpper, -0.12217304763960307F, 0.0F, 0.0F);
        this.RightLegMiddle = new ModelPart(this, 0, 19);
        this.RightLegMiddle.setPos(-1.6F, 4.6F, -0.3F);
        this.RightLegMiddle.addBox(-0.5F, 0.0F, -1.5F, 1, 1, 3, 0.0F);
        this.setRotateAngle(RightLegMiddle, -0.12217304763960307F, 0.0F, 0.0F);
        this.RightLegLower = new ModelPart(this, 0, 23);
        this.RightLegLower.setPos(0.0F, 0.7F, 0.7F);
        this.RightLegLower.addBox(-0.5F, -0.5F, -5.0F, 1, 1, 5, 0.0F);
        this.setRotateAngle(RightLegLower, 1.7278759594743864F, 0.0F, 0.0F);
        this.HeadThroatDetail3 = new ModelPart(this, 26, 27);
        this.HeadThroatDetail3.setPos(0.0F, 0.0F, 0.0F);
        this.HeadThroatDetail3.addBox(2.0F, -2.8F, -2.0F, 0, 2, 2, 0.0F);
        this.LeftLegFeet1 = new ModelPart(this, 0, 29);
        this.LeftLegFeet1.setPos(0.0F, 0.5F, -4.5F);
        this.LeftLegFeet1.addBox(-0.5F, 0.0F, -2.0F, 1, 1, 3, 0.0F);
        this.setRotateAngle(LeftLegFeet1, -1.2740903539558606F, 0.0F, 0.0F);
        this.LeftArm = new ModelPart(this, 15, 18);
        this.LeftArm.setPos(1.0F, 0.0F, 0.0F);
        this.LeftArm.addBox(0.0F, 1.0F, -0.5F, 1, 5, 1, 0.0F);
        this.UpperBody1 = new ModelPart(this, 9, 0);
        this.UpperBody1.setPos(0.0F, 0.0F, 0.0F);
        this.UpperBody1.addBox(-2.0F, -2.0F, -1.5F, 4, 3, 2, 0.0F);
        this.Body1 = new ModelPart(this, 0, 0);
        this.Body1.setPos(0.0F, 7.0F, 0.0F);
        this.Body1.addBox(-1.0F, 1.0F, -1.5F, 2, 2, 2, 0.0F);
        this.LeftLegMiddle = new ModelPart(this, 0, 19);
        this.LeftLegMiddle.setPos(-1.6F, 4.6F, -0.3F);
        this.LeftLegMiddle.addBox(-0.5F, 0.0F, -1.5F, 1, 1, 3, 0.0F);
        this.setRotateAngle(LeftLegMiddle, -0.12217304763960307F, 0.0F, 0.0F);
        this.HeadThroat = new ModelPart(this, 39, 1);
        this.HeadThroat.setPos(0.0F, 0.0F, 0.0F);
        this.HeadThroat.addBox(-2.0F, -2.5F, 0.0F, 4, 2, 2, 0.0F);
        this.HeadThroatDetail2 = new ModelPart(this, 26, 24);
        this.HeadThroatDetail2.setPos(0.0F, 0.0F, 0.0F);
        this.HeadThroatDetail2.addBox(-2.0F, -2.8F, -2.0F, 0, 2, 2, 0.0F);
        this.HeadUpper.addChild(this.HeadPoint1);
        this.LeftLegFeet1.addChild(this.LeftLegFeet2);
        this.Body1.addChild(this.LowerBody1);
        this.RightLegFeet1.addChild(this.RightLegFeet2);
        this.HeadUpper.addChild(this.HeadLower);
        this.LeftArm.addChild(this.LeftHandUpper);
        this.RightLegUpper.addChild(this.RightLegThigh);
        this.RightArm.addChild(this.RightHandUpper);
        this.RightArmShoulder.addChild(this.RightArm);
        this.LeftHandUpper.addChild(this.LeftHandLower);
        this.LeftLegUpper.addChild(this.LeftLegThigh);
        this.RightHandUpper.addChild(this.RightHandLower);
        this.HeadThroat.addChild(this.HeadThroatDetail1);
        this.LeftLegMiddle.addChild(this.LeftLegLower);
        this.RightLegLower.addChild(this.RightLegFeet1);
        this.HeadPoint1.addChild(this.HeadPoint2);
        this.RightLegUpper.addChild(this.RightLegMiddle);
        this.RightLegMiddle.addChild(this.RightLegLower);
        this.HeadThroat.addChild(this.HeadThroatDetail3);
        this.LeftLegLower.addChild(this.LeftLegFeet1);
        this.LeftArmShoulder.addChild(this.LeftArm);
        this.Body1.addChild(this.UpperBody1);
        this.LeftLegUpper.addChild(this.LeftLegMiddle);
        this.HeadUpper.addChild(this.HeadThroat);
        this.HeadThroat.addChild(this.HeadThroatDetail2);
    }

    @Override
    public void renderToBuffer(PoseStack matrixStackIn, VertexConsumer bufferIn, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha) {
        this.LeftLegUpper.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
        this.HeadUpper.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
        this.RightLegUpper.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
        this.Body1.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
        this.LeftArmShoulder.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
        this.RightArmShoulder.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
    }

    @Override
    public void setupAnim(T entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        if(!Minecraft.getInstance().isPaused()) {
            //if(EntityHelper.getState(ent) == 0)
            //{
            if(entityIn.distanceToSqr(entityIn.xo, entityIn.yo, entityIn.zo) > 0) {
                this.Body1.getXRot() = Mth.cos(limbSwing * 0.8662F) * 0.2F * limbSwingAmount;

                this.RightArmShoulder.getXRot() = Mth.cos(limbSwing * 0.8662F) * 0.5F * limbSwingAmount;
                this.RightHandUpper.getXRot() = Mth.cos(limbSwing * 0.8662F) * 0.8F * limbSwingAmount;

                this.RightLegUpper.getXRot() = Mth.cos(limbSwing * 0.5F) * 2.0F * limbSwingAmount;//degToRad(walkingUpperLegAnimation[cycleIndex]);

                this.LeftArmShoulder.getXRot() = Mth.cos(limbSwing * 0.8662F + (float) Math.PI) * 0.5F * limbSwingAmount;
                this.LeftHandUpper.getXRot() = Mth.cos(limbSwing * 0.8662F + (float) Math.PI) * 0.8F * limbSwingAmount;

                this.LeftLegUpper.getXRot() = Mth.cos(limbSwing * 0.5F + (float) Math.PI) * 2.0F * limbSwingAmount;
            }
            else {
                this.LeftLegUpper.getXRot() = degToRad(-7);
                this.RightLegMiddle.getXRot() = degToRad(-7);
                this.RightLegLower.getXRot() = degToRad(99);
                this.RightLegUpper.getXRot() = degToRad(-7);
            }
            //}
            //else
            //{

            //}
        }
    }

    /**
     * This is a helper function from Tabula to set the rotation of model parts
     */
    public void setRotateAngle(ModelPart modelRenderer, float x, float y, float z) {
        modelRenderer.getXRot() = x;
        modelRenderer.getYRot() = y;
        modelRenderer.zRot = z;
    }

    protected float degToRad(double degrees) {
        return (float) (degrees * (double)Math.PI / 180) ;
    }

    public void setRotation(ModelPart modelRenderer, float x, float y, float z) {
        modelRenderer.getXRot() = x;
        modelRenderer.getYRot() = y;
        modelRenderer.zRot = z;
    }
}