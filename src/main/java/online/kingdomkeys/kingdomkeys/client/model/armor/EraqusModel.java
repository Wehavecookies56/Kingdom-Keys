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

public class EraqusModel<T extends LivingEntity> extends ArmorBaseModel<T> {

    public static final ModelLayerLocation LAYER_LOCATION_TOP = new ModelLayerLocation(new ResourceLocation(KingdomKeys.MODID, "eraqus_top"), "main");
    public static final ModelLayerLocation LAYER_LOCATION_BOTTOM = new ModelLayerLocation(new ResourceLocation(KingdomKeys.MODID, "eraqus_bottom"), "main");

    public final ModelPart head;
    public final ModelPart body;
    public final ModelPart leftArm;
    public final ModelPart rightArm;
    public final ModelPart leftLeg;
    public final ModelPart rightLeg;  
    
    public EraqusModel(ModelPart root) {
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

		
        PartDefinition Visor = head.addOrReplaceChild("Visor", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition cube_r1 = Visor.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(68, 12).addBox(-2.5F, -0.5F, -2.5F, 5.0F, 1.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -5.516F, -3.0936F, 1.2658F, -0.2917F, -0.7402F));

		PartDefinition cube_r2 = Visor.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(68, 12).addBox(-2.5F, -0.5F, -2.5F, 5.0F, 1.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -5.516F, -3.0936F, 1.0353F, -0.4718F, -0.6537F));

		PartDefinition cube_r3 = Visor.addOrReplaceChild("cube_r3", CubeListBuilder.create().texOffs(68, 12).addBox(-2.5F, -0.5F, -2.5F, 5.0F, 1.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -5.516F, -3.0936F, 0.7805F, -0.6178F, -0.5208F));

		PartDefinition cube_r4 = Visor.addOrReplaceChild("cube_r4", CubeListBuilder.create().texOffs(68, 12).addBox(-1.0F, 1.0F, -1.0F, 5.0F, 1.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -6.2F, -5.6F, 0.4754F, -0.7268F, -0.3295F));

		PartDefinition cube_r5 = Visor.addOrReplaceChild("cube_r5", CubeListBuilder.create().texOffs(68, 12).addBox(-1.0F, 0.0F, -1.0F, 5.0F, 1.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -6.1F, -5.8F, 0.4754F, -0.7268F, -0.3295F));

		PartDefinition cube_r6 = Visor.addOrReplaceChild("cube_r6", CubeListBuilder.create().texOffs(68, 5).addBox(-1.0F, -1.0F, -1.0F, 5.0F, 1.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -6.0F, -6.0F, 0.4754F, -0.7268F, -0.3295F));

