package online.kingdomkeys.kingdomkeys.client.render.entity;

import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.fmlclient.registry.IRenderFactory;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.client.model.entity.AssassinModel;
import online.kingdomkeys.kingdomkeys.entity.EntityHelper;
import online.kingdomkeys.kingdomkeys.entity.mob.AssassinEntity;

public class AssassinRenderer extends MobRenderer<AssassinEntity, AssassinModel<AssassinEntity>> {

    public static final AssassinRenderer.Factory FACTORY = new AssassinRenderer.Factory();

    public AssassinRenderer(EntityRenderDispatcher renderManagerIn) {
        super(renderManagerIn, new AssassinModel<>(), 0.35F);
    }

    @Override
    public void render(AssassinEntity entityIn, float entityYaw, float partialTicks, PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn) {
    	matrixStackIn.pushPose();
        {
	    	if (EntityHelper.getState(entityIn) == 1) {
	    		matrixStackIn.translate(0, -1.5F, 0);
	    	} else if(EntityHelper.getState(entityIn) == 2){
	    		matrixStackIn.translate(0, 0.3F, 0);
	    		if(entityIn.tickCount % 10 == 0) {
	    			matrixStackIn.scale(1.2F, 1.2F, 1.2F);
	    		}
	    	}
	        super.render(entityIn, entityYaw, partialTicks, matrixStackIn, bufferIn, packedLightIn);
    	}
    	matrixStackIn.popPose();
    }
    
    @Override
    public ResourceLocation getTextureLocation(AssassinEntity entity) {
        return new ResourceLocation(KingdomKeys.MODID, "textures/entity/mob/assassin.png");
    }

    @Override
    protected void scale(AssassinEntity entitylivingbaseIn, PoseStack matrixStackIn, float partialTickTime) {
    	matrixStackIn.scale(2,2,2);
    	super.scale(entitylivingbaseIn, matrixStackIn, partialTickTime);
    }
    
    public static class Factory implements IRenderFactory<AssassinEntity> {
        @Override
        public EntityRenderer<? super AssassinEntity> createRenderFor(EntityRenderDispatcher entityRendererManager) {
            return new AssassinRenderer(entityRendererManager);
        }
    }
}
