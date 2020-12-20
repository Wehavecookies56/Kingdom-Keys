package online.kingdomkeys.kingdomkeys.client.model.entity;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;

public class DuskModel<T extends Entity> extends EntityModel<T> {
    public ModelRenderer Body1;
    public ModelRenderer RightLegUpper;
    public ModelRenderer LeftLegUpper;
    public ModelRenderer RightArmShoulder;
    public ModelRenderer LeftArmShoulder;
    public ModelRenderer HeadUpper;
    public ModelRenderer LowerBody1;
    public ModelRenderer UpperBody1;
    public ModelRenderer RightLegMiddle;
    public ModelRenderer RightLegThigh;
    public ModelRenderer RightLegLower;
    public ModelRenderer RightLegFeet1;
    public ModelRenderer RightLegFeet2;
    public ModelRenderer LeftLegMiddle;
    public ModelRenderer LeftLegThigh;
    public ModelRenderer LeftLegLower;
    public ModelRenderer LeftLegFeet1;
    public ModelRenderer LeftLegFeet2;
    public ModelRenderer RightArm;
    public ModelRenderer RightHandUpper;
    public ModelRenderer RightHandLower;
    public ModelRenderer LeftArm;
    public ModelRenderer LeftHandUpper;
    public ModelRenderer LeftHandLower;
    public ModelRenderer HeadLower;
    public ModelRenderer HeadThroat;
    public ModelRenderer HeadPoint1;
    public ModelRenderer HeadThroatDetail1;
    public ModelRenderer HeadThroatDetail2;
    public ModelRenderer HeadThroatDetail3;
    public ModelRenderer HeadPoint2;

    private int cycleIndex;
    private double totalDistance;
    private boolean legRotation = false;

