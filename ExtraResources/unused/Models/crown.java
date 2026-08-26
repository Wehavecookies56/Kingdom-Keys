// Made with Blockbench 5.1.6
// Exported for Minecraft version 1.17 or later with Mojang mappings
// Paste this class into your mod and generate all required imports


public class crown<T extends Entity> extends EntityModel<T> {
	// This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation("modid", "crown"), "main");
	private final ModelPart crown;

	public crown(ModelPart root) {
		this.crown = root.getChild("crown");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition crown = partdefinition.addOrReplaceChild("crown", CubeListBuilder.create().texOffs(0, 0).addBox(-4.0F, -6.0F, -4.0F, 8.0F, 6.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(0, 7).addBox(-4.0F, -6.0F, 3.0F, 8.0F, 6.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(0, 14).addBox(-4.0F, -6.0F, -3.0F, 1.0F, 6.0F, 6.0F, new CubeDeformation(0.0F))
		.texOffs(14, 14).addBox(3.0F, -6.0F, -3.0F, 1.0F, 6.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 24.0F, 0.0F));

		return LayerDefinition.create(meshdefinition, 32, 32);
	}

	@Override
	public void setupAnim(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {

	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		crown.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}
}