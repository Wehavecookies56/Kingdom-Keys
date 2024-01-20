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

public class EraqusShoulderModel<T extends LivingEntity> extends HumanoidModel<T> {

	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation(KingdomKeys.MODID, "eraqus_shoulder"), "main");

    public EraqusShoulderModel(ModelPart root) {
        super(root);
    }

    public static LayerDefinition createBodyLayer(CubeDeformation size) {
        MeshDefinition meshdefinition = HumanoidModel.createMesh(size, 0);
        PartDefinition partdefinition = meshdefinition.getRoot();
        
        PartDefinition leftArm = partdefinition.getChild("left_arm");
        
        PartDefinition ShoulderArmor = leftArm.addOrReplaceChild("ShoulderArmor", CubeListBuilder.create().texOffs(46, 52).addBox(4.1F, -3.0F, -1.6F, 0.1F, 7.0F, 4.3F, new CubeDeformation(0.0F))
		.texOffs(46, 38).addBox(4.0F, -3.1F, -1.7F, 0.1F, 7.2F, 4.5F, new CubeDeformation(0.0F)), PartPose.offset(-1.9F, 0.7F, -0.65F));

		PartDefinition cube_r1 = ShoulderArmor.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(39, 59).addBox(-1.0F, -1.0F, -1.0F, 0.5F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(5.0F, 2.0263F, 0.6263F, -0.7854F, 0.0F, 0.0F));

		PartDefinition cube_r2 = ShoulderArmor.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(32, 59).addBox(-1.0F, -1.0F, -1.0F, 0.5F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(5.0F, 0.6263F, 0.6263F, -0.7854F, 0.0F, 0.0F));

		PartDefinition cube_r3 = ShoulderArmor.addOrReplaceChild("cube_r3", CubeListBuilder.create().texOffs(26, 59).addBox(-1.0F, -1.0F, -1.0F, 0.5F, 2.3F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(5.0F, -1.0F, 2.2527F, -0.7854F, 0.0F, 0.0F));

		PartDefinition cube_r4 = ShoulderArmor.addOrReplaceChild("cube_r4", CubeListBuilder.create().texOffs(18, 59).addBox(-1.0F, -1.0F, -1.0F, 0.5F, 2.3F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(5.0F, -1.0F, -1.0F, 0.7854F, 0.0F, 0.0F));

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