    public DuskModel() {
        this.textureWidth = 64;
        this.textureHeight = 64;
        this.HeadPoint1 = new ModelRenderer(this, 31, 15);
        this.HeadPoint1.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.HeadPoint1.addBox(-1.5F, -5.5F, -3.0F, 3, 2, 1, 0.0F);
        this.LeftLegFeet2 = new ModelRenderer(this, 0, 34);
        this.LeftLegFeet2.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.LeftLegFeet2.addBox(-0.5F, 0.5F, -3.5F, 1, 1, 2, 0.0F);
        this.setRotateAngle(LeftLegFeet2, -0.2617993877991494F, 0.0F, 0.0F);
        this.LowerBody1 = new ModelRenderer(this, 0, 6);
        this.LowerBody1.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.LowerBody1.addBox(-1.5F, 3.0F, -1.5F, 3, 3, 2, 0.0F);
        this.RightLegUpper = new ModelRenderer(this, 0, 12);
        this.RightLegUpper.setRotationPoint(0.0F, 12.5F, 0.0F);
        this.RightLegUpper.addBox(-2.5F, 0.0F, -1.5F, 2, 5, 2, 0.0F);
        this.setRotateAngle(RightLegUpper, -0.12217304763960307F, 0.0F, 0.0F);
        this.RightArmShoulder = new ModelRenderer(this, 15, 13);
        this.RightArmShoulder.setRotationPoint(-2.0F, 6.0F, -0.5F);
        this.RightArmShoulder.addBox(-2.0F, -1.0F, -1.0F, 2, 2, 2, 0.0F);
        this.RightLegFeet2 = new ModelRenderer(this, 0, 34);
        this.RightLegFeet2.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.RightLegFeet2.addBox(-0.5F, 0.5F, -3.5F, 1, 1, 2, 0.0F);
        this.setRotateAngle(RightLegFeet2, -0.2617993877991494F, 0.0F, 0.0F);
        this.HeadLower = new ModelRenderer(this, 23, 7);
        this.HeadLower.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.HeadLower.addBox(-2.0F, -1.0F, -2.0F, 4, 1, 4, 0.0F);
        this.LeftHandUpper = new ModelRenderer(this, 15, 26);
        this.LeftHandUpper.setRotationPoint(0.5F, 6.0F, 0.0F);
        this.LeftHandUpper.addBox(-1.0F, 0.0F, -0.5F, 2, 4, 1, 0.0F);
        this.RightLegThigh = new ModelRenderer(this, 0, 38);
        this.RightLegThigh.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.RightLegThigh.addBox(-2.2F, -0.9F, -1.5F, 2, 2, 2, 0.0F);
        this.setRotateAngle(RightLegThigh, 0.0F, 0.0F, 0.4553564018453205F);
        this.RightHandUpper = new ModelRenderer(this, 15, 26);
        this.RightHandUpper.setRotationPoint(-1.5F, 6.0F, 0.0F);
        this.RightHandUpper.addBox(-1.0F, 0.0F, -0.5F, 2, 4, 1, 0.0F);
        this.RightArm = new ModelRenderer(this, 15, 18);
        this.RightArm.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.RightArm.addBox(-2.0F, 1.0F, -0.5F, 1, 5, 1, 0.0F);
        this.LeftHandLower = new ModelRenderer(this, 15, 32);
        this.LeftHandLower.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.LeftHandLower.addBox(-0.5F, 4.0F, -0.5F, 1, 2, 1, 0.0F);
        this.LeftLegThigh = new ModelRenderer(this, 0, 38);
        this.LeftLegThigh.setRotationPoint(-1.8F, 0.6F, 0.0F);
        this.LeftLegThigh.addBox(-1.8F, -1.4F, -1.5F, 2, 2, 2, 0.0F);
        this.setRotateAngle(LeftLegThigh, 0.0F, 0.0F, 1.0471975511965976F);
        this.RightHandLower = new ModelRenderer(this, 15, 32);
        this.RightHandLower.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.RightHandLower.addBox(-0.5F, 4.0F, -0.5F, 1, 2, 1, 0.0F);
        this.HeadThroatDetail1 = new ModelRenderer(this, 26, 23);
        this.HeadThroatDetail1.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.HeadThroatDetail1.addBox(-2.0F, -2.8F, -2.0F, 4, 2, 0, 0.0F);
        this.LeftLegLower = new ModelRenderer(this, 0, 23);
        this.LeftLegLower.setRotationPoint(0.0F, 0.7F, 0.7F);
        this.LeftLegLower.addBox(-0.5F, -0.5F, -5.0F, 1, 1, 5, 0.0F);
        this.setRotateAngle(LeftLegLower, 1.730144887501979F, 0.0F, 0.0F);
        this.RightLegFeet1 = new ModelRenderer(this, 0, 29);
        this.RightLegFeet1.setRotationPoint(0.0F, 0.5F, -4.5F);
        this.RightLegFeet1.addBox(-0.5F, 0.0F, -2.0F, 1, 1, 3, 0.0F);
        this.setRotateAngle(RightLegFeet1, -1.2740903539558606F, 0.0F, 0.0F);
        this.LeftArmShoulder = new ModelRenderer(this, 15, 13);
        this.LeftArmShoulder.setRotationPoint(2.0F, 6.0F, -0.5F);
        this.LeftArmShoulder.addBox(0.0F, -1.0F, -1.0F, 2, 2, 2, 0.0F);
        this.HeadPoint2 = new ModelRenderer(this, 32, 20);
        this.HeadPoint2.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.HeadPoint2.addBox(-1.0F, -5.5F, -3.9F, 2, 1, 1, 0.0F);
        this.HeadUpper = new ModelRenderer(this, 22, 0);
        this.HeadUpper.setRotationPoint(0.0F, 5.0F, -0.5F);
        this.HeadUpper.addBox(-2.0F, -5.5F, -2.0F, 4, 3, 4, 0.0F);
        this.LeftLegUpper = new ModelRenderer(this, 0, 12);
        this.LeftLegUpper.setRotationPoint(3.0F, 12.5F, 0.0F);
        this.LeftLegUpper.addBox(-2.5F, 0.0F, -1.5F, 2, 5, 2, 0.0F);
        this.setRotateAngle(LeftLegUpper, -0.12217304763960307F, 0.0F, 0.0F);
        this.RightLegMiddle = new ModelRenderer(this, 0, 19);
        this.RightLegMiddle.setRotationPoint(-1.6F, 4.6F, -0.3F);
        this.RightLegMiddle.addBox(-0.5F, 0.0F, -1.5F, 1, 1, 3, 0.0F);
        this.setRotateAngle(RightLegMiddle, -0.12217304763960307F, 0.0F, 0.0F);
        this.RightLegLower = new ModelRenderer(this, 0, 23);
        this.RightLegLower.setRotationPoint(0.0F, 0.7F, 0.7F);
        this.RightLegLower.addBox(-0.5F, -0.5F, -5.0F, 1, 1, 5, 0.0F);
        this.setRotateAngle(RightLegLower, 1.7278759594743864F, 0.0F, 0.0F);
        this.HeadThroatDetail3 = new ModelRenderer(this, 26, 27);
        this.HeadThroatDetail3.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.HeadThroatDetail3.addBox(2.0F, -2.8F, -2.0F, 0, 2, 2, 0.0F);
        this.LeftLegFeet1 = new ModelRenderer(this, 0, 29);
        this.LeftLegFeet1.setRotationPoint(0.0F, 0.5F, -4.5F);
        this.LeftLegFeet1.addBox(-0.5F, 0.0F, -2.0F, 1, 1, 3, 0.0F);
        this.setRotateAngle(LeftLegFeet1, -1.2740903539558606F, 0.0F, 0.0F);
        this.LeftArm = new ModelRenderer(this, 15, 18);
        this.LeftArm.setRotationPoint(1.0F, 0.0F, 0.0F);
        this.LeftArm.addBox(0.0F, 1.0F, -0.5F, 1, 5, 1, 0.0F);
        this.UpperBody1 = new ModelRenderer(this, 9, 0);
        this.UpperBody1.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.UpperBody1.addBox(-2.0F, -2.0F, -1.5F, 4, 3, 2, 0.0F);
        this.Body1 = new ModelRenderer(this, 0, 0);
        this.Body1.setRotationPoint(0.0F, 7.0F, 0.0F);
        this.Body1.addBox(-1.0F, 1.0F, -1.5F, 2, 2, 2, 0.0F);
        this.LeftLegMiddle = new ModelRenderer(this, 0, 19);
        this.LeftLegMiddle.setRotationPoint(-1.6F, 4.6F, -0.3F);
        this.LeftLegMiddle.addBox(-0.5F, 0.0F, -1.5F, 1, 1, 3, 0.0F);
        this.setRotateAngle(LeftLegMiddle, -0.12217304763960307F, 0.0F, 0.0F);
        this.HeadThroat = new ModelRenderer(this, 39, 1);
        this.HeadThroat.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.HeadThroat.addBox(-2.0F, -2.5F, 0.0F, 4, 2, 2, 0.0F);
        this.HeadThroatDetail2 = new ModelRenderer(this, 26, 24);
        this.HeadThroatDetail2.setRotationPoint(0.0F, 0.0F, 0.0F);
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
    public void render(MatrixStack matrixStackIn, IVertexBuilder bufferIn, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha) {
        this.LeftLegUpper.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
        this.HeadUpper.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
        this.RightLegUpper.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
        this.Body1.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
        this.LeftArmShoulder.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
        this.RightArmShoulder.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
    }

    @Override
    public void setRotationAngles(T entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        if(!Minecraft.getInstance().isGamePaused()) {
            //if(EntityHelper.getState(ent) == 0)
            //{
            if(entityIn.getDistanceSq(entityIn.prevPosX, entityIn.prevPosY, entityIn.prevPosZ) > 0) {
                this.Body1.rotateAngleX = MathHelper.cos(limbSwing * 0.8662F) * 0.2F * limbSwingAmount;

                this.RightArmShoulder.rotateAngleX = MathHelper.cos(limbSwing * 0.8662F) * 0.5F * limbSwingAmount;
                this.RightHandUpper.rotateAngleX = MathHelper.cos(limbSwing * 0.8662F) * 0.8F * limbSwingAmount;

                this.RightLegUpper.rotateAngleX = MathHelper.cos(limbSwing * 0.5F) * 2.0F * limbSwingAmount;//degToRad(walkingUpperLegAnimation[cycleIndex]);

                this.LeftArmShoulder.rotateAngleX = MathHelper.cos(limbSwing * 0.8662F + (float) Math.PI) * 0.5F * limbSwingAmount;
                this.LeftHandUpper.rotateAngleX = MathHelper.cos(limbSwing * 0.8662F + (float) Math.PI) * 0.8F * limbSwingAmount;

                this.LeftLegUpper.rotateAngleX = MathHelper.cos(limbSwing * 0.5F + (float) Math.PI) * 2.0F * limbSwingAmount;
            }
            else {
                this.LeftLegUpper.rotateAngleX = degToRad(-7);
                this.RightLegMiddle.rotateAngleX = degToRad(-7);
                this.RightLegLower.rotateAngleX = degToRad(99);
                this.RightLegUpper.rotateAngleX = degToRad(-7);
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
    public void setRotateAngle(ModelRenderer modelRenderer, float x, float y, float z) {
        modelRenderer.rotateAngleX = x;
        modelRenderer.rotateAngleY = y;
        modelRenderer.rotateAngleZ = z;
    }

    protected float degToRad(double degrees) {
        return (float) (degrees * (double)Math.PI / 180) ;
    }

    public void setRotation(ModelRenderer modelRenderer, float x, float y, float z) {
        modelRenderer.rotateAngleX = x;
        modelRenderer.rotateAngleY = y;
        modelRenderer.rotateAngleZ = z;
    }
}