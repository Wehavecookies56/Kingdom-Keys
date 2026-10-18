package online.kingdomkeys.kingdomkeys.magic;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.LivingEntity;
import online.kingdomkeys.kingdomkeys.ability.ModAbilities;
import online.kingdomkeys.kingdomkeys.entity.magic.WaterEntity;

public class MagicWater extends Magic {

	public MagicWater(ResourceLocation registryName, int tier, ResourceLocation gmAbility) {
		super(registryName, false, gmAbility);
		setTier(tier);
	}

	@Override
	public void magicUse(LivingEntity player, LivingEntity caster, float fullMPBlastMult, LivingEntity lockOnEntity) {
		float dmgMult = getRealDamageMult(caster) + abilityStacks(caster, ModAbilities.WATER_BOOST) * 0.2F;
		dmgMult *= fullMPBlastMult;

		WaterEntity.Tier[] tiers = WaterEntity.Tier.values();
		if (getTier() >= 0 && getTier() < tiers.length) {
			WaterEntity water = new WaterEntity(player.level(), player, dmgMult, tiers[getTier()]);
			water.setMagic(this);
			player.level().addFreshEntity(water);
		}

		if (player.isOnFire()) {
			player.clearFire();
		}
	}

	@Override
	public void playMagicCastSound(LivingEntity player, LivingEntity caster) {
		player.level().playSound(null, player.position().x(), player.position().y(), player.position().z(), SoundEvents.WATER_AMBIENT, SoundSource.PLAYERS, 1F, 1F);
	}

}