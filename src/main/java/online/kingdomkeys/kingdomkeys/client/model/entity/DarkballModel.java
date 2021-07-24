package online.kingdomkeys.kingdomkeys.client.model.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.world.entity.Entity;
import online.kingdomkeys.kingdomkeys.entity.EntityHelper;

/**
 * HeartlessDarkball - WYND Created using Tabula 7.0.0
 */
public class DarkballModel<T extends Entity> extends EntityModel<T> {
	public ModelPart body1;
	public ModelPart bodyLeft1;
	public ModelPart bodyLeft2;
	public ModelPart bodyRight1;
	public ModelPart bodyRight2;
	public ModelPart bodyFront1;
	public ModelPart bodyFront2;
	public ModelPart bodyBack1;
	public ModelPart bodyBack2;
	public ModelPart bodyUp1;
	public ModelPart bodyUp2;
	public ModelPart bodyDown1;
	public ModelPart bodyDown2;
	public ModelPart antenna11;
	public ModelPart antenna12;
	public ModelPart antenna13;
	public ModelPart antenna14;
	public ModelPart antenna141;
	public ModelPart antenna142;
	public ModelPart antenna143;
	public ModelPart antenna21;
	public ModelPart antenna22;
	public ModelPart antenna23;
	public ModelPart antenna24;
	public ModelPart antenna242;
	public ModelPart antenna243;
	public ModelPart antenna241;
	public ModelPart antenna31;
	public ModelPart antenna32;
	public ModelPart antenna33;
	public ModelPart antenna34;
	public ModelPart antenna341;
	public ModelPart antenna342;
	public ModelPart antenna343;

	private int cycleIndex;
	private double[] chargeHeadAnimation = new double[] { -2, -6, -10, -14, -18, -14, -10, -6, -2, 2, 6, 10, 14, 18 };

