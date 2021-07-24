package online.kingdomkeys.kingdomkeys.client.render.entity;

import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.fmlclient.registry.IRenderFactory;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.client.model.entity.ShadowGlobModel;
import online.kingdomkeys.kingdomkeys.entity.mob.ShadowGlobEntity;

public class ShadowGlobRenderer extends MobRenderer<ShadowGlobEntity, ShadowGlobModel<ShadowGlobEntity>> {

    public static final ShadowGlobRenderer.Factory FACTORY = new ShadowGlobRenderer.Factory();

    public ShadowGlobRenderer(EntityRenderDispatcher renderManagerIn) {
        super(renderManagerIn, new ShadowGlobModel<>(), 0.35F);
    }

    @Override
    public ResourceLocation getTextureLocation(ShadowGlobEntity entity) {
        return new ResourceLocation(KingdomKeys.MODID, "textures/entity/mob/shadow_glob.png");
    }

    @Override
    protected void scale(ShadowGlobEntity entitylivingbaseIn, PoseStack matrixStackIn, float partialTickTime) {
    	matrixStackIn.scale(1F, 1F, 1F);
    	super.scale(entitylivingbaseIn, matrixStackIn, partialTickTime);
    }
    
    public static class Factory implements IRenderFactory<ShadowGlobEntity> {
        @Override
        public EntityRenderer<? super ShadowGlobEntity> createRenderFor(EntityRenderDispatcher entityRendererManager) {
            return new ShadowGlobRenderer(entityRendererManager);
        }
    }
}
