package online.kingdomkeys.kingdomkeys.client.render.entity;

import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.client.model.entity.MoogleModel;
import online.kingdomkeys.kingdomkeys.client.model.entity.ShadowModel;
import online.kingdomkeys.kingdomkeys.entity.mob.MoogleEntity;
import online.kingdomkeys.kingdomkeys.entity.mob.ShadowEntity;

public class ShadowRenderer extends MobRenderer<ShadowEntity, ShadowModel<ShadowEntity>> {

    public static final ShadowRenderer.Factory FACTORY = new ShadowRenderer.Factory();

    public ShadowRenderer(EntityRendererManager renderManagerIn) {
        super(renderManagerIn, new ShadowModel<>(1D), 0.35F);
    }

    @Override
    public ResourceLocation getEntityTexture(ShadowEntity entity) {
        return new ResourceLocation(KingdomKeys.MODID, "textures/entity/mob/shadow.png");
    }

    public static class Factory implements IRenderFactory<ShadowEntity> {
        @Override
        public EntityRenderer<? super ShadowEntity> createRenderFor(EntityRendererManager entityRendererManager) {
            return new ShadowRenderer(entityRendererManager);
        }
    }
}
