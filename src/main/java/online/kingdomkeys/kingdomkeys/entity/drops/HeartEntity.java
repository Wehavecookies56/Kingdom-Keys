package online.kingdomkeys.kingdomkeys.entity.drops;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import online.kingdomkeys.kingdomkeys.data.PlayerData;
import online.kingdomkeys.kingdomkeys.entity.ModEntities;

import java.util.UUID;

public class HeartEntity extends Entity {

	public final static int MAX_TICKS = 30;

	// A heart lost to the darkness does not fade: it drifts away to where its owner can find it, and waits there as a light
	private static final EntityDataAccessor<Boolean> GUIDE = SynchedEntityData.defineId(HeartEntity.class, EntityDataSerializers.BOOLEAN);
	private static final double GUIDE_SPEED = 0.3D;
	private static final int OWNER_CHECK = 20;

	private Vec3 light;
	private UUID owner;

	public HeartEntity(EntityType<? extends Entity> type, Level world) {
		super(type, world);
		this.blocksBuilding = true;
	}

	public HeartEntity(Level world) {
		this(ModEntities.TYPE_HEART.get(), world);
	}

	public static HeartEntity guide(Level level, Vec3 from, Vec3 light, UUID owner) {
		HeartEntity heart = new HeartEntity(level);
		heart.setPos(from.x, from.y, from.z);
		heart.light = light;
		heart.owner = owner;
		heart.noPhysics = true;
		heart.entityData.set(GUIDE, true);
		return heart;
	}

	public boolean isGuide() {
		return entityData.get(GUIDE);
	}

	public boolean isGuideFor(UUID player) {
		return isGuide() && player.equals(owner);
	}

	@Override
	public void tick(){
		if (isGuide()) {
			tickGuide();
			super.tick();
			return;
		}

		if(this.tickCount < MAX_TICKS - 10) {
			this.setPos(getX(), getY() + 0.15, getZ());
		} else {
			level().addParticle(ParticleTypes.DRAGON_BREATH, getX(), getY(), getZ(), 0, 0, 0);
		}

		if(this.tickCount >= MAX_TICKS) {
			this.remove(RemovalReason.KILLED);
		}

		super.tick();
	}

	private void tickGuide() {
		if (level().isClientSide) {
			level().addParticle(ParticleTypes.END_ROD, getX() + (random.nextDouble() - 0.5D) * 0.4D, getY() + 0.3D + (random.nextDouble() - 0.5D) * 0.4D, getZ() + (random.nextDouble() - 0.5D) * 0.4D, 0D, 0.01D, 0D);
			return;
		}

		// Loaded back from disk the destination is gone; the owner's own tick puts a new one out
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
		if (step.lengthSqr() > GUIDE_SPEED * GUIDE_SPEED) {
			step = step.normalize().scale(GUIDE_SPEED);
		}
		setPos(getX() + step.x, getY() + step.y, getZ() + step.z);
	}

	@Override
	protected void defineSynchedData(SynchedEntityData.Builder pBuilder) {
		pBuilder.define(GUIDE, false);
	}

	@Override
	protected void readAdditionalSaveData(CompoundTag compound) {

	}

	@Override
	protected void addAdditionalSaveData(CompoundTag compound) {

	}

}
