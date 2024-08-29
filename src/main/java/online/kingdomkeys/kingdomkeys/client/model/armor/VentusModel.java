package online.kingdomkeys.kingdomkeys.client.model.armor;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.decoration.ArmorStand;
import online.kingdomkeys.kingdomkeys.KingdomKeys;

public class VentusModel<T extends LivingEntity> extends ArmorBaseModel<T> {

    public static final ModelLayerLocation LAYER_LOCATION_TOP = new ModelLayerLocation(ResourceLocation.fromNamespaceAndPath(KingdomKeys.MODID, "ventus_top"), "main");
    public static final ModelLayerLocation LAYER_LOCATION_BOTTOM = new ModelLayerLocation(ResourceLocation.fromNamespaceAndPath(KingdomKeys.MODID, "ventus_bottom"), "main");

    public final ModelPart head;
    public final ModelPart body;
    public final ModelPart leftArm;
    public final ModelPart rightArm;
    public final ModelPart leftLeg;
    public final ModelPart rightLeg;  
    
    public VentusModel(ModelPart root) {
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

        
        PartDefinition extras = head.addOrReplaceChild("extras", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition Visor4_r1 = extras.addOrReplaceChild("Visor4_r1", CubeListBuilder.create().texOffs(42, 8).addBox(-2.75F, -1.15F, -2.5F, 5.4F, 1.5F, 5.3F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.1354F, -4.3114F, -3.2999F, 1.2181F, 0.3326F, 0.7256F));

		PartDefinition Visor3_r1 = extras.addOrReplaceChild("Visor3_r1", CubeListBuilder.create().texOffs(41, 8).addBox(-2.75F, -1.25F, -2.8F, 5.5F, 1.6F, 5.6F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.0354F, -4.3114F, -3.2999F, 0.9136F, 0.5484F, 0.5942F));

		PartDefinition Visor2_r1 = extras.addOrReplaceChild("Visor2_r1", CubeListBuilder.create().texOffs(40, 7).addBox(-5.0F, 1.0F, -1.0F, 6.0F, 2.3F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -6.0F, -7.1F, 0.4194F, 0.7401F, 0.2921F));

		PartDefinition Visor1_r1 = extras.addOrReplaceChild("Visor1_r1", CubeListBuilder.create().texOffs(40, 0).addBox(-5.0F, -1.0F, -1.0F, 6.0F, 4.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -7.0F, -7.0F, 0.0617F, 0.7844F, 0.0436F));

		PartDefinition hornL = extras.addOrReplaceChild("hornL", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition cube_r1 = hornL.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(57, 19).addBox(-0.05F, -0.25F, 0.25F, 0.1F, 1.1F, 0.1F, new CubeDeformation(0.1F)), PartPose.offsetAndRotation(4.65F, -10.6848F, 3.7185F, -3.1154F, 0.0F, 0.0F));

		PartDefinition cube_r2 = hornL.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(57, 19).addBox(-0.05F, -0.45F, 0.25F, 0.1F, 1.5F, 0.1F, new CubeDeformation(0.1F)), PartPose.offsetAndRotation(4.65F, -10.6848F, 3.6185F, -3.1154F, 0.0F, 0.0F));

		PartDefinition cube_r3 = hornL.addOrReplaceChild("cube_r3", CubeListBuilder.create().texOffs(57, 19).addBox(-0.05F, -0.65F, 0.25F, 0.1F, 1.9F, 0.1F, new CubeDeformation(0.1F)), PartPose.offsetAndRotation(4.65F, -10.6848F, 3.5185F, -3.1154F, 0.0F, 0.0F));

		PartDefinition cube_r4 = hornL.addOrReplaceChild("cube_r4", CubeListBuilder.create().texOffs(57, 19).addBox(-0.05F, -0.75F, 0.25F, 0.1F, 2.2F, 0.1F, new CubeDeformation(0.1F)), PartPose.offsetAndRotation(4.65F, -10.6848F, 3.4185F, -3.1154F, 0.0F, 0.0F));

		PartDefinition cube_r5 = hornL.addOrReplaceChild("cube_r5", CubeListBuilder.create().texOffs(57, 19).addBox(-0.05F, -0.85F, 0.25F, 0.1F, 2.3F, 0.1F, new CubeDeformation(0.1F)), PartPose.offsetAndRotation(4.65F, -10.7848F, 3.3185F, -3.1154F, 0.0F, 0.0F));

		PartDefinition cube_r6 = hornL.addOrReplaceChild("cube_r6", CubeListBuilder.create().texOffs(57, 19).addBox(-0.05F, -0.85F, 0.25F, 0.1F, 2.5F, 0.1F, new CubeDeformation(0.1F)), PartPose.offsetAndRotation(4.65F, -10.7848F, 3.2185F, -3.1154F, 0.0F, 0.0F));

		PartDefinition cube_r7 = hornL.addOrReplaceChild("cube_r7", CubeListBuilder.create().texOffs(57, 19).addBox(-0.05F, -0.85F, 0.25F, 0.1F, 2.7F, 0.1F, new CubeDeformation(0.1F)), PartPose.offsetAndRotation(4.65F, -10.7848F, 3.1185F, -3.1154F, 0.0F, 0.0F));

		PartDefinition cube_r8 = hornL.addOrReplaceChild("cube_r8", CubeListBuilder.create().texOffs(57, 19).addBox(-0.05F, -0.85F, 0.25F, 0.1F, 2.9F, 0.1F, new CubeDeformation(0.1F)), PartPose.offsetAndRotation(4.65F, -10.7848F, 3.0185F, -3.1154F, 0.0F, 0.0F));

		PartDefinition cube_r9 = hornL.addOrReplaceChild("cube_r9", CubeListBuilder.create().texOffs(57, 19).addBox(-0.05F, -0.85F, 0.25F, 0.1F, 3.1F, 0.1F, new CubeDeformation(0.1F)), PartPose.offsetAndRotation(4.65F, -10.7848F, 2.9185F, -3.1154F, 0.0F, 0.0F));

		PartDefinition cube_r10 = hornL.addOrReplaceChild("cube_r10", CubeListBuilder.create().texOffs(57, 19).addBox(-0.05F, -0.85F, 0.25F, 0.1F, 3.3F, 0.1F, new CubeDeformation(0.1F)), PartPose.offsetAndRotation(4.65F, -10.7848F, 2.8185F, -3.1154F, 0.0F, 0.0F));

		PartDefinition cube_r11 = hornL.addOrReplaceChild("cube_r11", CubeListBuilder.create().texOffs(57, 19).addBox(-0.05F, -0.85F, 0.25F, 0.1F, 3.5F, 0.1F, new CubeDeformation(0.1F)), PartPose.offsetAndRotation(4.65F, -10.7848F, 2.7185F, -3.1154F, 0.0F, 0.0F));

		PartDefinition cube_r12 = hornL.addOrReplaceChild("cube_r12", CubeListBuilder.create().texOffs(57, 19).addBox(-0.15F, -0.85F, 0.25F, 0.3F, 3.6F, 0.1F, new CubeDeformation(0.1F)), PartPose.offsetAndRotation(4.65F, -10.7848F, 2.6185F, -3.1154F, 0.0F, 0.0F));

		PartDefinition cube_r13 = hornL.addOrReplaceChild("cube_r13", CubeListBuilder.create().texOffs(57, 19).addBox(-0.15F, -0.85F, 0.25F, 0.3F, 3.8F, 0.1F, new CubeDeformation(0.1F)), PartPose.offsetAndRotation(4.65F, -10.7848F, 2.5185F, -3.1154F, 0.0F, 0.0F));

		PartDefinition cube_r14 = hornL.addOrReplaceChild("cube_r14", CubeListBuilder.create().texOffs(56, 18).addBox(-0.15F, -0.85F, -0.65F, 0.3F, 3.7F, 1.5F, new CubeDeformation(0.1F)), PartPose.offsetAndRotation(4.65F, -8.5848F, 0.6185F, 2.2951F, 0.0F, 0.0F));

		PartDefinition cube_r15 = hornL.addOrReplaceChild("cube_r15", CubeListBuilder.create().texOffs(56, 18).addBox(-0.15F, -0.85F, -0.5F, 0.3F, 2.7F, 1.3F, new CubeDeformation(0.1F)), PartPose.offsetAndRotation(4.65F, -8.4348F, -0.0802F, 1.5795F, 0.0F, 0.0F));

		PartDefinition cube_r16 = hornL.addOrReplaceChild("cube_r16", CubeListBuilder.create().texOffs(57, 19).addBox(-0.15F, 0.275F, -0.475F, 0.3F, 1.7F, 1.0F, new CubeDeformation(0.1F))
		.texOffs(57, 19).addBox(-0.15F, 0.075F, -0.375F, 0.3F, 0.2F, 0.8F, new CubeDeformation(0.1F))
		.texOffs(57, 19).addBox(-0.15F, -0.125F, -0.275F, 0.3F, 0.2F, 0.6F, new CubeDeformation(0.1F))
		.texOffs(57, 19).addBox(-0.15F, -0.325F, -0.175F, 0.3F, 0.2F, 0.4F, new CubeDeformation(0.1F))
		.texOffs(57, 19).addBox(-0.15F, -0.525F, -0.075F, 0.3F, 0.2F, 0.2F, new CubeDeformation(0.1F)), PartPose.offsetAndRotation(4.65F, -7.8846F, -1.0618F, 2.0595F, 0.0F, 0.0F));

		PartDefinition cube_r17 = hornL.addOrReplaceChild("cube_r17", CubeListBuilder.create().texOffs(57, 19).addBox(-0.15F, -0.725F, -0.075F, 0.3F, 0.2F, 0.1F, new CubeDeformation(0.1F)), PartPose.offsetAndRotation(4.65F, -7.9846F, -1.0618F, 2.0595F, 0.0F, 0.0F));

		PartDefinition cube_r18 = hornL.addOrReplaceChild("cube_r18", CubeListBuilder.create().texOffs(57, 19).addBox(-0.15F, 1.4F, -0.1F, 0.3F, 0.2F, 0.1F, new CubeDeformation(0.1F)), PartPose.offsetAndRotation(4.65F, -5.7096F, -4.1868F, 1.5708F, 0.0F, 0.0F));

		PartDefinition cube_r19 = hornL.addOrReplaceChild("cube_r19", CubeListBuilder.create().texOffs(57, 19).addBox(-0.15F, 1.4F, -0.1F, 0.3F, 0.2F, 0.2F, new CubeDeformation(0.1F)), PartPose.offsetAndRotation(4.65F, -5.7096F, -3.9868F, 1.5708F, 0.0F, 0.0F));

		PartDefinition cube_r20 = hornL.addOrReplaceChild("cube_r20", CubeListBuilder.create().texOffs(57, 19).addBox(-0.15F, 1.4F, -0.2F, 0.3F, 0.2F, 0.4F, new CubeDeformation(0.1F)), PartPose.offsetAndRotation(4.65F, -5.7096F, -3.7868F, 1.5708F, 0.0F, 0.0F));

		PartDefinition cube_r21 = hornL.addOrReplaceChild("cube_r21", CubeListBuilder.create().texOffs(57, 19).addBox(-0.15F, 1.4F, -0.3F, 0.3F, 0.2F, 0.6F, new CubeDeformation(0.1F)), PartPose.offsetAndRotation(4.65F, -5.7096F, -3.5868F, 1.5708F, 0.0F, 0.0F));

		PartDefinition cube_r22 = hornL.addOrReplaceChild("cube_r22", CubeListBuilder.create().texOffs(57, 19).addBox(-0.15F, 1.4F, -0.4F, 0.3F, 0.2F, 0.8F, new CubeDeformation(0.1F)), PartPose.offsetAndRotation(4.65F, -5.7096F, -3.3868F, 1.5708F, 0.0F, 0.0F));

		PartDefinition cube_r23 = hornL.addOrReplaceChild("cube_r23", CubeListBuilder.create().texOffs(57, 19).addBox(-0.15F, 0.9F, -0.5F, 0.3F, 0.7F, 1.0F, new CubeDeformation(0.1F)), PartPose.offsetAndRotation(4.65F, -5.7096F, -2.6868F, 1.5708F, 0.0F, 0.0F));

		PartDefinition cube_r24 = hornL.addOrReplaceChild("cube_r24", CubeListBuilder.create().texOffs(57, 19).addBox(-0.15F, -1.0F, -0.5F, 0.3F, 2.6F, 1.0F, new CubeDeformation(0.1F)), PartPose.offsetAndRotation(4.65F, -5.7096F, -0.6868F, 1.5708F, 0.0F, 0.0F));

		PartDefinition cube_r25 = hornL.addOrReplaceChild("cube_r25", CubeListBuilder.create().texOffs(57, 19).addBox(-0.15F, -1.0F, -0.5F, 0.3F, 2.0F, 1.0F, new CubeDeformation(0.1F)), PartPose.offsetAndRotation(4.65F, -5.4096F, -0.2868F, 1.0036F, 0.0F, 0.0F));

		PartDefinition cube_r26 = hornL.addOrReplaceChild("cube_r26", CubeListBuilder.create().texOffs(56, 18).addBox(-0.15F, -2.9F, -0.5F, 0.3F, 3.9F, 1.3F, new CubeDeformation(0.1F)), PartPose.offsetAndRotation(4.65F, -6.3096F, 0.3132F, 0.0436F, 0.0F, 0.0F));

		PartDefinition cube_r27 = hornL.addOrReplaceChild("cube_r27", CubeListBuilder.create().texOffs(57, 19).addBox(-0.15F, -1.0F, -0.5F, 0.3F, 2.0F, 1.0F, new CubeDeformation(0.1F)), PartPose.offsetAndRotation(4.65F, -5.4096F, -0.2868F, 0.0436F, 0.0F, 0.0F));

		PartDefinition cube_r28 = hornL.addOrReplaceChild("cube_r28", CubeListBuilder.create().texOffs(57, 19).addBox(-0.15F, -1.0F, -1.0F, 0.3F, 2.0F, 1.0F, new CubeDeformation(0.1F)), PartPose.offsetAndRotation(4.65F, -5.0F, 0.0F, -0.9425F, 0.0F, 0.0F));

		PartDefinition hornR = extras.addOrReplaceChild("hornR", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition cube_r29 = hornR.addOrReplaceChild("cube_r29", CubeListBuilder.create().texOffs(57, 19).mirror().addBox(-0.05F, -0.25F, 0.25F, 0.1F, 1.1F, 0.1F, new CubeDeformation(0.1F)).mirror(false), PartPose.offsetAndRotation(-4.65F, -10.6848F, 3.7185F, -3.1154F, 0.0F, 0.0F));

		PartDefinition cube_r30 = hornR.addOrReplaceChild("cube_r30", CubeListBuilder.create().texOffs(57, 19).mirror().addBox(-0.05F, -0.45F, 0.25F, 0.1F, 1.5F, 0.1F, new CubeDeformation(0.1F)).mirror(false), PartPose.offsetAndRotation(-4.65F, -10.6848F, 3.6185F, -3.1154F, 0.0F, 0.0F));

		PartDefinition cube_r31 = hornR.addOrReplaceChild("cube_r31", CubeListBuilder.create().texOffs(57, 19).mirror().addBox(-0.05F, -0.65F, 0.25F, 0.1F, 1.9F, 0.1F, new CubeDeformation(0.1F)).mirror(false), PartPose.offsetAndRotation(-4.65F, -10.6848F, 3.5185F, -3.1154F, 0.0F, 0.0F));

		PartDefinition cube_r32 = hornR.addOrReplaceChild("cube_r32", CubeListBuilder.create().texOffs(57, 19).mirror().addBox(-0.05F, -0.75F, 0.25F, 0.1F, 2.2F, 0.1F, new CubeDeformation(0.1F)).mirror(false), PartPose.offsetAndRotation(-4.65F, -10.6848F, 3.4185F, -3.1154F, 0.0F, 0.0F));

		PartDefinition cube_r33 = hornR.addOrReplaceChild("cube_r33", CubeListBuilder.create().texOffs(57, 19).mirror().addBox(-0.05F, -0.85F, 0.25F, 0.1F, 2.3F, 0.1F, new CubeDeformation(0.1F)).mirror(false), PartPose.offsetAndRotation(-4.65F, -10.7848F, 3.3185F, -3.1154F, 0.0F, 0.0F));

		PartDefinition cube_r34 = hornR.addOrReplaceChild("cube_r34", CubeListBuilder.create().texOffs(57, 19).mirror().addBox(-0.05F, -0.85F, 0.25F, 0.1F, 2.5F, 0.1F, new CubeDeformation(0.1F)).mirror(false), PartPose.offsetAndRotation(-4.65F, -10.7848F, 3.2185F, -3.1154F, 0.0F, 0.0F));

		PartDefinition cube_r35 = hornR.addOrReplaceChild("cube_r35", CubeListBuilder.create().texOffs(57, 19).mirror().addBox(-0.05F, -0.85F, 0.25F, 0.1F, 2.7F, 0.1F, new CubeDeformation(0.1F)).mirror(false), PartPose.offsetAndRotation(-4.65F, -10.7848F, 3.1185F, -3.1154F, 0.0F, 0.0F));

		PartDefinition cube_r36 = hornR.addOrReplaceChild("cube_r36", CubeListBuilder.create().texOffs(57, 19).mirror().addBox(-0.05F, -0.85F, 0.25F, 0.1F, 2.9F, 0.1F, new CubeDeformation(0.1F)).mirror(false), PartPose.offsetAndRotation(-4.65F, -10.7848F, 3.0185F, -3.1154F, 0.0F, 0.0F));

		PartDefinition cube_r37 = hornR.addOrReplaceChild("cube_r37", CubeListBuilder.create().texOffs(57, 19).mirror().addBox(-0.05F, -0.85F, 0.25F, 0.1F, 3.1F, 0.1F, new CubeDeformation(0.1F)).mirror(false), PartPose.offsetAndRotation(-4.65F, -10.7848F, 2.9185F, -3.1154F, 0.0F, 0.0F));

		PartDefinition cube_r38 = hornR.addOrReplaceChild("cube_r38", CubeListBuilder.create().texOffs(57, 19).mirror().addBox(-0.05F, -0.85F, 0.25F, 0.1F, 3.3F, 0.1F, new CubeDeformation(0.1F)).mirror(false), PartPose.offsetAndRotation(-4.65F, -10.7848F, 2.8185F, -3.1154F, 0.0F, 0.0F));

		PartDefinition cube_r39 = hornR.addOrReplaceChild("cube_r39", CubeListBuilder.create().texOffs(57, 19).mirror().addBox(-0.05F, -0.85F, 0.25F, 0.1F, 3.5F, 0.1F, new CubeDeformation(0.1F)).mirror(false), PartPose.offsetAndRotation(-4.65F, -10.7848F, 2.7185F, -3.1154F, 0.0F, 0.0F));

		PartDefinition cube_r40 = hornR.addOrReplaceChild("cube_r40", CubeListBuilder.create().texOffs(57, 19).mirror().addBox(-0.15F, -0.85F, 0.25F, 0.3F, 3.6F, 0.1F, new CubeDeformation(0.1F)).mirror(false), PartPose.offsetAndRotation(-4.65F, -10.7848F, 2.6185F, -3.1154F, 0.0F, 0.0F));

		PartDefinition cube_r41 = hornR.addOrReplaceChild("cube_r41", CubeListBuilder.create().texOffs(57, 19).mirror().addBox(-0.15F, -0.85F, 0.25F, 0.3F, 3.8F, 0.1F, new CubeDeformation(0.1F)).mirror(false), PartPose.offsetAndRotation(-4.65F, -10.7848F, 2.5185F, -3.1154F, 0.0F, 0.0F));

		PartDefinition cube_r42 = hornR.addOrReplaceChild("cube_r42", CubeListBuilder.create().texOffs(56, 18).mirror().addBox(-0.15F, -0.85F, -0.65F, 0.3F, 3.7F, 1.5F, new CubeDeformation(0.1F)).mirror(false), PartPose.offsetAndRotation(-4.65F, -8.5848F, 0.6185F, 2.2951F, 0.0F, 0.0F));

		PartDefinition cube_r43 = hornR.addOrReplaceChild("cube_r43", CubeListBuilder.create().texOffs(56, 18).mirror().addBox(-0.15F, -0.85F, -0.5F, 0.3F, 2.7F, 1.3F, new CubeDeformation(0.1F)).mirror(false), PartPose.offsetAndRotation(-4.65F, -8.4348F, -0.0802F, 1.5795F, 0.0F, 0.0F));

		PartDefinition cube_r44 = hornR.addOrReplaceChild("cube_r44", CubeListBuilder.create().texOffs(57, 19).mirror().addBox(-0.15F, 0.275F, -0.475F, 0.3F, 1.7F, 1.0F, new CubeDeformation(0.1F)).mirror(false)
		.texOffs(57, 19).mirror().addBox(-0.15F, 0.075F, -0.375F, 0.3F, 0.2F, 0.8F, new CubeDeformation(0.1F)).mirror(false)
		.texOffs(57, 19).mirror().addBox(-0.15F, -0.125F, -0.275F, 0.3F, 0.2F, 0.6F, new CubeDeformation(0.1F)).mirror(false)
		.texOffs(57, 19).mirror().addBox(-0.15F, -0.325F, -0.175F, 0.3F, 0.2F, 0.4F, new CubeDeformation(0.1F)).mirror(false)
		.texOffs(57, 19).mirror().addBox(-0.15F, -0.525F, -0.075F, 0.3F, 0.2F, 0.2F, new CubeDeformation(0.1F)).mirror(false), PartPose.offsetAndRotation(-4.65F, -7.8846F, -1.0618F, 2.0595F, 0.0F, 0.0F));

		PartDefinition cube_r45 = hornR.addOrReplaceChild("cube_r45", CubeListBuilder.create().texOffs(57, 19).mirror().addBox(-0.15F, -0.725F, -0.075F, 0.3F, 0.2F, 0.1F, new CubeDeformation(0.1F)).mirror(false), PartPose.offsetAndRotation(-4.65F, -7.9846F, -1.0618F, 2.0595F, 0.0F, 0.0F));

		PartDefinition cube_r46 = hornR.addOrReplaceChild("cube_r46", CubeListBuilder.create().texOffs(57, 19).mirror().addBox(-0.15F, 1.4F, -0.1F, 0.3F, 0.2F, 0.1F, new CubeDeformation(0.1F)).mirror(false), PartPose.offsetAndRotation(-4.65F, -5.7096F, -4.1868F, 1.5708F, 0.0F, 0.0F));

		PartDefinition cube_r47 = hornR.addOrReplaceChild("cube_r47", CubeListBuilder.create().texOffs(57, 19).mirror().addBox(-0.15F, 1.4F, -0.1F, 0.3F, 0.2F, 0.2F, new CubeDeformation(0.1F)).mirror(false), PartPose.offsetAndRotation(-4.65F, -5.7096F, -3.9868F, 1.5708F, 0.0F, 0.0F));

		PartDefinition cube_r48 = hornR.addOrReplaceChild("cube_r48", CubeListBuilder.create().texOffs(57, 19).mirror().addBox(-0.15F, 1.4F, -0.2F, 0.3F, 0.2F, 0.4F, new CubeDeformation(0.1F)).mirror(false), PartPose.offsetAndRotation(-4.65F, -5.7096F, -3.7868F, 1.5708F, 0.0F, 0.0F));

		PartDefinition cube_r49 = hornR.addOrReplaceChild("cube_r49", CubeListBuilder.create().texOffs(57, 19).mirror().addBox(-0.15F, 1.4F, -0.3F, 0.3F, 0.2F, 0.6F, new CubeDeformation(0.1F)).mirror(false), PartPose.offsetAndRotation(-4.65F, -5.7096F, -3.5868F, 1.5708F, 0.0F, 0.0F));

		PartDefinition cube_r50 = hornR.addOrReplaceChild("cube_r50", CubeListBuilder.create().texOffs(57, 19).mirror().addBox(-0.15F, 1.4F, -0.4F, 0.3F, 0.2F, 0.8F, new CubeDeformation(0.1F)).mirror(false), PartPose.offsetAndRotation(-4.65F, -5.7096F, -3.3868F, 1.5708F, 0.0F, 0.0F));

		PartDefinition cube_r51 = hornR.addOrReplaceChild("cube_r51", CubeListBuilder.create().texOffs(57, 19).mirror().addBox(-0.15F, 0.9F, -0.5F, 0.3F, 0.7F, 1.0F, new CubeDeformation(0.1F)).mirror(false), PartPose.offsetAndRotation(-4.65F, -5.7096F, -2.6868F, 1.5708F, 0.0F, 0.0F));

		PartDefinition cube_r52 = hornR.addOrReplaceChild("cube_r52", CubeListBuilder.create().texOffs(57, 19).mirror().addBox(-0.15F, -1.0F, -0.5F, 0.3F, 2.6F, 1.0F, new CubeDeformation(0.1F)).mirror(false), PartPose.offsetAndRotation(-4.65F, -5.7096F, -0.6868F, 1.5708F, 0.0F, 0.0F));

		PartDefinition cube_r53 = hornR.addOrReplaceChild("cube_r53", CubeListBuilder.create().texOffs(57, 19).mirror().addBox(-0.15F, -1.0F, -0.5F, 0.3F, 2.0F, 1.0F, new CubeDeformation(0.1F)).mirror(false), PartPose.offsetAndRotation(-4.65F, -5.4096F, -0.2868F, 1.0036F, 0.0F, 0.0F));

		PartDefinition cube_r54 = hornR.addOrReplaceChild("cube_r54", CubeListBuilder.create().texOffs(56, 18).mirror().addBox(-0.15F, -2.9F, -0.5F, 0.3F, 3.9F, 1.3F, new CubeDeformation(0.1F)).mirror(false), PartPose.offsetAndRotation(-4.65F, -6.3096F, 0.3132F, 0.0436F, 0.0F, 0.0F));

		PartDefinition cube_r55 = hornR.addOrReplaceChild("cube_r55", CubeListBuilder.create().texOffs(57, 19).mirror().addBox(-0.15F, -1.0F, -0.5F, 0.3F, 2.0F, 1.0F, new CubeDeformation(0.1F)).mirror(false), PartPose.offsetAndRotation(-4.65F, -5.4096F, -0.2868F, 0.0436F, 0.0F, 0.0F));

		PartDefinition cube_r56 = hornR.addOrReplaceChild("cube_r56", CubeListBuilder.create().texOffs(57, 19).mirror().addBox(-0.15F, -1.0F, -1.0F, 0.3F, 2.0F, 1.0F, new CubeDeformation(0.1F)).mirror(false), PartPose.offsetAndRotation(-4.65F, -5.0F, 0.0F, -0.9425F, 0.0F, 0.0F));


		PartDefinition bodyExtras = body.addOrReplaceChild("bodyExtras", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition chestplate = bodyExtras.addOrReplaceChild("chestplate", CubeListBuilder.create().texOffs(2, 34).mirror().addBox(-0.3108F, 5.1343F, -2.6F, 0.7F, 0.2F, 0.1F, new CubeDeformation(0.0F)).mirror(false)
		.texOffs(2, 34).addBox(-4.0F, 1.25F, -3.0F, 8.0F, 2.0F, 0.5F, new CubeDeformation(0.0F))
		.texOffs(2, 34).mirror().addBox(-1.5108F, 3.1343F, -3.0F, 3.1F, 0.7F, 0.5F, new CubeDeformation(0.0F)).mirror(false)
		.texOffs(2, 34).mirror().addBox(-1.3108F, 3.8343F, -2.9F, 2.7F, 0.3F, 0.4F, new CubeDeformation(0.0F)).mirror(false)
		.texOffs(2, 34).mirror().addBox(-1.1108F, 4.1343F, -2.8F, 2.3F, 0.3F, 0.3F, new CubeDeformation(0.0F)).mirror(false)
		.texOffs(2, 34).mirror().addBox(-0.9108F, 4.4343F, -2.7F, 1.9F, 0.3F, 0.2F, new CubeDeformation(0.0F)).mirror(false)
		.texOffs(2, 34).mirror().addBox(-0.7108F, 4.7343F, -2.6F, 1.5F, 0.2F, 0.1F, new CubeDeformation(0.0F)).mirror(false)
		.texOffs(2, 34).mirror().addBox(-0.5108F, 4.9343F, -2.6F, 1.1F, 0.2F, 0.1F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition cube_r57 = chestplate.addOrReplaceChild("cube_r57", CubeListBuilder.create().texOffs(2, 34).mirror().addBox(-0.55F, -0.3F, -0.25F, 0.7F, 0.5F, 0.5F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(3.2392F, 3.3343F, -2.75F, 0.0F, 0.0F, 0.6283F));

		PartDefinition cube_r58 = chestplate.addOrReplaceChild("cube_r58", CubeListBuilder.create().texOffs(2, 34).addBox(-0.15F, -0.3F, -0.25F, 0.7F, 0.5F, 0.5F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-3.2392F, 3.3343F, -2.75F, 0.0F, 0.0F, -0.6283F));

		PartDefinition cube_r59 = chestplate.addOrReplaceChild("cube_r59", CubeListBuilder.create().texOffs(2, 34).addBox(-0.55F, -0.3F, -0.25F, 1.1F, 0.5F, 0.5F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-1.8392F, 3.3343F, -2.75F, 0.0F, 0.0F, 0.6283F));

		PartDefinition cube_r60 = chestplate.addOrReplaceChild("cube_r60", CubeListBuilder.create().texOffs(2, 34).mirror().addBox(-0.55F, -0.3F, -0.25F, 1.1F, 0.5F, 0.5F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(1.8392F, 3.3343F, -2.75F, 0.0F, 0.0F, -0.6283F));

		PartDefinition cube_r61 = chestplate.addOrReplaceChild("cube_r61", CubeListBuilder.create().texOffs(2, 34).mirror().addBox(-1.55F, -0.7F, -0.25F, 2.1F, 1.4F, 0.5F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(3.7392F, 2.9343F, -2.75F, 0.0F, 0.0F, 1.2828F));

		PartDefinition cube_r62 = chestplate.addOrReplaceChild("cube_r62", CubeListBuilder.create().texOffs(2, 34).mirror().addBox(-1.05F, -0.6F, -0.25F, 1.1F, 1.1F, 0.5F, new CubeDeformation(0.0F)).mirror(false)
		.texOffs(2, 34).mirror().addBox(0.05F, -0.4F, -0.15F, 0.5F, 0.9F, 0.4F, new CubeDeformation(0.0F)).mirror(false)
		.texOffs(2, 34).mirror().addBox(0.55F, -0.2F, -0.05F, 0.5F, 0.7F, 0.3F, new CubeDeformation(0.0F)).mirror(false)
		.texOffs(2, 34).mirror().addBox(1.05F, 0.0F, 0.05F, 0.4F, 0.5F, 0.2F, new CubeDeformation(0.0F)).mirror(false)
		.texOffs(2, 34).mirror().addBox(1.45F, 0.2F, 0.15F, 0.4F, 0.3F, 0.1F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(3.5971F, 4.0549F, -2.75F, 0.0F, 0.0F, 1.8326F));

		PartDefinition cube_r63 = chestplate.addOrReplaceChild("cube_r63", CubeListBuilder.create().texOffs(2, 34).addBox(-1.85F, 0.2F, 0.15F, 0.4F, 0.3F, 0.1F, new CubeDeformation(0.0F))
		.texOffs(2, 34).addBox(-1.45F, 0.0F, 0.05F, 0.4F, 0.5F, 0.2F, new CubeDeformation(0.0F))
		.texOffs(2, 34).addBox(-1.05F, -0.2F, -0.05F, 0.5F, 0.7F, 0.3F, new CubeDeformation(0.0F))
		.texOffs(2, 34).addBox(-0.55F, -0.4F, -0.15F, 0.5F, 0.9F, 0.4F, new CubeDeformation(0.0F))
		.texOffs(2, 34).addBox(-0.05F, -0.6F, -0.25F, 1.1F, 1.1F, 0.5F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-3.5971F, 4.0549F, -2.75F, 0.0F, 0.0F, -1.8326F));

		PartDefinition cube_r64 = chestplate.addOrReplaceChild("cube_r64", CubeListBuilder.create().texOffs(2, 34).addBox(-0.55F, -0.7F, -0.25F, 2.1F, 1.4F, 0.5F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-3.7392F, 2.9343F, -2.75F, 0.0F, 0.0F, -1.2828F));

		PartDefinition cube_r65 = chestplate.addOrReplaceChild("cube_r65", CubeListBuilder.create().texOffs(2, 34).addBox(-1.0F, -0.55F, -0.25F, 2.6F, 1.2F, 0.5F, new CubeDeformation(0.0F))
		.texOffs(2, 34).addBox(-5.6F, -0.55F, -0.25F, 2.6F, 1.2F, 0.5F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(2.0F, 0.8F, -2.45F, -0.4014F, 0.0F, 0.0F));

		PartDefinition cube_r66 = chestplate.addOrReplaceChild("cube_r66", CubeListBuilder.create().texOffs(1, 33).addBox(-1.0F, -0.7F, -0.35F, 2.0F, 1.4F, 0.7F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.7942F, -2.2626F, -0.2443F, 0.0F, 0.0F));

		PartDefinition Waist = bodyExtras.addOrReplaceChild("Waist", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition cube_r67 = Waist.addOrReplaceChild("cube_r67", CubeListBuilder.create().texOffs(1, 37).mirror().addBox(-1.6F, -0.65F, -0.15F, 2.6F, 0.5F, 5.5F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-3.3F, 12.75F, -2.65F, 0.0F, 0.0F, 0.3054F));

		PartDefinition cube_r68 = Waist.addOrReplaceChild("cube_r68", CubeListBuilder.create().texOffs(1, 37).mirror().addBox(-1.6F, -0.65F, -0.15F, 2.6F, 0.5F, 5.5F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-3.6F, 13.15F, -2.65F, 0.0F, 0.0F, 0.3054F));

		PartDefinition cube_r69 = Waist.addOrReplaceChild("cube_r69", CubeListBuilder.create().texOffs(1, 37).addBox(-1.0F, -0.65F, -0.15F, 2.6F, 0.5F, 5.5F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(3.6F, 13.15F, -2.65F, 0.0F, 0.0F, -0.3054F));

		PartDefinition cube_r70 = Waist.addOrReplaceChild("cube_r70", CubeListBuilder.create().texOffs(1, 37).addBox(-1.0F, -0.65F, -0.15F, 2.6F, 0.5F, 5.5F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(3.3F, 12.75F, -2.65F, 0.0F, 0.0F, -0.3054F));


		PartDefinition armExtras = leftArm.addOrReplaceChild("armExtras", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition Pauldron = armExtras.addOrReplaceChild("Pauldron", CubeListBuilder.create().texOffs(47, 41).addBox(1.1F, -3.4F, -2.6F, 3.2F, 3.8F, 5.3F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition rim_r1 = Pauldron.addOrReplaceChild("rim_r1", CubeListBuilder.create().texOffs(39, 41).addBox(-2.65F, -0.3F, -2.9F, 5.4F, 0.6F, 0.4F, new CubeDeformation(0.0F))
		.texOffs(39, 41).addBox(-2.65F, -0.3F, 2.5F, 5.4F, 0.6F, 0.4F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(2.5321F, -0.8617F, 0.0F, 0.0F, -1.5708F, 0.8727F));

		PartDefinition rim_r2 = Pauldron.addOrReplaceChild("rim_r2", CubeListBuilder.create().texOffs(39, 41).addBox(-2.8F, -0.3F, -0.2F, 5.7F, 0.6F, 0.4F, new CubeDeformation(0.0F))
		.texOffs(39, 41).addBox(-2.8F, -0.3F, -5.6F, 5.7F, 0.6F, 0.4F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(2.5F, -0.9F, 2.7F, 0.0F, 0.0F, 0.8727F));

		PartDefinition rim_r3 = Pauldron.addOrReplaceChild("rim_r3", CubeListBuilder.create().texOffs(39, 41).addBox(-2.65F, -0.3F, 2.5F, 5.4F, 0.6F, 0.4F, new CubeDeformation(0.0F))
		.texOffs(39, 41).addBox(-2.65F, -0.3F, -2.9F, 5.4F, 0.6F, 0.4F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(2.5321F, -1.3617F, 0.0F, 0.0F, -1.5708F, 0.8727F));

		PartDefinition rim_r4 = Pauldron.addOrReplaceChild("rim_r4", CubeListBuilder.create().texOffs(39, 41).addBox(-2.8F, -0.3F, -0.2F, 5.7F, 0.6F, 0.4F, new CubeDeformation(0.0F))
		.texOffs(39, 41).addBox(-2.8F, -0.3F, -5.6F, 5.7F, 0.6F, 0.4F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(2.5F, -1.4F, 2.7F, 0.0F, 0.0F, 0.8727F));

		PartDefinition Vambrace = armExtras.addOrReplaceChild("Vambrace", CubeListBuilder.create().texOffs(41, 32).addBox(-1.75F, 5.5F, -3.0F, 5.5F, 3.0F, 5.75F, new CubeDeformation(0.0F))
		.texOffs(38, 35).addBox(3.5F, 5.1F, -3.2F, 0.5F, 0.4F, 6.0F, new CubeDeformation(0.0F))
		.texOffs(38, 35).addBox(-2.0F, 5.1F, -3.2F, 0.5F, 0.4F, 6.0F, new CubeDeformation(0.0F))
		.texOffs(38, 35).addBox(3.5F, 8.2F, -3.2F, 0.5F, 0.4F, 6.0F, new CubeDeformation(0.0F))
		.texOffs(38, 35).addBox(-2.0F, 8.2F, -3.2F, 0.5F, 0.4F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition Rims_r1 = Vambrace.addOrReplaceChild("Rims_r1", CubeListBuilder.create().texOffs(38, 35).addBox(-3.0F, -0.2F, -3.0F, 0.5F, 0.4F, 6.0F, new CubeDeformation(0.0F))
		.texOffs(38, 35).addBox(2.5F, -0.2F, -3.0F, 0.5F, 0.4F, 6.0F, new CubeDeformation(0.0F))
		.texOffs(38, 35).addBox(2.5F, -3.3F, -3.0F, 0.5F, 0.4F, 6.0F, new CubeDeformation(0.0F))
		.texOffs(38, 35).addBox(-3.0F, -3.3F, -3.0F, 0.5F, 0.4F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.0F, 8.4F, -0.2F, 0.0F, 1.5708F, 0.0F));

		PartDefinition Spike = Vambrace.addOrReplaceChild("Spike", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition cube_r71 = Spike.addOrReplaceChild("cube_r71", CubeListBuilder.create().texOffs(65, 50).addBox(-0.1F, -1.6F, -1.75F, 0.2F, 0.5F, 3.5F, new CubeDeformation(0.0F))
		.texOffs(65, 51).addBox(-0.1F, -1.1F, -1.75F, 0.2F, 1.5F, 3.5F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(5.9315F, 1.1797F, -0.05F, 0.0F, 0.0F, -0.4538F));

		PartDefinition cube_r72 = Spike.addOrReplaceChild("cube_r72", CubeListBuilder.create().texOffs(65, 52).addBox(0.35F, -0.8F, -1.75F, 0.2F, 0.8F, 3.5F, new CubeDeformation(0.0F))
		.texOffs(54, 51).addBox(-0.95F, 0.0F, -1.75F, 1.5F, 0.8F, 3.5F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(5.7094F, 2.3201F, -0.05F, 0.0F, 0.0F, -0.1047F));

		PartDefinition cube_r73 = Spike.addOrReplaceChild("cube_r73", CubeListBuilder.create().texOffs(54, 51).addBox(-0.75F, -0.4F, -1.8F, 1.5F, 0.8F, 3.5F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(5.5531F, 3.2065F, 0.0F, 0.0F, 0.0F, 0.1745F));

		PartDefinition cube_r74 = Spike.addOrReplaceChild("cube_r74", CubeListBuilder.create().texOffs(54, 51).addBox(-0.8F, -0.9F, -1.8F, 1.5F, 2.1F, 3.5F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(5.1506F, 4.0494F, 0.0F, 0.0F, 0.0F, 0.5236F));

		PartDefinition cube_r75 = Spike.addOrReplaceChild("cube_r75", CubeListBuilder.create().texOffs(50, 70).addBox(-1.0F, -2.0F, -2.3F, 2.0F, 2.0F, 4.4F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(3.1F, 6.0F, 0.1F, 0.0F, 0.0F, 0.7854F));


		PartDefinition leftLegExtras = leftLeg.addOrReplaceChild("leftLegExtras", CubeListBuilder.create().texOffs(0, 46).addBox(-3.7F, 3.9F, -1.9F, 5.0F, 1.0F, 4.9F, new CubeDeformation(0.0F))
		.texOffs(0, 46).addBox(-3.7F, 5.9F, -1.9F, 5.0F, 1.0F, 4.9F, new CubeDeformation(0.0F)), PartPose.offset(1.1452F, 2.6647F, -0.5994F));

		PartDefinition cube_r76 = leftLegExtras.addOrReplaceChild("cube_r76", CubeListBuilder.create().texOffs(5, 51).addBox(0.15F, -0.45F, -0.1F, 0.9F, 0.1F, 0.1F, new CubeDeformation(0.0F))
		.texOffs(5, 51).addBox(-0.15F, -0.35F, -0.1F, 1.2F, 0.1F, 0.1F, new CubeDeformation(0.0F))
		.texOffs(5, 51).addBox(-0.45F, -0.25F, -0.1F, 1.7F, 0.1F, 0.1F, new CubeDeformation(0.0F))
		.texOffs(5, 51).addBox(-0.55F, -0.15F, -0.1F, 1.8F, 0.1F, 0.1F, new CubeDeformation(0.0F))
		.texOffs(5, 51).addBox(-0.75F, -0.05F, -0.1F, 2.0F, 0.1F, 0.2F, new CubeDeformation(0.0F))
		.texOffs(5, 51).addBox(-1.05F, 0.05F, -0.1F, 2.3F, 0.1F, 0.2F, new CubeDeformation(0.0F))
		.texOffs(5, 51).addBox(-1.25F, 0.15F, -0.1F, 2.5F, 0.1F, 0.2F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(2.7882F, 0.5939F, 2.5828F, -1.2808F, -0.2277F, 1.7627F));

		PartDefinition cube_r77 = leftLegExtras.addOrReplaceChild("cube_r77", CubeListBuilder.create().texOffs(5, 51).addBox(-1.7F, -0.3F, -0.1F, 3.6F, 0.6F, 0.2F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(2.1207F, 3.0163F, 2.6311F, -1.1423F, 0.1587F, 1.9149F));

		PartDefinition cube_r78 = leftLegExtras.addOrReplaceChild("cube_r78", CubeListBuilder.create().texOffs(5, 51).addBox(-1.35F, 0.4F, 0.0F, 2.0F, 0.7F, 0.2F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.95F, 3.8F, 2.5F, -0.3441F, -0.721F, 0.4973F));

		PartDefinition cube_r79 = leftLegExtras.addOrReplaceChild("cube_r79", CubeListBuilder.create().texOffs(5, 51).addBox(-1.35F, -0.5F, 0.0F, 2.0F, 0.8F, 0.2F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.95F, 3.8F, 2.5F, 0.2374F, -0.7561F, -0.339F));

		PartDefinition cube_r80 = leftLegExtras.addOrReplaceChild("cube_r80", CubeListBuilder.create().texOffs(5, 50).addBox(-0.35F, -0.5F, 0.0F, 1.0F, 1.5F, 0.2F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.95F, 3.8F, 2.5F, 0.1719F, -0.7703F, -0.2444F));

		PartDefinition cube_r81 = leftLegExtras.addOrReplaceChild("cube_r81", CubeListBuilder.create().texOffs(19, 37).addBox(-0.7F, -2.0F, 0.9F, 2.4F, 0.6F, 4.8F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.2548F, 3.6353F, -2.7006F, 0.0F, 0.0F, -0.3491F));

		PartDefinition cube_r82 = leftLegExtras.addOrReplaceChild("cube_r82", CubeListBuilder.create().texOffs(19, 37).addBox(-0.7F, -2.0F, 0.9F, 2.4F, 0.6F, 4.8F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.2548F, 1.9353F, -2.7006F, 0.0F, 0.0F, -0.3491F));

		PartDefinition cube_r83 = leftLegExtras.addOrReplaceChild("cube_r83", CubeListBuilder.create().texOffs(23, 41).addBox(-0.65F, -0.3F, -0.2F, 1.3F, 0.6F, 0.5F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.9725F, -0.6014F, -1.7895F, -0.2053F, 0.5667F, -0.7629F));

		PartDefinition cube_r84 = leftLegExtras.addOrReplaceChild("cube_r84", CubeListBuilder.create().texOffs(18, 36).addBox(-0.5F, -2.0F, 0.4F, 3.1F, 0.6F, 5.7F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.2548F, 0.5353F, -2.7006F, 0.0F, 0.0F, -0.3491F));


		PartDefinition armExtras2 = rightArm.addOrReplaceChild("armExtras2", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition Pauldron2 = armExtras2.addOrReplaceChild("Pauldron2", CubeListBuilder.create().texOffs(47, 41).mirror().addBox(-4.3F, -3.4F, -2.6F, 3.2F, 3.8F, 5.3F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition rim_r5 = Pauldron2.addOrReplaceChild("rim_r5", CubeListBuilder.create().texOffs(39, 41).mirror().addBox(-2.75F, -0.3F, -2.9F, 5.4F, 0.6F, 0.4F, new CubeDeformation(0.0F)).mirror(false)
		.texOffs(39, 41).mirror().addBox(-2.75F, -0.3F, 2.5F, 5.4F, 0.6F, 0.4F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-2.5321F, -0.8617F, 0.0F, 0.0F, 1.5708F, -0.8727F));

		PartDefinition rim_r6 = Pauldron2.addOrReplaceChild("rim_r6", CubeListBuilder.create().texOffs(39, 41).mirror().addBox(-2.9F, -0.3F, -0.2F, 5.7F, 0.6F, 0.4F, new CubeDeformation(0.0F)).mirror(false)
		.texOffs(39, 41).mirror().addBox(-2.9F, -0.3F, -5.6F, 5.7F, 0.6F, 0.4F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-2.5F, -0.9F, 2.7F, 0.0F, 0.0F, -0.8727F));

		PartDefinition rim_r7 = Pauldron2.addOrReplaceChild("rim_r7", CubeListBuilder.create().texOffs(39, 41).mirror().addBox(-2.75F, -0.3F, 2.5F, 5.4F, 0.6F, 0.4F, new CubeDeformation(0.0F)).mirror(false)
		.texOffs(39, 41).mirror().addBox(-2.75F, -0.3F, -2.9F, 5.4F, 0.6F, 0.4F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-2.5321F, -1.3617F, 0.0F, 0.0F, 1.5708F, -0.8727F));

		PartDefinition rim_r8 = Pauldron2.addOrReplaceChild("rim_r8", CubeListBuilder.create().texOffs(39, 41).mirror().addBox(-2.9F, -0.3F, -0.2F, 5.7F, 0.6F, 0.4F, new CubeDeformation(0.0F)).mirror(false)
		.texOffs(39, 41).mirror().addBox(-2.9F, -0.3F, -5.6F, 5.7F, 0.6F, 0.4F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-2.5F, -1.4F, 2.7F, 0.0F, 0.0F, -0.8727F));

		PartDefinition Vambrace2 = armExtras2.addOrReplaceChild("Vambrace2", CubeListBuilder.create().texOffs(41, 32).mirror().addBox(-3.75F, 5.5F, -3.0F, 5.5F, 3.0F, 5.75F, new CubeDeformation(0.0F)).mirror(false)
		.texOffs(38, 35).mirror().addBox(-4.0F, 5.1F, -3.2F, 0.5F, 0.4F, 6.0F, new CubeDeformation(0.0F)).mirror(false)
		.texOffs(38, 35).mirror().addBox(1.5F, 5.1F, -3.2F, 0.5F, 0.4F, 6.0F, new CubeDeformation(0.0F)).mirror(false)
		.texOffs(38, 35).mirror().addBox(-4.0F, 8.2F, -3.2F, 0.5F, 0.4F, 6.0F, new CubeDeformation(0.0F)).mirror(false)
		.texOffs(38, 35).mirror().addBox(1.5F, 8.2F, -3.2F, 0.5F, 0.4F, 6.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition Rims_r2 = Vambrace2.addOrReplaceChild("Rims_r2", CubeListBuilder.create().texOffs(38, 35).mirror().addBox(2.5F, -0.2F, -3.0F, 0.5F, 0.4F, 6.0F, new CubeDeformation(0.0F)).mirror(false)
		.texOffs(38, 35).mirror().addBox(-3.0F, -0.2F, -3.0F, 0.5F, 0.4F, 6.0F, new CubeDeformation(0.0F)).mirror(false)
		.texOffs(38, 35).mirror().addBox(-3.0F, -3.3F, -3.0F, 0.5F, 0.4F, 6.0F, new CubeDeformation(0.0F)).mirror(false)
		.texOffs(38, 35).mirror().addBox(2.5F, -3.3F, -3.0F, 0.5F, 0.4F, 6.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-1.0F, 8.4F, -0.2F, 0.0F, -1.5708F, 0.0F));

		PartDefinition Spike2 = Vambrace2.addOrReplaceChild("Spike2", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition cube_r85 = Spike2.addOrReplaceChild("cube_r85", CubeListBuilder.create().texOffs(65, 52).mirror().addBox(-0.55F, -0.8F, -1.75F, 0.2F, 0.8F, 3.5F, new CubeDeformation(0.0F)).mirror(false)
		.texOffs(54, 51).mirror().addBox(-0.55F, 0.0F, -1.75F, 1.5F, 0.8F, 3.5F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-5.7094F, 2.3201F, -0.05F, 0.0F, 0.0F, 0.1047F));

		PartDefinition cube_r86 = Spike2.addOrReplaceChild("cube_r86", CubeListBuilder.create().texOffs(65, 51).mirror().addBox(-0.1F, -1.1F, -1.75F, 0.2F, 1.5F, 3.5F, new CubeDeformation(0.0F)).mirror(false)
		.texOffs(65, 50).mirror().addBox(-0.1F, -1.6F, -1.75F, 0.2F, 0.5F, 3.5F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-5.9315F, 1.1797F, -0.05F, 0.0F, 0.0F, 0.4538F));

		PartDefinition cube_r87 = Spike2.addOrReplaceChild("cube_r87", CubeListBuilder.create().texOffs(54, 51).mirror().addBox(-0.75F, -0.4F, -1.8F, 1.5F, 0.8F, 3.5F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-5.5531F, 3.2065F, 0.0F, 0.0F, 0.0F, -0.1745F));

		PartDefinition cube_r88 = Spike2.addOrReplaceChild("cube_r88", CubeListBuilder.create().texOffs(54, 51).mirror().addBox(-0.7F, -0.9F, -1.8F, 1.5F, 2.1F, 3.5F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-5.1506F, 4.0494F, 0.0F, 0.0F, 0.0F, -0.5236F));

		PartDefinition cube_r89 = Spike2.addOrReplaceChild("cube_r89", CubeListBuilder.create().texOffs(50, 70).mirror().addBox(-1.0F, -2.0F, -2.3F, 2.0F, 2.0F, 4.4F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-3.1F, 6.0F, 0.1F, 0.0F, 0.0F, -0.7854F));


		PartDefinition rightLegExtras = rightLeg.addOrReplaceChild("rightLegExtras", CubeListBuilder.create().texOffs(0, 46).mirror().addBox(-1.3F, 3.9F, -1.9F, 5.0F, 1.0F, 4.9F, new CubeDeformation(0.0F)).mirror(false)
		.texOffs(0, 46).mirror().addBox(-1.3F, 5.9F, -1.9F, 5.0F, 1.0F, 4.9F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(-1.1452F, 2.6647F, -0.5994F));

		PartDefinition cube_r90 = rightLegExtras.addOrReplaceChild("cube_r90", CubeListBuilder.create().texOffs(5, 51).mirror().addBox(-1.05F, -0.45F, -0.1F, 0.9F, 0.1F, 0.1F, new CubeDeformation(0.0F)).mirror(false)
		.texOffs(5, 51).mirror().addBox(-1.05F, -0.35F, -0.1F, 1.2F, 0.1F, 0.1F, new CubeDeformation(0.0F)).mirror(false)
		.texOffs(5, 51).mirror().addBox(-1.25F, -0.25F, -0.1F, 1.7F, 0.1F, 0.1F, new CubeDeformation(0.0F)).mirror(false)
		.texOffs(5, 51).mirror().addBox(-1.25F, -0.15F, -0.1F, 1.8F, 0.1F, 0.1F, new CubeDeformation(0.0F)).mirror(false)
		.texOffs(5, 51).mirror().addBox(-1.25F, -0.05F, -0.1F, 2.0F, 0.1F, 0.2F, new CubeDeformation(0.0F)).mirror(false)
		.texOffs(5, 51).mirror().addBox(-1.25F, 0.05F, -0.1F, 2.3F, 0.1F, 0.2F, new CubeDeformation(0.0F)).mirror(false)
		.texOffs(5, 51).mirror().addBox(-1.25F, 0.15F, -0.1F, 2.5F, 0.1F, 0.2F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-2.7882F, 0.5939F, 2.5828F, -1.2808F, 0.2277F, -1.7627F));

		PartDefinition cube_r91 = rightLegExtras.addOrReplaceChild("cube_r91", CubeListBuilder.create().texOffs(5, 51).mirror().addBox(-1.9F, -0.3F, -0.1F, 3.6F, 0.6F, 0.2F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-2.1207F, 3.0163F, 2.6311F, -1.1423F, -0.1587F, -1.9149F));

		PartDefinition cube_r92 = rightLegExtras.addOrReplaceChild("cube_r92", CubeListBuilder.create().texOffs(5, 51).mirror().addBox(-0.65F, 0.4F, 0.0F, 2.0F, 0.7F, 0.2F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-1.95F, 3.8F, 2.5F, -0.3441F, 0.721F, -0.4973F));

		PartDefinition cube_r93 = rightLegExtras.addOrReplaceChild("cube_r93", CubeListBuilder.create().texOffs(5, 51).mirror().addBox(-0.65F, -0.5F, 0.0F, 2.0F, 0.8F, 0.2F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-1.95F, 3.8F, 2.5F, 0.2374F, 0.7561F, 0.339F));

		PartDefinition cube_r94 = rightLegExtras.addOrReplaceChild("cube_r94", CubeListBuilder.create().texOffs(5, 50).mirror().addBox(-0.65F, -0.5F, 0.0F, 1.0F, 1.5F, 0.2F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-1.95F, 3.8F, 2.5F, 0.1719F, 0.7703F, 0.2444F));

		PartDefinition cube_r95 = rightLegExtras.addOrReplaceChild("cube_r95", CubeListBuilder.create().texOffs(19, 37).mirror().addBox(-1.7F, -2.0F, 0.9F, 2.4F, 0.6F, 4.8F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-0.2548F, 3.6353F, -2.7006F, 0.0F, 0.0F, 0.3491F));

		PartDefinition cube_r96 = rightLegExtras.addOrReplaceChild("cube_r96", CubeListBuilder.create().texOffs(19, 37).mirror().addBox(-1.7F, -2.0F, 0.9F, 2.4F, 0.6F, 4.8F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-0.2548F, 1.9353F, -2.7006F, 0.0F, 0.0F, 0.3491F));

		PartDefinition cube_r97 = rightLegExtras.addOrReplaceChild("cube_r97", CubeListBuilder.create().texOffs(23, 41).mirror().addBox(-0.65F, -0.3F, -0.2F, 1.3F, 0.6F, 0.5F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.9725F, -0.6014F, -1.7895F, -0.2053F, -0.5667F, 0.7629F));

		PartDefinition cube_r98 = rightLegExtras.addOrReplaceChild("cube_r98", CubeListBuilder.create().texOffs(18, 36).mirror().addBox(-2.6F, -2.0F, 0.4F, 3.1F, 0.6F, 5.7F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-0.2548F, 0.5353F, -2.7006F, 0.0F, 0.0F, 0.3491F));

		return LayerDefinition.create(meshdefinition, 128, 128);
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer buffer, int packedLight, int packedOverlay, int colour) {
        rightArm.render(poseStack, buffer, packedLight, packedOverlay);
        rightLeg.render(poseStack, buffer, packedLight, packedOverlay);
        head.render(poseStack, buffer, packedLight, packedOverlay);
        body.render(poseStack, buffer, packedLight, packedOverlay);
        leftArm.render(poseStack, buffer, packedLight, packedOverlay);
        leftLeg.render(poseStack, buffer, packedLight, packedOverlay);
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