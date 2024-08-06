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
import yesman.epicfight.api.client.model.MeshProvider;
import yesman.epicfight.api.client.model.Meshes;
import yesman.epicfight.client.mesh.HumanoidMesh;
import yesman.epicfight.client.renderer.patched.entity.PHumanoidRenderer;

@Mixin(PHumanoidRenderer.class)
public class PHumanoidRendererMixin {

    @Inject(method = "<init>", at = @At("TAIL"))
    public void init(MeshProvider mesh, EntityRendererProvider.Context context, EntityType entityType, CallbackInfo ci) {
        PHumanoidRenderer thisOne = ((PHumanoidRenderer)(Object)this);
        thisOne.addPatchedLayer(DriveLayerRenderer.class, new PatchedDriveLayerRenderer<>());
        thisOne.addPatchedLayer(KeybladeArmorRenderer.class, new PatchedArmourLayerRenderer<>(false));
        thisOne.addPatchedLayer(ShoulderLayerRenderer.class, new PatchedShoulderLayerRenderer<>());
    }
}
