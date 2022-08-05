package online.kingdomkeys.kingdomkeys.client.model.entity;

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
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.entity.EntityHelper;

//TODO port new model
@OnlyIn(Dist.CLIENT)
public class MarluxiaModel<T extends LivingEntity> extends HumanoidModel<T> {
    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation(KingdomKeys.MODID, "marluxia"), "main");
    private final ModelPart head;
	private final ModelPart body;
	private final ModelPart hat;
	private final ModelPart right_arm;
	private final ModelPart right_leg;
	private final ModelPart left_arm;
	private final ModelPart left_leg;

	public MarluxiaModel(ModelPart root) {
		super(root);
		this.head = root.getChild("head");
		this.body = root.getChild("body");
		this.hat = root.getChild("hat");
		this.right_arm = root.getChild("right_arm");
		this.right_leg = root.getChild("right_leg");
		this.left_arm = root.getChild("left_arm");
		this.left_leg = root.getChild("left_leg");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition head = partdefinition.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 0).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition hair = head.addOrReplaceChild("hair", CubeListBuilder.create().texOffs(0, 0).addBox(-4.0F, -32.0F, -3.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.0F))
		.texOffs(0, 0).addBox(-4.0F, -32.5F, -3.0F, 8.0F, 1.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 24.0F, 0.0F));

		PartDefinition cube_r1 = hair.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(27, 1).addBox(-27.0F, -18.0F, -0.5F, 4.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.6417F, 0.3013F, 1.2132F));

		PartDefinition cube_r2 = hair.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(27, 1).addBox(-29.0F, -14.0F, 0.0F, 5.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(27, 1).addBox(-29.0F, -14.0F, -1.5F, 3.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 1.1202F, 0.3281F, 1.4586F));

		PartDefinition cube_r3 = hair.addOrReplaceChild("cube_r3", CubeListBuilder.create().texOffs(27, 1).addBox(-29.0F, -14.0F, -0.75F, 3.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 1.6142F, 0.3555F, 1.5007F));

		PartDefinition cube_r4 = hair.addOrReplaceChild("cube_r4", CubeListBuilder.create().texOffs(27, 1).addBox(-23.0F, 16.0F, -6.5F, 5.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -2.8639F, -0.0619F, 1.0233F));

		PartDefinition cube_r5 = hair.addOrReplaceChild("cube_r5", CubeListBuilder.create().texOffs(25, 2).addBox(-27.0F, 2.0F, 14.5F, 5.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 1.4897F, -0.0485F, 1.1976F));

		PartDefinition cube_r6 = hair.addOrReplaceChild("cube_r6", CubeListBuilder.create().texOffs(27, 1).addBox(-28.0F, 11.0F, 10.5F, 5.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 2.0133F, -0.0485F, 1.1976F));

		PartDefinition cube_r7 = hair.addOrReplaceChild("cube_r7", CubeListBuilder.create().texOffs(27, 1).addBox(-30.0F, 5.0F, 8.5F, 6.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 2.2667F, 0.0261F, 1.3482F));

		PartDefinition cube_r8 = hair.addOrReplaceChild("cube_r8", CubeListBuilder.create().texOffs(27, 1).addBox(-30.0F, 6.0F, -8.8F, 11.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 2.2756F, -0.2119F, 1.592F));

		PartDefinition cube_r9 = hair.addOrReplaceChild("cube_r9", CubeListBuilder.create().texOffs(27, 1).addBox(-30.0F, 6.0F, -8.8F, 11.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(27, 1).addBox(-30.0F, 7.0F, -7.8F, 11.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(27, 1).addBox(-30.0F, 8.65F, -3.8F, 11.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(27, 0).addBox(-30.0F, 9.65F, -4.8F, 11.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 2.1011F, -0.2119F, 1.592F));

		PartDefinition cube_r10 = hair.addOrReplaceChild("cube_r10", CubeListBuilder.create().texOffs(27, 1).addBox(-27.0F, -8.5F, -15.5F, 4.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 1.2729F, 0.335F, 1.8521F));

		PartDefinition cube_r11 = hair.addOrReplaceChild("cube_r11", CubeListBuilder.create().texOffs(27, 1).addBox(-27.0F, 5.0F, -10.5F, 5.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(27, 1).addBox(-29.0F, 2.0F, -12.5F, 5.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.929F, 0.1011F, 1.8319F));

		PartDefinition cube_r12 = hair.addOrReplaceChild("cube_r12", CubeListBuilder.create().texOffs(27, 1).addBox(-28.0F, 6.5F, -11.5F, 6.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(27, 1).addBox(-29.0F, 9.0F, -10.5F, 6.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 1.279F, -0.0661F, 1.8822F));

		PartDefinition cube_r13 = hair.addOrReplaceChild("cube_r13", CubeListBuilder.create().texOffs(27, 1).addBox(-28.0F, 7.0F, -14.5F, 7.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 1.2958F, -0.03F, 2.0061F));

		PartDefinition cube_r14 = hair.addOrReplaceChild("cube_r14", CubeListBuilder.create().texOffs(27, 1).addBox(-30.0F, 5.0F, 10.5F, 7.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(27, 1).addBox(-30.0F, 7.0F, 8.5F, 7.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(24, 0).addBox(-29.0F, 8.0F, 8.5F, 7.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 2.0967F, -0.0413F, 1.2846F));

		PartDefinition cube_r15 = hair.addOrReplaceChild("cube_r15", CubeListBuilder.create().texOffs(27, 1).addBox(-29.0F, 11.0F, 5.5F, 7.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 2.0529F, -0.1009F, 1.3005F));

		PartDefinition cube_r16 = hair.addOrReplaceChild("cube_r16", CubeListBuilder.create().texOffs(27, 1).addBox(-30.0F, -12.0F, 0.5F, 7.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -2.0319F, -0.1657F, 1.6866F));

		PartDefinition cube_r17 = hair.addOrReplaceChild("cube_r17", CubeListBuilder.create().texOffs(27, 1).addBox(-27.0F, -2.0F, -17.5F, 4.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 1.2993F, 0.1498F, 2.0009F));

		PartDefinition cube_r18 = hair.addOrReplaceChild("cube_r18", CubeListBuilder.create().texOffs(27, 1).addBox(-28.0F, 0.0F, -16.5F, 5.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 1.2496F, 0.1379F, 1.9584F));

		PartDefinition cube_r19 = hair.addOrReplaceChild("cube_r19", CubeListBuilder.create().texOffs(27, 1).addBox(-27.0F, 0.0F, -16.5F, 5.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 1.2993F, 0.1498F, 2.0009F));

		PartDefinition cube_r20 = hair.addOrReplaceChild("cube_r20", CubeListBuilder.create().texOffs(27, 1).addBox(-26.0F, 0.0F, -14.5F, 5.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(27, 1).addBox(-28.0F, 1.0F, -15.5F, 5.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 1.2932F, 0.1379F, 1.9584F));

		PartDefinition cube_r21 = hair.addOrReplaceChild("cube_r21", CubeListBuilder.create().texOffs(27, 1).addBox(-28.0F, -2.0F, -12.5F, 6.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(27, 1).addBox(-29.0F, -1.0F, -12.5F, 6.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 1.2781F, 0.1011F, 1.8319F));

		PartDefinition cube_r22 = hair.addOrReplaceChild("cube_r22", CubeListBuilder.create().texOffs(27, 1).addBox(-29.0F, 8.0F, -9.5F, 7.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 1.1185F, -0.0085F, 1.8503F));

		PartDefinition cube_r23 = hair.addOrReplaceChild("cube_r23", CubeListBuilder.create().texOffs(27, 1).addBox(-29.0F, 6.0F, -7.5F, 9.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(27, 1).addBox(-29.0F, 8.0F, -4.2F, 11.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(27, 1).addBox(-29.0F, 9.0F, -5.2F, 11.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(27, 1).addBox(-30.0F, 8.0F, -6.5F, 7.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(27, 1).addBox(-29.0F, 7.0F, -8.5F, 9.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 2.0574F, -0.2119F, 1.592F));

		PartDefinition cube_r24 = hair.addOrReplaceChild("cube_r24", CubeListBuilder.create().texOffs(27, 1).addBox(-29.0F, 9.0F, -2.2F, 9.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(27, 1).addBox(-29.0F, 10.0F, -3.2F, 9.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 2.0574F, -0.2119F, 1.592F));

		PartDefinition cube_r25 = hair.addOrReplaceChild("cube_r25", CubeListBuilder.create().texOffs(27, 1).addBox(-29.0F, 6.0F, -7.2F, 11.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(27, 1).addBox(-29.0F, 5.0F, -8.2F, 11.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(27, 1).addBox(-29.0F, 7.0F, -8.2F, 11.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 2.2756F, -0.2119F, 1.592F));

		PartDefinition cube_r26 = hair.addOrReplaceChild("cube_r26", CubeListBuilder.create().texOffs(27, 1).addBox(-27.0F, 17.0F, -2.5F, 8.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 1.2145F, -0.3813F, 1.8024F));

		PartDefinition cube_r27 = hair.addOrReplaceChild("cube_r27", CubeListBuilder.create().texOffs(27, 1).addBox(-24.0F, 16.0F, -5.5F, 6.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 2.1118F, -0.4785F, 1.433F));

		PartDefinition cube_r28 = hair.addOrReplaceChild("cube_r28", CubeListBuilder.create().texOffs(27, 1).addBox(-29.0F, -1.0F, 7.5F, 4.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 1.4812F, -0.0299F, 1.4151F));

		PartDefinition cube_r29 = hair.addOrReplaceChild("cube_r29", CubeListBuilder.create().texOffs(27, 1).addBox(-27.0F, -13.0F, -13.5F, 4.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 1.2563F, 0.4598F, 1.8093F));

		PartDefinition Cube_r30 = hair.addOrReplaceChild("Cube_r30", CubeListBuilder.create().texOffs(27, 1).addBox(-27.0F, -16.0F, -9.0F, 4.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 1.1827F, 0.4967F, 1.6371F));

		PartDefinition body = partdefinition.addOrReplaceChild("body", CubeListBuilder.create().texOffs(16, 16).addBox(-4.0F, 0.0F, -2.0F, 8.0F, 12.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition hat = partdefinition.addOrReplaceChild("hat", CubeListBuilder.create().texOffs(32, 0).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition right_arm = partdefinition.addOrReplaceChild("right_arm", CubeListBuilder.create().texOffs(40, 16).addBox(-1.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(5.0F, 2.0F, 0.0F, 0.0F, 0.0F, -0.1F));

		PartDefinition right_leg = partdefinition.addOrReplaceChild("right_leg", CubeListBuilder.create().texOffs(0, 16).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(1.9F, 12.0F, 0.1F));

		PartDefinition left_arm = partdefinition.addOrReplaceChild("left_arm", CubeListBuilder.create().texOffs(32, 48).addBox(-3.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-5.0F, 2.0F, 0.0F, 0.0F, 0.0F, 0.1F));

		PartDefinition left_leg = partdefinition.addOrReplaceChild("left_leg", CubeListBuilder.create().texOffs(16, 48).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(-1.9F, 12.0F, 0.1F));
		return LayerDefinition.create(meshdefinition, 64, 64);
	}

    @Override
    public void setupAnim(T entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
    	super.setupAnim(entityIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
    	this.rightArm.x=-7;
    	this.leftArm.x=7;
    	this.head.xRot -= (float) Math.toRadians(-20);
    	
    	if(EntityHelper.getState(entityIn) == 1) {
    		this.rightArm.zRot = (float) Math.toRadians(20);
    		this.leftArm.zRot = (float) Math.toRadians(-20);
    		this.leftArm.xRot = (float) Math.toRadians(0);

    		this.rightLeg.xRot = 0;
    		this.leftLeg.xRot = 0;
    		
    		this.rightLeg.zRot = (float) Math.toRadians(10);
    		this.leftLeg.zRot = (float) Math.toRadians(-10);	
    	}
    	
    	if(EntityHelper.getState(entityIn) == 3) {
    		this.rightArm.zRot = (float) Math.toRadians(20);
    		this.leftArm.zRot = (float) Math.toRadians(-20);
    		this.leftArm.xRot = (float) Math.toRadians(0);
    		this.rightArm.xRot = (float) Math.toRadians(0);


    		this.rightLeg.xRot = 0;
    		this.leftLeg.xRot = 0;
    		
    		this.rightLeg.zRot = (float) Math.toRadians(10);
    		this.leftLeg.zRot = (float) Math.toRadians(-10);	
    	}
    }
}
