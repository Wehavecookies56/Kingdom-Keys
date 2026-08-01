package online.kingdomkeys.kingdomkeys.entity.shotlock;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ThrowableProjectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import online.kingdomkeys.kingdomkeys.entity.ModEntities;

import java.util.List;

public class MinigameShotEntity extends BaseShotlockShotEntity {

	// How long the shots travel straight out before they turn on the target.
	public static final int OUTWARD_TICKS = 10;

	private static final double OUTWARD_SPEED = 0.75D;
	private static final int HOMING_INTERVAL = 8;
	// How far ahead we'll look for something to chase, and how tight the cone has to be.
	private static final double AIM_RANGE = 48D;
	private static final double AIM_COS = 0.94D; // ~20 degrees

	private static final EntityDataAccessor<Float> SPREAD_ANGLE = SynchedEntityData.defineId(MinigameShotEntity.class, EntityDataSerializers.FLOAT);

	// Look direction captured when the shot turns, used when there's nothing worth homing onto.
	private Vec3 fallbackAim = null;

	// Set the moment this shot connects, so it can only ever deal its damage once.
	private boolean spent = false;

	// Mirrored from the Shotlock's own bullet style.
	private boolean applyPoison = false;
	boolean waterVisual = false;

	public MinigameShotEntity(EntityType<? extends ThrowableProjectile> type, Level world) {
		super(type, world);
		this.blocksBuilding = true;
		this.maxTicks = 80;
	}

	public MinigameShotEntity(Level world, LivingEntity caster, Entity target, double dmg) {
		super(ModEntities.TYPE_SHOTLOCK_MINIGAME_SHOT.get(), world, caster, target, dmg);
		this.maxTicks = 80;
	}

	// Absolute yaw (degrees) of the outward leg.
	public void setSpreadAngle(float degrees) {
		this.entityData.set(SPREAD_ANGLE, degrees);
	}

	public float getSpreadAngle() {
		return this.entityData.get(SPREAD_ANGLE);
	}

	@Override
	public void tick() {
		if (this.tickCount > maxTicks) {
			this.remove(RemovalReason.KILLED);
			return;
		}

		if (this.tickCount == 1) {
			double rad = Math.toRadians(getSpreadAngle());
			// Minecraft yaw 0 faces +Z, and grows clockwise looking down.
			Vec3 dir = new Vec3(-Math.sin(rad), 0.12D, Math.cos(rad)).normalize();
			this.setDeltaMovement(dir.scale(OUTWARD_SPEED));
		}

		if (waterVisual && this.tickCount > 1 && level() instanceof ServerLevel serverLevel) {
			serverLevel.sendParticles(ParticleTypes.SPLASH, getX(), getY(), getZ(), 4, 0.2, 0.2, 0.2, 0.05);
		}

		if (this.tickCount == OUTWARD_TICKS) {
			acquireAim();
			steer();
		} else if (this.tickCount > OUTWARD_TICKS && (this.tickCount - OUTWARD_TICKS) % HOMING_INTERVAL == 0) {
			steer();
		}

		super.tick();

		if (this.tickCount > OUTWARD_TICKS) {
			applySpiralWobble();
		}
	}

	// Picks what to chase: whatever the caster is actually looking at wins, the originally locked target is the fallback, and failing both the shot just keeps going where the caster aimed.
	private void acquireAim() {
		if (level().isClientSide) {
			return;
		}

		Entity owner = getOwner();
		if (owner == null) {
			return;
		}

		Vec3 eye = owner.getEyePosition();
		Vec3 look = owner.getLookAngle().normalize();
		fallbackAim = look;

		Entity best = null;
		double bestDot = AIM_COS;

		AABB box = new AABB(eye, eye).inflate(AIM_RANGE);
		List<LivingEntity> candidates = level().getEntitiesOfClass(LivingEntity.class, box, e -> e.isAlive() && e != owner);

		for (LivingEntity candidate : candidates) {
			Vec3 toCandidate = candidate.getBoundingBox().getCenter().subtract(eye);
			if (toCandidate.lengthSqr() < 1.0E-4) {
				continue;
			}
			double dot = toCandidate.normalize().dot(look);
			if (dot > bestDot) {
				bestDot = dot;
				best = candidate;
			}
		}

		if (best != null) {
			setTarget(best.getId());
		}
	}

	private void steer() {
		Entity target = getTarget();

		if (target != null && target.isAlive() && target != getOwner()) {
			double dx = target.getX() - this.getX();
			double dy = (target.getY() + (target.getBbHeight() / 2.0F) - this.getBbHeight()) - this.getY() + 0.5D;
			double dz = target.getZ() - this.getZ();
			this.shoot(dx, dy, dz, 1.1F, 0);
			return;
		}

		Vec3 aim = fallbackAim;
		if (aim == null && getOwner() != null) {
			aim = getOwner().getLookAngle();
		}
		if (aim != null) {
			this.shoot(aim.x, aim.y, aim.z, 1.1F, 0);
		}
	}

	@Override
	protected void onHit(HitResult result) {
		if (spent) {
			return;
		}
		spent = true;

		super.onHit(result);

		if (!level().isClientSide && result instanceof EntityHitResult entityHit && entityHit.getEntity() instanceof LivingEntity target && target != getOwner()) {
			target.invulnerableTime = 0;
			target.hurt(buildDamageSource(target), dmg);

			if (applyPoison) {
				target.addEffect(new MobEffectInstance(MobEffects.POISON, 100, 0, false, true, true));
			}
		}

		remove(RemovalReason.KILLED);
	}

	@Override
	public void remove(RemovalReason reason) {
		this.setRemoved(reason);
	}

	@Override
	protected boolean canHitEntity(Entity entity) {
		return entity != getOwner() && super.canHitEntity(entity);
	}

	@Override
	protected void defineSynchedData(SynchedEntityData.Builder builder) {
		super.defineSynchedData(builder);
		builder.define(SPREAD_ANGLE, 0F);
	}

	public void setApplyPoison(boolean applyPoison) {
		this.applyPoison = applyPoison;
	}

	public static void spawnBurst(Player caster, Entity target, float damage, BaseShotlockCoreEntity.ShotStyle style, int count, float angleOffset) {
		for (int i = 0; i < count; i++) {
			MinigameShotEntity shot = new MinigameShotEntity(caster.level(), caster, target == null ? caster : target, damage);
			shot.setSpreadAngle(caster.getYRot() + angleOffset + (360F / count) * i);
			shot.setColor(style.colourFor(i));
			shot.setElement(style.element);
			shot.setVisualItem(style.visualItem);
			shot.setApplyPoison(style.applyPoison);
			shot.waterVisual = style.waterVisual;
			shot.setTrailStartTick(0);
			shot.setPos(caster.getX(), caster.getY() + caster.getBbHeight() * 0.6D, caster.getZ());
			if (target != null) {
				shot.setTarget(target.getId());
			}
			caster.level().addFreshEntity(shot);
		}
	}
}
