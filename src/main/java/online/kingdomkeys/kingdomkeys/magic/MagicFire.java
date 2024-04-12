package online.kingdomkeys.kingdomkeys.magic;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ThrowableProjectile;
import online.kingdomkeys.kingdomkeys.capability.ModCapabilities;
import online.kingdomkeys.kingdomkeys.client.sound.ModSounds;
import online.kingdomkeys.kingdomkeys.entity.magic.FiraEntity;
import online.kingdomkeys.kingdomkeys.entity.magic.FiragaEntity;
import online.kingdomkeys.kingdomkeys.entity.magic.FirazaEntity;
import online.kingdomkeys.kingdomkeys.entity.magic.FireEntity;
import online.kingdomkeys.kingdomkeys.lib.Strings;

public class MagicFire extends Magic {

	public MagicFire(ResourceLocation registryName, int maxLevel, String gmAbility) {
		super(registryName, false, maxLevel, gmAbility);
	}

	@Override
	public void magicUse(Player player, Player caster, int level, float fullMPBlastMult, LivingEntity lockOnEntity) {
		float dmgMult = getDamageMult(level) + ModCapabilities.getPlayer(player).getNumberOfAbilitiesEquipped(Strings.fireBoost) * 0.2F;
		dmgMult *= fullMPBlastMult;

		lockOnEntity = getMagicLockOn(level) ? lockOnEntity : null;
				
		switch (level) {
		case 0:
			ThrowableProjectile fire = new FireEntity(player.level(), player, dmgMult, lockOnEntity);
			player.level().addFreshEntity(fire);
			fire.shootFromRotation(player, player.getXRot(), player.getYRot(), 0, 2F, 0);
			break;
		case 1:
			ThrowableProjectile fira = new FiraEntity(player.level(), player, dmgMult, lockOnEntity);
			player.level().addFreshEntity(fira);
			fira.shootFromRotation(player, player.getXRot(), player.getYRot(), 0, 2F, 0);
			break;
		case 2:
			ThrowableProjectile firaga = new FiragaEntity(player.level(), player, dmgMult, lockOnEntity);
			player.level().addFreshEntity(firaga);
			firaga.shootFromRotation(player, player.getXRot(), player.getYRot(), 0, 2F, 0);
			break;
		case 3:
			ThrowableProjectile firaza = new FirazaEntity(player.level(), player, dmgMult);
			player.level().addFreshEntity(firaza);
			firaza.shootFromRotation(player, player.getXRot(), player.getYRot(), 0, 2F, 0);
			break;
		}
		
	}
	
	@Override
	protected void playMagicCastSound(Player player, Player caster, int level) {
		switch (level) {
			case 0 -> player.level().playSound(null, player.position().x(), player.position().y(), player.position().z(), ModSounds.fire.get(), SoundSource.PLAYERS, 1F, 1F);
			case 1 -> player.level().playSound(null, player.position().x(), player.position().y(), player.position().z(), ModSounds.fira.get(), SoundSource.PLAYERS, 1F, 1F);
			case 2 -> player.level().playSound(null, player.position().x(), player.position().y(), player.position().z(), ModSounds.firaga.get(), SoundSource.PLAYERS, 1F, 1F);
			case 3 -> player.level().playSound(null, player.position().x(), player.position().y(), player.position().z(), ModSounds.firaga.get(), SoundSource.PLAYERS, 1F, 0.7F);
		}
	}
}
