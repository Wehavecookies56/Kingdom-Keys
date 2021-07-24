package online.kingdomkeys.kingdomkeys.client.render.entity;

import javax.annotation.Nullable;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;

import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fmlclient.registry.IRenderFactory;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.client.model.entity.CubeModel;
import online.kingdomkeys.kingdomkeys.entity.organization.ArrowgunShotEntity;

@OnlyIn(Dist.CLIENT)
public class ArrowgunShotEntityRenderer extends EntityRenderer<ArrowgunShotEntity> {

	public static final Factory FACTORY = new ArrowgunShotEntityRenderer.Factory();
	private CubeModel model;

	public ArrowgunShotEntityRenderer(EntityRenderDispatcher renderManager) {
		super(renderManager);
        model = new CubeModel();
		this.shadowRadius = 0.25F;
	}

	@Override
	public void render(ArrowgunShotEntity entity, float entityYaw, float partialTicks, PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn) {
		matrixStackIn.pushPose();
    	{	
    		matrixStackIn.translate(0, 0.05, 0);
    		matrixStackIn.mulPose(Vector3f.YP.rotationDegrees(entity.getYRot()O + (entity.getYRot() - entity.getYRot()O)));
    		matrixStackIn.mulPose(Vector3f.XN.rotationDegrees(entity.getXRot()O + (entity.getXRot() - entity.getXRot()O)));
    		matrixStackIn.scale(0.1F, 0.1F, 0.8F);
    		model.renderToBuffer(matrixStackIn, bufferIn.getBuffer(model.renderType(getTextureLocation(entity))), packedLightIn, OverlayTexture.NO_OVERLAY, 1F, 0.2F, 0.2F, 1F);
     	}
     	matrixStackIn.popPose();
		super.render(entity, entityYaw, partialTicks, matrixStackIn, bufferIn, packedLightIn);
	}

	@Nullable
	@Override
	public ResourceLocation getTextureLocation(ArrowgunShotEntity entity) {
		return new ResourceLocation(KingdomKeys.MODID, "textures/entity/models/fire.png");
	}

	public static class Factory implements IRenderFactory<ArrowgunShotEntity> {
		@Override
		public EntityRenderer<? super ArrowgunShotEntity> createRenderFor(EntityRenderDispatcher manager) {
			return new ArrowgunShotEntityRenderer(manager);
		}
	}
}
