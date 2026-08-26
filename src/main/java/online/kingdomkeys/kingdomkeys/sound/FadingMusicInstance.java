package online.kingdomkeys.kingdomkeys.sound;

import net.minecraft.client.resources.sounds.AbstractTickableSoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class FadingMusicInstance extends AbstractTickableSoundInstance {
	private final int fade;
	private int loudness;
	private boolean fadingOut;

	public FadingMusicInstance(SoundEvent sound, int fade) {
		super(sound, SoundSource.MUSIC, SoundInstance.createUnseededRandom());
		this.fade = Math.max(1, fade);
		this.looping = true;
		this.delay = 0;
		this.volume = 0F;
		// Music belongs to the listener rather than to a place, the same way vanilla plays its own
		this.relative = true;
		this.attenuation = Attenuation.NONE;
		this.x = 0;
		this.y = 0;
		this.z = 0;
	}

	public void fadeOut() {
		this.fadingOut = true;
	}

	public boolean isFadingOut() {
		return this.fadingOut;
	}

	@Override
	public boolean canPlaySound() {
		return true;
	}

	// Otherwise the engine would drop it before the first tick, since it comes in at nothing
	@Override
	public boolean canStartSilent() {
		return true;
	}

	@Override
	public void tick() {
		loudness = Math.clamp(loudness + (fadingOut ? -1 : 1), 0, fade);
		volume = (float) loudness / fade;

		if (fadingOut && loudness == 0) {
			this.stop();
		}
	}
}
