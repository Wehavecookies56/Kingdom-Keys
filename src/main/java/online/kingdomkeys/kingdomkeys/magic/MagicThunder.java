package online.kingdomkeys.kingdomkeys.magic;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import online.kingdomkeys.kingdomkeys.ability.ModAbilities;
import online.kingdomkeys.kingdomkeys.entity.magic.ThunderEntity;

public class MagicThunder extends Magic {

	public MagicThunder(ResourceLocation registryName, int tier, ResourceLocation gmAbility) {
		super(registryName, false, gmAbility);
		setTier(tier);
	}

	@Override
	public void magicUse(LivingEntity player, LivingEntity caster, float fullMPBlastMult, LivingEntity lockOnEntity) {
		float dmgMult = getRealDamageMult(caster) + abilityStacks(caster, ModAbilities.THUNDER_BOOST) * 0.2F;
		dmgMult *= fullMPBlastMult;

		ThunderEntity.Tier[] tiers = ThunderEntity.Tier.values();
		if (getTier() >= 0 && getTier() < tiers.length) {
			player.level().addFreshEntity(new ThunderEntity(player.level(), player, dmgMult, lockOnEntity, tiers[getTier()]));
		}
	}

	@Override
	public void playMagicCastSound(LivingEntity player, LivingEntity caster) {

	}
}