// Made with Blockbench 4.6.4
// Exported for Minecraft version 1.17 or later with Mojang mappings
// Paste this class into your mod and generate all required imports


public class screwdiver<T extends Entity> extends EntityModel<T> {
	// This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation("modid", "screwdiver"), "main");
	private final ModelPart Head;
	private final ModelPart Body;
	private final ModelPart LegLeft;
	private final ModelPart LegRight;
	private final ModelPart UpperArmLeft;
	private final ModelPart ForeArmLeft;
	private final ModelPart ArmRight1;
	private final ModelPart ForeArmRight;

	public screwdiver(ModelPart root) {
		this.Head = root.getChild("Head");
		this.Body = root.getChild("Body");
		this.LegLeft = root.getChild("LegLeft");
		this.LegRight = root.getChild("LegRight");
		this.UpperArmLeft = root.getChild("UpperArmLeft");
		this.ForeArmLeft = root.getChild("ForeArmLeft");
		this.ArmRight1 = root.getChild("ArmRight1");
		this.ForeArmRight = root.getChild("ForeArmRight");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition Head = partdefinition.addOrReplaceChild("Head", CubeListBuilder.create().texOffs(0, 0).addBox(-2.0F, -2.25F, -1.375F, 4.0F, 4.0F, 4.0F, new CubeDeformation(0.0F))
		.texOffs(16, 0).addBox(-1.5F, -2.25F, 2.625F, 3.0F, 3.0F, 3.0F, new CubeDeformation(0.0F))
		.texOffs(28, 0).addBox(-1.0F, -2.25F, 5.625F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(36, 0).addBox(-0.5F, -2.25F, 7.625F, 1.0F, 1.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(0, 24).addBox(-2.0F, -3.75F, 9.625F, 4.0F, 4.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -0.75F, -0.625F));

		PartDefinition RightFin_r1 = Head.addOrReplaceChild("RightFin_r1", CubeListBuilder.create().texOffs(48, 0).mirror().addBox(0.0F, -3.0F, -1.875F, 0.0F, 5.0F, 3.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-2.0F, -1.0F, 5.0F, 1.5708F, 0.0F, -2.0944F));

		PartDefinition LeftFin_r1 = Head.addOrReplaceChild("LeftFin_r1", CubeListBuilder.create().texOffs(48, 0).addBox(0.0F, -3.0F, -1.875F, 0.0F, 5.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(2.0F, -1.0F, 5.0F, 1.5708F, 0.0F, 2.0944F));

		PartDefinition TopFin_r1 = Head.addOrReplaceChild("TopFin_r1", CubeListBuilder.create().texOffs(42, 0).addBox(0.0F, -3.0F, -1.0F, 0.0F, 5.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -3.0F, 4.0F, 1.5708F, 0.0F, 0.0F));

		PartDefinition Body = partdefinition.addOrReplaceChild("Body", CubeListBuilder.create().texOffs(0, 8).addBox(-3.0F, -4.0F, -1.0F, 6.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(0, 12).addBox(-2.0F, -2.0F, -1.0F, 4.0F, 4.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(0, 18).addBox(-1.0F, 2.0F, -1.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 5.0F, 0.0F));

		PartDefinition LegLeft = partdefinition.addOrReplaceChild("LegLeft", CubeListBuilder.create().texOffs(56, 10).addBox(-1.0F, -0.6667F, -0.5F, 2.0F, 5.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(18, 11).addBox(-0.5F, 4.3333F, 0.0F, 1.0F, 3.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(55, 0).addBox(-1.0F, 7.3333F, -3.5F, 2.0F, 0.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offset(1.5F, 8.6667F, -0.5F));

		PartDefinition LegRight = partdefinition.addOrReplaceChild("LegRight", CubeListBuilder.create().texOffs(56, 10).addBox(-1.0F, -0.6667F, -0.5F, 2.0F, 5.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(18, 11).addBox(-0.5F, 4.3333F, 0.0F, 1.0F, 3.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(55, 0).addBox(-1.0F, 7.3333F, -3.5F, 2.0F, 0.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offset(-1.5F, 8.6667F, -0.5F));

		PartDefinition UpperArmLeft = partdefinition.addOrReplaceChild("UpperArmLeft", CubeListBuilder.create().texOffs(18, 15).addBox(-0.8333F, -4.3333F, -0.5F, 1.0F, 3.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(3.8333F, 5.3333F, 0.0F, 0.0F, 0.0F, -0.2182F));

		PartDefinition ForeArmLeft = partdefinition.addOrReplaceChild("ForeArmLeft", CubeListBuilder.create(), PartPose.offset(3.8333F, 5.3333F, 0.0F));

		PartDefinition Fin_r1 = ForeArmLeft.addOrReplaceChild("Fin_r1", CubeListBuilder.create().texOffs(0, 20).addBox(-0.0833F, -0.62F, 0.4154F, 0.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0833F, 1.2898F, 0.5019F, 1.4835F, 0.0F, -0.1745F));

		PartDefinition Hand_r1 = ForeArmLeft.addOrReplaceChild("Hand_r1", CubeListBuilder.create().texOffs(17, 24).addBox(-0.4167F, 0.3769F, -1.5019F, 1.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(16, 19).addBox(-0.9167F, -2.6231F, -1.5019F, 2.0F, 3.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0833F, 1.2898F, 0.5019F, 0.0F, 0.0F, -0.1745F));

		PartDefinition ArmRight1 = partdefinition.addOrReplaceChild("ArmRight1", CubeListBuilder.create().texOffs(18, 15).addBox(-0.1667F, -4.3333F, -0.5F, 1.0F, 3.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-3.8333F, 5.3333F, 0.0F, 0.0F, 0.0F, 0.2182F));

		PartDefinition ForeArmRight = partdefinition.addOrReplaceChild("ForeArmRight", CubeListBuilder.create(), PartPose.offset(-4.8333F, 5.3333F, 2.0F));

		PartDefinition Fin_r2 = ForeArmRight.addOrReplaceChild("Fin_r2", CubeListBuilder.create().texOffs(0, 20).addBox(0.0833F, -0.62F, 0.4154F, 0.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.9167F, 1.2898F, -1.4981F, 1.4835F, 0.0F, 0.1745F));

		PartDefinition Arm3_r1 = ForeArmRight.addOrReplaceChild("Arm3_r1", CubeListBuilder.create().texOffs(16, 19).addBox(-1.0833F, -2.6231F, -1.5019F, 2.0F, 3.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(17, 24).addBox(-0.5833F, 0.3769F, -1.5019F, 1.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.9167F, 1.2898F, -1.4981F, 0.0F, 0.0F, 0.1745F));

		PartDefinition Trident = ForeArmRight.addOrReplaceChild("Trident", CubeListBuilder.create().texOffs(6, 35).addBox(-0.5F, -0.5F, -12.3626F, 1.0F, 1.0F, 28.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.3333F, 3.1667F, -8.6374F, 0.0F, 0.0F, -0.8727F));

		PartDefinition SpikeLeft2_r1 = Trident.addOrReplaceChild("SpikeLeft2_r1", CubeListBuilder.create().texOffs(36, 59).addBox(0.0F, -1.0F, -2.0F, 1.0F, 1.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(2.5F, 0.5F, -9.3626F, 0.0F, 0.3491F, 0.0F));

		PartDefinition SpikeLeft1_r1 = Trident.addOrReplaceChild("SpikeLeft1_r1", CubeListBuilder.create().texOffs(52, 57).addBox(0.0F, -1.0F, -4.0F, 1.0F, 1.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.5F, 0.5F, -6.3626F, 0.0F, -0.7854F, 0.0F));

		PartDefinition SpikeRight2_r1 = Trident.addOrReplaceChild("SpikeRight2_r1", CubeListBuilder.create().texOffs(36, 59).addBox(-1.0F, -1.0F, -2.0F, 1.0F, 1.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-2.5F, 0.5F, -9.3626F, 0.0F, -0.3491F, 0.0F));

		PartDefinition SpikeRight1_r1 = Trident.addOrReplaceChild("SpikeRight1_r1", CubeListBuilder.create().texOffs(52, 57).addBox(-1.0F, -1.0F, -4.0F, 1.0F, 1.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.5F, 0.5F, -6.3626F, 0.0F, 0.7854F, 0.0F));

		PartDefinition End2_r1 = Trident.addOrReplaceChild("End2_r1", CubeListBuilder.create().texOffs(55, 54).addBox(-1.0F, -1.0F, -1.0F, 1.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.5F, 0.5F, 16.6374F, 0.0F, -0.8727F, 0.0F));

		PartDefinition End1_r1 = Trident.addOrReplaceChild("End1_r1", CubeListBuilder.create().texOffs(55, 54).addBox(0.0F, -1.0F, -1.0F, 1.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.5F, 0.5F, 16.6374F, 0.0F, 0.8727F, 0.0F));

		return LayerDefinition.create(meshdefinition, 64, 64);
	}

	@Override
	public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {

	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		Head.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		Body.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		LegLeft.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		LegRight.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		UpperArmLeft.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		ForeArmLeft.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		ArmRight1.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		ForeArmRight.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}
}