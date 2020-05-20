package online.kingdomkeys.kingdomkeys.client.render.entity;

import com.mojang.blaze3d.matrix.MatrixStack;

import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.client.model.entity.ShadowGlobModel;
import online.kingdomkeys.kingdomkeys.entity.mob.ShadowGlobEntity;

public class ShadowGlobRenderer extends MobRenderer<ShadowGlobEntity, ShadowGlobModel<ShadowGlobEntity>> {

    public static final ShadowGlobRenderer.Factory FACTORY = new ShadowGlobRenderer.Factory();

    public ShadowGlobRenderer(EntityRendererManager renderManagerIn) {
        super(renderManagerIn, new ShadowGlobModel<>(), 0.35F);
    }

    @Override
    public ResourceLocation getEntityTexture(ShadowGlobEntity entity) {
        return new ResourceLocation(KingdomKeys.MODID, "textures/entity/mob/shadow_glob.png");
    }

    @Override
    protected void preRenderCallback(ShadowGlobEntity entitylivingbaseIn, MatrixStack matrixStackIn, float partialTickTime) {
    	matrixStackIn.scale(1F, 1F, 1F);
    	super.preRenderCallback(entitylivingbaseIn, matrixStackIn, partialTickTime);
    }
    
    public static class Factory implements IRenderFactory<ShadowGlobEntity> {
        @Override
        public EntityRenderer<? super ShadowGlobEntity> createRenderFor(EntityRendererManager entityRendererManager) {
            return new ShadowGlobRenderer(entityRendererManager);
        }
    }
}
