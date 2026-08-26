package online.kingdomkeys.kingdomkeys.sound;

import net.minecraft.client.resources.sounds.AbstractTickableSoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import online.kingdomkeys.kingdomkeys.client.sound.ModSounds;
import online.kingdomkeys.kingdomkeys.handler.ClientEvents;

@OnlyIn(Dist.CLIENT)
public class FlowmotionSoundInstance extends AbstractTickableSoundInstance {
	private final Player player;

	public FlowmotionSoundInstance(Player player) {
		super(ModSounds.flowmotionLoop.get(), SoundSource.PLAYERS, SoundInstance.createUnseededRandom());
		this.player = player;
		this.looping = true;
		this.delay = 0;
		this.volume = 0.2F;
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
		// Stopping rather than going quiet: the ride ends, so there is nothing to come back for
		if (player.isRemoved() || !ClientEvents.isGrinding()) {
			this.stop();
			return;
		}

		this.x = (float) player.getX();
		this.y = (float) player.getY();
		this.z = (float) player.getZ();
	}
}
