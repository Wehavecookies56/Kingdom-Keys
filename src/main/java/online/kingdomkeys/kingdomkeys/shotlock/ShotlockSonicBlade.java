package online.kingdomkeys.kingdomkeys.shotlock;

import java.util.List;

import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.fml.common.Mod;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.client.sound.ModSounds;
import online.kingdomkeys.kingdomkeys.config.ModConfigs;
import online.kingdomkeys.kingdomkeys.entity.shotlock.SonicBladeCoreEntity;
import online.kingdomkeys.kingdomkeys.lib.DamageCalculation;

@Mod.EventBusSubscriber(modid = KingdomKeys.MODID)
public class ShotlockSonicBlade extends Shotlock {

	public ShotlockSonicBlade(String registryName, int order, int cooldown, int max) {
		super(registryName, order, cooldown, max);
	}

	@Override
	public void onUse(Player player, List<Entity> targetList) {
		player.level().playSound(null, player.position().x(),player.position().y(),player.position().z(), ModSounds.portal.get(), SoundSource.PLAYERS, 1F, 1F);
		super.onUse(player,targetList);
	}

	@Override
	public void doPartialShotlock(Player player, List<Entity> targetList) {
		SonicBladeCoreEntity core = new SonicBladeCoreEntity(player.level(), player, targetList, getDamage(player));
		core.setPos(player.getX(), player.getY(), player.getZ());
		player.level().addFreshEntity(core);
	}

	@Override
	public void doFullShotlock(Player player, List<Entity> targetList) {
		doPartialShotlock(player,targetList);
	}

	@Override
	public float getDamage(Player player) {
		return (float) (DamageCalculation.getStrengthDamage(player) * ModConfigs.shotlockMult);
	}
}