package online.kingdomkeys.kingdomkeys.shotlock;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import online.kingdomkeys.kingdomkeys.entity.shotlock.RagnarokCoreEntity;

import java.util.List;

public class ShotlockThunderstorm extends Shotlock {

	private static final int SHOT_COLOR = 0xFFE24A;

	public ShotlockThunderstorm(ResourceLocation registryName, int order) {
		super(registryName, order);
	}

	@Override
	public void doPartialShotlock(Player player, List<Entity> targetList) {
		RagnarokCoreEntity core = new RagnarokCoreEntity(player.level(), player, targetList, getDamage(player));
		core.setShotColor(SHOT_COLOR);
		core.setElement(getElement());
		core.setPos(player.getX(), player.getY(), player.getZ());
		player.level().addFreshEntity(core);
	}

	@Override
	public void doFullShotlock(Player player, List<Entity> targetList) {
		doPartialShotlock(player, targetList);
	}
}
