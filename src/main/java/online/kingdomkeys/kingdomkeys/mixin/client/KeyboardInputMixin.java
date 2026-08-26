package online.kingdomkeys.kingdomkeys.mixin.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.player.KeyboardInput;
import online.kingdomkeys.kingdomkeys.effects.ModMobEffects;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(KeyboardInput.class)
public class KeyboardInputMixin {

	@Inject(method = "tick", at = @At("TAIL"))
	private void invertMovement(boolean isSneaking, float sneakingSpeedMultiplier, CallbackInfo ci) {
		Minecraft mc = Minecraft.getInstance();

		if (mc.player == null)
			return;

		if (!mc.player.hasEffect(ModMobEffects.CONFUSE))
			return;

		KeyboardInput input = (KeyboardInput)(Object)this;

		input.forwardImpulse *= -1F;
		input.leftImpulse *= -1F;

		boolean up = input.up;
		input.up = input.down;
		input.down = up;

		boolean left = input.left;
		input.left = input.right;
		input.right = left;
	}
}