package online.kingdomkeys.kingdomkeys.entity;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import online.kingdomkeys.kingdomkeys.data.PlayerData;

import java.util.UUID;

public class HeartlessLightEntity extends Entity {
	private static final double SPEED = 0.3D;
	private static final int OWNER_CHECK = 20;

	private Vec3 light;
	private UUID owner;

	public HeartlessLightEntity(EntityType<? extends Entity> type, Level world) {
		super(type, world);
		this.noPhysics = true;
	}

	public HeartlessLightEntity(Level level, Vec3 from, Vec3 light, UUID owner) {
		this(ModEntities.TYPE_HEARTLESS_LIGHT.get(), level);
		setPos(from.x, from.y, from.z);
		this.light = light;
		this.owner = owner;
	}

	public boolean isFor(UUID player) {
		return player.equals(owner);
	}

	@Override
	public void tick() {
		super.tick();
		if (level().isClientSide) {
			level().addParticle(ParticleTypes.END_ROD, getX() + (random.nextDouble() - 0.5D) * 0.4D, getY() + 0.3D + (random.nextDouble() - 0.5D) * 0.4D, getZ() + (random.nextDouble() - 0.5D) * 0.4D, 0D, 0.01D, 0D);
			return;
		}

		if (light == null || owner == null) {
			discard();
			return;
		}

		if (tickCount % OWNER_CHECK == 0) {
			Player player = level().getPlayerByUUID(owner);
			PlayerData data = player == null ? null : PlayerData.get(player);
			if (data != null && !data.isHeartless()) {
				discard();
				return;
			}
		}

		// Drifts off to the light and then bobs there
		Vec3 to = light.add(0D, 1D + Math.sin(tickCount * 0.1D) * 0.15D, 0D);
		Vec3 step = to.subtract(position());
		if (step.lengthSqr() > SPEED * SPEED) {
			step = step.normalize().scale(SPEED);
		}
		setPos(getX() + step.x, getY() + step.y, getZ() + step.z);
	}

	@Override
	protected void defineSynchedData(SynchedEntityData.Builder builder) {
	}

	@Override
	protected void readAdditionalSaveData(CompoundTag compound) {
	}

	@Override
	protected void addAdditionalSaveData(CompoundTag compound) {
	}
}
