package online.kingdomkeys.kingdomkeys.mixin.epicfight;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.world.entity.EntityType;
import online.kingdomkeys.kingdomkeys.client.render.DriveLayerRenderer;
import online.kingdomkeys.kingdomkeys.client.render.KeybladeArmorRenderer;
import online.kingdomkeys.kingdomkeys.client.render.OrganizationArmorOverlayRenderer;
import online.kingdomkeys.kingdomkeys.client.render.ShoulderLayerRenderer;
import online.kingdomkeys.kingdomkeys.integration.epicfight.PatchedArmourLayerRenderer;
import online.kingdomkeys.kingdomkeys.integration.epicfight.PatchedDriveLayerRenderer;
import online.kingdomkeys.kingdomkeys.integration.epicfight.PatchedOrganizationArmorOverlayRenderer;
import online.kingdomkeys.kingdomkeys.integration.epicfight.PatchedShoulderLayerRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import yesman.epicfight.client.renderer.FirstPersonRenderer;

@Mixin(FirstPersonRenderer.class)
public class FirstPersonRendererMixin {

    @SuppressWarnings("all")
    @Inject(method = "<init>", at = @At("TAIL"))
    public void addKKLayers(EntityRendererProvider.Context context, EntityType entityType, CallbackInfo ci) {
        FirstPersonRenderer thisOne = ((FirstPersonRenderer)(Object)this);
        thisOne.addPatchedLayer(DriveLayerRenderer.class, new PatchedDriveLayerRenderer<>(true));
        thisOne.addPatchedLayer(KeybladeArmorRenderer.class, new PatchedArmourLayerRenderer<>(true));
        thisOne.addPatchedLayer(ShoulderLayerRenderer.class, new PatchedShoulderLayerRenderer<>());
        thisOne.addPatchedLayer(OrganizationArmorOverlayRenderer.class, new PatchedOrganizationArmorOverlayRenderer<>(true));
    }

}
