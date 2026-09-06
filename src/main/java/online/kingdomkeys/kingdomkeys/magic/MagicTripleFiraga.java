package online.kingdomkeys.kingdomkeys.magic;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import online.kingdomkeys.kingdomkeys.ability.ModAbilities;
import online.kingdomkeys.kingdomkeys.entity.magic.TripleFiragaControllerEntity;

public class MagicTripleFiraga extends Magic {

	public MagicTripleFiraga(ResourceLocation registryName, int tier, ResourceLocation gmAbility) {
		super(registryName, false, gmAbility);
		setTier(tier);
	}

	@Override
	public void magicUse(LivingEntity player, LivingEntity caster, float fullMPBlastMult, LivingEntity lockOnEntity) {
		float dmgMult = getRealDamageMult(caster) + abilityStacks(caster, ModAbilities.FIRE_BOOST) * 0.2F;
		dmgMult *= fullMPBlastMult;
		lockOnEntity = getMagicLockOn() ? lockOnEntity : null;

		TripleFiragaControllerEntity tripleFiragaController = new TripleFiragaControllerEntity(player.level(), player, dmgMult, lockOnEntity);
		tripleFiragaController.setMagic(this);
		player.level().addFreshEntity(tripleFiragaController);
	}

	@Override
	public void playMagicCastSound(LivingEntity player, LivingEntity caster) {

	}
}
