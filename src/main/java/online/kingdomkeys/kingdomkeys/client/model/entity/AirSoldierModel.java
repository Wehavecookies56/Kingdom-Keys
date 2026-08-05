package online.kingdomkeys.kingdomkeys.client.model.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.util.Mth;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.entity.mob.BaseKHEntity;

@OnlyIn(Dist.CLIENT)
public class AirSoldierModel<Type extends BaseKHEntity> extends EntityModel<Type> {

	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(KingdomKeys.rl("air_soldier"), "main");

	private static final float HOVER_SPEED = 0.12F;
	private static final float HOVER_HEIGHT = 1.2F;
	private static final float WING_SPEED = 0.35F;
	private static final float WING_BEAT = 0.25F;
	private static final float LIMB_SWING_SCALE = 0.6F;

	private final ModelPart Head;
	private final ModelPart MainBody;
	private final ModelPart ArmLeft;
	private final ModelPart ArmRight;
	private final ModelPart LeftLeg;
	private final ModelPart RightLeg;
	private final ModelPart WingLeft;
	private final ModelPart WingRight;

	private float hover;

	public AirSoldierModel(ModelPart root) {
		this.Head = root.getChild("Head");
		this.MainBody = root.getChild("MainBody");
		this.ArmLeft = root.getChild("ArmLeft");
		this.ArmRight = root.getChild("ArmRight");
		this.LeftLeg = root.getChild("LeftLeg");
		this.RightLeg = root.getChild("RightLeg");
		this.WingLeft = root.getChild("WingLeft");
		this.WingRight = root.getChild("WingRight");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition Head = partdefinition.addOrReplaceChild("Head", CubeListBuilder.create()
				.texOffs(11, 15).addBox(-2.5F, -5F, -2.75F, 5F, 2F, 5F, new CubeDeformation(0.0F))
				.texOffs(40, 54).addBox(-2F, -5F, -2F, 4F, 4F, 4F, new CubeDeformation(0.0F))
				.texOffs(16, 11).addBox(-1F, -2F, -1F, 2F, 2F, 2F, new CubeDeformation(0.0F)), PartPose.offset(0F, -1F, 0F));
		Head.addOrReplaceChild("Hat1_r1", CubeListBuilder.create().texOffs(28, 25).addBox(-2F, -2F, -3F, 4F, 2F, 4F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0F, -4F, 1F, 0.2618F, 0F, 0F));
		Head.addOrReplaceChild("Hat2_r1", CubeListBuilder.create().texOffs(12, 41).addBox(-2F, -2F, -2F, 4F, 3F, 3F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0F, -5.8978F, 0.2235F, 0.5672F, 0F, 0F));
		Head.addOrReplaceChild("Hat3_r1", CubeListBuilder.create().texOffs(48, 0).addBox(-1F, -2F, -1F, 2F, 4F, 2F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0F, -7.8978F, -1.7765F, 0.8727F, 0F, 0F));
		Head.addOrReplaceChild("Hat4_r1", CubeListBuilder.create().texOffs(20, 47).addBox(-0.5F, -2F, -1F, 1F, 2F, 1F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0F, -9.8978F, -2.7765F, 1.0472F, 0F, 0F));
		PartDefinition MainBody = partdefinition.addOrReplaceChild("MainBody", CubeListBuilder.create()
				.texOffs(3, 0).addBox(-5F, -18F, -2F, 10F, 4F, 4F, new CubeDeformation(0.0F))
				.texOffs(0, 8).addBox(-2F, -14F, -2F, 4F, 8F, 4F, new CubeDeformation(0.0F)), PartPose.offset(0F, 17F, 0F));
		MainBody.addOrReplaceChild("MainBodyCore1_r1", CubeListBuilder.create().texOffs(0, 14).addBox(-2F, 0F, -2F, 4F, 2F, 4F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(2F, -14F, 0F, 0F, 0F, -0.7854F));
		MainBody.addOrReplaceChild("MainBodyCore2_r1", CubeListBuilder.create().texOffs(0, 14).addBox(-2F, 0F, -2F, 4F, 2F, 4F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-2F, -14F, 0F, 0F, 0F, 0.7854F));
		MainBody.addOrReplaceChild("MainBodyRight_r1", CubeListBuilder.create().texOffs(0, 35).addBox(-2F, -2F, -3F, 4F, 8F, 2F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-2F, -14F, 2F, 0F, 0F, -0.3491F));
		MainBody.addOrReplaceChild("MainBodyLeft_r1", CubeListBuilder.create().texOffs(32, 32).addBox(-2F, -2F, -1F, 4F, 8F, 2F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(2F, -14F, 0F, 0F, 0F, 0.3491F));
		PartDefinition ArmLeft = partdefinition.addOrReplaceChild("ArmLeft", CubeListBuilder.create()
				.texOffs(16, 29).addBox(-2F, 13F, -2F, 4F, 2F, 4F, new CubeDeformation(0.0F)), PartPose.offset(-7F, -4F, 0F));
		ArmLeft.addOrReplaceChild("Connector_r1", CubeListBuilder.create().texOffs(28, 52).addBox(0F, -5F, -1F, 2F, 4F, 2F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0F, 5F, 0F, 0F, 0F, 1.1781F));
		ArmLeft.addOrReplaceChild("Biscep_r1", CubeListBuilder.create().texOffs(20, 52).addBox(-0.5F, -4.5F, -1F, 2F, 6F, 2F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0F, 9F, 0F, 0F, 0F, 0.3054F));
		ArmLeft.addOrReplaceChild("Forearm_r1", CubeListBuilder.create().texOffs(0, 51).addBox(-1F, -3.5F, -1F, 2F, 6F, 2F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0F, 13F, 0F, 0F, 0F, 0F));
		ArmLeft.addOrReplaceChild("Hand_r1", CubeListBuilder.create().texOffs(12, 29).addBox(-0.8F, -1F, -1F, 2F, 2F, 2F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0F, 16F, 0F, 0F, 0F, -0.2618F));
		ArmLeft.addOrReplaceChild("Thumb_r1", CubeListBuilder.create().texOffs(44, 18).addBox(0F, -2F, -1.5F, 1F, 3F, 1F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0F, 16F, 2F, 0.896F, 0.2087F, -0.2211F));
		ArmLeft.addOrReplaceChild("Finger1_r1", CubeListBuilder.create().texOffs(0, 29).addBox(0F, -2F, -1.5F, 1F, 3F, 1F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-1F, 18F, 2F, 0.2608F, -0.0226F, 0.0843F));
		ArmLeft.addOrReplaceChild("Finger2_r1", CubeListBuilder.create().texOffs(26, 16).addBox(0F, -2.7F, -1.5F, 1F, 3F, 1F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-2F, 19F, 1F, -0.0359F, 0.02F, 0.4432F));
		ArmLeft.addOrReplaceChild("Finger3_r1", CubeListBuilder.create().texOffs(53, 41).addBox(0F, -4.7F, -1.75F, 1F, 5F, 1F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-2F, 19F, 0F, -0.2739F, 0.1307F, 0.4252F));
		PartDefinition ArmRight = partdefinition.addOrReplaceChild("ArmRight", CubeListBuilder.create()
				.texOffs(0, 29).addBox(-2F, 13F, -2F, 4F, 2F, 4F, new CubeDeformation(0.0F)), PartPose.offset(7F, -4F, 0F));
		ArmRight.addOrReplaceChild("Connector_r2", CubeListBuilder.create().texOffs(52, 27).addBox(-2F, -5F, -1F, 2F, 4F, 2F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0F, 5F, 0F, 0F, 0F, -1.1781F));
		ArmRight.addOrReplaceChild("Biscep_r2", CubeListBuilder.create().texOffs(12, 47).addBox(-1.5F, -4.5F, -1F, 2F, 6F, 2F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0F, 9F, 0F, 0F, 0F, -0.3054F));
		ArmRight.addOrReplaceChild("Forearm_r2", CubeListBuilder.create().texOffs(46, 46).addBox(-1F, -3.5F, -1F, 2F, 6F, 2F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0F, 13F, 0F, 0F, 0F, 0F));
		ArmRight.addOrReplaceChild("Hand_r2", CubeListBuilder.create().texOffs(12, 21).addBox(-1.2F, -1F, -1F, 2F, 2F, 2F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0F, 16F, 0F, 0F, 0F, 0.2618F));
		ArmRight.addOrReplaceChild("Thumb_r2", CubeListBuilder.create().texOffs(0, 20).addBox(-1F, -2F, -1.5F, 1F, 3F, 1F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0F, 16F, 2F, 0.896F, -0.2087F, 0.2211F));
		ArmRight.addOrReplaceChild("Finger2_r2", CubeListBuilder.create().texOffs(0, 8).addBox(-1F, -2F, -1.5F, 1F, 3F, 1F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1F, 18F, 2F, 0.2608F, 0.0226F, -0.0843F));
		ArmRight.addOrReplaceChild("Finger3_r2", CubeListBuilder.create().texOffs(0, 0).addBox(-1F, -2.7F, -1.5F, 1F, 3F, 1F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(2F, 19F, 1F, -0.0359F, -0.02F, -0.4432F));
		ArmRight.addOrReplaceChild("Finger4_r1", CubeListBuilder.create().texOffs(8, 51).addBox(-1F, -4.7F, -1.75F, 1F, 5F, 1F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(2F, 19F, 0F, -0.2739F, -0.1307F, -0.4252F));
		PartDefinition LeftLeg = partdefinition.addOrReplaceChild("LeftLeg", CubeListBuilder.create()
				.texOffs(34, 46).addBox(-2F, 7F, -1.5F, 3F, 3F, 3F, new CubeDeformation(0.0F))
				.texOffs(22, 46).addBox(-1.5F, 8F, 1F, 2F, 2F, 4F, new CubeDeformation(0.0F)), PartPose.offset(-3F, 9F, 0F));
		LeftLeg.addOrReplaceChild("Connector_r3", CubeListBuilder.create().texOffs(52, 52).addBox(-1F, -2F, -1F, 2F, 3F, 2F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1F, 2F, 0F, 0F, 0F, 0.7854F));
		LeftLeg.addOrReplaceChild("Thigh_r1", CubeListBuilder.create().texOffs(41, 39).addBox(-2F, -4F, -1.5F, 3F, 4F, 3F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0F, 6F, 0F, 0F, 0F, 0.1309F));
		LeftLeg.addOrReplaceChild("LowerThigh_r1", CubeListBuilder.create().texOffs(50, 35).addBox(-1.5F, -4F, 0F, 2F, 4F, 2F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0F, 8F, -1F, 0F, 0F, -0.0436F));
		PartDefinition RightLeg = partdefinition.addOrReplaceChild("RightLeg", CubeListBuilder.create()
				.texOffs(0, 45).addBox(-1F, 7F, -1.5F, 3F, 3F, 3F, new CubeDeformation(0.0F))
				.texOffs(44, 29).addBox(-0.5F, 8F, 1F, 2F, 2F, 4F, new CubeDeformation(0.0F)), PartPose.offset(3F, 9F, 0F));
		RightLeg.addOrReplaceChild("Connector_r4", CubeListBuilder.create().texOffs(36, 52).addBox(-1F, -2F, -1F, 2F, 3F, 2F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-1F, 2F, 0F, 0F, 0F, -0.7854F));
		RightLeg.addOrReplaceChild("Thigh_r2", CubeListBuilder.create().texOffs(41, 22).addBox(-1F, -4F, -1.5F, 3F, 4F, 3F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0F, 6F, 0F, 0F, 0F, -0.1309F));
		RightLeg.addOrReplaceChild("LowerThigh_r2", CubeListBuilder.create().texOffs(50, 18).addBox(-0.5F, -4F, 0F, 2F, 4F, 2F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0F, 8F, -1F, 0F, 0F, 0.0436F));
		PartDefinition WingLeft = partdefinition.addOrReplaceChild("WingLeft", CubeListBuilder.create(), PartPose.offset(0F, -2F, -2.7765F));
		WingLeft.addOrReplaceChild("WingBase1_r1", CubeListBuilder.create().texOffs(40, 16).addBox(-2F, -1F, 0F, 8F, 1F, 1F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(2F, 2.1022F, 0F, -0.6216F, 0.6121F, -0.6766F));
		WingLeft.addOrReplaceChild("WingBase2_r1", CubeListBuilder.create().texOffs(12, 39).addBox(-2F, -1F, 0F, 8F, 1F, 1F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(6F, 0.1022F, -4F, 0.5261F, 0.7211F, 0.8888F));
		WingLeft.addOrReplaceChild("WingBase3_r1", CubeListBuilder.create().texOffs(30, 46).addBox(0F, -2F, 0F, 1F, 2F, 1F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(5F, -0.8978F, -3F, 0.081F, -0.3496F, -0.4094F));
		WingLeft.addOrReplaceChild("WingBase4_r1", CubeListBuilder.create().texOffs(44, 35).addBox(0F, -1F, 0F, 1F, 2F, 1F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(5F, -1.8978F, -3F, 0.8824F, 0.2764F, -1.8739F));
		WingLeft.addOrReplaceChild("WingPart1_r1", CubeListBuilder.create().texOffs(28, 0).addBox(-4F, -2F, -1F, 9F, 3F, 0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(4F, 3F, -0.2235F, -0.6109F, 0.6109F, -0.6632F));
		WingLeft.addOrReplaceChild("WingPart2_r1", CubeListBuilder.create().texOffs(26, 42).addBox(-3F, -2F, -1F, 7F, 4F, 0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(7F, 2F, -3.2235F, 0.5236F, 0.7156F, 0.8727F));
		WingLeft.addOrReplaceChild("WingPart3_r1", CubeListBuilder.create().texOffs(38, 7).addBox(-3F, -2F, 0F, 8F, 4F, 0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(4.7667F, 3.308F, -2.221F, 0.0204F, 0.9042F, 0.0806F));
		PartDefinition WingRight = partdefinition.addOrReplaceChild("WingRight", CubeListBuilder.create(), PartPose.offset(0F, -2F, -2.7765F));
		WingRight.addOrReplaceChild("WingBase2_r2", CubeListBuilder.create().texOffs(12, 37).addBox(-6F, -1F, 0F, 8F, 1F, 1F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-2F, 2.1022F, 0F, -0.6216F, -0.6121F, 0.6766F));
		WingRight.addOrReplaceChild("WingBase3_r2", CubeListBuilder.create().texOffs(10, 35).addBox(-6F, -1F, 0F, 8F, 1F, 1F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-6F, 0.1022F, -4F, 0.5261F, -0.7211F, -0.8888F));
		WingRight.addOrReplaceChild("WingBase4_r2", CubeListBuilder.create().texOffs(44, 29).addBox(-1F, -2F, 0F, 1F, 2F, 1F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-5F, -0.8978F, -3F, 0.081F, 0.3496F, 0.4094F));
		WingRight.addOrReplaceChild("WingBase5_r1", CubeListBuilder.create().texOffs(40, 18).addBox(-1F, -1F, 0F, 1F, 2F, 1F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-5F, -1.8978F, -3F, 0.8824F, -0.2764F, 1.8739F));
		WingRight.addOrReplaceChild("WingPart2_r2", CubeListBuilder.create().texOffs(12, 8).addBox(-5F, -2F, -1F, 9F, 3F, 0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-4F, 3F, -0.2235F, -0.6109F, -0.6109F, 0.6632F));
		WingRight.addOrReplaceChild("WingPart3_r2", CubeListBuilder.create().texOffs(42, 11).addBox(-4F, -2F, -1F, 7F, 4F, 0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-7F, 2F, -3.2235F, 0.5236F, -0.7156F, -0.8727F));
		WingRight.addOrReplaceChild("WingPart4_r1", CubeListBuilder.create().texOffs(32, 3).addBox(-5F, -2F, 0F, 8F, 4F, 0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-4.7667F, 3.308F, -2.221F, 0.0204F, -0.9042F, -0.0806F));

		return LayerDefinition.create(meshdefinition, 64, 64);
	}

	@Override
	public void setupAnim(Type entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		hover = Mth.sin(ageInTicks * HOVER_SPEED) * HOVER_HEIGHT;
		float swing = Mth.cos(limbSwing * LIMB_SWING_SCALE) * limbSwingAmount;

		Head.yRot = netHeadYaw * ((float) Math.PI / 180F);
		Head.xRot = headPitch * ((float) Math.PI / 180F);

		// Arms sweep outwards as it moves, legs trail behind it.
		ArmLeft.zRot = -swing * 0.3F;
		ArmRight.zRot = swing * 0.3F;
		LeftLeg.xRot = swing * 0.5F;
		RightLeg.xRot = -swing * 0.5F;

		float beat = Mth.sin(ageInTicks * WING_SPEED) * WING_BEAT;
		WingLeft.zRot = beat;
		WingRight.zRot = -beat;
	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer buffer, int packedLight, int packedOverlay, int colour) {
		poseStack.pushPose();
		{
			poseStack.mulPose(Axis.YP.rotationDegrees(180));
			poseStack.translate(0, -hover / 16F, 0);

			Head.render(poseStack, buffer, packedLight, packedOverlay);
			MainBody.render(poseStack, buffer, packedLight, packedOverlay);
			ArmLeft.render(poseStack, buffer, packedLight, packedOverlay);
			ArmRight.render(poseStack, buffer, packedLight, packedOverlay);
			LeftLeg.render(poseStack, buffer, packedLight, packedOverlay);
			RightLeg.render(poseStack, buffer, packedLight, packedOverlay);
			WingLeft.render(poseStack, buffer, packedLight, packedOverlay);
			WingRight.render(poseStack, buffer, packedLight, packedOverlay);
		}
		poseStack.popPose();
	}
}
