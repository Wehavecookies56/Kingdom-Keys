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
import online.kingdomkeys.kingdomkeys.KingdomKeys;

public class TerraModel<T extends LivingEntity> extends ArmorBaseModel<T> {

    public static final ModelLayerLocation LAYER_LOCATION_TOP = new ModelLayerLocation(new ResourceLocation(KingdomKeys.MODID, "terra_top"), "main");
    public static final ModelLayerLocation LAYER_LOCATION_BOTTOM = new ModelLayerLocation(new ResourceLocation(KingdomKeys.MODID, "terra_bottom"), "main");
    
    public final ModelPart head;
    public final ModelPart body;
    public final ModelPart leftArm;
    public final ModelPart rightArm;
    public final ModelPart leftLeg;
    public final ModelPart rightLeg;  
    
    public TerraModel(ModelPart root) {
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

		PartDefinition Horn1 = headExtras.addOrReplaceChild("Horn1", CubeListBuilder.create(), PartPose.offset(0.5F, 0.0F, 0.2F));

		PartDefinition cube_r1 = Horn1.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(120, 27).addBox(-0.1F, 0.0F, -0.35F, 0.2F, 0.4F, 1.2F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(4.1F, -15.9004F, -0.8212F, 1.7628F, 0.0F, 0.0F));

		PartDefinition cube_r2 = Horn1.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(117, 29).mirror().addBox(0.7F, -2.0F, -1.0F, 0.3F, 2.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-4.9261F, -3.182F, 1.2967F, -0.9599F, 0.0F, 0.0F));

		PartDefinition cube_r3 = Horn1.addOrReplaceChild("cube_r3", CubeListBuilder.create().texOffs(117, 29).mirror().addBox(0.7F, -2.0F, -1.0F, 0.3F, 2.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-4.9261F, -6.282F, 1.2967F, -0.9599F, 0.0F, 0.0F));

