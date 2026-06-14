package online.kingdomkeys.kingdomkeys.magic;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ThrowableProjectile;
import online.kingdomkeys.kingdomkeys.client.sound.ModSounds;
import online.kingdomkeys.kingdomkeys.entity.magic.GravigaEntity;
import online.kingdomkeys.kingdomkeys.entity.magic.GraviraEntity;
import online.kingdomkeys.kingdomkeys.entity.magic.GravityEntity;

public class MagicGravity extends Magic {

	public MagicGravity(ResourceLocation registryName, int tier, String gmAbility) {
		super(registryName, false, gmAbility);
		setTier(tier);
	}

	@Override
	public void magicUse(LivingEntity player, Player caster, float fullMPBlastMult, LivingEntity lockOnEntity) {
		float dmg = getRealDamageMult(caster);
		dmg *= fullMPBlastMult;

		switch (getTier()) {
			case 0 -> {
				ThrowableProjectile gravity = new GravityEntity(player.level(), player, dmg);
				player.level().addFreshEntity(gravity);
				gravity.shootFromRotation(player, player.getXRot(), player.getYRot(), 0, 2F, 0);
				if (lockOnEntity != null) {
					gravity.setPos(lockOnEntity.getX(), lockOnEntity.getY(), lockOnEntity.getZ());
				}
			}
			case 1 -> {
				ThrowableProjectile gravira = new GraviraEntity(player.level(), player, dmg * 1.1F);
				player.level().addFreshEntity(gravira);
				gravira.shootFromRotation(player, player.getXRot(), player.getYRot(), 0, 2.3F, 0);
				if (lockOnEntity != null) {
					gravira.setPos(lockOnEntity.getX(), lockOnEntity.getY(), lockOnEntity.getZ());
				}

			}
			case 2 -> {
				ThrowableProjectile graviga = new GravigaEntity(player.level(), player, dmg * 1.2F);
				player.level().addFreshEntity(graviga);
				graviga.shootFromRotation(player, player.getXRot(), player.getYRot(), 0, 2.6F, 0);
				if (lockOnEntity != null) {
					graviga.setPos(lockOnEntity.getX(), lockOnEntity.getY(), lockOnEntity.getZ());
				}

			}
		}
		player.swing(InteractionHand.MAIN_HAND);
	}

	@Override
	public void playMagicCastSound(LivingEntity player, Player caster) {
		switch (getTier()) {
			case 0 -> player.level().playSound(null, player.position().x(), player.position().y(), player.position().z(), ModSounds.gravity.get(), SoundSource.PLAYERS, 1F, 1F);
			case 1 -> player.level().playSound(null, player.position().x(), player.position().y(), player.position().z(), ModSounds.gravira.get(), SoundSource.PLAYERS, 1F, 1F);
			case 2 -> player.level().playSound(null, player.position().x(), player.position().y(), player.position().z(), ModSounds.graviga.get(), SoundSource.PLAYERS, 1F, 1F);
			case 3 -> player.level().playSound(null, player.position().x(), player.position().y(), player.position().z(), ModSounds.graviga.get(), SoundSource.PLAYERS, 1F, 1F);
		}
	}

}
