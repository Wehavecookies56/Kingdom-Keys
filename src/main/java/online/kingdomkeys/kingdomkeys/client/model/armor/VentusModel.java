package online.kingdomkeys.kingdomkeys.client.model.armor;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.decoration.ArmorStand;
import online.kingdomkeys.kingdomkeys.KingdomKeys;

public class VentusModel<T extends LivingEntity> extends HumanoidModel<T> {

    public static final ModelLayerLocation LAYER_LOCATION_TOP = new ModelLayerLocation(new ResourceLocation(KingdomKeys.MODID, "ventus_top"), "main");
    public static final ModelLayerLocation LAYER_LOCATION_BOTTOM = new ModelLayerLocation(new ResourceLocation(KingdomKeys.MODID, "ventus_bottom"), "main");

    public VentusModel(ModelPart root) {
        super(root);
    }

    public static LayerDefinition createBodyLayer(CubeDeformation size) {
        MeshDefinition meshdefinition = HumanoidModel.createMesh(size, 0);
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition rightArm = partdefinition.getChild("right_arm"); //partdefinition.addOrReplaceChild("rightArm", CubeListBuilder.create().texOffs(40, 16).addBox(-1.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(5.0F, 2.0F, 0.0F));
        PartDefinition leftArm = partdefinition.getChild("left_arm"); //partdefinition.addOrReplaceChild("leftArm", CubeListBuilder.create().texOffs(32, 48).addBox(-3.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(-5.0F, 2.0F, 0.0F));

        PartDefinition rightElbowBase = leftArm.addOrReplaceChild("rightElbowBase", CubeListBuilder.create().texOffs(36, 32).addBox(-3.5F, -2.0F, -3.5F, 7.0F, 5.0F, 7.0F, new CubeDeformation(-0.8F, 0F, -0.8F)), PartPose.offsetAndRotation(1.0F, 5.0F, 0.0F, 0.0F, -3.1416F, 0.0F));

        PartDefinition rightElbowSpike = rightElbowBase.addOrReplaceChild("rightElbowSpike", CubeListBuilder.create().texOffs(26, 38).addBox(-0.5F, -3.0F, -1.5F, 1.0F, 3.0F, 3.0F, new CubeDeformation(-0.2F, 0, -0.3F)), PartPose.offsetAndRotation(-2.4F, 0.0F, 0.0F, 0.0F, 0.0F, -0.3491F));

        PartDefinition rightElbowSpike2 = rightElbowSpike.addOrReplaceChild("rightElbowSpike2", CubeListBuilder.create().texOffs(29, 44).addBox(-0.5F, -3.0F, -1.0F, 1.0F, 3.0F, 2.0F, new CubeDeformation(-0.3F, 0, -0.3F)), PartPose.offsetAndRotation(-0.1F, -2.9F, 0.0F, 0.0F, 0.0F, 0.126F));

        PartDefinition rightLeg = partdefinition.getChild("right_leg"); //partdefinition.addOrReplaceChild("rightLeg", CubeListBuilder.create().texOffs(0, 16).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(1.9F, 12.0F, 0.0F));
        PartDefinition leftLeg = partdefinition.getChild("left_leg"); //partdefinition.addOrReplaceChild("leftLeg", CubeListBuilder.create().texOffs(16, 48).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(-1.9F, 12.0F, 0.0F));

        PartDefinition rightFoot = rightLeg.addOrReplaceChild("rightFoot", CubeListBuilder.create().texOffs(0, 32).addBox(-1.9F, 0.0F, -3.0F, 4.0F, 3.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 9.25F, -1.0F));

        PartDefinition rightFootTip = rightFoot.addOrReplaceChild("rightFootTip", CubeListBuilder.create().texOffs(16, 41).addBox(-1.9F, 0.0F, -1.0F, 4.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 1.0F, -3.0F));

        PartDefinition rightKneeBase = leftLeg.addOrReplaceChild("rightKneeBase", CubeListBuilder.create().texOffs(16, 32).addBox(-2.4F, 0.0F, -0.5F, 4.8F, 1.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 4.0F, -2.0F));

        PartDefinition rightKnee1 = rightKneeBase.addOrReplaceChild("rightKnee1", CubeListBuilder.create().texOffs(31, 33).addBox(-1.5F, 0.5F, -0.5F, 2.0F, 1.0F, 0.2F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, -0.7854F));

        PartDefinition rightKnee2 = rightKneeBase.addOrReplaceChild("rightKnee2", CubeListBuilder.create().texOffs(31, 34).addBox(-0.2F, 0.5F, -0.5F, 1.7F, 1.0F, 0.2F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.7854F));

        PartDefinition rightKneeSpike = rightKneeBase.addOrReplaceChild("rightKneeSpike", CubeListBuilder.create().texOffs(17, 32).addBox(-0.5F, -2.8F, -0.4F, 0.5F, 2.5F, 0.8F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(2.4F, 0.0F, 2.0F, 0.0F, 0.0F, 0.3128F));

        PartDefinition head = partdefinition.getChild("head"); //partdefinition.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 0).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition rightHorn1 = head.addOrReplaceChild("rightHorn1", CubeListBuilder.create().texOffs(0, 53).addBox(-0.4F, 1.0F, 0.0F, 0.3F, 8.0F, 3.0F, new CubeDeformation(0.1F, 0F, 0F)), PartPose.offsetAndRotation(5.0F, -12.0F, 0.0F, -0.2618F, 0.0F, 0.0F));

        PartDefinition rightHorn2 = rightHorn1.addOrReplaceChild("rightHorn2", CubeListBuilder.create().texOffs(0, 57).addBox(-0.3F, 0.0F, -3.0F, 0.3F, 1.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(-0.2F, 9.0F, 0.0F));

        PartDefinition rightHorn3 = rightHorn1.addOrReplaceChild("rightHorn3", CubeListBuilder.create().texOffs(0, 60).addBox(-0.3F, 0.0F, -2.1F, 0.3F, 0.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.2F, 3.1F, 0.0F, 0.2618F, 0.0F, 0.0F));

        PartDefinition rightHorn4 = rightHorn1.addOrReplaceChild("rightHorn4", CubeListBuilder.create().texOffs(0, 62).addBox(-0.4F, 0.0F, 0.0F, 0.3F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 1.6F, 0.0F, -0.5117F, 0.0F, 0.0F));

        PartDefinition rightHorn5 = rightHorn1.addOrReplaceChild("rightHorn5", CubeListBuilder.create().texOffs(0, 62).addBox(-0.4F, 0.0F, 0.0F, 0.3F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.7F, 0.5F, -1.0982F, 0.0F, 0.0F));

        PartDefinition rightHorn6 = rightHorn1.addOrReplaceChild("rightHorn6", CubeListBuilder.create().texOffs(0, 59).addBox(-0.3F, -0.5F, -2.0F, 0.3F, 1.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.1F, 0.7F, 1.5F, -1.6064F, 0.0F, 0.0F));

        PartDefinition rightHorn7 = rightHorn1.addOrReplaceChild("rightHorn7", CubeListBuilder.create().texOffs(0, 60).addBox(-0.4F, -0.9F, -3.3F, 0.3F, 1.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.3F, 1.8F, -1.1373F, 0.0F, 0.0F));

        PartDefinition leftHorn1 = head.addOrReplaceChild("leftHorn1", CubeListBuilder.create().texOffs(0, 53).addBox(-0.4F, 1.0F, 0.0F, 0.3F, 8.0F, 3.0F, new CubeDeformation(0.1F, 0F, 0F)), PartPose.offsetAndRotation(-4.5F, -12.0F, 0.0F, -0.2618F, 0.0F, 0.0F));

        PartDefinition leftHorn2 = leftHorn1.addOrReplaceChild("leftHorn2", CubeListBuilder.create().texOffs(0, 57).addBox(-0.3F, 0.0F, -3.0F, 0.3F, 1.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 9.0F, 0.0F));

        PartDefinition leftHorn3 = leftHorn1.addOrReplaceChild("leftHorn3", CubeListBuilder.create().texOffs(0, 60).addBox(-0.3F, 0.0F, -2.1F, 0.3F, 0.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 3.1F, 0.0F, 0.2618F, 0.0F, 0.0F));

        PartDefinition leftHorn4 = leftHorn1.addOrReplaceChild("leftHorn4", CubeListBuilder.create().texOffs(0, 62).addBox(-0.4F, 0.0F, 0.0F, 0.3F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 1.6F, 0.0F, -0.5117F, 0.0F, 0.0F));

        PartDefinition leftHorn5 = leftHorn1.addOrReplaceChild("leftHorn5", CubeListBuilder.create().texOffs(0, 62).addBox(-0.4F, 0.0F, 0.0F, 0.3F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.7F, 0.5F, -1.0982F, 0.0F, 0.0F));

        PartDefinition leftHorn6 = leftHorn1.addOrReplaceChild("leftHorn6", CubeListBuilder.create().texOffs(0, 59).addBox(-0.3F, -0.5F, -2.0F, 0.3F, 1.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.1F, 0.7F, 1.5F, -1.6064F, 0.0F, 0.0F));

        PartDefinition leftHorn7 = leftHorn1.addOrReplaceChild("leftHorn7", CubeListBuilder.create().texOffs(0, 60).addBox(-0.4F, -0.9F, -3.3F, 0.3F, 1.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.3F, 1.8F, -1.1373F, 0.0F, 0.0F));

        PartDefinition body = partdefinition.getChild("body"); //partdefinition.addOrReplaceChild("body", CubeListBuilder.create().texOffs(16, 16).addBox(-4.0F, 0.0F, -2.0F, 8.0F, 12.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));


        PartDefinition leftElbowBase = rightArm.addOrReplaceChild("leftElbowBase", CubeListBuilder.create().texOffs(36, 32).addBox(-3.5F, -2.0F, -3.5F, 7.0F, 5.0F, 7.0F, new CubeDeformation(-0.8F, 0F, -0.8F)), PartPose.offset(-1.0F, 5.0F, 0.0F));

        PartDefinition leftElbowSpike = leftElbowBase.addOrReplaceChild("leftElbowSpike", CubeListBuilder.create().texOffs(26, 38).addBox(-0.5F, -3.0F, -1.5F, 1.0F, 3.0F, 3.0F, new CubeDeformation(-0.2F, 0F, -0.3F)), PartPose.offsetAndRotation(-2.4F, 0.0F, 0.0F, 0.0F, 0.0F, -0.3491F));

        PartDefinition leftElbowSpike2 = leftElbowSpike.addOrReplaceChild("leftElbowSpike2", CubeListBuilder.create().texOffs(29, 44).addBox(-0.5F, -3.0F, -1.0F, 1.0F, 3.0F, 2.0F, new CubeDeformation(-0.3F, 0F, -0.3F)), PartPose.offsetAndRotation(-0.1F, -2.9F, 0.0F, 0.0F, 0.0F, 0.126F));


        PartDefinition leftFoot = leftLeg.addOrReplaceChild("leftFoot", CubeListBuilder.create().texOffs(0, 32).addBox(-2.1F, 0.0F, -3.0F, 4.0F, 3.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 9.25F, -1.0F));

        PartDefinition leftFootTip = leftFoot.addOrReplaceChild("leftFootTip", CubeListBuilder.create().texOffs(16, 41).addBox(-2.1F, 0.0F, -1.0F, 4.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 1.0F, -3.0F));

        PartDefinition leftKneeBase = rightLeg.addOrReplaceChild("leftKneeBase", CubeListBuilder.create().texOffs(16, 32).addBox(-2.5F, 0.0F, -0.5F, 4.8F, 1.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 4.0F, -2.0F));

        PartDefinition leftKnee1 = leftKneeBase.addOrReplaceChild("leftKnee1", CubeListBuilder.create().texOffs(31, 32).addBox(-1.5F, 0.5F, -0.5F, 2.0F, 1.0F, 0.2F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, -0.7854F));

        PartDefinition leftKnee2 = leftKneeBase.addOrReplaceChild("leftKnee2", CubeListBuilder.create().texOffs(31, 34).addBox(-0.2F, 0.5F, -0.5F, 1.7F, 1.0F, 0.2F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.7854F));

        PartDefinition leftKneeSpike = leftKneeBase.addOrReplaceChild("leftKneeSpike", CubeListBuilder.create().texOffs(17, 32).addBox(-0.49F, -2.8F, -0.4F, 0.5F, 2.8F, 0.8F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-2.0F, 0.0F, 2.0F, 0.0F, 0.0F, -0.3128F));

        return LayerDefinition.create(meshdefinition, 64, 64);
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
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