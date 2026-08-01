package online.kingdomkeys.kingdomkeys.entity.shotlock;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ThrowableProjectile;
import net.minecraft.world.level.Level;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public abstract class BaseShotlockCoreEntity extends ThrowableProjectile {

	private static final EntityDataAccessor<Optional<UUID>> OWNER = SynchedEntityData.defineId(BaseShotlockCoreEntity.class, EntityDataSerializers.OPTIONAL_UUID);
	private static final EntityDataAccessor<String> TARGETS = SynchedEntityData.defineId(BaseShotlockCoreEntity.class, EntityDataSerializers.STRING);

	protected int maxTicks = 100;
	protected float dmg;
	protected List<Entity> targetList = new ArrayList<>();

	protected BaseShotlockCoreEntity(EntityType<? extends ThrowableProjectile> type, Level world) {
		super(type, world);
		this.blocksBuilding = true;
	}

	protected BaseShotlockCoreEntity(EntityType<? extends ThrowableProjectile> type, Level world, Player caster, List<Entity> targets, float dmg) {
		super(type, caster, world);
		setCaster(caster.getUUID());
		setTarget(joinTargetIds(targets));
		this.targetList = targets;
		this.dmg = dmg;
	}

	/**
	 * Turns the locked-on entity ids into the csv string the synched data carries.
	 */
	private static String joinTargetIds(List<Entity> targets) {
		StringBuilder ids = new StringBuilder();

		for (Entity target : targets) {
			if (target == null) {
				continue;
			}
			if (!ids.isEmpty()) {
				ids.append(',');
			}
			ids.append(target.getId());
		}

		return ids.toString();
	}

	public Player getCaster() {
		return this.getEntityData().get(OWNER).isPresent() ? this.level().getPlayerByUUID(this.getEntityData().get(OWNER).get()) : null;
	}

	public void setCaster(UUID uuid) {
		this.entityData.set(OWNER, Optional.of(uuid));
	}

	public List<Entity> getTargets() {
		List<Entity> list = new ArrayList<>();
		String[] ids = this.getEntityData().get(TARGETS).split(",");

		for (String id : ids) {
			if (!id.isEmpty())
				list.add(level().getEntity(Integer.parseInt(id)));
		}
		return list;
	}

	public void setTarget(String lists) {
		this.entityData.set(TARGETS, lists);
	}

	public int getMaxTicks() {
		return maxTicks;
	}

	public void setMaxTicks(int maxTicks) {
		this.maxTicks = maxTicks;
	}

	protected boolean isExpired() {
		return this.tickCount > maxTicks || getCaster() == null;
	}

	@Override
	public void addAdditionalSaveData(CompoundTag compound) {
		super.addAdditionalSaveData(compound);
		if (this.entityData.get(OWNER).isPresent()) {
			compound.putString("OwnerUUID", this.entityData.get(OWNER).get().toString());
			compound.putString("TargetsUUID", this.entityData.get(TARGETS));
		}
	}

	@Override
	public void readAdditionalSaveData(CompoundTag compound) {
		super.readAdditionalSaveData(compound);
		this.entityData.set(OWNER, Optional.of(UUID.fromString(compound.getString("OwnerUUID"))));
		this.entityData.set(TARGETS, compound.getString("TargetUUID"));
	}

	@Override
	protected void defineSynchedData(SynchedEntityData.Builder pBuilder) {
		pBuilder.define(OWNER, Optional.of(new UUID(0L, 0L)));
		pBuilder.define(TARGETS, "");
	}
}
