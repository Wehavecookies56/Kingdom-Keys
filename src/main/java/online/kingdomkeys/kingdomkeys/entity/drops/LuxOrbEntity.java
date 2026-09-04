package online.kingdomkeys.kingdomkeys.entity.drops;

import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import online.kingdomkeys.kingdomkeys.client.sound.ModSounds;
import online.kingdomkeys.kingdomkeys.data.PlayerData;
import online.kingdomkeys.kingdomkeys.entity.ModEntities;

public class LuxOrbEntity extends ItemDropEntity {
	private static final double BOB_HEIGHT = 0.06D;
	private static final double BOB_PERIOD = 60.0D;

	public LuxOrbEntity(Level worldIn, double x, double y, double z, int value) {
		super(ModEntities.TYPE_LUXORB.get(), worldIn, x, y, z, value);
		setNoGravity(true);
	}

	public LuxOrbEntity(EntityType<? extends Entity> type, Level world) {
		super(type, world);
		setNoGravity(true);
	}

	@Override
	public void tick() {
		super.tick();
		if (this.closestPlayer == null) {
			Vec3 motion = getDeltaMovement();
			double bob = Math.cos(tickCount * (2 * Math.PI / BOB_PERIOD)) * BOB_HEIGHT;
			setDeltaMovement(motion.x * 0.9D, bob, motion.z * 0.9D);
		}
	}

	@Override
	void onPickup(Player player) {
		PlayerData playerData = PlayerData.get(player);
		if (playerData != null && playerData.hasUnion()) {
			playerData.addLux(value);
		}
	}

	@Override
	public SoundEvent getPickupSound() {
		return ModSounds.hp_orb.get();
	}
}
