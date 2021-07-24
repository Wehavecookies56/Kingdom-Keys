package online.kingdomkeys.kingdomkeys.client.model.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

import net.minecraft.client.model.Model;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.RenderType;

public class CubeModel extends Model {

    ModelPart Shape1;

    public CubeModel() {
        super(RenderType::entityCutoutNoCull);
        texWidth = 64;
        texHeight = 64;

        Shape1 = new ModelPart(this, 0, 0);
        Shape1.addBox(-4.0F, -4.0F, -4.0F, 8, 8, 8);
        Shape1.setPos(0F, 0F, 0F);
        Shape1.setTexSize(64, 64);
        Shape1.mirror = true;
        setRotation(Shape1, 0F, 0F, 0F);
    }

    private void setRotation(ModelPart model, float x, float y, float z) {
        model.getXRot() = x;
        model.getYRot() = y;
        model.zRot = z;
    }

    @Override
    public void renderToBuffer(PoseStack matrixStackIn, VertexConsumer bufferIn, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha) {
        Shape1.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
    }
}
