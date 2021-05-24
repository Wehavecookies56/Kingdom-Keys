package online.kingdomkeys.kingdomkeys.client.model.entity;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;
import online.kingdomkeys.kingdomkeys.entity.EntityHelper;

public class NobodyCreeperModel<T extends Entity> extends EntityModel<T> {
    public ModelRenderer BodyLower;
    public ModelRenderer RightArmDetail;
    public ModelRenderer LeftArmDetail;
    public ModelRenderer BodyMiddle;
    public ModelRenderer BodyUpper;
    public ModelRenderer RightLegUpper;
    public ModelRenderer LeftLegUpper;
    public ModelRenderer Neck;
    public ModelRenderer RightLegMiddle;
    public ModelRenderer RightFootUpper;
    public ModelRenderer RightFootLower;
    public ModelRenderer LeftLegMiddle;
    public ModelRenderer LeftFootUpper;
    public ModelRenderer LeftFootLower;
    public ModelRenderer Head;
    public ModelRenderer HeadPoint;
    public ModelRenderer HeadPoint2;
    public ModelRenderer RightArmUpper;
    public ModelRenderer RightArmLower;
    public ModelRenderer RightFoot;
    public ModelRenderer RightFootDetail;
    public ModelRenderer LeftArmUpper;
    public ModelRenderer LeftArmLower;
    public ModelRenderer LeftFoot;
    public ModelRenderer LeftFootDetail;

    public ModelRenderer Spear_Handle;
    public ModelRenderer Spear_Head1;
    public ModelRenderer Spear_Detail1;
    public ModelRenderer Spear_Detail2;
    public ModelRenderer Spear_Head2;
    public ModelRenderer Spear_Wing1;
    public ModelRenderer Spear_Wing2;
    public ModelRenderer Spear_Head3;
    public ModelRenderer Spear_Wing11;
    public ModelRenderer Spear_Wing111;
    public ModelRenderer Spear_Wing22;
    public ModelRenderer Spear_Wing222;

    public ModelRenderer Sword_Handle1;
    public ModelRenderer Sword_Handle2;
    public ModelRenderer Sword_Handle3;
    public ModelRenderer Sword_Handle4;
    public ModelRenderer Sword_Handle5;
    public ModelRenderer Sword_Blade;
    public ModelRenderer Sword_Handle6;
    public ModelRenderer Sword_Blade2;
    public ModelRenderer Sword_Blade3;
    public ModelRenderer Sword_Blade4;
    public ModelRenderer Sword_Blade5;

    private int cycleIndex;
    private boolean canAnimate = true;
    private double frame;

