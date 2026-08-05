package online.kingdomkeys.kingdomkeys.client.model.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.util.Mth;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.entity.mob.BaseKHEntity;

@OnlyIn(Dist.CLIENT)
public class NeoshadowModel<Type extends BaseKHEntity> extends EntityModel<Type> {

	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(KingdomKeys.rl("neoshadow"), "main");

	private static final float LIMB_SWING_SCALE = 0.7F;
	private static final float LIMB_SWING_AMOUNT = 1.2F;

	private static final float ANTENNA_SPEED = 0.07F;
	private static final float ANTENNA_SWAY = 0.12F;

	private final ModelPart Head;
	private final ModelPart Body;
	private final ModelPart ArmR;
	private final ModelPart ArmL;
	private final ModelPart LegL;
	private final ModelPart LegR;
	private final ModelPart AntennaL;
	private final ModelPart AntennaR;

	public NeoshadowModel(ModelPart root) {
		this.Head = root.getChild("Head");
		this.Body = root.getChild("Body");
		this.ArmR = root.getChild("ArmR");
		this.ArmL = root.getChild("ArmL");
		this.LegL = root.getChild("LegL");
		this.LegR = root.getChild("LegR");
		this.AntennaL = Head.getChild("AntennaL");
		this.AntennaR = Head.getChild("AntennaR");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition Head = partdefinition.addOrReplaceChild("Head", CubeListBuilder.create()
				.texOffs(0, 0).addBox(-2F, -4F, -2F, 4F, 4F, 4F, new CubeDeformation(0.0F)), PartPose.offset(0F, 12F, 1F));
		PartDefinition AntennaL = Head.addOrReplaceChild("AntennaL", CubeListBuilder.create()
				.texOffs(0, 8).addBox(-0.5F, 0F, -6F, 0F, 6F, 4F, new CubeDeformation(0.0F)), PartPose.offset(-0.5F, -4F, -0.5F));
		AntennaL.addOrReplaceChild("Antenna1_r1", CubeListBuilder.create().texOffs(0, 18).addBox(-1F, -4F, -0.5F, 1F, 5F, 1F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0F, 0F, 0F, 0.9599F, 0F, 0F));
		AntennaL.addOrReplaceChild("Antenna2_r1", CubeListBuilder.create().texOffs(0, 0).addBox(-1F, -2F, -0.5F, 1F, 3F, 1F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0F, 0F, -4F, -0.5236F, 0F, 0F));
		PartDefinition AntennaR = Head.addOrReplaceChild("AntennaR", CubeListBuilder.create()
				.texOffs(0, 8).addBox(0.5F, 0F, -6F, 0F, 6F, 4F, new CubeDeformation(0.0F)), PartPose.offset(0.5F, -4F, -0.5F));
		AntennaR.addOrReplaceChild("Antenna2_r2", CubeListBuilder.create().texOffs(0, 18).addBox(0F, -4F, -0.5F, 1F, 5F, 1F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0F, 0F, 0F, 0.9599F, 0F, 0F));
		AntennaR.addOrReplaceChild("Antenna3_r1", CubeListBuilder.create().texOffs(0, 0).addBox(0F, -2F, -0.5F, 1F, 3F, 1F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0F, 0F, -4F, -0.5236F, 0F, 0F));
		PartDefinition Body = partdefinition.addOrReplaceChild("Body", CubeListBuilder.create()
				.texOffs(0, 8).addBox(-3F, -3.8333F, -1F, 6F, 2F, 2F, new CubeDeformation(0.0F))
				.texOffs(12, 0).addBox(-2F, -1.8333F, -1F, 4F, 2F, 2F, new CubeDeformation(0.0F))
				.texOffs(16, 9).addBox(-1F, 0.1667F, -1F, 2F, 1F, 2F, new CubeDeformation(0.0F)), PartPose.offset(0F, 15.8333F, 1F));
		PartDefinition ArmR = partdefinition.addOrReplaceChild("ArmR", CubeListBuilder.create()
				.texOffs(19, 22).addBox(-0.5F, 0F, -0.5F, 1F, 3F, 1F, new CubeDeformation(0.0F))
				.texOffs(18, 12).addBox(-0.5F, 3F, -0.5F, 1F, 5F, 1F, new CubeDeformation(0.0F)), PartPose.offset(3.5F, 12.5F, 1F));
		PartDefinition ArmL = partdefinition.addOrReplaceChild("ArmL", CubeListBuilder.create()
				.texOffs(19, 22).addBox(-0.5F, 0F, -0.5F, 1F, 3F, 1F, new CubeDeformation(0.0F))
				.texOffs(0, 18).addBox(-0.5F, 3F, -0.5F, 1F, 5F, 1F, new CubeDeformation(0.0F)), PartPose.offset(-3.5F, 12.5F, 1F));
		PartDefinition LegL = partdefinition.addOrReplaceChild("LegL", CubeListBuilder.create()
				.texOffs(16, 19).addBox(-0.5F, 0F, -0.5F, 1F, 3F, 1F, new CubeDeformation(0.0F))
				.texOffs(12, 19).addBox(-0.5F, 3F, -0.5F, 1F, 3F, 1F, new CubeDeformation(0.0F))
				.texOffs(14, 5).addBox(-0.5F, 6F, -0.25F, 1F, 1F, 3F, new CubeDeformation(0.0F)), PartPose.offset(-1F, 17F, 1F));
		PartDefinition LegR = partdefinition.addOrReplaceChild("LegR", CubeListBuilder.create()
				.texOffs(16, 19).addBox(-0.5F, 0F, -0.5F, 1F, 3F, 1F, new CubeDeformation(0.0F))
				.texOffs(12, 19).addBox(-0.5F, 3F, -0.5F, 1F, 3F, 1F, new CubeDeformation(0.0F))
				.texOffs(14, 5).addBox(-0.5F, 6F, -0.25F, 1F, 1F, 3F, new CubeDeformation(0.0F)), PartPose.offset(1F, 17F, 1F));

		return LayerDefinition.create(meshdefinition, 32, 32);
	}

	@Override
	public void setupAnim(Type entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		float swing = Mth.cos(limbSwing * LIMB_SWING_SCALE) * LIMB_SWING_AMOUNT * limbSwingAmount;

		LegL.xRot = swing;
		LegR.xRot = -swing;
		ArmL.xRot = -swing * 0.6F;
		ArmR.xRot = swing * 0.6F;

		Head.yRot = netHeadYaw * ((float) Math.PI / 180F);
		Head.xRot = headPitch * ((float) Math.PI / 180F);

		float sway = Mth.sin(ageInTicks * ANTENNA_SPEED) * ANTENNA_SWAY;
		AntennaL.zRot = sway;
		AntennaR.zRot = -sway;
	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer buffer, int packedLight, int packedOverlay, int colour) {
		poseStack.pushPose();
		{
			poseStack.mulPose(Axis.YP.rotationDegrees(180));

			Head.render(poseStack, buffer, packedLight, packedOverlay);
			Body.render(poseStack, buffer, packedLight, packedOverlay);
			ArmR.render(poseStack, buffer, packedLight, packedOverlay);
			ArmL.render(poseStack, buffer, packedLight, packedOverlay);
			LegL.render(poseStack, buffer, packedLight, packedOverlay);
			LegR.render(poseStack, buffer, packedLight, packedOverlay);
		}
		poseStack.popPose();
	}
}
