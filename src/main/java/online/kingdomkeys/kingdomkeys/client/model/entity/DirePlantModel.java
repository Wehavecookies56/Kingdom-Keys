package online.kingdomkeys.kingdomkeys.client.model.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.world.entity.Entity;

/**
 * DirePlant - WYND Created using Tabula 7.0.0
 */
public class DirePlantModel<T extends Entity> extends EntityModel<T> {
    public ModelPart body;
    public ModelPart leaf1;
    public ModelPart leaf2;
    public ModelPart leaf3;
    public ModelPart leaf4;
    public ModelPart body2;
    public ModelPart body3;
    public ModelPart neck;
    public ModelPart face1;
    public ModelPart petal1;
    public ModelPart petal2;
    public ModelPart petal3;
    public ModelPart petal4;

    public DirePlantModel() {
        this.texWidth = 64;
        this.texHeight = 64;
        this.leaf2 = new ModelPart(this, 45, 0);
        this.leaf2.setPos(-5.3F, 21.0F, 0.0F);
        this.leaf2.addBox(0.0F, -5.0F, -3.0F, 0, 9, 6, 0.0F);
        this.setRotateAngle(leaf2, 0.0F, 0.0F, -0.6108652381980153F);
        this.body3 = new ModelPart(this, 0, 0);
        this.body3.setPos(0.0F, 0.0F, 0.0F);
        this.body3.addBox(-1.5F, -16.0F, -1.5F, 3, 8, 3, 0.0F);
        this.petal1 = new ModelPart(this, 18, 38);
        this.petal1.setPos(0.0F, 0.0F, 0.0F);
        this.petal1.addBox(-12.9F, -19.8F, -3.5F, 4, 6, 0, 0.0F);
        this.setRotateAngle(petal1, 0.0F, 0.0F, 0.7853981633974483F);
        this.leaf1 = new ModelPart(this, 29, 0);
        this.leaf1.setPos(3.9F, 20.0F, 0.0F);
        this.leaf1.addBox(1.5F, -5.0F, -3.0F, 0, 9, 6, 0.0F);
        this.setRotateAngle(leaf1, 0.0F, 0.0F, 0.6108652381980153F);
        this.leaf3 = new ModelPart(this, 28, 21);
        this.leaf3.setPos(0.5F, 21.0F, -5.1F);
        this.leaf3.addBox(-3.5F, -5.0F, 0.0F, 6, 9, 0, 0.0F);
        this.setRotateAngle(leaf3, 0.6108652381980153F, 0.0F, 0.0F);
        this.petal2 = new ModelPart(this, 28, 38);
        this.petal2.setPos(0.0F, 0.0F, 0.0F);
        this.petal2.addBox(-12.9F, 2.2F, -3.5F, 4, 6, 0, 0.0F);
        this.setRotateAngle(petal2, 0.0F, 0.0F, 2.356194490192345F);
        this.petal4 = new ModelPart(this, 51, 38);
        this.petal4.setPos(0.0F, 0.0F, 0.0F);
        this.petal4.addBox(-12.9F, 13.8F, -3.5F, 4, 6, 0, 0.0F);
        this.setRotateAngle(petal4, 0.0F, 0.0F, 2.356194490192345F);
        this.leaf4 = new ModelPart(this, 45, 21);
        this.leaf4.setPos(0.3F, 21.0F, 5.0F);
        this.leaf4.addBox(-3.5F, -5.0F, 0.0F, 6, 9, 0, 0.0F);
        this.setRotateAngle(leaf4, -0.6108652381980153F, 0.0F, 0.0F);
        this.body2 = new ModelPart(this, 0, 15);
        this.body2.setPos(0.0F, 0.0F, 0.0F);
        this.body2.addBox(-2.5F, -8.0F, -2.5F, 5, 5, 5, 0.0F);
        this.petal3 = new ModelPart(this, 40, 38);
        this.petal3.setPos(0.0F, 0.0F, 0.0F);
        this.petal3.addBox(-12.9F, -8.2F, -3.5F, 4, 6, 0, 0.0F);
        this.setRotateAngle(petal3, 0.0F, 0.0F, 0.7853981633974483F);
        this.face1 = new ModelPart(this, 0, 37);
        this.face1.setPos(0.0F, 0.0F, 0.0F);
        this.face1.addBox(-3.5F, -19.0F, -4.0F, 7, 7, 1, 0.0F);
        this.body = new ModelPart(this, 0, 48);
        this.body.setPos(0.0F, 20.0F, 0.0F);
        this.body.addBox(-3.0F, -3.0F, -3.0F, 6, 7, 6, 0.0F);
        this.neck = new ModelPart(this, 0, 27);
        this.neck.setPos(0.0F, 0.0F, 0.0F);
        this.neck.addBox(-1.0F, -16.0F, -3.5F, 2, 2, 2, 0.0F);
        this.body2.addChild(this.body3);
        this.face1.addChild(this.petal1);
        this.face1.addChild(this.petal2);
        this.face1.addChild(this.petal4);
        this.body.addChild(this.body2);
        this.face1.addChild(this.petal3);
        this.neck.addChild(this.face1);
        this.body3.addChild(this.neck);
    }

    @Override
    public void renderToBuffer(PoseStack matrixStackIn, VertexConsumer bufferIn, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha) {
        this.leaf2.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
        this.leaf1.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
        this.leaf3.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
        this.leaf4.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
        this.body.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
    }

    @Override
    public void setupAnim(T entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {

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
