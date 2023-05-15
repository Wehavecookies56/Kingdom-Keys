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

public class TerraModel<T extends LivingEntity> extends HumanoidModel<T> {

    public static final ModelLayerLocation LAYER_LOCATION_TOP = new ModelLayerLocation(new ResourceLocation(KingdomKeys.MODID, "terra_top"), "main");
    public static final ModelLayerLocation LAYER_LOCATION_BOTTOM = new ModelLayerLocation(new ResourceLocation(KingdomKeys.MODID, "terra_bottom"), "main");

	//private final ModelPart rightArm2;
	

    public TerraModel(ModelPart root) {
        super(root);
		//this.rightArm2 = root.getChild("right_arm").getChild("leftArm2");

    }

    public static LayerDefinition createBodyLayer(CubeDeformation size) {
        MeshDefinition meshdefinition = HumanoidModel.createMesh(size, 0);
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition rightArm = partdefinition.getChild("right_arm");
        PartDefinition leftArm = partdefinition.getChild("left_arm");
        PartDefinition rightLeg = partdefinition.getChild("right_leg");
        PartDefinition leftLeg = partdefinition.getChild("left_leg");
        PartDefinition head = partdefinition.getChild("head");
        PartDefinition body = partdefinition.getChild("body");
		
        PartDefinition horn1 = head.addOrReplaceChild("horn1", CubeListBuilder.create().texOffs(41, 57).addBox(-1.0F, 0.0F, 0.0F, 1.0F, 4.5F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-3.0F, -12.0F, 0.0F, -0.2737F, 0.0F, 0.0F));
		PartDefinition horn2 = head.addOrReplaceChild("horn2", CubeListBuilder.create().texOffs(41, 57).addBox(-1.0F, 0.0F, 0.0F, 1.0F, 4.5F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(4.0F, -12.0F, 0.0F, -0.2737F, 0.0F, 0.0F));

		PartDefinition rightArm2 = rightArm.addOrReplaceChild("rightArm2", CubeListBuilder.create().texOffs(32, 48).addBox(-3.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(-5.0F, 2.0F, 0.0F));


        return LayerDefinition.create(meshdefinition, 64, 64);
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