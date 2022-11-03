package online.kingdomkeys.kingdomkeys.client.render.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.client.model.entity.CubeModel;
import online.kingdomkeys.kingdomkeys.entity.organization.ArrowgunShotEntity;

import javax.annotation.Nullable;

@OnlyIn(Dist.CLIENT)
public class ArrowgunShotEntityRenderer extends EntityRenderer<ArrowgunShotEntity> {

	private CubeModel model;
	private float[] color;

	public ArrowgunShotEntityRenderer(EntityRendererProvider.Context context) {
		super(context);
		model = new CubeModel<>(context.bakeLayer(CubeModel.LAYER_LOCATION));
		this.shadowRadius = 0.25F;
	}

	@Override
	public void render(ArrowgunShotEntity entity, float entityYaw, float partialTicks, PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn) {
		switch(entity.getShotType()) {
		case 0:
			color = new float[] {1F,0.2F,0.2F};
			break;
		case 1:
			color = new float[] {0.3F,0.3F,1F};
			break;
		}
		matrixStackIn.pushPose();
    	{	
    		matrixStackIn.translate(0, 0.05, 0);
    		matrixStackIn.mulPose(Vector3f.YP.rotationDegrees(entity.yRotO + (entity.getYRot() - entity.yRotO)));
    		matrixStackIn.mulPose(Vector3f.XN.rotationDegrees(entity.xRotO + (entity.getXRot() - entity.xRotO)));
    		matrixStackIn.scale(0.1F, 0.1F, 0.8F);
    		model.renderToBuffer(matrixStackIn, bufferIn.getBuffer(model.renderType(getTextureLocation(entity))), packedLightIn, OverlayTexture.NO_OVERLAY, color[0],color[1],color[2], 1F);
     	}
     	matrixStackIn.popPose();
		super.render(entity, entityYaw, partialTicks, matrixStackIn, bufferIn, packedLightIn);
	}

	@Nullable
	@Override
	public ResourceLocation getTextureLocation(ArrowgunShotEntity entity) {
		return new ResourceLocation(KingdomKeys.MODID, "textures/entity/models/fire.png");
	}
}
