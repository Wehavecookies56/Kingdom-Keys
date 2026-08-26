package online.kingdomkeys.kingdomkeys.sound;

import net.minecraft.client.resources.sounds.AbstractTickableSoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import online.kingdomkeys.kingdomkeys.client.sound.ModSounds;
import online.kingdomkeys.kingdomkeys.effects.ModMobEffects;

@OnlyIn(Dist.CLIENT)
public class KOSoundInstance extends AbstractTickableSoundInstance {
	private final Player player;

	public KOSoundInstance(Player player) {
		super(ModSounds.koLoop.get(), SoundSource.PLAYERS, SoundInstance.createUnseededRandom());
		this.player = player;
		this.looping = true;
		this.delay = 0;
		this.volume = 1F;
		this.x = (float) player.getX();
		this.y = (float) player.getY();
		this.z = (float) player.getZ();
	}

	@Override
	public boolean canPlaySound() {
		return true;
	}

	@Override
	public boolean canStartSilent() {
		return true;
	}

	@Override
	public void tick() {
		if (player.isRemoved() || !player.hasEffect(ModMobEffects.KO)) {
			this.stop();
			return;
		}

		this.x = (float) player.getX();
		this.y = (float) player.getY();
		this.z = (float) player.getZ();
	}
}
