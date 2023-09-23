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

	public MagicFire(ResourceLocation registryName, int maxLevel, String gmAbility, int order) {
		super(registryName, false, maxLevel, gmAbility, order);
	}

	@Override
	protected void magicUse(Player player, Player caster, int level, float fullMPBlastMult, LivingEntity lockOnEntity) {
		float dmgMult = getDamageMult(level) + ModCapabilities.getPlayer(player).getNumberOfAbilitiesEquipped(Strings.fireBoost) * 0.2F;
		dmgMult *= fullMPBlastMult;

		switch (level) {
		case 0:
			ThrowableProjectile fire = new FireEntity(player.level, player, dmgMult, lockOnEntity);
			player.level.addFreshEntity(fire);
			fire.shootFromRotation(player, player.getXRot(), player.getYRot(), 0, 2F, 0);
			player.level.playSound(null, player.blockPosition(), ModSounds.fire.get(), SoundSource.PLAYERS, 1F, 1F);
			break;
		case 1:
			ThrowableProjectile fira = new FiraEntity(player.level, player, dmgMult);
			player.level.addFreshEntity(fira);
			fira.shootFromRotation(player, player.getXRot(), player.getYRot(), 0, 2F, 0);
			player.level.playSound(null, player.blockPosition(), ModSounds.fire.get(), SoundSource.PLAYERS, 1F, 1F);
			break;
		case 2:
			ThrowableProjectile firaga = new FiragaEntity(player.level, player, dmgMult);
			player.level.addFreshEntity(firaga);
			firaga.shootFromRotation(player, player.getXRot(), player.getYRot(), 0, 2F, 0);
			player.level.playSound(null, player.blockPosition(), ModSounds.fire.get(), SoundSource.PLAYERS, 1F, 1F);
			break;
		case 3:
			ThrowableProjectile firaza = new FirazaEntity(player.level, player, dmgMult);
			player.level.addFreshEntity(firaza);
			firaza.shootFromRotation(player, player.getXRot(), player.getYRot(), 0, 2F, 0);
			player.level.playSound(null, player.blockPosition(), ModSounds.fire.get(), SoundSource.PLAYERS, 1F, 0.5F);
			break;
		}
		
	}
}