    public NobodyCreeperModel() {
        this.textureWidth = 64;
        this.textureHeight = 64;
        this.RightArmDetail = new ModelRenderer(this, 0, 26);
        this.RightArmDetail.setRotationPoint(-1.3F, 5.7F, -1.0F);
        this.RightArmDetail.addBox(-1.0F, -2.0F, -2.0F, 1, 4, 4, 0.0F);
        this.RightFootDetail = new ModelRenderer(this, 12, 36);
        this.RightFootDetail.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.RightFootDetail.addBox(-13.55F, -13.9F, -2.0F, 2, 3, 3, 0.0F);
        this.setRotateAngle(RightFootDetail, 0.0F, 0.0F, -0.5009094953223726F);
        this.LeftArmUpper = new ModelRenderer(this, 0, 15);
        this.LeftArmUpper.setRotationPoint(0.0F, 0.0F, 1.0F);
        this.LeftArmUpper.addBox(-5.6F, -1.8F, -1.5F, 6, 2, 2, 0.0F);
        this.setRotateAngle(LeftArmUpper, 0.0F, 0.0F, -1.0016444577195458F);
        this.RightFootLower = new ModelRenderer(this, 30, 38);
        this.RightFootLower.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.RightFootLower.addBox(-1.0F, 2.1F, 9.0F, 2, 4, 1, 0.0F);
        this.setRotateAngle(RightFootLower, -1.2292353921796064F, 0.0F, 0.0F);
        this.RightArmLower = new ModelRenderer(this, 0, 20);
        this.RightArmLower.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.RightArmLower.addBox(-17.0F, -4.2F, -1.5F, 13, 2, 2, 0.0F);
        this.setRotateAngle(RightArmLower, 0.0F, 0.0F, -0.5009094953223726F);
        this.RightFoot = new ModelRenderer(this, 12, 26);
        this.RightFoot.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.RightFoot.addBox(-18.1F, -8.5F, -2.0F, 2, 5, 3, 0.0F);
        this.setRotateAngle(RightFoot, 0.0F, 0.0F, -0.091106186954104F);
        this.LeftFootLower = new ModelRenderer(this, 30, 38);
        this.LeftFootLower.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.LeftFootLower.addBox(-1.0F, 2.1F, 9.0F, 2, 4, 1, 0.0F);
        this.setRotateAngle(LeftFootLower, -1.2292353921796064F, 0.0F, 0.0F);
        this.HeadPoint = new ModelRenderer(this, 30, 0);
        this.HeadPoint.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.HeadPoint.addBox(-1.5F, -4.5F, -4.5F, 3, 2, 2, 0.0F);
        this.BodyMiddle = new ModelRenderer(this, 31, 17);
        this.BodyMiddle.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.BodyMiddle.addBox(-1.5F, -2.0F, -1.0F, 3, 3, 2, 0.0F);
        this.HeadPoint2 = new ModelRenderer(this, 37, 5);
        this.HeadPoint2.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.HeadPoint2.addBox(-1.5F, -5.0F, -5.5F, 3, 2, 1, 0.0F);
        this.LeftArmLower = new ModelRenderer(this, 0, 20);
        this.LeftArmLower.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.LeftArmLower.addBox(-17.0F, -4.2F, -1.5F, 13, 2, 2, 0.0F);
        this.setRotateAngle(LeftArmLower, 0.0F, 0.0F, -0.5009094953223726F);
        this.LeftFoot = new ModelRenderer(this, 12, 26);
        this.LeftFoot.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.LeftFoot.addBox(-18.1F, -8.5F, -2.0F, 2, 5, 3, 0.0F);
        this.setRotateAngle(LeftFoot, 0.0F, 0.0F, -0.091106186954104F);
        this.RightLegMiddle = new ModelRenderer(this, 36, 30);
        this.RightLegMiddle.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.RightLegMiddle.addBox(-2.0F, 4.0F, -2.0F, 4, 2, 4, 0.0F);
        this.LeftFootDetail = new ModelRenderer(this, 12, 36);
        this.LeftFootDetail.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.LeftFootDetail.addBox(-13.55F, -13.9F, -2.0F, 2, 3, 3, 0.0F);
        this.setRotateAngle(LeftFootDetail, 0.0F, 0.0F, -0.5009094953223726F);
        this.BodyLower = new ModelRenderer(this, 32, 23);
        this.BodyLower.setRotationPoint(1.1F, 8.6F, 0.0F);
        this.BodyLower.addBox(-2.0F, 0.0F, -1.5F, 4, 3, 3, 0.0F);
        this.setRotateAngle(BodyLower, 0.4886921905584123F, 0.0F, 0.0F);
        this.RightArmUpper = new ModelRenderer(this, 0, 15);
        this.RightArmUpper.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.RightArmUpper.addBox(-5.6F, -1.8F, -1.5F, 6, 2, 2, 0.0F);
        this.setRotateAngle(RightArmUpper, 0.0F, 0.0F, -1.0016444577195458F);
        this.LeftLegUpper = new ModelRenderer(this, 23, 29);
        this.LeftLegUpper.setRotationPoint(2.5F, 1.5F, 0.0F);
        this.LeftLegUpper.addBox(-1.5F, 0.0F, -1.5F, 3, 4, 3, 0.0F);
        this.setRotateAngle(LeftLegUpper, -0.22759093446006054F, 0.0F, 0.0F);
        this.LeftFootUpper = new ModelRenderer(this, 25, 38);
        this.LeftFootUpper.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.LeftFootUpper.addBox(-0.5F, 6.0F, -0.5F, 1, 4, 1, 0.0F);
        this.RightFootUpper = new ModelRenderer(this, 25, 38);
        this.RightFootUpper.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.RightFootUpper.addBox(-0.5F, 6.0F, -0.5F, 1, 4, 1, 0.0F);
        this.RightLegUpper = new ModelRenderer(this, 23, 29);
        this.RightLegUpper.setRotationPoint(-2.5F, 1.5F, 0.0F);
        this.RightLegUpper.addBox(-1.5F, 0.0F, -1.5F, 3, 4, 3, 0.0F);
        this.setRotateAngle(RightLegUpper, -0.22759093446006054F, 0.0F, 0.0F);
        this.Neck = new ModelRenderer(this, 19, 11);
        this.Neck.setRotationPoint(0.0F, -3.6F, -0.2F);
        this.Neck.addBox(-1.0F, -3.0F, -0.9F, 2, 2, 2, 0.0F);
        this.setRotateAngle(Neck, 0.4553564018453205F, 0.0F, 0.0F);
        this.LeftArmDetail = new ModelRenderer(this, 0, 26);
        this.LeftArmDetail.setRotationPoint(3.6F, 5.7F, -1.0F);
        this.LeftArmDetail.addBox(-1.0F, -2.0F, -2.0F, 1, 4, 4, 0.0F);
        this.setRotateAngle(LeftArmDetail, 0.0F, 3.141592653589793F, 0.0F);
        this.Head = new ModelRenderer(this, 18, 0);
        this.Head.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.Head.addBox(-1.5F, -3.9F, -3.6F, 3, 2, 6, 0.0F);
        this.setRotateAngle(Head, 0.27314402793711257F, 0.0F, 0.0F);
        this.LeftLegMiddle = new ModelRenderer(this, 36, 30);
        this.LeftLegMiddle.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.LeftLegMiddle.addBox(-2.0F, 4.0F, -2.0F, 4, 2, 4, 0.0F);
        this.BodyUpper = new ModelRenderer(this, 31, 10);
        this.BodyUpper.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.BodyUpper.addBox(-2.5F, -5.0F, -1.5F, 5, 3, 3, 0.0F);
        this.RightArmLower.addChild(this.RightFootDetail);
        this.LeftArmDetail.addChild(this.LeftArmUpper);
        this.RightFootUpper.addChild(this.RightFootLower);
        this.RightArmUpper.addChild(this.RightArmLower);
        this.RightArmLower.addChild(this.RightFoot);
        this.LeftFootUpper.addChild(this.LeftFootLower);
        this.Head.addChild(this.HeadPoint);
        this.BodyLower.addChild(this.BodyMiddle);
        this.HeadPoint.addChild(this.HeadPoint2);
        this.LeftArmUpper.addChild(this.LeftArmLower);
        this.LeftArmLower.addChild(this.LeftFoot);
        this.RightLegUpper.addChild(this.RightLegMiddle);
        this.LeftArmLower.addChild(this.LeftFootDetail);
        this.RightArmDetail.addChild(this.RightArmUpper);
        this.BodyLower.addChild(this.LeftLegUpper);
        this.LeftLegMiddle.addChild(this.LeftFootUpper);
        this.RightLegMiddle.addChild(this.RightFootUpper);
        this.BodyLower.addChild(this.RightLegUpper);
        this.BodyLower.addChild(this.Neck);
        this.Neck.addChild(this.Head);
        this.LeftLegUpper.addChild(this.LeftLegMiddle);
        this.BodyLower.addChild(this.BodyUpper);

        this.Spear_Wing11 = new ModelRenderer(this, 9, 5);
        this.Spear_Wing11.setRotationPoint(-1.0F, 0.5F, 0.0F);
        this.Spear_Wing11.addBox(-2.5F, 1.0F, -0.5F, 2, 3, 1, 0.0F);
        this.Spear_Head2 = new ModelRenderer(this, 0, 28);
        this.Spear_Head2.setRotationPoint(0.0F, 4.0F, 0.0F);
        this.Spear_Head2.addBox(-1.5F, 0.0F, -0.5F, 3, 4, 1, 0.0F);
        this.Spear_Detail1 = new ModelRenderer(this, 5, 11);
        this.Spear_Detail1.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.Spear_Detail1.addBox(-1.0F, 11.0F, -1.0F, 2, 1, 2, 0.0F);
        this.Spear_Wing1 = new ModelRenderer(this, 4, 0);
        this.Spear_Wing1.setRotationPoint(-1.0F, 0.5F, 0.0F);
        this.Spear_Wing1.addBox(-1.5F, -0.5F, -0.5F, 1, 5, 1, 0.0F);
        this.Spear_Wing22 = new ModelRenderer(this, 9, 5);
        this.Spear_Wing22.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.Spear_Wing22.addBox(1.0F, 1.5F, -0.5F, 2, 3, 1, 0.0F);
        this.Spear_Wing111 = new ModelRenderer(this, 10, 0);
        this.Spear_Wing111.setRotationPoint(-1.0F, 0.5F, 0.0F);
        this.Spear_Wing111.addBox(-2.5F, 2.5F, -0.5F, 1, 1, 1, 0.0F);
        this.Spear_Wing2 = new ModelRenderer(this, 4, 0);
        this.Spear_Wing2.setRotationPoint(1.5F, 0.5F, 0.0F);
        this.Spear_Wing2.addBox(0.0F, -0.5F, -0.5F, 1, 5, 1, 0.0F);
        this.Spear_Head1 = new ModelRenderer(this, 7, 18);
        this.Spear_Head1.setRotationPoint(0.0F, 15.0F, 0.0F);
        this.Spear_Head1.addBox(-1.5F, 0.0F, -0.5F, 3, 2, 1, 0.0F);
        this.Spear_Head3 = new ModelRenderer(this, 0, 35);
        this.Spear_Head3.setRotationPoint(0.0F, 4.0F, 0.0F);
        this.Spear_Head3.addBox(-0.5F, 0.0F, -0.5F, 1, 2, 1, 0.0F);
        this.Spear_Handle = new ModelRenderer(this, 0, 0);
        this.Spear_Handle.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.Spear_Handle.addBox(-0.5F, -10.0F, -0.5F, 1, 25, 1, 0.0F);
        this.Spear_Detail2 = new ModelRenderer(this, 5, 11);
        this.Spear_Detail2.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.Spear_Detail2.addBox(-1.0F, -5.0F, -1.0F, 2, 1, 2, 0.0F);
        this.Spear_Wing222 = new ModelRenderer(this, 10, 0);
        this.Spear_Wing222.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.Spear_Wing222.addBox(3.0F, 3.5F, -0.5F, 1, 1, 1, 0.0F);
        this.Spear_Wing1.addChild(this.Spear_Wing11);
        this.Spear_Head1.addChild(this.Spear_Head2);
        this.Spear_Handle.addChild(this.Spear_Detail1);
        this.Spear_Head1.addChild(this.Spear_Wing1);
        this.Spear_Wing2.addChild(this.Spear_Wing22);
        this.Spear_Wing11.addChild(this.Spear_Wing111);
        this.Spear_Head1.addChild(this.Spear_Wing2);
        this.Spear_Handle.addChild(this.Spear_Head1);
        this.Spear_Head2.addChild(this.Spear_Head3);
        this.Spear_Handle.addChild(this.Spear_Detail2);
        this.Spear_Wing22.addChild(this.Spear_Wing222);

        this.Sword_Blade3 = new ModelRenderer(this, 14, 19);
        this.Sword_Blade3.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.Sword_Blade3.addBox(1.0F, -14.0F, -0.5F, 1, 5, 1, 0.0F);
        this.Sword_Handle2 = new ModelRenderer(this, 10, 37);
        this.Sword_Handle2.setRotationPoint(0.0F, -8.5F, 0.0F);
        this.Sword_Handle2.addBox(-2.5F, -1.0F, -0.5F, 5, 2, 1, 0.0F);
        this.Sword_Handle6 = new ModelRenderer(this, 13, 52);
        this.Sword_Handle6.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.Sword_Handle6.addBox(-1.0F, 7.0F, -0.5F, 2, 1, 1, 0.0F);
        this.Sword_Blade4 = new ModelRenderer(this, 11, 14);
        this.Sword_Blade4.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.Sword_Blade4.addBox(-2.0F, -16.0F, -0.5F, 4, 2, 1, 0.0F);
        this.Sword_Handle3 = new ModelRenderer(this, 3, 37);
        this.Sword_Handle3.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.Sword_Handle3.addBox(-4.5F, -1.0F, -0.5F, 2, 1, 1, 0.0F);
        this.Sword_Blade = new ModelRenderer(this, 11, 26);
        this.Sword_Blade.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.Sword_Blade.addBox(-2.0F, -9.0F, -0.5F, 4, 8, 1, 0.0F);
        this.Sword_Blade2 = new ModelRenderer(this, 14, 19);
        this.Sword_Blade2.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.Sword_Blade2.addBox(-2.0F, -14.0F, -0.5F, 1, 5, 1, 0.0F);
        this.Sword_Blade5 = new ModelRenderer(this, 13, 10);
        this.Sword_Blade5.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.Sword_Blade5.addBox(-1.0F, -18.0F, -0.5F, 2, 2, 1, 0.0F);
        this.Sword_Handle4 = new ModelRenderer(this, 23, 37);
        this.Sword_Handle4.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.Sword_Handle4.addBox(2.5F, -1.0F, -0.5F, 2, 1, 1, 0.0F);
        this.Sword_Handle5 = new ModelRenderer(this, 13, 41);
        this.Sword_Handle5.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.Sword_Handle5.addBox(-1.0F, 1.0F, -0.5F, 2, 1, 1, 0.0F);
        this.Sword_Handle1 = new ModelRenderer(this, 14, 44);
        this.Sword_Handle1.setRotationPoint(0.0F, 13.0F, 0.0F);
        this.Sword_Handle1.addBox(-0.5F, -6.5F, -0.5F, 1, 5, 1, 0.0F);
        this.Sword_Blade.addChild(this.Sword_Blade3);
        this.Sword_Handle1.addChild(this.Sword_Handle2);
        this.Sword_Handle2.addChild(this.Sword_Handle6);
        this.Sword_Blade.addChild(this.Sword_Blade4);
        this.Sword_Handle2.addChild(this.Sword_Handle3);
        this.Sword_Handle2.addChild(this.Sword_Blade);
        this.Sword_Blade.addChild(this.Sword_Blade2);
        this.Sword_Blade4.addChild(this.Sword_Blade5);
        this.Sword_Handle2.addChild(this.Sword_Handle4);
        this.Sword_Handle2.addChild(this.Sword_Handle5);
    }

