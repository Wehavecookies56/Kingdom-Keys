package online.kingdomkeys.kingdomkeys.shotlock;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import online.kingdomkeys.kingdomkeys.client.sound.ModSounds;
import online.kingdomkeys.kingdomkeys.config.ModConfigs;
import online.kingdomkeys.kingdomkeys.entity.shotlock.SonicBladeCoreEntity;
import online.kingdomkeys.kingdomkeys.lib.DamageCalculation;

import java.awt.*;
import java.util.List;

public class ShotlockPhotonCharge extends Shotlock {

	private static final Color TRAIL_COLOR = new Color(255, 250, 200);

	public ShotlockPhotonCharge(ResourceLocation registryName, int order) {
		super(registryName, order);
	}

	@Override
	public void onUse(Player player, List<Entity> targetList) {
		player.level().playSound(null, player.position().x(), player.position().y(), player.position().z(), ModSounds.portal.get(), SoundSource.PLAYERS, 1F, 1.4F);
		super.onUse(player, targetList);
	}

	@Override
	public void doPartialShotlock(Player player, List<Entity> targetList) {
		SonicBladeCoreEntity core = new SonicBladeCoreEntity(player.level(), player, targetList, getDamage(player));
		core.setElement(getElement());
		core.setParticleColor(TRAIL_COLOR);
		core.setPos(player.getX(), player.getY(), player.getZ());
		player.level().addFreshEntity(core);
	}

	@Override
	public void doFullShotlock(Player player, List<Entity> targetList) {
		doPartialShotlock(player, targetList);
	}

	@Override
	public float getDamage(Player player) {
		return (float) (DamageCalculation.getStrengthDamage(player) * ModConfigs.shotlockMult) * getShotlockData().getDmgMult();
	}
}
