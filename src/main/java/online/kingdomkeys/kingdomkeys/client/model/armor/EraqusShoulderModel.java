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

public class EraqusShoulderModel<T extends LivingEntity> extends HumanoidModel<T> {

	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation(KingdomKeys.MODID, "eraqus_shoulder"), "main");

    public EraqusShoulderModel(ModelPart root) {
        super(root);
    }

    public static LayerDefinition createBodyLayer(CubeDeformation size) {
        MeshDefinition meshdefinition = HumanoidModel.createMesh(size, 0);
        PartDefinition partdefinition = meshdefinition.getRoot();
        
        PartDefinition leftArm = partdefinition.getChild("left_arm");
        
		PartDefinition ButtonArmor = leftArm.addOrReplaceChild("ButtonArmor", CubeListBuilder.create().texOffs(1, 30).addBox(7.0F, -20.6F, -1.5F, 0.65F, 3.2F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(-5.0F, 20.9F, 0.0F));

		PartDefinition Armor3_r1 = ButtonArmor.addOrReplaceChild("Armor3_r1", CubeListBuilder.create().texOffs(0, 9).addBox(-0.125F, -1.0F, -2.0F, 0.25F, 2.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(7.375F, -14.8F, 0.0F, 0.0F, 0.0F, -0.2618F));

		PartDefinition Armor2_r1 = ButtonArmor.addOrReplaceChild("Armor2_r1", CubeListBuilder.create().texOffs(0, 17).addBox(-0.125F, -1.0F, -2.0F, 0.25F, 2.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(7.375F, -15.9F, 0.0F, 0.0F, 0.0F, -0.2618F));

		PartDefinition Armor1_r1 = ButtonArmor.addOrReplaceChild("Armor1_r1", CubeListBuilder.create().texOffs(0, 9).addBox(-0.125F, -1.0F, -2.0F, 0.25F, 2.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(7.375F, -17.2F, 0.0F, 0.0F, 0.0F, -0.2618F));

		PartDefinition Button_r1 = ButtonArmor.addOrReplaceChild("Button_r1", CubeListBuilder.create().texOffs(0, 0).addBox(-0.5F, -1.0F, -1.0F, 1.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(7.5F, -19.0F, 0.0F, -0.7854F, 0.0F, 0.0F));
		
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
