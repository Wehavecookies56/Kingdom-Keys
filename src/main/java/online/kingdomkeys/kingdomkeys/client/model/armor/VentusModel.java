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

public class VentusModel<T extends LivingEntity> extends HumanoidModel<T> {

    public static final ModelLayerLocation LAYER_LOCATION_TOP = new ModelLayerLocation(new ResourceLocation(KingdomKeys.MODID, "ventus_top"), "main");
    public static final ModelLayerLocation LAYER_LOCATION_BOTTOM = new ModelLayerLocation(new ResourceLocation(KingdomKeys.MODID, "ventus_bottom"), "main");
    private final ModelPart horn1;
    private final ModelPart horn2;
    private final ModelPart rightElbowBase;
    private final ModelPart rightElbowSpike;
    private final ModelPart rightElbowSpike2;
    private final ModelPart rightKneeBase;
    private final ModelPart rightKnee1;
    private final ModelPart rightKnee2;
    private final ModelPart rightKneeSpike;
    private final ModelPart rightFoot;
    private final ModelPart rightFootTip;
    private final ModelPart leftElbowBase;
    private final ModelPart leftElbowSpike;
    private final ModelPart leftElbowSpike2;
    private final ModelPart leftKneeBase;
    private final ModelPart leftKnee1;
    private final ModelPart leftKnee2;
    private final ModelPart leftKneeSpike;
    private final ModelPart leftFoot;
    private final ModelPart leftFootTip;

    public VentusModel(ModelPart root) {
        super(root);
        this.horn1 = head.getChild("horn1");
        this.horn2 = head.getChild("horn2");
        this.rightElbowBase = rightArm.getChild("rightElbowBase");
        this.rightElbowSpike = rightElbowBase.getChild("rightElbowSpike");
        this.rightElbowSpike2 = rightElbowSpike.getChild("rightElbowSpike2");
        this.rightKneeBase = rightLeg.getChild("rightKneeBase");
        this.rightKnee1 = rightKneeBase.getChild("rightKnee1");
        this.rightKnee2 = rightKneeBase.getChild("rightKnee2");
        this.rightKneeSpike = rightKneeBase.getChild("rightKneeSpike");
        this.rightFoot = rightLeg.getChild("rightFoot");
        this.rightFootTip = rightFoot.getChild("rightFootTip");
        this.leftElbowBase = leftArm.getChild("leftElbowBase");
        this.leftElbowSpike = leftElbowBase.getChild("leftElbowSpike");
        this.leftElbowSpike2 = leftElbowSpike.getChild("leftElbowSpike2");
        this.leftKneeBase = leftLeg.getChild("leftKneeBase");
        this.leftKnee1 = leftKneeBase.getChild("leftKnee1");
        this.leftKnee2 = leftKneeBase.getChild("leftKnee2");
        this.leftKneeSpike = leftKneeBase.getChild("leftKneeSpike");
        this.leftFoot = leftLeg.getChild("leftFoot");
        this.leftFootTip = leftFoot.getChild("leftFootTip");
    }

    public static LayerDefinition createBodyLayer(CubeDeformation size) {
        MeshDefinition meshdefinition = VentusModel.createMesh(size, 0);
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition horn1 = partdefinition.addOrReplaceChild("horn1", CubeListBuilder.create().texOffs(0, 32).addBox(-1.0F, 0.0F, 0.0F, 1.0F, 4.0F, 2.0F, size), PartPose.offsetAndRotation(-3.5F, -12.0F, 0.0F, -0.2737F, 0.0F, 0.0F));

        PartDefinition horn2 = partdefinition.addOrReplaceChild("horn2", CubeListBuilder.create().texOffs(0, 32).addBox(-1.0F, 0.0F, 0.0F, 1.0F, 4.0F, 2.0F, size), PartPose.offsetAndRotation(4.5F, -12.0F, 0.0F, -0.2737F, 0.0F, 0.0F));

        PartDefinition rightElbowBase = partdefinition.addOrReplaceChild("rightElbowBase", CubeListBuilder.create().texOffs(36, 32).addBox(-3.5F, -2.0F, -3.5F, 7.0F, 5.0F, 7.0F, size), PartPose.offsetAndRotation(6.0F, 7.0F, 0.0F, 0.0F, -3.1416F, 0.0F));

        PartDefinition rightElbowSpike = rightElbowBase.addOrReplaceChild("rightElbowSpike", CubeListBuilder.create().texOffs(26, 38).addBox(-0.5F, -3.0F, -1.5F, 1.0F, 3.0F, 3.0F, size), PartPose.offsetAndRotation(-2.6F, 0.0F, 0.0F, 0.0F, 0.0F, -0.3491F));

        PartDefinition rightElbowSpike2 = rightElbowSpike.addOrReplaceChild("rightElbowSpike2", CubeListBuilder.create().texOffs(29, 44).addBox(-0.5F, -3.0F, -1.0F, 1.0F, 3.0F, 2.0F, size), PartPose.offsetAndRotation(-0.1F, -2.9F, 0.0F, 0.0F, 0.0F, 0.126F));

        PartDefinition rightKneeBase = partdefinition.addOrReplaceChild("rightKneeBase", CubeListBuilder.create().texOffs(16, 32).addBox(-2.4F, 0.0F, -0.5F, 4.0F, 1.0F, 5.0F, size), PartPose.offset(1.9F, 16.0F, -1.9F));

        PartDefinition rightKnee1 = rightKneeBase.addOrReplaceChild("rightKnee1", CubeListBuilder.create().texOffs(31, 32).addBox(-1.2F, 0.5F, -0.5F, 1.0F, 1.0F, 0.0F, size), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, -0.7854F));

