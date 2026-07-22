package online.kingdomkeys.kingdomkeys.client.render.entity;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.entity.mob.BloxBugEntity;
import software.bernie.geckolib.model.DefaultedEntityGeoModel;
import software.bernie.geckolib.renderer.GeoEntityRenderer;
import software.bernie.geckolib.renderer.layer.AutoGlowingGeoLayer;

public class BloxBugEntityRenderer extends GeoEntityRenderer<BloxBugEntity> {
    public BloxBugEntityRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new DefaultedEntityGeoModel<>(KingdomKeys.rl("heartless/blox_bug")));
        addRenderLayer(new AutoGlowingGeoLayer<>(this));
    }
}
