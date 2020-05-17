package online.kingdomkeys.kingdomkeys.client.render.entity;

import com.mojang.blaze3d.matrix.MatrixStack;

import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.client.model.entity.ShadowModel;
import online.kingdomkeys.kingdomkeys.entity.mob.GigaShadowEntity;
import online.kingdomkeys.kingdomkeys.entity.mob.LargeBodyEntity;

public class GigaShadowRenderer extends MobRenderer<GigaShadowEntity, ShadowModel<GigaShadowEntity>> {

    public static final GigaShadowRenderer.Factory FACTORY = new GigaShadowRenderer.Factory();

    public GigaShadowRenderer(EntityRendererManager renderManagerIn) {
        super(renderManagerIn, new ShadowModel<>(1D), 0.35F);
    }

    @Override
    public ResourceLocation getEntityTexture(GigaShadowEntity entity) {
        return new ResourceLocation(KingdomKeys.MODID, "textures/entity/mob/shadow.png");
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
