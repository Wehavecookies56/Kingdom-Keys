package online.kingdomkeys.kingdomkeys.client.render.entity;

import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.fmlclient.registry.IRenderFactory;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.client.model.entity.ShadowModel;
import online.kingdomkeys.kingdomkeys.entity.EntityHelper;
import online.kingdomkeys.kingdomkeys.entity.mob.GigaShadowEntity;

public class GigaShadowRenderer extends MobRenderer<GigaShadowEntity, ShadowModel<GigaShadowEntity>> {

    public static final GigaShadowRenderer.Factory FACTORY = new GigaShadowRenderer.Factory();

    public GigaShadowRenderer(EntityRenderDispatcher renderManagerIn) {
        super(renderManagerIn, new ShadowModel<>(1D), 1.5F);
    }

    @Override
    public ResourceLocation getTextureLocation(GigaShadowEntity entity) {
        return new ResourceLocation(KingdomKeys.MODID, "textures/entity/mob/shadow.png");
    }

    @Override
    public void render(GigaShadowEntity entityIn, float entityYaw, float partialTicks, PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn) {
    	matrixStackIn.pushPose();
        {
	       
	    	if (EntityHelper.getState(entityIn) == 1) {
	            matrixStackIn.scale(1.5F, 0.01F, 1.5F);
	        }
	        super.render(entityIn, entityYaw, partialTicks, matrixStackIn, bufferIn, packedLightIn);
    	}
    	matrixStackIn.popPose();
    }

    @Override
    protected void scale(GigaShadowEntity entitylivingbaseIn, PoseStack matrixStackIn, float partialTickTime) {
    	matrixStackIn.scale(4F, 4F, 4F);
    	super.scale(entitylivingbaseIn, matrixStackIn, partialTickTime);
    }
    
    public static class Factory implements IRenderFactory<GigaShadowEntity> {
        @Override
        public EntityRenderer<? super GigaShadowEntity> createRenderFor(EntityRenderDispatcher entityRendererManager) {
            return new GigaShadowRenderer(entityRendererManager);
        }
    }
}
