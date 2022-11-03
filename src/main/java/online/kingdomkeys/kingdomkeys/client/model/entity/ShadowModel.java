package online.kingdomkeys.kingdomkeys.client.model.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import online.kingdomkeys.kingdomkeys.KingdomKeys;

/***
 * Shadow - Wynd
 * Ported to 1.18 using Blockbench - Wehavecookies56
 */
public class ShadowModel<Type extends Entity> extends EntityModel<Type> {
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation(KingdomKeys.MODID, "shadow"), "main");
	private final ModelPart UpperBody;
	private final ModelPart LowerBody;
	private final ModelPart LowerHead;
	private final ModelPart Head;
	private final ModelPart LegLeft1;
	private final ModelPart LegLeft3;
	private final ModelPart LegLeft2;
	private final ModelPart LegRight1;
	private final ModelPart LegRight3;
	private final ModelPart LegRight2;
	private final ModelPart ArmLeft1;
	private final ModelPart ArmLeft2;
	private final ModelPart ArmRight1;
	private final ModelPart ArmRight2;
	private final ModelPart AntenaLeft1;
	private final ModelPart AntenaLeft2;
	private final ModelPart AntenaRight1;
	private final ModelPart AntenaRight2;

	// Default variables for every model
	protected double distanceMovedTotal = 0.0D;
	public double CYCLES_PER_BLOCK = 2;
	protected int cycleIndex = 0;
	private int[][] ticksForWalkingAnimation = new int[][] { { 0, -90 }, { 100, 2 }, { 51, -51 } };

	// Walking animation
	protected double[][] animationWalk = new double[][] { { 0, 100, 51, -90, 2, -46 }, { -40, 51, 0, -40, 51, 0 }, { -90, 2, -46, 0, 100, 51 }, { -90, 2, -46, 0, 100, 51 }, { -40, 51, 0, -40, 51, 0 }, { 0, 100, -51, -90, 2, -46 }, };


	public ShadowModel(ModelPart root) {
		this.UpperBody = root.getChild("UpperBody");
		this.LowerBody = root.getChild("LowerBody");
		this.LowerHead = root.getChild("LowerHead");
		this.Head = root.getChild("Head");
		this.LegLeft1 = root.getChild("LegLeft1");
		this.LegLeft3 = root.getChild("LegLeft3");
		this.LegLeft2 = root.getChild("LegLeft2");
		this.LegRight1 = root.getChild("LegRight1");
		this.LegRight3 = root.getChild("LegRight3");
		this.LegRight2 = root.getChild("LegRight2");
		this.ArmLeft1 = root.getChild("ArmLeft1");
		this.ArmLeft2 = root.getChild("ArmLeft2");
		this.ArmRight1 = root.getChild("ArmRight1");
		this.ArmRight2 = root.getChild("ArmRight2");
		this.AntenaLeft1 = root.getChild("AntenaLeft1");
		this.AntenaLeft2 = root.getChild("AntenaLeft2");
		this.AntenaRight1 = root.getChild("AntenaRight1");
		this.AntenaRight2 = root.getChild("AntenaRight2");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition UpperBody = partdefinition.addOrReplaceChild("UpperBody", CubeListBuilder.create().texOffs(0, 0).mirror().addBox(-1.5F, -4.5F, -1.8F, 3.0F, 5.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.0F, 19.0F, 2.0F, 0.7064F, 0.0F, 0.0F));

		PartDefinition LowerBody = partdefinition.addOrReplaceChild("LowerBody", CubeListBuilder.create().texOffs(0, 0).mirror().addBox(-2.0F, -1.0F, -2.0F, 4.0F, 3.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(0.0F, 19.0F, 2.0F));

		PartDefinition LowerHead = partdefinition.addOrReplaceChild("LowerHead", CubeListBuilder.create().texOffs(0, 0).mirror().addBox(-1.5F, -5.0667F, -4.4F, 3.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(0.0F, 19.0F, 2.0F));

		PartDefinition Head = partdefinition.addOrReplaceChild("Head", CubeListBuilder.create().texOffs(0, 10).mirror().addBox(-2.5F, -5.5F, -9.0F, 5.0F, 5.0F, 5.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(0.0F, 19.0F, 2.0F));

		PartDefinition LegLeft1 = partdefinition.addOrReplaceChild("LegLeft1", CubeListBuilder.create().texOffs(0, 0).mirror().addBox(2.0F, 0.0667F, 0.5F, 1.0F, 3.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.0F, 19.0F, 2.0F, -0.7064F, 0.0F, 0.0F));

		PartDefinition LegLeft3 = partdefinition.addOrReplaceChild("LegLeft3", CubeListBuilder.create().texOffs(0, 0).mirror().addBox(1.5F, 4.0F, -3.5F, 2.0F, 1.0F, 5.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(0.0F, 19.0F, 2.0F));

		PartDefinition LegLeft2 = partdefinition.addOrReplaceChild("LegLeft2", CubeListBuilder.create().texOffs(0, 0).mirror().addBox(2.0F, 1.5333F, -3.0667F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.0F, 19.0F, 2.0F, 0.8923F, 0.0F, 0.0F));

		PartDefinition LegRight1 = partdefinition.addOrReplaceChild("LegRight1", CubeListBuilder.create().texOffs(0, 0).mirror().addBox(-3.0F, 0.0667F, 0.5F, 1.0F, 3.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.0F, 19.0F, 2.0F, -0.7064F, 0.0F, 0.0F));

		PartDefinition LegRight3 = partdefinition.addOrReplaceChild("LegRight3", CubeListBuilder.create().texOffs(0, 0).mirror().addBox(-3.5F, 4.0F, -3.5F, 2.0F, 1.0F, 5.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(0.0F, 19.0F, 2.0F));

		PartDefinition LegRight2 = partdefinition.addOrReplaceChild("LegRight2", CubeListBuilder.create().texOffs(0, 0).mirror().addBox(-3.0F, 1.5333F, -3.0667F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.0F, 19.0F, 2.0F, 0.8923F, 0.0F, 0.0F));

		PartDefinition ArmLeft1 = partdefinition.addOrReplaceChild("ArmLeft1", CubeListBuilder.create().texOffs(0, 0).mirror().addBox(1.0F, -1.4667F, -2.2667F, 2.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.0F, 19.0F, 2.0F, -0.409F, 0.0F, 0.0F));

		PartDefinition ArmLeft2 = partdefinition.addOrReplaceChild("ArmLeft2", CubeListBuilder.create().texOffs(0, 0).mirror().addBox(2.0F, -1.4F, -4.3333F, 1.0F, 1.0F, 3.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.0F, 19.0F, 2.0F, 0.4461F, 0.0F, 0.0F));

		PartDefinition ArmRight1 = partdefinition.addOrReplaceChild("ArmRight1", CubeListBuilder.create().texOffs(0, 0).mirror().addBox(-3.0F, -1.4667F, -2.2667F, 2.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.0F, 19.0F, 2.0F, -0.409F, 0.0F, 0.0F));

		PartDefinition ArmRight2 = partdefinition.addOrReplaceChild("ArmRight2", CubeListBuilder.create().texOffs(0, 0).mirror().addBox(-3.0F, -1.4F, -4.3333F, 1.0F, 1.0F, 3.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.0F, 19.0F, 2.0F, 0.4461F, 0.0F, 0.0F));

		PartDefinition AntenaLeft1 = partdefinition.addOrReplaceChild("AntenaLeft1", CubeListBuilder.create().texOffs(0, 0).mirror().addBox(1.0F, -6.5F, -8.0F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(0.0F, 19.0F, 2.0F));

		PartDefinition AntenaLeft2 = partdefinition.addOrReplaceChild("AntenaLeft2", CubeListBuilder.create().texOffs(0, 0).mirror().addBox(1.0F, -7.5F, -10.0F, 1.0F, 1.0F, 3.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(0.0F, 19.0F, 2.0F));

		PartDefinition AntenaRight1 = partdefinition.addOrReplaceChild("AntenaRight1", CubeListBuilder.create().texOffs(0, 0).mirror().addBox(-2.0F, -6.5F, -8.0F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(0.0F, 19.0F, 2.0F));

		PartDefinition AntenaRight2 = partdefinition.addOrReplaceChild("AntenaRight2", CubeListBuilder.create().texOffs(0, 0).mirror().addBox(-2.0F, -7.5F, -10.0F, 1.0F, 1.0F, 3.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(0.0F, 19.0F, 2.0F));

		return LayerDefinition.create(meshdefinition, 32, 32);
	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		UpperBody.render(poseStack, buffer, packedLight, packedOverlay);
		LowerBody.render(poseStack, buffer, packedLight, packedOverlay);
		LowerHead.render(poseStack, buffer, packedLight, packedOverlay);
		Head.render(poseStack, buffer, packedLight, packedOverlay);
		LegLeft1.render(poseStack, buffer, packedLight, packedOverlay);
		LegLeft3.render(poseStack, buffer, packedLight, packedOverlay);
		LegLeft2.render(poseStack, buffer, packedLight, packedOverlay);
		LegRight1.render(poseStack, buffer, packedLight, packedOverlay);
		LegRight3.render(poseStack, buffer, packedLight, packedOverlay);
		LegRight2.render(poseStack, buffer, packedLight, packedOverlay);
		ArmLeft1.render(poseStack, buffer, packedLight, packedOverlay);
		ArmLeft2.render(poseStack, buffer, packedLight, packedOverlay);
		ArmRight1.render(poseStack, buffer, packedLight, packedOverlay);
		ArmRight2.render(poseStack, buffer, packedLight, packedOverlay);
		AntenaLeft1.render(poseStack, buffer, packedLight, packedOverlay);
		AntenaLeft2.render(poseStack, buffer, packedLight, packedOverlay);
		AntenaRight1.render(poseStack, buffer, packedLight, packedOverlay);
		AntenaRight2.render(poseStack, buffer, packedLight, packedOverlay);
	}

	@Override
	public void setupAnim(Type e, float p_102619_, float p_102620_, float p_102621_, float p_102622_, float p_102623_) {
		updateDistanceMovedTotal(e);
		cycleIndex = (int) ((getDistanceMovedTotal() * CYCLES_PER_BLOCK) % animationWalk.length);

		if (e.distanceToSqr(e.xo, e.yo, e.zo) > 0) {
			LegLeft1.xRot = degToRad(animationWalk[cycleIndex][0]);
			LegLeft2.xRot = degToRad(animationWalk[cycleIndex][1]);
			LegLeft3.xRot = degToRad(animationWalk[cycleIndex][2]);

			LegRight1.xRot = degToRad(animationWalk[cycleIndex][3]);
			LegRight2.xRot = degToRad(animationWalk[cycleIndex][4]);
			LegRight3.xRot = degToRad(animationWalk[cycleIndex][5]);
		} else {
			LegLeft1.xRot = LegRight1.xRot = -0.7063936F;
			LegLeft2.xRot = LegRight2.xRot = 0.8922867F;
			LegLeft3.xRot = LegRight3.xRot = 0;
		}

	}

	// Default methods/functions for every model
	protected void updateDistanceMovedTotal(Entity e) {
		distanceMovedTotal += e.distanceToSqr(e.xo, e.yo, e.zo);
	}

	protected double getDistanceMovedTotal() {
		return (distanceMovedTotal);
	}

	protected float degToRad(double degrees) {
		return (float) (degrees * (double) Math.PI / 180);
	}
}