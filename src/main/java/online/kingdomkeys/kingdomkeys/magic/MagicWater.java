package online.kingdomkeys.kingdomkeys.magic;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import online.kingdomkeys.kingdomkeys.entity.magic.WaterEntity;
import online.kingdomkeys.kingdomkeys.entity.magic.WateraEntity;
import online.kingdomkeys.kingdomkeys.entity.magic.WatergaEntity;
import online.kingdomkeys.kingdomkeys.entity.magic.WaterzaEntity;

public class MagicWater extends Magic {

	public MagicWater(String registryName, int maxLevel, boolean hasRC, int order) {
		super(registryName, false, maxLevel, hasRC, order);
	}

	@Override
	protected void magicUse(PlayerEntity player, PlayerEntity caster, int level) {
		switch(level) {
		case 0:
			WaterEntity water = new WaterEntity(player.world, player, getDamageMult());
			water.setCaster(player.getDisplayName().getString());
			player.world.addEntity(water);
			player.world.playSound(null, player.getPosition(), SoundEvents.BLOCK_WATER_AMBIENT, SoundCategory.PLAYERS, 1F, 1F);
			break;
		case 1:
			WateraEntity watera = new WateraEntity(player.world, player, getDamageMult());
			watera.setCaster(player.getDisplayName().getString());
			player.world.addEntity(watera);
			player.world.playSound(null, player.getPosition(), SoundEvents.BLOCK_WATER_AMBIENT, SoundCategory.PLAYERS, 1F, 1F);
			break;
		case 2:
			WatergaEntity waterga = new WatergaEntity(player.world, player, getDamageMult());
			waterga.setCaster(player.getDisplayName().getString());
			player.world.addEntity(waterga);
			player.world.playSound(null, player.getPosition(), SoundEvents.BLOCK_WATER_AMBIENT, SoundCategory.PLAYERS, 1F, 1F);
			break;
		case 3:
			WaterzaEntity waterza = new WaterzaEntity(player.world, player, getDamageMult());
			waterza.setCaster(player.getDisplayName().getString());
			player.world.addEntity(waterza);
			player.world.playSound(null, player.getPosition(), SoundEvents.BLOCK_WATER_AMBIENT, SoundCategory.PLAYERS, 1F, 1F);
			break;
		}
		
		if(player.isBurning()) {
			player.extinguish();
		}
	}

}
