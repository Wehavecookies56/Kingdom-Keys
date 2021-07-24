package online.kingdomkeys.kingdomkeys.client.render.entity;

import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.fmlclient.registry.IRenderFactory;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.client.model.entity.LargeBodyModel;
import online.kingdomkeys.kingdomkeys.entity.mob.LargeBodyEntity;

public class LargeBodyRenderer extends MobRenderer<LargeBodyEntity, LargeBodyModel<LargeBodyEntity>> {

    public static final LargeBodyRenderer.Factory FACTORY = new LargeBodyRenderer.Factory();

    public LargeBodyRenderer(EntityRenderDispatcher renderManagerIn) {
        super(renderManagerIn, new LargeBodyModel<>(), 1F);
    }

    @Override
    public ResourceLocation getTextureLocation(LargeBodyEntity entity) {
        return new ResourceLocation(KingdomKeys.MODID, "textures/entity/mob/large_body.png");
    }    
    
    @Override
    protected void scale(LargeBodyEntity entitylivingbaseIn, PoseStack matrixStackIn, float partialTickTime) {
    	matrixStackIn.scale(1.5F, 1.5F, 1.5F);
    	super.scale(entitylivingbaseIn, matrixStackIn, partialTickTime);
    }

    public static class Factory implements IRenderFactory<LargeBodyEntity> {
        @Override
        public EntityRenderer<? super LargeBodyEntity> createRenderFor(EntityRenderDispatcher entityRendererManager) {
            return new LargeBodyRenderer(entityRendererManager);
        }
    }
}
