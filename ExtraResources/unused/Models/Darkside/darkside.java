// Made with Blockbench 4.6.4
// Exported for Minecraft version 1.17 or later with Mojang mappings
// Paste this class into your mod and generate all required imports


public class darkside<T extends Entity> extends EntityModel<T> {
	// This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation("modid", "darkside"), "main");
	private final ModelPart Head;
	private final ModelPart Body;
	private final ModelPart Leg1;
	private final ModelPart Leg2;
	private final ModelPart Arm1;
	private final ModelPart Arm2;
	private final ModelPart Wings;

	public darkside(ModelPart root) {
		this.Head = root.getChild("Head");
		this.Body = root.getChild("Body");
		this.Leg1 = root.getChild("Leg1");
		this.Leg2 = root.getChild("Leg2");
		this.Arm1 = root.getChild("Arm1");
		this.Arm2 = root.getChild("Arm2");
		this.Wings = root.getChild("Wings");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition Head = partdefinition.addOrReplaceChild("Head", CubeListBuilder.create().texOffs(16, 12).addBox(-1.5F, -2.0F, -1.0F, 3.0F, 4.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 4.0F, 0.0F, 0.2182F, 0.0F, 0.0F));

		PartDefinition Hair = Head.addOrReplaceChild("Hair", CubeListBuilder.create(), PartPose.offset(0.0F, 17.0F, 0.0F));

		PartDefinition Back4_r1 = Hair.addOrReplaceChild("Back4_r1", CubeListBuilder.create().texOffs(28, 19).addBox(-3.0F, -2.0F, 0.0F, 4.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.0F, -15.0F, 2.0F, -1.4835F, 0.0F, 0.0F));

		PartDefinition Back3_r1 = Hair.addOrReplaceChild("Back3_r1", CubeListBuilder.create().texOffs(30, 5).addBox(-3.0F, -2.0F, -1.0F, 4.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.0F, -15.0F, 3.0F, 0.5236F, 0.0F, 0.0F));

		PartDefinition Back2_r1 = Hair.addOrReplaceChild("Back2_r1", CubeListBuilder.create().texOffs(30, 22).addBox(-3.0F, -2.0F, -1.0F, 4.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.0F, -16.0F, 3.0F, 0.5236F, 0.0F, 0.0F));

		PartDefinition Back1_r1 = Hair.addOrReplaceChild("Back1_r1", CubeListBuilder.create().texOffs(24, 30).addBox(-3.0F, -2.0F, -1.0F, 4.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.0F, -17.0F, 3.0F, 0.5236F, 0.0F, 0.0F));

		PartDefinition BackSide6_r1 = Hair.addOrReplaceChild("BackSide6_r1", CubeListBuilder.create().texOffs(35, 0).addBox(0.0F, -2.0F, -1.0F, 1.0F, 1.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.0F, -15.0F, 0.0F, -0.3806F, 0.0992F, 0.2427F));

		PartDefinition BackSide5_r1 = Hair.addOrReplaceChild("BackSide5_r1", CubeListBuilder.create().texOffs(16, 7).addBox(-1.0F, -2.0F, -1.0F, 1.0F, 1.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-1.0F, -15.0F, 0.0F, -0.3806F, -0.0992F, -0.2427F));

		PartDefinition BackSide4_r1 = Hair.addOrReplaceChild("BackSide4_r1", CubeListBuilder.create().texOffs(29, 36).addBox(0.0F, -2.0F, -1.0F, 1.0F, 1.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.0F, -16.0F, 0.0F, -0.1687F, 0.045F, 0.258F));

		PartDefinition BackSide3_r1 = Hair.addOrReplaceChild("BackSide3_r1", CubeListBuilder.create().texOffs(16, 36).addBox(-1.0F, -2.0F, -1.0F, 1.0F, 1.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-1.0F, -16.0F, 0.0F, -0.1687F, -0.045F, -0.258F));

		PartDefinition BackSide2_r1 = Hair.addOrReplaceChild("BackSide2_r1", CubeListBuilder.create().texOffs(37, 16).addBox(-1.0F, -2.0F, -1.0F, 1.0F, 1.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-1.0F, -17.0F, 1.0F, 0.0F, 0.0F, -0.2618F));

		PartDefinition BackSide1_r1 = Hair.addOrReplaceChild("BackSide1_r1", CubeListBuilder.create().texOffs(37, 27).addBox(0.0F, -2.0F, -1.0F, 1.0F, 1.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.0F, -17.0F, 1.0F, 0.0F, 0.0F, 0.2618F));

		PartDefinition Top_r1 = Hair.addOrReplaceChild("Top_r1", CubeListBuilder.create().texOffs(32, 8).addBox(-2.0F, -2.0F, -1.0F, 4.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -19.0F, 2.0F, 1.4399F, 0.0F, 0.0F));

		PartDefinition cube_r1 = Hair.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(34, 40).addBox(0.0F, -2.0F, 0.0F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(3.0F, -15.0F, 0.0F, 0.0F, 0.0F, -1.1345F));

		PartDefinition cube_r2 = Hair.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(8, 4).addBox(-1.0F, -2.0F, -1.0F, 1.0F, 3.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(2.0F, -17.0F, 1.0F, 0.0F, 0.0F, -0.6545F));

		PartDefinition cube_r3 = Hair.addOrReplaceChild("cube_r3", CubeListBuilder.create().texOffs(40, 33).addBox(-1.0F, -2.0F, -1.0F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(4.0F, -17.0F, 0.0F, 0.0F, 0.0F, -1.2217F));

		PartDefinition cube_r4 = Hair.addOrReplaceChild("cube_r4", CubeListBuilder.create().texOffs(38, 40).addBox(-1.0F, -2.0F, -1.0F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(3.0F, -17.0F, 0.0F, 0.0F, 0.0F, -0.3054F));

		PartDefinition cube_r5 = Hair.addOrReplaceChild("cube_r5", CubeListBuilder.create().texOffs(0, 11).addBox(-1.0F, -2.0F, -1.0F, 1.0F, 3.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(2.0F, -18.0F, 0.0F, -1.4634F, -1.5199F, 0.5001F));

		PartDefinition cube_r6 = Hair.addOrReplaceChild("cube_r6", CubeListBuilder.create().texOffs(38, 31).addBox(-1.0F, -1.0F, -0.5F, 3.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -19.0F, -1.0F, 0.0F, -0.3054F, 0.0F));

		PartDefinition Face = Hair.addOrReplaceChild("Face", CubeListBuilder.create().texOffs(0, 15).addBox(-2.0F, -16.0F, -1.5F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition FaceCoverSideR3_r1 = Face.addOrReplaceChild("FaceCoverSideR3_r1", CubeListBuilder.create().texOffs(8, 41).addBox(0.5F, -2.0F, -0.5F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-4.0F, -16.0F, -1.0F, -0.1619F, 0.1891F, 0.9474F));

		PartDefinition FaceCoverSideR2_r1 = Face.addOrReplaceChild("FaceCoverSideR2_r1", CubeListBuilder.create().texOffs(19, 41).addBox(0.5F, -2.0F, -0.5F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-3.0F, -16.0F, -1.0F, -0.1745F, 0.0F, 0.1745F));

		PartDefinition FaceCoverSideR2_r2 = Face.addOrReplaceChild("FaceCoverSideR2_r2", CubeListBuilder.create().texOffs(8, 12).addBox(0.75F, -2.0F, -0.5F, 1.0F, 3.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-3.0F, -19.0F, -1.0F, 0.5327F, -0.5845F, 0.5584F));

		PartDefinition FaceCoverSideR1_r1 = Face.addOrReplaceChild("FaceCoverSideR1_r1", CubeListBuilder.create().texOffs(41, 10).addBox(0.75F, -2.0F, -0.25F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-3.0F, -17.0F, -1.0F, -0.0446F, -0.0151F, 0.0861F));

		PartDefinition FacecoverSideL_r1 = Face.addOrReplaceChild("FacecoverSideL_r1", CubeListBuilder.create().texOffs(0, 4).addBox(1.0F, -19.0F, -1.0F, 1.0F, 4.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.2182F, 0.0F));

		PartDefinition FacecoverLower2_r1 = Face.addOrReplaceChild("FacecoverLower2_r1", CubeListBuilder.create().texOffs(8, 8).addBox(-2.5F, -15.829F, -1.5F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.0F, 0.0F, 0.0F, -0.0019F, -0.0436F, 0.0873F));

		PartDefinition Body = partdefinition.addOrReplaceChild("Body", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));

		PartDefinition BaseBody = Body.addOrReplaceChild("BaseBody", CubeListBuilder.create().texOffs(16, 4).addBox(-2.5F, 7.0F, -1.0F, 5.0F, 1.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(0, 0).addBox(-4.0F, -1.0F, -1.0F, 8.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -17.0F, 0.0F));

		PartDefinition Groin_r1 = BaseBody.addOrReplaceChild("Groin_r1", CubeListBuilder.create().texOffs(0, 0).addBox(-1.0F, -2.0F, -1.0F, 2.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 9.0F, -1.0F, -0.7854F, 0.0F, 0.0F));

		PartDefinition Left4_r1 = BaseBody.addOrReplaceChild("Left4_r1", CubeListBuilder.create().texOffs(39, 20).addBox(0.0F, 0.0F, -1.0F, 2.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 7.0F, 0.0F, 0.0F, 0.0F, -0.7854F));

		PartDefinition Right4_r1 = BaseBody.addOrReplaceChild("Right4_r1", CubeListBuilder.create().texOffs(40, 0).addBox(-2.0F, 0.0F, -1.0F, 2.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 7.0F, 0.0F, 0.0F, 0.0F, 0.7854F));

		PartDefinition Left3_r1 = BaseBody.addOrReplaceChild("Left3_r1", CubeListBuilder.create().texOffs(40, 4).addBox(-1.0F, -1.0F, -1.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.6357F, 6.0016F, 0.0F, 0.0F, 0.0F, 0.3054F));

		PartDefinition Right3_r1 = BaseBody.addOrReplaceChild("Right3_r1", CubeListBuilder.create().texOffs(0, 40).addBox(-1.0F, -1.068F, -1.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-1.621F, 6.068F, 0.0F, -0.0017F, 0.0F, -0.3054F));

		PartDefinition Right2_r1 = BaseBody.addOrReplaceChild("Right2_r1", CubeListBuilder.create().texOffs(8, 36).addBox(-1.0F, -14.0F, -1.0F, 2.0F, 3.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 16.0F, 0.0F, 0.0F, 0.0F, -0.1745F));

		PartDefinition Left2_r1 = BaseBody.addOrReplaceChild("Left2_r1", CubeListBuilder.create().texOffs(0, 35).addBox(-1.0F, -14.0F, -1.0F, 2.0F, 3.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 16.0F, 0.0F, 0.0F, 0.0F, 0.1745F));

		PartDefinition Right_r1 = BaseBody.addOrReplaceChild("Right_r1", CubeListBuilder.create().texOffs(8, 30).addBox(-1.0F, -2.0F, -1.0F, 2.0F, 4.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-2.5578F, 2.2332F, 0.0F, 0.0F, 0.0F, -0.2182F));

		PartDefinition Left_r1 = BaseBody.addOrReplaceChild("Left_r1", CubeListBuilder.create().texOffs(16, 30).addBox(-1.0F, -2.0F, -1.0F, 2.0F, 4.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(2.5578F, 2.2332F, 0.0F, 0.0F, 0.0F, 0.2182F));

		PartDefinition Center_r1 = BaseBody.addOrReplaceChild("Center_r1", CubeListBuilder.create().texOffs(37, 36).addBox(-1.5F, -1.5F, -1.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 2.0F, 0.0F, 0.0F, 0.0F, 0.7854F));

		PartDefinition LowerShoulder_L_r1 = BaseBody.addOrReplaceChild("LowerShoulder_L_r1", CubeListBuilder.create().texOffs(22, 38).addBox(0.0F, -2.0F, -1.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.1204F, 1.5469F, 0.0F, 0.0F, 0.0F, 0.829F));

		PartDefinition LowerShoulder_R_r1 = BaseBody.addOrReplaceChild("LowerShoulder_R_r1", CubeListBuilder.create().texOffs(38, 23).addBox(-2.0F, -2.0F, -1.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.1204F, 1.5469F, 0.0F, 0.0F, 0.0F, -0.829F));

		PartDefinition Muscle = Body.addOrReplaceChild("Muscle", CubeListBuilder.create().texOffs(0, 0).addBox(-3.0F, -18.0F, -1.5F, 6.0F, 3.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition Leg1 = partdefinition.addOrReplaceChild("Leg1", CubeListBuilder.create().texOffs(28, 1).addBox(1.25F, -1.0F, -3.0F, 2.0F, 1.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 24.0F, 0.0F));

		PartDefinition FootHeel_r1 = Leg1.addOrReplaceChild("FootHeel_r1", CubeListBuilder.create().texOffs(0, 29).addBox(-0.75F, -3.0F, -1.0F, 2.0F, 4.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(2.0F, -1.0F, -0.2929F, -0.6109F, 0.0F, 0.0F));

		PartDefinition Shin_r1 = Leg1.addOrReplaceChild("Shin_r1", CubeListBuilder.create().texOffs(10, 19).addBox(-0.75F, -2.0F, -1.35F, 2.0F, 2.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(2.0F, -3.0F, 0.0F, -0.829F, 0.0F, 0.0F));

		PartDefinition Thigh2_r1 = Leg1.addOrReplaceChild("Thigh2_r1", CubeListBuilder.create().texOffs(16, 24).addBox(-0.75F, -4.0F, -1.0F, 2.0F, 4.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(2.0F, -4.0F, 0.0F, 0.0F, 0.0F, -0.0436F));

		PartDefinition Thigh1_r1 = Leg1.addOrReplaceChild("Thigh1_r1", CubeListBuilder.create().texOffs(34, 11).addBox(-1.0F, -2.5F, -1.0F, 2.0F, 3.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(2.0F, -7.5F, 0.0F, 0.0F, 0.0F, -0.2618F));

		PartDefinition Leg2 = partdefinition.addOrReplaceChild("Leg2", CubeListBuilder.create().texOffs(20, 0).addBox(-3.25F, -1.0F, -3.0F, 2.0F, 1.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 24.0F, 0.0F));

		PartDefinition FootHeel_r2 = Leg2.addOrReplaceChild("FootHeel_r2", CubeListBuilder.create().texOffs(24, 7).addBox(-1.25F, -3.0F, -1.0F, 2.0F, 4.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-2.0F, -1.0F, -0.2929F, -0.6109F, 0.0F, 0.0F));

		PartDefinition Shin_r2 = Leg2.addOrReplaceChild("Shin_r2", CubeListBuilder.create().texOffs(0, 18).addBox(-1.25F, -2.0F, -1.35F, 2.0F, 2.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-2.0F, -3.0F, 0.0F, -0.829F, 0.0F, 0.0F));

		PartDefinition Thigh3_r1 = Leg2.addOrReplaceChild("Thigh3_r1", CubeListBuilder.create().texOffs(8, 24).addBox(-1.25F, -4.0F, -1.0F, 2.0F, 4.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-2.0F, -4.0F, 0.0F, 0.0F, 0.0F, 0.0436F));

		PartDefinition Thigh2_r2 = Leg2.addOrReplaceChild("Thigh2_r2", CubeListBuilder.create().texOffs(32, 31).addBox(-1.0F, -2.5F, -1.0F, 2.0F, 3.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-2.0F, -7.5F, 0.0F, 0.0F, 0.0F, 0.2618F));

		PartDefinition Arm1 = partdefinition.addOrReplaceChild("Arm1", CubeListBuilder.create().texOffs(26, 13).addBox(8.0F, -16.0F, -1.0F, 2.0F, 4.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(0, 0).addBox(-2.0F, -16.0F, -1.5F, 2.0F, 4.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(-4.0F, 24.0F, 0.0F));

		PartDefinition Forearm_r1 = Arm1.addOrReplaceChild("Forearm_r1", CubeListBuilder.create().texOffs(24, 24).addBox(-1.0F, -5.1F, -1.85F, 2.0F, 4.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(9.0F, -7.0F, 0.0F, -0.1742F, 0.0151F, -0.0007F));

		PartDefinition Shoulder_Arm_r1 = Arm1.addOrReplaceChild("Shoulder_Arm_r1", CubeListBuilder.create().texOffs(24, 33).addBox(-1.0F, -1.0F, -1.0F, 2.0F, 3.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(8.0F, -17.0F, 0.0F, 0.0F, 0.0F, -0.5236F));

		PartDefinition Arm2 = partdefinition.addOrReplaceChild("Arm2", CubeListBuilder.create().texOffs(0, 23).addBox(-10.0F, -16.0F, -1.0F, 2.0F, 4.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(0, 0).addBox(0.0F, -16.0F, -1.5F, 2.0F, 4.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(4.0F, 24.0F, 0.0F));

		PartDefinition Forearm_r2 = Arm2.addOrReplaceChild("Forearm_r2", CubeListBuilder.create().texOffs(20, 18).addBox(-1.0F, -5.1F, -1.85F, 2.0F, 4.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-9.0F, -7.0F, 0.0F, -0.1742F, -0.0151F, 0.0007F));

		PartDefinition Shoulder_Arm_r2 = Arm2.addOrReplaceChild("Shoulder_Arm_r2", CubeListBuilder.create().texOffs(32, 25).addBox(-1.0F, -1.0F, -1.0F, 2.0F, 3.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-8.0F, -17.0F, 0.0F, 0.0F, 0.0F, 0.5236F));

		PartDefinition Wings = partdefinition.addOrReplaceChild("Wings", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));

		PartDefinition WingLeft = Wings.addOrReplaceChild("WingLeft", CubeListBuilder.create(), PartPose.offset(-1.0F, -15.0F, 0.0F));

		PartDefinition WingBit2_r1 = WingLeft.addOrReplaceChild("WingBit2_r1", CubeListBuilder.create().texOffs(15, 40).addBox(0.0F, -2.5F, 0.0F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(3.0F, -2.0F, 4.0F, 0.0F, 0.0F, 0.9163F));

		PartDefinition WingBit1_r1 = WingLeft.addOrReplaceChild("WingBit1_r1", CubeListBuilder.create().texOffs(30, 40).addBox(0.0F, -2.5F, 0.0F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(2.0F, -1.0F, 6.0F, 0.0F, 0.0F, 0.829F));

		PartDefinition WingSpine2_r1 = WingLeft.addOrReplaceChild("WingSpine2_r1", CubeListBuilder.create().texOffs(0, 11).addBox(0.0F, -2.25F, 2.25F, 1.0F, 1.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(7.0F, -3.0F, 1.0F, -0.2293F, -0.5445F, -0.4376F));

		PartDefinition WingSpine1_r1 = WingLeft.addOrReplaceChild("WingSpine1_r1", CubeListBuilder.create().texOffs(8, 12).addBox(0.0F, -2.0F, 2.0F, 1.0F, 1.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.7098F, 0.5791F, 0.545F));

		PartDefinition WingRight = Wings.addOrReplaceChild("WingRight", CubeListBuilder.create(), PartPose.offset(1.0F, -15.0F, 0.0F));

		PartDefinition WingBit3_r1 = WingRight.addOrReplaceChild("WingBit3_r1", CubeListBuilder.create().texOffs(27, 0).addBox(-1.0F, -2.5F, 0.0F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-3.0F, -2.0F, 4.0F, 0.0F, 0.0F, -0.9163F));

		PartDefinition WingBit2_r2 = WingRight.addOrReplaceChild("WingBit2_r2", CubeListBuilder.create().texOffs(34, 16).addBox(-1.0F, -2.5F, 0.0F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-2.0F, -1.0F, 6.0F, 0.0F, 0.0F, -0.829F));

		PartDefinition WingSpine3_r1 = WingRight.addOrReplaceChild("WingSpine3_r1", CubeListBuilder.create().texOffs(0, 4).addBox(-1.0F, -2.25F, 2.25F, 1.0F, 1.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-7.0F, -3.0F, 1.0F, -0.2293F, 0.5445F, 0.4376F));

		PartDefinition WingSpine2_r2 = WingRight.addOrReplaceChild("WingSpine2_r2", CubeListBuilder.create().texOffs(8, 5).addBox(-1.0F, -2.0F, 2.0F, 1.0F, 1.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.7098F, -0.5791F, -0.545F));

		return LayerDefinition.create(meshdefinition, 64, 64);
	}

	@Override
	public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {

	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		Head.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		Body.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		Leg1.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		Leg2.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		Arm1.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		Arm2.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		Wings.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}
}