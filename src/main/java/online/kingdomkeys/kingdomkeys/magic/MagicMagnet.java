package online.kingdomkeys.kingdomkeys.magic;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
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
	protected void magicUse(Player player, Player caster, int level, float fullMPBlastMult, LivingEntity lockOnEntity) {
		float dmg = /*ModCapabilities.getPlayer(player).isAbilityEquipped(Strings.waterBoost) ? getDamageMult(level) * 1.2F :*/ getDamageMult(level);
		dmg *= fullMPBlastMult;

		switch(level) {
		case 0:
			player.level.playSound(null, player.blockPosition(), ModSounds.magnet1.get(), SoundSource.PLAYERS, 1F, 1.1F);
			MagnetEntity magnet = new MagnetEntity(player.level, player, dmg);
			magnet.setCaster(player.getUUID());
			if(lockOnEntity != null) {
				magnet.setPos(lockOnEntity.getX(), lockOnEntity.getY()+1, lockOnEntity.getZ());
				magnet.shootFromRotation(lockOnEntity, -90, lockOnEntity.getYRot(),0,1,0);
			} else {
				magnet.shootFromRotation(player, -90, player.getYRot(), 0, 1F, 0);
			}
			player.level.addFreshEntity(magnet);

			break;
		case 1:
			player.level.playSound(null, player.blockPosition(), ModSounds.magnet1.get(), SoundSource.PLAYERS, 1F, 0.9F);
			MagneraEntity magnera = new MagneraEntity(player.level, player, dmg);
			magnera.setCaster(player.getUUID());
			if(lockOnEntity != null) {
				magnera.setPos(lockOnEntity.getX(), lockOnEntity.getY()+1, lockOnEntity.getZ());
				magnera.shootFromRotation(lockOnEntity, -90, lockOnEntity.getYRot(),0,1,0);
			} else {
				magnera.shootFromRotation(player, -90, player.getYRot(), 0, 1F, 0);
			}			
			player.level.addFreshEntity(magnera);
			break;
		case 2:
			player.level.playSound(null, player.blockPosition(), ModSounds.magnet1.get(), SoundSource.PLAYERS, 1F, 0.8F);
			MagnegaEntity magnega = new MagnegaEntity(player.level, player, dmg);
			magnega.setCaster(player.getUUID());
			if(lockOnEntity != null) {
				magnega.setPos(lockOnEntity.getX(), lockOnEntity.getY()+1, lockOnEntity.getZ());
				magnega.shootFromRotation(lockOnEntity, -90, lockOnEntity.getYRot(),0,1,0);
			} else {
				magnega.shootFromRotation(player, -90, player.getYRot(), 0, 1F, 0);
			}
			player.level.addFreshEntity(magnega);
			break;
		}
		
		player.swing(InteractionHand.MAIN_HAND);
	}

}
