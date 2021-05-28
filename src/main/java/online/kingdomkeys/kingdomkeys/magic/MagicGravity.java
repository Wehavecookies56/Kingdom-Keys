package online.kingdomkeys.kingdomkeys.magic;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ThrowableEntity;
import net.minecraft.util.Hand;
import online.kingdomkeys.kingdomkeys.entity.magic.GravigaEntity;
import online.kingdomkeys.kingdomkeys.entity.magic.GraviraEntity;
import online.kingdomkeys.kingdomkeys.entity.magic.GravityEntity;

public class MagicGravity extends Magic {

	public MagicGravity(String registryName, int maxLevel, boolean hasRC, int order) {
		super(registryName, false, maxLevel, hasRC, order);
	}

	@Override
	protected void magicUse(PlayerEntity player, PlayerEntity caster, int level) {
		switch(level) {
		case 0:
			ThrowableEntity gravity = new GravityEntity(player.world, player, getDamageMult());
			player.world.addEntity(gravity);
			gravity.setDirectionAndMovement(player, player.rotationPitch, player.rotationYaw, 0, 2F, 0);
			break;
		case 1:
			ThrowableEntity gravira = new GraviraEntity(player.world, player, getDamageMult());
			player.world.addEntity(gravira);
			gravira.setDirectionAndMovement(player, player.rotationPitch, player.rotationYaw, 0, 2.3F, 0);
			break;
		case 2:
			ThrowableEntity graviga = new GravigaEntity(player.world, player, getDamageMult());
			player.world.addEntity(graviga);
			graviga.setDirectionAndMovement(player, player.rotationPitch, player.rotationYaw, 0, 2.6F, 0);
			break;
		}		
		player.swingArm(Hand.MAIN_HAND);
	}

}
