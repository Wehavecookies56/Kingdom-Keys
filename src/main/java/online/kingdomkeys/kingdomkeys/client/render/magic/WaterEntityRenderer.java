package online.kingdomkeys.kingdomkeys.client.render.magic;

import javax.annotation.Nullable;

import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fmlclient.registry.IRenderFactory;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.client.model.BlizzardModel;
import online.kingdomkeys.kingdomkeys.entity.magic.WaterEntity;

@OnlyIn(Dist.CLIENT)
public class WaterEntityRenderer extends EntityRenderer<WaterEntity> {

	public static final Factory FACTORY = new WaterEntityRenderer.Factory();
	BlizzardModel shot;

	public WaterEntityRenderer(EntityRenderDispatcher renderManager, BlizzardModel fist) {
		super(renderManager);
		this.shot = fist;
		this.shadowRadius = 0.25F;
	}

	@Override
	public void render(WaterEntity entity, float entityYaw, float partialTicks, PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn) {
		/*matrixStackIn.push();
		{
			float r = 1, g = 0, b = 0;
				
			matrixStackIn.rotate(Vector3f.YP.rotationDegrees(entity.prevRotationYaw + (entity.rotationYaw - entity.prevRotationYaw)));
			matrixStackIn.rotate(Vector3f.XN.rotationDegrees(entity.prevRotationPitch + (entity.rotationPitch - entity.prevRotationPitch)));

			if (entity.ticksExisted > 1) //Prevent entity rendering in your face
				shot.render(matrixStackIn, bufferIn.getBuffer(shot.getRenderType(getEntityTexture(entity))), packedLightIn, OverlayTexture.NO_OVERLAY, r, g, b, 1F);

		}
		matrixStackIn.pop();*/
		super.render(entity, entityYaw, partialTicks, matrixStackIn, bufferIn, packedLightIn);
	}

	@Nullable
	@Override
	public ResourceLocation getTextureLocation(WaterEntity entity) {
		return new ResourceLocation(KingdomKeys.MODID, "textures/entity/models/fire.png");
	}

	public static class Factory implements IRenderFactory<WaterEntity> {
		@Override
		public EntityRenderer<? super WaterEntity> createRenderFor(EntityRenderDispatcher manager) {
			return new WaterEntityRenderer(manager, new BlizzardModel());
		}
	}
}
