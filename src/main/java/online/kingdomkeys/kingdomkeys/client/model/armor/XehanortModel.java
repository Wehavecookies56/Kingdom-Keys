package online.kingdomkeys.kingdomkeys.client.model.armor;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

import net.minecraft.client.model.HumanoidModel;
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
import online.kingdomkeys.kingdomkeys.KingdomKeys;

public class XehanortModel<T extends LivingEntity> extends ArmorBaseModel<T> {

    public static final ModelLayerLocation LAYER_LOCATION_TOP = new ModelLayerLocation(new ResourceLocation(KingdomKeys.MODID, "xehanort_top"), "main");
    public static final ModelLayerLocation LAYER_LOCATION_BOTTOM = new ModelLayerLocation(new ResourceLocation(KingdomKeys.MODID, "xehanort_bottom"), "main");

    public final ModelPart head;
    public final ModelPart body;
    public final ModelPart left_arm;
    public final ModelPart right_arm;
    public final ModelPart left_leg;
    public final ModelPart right_leg;  
    
    public XehanortModel(ModelPart root) {
        super(root);
		this.head = root.getChild("head");
		this.body = root.getChild("body");
		this.left_arm = root.getChild("left_arm");
		this.right_arm = root.getChild("right_arm");
		this.left_leg = root.getChild("left_leg");
		this.right_leg = root.getChild("right_leg");
    }

