package online.kingdomkeys.kingdomkeys.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.network.protocol.game.ServerboundPlayerActionPacket;
import net.minecraft.network.protocol.game.ServerboundSetCarriedItemPacket;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import online.kingdomkeys.kingdomkeys.world.StruggleHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerGamePacketListenerImpl.class)
public class ServerGamePacketListenerImplMixin {

    @ModifyExpressionValue(method = "handleMovePlayer", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/level/ServerPlayer;isChangingDimension()Z", ordinal = 1))
    public boolean handleCOMove(boolean original) {
        if (((ServerGamePacketListenerImpl)(Object)this).player.level().dimension().location().toString().contains("castle_oblivion_interior_")) {
            return true;
        } else {
            return original;
        }
    }

    /** Blocks switching hotbar slots (number keys or mouse wheel both funnel through this same packet)
     * while locked to a Struggle weapon during a match - the per-tick re-selection in StruggleHandler
     * already forces it back, but stopping it here means there's no 1-tick flicker to a different item
     * first. */
    @Inject(method = "handleSetCarriedItem", at = @At("HEAD"), cancellable = true)
    public void kk$blockHotbarSwitchDuringStruggle(ServerboundSetCarriedItemPacket packet, CallbackInfo ci) {
        if (StruggleHandler.isWeaponLocked(((ServerGamePacketListenerImpl)(Object)this).player.getUUID())) {
            ci.cancel();
        }
    }

    /** Blocks swapping to the offhand (the F key) while locked to a Struggle weapon during a match -
     * unlike hotbar slot selection, nothing else re-locks the offhand each tick, so this is the only
     * thing stopping a combatant from just swapping their weapon out of their main hand this way. */
    @Inject(method = "handlePlayerAction", at = @At("HEAD"), cancellable = true)
    public void kk$blockOffhandSwapDuringStruggle(ServerboundPlayerActionPacket packet, CallbackInfo ci) {
        if (packet.getAction() == ServerboundPlayerActionPacket.Action.SWAP_ITEM_WITH_OFFHAND
                && StruggleHandler.isWeaponLocked(((ServerGamePacketListenerImpl)(Object)this).player.getUUID())) {
            ci.cancel();
        }
    }

    /**
     * Players can move much faster than vanilla allows through Flowmotion, Airstep...
     * If the speed limit is exceeded, the server used to log "moved too quickly!" and teleported the player back.
     */
    @ModifyConstant(method = "handleMovePlayer", constant = @Constant(floatValue = 300.0F))
    private float kk$removeElytraMovementSpeedLimit(float original) {
        return Float.MAX_VALUE;
    }

    @ModifyConstant(method = "handleMovePlayer", constant = @Constant(floatValue = 100.0F))
    private float kk$removePlayerMovementSpeedLimit(float original) {
        return Float.MAX_VALUE;
    }

    /**
     * The same validation for vehicles (mainly gummi ships).
     */
    @ModifyConstant(method = "handleMoveVehicle", constant = @Constant(doubleValue = 100.0D))
    private double kk$removeVehicleMovementSpeedLimit(double original) {
        return Double.MAX_VALUE;
    }

}
