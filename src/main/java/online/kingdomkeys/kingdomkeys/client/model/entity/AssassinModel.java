package online.kingdomkeys.kingdomkeys.client.model.entity;

import java.util.ArrayList;
import java.util.List;

import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;

import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import online.kingdomkeys.kingdomkeys.entity.EntityHelper;
import online.kingdomkeys.kingdomkeys.util.Utils.ModelAnimation;

/**
 * Assassin - Abelatox
 * Created using Tabula 8.0.0
 */
@OnlyIn(Dist.CLIENT)
public class AssassinModel<T extends Entity> extends EntityModel<T> {
protected double distanceMovedTotal = 0.0D;
	
	public ModelRenderer bodyBot;
    public ModelRenderer leftLegTop;
    public ModelRenderer rightLegTop;
    public ModelRenderer bodyTop;
    public ModelRenderer skirtBack;
    public ModelRenderer leftLegBot;
    public ModelRenderer leftFootTop;
    public ModelRenderer leftFootBottom;
    public ModelRenderer rightLegBot;
    public ModelRenderer rightFootTop;
    public ModelRenderer rightFootBottom;
    public ModelRenderer neck;
    public ModelRenderer leftArm;
    public ModelRenderer rightArm;
    public ModelRenderer head;
    public ModelRenderer helmet;
    public ModelRenderer face;
    public ModelRenderer horn1;
    public ModelRenderer horn2;
    public ModelRenderer leftArmSpike4;
    public ModelRenderer leftArmSpike0;
    public ModelRenderer leftArmSpike5;
    public ModelRenderer leftArmSpike1;
    public ModelRenderer leftArmSpike2;
    public ModelRenderer leftArmSpike3;
    public ModelRenderer rightArmSpike4;
    public ModelRenderer rightArmSpike0;
    public ModelRenderer rightArmSpike5;
    public ModelRenderer rightArmSpike1;
    public ModelRenderer rightArmSpike2;
    public ModelRenderer rightArmSpike3;
    public ModelRenderer skirtTop;
    
    List<ModelAnimation> animation = new ArrayList<ModelAnimation>();
	