    public static LayerDefinition createBodyLayer(CubeDeformation size) {
        MeshDefinition meshdefinition = new MeshDefinition(); //HumanoidModel.createMesh(size, 0);
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition head = partdefinition.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 0).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.5F)), PartPose.offset(0.0F, 0.0F, 0.0F));
		PartDefinition hat = partdefinition.addOrReplaceChild("hat", CubeListBuilder.create().texOffs(32, 0).addBox(0, 0, 0, 0, 0, 0, new CubeDeformation(0)), PartPose.offset(0.0F, 0.0F, 0.0F));
		PartDefinition body = partdefinition.addOrReplaceChild("body", CubeListBuilder.create().texOffs(16, 16).addBox(-4.0F, 0.0F, -2.0F, 8.0F, 12.0F, 4.0F, new CubeDeformation(0.5F)), PartPose.offset(0.0F, 0.0F, 0.0F));
		PartDefinition left_arm = partdefinition.addOrReplaceChild("left_arm", CubeListBuilder.create().texOffs(40, 16).addBox(-1.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.5F)), PartPose.offset(5.0F, 2.0F, 0.0F));
		PartDefinition right_arm = partdefinition.addOrReplaceChild("right_arm", CubeListBuilder.create().texOffs(32, 48).addBox(-3.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.5F)), PartPose.offset(-5.0F, 2.0F, 0.0F));
		PartDefinition left_leg = partdefinition.addOrReplaceChild("left_leg", CubeListBuilder.create().texOffs(16, 48).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.25F)), PartPose.offset(1.9F, 12.0F, 0.0F));
		PartDefinition right_leg = partdefinition.addOrReplaceChild("right_leg", CubeListBuilder.create().texOffs(0, 16).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.25F)), PartPose.offset(-1.9F, 12.0F, 0.0F));


		PartDefinition Sides = head.addOrReplaceChild("Sides", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));

		PartDefinition cube_r1 = Sides.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(137, 54).addBox(-2.25F, -3.4F, -1.3F, 0.85F, 0.6F, 3.9F, new CubeDeformation(0.0F))
		.texOffs(137, 54).addBox(-2.25F, -2.8F, -1.3F, 0.85F, 1.0F, 3.9F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-2.45F, -28.0463F, -1.8007F, 0.0F, -0.1309F, 0.0F));

		PartDefinition cube_r2 = Sides.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(137, 54).addBox(-2.25F, -2.5F, -1.7F, 0.85F, 1.0F, 4.2F, new CubeDeformation(0.0F))
		.texOffs(137, 54).addBox(-2.25F, -1.5F, -1.8F, 0.6F, 1.0F, 4.3F, new CubeDeformation(0.0F))
		.texOffs(136, 53).addBox(-2.25F, -0.5F, -2.0F, 0.6F, 1.0F, 4.5F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-2.45F, -28.0463F, -1.8007F, -0.3054F, -0.1309F, 0.0F));

		PartDefinition cube_r3 = Sides.addOrReplaceChild("cube_r3", CubeListBuilder.create().texOffs(137, 54).mirror().addBox(1.4F, -3.4F, -1.3F, 0.85F, 0.6F, 3.9F, new CubeDeformation(0.0F)).mirror(false)
		.texOffs(137, 54).mirror().addBox(1.4F, -2.8F, -1.3F, 0.85F, 1.0F, 3.9F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(2.45F, -28.0463F, -1.8007F, 0.0F, 0.1309F, 0.0F));

		PartDefinition cube_r4 = Sides.addOrReplaceChild("cube_r4", CubeListBuilder.create().texOffs(137, 54).mirror().addBox(1.4F, -2.5F, -1.7F, 0.85F, 1.0F, 4.2F, new CubeDeformation(0.0F)).mirror(false)
		.texOffs(137, 54).mirror().addBox(1.65F, -1.5F, -1.8F, 0.6F, 1.0F, 4.3F, new CubeDeformation(0.0F)).mirror(false)
		.texOffs(136, 53).mirror().addBox(1.65F, -0.5F, -2.0F, 0.6F, 1.0F, 4.5F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(2.45F, -28.0463F, -1.8007F, -0.3054F, 0.1309F, 0.0F));

		PartDefinition spike2_r1 = Sides.addOrReplaceChild("spike2_r1", CubeListBuilder.create().texOffs(89, 42).mirror().addBox(0.0F, -5.0F, -6.0F, 0.0F, 10.0F, 12.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-6.3F, -30.25F, 2.9F, 0.4488F, -0.6067F, -0.0224F));

		PartDefinition spike1_r1 = Sides.addOrReplaceChild("spike1_r1", CubeListBuilder.create().texOffs(89, 42).addBox(0.0F, -5.0F, -6.0F, 0.0F, 10.0F, 12.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(6.3F, -30.25F, 2.9F, 0.4488F, 0.6067F, 0.0224F));

		PartDefinition Front = head.addOrReplaceChild("Front", CubeListBuilder.create().texOffs(162, 54).addBox(-4.5F, -7.4F, -4.8F, 9.0F, 2.8F, 0.3F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition cube_r5 = Front.addOrReplaceChild("cube_r5", CubeListBuilder.create().texOffs(186, 30).addBox(-1.9F, -1.5F, -1.9F, 2.9F, 2.0F, 2.9F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -6.6265F, -4.3361F, -0.7805F, -0.6178F, 0.5208F));

		PartDefinition cube_r6 = Front.addOrReplaceChild("cube_r6", CubeListBuilder.create().texOffs(161, 30).addBox(-1.9F, -1.5F, -1.9F, 3.9F, 2.0F, 3.9F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -6.1265F, -4.8361F, -0.1841F, -0.7769F, 0.1298F));

		PartDefinition cube_r7 = Front.addOrReplaceChild("cube_r7", CubeListBuilder.create().texOffs(161, 30).addBox(-2.0F, -0.5F, -2.0F, 4.0F, 1.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -4.124F, -4.6946F, 0.3038F, -0.762F, -0.2132F));

		PartDefinition cube_r8 = Front.addOrReplaceChild("cube_r8", CubeListBuilder.create().texOffs(161, 30).addBox(-2.1F, -1.0F, -2.1F, 4.1F, 2.0F, 4.1F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -5.0F, -4.7358F, 0.0F, -0.7854F, 0.0F));

		PartDefinition cube_r9 = Front.addOrReplaceChild("cube_r9", CubeListBuilder.create().texOffs(137, 32).addBox(-1.5F, -1.0F, -1.5F, 3.0F, 2.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.0308F, -3.062F, -3.3161F, -0.0592F, 0.7409F, -0.0399F));

		PartDefinition cube_r10 = Front.addOrReplaceChild("cube_r10", CubeListBuilder.create().texOffs(137, 32).addBox(-2.0F, -2.0F, -1.0F, 3.0F, 2.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -0.75F, -5.5F, -0.8499F, 0.544F, -0.5323F));

		PartDefinition Top = head.addOrReplaceChild("Top", CubeListBuilder.create().texOffs(205, 29).addBox(-4.0F, -14.0F, -4.0F, 8.0F, 5.0F, 8.0F, new CubeDeformation(0.5F)), PartPose.offset(0.0F, 0.0F, 0.0F));


		PartDefinition ChestplateExtras = body.addOrReplaceChild("ChestplateExtras", CubeListBuilder.create().texOffs(69, 0).addBox(-4.5F, -24.0F, -2.75F, 9.0F, 12.5F, 0.25F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 24.0F, 0.0F));

		PartDefinition RightShoulder = ChestplateExtras.addOrReplaceChild("RightShoulder", CubeListBuilder.create(), PartPose.offset(-5.85F, -25.6F, 0.0F));

		PartDefinition cube_r11 = RightShoulder.addOrReplaceChild("cube_r11", CubeListBuilder.create().texOffs(98, 0).addBox(-4.0F, -2.5F, -3.0F, 6.0F, 1.0F, 6.0F, new CubeDeformation(0.1F))
		.texOffs(98, 0).addBox(-3.5F, -1.5F, -3.0F, 6.0F, 2.0F, 6.0F, new CubeDeformation(0.1F))
		.texOffs(98, 0).addBox(-3.0F, 0.5F, -3.0F, 6.0F, 2.0F, 6.0F, new CubeDeformation(0.1F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, -0.2182F));


		PartDefinition SleeveVambraceThing1 = left_arm.addOrReplaceChild("SleeveVambraceThing1", CubeListBuilder.create().texOffs(148, 0).addBox(-2.0F, 4.0F, -3.0F, 6.0F, 1.0F, 6.0F, new CubeDeformation(0.0F))
		.texOffs(148, 0).addBox(-2.0F, 9.1F, -3.0F, 6.0F, 1.0F, 6.0F, new CubeDeformation(0.0F))
		.texOffs(180, 0).addBox(-1.9F, 5.0F, -2.9F, 5.8F, 1.0F, 5.8F, new CubeDeformation(0.0F))
		.texOffs(180, 0).addBox(-1.9F, 8.8F, -2.9F, 5.8F, 1.0F, 5.8F, new CubeDeformation(0.0F))
		.texOffs(180, 0).addBox(-1.8F, 6.0F, -2.8F, 5.6F, 1.0F, 5.6F, new CubeDeformation(0.0F))
		.texOffs(180, 0).addBox(-1.8F, 8.6F, -2.8F, 5.6F, 1.0F, 5.6F, new CubeDeformation(0.0F))
		.texOffs(181, 1).addBox(-1.6F, 7.0F, -2.6F, 5.2F, 1.0F, 5.2F, new CubeDeformation(0.0F))
		.texOffs(181, 1).addBox(-1.6F, 8.0F, -2.6F, 5.2F, 1.0F, 5.2F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition cube_r12 = SleeveVambraceThing1.addOrReplaceChild("cube_r12", CubeListBuilder.create().texOffs(212, 2).addBox(1.0F, -0.5F, -2.1F, 2.0F, 0.4F, 4.2F, new CubeDeformation(0.0F))
		.texOffs(212, 2).addBox(-1.0F, -0.5F, -2.4F, 2.0F, 0.6F, 4.8F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(8.2709F, -0.2293F, 0.05F, 0.0F, 0.0F, -1.0996F));

		PartDefinition cube_r13 = SleeveVambraceThing1.addOrReplaceChild("cube_r13", CubeListBuilder.create().texOffs(211, 1).addBox(-1.1F, -0.7F, -2.65F, 2.0F, 0.8F, 5.3F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(7.4469F, 1.5288F, 0.0F, 0.0F, 0.0F, -0.925F));

		PartDefinition cube_r14 = SleeveVambraceThing1.addOrReplaceChild("cube_r14", CubeListBuilder.create().texOffs(211, 1).addBox(0.9F, -0.5F, -2.75F, 2.0F, 0.9F, 5.5F, new CubeDeformation(0.0F))
		.texOffs(210, 0).addBox(-1.1F, -0.5F, -3.0F, 2.0F, 1.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(4.4352F, 4.0087F, 0.0F, 0.0F, 0.0F, -0.6632F));

		PartDefinition Pauldron1 = left_arm.addOrReplaceChild("Pauldron1", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition cube_r15 = Pauldron1.addOrReplaceChild("cube_r15", CubeListBuilder.create().texOffs(95, 19).addBox(-3.0F, -15.0F, -3.1F, 6.2F, 15.0F, 6.2F, new CubeDeformation(0.0F))
		.texOffs(63, 19).addBox(-3.0F, -2.0F, -3.0F, 6.0F, 4.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.0F, -2.0F, 0.0F, 0.0F, 0.0F, 0.1745F));


		PartDefinition SleeveVambraceThing2 = right_arm.addOrReplaceChild("SleeveVambraceThing2", CubeListBuilder.create().texOffs(148, 0).mirror().addBox(-4.0F, 4.0F, -3.0F, 6.0F, 1.0F, 6.0F, new CubeDeformation(0.0F)).mirror(false)
		.texOffs(148, 0).mirror().addBox(-4.0F, 9.1F, -3.0F, 6.0F, 1.0F, 6.0F, new CubeDeformation(0.0F)).mirror(false)
		.texOffs(180, 0).mirror().addBox(-3.9F, 5.0F, -2.9F, 5.8F, 1.0F, 5.8F, new CubeDeformation(0.0F)).mirror(false)
		.texOffs(180, 0).mirror().addBox(-3.9F, 8.8F, -2.9F, 5.8F, 1.0F, 5.8F, new CubeDeformation(0.0F)).mirror(false)
		.texOffs(180, 0).mirror().addBox(-3.8F, 6.0F, -2.8F, 5.6F, 1.0F, 5.6F, new CubeDeformation(0.0F)).mirror(false)
		.texOffs(180, 0).mirror().addBox(-3.8F, 8.6F, -2.8F, 5.6F, 1.0F, 5.6F, new CubeDeformation(0.0F)).mirror(false)
		.texOffs(181, 1).mirror().addBox(-3.6F, 7.0F, -2.6F, 5.2F, 1.0F, 5.2F, new CubeDeformation(0.0F)).mirror(false)
		.texOffs(181, 1).mirror().addBox(-3.6F, 8.0F, -2.6F, 5.2F, 1.0F, 5.2F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition cube_r16 = SleeveVambraceThing2.addOrReplaceChild("cube_r16", CubeListBuilder.create().texOffs(212, 2).mirror().addBox(-3.0F, -0.5F, -2.1F, 2.0F, 0.4F, 4.2F, new CubeDeformation(0.0F)).mirror(false)
		.texOffs(212, 2).mirror().addBox(-1.0F, -0.5F, -2.4F, 2.0F, 0.6F, 4.8F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-8.2709F, -0.2293F, 0.05F, 0.0F, 0.0F, 1.0996F));

		PartDefinition cube_r17 = SleeveVambraceThing2.addOrReplaceChild("cube_r17", CubeListBuilder.create().texOffs(211, 1).mirror().addBox(-0.9F, -0.7F, -2.65F, 2.0F, 0.8F, 5.3F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-7.4469F, 1.5288F, 0.0F, 0.0F, 0.0F, 0.925F));

		PartDefinition cube_r18 = SleeveVambraceThing2.addOrReplaceChild("cube_r18", CubeListBuilder.create().texOffs(211, 1).mirror().addBox(-2.9F, -0.5F, -2.75F, 2.0F, 0.9F, 5.5F, new CubeDeformation(0.0F)).mirror(false)
		.texOffs(210, 0).mirror().addBox(-0.9F, -0.5F, -3.0F, 2.0F, 1.0F, 6.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-4.4352F, 4.0087F, 0.0F, 0.0F, 0.0F, 0.6632F));

		PartDefinition Pauldron2 = right_arm.addOrReplaceChild("Pauldron2", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition cube_r19 = Pauldron2.addOrReplaceChild("cube_r19", CubeListBuilder.create().texOffs(63, 19).mirror().addBox(-3.0F, -2.0F, -3.0F, 6.0F, 4.0F, 6.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-1.0F, -2.0F, 0.0F, 0.0F, 0.0F, -0.1745F));


		PartDefinition bootmoment = left_leg.addOrReplaceChild("bootmoment", CubeListBuilder.create().texOffs(160, 73).addBox(-2.4F, 7.5F, -2.5F, 4.8F, 5.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition BootSpike_Front_r1 = bootmoment.addOrReplaceChild("BootSpike_Front_r1", CubeListBuilder.create().texOffs(151, 77).addBox(-1.0F, -5.0F, -1.0F, 2.0F, 6.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 7.9F, -2.0F, 0.4194F, -0.7401F, -0.2921F));

		PartDefinition Toe_cause_Abel = bootmoment.addOrReplaceChild("Toe_cause_Abel", CubeListBuilder.create().texOffs(107, 79).addBox(-2.0F, 10.5F, -4.0F, 4.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition FrontConnection_r1 = Toe_cause_Abel.addOrReplaceChild("FrontConnection_r1", CubeListBuilder.create().texOffs(110, 87).addBox(-1.2F, -1.9F, -1.3F, 2.2F, 3.9F, 2.3F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.1F, 10.0F, -2.0F, -0.8706F, -0.5724F, 0.5713F));

		PartDefinition cube_r20 = Toe_cause_Abel.addOrReplaceChild("cube_r20", CubeListBuilder.create().texOffs(107, 76).addBox(-2.0F, -0.9F, -1.0F, 3.0F, 1.8F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 11.6F, -7.0F, 0.0F, 0.7854F, 0.0F));

		PartDefinition cube_r21 = Toe_cause_Abel.addOrReplaceChild("cube_r21", CubeListBuilder.create().texOffs(106, 78).mirror().addBox(-0.75F, -0.9F, -2.15F, 1.0F, 1.8F, 2.8F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-1.0F, 11.595F, -5.9648F, 0.0F, -0.6109F, 0.0F));

		PartDefinition cube_r22 = Toe_cause_Abel.addOrReplaceChild("cube_r22", CubeListBuilder.create().texOffs(106, 78).addBox(-0.25F, -0.9F, -2.15F, 1.0F, 1.8F, 2.8F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.0F, 11.595F, -5.9648F, 0.0F, 0.6109F, 0.0F));

		PartDefinition cube_r23 = Toe_cause_Abel.addOrReplaceChild("cube_r23", CubeListBuilder.create().texOffs(106, 78).addBox(-2.0F, -1.0F, -1.4F, 4.0F, 1.8F, 2.8F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 11.695F, -4.4648F, 0.0F, 0.0F, 0.0F));

		PartDefinition cube_r24 = Toe_cause_Abel.addOrReplaceChild("cube_r24", CubeListBuilder.create().texOffs(105, 77).addBox(-2.0F, -1.0F, -2.8F, 4.0F, 2.0F, 3.8F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 10.5617F, -2.5871F, 0.3491F, 0.0F, 0.0F));

		PartDefinition leggingExtra = left_leg.addOrReplaceChild("leggingExtra", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));


		PartDefinition bootmoment2 = right_leg.addOrReplaceChild("bootmoment2", CubeListBuilder.create().texOffs(160, 73).mirror().addBox(-2.4F, 7.5F, -2.5F, 4.8F, 5.0F, 5.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition BootSpike_Front_r2 = bootmoment2.addOrReplaceChild("BootSpike_Front_r2", CubeListBuilder.create().texOffs(151, 77).mirror().addBox(-1.0F, -5.0F, -1.0F, 2.0F, 6.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.0F, 7.9F, -2.0F, 0.4194F, 0.7401F, 0.2921F));

		PartDefinition Toe_cause_Abel2 = bootmoment2.addOrReplaceChild("Toe_cause_Abel2", CubeListBuilder.create().texOffs(107, 79).mirror().addBox(-2.0F, 10.5F, -4.0F, 4.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition FrontConnection_r2 = Toe_cause_Abel2.addOrReplaceChild("FrontConnection_r2", CubeListBuilder.create().texOffs(110, 87).mirror().addBox(-1.0F, -1.9F, -1.3F, 2.2F, 3.9F, 2.3F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.1F, 10.0F, -2.0F, -0.8706F, 0.5724F, -0.5713F));

		PartDefinition cube_r25 = Toe_cause_Abel2.addOrReplaceChild("cube_r25", CubeListBuilder.create().texOffs(107, 76).mirror().addBox(-1.0F, -0.9F, -1.0F, 3.0F, 1.8F, 3.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.0F, 11.6F, -7.0F, 0.0F, -0.7854F, 0.0F));

		PartDefinition cube_r26 = Toe_cause_Abel2.addOrReplaceChild("cube_r26", CubeListBuilder.create().texOffs(106, 78).addBox(-0.25F, -0.9F, -2.15F, 1.0F, 1.8F, 2.8F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.0F, 11.595F, -5.9648F, 0.0F, 0.6109F, 0.0F));

		PartDefinition cube_r27 = Toe_cause_Abel2.addOrReplaceChild("cube_r27", CubeListBuilder.create().texOffs(106, 78).mirror().addBox(-0.75F, -0.9F, -2.15F, 1.0F, 1.8F, 2.8F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-1.0F, 11.595F, -5.9648F, 0.0F, -0.6109F, 0.0F));

		PartDefinition cube_r28 = Toe_cause_Abel2.addOrReplaceChild("cube_r28", CubeListBuilder.create().texOffs(106, 78).mirror().addBox(-2.0F, -1.0F, -1.4F, 4.0F, 1.8F, 2.8F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.0F, 11.695F, -4.4648F, 0.0F, 0.0F, 0.0F));

		PartDefinition cube_r29 = Toe_cause_Abel2.addOrReplaceChild("cube_r29", CubeListBuilder.create().texOffs(105, 77).mirror().addBox(-2.0F, -1.0F, -2.8F, 4.0F, 2.0F, 3.8F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.0F, 10.5617F, -2.5871F, 0.3491F, 0.0F, 0.0F));

		return LayerDefinition.create(meshdefinition, 256, 256);
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        rightArm.render(poseStack, vertexConsumer, packedLight, packedOverlay);
        rightLeg.render(poseStack, vertexConsumer, packedLight, packedOverlay);
        head.render(poseStack, vertexConsumer, packedLight, packedOverlay);
        body.render(poseStack, vertexConsumer, packedLight, packedOverlay);
        leftArm.render(poseStack, vertexConsumer, packedLight, packedOverlay);
        leftLeg.render(poseStack, vertexConsumer, packedLight, packedOverlay);
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