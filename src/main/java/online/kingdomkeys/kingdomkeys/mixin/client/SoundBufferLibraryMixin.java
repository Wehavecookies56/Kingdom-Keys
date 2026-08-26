package online.kingdomkeys.kingdomkeys.mixin.client;

import net.minecraft.Util;
import net.minecraft.client.sounds.AudioStream;
import net.minecraft.client.sounds.SoundBufferLibrary;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceProvider;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.sound.IntroLoopAudioStream;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;

/**
 * Lets a track keep an intro it only plays once.
 *
 * A sound that ships a <name>_intro.ogg beside it is handed a stream that reads the intro first and then
 * loops the main file for good, so the two halves arrive as one continuous run of samples. Everything else
 * goes through untouched, and a track without that file behaves exactly as before.
 *
 * It hooks the place the stream is opened rather than the mod's own sound instance because the same track can
 * be started from either side: the world music system plays its own instances, while Castle Oblivion goes
 * through vanilla's music manager. Both end up here.
 */
@Mixin(SoundBufferLibrary.class)
public class SoundBufferLibraryMixin {

	@Shadow
	@Final
	private ResourceProvider resourceManager;

	@Inject(method = "getStream", at = @At("HEAD"), cancellable = true)
	private void kkIntroLoop(ResourceLocation file, boolean looping, CallbackInfoReturnable<CompletableFuture<AudioStream>> cir) {
		// Only our own assets, so nobody else's sound can be caught by a name that happens to fit
		if (!file.getNamespace().equals(KingdomKeys.MODID)) {
			return;
		}

		ResourceLocation intro = IntroLoopAudioStream.introFor(file);

		if (intro == null || this.resourceManager.getResource(intro).isEmpty()) {
			return;
		}

		// Deliberately ignores the looping flag. Vanilla's music manager asks for a one shot and restarts the
		// sound when it ends, which puts a tick sized hole at every repeat; a stream that never ends leaves
		// the repeat inside the decoder, where it is exact. The manager still stops it to change track.
		cir.setReturnValue(CompletableFuture.supplyAsync(() -> {
			try {
				return (AudioStream) new IntroLoopAudioStream(this.resourceManager.open(intro), this.resourceManager.open(file));
			} catch (IOException failed) {
				throw new CompletionException(failed);
			}
		}, Util.nonCriticalIoPool()));
	}
}
