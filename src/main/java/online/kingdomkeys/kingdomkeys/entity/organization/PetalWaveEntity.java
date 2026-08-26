package online.kingdomkeys.kingdomkeys.entity.organization;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import online.kingdomkeys.kingdomkeys.entity.ModEntities;

// A flat ring of petals drawn as a trail, either opening outwards (Marluxia's slam) or sitting still
// at a fixed size (the mark a column is about to come up through). It carries no logic of its own -
// whatever spawned it keeps the damage - it exists purely so the client has something to hang the
// render on.
public class PetalWaveEntity extends Entity {

	// The slam's wave. Its goal reads these too, so the damaging edge and the drawn edge can't drift
	// apart. Deliberately slow - it's meant to be seen coming and stepped over, not to be a flash.
	public static final int WAVE_TICKS = 32;
	public static final double WAVE_SPEED = 0.4;

	private static final EntityDataAccessor<Float> RADIUS_START = SynchedEntityData.defineId(PetalWaveEntity.class, EntityDataSerializers.FLOAT);
	private static final EntityDataAccessor<Float> RADIUS_END = SynchedEntityData.defineId(PetalWaveEntity.class, EntityDataSerializers.FLOAT);
	private static final EntityDataAccessor<Integer> DURATION = SynchedEntityData.defineId(PetalWaveEntity.class, EntityDataSerializers.INT);

	public PetalWaveEntity(EntityType<?> type, Level level) {
		super(type, level);
		this.noPhysics = true;
		this.noCulling = true;
	}

	// The slam's shockwave: one ring opening out along the ground.
	public static PetalWaveEntity wave(Level level, Vec3 at) {
		return build(level, at, 0F, (float) (WAVE_TICKS * WAVE_SPEED), WAVE_TICKS);
	}

	// A ring sitting still on the floor, marking where a column is about to come up.
	public static PetalWaveEntity mark(Level level, Vec3 at, float radius, int duration) {
		return build(level, at, radius, radius, duration);
	}

	private static PetalWaveEntity build(Level level, Vec3 at, float radiusStart, float radiusEnd, int duration) {
		PetalWaveEntity entity = new PetalWaveEntity(ModEntities.TYPE_PETAL_WAVE.get(), level);
		entity.setPos(at.x, at.y, at.z);
		entity.entityData.set(RADIUS_START, radiusStart);
		entity.entityData.set(RADIUS_END, radiusEnd);
		entity.entityData.set(DURATION, duration);
		return entity;
	}

	public float getRadiusStart() {
		return entityData.get(RADIUS_START);
	}

	public float getRadiusEnd() {
		return entityData.get(RADIUS_END);
	}

	public int getDuration() {
		return entityData.get(DURATION);
	}

	@Override
	public void tick() {
		super.tick();

		// A few ticks of grace past the end so the fade has time to finish rather than popping.
		if (!level().isClientSide && tickCount > getDuration() + 8) {
			discard();
		}
	}

	@Override
	protected void defineSynchedData(SynchedEntityData.Builder builder) {
		builder.define(RADIUS_START, 0F);
		builder.define(RADIUS_END, 1F);
		builder.define(DURATION, WAVE_TICKS);
	}

	@Override
	protected void readAdditionalSaveData(CompoundTag compound) {
	}

	@Override
	protected void addAdditionalSaveData(CompoundTag compound) {
	}

	@Override
	public boolean isPickable() {
		return false;
	}
}