	public DarkballModel() {
		this.texWidth = 64;
		this.texHeight = 64;
		this.bodyDown1 = new ModelPart(this, 0, 51);
		this.bodyDown1.setPos(0.0F, 0.0F, 0.0F);
		this.bodyDown1.addBox(-4.5F, 4.5F, -4.5F, 9, 1, 9, 0.0F);
		this.antenna242 = new ModelPart(this, 42, 0);
		this.antenna242.setPos(0.0F, 0.0F, 0.0F);
		this.antenna242.addBox(-1.4F, -7.6F, -8.1F, 1, 3, 1, 0.0F);
		this.setRotatation(antenna242, -0.136659280431156F, -0.22759093446006054F, -0.045553093477052F);
		this.antenna22 = new ModelPart(this, 0, 0);
		this.antenna22.setPos(0.0F, 0.0F, 0.0F);
		this.antenna22.addBox(-0.1F, -5.8F, -1.7F, 2, 3, 2, 0.0F);
		this.setRotatation(antenna22, -0.36425021489121656F, 0.0F, 0.0F);
		this.antenna34 = new ModelPart(this, 0, 0);
		this.antenna34.setPos(0.0F, 0.0F, 0.0F);
		this.antenna34.addBox(-0.1F, -5.8F, -7.4F, 2, 2, 2, 0.0F);
		this.setRotatation(antenna34, -0.5462880558742251F, 0.0F, 0.0F);
		this.antenna342 = new ModelPart(this, 42, 0);
		this.antenna342.setPos(0.0F, 0.0F, 0.0F);
		this.antenna342.addBox(-1.4F, -7.6F, -8.1F, 1, 3, 1, 0.0F);
		this.setRotatation(antenna342, -0.136659280431156F, -0.22759093446006054F, -0.045553093477052F);
		this.bodyUp2 = new ModelPart(this, 32, 49);
		this.bodyUp2.setPos(0.0F, 0.0F, 0.0F);
		this.bodyUp2.addBox(-4.0F, -6.0F, -4.0F, 8, 1, 8, 0.0F);
		this.antenna343 = new ModelPart(this, 42, 0);
		this.antenna343.setPos(0.0F, 0.0F, 0.0F);
		this.antenna343.addBox(-1.3F, -8.0F, -7.4F, 1, 3, 1, 0.0F);
		this.setRotatation(antenna343, -0.136659280431156F, -0.18203784098300857F, 0.18203784098300857F);
		this.body1 = new ModelPart(this, 6, 0);
		this.body1.setPos(0.0F, 7.1F, 0.0F);
		this.body1.addBox(-5.0F, -5.0F, -5.0F, 10, 10, 10, 0.0F);
		this.setRotatation(body1, 0.0F, -6.975736996017264E-16F, -2.131475193227497E-17F);
		this.antenna32 = new ModelPart(this, 0, 0);
		this.antenna32.setPos(0.0F, 0.0F, 0.0F);
		this.antenna32.addBox(-0.1F, -5.8F, -1.7F, 2, 3, 2, 0.0F);
		this.setRotatation(antenna32, -0.30490902032340933F, 0.0F, 0.0F);
		this.bodyFront2 = new ModelPart(this, 38, 22);
		this.bodyFront2.setPos(0.0F, 0.0F, 0.0F);
		this.bodyFront2.addBox(-4.0F, -4.0F, -6.0F, 8, 8, 1, 0.0F);
		this.bodyBack1 = new ModelPart(this, 0, 21);
		this.bodyBack1.setPos(0.0F, 0.0F, 0.0F);
		this.bodyBack1.addBox(-4.5F, -4.5F, 4.5F, 9, 9, 1, 0.0F);
		this.bodyBack2 = new ModelPart(this, 20, 22);
		this.bodyBack2.setPos(0.0F, 0.0F, 0.0F);
		this.bodyBack2.addBox(-4.0F, -4.0F, 5.0F, 8, 8, 1, 0.0F);
		this.antenna14 = new ModelPart(this, 0, 0);
		this.antenna14.setPos(0.0F, 0.0F, 0.0F);
		this.antenna14.addBox(-0.1F, -5.8F, -7.4F, 2, 2, 2, 0.0F);
		this.setRotatation(antenna14, -0.5462880558742251F, 0.0F, 0.0F);
		this.antenna11 = new ModelPart(this, 0, 0);
		this.antenna11.setPos(4.8999999999999995F, 2.8F, 0.8999999999999999F);
		this.antenna11.addBox(-0.7F, -3.2F, -1.5F, 3, 4, 3, 0.0F);
		this.setRotatation(antenna11, 0.0F, 1.5707963267948966F, 0.767944870877505F);
		this.antenna24 = new ModelPart(this, 0, 0);
		this.antenna24.setPos(0.0F, 0.0F, 0.0F);
		this.antenna24.addBox(-0.1F, -5.8F, -7.4F, 2, 2, 2, 0.0F);
		this.setRotatation(antenna24, -0.5462880558742251F, 0.0F, 0.0F);
		this.antenna341 = new ModelPart(this, 42, 0);
		this.antenna341.setPos(0.0F, 0.0F, 0.0F);
		this.antenna341.addBox(-2.1F, -5.6F, -7.9F, 1, 3, 1, 0.0F);
		this.setRotatation(antenna341, -0.4553564018453205F, -0.36425021489121656F, 0.0F);
		this.antenna23 = new ModelPart(this, 0, 0);
		this.antenna23.setPos(0.0F, 0.0F, 0.0F);
		this.antenna23.addBox(-0.1F, -7.1F, -4.4F, 2, 3, 2, 0.0F);
		this.setRotatation(antenna23, -0.5462880558742251F, 0.0F, 0.0F);
		this.bodyRight2 = new ModelPart(this, 20, 34);
		this.bodyRight2.setPos(0.0F, 0.0F, 0.0F);
		this.bodyRight2.addBox(-6.0F, -4.0F, -4.0F, 1, 8, 8, 0.0F);
		this.antenna33 = new ModelPart(this, 0, 0);
		this.antenna33.setPos(0.0F, 0.0F, 0.0F);
		this.antenna33.addBox(-0.1F, -7.1F, -4.4F, 2, 3, 2, 0.0F);
		this.setRotatation(antenna33, -0.5515240436302082F, 0.0F, 0.0F);
		this.antenna142 = new ModelPart(this, 42, 0);
		this.antenna142.setPos(0.0F, 0.0F, 0.0F);
		this.antenna142.addBox(-1.4F, -7.6F, -8.1F, 1, 3, 1, 0.0F);
		this.setRotatation(antenna142, -0.136659280431156F, -0.22759093446006054F, -0.045553093477052F);
		this.antenna243 = new ModelPart(this, 42, 0);
		this.antenna243.setPos(0.0F, 0.0F, 0.0F);
		this.antenna243.addBox(-1.3F, -8.0F, -7.4F, 1, 3, 1, 0.0F);
		this.setRotatation(antenna243, -0.136659280431156F, -0.18203784098300857F, 0.18203784098300857F);
		this.bodyLeft1 = new ModelPart(this, 0, 32);
		this.bodyLeft1.setPos(0.0F, 0.0F, 0.0F);
		this.bodyLeft1.addBox(4.5F, -4.5F, -4.5F, 1, 9, 9, 0.0F);
		this.antenna13 = new ModelPart(this, 0, 0);
		this.antenna13.setPos(0.0F, 0.0F, 0.0F);
		this.antenna13.addBox(-0.1F, -7.1F, -4.4F, 2, 3, 2, 0.0F);
		this.setRotatation(antenna13, -0.5462880558742251F, 0.0F, 0.0F);
		this.bodyDown2 = new ModelPart(this, 32, 49);
		this.bodyDown2.setPos(0.0F, 0.0F, 0.0F);
		this.bodyDown2.addBox(-4.0F, 5.0F, -4.0F, 8, 1, 8, 0.0F);
		this.antenna12 = new ModelPart(this, 0, 0);
		this.antenna12.setPos(0.0F, 0.0F, 0.0F);
		this.antenna12.addBox(-0.1F, -5.8F, -1.7F, 2, 3, 2, 0.0F);
		this.setRotatation(antenna12, -0.36425021489121656F, 0.0F, 0.0F);
		this.antenna141 = new ModelPart(this, 42, 0);
		this.antenna141.setPos(0.0F, 0.0F, 0.0F);
		this.antenna141.addBox(-2.1F, -5.6F, -7.9F, 1, 3, 1, 0.0F);
		this.setRotatation(antenna141, -0.4553564018453205F, -0.36425021489121656F, 0.0F);
		this.bodyFront1 = new ModelPart(this, 0, 21);
		this.bodyFront1.setPos(0.0F, 0.0F, 0.0F);
		this.bodyFront1.addBox(-4.5F, -4.5F, -5.5F, 9, 9, 1, 0.0F);
		this.antenna143 = new ModelPart(this, 42, 0);
		this.antenna143.setPos(0.0F, 0.0F, 0.0F);
		this.antenna143.addBox(-1.3F, -8.0F, -7.4F, 1, 3, 1, 0.0F);
		this.setRotatation(antenna143, -0.136659280431156F, -0.18203784098300857F, 0.18203784098300857F);
		this.bodyLeft2 = new ModelPart(this, 20, 34);
		this.bodyLeft2.setPos(0.0F, 0.0F, 0.0F);
		this.bodyLeft2.addBox(5.0F, -4.0F, -4.0F, 1, 8, 8, 0.0F);
		this.bodyRight1 = new ModelPart(this, 0, 32);
		this.bodyRight1.setPos(0.0F, 0.0F, 0.0F);
		this.bodyRight1.addBox(-5.5F, -4.5F, -4.5F, 1, 9, 9, 0.0F);
		this.bodyUp1 = new ModelPart(this, 0, 51);
		this.bodyUp1.setPos(0.0F, 0.0F, 0.0F);
		this.bodyUp1.addBox(-4.5F, -5.5F, -4.5F, 9, 1, 9, 0.0F);
		this.antenna241 = new ModelPart(this, 42, 0);
		this.antenna241.setPos(0.0F, 0.0F, 0.0F);
		this.antenna241.addBox(-2.1F, -5.6F, -7.9F, 1, 3, 1, 0.0F);
		this.setRotatation(antenna241, -0.4553564018453205F, -0.36425021489121656F, 0.0F);
		this.antenna21 = new ModelPart(this, 0, 0);
		this.antenna21.setPos(-4.800000000000001F, 2.6999999999999997F, -0.5F);
		this.antenna21.addBox(-0.7F, -3.2F, -1.5F, 3, 4, 3, 0.0F);
		this.setRotatation(antenna21, 0.0F, -1.5707963267948966F, -0.6806784082777886F);
		this.antenna31 = new ModelPart(this, 0, 0);
		this.antenna31.setPos(1.0F, 13.5F, -0.5F);
		this.antenna31.addBox(-0.7F, -3.2F, -1.5F, 3, 6, 3, 0.0F);
		this.setRotatation(antenna31, 2.7750735106709836F, 3.141592653589793F, 0.0F);
		this.body1.addChild(this.bodyDown1);
		this.antenna24.addChild(this.antenna242);
		this.antenna21.addChild(this.antenna22);
		this.antenna33.addChild(this.antenna34);
		this.antenna34.addChild(this.antenna342);
		this.body1.addChild(this.bodyUp2);
		this.antenna34.addChild(this.antenna343);
		this.antenna31.addChild(this.antenna32);
		this.body1.addChild(this.bodyFront2);
		this.body1.addChild(this.bodyBack1);
		this.body1.addChild(this.bodyBack2);
		this.antenna13.addChild(this.antenna14);
		this.antenna23.addChild(this.antenna24);
		this.antenna34.addChild(this.antenna341);
		this.antenna22.addChild(this.antenna23);
		this.body1.addChild(this.bodyRight2);
		this.antenna32.addChild(this.antenna33);
		this.antenna14.addChild(this.antenna142);
		this.antenna24.addChild(this.antenna243);
		this.body1.addChild(this.bodyLeft1);
		this.antenna12.addChild(this.antenna13);
		this.body1.addChild(this.bodyDown2);
		this.antenna11.addChild(this.antenna12);
		this.antenna14.addChild(this.antenna141);
		this.body1.addChild(this.bodyFront1);
		this.antenna14.addChild(this.antenna143);
		this.body1.addChild(this.bodyLeft2);
		this.body1.addChild(this.bodyRight1);
		this.body1.addChild(this.bodyUp1);
		this.antenna24.addChild(this.antenna241);
	}

	@Override
	public void renderToBuffer(PoseStack matrixStackIn, VertexConsumer bufferIn, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha) {
		 this.body1.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn);
		 this.antenna11.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn);
		 this.antenna21.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn);
		 this.antenna31.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn);	
	}

	public void setRotatation(ModelPart modelRenderer, float x, float y, float z) {
		modelRenderer.getXRot() = x;
		modelRenderer.getYRot() = y;
		modelRenderer.zRot = z;
	}

	public void setupAnim(T ent, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {

		if (EntityHelper.getState(ent) == 0) {
			this.body1.getXRot() = 0;
			this.antenna11.getYRot() = 1.57F;
			this.antenna21.getYRot() = -1.57F;
		} else if (EntityHelper.getState(ent) == 1) {
			cycleIndex = (int) (ent.tickCount % chargeHeadAnimation.length);
			this.body1.getXRot() = degToRad(chargeHeadAnimation[cycleIndex]);
			this.antenna11.getYRot() = 0;
			this.antenna21.getYRot() = 0;
		}

		// if(EntityHelper.getState(ent) == 1)
		
	}

	protected float degToRad(double degrees) {
		return (float) (degrees * (double) Math.PI / 180);
	}

}
