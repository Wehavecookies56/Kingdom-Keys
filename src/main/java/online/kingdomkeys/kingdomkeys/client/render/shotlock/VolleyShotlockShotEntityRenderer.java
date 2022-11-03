package online.kingdomkeys.kingdomkeys.client.render.shotlock;

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
import online.kingdomkeys.kingdomkeys.entity.shotlock.BaseShotlockShotEntity;

import javax.annotation.Nullable;
import java.awt.*;

@OnlyIn(Dist.CLIENT)
public class VolleyShotlockShotEntityRenderer extends EntityRenderer<BaseShotlockShotEntity> {

	private CubeModel model;

	public VolleyShotlockShotEntityRenderer(EntityRendererProvider.Context context) {
		super(context);
        model = new CubeModel(context.bakeLayer(CubeModel.LAYER_LOCATION));
		this.shadowRadius = 0.25F;
	}

	@Override
	public void render(BaseShotlockShotEntity entity, float entityYaw, float partialTicks, PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn) {
		matrixStackIn.pushPose();
    	{	
    		matrixStackIn.translate(0, 0.05, 0);
    		matrixStackIn.mulPose(Vector3f.YP.rotationDegrees(entity.yRotO + (entity.getYRot() - entity.yRotO)));
    		matrixStackIn.mulPose(Vector3f.XN.rotationDegrees(entity.xRotO + (entity.getXRot() - entity.xRotO)));
			
    		matrixStackIn.scale(0.3F, 0.3F, 0.3F);
    		Color color = new Color(entity.getColor());
    		model.renderToBuffer(matrixStackIn, bufferIn.getBuffer(model.renderType(getTextureLocation(entity))), packedLightIn, OverlayTexture.NO_OVERLAY, color.getRed()/255, color.getGreen()/255F, color.getBlue()/255F, 1F);
     	}
     	matrixStackIn.popPose();
		super.render(entity, entityYaw, partialTicks, matrixStackIn, bufferIn, packedLightIn);
	}

	@Nullable
	@Override
	public ResourceLocation getTextureLocation(BaseShotlockShotEntity entity) {
		return new ResourceLocation(KingdomKeys.MODID, "textures/entity/models/fire.png");
	}
}