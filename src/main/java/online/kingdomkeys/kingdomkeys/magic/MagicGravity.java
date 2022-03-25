package online.kingdomkeys.kingdomkeys.magic;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ThrowableProjectile;
import net.minecraft.world.InteractionHand;
import online.kingdomkeys.kingdomkeys.entity.magic.GravigaEntity;
import online.kingdomkeys.kingdomkeys.entity.magic.GraviraEntity;
import online.kingdomkeys.kingdomkeys.entity.magic.GravityEntity;

public class MagicGravity extends Magic {

	public MagicGravity(String registryName, int maxLevel, boolean hasRC, int order) {
		super(registryName, false, maxLevel, hasRC, order);
	}

	@Override
	protected void magicUse(Player player, Player caster, int level, float fullMPBlastMult) {
		float dmg = /*ModCapabilities.getPlayer(player).isAbilityEquipped(Strings.waterBoost) ? getDamageMult(level) * 1.2F :*/ getDamageMult(level);
		dmg *= fullMPBlastMult;
		
		switch(level) {
		case 0:
			ThrowableProjectile gravity = new GravityEntity(player.level, player, dmg);
			player.level.addFreshEntity(gravity);
			gravity.shootFromRotation(player, player.getXRot(), player.getYRot(), 0, 2F, 0);
			break;
		case 1:
			ThrowableProjectile gravira = new GraviraEntity(player.level, player, dmg);
			player.level.addFreshEntity(gravira);
			gravira.shootFromRotation(player, player.getXRot(), player.getYRot(), 0, 2.3F, 0);
			break;
		case 2:
			ThrowableProjectile graviga = new GravigaEntity(player.level, player, dmg);
			player.level.addFreshEntity(graviga);
			graviga.shootFromRotation(player, player.getXRot(), player.getYRot(), 0, 2.6F, 0);
			break;
		}		
		player.swing(InteractionHand.MAIN_HAND);
	}

}
