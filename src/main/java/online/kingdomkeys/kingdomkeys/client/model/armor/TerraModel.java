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

public class TerraModel<T extends LivingEntity> extends HumanoidModel<T> {

    public static final ModelLayerLocation LAYER_LOCATION_TOP = new ModelLayerLocation(new ResourceLocation(KingdomKeys.MODID, "terra_top"), "main");
    public static final ModelLayerLocation LAYER_LOCATION_BOTTOM = new ModelLayerLocation(new ResourceLocation(KingdomKeys.MODID, "terra_bottom"), "main");

    public TerraModel(ModelPart root) {
        super(root);
    }

    public static LayerDefinition createBodyLayer(CubeDeformation size) {
        MeshDefinition meshdefinition = HumanoidModel.createMesh(size, 0);
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition rightArm = partdefinition.getChild("right_arm");
        PartDefinition leftArm = partdefinition.getChild("left_arm");
        PartDefinition rightLeg = partdefinition.getChild("right_leg");
        PartDefinition leftLeg = partdefinition.getChild("left_leg");
        PartDefinition head = partdefinition.getChild("head");
        PartDefinition body = partdefinition.getChild("body");
		

		PartDefinition leftShoulder = leftArm.addOrReplaceChild("leftShoulder", CubeListBuilder.create(), PartPose.offsetAndRotation(2.832F, -1.9973F, 0.0F, 0.0F, 0.0F, -0.1309F));

		PartDefinition Base2_r1 = leftShoulder.addOrReplaceChild("Base2_r1", CubeListBuilder.create().texOffs(53, 30).addBox(-1.0F, -5.5F, -3.0F, 0.3F, 5.5F, 5.0F, new CubeDeformation(0.35F)), PartPose.offsetAndRotation(1.918F, 1.9973F, 0.5F, 0.0F, 0.0F, -0.6109F));

		PartDefinition Base1_r1 = leftShoulder.addOrReplaceChild("Base1_r1", CubeListBuilder.create().texOffs(50, 41).addBox(-1.5F, -1.0F, -1.5F, 3.5F, 1.75F, 3.5F, new CubeDeformation(0.35F)), PartPose.offsetAndRotation(0.4717F, 0.1245F, 0.0F, -3.1416F, -0.7854F, -2.2253F));

		PartDefinition LeftGauntlet = leftArm.addOrReplaceChild("LeftGauntlet", CubeListBuilder.create(), PartPose.offset(3.1354F, 5.5003F, 0.0171F));

		PartDefinition Base1b_r1 = LeftGauntlet.addOrReplaceChild("Base1b_r1", CubeListBuilder.create().texOffs(51, 55).addBox(-1.0F, -3.5F, -2.25F, 1.75F, 4.5F, 4.5F, new CubeDeformation(0.35F)), PartPose.offsetAndRotation(-0.6354F, 3.0777F, -0.0171F, 0.0F, 0.0F, 0.0436F));

		PartDefinition Base1a_r1 = LeftGauntlet.addOrReplaceChild("Base1a_r1", CubeListBuilder.create().texOffs(2, 59).addBox(-1.0F, -2.5F, -1.0F, 1.75F, 3.5F, 1.75F, new CubeDeformation(0.35F)), PartPose.offsetAndRotation(-0.6354F, 2.0777F, -0.0171F, 0.0F, 0.7854F, 1.309F));

		PartDefinition Armor2b_r1 = LeftGauntlet.addOrReplaceChild("Armor2b_r1", CubeListBuilder.create().texOffs(61, 61).addBox(-0.5F, 0.5F, -0.5F, 0.5F, 2.0F, 1.0F, new CubeDeformation(0.35F)), PartPose.offsetAndRotation(0.3646F, -2.7938F, 2.0138F, -0.4363F, 0.0F, 0.0F));

		PartDefinition Armor2a_r1 = LeftGauntlet.addOrReplaceChild("Armor2a_r1", CubeListBuilder.create().texOffs(60, 60).addBox(-0.5F, -0.5F, -0.5F, 0.5F, 2.0F, 1.25F, new CubeDeformation(0.35F)), PartPose.offsetAndRotation(0.3646F, -0.7938F, 1.0138F, -0.6109F, 0.0F, -0.0873F));

		PartDefinition Armor1b_r1 = LeftGauntlet.addOrReplaceChild("Armor1b_r1", CubeListBuilder.create().texOffs(61, 61).addBox(-0.5F, 0.5F, -0.5F, 0.5F, 2.0F, 1.0F, new CubeDeformation(0.35F)), PartPose.offsetAndRotation(0.3646F, -2.7938F, -2.0479F, 0.4363F, 0.0F, 0.0F));

		PartDefinition Armor1a_r1 = LeftGauntlet.addOrReplaceChild("Armor1a_r1", CubeListBuilder.create().texOffs(60, 61).addBox(-0.5F, -0.5F, -0.5F, 0.75F, 2.0F, 1.0F, new CubeDeformation(0.35F)), PartPose.offsetAndRotation(0.3646F, -0.7938F, -1.0479F, 0.6109F, 0.0F, -0.0873F));


		PartDefinition leftLegExtra = leftLeg.addOrReplaceChild("leftLegExtra", CubeListBuilder.create(), PartPose.offset(2.225F, 1.1642F, 2.0F));

		PartDefinition Box_r1 = leftLegExtra.addOrReplaceChild("Box_r1", CubeListBuilder.create().texOffs(0, 51).addBox(-1.0F, -0.5F, -1.0F, 2.0F, 1.5F, 2.25F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.5F, 0.0F, -2.0F, 0.0F, -0.7854F, -0.7418F));

		PartDefinition Box_r2 = leftLegExtra.addOrReplaceChild("Box_r2", CubeListBuilder.create().texOffs(1, 41).addBox(-0.125F, -1.0F, -3.0F, 0.25F, 4.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -1.4142F, -2.0F, 0.7854F, 0.0F, 0.0F));

		PartDefinition leftBoot = leftLegExtra.addOrReplaceChild("leftBoot", CubeListBuilder.create().texOffs(0, 44).addBox(-4.35F, 8.5F, -4.0F, 0.25F, 0.5F, 4.0F, new CubeDeformation(0.35F))
		.texOffs(0, 44).addBox(-0.1F, 8.5F, -4.0F, 0.25F, 0.5F, 4.0F, new CubeDeformation(0.35F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition Box_r3 = leftBoot.addOrReplaceChild("Box_r3", CubeListBuilder.create().texOffs(1, 44).addBox(-2.125F, -0.25F, -4.25F, 0.25F, 0.5F, 4.25F, new CubeDeformation(0.35F))
		.texOffs(0, 44).addBox(1.875F, -0.25F, -4.25F, 0.25F, 0.5F, 4.25F, new CubeDeformation(0.35F)), PartPose.offsetAndRotation(0.025F, 8.75F, -2.0F, 0.0F, 1.5708F, 0.0F));

		PartDefinition Box_r4 = leftBoot.addOrReplaceChild("Box_r4", CubeListBuilder.create().texOffs(4, 47).addBox(-0.125F, -0.25F, -0.5F, 0.25F, 0.5F, 1.0F, new CubeDeformation(0.35F)), PartPose.offsetAndRotation(-4.225F, 7.453F, 1.2433F, 1.0908F, 0.0F, 0.0F));

		PartDefinition Box_r5 = leftBoot.addOrReplaceChild("Box_r5", CubeListBuilder.create().texOffs(2, 47).addBox(-0.125F, -0.25F, -1.25F, 0.25F, 0.5F, 1.75F, new CubeDeformation(0.35F)), PartPose.offsetAndRotation(-4.225F, 8.0F, 0.75F, 0.6109F, 0.0F, 0.0F));


		PartDefinition visor_r1 = head.addOrReplaceChild("visor_r1", CubeListBuilder.create().texOffs(41, 3).addBox(-1.75F, -2.5F, -1.75F, 5.75F, 5.0F, 5.75F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -4.5F, -4.4142F, -0.9136F, -0.5484F, 0.5942F));

		PartDefinition horn1 = head.addOrReplaceChild("horn1", CubeListBuilder.create(), PartPose.offset(-5.5F, -8.5F, 3.0F));

		PartDefinition Spike1d_r1 = horn1.addOrReplaceChild("Spike1d_r1", CubeListBuilder.create().texOffs(60, 57).mirror().addBox(0.0F, -1.5F, -0.75F, 0.5F, 5.0F, 1.5F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-0.2F, -4.0F, -2.05F, 0.0871F, 0.0076F, 0.0007F));

		PartDefinition Spike1c_r1 = horn1.addOrReplaceChild("Spike1c_r1", CubeListBuilder.create().texOffs(54, 58).mirror().addBox(-0.25F, -1.0F, -2.5F, 0.75F, 2.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.5F, 4.1192F, -2.4205F, 0.6109F, 0.0F, 0.0F));

		PartDefinition Spike1b_r1 = horn1.addOrReplaceChild("Spike1b_r1", CubeListBuilder.create().texOffs(56, 59).mirror().addBox(0.0F, 1.0F, -4.0F, 1.0F, 2.0F, 3.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.0F, 0.0F, -2.0F, 0.4363F, 0.0F, 0.0F));

		PartDefinition Spike1a_r1 = horn1.addOrReplaceChild("Spike1a_r1", CubeListBuilder.create().texOffs(59, 57).mirror().addBox(0.0F, -1.5F, -1.0F, 0.5F, 5.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.0F, 0.0F, -2.0F, -0.1309F, 0.0F, -0.0873F));

		PartDefinition horn2 = head.addOrReplaceChild("horn2", CubeListBuilder.create(), PartPose.offset(5.5F, -8.5F, 3.0F));

		PartDefinition Spike1d_r2 = horn2.addOrReplaceChild("Spike1d_r2", CubeListBuilder.create().texOffs(60, 57).addBox(-0.5F, -1.5F, -0.75F, 0.5F, 5.0F, 1.5F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.2F, -4.0F, -2.05F, 0.0871F, -0.0076F, -0.0007F));

		PartDefinition Spike1c_r2 = horn2.addOrReplaceChild("Spike1c_r2", CubeListBuilder.create().texOffs(54, 58).addBox(-0.5F, -1.0F, -2.5F, 0.75F, 2.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.5F, 4.1192F, -2.4205F, 0.6109F, 0.0F, 0.0F));

		PartDefinition Spike1b_r2 = horn2.addOrReplaceChild("Spike1b_r2", CubeListBuilder.create().texOffs(56, 59).addBox(-1.0F, 1.0F, -4.0F, 1.0F, 2.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, -2.0F, 0.4363F, 0.0F, 0.0F));

		PartDefinition Spike1a_r2 = horn2.addOrReplaceChild("Spike1a_r2", CubeListBuilder.create().texOffs(59, 57).addBox(-0.5F, -1.5F, -1.0F, 0.5F, 5.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, -2.0F, -0.1309F, 0.0F, 0.0873F));


		PartDefinition bodyExtra = body.addOrReplaceChild("bodyExtra", CubeListBuilder.create(), PartPose.offset(0.0F, 1.8417F, -0.8557F));

		PartDefinition Box_r6 = bodyExtra.addOrReplaceChild("Box_r6", CubeListBuilder.create().texOffs(57, 47).addBox(-1.0F, -1.25F, 0.0F, 3.0F, 1.25F, 0.25F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(2.75F, 2.0F, -2.0F, 0.0113F, -0.0421F, 1.3088F));

		PartDefinition Box_r7 = bodyExtra.addOrReplaceChild("Box_r7", CubeListBuilder.create().texOffs(57, 47).addBox(-2.0F, -1.25F, 0.0F, 3.0F, 1.25F, 0.25F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-2.75F, 2.0F, -2.0F, 0.0113F, 0.0421F, -1.3088F));

		PartDefinition Box_r8 = bodyExtra.addOrReplaceChild("Box_r8", CubeListBuilder.create().texOffs(56, 47).addBox(-2.0F, -1.0F, 0.75F, 3.75F, 2.0F, 0.25F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(2.0F, 0.1583F, -2.8943F, 0.0F, 0.0F, 0.6981F));

		PartDefinition Box_r9 = bodyExtra.addOrReplaceChild("Box_r9", CubeListBuilder.create().texOffs(56, 47).addBox(-1.75F, -1.0F, 0.75F, 3.75F, 2.0F, 0.25F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-2.0F, 0.1583F, -2.8943F, 0.0F, 0.0F, -0.6981F));

		PartDefinition Box_r10 = bodyExtra.addOrReplaceChild("Box_r10", CubeListBuilder.create().texOffs(56, 47).addBox(-2.0F, -1.0F, 0.5F, 3.0F, 2.0F, 0.75F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.1583F, -2.8943F, -0.0928F, 0.0924F, 0.7811F));

		PartDefinition Box_r11 = bodyExtra.addOrReplaceChild("Box_r11", CubeListBuilder.create().texOffs(56, 47).addBox(-1.0F, -1.0F, 0.5F, 3.0F, 2.0F, 0.75F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.1583F, -2.8943F, -0.0928F, -0.0924F, -0.7811F));


		PartDefinition RightPauldron = rightArm.addOrReplaceChild("RightPauldron", CubeListBuilder.create(), PartPose.offsetAndRotation(-2.832F, -1.9973F, 0.0F, 0.0F, 0.0F, 0.1309F));

		PartDefinition Plate_r1 = RightPauldron.addOrReplaceChild("Plate_r1", CubeListBuilder.create().texOffs(53, 30).addBox(0.7F, -5.5F, -3.0F, 0.3F, 5.5F, 5.0F, new CubeDeformation(0.35F)), PartPose.offsetAndRotation(-1.918F, 1.9973F, 0.5F, 0.0F, 0.0F, 0.6109F));

		PartDefinition Gem_r1 = RightPauldron.addOrReplaceChild("Gem_r1", CubeListBuilder.create().texOffs(50, 41).addBox(-2.0F, -1.0F, -1.5F, 3.5F, 1.75F, 3.5F, new CubeDeformation(0.35F)), PartPose.offsetAndRotation(-0.4717F, 0.1245F, 0.0F, -3.1416F, 0.7854F, 2.2253F));

		PartDefinition RightArmExtras = rightArm.addOrReplaceChild("RightArmExtras", CubeListBuilder.create().texOffs(30, 42).addBox(-0.75F, 1.25F, -4.25F, 4.5F, 0.5F, 4.5F, new CubeDeformation(0.35F)), PartPose.offset(-2.4889F, 7.5423F, 1.9906F));

		PartDefinition Box_r12 = RightArmExtras.addOrReplaceChild("Box_r12", CubeListBuilder.create().texOffs(36, 42).addBox(-2.25F, -0.25F, -2.0F, 1.75F, 0.75F, 4.0F, new CubeDeformation(0.35F)), PartPose.offsetAndRotation(1.5F, 2.0F, -2.0F, 0.0F, 0.0F, 0.1309F));

		PartDefinition Box_r13 = RightArmExtras.addOrReplaceChild("Box_r13", CubeListBuilder.create().texOffs(39, 44).addBox(-1.375F, -0.125F, -1.375F, 2.25F, 0.25F, 2.0F, new CubeDeformation(0.35F)), PartPose.offsetAndRotation(-0.4062F, 1.4626F, -1.9912F, 0.0F, 0.7854F, 0.9599F));

		PartDefinition Box_r14 = RightArmExtras.addOrReplaceChild("Box_r14", CubeListBuilder.create().texOffs(38, 44).addBox(-1.375F, -0.125F, -1.375F, 2.5F, 0.25F, 2.5F, new CubeDeformation(0.35F)), PartPose.offsetAndRotation(-0.4062F, 0.9626F, -1.9912F, 0.0F, 0.7854F, 0.9599F));

		PartDefinition Box_r15 = RightArmExtras.addOrReplaceChild("Box_r15", CubeListBuilder.create().texOffs(38, 44).addBox(-1.375F, -0.125F, -1.375F, 2.5F, 0.25F, 2.5F, new CubeDeformation(0.35F)), PartPose.offsetAndRotation(-0.4062F, 0.7126F, -1.9912F, 0.0F, 0.7854F, 0.9599F));

		PartDefinition Box_r16 = RightArmExtras.addOrReplaceChild("Box_r16", CubeListBuilder.create().texOffs(37, 44).addBox(-1.375F, -0.125F, -1.375F, 2.75F, 0.25F, 2.75F, new CubeDeformation(0.35F)), PartPose.offsetAndRotation(-0.4062F, 0.4626F, -1.9912F, 0.0F, 0.7854F, 0.9599F));

		PartDefinition Box_r17 = RightArmExtras.addOrReplaceChild("Box_r17", CubeListBuilder.create().texOffs(37, 44).addBox(-1.375F, -0.125F, -1.375F, 2.75F, 0.25F, 2.75F, new CubeDeformation(0.35F)), PartPose.offsetAndRotation(-0.4062F, 0.2126F, -1.9912F, 0.0F, 0.7854F, 0.9599F));

		PartDefinition Box_r18 = RightArmExtras.addOrReplaceChild("Box_r18", CubeListBuilder.create().texOffs(37, 44).addBox(-1.375F, -0.125F, -1.375F, 2.75F, 0.25F, 2.75F, new CubeDeformation(0.35F)), PartPose.offsetAndRotation(-0.4062F, -0.0374F, -1.9912F, 0.0F, 0.7854F, 0.9599F));

		PartDefinition Box_r19 = RightArmExtras.addOrReplaceChild("Box_r19", CubeListBuilder.create().texOffs(39, 44).addBox(-0.75F, -0.5F, -0.75F, 2.25F, 0.5F, 2.25F, new CubeDeformation(0.35F)), PartPose.offsetAndRotation(-0.515F, -0.4801F, -1.9912F, 0.0F, 0.7854F, 0.48F));

		PartDefinition Box_r20 = RightArmExtras.addOrReplaceChild("Box_r20", CubeListBuilder.create().texOffs(39, 44).addBox(-0.75F, -0.5F, -0.75F, 2.25F, 0.25F, 2.25F, new CubeDeformation(0.35F)), PartPose.offsetAndRotation(-0.765F, -0.7301F, -1.9912F, 0.0F, 0.7854F, 0.48F));

		PartDefinition Box_r21 = RightArmExtras.addOrReplaceChild("Box_r21", CubeListBuilder.create().texOffs(29, 41).addBox(-2.375F, -0.375F, -2.25F, 5.0F, 0.75F, 4.5F, new CubeDeformation(0.35F)), PartPose.offsetAndRotation(1.36F, -0.6051F, -1.9912F, 0.0236F, 0.0F, 0.4816F));

		PartDefinition Box_r22 = RightArmExtras.addOrReplaceChild("Box_r22", CubeListBuilder.create().texOffs(29, 41).addBox(-2.375F, -0.375F, -2.25F, 5.0F, 0.75F, 4.5F, new CubeDeformation(0.35F)), PartPose.offsetAndRotation(1.36F, -1.3551F, -1.9912F, 0.0236F, 0.0F, 0.438F));


		PartDefinition RightLegExtras = rightLeg.addOrReplaceChild("RightLegExtras", CubeListBuilder.create(), PartPose.offset(-2.225F, 1.1642F, 2.0F));

		PartDefinition Box_r23 = RightLegExtras.addOrReplaceChild("Box_r23", CubeListBuilder.create().texOffs(0, 51).addBox(-1.0F, -0.5F, -1.0F, 2.0F, 1.5F, 2.25F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.5F, 0.0F, -2.0F, 0.0F, 0.7854F, 0.7418F));

		PartDefinition Box_r24 = RightLegExtras.addOrReplaceChild("Box_r24", CubeListBuilder.create().texOffs(1, 42).addBox(-0.125F, -1.0F, -3.0F, 0.25F, 4.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -1.4142F, -2.0F, 0.7854F, 0.0F, 0.0F));

		PartDefinition rightBoot = RightLegExtras.addOrReplaceChild("rightBoot", CubeListBuilder.create().texOffs(0, 45).addBox(4.1F, 8.5F, -4.0F, 0.25F, 0.5F, 4.0F, new CubeDeformation(0.35F))
		.texOffs(1, 44).addBox(-0.15F, 8.5F, -4.0F, 0.25F, 0.5F, 4.0F, new CubeDeformation(0.35F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition Box_r25 = rightBoot.addOrReplaceChild("Box_r25", CubeListBuilder.create().texOffs(0, 45).addBox(1.875F, -0.25F, -4.25F, 0.25F, 0.5F, 4.25F, new CubeDeformation(0.35F))
		.texOffs(0, 45).addBox(-2.125F, -0.25F, -4.25F, 0.25F, 0.5F, 4.25F, new CubeDeformation(0.35F)), PartPose.offsetAndRotation(-0.025F, 8.75F, -2.0F, 0.0F, -1.5708F, 0.0F));

		PartDefinition Box_r26 = rightBoot.addOrReplaceChild("Box_r26", CubeListBuilder.create().texOffs(0, 48).addBox(-0.125F, -0.25F, -0.5F, 0.25F, 0.5F, 1.0F, new CubeDeformation(0.35F)), PartPose.offsetAndRotation(4.225F, 7.453F, 1.2433F, 1.0908F, 0.0F, 0.0F));

		PartDefinition Box_r27 = rightBoot.addOrReplaceChild("Box_r27", CubeListBuilder.create().texOffs(0, 47).addBox(-0.125F, -0.25F, -1.25F, 0.25F, 0.5F, 1.75F, new CubeDeformation(0.35F)), PartPose.offsetAndRotation(4.225F, 8.0F, 0.75F, 0.6109F, 0.0F, 0.0F));

		return LayerDefinition.create(meshdefinition, 64, 64);
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
          //  rightArm2.copyFrom(super.rightArm);

            super.setupAnim(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
        }
    }
}