package online.kingdomkeys.kingdomkeys.client.model;

import java.util.HashMap;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;

import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.obj.OBJLoader;
import net.minecraftforge.client.model.obj.OBJModel;
import online.kingdomkeys.kingdomkeys.entity.organization.ChakramEntity;

public class ModelChakram extends EntityModel<ChakramEntity> {
	public ModelRenderer fist;

	public ModelChakram() {
		this.textureWidth = 32;
		this.textureHeight = 32;
		this.fist = new ModelRenderer(this, 0, 0);
		this.fist.setRotationPoint(0.0F, 0.0F, 0.0F);
		this.fist.addBox(-2.0F, -2.0F, -2.0F, 4, 4, 4, 0.0F);
	}

	/**
	 * This is a helper function from Tabula to set the rotation of model parts
	 */
	public void setRotateAngle(ModelRenderer modelRenderer, float x, float y, float z) {
		modelRenderer.rotateAngleX = x;
		modelRenderer.rotateAngleY = y;
		modelRenderer.rotateAngleZ = z;
	}
	
	@Override
	public void setRotationAngles(ChakramEntity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void render(MatrixStack matrixStackIn, IVertexBuilder bufferIn, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha) {
		matrixStackIn.push();
		
		matrixStackIn.translate(0, 0.1, 0);
		//matrixStackIn.rotate(Vector3f.YP.rotationDegrees());
		//rotate(new Vector3f(0,0.707,0.707).rotationDegrees(15)) 
		fist.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn,red,green,blue,alpha);
		matrixStackIn.pop();
	}

/*	OBJModel model;
    HashMap<String, IBakedModel> modelParts;

    public ModelChakram(String name) {
        try {
            model = (OBJModel) OBJLoader.INSTANCE.loadModel(new ResourceLocation("kk:models/item/" + name + ".obj"));
            modelParts = ModelHelper.getModelsForGroups(model);
        } catch (Exception e) {

        }
    }

    public void renderGroupObject (String groupName) {
        ModelHelper.renderBakedModel(modelParts.get(groupName));
    }

    @Override
    public void render (Entity entity, float p_78088_2_, float p_78088_3_, float p_78088_4_, float p_78088_5_, float p_78088_6_, float scale) {
        super.render(entity, p_78088_2_, p_78088_3_, p_78088_4_, p_78088_5_, p_78088_6_, scale);
        renderGroupObject(ModelHelper.ALL_PARTS);
    }

    private void setRotation (ModelRenderer model, float x, float y, float z) {
        model.rotateAngleX = x;
        model.rotateAngleY = y;
        model.rotateAngleZ = z;
    }

    @Override
    public void setRotationAngles (float f, float f1, float f2, float f3, float f4, float f5, Entity e) {
        super.setRotationAngles(f, f1, f2, f3, f4, f5, e);
    }*/
}