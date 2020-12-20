package online.kingdomkeys.kingdomkeys.client.model.entity;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;

import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.entity.Entity;
import online.kingdomkeys.kingdomkeys.entity.EntityHelper;

/**
 * HeartlessLargeBody - WYND Created using Tabula 7.0.0
 */
public class LargeBodyModel<T extends Entity> extends EntityModel<T> {
    public ModelRenderer body;
    public ModelRenderer rightLeg1;
    public ModelRenderer leftLeg1;
    public ModelRenderer rightArm1;
    public ModelRenderer leftArm1;
    public ModelRenderer head;
    public ModelRenderer neck1;
    public ModelRenderer neck2;
    public ModelRenderer neck3;
    public ModelRenderer neck4;
    public ModelRenderer body1;
    public ModelRenderer body2;
    public ModelRenderer body3;
    public ModelRenderer body4;
    public ModelRenderer body5;
    public ModelRenderer body6;
    public ModelRenderer body7;
    public ModelRenderer rightLeg2;
    public ModelRenderer rightLeg3;
    public ModelRenderer leftLeg2;
    public ModelRenderer leftLeg3;
    public ModelRenderer rightArm2;
    public ModelRenderer rightArm3;
    public ModelRenderer leftArm2;
    public ModelRenderer leftArm3;
    public ModelRenderer hat;
    public ModelRenderer hat1;
    public ModelRenderer hat2;
    public ModelRenderer hat3;
    public ModelRenderer hat4;
    public ModelRenderer hat5;
    public ModelRenderer hat6;

	private int cycleIndex; 
	private double totalDistance;
    private double[] chargeFlailArmsAnimation = new double[] 
    		{1.65, 1.60, 1.57, 1.50, 1.57, 1.60, 1.65};
    private double[] legsMovementAnimation = new double[] 
    		{-22, -18, -14, -10, -6, -2, 2, 6, 10, 14, 18, 22, 18, 14, 10, 6, 2, -2, -6, -10, -14, -18, -22};
    private double[] afterAttackAnimation = new double[] 
    		{-180, -182, -184, -186, -188, -190, -188, -186, -184, -182, -180};
    private double[] mowdownAttackAnimation = new double[] 
    		{50, 40, 30, 20, 10, 0, -10, -20, -30, -40, -50, -50, -40, -30, -20, -10, 0, 10, 20, 30, 40, 50};
    
