package online.kingdomkeys.kingdomkeys.client.render.entity;

import com.mojang.blaze3d.matrix.MatrixStack;

import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.client.model.entity.DarkballModel;
import online.kingdomkeys.kingdomkeys.entity.mob.DarkballEntity;

public class DarkballRenderer extends MobRenderer<DarkballEntity, DarkballModel<DarkballEntity>> {

    public static final DarkballRenderer.Factory FACTORY = new DarkballRenderer.Factory();

    public DarkballRenderer(EntityRendererManager renderManagerIn) {
        super(renderManagerIn, new DarkballModel<>(), 0.35F);
    }

    @Override
    public ResourceLocation getEntityTexture(DarkballEntity entity) {
        return new ResourceLocation(KingdomKeys.MODID, "textures/entity/mob/darkball.png");
    }

    @Override
    protected void preRenderCallback(DarkballEntity entitylivingbaseIn, MatrixStack matrixStackIn, float partialTickTime) {
    	matrixStackIn.scale(1F, 1F, 1F);
    	super.preRenderCallback(entitylivingbaseIn, matrixStackIn, partialTickTime);
    }
    
    public static class Factory implements IRenderFactory<DarkballEntity> {
        @Override
        public EntityRenderer<? super DarkballEntity> createRenderFor(EntityRendererManager entityRendererManager) {
            return new DarkballRenderer(entityRendererManager);
        }
    }
}
