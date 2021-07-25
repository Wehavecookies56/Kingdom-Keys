package online.kingdomkeys.kingdomkeys.client.model.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.world.entity.Entity;

public class ShadowModel<T extends Entity> extends EntityModel<T> {
	public ModelPart UpperBody;
	public ModelPart LowerBody;
	public ModelPart LowerHead;
	public ModelPart Head;
	public ModelPart LegLeft1;
	public ModelPart LegLeft3;
	public ModelPart LegLeft2;
	public ModelPart LegRight1;
	public ModelPart LegRight3;
	public ModelPart LegRight2;
	public ModelPart ArmLeft1;
	public ModelPart ArmLeft2;
	public ModelPart ArmRight1;
	public ModelPart ArmRight2;
	public ModelPart AntenaLeft1;
	public ModelPart AntenaLeft2;
	public ModelPart AntenaRight1;
	public ModelPart AntenaRight2;

	// Default variables for every model
	protected double distanceMovedTotal = 0.0D;
	protected double CYCLES_PER_BLOCK;
	protected int cycleIndex = 0;
	private int[][] ticksForWalkingAnimation = new int[][] { { 0, -90 }, { 100, 2 }, { 51, -51 } };

	// Walking animation
	protected double[][] animationWalk = new double[][] { { 0, 100, 51, -90, 2, -46 }, { -40, 51, 0, -40, 51, 0 }, { -90, 2, -46, 0, 100, 51 }, { -90, 2, -46, 0, 100, 51 }, { -40, 51, 0, -40, 51, 0 }, { 0, 100, -51, -90, 2, -46 }, };

	public ShadowModel(double cycles) {
		this.CYCLES_PER_BLOCK = cycles;
		texWidth = 32;
		texHeight = 32;

		UpperBody = new ModelPart(this, 0, 0);
		UpperBody.addBox(-1.5F, -4.5F, -1.8F, 3, 5, 4);
		UpperBody.setPos(0F, 19F, 2F);
		UpperBody.setTexSize(32, 32);
		UpperBody.mirror = true;
		setRotation(UpperBody, 0.7063936F, 0F, 0F);

		LowerBody = new ModelPart(this, 0, 0);
		LowerBody.addBox(-2F, -1F, -2F, 4, 3, 4);
		LowerBody.setPos(0F, 19F, 2F);
		LowerBody.setTexSize(32, 32);
		LowerBody.mirror = true;
		setRotation(LowerBody, 0F, 0F, 0F);

		LowerHead = new ModelPart(this, 0, 0);
		LowerHead.addBox(-1.5F, -5.066667F, -4.4F, 3, 3, 3);
		LowerHead.setPos(0F, 19F, 2F);
		LowerHead.setTexSize(32, 32);
		LowerHead.mirror = true;
		setRotation(LowerHead, 0F, 0F, 0F);

		Head = new ModelPart(this, 0, 10);
		Head.addBox(-2.5F, -5.5F, -9F, 5, 5, 5);
		Head.setPos(0F, 19F, 2F);
		Head.setTexSize(32, 32);
		Head.mirror = true;
		setRotation(Head, 0F, 0F, 0F);

		LegLeft1 = new ModelPart(this, 0, 0);
		LegLeft1.addBox(2F, 0.06666667F, 0.5F, 1, 3, 2);
		LegLeft1.setPos(0F, 19F, 2F);
		LegLeft1.setTexSize(32, 32);
		LegLeft1.mirror = true;
		setRotation(LegLeft1, -0.7063936F, 0F, 0F);

		LegLeft3 = new ModelPart(this, 0, 0);
		LegLeft3.addBox(1.5F, 4F, -3.5F, 2, 1, 5);
		LegLeft3.setPos(0F, 19F, 2F);
		LegLeft3.setTexSize(32, 32);
		LegLeft3.mirror = true;
		setRotation(LegLeft3, 0F, 0F, 0F);

		LegLeft2 = new ModelPart(this, 0, 0);
		LegLeft2.addBox(2F, 1.533333F, -3.066667F, 1, 2, 1);
		LegLeft2.setPos(0F, 19F, 2F);
		LegLeft2.setTexSize(32, 32);
		LegLeft2.mirror = true;
		setRotation(LegLeft2, 0.8922867F, 0F, 0F);

		LegRight1 = new ModelPart(this, 0, 0);
		LegRight1.addBox(-3F, 0.06666667F, 0.5F, 1, 3, 2);
		LegRight1.setPos(0F, 19F, 2F);
		LegRight1.setTexSize(32, 32);
		LegRight1.mirror = true;
		setRotation(LegRight1, -0.7063936F, 0F, 0F);

		LegRight3 = new ModelPart(this, 0, 0);
		LegRight3.addBox(-3.5F, 4F, -3.5F, 2, 1, 5);
		LegRight3.setPos(0F, 19F, 2F);
		LegRight3.setTexSize(32, 32);
		LegRight3.mirror = true;
		setRotation(LegRight3, 0F, 0F, 0F);

		LegRight2 = new ModelPart(this, 0, 0);
		LegRight2.addBox(-3F, 1.533333F, -3.066667F, 1, 2, 1);
		LegRight2.setPos(0F, 19F, 2F);
		LegRight2.setTexSize(32, 32);
		LegRight2.mirror = true;
		setRotation(LegRight2, 0.8922867F, 0F, 0F);

		ArmLeft1 = new ModelPart(this, 0, 0);
		ArmLeft1.addBox(1F, -1.466667F, -2.266667F, 2, 2, 1);
		ArmLeft1.setPos(0F, 19F, 2F);
		ArmLeft1.setTexSize(32, 32);
		ArmLeft1.mirror = true;
		setRotation(ArmLeft1, -0.4089647F, 0F, 0F);

		ArmLeft2 = new ModelPart(this, 0, 0);
		ArmLeft2.addBox(2F, -1.4F, -4.333333F, 1, 1, 3);
		ArmLeft2.setPos(0F, 19F, 2F);
		ArmLeft2.setTexSize(32, 32);
		ArmLeft2.mirror = true;
		setRotation(ArmLeft2, 0.4461433F, 0F, 0F);

		ArmRight1 = new ModelPart(this, 0, 0);
		ArmRight1.addBox(-3F, -1.466667F, -2.266667F, 2, 2, 1);
		ArmRight1.setPos(0F, 19F, 2F);
		ArmRight1.setTexSize(32, 32);
		ArmRight1.mirror = true;
		setRotation(ArmRight1, -0.4089647F, 0F, 0F);

		ArmRight2 = new ModelPart(this, 0, 0);
		ArmRight2.addBox(-3F, -1.4F, -4.333333F, 1, 1, 3);
		ArmRight2.setPos(0F, 19F, 2F);
		ArmRight2.setTexSize(32, 32);
		ArmRight2.mirror = true;
		setRotation(ArmRight2, 0.4461433F, 0F, 0F);

		AntenaLeft1 = new ModelPart(this, 0, 0);
		AntenaLeft1.addBox(1F, -6.5F, -8F, 1, 2, 1);
		AntenaLeft1.setPos(0F, 19F, 2F);
		AntenaLeft1.setTexSize(32, 32);
		AntenaLeft1.mirror = true;
		setRotation(AntenaLeft1, 0F, 0F, 0F);

		AntenaLeft2 = new ModelPart(this, 0, 0);
		AntenaLeft2.addBox(1F, -7.5F, -10F, 1, 1, 3);
		AntenaLeft2.setPos(0F, 19F, 2F);
		AntenaLeft2.setTexSize(32, 32);
		AntenaLeft2.mirror = true;
		setRotation(AntenaLeft2, 0F, 0F, 0F);

		AntenaRight1 = new ModelPart(this, 0, 0);
		AntenaRight1.addBox(-2F, -6.5F, -8F, 1, 2, 1);
		AntenaRight1.setPos(0F, 19F, 2F);
		AntenaRight1.setTexSize(32, 32);
		AntenaRight1.mirror = true;
		setRotation(AntenaRight1, 0F, 0F, 0F);

		AntenaRight2 = new ModelPart(this, 0, 0);
		AntenaRight2.addBox(-2F, -7.5F, -10F, 1, 1, 3);
		AntenaRight2.setPos(0F, 19F, 2F);
		AntenaRight2.setTexSize(32, 32);
		AntenaRight2.mirror = true;
		setRotation(AntenaRight2, 0F, 0F, 0F);
	}

