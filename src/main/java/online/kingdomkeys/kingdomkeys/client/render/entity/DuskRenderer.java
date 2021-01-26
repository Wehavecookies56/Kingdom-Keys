package online.kingdomkeys.kingdomkeys.client.render.entity;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.client.model.entity.DarkballModel;
import online.kingdomkeys.kingdomkeys.client.model.entity.DuskModel;
import online.kingdomkeys.kingdomkeys.entity.mob.DarkballEntity;
import online.kingdomkeys.kingdomkeys.entity.mob.DuskEntity;

public class DuskRenderer extends MobRenderer<DuskEntity, DuskModel<DuskEntity>> {

    public static final DuskRenderer.Factory FACTORY = new DuskRenderer.Factory();

    public DuskRenderer(EntityRendererManager renderManagerIn) {
        super(renderManagerIn, new DuskModel<>(), 0.35F);
    }

    @Override
    public ResourceLocation getEntityTexture(DuskEntity entity) {
        return new ResourceLocation(KingdomKeys.MODID, "textures/entity/mob/dusk.png");
    }

    @Override
    protected void preRenderCallback(DuskEntity entitylivingbaseIn, MatrixStack matrixStackIn, float partialTickTime) {
    	matrixStackIn.scale(1.2F, 1.2F, 1.2F);
    	super.preRenderCallback(entitylivingbaseIn, matrixStackIn, partialTickTime);
    }
    
    public static class Factory implements IRenderFactory<DuskEntity> {
        @Override
        public EntityRenderer<? super DuskEntity> createRenderFor(EntityRendererManager entityRendererManager) {
            return new DuskRenderer(entityRendererManager);
        }
    }
}
