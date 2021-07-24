package online.kingdomkeys.kingdomkeys.client.model.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.world.entity.Entity;
import online.kingdomkeys.kingdomkeys.entity.EntityHelper;
import online.kingdomkeys.kingdomkeys.entity.mob.BaseElementalMusicalHeartlessEntity;

public class ElementalMusicalHeartlessModel<T extends Entity> extends EntityModel<T> {
    public ModelPart Body;
    public ModelPart Body1;
    public ModelPart RightLeg;
    public ModelPart LeftLeg;
    public ModelPart Body2;
    public ModelPart Collar1;
    public ModelPart Collar2;
    public ModelPart Collar3;
    public ModelPart Collar4;
    public ModelPart Head;
    public ModelPart Hat;
    public ModelPart Hat1;
    public ModelPart HatDetail1;
    public ModelPart HatDetail2;
    public ModelPart HatDetail3;
    public ModelPart HatDetail4;
    public ModelPart HatTop1;
    public ModelPart HatTop2;
    public ModelPart HatTop3;
    public ModelPart HatTop4;
    public ModelPart HatTop5;
    public ModelPart RightLegDetail1;
    public ModelPart RightLegDetail2;
    public ModelPart RightLegDetail3;
    public ModelPart LeftLegDetail1;
    public ModelPart LeftLegDetail2;
    public ModelPart LeftLegDetail3;

    private boolean canAnimate = true;
    private double frame;

