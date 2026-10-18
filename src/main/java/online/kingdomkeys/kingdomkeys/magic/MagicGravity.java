package online.kingdomkeys.kingdomkeys.magic;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ThrowableProjectile;
import online.kingdomkeys.kingdomkeys.client.sound.ModSounds;
import online.kingdomkeys.kingdomkeys.entity.magic.GravityEntity;

public class MagicGravity extends Magic {

	public MagicGravity(ResourceLocation registryName, int tier, ResourceLocation gmAbility) {
		super(registryName, false, gmAbility);
		setTier(tier);
	}

	@Override
	public void magicUse(LivingEntity player, LivingEntity caster, float fullMPBlastMult, LivingEntity lockOnEntity) {
		float dmg = getRealDamageMult(caster);
		dmg *= fullMPBlastMult;

		GravityEntity.Tier[] tiers = GravityEntity.Tier.values();
		if (getTier() >= 0 && getTier() < tiers.length) {
			GravityEntity.Tier tier = tiers[getTier()];
			ThrowableProjectile gravity = new GravityEntity(player.level(), player, dmg * tier.damageMult, tier);
			player.level().addFreshEntity(gravity);
			gravity.shootFromRotation(player, player.getXRot(), player.getYRot(), 0, tier.speed, 0);
			if (lockOnEntity != null) {
				gravity.setPos(lockOnEntity.getX(), lockOnEntity.getY(), lockOnEntity.getZ());
			}
		}
		player.swing(InteractionHand.MAIN_HAND);
	}

	@Override
	public void playMagicCastSound(LivingEntity player, LivingEntity caster) {
		switch (getTier()) {
			case 0 -> player.level().playSound(null, player.position().x(), player.position().y(), player.position().z(), ModSounds.gravity.get(), SoundSource.PLAYERS, 1F, 1F);
			case 1 -> player.level().playSound(null, player.position().x(), player.position().y(), player.position().z(), ModSounds.gravira.get(), SoundSource.PLAYERS, 1F, 1F);
			case 2 -> player.level().playSound(null, player.position().x(), player.position().y(), player.position().z(), ModSounds.graviga.get(), SoundSource.PLAYERS, 1F, 1F);
			case 3 -> player.level().playSound(null, player.position().x(), player.position().y(), player.position().z(), ModSounds.graviga.get(), SoundSource.PLAYERS, 1F, 1F);
		}
	}

}
