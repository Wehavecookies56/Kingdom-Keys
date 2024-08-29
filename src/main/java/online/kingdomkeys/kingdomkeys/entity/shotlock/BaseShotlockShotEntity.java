package online.kingdomkeys.kingdomkeys.entity.shotlock;

import java.util.Optional;
import java.util.UUID;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ThrowableProjectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.HitResult;
import online.kingdomkeys.kingdomkeys.data.ModData;
import online.kingdomkeys.kingdomkeys.lib.Strings;

public class BaseShotlockShotEntity extends ThrowableProjectile{
	
	int maxTicks = 100;
	public float dmg;

	public BaseShotlockShotEntity(EntityType<? extends ThrowableProjectile> type, Level world) {
		super(type, world);
		this.blocksBuilding = true;
	}

	public BaseShotlockShotEntity(EntityType<? extends ThrowableProjectile> type, Level world, LivingEntity player, Entity target, double dmg) {
		super(type, player, world);
		this.dmg = (float)dmg;
		setTarget(target.getId());
	}

	@Override
	protected double getDefaultGravity() {
		return 0D;
	}

	@Override
	public void remove(RemovalReason reason) {
		if(tickCount > 20) {
			super.remove(RemovalReason.KILLED);
		}
	}

	public int getMaxTicks() {
		return maxTicks;
	}

	public void setMaxTicks(int maxTicks) {
		this.maxTicks = maxTicks;
	}

	@Override
	protected void onHit(HitResult pResult) {
		if(!level().isClientSide) {
			if(getOwner() != null && getOwner() instanceof Player owner) {
	    		IPlayerData playerData = ModData.getPlayer(owner);
	    		if(playerData != null) {
	    			if(playerData.getNumberOfAbilitiesEquipped(Strings.hpGain) > 0) {
	    				owner.heal(playerData.getNumberOfAbilitiesEquipped(Strings.hpGain)*2);
	    			}
	    		}
	    	}
		}
	}
	@Override
	public void addAdditionalSaveData(CompoundTag compound) {
		super.addAdditionalSaveData(compound);
		if (this.entityData.get(OWNER) != null) {
			compound.putString("OwnerUUID", this.entityData.get(OWNER).get().toString());
			compound.putInt("TargetUUID", this.entityData.get(TARGET));
			compound.putInt("Color", this.entityData.get(COLOR));
		}
	}

	@Override
	public void readAdditionalSaveData(CompoundTag compound) {
		super.readAdditionalSaveData(compound);
		this.entityData.set(OWNER, Optional.of(UUID.fromString(compound.getString("OwnerUUID"))));
		this.entityData.set(TARGET, compound.getInt("TargetUUID"));
		this.entityData.set(COLOR, compound.getInt("Color"));
	}

	private static final EntityDataAccessor<Optional<UUID>> OWNER = SynchedEntityData.defineId(BaseShotlockShotEntity.class, EntityDataSerializers.OPTIONAL_UUID);
	private static final EntityDataAccessor<Integer> TARGET = SynchedEntityData.defineId(BaseShotlockShotEntity.class, EntityDataSerializers.INT);
	private static final EntityDataAccessor<Integer> COLOR = SynchedEntityData.defineId(BaseShotlockShotEntity.class, EntityDataSerializers.INT);

	public Player getCaster() {
		return this.getEntityData().get(OWNER).isPresent() ? this.level().getPlayerByUUID(this.getEntityData().get(OWNER).get()) : null;
	}

	public void setCaster(UUID uuid) {
		this.entityData.set(OWNER, Optional.of(uuid));
	}

	public Entity getTarget() {
		return this.level().getEntity(this.getEntityData().get(TARGET));
	}

	public void setTarget(int i) {
		this.entityData.set(TARGET, i);
	}

	public int getColor() {
		return this.getEntityData().get(COLOR);
	}
	
	public void setColor(int color) {
		this.entityData.set(COLOR, color);
	}

	@Override
	protected void defineSynchedData(SynchedEntityData.Builder pBuilder) {
		pBuilder.define(OWNER, Optional.of(new UUID(0L, 0L)));
		pBuilder.define(TARGET, 0);
		pBuilder.define(COLOR, 0);
	}
}
