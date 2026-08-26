package online.kingdomkeys.kingdomkeys.client.model.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
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
public class DefenderModel<Type extends BaseKHEntity> extends EntityModel<Type> {

	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(KingdomKeys.rl("defender"), "main");

	private static final float LIMB_SWING_SCALE = 0.6F;
	private static final float LIMB_SWING_AMOUNT = 1.0F;
	// The shield hangs off its arm rather than being welded on, so it lags behind the walk.
	private static final float SHIELD_SWAY = 0.09F;
	private static final float SHIELD_IDLE_SPEED = 0.06F;
	// The shield arm is held out in front, gripping it. Worked out from the two pivots: the grip sits
	// ten units below the shoulder and just under ten in front of it, which is about 44 degrees.
	private static final float SHIELD_ARM_PITCH = -0.78F;

	private final ModelPart Shield;
	private final ModelPart Main;
	private final ModelPart Head;
	private final ModelPart LeftLeg;
	private final ModelPart RightLeg;
	private final ModelPart LeftArm;
	private final ModelPart RightArm;

	public DefenderModel(ModelPart root) {
		this.Shield = root.getChild("Shield");
		this.Main = root.getChild("Main");
		this.Head = Main.getChild("Head");

		ModelPart legs = Main.getChild("Legs");
		this.LeftLeg = legs.getChild("LeftLeg");
		this.RightLeg = legs.getChild("RightLeg");

		ModelPart arms = Main.getChild("Arms");
		this.LeftArm = arms.getChild("LeftArm");
		this.RightArm = arms.getChild("RightArm");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition Shield = partdefinition.addOrReplaceChild("Shield", CubeListBuilder.create()
				.texOffs(68, 34).addBox(-2.5F, -2F, -12F, 4F, 5F, 3F, new CubeDeformation(0.0F))
				.texOffs(102, 53).addBox(-0.5F, -3F, 0F, 1F, 6F, 1F, new CubeDeformation(0.0F)), PartPose.offset(7.5F, 14F, -7.3333F));
		Shield.addOrReplaceChild("Base_r1", CubeListBuilder.create().texOffs(84, 0).addBox(-7F, -7F, -1F, 14F, 14F, 1F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.5F, -1F, -0.6667F, 0F, 0F, 0.7854F));
		Shield.addOrReplaceChild("Base2_r1", CubeListBuilder.create().texOffs(88, 16).addBox(-6F, -6F, 0F, 12F, 12F, 1F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.5F, -1F, -2.1667F, 0F, 0F, 0.7854F));
		Shield.addOrReplaceChild("Base3_r1", CubeListBuilder.create().texOffs(92, 30).addBox(-5F, -5F, -0.5F, 10F, 10F, 1F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.5F, -1F, -2.1667F, 0F, 0F, 0.7854F));
		Shield.addOrReplaceChild("Base4_r1", CubeListBuilder.create().texOffs(94, 42).addBox(6F, -15F, -12.3333F, 8F, 8F, 2F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-15.5F, 0F, 8.3333F, 0F, 0F, 0.7854F));
		Shield.addOrReplaceChild("Head1_r1", CubeListBuilder.create().texOffs(62, 0).addBox(-4F, -4F, 0F, 10F, 10F, 1F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.5F, -2F, -5F, 0F, 0F, 0.7854F));
		Shield.addOrReplaceChild("Head2_r1", CubeListBuilder.create().texOffs(62, 12).addBox(-3F, -3F, -1F, 9F, 9F, 2F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.5F, -2F, -6F, 0F, 0F, 0.7854F));
		Shield.addOrReplaceChild("Head3_r1", CubeListBuilder.create().texOffs(66, 24).addBox(-2F, -2F, -1F, 7F, 7F, 2F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.5F, -2F, -8F, 0F, 0F, 0.7854F));
		Shield.addOrReplaceChild("EarL_r1", CubeListBuilder.create().texOffs(53, 1).addBox(-2F, -5F, 0F, 3F, 5F, 1F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(2.5F, -2F, -6F, 0F, 0F, 0.5236F));
		Shield.addOrReplaceChild("EarR_r1", CubeListBuilder.create().texOffs(53, 1).addBox(-1F, -5F, 0F, 3F, 5F, 1F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-3.5F, -2F, -6F, 0F, 0F, -0.5236F));
		Shield.addOrReplaceChild("HandleTop_r1", CubeListBuilder.create().texOffs(108, 53).addBox(0F, -2F, -1F, 1F, 1F, 2F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.5F, -2F, -1F, -0.6545F, 0F, 0F));
		Shield.addOrReplaceChild("HandleBottom_r1", CubeListBuilder.create().texOffs(108, 58).addBox(0F, -2F, -1F, 1F, 1F, 2F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.5F, 4F, 0F, 0.1745F, 0F, 0F));
		PartDefinition Main = partdefinition.addOrReplaceChild("Main", CubeListBuilder.create(), PartPose.offset(0F, 24F, 0F));
		PartDefinition Head = Main.addOrReplaceChild("Head", CubeListBuilder.create()
				.texOffs(0, 27).addBox(-2.5F, -5F, -2.5F, 5F, 5F, 5F, new CubeDeformation(0.0F)), PartPose.offset(-0.5F, -19F, 2.5F));
		Head.addOrReplaceChild("Antenna_r1", CubeListBuilder.create().texOffs(0, 16).addBox(-2F, -4F, -1F, 1F, 4F, 1F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.5F, -4F, 1.5F, -0.9163F, 0F, 0F));
		PartDefinition BaseBody = Main.addOrReplaceChild("BaseBody", CubeListBuilder.create()
				.texOffs(0, 0).addBox(-6F, -13F, 0F, 11F, 3F, 5F, new CubeDeformation(0.0F))
				.texOffs(0, 8).addBox(-5F, -10F, 0F, 9F, 3F, 5F, new CubeDeformation(0.0F))
				.texOffs(23, 11).addBox(-4F, -7F, 0F, 7F, 2F, 5F, new CubeDeformation(0.0F))
				.texOffs(27, 3).addBox(-3F, -5F, 0F, 5F, 3F, 5F, new CubeDeformation(0.0F)), PartPose.offset(0F, -6F, 0F));
		PartDefinition Legs = Main.addOrReplaceChild("Legs", CubeListBuilder.create(), PartPose.offset(0F, -6F, 0F));
		PartDefinition LeftLeg = Legs.addOrReplaceChild("LeftLeg", CubeListBuilder.create(), PartPose.offset(2F, -4F, 2.5F));
		PartDefinition LeftUpperLeg = LeftLeg.addOrReplaceChild("LeftUpperLeg", CubeListBuilder.create()
				.texOffs(24, 40).addBox(-1F, 0F, -1.5F, 2F, 6F, 3F, new CubeDeformation(0.0F)), PartPose.offset(0F, 0F, 0F));
		PartDefinition LeftLowerLeg = LeftLeg.addOrReplaceChild("LeftLowerLeg", CubeListBuilder.create()
				.texOffs(15, 16).addBox(-1F, 0F, -1F, 2F, 2F, 2F, new CubeDeformation(0.0F))
				.texOffs(32, 27).addBox(-1F, 2F, -3.5F, 2F, 2F, 5F, new CubeDeformation(0.0F)), PartPose.offset(0F, 6F, 0F));
		PartDefinition RightLeg = Legs.addOrReplaceChild("RightLeg", CubeListBuilder.create(), PartPose.offset(-3F, -4F, 2.5F));
		PartDefinition RightUpperLeg = RightLeg.addOrReplaceChild("RightUpperLeg", CubeListBuilder.create()
				.texOffs(24, 40).addBox(-1F, 0F, -1.5F, 2F, 6F, 3F, new CubeDeformation(0.0F)), PartPose.offset(0F, 0F, 0F));
		PartDefinition RightLowerLeg = RightLeg.addOrReplaceChild("RightLowerLeg", CubeListBuilder.create()
				.texOffs(15, 16).addBox(-1F, 0F, -1F, 2F, 2F, 2F, new CubeDeformation(0.0F))
				.texOffs(32, 27).addBox(-1F, 2F, -3.5F, 2F, 2F, 5F, new CubeDeformation(0.0F)), PartPose.offset(0F, 6F, 0F));
		PartDefinition Arms = Main.addOrReplaceChild("Arms", CubeListBuilder.create(), PartPose.offset(0F, 0F, 0F));
		PartDefinition LeftArm = Arms.addOrReplaceChild("LeftArm", CubeListBuilder.create()
				.texOffs(15, 20).addBox(-1.75F, 0F, -3.5F, 4F, 4F, 7F, new CubeDeformation(0.0F))
				.texOffs(12, 40).addBox(-1.75F, 4F, -1.5F, 3F, 5F, 3F, new CubeDeformation(0.0F))
				.texOffs(20, 31).addBox(-2.25F, 9F, -2F, 4F, 5F, 4F, new CubeDeformation(0.0F))
				.texOffs(41, 27).addBox(-1.75F, 14F, -1.5F, 3F, 2F, 3F, new CubeDeformation(0.0F)), PartPose.offset(5.75F, -20F, 2.5F));
		PartDefinition RightArm = Arms.addOrReplaceChild("RightArm", CubeListBuilder.create()
				.texOffs(0, 16).addBox(-2.25F, 0F, -3.5F, 4F, 4F, 7F, new CubeDeformation(0.0F))
				.texOffs(0, 37).addBox(-1.25F, 4F, -1.5F, 3F, 5F, 3F, new CubeDeformation(0.0F))
				.texOffs(20, 31).addBox(-1.75F, 9F, -2F, 4F, 5F, 4F, new CubeDeformation(0.0F))
				.texOffs(41, 27).addBox(-1.25F, 14F, -1.5F, 3F, 2F, 3F, new CubeDeformation(0.0F)), PartPose.offset(-6.75F, -20F, 2.5F));

		return LayerDefinition.create(meshdefinition, 114, 114);
	}

	@Override
	public void setupAnim(Type entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		float swing = Mth.cos(limbSwing * LIMB_SWING_SCALE) * LIMB_SWING_AMOUNT * limbSwingAmount;

		LeftLeg.xRot = swing * 0.7F;
		RightLeg.xRot = -swing * 0.7F;

		// The shield arm is held out gripping it, and only wobbles with the stride rather than swinging like the free one.
		LeftArm.xRot = SHIELD_ARM_PITCH + swing * 0.08F;
		RightArm.xRot = swing * 0.3F;

		Head.yRot = netHeadYaw * ((float) Math.PI / 180F);
		Head.xRot = headPitch * ((float) Math.PI / 180F);

		// Kept square to the body rather than to the head, so it stays in the hand that's holding it.
		Shield.zRot = swing * SHIELD_SWAY + Mth.sin(ageInTicks * SHIELD_IDLE_SPEED) * SHIELD_SWAY;
	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer buffer, int packedLight, int packedOverlay, int colour) {
		Main.render(poseStack, buffer, packedLight, packedOverlay);
		Shield.render(poseStack, buffer, packedLight, packedOverlay);
	}
}
