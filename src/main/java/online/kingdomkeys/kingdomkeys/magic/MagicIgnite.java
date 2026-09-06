package online.kingdomkeys.kingdomkeys.magic;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import online.kingdomkeys.kingdomkeys.util.Utils;

import java.util.List;

public class MagicIgnite extends Magic {
	public MagicIgnite(ResourceLocation registryName, int tier, ResourceLocation gmAbility) {
		super(registryName, false, gmAbility);
		setTier(tier);
	}

	@Override
	public void magicUse(LivingEntity player, LivingEntity caster, float fullMPBlastMult, LivingEntity lockOnEntity) {
		float dmgMult = getRealDamageMult(caster) * fullMPBlastMult;


		int localLevel = getMagicLocalLevel(caster);
		int radius = 3 + localLevel;

		LivingEntity target = getMagicLockOn() && lockOnEntity != null ? lockOnEntity : getRandomEntity(caster, radius);
		if (target != null) {
			target.setRemainingFireTicks(Math.round(dmgMult));
		}
		player.swing(InteractionHand.MAIN_HAND);
	}

	public LivingEntity getRandomEntity(LivingEntity caster, float radius) {
		List<LivingEntity> list = Utils.getLivingEntitiesInRadiusExcludingParty(caster, radius);
		if (list.isEmpty()) {
			return null;
		}
		return list.get(Utils.randomWithRange(0, list.size() - 1));
	}

	@Override
	public void playMagicCastSound(LivingEntity player, LivingEntity caster) {
		player.level().playSound(null, player.position().x(), player.position().y(), player.position().z(), SoundEvents.GHAST_SHOOT, SoundSource.PLAYERS, 1F, 1F);
	}

}
