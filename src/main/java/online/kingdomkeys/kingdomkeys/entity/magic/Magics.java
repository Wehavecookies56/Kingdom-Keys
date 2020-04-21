package online.kingdomkeys.kingdomkeys.entity.magic;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ThrowableEntity;
import net.minecraft.util.Hand;

public class Magics {
	public static void fire(PlayerEntity player) {
		ThrowableEntity shot = new EntityFire(player.world, player);
		player.world.addEntity(shot);
		shot.shoot(player, player.rotationPitch, player.rotationYaw, 0, 1F, 0);
		//player.world.playSound(null, player.getPosition(), ModSounds.fistShot0, SoundCategory.MASTER, 1F, 1F);
		//player.world.playSound(null, player.getPosition(), Utils.getShootSound(player, message.charged), SoundCategory.MASTER, 1F, 1F);
		player.swingArm(Hand.MAIN_HAND);
		
	}
	
	public static void blizzard(PlayerEntity player) {
		ThrowableEntity shot = new EntityBlizzard(player.world, player);
		player.world.addEntity(shot);
		shot.shoot(player, player.rotationPitch, player.rotationYaw, 0, 1F, 0);
		player.swingArm(Hand.MAIN_HAND);
		
	}
}