        PartDefinition rightKneeSpike = rightKneeBase.addOrReplaceChild("rightKneeSpike", CubeListBuilder.create().texOffs(17, 32).addBox(-0.5F, -2.8F, -0.4F, 0.0F, 2.0F, 0.0F, size), PartPose.offsetAndRotation(2.4F, 0.0F, 2.0F, 0.0F, 0.0F, 0.3128F));

        PartDefinition rightKnee2 = rightKneeBase.addOrReplaceChild("rightKnee2", CubeListBuilder.create().texOffs(31, 34).addBox(-0.2F, 0.5F, -0.5F, 1.0F, 1.0F, 0.0F, size), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.7854F));

        PartDefinition rightFoot = partdefinition.addOrReplaceChild("rightFoot", CubeListBuilder.create().texOffs(0, 41).addBox(-1.9F, 0.0F, -3.0F, 4.0F, 3.0F, 2.0F, size), PartPose.offset(1.9F, 21.0F, -0.9F));

        PartDefinition rightFootTip = rightFoot.addOrReplaceChild("rightFootTip", CubeListBuilder.create().texOffs(16, 41).addBox(-1.9F, 0.0F, -1.0F, 4.0F, 2.0F, 1.0F, size), PartPose.offset(0.0F, 1.0F, -3.0F));

        PartDefinition leftElbowBase = partdefinition.addOrReplaceChild("leftElbowBase", CubeListBuilder.create().texOffs(36, 32).addBox(-3.5F, -2.0F, -3.5F, 7.0F, 5.0F, 7.0F, size), PartPose.offset(-6.0F, 7.0F, 0.0F));

        PartDefinition leftElbowSpike = leftElbowBase.addOrReplaceChild("leftElbowSpike", CubeListBuilder.create().texOffs(26, 38).addBox(-0.5F, -3.0F, -1.5F, 1.0F, 3.0F, 3.0F, size), PartPose.offsetAndRotation(-2.6F, 0.0F, 0.0F, 0.0F, 0.0F, -0.3491F));

        PartDefinition leftElbowSpike2 = leftElbowSpike.addOrReplaceChild("leftElbowSpike2", CubeListBuilder.create().texOffs(29, 44).addBox(-0.5F, -3.0F, -1.0F, 1.0F, 3.0F, 2.0F, size), PartPose.offsetAndRotation(-0.1F, -2.9F, 0.0F, 0.0F, 0.0F, 0.126F));

        PartDefinition leftKneeBase = partdefinition.addOrReplaceChild("leftKneeBase", CubeListBuilder.create().texOffs(16, 32).addBox(-2.5F, 0.0F, -0.5F, 4.0F, 1.0F, 5.0F, size), PartPose.offset(-1.9F, 16.0F, -1.9F));

        PartDefinition leftKnee1 = leftKneeBase.addOrReplaceChild("leftKnee1", CubeListBuilder.create().texOffs(31, 32).addBox(-1.2F, 0.5F, -0.5F, 1.0F, 1.0F, 0.0F, size), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, -0.7854F));

        PartDefinition leftKneeSpike = leftKneeBase.addOrReplaceChild("leftKneeSpike", CubeListBuilder.create().texOffs(17, 32).addBox(-0.49F, -2.8F, -0.4F, 0.0F, 2.0F, 0.0F, size), PartPose.offsetAndRotation(-2.0F, 0.0F, 2.0F, 0.0F, 0.0F, -0.3128F));

        PartDefinition leftKnee2 = leftKneeBase.addOrReplaceChild("leftKnee2", CubeListBuilder.create().texOffs(31, 34).addBox(-0.2F, 0.5F, -0.5F, 1.0F, 1.0F, 0.0F, size), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.7854F));

        PartDefinition leftFoot = partdefinition.addOrReplaceChild("leftFoot", CubeListBuilder.create().texOffs(0, 41).addBox(-2.1F, 0.0F, -3.0F, 4.0F, 3.0F, 2.0F, size), PartPose.offset(-1.9F, 21.0F, -0.9F));

        PartDefinition leftFootTip = leftFoot.addOrReplaceChild("leftFootTip", CubeListBuilder.create().texOffs(16, 41).addBox(-2.1F, 0.0F, -1.0F, 4.0F, 2.0F, 1.0F, size), PartPose.offset(0.0F, 1.0F, -3.0F));

        return LayerDefinition.create(meshdefinition, 64, 64);
    }
    
    @Override
    public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        if (entity instanceof ArmorStand) {
            super.setupAnim(entity, 0, 0, 0, 0, 0);
        } else {
            super.setupAnim(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
        }
    }
    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        super.renderToBuffer(poseStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
        horn1.render(poseStack, buffer, packedLight, packedOverlay);
        horn2.render(poseStack, buffer, packedLight, packedOverlay);
        rightElbowBase.render(poseStack, buffer, packedLight, packedOverlay);
        rightKneeBase.render(poseStack, buffer, packedLight, packedOverlay);
        rightFoot.render(poseStack, buffer, packedLight, packedOverlay);
        leftElbowBase.render(poseStack, buffer, packedLight, packedOverlay);
        leftKneeBase.render(poseStack, buffer, packedLight, packedOverlay);
        leftFoot.render(poseStack, buffer, packedLight, packedOverlay);
    }
}