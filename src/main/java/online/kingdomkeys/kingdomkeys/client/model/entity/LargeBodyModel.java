package online.kingdomkeys.kingdomkeys.client.model.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.entity.EntityHelper;

/**
 * HeartlessLargeBody - WYND
 * Created using Tabula 7.0.0
 * Ported to 1.18 using Tabula, Blockbench and manual code editing - Wehavecookies56
 */
public class LargeBodyModel<T extends Entity> extends EntityModel<T> {
    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation(KingdomKeys.MODID, "largebodymodel"), "main");

    private final ModelPart neck4;
    private final ModelPart neck3;
    private final ModelPart neck2;
    private final ModelPart neck1;
    private final ModelPart body;
    private final ModelPart body1;
    private final ModelPart body2;
    private final ModelPart body3;
    private final ModelPart body4;
    private final ModelPart body5;
    private final ModelPart body6;
    private final ModelPart body7;
    private final ModelPart rightArm1;
    private final ModelPart rightArm2;
    private final ModelPart rightArm3;
    private final ModelPart leftArm1;
    private final ModelPart leftArm2;
    private final ModelPart leftArm3;
    private final ModelPart head;
    private final ModelPart hat;
    private final ModelPart hat1;
    private final ModelPart hat2;
    private final ModelPart hat3;
    private final ModelPart hat4;
    private final ModelPart hat5;
    private final ModelPart hat6;
    private final ModelPart leftLeg1;
    private final ModelPart leftLeg2;
    private final ModelPart leftLeg3;
    private final ModelPart rightLeg1;
    private final ModelPart rightLeg2;
    private final ModelPart rightLeg3;

    private int cycleIndex;
    private double totalDistance;
    private double[] chargeFlailArmsAnimation = new double[]
            {1.65, 1.60, 1.57, 1.50, 1.57, 1.60, 1.65};
    private double[] legsMovementAnimation = new double[]
            {-22, -18, -14, -10, -6, -2, 2, 6, 10, 14, 18, 22, 18, 14, 10, 6, 2, -2, -6, -10, -14, -18, -22};
    private double[] afterAttackAnimation = new double[]
            {-180, -182, -184, -186, -188, -190, -188, -186, -184, -182, -180};
    private double[] mowdownAttackAnimation = new double[]
            {50, 40, 30, 20, 10, 0, -10, -20, -30, -40, -50, -50, -40, -30, -20, -10, 0, 10, 20, 30, 40, 50};

    public LargeBodyModel(ModelPart root) {
        this.neck4 = root.getChild("neck4");
        this.neck3 = root.getChild("neck3");
        this.neck2 = root.getChild("neck2");
        this.neck1 = root.getChild("neck1");
        this.body = root.getChild("body");
        this.body1 = body.getChild("body1");
        this.body2 = body.getChild("body2");
        this.body3 = body.getChild("body3");
        this.body4 = body.getChild("body4");
        this.body5 = body.getChild("body5");
        this.body6 = body.getChild("body6");
        this.body7 = body.getChild("body7");
        this.leftArm1 = body.getChild("leftArm1");
        this.leftArm2 = leftArm1.getChild("leftArm2");
        this.leftArm3 = leftArm2.getChild("leftArm3");
        this.rightArm1 = body.getChild("rightArm1");
        this.rightArm2 = rightArm1.getChild("rightArm2");
        this.rightArm3 = rightArm2.getChild("rightArm3");
        this.head = root.getChild("head");
        this.hat = head.getChild("hat");
        this.hat1 = hat.getChild("hat1");
        this.hat2 = hat1.getChild("hat2");
        this.hat3 = hat2.getChild("hat3");
        this.hat4 = hat3.getChild("hat4");
        this.hat5 = hat4.getChild("hat5");
        this.hat6 = hat5.getChild("hat6");
        this.leftLeg1 = root.getChild("leftLeg1");
        this.leftLeg2 = leftLeg1.getChild("leftLeg2");
        this.leftLeg3 = leftLeg2.getChild("leftLeg3");
        this.rightLeg1 = root.getChild("rightLeg1");
        this.rightLeg2 = rightLeg1.getChild("rightLeg2");
        this.rightLeg3 = rightLeg2.getChild("rightLeg3");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition neck4 = partdefinition.addOrReplaceChild("neck4", CubeListBuilder.create().texOffs(26, 50).addBox(-3.0F, -2.9F, 3.5F, 8.0F, 2.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition neck3 = partdefinition.addOrReplaceChild("neck3", CubeListBuilder.create().texOffs(26, 50).addBox(-3.0F, -2.9F, -3.5F, 8.0F, 2.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition body = partdefinition.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 0).addBox(-8.0F, -10.0F, -8.5F, 18.0F, 18.0F, 13.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 9.8F, 0.4F));

        PartDefinition rightArm1 = body.addOrReplaceChild("rightArm1", CubeListBuilder.create().texOffs(65, 91).addBox(-0.9F, 0.0F, -1.5F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(10.0F, -6.0F, -0.5F));

        PartDefinition rightArm2 = rightArm1.addOrReplaceChild("rightArm2", CubeListBuilder.create().texOffs(65, 103).addBox(-1.5F, 6.0F, -2.5F, 5.0F, 9.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition rightArm3 = rightArm2.addOrReplaceChild("rightArm3", CubeListBuilder.create().texOffs(92, 92).addBox(-1.0F, 15.0F, -2.0F, 4.0F, 2.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition body5 = body.addOrReplaceChild("body5", CubeListBuilder.create().texOffs(67, 50).addBox(-7.5F, -9.0F, -9.5F, 17.0F, 17.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition body7 = body.addOrReplaceChild("body7", CubeListBuilder.create().texOffs(67, 70).addBox(-7.0F, -8.0F, -10.5F, 16.0F, 16.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition body1 = body.addOrReplaceChild("body1", CubeListBuilder.create().texOffs(65, 0).addBox(9.7F, -9.5F, -8.0F, 1.0F, 17.0F, 12.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition body2 = body.addOrReplaceChild("body2", CubeListBuilder.create().texOffs(65, 0).addBox(-8.7F, -9.5F, -8.0F, 1.0F, 17.0F, 12.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition body3 = body.addOrReplaceChild("body3", CubeListBuilder.create().texOffs(65, 33).addBox(-7.5F, -10.7F, -8.0F, 17.0F, 1.0F, 12.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition body6 = body.addOrReplaceChild("body6", CubeListBuilder.create().texOffs(67, 50).addBox(-7.5F, -9.5F, 4.2F, 17.0F, 17.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition leftArm1 = body.addOrReplaceChild("leftArm1", CubeListBuilder.create().texOffs(65, 91).addBox(-1.0F, 0.0F, -1.5F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-8.0F, -6.0F, -0.2F, 0.0F, -3.1416F, 0.0F));

        PartDefinition leftArm2 = leftArm1.addOrReplaceChild("leftArm2", CubeListBuilder.create().texOffs(65, 103).addBox(-1.5F, 6.0F, -2.5F, 5.0F, 9.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition leftArm3 = leftArm2.addOrReplaceChild("leftArm3", CubeListBuilder.create().texOffs(92, 92).addBox(-1.0F, 15.0F, -2.0F, 4.0F, 2.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition body4 = body.addOrReplaceChild("body4", CubeListBuilder.create().texOffs(65, 33).addBox(-7.5F, 7.7F, -8.0F, 17.0F, 1.0F, 12.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition neck2 = partdefinition.addOrReplaceChild("neck2", CubeListBuilder.create().texOffs(26, 50).addBox(5.0F, -2.9F, -3.5F, 0.0F, 2.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition neck1 = partdefinition.addOrReplaceChild("neck1", CubeListBuilder.create().texOffs(26, 50).addBox(-3.0F, -2.9F, -3.5F, 0.0F, 2.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition head = partdefinition.addOrReplaceChild("head", CubeListBuilder.create().texOffs(21, 36).addBox(-2.5F, -5.0F, -2.5F, 5.0F, 5.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offset(1.0F, -0.7F, 0.0F));

        PartDefinition hat = head.addOrReplaceChild("hat", CubeListBuilder.create().texOffs(38, 32).addBox(-3.5F, -6.0F, -3.5F, 7.0F, 1.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition hat1 = hat.addOrReplaceChild("hat1", CubeListBuilder.create().texOffs(45, 48).addBox(-2.5F, -7.0F, -2.5F, 5.0F, 1.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition hat2 = hat1.addOrReplaceChild("hat2", CubeListBuilder.create().texOffs(21, 66).addBox(-1.5F, -8.0F, -1.5F, 3.0F, 1.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition hat3 = hat2.addOrReplaceChild("hat3", CubeListBuilder.create().texOffs(21, 73).addBox(-0.5F, -10.0F, -0.5F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition hat4 = hat3.addOrReplaceChild("hat4", CubeListBuilder.create().texOffs(21, 73).addBox(-7.4F, -8.7F, -0.5F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.7854F));

        PartDefinition hat5 = hat4.addOrReplaceChild("hat5", CubeListBuilder.create().texOffs(21, 73).addBox(8.7F, -8.4F, -0.5F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, -1.5708F));

        PartDefinition hat6 = hat5.addOrReplaceChild("hat6", CubeListBuilder.create().texOffs(21, 73).addBox(8.4F, 6.7F, -0.5F, 1.0F, 3.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, -1.5708F));

        PartDefinition leftLeg1 = partdefinition.addOrReplaceChild("leftLeg1", CubeListBuilder.create().texOffs(0, 42).addBox(-1.5F, 0.0F, -1.5F, 3.0F, 4.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-3.5F, 18.0F, 0.0F, 0.0F, 0.2269F, 0.0F));

        PartDefinition leftLeg2 = leftLeg1.addOrReplaceChild("leftLeg2", CubeListBuilder.create().texOffs(0, 52).addBox(-1.5F, 4.0F, -5.5F, 3.0F, 2.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition leftLeg3 = leftLeg2.addOrReplaceChild("leftLeg3", CubeListBuilder.create().texOffs(0, 62).addBox(-1.5F, 3.0F, -5.5F, 3.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition rightLeg1 = partdefinition.addOrReplaceChild("rightLeg1", CubeListBuilder.create().texOffs(0, 42).addBox(-1.5F, 0.0F, -1.5F, 3.0F, 4.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(5.0F, 18.0F, 0.0F, 0.0F, -0.2269F, 0.0F));

        PartDefinition rightLeg2 = rightLeg1.addOrReplaceChild("rightLeg2", CubeListBuilder.create().texOffs(0, 52).addBox(-1.5F, 4.0F, -5.5F, 3.0F, 2.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition rightLeg3 = rightLeg2.addOrReplaceChild("rightLeg3", CubeListBuilder.create().texOffs(0, 62).addBox(-1.5F, 3.0F, -5.5F, 3.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        return LayerDefinition.create(meshdefinition, 128, 128);
    }

    @Override
    public void setupAnim(T ent, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        totalDistance += ent.distanceToSqr(ent.xOld, ent.yOld, ent.zOld);

        if(ent.distanceToSqr(ent.xOld, ent.yOld, ent.zOld) > 0) {
            cycleIndex = (int) ((totalDistance * 4) % this.legsMovementAnimation.length);
            this.leftLeg1.xRot = degToRad(legsMovementAnimation[cycleIndex]);
            this.rightLeg1.xRot = -degToRad(legsMovementAnimation[cycleIndex]);
        }
        else {
            this.leftLeg1.xRot = this.rightLeg1.xRot = 0;
        }

        if(EntityHelper.getState(ent) == 0) {
            this.body.yRot = 0;
            this.leftArm1.xRot = this.rightArm1.xRot = 0;
            this.leftLeg1.yRot = this.degToRad(-13);
            this.rightLeg1.yRot = this.degToRad(13);

            this.rightArm1.xRot = this.rightArm1.yRot = this.rightArm1.zRot = 0;
        }
        else if(EntityHelper.getState(ent) == 1) {
            cycleIndex = (int) (ent.tickCount % chargeFlailArmsAnimation.length);
            this.leftArm1.xRot  = (float) -chargeFlailArmsAnimation[cycleIndex];
            this.rightArm1.xRot  = (float) chargeFlailArmsAnimation[cycleIndex];
        }
        else if(EntityHelper.getState(ent) == 2) {
            this.leftArm1.zRot  = degToRad(-60);
            this.rightArm1.zRot  = degToRad(60);
            cycleIndex = (int) ((ent.tickCount * 1.4) % mowdownAttackAnimation.length);
            this.body.yRot = degToRad(mowdownAttackAnimation[cycleIndex]);
        }
        else if(EntityHelper.getState(ent) == 10) {
            this.leftArm1.zRot = this.rightArm1.zRot = 0;
            this.leftArm1.xRot = this.rightArm1.xRot = 0;

            cycleIndex = (int) (ent.tickCount % afterAttackAnimation.length);
            this.rightArm1.xRot = degToRad((float) afterAttackAnimation[cycleIndex]);
            this.rightArm1.yRot = degToRad(-26);
            this.rightArm1.zRot = degToRad(18);
        }
    }

    protected float degToRad(double degrees) {
        return (float) (degrees * (double)Math.PI / 180) ;
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        neck4.render(poseStack, buffer, packedLight, packedOverlay);
        neck3.render(poseStack, buffer, packedLight, packedOverlay);
        body.render(poseStack, buffer, packedLight, packedOverlay);
        neck2.render(poseStack, buffer, packedLight, packedOverlay);
        neck1.render(poseStack, buffer, packedLight, packedOverlay);
        head.render(poseStack, buffer, packedLight, packedOverlay);
        leftLeg1.render(poseStack, buffer, packedLight, packedOverlay);
        rightLeg1.render(poseStack, buffer, packedLight, packedOverlay);
    }
}