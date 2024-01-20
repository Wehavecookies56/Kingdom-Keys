package online.kingdomkeys.kingdomkeys.client.model.armor;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.decoration.ArmorStand;

public class UXArmorModel<T extends LivingEntity> extends ArmorBaseModel<T> {

    public static final ModelLayerLocation LAYER_LOCATION_TOP = new ModelLayerLocation(new ResourceLocation("minecraft:player"), "ux1");
    public static final ModelLayerLocation LAYER_LOCATION_BOTTOM = new ModelLayerLocation(new ResourceLocation("minecraft:player"), "ux2");

	public static final ModelLayerLocation SLIM_LAYER_LOCATION_TOP = new ModelLayerLocation(new ResourceLocation("minecraft:player_slim"), "ux1");
	public static final ModelLayerLocation SLIM_LAYER_LOCATION_BOTTOM = new ModelLayerLocation(new ResourceLocation("minecraft:player_slim"), "ux2");

	public final ModelPart head;
    public final ModelPart body;
    public final ModelPart leftArm;
    public final ModelPart rightArm;
    public final ModelPart leftLeg;
    public final ModelPart rightLeg;    
    
    public UXArmorModel(ModelPart root) {
        super(root);
		this.head = root.getChild("head");
		this.body = root.getChild("body");
		this.leftArm = root.getChild("left_arm");
		this.rightArm = root.getChild("right_arm");
		this.leftLeg = root.getChild("left_leg");
		this.rightLeg = root.getChild("right_leg");

    }

