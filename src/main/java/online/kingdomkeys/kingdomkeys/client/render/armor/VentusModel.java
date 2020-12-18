package online.kingdomkeys.kingdomkeys.client.render.armor;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;

import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.ArmorStandEntity;

public class VentusModel extends BipedModel {
		
	public ModelRenderer rightElbowBase;
    public ModelRenderer rightElbowSpike;
    public ModelRenderer rightElbowSpike2;
    public ModelRenderer rightFoot;
    public ModelRenderer rightKneeBase;
    public ModelRenderer rightFootTip;
    public ModelRenderer rightKnee1;
    public ModelRenderer rightKnee2;
    public ModelRenderer rightKneeSpike;
    public ModelRenderer horn1;
    public ModelRenderer horn2;
    public ModelRenderer leftElbowBase;
    public ModelRenderer leftElbowSpike;
    public ModelRenderer leftElbowSpike2;
    public ModelRenderer leftFoot;
    public ModelRenderer leftKneeBase;
    public ModelRenderer leftFootTip;
    public ModelRenderer leftKnee1;
    public ModelRenderer leftKnee2;
    public ModelRenderer leftKneeSpike;

	public VentusModel(float size) {
		super(size, 0, 64, 64);

		this.leftKnee1 = new ModelRenderer(this, 31, 32);
        this.leftKnee1.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.leftKnee1.addBox(-0.5F, 0.5F, -0.5F, 1.7F, 1.0F, 0.2F, 0.0F, 0.0F, 0.0F);
        this.setRotateAngle(leftKnee1, 0.0F, 0.0F, 0.7853981633974483F);
        this.rightKnee1 = new ModelRenderer(this, 31, 32);
        this.rightKnee1.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.rightKnee1.addBox(-0.5F, 0.5F, -0.5F, 1.7F, 1.0F, 0.2F, 0.0F, 0.0F, 0.0F);
        this.setRotateAngle(rightKnee1, 0.0F, 0.0F, 0.7853981633974483F);
        this.rightElbowSpike = new ModelRenderer(this, 26, 38);
        this.rightElbowSpike.setRotationPoint(2.6F, 0.0F, 0.0F);
        this.rightElbowSpike.addBox(-0.5F, -3.0F, -1.5F, 1.0F, 3.0F, 3.0F, -0.2F, 0.0F, -0.3F);
        this.setRotateAngle(rightElbowSpike, 0.0F, 0.0F, 0.3490658503988659F);
        this.rightKneeSpike = new ModelRenderer(this, 17, 32);
        this.rightKneeSpike.setRotationPoint(-2.4F, 0.0F, 2.0F);
        this.rightKneeSpike.addBox(0.0F, -2.8F, -0.4F, 0.5F, 2.8F, 0.8F, 0.0F, 0.0F, 0.0F);
        this.setRotateAngle(rightKneeSpike, 0.0F, 0.0F, -0.3127630032889644F);
        this.leftElbowBase = new ModelRenderer(this, 36, 32);
        this.leftElbowBase.setRotationPoint(1.0F, 5.0F, 0.0F);
        this.leftElbowBase.addBox(-3.5F, -2.0F, -3.5F, 7.0F, 5.0F, 7.0F, -0.8F, 0.0F, -0.8F);
        this.leftKneeSpike = new ModelRenderer(this, 17, 32);
        this.leftKneeSpike.setRotationPoint(2.0F, 0.0F, 2.0F);
        this.leftKneeSpike.addBox(-0.01F, -2.8F, -0.4F, 0.5F, 2.8F, 0.8F, 0.0F, 0.0F, 0.0F);
        this.setRotateAngle(leftKneeSpike, 0.0F, 0.0F, 0.3127630032889644F);
        this.horn1 = new ModelRenderer(this, 0, 0);
        this.horn1.setRotationPoint(3.5F, -12.0F, 0.0F);
        this.horn1.setTextureOffset(0, 32).addBox(0.0F, 0.0F, 0.0F, 1.0F, 4.5F, 2.0F, 0.0F, 0.0F, 0.0F);
        this.setRotateAngle(horn1, -0.27366763203903305F, 0.0F, 0.0F);
        this.rightKnee2 = new ModelRenderer(this, 31, 34);
        this.rightKnee2.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.rightKnee2.addBox(-1.5F, 0.5F, -0.5F, 1.7F, 1.0F, 0.2F, 0.0F, 0.0F, 0.0F);
        this.setRotateAngle(rightKnee2, 0.0F, 0.0F, -0.7853981633974483F);
        this.leftElbowSpike = new ModelRenderer(this, 26, 38);
        this.leftElbowSpike.setRotationPoint(2.6F, 0.0F, 0.0F);
        this.leftElbowSpike.addBox(-0.5F, -3.0F, -1.5F, 1.0F, 3.0F, 3.0F, -0.2F, 0.0F, -0.3F);
        this.setRotateAngle(leftElbowSpike, 0.0F, 0.0F, 0.3490658503988659F);
        this.rightKneeBase = new ModelRenderer(this, 16, 32);
        this.rightKneeBase.setRotationPoint(0.0F, 4.0F, -2.0F);
        this.rightKneeBase.addBox(-2.4F, 0.0F, -0.5F, 4.8F, 1.0F, 5.0F, 0.0F, 0.0F, 0.0F);
        this.leftKneeBase = new ModelRenderer(this, 16, 32);
        this.leftKneeBase.setRotationPoint(0.0F, 4.0F, -2.0F);
        this.leftKneeBase.addBox(-2.3F, 0.0F, -0.5F, 4.8F, 1.0F, 5.0F, 0.0F, 0.0F, 0.0F);
        this.rightElbowSpike2 = new ModelRenderer(this, 29, 44);
        this.rightElbowSpike2.setRotationPoint(0.1F, -2.9F, 0.0F);
        this.rightElbowSpike2.addBox(-0.5F, -3.0F, -1.0F, 1.0F, 3.0F, 2.0F, -0.3F, 0.0F, -0.3F);
        this.setRotateAngle(rightElbowSpike2, 0.0F, 0.0F, -0.1260127683321441F);
        this.rightElbowBase = new ModelRenderer(this, 36, 32);
        this.rightElbowBase.setRotationPoint(-1.0F, 5.0F, 0.0F);
        this.rightElbowBase.addBox(-3.5F, -2.0F, -3.5F, 7.0F, 5.0F, 7.0F, -0.8F, 0.0F, -0.8F);
        this.setRotateAngle(rightElbowBase, 0.0F, 3.141592653589793F, 0.0F);
        this.leftElbowSpike2 = new ModelRenderer(this, 29, 44);
        this.leftElbowSpike2.setRotationPoint(0.1F, -2.9F, 0.0F);
        this.leftElbowSpike2.addBox(-0.5F, -3.0F, -1.0F, 1.0F, 3.0F, 2.0F, -0.3F, 0.0F, -0.3F);
        this.setRotateAngle(leftElbowSpike2, 0.0F, 0.0F, -0.1260127683321441F);
        this.leftKnee2 = new ModelRenderer(this, 31, 34);
        this.leftKnee2.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.leftKnee2.addBox(-1.5F, 0.5F, -0.5F, 1.7F, 1.0F, 0.2F, 0.0F, 0.0F, 0.0F);
        this.setRotateAngle(leftKnee2, 0.0F, 0.0F, -0.7853981633974483F);
        this.rightFoot = new ModelRenderer(this, 0, 41);
        this.rightFoot.setRotationPoint(0.0F, 9.0F, -1.0F);
        this.rightFoot.addBox(-2.1F, 0.0F, -3.0F, 4.0F, 3.0F, 2.0F, -0.1F, 0.0F, 0.0F);
        this.leftFoot = new ModelRenderer(this, 0, 41);
        this.leftFoot.setRotationPoint(0.0F, 9.0F, -1.0F);
        this.leftFoot.addBox(-1.9F, 0.0F, -3.0F, 4.0F, 3.0F, 2.0F, -0.1F, 0.0F, 0.0F);
        this.horn2 = new ModelRenderer(this, 0, 0);
        this.horn2.setRotationPoint(-4.5F, -12.0F, 0.0F);
        this.horn2.setTextureOffset(0, 32).addBox(0.0F, 0.0F, 0.0F, 1.0F, 4.5F, 2.0F, 0.0F, 0.0F, 0.0F);
        this.setRotateAngle(horn2, -0.27366763203903305F, 0.0F, 0.0F);
        this.leftFootTip = new ModelRenderer(this, 16, 41);
        this.leftFootTip.setRotationPoint(0.0F, 1.0F, -3.0F);
        this.leftFootTip.addBox(-1.9F, 0.0F, -1.0F, 4.0F, 2.0F, 1.0F, -0.2F, 0.0F, 0.0F);
        this.rightFootTip = new ModelRenderer(this, 16, 41);
        this.rightFootTip.setRotationPoint(0.0F, 1.0F, -3.0F);
        this.rightFootTip.addBox(-2.1F, 0.0F, -1.0F, 4.0F, 2.0F, 1.0F, -0.2F, 0.0F, 0.0F);
        this.leftKneeBase.addChild(this.leftKnee1);
        this.rightKneeBase.addChild(this.rightKnee1);
        this.rightElbowBase.addChild(this.rightElbowSpike);
        this.rightKneeBase.addChild(this.rightKneeSpike);
        this.bipedLeftArm.addChild(this.leftElbowBase);
        this.leftKneeBase.addChild(this.leftKneeSpike);
        this.bipedHead.addChild(this.horn1);
        this.rightKneeBase.addChild(this.rightKnee2);
        this.leftElbowBase.addChild(this.leftElbowSpike);
        this.bipedRightLeg.addChild(this.rightKneeBase);
        this.bipedLeftLeg.addChild(this.leftKneeBase);
        this.rightElbowSpike.addChild(this.rightElbowSpike2);
        this.bipedRightArm.addChild(this.rightElbowBase);
        this.leftElbowSpike.addChild(this.leftElbowSpike2);
        this.leftKneeBase.addChild(this.leftKnee2);
        this.bipedRightLeg.addChild(this.rightFoot);
        this.bipedLeftLeg.addChild(this.leftFoot);
        this.bipedHead.addChild(this.horn2);
        this.leftFoot.addChild(this.leftFootTip);
        this.rightFoot.addChild(this.rightFootTip);
    }

	
	@Override
	public void render(MatrixStack matrixStackIn, IVertexBuilder bufferIn, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha) {
		super.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
	}
	
	@Override
	public void setRotationAngles(LivingEntity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		if(entityIn instanceof ArmorStandEntity) {
			super.setRotationAngles(entityIn, 0, 0, 0, 0, 0);
		} else {
			super.setRotationAngles(entityIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
		}
	}

    public void setRotateAngle(ModelRenderer modelRenderer, float x, float y, float z) {
        modelRenderer.rotateAngleX = x;
        modelRenderer.rotateAngleY = y;
        modelRenderer.rotateAngleZ = z;
    }


}
