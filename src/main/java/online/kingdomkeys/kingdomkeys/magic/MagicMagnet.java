package online.kingdomkeys.kingdomkeys.magic;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import online.kingdomkeys.kingdomkeys.client.sound.ModSounds;
import online.kingdomkeys.kingdomkeys.entity.magic.MagnegaEntity;
import online.kingdomkeys.kingdomkeys.entity.magic.MagneraEntity;
import online.kingdomkeys.kingdomkeys.entity.magic.MagnetEntity;

public class MagicMagnet extends Magic {

	public MagicMagnet(ResourceLocation registryName, int maxLevel, String gmAbility, int order) {
		super(registryName, false, maxLevel, gmAbility, order);
	}

	@Override
	protected void magicUse(Player player, Player caster, int level, float fullMPBlastMult) {
		float dmg = /*ModCapabilities.getPlayer(player).isAbilityEquipped(Strings.waterBoost) ? getDamageMult(level) * 1.2F :*/ getDamageMult(level);
		dmg *= fullMPBlastMult;

		switch(level) {
		case 0:
			player.level.playSound(null, player.blockPosition(), ModSounds.magnet1.get(), SoundSource.PLAYERS, 1F, 1.1F);
			MagnetEntity magent = new MagnetEntity(player.level, player, dmg);
			magent.setCaster(player.getUUID());
			player.level.addFreshEntity(magent);
			magent.shootFromRotation(player, -90, player.getYRot(), 0, 1F, 0);
			break;
		case 1:
			player.level.playSound(null, player.blockPosition(), ModSounds.magnet1.get(), SoundSource.PLAYERS, 1F, 0.9F);
			MagneraEntity magnera = new MagneraEntity(player.level, player, dmg);
			magnera.setCaster(player.getUUID());
			player.level.addFreshEntity(magnera);
			magnera.shootFromRotation(player, -90, player.getYRot(), 0, 1F, 0);
			break;
		case 2:
			player.level.playSound(null, player.blockPosition(), ModSounds.magnet1.get(), SoundSource.PLAYERS, 1F, 0.8F);
			MagnegaEntity magnega = new MagnegaEntity(player.level, player, dmg);
			magnega.setCaster(player.getUUID());
			player.level.addFreshEntity(magnega);
			magnega.shootFromRotation(player, -90, player.getYRot(), 0, 1F, 0);
			break;
		}
		
		player.swing(InteractionHand.MAIN_HAND);
	}

}
