package online.kingdomkeys.kingdomkeys.client.model.armor;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

import net.minecraft.client.model.EntityModel;
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
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.decoration.ArmorStand;
import online.kingdomkeys.kingdomkeys.KingdomKeys;

public class TerraShoulderModel<T extends LivingEntity> extends HumanoidModel<T> {

	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation(KingdomKeys.MODID, "terra_shoulder"), "main");

    public TerraShoulderModel(ModelPart root) {
        super(root);
    }

    public static LayerDefinition createBodyLayer(CubeDeformation size) {
        MeshDefinition meshdefinition = HumanoidModel.createMesh(size, 0);
        PartDefinition partdefinition = meshdefinition.getRoot();
        
        PartDefinition leftArm = partdefinition.getChild("left_arm");
        
		PartDefinition Pauldron = leftArm.addOrReplaceChild("Pauldron", CubeListBuilder.create(), PartPose.offsetAndRotation(2.0512F, 0.7372F, 0.0179F, 0.0F, 0.0F, 0.48F));

		PartDefinition cube_r1 = Pauldron.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(117, 50).addBox(-0.1032F, -0.324F, -2.05F, 0.2F, 0.3F, 4.1F, new CubeDeformation(0.5F)), PartPose.offsetAndRotation(-3.545F, -1.0817F, 0.0321F, 0.0F, 0.0F, -1.885F));

		PartDefinition cube_r2 = Pauldron.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(117, 50).addBox(-0.1316F, -0.1269F, -2.25F, 0.3F, 0.6F, 4.5F, new CubeDeformation(0.5F)), PartPose.offsetAndRotation(-3.545F, -1.0817F, 0.0321F, 0.0F, 0.0F, -1.7977F));

		PartDefinition cube_r3 = Pauldron.addOrReplaceChild("cube_r3", CubeListBuilder.create().texOffs(120, 53).addBox(-0.15F, -0.2F, -0.9F, 0.3F, 0.4F, 1.8F, new CubeDeformation(0.5F)), PartPose.offsetAndRotation(0.6808F, 0.1279F, 1.0821F, 0.4363F, 0.0F, -0.8203F));

		PartDefinition cube_r4 = Pauldron.addOrReplaceChild("cube_r4", CubeListBuilder.create().texOffs(120, 53).addBox(-0.15F, -0.5F, -0.8F, 0.3F, 0.7F, 1.7F, new CubeDeformation(0.5F)), PartPose.offsetAndRotation(0.9105F, 0.3422F, 1.1183F, 0.0873F, 0.0F, -0.8203F));

		PartDefinition cube_r5 = Pauldron.addOrReplaceChild("cube_r5", CubeListBuilder.create().texOffs(120, 53).addBox(-0.15F, -0.2F, -0.7F, 0.1F, 0.8F, 1.5F, new CubeDeformation(0.5F)), PartPose.offsetAndRotation(0.9105F, 0.3422F, 1.1183F, -0.384F, 0.0F, -0.8203F));

		PartDefinition cube_r6 = Pauldron.addOrReplaceChild("cube_r6", CubeListBuilder.create().texOffs(120, 53).addBox(-0.15F, -0.5F, -0.55F, 0.1F, 0.5F, 1.1F, new CubeDeformation(0.5F)), PartPose.offsetAndRotation(1.6023F, 0.9873F, 0.7901F, -0.5149F, 0.0F, -0.8203F));

		PartDefinition cube_r7 = Pauldron.addOrReplaceChild("cube_r7", CubeListBuilder.create().texOffs(120, 53).addBox(-0.15F, -0.5F, -0.55F, 0.1F, 0.5F, 1.1F, new CubeDeformation(0.5F)), PartPose.offsetAndRotation(1.6023F, 0.9873F, -0.8258F, 0.5149F, 0.0F, -0.8203F));

		PartDefinition cube_r8 = Pauldron.addOrReplaceChild("cube_r8", CubeListBuilder.create().texOffs(120, 53).addBox(-0.15F, -0.2F, -0.8F, 0.1F, 0.8F, 1.5F, new CubeDeformation(0.5F)), PartPose.offsetAndRotation(0.9105F, 0.3422F, -1.154F, 0.384F, 0.0F, -0.8203F));

		PartDefinition cube_r9 = Pauldron.addOrReplaceChild("cube_r9", CubeListBuilder.create().texOffs(120, 53).addBox(-0.15F, -0.5F, -0.9F, 0.3F, 0.7F, 1.7F, new CubeDeformation(0.5F)), PartPose.offsetAndRotation(0.9105F, 0.3422F, -1.154F, -0.0873F, 0.0F, -0.8203F));

		PartDefinition cube_r10 = Pauldron.addOrReplaceChild("cube_r10", CubeListBuilder.create().texOffs(120, 53).addBox(-0.15F, -0.2F, -0.9F, 0.3F, 0.4F, 1.8F, new CubeDeformation(0.5F)), PartPose.offsetAndRotation(0.6808F, 0.1279F, -1.1179F, -0.4363F, 0.0F, -0.8203F));

		PartDefinition cube_r11 = Pauldron.addOrReplaceChild("cube_r11", CubeListBuilder.create().texOffs(119, 52).addBox(-0.15F, -0.2F, -1.15F, 0.3F, 0.4F, 2.2F, new CubeDeformation(0.5F))
		.texOffs(117, 50).addBox(-0.15F, -0.6F, -2.05F, 0.3F, 0.4F, 4.1F, new CubeDeformation(0.5F)), PartPose.offsetAndRotation(0.6808F, 0.1279F, 0.0321F, 0.0F, 0.0F, -0.8203F));

		PartDefinition cube_r12 = Pauldron.addOrReplaceChild("cube_r12", CubeListBuilder.create().texOffs(117, 50).addBox(-0.15F, -0.35F, -2.25F, 0.3F, 0.7F, 4.5F, new CubeDeformation(0.5F)), PartPose.offsetAndRotation(-0.014F, -0.52F, 0.0321F, 0.0F, 0.0F, -0.8203F));

		PartDefinition cube_r13 = Pauldron.addOrReplaceChild("cube_r13", CubeListBuilder.create().texOffs(117, 50).addBox(-0.25F, -2.5F, -2.35F, 0.4F, 3.1F, 4.7F, new CubeDeformation(0.5F)), PartPose.offsetAndRotation(-0.7118F, -0.8472F, 0.0321F, 0.0F, 0.0F, -1.3963F));

		PartDefinition p2 = Pauldron.addOrReplaceChild("p2", CubeListBuilder.create().texOffs(94, 2).addBox(-2.9F, -2.0F, -2.1F, 2.4F, 1.0F, 4.2F, new CubeDeformation(0.5F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		return LayerDefinition.create(meshdefinition, 128, 128);    
	}

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        leftArm.render(poseStack, vertexConsumer, packedLight, packedOverlay);
    }
    
    @Override
    public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
    	if (entity instanceof ArmorStand) {
            super.setupAnim(entity, 0, 0, 0, 0, 0);
        } else {
        	leftArm.copyFrom(super.leftArm);
            super.setupAnim(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
        }
    }
}
