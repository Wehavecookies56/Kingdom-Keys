package online.kingdomkeys.kingdomkeys.client.render.entity;

import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.client.model.entity.LargeBodyModel;
import online.kingdomkeys.kingdomkeys.entity.mob.LargeBodyEntity;

public class LargeBodyRenderer extends MobRenderer<LargeBodyEntity, LargeBodyModel<LargeBodyEntity>> {

    public static final LargeBodyRenderer.Factory FACTORY = new LargeBodyRenderer.Factory();

    public LargeBodyRenderer(EntityRendererManager renderManagerIn) {
        super(renderManagerIn, new LargeBodyModel<>(), 1F);
    }

    @Override
    public ResourceLocation getEntityTexture(LargeBodyEntity entity) {
        return new ResourceLocation(KingdomKeys.MODID, "textures/entity/mob/largebody.png");
    }

    public static class Factory implements IRenderFactory<LargeBodyEntity> {
        @Override
        public EntityRenderer<? super LargeBodyEntity> createRenderFor(EntityRendererManager entityRendererManager) {
            return new LargeBodyRenderer(entityRendererManager);
        }
    }
}
