package online.kingdomkeys.kingdomkeys.client.render.entity;

import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.fmlclient.registry.IRenderFactory;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.client.model.entity.DuskModel;
import online.kingdomkeys.kingdomkeys.entity.mob.DuskEntity;

public class DuskRenderer extends MobRenderer<DuskEntity, DuskModel<DuskEntity>> {

    public static final DuskRenderer.Factory FACTORY = new DuskRenderer.Factory();

    public DuskRenderer(EntityRenderDispatcher renderManagerIn) {
        super(renderManagerIn, new DuskModel<>(), 0.35F);
    }

    @Override
    public ResourceLocation getTextureLocation(DuskEntity entity) {
        return new ResourceLocation(KingdomKeys.MODID, "textures/entity/mob/dusk.png");
    }

    @Override
    protected void scale(DuskEntity entitylivingbaseIn, PoseStack matrixStackIn, float partialTickTime) {
    	matrixStackIn.scale(1.2F, 1.2F, 1.2F);
    	super.scale(entitylivingbaseIn, matrixStackIn, partialTickTime);
    }
    
    public static class Factory implements IRenderFactory<DuskEntity> {
        @Override
        public EntityRenderer<? super DuskEntity> createRenderFor(EntityRenderDispatcher entityRendererManager) {
            return new DuskRenderer(entityRendererManager);
        }
    }
}