    public AssassinModel() {
    	this.textureWidth = 64;
        this.textureHeight = 32;
        this.rightArm = new ModelRenderer(this, 22, 8);
        this.rightArm.setRotationPoint(-3.0F, 1.1F, 1.1F);
        this.rightArm.addBox(-0.5F, -1.0F, -1.0F, 2.0F, 9.0F, 2.0F, -0.1F, 0.0F, -0.1F);
        this.setRotateAngle(rightArm, -1.9198621771937625F, 0.0F, 3.141592653589793F);
        this.horn1 = new ModelRenderer(this, 35, 12);
        this.horn1.setRotationPoint(0.0F, -3.4F, 2.0F);
        this.horn1.addBox(-0.5F, -4.0F, 0.0F, 1.0F, 6.0F, 1.0F, -0.3F, 0.0F, 0.0F);
        this.setRotateAngle(horn1, -0.8726646259971648F, 0.0F, 0.0F);
        this.rightArmSpike4 = new ModelRenderer(this, 36, 8);
        this.rightArmSpike4.setRotationPoint(0.5F, 8.0F, 0.0F);
        this.rightArmSpike4.addBox(-0.5F, 0.0F, -0.5F, 1.0F, 1.0F, 1.0F, 0.1F, 0.0F, 0.1F);
        this.rightArmSpike2 = new ModelRenderer(this, 46, 17);
        this.rightArmSpike2.setRotationPoint(0.0F, 2.0F, 0.0F);
        this.rightArmSpike2.addBox(-1.6F, -1.7F, -0.5F, 8.0F, 5.0F, 1.0F, -2.2F, -1.9F, -0.49F);
        this.rightFootTop = new ModelRenderer(this, 0, 18);
        this.rightFootTop.setRotationPoint(0.0F, 1.0F, 3.0F);
        this.rightFootTop.addBox(-1.25F, 0.0F, 0.0F, 1.0F, 2.0F, 1.0F, 0.0F, 0.0F, 0.0F);
        this.setRotateAngle(rightFootTop, 0.4363323129985824F, 0.0F, 0.0F);
        this.bodyBot = new ModelRenderer(this, 0, 0);
        this.bodyBot.setRotationPoint(-2.0F, 10.4F, 0.0F);
        this.bodyBot.addBox(0.0F, 0.0F, 0.0F, 4.0F, 5.0F, 3.0F, 0.0F, 0.0F, 0.0F);
        this.rightLegBot = new ModelRenderer(this, 9, 9);
        this.rightLegBot.setRotationPoint(0.0F, 4.8F, 1.0F);
        this.rightLegBot.addBox(-1.75F, 0.0F, 0.0F, 2.0F, 1.0F, 4.0F, -0.3F, 0.0F, 0.0F);
        this.setRotateAngle(rightLegBot, 0.17453292519943295F, 0.0F, 0.0F);
        this.rightArmSpike1 = new ModelRenderer(this, 46, 24);
        this.rightArmSpike1.setRotationPoint(0.0F, 2.0F, 0.0F);
        this.rightArmSpike1.addBox(-1.6F, -1.7F, -0.5F, 8.0F, 5.0F, 1.0F, -2.2F, -1.9F, -0.49F);
        this.leftArmSpike2 = new ModelRenderer(this, 46, 17);
        this.leftArmSpike2.setRotationPoint(0.0F, 2.0F, 0.0F);
        this.leftArmSpike2.addBox(-1.6F, -1.7F, -0.5F, 8.0F, 5.0F, 1.0F, -2.2F, -1.9F, -0.49F);
        this.leftArmSpike1 = new ModelRenderer(this, 46, 24);
        this.leftArmSpike1.setRotationPoint(0.0F, 2.0F, 0.0F);
        this.leftArmSpike1.addBox(-1.6F, -1.7F, -0.5F, 8.0F, 5.0F, 1.0F, -2.2F, -1.9F, -0.49F);
        this.leftArmSpike0 = new ModelRenderer(this, 46, 17);
        this.leftArmSpike0.setRotationPoint(0.8F, 0.5F, 0.0F);
        this.leftArmSpike0.addBox(-1.6F, -1.7F, -0.5F, 8.0F, 5.0F, 1.0F, -2.2F, -1.9F, -0.49F);
        this.face = new ModelRenderer(this, 28, 20);
        this.face.setRotationPoint(1.0F, -2.2F, 0.8F);
        this.face.addBox(-2.5F, -0.5F, -0.5F, 5.0F, 9.0F, 1.0F, -1.0F, 0.0F, -0.4F);
        this.setRotateAngle(face, -2.0943951023931953F, 0.0F, 0.0F);
        this.leftArmSpike3 = new ModelRenderer(this, 46, 24);
        this.leftArmSpike3.setRotationPoint(0.0F, 2.0F, 0.0F);
        this.leftArmSpike3.addBox(-1.6F, -1.7F, -0.5F, 8.0F, 5.0F, 1.0F, -2.2F, -1.9F, -0.49F);
        this.leftArm = new ModelRenderer(this, 22, 8);
        this.leftArm.setRotationPoint(3.0F, 1.1F, 1.1F);
        this.leftArm.addBox(-0.5F, -1.0F, -1.0F, 2.0F, 9.0F, 2.0F, -0.1F, 0.0F, -0.1F);
        this.setRotateAngle(leftArm, -1.2217304763960306F, 0.0F, 0.0F);
        this.head = new ModelRenderer(this, 45, 0);
        this.head.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.head.addBox(-0.5F, -2.0F, -0.5F, 3.0F, 3.0F, 3.0F, 0.0F, 0.0F, 0.0F);
        this.setRotateAngle(head, 0.5235987755982988F, 0.0F, 0.0F);
        this.neck = new ModelRenderer(this, 35, 0);
        this.neck.setRotationPoint(-1.0F, -0.6F, -0.6F);
        this.neck.addBox(0.0F, 0.0F, 0.0F, 2.0F, 3.0F, 2.0F, 0.0F, 0.0F, 0.0F);
        this.setRotateAngle(neck, 0.7853981633974483F, 0.0F, 0.0F);
        this.horn2 = new ModelRenderer(this, 41, 13);
        this.horn2.setRotationPoint(0.0F, 0.0F, 3.5F);
        this.horn2.addBox(-0.5F, -8.0F, 0.0F, 1.0F, 9.0F, 1.0F, -0.3F, 0.0F, 0.0F);
        this.setRotateAngle(horn2, -0.2617993877991494F, 0.0F, 0.0F);
        this.bodyTop = new ModelRenderer(this, 15, 0);
        this.bodyTop.setRotationPoint(2.0F, 0.0F, -1.2F);
        this.bodyTop.addBox(-3.0F, 0.0F, 0.0F, 6.0F, 3.0F, 3.0F, 0.0F, 0.0F, 0.0F);
        this.setRotateAngle(bodyTop, 0.7853981633974483F, 0.0F, 0.0F);
        this.rightLegTop = new ModelRenderer(this, 0, 9);
        this.rightLegTop.setRotationPoint(1.6F, 5.0F, 0.0F);
        this.rightLegTop.addBox(-1.8F, -0.2F, 0.0F, 2.0F, 6.0F, 2.0F, -0.2F, 0.0F, -0.2F);
        this.setRotateAngle(rightLegTop, -0.6108652381980153F, 0.0F, 0.0F);
        this.rightArmSpike0 = new ModelRenderer(this, 46, 17);
        this.rightArmSpike0.setRotationPoint(0.8F, 0.5F, 0.0F);
        this.rightArmSpike0.addBox(-1.6F, -1.7F, -0.5F, 8.0F, 5.0F, 1.0F, -2.2F, -1.9F, -0.49F);
        this.skirtTop = new ModelRenderer(this, 11, 23);
        this.skirtTop.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.skirtTop.addBox(-2.0F, 0.0F, -3.5F, 4.0F, 4.0F, 4.0F, 0.1F, 0.0F, -0.4F);
        this.rightFootBottom = new ModelRenderer(this, 6, 15);
        this.rightFootBottom.setRotationPoint(0.0F, 0.8F, 0.0F);
        this.rightFootBottom.addBox(-1.25F, 0.5F, -6.0F, 1.0F, 1.0F, 6.0F, -0.1F, -0.3F, 0.0F);
        this.leftFootBottom = new ModelRenderer(this, 6, 15);
        this.leftFootBottom.setRotationPoint(0.0F, 0.8F, 0.0F);
        this.leftFootBottom.addBox(-1.25F, 0.5F, -6.0F, 1.0F, 1.0F, 6.0F, -0.1F, -0.3F, 0.0F);
        this.skirtBack = new ModelRenderer(this, 0, 23);
        this.skirtBack.setRotationPoint(2.0F, 3.5F, 2.9F);
        this.skirtBack.addBox(-2.0F, 0.0F, -0.4F, 4.0F, 8.0F, 1.0F, 0.1F, 0.0F, -0.4F);
        this.leftLegBot = new ModelRenderer(this, 9, 9);
        this.leftLegBot.setRotationPoint(0.0F, 4.8F, 1.0F);
        this.leftLegBot.addBox(-1.75F, 0.0F, 0.0F, 2.0F, 1.0F, 4.0F, -0.3F, 0.0F, 0.0F);
        this.setRotateAngle(leftLegBot, 0.17453292519943295F, 0.0F, 0.0F);
        this.rightArmSpike3 = new ModelRenderer(this, 46, 24);
        this.rightArmSpike3.setRotationPoint(0.0F, 2.0F, 0.0F);
        this.rightArmSpike3.addBox(-1.6F, -1.7F, -0.5F, 8.0F, 5.0F, 1.0F, -2.2F, -1.9F, -0.49F);
        this.rightArmSpike5 = new ModelRenderer(this, 36, 8);
        this.rightArmSpike5.setRotationPoint(0.0F, 1.0F, 0.0F);
        this.rightArmSpike5.addBox(-0.5F, -0.5F, -0.5F, 1.0F, 2.0F, 1.0F, -0.2F, 0.0F, -0.2F);
        this.leftLegTop = new ModelRenderer(this, 0, 9);
        this.leftLegTop.setRotationPoint(4.0F, 5.0F, 0.0F);
        this.leftLegTop.addBox(-1.8F, -0.2F, 0.0F, 2.0F, 6.0F, 2.0F, -0.2F, 0.0F, -0.2F);
        this.setRotateAngle(leftLegTop, -0.6108652381980153F, 0.0F, 0.0F);
        this.leftFootTop = new ModelRenderer(this, 0, 18);
        this.leftFootTop.setRotationPoint(0.0F, 1.0F, 3.0F);
        this.leftFootTop.addBox(-1.25F, 0.0F, 0.0F, 1.0F, 2.0F, 1.0F, 0.0F, 0.0F, 0.0F);
        this.setRotateAngle(leftFootTop, 0.4363323129985824F, 0.0F, 0.0F);
        this.helmet = new ModelRenderer(this, 47, 8);
        this.helmet.setRotationPoint(1.0F, 0.0F, 0.0F);
        this.helmet.addBox(-2.0F, -2.6F, 1.0F, 4.0F, 4.0F, 3.0F, 0.0F, 0.0F, 0.0F);
        this.leftArmSpike4 = new ModelRenderer(this, 36, 8);
        this.leftArmSpike4.setRotationPoint(0.5F, 8.0F, 0.0F);
        this.leftArmSpike4.addBox(-0.5F, 0.0F, -0.5F, 1.0F, 1.0F, 1.0F, 0.1F, 0.0F, 0.1F);
        this.leftArmSpike5 = new ModelRenderer(this, 36, 8);
        this.leftArmSpike5.setRotationPoint(0.0F, 1.0F, 0.0F);
        this.leftArmSpike5.addBox(-0.5F, -0.5F, -0.5F, 1.0F, 2.0F, 1.0F, -0.2F, 0.0F, -0.2F);
        this.bodyTop.addChild(this.rightArm);
        this.helmet.addChild(this.horn1);
        this.rightArm.addChild(this.rightArmSpike4);
        this.rightArmSpike1.addChild(this.rightArmSpike2);
        this.rightLegBot.addChild(this.rightFootTop);
        this.rightLegTop.addChild(this.rightLegBot);
        this.rightArmSpike0.addChild(this.rightArmSpike1);
        this.leftArmSpike1.addChild(this.leftArmSpike2);
        this.leftArmSpike0.addChild(this.leftArmSpike1);
        this.leftArm.addChild(this.leftArmSpike0);
        this.head.addChild(this.face);
        this.leftArmSpike2.addChild(this.leftArmSpike3);
        this.bodyTop.addChild(this.leftArm);
        this.neck.addChild(this.head);
        this.bodyTop.addChild(this.neck);
        this.helmet.addChild(this.horn2);
        this.bodyBot.addChild(this.bodyTop);
        this.bodyBot.addChild(this.rightLegTop);
        this.rightArm.addChild(this.rightArmSpike0);
        this.skirtBack.addChild(this.skirtTop);
        this.rightFootTop.addChild(this.rightFootBottom);
        this.leftFootTop.addChild(this.leftFootBottom);
        this.bodyBot.addChild(this.skirtBack);
        this.leftLegTop.addChild(this.leftLegBot);
        this.rightArmSpike2.addChild(this.rightArmSpike3);
        this.rightArmSpike4.addChild(this.rightArmSpike5);
        this.bodyBot.addChild(this.leftLegTop);
        this.leftLegBot.addChild(this.leftFootTop);
        this.head.addChild(this.helmet);
        this.leftArm.addChild(this.leftArmSpike4);
        this.leftArmSpike4.addChild(this.leftArmSpike5);
        
        ModelAnimation leftLegTopAnim = new ModelAnimation(leftLegTop, -35, -55, -15, 0, true, rightLegTop);
        //ModelAnimation rightLegTopAnim = new ModelAnimation(rightLegTop, -35, -45, -25, 0, false);
        
        animation.add(leftLegTopAnim);
        //animation.add(rightLegTopAnim);
    }

