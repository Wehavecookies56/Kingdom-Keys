package online.kingdomkeys.kingdomkeys.magic;

import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ThrowableProjectile;
import online.kingdomkeys.kingdomkeys.client.sound.ModSounds;
import online.kingdomkeys.kingdomkeys.entity.magic.GravigaEntity;
import online.kingdomkeys.kingdomkeys.entity.magic.GraviraEntity;
import online.kingdomkeys.kingdomkeys.entity.magic.GravityEntity;

public class MagicGravity extends Magic {

	public MagicGravity(String registryName, int maxLevel, String gmAbility, int order) {
		super(registryName, false, maxLevel, gmAbility, order);
	}

	@Override
	protected void magicUse(Player player, Player caster, int level, float fullMPBlastMult) {
		float dmg = /*ModCapabilities.getPlayer(player).isAbilityEquipped(Strings.waterBoost) ? getDamageMult(level) * 1.2F :*/ getDamageMult(level);
		dmg *= fullMPBlastMult;
		player.level.playSound(null, player.blockPosition(), ModSounds.gravity.get(), SoundSource.PLAYERS, 1F, 1F);

		switch(level) {
		case 0:
			ThrowableProjectile gravity = new GravityEntity(player.level, player, dmg * 1F);
			player.level.addFreshEntity(gravity);
			gravity.shootFromRotation(player, player.getXRot(), player.getYRot(), 0, 2F, 0);
			break;
		case 1:
			ThrowableProjectile gravira = new GraviraEntity(player.level, player, dmg * 1.1F);
			player.level.addFreshEntity(gravira);
			gravira.shootFromRotation(player, player.getXRot(), player.getYRot(), 0, 2.3F, 0);
			break;
		case 2:
			ThrowableProjectile graviga = new GravigaEntity(player.level, player, dmg * 1.2F);
			player.level.addFreshEntity(graviga);
			graviga.shootFromRotation(player, player.getXRot(), player.getYRot(), 0, 2.6F, 0);
			break;
		}		
		player.swing(InteractionHand.MAIN_HAND);
	}

}
