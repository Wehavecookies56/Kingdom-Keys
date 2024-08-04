/* We're not using this right now so I'll fix it when we want to
package online.kingdomkeys.kingdomkeys.client.model.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.world.entity.Entity;

 //WhiteMushroom - WYND Created using Tabula 7.0.0

public class WhiteMushroomModel<T extends Entity> extends EntityModel<T> {
    public ModelPart body;
    public ModelPart cloth1;
    public ModelPart head;
    public ModelPart cloth2;
    public ModelPart leftArm;
    public ModelPart rightArm;
    public ModelPart hat;
    public ModelPart hat2;
    public ModelPart hat3;
    public ModelPart hat4;
    public ModelPart hat5;
    public ModelPart hat6;

    public WhiteMushroomModel() {
        this.texWidth = 64;
        this.texHeight = 64;
        this.hat5 = new ModelPart(this, 30, 1);
        this.hat5.setPos(0.0F, 0.0F, 0.0F);
        this.hat5.addBox(-4.0F, -18.5F, -5.0F, 8, 5, 1, 0.0F);
        this.body = new ModelPart(this, 0, 0);
        this.body.setPos(0.0F, 15.0F, 0.0F);
        this.body.addBox(-4.0F, -8.0F, -4.0F, 8, 17, 8, 0.0F);
        this.cloth2 = new ModelPart(this, 28, 46);
        this.cloth2.setPos(0.0F, 0.0F, 0.0F);
        this.cloth2.addBox(-4.5F, -8.0F, -4.5F, 9, 2, 9, 0.0F);
        this.rightArm = new ModelPart(this, 34, 10);
        this.rightArm.setPos(-4.5F, -7.0F, 0.5F);
        this.rightArm.addBox(-1.5F, 0.0F, -1.5F, 2, 9, 3, 0.0F);
        this.cloth1 = new ModelPart(this, 24, 25);
        this.cloth1.setPos(0.0F, 0.0F, 0.0F);
        this.cloth1.addBox(-5.0F, 5.0F, -5.0F, 10, 4, 10, 0.0F);
        this.hat4 = new ModelPart(this, 4, 55);
        this.hat4.setPos(0.0F, 0.0F, 0.0F);
        this.hat4.addBox(-4.0F, -19.5F, -4.0F, 8, 1, 8, 0.0F);
        this.hat = new ModelPart(this, 0, 48);
        this.hat.setPos(0.0F, 0.0F, 0.0F);
        this.hat.addBox(-4.5F, -19.0F, -4.5F, 9, 6, 9, 0.0F);
        this.hat6 = new ModelPart(this, 17, 57);
        this.hat6.setPos(0.0F, 0.0F, 0.0F);
        this.hat6.addBox(-4.0F, -18.5F, 4.0F, 8, 5, 1, 0.0F);
        this.hat2 = new ModelPart(this, 15, 50);
        this.hat2.setPos(0.0F, 0.0F, 0.0F);
        this.hat2.addBox(-5.0F, -18.5F, -4.0F, 1, 5, 8, 0.0F);
        this.hat3 = new ModelPart(this, 16, 50);
        this.hat3.setPos(0.0F, 0.0F, 0.0F);
        this.hat3.addBox(4.0F, -18.5F, -4.0F, 1, 5, 8, 0.0F);
        this.leftArm = new ModelPart(this, 34, 10);
        this.leftArm.setPos(4.0F, -7.0F, 0.5F);
        this.leftArm.addBox(0.0F, 0.0F, -1.5F, 2, 9, 3, 0.0F);
        this.head = new ModelPart(this, 0, 34);
        this.head.setPos(0.0F, 0.0F, 0.0F);
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
    public void renderToBuffer(PoseStack matrixStackIn, VertexConsumer bufferIn, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha) {
        this.body.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
    }

    @Override
    public void setupAnim(T entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {

    }

}
*/