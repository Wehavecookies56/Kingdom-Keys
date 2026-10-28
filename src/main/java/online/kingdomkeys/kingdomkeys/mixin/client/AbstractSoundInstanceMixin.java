package online.kingdomkeys.kingdomkeys.mixin.client;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.client.resources.sounds.AbstractSoundInstance;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundSource;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.config.ModConfigs;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

/**
 * Kingdom Keys volume slider. SoundSource isn't extensible in NeoForge 1.21.1, so instead of a new category every
 * kingdomkeys sound is scaled on top of the category it plays in. Music and records keep only their own slider.
 */
@Mixin(AbstractSoundInstance.class)
public class AbstractSoundInstanceMixin {

	@Shadow
	@Final
	protected SoundSource source;

	@Shadow
	@Final
	protected ResourceLocation location;

	@ModifyReturnValue(method = "getVolume", at = @At("RETURN"))
	private float kkSoundVolume(float volume) {
		if (source == SoundSource.MUSIC || source == SoundSource.RECORDS || !location.getNamespace().equals(KingdomKeys.MODID)) {
			return volume;
		}
		return volume * (float) ModConfigs.soundVolume;
	}
}
