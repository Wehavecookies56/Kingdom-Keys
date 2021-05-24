package online.kingdomkeys.kingdomkeys.client.render.shotlock;

import javax.annotation.Nullable;

import com.mojang.blaze3d.matrix.MatrixStack;

import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.client.model.entity.CubeModel;
import online.kingdomkeys.kingdomkeys.entity.shotlock.VolleyShotEntity;

@OnlyIn(Dist.CLIENT)
public class VolleyShotlockShotEntityRenderer extends EntityRenderer<VolleyShotEntity> {

	public static final Factory FACTORY = new VolleyShotlockShotEntityRenderer.Factory();
	private CubeModel model;

	public VolleyShotlockShotEntityRenderer(EntityRendererManager renderManager) {
		super(renderManager);
        model = new CubeModel();
		this.shadowSize = 0.25F;
	}

	@Override
	public void render(VolleyShotEntity entity, float entityYaw, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn) {
		matrixStackIn.push();
    	{	
    		matrixStackIn.translate(0, 0.05, 0);
    		matrixStackIn.rotate(Vector3f.YP.rotationDegrees(entity.prevRotationYaw + (entity.rotationYaw - entity.prevRotationYaw)));
    		matrixStackIn.rotate(Vector3f.XN.rotationDegrees(entity.prevRotationPitch + (entity.rotationPitch - entity.prevRotationPitch)));
    		if(entity.getMotion().equals(new Vector3d(0,0,0))) {
    			matrixStackIn.scale(0.3F, 0.3F, 0.3F);
    		} else {
    			matrixStackIn.scale(0.2F, 0.2F, 0.8F);
    		}
    		model.render(matrixStackIn, bufferIn.getBuffer(model.getRenderType(getEntityTexture(entity))), packedLightIn, OverlayTexture.NO_OVERLAY, 0.3F, 0.1F, 0.3F, 1F);
     	}
     	matrixStackIn.pop();
		super.render(entity, entityYaw, partialTicks, matrixStackIn, bufferIn, packedLightIn);
	}

	@Nullable
	@Override
	public ResourceLocation getEntityTexture(VolleyShotEntity entity) {
		return new ResourceLocation(KingdomKeys.MODID, "textures/entity/models/fire.png");
	}

	public static class Factory implements IRenderFactory<VolleyShotEntity> {
		@Override
		public EntityRenderer<? super VolleyShotEntity> createRenderFor(EntityRendererManager manager) {
			return new VolleyShotlockShotEntityRenderer(manager);
		}
	}
}