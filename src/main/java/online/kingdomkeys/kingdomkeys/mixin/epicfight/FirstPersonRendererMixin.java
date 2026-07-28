package online.kingdomkeys.kingdomkeys.mixin.epicfight;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.world.entity.EntityType;
import online.kingdomkeys.kingdomkeys.client.render.DriveLayerRenderer;
import online.kingdomkeys.kingdomkeys.client.render.KeybladeArmorRenderer;
import online.kingdomkeys.kingdomkeys.client.render.ShoulderLayerRenderer;
import online.kingdomkeys.kingdomkeys.integration.epicfight.PatchedArmourLayerRenderer;
import online.kingdomkeys.kingdomkeys.integration.epicfight.PatchedDriveLayerRenderer;
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
        thisOne.addPatchedLayer(DriveLayerRenderer.class, new PatchedDriveLayerRenderer<>());
        // First person bake: arms only, no helmet/torso/legs near the camera.
        thisOne.addPatchedLayer(KeybladeArmorRenderer.class, new PatchedArmourLayerRenderer<>(true));
        // Pauldrons sit right beside the camera in first person and block the view. Vanilla shows no
        // body armour in first person either, so this only belongs on the third-person renderer.
        thisOne.addPatchedLayer(ShoulderLayerRenderer.class, new PatchedShoulderLayerRenderer<>());
    }

}