    @Override
    public void render(MatrixStack matrixStackIn, IVertexBuilder bufferIn, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha) { 
        ImmutableList.of(this.bodyBot).forEach((modelRenderer) -> { 
            modelRenderer.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
        });
    }

    @Override
	public void setRotationAngles(T ent, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		updateDistanceMovedTotal(ent);
		if (ent.getDistanceSq(ent.prevPosX, ent.prevPosY, ent.prevPosZ) > 0) {
			for(int i = 0; i < animation.size(); i++) { //iterate through the legs array
				ModelAnimation m = animation.get(i); 
				//System.out.println(i + " " + m);
				if(m != null && m.model != null) {
					if(m.increasing) { //animnation increase
						m.actVal += 2;
						if(m.actVal >= m.maxVal) {
							m.increasing = false;
						}
					} else { //Animation decrease
						m.actVal -= 2;
						if(m.actVal <= m.minVal) {
							m.increasing = true;
						}
					}
					m.model.rotateAngleX = (float) Math.toRadians(m.actVal);
					if(m.modelCounterpart != null) {
						m.modelCounterpart.rotateAngleX = (float) Math.toRadians(m.defVal*2-m.actVal);
					}
				}
			}
		
		} else {
	    	this.leftLegTop.rotateAngleX = this.rightLegTop.rotateAngleX = (float) Math.toRadians(-35);
			this.leftLegBot.rotateAngleX = this.rightLegBot.rotateAngleX = (float) Math.toRadians(10);
			this.leftFootTop.rotateAngleX = this.rightFootTop.rotateAngleX = (float) Math.toRadians(25);
		}
		
		if (EntityHelper.getState(ent) == 0) {
			//this.rightArm.rotateAngleX =  (float) Math.toRadians(0);
			this.rightArm.rotateAngleY = (float) Math.toRadians(0);
			this.rightArm.rotateAngleZ = (float) Math.toRadians(180);

			this.leftArm.rotateAngleX =  (float) Math.toRadians(270);
			this.leftArm.rotateAngleY = (float) Math.toRadians(0);
			this.leftArm.rotateAngleZ = (float) Math.toRadians(0);
			
		} else if (EntityHelper.getState(ent) == 1) {
			this.rightArm.rotateAngleX =  (float) Math.toRadians(270);
			this.rightArm.rotateAngleY = (float) Math.toRadians(90 - ent.ticksExisted*50);
			this.rightArm.rotateAngleZ = (float) Math.toRadians(90);

			this.leftArm.rotateAngleX =  (float) Math.toRadians(270);
			this.leftArm.rotateAngleY = (float) Math.toRadians(-90 - ent.ticksExisted*50);
			this.leftArm.rotateAngleZ = (float) Math.toRadians(90);
			
		} else if (EntityHelper.getState(ent) == 2) {
			this.leftLegTop.rotateAngleX = (float) Math.toRadians(-135);
			this.rightLegTop.rotateAngleX = (float) Math.toRadians(-135);
			this.leftLegBot.rotateAngleX = (float) Math.toRadians(90);
			this.rightLegBot.rotateAngleX = (float) Math.toRadians(90);
			
			this.leftArm.rotateAngleY = (float) Math.toRadians(110);
			this.rightArm.rotateAngleY = (float) Math.toRadians(110);
		}

    }
    
    protected void updateDistanceMovedTotal(Entity e) {
    	distanceMovedTotal += e.getDistanceSq(e.prevPosX, e.prevPosY, e.prevPosZ);
	}

	protected double getDistanceMovedTotal() {
		return (distanceMovedTotal);
	}



    /**
     * This is a helper function from Tabula to set the rotation of model parts
     */
    public void setRotateAngle(ModelRenderer modelRenderer, float x, float y, float z) {
        modelRenderer.rotateAngleX = x;
        modelRenderer.rotateAngleY = y;
        modelRenderer.rotateAngleZ = z;
    }
}
