package online.kingdomkeys.kingdomkeys.client.model.entity;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;

import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.model.Model;
import net.minecraft.client.renderer.model.ModelRenderer;

public class CubeModel extends Model {

    ModelRenderer Shape1;

    public CubeModel() {
        super(RenderType::getEntityCutoutNoCull);
        textureWidth = 64;
        textureHeight = 64;

        Shape1 = new ModelRenderer(this, 0, 0);
        Shape1.addBox(-4.0F, -4.0F, -4.0F, 8, 8, 8);
        Shape1.setRotationPoint(0F, 0F, 0F);
        Shape1.setTextureSize(64, 64);
        Shape1.mirror = true;
        setRotation(Shape1, 0F, 0F, 0F);
    }

    private void setRotation(ModelRenderer model, float x, float y, float z) {
        model.rotateAngleX = x;
        model.rotateAngleY = y;
        model.rotateAngleZ = z;
    }

    @Override
    public void render(MatrixStack matrixStackIn, IVertexBuilder bufferIn, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha) {
        Shape1.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
    }
}
