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

public class XehanortShoulderModel<T extends LivingEntity> extends HumanoidModel<T> {

	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation(KingdomKeys.MODID, "xehanort_shoulder"), "main");

    public XehanortShoulderModel(ModelPart root) {
        super(root);
    }

    public static LayerDefinition createBodyLayer(CubeDeformation size) {
        MeshDefinition meshdefinition = HumanoidModel.createMesh(size, 0);
        PartDefinition partdefinition = meshdefinition.getRoot();
        
        PartDefinition leftArm = partdefinition.getChild("left_arm");
        
		PartDefinition ShoulderArmor = leftArm.addOrReplaceChild("ShoulderArmor", CubeListBuilder.create().texOffs(21, 61).addBox(2.1F, -2.3F, -0.2F, 0.25F, 2.3F, 0.4F, new CubeDeformation(0.0F))
		.texOffs(21, 61).addBox(2.1F, 0.4F, -0.2F, 0.25F, 1.6F, 0.4F, new CubeDeformation(0.0F))
		.texOffs(38, 56).addBox(2.1F, -1.7064F, -2.1308F, 0.15F, 3.5F, 4.3F, new CubeDeformation(0.0F)), PartPose.offset(0.1F, 0.7F, 0.0F));

		PartDefinition cube_r1 = ShoulderArmor.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(28, 58).addBox(-1.9F, -2.0F, -1.0F, 0.15F, 3.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(4.0F, 2.5F, 0.0F, 0.7854F, 0.0F, 0.0F));

		PartDefinition cube_r2 = ShoulderArmor.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(28, 58).addBox(-1.9F, -2.0F, -1.0F, 0.15F, 3.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(4.0F, -1.0F, 0.0F, 0.7854F, 0.0F, 0.0F));

		PartDefinition cube_r3 = ShoulderArmor.addOrReplaceChild("cube_r3", CubeListBuilder.create().texOffs(20, 60).addBox(-0.125F, -0.4F, -0.4F, 0.25F, 0.8F, 0.8F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(2.225F, -2.5F, 0.0F, 0.7854F, 0.0F, 0.0F));

		PartDefinition cube_r4 = ShoulderArmor.addOrReplaceChild("cube_r4", CubeListBuilder.create().texOffs(21, 61).addBox(-0.125F, -0.4F, -0.4F, 0.25F, 0.7F, 0.7F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(2.225F, -1.3F, 1.1F, 0.7854F, 0.0F, 0.0F));

		PartDefinition cube_r5 = ShoulderArmor.addOrReplaceChild("cube_r5", CubeListBuilder.create().texOffs(21, 61).addBox(-0.125F, -0.4F, -0.3F, 0.25F, 0.7F, 0.7F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(2.225F, -1.3F, -1.1F, -0.7854F, 0.0F, 0.0F));

		PartDefinition cube_r6 = ShoulderArmor.addOrReplaceChild("cube_r6", CubeListBuilder.create().texOffs(21, 61).addBox(-0.125F, -0.5F, -0.5F, 0.25F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(2.225F, 1.6F, -0.2F, -0.7854F, 0.0F, 0.0F));

		PartDefinition cube_r7 = ShoulderArmor.addOrReplaceChild("cube_r7", CubeListBuilder.create().texOffs(21, 61).addBox(-0.125F, -0.5F, -0.5F, 0.25F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(2.225F, 1.6F, 0.2F, -0.7854F, 0.0F, 0.0F));

		PartDefinition cube_r8 = ShoulderArmor.addOrReplaceChild("cube_r8", CubeListBuilder.create().texOffs(21, 61).addBox(-0.125F, -0.5F, -0.5F, 0.25F, 1.6F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(2.225F, 1.6F, 1.0F, -0.7854F, 0.0F, 0.0F));

		PartDefinition cube_r9 = ShoulderArmor.addOrReplaceChild("cube_r9", CubeListBuilder.create().texOffs(21, 61).addBox(-0.125F, -0.2F, -0.8F, 0.25F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(21, 61).addBox(-0.125F, -0.3F, -0.9F, 0.25F, 1.0F, 1.2F, new CubeDeformation(0.0F))
		.texOffs(21, 61).addBox(-0.125F, -0.4F, -1.0F, 0.25F, 1.0F, 1.4F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(2.225F, 1.6F, 1.0F, 0.7854F, 0.0F, 0.0F));

		PartDefinition cube_r10 = ShoulderArmor.addOrReplaceChild("cube_r10", CubeListBuilder.create().texOffs(21, 61).addBox(-0.125F, -0.2F, -0.2F, 0.25F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(21, 61).addBox(-0.125F, -0.3F, -0.3F, 0.25F, 1.0F, 1.2F, new CubeDeformation(0.0F))
		.texOffs(21, 61).addBox(-0.125F, -0.4F, -0.4F, 0.25F, 1.0F, 1.4F, new CubeDeformation(0.0F))
		.texOffs(20, 60).addBox(-0.125F, -0.5F, -0.5F, 0.25F, 1.0F, 1.6F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(2.225F, 1.6F, -1.0F, -0.7854F, 0.0F, 0.0F));

		PartDefinition cube_r11 = ShoulderArmor.addOrReplaceChild("cube_r11", CubeListBuilder.create().texOffs(21, 61).addBox(-0.125F, -0.4F, -0.4F, 0.25F, 0.8F, 0.8F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(2.225F, -1.3F, 0.0F, -0.7854F, 0.0F, 0.0F));

		PartDefinition cube_r12 = ShoulderArmor.addOrReplaceChild("cube_r12", CubeListBuilder.create().texOffs(21, 61).addBox(-0.125F, -0.5F, -0.5F, 0.25F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(2.225F, 0.2F, 0.0F, -0.7854F, 0.0F, 0.0F));

		PartDefinition cube_r13 = ShoulderArmor.addOrReplaceChild("cube_r13", CubeListBuilder.create().texOffs(21, 61).addBox(-0.125F, -0.9F, -0.2F, 0.25F, 1.8F, 0.4F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(2.225F, -1.3F, 0.0F, -1.5708F, 0.0F, 0.0F));

		return LayerDefinition.create(meshdefinition, 64, 64);
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
