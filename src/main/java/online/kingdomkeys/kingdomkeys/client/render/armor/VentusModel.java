package online.kingdomkeys.kingdomkeys.client.render.armor;

import javax.annotation.Nonnull;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;

import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.ResourceLocation;
import online.kingdomkeys.kingdomkeys.KingdomKeys;

public class VentusModel extends BipedModel {
		
	    public ModelRenderer horn1;
	    public ModelRenderer horn2;

		public VentusModel(float size) {
			super(size, 0, 64, 64);
	        
	        this.horn2 = new ModelRenderer(this, 0, 0);
	        this.horn2.setRotationPoint(-4.0F, -12.0F, 0.0F);
	        this.horn2.setTextureOffset(0, 32).addBox(0.0F, 0.0F, 0.0F, 1.0F, 4.5F, 2.0F, 0.0F, 0.0F, 0.0F);
	        this.setRotateAngle(horn2, -0.27366763203903305F, 0.0F, 0.0F);
	       
	        this.horn1 = new ModelRenderer(this, 0, 0);
	        this.horn1.setRotationPoint(3.0F, -12.0F, 0.0F);
	        this.horn1.setTextureOffset(0, 32).addBox(0.0F, 0.0F, 0.0F, 1.0F, 4.5F, 2.0F, 0.0F, 0.0F, 0.0F);
	        this.setRotateAngle(horn1, -0.27366763203903305F, 0.0F, 0.0F);
	        
	        this.bipedHead.addChild(horn1);
	        this.bipedHead.addChild(horn2);
	    }

	
	@Override
	public void render(MatrixStack matrixStackIn, IVertexBuilder bufferIn, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha) {
		super.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
         /*this.getHeadParts().forEach((model) -> {
            ((ModelRenderer) model).render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
         });
         
         this.getBodyParts().forEach((model) -> {
            ((ModelRenderer) model).render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
         });*/
	     
		//this.bipedHead.render(matrixStackIn, bufferIn, packedLightIn, OverlayTexture.NO_OVERLAY, 1F, 1F, 1F, 1F);

		/*this.rightArm.render(matrixStackIn, bufferIn, packedLightIn, OverlayTexture.NO_OVERLAY, 1F, 1F, 1F, 1F);
		this.leftArm.render(matrixStackIn, bufferIn, packedLightIn, OverlayTexture.NO_OVERLAY, 1F, 1F, 1F, 1F);

		this.body.render(matrixStackIn, bufferIn, packedLightIn, OverlayTexture.NO_OVERLAY, 1F, 1F, 1F, 1F);

		this.leftLeg.render(matrixStackIn, bufferIn, packedLightIn, OverlayTexture.NO_OVERLAY, 1F, 1F, 1F, 1F);
		this.rightLeg.render(matrixStackIn, bufferIn, packedLightIn, OverlayTexture.NO_OVERLAY, 1F, 1F, 1F, 1F);*/
	}

    public void setRotateAngle(ModelRenderer modelRenderer, float x, float y, float z) {
        modelRenderer.rotateAngleX = x;
        modelRenderer.rotateAngleY = y;
        modelRenderer.rotateAngleZ = z;
    }


}
