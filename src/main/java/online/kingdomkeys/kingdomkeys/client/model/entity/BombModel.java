package online.kingdomkeys.kingdomkeys.client.model.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.world.entity.Entity;

/**
 * ModelMinuteBomb - Nathan
 * Created using Tabula 7.0.0
 */
public class BombModel<T extends Entity> extends EntityModel<T> {
    public float[] modelScale = new float[] { 1.0F, 1.2F, 1.0F };
    public ModelPart headMain;
    public ModelPart TopHead;
    public ModelPart BottemHead;
    public ModelPart Wick1;
    public ModelPart wick2;
    public ModelPart wick3;
    public ModelPart wick4;
    public ModelPart wick5;
    public ModelPart body;
    public ModelPart rightleg;
    public ModelPart leftleg;

    public BombModel() {
        this.texWidth = 64;
        this.texHeight = 64;
        this.wick5 = new ModelPart(this, 25, 14);
        this.wick5.setPos(0.0F, -1.0F, 2.0F);
        this.wick5.addBox(-0.5F, -0.5F, -0.5F, 1, 1, 1, 0.0F);
        this.BottemHead = new ModelPart(this, 37, 11);
        this.BottemHead.setPos(0.0F, 2.2F, 0.0F);
        this.BottemHead.addBox(-3.0F, -1.0F, -3.0F, 6, 2, 6, 0.0F);
        this.setRotateAngle(BottemHead, -0.01361356816555577F, 0.0F, 0.0F);
        this.leftleg = new ModelPart(this, 21, 23);
        this.leftleg.setPos(-1.3F, 4.4F, 0.0F);
        this.leftleg.addBox(-0.5F, -0.5F, -0.5F, 1, 5, 1, 0.0F);
        this.wick3 = new ModelPart(this, 10, 13);
        this.wick3.setPos(2.0F, -1.0F, 0.0F);
        this.wick3.addBox(-0.5F, -0.5F, -0.5F, 1, 3, 1, 0.0F);
        this.setRotateAngle(wick3, 0.0F, 0.0F, 3.141592653589793F);
        this.body = new ModelPart(this, 3, 22);
        this.body.setPos(0.0F, 19.900000000000023F, 0.0F);
        this.body.addBox(-1.0F, -1.0F, -1.0F, 2, 6, 2, 0.0F);
        this.wick2 = new ModelPart(this, 3, 13);
        this.wick2.setPos(0.0F, -1.0F, 0.0F);
        this.wick2.addBox(-0.5F, -0.5F, -0.5F, 3, 1, 1, 0.0F);
        this.setRotateAngle(wick2, 0.0F, 3.141592653589793F, 0.0F);
        this.headMain = new ModelPart(this, 0, 0);
        this.headMain.setPos(0.0F, 15.79999999999996F, 0.0F);
        this.headMain.addBox(-2.5F, -2.5F, -2.5F, 5, 4, 5, 0.0F);
        this.wick4 = new ModelPart(this, 14, 13);
        this.wick4.setPos(0.0F, 3.0F, 0.0F);
        this.wick4.addBox(-0.5F, -0.5F, -0.5F, 1, 1, 3, 0.0F);
        this.setRotateAngle(wick4, 0.0F, 1.5707963267948966F, 0.0F);
        this.rightleg = new ModelPart(this, 14, 24);
        this.rightleg.setPos(1.3F, 4.4F, 0.0F);
        this.rightleg.addBox(-0.5F, -0.5F, -0.5F, 1, 5, 1, 0.0F);
        this.TopHead = new ModelPart(this, 24, 0);
        this.TopHead.setPos(0.5F, -3.9F, 0.5F);
        this.TopHead.addBox(-3.5F, -1.0F, -3.5F, 6, 3, 6, 0.0F);
        this.setRotateAngle(TopHead, -0.009948376736367679F, 0.009948376736367679F, 0.0F);
        this.Wick1 = new ModelPart(this, 0, 13);
        this.Wick1.setPos(-0.6F, -2.5F, -0.6F);
        this.Wick1.addBox(-0.5F, -0.5F, -0.5F, 1, 2, 1, 0.0F);
        this.wick4.addChild(this.wick5);
        this.headMain.addChild(this.BottemHead);
        this.body.addChild(this.leftleg);
        this.wick2.addChild(this.wick3);
        this.Wick1.addChild(this.wick2);
        this.wick3.addChild(this.wick4);
        this.body.addChild(this.rightleg);
        this.headMain.addChild(this.TopHead);
        this.TopHead.addChild(this.Wick1);
    }

    @Override
    public void renderToBuffer(PoseStack matrixStackIn, VertexConsumer bufferIn, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha) {
        matrixStackIn.pushPose();
        matrixStackIn.scale(1F / modelScale[0], 1F / modelScale[1], 1F / modelScale[2]);
        this.body.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
        this.headMain.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
        matrixStackIn.popPose();
    }

    /**
     * This is a helper function from Tabula to set the rotation of model parts
     */
    public void setRotateAngle(ModelPart modelRenderer, float x, float y, float z) {
        modelRenderer.getXRot() = x;
        modelRenderer.getYRot() = y;
        modelRenderer.zRot = z;
    }

    @Override
    public void setupAnim(T entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {

    }
}