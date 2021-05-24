package online.kingdomkeys.kingdomkeys.client.render.entity;

import com.mojang.blaze3d.matrix.MatrixStack;

import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.client.model.entity.DirePlantModel;
import online.kingdomkeys.kingdomkeys.entity.mob.DirePlantEntity;

public class DirePlantRenderer extends MobRenderer<DirePlantEntity, DirePlantModel<DirePlantEntity>> {

    public static final DirePlantRenderer.Factory FACTORY = new DirePlantRenderer.Factory();

    public DirePlantRenderer(EntityRendererManager renderManagerIn) {
        super(renderManagerIn, new DirePlantModel<>(), 0.35F);
    }

    @Override
    public ResourceLocation getEntityTexture(DirePlantEntity entity) {
        return new ResourceLocation(KingdomKeys.MODID, "textures/entity/mob/dire_plant.png");
    }

    @Override
    protected void preRenderCallback(DirePlantEntity entitylivingbaseIn, MatrixStack matrixStackIn, float partialTickTime) {
    	matrixStackIn.scale(1F, 1F, 1F);
    	super.preRenderCallback(entitylivingbaseIn, matrixStackIn, partialTickTime);
    }
    
    public static class Factory implements IRenderFactory<DirePlantEntity> {
        @Override
        public EntityRenderer<? super DirePlantEntity> createRenderFor(EntityRendererManager entityRendererManager) {
            return new DirePlantRenderer(entityRendererManager);
        }
    }
}
