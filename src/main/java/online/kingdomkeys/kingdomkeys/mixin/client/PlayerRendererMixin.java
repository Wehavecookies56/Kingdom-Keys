package online.kingdomkeys.kingdomkeys.mixin.client;

import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import online.kingdomkeys.kingdomkeys.util.IDisabledAnimations;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerRenderer.class)
public abstract class PlayerRendererMixin implements IDisabledAnimations {

    @Unique private boolean kingdom_Keys$disabled = false;

    @Unique
    @Override
    public void kingdom_Keys$setDisabled(boolean disabled) {
        this.kingdom_Keys$disabled = disabled;
    }

    @Unique
    @Override
    public boolean kingdom_Keys$isDisabled() {
        return kingdom_Keys$disabled;
    }

    @Inject(method = "setupRotations(Lnet/minecraft/client/player/AbstractClientPlayer;Lcom/mojang/blaze3d/vertex/PoseStack;FFFF)V", at = @At(value = "HEAD"), cancellable = true)
    public void injectSetupRotations(CallbackInfo ci) {
        if (kingdom_Keys$isDisabled()) {
            ci.cancel();
        }
    }
}
