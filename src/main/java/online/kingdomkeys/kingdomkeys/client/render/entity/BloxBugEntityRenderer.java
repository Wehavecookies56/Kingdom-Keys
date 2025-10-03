package online.kingdomkeys.kingdomkeys.client.render.entity;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.entity.mob.BloxBugEntity;
import software.bernie.geckolib.model.DefaultedEntityGeoModel;
import software.bernie.geckolib.renderer.GeoEntityRenderer;
import software.bernie.geckolib.renderer.layer.AutoGlowingGeoLayer;

public class BloxBugEntityRenderer extends GeoEntityRenderer<BloxBugEntity> {
    public BloxBugEntityRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new DefaultedEntityGeoModel<>(ResourceLocation.fromNamespaceAndPath(KingdomKeys.MODID, "heartless/blox_bug")));
        addRenderLayer(new AutoGlowingGeoLayer<>(this));
    }
}
