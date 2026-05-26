package online.kingdomkeys.kingdomkeys.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

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

}
