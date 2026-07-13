package online.kingdomkeys.kingdomkeys.magic;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import online.kingdomkeys.kingdomkeys.ability.ModAbilities;
import online.kingdomkeys.kingdomkeys.data.PlayerData;
import online.kingdomkeys.kingdomkeys.entity.magic.WaterEntity;
import online.kingdomkeys.kingdomkeys.entity.magic.WateraEntity;
import online.kingdomkeys.kingdomkeys.entity.magic.WatergaEntity;
import online.kingdomkeys.kingdomkeys.entity.magic.WaterzaEntity;
import online.kingdomkeys.kingdomkeys.lib.Strings;

public class MagicWater extends Magic {

	public MagicWater(ResourceLocation registryName, int tier, ResourceLocation gmAbility) {
		super(registryName, false, gmAbility);
		setTier(tier);
	}

	@Override
	public void magicUse(LivingEntity player, Player caster, float fullMPBlastMult, LivingEntity lockOnEntity) {
		float dmgMult = getRealDamageMult(caster) + PlayerData.get(caster).getNumberOfAbilitiesEquipped(ModAbilities.WATER_BOOST) * 0.2F;
		dmgMult *= fullMPBlastMult;

		switch (getTier()) {
			case 0 -> {
				WaterEntity water = new WaterEntity(player.level(), player, dmgMult);
				player.level().addFreshEntity(water);
			}
			case 1 -> {
				WateraEntity watera = new WateraEntity(player.level(), player, dmgMult);
				player.level().addFreshEntity(watera);
			}
			case 2 -> {
				WatergaEntity waterga = new WatergaEntity(player.level(), player, dmgMult);
				player.level().addFreshEntity(waterga);
			}
			case 3 -> {
				WaterzaEntity waterza = new WaterzaEntity(player.level(), player, dmgMult);
				player.level().addFreshEntity(waterza);
			}
		}

		if (player.isOnFire()) {
			player.clearFire();
		}
	}

	@Override
	public void playMagicCastSound(LivingEntity player, Player caster) {
		player.level().playSound(null, player.position().x(), player.position().y(), player.position().z(), SoundEvents.WATER_AMBIENT, SoundSource.PLAYERS, 1F, 1F);
	}

}