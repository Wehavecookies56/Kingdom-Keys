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

public class TerraShoulderModel<T extends LivingEntity> extends HumanoidModel<T> {

	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation(KingdomKeys.MODID, "terra_shoulder"), "main");

    public TerraShoulderModel(ModelPart root) {
        super(root);
    }

    public static LayerDefinition createBodyLayer(CubeDeformation size) {
        MeshDefinition meshdefinition = HumanoidModel.createMesh(size, 0);
        PartDefinition partdefinition = meshdefinition.getRoot();
        
        PartDefinition leftArm = partdefinition.getChild("left_arm");
        

        		PartDefinition ShoulderArmor = leftArm.addOrReplaceChild("ShoulderArmor", CubeListBuilder.create().texOffs(18, 60).addBox(-0.25F, -1.0F, -1.0F, 0.4F, 2.0F, 2.0F, new CubeDeformation(0.0F))
        		.texOffs(24, 58).addBox(-0.25F, -1.2F, -1.2F, 0.25F, 2.4F, 2.4F, new CubeDeformation(0.0F)), PartPose.offset(2.45F, -0.1F, 0.1F));

        		PartDefinition cube_r1 = ShoulderArmor.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(24, 58).addBox(-0.025F, -0.45F, -1.1F, 0.05F, 0.4F, 2.2F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.125F, 1.65F, 0.0F, 0.0F, 0.0F, -0.1309F));

        		PartDefinition cube_r2 = ShoulderArmor.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(24, 58).addBox(-0.125F, -1.0F, -1.0F, 0.25F, 2.2F, 2.2F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.125F, -0.5F, 0.0F, -0.7854F, 0.0F, 0.0F));

        		PartDefinition TopPlate = ShoulderArmor.addOrReplaceChild("TopPlate", CubeListBuilder.create().texOffs(31, 57).addBox(-0.1287F, -3.1511F, -1.95F, 0.1F, 2.4F, 3.9F, new CubeDeformation(0.0F)), PartPose.offset(-0.2213F, 1.2511F, -0.05F));

        		PartDefinition cube_r3 = TopPlate.addOrReplaceChild("cube_r3", CubeListBuilder.create().texOffs(31, 57).addBox(-0.05F, -0.75F, -1.95F, 0.1F, 1.2F, 3.9F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, -0.1047F));

        		PartDefinition cube_r4 = TopPlate.addOrReplaceChild("cube_r4", CubeListBuilder.create().texOffs(33, 59).addBox(-0.65F, -2.1F, -1.0F, 0.1F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.5213F, -2.3511F, 0.75F, 0.7854F, 0.0F, 0.0F));

        		PartDefinition cube_r5 = TopPlate.addOrReplaceChild("cube_r5", CubeListBuilder.create().texOffs(33, 59).addBox(-0.05F, -0.3F, -0.55F, 0.1F, 1.0F, 1.5F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.0787F, -1.8511F, 2.2F, -1.309F, 0.0F, 0.0F));

        		PartDefinition cube_r6 = TopPlate.addOrReplaceChild("cube_r6", CubeListBuilder.create().texOffs(34, 60).addBox(-0.05F, -0.3F, -0.4F, 0.1F, 1.1F, 1.35F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.0787F, -2.2511F, 2.7F, -0.9163F, 0.0F, 0.0F));

        		PartDefinition cube_r7 = TopPlate.addOrReplaceChild("cube_r7", CubeListBuilder.create().texOffs(33, 59).addBox(-0.05F, -0.3F, -0.95F, 0.1F, 1.0F, 1.5F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.0787F, -1.8511F, -2.3F, 1.309F, 0.0F, 0.0F));

        		PartDefinition cube_r8 = TopPlate.addOrReplaceChild("cube_r8", CubeListBuilder.create().texOffs(34, 60).addBox(-0.05F, -0.3F, -0.95F, 0.1F, 1.1F, 1.35F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.0787F, -2.2511F, -2.8F, 0.9163F, 0.0F, 0.0F));

        		PartDefinition MiddlePlate1 = ShoulderArmor.addOrReplaceChild("MiddlePlate1", CubeListBuilder.create().texOffs(40, 60).addBox(7.2F, -20.4F, -1.1F, 0.1F, 0.4F, 2.4F, new CubeDeformation(0.0F))
        		.texOffs(41, 61).addBox(7.2F, -20.0F, -0.6F, 0.1F, 0.4F, 1.6F, new CubeDeformation(0.0F)), PartPose.offset(-7.45F, 22.1F, -0.1F));

        		PartDefinition cube_r9 = MiddlePlate1.addOrReplaceChild("cube_r9", CubeListBuilder.create().texOffs(42, 62).addBox(-0.05F, -0.2F, -0.4F, 0.1F, 0.4F, 0.6F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(7.25F, -19.8F, -0.7F, -0.9599F, 0.0F, 0.0F));

        		PartDefinition cube_r10 = MiddlePlate1.addOrReplaceChild("cube_r10", CubeListBuilder.create().texOffs(42, 62).addBox(-0.05F, -0.2F, -0.2F, 0.1F, 0.4F, 0.6F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(7.25F, -19.8F, 0.9F, 0.9599F, 0.0F, 0.0F));

        		PartDefinition MiddlePlate2 = ShoulderArmor.addOrReplaceChild("MiddlePlate2", CubeListBuilder.create().texOffs(25, 60).addBox(-0.05F, -0.459F, -1.225F, 0.1F, 0.4F, 2.4F, new CubeDeformation(0.0F))
        		.texOffs(25, 60).addBox(-0.05F, -0.059F, -0.725F, 0.1F, 0.4F, 1.6F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.2F, 2.659F, 0.025F, 0.0F, 0.0F, 0.2269F));

        		PartDefinition cube_r11 = MiddlePlate2.addOrReplaceChild("cube_r11", CubeListBuilder.create().texOffs(27, 62).addBox(-0.05F, -0.2F, -0.4F, 0.1F, 0.4F, 0.6F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.141F, -0.825F, -0.9599F, 0.0F, 0.0F));

        		PartDefinition cube_r12 = MiddlePlate2.addOrReplaceChild("cube_r12", CubeListBuilder.create().texOffs(27, 62).addBox(-0.05F, -0.2F, -0.2F, 0.1F, 0.4F, 0.6F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.141F, 0.775F, 0.9599F, 0.0F, 0.0F));

        		PartDefinition MiddlePlate3 = ShoulderArmor.addOrReplaceChild("MiddlePlate3", CubeListBuilder.create().texOffs(40, 60).addBox(-0.05F, -0.459F, -1.225F, 0.1F, 0.4F, 2.4F, new CubeDeformation(0.0F))
        		.texOffs(41, 61).addBox(-0.05F, -0.059F, -0.925F, 0.1F, 1.3F, 1.8F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.2249F, 3.3733F, 0.025F, 0.0F, 0.0F, 0.0436F));

        		PartDefinition cube_r13 = MiddlePlate3.addOrReplaceChild("cube_r13", CubeListBuilder.create().texOffs(42, 62).addBox(-0.05F, -0.2F, -0.4F, 0.1F, 0.4F, 0.9F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.141F, -0.825F, -0.9599F, 0.0F, 0.0F));

        		PartDefinition cube_r14 = MiddlePlate3.addOrReplaceChild("cube_r14", CubeListBuilder.create().texOffs(42, 62).addBox(-0.05F, -0.2F, -0.4F, 0.1F, 0.4F, 0.8F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.141F, 0.775F, 0.9599F, 0.0F, 0.0F));

        		PartDefinition Vambrace = ShoulderArmor.addOrReplaceChild("Vambrace", CubeListBuilder.create().texOffs(46, 59).addBox(-4.5F, 4.6F, -2.25F, 4.3F, 0.5F, 4.25F, new CubeDeformation(0.0F))
        		.texOffs(46, 58).addBox(-4.6F, 5.1F, -2.35F, 4.5F, 1.5F, 4.45F, new CubeDeformation(0.0F))
        		.texOffs(45, 38).addBox(-4.5F, 6.8F, -2.45F, 4.7F, 2.0F, 4.65F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        		PartDefinition cube_r15 = Vambrace.addOrReplaceChild("cube_r15", CubeListBuilder.create().texOffs(44, 46).addBox(-2.125F, -0.25F, -2.125F, 4.35F, 0.5F, 4.25F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-2.272F, 8.8229F, -0.125F, 0.0F, 0.0F, 0.0873F));

        		PartDefinition cube_r16 = Vambrace.addOrReplaceChild("cube_r16", CubeListBuilder.create().texOffs(45, 58).addBox(-2.35F, -0.75F, -2.325F, 4.7F, 1.5F, 4.65F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-2.25F, 6.45F, -0.125F, 0.0F, 0.0F, -0.1309F));

        		PartDefinition Extra = Vambrace.addOrReplaceChild("Extra", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

        		PartDefinition cube_r17 = Extra.addOrReplaceChild("cube_r17", CubeListBuilder.create().texOffs(51, 46).addBox(-0.075F, -1.0F, -1.0F, 0.15F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0715F, 5.1306F, 0.0F, 0.7854F, 0.0F, -0.0873F));

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
