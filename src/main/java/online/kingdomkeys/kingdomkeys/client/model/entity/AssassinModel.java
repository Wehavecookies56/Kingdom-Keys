package online.kingdomkeys.kingdomkeys.client.model.entity;

import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;

import io.netty.util.internal.MathUtil;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import online.kingdomkeys.kingdomkeys.entity.EntityHelper;

/**
 * Assassin - Abelatox
 * Created using Tabula 8.0.0
 */
@OnlyIn(Dist.CLIENT)
public class AssassinModel<T extends Entity> extends EntityModel<T> {
	public ModelRenderer bodyBot;
    public ModelRenderer leftLegTop;
    public ModelRenderer rightLegTop;
    public ModelRenderer bodyTop;
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
    public ModelRenderer horn1;
    public ModelRenderer horn2;
    public ModelRenderer leftArmSpike1;
    public ModelRenderer leftArmSpike2;
    public ModelRenderer leftArmSpike3;
    public ModelRenderer leftArmSpike4;
    public ModelRenderer rightArmSpike1;
    public ModelRenderer rightArmSpike2;
    public ModelRenderer rightArmSpike3;
    public ModelRenderer rightArmSpike4;

    public AssassinModel() {
        this.textureWidth = 64;
        this.textureHeight = 32;
        this.rightArmSpike3 = new ModelRenderer(this, 23, 14);
        this.rightArmSpike3.setRotationPoint(2.0F, 0.0F, 0.0F);
        this.rightArmSpike3.addBox(0.0F, 0.0F, 0.0F, 1.0F, 3.0F, 1.0F, 0.0F, 0.0F, 0.0F);
        this.helmet = new ModelRenderer(this, 47, 8);
        this.helmet.setRotationPoint(1.0F, 0.0F, 0.0F);
        this.helmet.addBox(-2.0F, -2.6F, 1.0F, 4.0F, 4.0F, 3.0F, 0.0F, 0.0F, 0.0F);
        this.neck = new ModelRenderer(this, 35, 0);
        this.neck.setRotationPoint(-1.0F, -0.6F, -0.6F);
        this.neck.addBox(0.0F, 0.0F, 0.0F, 2.0F, 3.0F, 2.0F, 0.0F, 0.0F, 0.0F);
        this.setRotateAngle(neck, 0.7853981633974483F, 0.0F, 0.0F);
        this.rightLegTop = new ModelRenderer(this, 0, 9);
        this.rightLegTop.setRotationPoint(1.6F, 5.0F, 0.0F);
        this.rightLegTop.addBox(-1.8F, -0.2F, 0.0F, 2.0F, 6.0F, 2.0F, -0.2F, 0.0F, -0.2F);
        this.setRotateAngle(rightLegTop, -0.6108652381980153F, 0.0F, 0.0F);
        this.leftFootBottom = new ModelRenderer(this, 6, 15);
        this.leftFootBottom.setRotationPoint(0.0F, 0.8F, 0.0F);
        this.leftFootBottom.addBox(-1.25F, 0.5F, -6.0F, 1.0F, 1.0F, 6.0F, -0.1F, -0.3F, 0.0F);
        this.leftArmSpike1 = new ModelRenderer(this, 23, 14);
        this.leftArmSpike1.setRotationPoint(2.0F, -4.0F, -0.5F);
        this.leftArmSpike1.addBox(0.0F, 0.0F, 0.0F, 1.0F, 3.0F, 1.0F, 0.0F, 0.0F, 0.0F);
        this.leftArm = new ModelRenderer(this, 22, 8);
        this.leftArm.setRotationPoint(3.0F, 1.1F, 1.1F);
        this.leftArm.addBox(-0.5F, -1.0F, -1.0F, 10.0F, 2.0F, 2.0F, 0.0F, 0.0F, 0.0F);
        this.setRotateAngle(leftArm, 0.0F, 1.2217304763960306F, 1.5707963267948966F);
        this.leftArmSpike4 = new ModelRenderer(this, 29, 14);
        this.leftArmSpike4.setRotationPoint(2.0F, 0.0F, 0.0F);
        this.leftArmSpike4.addBox(0.0F, 0.0F, 0.0F, 1.0F, 3.0F, 1.0F, 0.0F, 0.0F, 0.0F);
        this.bodyTop = new ModelRenderer(this, 15, 0);
        this.bodyTop.setRotationPoint(2.0F, 0.0F, -1.2F);
        this.bodyTop.addBox(-3.0F, 0.0F, 0.0F, 6.0F, 3.0F, 3.0F, 0.0F, 0.0F, 0.0F);
        this.setRotateAngle(bodyTop, 0.7853981633974483F, 0.0F, 0.0F);
        this.leftArmSpike3 = new ModelRenderer(this, 23, 14);
        this.leftArmSpike3.setRotationPoint(2.0F, 0.0F, 0.0F);
        this.leftArmSpike3.addBox(0.0F, 0.0F, 0.0F, 1.0F, 3.0F, 1.0F, 0.0F, 0.0F, 0.0F);
        this.horn1 = new ModelRenderer(this, 35, 13);
        this.horn1.setRotationPoint(0.0F, -3.0F, 1.5F);
        this.horn1.addBox(-0.5F, -4.0F, 0.0F, 1.0F, 5.0F, 1.0F, -0.3F, 0.0F, 0.0F);
        this.setRotateAngle(horn1, -0.8726646259971648F, 0.0F, 0.0F);
        this.leftArmSpike2 = new ModelRenderer(this, 29, 14);
        this.leftArmSpike2.setRotationPoint(2.0F, 0.0F, 0.0F);
        this.leftArmSpike2.addBox(0.0F, 0.0F, 0.0F, 1.0F, 3.0F, 1.0F, 0.0F, 0.0F, 0.0F);
        this.leftLegTop = new ModelRenderer(this, 0, 9);
        this.leftLegTop.setRotationPoint(4.0F, 5.0F, 0.0F);
        this.leftLegTop.addBox(-1.8F, -0.2F, 0.0F, 2.0F, 6.0F, 2.0F, -0.2F, 0.0F, -0.2F);
        this.setRotateAngle(leftLegTop, -0.6108652381980153F, 0.0F, 0.0F);
        this.leftLegBot = new ModelRenderer(this, 9, 9);
        this.leftLegBot.setRotationPoint(0.0F, 4.8F, 1.0F);
        this.leftLegBot.addBox(-1.75F, 0.0F, 0.0F, 2.0F, 1.0F, 4.0F, -0.3F, 0.0F, 0.0F);
        this.setRotateAngle(leftLegBot, 0.17453292519943295F, 0.0F, 0.0F);
        this.rightArm = new ModelRenderer(this, 22, 8);
        this.rightArm.setRotationPoint(-3.0F, 1.1F, 1.1F);
        this.rightArm.addBox(-0.5F, -1.0F, -1.0F, 10.0F, 2.0F, 2.0F, 0.0F, 0.0F, 0.0F);
        this.setRotateAngle(rightArm, 0.0F, 1.9198621771937625F, -1.5707963267948966F);
        this.rightFootTop = new ModelRenderer(this, 0, 18);
        this.rightFootTop.setRotationPoint(0.0F, 1.0F, 3.0F);
        this.rightFootTop.addBox(-1.25F, 0.0F, 0.0F, 1.0F, 2.0F, 1.0F, 0.0F, 0.0F, 0.0F);
        this.setRotateAngle(rightFootTop, 0.4363323129985824F, 0.0F, 0.0F);
        this.leftFootTop = new ModelRenderer(this, 0, 18);
        this.leftFootTop.setRotationPoint(0.0F, 1.0F, 3.0F);
        this.leftFootTop.addBox(-1.25F, 0.0F, 0.0F, 1.0F, 2.0F, 1.0F, 0.0F, 0.0F, 0.0F);
        this.setRotateAngle(leftFootTop, 0.4363323129985824F, 0.0F, 0.0F);
        this.rightArmSpike2 = new ModelRenderer(this, 29, 14);
        this.rightArmSpike2.setRotationPoint(2.0F, 0.0F, 0.0F);
        this.rightArmSpike2.addBox(0.0F, 0.0F, 0.0F, 1.0F, 3.0F, 1.0F, 0.0F, 0.0F, 0.0F);
        this.head = new ModelRenderer(this, 45, 0);
        this.head.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.head.addBox(-0.5F, -2.0F, -0.5F, 3.0F, 3.0F, 3.0F, 0.0F, 0.0F, 0.0F);
        this.setRotateAngle(head, 0.5235987755982988F, 0.0F, 0.0F);
        this.bodyBot = new ModelRenderer(this, 0, 0);
        this.bodyBot.setRotationPoint(-2.0F, 3.0F, 0.0F);
        this.bodyBot.addBox(0.0F, 0.0F, 0.0F, 4.0F, 5.0F, 3.0F, 0.0F, 0.0F, 0.0F);
        this.rightArmSpike4 = new ModelRenderer(this, 29, 14);
        this.rightArmSpike4.setRotationPoint(2.0F, 0.0F, 0.0F);
        this.rightArmSpike4.addBox(0.0F, 0.0F, 0.0F, 1.0F, 3.0F, 1.0F, 0.0F, 0.0F, 0.0F);
        this.rightFootBottom = new ModelRenderer(this, 6, 15);
        this.rightFootBottom.setRotationPoint(0.0F, 0.8F, 0.0F);
        this.rightFootBottom.addBox(-1.25F, 0.5F, -6.0F, 1.0F, 1.0F, 6.0F, -0.1F, -0.3F, 0.0F);
        this.horn2 = new ModelRenderer(this, 41, 13);
        this.horn2.setRotationPoint(0.0F, 0.0F, 3.5F);
        this.horn2.addBox(-0.5F, -8.0F, 0.0F, 1.0F, 9.0F, 1.0F, -0.3F, 0.0F, 0.0F);
        this.setRotateAngle(horn2, -0.2617993877991494F, 0.0F, 0.0F);
        this.rightLegBot = new ModelRenderer(this, 9, 9);
        this.rightLegBot.setRotationPoint(0.0F, 4.8F, 1.0F);
        this.rightLegBot.addBox(-1.75F, 0.0F, 0.0F, 2.0F, 1.0F, 4.0F, -0.3F, 0.0F, 0.0F);
        this.setRotateAngle(rightLegBot, 0.17453292519943295F, 0.0F, 0.0F);
        this.rightArmSpike1 = new ModelRenderer(this, 23, 14);
        this.rightArmSpike1.setRotationPoint(2.0F, -4.0F, -0.5F);
        this.rightArmSpike1.addBox(0.0F, 0.0F, 0.0F, 1.0F, 3.0F, 1.0F, 0.0F, 0.0F, 0.0F);
        this.rightArmSpike2.addChild(this.rightArmSpike3);
        this.head.addChild(this.helmet);
        this.bodyTop.addChild(this.neck);
        this.bodyBot.addChild(this.rightLegTop);
        this.leftFootTop.addChild(this.leftFootBottom);
        this.leftArm.addChild(this.leftArmSpike1);
        this.bodyTop.addChild(this.leftArm);
        this.leftArmSpike3.addChild(this.leftArmSpike4);
        this.bodyBot.addChild(this.bodyTop);
        this.leftArmSpike2.addChild(this.leftArmSpike3);
        this.helmet.addChild(this.horn1);
        this.leftArmSpike1.addChild(this.leftArmSpike2);
        this.bodyBot.addChild(this.leftLegTop);
        this.leftLegTop.addChild(this.leftLegBot);
        this.bodyTop.addChild(this.rightArm);
        this.rightLegBot.addChild(this.rightFootTop);
        this.leftLegBot.addChild(this.leftFootTop);
        this.rightArmSpike1.addChild(this.rightArmSpike2);
        this.neck.addChild(this.head);
        this.rightArmSpike3.addChild(this.rightArmSpike4);
        this.rightFootTop.addChild(this.rightFootBottom);
        this.helmet.addChild(this.horn2);
        this.rightLegTop.addChild(this.rightLegBot);
        this.rightArm.addChild(this.rightArmSpike1);
    }

    @Override
    public void render(MatrixStack matrixStackIn, IVertexBuilder bufferIn, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha) { 
        ImmutableList.of(this.bodyBot).forEach((modelRenderer) -> { 
            modelRenderer.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
        });
    }

    @Override
	public void setRotationAngles(T ent, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {

		if (EntityHelper.getState(ent) == 0) {
			//this.rightArm.rotateAngleX =  (float) Math.toRadians(0);
			this.rightArm.rotateAngleY = (float) Math.toRadians(70);
			this.rightArm.rotateAngleZ = (float) Math.toRadians(180);

			this.leftArm.rotateAngleX =  (float) Math.toRadians(270);
			this.leftArm.rotateAngleY = (float) Math.toRadians(70);
			this.leftArm.rotateAngleZ = (float) Math.toRadians(0);
			
		} else if (EntityHelper.getState(ent) == 1) {
			this.rightArm.rotateAngleX =  (float) Math.toRadians(270);
			this.rightArm.rotateAngleY = -90;
			this.rightArm.rotateAngleZ = 70;

			this.leftArm.rotateAngleX =  (float) Math.toRadians(270);
			this.leftArm.rotateAngleY = -90;
			this.leftArm.rotateAngleZ = 90;
		}

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
