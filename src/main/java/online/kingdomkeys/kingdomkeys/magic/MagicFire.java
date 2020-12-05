package online.kingdomkeys.kingdomkeys.magic;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ThrowableEntity;
import net.minecraft.util.Hand;
import online.kingdomkeys.kingdomkeys.entity.magic.FireEntity;

public class MagicFire extends Magic {

	public MagicFire(String registryName, int cost, int order) {
		super(registryName, cost, false, order);
		this.name = registryName;
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onUse(PlayerEntity player) {
		ThrowableEntity shot = new FireEntity(player.world, player);
		player.world.addEntity(shot);
		shot.shoot(player, player.rotationPitch, player.rotationYaw, 0, 1F, 0);
		// player.world.playSound(null, player.getPosition(), ModSounds.fistShot0,
		// SoundCategory.MASTER, 1F, 1F);
		// player.world.playSound(null, player.getPosition(),
		// Utils.getShootSound(player, message.charged), SoundCategory.MASTER, 1F, 1F);
		player.swingArm(Hand.MAIN_HAND);
	}

}
