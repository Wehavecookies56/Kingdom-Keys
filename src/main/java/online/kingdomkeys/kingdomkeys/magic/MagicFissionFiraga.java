package online.kingdomkeys.kingdomkeys.magic;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.LivingEntity;
import online.kingdomkeys.kingdomkeys.ability.ModAbilities;
import online.kingdomkeys.kingdomkeys.client.sound.ModSounds;
import online.kingdomkeys.kingdomkeys.entity.magic.FissionFiragaEntity;

public class MagicFissionFiraga extends Magic {

	public MagicFissionFiraga(ResourceLocation registryName, int tier, ResourceLocation gmAbility) {
		super(registryName, false, gmAbility);
		setTier(tier);
	}

	@Override
	public boolean isProjectile() {
		return true;
	}

	@Override
	public void magicUse(LivingEntity player, LivingEntity caster, float fullMPBlastMult, LivingEntity lockOnEntity) {
		float dmgMult = getRealDamageMult(caster) + abilityStacks(caster, ModAbilities.FIRE_BOOST) * 0.2F;
		dmgMult *= fullMPBlastMult;
		lockOnEntity = getMagicLockOn() ? lockOnEntity : null;

		FissionFiragaEntity fissionFiraga = new FissionFiragaEntity(player.level(), player, dmgMult, lockOnEntity);
		fissionFiraga.setMagic(this);
		player.level().addFreshEntity(fissionFiraga);
		fissionFiraga.shootFromRotation(player, player.getXRot(), player.getYRot(), 0, 1.5F, 0);
	}

	@Override
	public void playMagicCastSound(LivingEntity player, LivingEntity caster) {
		player.level().playSound(null, player.position().x(), player.position().y(), player.position().z(), ModSounds.firaga.get(), SoundSource.PLAYERS, 1F, 0.9F);
	}
}
