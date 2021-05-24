package online.kingdomkeys.kingdomkeys.client.render.entity;

import com.mojang.blaze3d.matrix.MatrixStack;

import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.client.model.entity.AssassinModel;
import online.kingdomkeys.kingdomkeys.entity.EntityHelper;
import online.kingdomkeys.kingdomkeys.entity.mob.AssassinEntity;

public class AssassinRenderer extends MobRenderer<AssassinEntity, AssassinModel<AssassinEntity>> {

    public static final AssassinRenderer.Factory FACTORY = new AssassinRenderer.Factory();

    public AssassinRenderer(EntityRendererManager renderManagerIn) {
        super(renderManagerIn, new AssassinModel<>(), 0.35F);
    }

    @Override
    public void render(AssassinEntity entityIn, float entityYaw, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn) {
    	matrixStackIn.push();
        {
	    	if (EntityHelper.getState(entityIn) == 1) {
	    		matrixStackIn.translate(0, -1.5F, 0);
	    	} else if(EntityHelper.getState(entityIn) == 2){
	    		matrixStackIn.translate(0, 0.3F, 0);
	    		if(entityIn.ticksExisted % 10 == 0) {
	    			matrixStackIn.scale(1.2F, 1.2F, 1.2F);
	    		}
	    	}
	        super.render(entityIn, entityYaw, partialTicks, matrixStackIn, bufferIn, packedLightIn);
    	}
    	matrixStackIn.pop();
    }
    
    @Override
    public ResourceLocation getEntityTexture(AssassinEntity entity) {
        return new ResourceLocation(KingdomKeys.MODID, "textures/entity/mob/assassin.png");
    }

    @Override
    protected void preRenderCallback(AssassinEntity entitylivingbaseIn, MatrixStack matrixStackIn, float partialTickTime) {
    	matrixStackIn.scale(2,2,2);
    	super.preRenderCallback(entitylivingbaseIn, matrixStackIn, partialTickTime);
    }
    
    public static class Factory implements IRenderFactory<AssassinEntity> {
        @Override
        public EntityRenderer<? super AssassinEntity> createRenderFor(EntityRendererManager entityRendererManager) {
            return new AssassinRenderer(entityRendererManager);
        }
    }
}
