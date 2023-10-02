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

	public MagicGravity(ResourceLocation registryName, int maxLevel, String gmAbility) {
		super(registryName, false, maxLevel, gmAbility);
	}

	@Override
	protected void magicUse(Player player, Player caster, int level, float fullMPBlastMult, LivingEntity lockOnEntity) {
		float dmg = /*ModCapabilities.getPlayer(player).isAbilityEquipped(Strings.waterBoost) ? getDamageMult(level) * 1.2F :*/ getDamageMult(level);
		dmg *= fullMPBlastMult;
		player.level.playSound(null, player.blockPosition(), ModSounds.gravity.get(), SoundSource.PLAYERS, 1F, 1F);

		switch (level) {
			case 0 -> {
				ThrowableProjectile gravity = new GravityEntity(player.level, player, dmg * 1F);
				player.level.addFreshEntity(gravity);
				gravity.shootFromRotation(player, player.getXRot(), player.getYRot(), 0, 2F, 0);
				if(lockOnEntity != null) {
					gravity.setPos(lockOnEntity.getX(), lockOnEntity.getY(), lockOnEntity.getZ());
				}
			}
			case 1 -> {
				ThrowableProjectile gravira = new GraviraEntity(player.level, player, dmg * 1.1F);
				player.level.addFreshEntity(gravira);
				gravira.shootFromRotation(player, player.getXRot(), player.getYRot(), 0, 2.3F, 0);
				if(lockOnEntity != null) {
					gravira.setPos(lockOnEntity.getX(), lockOnEntity.getY(), lockOnEntity.getZ());
				}

			}
			case 2 -> {
				ThrowableProjectile graviga = new GravigaEntity(player.level, player, dmg * 1.2F);
				player.level.addFreshEntity(graviga);
				graviga.shootFromRotation(player, player.getXRot(), player.getYRot(), 0, 2.6F, 0);
				if(lockOnEntity != null) {
					graviga.setPos(lockOnEntity.getX(), lockOnEntity.getY(), lockOnEntity.getZ());
				}

			}
		}
		player.swing(InteractionHand.MAIN_HAND);
	}

}
