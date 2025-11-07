package online.kingdomkeys.kingdomkeys.entity;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ThrowableProjectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;

import java.util.Optional;
import java.util.UUID;

public class GummiShotEntity extends ThrowableProjectile{

	int maxTicks = 100;
	public float dmg;

	public GummiShotEntity(EntityType<? extends ThrowableProjectile> type, Level world) {
		super(type, world);
		this.blocksBuilding = true;
	}

	public GummiShotEntity(EntityType<? extends ThrowableProjectile> type, Level world, LivingEntity player, double dmg) {
		super(type, player, world);
		this.dmg = (float)dmg;
	}

    public GummiShotEntity(Level world, LivingEntity player, double dmg) {
        this(ModEntities.TYPE_GUMMI_SHOT.get(), world, player, dmg);
    }

    @Override
    public void tick() {
        super.tick();
        if(tickCount > 100) {
            super.remove(RemovalReason.KILLED);
        }
    }

    @Override
    protected void onHit(HitResult rtRes) {
        super.onHit(rtRes);
        if (!level().isClientSide) {
            if (rtRes instanceof EntityHitResult ertResult) {
                if (ertResult.getEntity() instanceof LivingEntity target) {
                    if (target != getOwner()) {
                        target.invulnerableTime = 0;
                        target.hurt(target.damageSources().thrown(this, this.getOwner()), dmg);
                        super.remove(RemovalReason.KILLED);
                    }
                }
            }
            remove(RemovalReason.KILLED);
        }
    }

    @Override
	protected double getDefaultGravity() {
		return 0D;
	}

	@Override
	public void remove(RemovalReason reason) {
		if(tickCount > 100) {
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
	public void addAdditionalSaveData(CompoundTag compound) {
		super.addAdditionalSaveData(compound);
		if (this.entityData.get(OWNER).isPresent()) {
			compound.putString("OwnerUUID", this.entityData.get(OWNER).get().toString());
			compound.putInt("Color", this.entityData.get(COLOR));
		}
	}

	@Override
	public void readAdditionalSaveData(CompoundTag compound) {
		super.readAdditionalSaveData(compound);
		this.entityData.set(OWNER, Optional.of(UUID.fromString(compound.getString("OwnerUUID"))));
		this.entityData.set(COLOR, compound.getInt("Color"));
	}

	private static final EntityDataAccessor<Optional<UUID>> OWNER = SynchedEntityData.defineId(GummiShotEntity.class, EntityDataSerializers.OPTIONAL_UUID);
	private static final EntityDataAccessor<Integer> COLOR = SynchedEntityData.defineId(GummiShotEntity.class, EntityDataSerializers.INT);

	public Player getCaster() {
		return this.getEntityData().get(OWNER).isPresent() ? this.level().getPlayerByUUID(this.getEntityData().get(OWNER).get()) : null;
	}

	public void setCaster(UUID uuid) {
		this.entityData.set(OWNER, Optional.of(uuid));
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
		pBuilder.define(COLOR, 0);
	}
}
