package online.kingdomkeys.kingdomkeys.mixin.client;

import net.minecraft.client.Camera;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.phys.Vec3;
import online.kingdomkeys.kingdomkeys.entity.GummiShipEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Camera.class)
public abstract class CameraMixin {

    @Shadow protected abstract void setPosition(Vec3 pos);

    @Shadow public abstract Vec3 getPosition();

    // A third person camera stays behind the ship during a barrel roll instead of circling with the rider
    @Inject(method = "setup", at = @At("TAIL"))
    private void stayOffGummiRoll(BlockGetter level, Entity entity, boolean detached, boolean thirdPersonReverse, float partialTick, CallbackInfo ci) {
        if (detached && entity.getVehicle() instanceof GummiShipEntity ship) {
            Vec3 shift = ship.getRollShift(entity, partialTick);
            if (shift != Vec3.ZERO) {
                setPosition(getPosition().subtract(shift));
            }
        }
    }
}
