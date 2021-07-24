package online.kingdomkeys.kingdomkeys.client.render.entity;

import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.fmlclient.registry.IRenderFactory;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.client.model.entity.DarkballModel;
import online.kingdomkeys.kingdomkeys.entity.mob.DarkballEntity;

public class DarkballRenderer extends MobRenderer<DarkballEntity, DarkballModel<DarkballEntity>> {

    public static final DarkballRenderer.Factory FACTORY = new DarkballRenderer.Factory();

    public DarkballRenderer(EntityRenderDispatcher renderManagerIn) {
        super(renderManagerIn, new DarkballModel<>(), 0.35F);
    }

    @Override
    public ResourceLocation getTextureLocation(DarkballEntity entity) {
        return new ResourceLocation(KingdomKeys.MODID, "textures/entity/mob/darkball.png");
    }

    @Override
    protected void scale(DarkballEntity entitylivingbaseIn, PoseStack matrixStackIn, float partialTickTime) {
    	matrixStackIn.scale(1F, 1F, 1F);
    	super.scale(entitylivingbaseIn, matrixStackIn, partialTickTime);
    }
    
    public static class Factory implements IRenderFactory<DarkballEntity> {
        @Override
        public EntityRenderer<? super DarkballEntity> createRenderFor(EntityRenderDispatcher entityRendererManager) {
            return new DarkballRenderer(entityRendererManager);
        }
    }
}
