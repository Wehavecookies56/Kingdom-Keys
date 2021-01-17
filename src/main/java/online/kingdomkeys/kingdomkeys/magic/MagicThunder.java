package online.kingdomkeys.kingdomkeys.magic;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Hand;
import online.kingdomkeys.kingdomkeys.entity.magic.ThunderEntity;

public class MagicThunder extends Magic {

	public MagicThunder(String registryName, int cost, int order) {
		super(registryName, cost, false, order);
		this.name = registryName;
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onUse(PlayerEntity player, PlayerEntity caster) {
		ThunderEntity thunderController = new ThunderEntity(player.world, player);
		thunderController.setCaster(player.getUniqueID());
		player.world.addEntity(thunderController);
		caster.swingArm(Hand.MAIN_HAND);
	}

}