    public static LayerDefinition createBodyLayer(CubeDeformation size, boolean slim) {
        MeshDefinition meshdefinition = new MeshDefinition();//HumanoidModel.createMesh(size, 0);
        PartDefinition partdefinition = meshdefinition.getRoot();
		PartDefinition head;
		/*
		if (slim) {
			//head = partdefinition.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 0).addBox(-4.0F, -8.0F, -4.0F, 10.0F, 10.0F, 10.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));
		} else {
		}*/
		head = partdefinition.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 0).addBox(-4.0F, -8.0F, -4.0F, 0.0F, 0.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));
		PartDefinition hat = partdefinition.addOrReplaceChild("hat", CubeListBuilder.create().texOffs(32, 0).addBox(0, 0, 0, 0, 0, 0, new CubeDeformation(0)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition Helmet = head.addOrReplaceChild("Helmet", CubeListBuilder.create(), PartPose.offset(0.0F, -2.2504F, -4.6043F));

		PartDefinition Visor = Helmet.addOrReplaceChild("Visor", CubeListBuilder.create(), PartPose.offset(0.0F, 26.2504F, 4.6043F));

		PartDefinition Visor3_r1 = Visor.addOrReplaceChild("Visor3_r1", CubeListBuilder.create().texOffs(93, 17).addBox(-4.45F, -0.5F, -3.9F, 8.9F, 1.0F, 7.8F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.4596F, -27.5202F, -3.5442F, 0.922F, 0.5435F, 0.5985F));

		PartDefinition Visor2_r1 = Visor.addOrReplaceChild("Visor2_r1", CubeListBuilder.create().texOffs(92, 16).addBox(-4.5F, -1.5F, -4.3F, 8.9F, 5.0F, 8.8F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.1414F, -30.1196F, -5.0622F, 0.7805F, 0.6178F, 0.5208F));

		PartDefinition Visor1_r1 = Visor.addOrReplaceChild("Visor1_r1", CubeListBuilder.create().texOffs(92, -1).addBox(-4.25F, -5.0F, -4.95F, 9.0F, 3.0F, 9.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -27.0F, -3.8F, 0.2683F, 0.7672F, 0.1885F));

		PartDefinition VisorBorder = Helmet.addOrReplaceChild("VisorBorder", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition BorderTop3_r1 = VisorBorder.addOrReplaceChild("BorderTop3_r1", CubeListBuilder.create().texOffs(96, 39).addBox(-3.5F, -0.3F, -2.5F, 7.0F, 0.4F, 5.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -6.0452F, 7.033F, 0.0087F, 0.0F, 0.0F));

		PartDefinition BorderTop2_r1 = VisorBorder.addOrReplaceChild("BorderTop2_r1", CubeListBuilder.create().texOffs(96, 39).addBox(-5.05F, 0.4F, -1.0F, 10.1F, 0.4F, 5.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.25F, -7.079F, 2.1009F, -0.0873F, 0.0F, 0.0F));

		PartDefinition BorderTop1_r1 = VisorBorder.addOrReplaceChild("BorderTop1_r1", CubeListBuilder.create().texOffs(99, 42).addBox(-5.8F, 0.7F, -1.0F, 12.1F, 0.4F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -7.0F, 0.0F, 0.48F, 0.0F, 0.0F));

		PartDefinition LowerVisorBorderL2_r1 = VisorBorder.addOrReplaceChild("LowerVisorBorderL2_r1", CubeListBuilder.create().texOffs(121, 54).mirror().addBox(-1.0F, -1.9F, -0.35F, 1.8F, 4.9F, 1.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(2.6622F, 2.5754F, -1.0856F, -0.7134F, -0.6534F, 1.4285F));

		PartDefinition LowerVisorBorderL1_r1 = VisorBorder.addOrReplaceChild("LowerVisorBorderL1_r1", CubeListBuilder.create().texOffs(120, 53).mirror().addBox(-2.2F, -2.0F, 0.45F, 2.0F, 3.8F, 1.1F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(5.959F, 1.7504F, 0.833F, -0.5592F, -0.7837F, 1.1953F));

		PartDefinition LowerVisorBorderR2_r1 = VisorBorder.addOrReplaceChild("LowerVisorBorderR2_r1", CubeListBuilder.create().texOffs(121, 54).addBox(-0.8F, -1.9F, -0.35F, 1.8F, 4.9F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-2.6622F, 2.5754F, -1.0856F, -0.7134F, 0.6534F, -1.4285F));

		PartDefinition LowerVisorBorderR1_r1 = VisorBorder.addOrReplaceChild("LowerVisorBorderR1_r1", CubeListBuilder.create().texOffs(120, 53).addBox(0.2F, -2.0F, 0.45F, 2.0F, 3.8F, 1.1F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-5.959F, 1.7504F, 0.833F, -0.5592F, 0.7837F, -1.1953F));

		PartDefinition UpperVisorBorderL3_r1 = VisorBorder.addOrReplaceChild("UpperVisorBorderL3_r1", CubeListBuilder.create().texOffs(119, 52).mirror().addBox(-0.65F, -3.5F, -1.5F, 1.3F, 6.5F, 3.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(5.3713F, -3.2496F, 2.5916F, 0.0F, -0.0436F, 0.0F));

		PartDefinition UpperVisorBorderL2_r1 = VisorBorder.addOrReplaceChild("UpperVisorBorderL2_r1", CubeListBuilder.create().texOffs(119, 52).mirror().addBox(-0.9F, -3.1F, -1.85F, 1.3F, 6.1F, 3.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(6.1189F, -3.2496F, 1.4401F, 0.0F, -0.3927F, 0.0F));

		PartDefinition UpperVisorBorderL1_r1 = VisorBorder.addOrReplaceChild("UpperVisorBorderL1_r1", CubeListBuilder.create().texOffs(119, 52).mirror().addBox(-0.5F, -3.0F, -1.15F, 0.8F, 6.1F, 2.3F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(5.8361F, -3.2496F, 0.3087F, 0.0F, -0.7854F, 0.0F));

		PartDefinition UpperVisorBorderR3_r1 = VisorBorder.addOrReplaceChild("UpperVisorBorderR3_r1", CubeListBuilder.create().texOffs(119, 52).addBox(-0.65F, -3.5F, -1.5F, 1.3F, 6.5F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-5.3713F, -3.2496F, 2.5916F, 0.0F, 0.0436F, 0.0F));

		PartDefinition UpperVisorBorderR2_r1 = VisorBorder.addOrReplaceChild("UpperVisorBorderR2_r1", CubeListBuilder.create().texOffs(119, 52).addBox(-0.4F, -3.1F, -1.85F, 1.3F, 6.1F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-6.1189F, -3.2496F, 1.4401F, 0.0F, 0.3927F, 0.0F));

		PartDefinition UpperVisorBorderR1_r1 = VisorBorder.addOrReplaceChild("UpperVisorBorderR1_r1", CubeListBuilder.create().texOffs(119, 52).addBox(-0.3F, -3.0F, -1.15F, 0.8F, 6.1F, 2.3F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-5.8361F, -3.2496F, 0.3087F, 0.0F, 0.7854F, 0.0F));

		PartDefinition VisorLowerCenter_r1 = VisorBorder.addOrReplaceChild("VisorLowerCenter_r1", CubeListBuilder.create().texOffs(122, 67).addBox(-1.0F, -1.0F, -0.5F, 2.0F, 1.7F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 4.0F, -2.5F, 0.48F, 0.0F, 0.0F));

		PartDefinition HelmetAcents = Helmet.addOrReplaceChild("HelmetAcents", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition RightSide6_r1 = HelmetAcents.addOrReplaceChild("RightSide6_r1", CubeListBuilder.create().texOffs(73, 1).mirror().addBox(0.1F, -1.1F, 2.65F, 0.2F, 1.5F, 3.2F, new CubeDeformation(0.0F)).mirror(false)
				.texOffs(72, 0).mirror().addBox(0.0F, -1.3F, -1.85F, 0.3F, 1.7F, 4.5F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-4.5238F, -11.8103F, 9.478F, 1.4868F, 0.064F, 0.0431F));

		PartDefinition RightSide4_r1 = HelmetAcents.addOrReplaceChild("RightSide4_r1", CubeListBuilder.create().texOffs(72, 0).mirror().addBox(0.05F, 0.0F, 4.8F, 0.4F, 1.7F, 4.5F, new CubeDeformation(0.0F)).mirror(false)
				.texOffs(88, 0).mirror().addBox(-0.15F, -1.6F, 0.3F, 0.6F, 3.3F, 4.5F, new CubeDeformation(0.0F)).mirror(false)
				.texOffs(88, 0).mirror().addBox(-0.45F, -2.3F, 0.0F, 0.9F, 3.7F, 4.5F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-5.25F, -2.0F, 4.6F, 1.1808F, 0.074F, 0.0218F));

		PartDefinition RightSide_r1 = HelmetAcents.addOrReplaceChild("RightSide_r1", CubeListBuilder.create().texOffs(90, 1).mirror().addBox(-0.45F, -4.0F, -1.0F, 0.9F, 5.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-5.25F, -2.0F, 4.6F, 0.3927F, 0.1047F, 0.0F));

		PartDefinition LeftSide6_r1 = HelmetAcents.addOrReplaceChild("LeftSide6_r1", CubeListBuilder.create().texOffs(73, 1).addBox(-0.3F, -1.1F, 2.65F, 0.2F, 1.5F, 3.2F, new CubeDeformation(0.0F))
				.texOffs(72, 0).addBox(-0.3F, -1.3F, -1.85F, 0.3F, 1.7F, 4.5F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(4.5238F, -11.8103F, 9.478F, 1.4868F, -0.064F, -0.0431F));

		PartDefinition LeftSide4_r1 = HelmetAcents.addOrReplaceChild("LeftSide4_r1", CubeListBuilder.create().texOffs(72, 0).addBox(-0.45F, 0.0F, 4.8F, 0.4F, 1.7F, 4.5F, new CubeDeformation(0.0F))
				.texOffs(88, 0).addBox(-0.45F, -1.6F, 0.3F, 0.6F, 3.3F, 4.5F, new CubeDeformation(0.0F))
				.texOffs(88, 0).addBox(-0.45F, -2.3F, 0.0F, 0.9F, 3.7F, 4.5F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(5.25F, -2.0F, 4.6F, 1.1808F, -0.074F, -0.0218F));

		PartDefinition LeftSide_r1 = HelmetAcents.addOrReplaceChild("LeftSide_r1", CubeListBuilder.create().texOffs(90, 1).addBox(-0.45F, -4.0F, -1.0F, 0.9F, 5.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(5.25F, -2.0F, 4.6F, 0.3927F, -0.1047F, 0.0F));

		PartDefinition body = partdefinition.addOrReplaceChild("body", CubeListBuilder.create().texOffs(16, 16).addBox(-4.0F, 0.0F, -2.0F, 8.0F, 12.0F, 4.0F, new CubeDeformation(0.5F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition Armor = body.addOrReplaceChild("Armor", CubeListBuilder.create().texOffs(110, 88).addBox(-3.4333F, -2.3705F, -1.0309F, 7.0F, 3.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0333F, 3.0705F, -2.0691F));

		PartDefinition BreastplateL2_r1 = Armor.addOrReplaceChild("BreastplateL2_r1", CubeListBuilder.create().texOffs(100, 55).mirror().addBox(-0.6F, -2.0F, 0.7F, 1.6F, 2.0F, 0.3F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(2.8333F, 0.9F, -1.4F, 0.0F, 0.0F, 0.3927F));

		PartDefinition BreastplateL1_r1 = Armor.addOrReplaceChild("BreastplateL1_r1", CubeListBuilder.create().texOffs(100, 52).mirror().addBox(-1.0F, -2.0F, 0.7F, 2.0F, 2.0F, 0.3F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(2.4333F, 0.0F, -1.4F, 0.0F, 0.0F, 0.829F));

		PartDefinition BreastplateR2_r1 = Armor.addOrReplaceChild("BreastplateR2_r1", CubeListBuilder.create().texOffs(100, 55).addBox(-1.0F, -2.0F, 0.7F, 1.6F, 2.0F, 0.3F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-2.9F, 0.9F, -1.4F, 0.0F, 0.0F, -0.3927F));

		PartDefinition BreastplateR1_r1 = Armor.addOrReplaceChild("BreastplateR1_r1", CubeListBuilder.create().texOffs(100, 52).addBox(-1.0F, -2.0F, 0.7F, 2.0F, 2.0F, 0.3F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-2.5F, 0.0F, -1.4F, 0.0F, 0.0F, -0.829F));

		PartDefinition BreastplateCore2_r1 = Armor.addOrReplaceChild("BreastplateCore2_r1", CubeListBuilder.create().texOffs(116, 99).addBox(-3.0F, -3.0F, -0.65F, 4.0F, 4.0F, 1.55F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.0333F, 2.0295F, 0.0691F, 0.0309F, -0.0308F, 0.7849F));

		PartDefinition BreastplateCore1_r1 = Armor.addOrReplaceChild("BreastplateCore1_r1", CubeListBuilder.create().texOffs(117, 81).addBox(-1.5F, -1.5F, -1.1F, 3.0F, 3.0F, 2.1F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.0333F, 0.2505F, -0.0522F, 0.124F, -0.1231F, 0.7777F));

		PartDefinition HipArmor = Armor.addOrReplaceChild("HipArmor", CubeListBuilder.create().texOffs(96, 109).addBox(-8.2313F, -2.3432F, -2.525F, 9.3F, 1.0F, 5.2F, new CubeDeformation(0.0F))
				.texOffs(87, 96).addBox(1.0688F, -0.8432F, -1.925F, 0.25F, 3.0F, 3.8F, new CubeDeformation(0.0F))
				.texOffs(87, 96).mirror().addBox(-8.4479F, -0.8432F, -1.925F, 0.25F, 3.0F, 3.8F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(3.5313F, 10.5432F, 2.025F));

		PartDefinition HipArmorBaseR3_r1 = HipArmor.addOrReplaceChild("HipArmorBaseR3_r1", CubeListBuilder.create().texOffs(88, 97).mirror().addBox(-0.125F, -1.0F, -1.0F, 0.25F, 3.0F, 3.0F, new CubeDeformation(0.0F)).mirror(false)
				.texOffs(88, 97).addBox(9.3917F, -1.0F, -1.0F, 0.25F, 3.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-8.3229F, 1.4568F, -0.025F, -0.7854F, 0.0F, 0.0F));

		PartDefinition HipArmorBaseR1_r1 = HipArmor.addOrReplaceChild("HipArmorBaseR1_r1", CubeListBuilder.create().texOffs(88, 97).mirror().addBox(-0.125F, -1.2F, -1.2F, 0.25F, 3.2F, 3.2F, new CubeDeformation(0.0F)).mirror(false)
				.texOffs(88, 97).addBox(9.3917F, -1.2F, -1.2F, 0.25F, 3.2F, 3.2F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-8.3229F, -1.5432F, -0.025F, -0.7854F, 0.0F, 0.0F));

		PartDefinition HipArmorGemR3_r1 = HipArmor.addOrReplaceChild("HipArmorGemR3_r1", CubeListBuilder.create().texOffs(91, 84).mirror().addBox(-0.2F, -1.0F, -1.0F, 0.4F, 2.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-8.5292F, 2.0F, 0.0F, 0.0F, 0.0F, -0.0698F));

		PartDefinition HipArmorGemR2_r1 = HipArmor.addOrReplaceChild("HipArmorGemR2_r1", CubeListBuilder.create().texOffs(91, 84).mirror().addBox(-0.25F, -1.0F, -1.0F, 0.5F, 2.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false)
				.texOffs(91, 84).addBox(9.7792F, -1.0F, -1.0F, 0.5F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-8.5792F, 0.1F, 0.0F, 0.0F, 0.0F, 0.0F));

		PartDefinition HipArmorGemR1_r1 = HipArmor.addOrReplaceChild("HipArmorGemR1_r1", CubeListBuilder.create().texOffs(91, 84).mirror().addBox(-0.1F, -1.0F, -1.0F, 0.5F, 2.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false)
				.texOffs(91, 84).addBox(9.9292F, -1.0F, -1.0F, 0.5F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-8.7292F, -1.0F, 0.0F, 0.7854F, 0.0F, 0.0F));

		PartDefinition HipArmorGemL3_r1 = HipArmor.addOrReplaceChild("HipArmorGemL3_r1", CubeListBuilder.create().texOffs(91, 84).addBox(-0.2F, -1.0F, -1.0F, 0.4F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.4F, 2.0F, 0.0F, 0.0F, 0.0F, 0.0698F));

		PartDefinition leftArm;
		if (slim) {
			leftArm = partdefinition.addOrReplaceChild("left_arm", CubeListBuilder.create().texOffs(40, 16).addBox(-1.0F, -2.0F, -2.0F, 3.0F, 12.0F, 4.0F, new CubeDeformation(0.25F)), PartPose.offset(5.0F, 2.5F, 0.0F));
		} else {
			leftArm = partdefinition.addOrReplaceChild("left_arm", CubeListBuilder.create().texOffs(40, 16).addBox(-1.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.25F)), PartPose.offset(5.0F, 2.0F, 0.0F));
		}
		PartDefinition VambraceL = leftArm.addOrReplaceChild("VambraceL", CubeListBuilder.create().texOffs(86, 64).addBox(-1.6F, 5.6F, -2.6F, 5.25F, 4.0F, 5.2F, new CubeDeformation(0.0F))
				.texOffs(106, 122).addBox(-1.7F, 9.45F, -2.7F, 5.5F, 0.25F, 5.4F, new CubeDeformation(0.0F))
				.texOffs(106, 122).addBox(-1.7F, 5.45F, -2.7F, 5.5F, 0.25F, 5.4F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition cube_r1 = VambraceL.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(77, 111).addBox(1.1F, -1.0F, -0.025F, 2.0F, 1.8F, 0.0F, new CubeDeformation(0.0F))
				.texOffs(77, 114).addBox(-0.9F, -1.0F, -0.175F, 2.0F, 1.9F, 0.35F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(5.3564F, 5.5102F, 0.125F, 0.0F, 0.0F, -0.6109F));

		PartDefinition cube_r2 = VambraceL.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(83, 114).addBox(-1.0F, -1.0F, -0.175F, 2.0F, 2.0F, 0.35F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(3.8F, 6.6F, 0.125F, 0.0F, 0.0F, -0.6109F));

		PartDefinition PauldronL = leftArm.addOrReplaceChild("PauldronL", CubeListBuilder.create(), PartPose.offsetAndRotation(4.1331F, -1.6538F, 0.0F, 0.0F, 0.0F, 0.1396F));

		PartDefinition Gem5_r1 = PauldronL.addOrReplaceChild("Gem5_r1", CubeListBuilder.create().texOffs(50, 112).addBox(-0.075F, -1.35F, -1.4F, 0.15F, 2.7F, 2.85F, new CubeDeformation(0.0F))
				.texOffs(50, 112).addBox(-0.225F, -1.45F, -1.5F, 0.15F, 2.9F, 3.05F, new CubeDeformation(0.0F))
				.texOffs(50, 112).addBox(-0.375F, -1.55F, -1.6F, 0.15F, 3.1F, 3.25F, new CubeDeformation(0.0F))
				.texOffs(50, 112).addBox(-0.625F, -1.65F, -1.7F, 0.25F, 3.3F, 3.45F, new CubeDeformation(0.0F))
				.texOffs(49, 111).addBox(-0.875F, -1.75F, -1.8F, 0.25F, 3.5F, 3.65F, new CubeDeformation(0.0F))
				.texOffs(84, 118).addBox(-1.825F, -2.35F, -2.35F, 0.25F, 4.7F, 4.7F, new CubeDeformation(0.0F))
				.texOffs(74, 119).addBox(-1.575F, -2.25F, -2.25F, 0.25F, 4.5F, 4.5F, new CubeDeformation(0.0F))
				.texOffs(75, 119).addBox(-1.325F, -2.15F, -2.15F, 0.25F, 4.3F, 4.3F, new CubeDeformation(0.0F))
				.texOffs(75, 120).addBox(-1.125F, -2.0F, -2.0F, 0.25F, 4.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.2919F, -0.3462F, 0.0F, 0.0F, 0.0F, -0.2618F));

		PartDefinition rightArm;
		if (slim) {
			rightArm = partdefinition.addOrReplaceChild("right_arm", CubeListBuilder.create().texOffs(32, 48).addBox(-2.0F, -2.0F, -2.0F, 3.0F, 12.0F, 4.0F, new CubeDeformation(0.25F)), PartPose.offset(-5.0F, 2.5F, 0.0F));
		} else {
			rightArm = partdefinition.addOrReplaceChild("right_arm", CubeListBuilder.create().texOffs(32, 48).addBox(-3.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.25F)), PartPose.offset(-5.0F, 2.0F, 0.0F));
		}
		PartDefinition VambraceR = rightArm.addOrReplaceChild("VambraceR", CubeListBuilder.create().texOffs(86, 64).mirror().addBox(-3.65F, 5.6F, -2.6F, 5.25F, 4.0F, 5.2F, new CubeDeformation(0.0F)).mirror(false)
				.texOffs(106, 122).mirror().addBox(-3.8F, 9.45F, -2.7F, 5.5F, 0.25F, 5.4F, new CubeDeformation(0.0F)).mirror(false)
				.texOffs(106, 122).mirror().addBox(-3.8F, 5.45F, -2.7F, 5.5F, 0.25F, 5.4F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition cube_r3 = VambraceR.addOrReplaceChild("cube_r3", CubeListBuilder.create().texOffs(77, 111).mirror().addBox(-3.1F, -1.0F, -0.025F, 2.0F, 1.8F, 0.0F, new CubeDeformation(0.0F)).mirror(false)
				.texOffs(77, 114).mirror().addBox(-1.1F, -1.0F, -0.175F, 2.0F, 1.9F, 0.35F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-5.3564F, 5.5102F, 0.125F, 0.0F, 0.0F, 0.6109F));

		PartDefinition cube_r4 = VambraceR.addOrReplaceChild("cube_r4", CubeListBuilder.create().texOffs(83, 114).mirror().addBox(-1.0F, -1.0F, -0.175F, 2.0F, 2.0F, 0.35F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-3.8F, 6.6F, 0.125F, 0.0F, 0.0F, 0.6109F));

		PartDefinition PauldronR = rightArm.addOrReplaceChild("PauldronR", CubeListBuilder.create(), PartPose.offsetAndRotation(-4.1331F, -1.6538F, 0.0F, 0.0F, 0.0F, -0.1396F));

		PartDefinition Gem6_r1 = PauldronR.addOrReplaceChild("Gem6_r1", CubeListBuilder.create().texOffs(50, 112).mirror().addBox(-0.075F, -1.35F, -1.4F, 0.15F, 2.7F, 2.85F, new CubeDeformation(0.0F)).mirror(false)
				.texOffs(50, 112).mirror().addBox(0.075F, -1.45F, -1.5F, 0.15F, 2.9F, 3.05F, new CubeDeformation(0.0F)).mirror(false)
				.texOffs(50, 112).mirror().addBox(0.225F, -1.55F, -1.6F, 0.15F, 3.1F, 3.25F, new CubeDeformation(0.0F)).mirror(false)
				.texOffs(50, 112).mirror().addBox(0.375F, -1.65F, -1.7F, 0.25F, 3.3F, 3.45F, new CubeDeformation(0.0F)).mirror(false)
				.texOffs(49, 111).mirror().addBox(0.625F, -1.75F, -1.8F, 0.25F, 3.5F, 3.65F, new CubeDeformation(0.0F)).mirror(false)
				.texOffs(84, 118).mirror().addBox(1.575F, -2.35F, -2.35F, 0.25F, 4.7F, 4.7F, new CubeDeformation(0.0F)).mirror(false)
				.texOffs(74, 119).mirror().addBox(1.325F, -2.25F, -2.25F, 0.25F, 4.5F, 4.5F, new CubeDeformation(0.0F)).mirror(false)
				.texOffs(75, 119).mirror().addBox(1.075F, -2.15F, -2.15F, 0.25F, 4.3F, 4.3F, new CubeDeformation(0.0F)).mirror(false)
				.texOffs(75, 120).mirror().addBox(0.875F, -2.0F, -2.0F, 0.25F, 4.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-1.2919F, -0.3462F, 0.0F, 0.0F, 0.0F, 0.2618F));

		PartDefinition leftLeg = partdefinition.addOrReplaceChild("left_leg", CubeListBuilder.create().texOffs(16, 48).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.25F)), PartPose.offset(1.9F, 12.0F, 0.0F));

		PartDefinition Boot = leftLeg.addOrReplaceChild("Boot", CubeListBuilder.create().texOffs(0, 119).addBox(-2.4F, 8.5F, -2.3F, 4.8F, 3.9F, 4.6F, new CubeDeformation(0.0F))
				.texOffs(25, 124).addBox(-2.4F, 8.75F, -2.4F, 4.8F, 3.65F, 0.1F, new CubeDeformation(0.0F))
				.texOffs(25, 124).addBox(-2.4F, 8.85F, -2.5F, 4.8F, 3.55F, 0.1F, new CubeDeformation(0.0F))
				.texOffs(25, 124).addBox(-2.4F, 8.95F, -2.6F, 4.8F, 3.45F, 0.1F, new CubeDeformation(0.0F))
				.texOffs(24, 123).addBox(-2.4F, 9.05F, -3.0F, 4.8F, 3.35F, 0.4F, new CubeDeformation(0.0F))
				.texOffs(24, 123).addBox(-2.3F, 9.15F, -3.4F, 4.6F, 3.25F, 0.4F, new CubeDeformation(0.0F))
				.texOffs(24, 123).addBox(-2.2F, 9.25F, -3.8F, 4.4F, 3.15F, 0.4F, new CubeDeformation(0.0F))
				.texOffs(24, 123).addBox(-2.0F, 9.45F, -4.2F, 4.0F, 2.95F, 0.4F, new CubeDeformation(0.0F))
				.texOffs(24, 123).addBox(-1.9F, 9.55F, -4.6F, 3.8F, 2.85F, 0.4F, new CubeDeformation(0.0F))
				.texOffs(24, 123).addBox(-1.8F, 9.65F, -5.0F, 3.6F, 2.75F, 0.4F, new CubeDeformation(0.0F))
				.texOffs(24, 123).addBox(-1.7F, 9.8F, -5.4F, 3.4F, 2.6F, 0.4F, new CubeDeformation(0.0F))
				.texOffs(24, 123).addBox(-1.6F, 10.05F, -5.8F, 3.2F, 2.35F, 0.4F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition Kneepad = leftLeg.addOrReplaceChild("Kneepad", CubeListBuilder.create().texOffs(58, 122).addBox(-2.3F, 4.5F, -2.3F, 4.6F, 1.0F, 4.6F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition cube_r5 = Kneepad.addOrReplaceChild("cube_r5", CubeListBuilder.create().texOffs(43, 125).addBox(-1.0F, -1.0F, -0.25F, 2.0F, 2.0F, 0.5F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 5.0F, -2.25F, 0.0F, 0.0F, -0.7854F));

		PartDefinition rightLeg = partdefinition.addOrReplaceChild("right_leg", CubeListBuilder.create().texOffs(0, 16).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.25F)), PartPose.offset(-1.9F, 12.0F, 0.0F));

		PartDefinition Boot2 = rightLeg.addOrReplaceChild("Boot2", CubeListBuilder.create().texOffs(0, 119).mirror().addBox(-2.4F, 8.5F, -2.3F, 4.8F, 3.9F, 4.6F, new CubeDeformation(0.0F)).mirror(false)
				.texOffs(25, 124).mirror().addBox(-2.4F, 8.75F, -2.4F, 4.8F, 3.65F, 0.1F, new CubeDeformation(0.0F)).mirror(false)
				.texOffs(25, 124).mirror().addBox(-2.4F, 8.85F, -2.5F, 4.8F, 3.55F, 0.1F, new CubeDeformation(0.0F)).mirror(false)
				.texOffs(25, 124).mirror().addBox(-2.4F, 8.95F, -2.6F, 4.8F, 3.45F, 0.1F, new CubeDeformation(0.0F)).mirror(false)
				.texOffs(24, 123).mirror().addBox(-2.4F, 9.05F, -3.0F, 4.8F, 3.35F, 0.4F, new CubeDeformation(0.0F)).mirror(false)
				.texOffs(24, 123).mirror().addBox(-2.3F, 9.15F, -3.4F, 4.6F, 3.25F, 0.4F, new CubeDeformation(0.0F)).mirror(false)
				.texOffs(24, 123).mirror().addBox(-2.2F, 9.25F, -3.8F, 4.4F, 3.15F, 0.4F, new CubeDeformation(0.0F)).mirror(false)
				.texOffs(24, 123).mirror().addBox(-2.0F, 9.45F, -4.2F, 4.0F, 2.95F, 0.4F, new CubeDeformation(0.0F)).mirror(false)
				.texOffs(24, 123).mirror().addBox(-1.9F, 9.55F, -4.6F, 3.8F, 2.85F, 0.4F, new CubeDeformation(0.0F)).mirror(false)
				.texOffs(24, 123).mirror().addBox(-1.8F, 9.65F, -5.0F, 3.6F, 2.75F, 0.4F, new CubeDeformation(0.0F)).mirror(false)
				.texOffs(24, 123).mirror().addBox(-1.7F, 9.8F, -5.4F, 3.4F, 2.6F, 0.4F, new CubeDeformation(0.0F)).mirror(false)
				.texOffs(24, 123).mirror().addBox(-1.6F, 10.05F, -5.8F, 3.2F, 2.35F, 0.4F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition Kneepad2 = rightLeg.addOrReplaceChild("Kneepad2", CubeListBuilder.create().texOffs(58, 122).mirror().addBox(-2.3F, 4.5F, -2.3F, 4.6F, 1.0F, 4.6F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition cube_r6 = Kneepad2.addOrReplaceChild("cube_r6", CubeListBuilder.create().texOffs(43, 125).mirror().addBox(-1.0F, -1.0F, -0.25F, 2.0F, 2.0F, 0.5F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.0F, 5.0F, -2.25F, 0.0F, 0.0F, 0.7854F));

		return LayerDefinition.create(meshdefinition, 128, 128);
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
    //super.renderToBuffer(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
    }

    @Override
    public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        if (entity instanceof ArmorStand) {
            super.setupAnim(entity, 0, 0, 0, 0, 0);
        } else {
            rightArm.copyFrom(super.rightArm);
            leftArm.copyFrom(super.leftArm);
            head.copyFrom(super.head);
            body.copyFrom(super.body);
            leftLeg.copyFrom(super.leftLeg);
            rightLeg.copyFrom(super.rightLeg);

            super.setupAnim(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
        }
    }

	
}