// Made with Blockbench 4.6.4
// Exported for Minecraft version 1.17 or later with Mojang mappings
// Paste this class into your mod and generate all required imports


public class airSoldier<T extends Entity> extends EntityModel<T> {
	// This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation("modid", "airsoldier"), "main");
	private final ModelPart Head;
	private final ModelPart MainBody;
	private final ModelPart ArmLeft;
	private final ModelPart ArmRight;
	private final ModelPart LeftLeg;
	private final ModelPart RightLeg;
	private final ModelPart WingLeft;

	public airSoldier(ModelPart root) {
		this.Head = root.getChild("Head");
		this.MainBody = root.getChild("MainBody");
		this.ArmLeft = root.getChild("ArmLeft");
		this.ArmRight = root.getChild("ArmRight");
		this.LeftLeg = root.getChild("LeftLeg");
		this.RightLeg = root.getChild("RightLeg");
		this.WingLeft = root.getChild("WingLeft");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition Head = partdefinition.addOrReplaceChild("Head", CubeListBuilder.create().texOffs(0, 0).addBox(-2.5F, -22.0F, -2.75F, 5.0F, 1.0F, 5.0F, new CubeDeformation(0.0F))
		.texOffs(0, 0).addBox(-2.0F, -23.0F, -2.0F, 4.0F, 4.0F, 4.0F, new CubeDeformation(0.0F))
		.texOffs(0, 0).addBox(-1.0F, -20.0F, -1.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 17.0F, 0.0F));

		PartDefinition Hat4_r1 = Head.addOrReplaceChild("Hat4_r1", CubeListBuilder.create().texOffs(0, 0).addBox(-0.5F, -2.0F, -1.0F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -27.8978F, -2.7765F, 1.0472F, 0.0F, 0.0F));

		PartDefinition Hat3_r1 = Head.addOrReplaceChild("Hat3_r1", CubeListBuilder.create().texOffs(0, 0).addBox(-1.0F, -2.0F, -1.0F, 2.0F, 4.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -25.8978F, -1.7765F, 0.8727F, 0.0F, 0.0F));

		PartDefinition Hat2_r1 = Head.addOrReplaceChild("Hat2_r1", CubeListBuilder.create().texOffs(0, 0).addBox(-2.0F, -2.0F, -2.0F, 4.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -23.8978F, 0.2235F, 0.5672F, 0.0F, 0.0F));

		PartDefinition Hat1_r1 = Head.addOrReplaceChild("Hat1_r1", CubeListBuilder.create().texOffs(0, 0).addBox(-2.0F, -2.0F, -3.0F, 4.0F, 3.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -22.0F, 1.0F, 0.2618F, 0.0F, 0.0F));

		PartDefinition MainBody = partdefinition.addOrReplaceChild("MainBody", CubeListBuilder.create().texOffs(0, 0).addBox(-6.0F, -18.0F, -2.0F, 12.0F, 4.0F, 4.0F, new CubeDeformation(0.0F))
		.texOffs(0, 0).addBox(-2.0F, -14.0F, -2.0F, 4.0F, 8.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 17.0F, 0.0F));

		PartDefinition MainBodyLeft_r1 = MainBody.addOrReplaceChild("MainBodyLeft_r1", CubeListBuilder.create().texOffs(0, 0).mirror().addBox(-2.0F, -2.0F, -1.0F, 4.0F, 8.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(2.0F, -14.0F, 0.0F, 0.0F, 0.0F, 0.3491F));

		PartDefinition MainBodyRight_r1 = MainBody.addOrReplaceChild("MainBodyRight_r1", CubeListBuilder.create().texOffs(0, 0).addBox(-2.0F, -2.0F, -3.0F, 4.0F, 8.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-2.0F, -14.0F, 2.0F, 0.0F, 0.0F, -0.3491F));

		PartDefinition MainBodyCore2_r1 = MainBody.addOrReplaceChild("MainBodyCore2_r1", CubeListBuilder.create().texOffs(0, 0).mirror().addBox(-2.0F, -2.0F, -2.0F, 4.0F, 4.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-2.0F, -14.0F, 0.0F, 0.0F, 0.0F, 0.7854F));

		PartDefinition MainBodyCore1_r1 = MainBody.addOrReplaceChild("MainBodyCore1_r1", CubeListBuilder.create().texOffs(0, 0).addBox(-2.0F, -2.0F, -2.0F, 4.0F, 4.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(2.0F, -14.0F, 0.0F, 0.0F, 0.0F, -0.7854F));

		PartDefinition MainBodyCore0_r1 = MainBody.addOrReplaceChild("MainBodyCore0_r1", CubeListBuilder.create().texOffs(0, 0).addBox(-2.0F, -2.0F, -2.0F, 4.0F, 4.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -12.0F, 0.0F, 0.0F, 0.0F, -0.7854F));

		PartDefinition ArmLeft = partdefinition.addOrReplaceChild("ArmLeft", CubeListBuilder.create().texOffs(0, 0).addBox(-10.0F, -4.0F, -2.0F, 4.0F, 2.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 13.0F, 0.0F));

		PartDefinition Finger3_r1 = ArmLeft.addOrReplaceChild("Finger3_r1", CubeListBuilder.create().texOffs(0, 0).addBox(0.0F, -4.7F, -1.75F, 1.0F, 5.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-10.0F, 2.0F, 0.0F, -0.2739F, 0.1307F, 0.4252F));

		PartDefinition Finger2_r1 = ArmLeft.addOrReplaceChild("Finger2_r1", CubeListBuilder.create().texOffs(0, 0).addBox(0.0F, -2.7F, -1.5F, 1.0F, 3.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-10.0F, 2.0F, 1.0F, -0.0359F, 0.02F, 0.4432F));

		PartDefinition Finger1_r1 = ArmLeft.addOrReplaceChild("Finger1_r1", CubeListBuilder.create().texOffs(0, 0).addBox(0.0F, -2.0F, -1.5F, 1.0F, 3.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-9.0F, 1.0F, 2.0F, 0.2608F, -0.0226F, 0.0843F));

		PartDefinition Thumb_r1 = ArmLeft.addOrReplaceChild("Thumb_r1", CubeListBuilder.create().texOffs(0, 0).addBox(0.0F, -2.0F, -1.5F, 1.0F, 3.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-8.0F, -1.0F, 2.0F, 0.896F, 0.2087F, -0.2211F));

		PartDefinition Hand_r1 = ArmLeft.addOrReplaceChild("Hand_r1", CubeListBuilder.create().texOffs(0, 0).addBox(-0.8F, -1.0F, -1.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-8.0F, -1.0F, 0.0F, 0.0F, 0.0F, -0.2618F));

		PartDefinition Forearm_r1 = ArmLeft.addOrReplaceChild("Forearm_r1", CubeListBuilder.create().texOffs(0, 0).addBox(-1.0F, -3.5F, -1.0F, 2.0F, 6.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-8.0F, -4.0F, 0.0F, 0.0F, 0.0F, 0.0F));

		PartDefinition Biscep_r1 = ArmLeft.addOrReplaceChild("Biscep_r1", CubeListBuilder.create().texOffs(0, 0).addBox(-0.5F, -4.5F, -1.0F, 2.0F, 6.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-8.0F, -8.0F, 0.0F, 0.0F, 0.0F, 0.3054F));

		PartDefinition Connector_r1 = ArmLeft.addOrReplaceChild("Connector_r1", CubeListBuilder.create().texOffs(0, 0).addBox(0.0F, -5.0F, -1.0F, 2.0F, 4.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-8.0F, -12.0F, 0.0F, 0.0F, 0.0F, 1.1781F));

		PartDefinition ArmRight = partdefinition.addOrReplaceChild("ArmRight", CubeListBuilder.create().texOffs(0, 0).mirror().addBox(6.0F, -4.0F, -2.0F, 4.0F, 2.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(0.0F, 13.0F, 0.0F));

		PartDefinition Finger4_r1 = ArmRight.addOrReplaceChild("Finger4_r1", CubeListBuilder.create().texOffs(0, 0).mirror().addBox(-1.0F, -4.7F, -1.75F, 1.0F, 5.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(10.0F, 2.0F, 0.0F, -0.2739F, -0.1307F, -0.4252F));

		PartDefinition Finger3_r2 = ArmRight.addOrReplaceChild("Finger3_r2", CubeListBuilder.create().texOffs(0, 0).mirror().addBox(-1.0F, -2.7F, -1.5F, 1.0F, 3.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(10.0F, 2.0F, 1.0F, -0.0359F, -0.02F, -0.4432F));

		PartDefinition Finger2_r2 = ArmRight.addOrReplaceChild("Finger2_r2", CubeListBuilder.create().texOffs(0, 0).mirror().addBox(-1.0F, -2.0F, -1.5F, 1.0F, 3.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(9.0F, 1.0F, 2.0F, 0.2608F, 0.0226F, -0.0843F));

		PartDefinition Thumb_r2 = ArmRight.addOrReplaceChild("Thumb_r2", CubeListBuilder.create().texOffs(0, 0).mirror().addBox(-1.0F, -2.0F, -1.5F, 1.0F, 3.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(8.0F, -1.0F, 2.0F, 0.896F, -0.2087F, 0.2211F));

		PartDefinition Hand_r2 = ArmRight.addOrReplaceChild("Hand_r2", CubeListBuilder.create().texOffs(0, 0).mirror().addBox(-1.2F, -1.0F, -1.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(8.0F, -1.0F, 0.0F, 0.0F, 0.0F, 0.2618F));

		PartDefinition Forearm_r2 = ArmRight.addOrReplaceChild("Forearm_r2", CubeListBuilder.create().texOffs(0, 0).mirror().addBox(-1.0F, -3.5F, -1.0F, 2.0F, 6.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(8.0F, -4.0F, 0.0F, 0.0F, 0.0F, 0.0F));

		PartDefinition Biscep_r2 = ArmRight.addOrReplaceChild("Biscep_r2", CubeListBuilder.create().texOffs(0, 0).mirror().addBox(-1.5F, -4.5F, -1.0F, 2.0F, 6.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(8.0F, -8.0F, 0.0F, 0.0F, 0.0F, -0.3054F));

		PartDefinition Connector_r2 = ArmRight.addOrReplaceChild("Connector_r2", CubeListBuilder.create().texOffs(0, 0).mirror().addBox(-2.0F, -5.0F, -1.0F, 2.0F, 4.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(8.0F, -12.0F, 0.0F, 0.0F, 0.0F, -1.1781F));

		PartDefinition LeftLeg = partdefinition.addOrReplaceChild("LeftLeg", CubeListBuilder.create().texOffs(0, 0).addBox(-5.0F, 5.0F, -1.5F, 3.0F, 3.0F, 3.0F, new CubeDeformation(0.0F))
		.texOffs(0, 0).addBox(-4.5F, 6.0F, 1.0F, 2.0F, 2.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 11.0F, 0.0F));

		PartDefinition LowerThigh_r1 = LeftLeg.addOrReplaceChild("LowerThigh_r1", CubeListBuilder.create().texOffs(0, 0).addBox(-1.5F, -4.0F, 0.0F, 2.0F, 4.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-3.0F, 6.0F, -1.0F, 0.0F, 0.0F, -0.0436F));

		PartDefinition Thigh_r1 = LeftLeg.addOrReplaceChild("Thigh_r1", CubeListBuilder.create().texOffs(0, 0).addBox(-2.0F, -4.0F, -1.5F, 3.0F, 4.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-3.0F, 4.0F, 0.0F, 0.0F, 0.0F, 0.1309F));

		PartDefinition Connector_r3 = LeftLeg.addOrReplaceChild("Connector_r3", CubeListBuilder.create().texOffs(0, 0).addBox(-1.0F, -2.0F, -1.0F, 2.0F, 3.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-2.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.7854F));

		PartDefinition RightLeg = partdefinition.addOrReplaceChild("RightLeg", CubeListBuilder.create().texOffs(0, 0).mirror().addBox(2.0F, 5.0F, -1.5F, 3.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)).mirror(false)
		.texOffs(0, 0).mirror().addBox(2.5F, 6.0F, 1.0F, 2.0F, 2.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(0.0F, 11.0F, 0.0F));

		PartDefinition LowerThigh_r2 = RightLeg.addOrReplaceChild("LowerThigh_r2", CubeListBuilder.create().texOffs(0, 0).mirror().addBox(-0.5F, -4.0F, 0.0F, 2.0F, 4.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(3.0F, 6.0F, -1.0F, 0.0F, 0.0F, 0.0436F));

		PartDefinition Thigh_r2 = RightLeg.addOrReplaceChild("Thigh_r2", CubeListBuilder.create().texOffs(0, 0).mirror().addBox(-1.0F, -4.0F, -1.5F, 3.0F, 4.0F, 3.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(3.0F, 4.0F, 0.0F, 0.0F, 0.0F, -0.1309F));

		PartDefinition Connector_r4 = RightLeg.addOrReplaceChild("Connector_r4", CubeListBuilder.create().texOffs(0, 0).mirror().addBox(-1.0F, -2.0F, -1.0F, 2.0F, 3.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(2.0F, 0.0F, 0.0F, 0.0F, 0.0F, -0.7854F));

		PartDefinition WingLeft = partdefinition.addOrReplaceChild("WingLeft", CubeListBuilder.create(), PartPose.offset(0.0F, -10.8978F, -2.7765F));

		return LayerDefinition.create(meshdefinition, 64, 64);
	}

	@Override
	public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {

	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		Head.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		MainBody.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		ArmLeft.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		ArmRight.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		LeftLeg.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		RightLeg.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		WingLeft.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}
}