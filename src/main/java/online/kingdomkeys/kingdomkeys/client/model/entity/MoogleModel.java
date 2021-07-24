package online.kingdomkeys.kingdomkeys.client.model.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.world.entity.Entity;

public class MoogleModel<T extends Entity> extends EntityModel<T> {

    public ModelPart shape1;
    public ModelPart shape2;
    public ModelPart shape3;
    public ModelPart shape4;
    public ModelPart shape5;
    public ModelPart shape7;
    public ModelPart shape8;
    public ModelPart shape6;
    public ModelPart shape9;
    public ModelPart shape11;
    public ModelPart shape13;
    public ModelPart shape14;
    public ModelPart shape12;

    public MoogleModel() {
        this.texWidth = 64;
        this.texHeight = 32;
        this.shape13 = new ModelPart(this, 16, 30);
        this.shape13.setPos(-2.0F, 1.0F, -2.0F);
        this.shape13.addBox(0.0F, 0.0F, 0.0F, 1, 1, 1, 0.0F);
        this.shape6 = new ModelPart(this, 10, 10);
        this.shape6.setPos(-4.5F, 10.5F, -0.5F);
        this.shape6.addBox(0.0F, 0.0F, 0.0F, 3, 1, 1, 0.0F);
        this.setRotateAngle(shape6, 0.0F, 0.0F, -0.4363323129985824F);
        this.shape7 = new ModelPart(this, 21, 0);
        this.shape7.setPos(2.0F, 9.3F, -0.6F);
        this.shape7.addBox(0.0F, 0.0F, 0.0F, 3, 1, 1, 0.0F);
        this.setRotateAngle(shape7, 0.0F, 0.0F, 0.4363323129985824F);
        this.shape3 = new ModelPart(this, 0, 22);
        this.shape3.setPos(-0.5F, -1.0F, -0.5F);
        this.shape3.addBox(0.0F, 0.0F, 0.0F, 1, 3, 1, 0.0F);
        this.shape1 = new ModelPart(this, 0, 0);
        this.shape1.setPos(-1.5F, -4.0F, -1.5F);
        this.shape1.addBox(0.0F, 0.0F, 0.0F, 3, 3, 3, 0.0F);
        this.shape9 = new ModelPart(this, 21, 8);
        this.shape9.setPos(0.5F, 12.0F, 0.0F);
        this.shape9.addBox(0.0F, 0.0F, 0.0F, 1, 3, 1, 0.0F);
        this.setRotateAngle(shape9, 0.5759586531581287F, 0.0F, 0.0F);
        this.shape8 = new ModelPart(this, 0, 9);
        this.shape8.setPos(-1.5F, 12.0F, 0.0F);
        this.shape8.addBox(0.0F, 0.0F, 0.0F, 1, 3, 1, 0.0F);
        this.setRotateAngle(shape8, 0.5759586531581287F, 0.0F, 0.0F);
        this.shape14 = new ModelPart(this, 25, 30);
        this.shape14.setPos(1.0F, 1.0F, -2.0F);
        this.shape14.addBox(0.0F, 0.0F, 0.0F, 1, 1, 1, 0.0F);
        this.shape5 = new ModelPart(this, 33, 0);
        this.shape5.setPos(-2.0F, 8.0F, -2.0F);
        this.shape5.addBox(0.0F, 0.0F, 0.0F, 4, 4, 4, 0.0F);
        this.shape11 = new ModelPart(this, 29, 9);
        this.shape11.setPos(1.0F, 8.5F, 2.0F);
        this.shape11.addBox(0.0F, 0.0F, 0.0F, 1, 1, 3, 0.0F);
        this.setRotateAngle(shape11, -0.18203784098300857F, 0.5918411493512771F, 0.0F);
        this.shape2 = new ModelPart(this, 35, 20);
        this.shape2.setPos(-3.0F, 2.0F, -3.0F);
        this.shape2.addBox(0.0F, 0.0F, 0.0F, 6, 6, 6, 0.0F);
        this.shape12 = new ModelPart(this, 29, 9);
        this.shape12.setPos(-2.0F, 8.5F, 2.0F);
        this.shape12.addBox(0.0F, 0.0F, 0.0F, 1, 1, 3, 0.0F);
        this.setRotateAngle(shape12, -0.18203784098300857F, -0.5918411493512771F, 0.0F);
        this.shape4 = new ModelPart(this, 0, 15);
        this.shape4.setPos(-1.5F, 4.0F, -4.0F);
        this.shape4.addBox(0.0F, 0.0F, 0.0F, 3, 2, 1, 0.0F);
    }

    @Override
    public void renderToBuffer(PoseStack matrixStackIn, VertexConsumer bufferIn, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha) {
        this.shape13.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn);
        this.shape6.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn);
        this.shape7.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn);
        this.shape3.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn);
        this.shape1.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn);
        this.shape9.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn);
        this.shape8.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn);
        this.shape14.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn);
        this.shape5.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn);
        this.shape11.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn);
        this.shape2.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn);
        this.shape12.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn);
        this.shape4.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn);
    }

    @Override
    public void setupAnim(Entity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {

    }

    public void setRotateAngle(ModelPart modelRenderer, float x, float y, float z) {
        modelRenderer.getXRot() = x;
        modelRenderer.getYRot() = y;
        modelRenderer.zRot = z;
    }

}
