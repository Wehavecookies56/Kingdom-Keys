package online.kingdomkeys.kingdomkeys.magic;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ThrowableProjectile;
import online.kingdomkeys.kingdomkeys.ability.Ability;
import online.kingdomkeys.kingdomkeys.capability.ModCapabilities;
import online.kingdomkeys.kingdomkeys.entity.magic.FiraEntity;
import online.kingdomkeys.kingdomkeys.entity.magic.FiragaEntity;
import online.kingdomkeys.kingdomkeys.entity.magic.FirazaEntity;
import online.kingdomkeys.kingdomkeys.entity.magic.FireEntity;
import online.kingdomkeys.kingdomkeys.lib.Strings;
import online.kingdomkeys.kingdomkeys.reactioncommands.ReactionCommand;

public class MagicFire extends Magic {

	public MagicFire(String registryName, int maxLevel, boolean hasRC, String gmAbility, int order) {
		super(registryName, false, maxLevel, hasRC, gmAbility, order);
	}

	@Override
	protected void magicUse(Player player, Player caster, int level, float fullMPBlastMult) {
		float dmg = ModCapabilities.getPlayer(player).isAbilityEquipped(Strings.fireBoost) ? getDamageMult(level) * 1.2F : getDamageMult(level);
		dmg *= fullMPBlastMult;

		switch (level) {
		case 0:
			ThrowableProjectile fire = new FireEntity(player.level, player, dmg);
			player.level.addFreshEntity(fire);
			fire.shootFromRotation(player, player.getXRot(), player.getYRot(), 0, 2F, 0);
			player.level.playSound(null, player.blockPosition(), SoundEvents.GHAST_SHOOT, SoundSource.PLAYERS, 1F, 1F);
			break;
		case 1:
			ThrowableProjectile fira = new FiraEntity(player.level, player, dmg);
			player.level.addFreshEntity(fira);
			fira.shootFromRotation(player, player.getXRot(), player.getYRot(), 0, 2F, 0);
			player.level.playSound(null, player.blockPosition(), SoundEvents.GHAST_SHOOT, SoundSource.PLAYERS, 1F, 1F);
			break;
		case 2:
			ThrowableProjectile firaga = new FiragaEntity(player.level, player, dmg);
			player.level.addFreshEntity(firaga);
			firaga.shootFromRotation(player, player.getXRot(), player.getYRot(), 0, 2F, 0);
			player.level.playSound(null, player.blockPosition(), SoundEvents.GHAST_SHOOT, SoundSource.PLAYERS, 1F, 1F);
			break;
		case 3:
			ThrowableProjectile firaza = new FirazaEntity(player.level, player, dmg);
			player.level.addFreshEntity(firaza);
			firaza.shootFromRotation(player, player.getXRot(), player.getYRot(), 0, 2F, 0);
			player.level.playSound(null, player.blockPosition(), SoundEvents.GHAST_SHOOT, SoundSource.PLAYERS, 1F, 0.5F);
			break;
		}
		
	}
}
