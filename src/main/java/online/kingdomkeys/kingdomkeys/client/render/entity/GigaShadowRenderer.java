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
import online.kingdomkeys.kingdomkeys.entity.mob.GigaShadowEntity;
import online.kingdomkeys.kingdomkeys.entity.mob.LargeBodyEntity;
import online.kingdomkeys.kingdomkeys.entity.mob.ShadowEntity;

public class GigaShadowRenderer extends MobRenderer<GigaShadowEntity, ShadowModel<GigaShadowEntity>> {

    public static final GigaShadowRenderer.Factory FACTORY = new GigaShadowRenderer.Factory();

    public GigaShadowRenderer(EntityRendererManager renderManagerIn) {
        super(renderManagerIn, new ShadowModel<>(1D), 2.0F);
    }

    @Override
    public ResourceLocation getEntityTexture(GigaShadowEntity entity) {
        return new ResourceLocation(KingdomKeys.MODID, "textures/entity/mob/shadow.png");
    }

    @Override
    public void render(GigaShadowEntity entityIn, float entityYaw, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn) {
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
    protected void preRenderCallback(GigaShadowEntity entitylivingbaseIn, MatrixStack matrixStackIn, float partialTickTime) {
    	matrixStackIn.scale(4F, 4F, 4F);
    	super.preRenderCallback(entitylivingbaseIn, matrixStackIn, partialTickTime);
    }
    
    public static class Factory implements IRenderFactory<GigaShadowEntity> {
        @Override
        public EntityRenderer<? super GigaShadowEntity> createRenderFor(EntityRendererManager entityRendererManager) {
            return new GigaShadowRenderer(entityRendererManager);
        }
    }
}
