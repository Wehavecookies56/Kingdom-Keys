package online.kingdomkeys.kingdomkeys.magic;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.LivingEntity;
import online.kingdomkeys.kingdomkeys.ability.ModAbilities;
import online.kingdomkeys.kingdomkeys.client.sound.ModSounds;
import online.kingdomkeys.kingdomkeys.entity.magic.BaseMagicProjectile;
import online.kingdomkeys.kingdomkeys.entity.magic.DarkFiragaEntity;
import online.kingdomkeys.kingdomkeys.entity.magic.FireEntity;
import online.kingdomkeys.kingdomkeys.lib.Strings;

public class MagicFire extends Magic {

	public MagicFire(ResourceLocation registryName, int tier, ResourceLocation gmAbility) {
		super(registryName, false, gmAbility);
		setTier(tier);
	}

	@Override
	public boolean isProjectile() {
		return true;
	}

	@Override
	public void magicUse(LivingEntity player, LivingEntity caster, float fullMPBlastMult, LivingEntity lockOnEntity) {
		float dmgMult = getRealDamageMult(caster) + abilityStacks(caster, ModAbilities.FIRAZA) * 0.2F;
		dmgMult *= fullMPBlastMult;
		lockOnEntity = getMagicLockOn() ? lockOnEntity : null;

		//If it's dark firaga cast it directly
		if (is(Strings.Magic_DarkFiraga)) {
			DarkFiragaEntity darkFiraga = new DarkFiragaEntity(player.level(), player, dmgMult, lockOnEntity);
			darkFiraga.setMagic(this);
			player.level().addFreshEntity(darkFiraga);
			darkFiraga.shootFromRotation(player, player.getXRot(), player.getYRot(), 0, 2F, 0);
			return;
		}

		FireEntity.Tier[] tiers = FireEntity.Tier.values();
		if (getTier() >= 0 && getTier() < tiers.length) {
			BaseMagicProjectile fire = new FireEntity(player.level(), player, dmgMult, lockOnEntity, tiers[getTier()]);
			fire.setMagic(this);
			player.level().addFreshEntity(fire);
			fire.shootFromRotation(player, player.getXRot(), player.getYRot(), 0, 2F, 0);
		}

	}

	@Override
	public void playMagicCastSound(LivingEntity player, LivingEntity caster) {
		switch (getTier()) {
			case 0 -> player.level().playSound(null, player.position().x(), player.position().y(), player.position().z(), ModSounds.fire.get(), SoundSource.PLAYERS, 1F, 1F);
			case 1 -> player.level().playSound(null, player.position().x(), player.position().y(), player.position().z(), ModSounds.fira.get(), SoundSource.PLAYERS, 1F, 1F);
			case 2 -> player.level().playSound(null, player.position().x(), player.position().y(), player.position().z(), ModSounds.firaga.get(), SoundSource.PLAYERS, 1F, 1F);
			case 3 -> player.level().playSound(null, player.position().x(), player.position().y(), player.position().z(), ModSounds.firaga.get(), SoundSource.PLAYERS, 1F, 0.7F);
		}
	}
}
