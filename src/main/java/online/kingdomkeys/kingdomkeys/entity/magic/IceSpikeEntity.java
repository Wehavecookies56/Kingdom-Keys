package online.kingdomkeys.kingdomkeys.entity.magic;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ThrowableProjectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import online.kingdomkeys.kingdomkeys.damagesource.KKDamageTypes;
import online.kingdomkeys.kingdomkeys.entity.ModEntities;

public class IceSpikeEntity extends BaseMagicProjectile {

	private static final int RISE_TIME = 5;
	private static final int LIFE_TIME = 200;
	private static final EntityDataAccessor<Boolean> BREAKING = SynchedEntityData.defineId(IceSpikeEntity.class, EntityDataSerializers.BOOLEAN);
	private static final EntityDataAccessor<Integer> VARIANT = SynchedEntityData.defineId(IceSpikeEntity.class, EntityDataSerializers.INT);
	private boolean damaged = false;

	public IceSpikeEntity(EntityType<? extends ThrowableProjectile> type, Level level) {
		super(type, level);
	}

	public IceSpikeEntity(Level level, LivingEntity owner, float dmgMult) {
		super(ModEntities.TYPE_ICESPIKE.get(), owner, level);
		this.dmgMult = dmgMult;
		this.damageType = KKDamageTypes.ICE;
		this.blocksBuilding = true;
	}

	public void startBreaking() {
		entityData.set(BREAKING, true);
	}

	public boolean isBreaking() {
		return entityData.get(BREAKING);
	}

	private static final EntityDataAccessor<Float> BASE_YAW = SynchedEntityData.defineId(IceSpikeEntity.class, EntityDataSerializers.FLOAT);
	private static final EntityDataAccessor<Float> TILT = SynchedEntityData.defineId(IceSpikeEntity.class, EntityDataSerializers.FLOAT);

	@Override
	protected void defineSynchedData(SynchedEntityData.Builder builder) {
		super.defineSynchedData(builder);
		builder.define(BREAKING, false);
		builder.define(VARIANT, 0);
		builder.define(BASE_YAW, 0F);
		builder.define(TILT, 0F);
	}

	public int getVariant() {
		return entityData.get(VARIANT);
	}

	public void setVariant(int variant) {
		entityData.set(VARIANT, variant);
	}

	public float getBaseYaw() {
		return entityData.get(BASE_YAW);
	}

	public void setBaseYaw(float yaw) {
		entityData.set(BASE_YAW, yaw);
	}

	public float getTilt() {
		return entityData.get(TILT);
	}

	public void setTilt(float tilt) {
		entityData.set(TILT, tilt);
	}

	@Override
	protected double getDefaultGravity() {
		return 0;
	}

	int breakTicks = 0;
	@Override
	public void tick() {
		super.tick();

		if (isBreaking()) {
			breakTicks++;

			if (level() instanceof ServerLevel serverLevel) {
				serverLevel.sendParticles(ParticleTypes.SNOWFLAKE, getX(), getY() + 1, getZ(), 2, 0.2, 0.2, 0.2, 0.01);
			}

			if (breakTicks >= 10) {
				discard();
			}
		}

		if (tickCount == 1) {
			setPos(getX(), getY() - 1.2D, getZ());
		}

		if (tickCount <= RISE_TIME) {
			setPos(getX(), getY() + (1.2D / RISE_TIME), getZ());
			level().addParticle(ParticleTypes.SNOWFLAKE, getX(), getY() + 1, getZ(), 0, 0.05, 0);

			if (!level().isClientSide) {
				damageNearbyEntities();
			}
		}

		if (tickCount >= LIFE_TIME) {
			discard();
		}
	}

	private void damageNearbyEntities() {
		if (damaged) {
			return;
		}

		if (!(getOwner() instanceof LivingEntity owner)) {
			return;
		}

		AABB area = getBoundingBox().inflate(1.0D, 1.5D, 1.0D);
		for (LivingEntity target : level().getEntitiesOfClass(LivingEntity.class, area, e -> e != owner && e.isAlive())) {
			damageEntity(target);
			damaged = true;
		}
	}
}