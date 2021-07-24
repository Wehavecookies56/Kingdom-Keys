package online.kingdomkeys.kingdomkeys.client.model.armor;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.decoration.ArmorStand;

public class VentusModel extends HumanoidModel {
		
	public ModelPart rightElbowBase;
    public ModelPart rightElbowSpike;
    public ModelPart rightElbowSpike2;
    public ModelPart rightFoot;
    public ModelPart rightKneeBase;
    public ModelPart rightFootTip;
    public ModelPart rightKnee1;
    public ModelPart rightKnee2;
    public ModelPart rightKneeSpike;
    public ModelPart horn1;
    public ModelPart horn2;
    public ModelPart leftElbowBase;
    public ModelPart leftElbowSpike;
    public ModelPart leftElbowSpike2;
    public ModelPart leftFoot;
    public ModelPart leftKneeBase;
    public ModelPart leftFootTip;
    public ModelPart leftKnee1;
    public ModelPart leftKnee2;
    public ModelPart leftKneeSpike;

	public VentusModel(float size) {
		super(size, 0, 64, 64);

		this.leftKnee1 = new ModelPart(this, 31, 32);
        this.leftKnee1.setPos(0.0F, 0.0F, 0.0F);
        this.leftKnee1.addBox(-0.5F, 0.5F, -0.5F, 1.7F, 1.0F, 0.2F, 0.0F, 0.0F, 0.0F);
        this.setRotateAngle(leftKnee1, 0.0F, 0.0F, 0.7853981633974483F);
        this.rightKnee1 = new ModelPart(this, 31, 32);
        this.rightKnee1.setPos(0.0F, 0.0F, 0.0F);
        this.rightKnee1.addBox(-0.5F, 0.5F, -0.5F, 1.7F, 1.0F, 0.2F, 0.0F, 0.0F, 0.0F);
        this.setRotateAngle(rightKnee1, 0.0F, 0.0F, 0.7853981633974483F);
        this.rightElbowSpike = new ModelPart(this, 26, 38);
        this.rightElbowSpike.setPos(2.6F, 0.0F, 0.0F);
        this.rightElbowSpike.addBox(-0.5F, -3.0F, -1.5F, 1.0F, 3.0F, 3.0F, -0.2F, 0.0F, -0.3F);
        this.setRotateAngle(rightElbowSpike, 0.0F, 0.0F, 0.3490658503988659F);
        this.rightKneeSpike = new ModelPart(this, 17, 32);
        this.rightKneeSpike.setPos(-2.4F, 0.0F, 2.0F);
        this.rightKneeSpike.addBox(0.0F, -2.8F, -0.4F, 0.5F, 2.8F, 0.8F, 0.0F, 0.0F, 0.0F);
        this.setRotateAngle(rightKneeSpike, 0.0F, 0.0F, -0.3127630032889644F);
        this.leftElbowBase = new ModelPart(this, 36, 32);
        this.leftElbowBase.setPos(1.0F, 5.0F, 0.0F);
        this.leftElbowBase.addBox(-3.5F, -2.0F, -3.5F, 7.0F, 5.0F, 7.0F, -0.8F, 0.0F, -0.8F);
        this.leftKneeSpike = new ModelPart(this, 17, 32);
        this.leftKneeSpike.setPos(2.0F, 0.0F, 2.0F);
        this.leftKneeSpike.addBox(-0.01F, -2.8F, -0.4F, 0.5F, 2.8F, 0.8F, 0.0F, 0.0F, 0.0F);
        this.setRotateAngle(leftKneeSpike, 0.0F, 0.0F, 0.3127630032889644F);
        this.horn1 = new ModelPart(this, 0, 0);
        this.horn1.setPos(3.5F, -12.0F, 0.0F);
        this.horn1.texOffs(0, 32).addBox(0.0F, 0.0F, 0.0F, 1.0F, 4.5F, 2.0F, 0.0F, 0.0F, 0.0F);
        this.setRotateAngle(horn1, -0.27366763203903305F, 0.0F, 0.0F);
        this.rightKnee2 = new ModelPart(this, 31, 34);
        this.rightKnee2.setPos(0.0F, 0.0F, 0.0F);
        this.rightKnee2.addBox(-1.5F, 0.5F, -0.5F, 1.7F, 1.0F, 0.2F, 0.0F, 0.0F, 0.0F);
        this.setRotateAngle(rightKnee2, 0.0F, 0.0F, -0.7853981633974483F);
        this.leftElbowSpike = new ModelPart(this, 26, 38);
        this.leftElbowSpike.setPos(2.6F, 0.0F, 0.0F);
        this.leftElbowSpike.addBox(-0.5F, -3.0F, -1.5F, 1.0F, 3.0F, 3.0F, -0.2F, 0.0F, -0.3F);
        this.setRotateAngle(leftElbowSpike, 0.0F, 0.0F, 0.3490658503988659F);
        this.rightKneeBase = new ModelPart(this, 16, 32);
        this.rightKneeBase.setPos(0.0F, 4.0F, -2.0F);
        this.rightKneeBase.addBox(-2.4F, 0.0F, -0.5F, 4.8F, 1.0F, 5.0F, 0.0F, 0.0F, 0.0F);
        this.leftKneeBase = new ModelPart(this, 16, 32);
        this.leftKneeBase.setPos(0.0F, 4.0F, -2.0F);
        this.leftKneeBase.addBox(-2.3F, 0.0F, -0.5F, 4.8F, 1.0F, 5.0F, 0.0F, 0.0F, 0.0F);
        this.rightElbowSpike2 = new ModelPart(this, 29, 44);
        this.rightElbowSpike2.setPos(0.1F, -2.9F, 0.0F);
        this.rightElbowSpike2.addBox(-0.5F, -3.0F, -1.0F, 1.0F, 3.0F, 2.0F, -0.3F, 0.0F, -0.3F);
        this.setRotateAngle(rightElbowSpike2, 0.0F, 0.0F, -0.1260127683321441F);
        this.rightElbowBase = new ModelPart(this, 36, 32);
        this.rightElbowBase.setPos(-1.0F, 5.0F, 0.0F);
        this.rightElbowBase.addBox(-3.5F, -2.0F, -3.5F, 7.0F, 5.0F, 7.0F, -0.8F, 0.0F, -0.8F);
        this.setRotateAngle(rightElbowBase, 0.0F, 3.141592653589793F, 0.0F);
        this.leftElbowSpike2 = new ModelPart(this, 29, 44);
        this.leftElbowSpike2.setPos(0.1F, -2.9F, 0.0F);
        this.leftElbowSpike2.addBox(-0.5F, -3.0F, -1.0F, 1.0F, 3.0F, 2.0F, -0.3F, 0.0F, -0.3F);
        this.setRotateAngle(leftElbowSpike2, 0.0F, 0.0F, -0.1260127683321441F);
        this.leftKnee2 = new ModelPart(this, 31, 34);
        this.leftKnee2.setPos(0.0F, 0.0F, 0.0F);
        this.leftKnee2.addBox(-1.5F, 0.5F, -0.5F, 1.7F, 1.0F, 0.2F, 0.0F, 0.0F, 0.0F);
        this.setRotateAngle(leftKnee2, 0.0F, 0.0F, -0.7853981633974483F);
        this.rightFoot = new ModelPart(this, 0, 41);
        this.rightFoot.setPos(0.0F, 9.0F, -1.0F);
        this.rightFoot.addBox(-2.1F, 0.0F, -3.0F, 4.0F, 3.0F, 2.0F, -0.1F, 0.0F, 0.0F);
        this.leftFoot = new ModelPart(this, 0, 41);
        this.leftFoot.setPos(0.0F, 9.0F, -1.0F);
        this.leftFoot.addBox(-1.9F, 0.0F, -3.0F, 4.0F, 3.0F, 2.0F, -0.1F, 0.0F, 0.0F);
        this.horn2 = new ModelPart(this, 0, 0);
        this.horn2.setPos(-4.5F, -12.0F, 0.0F);
        this.horn2.texOffs(0, 32).addBox(0.0F, 0.0F, 0.0F, 1.0F, 4.5F, 2.0F, 0.0F, 0.0F, 0.0F);
        this.setRotateAngle(horn2, -0.27366763203903305F, 0.0F, 0.0F);
        this.leftFootTip = new ModelPart(this, 16, 41);
        this.leftFootTip.setPos(0.0F, 1.0F, -3.0F);
        this.leftFootTip.addBox(-1.9F, 0.0F, -1.0F, 4.0F, 2.0F, 1.0F, -0.2F, 0.0F, 0.0F);
        this.rightFootTip = new ModelPart(this, 16, 41);
        this.rightFootTip.setPos(0.0F, 1.0F, -3.0F);
        this.rightFootTip.addBox(-2.1F, 0.0F, -1.0F, 4.0F, 2.0F, 1.0F, -0.2F, 0.0F, 0.0F);
        this.leftKneeBase.addChild(this.leftKnee1);
        this.rightKneeBase.addChild(this.rightKnee1);
        this.rightElbowBase.addChild(this.rightElbowSpike);
        this.rightKneeBase.addChild(this.rightKneeSpike);
        this.leftArm.addChild(this.leftElbowBase);
        this.leftKneeBase.addChild(this.leftKneeSpike);
        this.head.addChild(this.horn1);
        this.rightKneeBase.addChild(this.rightKnee2);
        this.leftElbowBase.addChild(this.leftElbowSpike);
        this.rightLeg.addChild(this.rightKneeBase);
        this.leftLeg.addChild(this.leftKneeBase);
        this.rightElbowSpike.addChild(this.rightElbowSpike2);
        this.rightArm.addChild(this.rightElbowBase);
        this.leftElbowSpike.addChild(this.leftElbowSpike2);
        this.leftKneeBase.addChild(this.leftKnee2);
        this.rightLeg.addChild(this.rightFoot);
        this.leftLeg.addChild(this.leftFoot);
        this.head.addChild(this.horn2);
        this.leftFoot.addChild(this.leftFootTip);
        this.rightFoot.addChild(this.rightFootTip);
    }

	
	@Override
	public void renderToBuffer(PoseStack matrixStackIn, VertexConsumer bufferIn, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha) {
		super.renderToBuffer(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
	}
	
	@Override
	public void setupAnim(LivingEntity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		if(entityIn instanceof ArmorStand) {
			super.setupAnim(entityIn, 0, 0, 0, 0, 0);
		} else {
			super.setupAnim(entityIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
		}
	}

    public void setRotateAngle(ModelPart modelRenderer, float x, float y, float z) {
        modelRenderer.getXRot() = x;
        modelRenderer.getYRot() = y;
        modelRenderer.zRot = z;
    }


}
