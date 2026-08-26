// Made with Blockbench 5.1.6
// Exported for Minecraft version 1.17 or later with Mojang mappings
// Paste this class into your mod and generate all required imports


public class neoshadow<T extends Entity> extends EntityModel<T> {
	// This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation("modid", "neoshadow"), "main");
	private final ModelPart Head;
	private final ModelPart AntennaL;
	private final ModelPart AntennaR;
	private final ModelPart Body;
	private final ModelPart ArmR;
	private final ModelPart ArmL;
	private final ModelPart LegL;
	private final ModelPart LegR;

	public neoshadow(ModelPart root) {
		this.Head = root.getChild("Head");
		this.AntennaL = this.Head.getChild("AntennaL");
		this.AntennaR = this.Head.getChild("AntennaR");
		this.Body = root.getChild("Body");
		this.ArmR = root.getChild("ArmR");
		this.ArmL = root.getChild("ArmL");
		this.LegL = root.getChild("LegL");
		this.LegR = root.getChild("LegR");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition Head = partdefinition.addOrReplaceChild("Head", CubeListBuilder.create().texOffs(0, 0).addBox(-2.0F, -16.0F, -1.0F, 4.0F, 4.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 24.0F, 0.0F));

		PartDefinition AntennaL = Head.addOrReplaceChild("AntennaL", CubeListBuilder.create().texOffs(0, 8).addBox(-0.5F, 0.0F, -6.0F, 0.0F, 6.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(-0.5F, -16.0F, 0.5F));

		PartDefinition Antenna2_r1 = AntennaL.addOrReplaceChild("Antenna2_r1", CubeListBuilder.create().texOffs(0, 0).addBox(-1.0F, -2.0F, -0.5F, 1.0F, 3.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, -4.0F, -0.5236F, 0.0F, 0.0F));

		PartDefinition Antenna1_r1 = AntennaL.addOrReplaceChild("Antenna1_r1", CubeListBuilder.create().texOffs(0, 18).addBox(-1.0F, -4.0F, -0.5F, 1.0F, 5.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.9599F, 0.0F, 0.0F));

		PartDefinition AntennaR = Head.addOrReplaceChild("AntennaR", CubeListBuilder.create().texOffs(0, 8).addBox(0.5F, 0.0F, -6.0F, 0.0F, 6.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.5F, -16.0F, 0.5F));

		PartDefinition Antenna3_r1 = AntennaR.addOrReplaceChild("Antenna3_r1", CubeListBuilder.create().texOffs(0, 0).addBox(0.0F, -2.0F, -0.5F, 1.0F, 3.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, -4.0F, -0.5236F, 0.0F, 0.0F));

		PartDefinition Antenna2_r2 = AntennaR.addOrReplaceChild("Antenna2_r2", CubeListBuilder.create().texOffs(0, 18).addBox(0.0F, -4.0F, -0.5F, 1.0F, 5.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.9599F, 0.0F, 0.0F));

		PartDefinition Body = partdefinition.addOrReplaceChild("Body", CubeListBuilder.create().texOffs(0, 8).addBox(-3.0F, -3.8333F, -1.0F, 6.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(12, 0).addBox(-2.0F, -1.8333F, -1.0F, 4.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(16, 9).addBox(-1.0F, 0.1667F, -1.0F, 2.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 15.8333F, 1.0F));

		PartDefinition ArmR = partdefinition.addOrReplaceChild("ArmR", CubeListBuilder.create().texOffs(19, 22).addBox(3.0F, -3.5F, -0.5F, 1.0F, 3.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(18, 12).addBox(3.0F, -0.5F, -0.5F, 1.0F, 5.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 16.0F, 1.0F));

		PartDefinition ArmL = partdefinition.addOrReplaceChild("ArmL", CubeListBuilder.create().texOffs(19, 22).addBox(-4.0F, -3.5F, -0.5F, 1.0F, 3.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(0, 18).addBox(-4.0F, -0.5F, -0.5F, 1.0F, 5.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 16.0F, 1.0F));

		PartDefinition LegL = partdefinition.addOrReplaceChild("LegL", CubeListBuilder.create().texOffs(16, 19).addBox(-1.5F, 1.0F, -0.5F, 1.0F, 3.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(12, 19).addBox(-1.5F, 4.0F, -0.5F, 1.0F, 3.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(14, 5).addBox(-1.5F, 7.0F, -0.25F, 1.0F, 1.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 16.0F, 1.0F));

		PartDefinition LegR = partdefinition.addOrReplaceChild("LegR", CubeListBuilder.create().texOffs(16, 19).addBox(0.5F, 1.0F, -0.5F, 1.0F, 3.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(12, 19).addBox(0.5F, 4.0F, -0.5F, 1.0F, 3.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(14, 5).addBox(0.5F, 7.0F, -0.25F, 1.0F, 1.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 16.0F, 1.0F));

		return LayerDefinition.create(meshdefinition, 32, 32);
	}

	@Override
	public void setupAnim(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {

	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		Head.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		Body.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		ArmR.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		ArmL.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		LegL.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		LegR.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}
}