package online.kingdomkeys.kingdomkeys.magic;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import online.kingdomkeys.kingdomkeys.ability.ModAbilities;
import online.kingdomkeys.kingdomkeys.entity.magic.TripleBlizzagaControllerEntity;

public class MagicTripleBlizzaga extends Magic {

	public MagicTripleBlizzaga(ResourceLocation registryName, int tier, ResourceLocation gmAbility) {
		super(registryName, false, gmAbility);
		setTier(tier);
	}

	@Override
	public void magicUse(LivingEntity player, LivingEntity caster, float fullMPBlastMult, LivingEntity lockOnEntity) {
		float dmgMult = getRealDamageMult(caster) + abilityStacks(caster, ModAbilities.BLIZZARD_BOOST) * 0.2F;
		dmgMult *= fullMPBlastMult;
		lockOnEntity = getMagicLockOn() ? lockOnEntity : null;

		TripleBlizzagaControllerEntity tripleFiragaController = new TripleBlizzagaControllerEntity(player.level(), player, dmgMult, lockOnEntity);
		tripleFiragaController.setMagic(this);
		player.level().addFreshEntity(tripleFiragaController);
	}

	@Override
	public void playMagicCastSound(LivingEntity player, LivingEntity caster) {

	}
}