    public LargeBodyModel() {
        this.textureWidth = 128;
        this.textureHeight = 128;
        this.rightArm1 = new ModelRenderer(this, 65, 91);
        this.rightArm1.setRotationPoint(-10.0F, -6.0F, -0.5F);
        this.rightArm1.addBox(-3.1F, 0.0F, -1.5F, 4, 6, 4, 0.0F);
        this.leftArm3 = new ModelRenderer(this, 92, 92);
        this.leftArm3.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.leftArm3.addBox(-3.0F, 15.0F, -2.0F, 4, 2, 5, 0.0F);
        this.leftLeg3 = new ModelRenderer(this, 0, 62);
        this.leftLeg3.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.leftLeg3.addBox(-1.5F, 3.0F, -5.5F, 3, 1, 2, 0.0F);
        this.hat = new ModelRenderer(this, 38, 32);
        this.hat.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.hat.addBox(-3.5F, -6.0F, -3.5F, 7, 1, 7, 0.0F);
        this.body5 = new ModelRenderer(this, 67, 50);
        this.body5.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.body5.addBox(-9.5F, -9.0F, -9.5F, 17, 17, 1, 0.0F);
        this.rightLeg2 = new ModelRenderer(this, 0, 52);
        this.rightLeg2.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.rightLeg2.addBox(-1.5F, 4.0F, -5.5F, 3, 2, 7, 0.0F);
        this.rightLeg1 = new ModelRenderer(this, 0, 42);
        this.rightLeg1.setRotationPoint(-5.0F, 18.0F, 0.0F);
        this.rightLeg1.addBox(-1.5F, 0.0F, -1.5F, 3, 4, 3, 0.0F);
        this.setRotation(rightLeg1, 0.0F, 0.22689280275926282F, 0.0F);
        this.neck2 = new ModelRenderer(this, 26, 50);
        this.neck2.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.neck2.addBox(-5.0F, -2.9F, -3.5F, 0, 2, 7, 0.0F);
        this.hat2 = new ModelRenderer(this, 21, 66);
        this.hat2.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.hat2.addBox(-1.5F, -8.0F, -1.5F, 3, 1, 3, 0.0F);
        this.body7 = new ModelRenderer(this, 67, 70);
        this.body7.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.body7.addBox(-9.0F, -8.0F, -10.5F, 16, 16, 1, 0.0F);
        this.rightLeg3 = new ModelRenderer(this, 0, 62);
        this.rightLeg3.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.rightLeg3.addBox(-1.5F, 3.0F, -5.5F, 3, 1, 2, 0.0F);
        this.neck4 = new ModelRenderer(this, 26, 50);
        this.neck4.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.neck4.addBox(-5.0F, -2.9F, 3.5F, 8, 2, 0, 0.0F);
        this.hat6 = new ModelRenderer(this, 21, 73);
        this.hat6.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.hat6.addBox(-9.4F, 6.7F, -0.5F, 1, 3, 1, 0.0F);
        this.setRotation(hat6, 0.0F, 0.0F, 1.5707963267948966F);
        this.hat3 = new ModelRenderer(this, 21, 73);
        this.hat3.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.hat3.addBox(-0.5F, -10.0F, -0.5F, 1, 2, 1, 0.0F);
        this.head = new ModelRenderer(this, 21, 36);
        this.head.setRotationPoint(-1.0F, -0.7F, 0.0F);
        this.head.addBox(-2.5F, -5.0F, -2.5F, 5, 5, 5, 0.0F);
        this.leftLeg2 = new ModelRenderer(this, 0, 52);
        this.leftLeg2.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.leftLeg2.addBox(-1.5F, 4.0F, -5.5F, 3, 2, 7, 0.0F);
        this.neck3 = new ModelRenderer(this, 26, 50);
        this.neck3.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.neck3.addBox(-5.0F, -2.9F, -3.5F, 8, 2, 0, 0.0F);
        this.rightArm2 = new ModelRenderer(this, 65, 103);
        this.rightArm2.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.rightArm2.addBox(-3.5F, 6.0F, -2.5F, 5, 9, 6, 0.0F);
        this.body1 = new ModelRenderer(this, 65, 0);
        this.body1.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.body1.addBox(-10.7F, -9.5F, -8.0F, 1, 17, 12, 0.0F);
        this.neck1 = new ModelRenderer(this, 26, 50);
        this.neck1.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.neck1.addBox(3.0F, -2.9F, -3.5F, 0, 2, 7, 0.0F);
        this.leftArm2 = new ModelRenderer(this, 65, 103);
        this.leftArm2.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.leftArm2.addBox(-3.5F, 6.0F, -2.5F, 5, 9, 6, 0.0F);
        this.body2 = new ModelRenderer(this, 65, 0);
        this.body2.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.body2.addBox(7.7F, -9.5F, -8.0F, 1, 17, 12, 0.0F);
        this.body3 = new ModelRenderer(this, 65, 33);
        this.body3.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.body3.addBox(-9.5F, -10.7F, -8.0F, 17, 1, 12, 0.0F);
        this.body6 = new ModelRenderer(this, 67, 50);
        this.body6.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.body6.addBox(-9.5F, -9.5F, 4.2F, 17, 17, 1, 0.0F);
        this.hat4 = new ModelRenderer(this, 21, 73);
        this.hat4.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.hat4.addBox(6.4F, -8.7F, -0.5F, 1, 2, 1, 0.0F);
        this.setRotation(hat4, 0.0F, 0.0F, -0.7853981633974483F);
        this.leftArm1 = new ModelRenderer(this, 65, 91);
        this.leftArm1.setRotationPoint(8.0F, -6.0F, -0.2F);
        this.leftArm1.addBox(-3.0F, 0.0F, -1.5F, 4, 6, 4, 0.0F);
        this.setRotation(leftArm1, 0.0F, 3.141592653589793F, 0.0F);
        this.body = new ModelRenderer(this, 0, 0);
        this.body.setRotationPoint(0.0F, 9.8F, 0.4F);
        this.body.addBox(-10.0F, -10.0F, -8.5F, 18, 18, 13, 0.0F);
        this.hat5 = new ModelRenderer(this, 21, 73);
        this.hat5.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.hat5.addBox(-9.7F, -8.4F, -0.5F, 1, 2, 1, 0.0F);
        this.setRotation(hat5, 0.0F, 0.0F, 1.5707963267948966F);
        this.rightArm3 = new ModelRenderer(this, 92, 92);
        this.rightArm3.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.rightArm3.addBox(-3.0F, 15.0F, -2.0F, 4, 2, 5, 0.0F);
        this.hat1 = new ModelRenderer(this, 45, 48);
        this.hat1.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.hat1.addBox(-2.5F, -7.0F, -2.5F, 5, 1, 5, 0.0F);
        this.body4 = new ModelRenderer(this, 65, 33);
        this.body4.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.body4.addBox(-9.5F, 7.7F, -8.0F, 17, 1, 12, 0.0F);
        this.leftLeg1 = new ModelRenderer(this, 0, 42);
        this.leftLeg1.setRotationPoint(3.5F, 18.0F, 0.0F);
        this.leftLeg1.addBox(-1.5F, 0.0F, -1.5F, 3, 4, 3, 0.0F);
        this.setRotation(leftLeg1, 0.0F, -0.22689280275926282F, 0.0F);
        this.body.addChild(this.rightArm1);
        this.leftArm2.addChild(this.leftArm3);
        this.leftLeg2.addChild(this.leftLeg3);
        this.head.addChild(this.hat);
        this.body.addChild(this.body5);
        this.rightLeg1.addChild(this.rightLeg2);
        this.hat1.addChild(this.hat2);
        this.body.addChild(this.body7);
        this.rightLeg2.addChild(this.rightLeg3);
        this.hat5.addChild(this.hat6);
        this.hat2.addChild(this.hat3);
        this.leftLeg1.addChild(this.leftLeg2);
        this.rightArm1.addChild(this.rightArm2);
        this.body.addChild(this.body1);
        this.leftArm1.addChild(this.leftArm2);
        this.body.addChild(this.body2);
        this.body.addChild(this.body3);
        this.body.addChild(this.body6);
        this.hat3.addChild(this.hat4);
        this.body.addChild(this.leftArm1);
        this.hat4.addChild(this.hat5);
        this.rightArm2.addChild(this.rightArm3);
        this.hat.addChild(this.hat1);
        this.body.addChild(this.body4);
    }

