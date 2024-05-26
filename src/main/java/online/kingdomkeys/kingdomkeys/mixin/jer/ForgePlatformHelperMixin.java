package online.kingdomkeys.kingdomkeys.mixin.jer;

import jeresources.api.IJERAPI;
import jeresources.forge.ForgePlatformHelper;
import online.kingdomkeys.kingdomkeys.integration.jer.KKJERPlugin;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ForgePlatformHelper.class)
public class ForgePlatformHelperMixin {

    @Inject(method = "injectApi", remap = false, at = @At(value = "HEAD"))
    public void injectKKJERAPI(IJERAPI instance, CallbackInfo ci) {
        KKJERPlugin.setup(instance);
    }

}
