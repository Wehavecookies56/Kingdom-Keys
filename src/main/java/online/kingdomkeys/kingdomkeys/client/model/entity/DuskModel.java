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
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import online.kingdomkeys.kingdomkeys.KingdomKeys;

/**
 * Dusk - WYND
 * Ported to 1.18 using Tabula, Blockbench and manual code editing - Wehavecookies56
 */
public class DuskModel<T extends Entity> extends EntityModel<T> {
    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation(KingdomKeys.MODID, "dusk"), "main");
    private final ModelPart RightLegUpper;
    private final ModelPart RightLegThigh;
    private final ModelPart RightLegMiddle;
    private final ModelPart RightLegLower;
    private final ModelPart RightLegFeet1;
    private final ModelPart RightLegFeet2;
    private final ModelPart HeadUpper;
    private final ModelPart HeadPoint1;
    private final ModelPart HeadPoint2;
    private final ModelPart HeadLower;
    private final ModelPart HeadThroat;
    private final ModelPart HeadThroatDetail1;
    private final ModelPart HeadThroatDetail2;
    private final ModelPart HeadThroatDetail3;
    private final ModelPart LeftArmShoulder;
    private final ModelPart LeftArm;
    private final ModelPart LeftHandUpper;
    private final ModelPart LeftHandLower;
    private final ModelPart LeftLegUpper;
    private final ModelPart LeftLegThigh;
    private final ModelPart LeftLegMiddle;
    private final ModelPart LeftLegLower;
    private final ModelPart LeftLegFeet1;
    private final ModelPart LeftLegFeet2;
    private final ModelPart RightArmShoulder;
    private final ModelPart RightArm;
    private final ModelPart RightHandUpper;
    private final ModelPart RightHandLower;
    private final ModelPart Body1;
    private final ModelPart LowerBody1;
    private final ModelPart UpperBody1;

    private int cycleIndex;
    private double totalDistance;
    private boolean legRotation = false;

    public DuskModel(ModelPart root) {
        this.RightLegUpper = root.getChild("RightLegUpper");
        this.RightLegThigh = RightLegUpper.getChild("RightLegThigh");
        this.RightLegMiddle = RightLegUpper.getChild("RightLegMiddle");
        this.RightLegLower = RightLegMiddle.getChild("RightLegLower");
        this.RightLegFeet1 = RightLegLower.getChild("RightLegFeet1");
        this.RightLegFeet2 = RightLegFeet1.getChild("RightLegFeet2");
        this.HeadUpper = root.getChild("HeadUpper");
        this.HeadPoint1 = HeadUpper.getChild("HeadPoint1");
        this.HeadPoint2 = HeadPoint1.getChild("HeadPoint2");
        this.HeadLower = HeadUpper.getChild("HeadLower");
        this.HeadThroat = HeadUpper.getChild("HeadThroat");
        this.HeadThroatDetail1 = HeadThroat.getChild("HeadThroatDetail1");
        this.HeadThroatDetail2 = HeadThroat.getChild("HeadThroatDetail2");
        this.HeadThroatDetail3 = HeadThroat.getChild("HeadThroatDetail3");
        this.LeftArmShoulder = root.getChild("LeftArmShoulder");
        this.LeftArm = LeftArmShoulder.getChild("LeftArm");
        this.LeftHandUpper = LeftArm.getChild("LeftHandUpper");
        this.LeftHandLower = LeftHandUpper.getChild("LeftHandLower");
        this.LeftLegUpper = root.getChild("LeftLegUpper");
        this.LeftLegThigh = LeftLegUpper.getChild("LeftLegThigh");
        this.LeftLegMiddle = LeftLegUpper.getChild("LeftLegMiddle");
        this.LeftLegLower = LeftLegMiddle.getChild("LeftLegLower");
        this.LeftLegFeet1 = LeftLegLower.getChild("LeftLegFeet1");
        this.LeftLegFeet2 = LeftLegFeet1.getChild("LeftLegFeet2");
        this.RightArmShoulder = root.getChild("RightArmShoulder");
        this.RightArm = RightArmShoulder.getChild("RightArm");
        this.RightHandUpper = RightArm.getChild("RightHandUpper");
        this.RightHandLower = RightHandUpper.getChild("RightHandLower");
        this.Body1 = root.getChild("Body1");
        this.LowerBody1 = Body1.getChild("LowerBody1");
        this.UpperBody1 = Body1.getChild("UpperBody1");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition RightLegUpper = partdefinition.addOrReplaceChild("RightLegUpper", CubeListBuilder.create().texOffs(0, 12).addBox(0.5F, 0.0F, -1.5F, 2.0F, 5.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 12.5F, 0.0F, -0.1222F, 0.0F, 0.0F));

        PartDefinition RightLegThigh = RightLegUpper.addOrReplaceChild("RightLegThigh", CubeListBuilder.create().texOffs(0, 38).addBox(0.2F, -0.9F, -1.5F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, -0.4554F));

        PartDefinition RightLegMiddle = RightLegUpper.addOrReplaceChild("RightLegMiddle", CubeListBuilder.create().texOffs(0, 19).addBox(-0.5F, 0.0F, -1.5F, 1.0F, 1.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.6F, 4.6F, -0.3F, -0.1222F, 0.0F, 0.0F));

        PartDefinition RightLegLower = RightLegMiddle.addOrReplaceChild("RightLegLower", CubeListBuilder.create().texOffs(0, 23).addBox(-0.5F, -0.5F, -5.0F, 1.0F, 1.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.7F, 0.7F, 1.7279F, 0.0F, 0.0F));

        PartDefinition RightLegFeet1 = RightLegLower.addOrReplaceChild("RightLegFeet1", CubeListBuilder.create().texOffs(0, 29).addBox(-0.5F, 0.0F, -2.0F, 1.0F, 1.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.5F, -4.5F, -1.2741F, 0.0F, 0.0F));

        PartDefinition RightLegFeet2 = RightLegFeet1.addOrReplaceChild("RightLegFeet2", CubeListBuilder.create().texOffs(0, 34).addBox(-0.5F, 0.5F, -3.5F, 1.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.2618F, 0.0F, 0.0F));

        PartDefinition HeadUpper = partdefinition.addOrReplaceChild("HeadUpper", CubeListBuilder.create().texOffs(22, 0).addBox(-2.0F, -5.5F, -2.0F, 4.0F, 3.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 5.0F, -0.5F));

        PartDefinition HeadPoint1 = HeadUpper.addOrReplaceChild("HeadPoint1", CubeListBuilder.create().texOffs(31, 15).addBox(-1.5F, -5.5F, -3.0F, 3.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition HeadPoint2 = HeadPoint1.addOrReplaceChild("HeadPoint2", CubeListBuilder.create().texOffs(32, 20).addBox(-1.0F, -5.5F, -3.9F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition HeadLower = HeadUpper.addOrReplaceChild("HeadLower", CubeListBuilder.create().texOffs(23, 7).addBox(-2.0F, -1.0F, -2.0F, 4.0F, 1.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition HeadThroat = HeadUpper.addOrReplaceChild("HeadThroat", CubeListBuilder.create().texOffs(39, 1).addBox(-2.0F, -2.5F, 0.0F, 4.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition HeadThroatDetail1 = HeadThroat.addOrReplaceChild("HeadThroatDetail1", CubeListBuilder.create().texOffs(26, 23).addBox(-2.0F, -2.8F, -2.0F, 4.0F, 2.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition HeadThroatDetail3 = HeadThroat.addOrReplaceChild("HeadThroatDetail3", CubeListBuilder.create().texOffs(26, 27).addBox(-2.0F, -2.8F, -2.0F, 0.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition HeadThroatDetail2 = HeadThroat.addOrReplaceChild("HeadThroatDetail2", CubeListBuilder.create().texOffs(26, 24).addBox(2.0F, -2.8F, -2.0F, 0.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition LeftArmShoulder = partdefinition.addOrReplaceChild("LeftArmShoulder", CubeListBuilder.create().texOffs(15, 13).addBox(-2.0F, -1.0F, -1.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(-2.0F, 6.0F, -0.5F));

        PartDefinition LeftArm = LeftArmShoulder.addOrReplaceChild("LeftArm", CubeListBuilder.create().texOffs(15, 18).addBox(-1.0F, 1.0F, -0.5F, 1.0F, 5.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(-1.0F, 0.0F, 0.0F));

        PartDefinition LeftHandUpper = LeftArm.addOrReplaceChild("LeftHandUpper", CubeListBuilder.create().texOffs(15, 26).addBox(-1.0F, 0.0F, -0.5F, 2.0F, 4.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(-0.5F, 6.0F, 0.0F));

        PartDefinition LeftHandLower = LeftHandUpper.addOrReplaceChild("LeftHandLower", CubeListBuilder.create().texOffs(15, 32).addBox(-0.5F, 4.0F, -0.5F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition LeftLegUpper = partdefinition.addOrReplaceChild("LeftLegUpper", CubeListBuilder.create().texOffs(0, 12).addBox(0.5F, 0.0F, -1.5F, 2.0F, 5.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-3.0F, 12.5F, 0.0F, -0.1222F, 0.0F, 0.0F));

        PartDefinition LeftLegThigh = LeftLegUpper.addOrReplaceChild("LeftLegThigh", CubeListBuilder.create().texOffs(0, 38).addBox(-0.2F, -1.4F, -1.5F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.8F, 0.6F, 0.0F, 0.0F, 0.0F, -1.0472F));

        PartDefinition LeftLegMiddle = LeftLegUpper.addOrReplaceChild("LeftLegMiddle", CubeListBuilder.create().texOffs(0, 19).addBox(-0.5F, 0.0F, -1.5F, 1.0F, 1.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.6F, 4.6F, -0.3F, -0.1222F, 0.0F, 0.0F));

        PartDefinition LeftLegLower = LeftLegMiddle.addOrReplaceChild("LeftLegLower", CubeListBuilder.create().texOffs(0, 23).addBox(-0.5F, -0.5F, -5.0F, 1.0F, 1.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.7F, 0.7F, 1.7301F, 0.0F, 0.0F));

        PartDefinition LeftLegFeet1 = LeftLegLower.addOrReplaceChild("LeftLegFeet1", CubeListBuilder.create().texOffs(0, 29).addBox(-0.5F, 0.0F, -2.0F, 1.0F, 1.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.5F, -4.5F, -1.2741F, 0.0F, 0.0F));

        PartDefinition LeftLegFeet2 = LeftLegFeet1.addOrReplaceChild("LeftLegFeet2", CubeListBuilder.create().texOffs(0, 34).addBox(-0.5F, 0.5F, -3.5F, 1.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.2618F, 0.0F, 0.0F));

        PartDefinition RightArmShoulder = partdefinition.addOrReplaceChild("RightArmShoulder", CubeListBuilder.create().texOffs(15, 13).addBox(0.0F, -1.0F, -1.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(2.0F, 6.0F, -0.5F));

        PartDefinition RightArm = RightArmShoulder.addOrReplaceChild("RightArm", CubeListBuilder.create().texOffs(15, 18).addBox(1.0F, 1.0F, -0.5F, 1.0F, 5.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition RightHandUpper = RightArm.addOrReplaceChild("RightHandUpper", CubeListBuilder.create().texOffs(15, 26).addBox(-1.0F, 0.0F, -0.5F, 2.0F, 4.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(1.5F, 6.0F, 0.0F));

        PartDefinition RightHandLower = RightHandUpper.addOrReplaceChild("RightHandLower", CubeListBuilder.create().texOffs(15, 32).addBox(-0.5F, 4.0F, -0.5F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition Body1 = partdefinition.addOrReplaceChild("Body1", CubeListBuilder.create().texOffs(0, 0).addBox(-1.0F, 1.0F, -1.5F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 7.0F, 0.0F));

        PartDefinition LowerBody1 = Body1.addOrReplaceChild("LowerBody1", CubeListBuilder.create().texOffs(0, 6).addBox(-1.5F, 3.0F, -1.5F, 3.0F, 3.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition UpperBody1 = Body1.addOrReplaceChild("UpperBody1", CubeListBuilder.create().texOffs(9, 0).addBox(-2.0F, -2.0F, -1.5F, 4.0F, 3.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        return LayerDefinition.create(meshdefinition, 64, 64);
    }

    @Override
    public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        if(!Minecraft.getInstance().isPaused()) {
            //if(EntityHelper.getState(ent) == 0)
            //{
			if (entity.distanceToSqr(entity.xOld, entity.yOld, entity.zOld) > 0) {
				this.Body1.xRot = Mth.cos(limbSwing * 0.8662F) * 0.2F * limbSwingAmount;

				this.RightArmShoulder.xRot = Mth.cos(limbSwing * 0.8662F) * 0.5F * limbSwingAmount;
				this.RightHandUpper.xRot = Mth.cos(limbSwing * 0.8662F) * 0.8F * limbSwingAmount;

				this.RightLegUpper.xRot = Mth.cos(limbSwing * 0.5F) * 2.0F * limbSwingAmount;// degToRad(walkingUpperLegAnimation[cycleIndex]);

				this.LeftArmShoulder.xRot = Mth.cos(limbSwing * 0.8662F + (float) Math.PI) * 0.5F * limbSwingAmount;
				this.LeftHandUpper.xRot = Mth.cos(limbSwing * 0.8662F + (float) Math.PI) * 0.8F * limbSwingAmount;

				this.LeftLegUpper.xRot = Mth.cos(limbSwing * 0.5F + (float) Math.PI) * 2.0F * limbSwingAmount;
			} else {
				this.LeftLegUpper.xRot = degToRad(-7);
				this.RightLegMiddle.xRot = degToRad(-7);
				this.RightLegLower.xRot = degToRad(99);
				this.RightLegUpper.xRot = degToRad(-7);
			}
        }
    }

    protected float degToRad(double degrees) {
        return (float) (degrees * (double)Math.PI / 180) ;
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        RightLegUpper.render(poseStack, buffer, packedLight, packedOverlay);
        HeadUpper.render(poseStack, buffer, packedLight, packedOverlay);
        LeftArmShoulder.render(poseStack, buffer, packedLight, packedOverlay);
        LeftLegUpper.render(poseStack, buffer, packedLight, packedOverlay);
        RightArmShoulder.render(poseStack, buffer, packedLight, packedOverlay);
        Body1.render(poseStack, buffer, packedLight, packedOverlay);
    }
}