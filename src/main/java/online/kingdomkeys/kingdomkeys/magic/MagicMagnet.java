package online.kingdomkeys.kingdomkeys.magic;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ThrowableEntity;
import net.minecraft.util.Hand;
import online.kingdomkeys.kingdomkeys.entity.magic.BlizzardEntity;
import online.kingdomkeys.kingdomkeys.entity.magic.FireEntity;
import online.kingdomkeys.kingdomkeys.entity.magic.MagnetEntity;

public class MagicMagnet extends Magic {

	public MagicMagnet(String registryName, int cost, int order) {
		super(registryName, cost, false, order);
		this.name = registryName;
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onUse(PlayerEntity player, PlayerEntity caster) {
		MagnetEntity shot = new MagnetEntity(player.world, player);
		shot.setCaster(player.getUniqueID());
		player.world.addEntity(shot);
		shot.func_234612_a_(player, -90, player.rotationYaw, 0, 1F, 0);
		player.swingArm(Hand.MAIN_HAND);
	}

}
