package online.kingdomkeys.kingdomkeys.client.render.entity;

import com.mojang.blaze3d.matrix.MatrixStack;

import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.client.model.entity.ShadowModel;
import online.kingdomkeys.kingdomkeys.entity.EntityHelper;
import online.kingdomkeys.kingdomkeys.entity.mob.MegaShadowEntity;

public class MegaShadowRenderer extends MobRenderer<MegaShadowEntity, ShadowModel<MegaShadowEntity>> {

	public static final MegaShadowRenderer.Factory FACTORY = new MegaShadowRenderer.Factory();

	public MegaShadowRenderer(EntityRendererManager renderManagerIn) {
		super(renderManagerIn, new ShadowModel<>(1D), 1F);
	}

	@Override
	public ResourceLocation getEntityTexture(MegaShadowEntity entity) {
		return new ResourceLocation(KingdomKeys.MODID, "textures/entity/mob/shadow.png");
	}

	@Override
	public void render(MegaShadowEntity entityIn, float entityYaw, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn) {
		matrixStackIn.push();
        {
	       
	    	if (EntityHelper.getState(entityIn) == 1) {
	            matrixStackIn.scale(1.5F, 0.01F, 1.5F);
	        }
	        super.render(entityIn, entityYaw, partialTicks, matrixStackIn, bufferIn, packedLightIn);
    	}
    	matrixStackIn.pop();
    }

	@Override
	protected void preRenderCallback(MegaShadowEntity entitylivingbaseIn, MatrixStack matrixStackIn, float partialTickTime) {
		matrixStackIn.scale(2.5F, 2.5F, 2.5F);
		super.preRenderCallback(entitylivingbaseIn, matrixStackIn, partialTickTime);
	}

	public static class Factory implements IRenderFactory<MegaShadowEntity> {
		@Override
		public EntityRenderer<? super MegaShadowEntity> createRenderFor(EntityRendererManager entityRendererManager) {
			return new MegaShadowRenderer(entityRendererManager);
		}
	}
}
