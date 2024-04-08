package online.kingdomkeys.kingdomkeys.mixin.epicfight;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import online.kingdomkeys.kingdomkeys.client.render.DriveLayerRenderer;
import online.kingdomkeys.kingdomkeys.client.render.KeybladeArmorRenderer;
import online.kingdomkeys.kingdomkeys.client.render.ShoulderLayerRenderer;
import online.kingdomkeys.kingdomkeys.integration.epicfight.PatchedArmourLayerRenderer;
import online.kingdomkeys.kingdomkeys.integration.epicfight.PatchedDriveLayerRenderer;
import online.kingdomkeys.kingdomkeys.integration.epicfight.PatchedShoulderLayerRenderer;
import yesman.epicfight.api.client.model.Meshes;
import yesman.epicfight.client.mesh.HumanoidMesh;
import yesman.epicfight.client.renderer.patched.entity.PHumanoidRenderer;

@Mixin(PHumanoidRenderer.class)
public class PHumanoidRendererMixin {

    @Inject(method = "<init>", at = @At("TAIL"))
    public void init(HumanoidMesh mesh, CallbackInfo ci) {
        PHumanoidRenderer thisOne = ((PHumanoidRenderer)(Object)this);
        thisOne.addPatchedLayer(DriveLayerRenderer.class, new PatchedDriveLayerRenderer<>(Meshes.BIPED));
        thisOne.addPatchedLayer(KeybladeArmorRenderer.class, new PatchedArmourLayerRenderer<>(Meshes.BIPED, false));
        thisOne.addPatchedLayer(ShoulderLayerRenderer.class, new PatchedShoulderLayerRenderer<>(Meshes.BIPED));
    }
}
