package online.kingdomkeys.kingdomkeys.mixin.epicfight;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.world.entity.EntityType;
import online.kingdomkeys.kingdomkeys.client.render.*;
import online.kingdomkeys.kingdomkeys.integration.epicfight.*;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import yesman.epicfight.client.renderer.patched.entity.PatchedLivingEntityRenderer;

@Mixin(PatchedLivingEntityRenderer.class)
public class PHumanoidRendererMixin {

    @Inject(method = "<init>", at = @At("TAIL"))
    public void init(EntityRendererProvider.Context context, EntityType entityType, CallbackInfo ci) {
        PatchedLivingEntityRenderer thisOne = ((PatchedLivingEntityRenderer)(Object)this);
        thisOne.addPatchedLayer(DriveLayerRenderer.class, new PatchedDriveLayerRenderer<>(false));
        thisOne.addPatchedLayer(KeybladeArmorRenderer.class, new PatchedArmourLayerRenderer<>(false));
        thisOne.addPatchedLayer(ShoulderLayerRenderer.class, new PatchedShoulderLayerRenderer<>());
        thisOne.addPatchedLayer(AeroLayerRenderer.class, new PatchedAeroLayerRenderer<>());
        thisOne.addPatchedLayer(CrownLayerRenderer.class, new PatchedCrownLayerRenderer<>());
        thisOne.addPatchedLayer(FreezeLayerRenderer.class, new PatchedFreezeLayerRenderer<>());
        thisOne.addPatchedLayer(StopLayerRenderer.class, new PatchedStopLayerRenderer<>());
        thisOne.addPatchedLayer(ClothArmorOverlayRenderer.class, new PatchedClothArmorOverlayRenderer<>(false));
    }
}
