// Made with Blockbench 4.6.4
// Exported for Minecraft version 1.17 or later with Mojang mappings
// Paste this class into your mod and generate all required imports


public class wightknight<T extends Entity> extends EntityModel<T> {
	// This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation("modid", "wightknight"), "main");
	private final ModelPart Head;
	private final ModelPart Body;
	private final ModelPart LeftArm;
	private final ModelPart RightArm;
	private final ModelPart UpperLegL;
	private final ModelPart LowerLegL;
	private final ModelPart UpperLegR;
	private final ModelPart LowerLegR;

	public wightknight(ModelPart root) {
		this.Head = root.getChild("Head");
		this.Body = root.getChild("Body");
		this.LeftArm = root.getChild("LeftArm");
		this.RightArm = root.getChild("RightArm");
		this.UpperLegL = root.getChild("UpperLegL");
		this.LowerLegL = root.getChild("LowerLegL");
		this.UpperLegR = root.getChild("UpperLegR");
		this.LowerLegR = root.getChild("LowerLegR");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition Head = partdefinition.addOrReplaceChild("Head", CubeListBuilder.create().texOffs(0, 6).addBox(-1.5F, -16.0F, -2.0F, 3.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 24.0F, 0.0F));

		PartDefinition Antenna2_r1 = Head.addOrReplaceChild("Antenna2_r1", CubeListBuilder.create().texOffs(5, 12).addBox(-1.5F, -2.0F, -1.0F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.0F, -17.0F, 1.0F, -1.0908F, 0.0F, 0.0F));

		PartDefinition Antenna1_r1 = Head.addOrReplaceChild("Antenna1_r1", CubeListBuilder.create().texOffs(0, 20).addBox(-1.5F, -3.0F, -1.0F, 1.0F, 3.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.0F, -15.0F, 0.0F, -0.5236F, 0.0F, 0.0F));

		PartDefinition Body = partdefinition.addOrReplaceChild("Body", CubeListBuilder.create().texOffs(9, 3).addBox(-1.5F, -13.0F, -2.0F, 3.0F, 1.0F, 3.0F, new CubeDeformation(0.0F))
		.texOffs(8, 13).addBox(-1.0F, -12.0F, -1.0F, 2.0F, 1.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(22, 0).addBox(-0.5F, -12.0F, 0.0F, 1.0F, 3.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(14, 7).addBox(-1.0F, -9.0F, -1.0F, 2.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 24.0F, 0.0F));

		PartDefinition Chest2_r1 = Body.addOrReplaceChild("Chest2_r1", CubeListBuilder.create().texOffs(9, 0).addBox(-1.0F, -0.5F, -0.5F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -12.2624F, -1.1485F, -0.5672F, 0.0F, 0.0F));

		PartDefinition Ribs = Body.addOrReplaceChild("Ribs", CubeListBuilder.create().texOffs(0, 0).addBox(-1.5F, -12.0F, -1.9F, 3.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition LeftArm = partdefinition.addOrReplaceChild("LeftArm", CubeListBuilder.create(), PartPose.offset(0.0F, 22.0F, -1.0F));

		PartDefinition UpperArmL = LeftArm.addOrReplaceChild("UpperArmL", CubeListBuilder.create().texOffs(21, 14).addBox(1.5F, -13.0F, -1.0F, 1.0F, 3.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 2.0F, 1.0F));

		PartDefinition LowerArmL1 = LeftArm.addOrReplaceChild("LowerArmL1", CubeListBuilder.create().texOffs(18, 18).addBox(1.5F, -10.0F, -1.0F, 1.0F, 3.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 2.0F, 1.0F));

		PartDefinition LowerArmL2 = LeftArm.addOrReplaceChild("LowerArmL2", CubeListBuilder.create().texOffs(8, 21).addBox(1.5F, -10.0F, -1.0F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(0, 16).addBox(1.5F, -8.0F, -1.5F, 1.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 5.0F, 1.0F));

		PartDefinition RightArm = partdefinition.addOrReplaceChild("RightArm", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));

		PartDefinition UpperArmR = RightArm.addOrReplaceChild("UpperArmR", CubeListBuilder.create().texOffs(21, 4).addBox(-2.5F, -13.0F, -1.0F, 1.0F, 3.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition LowerArmR1 = RightArm.addOrReplaceChild("LowerArmR1", CubeListBuilder.create().texOffs(14, 18).addBox(-2.5F, -10.0F, -1.0F, 1.0F, 3.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition LowerArmR2 = RightArm.addOrReplaceChild("LowerArmR2", CubeListBuilder.create().texOffs(4, 21).addBox(-2.5F, -10.0F, -1.0F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(14, 14).addBox(-2.5F, -8.0F, -1.5F, 1.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 3.0F, 0.0F));

		PartDefinition UpperLegL = partdefinition.addOrReplaceChild("UpperLegL", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));

		PartDefinition cube_r1 = UpperLegL.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(18, 10).addBox(-0.5F, -1.5F, -0.5F, 1.0F, 4.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.0F, -7.0F, -1.0F, -0.4795F, -0.0033F, -0.005F));

		PartDefinition LowerLegL = partdefinition.addOrReplaceChild("LowerLegL", CubeListBuilder.create().texOffs(0, 12).addBox(0.5F, -1.0F, -4.0F, 1.0F, 1.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 24.0F, 0.0F));

		PartDefinition LowerLeg_r1 = LowerLegL.addOrReplaceChild("LowerLeg_r1", CubeListBuilder.create().texOffs(10, 16).addBox(0.5F, -3.0F, -1.0F, 1.0F, 4.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -2.0F, -1.0F, 0.1309F, 0.0F, 0.0F));

		PartDefinition UpperLegR = partdefinition.addOrReplaceChild("UpperLegR", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));

		PartDefinition cube_r2 = UpperLegR.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(18, 0).addBox(-0.5F, -1.5F, -0.5F, 1.0F, 4.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-1.0F, -7.0F, -1.0F, -0.4795F, 0.0033F, 0.005F));

		PartDefinition LowerLegR = partdefinition.addOrReplaceChild("LowerLegR", CubeListBuilder.create().texOffs(9, 9).addBox(-1.5F, -1.0F, -4.0F, 1.0F, 1.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 24.0F, 0.0F));

		PartDefinition LowerLeg_r2 = LowerLegR.addOrReplaceChild("LowerLeg_r2", CubeListBuilder.create().texOffs(6, 16).addBox(-1.5F, -3.0F, -1.0F, 1.0F, 4.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -2.0F, -1.0F, 0.1309F, 0.0F, 0.0F));

		return LayerDefinition.create(meshdefinition, 32, 32);
	}

	@Override
	public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {

	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		Head.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		Body.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		LeftArm.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		RightArm.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		UpperLegL.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		LowerLegL.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		UpperLegR.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		LowerLegR.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}
}