    @Override
    public void render(MatrixStack matrixStackIn, IVertexBuilder bufferIn, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha) {
        //Handled in NobodyCreeperRenderer
    }

    @Override
    public void setRotationAngles(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        /*
    	  Manually get these rotations ( in degrees ) while having tabula opened
    	  (or if it's easy like in this case just add +10)

    	  feel free to add more rotations for the sword if you feel like it's too short :)
    	 */
        double[] animationSwordFirstHit = new double[]
                {0, 10, 20, 30, 40, 50, 60, 70, 80, 90, 100, 110, 120, 130, 140, 150, 160, 170, 180, 190, 200, 210, 220, 230, 240, 250, 260, 270, 280, 290, 300, 310, 320, 330, 340, 350, 360};
        double[] animationSwordSecondHit = new double[]
                {180, 170, 160, 150, 140, 130, 120, 110, 100, 90, 80, 70, 60, 50, 40, 30, 20, 10, 0, -10, -20, -30, -40, -50, -60, -70, -80, -90, -100, -110, -120, -130, -140, -150, -160, -170, -180, -190, -200, -210, -220, -230, -240};

        // another thing to mention is that going under 5 degrees difference from frame-1 to frame-2 (so for example [0, 5, 10, 15] instead of [0, 10, 20, 30]) won't make the animation smoother or nicer...especially not for long animations
        // but in case of an animation with 5-10 frames you might wanna go from 1 to 1, it really depends on your specific case
        double[] animationLegHit = new double[]
                {28, 30, 40, 50, 60, 70, 60, 50, 40, 30, 20, 10, 0, -10, -20, -30, -40, -50, -60, -70, -80, -90, -100, -90, -80, -70, -60, -50, -40, -30, -20, -10, 0, 10, 20, 28};


        // we need to check if minecraft is paused otherwise the animation will run while the game is paused (doesn't look that good)
        if(!Minecraft.getInstance().isGamePaused())
        {
            if(EntityHelper.getState(entity) == 0)
            {
                this.BodyLower.rotateAngleX = MathHelper.cos(limbSwing * 0.8F) * 2.0F * limbSwingAmount;

                this.RightArmDetail.rotateAngleX = MathHelper.cos(limbSwing * 1.8F) * 0.9F * limbSwingAmount;
                this.LeftArmDetail.rotateAngleX = MathHelper.cos(limbSwing * 1.8F + (float) Math.PI) * 0.9F * limbSwingAmount;
            }
            else if (EntityHelper.getState(entity) == 1) // if the sword AI is active we begin the animation
            {
	    		/*
	    		  small bits of math here but I hope it's understandable even if it's not the nicest looking code :'(
	    		  frame is double variable that gets incremented each frame (last line frame += 0.5) it's also used to "scroll" through the animation frames and thus controls the speed of the animation

	    		 (really hope this is understandable cuz I don't really know how to explain it....)

	    		 Important thing to note is that by default vanilla uses radians instead of degrees .... so I have this fancy degToRad (formula stolen from stackoverflow obviously) to transform degrees in radians for readability
	    		 */
                if(frame < animationSwordFirstHit.length)
                {
                    this.Sword_Handle1.rotateAngleZ = degToRad(animationSwordFirstHit[(int) frame]);
                }
                else if(frame > animationSwordFirstHit.length - 1 && frame < animationSwordFirstHit.length + animationSwordSecondHit.length)
                {
                    this.Sword_Handle1.rotateAngleX = degToRad(-90);
                    this.Sword_Handle1.rotateAngleZ = degToRad(180);
                    this.Sword_Handle1.rotateAngleY = degToRad(animationSwordSecondHit[(int) frame - animationSwordFirstHit.length]);
                }
                else
                {
	    			/*
	    			  When the animation is finished, that means when the frame counter goes beyond the possible animatio frames, we reset the rotations (not really visible but it's for future animations)
	    			  reset the frame counter (same reason), and the state to make it look like a mob again
	    			 */
                    this.Sword_Handle1.rotateAngleX = this.Sword_Handle1.rotateAngleY = this.Sword_Handle1.rotateAngleZ = degToRad(0);
                    frame = 0;
                    EntityHelper.setState(entity, 0);
                }

                frame += 0.5;
            }
            else if(EntityHelper.getState(entity) == 3) // if the sword AI is active we begin the animation
            {
                if(frame < animationLegHit.length)
                {
                    this.BodyLower.rotateAngleX = degToRad(animationLegHit[(int) frame]);
                }
                else
                {
                    this.BodyLower.rotateAngleX = degToRad(28);
                    frame = 0;
                    EntityHelper.setState(entity, 0);
                }

                frame += 0.6;
            }
        }
    }

    protected float degToRad(double degrees)
    {
        return (float) (degrees * (double)Math.PI / 180) ;
    }

    /**
     * This is a helper function from Tabula to set the rotation of model parts
     */
    public void setRotateAngle(ModelRenderer modelRenderer, float x, float y, float z)
    {
        modelRenderer.rotateAngleX = x;
        modelRenderer.rotateAngleY = y;
        modelRenderer.rotateAngleZ = z;
    }
}