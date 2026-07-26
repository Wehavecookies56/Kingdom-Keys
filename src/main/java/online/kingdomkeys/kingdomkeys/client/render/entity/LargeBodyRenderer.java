package online.kingdomkeys.kingdomkeys.client.render.entity;

import online.kingdomkeys.kingdomkeys.client.ClientUtils;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.client.model.entity.LargeBodyModel;
import online.kingdomkeys.kingdomkeys.client.render.HeartlessEyesLayerRenderer;
import online.kingdomkeys.kingdomkeys.entity.mob.LargeBodyEntity;

public class LargeBodyRenderer extends MobRenderer<LargeBodyEntity, LargeBodyModel<LargeBodyEntity>> {

    public LargeBodyRenderer(EntityRendererProvider.Context context) {
        super(context, new LargeBodyModel<>(context.bakeLayer(LargeBodyModel.LAYER_LOCATION)), 1F);
        this.addLayer(new HeartlessEyesLayerRenderer<>(this, KingdomKeys.rl("textures/entity/mob/large_body_eyes.png")));
    }

    @Override
    public ResourceLocation getTextureLocation(LargeBodyEntity entity) {
        return ClientUtils.variantTexture(KingdomKeys.rl("textures/entity/mob/large_body.png"), entity);
    }
}
