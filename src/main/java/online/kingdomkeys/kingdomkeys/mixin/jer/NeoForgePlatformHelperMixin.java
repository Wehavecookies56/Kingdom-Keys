package online.kingdomkeys.kingdomkeys.mixin.jer;

import jeresources.api.IJERAPI;
import jeresources.neoforge.NeoForgePlatformHelper;
import online.kingdomkeys.kingdomkeys.integration.jer.KKJERPlugin;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

// JER's own plugin discovery can't find us. Its injectApi scans mod files for annotations matching
// Type.getType(IJERPlugin.class) - the interface, not the @JERPlugin annotation - and since no class
// can be annotated with an interface, that loop never matches anything and receive() is never called.
//
// So we hook the moment JER hands its API out and register ourselves directly. Nothing is cancelled:
// JER still runs its scan afterwards, it just won't find anything.
@Mixin(value = NeoForgePlatformHelper.class, remap = false)
public class NeoForgePlatformHelperMixin {

	@Inject(method = "injectApi", at = @At("HEAD"), remap = false)
	public void kingdomkeys$injectKKPlugin(IJERAPI instance, CallbackInfo ci) {
		KKJERPlugin.setup(instance);
	}
}
