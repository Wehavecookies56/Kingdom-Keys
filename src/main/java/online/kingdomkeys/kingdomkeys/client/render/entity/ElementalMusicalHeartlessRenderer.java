package online.kingdomkeys.kingdomkeys.client.render.entity;


import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.fmlclient.registry.IRenderFactory;
import online.kingdomkeys.kingdomkeys.client.model.entity.ElementalMusicalHeartlessModel;
import online.kingdomkeys.kingdomkeys.entity.mob.BaseElementalMusicalHeartlessEntity;

public class ElementalMusicalHeartlessRenderer extends MobRenderer<BaseElementalMusicalHeartlessEntity, ElementalMusicalHeartlessModel<BaseElementalMusicalHeartlessEntity>> { //my god that's a long one

    public static final ElementalMusicalHeartlessRenderer.Factory FACTORY = new ElementalMusicalHeartlessRenderer.Factory();
    static final double MAX = 200;

    public ElementalMusicalHeartlessRenderer(EntityRenderDispatcher renderManagerIn) {
        super(renderManagerIn, new ElementalMusicalHeartlessModel<>(), 0.35F);
    }

    @Override
    public ResourceLocation getTextureLocation(BaseElementalMusicalHeartlessEntity entity) {
        return entity.getTexture();
    }

    @Override
    protected void scale(BaseElementalMusicalHeartlessEntity entitylivingbaseIn, PoseStack matrixStackIn, float partialTickTime) {
        matrixStackIn.scale(1F, 1F, 1F);
        double pos = entitylivingbaseIn.tickCount % MAX / (MAX /2D);
        if(entitylivingbaseIn.tickCount % MAX < (MAX / 2)) {
        	matrixStackIn.translate(0, pos*0.6, 0);
        } else {
        	matrixStackIn.translate(0, (MAX - entitylivingbaseIn.tickCount % MAX) / (MAX / 2D) * 0.6, 0);
        }
        super.scale(entitylivingbaseIn, matrixStackIn, partialTickTime);
    }

    public static class Factory implements IRenderFactory<BaseElementalMusicalHeartlessEntity> {
        @Override
        public EntityRenderer<? super BaseElementalMusicalHeartlessEntity> createRenderFor(EntityRenderDispatcher entityRendererManager) {
            return new ElementalMusicalHeartlessRenderer(entityRendererManager);
        }
    }

}
