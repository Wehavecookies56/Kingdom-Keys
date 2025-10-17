package online.kingdomkeys.kingdomkeys.mixin.client;

import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import online.kingdomkeys.kingdomkeys.entity.GummiShipEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Camera.class)
public class CameraMixin {

    @Inject(method = "getMaxZoom", at = @At("HEAD"), cancellable = true)
    public void stopZoomInGummiShip(float maxZoom, CallbackInfoReturnable<Float> cir) {
        if (Minecraft.getInstance().player.getVehicle() != null) {
            if (Minecraft.getInstance().player.getVehicle() instanceof GummiShipEntity) {
                cir.setReturnValue(maxZoom);
            }
        }
    }

}
