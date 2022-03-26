package online.kingdomkeys.kingdomkeys.magic;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import online.kingdomkeys.kingdomkeys.entity.magic.WaterEntity;
import online.kingdomkeys.kingdomkeys.entity.magic.WateraEntity;
import online.kingdomkeys.kingdomkeys.entity.magic.WatergaEntity;
import online.kingdomkeys.kingdomkeys.entity.magic.WaterzaEntity;

public class MagicWater extends Magic {

	public MagicWater(String registryName, int maxLevel, boolean hasRC, int order) {
		super(registryName, false, maxLevel, hasRC, order);
	}

	@Override
	protected void magicUse(Player player, Player caster, int level, float fullMPBlastMult) {
		float dmg = /*ModCapabilities.getPlayer(player).isAbilityEquipped(Strings.waterBoost) ? getDamageMult(level) * 1.2F :*/ getDamageMult(level);
		dmg *= fullMPBlastMult;

		switch(level) {
		case 0:
			WaterEntity water = new WaterEntity(player.level, player, dmg);
			water.setCaster(player.getDisplayName().getString());
			player.level.addFreshEntity(water);
			player.level.playSound(null, player.blockPosition(), SoundEvents.WATER_AMBIENT, SoundSource.PLAYERS, 1F, 1F);
			break;
		case 1:
			WateraEntity watera = new WateraEntity(player.level, player, dmg);
			watera.setCaster(player.getDisplayName().getString());
			player.level.addFreshEntity(watera);
			player.level.playSound(null, player.blockPosition(), SoundEvents.WATER_AMBIENT, SoundSource.PLAYERS, 1F, 1F);
			break;
		case 2:
			WatergaEntity waterga = new WatergaEntity(player.level, player, dmg);
			waterga.setCaster(player.getDisplayName().getString());
			player.level.addFreshEntity(waterga);
			player.level.playSound(null, player.blockPosition(), SoundEvents.WATER_AMBIENT, SoundSource.PLAYERS, 1F, 1F);
			break;
		case 3:
			WaterzaEntity waterza = new WaterzaEntity(player.level, player, dmg);
			waterza.setCaster(player.getDisplayName().getString());
			player.level.addFreshEntity(waterza);
			player.level.playSound(null, player.blockPosition(), SoundEvents.WATER_AMBIENT, SoundSource.PLAYERS, 1F, 1F);
			break;
		}
		
		if(player.isOnFire()) {
			player.clearFire();
		}
	}

}