		PartDefinition EmblemHelm = head.addOrReplaceChild("EmblemHelm", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0512F, -9.5821F, -4.8F, -0.1745F, 0.0F, 0.0F));

		PartDefinition cube_r7 = EmblemHelm.addOrReplaceChild("cube_r7", CubeListBuilder.create().texOffs(118, 45).mirror().addBox(-0.5F, -0.5F, -0.1F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.0024F, 0.2357F, -0.3F, 0.0F, 0.0F, 0.7854F));

		PartDefinition cube_r8 = EmblemHelm.addOrReplaceChild("cube_r8", CubeListBuilder.create().texOffs(115, 47).mirror().addBox(0.0F, -0.5F, -0.2F, 1.0F, 1.0F, 0.4F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.3488F, -0.1179F, -0.3F, 0.0F, 0.0F, -0.7854F));

		PartDefinition cube_r9 = EmblemHelm.addOrReplaceChild("cube_r9", CubeListBuilder.create().texOffs(115, 47).addBox(-1.0F, -0.5F, -0.2F, 1.0F, 1.0F, 0.4F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.3512F, -0.1179F, -0.3F, 0.0F, 0.0F, 0.7854F));

		PartDefinition centerHorn = head.addOrReplaceChild("centerHorn", CubeListBuilder.create(), PartPose.offset(0.0F, -8.0F, 0.0F));

		PartDefinition cube_r10 = centerHorn.addOrReplaceChild("cube_r10", CubeListBuilder.create().texOffs(108, 16).addBox(-1.0F, -1.0F, -1.0F, 2.0F, 5.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -10.3873F, -3.75F, -0.2444F, -0.7703F, 0.1719F));

		PartDefinition cube_r11 = centerHorn.addOrReplaceChild("cube_r11", CubeListBuilder.create().texOffs(110, 4).addBox(-1.0F, -4.0F, -1.0F, 2.0F, 8.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -3.0F, -3.75F, 0.2444F, -0.7703F, -0.1719F));

		PartDefinition cube_r12 = centerHorn.addOrReplaceChild("cube_r12", CubeListBuilder.create().texOffs(97, 3).addBox(-1.5F, -2.0F, -1.0F, 3.5F, 3.5F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, -4.0F, -0.4391F, -0.3715F, -0.658F));

		PartDefinition sidehorn1 = head.addOrReplaceChild("sidehorn1", CubeListBuilder.create(), PartPose.offset(-0.3F, 0.0F, 0.0F));

		PartDefinition cube_r13 = sidehorn1.addOrReplaceChild("cube_r13", CubeListBuilder.create().texOffs(99, 41).addBox(-4.0F, -9.7F, 0.125F, 5.0F, 11.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(4.9929F, -3.7935F, -1.262F, -0.9844F, -0.5565F, 1.5963F));

		PartDefinition sidehorn2 = head.addOrReplaceChild("sidehorn2", CubeListBuilder.create(), PartPose.offset(0.3F, 0.0F, 0.0F));

		PartDefinition cube_r14 = sidehorn2.addOrReplaceChild("cube_r14", CubeListBuilder.create().texOffs(99, 41).mirror().addBox(-1.0F, -9.7F, 0.125F, 5.0F, 11.0F, 0.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-4.9929F, -3.7935F, -1.262F, -0.9844F, 0.5565F, -1.5963F));


		PartDefinition waistArmor = body.addOrReplaceChild("waistArmor", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition Emblem = waistArmor.addOrReplaceChild("Emblem", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.5F));

		PartDefinition cube_r15 = Emblem.addOrReplaceChild("cube_r15", CubeListBuilder.create().texOffs(8, 107).mirror().addBox(-0.5F, -0.5F, -0.1F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.0536F, 11.8536F, -3.2F, 0.0F, 0.0F, 0.7854F));

		PartDefinition cube_r16 = Emblem.addOrReplaceChild("cube_r16", CubeListBuilder.create().texOffs(5, 109).mirror().addBox(0.0F, -0.5F, -0.2F, 1.0F, 1.0F, 0.4F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.4F, 11.5F, -3.2F, 0.0F, 0.0F, -0.7854F));

		PartDefinition cube_r17 = Emblem.addOrReplaceChild("cube_r17", CubeListBuilder.create().texOffs(5, 109).addBox(-1.0F, -0.5F, -0.2F, 1.0F, 1.0F, 0.4F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.3F, 11.5F, -3.2F, 0.0F, 0.0F, 0.7854F));

		PartDefinition Hips = waistArmor.addOrReplaceChild("Hips", CubeListBuilder.create().texOffs(17, 104).addBox(-4.9F, 12.5F, -2.6F, 9.8F, 2.0F, 5.2F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -1.5F, 0.0F));

		PartDefinition cube_r18 = Hips.addOrReplaceChild("cube_r18", CubeListBuilder.create().texOffs(106, 94).addBox(-4.0F, -2.65F, 0.0F, 8.0F, 7.3F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 15.35F, 3.1F, 0.2182F, 0.0F, 0.0F));

		PartDefinition cube_r19 = Hips.addOrReplaceChild("cube_r19", CubeListBuilder.create().texOffs(111, 105).addBox(-2.0F, -2.15F, 0.0F, 4.0F, 4.3F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 12.8619F, 2.7918F, 0.1484F, 0.0F, 0.0F));

		PartDefinition HipArmor1 = Hips.addOrReplaceChild("HipArmor1", CubeListBuilder.create(), PartPose.offsetAndRotation(6.8F, 15.2F, 0.0F, 0.0F, 0.0F, 0.2618F));

		PartDefinition TopLayer = HipArmor1.addOrReplaceChild("TopLayer", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition Box_r1 = TopLayer.addOrReplaceChild("Box_r1", CubeListBuilder.create().texOffs(40, 98).addBox(-4.3246F, 0.1979F, -0.7679F, 4.0F, 0.1F, 2.1F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.6829F, 1.7797F, 2.5076F, -0.353F, 0.0343F, 1.1348F));

		PartDefinition Box_r2 = TopLayer.addOrReplaceChild("Box_r2", CubeListBuilder.create().texOffs(40, 102).addBox(-4.3246F, 0.1979F, -1.3321F, 4.0F, 0.1F, 2.1F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.6829F, 1.7797F, -2.5076F, 0.353F, -0.0343F, 1.1348F));

		PartDefinition Box_r3 = TopLayer.addOrReplaceChild("Box_r3", CubeListBuilder.create().texOffs(53, 100).addBox(-3.1026F, 1.1819F, -2.0F, 4.0F, 0.5F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 1.0908F));

		PartDefinition MiddleLayer = HipArmor1.addOrReplaceChild("MiddleLayer", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition Box_r4 = MiddleLayer.addOrReplaceChild("Box_r4", CubeListBuilder.create().texOffs(71, 99).addBox(-1.0F, -0.1F, -1.15F, 2.5F, 0.1F, 2.3F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.6377F, 2.4437F, 2.5528F, -0.3176F, 0.0569F, 1.0505F));

		PartDefinition Box_r5 = MiddleLayer.addOrReplaceChild("Box_r5", CubeListBuilder.create().texOffs(71, 102).addBox(-1.0F, -0.1F, -1.15F, 2.5F, 0.1F, 2.3F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.6377F, 2.4437F, -2.5528F, 0.3176F, -0.0569F, 1.0505F));

		PartDefinition Box_r6 = MiddleLayer.addOrReplaceChild("Box_r6", CubeListBuilder.create().texOffs(71, 106).addBox(-1.7337F, -0.8251F, -1.6772F, 2.0F, 0.1F, 3.2F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.4964F, 3.1896F, 0.0772F, 0.0F, 0.0F, 1.0036F));

		PartDefinition BottomLayer = HipArmor1.addOrReplaceChild("BottomLayer", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition Box_r7 = BottomLayer.addOrReplaceChild("Box_r7", CubeListBuilder.create().texOffs(88, 100).addBox(-1.15F, 0.1F, -1.05F, 2.4F, 0.1F, 2.3F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.7907F, 3.7746F, 2.422F, -0.3606F, 0.0166F, 0.7018F));

		PartDefinition Box_r8 = BottomLayer.addOrReplaceChild("Box_r8", CubeListBuilder.create().texOffs(88, 103).addBox(-1.15F, 0.1F, -1.25F, 2.4F, 0.1F, 2.3F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.7907F, 3.7746F, -2.422F, 0.3606F, -0.0166F, 0.7018F));

		PartDefinition Box_r9 = BottomLayer.addOrReplaceChild("Box_r9", CubeListBuilder.create().texOffs(88, 106).addBox(-0.6667F, -0.1439F, -1.4613F, 2.2F, 0.1F, 2.8F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.6916F, 3.4178F, 0.0613F, 0.0F, 0.0F, 0.6981F));

		PartDefinition HipArmor2 = Hips.addOrReplaceChild("HipArmor2", CubeListBuilder.create(), PartPose.offsetAndRotation(-6.8F, 15.2F, 0.0F, 0.0F, 0.0F, -0.2618F));

		PartDefinition TopLayer2 = HipArmor2.addOrReplaceChild("TopLayer2", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition Box_r10 = TopLayer2.addOrReplaceChild("Box_r10", CubeListBuilder.create().texOffs(40, 98).mirror().addBox(0.3246F, 0.1979F, -0.7679F, 4.0F, 0.1F, 2.1F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.6829F, 1.7797F, 2.5076F, -0.353F, -0.0343F, -1.1348F));

		PartDefinition Box_r11 = TopLayer2.addOrReplaceChild("Box_r11", CubeListBuilder.create().texOffs(40, 102).mirror().addBox(0.3246F, 0.1979F, -1.3321F, 4.0F, 0.1F, 2.1F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.6829F, 1.7797F, -2.5076F, 0.353F, 0.0343F, -1.1348F));

		PartDefinition Box_r12 = TopLayer2.addOrReplaceChild("Box_r12", CubeListBuilder.create().texOffs(53, 100).mirror().addBox(-0.8974F, 1.1819F, -2.0F, 4.0F, 0.5F, 4.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, -1.0908F));

		PartDefinition MiddleLayer2 = HipArmor2.addOrReplaceChild("MiddleLayer2", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition Box_r13 = MiddleLayer2.addOrReplaceChild("Box_r13", CubeListBuilder.create().texOffs(71, 99).mirror().addBox(-1.5F, -0.1F, -1.15F, 2.5F, 0.1F, 2.3F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.6377F, 2.4437F, 2.5528F, -0.3176F, -0.0569F, -1.0505F));

		PartDefinition Box_r14 = MiddleLayer2.addOrReplaceChild("Box_r14", CubeListBuilder.create().texOffs(71, 102).mirror().addBox(-1.5F, -0.1F, -1.15F, 2.5F, 0.1F, 2.3F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.6377F, 2.4437F, -2.5528F, 0.3176F, 0.0569F, -1.0505F));

		PartDefinition Box_r15 = MiddleLayer2.addOrReplaceChild("Box_r15", CubeListBuilder.create().texOffs(71, 106).mirror().addBox(-0.2663F, -0.8251F, -1.6772F, 2.0F, 0.1F, 3.2F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.4964F, 3.1896F, 0.0772F, 0.0F, 0.0F, -1.0036F));

		PartDefinition BottomLayer2 = HipArmor2.addOrReplaceChild("BottomLayer2", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition Box_r16 = BottomLayer2.addOrReplaceChild("Box_r16", CubeListBuilder.create().texOffs(88, 100).mirror().addBox(-1.25F, 0.1F, -1.05F, 2.4F, 0.1F, 2.3F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-0.7907F, 3.7746F, 2.422F, -0.3606F, -0.0166F, -0.7018F));

		PartDefinition Box_r17 = BottomLayer2.addOrReplaceChild("Box_r17", CubeListBuilder.create().texOffs(88, 103).mirror().addBox(-1.25F, 0.1F, -1.25F, 2.4F, 0.1F, 2.3F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-0.7907F, 3.7746F, -2.422F, 0.3606F, 0.0166F, -0.7018F));

		PartDefinition Box_r18 = BottomLayer2.addOrReplaceChild("Box_r18", CubeListBuilder.create().texOffs(88, 106).mirror().addBox(-1.5333F, -0.1439F, -1.4613F, 2.2F, 0.1F, 2.8F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-0.6916F, 3.4178F, 0.0613F, 0.0F, 0.0F, -0.6981F));

		PartDefinition ChestArmor = body.addOrReplaceChild("ChestArmor", CubeListBuilder.create().texOffs(45, 88).addBox(-4.5F, -1.5F, 0.1F, 1.0F, 2.5F, 0.2F, new CubeDeformation(0.0F))
		.texOffs(45, 88).mirror().addBox(3.5F, -1.5F, 0.1F, 1.0F, 2.5F, 0.2F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(0.0F, 3.5F, -2.8F));

		PartDefinition cube_r20 = ChestArmor.addOrReplaceChild("cube_r20", CubeListBuilder.create().texOffs(33, 88).mirror().addBox(-2.375F, -1.0F, 0.0F, 3.0F, 3.4F, 0.25F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(3.375F, 1.0F, 0.0F, 0.0F, 0.0F, 0.7854F));

		PartDefinition cube_r21 = ChestArmor.addOrReplaceChild("cube_r21", CubeListBuilder.create().texOffs(49, 88).mirror().addBox(-0.375F, -1.0F, 0.1F, 1.0F, 1.4F, 0.15F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(3.375F, -1.2F, 0.1F, 0.0F, 0.0F, 0.7854F));

		PartDefinition cube_r22 = ChestArmor.addOrReplaceChild("cube_r22", CubeListBuilder.create().texOffs(49, 88).addBox(-0.625F, -1.0F, 0.1F, 1.0F, 1.4F, 0.15F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-3.375F, -1.2F, 0.1F, 0.0F, 0.0F, -0.7854F));

		PartDefinition cube_r23 = ChestArmor.addOrReplaceChild("cube_r23", CubeListBuilder.create().texOffs(33, 88).addBox(-0.625F, -1.0F, 0.0F, 3.0F, 3.4F, 0.25F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-3.375F, 1.0F, 0.0F, 0.0F, 0.0F, -0.7854F));

		PartDefinition cube_r24 = ChestArmor.addOrReplaceChild("cube_r24", CubeListBuilder.create().texOffs(24, 88).mirror().addBox(-1.4F, -1.0F, -0.15F, 2.4F, 2.0F, 0.3F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(2.0F, -1.5F, 0.05F, -0.1337F, 0.1918F, 1.174F));

		PartDefinition cube_r25 = ChestArmor.addOrReplaceChild("cube_r25", CubeListBuilder.create().texOffs(23, 87).mirror().addBox(-1.4F, -1.0F, -0.25F, 2.4F, 2.0F, 0.4F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.5073F, -0.7808F, 0.5029F, 0.5139F, 0.0751F, 1.0609F));

		PartDefinition cube_r26 = ChestArmor.addOrReplaceChild("cube_r26", CubeListBuilder.create().texOffs(23, 87).addBox(-1.0F, -1.0F, -0.35F, 2.4F, 2.0F, 0.5F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.5073F, -0.7808F, 0.5029F, 0.5139F, -0.0751F, -1.0609F));

		PartDefinition cube_r27 = ChestArmor.addOrReplaceChild("cube_r27", CubeListBuilder.create().texOffs(24, 88).addBox(-1.0F, -1.0F, -0.15F, 2.4F, 2.0F, 0.3F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-2.0F, -1.5F, 0.05F, -0.1337F, -0.1918F, -1.174F));

		PartDefinition cube_r28 = ChestArmor.addOrReplaceChild("cube_r28", CubeListBuilder.create().texOffs(40, 85).addBox(-1.0F, -0.5F, -0.15F, 2.0F, 1.0F, 0.3F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 1.4683F, 0.4251F, 0.4102F, 0.0F, 0.0F));

		PartDefinition Center_r1 = ChestArmor.addOrReplaceChild("Center_r1", CubeListBuilder.create().texOffs(0, 88).addBox(-3.9F, -1.5F, 0.0F, 7.9F, 3.0F, 0.2F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.2F, 0.0524F, 0.0F, 0.0F));


		PartDefinition Vambrace = leftArm.addOrReplaceChild("Vambrace", CubeListBuilder.create().texOffs(1, 73).addBox(-2.65F, -3.0F, -2.7F, 5.3F, 4.0F, 5.3F, new CubeDeformation(0.0F))
		.texOffs(25, 74).addBox(-2.85F, -3.5F, -3.0F, 5.75F, 1.0F, 6.0F, new CubeDeformation(0.0F))
		.texOffs(26, 75).addBox(-2.6F, -4.0F, -2.75F, 5.25F, 0.5F, 5.5F, new CubeDeformation(0.0F)), PartPose.offset(1.0F, 8.0F, 0.0F));

		PartDefinition cube_r29 = Vambrace.addOrReplaceChild("cube_r29", CubeListBuilder.create().texOffs(53, 76).addBox(-1.0F, -1.0F, -1.0F, 0.25F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(3.5F, -2.5F, 0.0F, 0.7854F, 0.0F, 0.0F));

		PartDefinition Pauldron = leftArm.addOrReplaceChild("Pauldron", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition cube_r30 = Pauldron.addOrReplaceChild("cube_r30", CubeListBuilder.create().texOffs(63, 73).addBox(-0.25F, -2.5F, -2.5F, 0.5F, 5.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(3.95F, -2.1213F, 0.0F, 0.7854F, 0.0F, -0.3054F));

		PartDefinition bottompart = Pauldron.addOrReplaceChild("bottompart", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition ArmArmor1 = bottompart.addOrReplaceChild("ArmArmor1", CubeListBuilder.create(), PartPose.offsetAndRotation(5.2F, -1.5F, 0.0F, 0.0F, 0.0F, 0.2618F));

		PartDefinition Top = ArmArmor1.addOrReplaceChild("Top", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition Box_r19 = Top.addOrReplaceChild("Box_r19", CubeListBuilder.create().texOffs(71, 99).addBox(-1.0F, -0.1F, -1.15F, 2.5F, 0.1F, 2.3F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.6377F, 2.4437F, 2.5528F, -0.3176F, 0.0569F, 1.0505F));

		PartDefinition Box_r20 = Top.addOrReplaceChild("Box_r20", CubeListBuilder.create().texOffs(71, 102).addBox(-1.0F, -0.1F, -1.15F, 2.5F, 0.1F, 2.3F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.6377F, 2.4437F, -2.5528F, 0.3176F, -0.0569F, 1.0505F));

		PartDefinition Box_r21 = Top.addOrReplaceChild("Box_r21", CubeListBuilder.create().texOffs(71, 106).addBox(-1.7337F, -0.8251F, -1.6772F, 2.0F, 0.1F, 3.2F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.4964F, 3.1896F, 0.0772F, 0.0F, 0.0F, 1.0036F));

		PartDefinition BottomLayer3 = ArmArmor1.addOrReplaceChild("BottomLayer3", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition Box_r22 = BottomLayer3.addOrReplaceChild("Box_r22", CubeListBuilder.create().texOffs(88, 100).addBox(-1.15F, 0.1F, -1.05F, 2.4F, 0.1F, 2.3F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.7907F, 3.7746F, 2.422F, -0.3606F, 0.0166F, 0.7018F));

		PartDefinition Box_r23 = BottomLayer3.addOrReplaceChild("Box_r23", CubeListBuilder.create().texOffs(88, 103).addBox(-1.15F, 0.1F, -1.25F, 2.4F, 0.1F, 2.3F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.7907F, 3.7746F, -2.422F, 0.3606F, -0.0166F, 0.7018F));

		PartDefinition Box_r24 = BottomLayer3.addOrReplaceChild("Box_r24", CubeListBuilder.create().texOffs(88, 106).addBox(-0.6667F, -0.1439F, -1.4613F, 2.2F, 0.1F, 2.8F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.6916F, 3.4178F, 0.0613F, 0.0F, 0.0F, 0.6981F));

		PartDefinition topSpikes = Pauldron.addOrReplaceChild("topSpikes", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition cube_r31 = topSpikes.addOrReplaceChild("cube_r31", CubeListBuilder.create().texOffs(81, 77).addBox(-0.025F, -0.1F, -0.925F, 0.15F, 1.1F, 2.15F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(2.6342F, -5.6223F, 1.8978F, -1.666F, -0.0472F, -0.3316F));

		PartDefinition cube_r32 = topSpikes.addOrReplaceChild("cube_r32", CubeListBuilder.create().texOffs(77, 78).addBox(-0.125F, 0.15F, 0.475F, 0.15F, 0.45F, 0.85F, new CubeDeformation(0.0F))
		.texOffs(77, 78).addBox(-0.125F, -0.1F, 0.075F, 0.15F, 0.25F, 1.25F, new CubeDeformation(0.0F))
		.texOffs(77, 78).addBox(-0.125F, 0.1F, 0.075F, 0.15F, 0.2F, 0.45F, new CubeDeformation(0.0F))
		.texOffs(77, 78).addBox(-0.125F, -0.1F, -0.525F, 0.15F, 0.2F, 0.85F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(2.1868F, -7.6743F, 1.4052F, -1.1523F, -0.1068F, -0.2665F));

		PartDefinition cube_r33 = topSpikes.addOrReplaceChild("cube_r33", CubeListBuilder.create().texOffs(78, 81).addBox(-0.125F, -2.5F, -0.125F, 0.25F, 3.5F, 0.25F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(2.875F, -5.5F, 0.975F, -0.1249F, 0.0166F, -0.3225F));

		PartDefinition cube_r34 = topSpikes.addOrReplaceChild("cube_r34", CubeListBuilder.create().texOffs(81, 77).addBox(-0.025F, -0.1F, -1.225F, 0.15F, 1.1F, 2.15F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(2.6342F, -5.6223F, -1.8978F, 1.666F, 0.0472F, -0.3316F));

		PartDefinition cube_r35 = topSpikes.addOrReplaceChild("cube_r35", CubeListBuilder.create().texOffs(77, 78).addBox(-0.125F, 0.15F, -1.325F, 0.15F, 0.45F, 0.85F, new CubeDeformation(0.0F))
		.texOffs(77, 78).addBox(-0.125F, -0.1F, -1.325F, 0.15F, 0.25F, 1.25F, new CubeDeformation(0.0F))
		.texOffs(77, 78).addBox(-0.125F, 0.1F, -0.525F, 0.15F, 0.2F, 0.45F, new CubeDeformation(0.0F))
		.texOffs(77, 78).addBox(-0.125F, -0.1F, -0.325F, 0.15F, 0.2F, 0.85F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(2.1868F, -7.6743F, -1.4052F, 1.1523F, 0.1068F, -0.2665F));

		PartDefinition cube_r36 = topSpikes.addOrReplaceChild("cube_r36", CubeListBuilder.create().texOffs(78, 81).addBox(-0.125F, -2.5F, -0.125F, 0.25F, 3.5F, 0.25F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(2.875F, -5.5F, -0.975F, 0.1249F, -0.0166F, -0.3225F));


		PartDefinition Pauldron2 = rightArm.addOrReplaceChild("Pauldron2", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition cube_r37 = Pauldron2.addOrReplaceChild("cube_r37", CubeListBuilder.create().texOffs(63, 73).mirror().addBox(-0.25F, -2.5F, -2.5F, 0.5F, 5.0F, 5.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-3.95F, -2.1213F, 0.0F, 0.7854F, 0.0F, 0.3054F));

		PartDefinition bottompart2 = Pauldron2.addOrReplaceChild("bottompart2", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition ArmArmor2 = bottompart2.addOrReplaceChild("ArmArmor2", CubeListBuilder.create(), PartPose.offsetAndRotation(-5.2F, -1.5F, 0.0F, 0.0F, 0.0F, -0.2618F));

		PartDefinition Top2 = ArmArmor2.addOrReplaceChild("Top2", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition Box_r25 = Top2.addOrReplaceChild("Box_r25", CubeListBuilder.create().texOffs(71, 99).mirror().addBox(-1.5F, -0.1F, -1.15F, 2.5F, 0.1F, 2.3F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.6377F, 2.4437F, 2.5528F, -0.3176F, -0.0569F, -1.0505F));

		PartDefinition Box_r26 = Top2.addOrReplaceChild("Box_r26", CubeListBuilder.create().texOffs(71, 102).mirror().addBox(-1.5F, -0.1F, -1.15F, 2.5F, 0.1F, 2.3F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.6377F, 2.4437F, -2.5528F, 0.3176F, 0.0569F, -1.0505F));

		PartDefinition Box_r27 = Top2.addOrReplaceChild("Box_r27", CubeListBuilder.create().texOffs(71, 106).mirror().addBox(-0.2663F, -0.8251F, -1.6772F, 2.0F, 0.1F, 3.2F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.4964F, 3.1896F, 0.0772F, 0.0F, 0.0F, -1.0036F));

		PartDefinition BottomLayer4 = ArmArmor2.addOrReplaceChild("BottomLayer4", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition Box_r28 = BottomLayer4.addOrReplaceChild("Box_r28", CubeListBuilder.create().texOffs(88, 100).mirror().addBox(-1.25F, 0.1F, -1.05F, 2.4F, 0.1F, 2.3F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-0.7907F, 3.7746F, 2.422F, -0.3606F, -0.0166F, -0.7018F));

		PartDefinition Box_r29 = BottomLayer4.addOrReplaceChild("Box_r29", CubeListBuilder.create().texOffs(88, 103).mirror().addBox(-1.25F, 0.1F, -1.25F, 2.4F, 0.1F, 2.3F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-0.7907F, 3.7746F, -2.422F, 0.3606F, 0.0166F, -0.7018F));

		PartDefinition Box_r30 = BottomLayer4.addOrReplaceChild("Box_r30", CubeListBuilder.create().texOffs(88, 106).mirror().addBox(-1.5333F, -0.1439F, -1.4613F, 2.2F, 0.1F, 2.8F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-0.6916F, 3.4178F, 0.0613F, 0.0F, 0.0F, -0.6981F));

		PartDefinition topSpikes2 = Pauldron2.addOrReplaceChild("topSpikes2", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition cube_r38 = topSpikes2.addOrReplaceChild("cube_r38", CubeListBuilder.create().texOffs(81, 77).mirror().addBox(-0.125F, -0.1F, -0.925F, 0.15F, 1.1F, 2.15F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-2.6342F, -5.6223F, 1.8978F, -1.666F, 0.0472F, 0.3316F));

		PartDefinition cube_r39 = topSpikes2.addOrReplaceChild("cube_r39", CubeListBuilder.create().texOffs(77, 78).mirror().addBox(-0.025F, 0.15F, 0.475F, 0.15F, 0.45F, 0.85F, new CubeDeformation(0.0F)).mirror(false)
		.texOffs(77, 78).mirror().addBox(-0.025F, -0.1F, 0.075F, 0.15F, 0.25F, 1.25F, new CubeDeformation(0.0F)).mirror(false)
		.texOffs(77, 78).mirror().addBox(-0.025F, 0.1F, 0.075F, 0.15F, 0.2F, 0.45F, new CubeDeformation(0.0F)).mirror(false)
		.texOffs(77, 78).mirror().addBox(-0.025F, -0.1F, -0.525F, 0.15F, 0.2F, 0.85F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-2.1868F, -7.6743F, 1.4052F, -1.1523F, 0.1068F, 0.2665F));

		PartDefinition cube_r40 = topSpikes2.addOrReplaceChild("cube_r40", CubeListBuilder.create().texOffs(78, 81).mirror().addBox(-0.125F, -2.5F, -0.125F, 0.25F, 3.5F, 0.25F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-2.875F, -5.5F, 0.975F, -0.1249F, -0.0166F, 0.3225F));

		PartDefinition cube_r41 = topSpikes2.addOrReplaceChild("cube_r41", CubeListBuilder.create().texOffs(81, 77).mirror().addBox(-0.125F, -0.1F, -1.225F, 0.15F, 1.1F, 2.15F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-2.6342F, -5.6223F, -1.8978F, 1.666F, -0.0472F, 0.3316F));

		PartDefinition cube_r42 = topSpikes2.addOrReplaceChild("cube_r42", CubeListBuilder.create().texOffs(77, 78).mirror().addBox(-0.025F, 0.15F, -1.325F, 0.15F, 0.45F, 0.85F, new CubeDeformation(0.0F)).mirror(false)
		.texOffs(77, 78).mirror().addBox(-0.025F, -0.1F, -1.325F, 0.15F, 0.25F, 1.25F, new CubeDeformation(0.0F)).mirror(false)
		.texOffs(77, 78).mirror().addBox(-0.025F, 0.1F, -0.525F, 0.15F, 0.2F, 0.45F, new CubeDeformation(0.0F)).mirror(false)
		.texOffs(77, 78).mirror().addBox(-0.025F, -0.1F, -0.325F, 0.15F, 0.2F, 0.85F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-2.1868F, -7.6743F, -1.4052F, 1.1523F, -0.1068F, 0.2665F));

		PartDefinition cube_r43 = topSpikes2.addOrReplaceChild("cube_r43", CubeListBuilder.create().texOffs(78, 81).mirror().addBox(-0.125F, -2.5F, -0.125F, 0.25F, 3.5F, 0.25F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-2.875F, -5.5F, -0.975F, 0.1249F, 0.0166F, 0.3225F));

		PartDefinition Vambrace2 = rightArm.addOrReplaceChild("Vambrace2", CubeListBuilder.create().texOffs(1, 73).mirror().addBox(-2.65F, -3.0F, -2.7F, 5.3F, 4.0F, 5.3F, new CubeDeformation(0.0F)).mirror(false)
		.texOffs(25, 74).mirror().addBox(-2.9F, -3.5F, -3.0F, 5.75F, 1.0F, 6.0F, new CubeDeformation(0.0F)).mirror(false)
		.texOffs(26, 75).mirror().addBox(-2.65F, -4.0F, -2.75F, 5.25F, 0.5F, 5.5F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(-1.0F, 8.0F, 0.0F));

		PartDefinition cube_r44 = Vambrace2.addOrReplaceChild("cube_r44", CubeListBuilder.create().texOffs(53, 76).mirror().addBox(0.75F, -1.0F, -1.0F, 0.25F, 2.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-3.5F, -2.5F, 0.0F, 0.7854F, 0.0F, 0.0F));


		PartDefinition Boot1 = leftLeg.addOrReplaceChild("Boot1", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition Foot = Boot1.addOrReplaceChild("Foot", CubeListBuilder.create().texOffs(16, 123).addBox(-1.7F, -0.5478F, 2.2578F, 3.4F, 1.8F, 1.5F, new CubeDeformation(0.0F))
		.texOffs(16, 123).addBox(-1.9F, -0.3478F, 2.6578F, 3.8F, 1.6F, 1.1F, new CubeDeformation(0.0F))
		.texOffs(17, 124).addBox(-2.1F, -0.1478F, 3.1578F, 4.2F, 1.4F, 0.6F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 10.4478F, -6.0578F));

		PartDefinition Base_r1 = Foot.addOrReplaceChild("Base_r1", CubeListBuilder.create().texOffs(17, 124).addBox(-1.5F, -0.1F, 0.25F, 3.0F, 1.0F, 0.5F, new CubeDeformation(0.0F))
		.texOffs(17, 124).addBox(-1.4F, -0.2F, 0.05F, 2.8F, 1.1F, 0.7F, new CubeDeformation(0.0F))
		.texOffs(17, 124).addBox(-1.3F, -0.3F, -0.15F, 2.6F, 1.2F, 0.9F, new CubeDeformation(0.0F))
		.texOffs(17, 124).addBox(-1.2F, -0.5F, -0.25F, 2.4F, 1.4F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(16, 123).addBox(-1.1F, -0.6F, -0.35F, 2.2F, 1.5F, 1.1F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.1887F, 1.7663F, -0.2618F, 0.0F, 0.0F));

		PartDefinition Sole = Foot.addOrReplaceChild("Sole", CubeListBuilder.create().texOffs(2, 125).addBox(-2.2F, 11.7F, -3.0F, 4.4F, 0.5F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(3, 126).addBox(-2.0F, 11.7F, -3.5F, 4.0F, 0.5F, 0.5F, new CubeDeformation(0.0F))
		.texOffs(3, 126).addBox(-1.8F, 11.7F, -4.0F, 3.6F, 0.5F, 0.5F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -10.4478F, 6.0578F));

		PartDefinition Sole12_r1 = Sole.addOrReplaceChild("Sole12_r1", CubeListBuilder.create().texOffs(3, 126).addBox(-0.1F, -0.1F, -1.05F, 0.2F, 0.1F, 0.2F, new CubeDeformation(0.0F))
		.texOffs(3, 126).addBox(-0.2F, -0.1F, -0.85F, 0.4F, 0.1F, 0.2F, new CubeDeformation(0.0F))
		.texOffs(3, 126).addBox(-0.4F, -0.1F, -0.65F, 0.8F, 0.1F, 0.2F, new CubeDeformation(0.0F))
		.texOffs(3, 126).addBox(-0.6F, -0.1F, -0.45F, 1.2F, 0.1F, 0.2F, new CubeDeformation(0.0F))
		.texOffs(3, 126).addBox(-0.8F, -0.1F, -0.25F, 1.6F, 0.1F, 0.5F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 11.1611F, -5.9917F, -0.6545F, 0.0F, 0.0F));

		PartDefinition Sole7_r1 = Sole.addOrReplaceChild("Sole7_r1", CubeListBuilder.create().texOffs(3, 126).addBox(-1.0F, -0.25F, -1.75F, 2.0F, 0.2F, 0.5F, new CubeDeformation(0.0F))
		.texOffs(3, 126).addBox(-1.2F, -0.25F, -1.25F, 2.4F, 0.3F, 0.5F, new CubeDeformation(0.0F))
		.texOffs(3, 126).addBox(-1.4F, -0.25F, -0.75F, 2.8F, 0.4F, 0.5F, new CubeDeformation(0.0F))
		.texOffs(3, 126).addBox(-1.6F, -0.25F, -0.25F, 3.2F, 0.5F, 0.5F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 11.8938F, -4.1768F, -0.2618F, 0.0F, 0.0F));

		PartDefinition Toe = Foot.addOrReplaceChild("Toe", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition Base_r2 = Toe.addOrReplaceChild("Base_r2", CubeListBuilder.create().texOffs(28, 125).addBox(-0.3F, -0.15F, -0.3F, 0.6F, 0.6F, 0.3F, new CubeDeformation(0.0F))
		.texOffs(28, 125).addBox(-0.4F, -0.45F, 0.0F, 0.8F, 0.9F, 0.3F, new CubeDeformation(0.0F))
		.texOffs(27, 124).addBox(-0.7F, -0.75F, 0.3F, 1.4F, 1.2F, 0.6F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.6109F, 0.0F, 0.0F));

		PartDefinition Base_r3 = Toe.addOrReplaceChild("Base_r3", CubeListBuilder.create().texOffs(27, 124).addBox(-0.9F, -0.65F, -0.45F, 1.8F, 1.4F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.2231F, 0.9288F, -0.1309F, 0.0F, 0.0F));

		PartDefinition Shin = Boot1.addOrReplaceChild("Shin", CubeListBuilder.create().texOffs(41, 121).addBox(-2.5F, 7.9F, -2.6F, 4.9F, 0.8F, 5.1F, new CubeDeformation(0.0F))
		.texOffs(63, 120).addBox(-2.4F, 5.4F, -2.6F, 4.7F, 2.5F, 5.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition cube_r45 = Shin.addOrReplaceChild("cube_r45", CubeListBuilder.create().texOffs(35, 120).addBox(-1.0F, -1.0F, -0.1F, 2.0F, 2.0F, 0.2F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 5.3F, -2.7F, -0.0928F, 0.0924F, 0.7811F));

		PartDefinition cube_r46 = Shin.addOrReplaceChild("cube_r46", CubeListBuilder.create().texOffs(35, 124).addBox(-1.0F, -1.0F, 0.8F, 2.0F, 2.0F, 0.2F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 8.3F, -3.3F, 0.0F, 0.0F, 0.7854F));

		PartDefinition cube_r47 = Shin.addOrReplaceChild("cube_r47", CubeListBuilder.create().texOffs(83, 119).addBox(-2.55F, -1.25F, -2.7F, 5.1F, 1.4F, 5.2F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.05F, 5.85F, -0.1F, -0.2182F, 0.0F, 0.0F));


		PartDefinition Boot2 = rightLeg.addOrReplaceChild("Boot2", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition Foot2 = Boot2.addOrReplaceChild("Foot2", CubeListBuilder.create().texOffs(16, 123).mirror().addBox(-1.7F, -0.5478F, 2.2578F, 3.4F, 1.8F, 1.5F, new CubeDeformation(0.0F)).mirror(false)
		.texOffs(16, 123).mirror().addBox(-1.9F, -0.3478F, 2.6578F, 3.8F, 1.6F, 1.1F, new CubeDeformation(0.0F)).mirror(false)
		.texOffs(17, 124).mirror().addBox(-2.1F, -0.1478F, 3.1578F, 4.2F, 1.4F, 0.6F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(0.0F, 10.4478F, -6.0578F));

		PartDefinition Base_r4 = Foot2.addOrReplaceChild("Base_r4", CubeListBuilder.create().texOffs(17, 124).mirror().addBox(-1.5F, -0.1F, 0.25F, 3.0F, 1.0F, 0.5F, new CubeDeformation(0.0F)).mirror(false)
		.texOffs(17, 124).mirror().addBox(-1.4F, -0.2F, 0.05F, 2.8F, 1.1F, 0.7F, new CubeDeformation(0.0F)).mirror(false)
		.texOffs(17, 124).mirror().addBox(-1.3F, -0.3F, -0.15F, 2.6F, 1.2F, 0.9F, new CubeDeformation(0.0F)).mirror(false)
		.texOffs(17, 124).mirror().addBox(-1.2F, -0.5F, -0.25F, 2.4F, 1.4F, 1.0F, new CubeDeformation(0.0F)).mirror(false)
		.texOffs(16, 123).mirror().addBox(-1.1F, -0.6F, -0.35F, 2.2F, 1.5F, 1.1F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.0F, 0.1887F, 1.7663F, -0.2618F, 0.0F, 0.0F));

		PartDefinition Sole2 = Foot2.addOrReplaceChild("Sole2", CubeListBuilder.create().texOffs(2, 125).mirror().addBox(-2.2F, 11.7F, -3.0F, 4.4F, 0.5F, 1.0F, new CubeDeformation(0.0F)).mirror(false)
		.texOffs(3, 126).mirror().addBox(-2.0F, 11.7F, -3.5F, 4.0F, 0.5F, 0.5F, new CubeDeformation(0.0F)).mirror(false)
		.texOffs(3, 126).mirror().addBox(-1.8F, 11.7F, -4.0F, 3.6F, 0.5F, 0.5F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(0.0F, -10.4478F, 6.0578F));

		PartDefinition Sole13_r1 = Sole2.addOrReplaceChild("Sole13_r1", CubeListBuilder.create().texOffs(3, 126).mirror().addBox(-0.1F, -0.1F, -1.05F, 0.2F, 0.1F, 0.2F, new CubeDeformation(0.0F)).mirror(false)
		.texOffs(3, 126).mirror().addBox(-0.2F, -0.1F, -0.85F, 0.4F, 0.1F, 0.2F, new CubeDeformation(0.0F)).mirror(false)
		.texOffs(3, 126).mirror().addBox(-0.4F, -0.1F, -0.65F, 0.8F, 0.1F, 0.2F, new CubeDeformation(0.0F)).mirror(false)
		.texOffs(3, 126).mirror().addBox(-0.6F, -0.1F, -0.45F, 1.2F, 0.1F, 0.2F, new CubeDeformation(0.0F)).mirror(false)
		.texOffs(3, 126).mirror().addBox(-0.8F, -0.1F, -0.25F, 1.6F, 0.1F, 0.5F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.0F, 11.1611F, -5.9917F, -0.6545F, 0.0F, 0.0F));

		PartDefinition Sole8_r1 = Sole2.addOrReplaceChild("Sole8_r1", CubeListBuilder.create().texOffs(3, 126).mirror().addBox(-1.0F, -0.25F, -1.75F, 2.0F, 0.2F, 0.5F, new CubeDeformation(0.0F)).mirror(false)
		.texOffs(3, 126).mirror().addBox(-1.2F, -0.25F, -1.25F, 2.4F, 0.3F, 0.5F, new CubeDeformation(0.0F)).mirror(false)
		.texOffs(3, 126).mirror().addBox(-1.4F, -0.25F, -0.75F, 2.8F, 0.4F, 0.5F, new CubeDeformation(0.0F)).mirror(false)
		.texOffs(3, 126).mirror().addBox(-1.6F, -0.25F, -0.25F, 3.2F, 0.5F, 0.5F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.0F, 11.8938F, -4.1768F, -0.2618F, 0.0F, 0.0F));

		PartDefinition Toe2 = Foot2.addOrReplaceChild("Toe2", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition Base_r5 = Toe2.addOrReplaceChild("Base_r5", CubeListBuilder.create().texOffs(28, 125).mirror().addBox(-0.3F, -0.15F, -0.3F, 0.6F, 0.6F, 0.3F, new CubeDeformation(0.0F)).mirror(false)
		.texOffs(28, 125).mirror().addBox(-0.4F, -0.45F, 0.0F, 0.8F, 0.9F, 0.3F, new CubeDeformation(0.0F)).mirror(false)
		.texOffs(27, 124).mirror().addBox(-0.7F, -0.75F, 0.3F, 1.4F, 1.2F, 0.6F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.6109F, 0.0F, 0.0F));

		PartDefinition Base_r6 = Toe2.addOrReplaceChild("Base_r6", CubeListBuilder.create().texOffs(27, 124).mirror().addBox(-0.9F, -0.65F, -0.45F, 1.8F, 1.4F, 1.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.0F, 0.2231F, 0.9288F, -0.1309F, 0.0F, 0.0F));

		PartDefinition Shin2 = Boot2.addOrReplaceChild("Shin2", CubeListBuilder.create().texOffs(41, 121).mirror().addBox(-2.4F, 7.9F, -2.6F, 4.9F, 0.8F, 5.1F, new CubeDeformation(0.0F)).mirror(false)
		.texOffs(63, 120).mirror().addBox(-2.3F, 5.4F, -2.6F, 4.7F, 2.5F, 5.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition cube_r48 = Shin2.addOrReplaceChild("cube_r48", CubeListBuilder.create().texOffs(35, 120).mirror().addBox(-1.0F, -1.0F, -0.1F, 2.0F, 2.0F, 0.2F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.0F, 5.3F, -2.7F, -0.0928F, -0.0924F, -0.7811F));

		PartDefinition cube_r49 = Shin2.addOrReplaceChild("cube_r49", CubeListBuilder.create().texOffs(35, 124).mirror().addBox(-1.0F, -1.0F, 0.8F, 2.0F, 2.0F, 0.2F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.0F, 8.3F, -3.3F, 0.0F, 0.0F, -0.7854F));

		PartDefinition cube_r50 = Shin2.addOrReplaceChild("cube_r50", CubeListBuilder.create().texOffs(83, 119).mirror().addBox(-2.55F, -1.25F, -2.7F, 5.1F, 1.4F, 5.2F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.05F, 5.85F, -0.1F, -0.2182F, 0.0F, 0.0F));

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
          //  rightArm2.copyFrom(super.rightArm);

            super.setupAnim(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
        }
    }
}