package online.kingdomkeys.kingdomkeys.magic;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import online.kingdomkeys.kingdomkeys.ability.ModAbilities;
import online.kingdomkeys.kingdomkeys.entity.magic.FaithEntityController;

public class MagicFaith extends Magic {

	public MagicFaith(ResourceLocation registryName, boolean hasToSelect, int tier, ResourceLocation gmAbility) {
		super(registryName, hasToSelect, gmAbility);
		setTier(tier);
	}

	@Override
	public void magicUse(LivingEntity player, LivingEntity caster, float fullMPBlastMult, LivingEntity lockOnEntity) {
		float dmgMult = getDamageMult() + abilityStacks(caster, ModAbilities.THUNDER_BOOST) * 0.25F;
		dmgMult *= fullMPBlastMult;

		FaithEntityController faith = new FaithEntityController(player.level(), player, dmgMult, lockOnEntity);
		faith.setPos(player.getX(), player.getY() + 1.8F, player.getZ());
		player.level().addFreshEntity(faith);
	}

	@Override
	public void playMagicCastSound(LivingEntity player, LivingEntity caster) {
	}
}
