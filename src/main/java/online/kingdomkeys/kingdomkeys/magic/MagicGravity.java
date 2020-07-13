package online.kingdomkeys.kingdomkeys.magic;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ThrowableEntity;
import net.minecraft.util.Hand;
import online.kingdomkeys.kingdomkeys.entity.magic.GravityEntity;

public class MagicGravity extends Magic {
	String name;

	public MagicGravity(String registryName, int cost, int order) {
		super(registryName, cost, order);
		this.name = registryName;
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onUse(PlayerEntity player) {
		ThrowableEntity shot = new GravityEntity(player.world, player);
		player.world.addEntity(shot);
		shot.shoot(player, player.rotationPitch, player.rotationYaw, 0, 1F, 0);
		player.swingArm(Hand.MAIN_HAND);
	}

}
