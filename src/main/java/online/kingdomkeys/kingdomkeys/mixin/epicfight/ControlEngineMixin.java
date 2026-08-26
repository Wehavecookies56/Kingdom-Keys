package online.kingdomkeys.kingdomkeys.mixin.epicfight;

import online.kingdomkeys.kingdomkeys.client.shotlock.ShotlockMinigameClient;
import online.kingdomkeys.kingdomkeys.handler.ClientEvents;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import yesman.epicfight.client.events.engine.ControlEngine;

@Mixin(value = ControlEngine.class, remap = false)
public class ControlEngineMixin {
	// Avoid attacking if the player is starting the shotlock or doing the minigame
	@Inject(method = "maybeAttack", at = @At("HEAD"), cancellable = true, remap = false)
	private void kingdomkeys$blockAttackDuringShotlock(CallbackInfo ci) {
		if (ClientEvents.focusing || ShotlockMinigameClient.active || ShotlockMinigameClient.movementLocked) {
			ci.cancel();
		}
	}
}