	public void renderToBuffer(PoseStack matrixStackIn, VertexConsumer bufferIn, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha) {
		this.UpperBody.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn);
		this.LowerBody.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn);
		this.LowerHead.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn);
		this.Head.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn);
		this.LegLeft1.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn);
		this.LegLeft3.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn);
		this.LegLeft2.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn);
		this.LegRight1.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn);
		this.LegRight1.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn);
		this.LegRight3.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn);
		this.LegRight2.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn);
		this.ArmLeft1.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn);
		this.ArmLeft2.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn);
		this.ArmRight1.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn);
		this.ArmRight2.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn);
		this.AntenaLeft1.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn);
		this.AntenaLeft2.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn);
		this.AntenaRight1.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn);
		this.AntenaRight2.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn);
	}

	private void setRotation(ModelPart model, float x, float y, float z) {
		model.xRot = x;
		model.yRot = y;
		model.zRot = z;
	}

	public void setupAnim(T e, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		updateDistanceMovedTotal(e);
		cycleIndex = (int) ((getDistanceMovedTotal() * CYCLES_PER_BLOCK) % animationWalk.length);

		if (e.distanceToSqr(e.xo, e.yo, e.zo) > 0) {
			LegLeft1.getXRot() = degToRad(animationWalk[cycleIndex][0]);
			LegLeft2.getXRot() = degToRad(animationWalk[cycleIndex][1]);
			LegLeft3.getXRot() = degToRad(animationWalk[cycleIndex][2]);

			LegRight1.getXRot() = degToRad(animationWalk[cycleIndex][3]);
			LegRight2.getXRot() = degToRad(animationWalk[cycleIndex][4]);
			LegRight3.getXRot() = degToRad(animationWalk[cycleIndex][5]);
		} else {
			LegLeft1.getXRot() = LegRight1.getXRot() = -0.7063936F;
			LegLeft2.getXRot() = LegRight2.getXRot() = 0.8922867F;
			LegLeft3.getXRot() = LegRight3.getXRot() = 0;
		}

	}

	// Default methods/functions for every model
	protected void updateDistanceMovedTotal(Entity e) {
		distanceMovedTotal += e.distanceToSqr(e.xo, e.yo, e.zo);
	}

	protected double getDistanceMovedTotal() {
		return (distanceMovedTotal);
	}

	protected float degToRad(double degrees) {
		return (float) (degrees * (double) Math.PI / 180);
	}
}