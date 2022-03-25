package online.kingdomkeys.kingdomkeys.magic;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.InteractionHand;
import online.kingdomkeys.kingdomkeys.entity.magic.MagnegaEntity;
import online.kingdomkeys.kingdomkeys.entity.magic.MagneraEntity;
import online.kingdomkeys.kingdomkeys.entity.magic.MagnetEntity;

public class MagicMagnet extends Magic {

	public MagicMagnet(String registryName, int maxLevel, boolean hasRC, int order) {
		super(registryName, false, maxLevel, hasRC, order);
	}

	@Override
	protected void magicUse(Player player, Player caster, int level, float fullMPBlastMult) {
		float dmg = /*ModCapabilities.getPlayer(player).isAbilityEquipped(Strings.waterBoost) ? getDamageMult(level) * 1.2F :*/ getDamageMult(level);
		dmg *= fullMPBlastMult;
		
		switch(level) {
		case 0:
			MagnetEntity magent = new MagnetEntity(player.level, player, dmg);
			magent.setCaster(player.getUUID());
			player.level.addFreshEntity(magent);
			magent.shootFromRotation(player, -90, player.getYRot(), 0, 1F, 0);
			break;
		case 1:
			MagneraEntity magnera = new MagneraEntity(player.level, player, dmg);
			magnera.setCaster(player.getUUID());
			player.level.addFreshEntity(magnera);
			magnera.shootFromRotation(player, -90, player.getYRot(), 0, 1F, 0);
			break;
		case 2:
			MagnegaEntity magnega = new MagnegaEntity(player.level, player, dmg);
			magnega.setCaster(player.getUUID());
			player.level.addFreshEntity(magnega);
			magnega.shootFromRotation(player, -90, player.getYRot(), 0, 1F, 0);
			break;
		}
		
		player.swing(InteractionHand.MAIN_HAND);
	}

}
