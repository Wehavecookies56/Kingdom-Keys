package online.kingdomkeys.kingdomkeys.client.model.entity;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;

import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.entity.Entity;

/**
 * WhiteMushroom - WYND Created using Tabula 7.0.0
 */
public class WhiteMushroomModel<T extends Entity> extends EntityModel<T> {
    public ModelRenderer body;
    public ModelRenderer cloth1;
    public ModelRenderer head;
    public ModelRenderer cloth2;
    public ModelRenderer leftArm;
    public ModelRenderer rightArm;
    public ModelRenderer hat;
    public ModelRenderer hat2;
    public ModelRenderer hat3;
    public ModelRenderer hat4;
    public ModelRenderer hat5;
    public ModelRenderer hat6;

    public WhiteMushroomModel() {
        this.textureWidth = 64;
        this.textureHeight = 64;
        this.hat5 = new ModelRenderer(this, 30, 1);
        this.hat5.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.hat5.addBox(-4.0F, -18.5F, -5.0F, 8, 5, 1, 0.0F);
        this.body = new ModelRenderer(this, 0, 0);
        this.body.setRotationPoint(0.0F, 15.0F, 0.0F);
        this.body.addBox(-4.0F, -8.0F, -4.0F, 8, 17, 8, 0.0F);
        this.cloth2 = new ModelRenderer(this, 28, 46);
        this.cloth2.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.cloth2.addBox(-4.5F, -8.0F, -4.5F, 9, 2, 9, 0.0F);
        this.rightArm = new ModelRenderer(this, 34, 10);
        this.rightArm.setRotationPoint(-4.5F, -7.0F, 0.5F);
        this.rightArm.addBox(-1.5F, 0.0F, -1.5F, 2, 9, 3, 0.0F);
        this.cloth1 = new ModelRenderer(this, 24, 25);
        this.cloth1.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.cloth1.addBox(-5.0F, 5.0F, -5.0F, 10, 4, 10, 0.0F);
        this.hat4 = new ModelRenderer(this, 4, 55);
        this.hat4.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.hat4.addBox(-4.0F, -19.5F, -4.0F, 8, 1, 8, 0.0F);
        this.hat = new ModelRenderer(this, 0, 48);
        this.hat.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.hat.addBox(-4.5F, -19.0F, -4.5F, 9, 6, 9, 0.0F);
        this.hat6 = new ModelRenderer(this, 17, 57);
        this.hat6.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.hat6.addBox(-4.0F, -18.5F, 4.0F, 8, 5, 1, 0.0F);
        this.hat2 = new ModelRenderer(this, 15, 50);
        this.hat2.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.hat2.addBox(-5.0F, -18.5F, -4.0F, 1, 5, 8, 0.0F);
        this.hat3 = new ModelRenderer(this, 16, 50);
        this.hat3.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.hat3.addBox(4.0F, -18.5F, -4.0F, 1, 5, 8, 0.0F);
        this.leftArm = new ModelRenderer(this, 34, 10);
        this.leftArm.setRotationPoint(4.0F, -7.0F, 0.5F);
        this.leftArm.addBox(0.0F, 0.0F, -1.5F, 2, 9, 3, 0.0F);
        this.head = new ModelRenderer(this, 0, 34);
        this.head.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.head.addBox(-3.5F, -13.0F, -3.5F, 7, 5, 7, 0.0F);
        this.hat.addChild(this.hat5);
        this.body.addChild(this.cloth2);
        this.body.addChild(this.rightArm);
        this.body.addChild(this.cloth1);
        this.hat.addChild(this.hat4);
        this.head.addChild(this.hat);
        this.hat.addChild(this.hat6);
        this.hat.addChild(this.hat2);
        this.hat.addChild(this.hat3);
        this.body.addChild(this.leftArm);
        this.body.addChild(this.head);
    }

    @Override
    public void render(MatrixStack matrixStackIn, IVertexBuilder bufferIn, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha) {
        this.body.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
    }

    @Override
    public void setRotationAngles(T entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {

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