    public ElementalMusicalHeartlessModel() {
        this.texWidth = 64;
        this.texHeight = 64;
        this.HatDetail4 = new ModelPart(this, 29, 5);
        this.HatDetail4.setPos(-2.5F, 2.0F, 0.5F);
        this.HatDetail4.addBox(0.0F, -0.5F, -3.0F, 0, 1, 6, 0.0F);
        this.Collar4 = new ModelPart(this, 29, -3);
        this.Collar4.setPos(-3.0F, -1.0F, 0.0F);
        this.Collar4.addBox(0.0F, -1.0F, -3.0F, 0, 2, 6, 0.0F);
        this.RightLegDetail3 = new ModelPart(this, 0, 35);
        this.RightLegDetail3.setPos(0.0F, 2.5F, 0.0F);
        this.RightLegDetail3.addBox(-0.5F, 0.0F, -0.5F, 1, 2, 1, 0.0F);
        this.Body2 = new ModelPart(this, 0, 23);
        this.Body2.setPos(0.0F, -3.0F, 0.0F);
        this.Body2.addBox(-3.0F, 0.0F, -3.0F, 6, 3, 6, 0.0F);
        this.RightLegDetail1 = new ModelPart(this, 0, 35);
        this.RightLegDetail1.setPos(0.0F, 2.5F, 0.0F);
        this.RightLegDetail1.addBox(-0.5F, 0.0F, -0.5F, 1, 2, 1, 0.0F);
        this.setRotateAngle(RightLegDetail1, 0.0F, 0.0F, 0.7853981633974483F);
        this.HatDetail2 = new ModelPart(this, 29, 6);
        this.HatDetail2.setPos(0.5F, 2.0F, 3.5F);
        this.HatDetail2.addBox(-3.0F, -0.5F, 0.0F, 6, 1, 0, 0.0F);
        this.LeftLegDetail1 = new ModelPart(this, 0, 35);
        this.LeftLegDetail1.setPos(0.0F, 2.5F, 0.0F);
        this.LeftLegDetail1.addBox(-0.5F, 0.0F, -0.5F, 1, 2, 1, 0.0F);
        this.setRotateAngle(LeftLegDetail1, 0.0F, 0.0F, 0.7853981633974483F);
        this.LeftLegDetail2 = new ModelPart(this, 0, 35);
        this.LeftLegDetail2.setPos(0.0F, 2.5F, 0.0F);
        this.LeftLegDetail2.addBox(-0.5F, 0.0F, -0.5F, 1, 2, 1, 0.0F);
        this.setRotateAngle(LeftLegDetail2, 0.0F, 0.0F, -0.7853981633974483F);
        this.HatTop3 = new ModelPart(this, 28, 25);
        this.HatTop3.setPos(0.5F, -2.5F, 0.0F);
        this.HatTop3.addBox(-0.5F, 0.0F, -0.5F, 1, 3, 1, 0.0F);
        this.setRotateAngle(HatTop3, 0.0F, 0.0F, 1.5707963267948966F);
        this.RightLeg = new ModelPart(this, 0, 35);
        this.RightLeg.setPos(-2.0F, 1.0F, 0.5F);
        this.RightLeg.addBox(-0.5F, 0.0F, -0.5F, 1, 3, 1, 0.0F);
        this.Collar1 = new ModelPart(this, 29, 0);
        this.Collar1.setPos(0.0F, -1.0F, -3.0F);
        this.Collar1.addBox(-3.0F, -1.0F, 0.0F, 6, 2, 0, 0.0F);
        this.Head = new ModelPart(this, 0, 53);
        this.Head.setPos(0.0F, -2.0F, 0.0F);
        this.Head.addBox(-2.0F, -2.0F, -2.0F, 4, 4, 4, 0.0F);
        this.HatTop1 = new ModelPart(this, 28, 34);
        this.HatTop1.setPos(0.0F, -0.5F, 0.0F);
        this.HatTop1.addBox(-0.5F, -4.5F, -0.5F, 1, 4, 1, 0.0F);
        this.LeftLeg = new ModelPart(this, 0, 35);
        this.LeftLeg.setPos(2.0F, 1.0F, 0.5F);
        this.LeftLeg.addBox(-0.5F, 0.0F, -0.5F, 1, 3, 1, 0.0F);
        this.LeftLegDetail3 = new ModelPart(this, 0, 35);
        this.LeftLegDetail3.setPos(0.0F, 2.5F, 0.0F);
        this.LeftLegDetail3.addBox(-0.5F, 0.0F, -0.5F, 1, 2, 1, 0.0F);
        this.HatTop4 = new ModelPart(this, 28, 20);
        this.HatTop4.setPos(-0.5F, 3.3F, 0.0F);
        this.HatTop4.addBox(-0.5F, -3.0F, -0.5F, 1, 3, 1, 0.0F);
        this.setRotateAngle(HatTop4, 0.0F, 0.0F, 1.5707963267948966F);
        this.HatTop2 = new ModelPart(this, 28, 30);
        this.HatTop2.setPos(-0.1F, -4.2F, 0.0F);
        this.HatTop2.addBox(-0.5F, -2.0F, -0.5F, 1, 2, 1, 0.0F);
        this.setRotateAngle(HatTop2, 0.0F, 0.0F, 0.7853981633974483F);
        this.Hat = new ModelPart(this, 0, 34);
        this.Hat.setPos(-0.5F, -3.5F, -0.5F);
        this.Hat.addBox(-2.5F, -0.5F, -2.5F, 6, 2, 6, 0.0F);
        this.Hat1 = new ModelPart(this, 0, 44);
        this.Hat1.setPos(0.5F, -0.5F, 0.5F);
        this.Hat1.addBox(-2.5F, -1.0F, -2.5F, 5, 1, 5, 0.0F);
        this.HatDetail1 = new ModelPart(this, 29, 6);
        this.HatDetail1.setPos(0.5F, 2.0F, -2.5F);
        this.HatDetail1.addBox(-3.0F, -0.5F, 0.0F, 6, 1, 0, 0.0F);
        this.Body1 = new ModelPart(this, 0, 12);
        this.Body1.setPos(0.0F, -3.0F, 0.0F);
        this.Body1.addBox(-3.5F, 0.0F, -3.5F, 7, 2, 7, 0.0F);
        this.RightLegDetail2 = new ModelPart(this, 0, 35);
        this.RightLegDetail2.setPos(0.0F, 2.5F, 0.0F);
        this.RightLegDetail2.addBox(-0.5F, 0.0F, -0.5F, 1, 2, 1, 0.0F);
        this.setRotateAngle(RightLegDetail2, 0.0F, 0.0F, -0.7853981633974483F);
        this.HatTop5 = new ModelPart(this, 22, 23);
        this.HatTop5.setPos(-1.0F, -2.5F, 0.0F);
        this.HatTop5.addBox(-0.5F, -0.5F, -0.5F, 1, 1, 1, 0.0F);
        this.setRotateAngle(HatTop5, 0.0F, 0.0F, 1.5707963267948966F);
        this.Collar3 = new ModelPart(this, 29, -3);
        this.Collar3.setPos(3.0F, -1.0F, 0.0F);
        this.Collar3.addBox(0.0F, -1.0F, -3.0F, 0, 2, 6, 0.0F);
        this.Collar2 = new ModelPart(this, 29, 0);
        this.Collar2.setPos(0.0F, -1.0F, 3.0F);
        this.Collar2.addBox(-3.0F, -1.0F, 0.0F, 6, 2, 0, 0.0F);
        this.HatDetail3 = new ModelPart(this, 29, 5);
        this.HatDetail3.setPos(3.5F, 2.0F, 0.5F);
        this.HatDetail3.addBox(0.0F, -0.5F, -3.0F, 0, 1, 6, 0.0F);
        this.Body = new ModelPart(this, 0, 0);
        this.Body.setPos(0.0F, 8.1F, 0.0F);
        this.Body.addBox(-4.0F, -1.0F, -4.0F, 8, 2, 8, 0.0F);
        this.Hat.addChild(this.HatDetail4);
        this.Body2.addChild(this.Collar4);
        this.RightLeg.addChild(this.RightLegDetail3);
        this.Body1.addChild(this.Body2);
        this.RightLeg.addChild(this.RightLegDetail1);
        this.Hat.addChild(this.HatDetail2);
        this.LeftLeg.addChild(this.LeftLegDetail1);
        this.LeftLeg.addChild(this.LeftLegDetail2);
        this.HatTop2.addChild(this.HatTop3);
        this.Body.addChild(this.RightLeg);
        this.Body2.addChild(this.Collar1);
        this.Body2.addChild(this.Head);
        this.Hat1.addChild(this.HatTop1);
        this.Body.addChild(this.LeftLeg);
        this.LeftLeg.addChild(this.LeftLegDetail3);
        this.HatTop3.addChild(this.HatTop4);
        this.HatTop1.addChild(this.HatTop2);
        this.Head.addChild(this.Hat);
        this.Hat.addChild(this.Hat1);
        this.Hat.addChild(this.HatDetail1);
        this.Body.addChild(this.Body1);
        this.RightLeg.addChild(this.RightLegDetail2);
        this.HatTop4.addChild(this.HatTop5);
        this.Body2.addChild(this.Collar3);
        this.Body2.addChild(this.Collar2);
        this.Hat.addChild(this.HatDetail3);
    }

