package online.kingdomkeys.kingdomkeys.shotlock;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import online.kingdomkeys.kingdomkeys.client.sound.ModSounds;
import online.kingdomkeys.kingdomkeys.entity.shotlock.DarkVolleyCoreEntity;

import java.util.List;

public class ShotlockPulseBomb extends Shotlock {

	private static final int SHOT_COLOR = 0x50419e;

	public ShotlockPulseBomb(ResourceLocation registryName, int order) {
		super(registryName, order);
	}

	@Override
	public void onUse(Player player, List<Entity> targetList) {
		player.level().playSound(null, player.position().x(), player.position().y(), player.position().z(), ModSounds.portal.get(), SoundSource.PLAYERS, 1F, 1F);
		super.onUse(player, targetList);
	}

	@Override
	public void doPartialShotlock(Player player, List<Entity> targetList) {
		DarkVolleyCoreEntity core = new DarkVolleyCoreEntity(player.level(), player, targetList, getDamage(player));
		core.setShotColor(SHOT_COLOR);
		core.setElement(getElement());
		core.setExplodeOnHit(true);
		core.setPos(player.getX(), player.getY(), player.getZ());
		player.level().addFreshEntity(core);
	}

	@Override
	public void doFullShotlock(Player player, List<Entity> targetList) {
		doPartialShotlock(player, targetList);
	}
}
