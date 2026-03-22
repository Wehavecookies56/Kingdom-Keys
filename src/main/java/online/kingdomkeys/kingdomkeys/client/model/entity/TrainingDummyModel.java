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

public class TrainingDummyModel<T extends Entity> extends EntityModel<T> {
    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(ResourceLocation.fromNamespaceAndPath(KingdomKeys.MODID, "training_dummy"), "main");
    private final ModelPart bone;

    public TrainingDummyModel(ModelPart root) {
        this.bone = root.getChild("bone");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition bone = partdefinition.addOrReplaceChild("bone", CubeListBuilder.create().texOffs(40, 53).addBox(-1.0F, -15.0F, -1.0F, 2.0F, 15.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(48, 53).addBox(-2.0F, -14.0F, -2.0F, 4.0F, 2.0F, 4.0F, new CubeDeformation(0.0F))
                .texOffs(40, 0).addBox(-3.0F, -20.0F, -3.0F, 6.0F, 6.0F, 6.0F, new CubeDeformation(0.0F))
                .texOffs(0, 22).addBox(-4.0F, -36.0F, -5.0F, 8.0F, 16.0F, 9.0F, new CubeDeformation(0.0F))
                .texOffs(58, 38).addBox(20.0F, -35.0F, -1.0F, 6.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(58, 42).addBox(-26.0F, -35.0F, -1.0F, 6.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(34, 22).addBox(4.0F, -36.0F, -2.0F, 16.0F, 4.0F, 4.0F, new CubeDeformation(0.0F))
                .texOffs(34, 30).addBox(-20.0F, -36.0F, -2.0F, 16.0F, 4.0F, 4.0F, new CubeDeformation(0.0F))
                .texOffs(0, 0).addBox(-5.0F, -48.0F, -5.0F, 10.0F, 12.0F, 10.0F, new CubeDeformation(0.0F))
                .texOffs(34, 38).addBox(-3.0F, -56.0F, -3.0F, 6.0F, 9.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 24.0F, 0.0F));

        PartDefinition back_skirt_r1 = bone.addOrReplaceChild("back_skirt_r1", CubeListBuilder.create().texOffs(40, 12).addBox(-7.0F, -8.0F, 5.0F, 8.0F, 8.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(3.0F, -17.0F, -13.0F, -0.5236F, 0.0F, 0.0F));

        PartDefinition right_skirt_r1 = bone.addOrReplaceChild("right_skirt_r1", CubeListBuilder.create().texOffs(0, 67).addBox(2.0F, -8.0F, -5.0F, 2.0F, 8.0F, 9.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-9.0F, -16.0F, 0.0F, 0.0F, 0.0F, 0.5236F));

        PartDefinition left_skirt_r1 = bone.addOrReplaceChild("left_skirt_r1", CubeListBuilder.create().texOffs(-1, 46).addBox(15.0F, -5.0F, -5.0F, 2.0F, 8.0F, 9.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-9.0F, -9.0F, 0.0F, 0.0F, 0.0F, -0.5236F));

        return LayerDefinition.create(meshdefinition, 128, 128);
    }

    @Override
    public void setupAnim(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {

    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer buffer, int packedLight, int packedOverlay, int colour) {
        bone.render(poseStack, buffer, packedLight, packedOverlay, colour);
    }
}

