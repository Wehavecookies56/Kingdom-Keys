package online.kingdomkeys.kingdomkeys.client.model.entity;

import java.util.ArrayList;
import java.util.List;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.client.ClientUtils.Angle;
import online.kingdomkeys.kingdomkeys.client.ClientUtils.ModelAnimation;
import online.kingdomkeys.kingdomkeys.entity.EntityHelper;

//TODO port new model
@OnlyIn(Dist.CLIENT)
public class SoldierModel<T extends LivingEntity> extends EntityModel<T> {
    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation(KingdomKeys.MODID, "soldier"), "main");
    private final ModelPart right_leg;
	private final ModelPart left_leg;
	private final ModelPart body;
	private final ModelPart left_arm;
	private final ModelPart right_arm;
	private final ModelPart head;

	public SoldierModel(ModelPart root) {
		this.right_leg = root.getChild("right_leg");
		this.left_leg = root.getChild("left_leg");
		this.body = root.getChild("body");
		this.left_arm = root.getChild("left_arm");
		this.right_arm = root.getChild("right_arm");
		this.head = root.getChild("head");
		
        ModelAnimation leftLegTopAnim = new ModelAnimation(left_leg, 0, -40, 40, 0, true, Angle.X, right_leg);
        ModelAnimation headAnim = new ModelAnimation(head, 0, -30, 30, 0, true, Angle.Z, null);

        animation.add(leftLegTopAnim);
        animation.add(headAnim);
	}
	
    List<ModelAnimation> animation = new ArrayList<ModelAnimation>();

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition right_leg = partdefinition.addOrReplaceChild("right_leg", CubeListBuilder.create().texOffs(17, 51).addBox(-1.0F, 9.5F, -5.5F, 2.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(80, 18).addBox(-1.5F, 9.5F, -4.5F, 3.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(25, 72).addBox(-2.0F, 9.5F, -2.5F, 4.0F, 2.0F, 4.0F, new CubeDeformation(0.0F))
		.texOffs(76, 29).addBox(-1.5F, 8.5F, -2.5F, 3.0F, 1.0F, 4.0F, new CubeDeformation(0.0F))
		.texOffs(80, 13).addBox(-1.5F, 6.5F, -1.0F, 3.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(50, 0).addBox(-1.5F, 9.5F, 1.5F, 3.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(63, 10).addBox(-2.0F, -1.5F, -2.0F, 4.0F, 8.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(-2.5F, 12.5F, 0.5F));

		PartDefinition left_leg = partdefinition.addOrReplaceChild("left_leg", CubeListBuilder.create().texOffs(30, 19).addBox(-1.0F, 9.5F, -5.5F, 2.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(26, 79).addBox(-1.5F, 9.5F, -4.5F, 3.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(68, 71).addBox(-2.0F, 9.5F, -2.5F, 4.0F, 2.0F, 4.0F, new CubeDeformation(0.0F))
		.texOffs(52, 19).addBox(-1.5F, 8.5F, -2.5F, 3.0F, 1.0F, 4.0F, new CubeDeformation(0.0F))
		.texOffs(0, 79).addBox(-1.5F, 6.5F, -1.0F, 3.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(28, 55).addBox(-1.5F, 9.5F, 1.5F, 3.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(54, 55).addBox(-2.0F, -1.5F, -2.0F, 4.0F, 8.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(2.5F, 12.5F, 0.5F));

		PartDefinition body = partdefinition.addOrReplaceChild("body", CubeListBuilder.create().texOffs(30, 11).addBox(-5.0F, 5.0F, -3.0F, 10.0F, 1.0F, 6.0F, new CubeDeformation(0.0F))
		.texOffs(0, 17).addBox(-5.5F, 6.0F, -3.5F, 11.0F, 2.0F, 7.0F, new CubeDeformation(0.0F))
		.texOffs(0, 27).addBox(-5.0F, -5.0F, -2.5F, 10.0F, 7.0F, 5.0F, new CubeDeformation(0.0F))
		.texOffs(79, 35).addBox(-3.0F, 2.0F, -3.5F, 6.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(0, 68).addBox(-2.0F, 3.0F, -3.5F, 4.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(0, 68).addBox(-2.0F, 3.0F, 2.5F, 4.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(79, 35).addBox(-3.0F, 2.0F, 2.5F, 6.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(32, 64).addBox(-4.0F, -4.0F, -3.5F, 8.0F, 6.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(32, 64).addBox(-4.0F, -4.0F, 2.5F, 8.0F, 6.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(31, 31).addBox(-4.5F, 4.0F, -2.5F, 9.0F, 1.0F, 5.0F, new CubeDeformation(0.0F))
		.texOffs(26, 0).addBox(-4.5F, 2.0F, -2.5F, 9.0F, 1.0F, 5.0F, new CubeDeformation(0.0F))
		.texOffs(34, 38).addBox(-4.0F, 3.0F, -2.5F, 8.0F, 1.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 5.0F, 0.5F));

		PartDefinition left_arm = partdefinition.addOrReplaceChild("left_arm", CubeListBuilder.create().texOffs(52, 76).addBox(-1.2067F, 0.0F, -1.3846F, 3.0F, 6.0F, 3.0F, new CubeDeformation(0.0F))
		.texOffs(74, 60).addBox(-1.2067F, 6.0F, -1.8846F, 4.0F, 1.0F, 4.0F, new CubeDeformation(0.0F))
		.texOffs(55, 0).addBox(-2.2067F, -2.0F, -2.3846F, 5.0F, 4.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offset(7.2067F, 1.0F, 0.3846F));

		PartDefinition left_hand = left_arm.addOrReplaceChild("left_hand", CubeListBuilder.create().texOffs(31, 31).addBox(10.0F, -12.0F, 2.0F, 1.0F, 3.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(0, 27).addBox(10.0F, -12.0F, -2.0F, 1.0F, 3.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(26, 0).addBox(10.0F, -12.0F, 0.0F, 1.0F, 3.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(0, 40).addBox(6.0F, -12.0F, -2.0F, 1.0F, 3.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(55, 26).addBox(6.0F, -15.0F, -2.0F, 5.0F, 3.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offset(-7.2067F, 22.0F, -0.3846F));

		PartDefinition cube_r1 = left_hand.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(50, 7).addBox(-0.5F, -1.0F, 3.5F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(57, 10).addBox(-0.5F, -1.0F, 1.5F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(62, 10).addBox(-0.5F, -1.0F, -0.5F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(10.0168F, -8.4676F, -1.5F, 0.0F, 0.0F, 0.6109F));

		PartDefinition fingertip_r1 = left_hand.addOrReplaceChild("fingertip_r1", CubeListBuilder.create().texOffs(10, 57).addBox(-0.5F, 1.5F, 1.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(57, 14).addBox(-0.5F, 1.5F, 3.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(0, 62).addBox(-0.5F, 1.5F, -0.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(11.0914F, -8.3446F, -1.5F, 0.0F, 0.0F, 1.2217F));

		PartDefinition right_arm = partdefinition.addOrReplaceChild("right_arm", CubeListBuilder.create().texOffs(39, 76).addBox(-1.7933F, 0.0F, -1.3846F, 3.0F, 6.0F, 3.0F, new CubeDeformation(0.0F))
		.texOffs(11, 75).addBox(-2.7933F, 6.1F, -1.8846F, 4.0F, 1.0F, 4.0F, new CubeDeformation(0.0F))
		.texOffs(12, 55).addBox(-2.7933F, -2.0F, -2.3846F, 5.0F, 4.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offset(-7.2067F, 1.0F, 0.3846F));

		PartDefinition right_hand = right_arm.addOrReplaceChild("right_hand", CubeListBuilder.create().texOffs(33, 55).addBox(0.0914F, -6.5019F, 0.6014F, 5.0F, 3.0F, 5.0F, new CubeDeformation(0.0F))
		.texOffs(0, 57).addBox(0.0914F, -3.5019F, 4.6014F, 1.0F, 3.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(49, 55).addBox(0.0914F, -3.5019F, 2.6014F, 1.0F, 3.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(34, 38).addBox(0.0914F, -3.5019F, 0.6014F, 1.0F, 3.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(17, 45).addBox(4.0914F, -3.5019F, 0.6014F, 1.0F, 3.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(-3.8847F, 13.6019F, -2.986F));

		PartDefinition fingertip_r2 = right_hand.addOrReplaceChild("fingertip_r2", CubeListBuilder.create().texOffs(51, 68).addBox(-0.5F, 1.5F, 3.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(56, 68).addBox(-0.5F, 1.5F, 1.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(49, 76).addBox(-0.5F, 1.5F, -0.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.1535F, 1.1014F, 0.0F, 0.0F, -1.2217F));

		PartDefinition cube_r2 = right_hand.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(24, 65).addBox(-0.5F, -1.0F, 3.5F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(38, 72).addBox(-0.5F, -1.0F, 1.5F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(43, 72).addBox(-0.5F, -1.0F, -0.5F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.0745F, 0.0305F, 1.1014F, 0.0F, 0.0F, -0.6109F));

		PartDefinition head = partdefinition.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 71).addBox(-2.0F, -3.0F, -1.46F, 4.0F, 4.0F, 3.0F, new CubeDeformation(0.0F))
		.texOffs(0, 0).addBox(-4.5F, -12.0F, -2.46F, 9.0F, 9.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 1.0F, 0.46F));

		PartDefinition hat = head.addOrReplaceChild("hat", CubeListBuilder.create().texOffs(71, 23).addBox(-3.5F, -30.0F, -6.0F, 7.0F, 3.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(0, 17).addBox(3.5F, -36.0F, -4.0F, 1.0F, 3.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(71, 55).addBox(-3.5F, -36.0F, -4.0F, 7.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(49, 71).addBox(-3.5F, -38.0F, 5.0F, 7.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(30, 20).addBox(-3.5F, -39.0F, -2.0F, 7.0F, 3.0F, 7.0F, new CubeDeformation(0.0F))
		.texOffs(64, 61).addBox(3.5F, -38.0F, -2.0F, 1.0F, 2.0F, 7.0F, new CubeDeformation(0.0F))
		.texOffs(0, 58).addBox(-4.5F, -38.0F, -2.0F, 1.0F, 2.0F, 7.0F, new CubeDeformation(0.0F))
		.texOffs(0, 40).addBox(-5.5F, -36.0F, -2.0F, 1.0F, 9.0F, 7.0F, new CubeDeformation(0.0F))
		.texOffs(71, 0).addBox(-3.5F, -38.0F, -4.0F, 7.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(62, 42).addBox(-4.5F, -36.0F, 5.0F, 9.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(10, 40).addBox(-3.5F, -36.0F, -6.0F, 7.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(41, 45).addBox(-4.5F, -34.0F, 5.0F, 9.0F, 7.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(24, 38).addBox(4.5F, -36.0F, -2.0F, 1.0F, 9.0F, 7.0F, new CubeDeformation(0.0F))
		.texOffs(64, 47).addBox(-3.5F, -34.0F, 7.0F, 7.0F, 6.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(33, 7).addBox(-3.5F, -36.0F, 7.0F, 7.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(58, 36).addBox(-4.5F, -30.0F, -4.0F, 9.0F, 3.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(0, 0).addBox(-4.5F, -36.0F, -4.0F, 1.0F, 3.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(76, 5).addBox(-1.5F, -43.0F, 0.0F, 3.0F, 4.0F, 3.0F, new CubeDeformation(0.0F))
		.texOffs(78, 78).addBox(-1.5F, -45.0F, 1.0F, 3.0F, 3.0F, 3.0F, new CubeDeformation(0.0F))
		.texOffs(12, 65).addBox(-1.5F, -47.0F, 2.0F, 3.0F, 3.0F, 5.0F, new CubeDeformation(0.0F))
		.texOffs(65, 78).addBox(-1.5F, -45.0F, 6.0F, 3.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 24.0F, -0.46F));

		return LayerDefinition.create(meshdefinition, 128, 128);
	}

    @Override
    public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
    	//animation.set(1,new ModelAnimation(head, 0, -20, 20, 0, true, Angle.Z, null));
        if(!Minecraft.getInstance().isPaused()) {
        	if(EntityHelper.getState(entity) == 1) {
        		this.right_arm.zRot = (float) Math.toRadians(90);
        		this.left_arm.zRot = (float) Math.toRadians(-90);
        		entity.yBodyRot = (entity.tickCount/10)%360;
        	} else {
        		/*this.right_arm.x = (float) Math.toRadians(90);
        		this.left_arm.x = (float) Math.toRadians(90);*/

	        	if(entity.distanceToSqr(entity.xOld, entity.yOld, entity.zOld) > 0) {
	        		for(int i = 0; i < animation.size(); i++) { //iterate through the legs array
	                    ModelAnimation m = animation.get(i);
	                    m.animate();
	                }
	        	} else {
	        		for(int i = 0; i < animation.size(); i++) { //iterate through the legs array
	                    ModelAnimation m = animation.get(i);
	                    m.setDefault();
	                }
	        		this.right_arm.z = 0;
	        		this.left_arm.z = 0;
	        	}
	        }
        }	
    }

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer buffer, int packedLight, int packedOverlay, float pRed, float pGreen, float pBlue, float pAlpha) {
		this.head.render(poseStack, buffer, packedLight, packedOverlay);
		this.body.render(poseStack, buffer, packedLight, packedOverlay);
		this.right_arm.render(poseStack, buffer, packedLight, packedOverlay);
		this.right_leg.render(poseStack, buffer, packedLight, packedOverlay);
		this.left_arm.render(poseStack, buffer, packedLight, packedOverlay);
		this.left_leg.render(poseStack, buffer, packedLight, packedOverlay);
	}
}
