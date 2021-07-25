package online.kingdomkeys.kingdomkeys.client.model.entity;

import java.util.ArrayList;
import java.util.List;

import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.world.entity.Entity;
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
	
	public ModelPart bodyBot;
    public ModelPart leftLegTop;
    public ModelPart rightLegTop;
    public ModelPart bodyTop;
    public ModelPart skirtBack;
    public ModelPart leftLegBot;
    public ModelPart leftFootTop;
    public ModelPart leftFootBottom;
    public ModelPart rightLegBot;
    public ModelPart rightFootTop;
    public ModelPart rightFootBottom;
    public ModelPart neck;
    public ModelPart leftArm;
    public ModelPart rightArm;
    public ModelPart head;
    public ModelPart helmet;
    public ModelPart face;
    public ModelPart horn1;
    public ModelPart horn2;
    public ModelPart leftArmSpike4;
    public ModelPart leftArmSpike0;
    public ModelPart leftArmSpike5;
    public ModelPart leftArmSpike1;
    public ModelPart leftArmSpike2;
    public ModelPart leftArmSpike3;
    public ModelPart rightArmSpike4;
    public ModelPart rightArmSpike0;
    public ModelPart rightArmSpike5;
    public ModelPart rightArmSpike1;
    public ModelPart rightArmSpike2;
    public ModelPart rightArmSpike3;
    public ModelPart skirtTop;
    
    List<ModelAnimation> animation = new ArrayList<ModelAnimation>();
	
    public AssassinModel() {
    	this.texWidth = 64;
        this.texHeight = 32;
        this.rightArm = new ModelPart(this, 22, 8);
        this.rightArm.setPos(-3.0F, 1.1F, 1.1F);
        this.rightArm.addBox(-0.5F, -1.0F, -1.0F, 2.0F, 9.0F, 2.0F, -0.1F, 0.0F, -0.1F);
        this.setRotateAngle(rightArm, -1.9198621771937625F, 0.0F, 3.141592653589793F);
        this.horn1 = new ModelPart(this, 35, 12);
        this.horn1.setPos(0.0F, -3.4F, 2.0F);
        this.horn1.addBox(-0.5F, -4.0F, 0.0F, 1.0F, 6.0F, 1.0F, -0.3F, 0.0F, 0.0F);
        this.setRotateAngle(horn1, -0.8726646259971648F, 0.0F, 0.0F);
        this.rightArmSpike4 = new ModelPart(this, 36, 8);
        this.rightArmSpike4.setPos(0.5F, 8.0F, 0.0F);
        this.rightArmSpike4.addBox(-0.5F, 0.0F, -0.5F, 1.0F, 1.0F, 1.0F, 0.1F, 0.0F, 0.1F);
        this.rightArmSpike2 = new ModelPart(this, 46, 17);
        this.rightArmSpike2.setPos(0.0F, 2.0F, 0.0F);
        this.rightArmSpike2.addBox(-1.6F, -1.7F, -0.5F, 8.0F, 5.0F, 1.0F, -2.2F, -1.9F, -0.49F);
        this.rightFootTop = new ModelPart(this, 0, 18);
        this.rightFootTop.setPos(0.0F, 1.0F, 3.0F);
        this.rightFootTop.addBox(-1.25F, 0.0F, 0.0F, 1.0F, 2.0F, 1.0F, 0.0F, 0.0F, 0.0F);
        this.setRotateAngle(rightFootTop, 0.4363323129985824F, 0.0F, 0.0F);
        this.bodyBot = new ModelPart(this, 0, 0);
        this.bodyBot.setPos(-2.0F, 10.4F, 0.0F);
        this.bodyBot.addBox(0.0F, 0.0F, 0.0F, 4.0F, 5.0F, 3.0F, 0.0F, 0.0F, 0.0F);
        this.rightLegBot = new ModelPart(this, 9, 9);
        this.rightLegBot.setPos(0.0F, 4.8F, 1.0F);
        this.rightLegBot.addBox(-1.75F, 0.0F, 0.0F, 2.0F, 1.0F, 4.0F, -0.3F, 0.0F, 0.0F);
        this.setRotateAngle(rightLegBot, 0.17453292519943295F, 0.0F, 0.0F);
        this.rightArmSpike1 = new ModelPart(this, 46, 24);
        this.rightArmSpike1.setPos(0.0F, 2.0F, 0.0F);
        this.rightArmSpike1.addBox(-1.6F, -1.7F, -0.5F, 8.0F, 5.0F, 1.0F, -2.2F, -1.9F, -0.49F);
        this.leftArmSpike2 = new ModelPart(this, 46, 17);
        this.leftArmSpike2.setPos(0.0F, 2.0F, 0.0F);
        this.leftArmSpike2.addBox(-1.6F, -1.7F, -0.5F, 8.0F, 5.0F, 1.0F, -2.2F, -1.9F, -0.49F);
        this.leftArmSpike1 = new ModelPart(this, 46, 24);
        this.leftArmSpike1.setPos(0.0F, 2.0F, 0.0F);
        this.leftArmSpike1.addBox(-1.6F, -1.7F, -0.5F, 8.0F, 5.0F, 1.0F, -2.2F, -1.9F, -0.49F);
        this.leftArmSpike0 = new ModelPart(this, 46, 17);
        this.leftArmSpike0.setPos(0.8F, 0.5F, 0.0F);
        this.leftArmSpike0.addBox(-1.6F, -1.7F, -0.5F, 8.0F, 5.0F, 1.0F, -2.2F, -1.9F, -0.49F);
        this.face = new ModelPart(this, 28, 20);
        this.face.setPos(1.0F, -2.2F, 0.8F);
        this.face.addBox(-2.5F, -0.5F, -0.5F, 5.0F, 9.0F, 1.0F, -1.0F, 0.0F, -0.4F);
        this.setRotateAngle(face, -2.0943951023931953F, 0.0F, 0.0F);
        this.leftArmSpike3 = new ModelPart(this, 46, 24);
        this.leftArmSpike3.setPos(0.0F, 2.0F, 0.0F);
        this.leftArmSpike3.addBox(-1.6F, -1.7F, -0.5F, 8.0F, 5.0F, 1.0F, -2.2F, -1.9F, -0.49F);
        this.leftArm = new ModelPart(this, 22, 8);
        this.leftArm.setPos(3.0F, 1.1F, 1.1F);
        this.leftArm.addBox(-0.5F, -1.0F, -1.0F, 2.0F, 9.0F, 2.0F, -0.1F, 0.0F, -0.1F);
        this.setRotateAngle(leftArm, -1.2217304763960306F, 0.0F, 0.0F);
        this.head = new ModelPart(this, 45, 0);
        this.head.setPos(0.0F, 0.0F, 0.0F);
        this.head.addBox(-0.5F, -2.0F, -0.5F, 3.0F, 3.0F, 3.0F, 0.0F, 0.0F, 0.0F);
        this.setRotateAngle(head, 0.5235987755982988F, 0.0F, 0.0F);
        this.neck = new ModelPart(this, 35, 0);
        this.neck.setPos(-1.0F, -0.6F, -0.6F);
        this.neck.addBox(0.0F, 0.0F, 0.0F, 2.0F, 3.0F, 2.0F, 0.0F, 0.0F, 0.0F);
        this.setRotateAngle(neck, 0.7853981633974483F, 0.0F, 0.0F);
        this.horn2 = new ModelPart(this, 41, 13);
        this.horn2.setPos(0.0F, 0.0F, 3.5F);
        this.horn2.addBox(-0.5F, -8.0F, 0.0F, 1.0F, 9.0F, 1.0F, -0.3F, 0.0F, 0.0F);
        this.setRotateAngle(horn2, -0.2617993877991494F, 0.0F, 0.0F);
        this.bodyTop = new ModelPart(this, 15, 0);
        this.bodyTop.setPos(2.0F, 0.0F, -1.2F);
        this.bodyTop.addBox(-3.0F, 0.0F, 0.0F, 6.0F, 3.0F, 3.0F, 0.0F, 0.0F, 0.0F);
        this.setRotateAngle(bodyTop, 0.7853981633974483F, 0.0F, 0.0F);
        this.rightLegTop = new ModelPart(this, 0, 9);
        this.rightLegTop.setPos(1.6F, 5.0F, 0.0F);
        this.rightLegTop.addBox(-1.8F, -0.2F, 0.0F, 2.0F, 6.0F, 2.0F, -0.2F, 0.0F, -0.2F);
        this.setRotateAngle(rightLegTop, -0.6108652381980153F, 0.0F, 0.0F);
        this.rightArmSpike0 = new ModelPart(this, 46, 17);
        this.rightArmSpike0.setPos(0.8F, 0.5F, 0.0F);
        this.rightArmSpike0.addBox(-1.6F, -1.7F, -0.5F, 8.0F, 5.0F, 1.0F, -2.2F, -1.9F, -0.49F);
        this.skirtTop = new ModelPart(this, 11, 23);
        this.skirtTop.setPos(0.0F, 0.0F, 0.0F);
        this.skirtTop.addBox(-2.0F, 0.0F, -3.5F, 4.0F, 4.0F, 4.0F, 0.1F, 0.0F, -0.4F);
        this.rightFootBottom = new ModelPart(this, 6, 15);
        this.rightFootBottom.setPos(0.0F, 0.8F, 0.0F);
        this.rightFootBottom.addBox(-1.25F, 0.5F, -6.0F, 1.0F, 1.0F, 6.0F, -0.1F, -0.3F, 0.0F);
        this.leftFootBottom = new ModelPart(this, 6, 15);
        this.leftFootBottom.setPos(0.0F, 0.8F, 0.0F);
        this.leftFootBottom.addBox(-1.25F, 0.5F, -6.0F, 1.0F, 1.0F, 6.0F, -0.1F, -0.3F, 0.0F);
        this.skirtBack = new ModelPart(this, 0, 23);
        this.skirtBack.setPos(2.0F, 3.5F, 2.9F);
        this.skirtBack.addBox(-2.0F, 0.0F, -0.4F, 4.0F, 8.0F, 1.0F, 0.1F, 0.0F, -0.4F);
        this.leftLegBot = new ModelPart(this, 9, 9);
        this.leftLegBot.setPos(0.0F, 4.8F, 1.0F);
        this.leftLegBot.addBox(-1.75F, 0.0F, 0.0F, 2.0F, 1.0F, 4.0F, -0.3F, 0.0F, 0.0F);
        this.setRotateAngle(leftLegBot, 0.17453292519943295F, 0.0F, 0.0F);
        this.rightArmSpike3 = new ModelPart(this, 46, 24);
        this.rightArmSpike3.setPos(0.0F, 2.0F, 0.0F);
        this.rightArmSpike3.addBox(-1.6F, -1.7F, -0.5F, 8.0F, 5.0F, 1.0F, -2.2F, -1.9F, -0.49F);
        this.rightArmSpike5 = new ModelPart(this, 36, 8);
        this.rightArmSpike5.setPos(0.0F, 1.0F, 0.0F);
        this.rightArmSpike5.addBox(-0.5F, -0.5F, -0.5F, 1.0F, 2.0F, 1.0F, -0.2F, 0.0F, -0.2F);
        this.leftLegTop = new ModelPart(this, 0, 9);
        this.leftLegTop.setPos(4.0F, 5.0F, 0.0F);
        this.leftLegTop.addBox(-1.8F, -0.2F, 0.0F, 2.0F, 6.0F, 2.0F, -0.2F, 0.0F, -0.2F);
        this.setRotateAngle(leftLegTop, -0.6108652381980153F, 0.0F, 0.0F);
        this.leftFootTop = new ModelPart(this, 0, 18);
        this.leftFootTop.setPos(0.0F, 1.0F, 3.0F);
        this.leftFootTop.addBox(-1.25F, 0.0F, 0.0F, 1.0F, 2.0F, 1.0F, 0.0F, 0.0F, 0.0F);
        this.setRotateAngle(leftFootTop, 0.4363323129985824F, 0.0F, 0.0F);
        this.helmet = new ModelPart(this, 47, 8);
        this.helmet.setPos(1.0F, 0.0F, 0.0F);
        this.helmet.addBox(-2.0F, -2.6F, 1.0F, 4.0F, 4.0F, 3.0F, 0.0F, 0.0F, 0.0F);
        this.leftArmSpike4 = new ModelPart(this, 36, 8);
        this.leftArmSpike4.setPos(0.5F, 8.0F, 0.0F);
        this.leftArmSpike4.addBox(-0.5F, 0.0F, -0.5F, 1.0F, 1.0F, 1.0F, 0.1F, 0.0F, 0.1F);
        this.leftArmSpike5 = new ModelPart(this, 36, 8);
        this.leftArmSpike5.setPos(0.0F, 1.0F, 0.0F);
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
    public void renderToBuffer(PoseStack matrixStackIn, VertexConsumer bufferIn, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha) { 
        ImmutableList.of(this.bodyBot).forEach((modelRenderer) -> { 
            modelRenderer.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
        });
    }

    @Override
	public void setupAnim(T ent, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		updateDistanceMovedTotal(ent);
		if (ent.distanceToSqr(ent.xo, ent.yo, ent.zo) > 0) {
			for(int i = 0; i < animation.size(); i++) { //iterate through the legs array
				ModelAnimation m = animation.get(i); 
				
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
					m.model.xRot = (float) Math.toRadians(m.actVal);
					if(m.modelCounterpart != null) {
						m.modelCounterpart.xRot = (float) Math.toRadians(m.defVal*2-m.actVal);
					}
				}
			}
		
		} else {
	    	this.leftLegTop.xRot = this.rightLegTop.xRot = (float) Math.toRadians(-35);
			this.leftLegBot.xRot = this.rightLegBot.xRot = (float) Math.toRadians(10);
			this.leftFootTop.xRot = this.rightFootTop.xRot = (float) Math.toRadians(25);
		}
		
		if (EntityHelper.getState(ent) == 0) { //Standing
			//this.rightArm.rotateAngleX =  (float) Math.toRadians(0);
			this.rightArm.yRot = (float) Math.toRadians(0);
			this.rightArm.zRot = (float) Math.toRadians(180);

			this.leftArm.xRot =  (float) Math.toRadians(270);
			this.leftArm.yRot = (float) Math.toRadians(0);
			this.leftArm.zRot = (float) Math.toRadians(0);
			
		} else if (EntityHelper.getState(ent) == 1) { //Underground
			this.rightArm.xRot =  (float) Math.toRadians(270);
			this.rightArm.yRot = (float) Math.toRadians(90 - ent.tickCount*50);
			this.rightArm.zRot = (float) Math.toRadians(90);

			this.leftArm.xRot =  (float) Math.toRadians(270);
			this.leftArm.yRot = (float) Math.toRadians(-90 - ent.tickCount*50);
			this.leftArm.zRot = (float) Math.toRadians(90);
			
		} else if (EntityHelper.getState(ent) == 2) { //Exploding
			this.leftLegTop.xRot = (float) Math.toRadians(-135);
			this.rightLegTop.xRot = (float) Math.toRadians(-135);
			this.leftLegBot.xRot = (float) Math.toRadians(90);
			this.rightLegBot.xRot = (float) Math.toRadians(90);
			
			this.leftArm.yRot = (float) Math.toRadians(110);
			this.rightArm.yRot = (float) Math.toRadians(110);
		}

    }
    
    protected void updateDistanceMovedTotal(Entity e) {
    	distanceMovedTotal += e.distanceToSqr(e.xo, e.yo, e.zo);
	}

	protected double getDistanceMovedTotal() {
		return (distanceMovedTotal);
	}



    /**
     * This is a helper function from Tabula to set the rotation of model parts
     */
    public void setRotateAngle(ModelPart modelRenderer, float x, float y, float z) {
        modelRenderer.xRot = x;
        modelRenderer.yRot = y;
        modelRenderer.zRot = z;
    }
}
