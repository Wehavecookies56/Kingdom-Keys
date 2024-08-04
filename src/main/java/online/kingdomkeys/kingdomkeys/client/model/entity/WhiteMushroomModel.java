package online.kingdomkeys.kingdomkeys.client.model.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.client.ClientUtils.Angle;
import online.kingdomkeys.kingdomkeys.client.ClientUtils.ModelAnimation;
import online.kingdomkeys.kingdomkeys.entity.EntityHelper;

import java.util.ArrayList;
import java.util.List;

//TODO port new model
@OnlyIn(Dist.CLIENT)
public class WhiteMushroomModel<T extends LivingEntity> extends EntityModel<T> {
    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation(KingdomKeys.MODID, "white_mushroom"), "main");
	private final ModelPart main;
	private final ModelPart body;
	private final ModelPart head;
	private final ModelPart rightarm, rightarm2;
	private final ModelPart leftarm, leftarm2;

	public WhiteMushroomModel(ModelPart root) {
		this.main = root.getChild("main");
		this.body = main.getChild("body");
		this.head = main.getChild("head");
		this.rightarm = main.getChild("rightarm");
		this.leftarm = main.getChild("leftarm");
		this.rightarm2 = rightarm.getChild("rightarm2_r1");
		this.leftarm2 = leftarm.getChild("leftarm2_r1");

	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition main = partdefinition.addOrReplaceChild("main", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));

		PartDefinition body = main.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 32).addBox(-9.0F, -18.0F, 1.0F, 10.0F, 14.0F, 8.0F, new CubeDeformation(0.0F))
				.texOffs(34, 22).addBox(-10.0F, -6.0F, 0.0F, 12.0F, 3.0F, 10.0F, new CubeDeformation(0.0F))
				.texOffs(0, 0).addBox(-11.0F, -3.0F, -1.0F, 14.0F, 3.0F, 12.0F, new CubeDeformation(0.0F)), PartPose.offset(4.0F, 0.0F, -5.0F));

		PartDefinition head = main.addOrReplaceChild("head", CubeListBuilder.create().texOffs(36, 35).addBox(-4.0F, -7.0F, -4.0F, 8.0F, 7.0F, 8.0F, new CubeDeformation(0.0F))
				.texOffs(0, 15).addBox(-6.0F, -11.0F, -5.0F, 12.0F, 6.0F, 10.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -18.0F, 0.0F));

		PartDefinition rightarm = main.addOrReplaceChild("rightarm", CubeListBuilder.create(), PartPose.offset(-5.0F, -18.0F, -1.0F));

		PartDefinition rightarm2_r1 = rightarm.addOrReplaceChild("rightarm2_r1", CubeListBuilder.create().texOffs(40, 0).addBox(-2.0F, -5.0F, -1.0F, 2.0F, 5.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 5.0F, 1.0F, 2.9671F, 0.0F, 0.0F));

		PartDefinition rightarm1_r1 = rightarm.addOrReplaceChild("rightarm1_r1", CubeListBuilder.create().texOffs(40, 0).addBox(-2.0F, -3.0F, -1.0F, 2.0F, 5.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 2.0F, 1.0F, 3.1416F, 0.0F, 0.0F));

		PartDefinition leftarm = main.addOrReplaceChild("leftarm", CubeListBuilder.create(), PartPose.offset(5.0F, -18.0F, -1.0F));

		PartDefinition leftarm2_r1 = leftarm.addOrReplaceChild("leftarm2_r1", CubeListBuilder.create().texOffs(0, 0).addBox(0.0F, -5.0F, -1.0F, 2.0F, 5.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 5.0F, 1.0F, 2.9671F, 0.0F, 0.0F));

		PartDefinition leftarm1_r1 = leftarm.addOrReplaceChild("leftarm1_r1", CubeListBuilder.create().texOffs(0, 0).addBox(0.0F, -3.0F, -1.0F, 2.0F, 5.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 2.0F, 1.0F, 3.1416F, 0.0F, 0.0F));

		return LayerDefinition.create(meshdefinition, 128, 128);
	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		main.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}

	@Override
	public void setupAnim(T entity, float pLimbSwing, float pLimbSwingAmount, float pAgeInTicks, float pNetHeadYaw, float pHeadPitch) {
		if(!Minecraft.getInstance().isPaused()) {
			switch(EntityHelper.getState(entity)) {
				case 0 -> {//Normal, no charade
					System.out.println("Normal arms");
					this.main.xRot = (float) Math.toRadians(0);
					this.head.xRot = (float) Math.toRadians(0);

					this.rightarm.xRot = (float) Math.toRadians(0);
					this.leftarm.xRot = (float) Math.toRadians(0);
					rightarm.zRot = (float) Math.toRadians(0);
					leftarm.zRot = (float) Math.toRadians(0);
					rightarm2.xRot = (float) Math.toRadians(180);
					leftarm2.xRot = (float) Math.toRadians(180);
				}
				case 1 -> {
					System.out.println("Waiting for fire");
					this.main.xRot = (float) Math.toRadians(10);
					this.head.xRot = (float) Math.toRadians(20);
					this.rightarm.xRot = (float) Math.toRadians(-30);
					this.leftarm.xRot = (float) Math.toRadians(-30);

					this.rightarm.yRot = (float) Math.toRadians(-40);
					this.leftarm.yRot = (float) Math.toRadians(40);

					rightarm2.xRot = (float) Math.toRadians(90);
					leftarm2.xRot = (float) Math.toRadians(90);
				}
				case 2 -> {
					System.out.println("Waiting for blizzard");
					this.main.xRot = (float) Math.toRadians(-20);
					this.head.xRot = (float) Math.toRadians(0);

					this.rightarm.xRot = (float) Math.toRadians(-90);
					this.leftarm.xRot = (float) Math.toRadians(-90);

					this.rightarm.yRot = (float) Math.toRadians(0);
					this.leftarm.yRot = (float) Math.toRadians(0);

					rightarm2.xRot = (float) Math.toRadians(90);
					leftarm2.xRot = (float) Math.toRadians(90);
					rightarm.zRot = (float) Math.toRadians(15);
					leftarm.zRot = (float) Math.toRadians(-15);
				}
				case 3 -> {
					System.out.println("Waiting for thunder");
					this.main.xRot = (float) Math.toRadians(15);
					this.head.xRot = (float) Math.toRadians(40);

					this.rightarm.xRot = (float) Math.toRadians(-20);
					this.leftarm.xRot = (float) Math.toRadians(-20);

					rightarm2.xRot = (float) Math.toRadians(180);
					leftarm2.xRot = (float) Math.toRadians(180);

				}
			}
		}

	}
}