    @Override
    public void renderToBuffer(PoseStack matrixStackIn, VertexConsumer bufferIn, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha) {
        this.Body.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
    }

    @Override
    public void setupAnim(T entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        double[] animationShootFire = new double[]
                {0, 10, 20, 30, 40, 50, 60, 70, 80, 90, 100, 110, 120, 130, 140, 150, 160, 170, 180, 190, 200, 210, 220, 230, 240, 250, 260, 270, 280, 290, 300, 310, 320, 330, 340, 350, 360};
        double[] animationShootBlizzard = new double[]
                {0, -10, -20, -30, -20, -10, 0, 10, 20, 30, 40, 50, 60, 70, 60, 50, 40, 30, 20, 10, 5, 0};
        //TODO aero and thunder

        double[] animationMeleeAttack = new double[]
                {0, 10, 20, 30, 40, 50, 60, 70, 80, 90, 100, 110, 120, 130, 140, 150, 160, 170, 180, 190, 200, 210, 220, 230, 240, 250, 260, 270, 280, 290, 300, 310, 320, 330, 340, 350, 360, 370, 380, 390, 400, 410, 420, 430, 440, 450, 460, 470, 480, 490, 500, 510, 520, 530};


        if(!Minecraft.getInstance().isPaused()) {
            if(EntityHelper.getState(entityIn) == 1) {
                if (entityIn instanceof BaseElementalMusicalHeartlessEntity) {
                    BaseElementalMusicalHeartlessEntity entity = (BaseElementalMusicalHeartlessEntity) entityIn;
                    if (entity.getElementToUse() == BaseElementalMusicalHeartlessEntity.Element.FIRE) {
                        if (frame < animationShootFire.length) {
                            this.Hat1.y = this.HatTop1.y = -0.2F;
                            this.Hat1.getYRot() = degToRad(animationShootFire[(int) frame]);
                            this.HatTop1.getYRot() = degToRad(animationShootFire[(animationShootFire.length - 1) - (int) frame]) * 2;
                        } else {
                            this.Hat1.getYRot() = this.HatTop1.getYRot() = degToRad(0);
                            this.Hat1.y = this.HatTop1.y = 0F;
                            frame = 0;
                            EntityHelper.setState(entityIn, 0);
                        }

                        this.frame += 0.7;
                    } else if (entity.getElementToUse() == BaseElementalMusicalHeartlessEntity.Element.BLIZZARD) {
                        if (frame < animationShootBlizzard.length) {
                            this.Body.getXRot() = degToRad(animationShootBlizzard[(int) frame]);

                            if (frame > animationShootBlizzard.length - 16) {
                                this.Hat1.y = this.HatTop1.y = -0.2F;
                                this.Hat1.getYRot() = degToRad(animationShootFire[(int) frame]);
                                this.HatTop1.getYRot() = degToRad(animationShootFire[(animationShootFire.length - 1) - (int) frame]) * 2;
                            }
                        } else {
                            this.Hat1.getYRot() = this.HatTop1.getYRot() = degToRad(0);
                            this.Hat1.y = this.HatTop1.y = 0F;
                            this.Body.getXRot() = degToRad(0);
                            frame = 0;
                            EntityHelper.setState(entity, 0);
                        }

                        this.frame += 0.6;
                    }
                }
            }
            else if(EntityHelper.getState(entityIn) == 2) {
                if(frame < animationMeleeAttack.length) {
                    this.Hat1.y = this.HatTop1.y = -0.6F;
                    this.Body.getXRot() = degToRad(85);
                    this.Body.getYRot() = degToRad(animationMeleeAttack[(int) frame]);
                }
                else {
                    this.Body.getXRot() = degToRad(0);
                    this.Hat1.getYRot() = this.Body.getYRot() = degToRad(0);
                    this.Hat1.y = this.HatTop1.y = 0F;
                    frame = 0;
                    EntityHelper.setState(entityIn, 0);
                }

                this.frame += 1.2;
            }
            else if(EntityHelper.getState(entityIn) == 3) {
                if(frame < animationMeleeAttack.length) {
                    this.Body.getXRot() = degToRad(90);
                    this.Body.zRot = degToRad(animationMeleeAttack[(int) frame]);
                }
                else {
                    this.Body.getXRot() = this.Body.zRot = degToRad(0);
                    frame = 0;
                    EntityHelper.setState(entityIn, 0);
                }

                this.frame += 1.2;
            }
        }
    }

    protected float degToRad(double degrees) {
        return (float) (degrees * (double)Math.PI / 180);
    }

    /**
     * This is a helper function from Tabula to set the rotation of model parts
     */
    public void setRotateAngle(ModelPart modelRenderer, float x, float y, float z) {
        modelRenderer.getXRot() = x;
        modelRenderer.getYRot() = y;
        modelRenderer.zRot = z;
    }

}
