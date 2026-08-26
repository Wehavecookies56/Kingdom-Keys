package online.kingdomkeys.kingdomkeys.entity.shotlock;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ThrowableProjectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import online.kingdomkeys.kingdomkeys.damagesource.KKDamageTypes;
import online.kingdomkeys.kingdomkeys.entity.ModEntities;
import org.joml.Vector3f;
import net.minecraft.world.phys.Vec3;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class SonicBladeCoreEntity extends BaseShotlockCoreEntity {
	List<VolleyShotEntity> list = new ArrayList<>();
	private ResourceKey<DamageType> element = null; // null = original generic damage - set to a KKDamageTypes entry for elemental reskins (e.g. Absolute Zero)
	private Color particleColor = new Color(255,255,255);

	public void setElement(ResourceKey<DamageType> element) {
		this.element = element;
	}

	public void setParticleColor(Color color) {
		this.particleColor = color;
	}

	private DamageSource buildDamageSource(LivingEntity target) {
		if (element != null) {
			return KKDamageTypes.getElementalDamage(element, this, getOwner());
		}
		return target.damageSources().thrown(this, getOwner());
	}

	private final List<Entity> dashTargets = new ArrayList<>();
	private final java.util.Map<Integer, Integer> locksRemaining = new java.util.HashMap<>();

	private int dashTargetIndex = 0;
	private boolean targetsPrepared = false;

	private static final double DASH_SPEED = 2.1D;
	private static final double HIT_PADDING = 0.35D;
	private static final double CLEAR_PADDING = 0.70D;
	private int clearingTargetId = -1;
	private Vec3 clearVelocity = Vec3.ZERO;
	private int clearTicks = 0;
	private static final int MIN_CLEAR_TICKS = 1;

	private boolean gravityCaptured = false;
	private boolean previousNoGravity = false;
	
	@Override
	public boolean movesCaster() {
		return true; // this one dashes the player from target to target
	}

	public SonicBladeCoreEntity(EntityType<? extends ThrowableProjectile> type, Level world) {
		super(type, world);
		this.maxTicks = 260;
	}

	public SonicBladeCoreEntity(Level world, Player player, List<Entity> targets, float dmg) {
		super(ModEntities.TYPE_SHOTLOCK_SONIC_BLADE.get(), world, player, targets, dmg);
		this.maxTicks = 260;
	}

	@Override
	public void tick() {
		if(getCaster() == null) {
			this.remove(RemovalReason.KILLED);
			return;
		}

		getCaster().startAutoSpinAttack(
				0,
				0.0F,
				getCaster().getMainHandItem()
		);


		if (!level().isClientSide) {
			if (!tickSonicBladeMovement(getCaster())) {
				finishSonicBlade();
				return;
			}
		}
		
		if (this.tickCount > maxTicks) {
			this.remove(RemovalReason.KILLED);
		}
		
		if(tickCount > 1 && getOwner() != null && level() instanceof ServerLevel serverLevel) {
			double ex = getOwner().getX();
			double ey = getOwner().getY() + 1;
			double ez = getOwner().getZ();
			Color color = particleColor;

			// Base color cloud, now with actual spread/count instead of one lonely particle a tick.
			serverLevel.sendParticles(new DustParticleOptions(new Vector3f(color.getRed()/255F, color.getGreen()/255F, color.getBlue()/255F), 1.2F), ex, ey, ez, 5, 0.35, 0.5, 0.35, 0.02);

			// An actual orbiting ring around the caster - matches BBS's own description of these as
			// "dashing around surrounded by an elemental aura" instead of just a particle color swap.
			double angleBase = Math.toRadians(tickCount * 25D);
			for (int i = 0; i < 4; i++) {
				double angle = angleBase + i * (Math.PI / 2D);
				double ox = ex + Math.cos(angle) * 0.7D;
				double oz = ez + Math.sin(angle) * 0.7D;

				if (element != null && element.equals(KKDamageTypes.ICE)) {
					serverLevel.sendParticles(net.minecraft.core.particles.ParticleTypes.SNOWFLAKE, ox, ey, oz, 2, 0.05, 0.15, 0.05, 0.01);
				} else if (element != null && element.equals(KKDamageTypes.LIGHT)) {
					serverLevel.sendParticles(net.minecraft.core.particles.ParticleTypes.END_ROD, ox, ey, oz, 2, 0.05, 0.1, 0.05, 0.01);
				} else if (element != null && element.equals(KKDamageTypes.LIGHTNING)) {
					serverLevel.sendParticles(net.minecraft.core.particles.ParticleTypes.ELECTRIC_SPARK, ox, ey, oz, 2, 0.05, 0.15, 0.05, 0.02);
				} else if (element != null && element.equals(KKDamageTypes.DARKNESS)) {
					serverLevel.sendParticles(net.minecraft.core.particles.ParticleTypes.SQUID_INK, ox, ey, oz, 1, 0.05, 0.1, 0.05, 0.005);
					serverLevel.sendParticles(net.minecraft.core.particles.ParticleTypes.SMOKE, ox, ey, oz, 1, 0.05, 0.1, 0.05, 0.005);
				}
			}

			// A little extra punctuation on top of the ring, unique per element.
			if (element != null && element.equals(KKDamageTypes.ICE)) {
				serverLevel.sendParticles(net.minecraft.core.particles.ParticleTypes.ITEM_SNOWBALL, ex, ey, ez, 3, 0.3, 0.3, 0.3, 0.02);
			} else if (element != null && element.equals(KKDamageTypes.LIGHT)) {
				serverLevel.sendParticles(net.minecraft.core.particles.ParticleTypes.FLASH, ex, ey, ez, 1, 0, 0, 0, 0);
			} else if (element != null && element.equals(KKDamageTypes.LIGHTNING)) {
				serverLevel.sendParticles(net.minecraft.core.particles.ParticleTypes.CRIT, ex, ey, ez, 4, 0.3, 0.4, 0.3, 0.05);
			}
		}

		super.tick();
	}

	private void keepCasterSpinning(Player caster) {
		caster.startAutoSpinAttack(
				0,
				0F,
				caster.getMainHandItem()
		);
	}

	private void prepareDashTargets() {
		if (targetsPrepared) {
			return;
		}

		targetsPrepared = true;

		for (Entity target : getTargets()) {
			if (target == null) {
				continue;
			}

			int id = target.getId();

			locksRemaining.put(
					id,
					locksRemaining.getOrDefault(id, 0) + 1
			);

			if (!dashTargets.contains(target)) {
				dashTargets.add(target);
			}
		}
	}

	private boolean tickSonicBladeMovement(Player caster) {

		prepareDashTargets();

		if (!gravityCaptured) {
			previousNoGravity = caster.isNoGravity();
			gravityCaptured = true;
		}

		caster.setNoGravity(true);
		caster.setOnGround(false);
		caster.resetFallDistance();

		if (clearingTargetId != -1) {

			Entity previousTarget = level().getEntity(clearingTargetId);

			if (previousTarget == null
					|| previousTarget.isRemoved()
					|| !previousTarget.isAlive()) {

				// Completely through the enemy.
				clearingTargetId = -1;
				clearVelocity = Vec3.ZERO;
				clearTicks = 0;

				// We already advanced the target index when the previous enemy was hit.
				// Now physically face the NEXT enemy before dashing toward it.
				Entity nextTarget = getNextDashTarget();

				if (nextTarget != null) {
					rotateCasterToward(caster, nextTarget);
				}

			} else {

				AABB clearBox =
						previousTarget.getBoundingBox().inflate(CLEAR_PADDING);

				boolean stillInside =
						caster.getBoundingBox().intersects(clearBox);

				/*
				 * Force at least one full movement tick THROUGH the target.
				 *
				 * After that, keep going until we're genuinely outside its box.
				 */
				if (clearTicks > 0 || stillInside) {

					caster.setDeltaMovement(clearVelocity);
					caster.hurtMarked = true;
					caster.resetFallDistance();

					if (clearTicks > 0) {
						clearTicks--;
					}

					return true;
				}

				// Completely through the enemy.
				clearingTargetId = -1;
				clearVelocity = Vec3.ZERO;
				clearTicks = 0;

				// We already advanced the target index when the previous enemy was hit.
				// Now physically face the NEXT enemy before dashing toward it.
				Entity nextTarget = getNextDashTarget();

				if (nextTarget != null) {
					rotateCasterToward(caster, nextTarget);
				}
			}
		}

		Entity target = getNextDashTarget();

		if (target == null) {
			return false;
		}

		/*
		 * Aim at the BODY of the target rather than its BlockPos.
		 *
		 * Slightly above geometric center generally looks better for
		 * humanoid mobs and avoids the ground-seeking behavior.
		 */
		Vec3 casterCenter = caster.getBoundingBox().getCenter();

		Vec3 targetPoint = new Vec3(
				target.getX(),
				target.getY() + target.getBbHeight() * 0.55D,
				target.getZ()
		);

		Vec3 difference = targetPoint.subtract(casterCenter);

		double distance = difference.length();

		if (distance < 0.0001D) {
			difference = caster.getLookAngle();
			distance = difference.length();
		}

		Vec3 direction = difference.normalize();

		/*
		 * Fixed high-speed dash.
		 */
		Vec3 velocity = direction.scale(
				Math.min(DASH_SPEED, Math.max(distance, 0.35D))
		);

		/*
		 * SWEPT COLLISION.
		 *
		 * This fixes another problem visible in your clip:
		 * at 2.8 blocks/tick, we can go from one side of a mob to
		 * the other without ever having overlapping hitboxes on
		 * an individual tick.
		 */
		AABB sweptBox = caster.getBoundingBox()
				.expandTowards(velocity)
				.inflate(HIT_PADDING);

		boolean willHit =
				sweptBox.intersects(target.getBoundingBox());

		if (willHit) {

			boolean damaged = false;

			if (target instanceof LivingEntity enemy) {

				// Each physical Sonic Blade pass should be allowed
				// to register as its own hit.
				enemy.invulnerableTime = 0;

				damaged = enemy.hurt(
						buildDamageSource(enemy),
						dmg
				);

				// Only apply on-hit effects if the hit actually succeeded.
				if (damaged) {

					if (element != null
							&& element.equals(KKDamageTypes.ICE)) {

						enemy.addEffect(
								new MobEffectInstance(
										online.kingdomkeys.kingdomkeys.effects.ModMobEffects.FREEZE,
										40,
										50,
										false,
										true,
										true
								)
						);
					}

					// Consume EXACTLY ONE lock from THIS target.
					int remaining = locksRemaining.getOrDefault(target.getId(), 0);

					locksRemaining.put(target.getId(), Math.max(0, remaining - 1));

					// Only move to the next target when this hit
					// successfully counted.
					advanceDashTarget();
				}
			}

			/*
			 * ALWAYS continue through the target.
			 *
			 * Even if hurt() returned false, we do not want to sit
			 * inside/on top of the enemy. We pass through, clear it,
			 * then retry the same lock afterward.
			 */
			Vec3 exitVelocity = direction.scale(DASH_SPEED);

			caster.setDeltaMovement(exitVelocity);
			caster.hurtMarked = true;
			caster.resetFallDistance();

			clearingTargetId = target.getId();
			clearVelocity = exitVelocity;
			clearTicks = MIN_CLEAR_TICKS;

			/*
			 * If the hit succeeded, dashTargetIndex has already advanced.
			 * We DON'T need to change movement toward the next enemy yet.
			 * The clearing phase should finish the current pass first.
			 * Rotation toward the next enemy should happen when clearing
			 * finishes.
			 */
			return true;
		}

		caster.setDeltaMovement(velocity);
		caster.hurtMarked = true;
		caster.resetFallDistance();

		if (caster.getVehicle() != null) {
			caster.getVehicle().onPassengerTurned(caster);
		}

		return true;
	}

	private Entity getNextDashTarget() {
		prepareDashTargets();

		if (dashTargets.isEmpty()) {
			return null;
		}

		int checked = 0;

		while (checked < dashTargets.size()) {

			if (dashTargetIndex >= dashTargets.size()) {
				dashTargetIndex = 0;
			}

			Entity target = dashTargets.get(dashTargetIndex);

			if (target != null
					&& target.isAlive()
					&& !target.isRemoved()
					&& locksRemaining.getOrDefault(target.getId(), 0) > 0) {
				return target;
			}

			dashTargetIndex++;
			checked++;
		}

		// Nobody has locks left.
		return null;
	}

	private void advanceDashTarget() {
		if (dashTargets.isEmpty()) {
			return;
		}

		dashTargetIndex++;

		if (dashTargetIndex >= dashTargets.size()) {
			dashTargetIndex = 0;
		}
	}

	private void rotateCasterToward(Player caster, Entity target) {
		if (target == null) {
			return;
		}

		Vec3 from = caster.getEyePosition();

		Vec3 to = new Vec3(
				target.getX(),
				target.getY() + target.getBbHeight() * 0.55D,
				target.getZ()
		);

		Vec3 delta = to.subtract(from);

		double horizontal = Math.sqrt(
				delta.x * delta.x +
						delta.z * delta.z
		);

		float yaw = (float)(
				Math.toDegrees(Math.atan2(delta.z, delta.x)) - 90.0D
		);

		float pitch = (float)(
				-Math.toDegrees(Math.atan2(delta.y, horizontal))
		);

		// Server-side entity rotation
		caster.setYRot(yaw);
		caster.setXRot(pitch);

		// Make the BODY and HEAD follow it too.
		caster.setYBodyRot(yaw);
		caster.setYHeadRot(yaw);

		// Prevent interpolation from visually dragging from the old facing.
		caster.yBodyRotO = yaw;
		caster.yHeadRotO = yaw;

		/*
		 * IMPORTANT:
		 * ServerPlayer rotation has to be sent back to its own client.
		 *
		 * Same XYZ, different yaw/pitch.
		 */
		if (caster instanceof ServerPlayer serverPlayer) {
			serverPlayer.connection.teleport(
					serverPlayer.getX(),
					serverPlayer.getY(),
					serverPlayer.getZ(),
					yaw,
					pitch
			);
		}
	}

	private boolean cleanedUp = false;

	private void cleanupCaster() {
		if (cleanedUp) {
			return;
		}

		cleanedUp = true;

		Player caster = getCaster();

		if (caster != null && !level().isClientSide) {

			if (gravityCaptured) {
				caster.setNoGravity(previousNoGravity);
			}

			caster.resetFallDistance();
			caster.setDeltaMovement(Vec3.ZERO);
			caster.hurtMarked = true;

			// Allow vanilla to clear our forced spin flag.
			caster.startAutoSpinAttack(
					1,
					0.0F,
					caster.getMainHandItem()
			);
		}

		gravityCaptured = false;
	}

	private void finishSonicBlade() {
		cleanupCaster();
		this.remove(RemovalReason.KILLED);
	}

	private void restoreCasterMovement() {
		Player caster = getCaster();

		if (caster != null && !level().isClientSide && gravityCaptured) {
			caster.setNoGravity(previousNoGravity);
			caster.resetFallDistance();

			// Kill leftover Sonic Blade momentum.
			caster.setDeltaMovement(Vec3.ZERO);
			caster.hurtMarked = true;
		}

		gravityCaptured = false;
	}

	@Override
	public void remove(RemovalReason reason) {
		cleanupCaster();
		restoreCasterMovement();
		super.remove(reason);
	}

	
	public int getMaxTicks() {
		return maxTicks;
	}

	public void setMaxTicks(int maxTicks) {
		this.maxTicks = maxTicks;
	}
	
	private Entity getTarget() {
		if(getTargets().size() <= getActualTargetIndex())
			return null;
		return getTargets().get(getActualTargetIndex());
	}

	@Override
	public void addAdditionalSaveData(CompoundTag compound) {
		super.addAdditionalSaveData(compound);
		compound.putInt("ActualTargetIndex", this.entityData.get(ACTUAL_TARGET_INDEX));
	}

	@Override
	public void readAdditionalSaveData(CompoundTag compound) {
		super.readAdditionalSaveData(compound);
		this.entityData.set(ACTUAL_TARGET_INDEX, compound.getInt("ActualTargetIndex"));
	}

	/** Which of the locked-on targets the blade is dashing at right now - only this core tracks one. */
	private static final EntityDataAccessor<Integer> ACTUAL_TARGET_INDEX = SynchedEntityData.defineId(SonicBladeCoreEntity.class, EntityDataSerializers.INT);

	public int getActualTargetIndex() {
		return this.getEntityData().get(ACTUAL_TARGET_INDEX);
	}

	public void setActualTargetIndex(int actual) {
		this.entityData.set(ACTUAL_TARGET_INDEX, actual);
	}

	@Override
	protected void defineSynchedData(SynchedEntityData.Builder pBuilder) {
		super.defineSynchedData(pBuilder);
		pBuilder.define(ACTUAL_TARGET_INDEX, 0);
	}
}