		PartDefinition cube_r4 = Horn1.addOrReplaceChild("cube_r4", CubeListBuilder.create().texOffs(120, 27).addBox(-0.1F, -0.1F, -0.35F, 0.2F, 0.2F, 1.2F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(4.1F, -15.9004F, -0.8212F, 1.5882F, 0.0F, 0.0F));

		PartDefinition cube_r5 = Horn1.addOrReplaceChild("cube_r5", CubeListBuilder.create().texOffs(121, 28).addBox(-0.05F, 0.0468F, -0.201F, 0.1F, 0.2F, 0.5F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(4.05F, -16.3468F, -1.029F, 1.021F, 0.0F, 0.0F));

		PartDefinition cube_r6 = Horn1.addOrReplaceChild("cube_r6", CubeListBuilder.create().texOffs(121, 28).addBox(0.0F, 0.6F, -0.75F, 0.1F, 0.2F, 0.7F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(4.0F, -16.1846F, -1.7263F, 1.5882F, 0.0F, 0.0F));

		PartDefinition cube_r7 = Horn1.addOrReplaceChild("cube_r7", CubeListBuilder.create().texOffs(121, 28).addBox(0.0F, 0.0F, -0.3F, 0.0F, 0.2F, 0.7F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(4.0F, -15.7434F, -1.2185F, 1.1519F, 0.0F, 0.0F));

		PartDefinition cube_r8 = Horn1.addOrReplaceChild("cube_r8", CubeListBuilder.create().texOffs(119, 27).addBox(-0.15F, -1.1624F, -1.4F, 0.0F, 0.2F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(119, 27).addBox(-0.15F, -1.0F, -1.4F, 0.1F, 0.3F, 1.9F, new CubeDeformation(0.0F))
		.texOffs(119, 27).addBox(-0.15F, -0.8F, -1.4F, 0.2F, 0.3F, 1.9F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(4.15F, -15.5976F, -0.0035F, 2.1642F, 0.0F, 0.0F));

		PartDefinition cube_r9 = Horn1.addOrReplaceChild("cube_r9", CubeListBuilder.create().texOffs(112, 27).addBox(-0.1F, -0.85F, -4.95F, 0.0F, 0.2F, 5.9F, new CubeDeformation(0.0F))
		.texOffs(112, 27).addBox(-0.1F, -0.65F, -4.95F, 0.1F, 0.2F, 5.9F, new CubeDeformation(0.0F))
		.texOffs(112, 26).addBox(-0.1F, -0.45F, -5.05F, 0.2F, 0.3F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(4.1F, -13.3688F, 0.7158F, 1.8151F, 0.0F, 0.0F));

		PartDefinition cube_r10 = Horn1.addOrReplaceChild("cube_r10", CubeListBuilder.create().texOffs(121, 28).addBox(-0.15F, 0.05F, 2.35F, 0.1F, 0.1F, 0.1F, new CubeDeformation(0.0F))
		.texOffs(121, 28).addBox(-0.15F, -0.05F, 2.25F, 0.1F, 0.2F, 0.1F, new CubeDeformation(0.0F))
		.texOffs(121, 28).addBox(-0.15F, -0.15F, 2.15F, 0.1F, 0.3F, 0.1F, new CubeDeformation(0.0F))
		.texOffs(121, 28).addBox(-0.15F, -0.25F, 2.05F, 0.1F, 0.4F, 0.1F, new CubeDeformation(0.0F))
		.texOffs(121, 28).addBox(-0.15F, -0.35F, 1.95F, 0.1F, 0.5F, 0.1F, new CubeDeformation(0.0F))
		.texOffs(121, 28).addBox(-0.15F, -0.45F, 1.85F, 0.1F, 0.6F, 0.1F, new CubeDeformation(0.0F))
		.texOffs(120, 28).addBox(-0.15F, -0.55F, 0.85F, 0.2F, 0.7F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(116, 28).addBox(-0.15F, -0.85F, -1.05F, 0.3F, 1.0F, 1.9F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(4.15F, -15.5032F, 0.4823F, 2.1642F, 0.0F, 0.0F));

		PartDefinition cube_r11 = Horn1.addOrReplaceChild("cube_r11", CubeListBuilder.create().texOffs(118, 26).addBox(-0.1F, -0.85F, 1.45F, 0.3F, 1.0F, 2.9F, new CubeDeformation(0.0F))
		.texOffs(118, 26).addBox(-0.1F, -0.85F, -1.45F, 0.3F, 1.0F, 2.9F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(4.1F, -10.4574F, 2.1526F, 1.8151F, 0.0F, 0.0F));

		PartDefinition cube_r12 = Horn1.addOrReplaceChild("cube_r12", CubeListBuilder.create().texOffs(118, 26).addBox(-0.1F, 0.45F, -0.25F, 0.3F, 1.0F, 2.9F, new CubeDeformation(0.0F))
		.texOffs(120, 27).addBox(-0.1F, -1.35F, 0.65F, 0.2F, 1.1F, 1.2F, new CubeDeformation(0.0F))
		.texOffs(114, 27).addBox(-0.1F, -0.25F, -3.25F, 0.2F, 1.7F, 5.1F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(4.1F, -6.6499F, 0.9336F, 1.4835F, 0.0F, 0.0F));

		PartDefinition cube_r13 = Horn1.addOrReplaceChild("cube_r13", CubeListBuilder.create().texOffs(118, 26).addBox(-0.1F, -0.85F, -1.85F, 0.2F, 1.1F, 2.7F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(4.1F, -7.1716F, 0.6731F, 2.5744F, 0.0F, 0.0F));

		PartDefinition cube_r14 = Horn1.addOrReplaceChild("cube_r14", CubeListBuilder.create().texOffs(117, 25).addBox(-0.1F, 1.0F, -2.0F, 0.2F, 1.1F, 3.9F, new CubeDeformation(0.0F))
		.texOffs(116, 24).addBox(-0.1F, 0.0F, -2.0F, 0.2F, 1.1F, 4.9F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(4.1F, -5.4038F, -1.1654F, 2.3562F, 0.0F, 0.0F));

		PartDefinition cube_r15 = Horn1.addOrReplaceChild("cube_r15", CubeListBuilder.create().texOffs(116, 24).addBox(-0.1F, -0.8F, -1.3F, 0.2F, 1.1F, 4.9F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(4.1F, -5.4038F, -1.1654F, 2.3562F, 0.0F, 0.0F));

		PartDefinition cube_r16 = Horn1.addOrReplaceChild("cube_r16", CubeListBuilder.create().texOffs(120, 27).addBox(-0.1F, -2.05F, -0.95F, 0.2F, 3.7F, 1.1F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(4.1F, -8.1781F, -1.4444F, 1.6581F, 0.0F, 0.0F));

		PartDefinition cube_r17 = Horn1.addOrReplaceChild("cube_r17", CubeListBuilder.create().texOffs(120, 27).addBox(-0.1F, -0.5F, -0.5F, 0.2F, 3.7F, 1.1F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(4.1F, -3.0673F, -1.8749F, 2.0071F, 0.0F, 0.0F));

		PartDefinition cube_r18 = Horn1.addOrReplaceChild("cube_r18", CubeListBuilder.create().texOffs(120, 27).addBox(-0.1F, -0.5F, -0.5F, 0.2F, 4.0F, 1.1F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(4.1F, -2.2673F, -1.8749F, 2.0071F, 0.0F, 0.0F));

		PartDefinition cube_r19 = Horn1.addOrReplaceChild("cube_r19", CubeListBuilder.create().texOffs(121, 28).addBox(-0.1F, -2.1F, 1.05F, 0.1F, 0.1F, 0.1F, new CubeDeformation(0.0F))
		.texOffs(121, 28).addBox(-0.1F, -2.0F, 0.95F, 0.1F, 0.1F, 0.2F, new CubeDeformation(0.0F))
		.texOffs(121, 28).addBox(-0.1F, -1.9F, 0.85F, 0.1F, 0.1F, 0.3F, new CubeDeformation(0.0F))
		.texOffs(121, 28).addBox(-0.1F, -1.8F, 0.75F, 0.1F, 0.1F, 0.4F, new CubeDeformation(0.0F))
		.texOffs(121, 28).addBox(-0.1F, -1.7F, 0.65F, 0.1F, 0.1F, 0.5F, new CubeDeformation(0.0F))
		.texOffs(121, 28).addBox(-0.1F, -1.6F, 0.55F, 0.1F, 0.1F, 0.6F, new CubeDeformation(0.0F))
		.texOffs(121, 28).addBox(-0.1F, -1.5F, 0.45F, 0.1F, 0.1F, 0.7F, new CubeDeformation(0.0F))
		.texOffs(120, 28).addBox(-0.1F, -1.4F, 0.35F, 0.1F, 0.1F, 0.8F, new CubeDeformation(0.0F))
		.texOffs(120, 28).addBox(-0.1F, -1.3F, 0.25F, 0.1F, 0.1F, 0.9F, new CubeDeformation(0.0F))
		.texOffs(120, 27).addBox(-0.1F, -1.2F, 0.15F, 0.1F, 0.1F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(120, 27).addBox(-0.1F, -1.1F, 0.05F, 0.1F, 0.1F, 1.1F, new CubeDeformation(0.0F))
		.texOffs(120, 27).addBox(-0.1F, -1.0F, -0.05F, 0.1F, 0.1F, 1.2F, new CubeDeformation(0.0F))
		.texOffs(120, 27).addBox(-0.1F, -0.9F, -0.35F, 0.2F, 0.5F, 1.5F, new CubeDeformation(0.0F))
		.texOffs(120, 28).addBox(-0.1F, -0.4F, -0.45F, 0.2F, 1.0F, 0.9F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(4.1F, -2.1609F, -2.5717F, 1.309F, 0.0F, 0.0F));

		PartDefinition cube_r20 = Horn1.addOrReplaceChild("cube_r20", CubeListBuilder.create().texOffs(119, 26).addBox(-0.1F, -0.5F, -0.9F, 0.2F, 1.0F, 2.7F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(4.1F, -3.2825F, -2.0867F, 0.3491F, 0.0F, 0.0F));

		PartDefinition Horn2 = headExtras.addOrReplaceChild("Horn2", CubeListBuilder.create(), PartPose.offset(-4.5739F, -9.282F, -1.1033F));

		PartDefinition cube_r21 = Horn2.addOrReplaceChild("cube_r21", CubeListBuilder.create().texOffs(117, 29).addBox(-1.0F, -2.0F, -1.0F, 0.3F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(9.0F, 6.1F, 2.6F, -0.9599F, 0.0F, 0.0F));

		PartDefinition cube_r22 = Horn2.addOrReplaceChild("cube_r22", CubeListBuilder.create().texOffs(117, 29).addBox(-1.0F, -2.0F, -1.0F, 0.3F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(9.0F, 3.0F, 2.6F, -0.9599F, 0.0F, 0.0F));

		PartDefinition cube_r23 = Horn2.addOrReplaceChild("cube_r23", CubeListBuilder.create().texOffs(120, 27).mirror().addBox(-0.1F, 0.0F, -0.35F, 0.2F, 0.4F, 1.2F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-0.0261F, -6.6184F, 0.482F, 1.7628F, 0.0F, 0.0F));

		PartDefinition cube_r24 = Horn2.addOrReplaceChild("cube_r24", CubeListBuilder.create().texOffs(120, 27).mirror().addBox(-0.1F, -0.1F, -0.35F, 0.2F, 0.2F, 1.2F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-0.0261F, -6.6184F, 0.482F, 1.5882F, 0.0F, 0.0F));

		PartDefinition cube_r25 = Horn2.addOrReplaceChild("cube_r25", CubeListBuilder.create().texOffs(121, 28).mirror().addBox(-0.05F, 0.0468F, -0.201F, 0.1F, 0.2F, 0.5F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.0239F, -7.0648F, 0.2742F, 1.021F, 0.0F, 0.0F));

		PartDefinition cube_r26 = Horn2.addOrReplaceChild("cube_r26", CubeListBuilder.create().texOffs(121, 28).mirror().addBox(-0.1F, 0.6F, -0.75F, 0.1F, 0.2F, 0.7F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.0739F, -6.9026F, -0.4231F, 1.5882F, 0.0F, 0.0F));

		PartDefinition cube_r27 = Horn2.addOrReplaceChild("cube_r27", CubeListBuilder.create().texOffs(121, 28).mirror().addBox(0.0F, 0.0F, -0.3F, 0.0F, 0.2F, 0.7F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.0739F, -6.4614F, 0.0847F, 1.1519F, 0.0F, 0.0F));

		PartDefinition cube_r28 = Horn2.addOrReplaceChild("cube_r28", CubeListBuilder.create().texOffs(119, 27).mirror().addBox(0.15F, -1.1624F, -1.4F, 0.0F, 0.2F, 2.0F, new CubeDeformation(0.0F)).mirror(false)
		.texOffs(119, 27).mirror().addBox(0.05F, -1.0F, -1.4F, 0.1F, 0.3F, 1.9F, new CubeDeformation(0.0F)).mirror(false)
		.texOffs(119, 27).mirror().addBox(-0.05F, -0.8F, -1.4F, 0.2F, 0.3F, 1.9F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-0.0761F, -6.3156F, 1.2997F, 2.1642F, 0.0F, 0.0F));

		PartDefinition cube_r29 = Horn2.addOrReplaceChild("cube_r29", CubeListBuilder.create().texOffs(112, 27).mirror().addBox(0.1F, -0.85F, -4.95F, 0.0F, 0.2F, 5.9F, new CubeDeformation(0.0F)).mirror(false)
		.texOffs(112, 27).mirror().addBox(0.0F, -0.65F, -4.95F, 0.1F, 0.2F, 5.9F, new CubeDeformation(0.0F)).mirror(false)
		.texOffs(112, 26).mirror().addBox(-0.1F, -0.45F, -5.05F, 0.2F, 0.3F, 6.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-0.0261F, -4.0868F, 2.019F, 1.8151F, 0.0F, 0.0F));

		PartDefinition cube_r30 = Horn2.addOrReplaceChild("cube_r30", CubeListBuilder.create().texOffs(121, 28).mirror().addBox(0.05F, 0.05F, 2.35F, 0.1F, 0.1F, 0.1F, new CubeDeformation(0.0F)).mirror(false)
		.texOffs(121, 28).mirror().addBox(0.05F, -0.05F, 2.25F, 0.1F, 0.2F, 0.1F, new CubeDeformation(0.0F)).mirror(false)
		.texOffs(121, 28).mirror().addBox(0.05F, -0.15F, 2.15F, 0.1F, 0.3F, 0.1F, new CubeDeformation(0.0F)).mirror(false)
		.texOffs(121, 28).mirror().addBox(0.05F, -0.25F, 2.05F, 0.1F, 0.4F, 0.1F, new CubeDeformation(0.0F)).mirror(false)
		.texOffs(121, 28).mirror().addBox(0.05F, -0.35F, 1.95F, 0.1F, 0.5F, 0.1F, new CubeDeformation(0.0F)).mirror(false)
		.texOffs(121, 28).mirror().addBox(0.05F, -0.45F, 1.85F, 0.1F, 0.6F, 0.1F, new CubeDeformation(0.0F)).mirror(false)
		.texOffs(120, 28).mirror().addBox(-0.05F, -0.55F, 0.85F, 0.2F, 0.7F, 1.0F, new CubeDeformation(0.0F)).mirror(false)
		.texOffs(119, 27).mirror().addBox(-0.15F, -0.85F, -1.05F, 0.3F, 1.0F, 1.9F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-0.0761F, -6.2212F, 1.7856F, 2.1642F, 0.0F, 0.0F));

		PartDefinition cube_r31 = Horn2.addOrReplaceChild("cube_r31", CubeListBuilder.create().texOffs(118, 26).mirror().addBox(-0.2F, -0.85F, 1.45F, 0.3F, 1.0F, 2.9F, new CubeDeformation(0.0F)).mirror(false)
		.texOffs(118, 26).mirror().addBox(-0.2F, -0.85F, -1.45F, 0.3F, 1.0F, 2.9F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-0.0261F, -1.1754F, 3.4559F, 1.8151F, 0.0F, 0.0F));

		PartDefinition cube_r32 = Horn2.addOrReplaceChild("cube_r32", CubeListBuilder.create().texOffs(118, 26).mirror().addBox(-0.2F, 0.45F, -0.25F, 0.3F, 1.0F, 2.9F, new CubeDeformation(0.0F)).mirror(false)
		.texOffs(120, 27).mirror().addBox(-0.1F, -1.35F, 0.65F, 0.2F, 1.1F, 1.2F, new CubeDeformation(0.0F)).mirror(false)
		.texOffs(114, 27).mirror().addBox(-0.1F, -0.25F, -3.25F, 0.2F, 1.7F, 5.1F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-0.0261F, 2.6321F, 2.2369F, 1.4835F, 0.0F, 0.0F));

		PartDefinition cube_r33 = Horn2.addOrReplaceChild("cube_r33", CubeListBuilder.create().texOffs(118, 26).mirror().addBox(-0.1F, -0.85F, -1.85F, 0.2F, 1.1F, 2.7F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-0.0261F, 2.1104F, 1.9763F, 2.5744F, 0.0F, 0.0F));

		PartDefinition cube_r34 = Horn2.addOrReplaceChild("cube_r34", CubeListBuilder.create().texOffs(117, 25).mirror().addBox(-0.1F, 1.0F, -2.0F, 0.2F, 1.1F, 3.9F, new CubeDeformation(0.0F)).mirror(false)
		.texOffs(116, 24).mirror().addBox(-0.1F, 0.0F, -2.0F, 0.2F, 1.1F, 4.9F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-0.0261F, 3.8782F, 0.1379F, 2.3562F, 0.0F, 0.0F));

		PartDefinition cube_r35 = Horn2.addOrReplaceChild("cube_r35", CubeListBuilder.create().texOffs(116, 24).mirror().addBox(-0.1F, -0.8F, -1.3F, 0.2F, 1.1F, 4.9F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-0.0261F, 3.8782F, 0.1379F, 2.3562F, 0.0F, 0.0F));

		PartDefinition cube_r36 = Horn2.addOrReplaceChild("cube_r36", CubeListBuilder.create().texOffs(120, 27).mirror().addBox(-0.1F, -2.05F, -0.95F, 0.2F, 3.7F, 1.1F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-0.0261F, 1.1039F, -0.1412F, 1.6581F, 0.0F, 0.0F));

		PartDefinition cube_r37 = Horn2.addOrReplaceChild("cube_r37", CubeListBuilder.create().texOffs(120, 27).mirror().addBox(-0.1F, -0.5F, -0.5F, 0.2F, 3.7F, 1.1F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-0.0261F, 6.2147F, -0.5716F, 2.0071F, 0.0F, 0.0F));

		PartDefinition cube_r38 = Horn2.addOrReplaceChild("cube_r38", CubeListBuilder.create().texOffs(120, 27).mirror().addBox(-0.1F, -0.5F, -0.5F, 0.2F, 4.0F, 1.1F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-0.0261F, 7.0147F, -0.5716F, 2.0071F, 0.0F, 0.0F));

		PartDefinition cube_r39 = Horn2.addOrReplaceChild("cube_r39", CubeListBuilder.create().texOffs(121, 28).mirror().addBox(0.0F, -2.1F, 1.05F, 0.1F, 0.1F, 0.1F, new CubeDeformation(0.0F)).mirror(false)
		.texOffs(121, 28).mirror().addBox(0.0F, -2.0F, 0.95F, 0.1F, 0.1F, 0.2F, new CubeDeformation(0.0F)).mirror(false)
		.texOffs(121, 28).mirror().addBox(0.0F, -1.9F, 0.85F, 0.1F, 0.1F, 0.3F, new CubeDeformation(0.0F)).mirror(false)
		.texOffs(121, 28).mirror().addBox(0.0F, -1.8F, 0.75F, 0.1F, 0.1F, 0.4F, new CubeDeformation(0.0F)).mirror(false)
		.texOffs(121, 28).mirror().addBox(0.0F, -1.7F, 0.65F, 0.1F, 0.1F, 0.5F, new CubeDeformation(0.0F)).mirror(false)
		.texOffs(121, 28).mirror().addBox(0.0F, -1.6F, 0.55F, 0.1F, 0.1F, 0.6F, new CubeDeformation(0.0F)).mirror(false)
		.texOffs(121, 28).mirror().addBox(0.0F, -1.5F, 0.45F, 0.1F, 0.1F, 0.7F, new CubeDeformation(0.0F)).mirror(false)
		.texOffs(120, 28).mirror().addBox(0.0F, -1.4F, 0.35F, 0.1F, 0.1F, 0.8F, new CubeDeformation(0.0F)).mirror(false)
		.texOffs(120, 28).mirror().addBox(0.0F, -1.3F, 0.25F, 0.1F, 0.1F, 0.9F, new CubeDeformation(0.0F)).mirror(false)
		.texOffs(120, 27).mirror().addBox(0.0F, -1.2F, 0.15F, 0.1F, 0.1F, 1.0F, new CubeDeformation(0.0F)).mirror(false)
		.texOffs(120, 27).mirror().addBox(0.0F, -1.1F, 0.05F, 0.1F, 0.1F, 1.1F, new CubeDeformation(0.0F)).mirror(false)
		.texOffs(120, 27).mirror().addBox(0.0F, -1.0F, -0.05F, 0.1F, 0.1F, 1.2F, new CubeDeformation(0.0F)).mirror(false)
		.texOffs(120, 27).mirror().addBox(-0.1F, -0.9F, -0.35F, 0.2F, 0.5F, 1.5F, new CubeDeformation(0.0F)).mirror(false)
		.texOffs(120, 28).mirror().addBox(-0.1F, -0.4F, -0.45F, 0.2F, 1.0F, 0.9F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-0.0261F, 7.1211F, -1.2685F, 1.309F, 0.0F, 0.0F));

		PartDefinition cube_r40 = Horn2.addOrReplaceChild("cube_r40", CubeListBuilder.create().texOffs(119, 26).mirror().addBox(-0.1F, -0.5F, -0.9F, 0.2F, 1.0F, 2.7F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-0.0261F, 5.9995F, -0.7835F, 0.3491F, 0.0F, 0.0F));

		PartDefinition Visor = head.addOrReplaceChild("Visor", CubeListBuilder.create(), PartPose.offset(-0.1F, 0.0F, -0.6F));

		PartDefinition cube_r41 = Visor.addOrReplaceChild("cube_r41", CubeListBuilder.create().texOffs(112, 9).addBox(-1.5F, -0.5F, -2.0F, 3.0F, 1.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.4607F, -4.1709F, -3.1862F, 1.2591F, -0.2976F, -0.7383F));

		PartDefinition cube_r42 = Visor.addOrReplaceChild("cube_r42", CubeListBuilder.create().texOffs(112, 9).addBox(-1.5F, -0.5F, -2.0F, 3.0F, 1.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.4607F, -4.1709F, -3.1862F, 0.922F, -0.5435F, -0.5985F));

		PartDefinition cube_r43 = Visor.addOrReplaceChild("cube_r43", CubeListBuilder.create().texOffs(112, 9).addBox(-1.0F, -2.0F, 0.0F, 4.0F, 2.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.6F, -3.0F, -5.0F, 0.5299F, -0.7119F, -0.3655F));

		PartDefinition cube_r44 = Visor.addOrReplaceChild("cube_r44", CubeListBuilder.create().texOffs(111, -1).addBox(-4.0F, -2.0F, -4.0F, 5.0F, 2.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -6.0F, -1.2F, 0.5299F, -0.7119F, -0.3655F));


		PartDefinition chestExtras = body.addOrReplaceChild("chestExtras", CubeListBuilder.create().texOffs(120, 41).addBox(-3.8F, -3.1F, -0.1F, 2.3F, 2.0F, 0.2F, new CubeDeformation(0.25F)), PartPose.offset(2.705F, 4.1071F, -2.684F));

		PartDefinition cube_r45 = chestExtras.addOrReplaceChild("cube_r45", CubeListBuilder.create().texOffs(121, 42).addBox(-1.0F, -0.9F, -1.1F, 1.6F, 1.5F, 0.0F, new CubeDeformation(0.25F))
		.texOffs(121, 42).addBox(-1.2F, -1.1F, -1.0F, 2.0F, 1.9F, 0.0F, new CubeDeformation(0.25F))
		.texOffs(121, 42).addBox(-1.3F, -1.2F, -0.8F, 2.3F, 2.2F, 0.5F, new CubeDeformation(0.25F)), PartPose.offsetAndRotation(-2.7F, -1.0F, 0.9F, 0.1555F, -0.1536F, 0.7734F));

		PartDefinition cube_r46 = chestExtras.addOrReplaceChild("cube_r46", CubeListBuilder.create().texOffs(121, 39).addBox(-0.625F, -0.225F, 0.05F, 0.75F, 0.45F, 0.0F, new CubeDeformation(0.25F))
		.texOffs(121, 39).addBox(-0.625F, -0.225F, 0.05F, 0.75F, 0.45F, 0.0F, new CubeDeformation(0.25F)), PartPose.offsetAndRotation(0.0318F, 1.1177F, 0.0308F, 0.0F, 0.0F, 2.4784F));

		PartDefinition cube_r47 = chestExtras.addOrReplaceChild("cube_r47", CubeListBuilder.create().texOffs(121, 39).addBox(-0.125F, -0.275F, 0.05F, 0.25F, 0.55F, 0.0F, new CubeDeformation(0.25F))
		.texOffs(121, 39).addBox(-0.125F, -0.275F, 0.05F, 0.25F, 0.55F, 0.0F, new CubeDeformation(0.25F)), PartPose.offsetAndRotation(0.1396F, 0.942F, 0.0308F, 0.0F, 0.0F, 1.8762F));

		PartDefinition cube_r48 = chestExtras.addOrReplaceChild("cube_r48", CubeListBuilder.create().texOffs(121, 39).addBox(-0.225F, -0.275F, 0.05F, 0.25F, 0.65F, 0.0F, new CubeDeformation(0.25F))
		.texOffs(121, 39).addBox(-0.225F, -0.275F, 0.05F, 0.25F, 0.65F, 0.0F, new CubeDeformation(0.25F)), PartPose.offsetAndRotation(0.2522F, 0.9013F, 0.0308F, 0.0F, 0.0F, 2.3126F));

		PartDefinition cube_r49 = chestExtras.addOrReplaceChild("cube_r49", CubeListBuilder.create().texOffs(121, 39).addBox(0.075F, -0.375F, 0.05F, 0.25F, 0.75F, 0.0F, new CubeDeformation(0.25F))
		.texOffs(121, 39).addBox(-0.125F, -0.375F, 0.05F, 0.25F, 0.75F, 0.0F, new CubeDeformation(0.25F))
		.texOffs(121, 39).addBox(0.075F, -0.375F, 0.05F, 0.25F, 0.75F, 0.0F, new CubeDeformation(0.25F))
		.texOffs(121, 39).addBox(-0.125F, -0.375F, 0.05F, 0.25F, 0.75F, 0.0F, new CubeDeformation(0.25F)), PartPose.offsetAndRotation(0.3388F, 0.5108F, 0.0308F, 0.0F, 0.0F, 1.789F));

		PartDefinition cube_r50 = chestExtras.addOrReplaceChild("cube_r50", CubeListBuilder.create().texOffs(121, 39).mirror().addBox(-1.0F, -1.1F, 0.7F, 2.0F, 1.6F, 0.3F, new CubeDeformation(0.25F)).mirror(false)
		.texOffs(121, 39).mirror().addBox(-1.0F, -1.0F, 0.7F, 2.0F, 1.5F, 0.3F, new CubeDeformation(0.25F)).mirror(false), PartPose.offsetAndRotation(-3.7052F, -1.7071F, -0.9192F, 0.0F, 0.0F, 0.6109F));

		PartDefinition cube_r51 = chestExtras.addOrReplaceChild("cube_r51", CubeListBuilder.create().texOffs(121, 39).mirror().addBox(-1.0F, -0.5F, 0.0F, 2.0F, 1.7F, 0.2F, new CubeDeformation(0.25F)).mirror(false)
		.texOffs(121, 39).mirror().addBox(-1.0F, -0.5F, 0.0F, 2.0F, 1.7F, 0.2F, new CubeDeformation(0.25F)).mirror(false), PartPose.offsetAndRotation(-4.5567F, -2.1638F, -0.1192F, 0.0F, 0.0F, -0.5236F));

		PartDefinition cube_r52 = chestExtras.addOrReplaceChild("cube_r52", CubeListBuilder.create().texOffs(121, 39).mirror().addBox(-1.0F, -0.95F, 0.1F, 2.0F, 1.3F, 0.0F, new CubeDeformation(0.25F)).mirror(false)
		.texOffs(121, 39).mirror().addBox(-1.0F, -0.95F, 0.1F, 2.0F, 1.3F, 0.0F, new CubeDeformation(0.25F)).mirror(false), PartPose.offsetAndRotation(-5.0817F, -0.8607F, -0.0192F, 0.0F, 0.0F, -1.1345F));

		PartDefinition cube_r53 = chestExtras.addOrReplaceChild("cube_r53", CubeListBuilder.create().texOffs(121, 39).mirror().addBox(-0.075F, -0.525F, 0.05F, 0.35F, 1.05F, 0.0F, new CubeDeformation(0.25F)).mirror(false)
		.texOffs(121, 39).mirror().addBox(-0.075F, -0.525F, 0.05F, 0.35F, 1.05F, 0.0F, new CubeDeformation(0.25F)).mirror(false), PartPose.offsetAndRotation(-5.6946F, 0.0396F, 0.0308F, 0.0F, 0.0F, -1.1083F));

		PartDefinition cube_r54 = chestExtras.addOrReplaceChild("cube_r54", CubeListBuilder.create().texOffs(121, 39).mirror().addBox(-1.45F, -0.5F, 0.1F, 0.35F, 0.85F, 0.0F, new CubeDeformation(0.25F)).mirror(false)
		.texOffs(121, 39).mirror().addBox(-1.65F, -0.4F, 0.1F, 0.25F, 0.75F, 0.0F, new CubeDeformation(0.25F)).mirror(false)
		.texOffs(121, 39).mirror().addBox(-1.45F, -0.5F, 0.1F, 0.35F, 0.85F, 0.0F, new CubeDeformation(0.25F)).mirror(false)
		.texOffs(121, 39).mirror().addBox(-1.65F, -0.4F, 0.1F, 0.25F, 0.75F, 0.0F, new CubeDeformation(0.25F)).mirror(false), PartPose.offsetAndRotation(-5.0817F, -0.8607F, -0.0192F, 0.0F, 0.0F, -1.1345F));

		PartDefinition cube_r55 = chestExtras.addOrReplaceChild("cube_r55", CubeListBuilder.create().texOffs(121, 39).mirror().addBox(-0.125F, -0.375F, 0.05F, 0.25F, 0.75F, 0.0F, new CubeDeformation(0.25F)).mirror(false)
		.texOffs(121, 39).mirror().addBox(-0.325F, -0.375F, 0.05F, 0.25F, 0.75F, 0.0F, new CubeDeformation(0.25F)).mirror(false)
		.texOffs(121, 39).mirror().addBox(-0.125F, -0.375F, 0.05F, 0.25F, 0.75F, 0.0F, new CubeDeformation(0.25F)).mirror(false)
		.texOffs(121, 39).mirror().addBox(-0.325F, -0.375F, 0.05F, 0.25F, 0.75F, 0.0F, new CubeDeformation(0.25F)).mirror(false), PartPose.offsetAndRotation(-5.7489F, 0.5108F, 0.0308F, 0.0F, 0.0F, -1.789F));

		PartDefinition cube_r56 = chestExtras.addOrReplaceChild("cube_r56", CubeListBuilder.create().texOffs(121, 39).mirror().addBox(-0.125F, -0.375F, 0.05F, 0.25F, 0.75F, 0.0F, new CubeDeformation(0.25F)).mirror(false)
		.texOffs(121, 39).mirror().addBox(-0.125F, -0.375F, 0.05F, 0.25F, 0.75F, 0.0F, new CubeDeformation(0.25F)).mirror(false), PartPose.offsetAndRotation(-5.7646F, 0.3114F, 0.0308F, 0.0F, 0.0F, -1.6144F));

		PartDefinition cube_r57 = chestExtras.addOrReplaceChild("cube_r57", CubeListBuilder.create().texOffs(121, 39).mirror().addBox(0.175F, -0.275F, 0.05F, 0.65F, 0.75F, 0.0F, new CubeDeformation(0.25F)).mirror(false)
		.texOffs(121, 39).mirror().addBox(0.175F, -0.275F, 0.05F, 0.65F, 0.75F, 0.0F, new CubeDeformation(0.25F)).mirror(false), PartPose.offsetAndRotation(-5.7646F, 0.3114F, 0.0308F, 0.0F, 0.0F, -1.9286F));

		PartDefinition cube_r58 = chestExtras.addOrReplaceChild("cube_r58", CubeListBuilder.create().texOffs(121, 39).mirror().addBox(-0.025F, -0.275F, 0.05F, 0.25F, 0.65F, 0.0F, new CubeDeformation(0.25F)).mirror(false)
		.texOffs(121, 39).mirror().addBox(-0.025F, -0.275F, 0.05F, 0.25F, 0.65F, 0.0F, new CubeDeformation(0.25F)).mirror(false), PartPose.offsetAndRotation(-5.6623F, 0.9013F, 0.0308F, 0.0F, 0.0F, -2.3126F));

		PartDefinition cube_r59 = chestExtras.addOrReplaceChild("cube_r59", CubeListBuilder.create().texOffs(121, 39).mirror().addBox(-0.125F, -0.275F, 0.05F, 0.25F, 0.55F, 0.0F, new CubeDeformation(0.25F)).mirror(false)
		.texOffs(121, 39).mirror().addBox(-0.125F, -0.275F, 0.05F, 0.25F, 0.55F, 0.0F, new CubeDeformation(0.25F)).mirror(false), PartPose.offsetAndRotation(-5.5497F, 0.942F, 0.0308F, 0.0F, 0.0F, -1.8762F));

		PartDefinition cube_r60 = chestExtras.addOrReplaceChild("cube_r60", CubeListBuilder.create().texOffs(121, 39).mirror().addBox(-0.125F, -0.225F, 0.05F, 0.75F, 0.45F, 0.0F, new CubeDeformation(0.25F)).mirror(false)
		.texOffs(121, 39).mirror().addBox(-0.125F, -0.225F, 0.05F, 0.75F, 0.45F, 0.0F, new CubeDeformation(0.25F)).mirror(false), PartPose.offsetAndRotation(-5.4419F, 1.1177F, 0.0308F, 0.0F, 0.0F, -2.4784F));

		PartDefinition cube_r61 = chestExtras.addOrReplaceChild("cube_r61", CubeListBuilder.create().texOffs(121, 39).addBox(-0.825F, -0.275F, 0.05F, 0.65F, 0.75F, 0.0F, new CubeDeformation(0.25F))
		.texOffs(121, 39).addBox(-0.825F, -0.275F, 0.05F, 0.65F, 0.75F, 0.0F, new CubeDeformation(0.25F)), PartPose.offsetAndRotation(0.3545F, 0.3114F, 0.0308F, 0.0F, 0.0F, 1.9286F));

		PartDefinition cube_r62 = chestExtras.addOrReplaceChild("cube_r62", CubeListBuilder.create().texOffs(121, 39).addBox(-0.125F, -0.375F, 0.05F, 0.25F, 0.75F, 0.0F, new CubeDeformation(0.25F))
		.texOffs(121, 39).addBox(-0.125F, -0.375F, 0.05F, 0.25F, 0.75F, 0.0F, new CubeDeformation(0.25F)), PartPose.offsetAndRotation(0.3545F, 0.3114F, 0.0308F, 0.0F, 0.0F, 1.6144F));

		PartDefinition cube_r63 = chestExtras.addOrReplaceChild("cube_r63", CubeListBuilder.create().texOffs(121, 39).addBox(1.4F, -0.4F, 0.1F, 0.25F, 0.75F, 0.0F, new CubeDeformation(0.25F))
		.texOffs(121, 39).addBox(1.1F, -0.5F, 0.1F, 0.35F, 0.85F, 0.0F, new CubeDeformation(0.25F))
		.texOffs(121, 39).addBox(1.4F, -0.4F, 0.1F, 0.25F, 0.75F, 0.0F, new CubeDeformation(0.25F))
		.texOffs(121, 39).addBox(1.1F, -0.5F, 0.1F, 0.35F, 0.85F, 0.0F, new CubeDeformation(0.25F)), PartPose.offsetAndRotation(-0.3284F, -0.8607F, -0.0192F, 0.0F, 0.0F, 1.1345F));

		PartDefinition cube_r64 = chestExtras.addOrReplaceChild("cube_r64", CubeListBuilder.create().texOffs(121, 39).addBox(-0.275F, -0.525F, 0.05F, 0.35F, 1.05F, 0.0F, new CubeDeformation(0.25F))
		.texOffs(121, 39).addBox(-0.275F, -0.525F, 0.05F, 0.35F, 1.05F, 0.0F, new CubeDeformation(0.25F)), PartPose.offsetAndRotation(0.2846F, 0.0396F, 0.0308F, 0.0F, 0.0F, 1.1083F));

		PartDefinition cube_r65 = chestExtras.addOrReplaceChild("cube_r65", CubeListBuilder.create().texOffs(121, 39).addBox(-1.0F, -0.95F, 0.1F, 2.0F, 1.3F, 0.0F, new CubeDeformation(0.25F))
		.texOffs(121, 39).addBox(-1.0F, -0.95F, 0.1F, 2.0F, 1.3F, 0.0F, new CubeDeformation(0.25F)), PartPose.offsetAndRotation(-0.3284F, -0.8607F, -0.0192F, 0.0F, 0.0F, 1.1345F));

		PartDefinition cube_r66 = chestExtras.addOrReplaceChild("cube_r66", CubeListBuilder.create().texOffs(121, 39).addBox(-1.0F, -0.5F, 0.0F, 2.0F, 1.7F, 0.2F, new CubeDeformation(0.25F))
		.texOffs(121, 39).addBox(-1.0F, -0.5F, 0.0F, 2.0F, 1.7F, 0.2F, new CubeDeformation(0.25F)), PartPose.offsetAndRotation(-0.8534F, -2.1638F, -0.1192F, 0.0F, 0.0F, 0.5236F));

		PartDefinition cube_r67 = chestExtras.addOrReplaceChild("cube_r67", CubeListBuilder.create().texOffs(121, 39).addBox(-1.0F, -1.0F, 0.7F, 2.0F, 1.5F, 0.3F, new CubeDeformation(0.25F))
		.texOffs(121, 39).addBox(-1.0F, -1.1F, 0.7F, 2.0F, 1.6F, 0.3F, new CubeDeformation(0.25F)), PartPose.offsetAndRotation(-1.7049F, -1.7071F, -0.9192F, 0.0F, 0.0F, -0.6109F));

		PartDefinition Hips = body.addOrReplaceChild("Hips", CubeListBuilder.create().texOffs(115, 59).addBox(-0.8F, -1.3F, -2.3F, 2.0F, 2.0F, 4.2F, new CubeDeformation(0.3F))
		.texOffs(115, 59).mirror().addBox(5.7813F, -1.3F, -2.3F, 2.0F, 2.0F, 4.2F, new CubeDeformation(0.3F)).mirror(false), PartPose.offset(-3.3906F, 11.7423F, 0.1F));

		PartDefinition cube_r68 = Hips.addOrReplaceChild("cube_r68", CubeListBuilder.create().texOffs(117, 49).mirror().addBox(-0.5F, -1.2F, -2.4F, 1.0F, 2.8F, 4.4F, new CubeDeformation(0.3F)).mirror(false), PartPose.offsetAndRotation(6.4906F, 0.5577F, 0.0F, 0.0F, 0.0F, -1.1345F));

		PartDefinition cube_r69 = Hips.addOrReplaceChild("cube_r69", CubeListBuilder.create().texOffs(117, 49).mirror().addBox(-0.5F, -1.2F, -2.4F, 1.0F, 2.4F, 4.4F, new CubeDeformation(0.3F)).mirror(false), PartPose.offsetAndRotation(6.8906F, -0.6423F, 0.0F, 0.0F, 0.0F, 0.9163F));

		PartDefinition cube_r70 = Hips.addOrReplaceChild("cube_r70", CubeListBuilder.create().texOffs(117, 49).addBox(-0.5F, -1.2F, -2.4F, 1.0F, 2.8F, 4.4F, new CubeDeformation(0.3F)), PartPose.offsetAndRotation(0.4906F, 0.5577F, 0.0F, 0.0F, 0.0F, 1.1345F));

		PartDefinition cube_r71 = Hips.addOrReplaceChild("cube_r71", CubeListBuilder.create().texOffs(117, 49).addBox(-0.5F, -1.2F, -2.4F, 1.0F, 2.4F, 4.4F, new CubeDeformation(0.3F)), PartPose.offsetAndRotation(0.0906F, -0.6423F, 0.0F, 0.0F, 0.0F, -0.9163F));


		PartDefinition Pauldron = leftArm.addOrReplaceChild("Pauldron", CubeListBuilder.create(), PartPose.offsetAndRotation(3.0512F, -0.2628F, 0.0179F, 0.0F, 0.0F, 0.48F));

		PartDefinition cube_r72 = Pauldron.addOrReplaceChild("cube_r72", CubeListBuilder.create().texOffs(117, 50).addBox(-0.1032F, -0.324F, -2.05F, 0.2F, 0.3F, 4.1F, new CubeDeformation(0.5F)), PartPose.offsetAndRotation(-3.545F, -1.0817F, 0.0321F, 0.0F, 0.0F, -1.885F));

		PartDefinition cube_r73 = Pauldron.addOrReplaceChild("cube_r73", CubeListBuilder.create().texOffs(117, 50).addBox(-0.1316F, -0.1269F, -2.25F, 0.3F, 0.6F, 4.5F, new CubeDeformation(0.5F)), PartPose.offsetAndRotation(-3.545F, -1.0817F, 0.0321F, 0.0F, 0.0F, -1.7977F));

		PartDefinition cube_r74 = Pauldron.addOrReplaceChild("cube_r74", CubeListBuilder.create().texOffs(120, 53).addBox(-0.15F, -0.2F, -0.9F, 0.3F, 0.4F, 1.8F, new CubeDeformation(0.5F)), PartPose.offsetAndRotation(0.6808F, 0.1279F, 1.0821F, 0.4363F, 0.0F, -0.8203F));

		PartDefinition cube_r75 = Pauldron.addOrReplaceChild("cube_r75", CubeListBuilder.create().texOffs(120, 53).addBox(-0.15F, -0.5F, -0.8F, 0.3F, 0.7F, 1.7F, new CubeDeformation(0.5F)), PartPose.offsetAndRotation(0.9105F, 0.3422F, 1.1183F, 0.0873F, 0.0F, -0.8203F));

		PartDefinition cube_r76 = Pauldron.addOrReplaceChild("cube_r76", CubeListBuilder.create().texOffs(120, 53).addBox(-0.15F, -0.2F, -0.7F, 0.1F, 0.8F, 1.5F, new CubeDeformation(0.5F)), PartPose.offsetAndRotation(0.9105F, 0.3422F, 1.1183F, -0.384F, 0.0F, -0.8203F));

		PartDefinition cube_r77 = Pauldron.addOrReplaceChild("cube_r77", CubeListBuilder.create().texOffs(120, 53).addBox(-0.15F, -0.5F, -0.55F, 0.1F, 0.5F, 1.1F, new CubeDeformation(0.5F)), PartPose.offsetAndRotation(1.6023F, 0.9873F, 0.7901F, -0.5149F, 0.0F, -0.8203F));

		PartDefinition cube_r78 = Pauldron.addOrReplaceChild("cube_r78", CubeListBuilder.create().texOffs(120, 53).addBox(-0.15F, -0.5F, -0.55F, 0.1F, 0.5F, 1.1F, new CubeDeformation(0.5F)), PartPose.offsetAndRotation(1.6023F, 0.9873F, -0.8258F, 0.5149F, 0.0F, -0.8203F));

		PartDefinition cube_r79 = Pauldron.addOrReplaceChild("cube_r79", CubeListBuilder.create().texOffs(120, 53).addBox(-0.15F, -0.2F, -0.8F, 0.1F, 0.8F, 1.5F, new CubeDeformation(0.5F)), PartPose.offsetAndRotation(0.9105F, 0.3422F, -1.154F, 0.384F, 0.0F, -0.8203F));

		PartDefinition cube_r80 = Pauldron.addOrReplaceChild("cube_r80", CubeListBuilder.create().texOffs(120, 53).addBox(-0.15F, -0.5F, -0.9F, 0.3F, 0.7F, 1.7F, new CubeDeformation(0.5F)), PartPose.offsetAndRotation(0.9105F, 0.3422F, -1.154F, -0.0873F, 0.0F, -0.8203F));

		PartDefinition cube_r81 = Pauldron.addOrReplaceChild("cube_r81", CubeListBuilder.create().texOffs(120, 53).addBox(-0.15F, -0.2F, -0.9F, 0.3F, 0.4F, 1.8F, new CubeDeformation(0.5F)), PartPose.offsetAndRotation(0.6808F, 0.1279F, -1.1179F, -0.4363F, 0.0F, -0.8203F));

		PartDefinition cube_r82 = Pauldron.addOrReplaceChild("cube_r82", CubeListBuilder.create().texOffs(119, 52).addBox(-0.15F, -0.2F, -1.15F, 0.3F, 0.4F, 2.2F, new CubeDeformation(0.5F))
		.texOffs(117, 50).addBox(-0.15F, -0.6F, -2.05F, 0.3F, 0.4F, 4.1F, new CubeDeformation(0.5F)), PartPose.offsetAndRotation(0.6808F, 0.1279F, 0.0321F, 0.0F, 0.0F, -0.8203F));

		PartDefinition cube_r83 = Pauldron.addOrReplaceChild("cube_r83", CubeListBuilder.create().texOffs(117, 50).addBox(-0.15F, -0.35F, -2.25F, 0.3F, 0.7F, 4.5F, new CubeDeformation(0.5F)), PartPose.offsetAndRotation(-0.014F, -0.52F, 0.0321F, 0.0F, 0.0F, -0.8203F));

		PartDefinition cube_r84 = Pauldron.addOrReplaceChild("cube_r84", CubeListBuilder.create().texOffs(117, 50).addBox(-0.25F, -2.5F, -2.35F, 0.4F, 3.1F, 4.7F, new CubeDeformation(0.5F)), PartPose.offsetAndRotation(-0.7118F, -0.8472F, 0.0321F, 0.0F, 0.0F, -1.3963F));

		PartDefinition p2 = Pauldron.addOrReplaceChild("p2", CubeListBuilder.create().texOffs(94, 2).addBox(-2.9F, -2.0F, -2.1F, 2.4F, 1.0F, 4.2F, new CubeDeformation(0.5F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition GauntletL = leftArm.addOrReplaceChild("GauntletL", CubeListBuilder.create().texOffs(97, 13).addBox(4.0F, 5.55F, -0.5F, 0.2F, 2.25F, 1.0F, new CubeDeformation(0.2F))
		.texOffs(95, 11).addBox(4.0F, 6.05F, -1.25F, 0.2F, 1.25F, 2.5F, new CubeDeformation(0.2F)), PartPose.offset(-0.5F, 0.5F, 0.0F));

		PartDefinition cube_r85 = GauntletL.addOrReplaceChild("cube_r85", CubeListBuilder.create().texOffs(98, 14).addBox(0.1548F, 0.3054F, 0.2488F, 0.0F, 0.55F, 0.1F, new CubeDeformation(0.2F)), PartPose.offsetAndRotation(4.3244F, 2.3977F, 0.0142F, 0.6401F, 0.0419F, 0.2793F));

		PartDefinition cube_r86 = GauntletL.addOrReplaceChild("cube_r86", CubeListBuilder.create().texOffs(98, 14).addBox(0.1548F, 0.3054F, -0.3488F, 0.0F, 0.55F, 0.1F, new CubeDeformation(0.2F)), PartPose.offsetAndRotation(4.3244F, 2.3977F, -0.0142F, -0.6401F, -0.0419F, 0.2793F));

		PartDefinition cube_r87 = GauntletL.addOrReplaceChild("cube_r87", CubeListBuilder.create().texOffs(98, 14).addBox(-0.0467F, 0.1601F, 0.6809F, 0.0F, 0.65F, 0.1F, new CubeDeformation(0.2F)), PartPose.offsetAndRotation(4.3244F, 3.0977F, -0.0142F, 0.5958F, 0.0523F, 0.2653F));

		PartDefinition cube_r88 = GauntletL.addOrReplaceChild("cube_r88", CubeListBuilder.create().texOffs(98, 14).addBox(-0.0452F, 0.2054F, -0.7488F, 0.0F, 0.65F, 0.1F, new CubeDeformation(0.2F)), PartPose.offsetAndRotation(4.3244F, 3.0977F, -0.0142F, -0.6394F, -0.0523F, 0.2653F));

		PartDefinition cube_r89 = GauntletL.addOrReplaceChild("cube_r89", CubeListBuilder.create().texOffs(97, 13).addBox(0.0F, -0.025F, -0.45F, 0.1F, 0.65F, 0.4F, new CubeDeformation(0.2F)), PartPose.offsetAndRotation(4.2147F, 3.3761F, 1.2748F, 0.3776F, 0.0523F, 0.0908F));

		PartDefinition cube_r90 = GauntletL.addOrReplaceChild("cube_r90", CubeListBuilder.create().texOffs(97, 13).addBox(0.0F, -0.025F, 0.05F, 0.1F, 0.65F, 0.4F, new CubeDeformation(0.2F)), PartPose.offsetAndRotation(4.2147F, 3.3761F, -1.2748F, -0.3776F, -0.0523F, 0.0908F));

		PartDefinition cube_r91 = GauntletL.addOrReplaceChild("cube_r91", CubeListBuilder.create().texOffs(97, 13).addBox(-0.1F, -0.025F, -0.25F, 0.1F, 0.65F, 0.5F, new CubeDeformation(0.2F)), PartPose.offsetAndRotation(4.2147F, 4.1761F, -1.2748F, -0.1595F, -0.0523F, 0.0908F));

		PartDefinition cube_r92 = GauntletL.addOrReplaceChild("cube_r92", CubeListBuilder.create().texOffs(97, 13).addBox(-0.1F, -0.025F, -0.25F, 0.1F, 0.65F, 0.5F, new CubeDeformation(0.2F)), PartPose.offsetAndRotation(4.2147F, 4.1761F, 1.2748F, 0.1595F, 0.0523F, 0.0908F));

		PartDefinition cube_r93 = GauntletL.addOrReplaceChild("cube_r93", CubeListBuilder.create().texOffs(97, 13).addBox(-0.2F, -0.625F, -0.35F, 0.2F, 1.25F, 0.7F, new CubeDeformation(0.2F)), PartPose.offsetAndRotation(4.2F, 5.2661F, 0.9688F, -0.5212F, 0.0523F, 0.0908F));

		PartDefinition cube_r94 = GauntletL.addOrReplaceChild("cube_r94", CubeListBuilder.create().texOffs(97, 13).addBox(-0.2F, -0.625F, -0.35F, 0.2F, 1.25F, 0.7F, new CubeDeformation(0.2F)), PartPose.offsetAndRotation(4.2F, 5.2661F, 0.4688F, -1.1781F, 0.0F, 0.0F));

		PartDefinition cube_r95 = GauntletL.addOrReplaceChild("cube_r95", CubeListBuilder.create().texOffs(97, 13).addBox(-0.2F, -0.625F, -0.35F, 0.2F, 1.25F, 0.7F, new CubeDeformation(0.2F)), PartPose.offsetAndRotation(4.2F, 5.2661F, -0.4688F, 1.1781F, 0.0F, 0.0F));

		PartDefinition cube_r96 = GauntletL.addOrReplaceChild("cube_r96", CubeListBuilder.create().texOffs(97, 13).addBox(-0.2F, -0.625F, -0.35F, 0.2F, 1.25F, 0.7F, new CubeDeformation(0.2F)), PartPose.offsetAndRotation(4.2F, 5.2661F, -0.9688F, 0.5212F, -0.0523F, 0.0908F));

		PartDefinition cube_r97 = GauntletL.addOrReplaceChild("cube_r97", CubeListBuilder.create().texOffs(97, 13).addBox(-0.2F, -0.25F, -0.35F, 0.2F, 1.25F, 0.7F, new CubeDeformation(0.2F)), PartPose.offsetAndRotation(4.2F, 5.9F, -0.65F, 0.2182F, 0.0F, 0.0F));

		PartDefinition cube_r98 = GauntletL.addOrReplaceChild("cube_r98", CubeListBuilder.create().texOffs(97, 13).addBox(-0.2F, -1.0F, -0.35F, 0.2F, 1.9F, 0.7F, new CubeDeformation(0.2F)), PartPose.offsetAndRotation(4.2F, 6.9F, -0.65F, -0.2182F, 0.0F, 0.0F));

		PartDefinition cube_r99 = GauntletL.addOrReplaceChild("cube_r99", CubeListBuilder.create().texOffs(97, 13).addBox(-0.2F, -1.0F, -0.55F, 0.2F, 1.8F, 0.9F, new CubeDeformation(0.2F)), PartPose.offsetAndRotation(4.2F, 7.5F, -0.55F, -0.829F, 0.0F, 0.0F));

		PartDefinition cube_r100 = GauntletL.addOrReplaceChild("cube_r100", CubeListBuilder.create().texOffs(97, 13).addBox(-0.2F, -0.25F, -0.35F, 0.2F, 1.25F, 0.7F, new CubeDeformation(0.2F)), PartPose.offsetAndRotation(4.2F, 5.9F, 0.65F, -0.2182F, 0.0F, 0.0F));

		PartDefinition cube_r101 = GauntletL.addOrReplaceChild("cube_r101", CubeListBuilder.create().texOffs(97, 13).addBox(-0.2F, -1.0F, -0.35F, 0.2F, 1.9F, 0.7F, new CubeDeformation(0.2F)), PartPose.offsetAndRotation(4.2F, 6.9F, 0.65F, 0.2182F, 0.0F, 0.0F));

		PartDefinition cube_r102 = GauntletL.addOrReplaceChild("cube_r102", CubeListBuilder.create().texOffs(97, 13).addBox(-0.2F, -1.0F, -0.35F, 0.2F, 1.8F, 0.9F, new CubeDeformation(0.2F)), PartPose.offsetAndRotation(4.2F, 7.5F, 0.55F, 0.829F, 0.0F, 0.0F));

		PartDefinition Gem = GauntletL.addOrReplaceChild("Gem", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition cube_r103 = Gem.addOrReplaceChild("cube_r103", CubeListBuilder.create().texOffs(119, 13).addBox(-0.375F, -0.55F, -0.45F, 0.75F, 1.0F, 1.0F, new CubeDeformation(0.2F)), PartPose.offsetAndRotation(4.375F, 6.05F, 0.0F, 0.7854F, 0.0F, -0.48F));

		PartDefinition cube_r104 = Gem.addOrReplaceChild("cube_r104", CubeListBuilder.create().texOffs(118, 13).addBox(-0.375F, -0.75F, -0.75F, 0.75F, 1.5F, 1.5F, new CubeDeformation(0.2F)), PartPose.offsetAndRotation(4.375F, 6.75F, 0.0F, 0.0F, 0.0F, 0.0436F));


		PartDefinition Pauldron2 = rightArm.addOrReplaceChild("Pauldron2", CubeListBuilder.create(), PartPose.offsetAndRotation(-3.0512F, -0.2628F, 0.0179F, 0.0F, 0.0F, -0.48F));

		PartDefinition cube_r105 = Pauldron2.addOrReplaceChild("cube_r105", CubeListBuilder.create().texOffs(117, 51).mirror().addBox(-0.0968F, -0.324F, -2.05F, 0.2F, 0.3F, 4.1F, new CubeDeformation(0.5F)).mirror(false), PartPose.offsetAndRotation(3.545F, -1.0817F, 0.0321F, 0.0F, 0.0F, 1.885F));

		PartDefinition cube_r106 = Pauldron2.addOrReplaceChild("cube_r106", CubeListBuilder.create().texOffs(117, 51).mirror().addBox(-0.1684F, -0.1269F, -2.25F, 0.3F, 0.6F, 4.5F, new CubeDeformation(0.5F)).mirror(false), PartPose.offsetAndRotation(3.545F, -1.0817F, 0.0321F, 0.0F, 0.0F, 1.7977F));

		PartDefinition cube_r107 = Pauldron2.addOrReplaceChild("cube_r107", CubeListBuilder.create().texOffs(120, 54).mirror().addBox(-0.15F, -0.2F, -0.9F, 0.3F, 0.4F, 1.8F, new CubeDeformation(0.5F)).mirror(false), PartPose.offsetAndRotation(-0.6808F, 0.1279F, 1.0821F, 0.4363F, 0.0F, 0.8203F));

		PartDefinition cube_r108 = Pauldron2.addOrReplaceChild("cube_r108", CubeListBuilder.create().texOffs(120, 54).mirror().addBox(-0.15F, -0.5F, -0.8F, 0.3F, 0.7F, 1.7F, new CubeDeformation(0.5F)).mirror(false), PartPose.offsetAndRotation(-0.9105F, 0.3422F, 1.1183F, 0.0873F, 0.0F, 0.8203F));

		PartDefinition cube_r109 = Pauldron2.addOrReplaceChild("cube_r109", CubeListBuilder.create().texOffs(120, 54).mirror().addBox(0.05F, -0.2F, -0.7F, 0.1F, 0.8F, 1.5F, new CubeDeformation(0.5F)).mirror(false), PartPose.offsetAndRotation(-0.9105F, 0.3422F, 1.1183F, -0.384F, 0.0F, 0.8203F));

		PartDefinition cube_r110 = Pauldron2.addOrReplaceChild("cube_r110", CubeListBuilder.create().texOffs(120, 54).mirror().addBox(0.05F, -0.5F, -0.55F, 0.1F, 0.5F, 1.1F, new CubeDeformation(0.5F)).mirror(false), PartPose.offsetAndRotation(-1.6023F, 0.9873F, 0.7901F, -0.5149F, 0.0F, 0.8203F));

		PartDefinition cube_r111 = Pauldron2.addOrReplaceChild("cube_r111", CubeListBuilder.create().texOffs(120, 54).mirror().addBox(0.05F, -0.5F, -0.55F, 0.1F, 0.5F, 1.1F, new CubeDeformation(0.5F)).mirror(false), PartPose.offsetAndRotation(-1.6023F, 0.9873F, -0.8258F, 0.5149F, 0.0F, 0.8203F));

		PartDefinition cube_r112 = Pauldron2.addOrReplaceChild("cube_r112", CubeListBuilder.create().texOffs(120, 54).mirror().addBox(0.05F, -0.2F, -0.8F, 0.1F, 0.8F, 1.5F, new CubeDeformation(0.5F)).mirror(false), PartPose.offsetAndRotation(-0.9105F, 0.3422F, -1.154F, 0.384F, 0.0F, 0.8203F));

		PartDefinition cube_r113 = Pauldron2.addOrReplaceChild("cube_r113", CubeListBuilder.create().texOffs(120, 54).mirror().addBox(-0.15F, -0.5F, -0.9F, 0.3F, 0.7F, 1.7F, new CubeDeformation(0.5F)).mirror(false), PartPose.offsetAndRotation(-0.9105F, 0.3422F, -1.154F, -0.0873F, 0.0F, 0.8203F));

		PartDefinition cube_r114 = Pauldron2.addOrReplaceChild("cube_r114", CubeListBuilder.create().texOffs(120, 54).mirror().addBox(-0.15F, -0.2F, -0.9F, 0.3F, 0.4F, 1.8F, new CubeDeformation(0.5F)).mirror(false), PartPose.offsetAndRotation(-0.6808F, 0.1279F, -1.1179F, -0.4363F, 0.0F, 0.8203F));

		PartDefinition cube_r115 = Pauldron2.addOrReplaceChild("cube_r115", CubeListBuilder.create().texOffs(119, 53).mirror().addBox(-0.15F, -0.2F, -1.15F, 0.3F, 0.4F, 2.2F, new CubeDeformation(0.5F)).mirror(false)
		.texOffs(117, 51).mirror().addBox(-0.15F, -0.6F, -2.05F, 0.3F, 0.4F, 4.1F, new CubeDeformation(0.5F)).mirror(false), PartPose.offsetAndRotation(-0.6808F, 0.1279F, 0.0321F, 0.0F, 0.0F, 0.8203F));

		PartDefinition cube_r116 = Pauldron2.addOrReplaceChild("cube_r116", CubeListBuilder.create().texOffs(117, 51).mirror().addBox(-0.15F, -0.35F, -2.25F, 0.3F, 0.7F, 4.5F, new CubeDeformation(0.5F)).mirror(false), PartPose.offsetAndRotation(0.014F, -0.52F, 0.0321F, 0.0F, 0.0F, 0.8203F));

		PartDefinition cube_r117 = Pauldron2.addOrReplaceChild("cube_r117", CubeListBuilder.create().texOffs(117, 50).mirror().addBox(-0.15F, -2.5F, -2.35F, 0.4F, 3.1F, 4.7F, new CubeDeformation(0.5F)).mirror(false), PartPose.offsetAndRotation(0.7118F, -0.8472F, 0.0321F, 0.0F, 0.0F, 1.3963F));

		PartDefinition p3 = Pauldron2.addOrReplaceChild("p3", CubeListBuilder.create().texOffs(94, 2).mirror().addBox(0.5F, -2.0F, -2.1F, 2.4F, 1.0F, 4.2F, new CubeDeformation(0.5F)).mirror(false), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition GauntletR = rightArm.addOrReplaceChild("GauntletR", CubeListBuilder.create().texOffs(109, 72).addBox(-2.2164F, -0.3433F, -2.25F, 4.5F, 3.0F, 4.5F, new CubeDeformation(0.3F))
		.texOffs(107, 71).addBox(-2.5168F, -0.4533F, -2.35F, 5.0F, 0.75F, 4.7F, new CubeDeformation(0.3F))
		.texOffs(107, 71).addBox(-2.5168F, 2.2467F, -2.35F, 5.0F, 0.75F, 4.7F, new CubeDeformation(0.3F))
		.texOffs(107, 71).addBox(-2.5168F, 0.8467F, -2.35F, 5.0F, 0.75F, 4.7F, new CubeDeformation(0.3F))
		.texOffs(112, 81).addBox(-2.2F, -1.6F, -2.3F, 3.1F, 1.6F, 4.5F, new CubeDeformation(0.3F)), PartPose.offset(-1.0336F, 6.3433F, 0.0F));

		PartDefinition Base_r1 = GauntletR.addOrReplaceChild("Base_r1", CubeListBuilder.create().texOffs(107, 71).addBox(-2.5F, -0.375F, -2.35F, 5.1F, 0.75F, 4.7F, new CubeDeformation(0.3F)), PartPose.offsetAndRotation(-0.0168F, -1.0783F, 0.0F, 0.0F, 0.0F, 0.3054F));


		PartDefinition LegExtras1 = leftLeg.addOrReplaceChild("LegExtras1", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition Boot = LegExtras1.addOrReplaceChild("Boot", CubeListBuilder.create().texOffs(0, 122).addBox(-2.25F, 9.6F, -2.25F, 4.6F, 0.7F, 4.5F, new CubeDeformation(0.5F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition spike = Boot.addOrReplaceChild("spike", CubeListBuilder.create().texOffs(8, 124).addBox(-0.25F, -0.325F, -0.125F, 0.5F, 0.65F, 0.25F, new CubeDeformation(0.2F)), PartPose.offset(0.0F, 9.925F, 2.375F));

		PartDefinition cube_r118 = spike.addOrReplaceChild("cube_r118", CubeListBuilder.create().texOffs(28, 126).addBox(-0.025F, -0.1F, -0.3F, 0.05F, 0.2F, 0.6F, new CubeDeformation(0.2F)), PartPose.offsetAndRotation(0.025F, -2.7152F, 2.4409F, 0.6545F, 0.0F, 0.0F));

		PartDefinition cube_r119 = spike.addOrReplaceChild("cube_r119", CubeListBuilder.create().texOffs(28, 125).addBox(-0.025F, -0.45F, 0.2F, 0.05F, 0.2F, 1.4F, new CubeDeformation(0.2F)), PartPose.offsetAndRotation(0.025F, -0.9403F, 1.9617F, 1.1781F, 0.0F, 0.0F));

		PartDefinition cube_r120 = spike.addOrReplaceChild("cube_r120", CubeListBuilder.create().texOffs(27, 125).addBox(-0.025F, -0.55F, -0.325F, 0.05F, 0.2F, 1.6F, new CubeDeformation(0.2F)), PartPose.offsetAndRotation(0.025F, -0.4708F, 1.1133F, 0.5672F, 0.0F, 0.0F));

		PartDefinition cube_r121 = spike.addOrReplaceChild("cube_r121", CubeListBuilder.create().texOffs(28, 126).addBox(-0.025F, -0.45F, -0.8F, 0.05F, 0.2F, 0.65F, new CubeDeformation(0.2F)), PartPose.offsetAndRotation(0.025F, -0.45F, 1.0F, 0.2618F, 0.0F, 0.0F));

		PartDefinition cube_r122 = spike.addOrReplaceChild("cube_r122", CubeListBuilder.create().texOffs(29, 126).addBox(-0.075F, 0.0F, -0.35F, 0.15F, 0.2F, 0.5F, new CubeDeformation(0.2F)), PartPose.offsetAndRotation(0.025F, -2.8286F, 2.5851F, 0.48F, 0.0F, 0.0F));

		PartDefinition cube_r123 = spike.addOrReplaceChild("cube_r123", CubeListBuilder.create().texOffs(27, 125).addBox(-0.075F, -0.45F, 0.2F, 0.15F, 0.2F, 1.7F, new CubeDeformation(0.2F))
		.texOffs(29, 126).addBox(-0.125F, -0.25F, 2.0F, 0.25F, 0.5F, 0.3F, new CubeDeformation(0.2F))
		.texOffs(27, 125).addBox(-0.125F, -0.25F, 0.2F, 0.25F, 0.5F, 1.8F, new CubeDeformation(0.2F)), PartPose.offsetAndRotation(0.025F, -0.7403F, 1.9617F, 1.1781F, 0.0F, 0.0F));

		PartDefinition cube_r124 = spike.addOrReplaceChild("cube_r124", CubeListBuilder.create().texOffs(27, 125).addBox(-0.075F, -0.55F, -0.325F, 0.15F, 0.2F, 1.6F, new CubeDeformation(0.2F))
		.texOffs(27, 125).addBox(-0.125F, -0.35F, -0.325F, 0.25F, 0.5F, 1.6F, new CubeDeformation(0.2F)), PartPose.offsetAndRotation(0.025F, -0.2708F, 1.1133F, 0.5672F, 0.0F, 0.0F));

		PartDefinition cube_r125 = spike.addOrReplaceChild("cube_r125", CubeListBuilder.create().texOffs(28, 126).addBox(-0.075F, -0.45F, -0.8F, 0.15F, 0.2F, 0.65F, new CubeDeformation(0.2F))
		.texOffs(28, 126).addBox(-0.125F, -0.25F, -1.0F, 0.25F, 0.5F, 0.85F, new CubeDeformation(0.2F)), PartPose.offsetAndRotation(0.025F, -0.25F, 1.0F, 0.2618F, 0.0F, 0.0F));

		PartDefinition Thigh = LegExtras1.addOrReplaceChild("Thigh", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition cube_r126 = Thigh.addOrReplaceChild("cube_r126", CubeListBuilder.create().texOffs(36, 121).addBox(-0.125F, -2.6F, -2.25F, 0.35F, 1.6F, 4.5F, new CubeDeformation(0.3F)), PartPose.offsetAndRotation(3.525F, 2.0F, 0.0F, 0.0F, 0.0F, -1.7453F));

		PartDefinition cube_r127 = Thigh.addOrReplaceChild("cube_r127", CubeListBuilder.create().texOffs(36, 121).addBox(-0.125F, -2.6F, -2.25F, 0.35F, 1.6F, 4.5F, new CubeDeformation(0.3F)), PartPose.offsetAndRotation(3.525F, 3.3F, 0.0F, 0.0F, 0.0F, -1.7453F));


		PartDefinition LegExtras2 = rightLeg.addOrReplaceChild("LegExtras2", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition Boot2 = LegExtras2.addOrReplaceChild("Boot2", CubeListBuilder.create().texOffs(0, 122).mirror().addBox(-2.35F, 9.6F, -2.25F, 4.6F, 0.7F, 4.5F, new CubeDeformation(0.5F)).mirror(false), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition spike2 = Boot2.addOrReplaceChild("spike2", CubeListBuilder.create().texOffs(9, 123).mirror().addBox(-0.25F, -0.325F, -0.125F, 0.5F, 0.65F, 0.25F, new CubeDeformation(0.2F)).mirror(false), PartPose.offset(0.0F, 9.925F, 2.375F));

		PartDefinition cube_r128 = spike2.addOrReplaceChild("cube_r128", CubeListBuilder.create().texOffs(29, 125).mirror().addBox(-0.025F, -0.1F, -0.3F, 0.05F, 0.2F, 0.6F, new CubeDeformation(0.2F)).mirror(false), PartPose.offsetAndRotation(-0.025F, -2.7152F, 2.4409F, 0.6545F, 0.0F, 0.0F));

		PartDefinition cube_r129 = spike2.addOrReplaceChild("cube_r129", CubeListBuilder.create().texOffs(28, 125).mirror().addBox(-0.025F, -0.45F, 0.2F, 0.05F, 0.2F, 1.4F, new CubeDeformation(0.2F)).mirror(false), PartPose.offsetAndRotation(-0.025F, -0.9403F, 1.9617F, 1.1781F, 0.0F, 0.0F));

		PartDefinition cube_r130 = spike2.addOrReplaceChild("cube_r130", CubeListBuilder.create().texOffs(28, 125).mirror().addBox(-0.025F, -0.55F, -0.325F, 0.05F, 0.2F, 1.6F, new CubeDeformation(0.2F)).mirror(false), PartPose.offsetAndRotation(-0.025F, -0.4708F, 1.1133F, 0.5672F, 0.0F, 0.0F));

		PartDefinition cube_r131 = spike2.addOrReplaceChild("cube_r131", CubeListBuilder.create().texOffs(28, 126).mirror().addBox(-0.025F, -0.45F, -0.8F, 0.05F, 0.2F, 0.65F, new CubeDeformation(0.2F)).mirror(false), PartPose.offsetAndRotation(-0.025F, -0.45F, 1.0F, 0.2618F, 0.0F, 0.0F));

		PartDefinition cube_r132 = spike2.addOrReplaceChild("cube_r132", CubeListBuilder.create().texOffs(28, 126).mirror().addBox(-0.075F, 0.0F, -0.35F, 0.15F, 0.2F, 0.5F, new CubeDeformation(0.2F)).mirror(false), PartPose.offsetAndRotation(-0.025F, -2.8286F, 2.5851F, 0.48F, 0.0F, 0.0F));

		PartDefinition cube_r133 = spike2.addOrReplaceChild("cube_r133", CubeListBuilder.create().texOffs(27, 125).mirror().addBox(-0.075F, -0.45F, 0.2F, 0.15F, 0.2F, 1.7F, new CubeDeformation(0.2F)).mirror(false)
		.texOffs(29, 126).mirror().addBox(-0.125F, -0.25F, 2.0F, 0.25F, 0.5F, 0.3F, new CubeDeformation(0.2F)).mirror(false)
		.texOffs(27, 125).mirror().addBox(-0.125F, -0.25F, 0.2F, 0.25F, 0.5F, 1.8F, new CubeDeformation(0.2F)).mirror(false), PartPose.offsetAndRotation(-0.025F, -0.7403F, 1.9617F, 1.1781F, 0.0F, 0.0F));

		PartDefinition cube_r134 = spike2.addOrReplaceChild("cube_r134", CubeListBuilder.create().texOffs(27, 125).mirror().addBox(-0.075F, -0.55F, -0.325F, 0.15F, 0.2F, 1.6F, new CubeDeformation(0.2F)).mirror(false)
		.texOffs(27, 125).mirror().addBox(-0.125F, -0.35F, -0.325F, 0.25F, 0.5F, 1.6F, new CubeDeformation(0.2F)).mirror(false), PartPose.offsetAndRotation(-0.025F, -0.2708F, 1.1133F, 0.5672F, 0.0F, 0.0F));

		PartDefinition cube_r135 = spike2.addOrReplaceChild("cube_r135", CubeListBuilder.create().texOffs(28, 126).mirror().addBox(-0.075F, -0.45F, -0.8F, 0.15F, 0.2F, 0.65F, new CubeDeformation(0.2F)).mirror(false)
		.texOffs(28, 125).mirror().addBox(-0.125F, -0.25F, -1.0F, 0.25F, 0.5F, 0.85F, new CubeDeformation(0.2F)).mirror(false), PartPose.offsetAndRotation(-0.025F, -0.25F, 1.0F, 0.2618F, 0.0F, 0.0F));

		PartDefinition Thigh2 = LegExtras2.addOrReplaceChild("Thigh2", CubeListBuilder.create(), PartPose.offset(-0.4F, 0.0F, 0.0F));

		PartDefinition cube_r136 = Thigh2.addOrReplaceChild("cube_r136", CubeListBuilder.create().texOffs(36, 121).mirror().addBox(-0.225F, -2.6F, -2.25F, 0.35F, 1.6F, 4.5F, new CubeDeformation(0.3F)).mirror(false), PartPose.offsetAndRotation(-3.125F, 3.3F, 0.0F, 0.0F, 0.0F, 1.7453F));

		PartDefinition cube_r137 = Thigh2.addOrReplaceChild("cube_r137", CubeListBuilder.create().texOffs(36, 121).mirror().addBox(-0.225F, -2.6F, -2.25F, 0.35F, 1.6F, 4.5F, new CubeDeformation(0.3F)).mirror(false), PartPose.offsetAndRotation(-3.125F, 2.0F, 0.0F, 0.0F, 0.0F, 1.7453F));

		return LayerDefinition.create(meshdefinition, 128, 128);
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