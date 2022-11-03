package online.kingdomkeys.kingdomkeys.client.model.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.client.ClientUtils.Angle;
import online.kingdomkeys.kingdomkeys.client.ClientUtils.ModelAnimation;
import online.kingdomkeys.kingdomkeys.entity.EntityHelper;

import java.util.ArrayList;
import java.util.List;

//TODO port new model
@OnlyIn(Dist.CLIENT)
public class DragoonModel<T extends LivingEntity> extends EntityModel<T> {
    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation(KingdomKeys.MODID, "dragoon"), "main");
    private final ModelPart right_leg;
	private final ModelPart left_leg;
	private final ModelPart body;
	private final ModelPart left_arm;
	private final ModelPart right_arm;
	private final ModelPart head;
	private final ModelPart right_wing;
	private final ModelPart left_wing;

	public DragoonModel(ModelPart root) {
		this.right_leg = root.getChild("body").getChild("right_leg");
		this.left_leg = root.getChild("body").getChild("left_leg");
		this.body = root.getChild("body");
		this.left_arm = root.getChild("body").getChild("left_arm");
		this.right_arm = root.getChild("body").getChild("right_arm");
		this.head = root.getChild("body").getChild("head");
		this.right_wing = root.getChild("body").getChild("right_wing");
		this.left_wing = root.getChild("body").getChild("left_wing");
		
        ModelAnimation rightWingAnim = new ModelAnimation(right_wing, 0, -40, 40, 0, true, Angle.Y, left_wing);
      //  ModelAnimation leftWingAnim = new ModelAnimation(left_wing, 0, -40, 40, 0, true, Angle.Y, null);
        //ModelAnimation headAnim = new ModelAnimation(head, 0, -30, 30, 0, true, Angle.Z, null);

        animation.add(rightWingAnim);
       // animation.add(leftWingAnim);
	}
	
    List<ModelAnimation> animation = new ArrayList<ModelAnimation>();

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition body = partdefinition.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 10).addBox(-4.0917F, 7.2918F, -3.9461F, 8.0F, 4.0F, 5.0F, new CubeDeformation(0.0F))
		.texOffs(42, 40).addBox(-2.9917F, 5.2918F, -3.9461F, 6.0F, 2.0F, 5.0F, new CubeDeformation(0.0F))
		.texOffs(28, 24).addBox(-2.9917F, -2.2082F, 2.3039F, 1.0F, 4.0F, 3.0F, new CubeDeformation(0.0F))
		.texOffs(34, 87).addBox(2.0083F, -2.2082F, 2.3039F, 1.0F, 4.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(-0.0083F, -6.7918F, 3.6961F));

		PartDefinition cube_r1 = body.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(0, 0).addBox(-4.0F, -2.0F, -2.5F, 8.0F, 4.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0083F, 0.2918F, 0.5539F, -0.2182F, 0.0F, 0.0F));

		PartDefinition cube_r2 = body.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(102, 66).addBox(-4.0F, 1.5F, -2.5F, 8.0F, 1.0F, 5.0F, new CubeDeformation(0.0F))
		.texOffs(0, 40).addBox(-4.0F, -1.5F, -2.5F, 8.0F, 3.0F, 5.0F, new CubeDeformation(0.0F))
		.texOffs(60, 40).addBox(-4.0F, -2.5F, -0.5F, 8.0F, 1.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0083F, 3.7918F, -0.4461F, -0.3491F, 0.0F, 0.0F));

		PartDefinition cube_r3 = body.addOrReplaceChild("cube_r3", CubeListBuilder.create().texOffs(81, 35).addBox(-3.25F, -0.75F, -0.75F, 3.0F, 2.0F, 3.0F, new CubeDeformation(0.0F))
		.texOffs(17, 93).addBox(-0.5F, -0.25F, -0.75F, 1.0F, 2.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(4.2583F, 11.0418F, -1.4461F, -0.1745F, 0.0F, 0.0F));

		PartDefinition cube_r4 = body.addOrReplaceChild("cube_r4", CubeListBuilder.create().texOffs(91, 90).addBox(-0.5F, 0.0F, -1.0F, 1.0F, 2.0F, 3.0F, new CubeDeformation(0.0F))
		.texOffs(83, 0).addBox(0.25F, -0.75F, -1.0F, 3.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-4.2417F, 10.0418F, -1.6961F, -0.3927F, 0.0F, 0.0F));

		PartDefinition tail = body.addOrReplaceChild("tail", CubeListBuilder.create().texOffs(16, 26).addBox(-1.0F, -1.702F, 4.0F, 2.0F, 2.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0083F, 8.9938F, 1.3039F));

		PartDefinition cube_r5 = tail.addOrReplaceChild("cube_r5", CubeListBuilder.create().texOffs(90, 26).addBox(-1.0F, -1.5F, 0.75F, 2.0F, 5.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 1.298F, 10.0F, 0.7418F, 0.0F, 0.0F));

		PartDefinition cube_r6 = tail.addOrReplaceChild("cube_r6", CubeListBuilder.create().texOffs(12, 70).addBox(-1.0F, -2.5F, -2.5F, 2.0F, 2.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 1.298F, 2.25F, 0.2618F, 0.0F, 0.0F));

		PartDefinition left_leg = body.addOrReplaceChild("left_leg", CubeListBuilder.create(), PartPose.offset(3.0083F, 11.3032F, -1.3159F));

		PartDefinition cube_r7 = left_leg.addOrReplaceChild("cube_r7", CubeListBuilder.create().texOffs(44, 0).addBox(-2.0F, -5.5F, -2.0F, 4.0F, 8.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 5.9886F, -0.1302F, -0.1745F, 0.0F, 0.0F));

		PartDefinition cube_r8 = left_leg.addOrReplaceChild("cube_r8", CubeListBuilder.create().texOffs(77, 66).addBox(-1.5F, -2.0F, -1.5F, 3.0F, 4.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 9.4886F, -0.1302F, 0.3927F, 0.0F, 0.0F));

		PartDefinition left_foot = left_leg.addOrReplaceChild("left_foot", CubeListBuilder.create(), PartPose.offset(1.0F, 18.4886F, -0.6302F));

		PartDefinition cube_r9 = left_foot.addOrReplaceChild("cube_r9", CubeListBuilder.create().texOffs(100, 17).addBox(-0.5F, -0.5654F, -2.4986F, 1.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-1.0F, -2.7464F, -0.4776F, 1.309F, 0.0F, 0.0F));

		PartDefinition cube_r10 = left_foot.addOrReplaceChild("cube_r10", CubeListBuilder.create().texOffs(49, 101).addBox(0.3192F, -0.5F, -2.5224F, 1.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-1.0F, -2.7464F, -0.4776F, 1.2752F, -0.0779F, -0.2502F));

		PartDefinition cube_r11 = left_foot.addOrReplaceChild("cube_r11", CubeListBuilder.create().texOffs(72, 4).addBox(-1.5F, -1.0F, -0.5F, 3.0F, 2.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-1.0F, -2.7464F, -0.4776F, 1.2654F, 0.0F, 0.0F));

		PartDefinition cube_r12 = left_foot.addOrReplaceChild("cube_r12", CubeListBuilder.create().texOffs(87, 101).addBox(-1.3192F, -0.5F, -2.5224F, 1.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-1.0F, -2.7464F, -0.4776F, 1.2752F, 0.0779F, 0.2502F));

		PartDefinition cube_r13 = left_foot.addOrReplaceChild("cube_r13", CubeListBuilder.create().texOffs(80, 98).addBox(-1.0F, -1.25F, -1.5F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-1.0F, -6.5F, 1.25F, -0.3927F, 0.0F, 0.0F));

		PartDefinition right_leg = body.addOrReplaceChild("right_leg", CubeListBuilder.create(), PartPose.offset(-2.9917F, 11.7042F, -1.802F));

		PartDefinition cube_r14 = right_leg.addOrReplaceChild("cube_r14", CubeListBuilder.create().texOffs(63, 18).addBox(-2.0F, -9.9732F, -2.6924F, 4.0F, 7.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 7.997F, -5.8935F, -0.6981F, 0.0F, 0.0F));

		PartDefinition cube_r15 = right_leg.addOrReplaceChild("cube_r15", CubeListBuilder.create().texOffs(77, 27).addBox(-1.5F, -5.647F, -0.5398F, 3.0F, 4.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 9.5361F, -4.7315F, 0.0873F, 0.0F, 0.0F));

		PartDefinition right_foot = right_leg.addOrReplaceChild("right_foot", CubeListBuilder.create(), PartPose.offset(1.0F, 18.5875F, 0.8559F));

		PartDefinition cube_r16 = right_foot.addOrReplaceChild("cube_r16", CubeListBuilder.create().texOffs(94, 101).addBox(-0.5F, 0.8053F, -4.5382F, 1.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-1.0F, -9.0514F, -5.5874F, 1.0472F, 0.0F, 0.0F));

		PartDefinition cube_r17 = right_foot.addOrReplaceChild("cube_r17", CubeListBuilder.create().texOffs(101, 101).addBox(-0.1928F, 0.9584F, -4.433F, 1.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-1.0F, -9.0514F, -5.5874F, 1.0192F, -0.1395F, -0.2223F));

		PartDefinition cube_r18 = right_foot.addOrReplaceChild("cube_r18", CubeListBuilder.create().texOffs(72, 59).addBox(-1.5F, 0.4584F, -2.4779F, 3.0F, 2.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-1.0F, -9.0514F, -5.5874F, 1.0036F, 0.0F, 0.0F));

		PartDefinition cube_r19 = right_foot.addOrReplaceChild("cube_r19", CubeListBuilder.create().texOffs(102, 21).addBox(-0.8072F, 0.9584F, -4.433F, 1.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-1.0F, -9.0514F, -5.5874F, 1.0192F, 0.1395F, 0.2223F));

		PartDefinition cube_r20 = right_foot.addOrReplaceChild("cube_r20", CubeListBuilder.create().texOffs(93, 19).addBox(-1.0F, -1.7108F, 0.5931F, 2.0F, 3.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-1.0F, -9.0514F, -5.5874F, 0.4363F, 0.0F, 0.0F));

		PartDefinition left_wing = body.addOrReplaceChild("left_wing", CubeListBuilder.create().texOffs(90, 13).addBox(-0.5F, -2.0F, -0.42F, 4.0F, 4.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(2.5083F, -0.2082F, 5.7239F));

		PartDefinition cube_r21 = left_wing.addOrReplaceChild("cube_r21", CubeListBuilder.create().texOffs(42, 40).addBox(0.75F, 11.5F, -0.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(73, 66).addBox(0.25F, 10.5F, -0.5F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(99, 74).addBox(-0.25F, 8.5F, -0.5F, 3.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(9, 89).addBox(-0.75F, 3.5F, -0.5F, 4.0F, 5.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(61, 100).addBox(-3.5F, -1.5F, -0.5F, 2.0F, 3.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(5.6832F, -0.6823F, 0.08F, 0.0F, 0.0F, -0.3054F));

		PartDefinition cube_r22 = left_wing.addOrReplaceChild("cube_r22", CubeListBuilder.create().texOffs(97, 89).addBox(-3.0008F, -6.4807F, -0.5F, 3.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(63, 95).addBox(-3.0008F, -9.3807F, -0.5F, 3.0F, 3.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(32, 17).addBox(-2.9992F, 8.4807F, -0.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(101, 13).addBox(-3.4992F, 6.4807F, -0.5F, 2.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(98, 65).addBox(-3.9992F, 4.4807F, -0.5F, 3.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(33, 51).addBox(-4.4992F, 1.4807F, -0.5F, 4.0F, 3.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(77, 11).addBox(-4.9992F, -4.5193F, -0.5F, 5.0F, 6.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(11.5114F, -15.7138F, 0.08F, 0.0F, 0.0F, -2.2689F));

		PartDefinition cube_r23 = left_wing.addOrReplaceChild("cube_r23", CubeListBuilder.create().texOffs(42, 100).addBox(-0.25F, -2.25F, -0.5F, 2.0F, 3.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(9.1848F, -7.5048F, 0.08F, 0.0F, 0.0F, -2.2689F));

		PartDefinition cube_r24 = left_wing.addOrReplaceChild("cube_r24", CubeListBuilder.create().texOffs(91, 96).addBox(2.5F, -0.5F, -0.5F, 3.0F, 3.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(8.9287F, -1.8358F, 0.08F, 0.0F, 0.0F, -1.8326F));

		PartDefinition cube_r25 = left_wing.addOrReplaceChild("cube_r25", CubeListBuilder.create().texOffs(53, 96).addBox(-2.5F, -0.75F, -0.5F, 3.0F, 3.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(6.2771F, -0.9519F, 0.08F, 0.0F, 0.0F, -0.0436F));

		PartDefinition cube_r26 = left_wing.addOrReplaceChild("cube_r26", CubeListBuilder.create().texOffs(99, 25).addBox(2.55F, -3.0F, -0.5F, 2.0F, 4.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(11.2572F, -0.5549F, 0.08F, 0.0F, 0.0F, -1.5708F));

		PartDefinition cube_r27 = left_wing.addOrReplaceChild("cube_r27", CubeListBuilder.create().texOffs(28, 99).addBox(-3.25F, 0.0F, -0.5F, 2.0F, 4.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(8.9287F, -1.8358F, 0.08F, 0.0F, 0.0F, -0.3491F));

		PartDefinition cube_r28 = left_wing.addOrReplaceChild("cube_r28", CubeListBuilder.create().texOffs(96, 0).addBox(-1.5F, -2.25F, -0.5F, 3.0F, 3.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(9.8126F, -3.0732F, 0.08F, 0.0F, 0.0F, -1.5272F));

		PartDefinition cube_r29 = left_wing.addOrReplaceChild("cube_r29", CubeListBuilder.create().texOffs(97, 34).addBox(-2.0F, -1.75F, -0.5F, 3.0F, 3.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(8.9287F, -1.8358F, 0.08F, 0.0F, 0.0F, -0.7854F));

		PartDefinition cube_r30 = left_wing.addOrReplaceChild("cube_r30", CubeListBuilder.create().texOffs(0, 40).addBox(2.05F, 8.35F, -0.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(72, 45).addBox(1.55F, 7.35F, -0.5F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(71, 99).addBox(1.05F, 5.35F, -0.5F, 3.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(53, 89).addBox(0.55F, 0.6F, -0.5F, 4.0F, 5.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(11.6137F, -1.6338F, 0.08F, 0.0F, 0.0F, -1.5708F));

		PartDefinition right_wing = body.addOrReplaceChild("right_wing", CubeListBuilder.create().texOffs(90, 7).addBox(-3.5F, -2.0F, -0.42F, 4.0F, 4.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(-2.4917F, -0.2082F, 5.7239F));

		PartDefinition cube_r31 = right_wing.addOrReplaceChild("cube_r31", CubeListBuilder.create().texOffs(32, 0).addBox(-1.75F, 11.5F, -0.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(65, 45).addBox(-2.25F, 10.5F, -0.5F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(0, 33).addBox(-2.75F, 8.5F, -0.5F, 3.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(23, 87).addBox(-3.25F, 3.5F, -0.5F, 4.0F, 5.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(35, 100).addBox(1.5F, -1.5F, -0.5F, 2.0F, 3.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-5.6832F, -0.6823F, 0.08F, 0.0F, 0.0F, 0.3054F));

		PartDefinition cube_r32 = right_wing.addOrReplaceChild("cube_r32", CubeListBuilder.create().texOffs(44, 33).addBox(0.0008F, -6.4807F, -0.5F, 3.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(26, 94).addBox(0.0008F, -9.3807F, -0.5F, 3.0F, 3.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(32, 5).addBox(1.9992F, 8.4807F, -0.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(101, 9).addBox(1.4992F, 6.4807F, -0.5F, 2.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(72, 0).addBox(0.9992F, 4.4807F, -0.5F, 3.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(22, 40).addBox(0.4992F, 1.4807F, -0.5F, 4.0F, 3.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(0, 77).addBox(-0.0008F, -4.5193F, -0.5F, 5.0F, 6.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-11.5114F, -15.7138F, 0.08F, 0.0F, 0.0F, 2.2689F));

		PartDefinition cube_r33 = right_wing.addOrReplaceChild("cube_r33", CubeListBuilder.create().texOffs(0, 100).addBox(-1.75F, -2.25F, -0.5F, 2.0F, 3.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-9.1848F, -7.5048F, 0.08F, 0.0F, 0.0F, 2.2689F));

		PartDefinition cube_r34 = right_wing.addOrReplaceChild("cube_r34", CubeListBuilder.create().texOffs(94, 51).addBox(-5.5F, -0.5F, -0.5F, 3.0F, 3.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-8.9287F, -1.8358F, 0.08F, 0.0F, 0.0F, 1.8326F));

		PartDefinition cube_r35 = right_wing.addOrReplaceChild("cube_r35", CubeListBuilder.create().texOffs(0, 95).addBox(-0.5F, -0.75F, -0.5F, 3.0F, 3.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-6.2771F, -0.9519F, 0.08F, 0.0F, 0.0F, 0.0436F));

		PartDefinition cube_r36 = right_wing.addOrReplaceChild("cube_r36", CubeListBuilder.create().texOffs(14, 99).addBox(-4.55F, -3.0F, -0.5F, 2.0F, 4.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-11.2572F, -0.5549F, 0.08F, 0.0F, 0.0F, 1.5708F));

		PartDefinition cube_r37 = right_wing.addOrReplaceChild("cube_r37", CubeListBuilder.create().texOffs(21, 99).addBox(1.25F, 0.0F, -0.5F, 2.0F, 4.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-8.9287F, -1.8358F, 0.08F, 0.0F, 0.0F, 0.3491F));

		PartDefinition cube_r38 = right_wing.addOrReplaceChild("cube_r38", CubeListBuilder.create().texOffs(35, 95).addBox(-1.5F, -2.25F, -0.5F, 3.0F, 3.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-9.8126F, -3.0732F, 0.08F, 0.0F, 0.0F, 1.5272F));

		PartDefinition cube_r39 = right_wing.addOrReplaceChild("cube_r39", CubeListBuilder.create().texOffs(44, 95).addBox(-1.0F, -1.75F, -0.5F, 3.0F, 3.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-8.9287F, -1.8358F, 0.08F, 0.0F, 0.0F, 0.7854F));

		PartDefinition cube_r40 = right_wing.addOrReplaceChild("cube_r40", CubeListBuilder.create().texOffs(32, 14).addBox(-3.05F, 8.35F, -0.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(68, 59).addBox(-3.55F, 7.35F, -0.5F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(13, 78).addBox(-4.05F, 5.35F, -0.5F, 3.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(87, 57).addBox(-4.55F, 0.6F, -0.5F, 4.0F, 5.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-11.6137F, -1.6338F, 0.08F, 0.0F, 0.0F, 1.5708F));

		PartDefinition head = body.addOrReplaceChild("head", CubeListBuilder.create(), PartPose.offset(0.0083F, 0.4327F, 0.8039F));

		PartDefinition cube_r41 = head.addOrReplaceChild("cube_r41", CubeListBuilder.create().texOffs(59, 82).addBox(-2.5F, -3.0F, -0.5F, 5.0F, 5.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -9.6498F, -4.0009F, 2.0071F, 0.0F, 0.0F));

		PartDefinition cube_r42 = head.addOrReplaceChild("cube_r42", CubeListBuilder.create().texOffs(98, 42).addBox(-0.5F, -8.0F, -3.5F, 2.0F, 1.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(22, 0).addBox(-1.0F, -7.0F, -4.0F, 3.0F, 1.0F, 3.0F, new CubeDeformation(0.0F))
		.texOffs(0, 69).addBox(-1.5F, -6.0F, -4.5F, 4.0F, 1.0F, 4.0F, new CubeDeformation(0.0F))
		.texOffs(58, 30).addBox(-2.0F, -5.0F, -5.0F, 5.0F, 1.0F, 5.0F, new CubeDeformation(0.0F))
		.texOffs(0, 20).addBox(-2.5F, -4.0F, -5.0F, 6.0F, 7.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.5F, -15.1143F, -2.7509F, 1.5708F, 0.0F, 0.0F));

		PartDefinition cube_r43 = head.addOrReplaceChild("cube_r43", CubeListBuilder.create().texOffs(0, 85).addBox(0.25F, -3.25F, -1.25F, 2.0F, 7.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(2.0F, -16.1143F, -3.2509F, 2.193F, 0.1782F, 0.1265F));

		PartDefinition cube_r44 = head.addOrReplaceChild("cube_r44", CubeListBuilder.create().texOffs(9, 96).addBox(-0.5F, -3.5F, -0.5F, 1.0F, 7.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(3.6317F, -11.9725F, -1.2363F, 0.7967F, 0.1782F, 0.1265F));

		PartDefinition cube_r45 = head.addOrReplaceChild("cube_r45", CubeListBuilder.create().texOffs(98, 56).addBox(-0.5F, -3.5F, -0.5F, 1.0F, 7.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-3.6317F, -11.9725F, -1.2363F, 0.7967F, -0.1782F, -0.1265F));

		PartDefinition cube_r46 = head.addOrReplaceChild("cube_r46", CubeListBuilder.create().texOffs(44, 85).addBox(-2.25F, -3.25F, -1.25F, 2.0F, 7.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-2.0F, -16.1143F, -3.2509F, 2.193F, -0.1782F, -0.1265F));

		PartDefinition cube_r47 = head.addOrReplaceChild("cube_r47", CubeListBuilder.create().texOffs(87, 64).addBox(-1.5F, -1.5566F, -0.5924F, 3.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -0.9317F, 3.8625F, -1.2654F, 0.0F, 0.0F));

		PartDefinition cube_r48 = head.addOrReplaceChild("cube_r48", CubeListBuilder.create().texOffs(80, 19).addBox(-1.5F, -1.4544F, -2.1884F, 3.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -0.9317F, 3.8625F, -0.3927F, 0.0F, 0.0F));

		PartDefinition cube_r49 = head.addOrReplaceChild("cube_r49", CubeListBuilder.create().texOffs(90, 77).addBox(-1.5F, -1.5566F, -0.5924F, 3.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -4.9317F, 5.1125F, -1.2654F, 0.0F, 0.0F));

		PartDefinition cube_r50 = head.addOrReplaceChild("cube_r50", CubeListBuilder.create().texOffs(80, 79).addBox(-1.5F, -1.4544F, -2.1884F, 3.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -4.9317F, 5.1125F, -0.3927F, 0.0F, 0.0F));

		PartDefinition cube_r51 = head.addOrReplaceChild("cube_r51", CubeListBuilder.create().texOffs(103, 50).addBox(-1.5F, -1.5187F, -2.1998F, 3.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -9.3674F, 5.6239F, 0.0F, 0.0F, 0.0F));

		PartDefinition cube_r52 = head.addOrReplaceChild("cube_r52", CubeListBuilder.create().texOffs(91, 46).addBox(-1.5F, -1.5241F, -0.5358F, 3.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -9.496F, 5.601F, -0.8727F, 0.0F, 0.0F));

		PartDefinition cube_r53 = head.addOrReplaceChild("cube_r53", CubeListBuilder.create().texOffs(91, 84).addBox(-1.5F, 0.25F, -2.0F, 3.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -14.375F, 5.1715F, -0.48F, 0.0F, 0.0F));

		PartDefinition cube_r54 = head.addOrReplaceChild("cube_r54", CubeListBuilder.create().texOffs(10, 82).addBox(-1.5F, -1.5F, -1.5F, 3.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -13.1169F, 2.4702F, 0.3927F, 0.0F, 0.0F));

		PartDefinition cube_r55 = head.addOrReplaceChild("cube_r55", CubeListBuilder.create().texOffs(53, 58).addBox(-2.5F, -1.75F, -4.5F, 5.0F, 5.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -13.6698F, 1.9635F, 0.7418F, 0.0F, 0.0F));

		PartDefinition cube_r56 = head.addOrReplaceChild("cube_r56", CubeListBuilder.create().texOffs(0, 59).addBox(-2.0F, -0.75F, -3.25F, 5.0F, 5.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.5F, -10.3684F, 3.0706F, -0.0436F, 0.0F, 0.0F));

		PartDefinition cube_r57 = head.addOrReplaceChild("cube_r57", CubeListBuilder.create().texOffs(34, 58).addBox(-2.0F, -4.0F, -2.5F, 5.0F, 5.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.5F, -2.641F, 1.0F, -0.2618F, 0.0F, 0.0F));

		PartDefinition left_arm = body.addOrReplaceChild("left_arm", CubeListBuilder.create().texOffs(58, 8).addBox(0.0F, -1.6642F, -2.5286F, 4.0F, 4.0F, 5.0F, new CubeDeformation(0.0F))
		.texOffs(60, 68).addBox(-0.25F, -2.1642F, -2.0286F, 4.0F, 1.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(4.0083F, 0.059F, 1.6269F));

		PartDefinition cube_r58 = left_arm.addOrReplaceChild("cube_r58", CubeListBuilder.create().texOffs(79, 41).addBox(-1.5F, 11.0F, -1.65F, 2.0F, 2.0F, 4.0F, new CubeDeformation(0.0F))
		.texOffs(86, 70).addBox(-3.5F, 11.0F, -1.65F, 1.0F, 2.0F, 4.0F, new CubeDeformation(0.0F))
		.texOffs(44, 23).addBox(-3.5F, 7.0F, -1.9F, 4.0F, 4.0F, 5.0F, new CubeDeformation(0.0F))
		.texOffs(45, 68).addBox(-3.0F, 3.0F, -1.4F, 3.0F, 4.0F, 4.0F, new CubeDeformation(0.0F))
		.texOffs(35, 77).addBox(-2.5F, -3.0F, -0.9F, 2.0F, 6.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(7.0725F, 3.8657F, -0.4886F, 0.0F, 0.0F, 0.0F));

		PartDefinition left_shoulder = left_arm.addOrReplaceChild("left_shoulder", CubeListBuilder.create().texOffs(19, 51).addBox(-2.0F, -2.0F, -2.5F, 4.0F, 4.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(4.1234F, 1.2274F, 0.0963F, 0.0F, 0.0F, 0.7854F));

		PartDefinition right_arm = body.addOrReplaceChild("right_arm", CubeListBuilder.create(), PartPose.offset(-4.0901F, 0.4818F, 1.2128F));

		PartDefinition cube_r59 = right_arm.addOrReplaceChild("cube_r59", CubeListBuilder.create().texOffs(30, 68).addBox(-1.5F, -4.0F, -5.0F, 3.0F, 4.0F, 4.0F, new CubeDeformation(0.0F))
		.texOffs(44, 13).addBox(-2.0F, 0.0F, -5.5F, 4.0F, 4.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-5.474F, 10.1384F, 1.0023F, -0.7854F, 0.0F, 0.0F));

		PartDefinition cube_r60 = right_arm.addOrReplaceChild("cube_r60", CubeListBuilder.create().texOffs(68, 53).addBox(-0.8133F, -1.9129F, -2.0F, 4.0F, 1.0F, 4.0F, new CubeDeformation(0.0F))
		.texOffs(0, 49).addBox(-1.0633F, -1.4129F, -2.5F, 4.0F, 4.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-2.8382F, -0.6721F, 0.5101F, 0.0F, 0.0F, 0.0F));

		PartDefinition cube_r61 = right_arm.addOrReplaceChild("cube_r61", CubeListBuilder.create().texOffs(24, 77).addBox(-1.0F, -7.25F, 0.75F, 2.0F, 6.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-5.474F, 7.7389F, -1.3546F, 0.0436F, 0.0F, 0.0F));

		PartDefinition cube_r62 = right_arm.addOrReplaceChild("cube_r62", CubeListBuilder.create().texOffs(45, 48).addBox(-1.76F, -0.1706F, -2.5F, 4.0F, 4.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-2.8382F, -0.6721F, 0.5101F, 0.0F, 0.0F, 0.829F));

		PartDefinition weapon = right_arm.addOrReplaceChild("weapon", CubeListBuilder.create().texOffs(0, 0).addBox(-1.5F, 1.75F, -13.0F, 3.0F, 2.0F, 37.0F, new CubeDeformation(0.0F))
		.texOffs(27, 40).addBox(-1.5F, 1.75F, -20.25F, 3.0F, 2.0F, 8.0F, new CubeDeformation(0.0F))
		.texOffs(70, 74).addBox(-1.5F, -2.25F, 20.0F, 3.0F, 4.0F, 3.0F, new CubeDeformation(0.0F))
		.texOffs(14, 49).addBox(-1.0F, 1.75F, 28.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(61, 0).addBox(-1.5F, 1.25F, 24.0F, 3.0F, 3.0F, 4.0F, new CubeDeformation(0.0F))
		.texOffs(57, 74).addBox(-1.5F, 3.75F, 20.0F, 3.0F, 4.0F, 3.0F, new CubeDeformation(0.0F))
		.texOffs(82, 93).addBox(-1.0F, -4.25F, 20.5F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(93, 69).addBox(-1.0F, 7.75F, 20.5F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(-5.474F, 7.7389F, -2.3546F));

		PartDefinition cube_r63 = weapon.addOrReplaceChild("cube_r63", CubeListBuilder.create().texOffs(100, 93).addBox(-1.0F, 1.0F, -1.0F, 2.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 7.4327F, -0.3746F, -1.5708F, 0.0F, 0.0F));

		PartDefinition cube_r64 = weapon.addOrReplaceChild("cube_r64", CubeListBuilder.create().texOffs(57, 0).addBox(-1.0F, -1.0F, -1.0F, 2.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 7.2591F, -0.3898F, -1.3963F, 0.0F, 0.0F));

		PartDefinition cube_r65 = weapon.addOrReplaceChild("cube_r65", CubeListBuilder.create().texOffs(0, 0).addBox(-0.5F, 0.75F, 2.875F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(22, 5).addBox(-1.0F, 0.25F, -2.125F, 2.0F, 3.0F, 5.0F, new CubeDeformation(0.0F))
		.texOffs(91, 38).addBox(-0.5F, 0.75F, -4.875F, 1.0F, 2.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 8.4075F, -10.0295F, -2.6616F, 0.0F, 0.0F));

		PartDefinition cube_r66 = weapon.addOrReplaceChild("cube_r66", CubeListBuilder.create().texOffs(0, 10).addBox(-0.5F, 0.0F, 2.25F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(46, 78).addBox(-1.0F, -0.5F, -1.75F, 2.0F, 2.0F, 4.0F, new CubeDeformation(0.0F))
		.texOffs(9, 33).addBox(-0.5F, 0.0F, -3.75F, 1.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 10.2988F, -5.6174F, -2.0308F, 0.069F, -0.0485F));

		PartDefinition cube_r67 = weapon.addOrReplaceChild("cube_r67", CubeListBuilder.create().texOffs(0, 20).addBox(-0.5F, 0.75F, 4.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(64, 89).addBox(-1.0F, 0.25F, 1.0F, 2.0F, 2.0F, 3.0F, new CubeDeformation(0.0F))
		.texOffs(99, 80).addBox(-0.5F, 0.75F, -1.0F, 1.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 8.2439F, -0.2161F, -1.7017F, 0.0F, 0.0F));

		PartDefinition cube_r68 = weapon.addOrReplaceChild("cube_r68", CubeListBuilder.create().texOffs(100, 97).addBox(-1.0F, 3.0F, -1.0F, 2.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 7.9536F, -0.4201F, -1.7453F, 0.0F, 0.0F));

		PartDefinition cube_r69 = weapon.addOrReplaceChild("cube_r69", CubeListBuilder.create().texOffs(56, 68).addBox(-1.0F, 5.5F, 4.25F, 2.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 6.2685F, 2.2153F, -2.2253F, 0.0F, 0.0F));

		PartDefinition cube_r70 = weapon.addOrReplaceChild("cube_r70", CubeListBuilder.create().texOffs(101, 5).addBox(-1.0F, -0.5F, -0.75F, 2.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 5.9739F, 1.6329F, -0.9163F, 0.0F, 0.0F));

		PartDefinition cube_r71 = weapon.addOrReplaceChild("cube_r71", CubeListBuilder.create().texOffs(49, 58).addBox(-1.0F, -1.0F, -0.5F, 2.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0658F, -9.9323F, -2.0508F, 0.0F, 0.0F));

		PartDefinition cube_r72 = weapon.addOrReplaceChild("cube_r72", CubeListBuilder.create().texOffs(64, 53).addBox(-1.0F, -1.0F, -0.5F, 2.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0658F, -9.9323F, -2.0071F, 0.0F, 0.0F));

		PartDefinition cube_r73 = weapon.addOrReplaceChild("cube_r73", CubeListBuilder.create().texOffs(72, 83).addBox(-1.0F, -1.5F, -2.75F, 2.0F, 4.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 1.4675F, -16.5794F, -0.6545F, 0.0F, 0.0F));

		PartDefinition cube_r74 = weapon.addOrReplaceChild("cube_r74", CubeListBuilder.create().texOffs(22, 15).addBox(-1.0F, -3.0F, -3.0F, 2.0F, 3.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 1.4675F, -16.5794F, -0.9163F, 0.0F, 0.0F));

		PartDefinition cube_r75 = weapon.addOrReplaceChild("cube_r75", CubeListBuilder.create().texOffs(117, 11).addBox(1.0F, -4.75F, 1.75F, -2.0F, 11.0F, -2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -2.5833F, -7.2426F, -1.2677F, 0.0F, 0.0F));

		PartDefinition cube_r76 = weapon.addOrReplaceChild("cube_r76", CubeListBuilder.create().texOffs(126, 9).addBox(1.0F, -10.5F, 1.75F, -2.0F, 9.0F, -2.0F, new CubeDeformation(0.0F))
		.texOffs(83, 86).addBox(-1.0F, -1.5F, -0.25F, 2.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -3.4363F, -11.8344F, -1.4835F, 0.0F, 0.0F));

		PartDefinition cube_r77 = weapon.addOrReplaceChild("cube_r77", CubeListBuilder.create().texOffs(19, 61).addBox(-1.0F, -1.0F, -8.25F, 2.0F, 3.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 4.7067F, -11.8882F, -1.2654F, 0.0F, 0.0F));

		PartDefinition cube_r78 = weapon.addOrReplaceChild("cube_r78", CubeListBuilder.create().texOffs(27, 45).addBox(-1.0F, 0.25F, -0.25F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -0.3394F, -11.5884F, -0.7854F, 0.0F, 0.0F));

		PartDefinition cube_r79 = weapon.addOrReplaceChild("cube_r79", CubeListBuilder.create().texOffs(41, 68).addBox(-1.0F, -2.0F, -5.5F, 2.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 4.6606F, -12.5884F, -1.5708F, 0.0F, 0.0F));

		PartDefinition cube_r80 = weapon.addOrReplaceChild("cube_r80", CubeListBuilder.create().texOffs(75, 91).addBox(-1.0F, -9.0F, -8.5F, 2.0F, 6.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 4.6041F, 1.994F, 0.48F, 0.0F, 0.0F));

		PartDefinition cube_r81 = weapon.addOrReplaceChild("cube_r81", CubeListBuilder.create().texOffs(22, 70).addBox(-1.0F, -2.0F, -0.5F, 2.0F, 3.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 4.75F, 2.2F, -0.48F, 0.0F, 0.0F));

		return LayerDefinition.create(meshdefinition, 128, 128);
	}

    @Override
    public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
    	//animation.set(0,new ModelAnimation(right_wing, 0, -40, 40, 0, true, Angle.Y, left_wing));
    	//this.left_wing.xRot =0;
    	/*this.left_arm.y = -3;
    	this.right_arm.y = -3;
    	this.left_leg.y = 8;
    	this.right_leg.y = 8;
    	this.head.y = -4;*/
    	
        if(!Minecraft.getInstance().isPaused()) {
        	//EntityHelper.setState(entity,0);
        	if(EntityHelper.getState(entity) == 0) { //Normal, arms in front
        		this.head.xRot= 0;
        		this.left_leg.xRot= 0;
        		this.right_leg.xRot= 0;
        		this.left_wing.xRot = 0;
        		this.right_wing.xRot = 0;
        		this.body.z = 0;
        		this.body.xRot = 0;


        		if(animation.size() > 0) {
	                animation.get(0).animate();
	        	}
        		
        	} else if(EntityHelper.getState(entity) == 1) { //Ball-shape
        		this.head.xRot = 90;
        		this.left_leg.xRot = 90;
        		this.right_leg.xRot = 90;
        		this.left_wing.xRot = 90;
        		this.right_wing.xRot = 90;
        		this.body.xRot = 0;
        		this.body.z = 0;

        	} else if(EntityHelper.getState(entity) == 2) { //Falling down
        		this.head.xRot = 90;
        		this.left_leg.xRot = 90;
        		this.right_leg.xRot = 90;
        		this.left_wing.xRot = 90;
        		this.right_wing.xRot = 90;
        		this.body.xRot = (float) Math.toRadians(90);
        		this.body.z = -3;
        	}
        		
        }

        	
        	/*if(entity.distanceToSqr(entity.xOld, entity.yOld, entity.zOld) > 0) {
        		for(int i = 0; i < animation.size(); i++) { //iterate through the legs array
                    ModelAnimation m = animation.get(i);
                    m.animate();
                }
        	} else {
        		for(int i = 0; i < animation.size(); i++) { //iterate through the legs array
                    ModelAnimation m = animation.get(i);
                    m.setDefault();
                }
        		//this.right_arm.zRot = 0;
        		//this.left_arm.zRot = 0;
        	}
	        
        }	*/
    }

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer buffer, int packedLight, int packedOverlay, float pRed, float pGreen, float pBlue, float pAlpha) {
		//this.head.render(poseStack, buffer, packedLight, packedOverlay);
		this.body.render(poseStack, buffer, packedLight, packedOverlay);
		/*this.right_arm.render(poseStack, buffer, packedLight, packedOverlay);
		this.right_leg.render(poseStack, buffer, packedLight, packedOverlay);
		this.left_arm.render(poseStack, buffer, packedLight, packedOverlay);
		this.left_leg.render(poseStack, buffer, packedLight, packedOverlay);*/
	}
}
