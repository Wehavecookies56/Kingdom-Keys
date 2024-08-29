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

public class VentusShoulderModel<T extends LivingEntity> extends HumanoidModel<T> {

	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(ResourceLocation.fromNamespaceAndPath(KingdomKeys.MODID, "ventus_shoulder"), "main");

    public VentusShoulderModel(ModelPart root) {
        super(root);
    }

    public static LayerDefinition createBodyLayer(CubeDeformation size) {
        MeshDefinition meshdefinition = HumanoidModel.createMesh(size, 0);
        PartDefinition partdefinition = meshdefinition.getRoot();
        
        PartDefinition leftArm = partdefinition.getChild("left_arm");
        
        PartDefinition ShoulderArmor = leftArm.addOrReplaceChild("ShoulderArmor", CubeListBuilder.create(), PartPose.offset(2.45F, -0.1F, 0.1F));

		PartDefinition cube_r1 = ShoulderArmor.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(43, 49).addBox(-0.125F, -0.875F, -1.1F, 0.15F, 1.85F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.0333F, 1.5295F, -0.5F, 0.0F, 0.0F, -0.1309F));

		PartDefinition cube_r2 = ShoulderArmor.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(35, 49).addBox(-0.125F, -0.875F, -1.1F, 0.15F, 1.25F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.1667F, 1.5295F, -0.5F, 0.0F, 0.0F, -0.3491F));

		PartDefinition TopArmor = ShoulderArmor.addOrReplaceChild("TopArmor", CubeListBuilder.create().texOffs(34, 56).addBox(-0.25F, -2.25F, -2.0F, 0.25F, 3.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition cube_r3 = TopArmor.addOrReplaceChild("cube_r3", CubeListBuilder.create().texOffs(61, 61).addBox(-0.125F, -0.65F, -0.65F, 0.15F, 1.3F, 1.3F, new CubeDeformation(0.0F))
		.texOffs(28, 59).addBox(-0.175F, -0.9F, -0.9F, 0.15F, 1.8F, 1.8F, new CubeDeformation(0.0F))
		.texOffs(28, 59).addBox(-0.225F, -1.0F, -1.0F, 0.15F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.225F, -0.7071F, -0.0929F, -0.7854F, 0.0F, 0.0F));

		PartDefinition Plate10_r1 = TopArmor.addOrReplaceChild("Plate10_r1", CubeListBuilder.create().texOffs(28, 54).addBox(-1.0F, -1.0F, -1.0F, 0.05F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.75F, -2.9F, -0.1F, -0.7854F, 0.0F, 0.0F));

		PartDefinition Plate9_r1 = TopArmor.addOrReplaceChild("Plate9_r1", CubeListBuilder.create().texOffs(28, 52).addBox(-0.125F, -0.25F, -0.825F, 0.15F, 0.75F, 1.65F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.125F, -2.9063F, -0.1825F, -1.4661F, 0.0F, 0.0F));

		PartDefinition Plate8_r1 = TopArmor.addOrReplaceChild("Plate8_r1", CubeListBuilder.create().texOffs(28, 52).addBox(-0.125F, -0.25F, -0.825F, 0.15F, 0.75F, 1.65F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.125F, -2.9063F, -0.0175F, 1.4661F, 0.0F, 0.0F));

		PartDefinition Plate7_r1 = TopArmor.addOrReplaceChild("Plate7_r1", CubeListBuilder.create().texOffs(28, 52).addBox(-0.125F, -0.5F, -0.825F, 0.15F, 1.0F, 1.65F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.125F, -2.7119F, 0.5024F, 0.9599F, 0.0F, 0.0F));

		PartDefinition Plate6_r1 = TopArmor.addOrReplaceChild("Plate6_r1", CubeListBuilder.create().texOffs(28, 52).addBox(-0.125F, -0.5F, -0.825F, 0.15F, 1.0F, 1.65F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.125F, -2.7119F, -0.7024F, -0.9599F, 0.0F, 0.0F));

		PartDefinition Plate5_r1 = TopArmor.addOrReplaceChild("Plate5_r1", CubeListBuilder.create().texOffs(28, 52).addBox(-0.125F, -1.0F, -1.0F, 0.15F, 1.0F, 1.65F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.125F, -1.833F, -0.9821F, -0.3927F, 0.0F, 0.0F));

		PartDefinition Plate4_r1 = TopArmor.addOrReplaceChild("Plate4_r1", CubeListBuilder.create().texOffs(28, 52).addBox(-0.125F, -1.0F, -0.65F, 0.15F, 1.0F, 1.65F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.125F, -1.833F, 0.7821F, 0.3927F, 0.0F, 0.0F));

		PartDefinition Plate3_r1 = TopArmor.addOrReplaceChild("Plate3_r1", CubeListBuilder.create().texOffs(20, 58).addBox(-0.25F, -1.5F, 1.0F, 0.15F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.5F, 0.7F, 0.5236F, 0.0F, 0.0F));

		PartDefinition Plate2_r1 = TopArmor.addOrReplaceChild("Plate2_r1", CubeListBuilder.create().texOffs(20, 58).addBox(-0.25F, -1.5F, -3.0F, 0.15F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.5F, -0.9F, -0.5236F, 0.0F, 0.0F));

		PartDefinition TopArmorEnd = ShoulderArmor.addOrReplaceChild("TopArmorEnd", CubeListBuilder.create().texOffs(55, 61).addBox(-0.025F, -1.1066F, -1.4545F, 0.05F, 1.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(55, 61).addBox(-0.025F, -0.4066F, -1.7045F, 0.05F, 0.5F, 2.5F, new CubeDeformation(0.0F)), PartPose.offset(0.025F, 0.7995F, 0.3616F));

		PartDefinition cube_r4 = TopArmorEnd.addOrReplaceChild("cube_r4", CubeListBuilder.create().texOffs(57, 63).addBox(-0.025F, -0.375F, 0.35F, 0.05F, 0.65F, 0.5F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.7854F, 0.0F, 0.0F));

		PartDefinition cube_r5 = TopArmorEnd.addOrReplaceChild("cube_r5", CubeListBuilder.create().texOffs(57, 63).addBox(-0.025F, -0.375F, -0.85F, 0.05F, 0.65F, 0.5F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, -0.9232F, -0.7854F, 0.0F, 0.0F));

		return LayerDefinition.create(meshdefinition, 64, 64);
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, int colour) {
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
