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

public class AquaModel<T extends LivingEntity> extends ArmorBaseModel<T> {

    public static final ModelLayerLocation LAYER_LOCATION_TOP = new ModelLayerLocation(new ResourceLocation(KingdomKeys.MODID, "aqua_top"), "main");
    public static final ModelLayerLocation LAYER_LOCATION_BOTTOM = new ModelLayerLocation(new ResourceLocation(KingdomKeys.MODID, "aqua_bottom"), "main");

    public final ModelPart head;
    public final ModelPart body;
    public final ModelPart leftArm;
    public final ModelPart rightArm;
    public final ModelPart leftLeg;
    public final ModelPart rightLeg;  
    
    public AquaModel(ModelPart root) {
        super(root);
		this.head = root.getChild("head");
		this.body = root.getChild("body");
		this.leftArm = root.getChild("left_arm");
		this.rightArm = root.getChild("right_arm");
		this.leftLeg = root.getChild("left_leg");
		this.rightLeg = root.getChild("right_leg");
    }

    public static LayerDefinition createBodyLayer(CubeDeformation size) {
        MeshDefinition meshdefinition = new MeshDefinition(); //HumanoidModel.createMesh(size, 0);
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition head = partdefinition.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 0).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.5F)), PartPose.offset(0.0F, 0.0F, 0.0F));
		PartDefinition hat = partdefinition.addOrReplaceChild("hat", CubeListBuilder.create().texOffs(32, 0).addBox(0, 0, 0, 0, 0, 0, new CubeDeformation(0)), PartPose.offset(0.0F, 0.0F, 0.0F));
		PartDefinition body = partdefinition.addOrReplaceChild("body", CubeListBuilder.create().texOffs(16, 16).addBox(-4.0F, 0.0F, -2.0F, 8.0F, 12.0F, 4.0F, new CubeDeformation(0.5F)), PartPose.offset(0.0F, 0.0F, 0.0F));
		PartDefinition leftArm = partdefinition.addOrReplaceChild("left_arm", CubeListBuilder.create().texOffs(40, 16).addBox(-1.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.5F)), PartPose.offset(5.0F, 2.0F, 0.0F));
		PartDefinition rightArm = partdefinition.addOrReplaceChild("right_arm", CubeListBuilder.create().texOffs(32, 48).addBox(-3.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.5F)), PartPose.offset(-5.0F, 2.0F, 0.0F));
		PartDefinition leftLeg = partdefinition.addOrReplaceChild("left_leg", CubeListBuilder.create().texOffs(16, 48).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.25F)), PartPose.offset(1.9F, 12.0F, 0.0F));
		PartDefinition rightLeg = partdefinition.addOrReplaceChild("right_leg", CubeListBuilder.create().texOffs(0, 16).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.25F)), PartPose.offset(-1.9F, 12.0F, 0.0F));

		
		PartDefinition headExtras = head.addOrReplaceChild("headExtras", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition VisorLower_r1 = headExtras.addOrReplaceChild("VisorLower_r1", CubeListBuilder.create().texOffs(40, 38).addBox(-3.0F, -1.6F, -3.0F, 5.6F, 4.0F, 5.6F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -3.8F, -2.3F, 0.7333F, -0.639F, -0.493F));

		PartDefinition Visor_r1 = headExtras.addOrReplaceChild("Visor_r1", CubeListBuilder.create().texOffs(40, 33).addBox(-3.0F, -1.6F, -3.0F, 5.6F, 3.4F, 5.6F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -5.8F, -3.3F, 0.4194F, -0.7401F, -0.2921F));

		PartDefinition horn1 = headExtras.addOrReplaceChild("horn1", CubeListBuilder.create().texOffs(46, 10).addBox(0.8F, -2.0F, -1.0F, 0.2F, 1.4F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(-5.5F, -2.3F, -2.5F));

		PartDefinition cube_r1 = horn1.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(43, 7).addBox(0.0F, -0.25F, -0.9F, 0.1F, 0.2F, 4.5F, new CubeDeformation(0.0F))
		.texOffs(42, 6).addBox(0.0F, -0.45F, -0.9F, 0.1F, 0.2F, 5.1F, new CubeDeformation(0.0F))
		.texOffs(42, 6).addBox(-0.1F, -0.65F, -0.9F, 0.3F, 0.2F, 5.6F, new CubeDeformation(0.0F))
		.texOffs(44, 7).addBox(0.0F, -0.05F, -0.9F, 0.1F, 0.2F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.8F, -4.6379F, 6.0709F, -0.6545F, -0.2182F, 0.0F));

		PartDefinition cube_r2 = horn1.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(42, 6).addBox(-0.2F, -0.75F, -0.9F, 0.5F, 0.2F, 5.9F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.8F, -4.7379F, 6.0709F, -0.6545F, -0.2182F, 0.0F));

		PartDefinition cube_r3 = horn1.addOrReplaceChild("cube_r3", CubeListBuilder.create().texOffs(46, 10).addBox(-0.1F, -0.55F, 0.7F, 0.2F, 1.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.8F, -3.8379F, 4.0709F, 0.2618F, 0.0F, 0.0F));

		PartDefinition cube_r4 = horn1.addOrReplaceChild("cube_r4", CubeListBuilder.create().texOffs(44, 8).addBox(-0.2F, -0.75F, -0.9F, 0.4F, 1.5F, 3.6F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.8F, -3.8379F, 4.0709F, 0.7418F, 0.0F, 0.0F));

		PartDefinition cube_r5 = horn1.addOrReplaceChild("cube_r5", CubeListBuilder.create().texOffs(44, 8).addBox(-0.2F, -0.75F, -0.9F, 0.4F, 1.5F, 3.9F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.8F, -2.7379F, 2.0709F, 0.2618F, 0.0F, 0.0F));

		PartDefinition cube_r6 = horn1.addOrReplaceChild("cube_r6", CubeListBuilder.create().texOffs(45, 9).addBox(-0.2F, -0.75F, -1.5F, 0.4F, 1.5F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.8F, -1.7379F, 2.0709F, 0.48F, 0.0F, 0.0F));

		PartDefinition cube_r7 = horn1.addOrReplaceChild("cube_r7", CubeListBuilder.create().texOffs(44, 8).addBox(0.1F, -0.75F, -1.8F, 0.1F, 1.5F, 3.6F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.8F, 0.2557F, 0.9411F, -1.789F, 0.0F, 0.0F));

		PartDefinition horn2 = headExtras.addOrReplaceChild("horn2", CubeListBuilder.create().texOffs(46, 10).mirror().addBox(-1.0F, -2.0F, -1.0F, 0.2F, 1.4F, 2.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(5.5F, -2.3F, -2.5F));

		PartDefinition cube_r8 = horn2.addOrReplaceChild("cube_r8", CubeListBuilder.create().texOffs(43, 7).mirror().addBox(-0.1F, -0.25F, -0.9F, 0.1F, 0.2F, 4.5F, new CubeDeformation(0.0F)).mirror(false)
		.texOffs(42, 6).mirror().addBox(-0.1F, -0.45F, -0.9F, 0.1F, 0.2F, 5.1F, new CubeDeformation(0.0F)).mirror(false)
		.texOffs(42, 6).mirror().addBox(-0.2F, -0.65F, -0.9F, 0.3F, 0.2F, 5.6F, new CubeDeformation(0.0F)).mirror(false)
		.texOffs(44, 7).mirror().addBox(-0.1F, -0.05F, -0.9F, 0.1F, 0.2F, 4.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-0.8F, -4.6379F, 6.0709F, -0.6545F, 0.2182F, 0.0F));

		PartDefinition cube_r9 = horn2.addOrReplaceChild("cube_r9", CubeListBuilder.create().texOffs(42, 6).mirror().addBox(-0.3F, -0.75F, -0.9F, 0.5F, 0.2F, 5.9F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-0.8F, -4.7379F, 6.0709F, -0.6545F, 0.2182F, 0.0F));

		PartDefinition cube_r10 = horn2.addOrReplaceChild("cube_r10", CubeListBuilder.create().texOffs(46, 10).mirror().addBox(-0.1F, -0.55F, 0.7F, 0.2F, 1.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-0.8F, -3.8379F, 4.0709F, 0.2618F, 0.0F, 0.0F));

		PartDefinition cube_r11 = horn2.addOrReplaceChild("cube_r11", CubeListBuilder.create().texOffs(44, 8).mirror().addBox(-0.2F, -0.75F, -0.9F, 0.4F, 1.5F, 3.6F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-0.8F, -3.8379F, 4.0709F, 0.7418F, 0.0F, 0.0F));

		PartDefinition cube_r12 = horn2.addOrReplaceChild("cube_r12", CubeListBuilder.create().texOffs(44, 8).mirror().addBox(-0.2F, -0.75F, -0.9F, 0.4F, 1.5F, 3.9F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-0.8F, -2.7379F, 2.0709F, 0.2618F, 0.0F, 0.0F));

		PartDefinition cube_r13 = horn2.addOrReplaceChild("cube_r13", CubeListBuilder.create().texOffs(45, 9).mirror().addBox(-0.2F, -0.75F, -1.5F, 0.4F, 1.5F, 3.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-0.8F, -1.7379F, 2.0709F, 0.48F, 0.0F, 0.0F));

		PartDefinition cube_r14 = horn2.addOrReplaceChild("cube_r14", CubeListBuilder.create().texOffs(44, 8).mirror().addBox(-0.2F, -0.75F, -1.8F, 0.1F, 1.5F, 3.6F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-0.8F, 0.2557F, 0.9411F, -1.789F, 0.0F, 0.0F));


		PartDefinition bodyExtras = body.addOrReplaceChild("bodyExtras", CubeListBuilder.create().texOffs(58, 5).addBox(-1.7F, 11.75F, -2.75F, 3.5F, 1.2F, 0.25F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition cube_r15 = bodyExtras.addOrReplaceChild("cube_r15", CubeListBuilder.create().texOffs(58, 5).mirror().addBox(-1.0F, -0.6F, -0.125F, 2.0F, 1.2F, 0.25F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-0.55F, 12.35F, -2.625F, 0.0F, 0.0F, -0.7418F));

		PartDefinition cube_r16 = bodyExtras.addOrReplaceChild("cube_r16", CubeListBuilder.create().texOffs(58, 5).addBox(-1.0F, -0.6F, -0.125F, 2.0F, 1.2F, 0.25F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.55F, 12.35F, -2.625F, 0.0F, 0.0F, 0.7418F));

		PartDefinition cube_r17 = bodyExtras.addOrReplaceChild("cube_r17", CubeListBuilder.create().texOffs(58, 5).addBox(-1.0F, -2.0F, -0.75F, 2.0F, 2.0F, 0.25F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.75F, 12.75F, -2.0F, 0.0F, 0.0F, -0.7854F));

		PartDefinition cube_r18 = bodyExtras.addOrReplaceChild("cube_r18", CubeListBuilder.create().texOffs(51, 1).addBox(-2.0F, -0.9F, -0.05F, 3.0F, 1.4F, 0.55F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(3.5F, 4.0F, -3.0F, 0.0F, 0.0F, 1.2217F));

		PartDefinition cube_r19 = bodyExtras.addOrReplaceChild("cube_r19", CubeListBuilder.create().texOffs(51, 1).mirror().addBox(-1.0F, -0.9F, -0.05F, 3.0F, 1.4F, 0.55F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-3.5F, 4.0F, -3.0F, 0.0F, 0.0F, -1.2217F));

		PartDefinition cube_r20 = bodyExtras.addOrReplaceChild("cube_r20", CubeListBuilder.create().texOffs(51, 1).mirror().addBox(-2.0F, -1.0F, -0.5F, 4.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-1.5F, 2.0F, -3.0F, -0.0149F, 0.041F, -0.3494F));

		PartDefinition cube_r21 = bodyExtras.addOrReplaceChild("cube_r21", CubeListBuilder.create().texOffs(51, 1).addBox(-2.0F, -1.0F, -0.5F, 4.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.5F, 2.0F, -3.0F, -0.0149F, -0.041F, 0.3494F));


		PartDefinition leftArmExtras = leftArm.addOrReplaceChild("leftArmExtras", CubeListBuilder.create().texOffs(58, 9).addBox(3.5F, -2.0F, -1.25F, 0.25F, 4.25F, 2.5F, new CubeDeformation(0.0F))
		.texOffs(36, 4).addBox(-1.7F, 5.0F, -2.6F, 5.3F, 5.0F, 5.2F, new CubeDeformation(0.0F))
		.texOffs(36, 8).addBox(-1.8F, 8.5F, -2.7F, 5.5F, 1.5F, 5.4F, new CubeDeformation(0.0F))
		.texOffs(36, 8).addBox(-1.8F, 4.95F, -2.7F, 5.5F, 1.5F, 5.4F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition cube_r22 = leftArmExtras.addOrReplaceChild("cube_r22", CubeListBuilder.create().texOffs(44, 1).addBox(-0.65F, -0.7F, -0.5061F, 1.2F, 1.0F, 1.2F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(3.5697F, 7.2F, 0.1061F, -3.1416F, -0.7854F, -2.8362F));

		PartDefinition cube_r23 = leftArmExtras.addOrReplaceChild("cube_r23", CubeListBuilder.create().texOffs(44, 1).addBox(-0.65F, -0.9F, -0.5061F, 1.2F, 1.4F, 1.2F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(3.4697F, 7.5F, 0.1061F, -3.1416F, -0.7854F, -2.4871F));

		PartDefinition cube_r24 = leftArmExtras.addOrReplaceChild("cube_r24", CubeListBuilder.create().texOffs(44, 1).addBox(-0.4F, -1.0F, -0.4F, 1.4F, 1.5F, 1.4F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(4.0F, 6.5F, 0.0F, -3.1416F, -0.7854F, 2.9671F));

		PartDefinition cube_r25 = leftArmExtras.addOrReplaceChild("cube_r25", CubeListBuilder.create().texOffs(36, 6).addBox(-0.1F, -0.9F, 0.1F, 0.25F, 0.8F, 0.8F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(3.9F, 4.6F, 0.0F, 0.7854F, 0.0F, 2.6878F));

		PartDefinition cube_r26 = leftArmExtras.addOrReplaceChild("cube_r26", CubeListBuilder.create().texOffs(35, 5).addBox(0.3F, -0.7F, -0.8F, 0.05F, 1.5F, 1.5F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(4.3F, 4.1F, 0.0F, 0.7854F, 0.0F, -3.1154F));

		PartDefinition cube_r27 = leftArmExtras.addOrReplaceChild("cube_r27", CubeListBuilder.create().texOffs(35, 5).addBox(0.3F, -0.9F, -1.0F, 0.05F, 1.9F, 1.9F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(4.3F, 4.7F, 0.0F, 0.7854F, 0.0F, -3.1154F));

		PartDefinition cube_r28 = leftArmExtras.addOrReplaceChild("cube_r28", CubeListBuilder.create().texOffs(34, 4).addBox(0.0F, -2.0F, -1.0F, 0.55F, 3.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(4.0F, 6.0F, 0.0F, 0.7854F, 0.0F, -3.0805F));

		PartDefinition Gem4_r1 = leftArmExtras.addOrReplaceChild("Gem4_r1", CubeListBuilder.create().texOffs(52, 4).addBox(-0.15F, -0.625F, -0.75F, 0.9F, 1.25F, 0.9F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(3.6464F, -0.775F, 0.0F, 0.0F, -0.7854F, -1.0385F));

		PartDefinition Gem3_r1 = leftArmExtras.addOrReplaceChild("Gem3_r1", CubeListBuilder.create().texOffs(51, 4).addBox(-1.0F, -0.75F, -0.45F, 1.45F, 2.05F, 1.45F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(3.6F, 1.75F, 0.0F, 0.0F, -0.7854F, 0.4363F));

		PartDefinition Gem1_r1 = leftArmExtras.addOrReplaceChild("Gem1_r1", CubeListBuilder.create().texOffs(51, 4).addBox(-1.0F, -0.75F, -0.45F, 1.45F, 1.45F, 1.45F, new CubeDeformation(0.0F))
		.texOffs(51, 4).addBox(-1.0F, -2.1F, -0.5F, 1.5F, 1.35F, 1.5F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(4.0F, 0.75F, 0.0F, 0.0F, -0.7854F, 0.0F));

		PartDefinition shoulderPlate_r1 = leftArmExtras.addOrReplaceChild("shoulderPlate_r1", CubeListBuilder.create().texOffs(58, 9).addBox(-0.125F, -0.625F, -1.25F, 0.15F, 1.25F, 2.5F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(3.625F, -0.675F, 1.75F, -0.7418F, 0.0F, 0.0F));

		PartDefinition shoulderPlate_r2 = leftArmExtras.addOrReplaceChild("shoulderPlate_r2", CubeListBuilder.create().texOffs(58, 9).addBox(-0.125F, -0.625F, -1.25F, 0.15F, 0.95F, 2.5F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(3.625F, 0.975F, 2.5F, -1.5708F, -0.4102F, 0.0F));

		PartDefinition shoulderPlate_r3 = leftArmExtras.addOrReplaceChild("shoulderPlate_r3", CubeListBuilder.create().texOffs(58, 9).addBox(-0.125F, -0.625F, -1.25F, 0.25F, 0.95F, 2.5F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(3.625F, 0.975F, -2.3F, 1.5708F, 0.4102F, 0.0F));

		PartDefinition shoulderPlate_r4 = leftArmExtras.addOrReplaceChild("shoulderPlate_r4", CubeListBuilder.create().texOffs(58, 9).addBox(-0.125F, -0.625F, -1.25F, 0.25F, 1.25F, 2.5F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(3.625F, -0.675F, -1.75F, 0.7418F, 0.0F, 0.0F));

		PartDefinition shoulderPlate_r5 = leftArmExtras.addOrReplaceChild("shoulderPlate_r5", CubeListBuilder.create().texOffs(58, 9).addBox(-0.875F, -1.125F, -0.75F, 0.8F, 1.75F, 1.75F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(3.725F, 2.525F, 0.1F, 0.7854F, 0.0F, 0.0F));


		PartDefinition rightArmExtras = rightArm.addOrReplaceChild("rightArmExtras", CubeListBuilder.create().texOffs(58, 9).mirror().addBox(-3.75F, -2.0F, -1.25F, 0.25F, 4.25F, 2.5F, new CubeDeformation(0.0F)).mirror(false)
		.texOffs(36, 4).mirror().addBox(-3.6F, 5.0F, -2.6F, 5.3F, 5.0F, 5.2F, new CubeDeformation(0.0F)).mirror(false)
		.texOffs(36, 8).mirror().addBox(-3.7F, 8.5F, -2.7F, 5.5F, 1.5F, 5.4F, new CubeDeformation(0.0F)).mirror(false)
		.texOffs(36, 8).mirror().addBox(-3.7F, 4.95F, -2.7F, 5.5F, 1.5F, 5.4F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition cube_r29 = rightArmExtras.addOrReplaceChild("cube_r29", CubeListBuilder.create().texOffs(44, 1).mirror().addBox(-0.55F, -0.7F, -0.5061F, 1.2F, 1.0F, 1.2F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-3.5697F, 7.2F, 0.1061F, -3.1416F, 0.7854F, 2.8362F));

		PartDefinition cube_r30 = rightArmExtras.addOrReplaceChild("cube_r30", CubeListBuilder.create().texOffs(44, 1).mirror().addBox(-0.55F, -0.9F, -0.5061F, 1.2F, 1.4F, 1.2F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-3.4697F, 7.5F, 0.1061F, -3.1416F, 0.7854F, 2.4871F));

		PartDefinition cube_r31 = rightArmExtras.addOrReplaceChild("cube_r31", CubeListBuilder.create().texOffs(44, 1).mirror().addBox(-1.0F, -1.0F, -0.4F, 1.4F, 1.5F, 1.4F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-4.0F, 6.5F, 0.0F, -3.1416F, 0.7854F, -2.9671F));

		PartDefinition cube_r32 = rightArmExtras.addOrReplaceChild("cube_r32", CubeListBuilder.create().texOffs(36, 6).mirror().addBox(-0.15F, -0.9F, 0.1F, 0.25F, 0.8F, 0.8F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-3.9F, 4.6F, 0.0F, 0.7854F, 0.0F, -2.6878F));

		PartDefinition cube_r33 = rightArmExtras.addOrReplaceChild("cube_r33", CubeListBuilder.create().texOffs(35, 5).mirror().addBox(-0.35F, -0.7F, -0.8F, 0.05F, 1.5F, 1.5F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-4.3F, 4.1F, 0.0F, 0.7854F, 0.0F, 3.1154F));

		PartDefinition cube_r34 = rightArmExtras.addOrReplaceChild("cube_r34", CubeListBuilder.create().texOffs(35, 5).mirror().addBox(-0.35F, -0.9F, -1.0F, 0.05F, 1.9F, 1.9F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-4.3F, 4.7F, 0.0F, 0.7854F, 0.0F, 3.1154F));

		PartDefinition cube_r35 = rightArmExtras.addOrReplaceChild("cube_r35", CubeListBuilder.create().texOffs(34, 4).mirror().addBox(-0.55F, -2.0F, -1.0F, 0.55F, 3.0F, 3.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-4.0F, 6.0F, 0.0F, 0.7854F, 0.0F, 3.0805F));

		PartDefinition Gem5_r1 = rightArmExtras.addOrReplaceChild("Gem5_r1", CubeListBuilder.create().texOffs(52, 4).mirror().addBox(-0.75F, -0.625F, -0.75F, 0.9F, 1.25F, 0.9F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-3.6464F, -0.775F, 0.0F, 0.0F, 0.7854F, 1.0385F));

		PartDefinition Gem4_r2 = rightArmExtras.addOrReplaceChild("Gem4_r2", CubeListBuilder.create().texOffs(51, 4).mirror().addBox(-0.45F, -0.75F, -0.45F, 1.45F, 2.05F, 1.45F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-3.6F, 1.75F, 0.0F, 0.0F, 0.7854F, -0.4363F));

		PartDefinition Gem2_r1 = rightArmExtras.addOrReplaceChild("Gem2_r1", CubeListBuilder.create().texOffs(51, 4).mirror().addBox(-0.45F, -0.75F, -0.45F, 1.45F, 1.45F, 1.45F, new CubeDeformation(0.0F)).mirror(false)
		.texOffs(51, 4).mirror().addBox(-0.5F, -2.1F, -0.5F, 1.5F, 1.35F, 1.5F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-4.0F, 0.75F, 0.0F, 0.0F, 0.7854F, 0.0F));

		PartDefinition shoulderPlate_r6 = rightArmExtras.addOrReplaceChild("shoulderPlate_r6", CubeListBuilder.create().texOffs(58, 9).mirror().addBox(-0.025F, -0.625F, -1.25F, 0.15F, 1.25F, 2.5F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-3.625F, -0.675F, 1.75F, -0.7418F, 0.0F, 0.0F));

		PartDefinition shoulderPlate_r7 = rightArmExtras.addOrReplaceChild("shoulderPlate_r7", CubeListBuilder.create().texOffs(58, 9).mirror().addBox(-0.025F, -0.625F, -1.25F, 0.15F, 0.95F, 2.5F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-3.625F, 0.975F, 2.5F, -1.5708F, 0.4102F, 0.0F));

		PartDefinition shoulderPlate_r8 = rightArmExtras.addOrReplaceChild("shoulderPlate_r8", CubeListBuilder.create().texOffs(58, 9).mirror().addBox(-0.125F, -0.625F, -1.25F, 0.25F, 0.95F, 2.5F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-3.625F, 0.975F, -2.3F, 1.5708F, -0.4102F, 0.0F));

		PartDefinition shoulderPlate_r9 = rightArmExtras.addOrReplaceChild("shoulderPlate_r9", CubeListBuilder.create().texOffs(58, 9).mirror().addBox(-0.125F, -0.625F, -1.25F, 0.25F, 1.25F, 2.5F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-3.625F, -0.675F, -1.75F, 0.7418F, 0.0F, 0.0F));

		PartDefinition shoulderPlate_r10 = rightArmExtras.addOrReplaceChild("shoulderPlate_r10", CubeListBuilder.create().texOffs(58, 9).mirror().addBox(0.075F, -1.125F, -0.75F, 0.8F, 1.75F, 1.75F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-3.725F, 2.525F, 0.1F, 0.7854F, 0.0F, 0.0F));


		PartDefinition legExtras = leftLeg.addOrReplaceChild("legExtras", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition UpperLeg = legExtras.addOrReplaceChild("UpperLeg", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition cube_r36 = UpperLeg.addOrReplaceChild("cube_r36", CubeListBuilder.create().texOffs(-1, 34).addBox(-1.05F, -0.1F, -1.05F, 2.1F, 0.2F, 2.1F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.6638F, 3.1366F, 0.0F, 3.1416F, -0.7854F, -2.0159F));

		PartDefinition cube_r37 = UpperLeg.addOrReplaceChild("cube_r37", CubeListBuilder.create().texOffs(-1, 34).addBox(-1.05F, -0.1F, -1.05F, 2.1F, 0.2F, 2.1F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.6638F, 3.1366F, 0.0F, 3.1416F, -0.7854F, -2.1031F));

		PartDefinition cube_r38 = UpperLeg.addOrReplaceChild("cube_r38", CubeListBuilder.create().texOffs(-1, 34).addBox(-1.05F, -0.1F, -1.05F, 2.1F, 0.2F, 2.1F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.6638F, 3.1366F, 0.0F, 3.1416F, -0.7854F, -2.2078F));

		PartDefinition cube_r39 = UpperLeg.addOrReplaceChild("cube_r39", CubeListBuilder.create().texOffs(-1, 34).addBox(-1.05F, -0.1F, -1.05F, 2.1F, 0.2F, 2.1F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.6638F, 3.1366F, 0.0F, 3.1416F, -0.7854F, -2.2951F));

		PartDefinition cube_r40 = UpperLeg.addOrReplaceChild("cube_r40", CubeListBuilder.create().texOffs(-1, 34).addBox(-1.05F, -0.1F, -1.05F, 2.1F, 0.2F, 2.1F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.6638F, 3.1366F, 0.0F, -3.1416F, -0.7854F, -2.4173F));

		PartDefinition cube_r41 = UpperLeg.addOrReplaceChild("cube_r41", CubeListBuilder.create().texOffs(-1, 34).addBox(-1.05F, -0.1F, -1.05F, 2.1F, 0.2F, 2.1F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.6638F, 3.1366F, 0.0F, 3.1416F, -0.7854F, -2.5569F));

		PartDefinition cube_r42 = UpperLeg.addOrReplaceChild("cube_r42", CubeListBuilder.create().texOffs(-1, 34).addBox(-1.05F, -0.1F, -1.05F, 2.1F, 0.2F, 2.1F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.6638F, 3.1366F, 0.0F, -3.1416F, -0.7854F, -2.6965F));

		PartDefinition cube_r43 = UpperLeg.addOrReplaceChild("cube_r43", CubeListBuilder.create().texOffs(-1, 34).addBox(-1.05F, -0.1F, -1.05F, 2.1F, 0.2F, 2.1F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.6638F, 3.1366F, 0.0F, -3.1416F, -0.7854F, -2.8187F));

		PartDefinition cube_r44 = UpperLeg.addOrReplaceChild("cube_r44", CubeListBuilder.create().texOffs(-1, 34).addBox(-1.05F, -0.1F, -1.05F, 2.1F, 0.2F, 2.1F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.6638F, 3.1366F, 0.0F, -3.1416F, -0.7854F, -2.9409F));

		PartDefinition cube_r45 = UpperLeg.addOrReplaceChild("cube_r45", CubeListBuilder.create().texOffs(-1, 34).addBox(-1.1F, -1.0F, -1.1F, 2.1F, 0.2F, 2.1F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.5F, 3.8F, 0.0F, -3.1416F, -0.7854F, -2.9234F));

		PartDefinition cube_r46 = UpperLeg.addOrReplaceChild("cube_r46", CubeListBuilder.create().texOffs(0, 35).addBox(-1.0F, -1.4F, -1.0F, 2.0F, 1.5F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.9F, 2.8F, 0.0F, 3.1416F, -0.7854F, 2.522F));

		PartDefinition cube_r47 = UpperLeg.addOrReplaceChild("cube_r47", CubeListBuilder.create().texOffs(0, 35).addBox(-1.0F, -1.0F, -1.0F, 2.0F, 1.1F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.9F, 2.8F, 0.0F, -3.1416F, -0.7854F, -2.9234F));

		PartDefinition LowerLeg = legExtras.addOrReplaceChild("LowerLeg", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition cube_r48 = LowerLeg.addOrReplaceChild("cube_r48", CubeListBuilder.create().texOffs(21, 36).mirror().addBox(-0.7F, -0.5F, -0.15F, 1.5F, 0.3F, 0.3F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(3.2351F, 9.1509F, 2.6006F, -0.8479F, -0.2561F, 1.3898F));

		PartDefinition cube_r49 = LowerLeg.addOrReplaceChild("cube_r49", CubeListBuilder.create().texOffs(21, 36).mirror().addBox(-0.75F, -0.25F, -0.15F, 0.8F, 0.4F, 0.3F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(3.35F, 8.75F, 2.75F, -0.3776F, -0.7926F, 0.5545F));

		PartDefinition cube_r50 = LowerLeg.addOrReplaceChild("cube_r50", CubeListBuilder.create().texOffs(21, 36).mirror().addBox(-1.2F, -0.5F, 0.7F, 1.5F, 0.5F, 0.3F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(3.6F, 9.0F, 1.9F, 0.7148F, -0.8701F, -0.5857F));

		PartDefinition Ankle_r1 = LowerLeg.addOrReplaceChild("Ankle_r1", CubeListBuilder.create().texOffs(16, 32).addBox(-2.4F, -1.0F, -5.7F, 4.8F, 1.0F, 4.9F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 9.6F, 3.3F, -0.0524F, 0.0F, 0.0F));


		PartDefinition LowerLeg2 = rightLeg.addOrReplaceChild("LowerLeg2", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition cube_r51 = LowerLeg2.addOrReplaceChild("cube_r51", CubeListBuilder.create().texOffs(21, 36).addBox(-0.8F, -0.5F, -0.15F, 1.5F, 0.3F, 0.3F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-3.2351F, 9.1509F, 2.6006F, -0.8479F, 0.2561F, -1.3898F));

		PartDefinition cube_r52 = LowerLeg2.addOrReplaceChild("cube_r52", CubeListBuilder.create().texOffs(21, 36).addBox(-0.05F, -0.25F, -0.15F, 0.8F, 0.4F, 0.3F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-3.35F, 8.75F, 2.75F, -0.3776F, 0.7926F, -0.5545F));

		PartDefinition cube_r53 = LowerLeg2.addOrReplaceChild("cube_r53", CubeListBuilder.create().texOffs(21, 36).addBox(-0.3F, -0.5F, 0.7F, 1.5F, 0.5F, 0.3F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-3.6F, 9.0F, 1.9F, 0.7148F, 0.8701F, 0.5857F));

		PartDefinition Ankle_r2 = LowerLeg2.addOrReplaceChild("Ankle_r2", CubeListBuilder.create().texOffs(16, 32).mirror().addBox(-2.4F, -1.0F, -5.7F, 4.8F, 1.0F, 4.9F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.0F, 9.6F, 3.3F, -0.0524F, 0.0F, 0.0F));

		PartDefinition UpperLeg2 = rightLeg.addOrReplaceChild("UpperLeg2", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition cube_r54 = UpperLeg2.addOrReplaceChild("cube_r54", CubeListBuilder.create().texOffs(-1, 34).mirror().addBox(-1.05F, -0.1F, -1.05F, 2.1F, 0.2F, 2.1F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-1.6638F, 3.1366F, 0.0F, 3.1416F, 0.7854F, 2.0159F));

		PartDefinition cube_r55 = UpperLeg2.addOrReplaceChild("cube_r55", CubeListBuilder.create().texOffs(-1, 34).mirror().addBox(-1.05F, -0.1F, -1.05F, 2.1F, 0.2F, 2.1F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-1.6638F, 3.1366F, 0.0F, 3.1416F, 0.7854F, 2.1031F));

		PartDefinition cube_r56 = UpperLeg2.addOrReplaceChild("cube_r56", CubeListBuilder.create().texOffs(-1, 34).mirror().addBox(-1.05F, -0.1F, -1.05F, 2.1F, 0.2F, 2.1F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-1.6638F, 3.1366F, 0.0F, 3.1416F, 0.7854F, 2.2078F));

		PartDefinition cube_r57 = UpperLeg2.addOrReplaceChild("cube_r57", CubeListBuilder.create().texOffs(-1, 34).mirror().addBox(-1.05F, -0.1F, -1.05F, 2.1F, 0.2F, 2.1F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-1.6638F, 3.1366F, 0.0F, 3.1416F, 0.7854F, 2.2951F));

		PartDefinition cube_r58 = UpperLeg2.addOrReplaceChild("cube_r58", CubeListBuilder.create().texOffs(-1, 34).mirror().addBox(-1.05F, -0.1F, -1.05F, 2.1F, 0.2F, 2.1F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-1.6638F, 3.1366F, 0.0F, -3.1416F, 0.7854F, 2.4173F));

		PartDefinition cube_r59 = UpperLeg2.addOrReplaceChild("cube_r59", CubeListBuilder.create().texOffs(-1, 34).mirror().addBox(-1.05F, -0.1F, -1.05F, 2.1F, 0.2F, 2.1F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-1.6638F, 3.1366F, 0.0F, 3.1416F, 0.7854F, 2.5569F));

		PartDefinition cube_r60 = UpperLeg2.addOrReplaceChild("cube_r60", CubeListBuilder.create().texOffs(-1, 34).mirror().addBox(-1.05F, -0.1F, -1.05F, 2.1F, 0.2F, 2.1F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-1.6638F, 3.1366F, 0.0F, -3.1416F, 0.7854F, 2.6965F));

		PartDefinition cube_r61 = UpperLeg2.addOrReplaceChild("cube_r61", CubeListBuilder.create().texOffs(-1, 34).mirror().addBox(-1.05F, -0.1F, -1.05F, 2.1F, 0.2F, 2.1F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-1.6638F, 3.1366F, 0.0F, -3.1416F, 0.7854F, 2.8187F));

		PartDefinition cube_r62 = UpperLeg2.addOrReplaceChild("cube_r62", CubeListBuilder.create().texOffs(-1, 34).mirror().addBox(-1.05F, -0.1F, -1.05F, 2.1F, 0.2F, 2.1F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-1.6638F, 3.1366F, 0.0F, -3.1416F, 0.7854F, 2.9409F));

		PartDefinition cube_r63 = UpperLeg2.addOrReplaceChild("cube_r63", CubeListBuilder.create().texOffs(-1, 34).mirror().addBox(-1.0F, -1.0F, -1.1F, 2.1F, 0.2F, 2.1F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-1.5F, 3.8F, 0.0F, -3.1416F, 0.7854F, 2.9234F));

		PartDefinition cube_r64 = UpperLeg2.addOrReplaceChild("cube_r64", CubeListBuilder.create().texOffs(0, 35).mirror().addBox(-1.0F, -1.4F, -1.0F, 2.0F, 1.5F, 2.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-1.9F, 2.8F, 0.0F, 3.1416F, 0.7854F, -2.522F));

		PartDefinition cube_r65 = UpperLeg2.addOrReplaceChild("cube_r65", CubeListBuilder.create().texOffs(0, 35).mirror().addBox(-1.0F, -1.0F, -1.0F, 2.0F, 1.1F, 2.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-1.9F, 2.8F, 0.0F, -3.1416F, 0.7854F, 2.9234F));

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