	public void render(MatrixStack matrixStackIn, IVertexBuilder bufferIn, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha) { 
		 this.neck3.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn);
		 this.neck2.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn);
		 this.body.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn);
		 this.rightLeg1.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn);
		 this.leftLeg1.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn);
		 this.neck1.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn);
		 this.head.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn);
		 this.neck4.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn);
    }

	public void setRotationAngles(T ent, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
    	totalDistance += ent.getDistanceSq(ent.prevPosX, ent.prevPosY, ent.prevPosZ);
    	
    	if(ent.getDistanceSq(ent.prevPosX, ent.prevPosY, ent.prevPosZ) > 0) {
	    	cycleIndex = (int) ((totalDistance * 4) % this.legsMovementAnimation.length);
	    	this.leftLeg1.rotateAngleX = degToRad(legsMovementAnimation[cycleIndex]);
	    	this.rightLeg1.rotateAngleX = -degToRad(legsMovementAnimation[cycleIndex]);
    	}
    	else {
    		this.leftLeg1.rotateAngleX = this.rightLeg1.rotateAngleX = 0;
    	}
    	
    	//System.out.println(EntityHelper.getState(ent));
    	
    	if(EntityHelper.getState(ent) == 0) {
    		this.body.rotateAngleY = 0;
    		this.leftArm1.rotateAngleX = this.rightArm1.rotateAngleX = 0;
    		this.leftLeg1.rotateAngleY = this.degToRad(-13);
    		this.rightLeg1.rotateAngleY = this.degToRad(13);
    		
    		this.rightArm1.rotateAngleX = this.rightArm1.rotateAngleY = this.rightArm1.rotateAngleZ = 0;
    	}
    	else if(EntityHelper.getState(ent) == 1) {
    		cycleIndex = (int) (ent.ticksExisted % chargeFlailArmsAnimation.length);
    		this.leftArm1.rotateAngleX  = (float) -chargeFlailArmsAnimation[cycleIndex];
    		this.rightArm1.rotateAngleX  = (float) chargeFlailArmsAnimation[cycleIndex];
    	} 	
    	else if(EntityHelper.getState(ent) == 2) {
    		this.leftArm1.rotateAngleZ  = degToRad(-60);
    		this.rightArm1.rotateAngleZ  = degToRad(60);
    		cycleIndex = (int) ((ent.ticksExisted * 1.4) % mowdownAttackAnimation.length);
    		this.body.rotateAngleY = degToRad(mowdownAttackAnimation[cycleIndex]);
    	} 
    	else if(EntityHelper.getState(ent) == 10) {
    		this.leftArm1.rotateAngleZ = this.rightArm1.rotateAngleZ = 0;
    		this.leftArm1.rotateAngleX = this.rightArm1.rotateAngleX = 0;
    		
    		cycleIndex = (int) (ent.ticksExisted % afterAttackAnimation.length);
    		this.rightArm1.rotateAngleX = degToRad((float) afterAttackAnimation[cycleIndex]);
    		this.rightArm1.rotateAngleY = degToRad(-26);
    		this.rightArm1.rotateAngleZ = degToRad(18);
    	}
	}
    
    protected float degToRad(double degrees)
    {
        return (float) (degrees * (double)Math.PI / 180) ;
    }
    
    public void setRotation(ModelRenderer modelRenderer, float x, float y, float z) {
        modelRenderer.rotateAngleX = x;
        modelRenderer.rotateAngleY = y;
        modelRenderer.rotateAngleZ = z;
    }

}

