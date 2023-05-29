package online.kingdomkeys.kingdomkeys.mixin;

import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import online.kingdomkeys.kingdomkeys.util.IDisabledAnimations;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerRenderer.class)
public abstract class PlayerRendererMixin implements IDisabledAnimations {

    @Unique private boolean disabled = false;

    @Unique
    @Override
    public void setDisabled(boolean disabled) {
        this.disabled = disabled;
    }

    @Unique
    @Override
    public boolean isDisabled() {
        return disabled;
    }

    @Inject(method = "setupRotations(Lnet/minecraft/client/player/AbstractClientPlayer;Lcom/mojang/blaze3d/vertex/PoseStack;FFF)V", at = @At(value = "HEAD"), cancellable = true)
    public void injectSetupRotations(CallbackInfo ci) {
        if (isDisabled()) {
            ci.cancel();
        }
    }
}
