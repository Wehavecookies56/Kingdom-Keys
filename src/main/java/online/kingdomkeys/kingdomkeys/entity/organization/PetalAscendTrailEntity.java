package online.kingdomkeys.kingdomkeys.entity.organization;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;

public class PetalAscendTrailEntity extends Entity {

	private static final int DURATION_TICKS = 25;

	private static final EntityDataAccessor<Integer> TARGET = SynchedEntityData.defineId(PetalAscendTrailEntity.class, EntityDataSerializers.INT);

	public PetalAscendTrailEntity(EntityType<?> type, Level level) {
		super(type, level);
		this.noPhysics = true;
		this.noCulling = true;
	}

	public PetalAscendTrailEntity(EntityType<?> type, Level level, LivingEntity target) {
		this(type, level);
		this.entityData.set(TARGET, target.getId());
		this.setPos(target.getX(), target.getY(), target.getZ());
	}

	public LivingEntity getTarget() {
		if (this.entityData.get(TARGET) == 0)
			return null;

		int id = this.entityData.get(TARGET);
		Entity e = level().getEntity(id);
		return e instanceof LivingEntity living ? living : null;
	}

	@Override
	public void tick() {
		super.tick();

		LivingEntity target = getTarget();
		if (target == null || !target.isAlive() || this.tickCount > DURATION_TICKS) {
			if (!level().isClientSide) {
				this.discard();
			}
			return;
		}

		this.setPos(target.getX(), target.getY(), target.getZ());
	}

	@Override
	protected void defineSynchedData(SynchedEntityData.Builder builder) {
		builder.define(TARGET, 0);
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
