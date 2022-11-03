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

/**
 * Moogle - cheriecheese
 * Created using Blockbench
 */
public class MoogleModel<T extends Entity> extends EntityModel<T> {
    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation(KingdomKeys.MODID, "moogle"), "main");
    private final ModelPart bb_main;

    public MoogleModel(ModelPart root) {
        this.bb_main = root.getChild("bb_main");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition bb_main = partdefinition.addOrReplaceChild("bb_main", CubeListBuilder.create().texOffs(0, 10).addBox(-2.0F, -8.0F, -2.0F, 4.0F, 5.0F, 3.0F, new CubeDeformation(0.0F))
                .texOffs(0, 0).addBox(-3.0F, -13.0F, -3.0F, 6.0F, 5.0F, 5.0F, new CubeDeformation(0.0F))
                .texOffs(0, 18).addBox(-1.0F, -11.0F, -4.0F, 2.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(17, 0).addBox(2.0F, -14.0F, -1.0F, 2.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(11, 10).addBox(-4.0F, -14.0F, -1.0F, 2.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 24.0F, 0.0F));

        PartDefinition antennaEnd_r1 = bb_main.addOrReplaceChild("antennaEnd_r1", CubeListBuilder.create().texOffs(14, 10).addBox(-1.5F, -19.0F, -3.5F, 3.0F, 3.0F, 3.0F, new CubeDeformation(0.0F))
                .texOffs(14, 16).addBox(-0.5F, -16.0F, -2.5F, 1.0F, 5.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.1309F, 0.0F, 0.0F));

        PartDefinition leftWing_r1 = bb_main.addOrReplaceChild("leftWing_r1", CubeListBuilder.create().texOffs(0, 10).addBox(5.0F, -5.0F, 2.0F, 1.0F, 2.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.2182F, 0.0F, -0.7854F));

        PartDefinition rightWing_r1 = bb_main.addOrReplaceChild("rightWing_r1", CubeListBuilder.create().texOffs(17, 3).addBox(-6.0F, -5.0F, 2.0F, 1.0F, 2.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.2182F, 0.0F, 0.7854F));

        PartDefinition rightArm_r1 = bb_main.addOrReplaceChild("rightArm_r1", CubeListBuilder.create().texOffs(0, 0).addBox(-5.0F, -6.0F, -1.0F, 1.0F, 3.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.3927F));

        PartDefinition leftArm_r1 = bb_main.addOrReplaceChild("leftArm_r1", CubeListBuilder.create().texOffs(6, 18).addBox(4.0F, -6.0F, -1.0F, 1.0F, 3.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, -0.3927F));

        PartDefinition rightLeg_r1 = bb_main.addOrReplaceChild("rightLeg_r1", CubeListBuilder.create().texOffs(10, 18).addBox(-2.0F, -4.0F, 0.0F, 1.0F, 3.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(18, 16).addBox(1.0F, -4.0F, 0.0F, 1.0F, 3.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.3491F, 0.0F, 0.0F));

        return LayerDefinition.create(meshdefinition, 32, 32);
    }

    @Override
    public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {

    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
    	poseStack.translate(0,-0.5,0);
        bb_main.render(poseStack, buffer, packedLight, packedOverlay);
    }
}