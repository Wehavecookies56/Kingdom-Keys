package online.kingdomkeys.kingdomkeys.client.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.world.entity.Entity;
import online.kingdomkeys.kingdomkeys.KingdomKeys;

public class MineModel<T extends Entity> extends EntityModel<T> {
	// This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(KingdomKeys.rl("mine"), "main");
	private final ModelPart bb_main;

	public MineModel(ModelPart root) {
		this.bb_main = root.getChild("bb_main");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition bb_main = partdefinition.addOrReplaceChild("bb_main", CubeListBuilder.create().texOffs(0, 7).addBox(-2.0F, -1.0F, 3.0F, 4.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(0, 0).addBox(-3.0F, -0.5F, -3.0F, 6.0F, 1.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 1.5F, 0.0F));

		PartDefinition Ring4_r1 = bb_main.addOrReplaceChild("Ring4_r1", CubeListBuilder.create().texOffs(0, 7).addBox(-2.0F, -1.0F, 3.0F, 4.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -3.1416F, 0.7854F, 3.1416F));

		PartDefinition Ring5_r1 = bb_main.addOrReplaceChild("Ring5_r1", CubeListBuilder.create().texOffs(0, 7).addBox(-2.0F, -1.0F, 3.0F, 4.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -3.1416F, 0.0F, 3.1416F));

		PartDefinition Ring6_r1 = bb_main.addOrReplaceChild("Ring6_r1", CubeListBuilder.create().texOffs(0, 7).addBox(-2.0F, -1.0F, 3.0F, 4.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -3.1416F, -0.7854F, 3.1416F));

		PartDefinition Ring8_r1 = bb_main.addOrReplaceChild("Ring8_r1", CubeListBuilder.create().texOffs(0, 7).addBox(-2.0F, -1.0F, 3.0F, 4.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 1.5708F, 0.0F));

		PartDefinition Ring7_r1 = bb_main.addOrReplaceChild("Ring7_r1", CubeListBuilder.create().texOffs(0, 7).addBox(-2.0F, -1.0F, 3.0F, 4.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, -1.5708F, 0.0F));

		PartDefinition Ring3_r1 = bb_main.addOrReplaceChild("Ring3_r1", CubeListBuilder.create().texOffs(0, 7).addBox(-2.0F, -1.0F, 3.0F, 4.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, -0.7854F, 0.0F));

		PartDefinition Ring2_r1 = bb_main.addOrReplaceChild("Ring2_r1", CubeListBuilder.create().texOffs(0, 7).addBox(-2.0F, -1.0F, 3.0F, 4.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.7854F, 0.0F));

		return LayerDefinition.create(meshdefinition, 32, 32);
	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, int color) {
		bb_main.render(poseStack, vertexConsumer, packedLight, packedOverlay);
	}

	@Override
	public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {

	}
}