package online.kingdomkeys.kingdomkeys.entity.mob.goal;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.target.TargetGoal;
import net.minecraft.world.entity.projectile.ThrowableProjectile;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import online.kingdomkeys.kingdomkeys.entity.magic.BlizzardEntity;
import online.kingdomkeys.kingdomkeys.entity.magic.FireEntity;
import online.kingdomkeys.kingdomkeys.entity.mob.BaseKHEntity;

import java.util.List;

public class DefenderGoal extends TargetGoal {
	// 0-normal, 1-guarding, 2-bashing, 3-casting
	public static final int STATE_IDLE = 0, STATE_GUARD = 1, STATE_BASH = 2, STATE_CAST = 3;

	private static final int DECIDE_COOLDOWN = 90;
	private static final int GUARD_TICKS = 80;
	private static final int MAX_BASH_TICKS = 24;
	private static final double BASH_RANGE = 5;
	private static final double BASH_SPEED = 0.6;
	private static final double BASH_LIFT = 0.25;
	private static final double BASH_HIT_RADIUS = 1.8;

	// How long the shield's mouth glows before it spits, and how long the whole cast lasts.
	private static final int CAST_WINDUP = 24;
	private static final int CAST_TICKS = 44;
	private static final double CAST_RANGE = 20;
	// Chance of casting rather than turtling, once it has decided to hang back.
	private static final float CAST_CHANCE = 0.6F;
	// The Defender's shield breathes both, so it picks one per cast.
	private static final float FIRE_CHANCE = 0.5F;

	private final BaseKHEntity mob;

	private int moveTicks;
	private int ticksToChooseAI = 20;
	private boolean castingFire;
	private boolean shotFired;

	public DefenderGoal(PathfinderMob creature) {
		super(creature, true);
		this.mob = (BaseKHEntity) creature;
	}

	@Override
	public boolean canUse() {
		return this.mob.getTarget() != null;
	}

	@Override
	public boolean canContinueToUse() {
		LivingEntity target = mob.getTarget();
		if (target == null) {
			mob.setState(STATE_IDLE);
			return false;
		}

		switch (mob.getState()) {
			case STATE_BASH -> bashAI();
			case STATE_GUARD -> guardAI();
			case STATE_CAST -> castAI(target);
			default -> {
				if (ticksToChooseAI <= 0) {
					// Close enough to bash with the shield
					if (mob.distanceTo(target) < BASH_RANGE) {
						startBash(target);
					} else if (mob.distanceTo(target) < CAST_RANGE && mob.getRandom().nextFloat() < CAST_CHANCE) {
						startCast();
					} else {
						startGuard();
					}
					ticksToChooseAI = DECIDE_COOLDOWN;
				} else {
					// Counted in twos because the goal selector only polls every other tick.
					ticksToChooseAI -= 2;
				}
			}
		}
		return true;
	}

	@Override
	public void start() {
		mob.setState(STATE_IDLE);
	}

	private void startGuard() {
		moveTicks = 0;
		mob.setState(STATE_GUARD);
	}

	// Holds the shield up. The damage reduction that goes with it lives on the entity, alongside the rest of its hurt handling.
	private void guardAI() {
		moveTicks += 2;
		if (moveTicks >= GUARD_TICKS) {
			mob.setState(STATE_IDLE);
			moveTicks = 0;
		}
	}

	private void startCast() {
		moveTicks = 0;
		shotFired = false;
		castingFire = mob.getRandom().nextFloat() < FIRE_CHANCE;
		mob.setState(STATE_CAST);
	}

	// Plants itself, the head on the shield charges up, then it spits a single magic. Standing still for the wind-up is what telegraphs it
	private void castAI(LivingEntity target) {
		moveTicks += 2;

		mob.getNavigation().stop();
		mob.getLookControl().setLookAt(target, 30F, 30F);
		mob.setDeltaMovement(mob.getDeltaMovement().x * 0.5, mob.getDeltaMovement().y, mob.getDeltaMovement().z * 0.5);

		if (mob.level() instanceof ServerLevel level) {
			Vec3 mouth = shieldMouth();
			level.sendParticles(castingFire ? ParticleTypes.FLAME : ParticleTypes.SNOWFLAKE, mouth.x, mouth.y, mouth.z, 2, 0.1, 0.1, 0.1, 0.01);
		}

		if (!shotFired && moveTicks >= CAST_WINDUP) {
			shotFired = true;
			spit(target);
		}

		if (moveTicks >= CAST_TICKS) {
			mob.setState(STATE_IDLE);
			moveTicks = 0;
		}
	}

	// Roughly where the shield sits: out in front of it, at chest height.
	private Vec3 shieldMouth() {
		Vec3 forward = Vec3.directionFromRotation(0, mob.getYRot()).scale(0.9);
		return mob.position().add(forward.x, mob.getBbHeight() * 0.6, forward.z);
	}

	private void spit(LivingEntity target) {
		float damage = (float) mob.getAttribute(Attributes.ATTACK_DAMAGE).getBaseValue();

		double dx = target.getX() - mob.getX();
		double dy = target.getBoundingBox().minY + target.getBbHeight() / 2.0 - (mob.getY() + mob.getBbHeight() / 2.0);
		double dz = target.getZ() - mob.getZ();

		ThrowableProjectile shot = castingFire ? new FireEntity(mob.level(), mob, damage, null) : new BlizzardEntity(mob.level(), mob, damage, 100);

		shot.shoot(dx, dy, dz, 1, 0);

		Vec3 mouth = shieldMouth();
		shot.setPos(mouth.x, mouth.y, mouth.z);
		mob.level().addFreshEntity(shot);
	}

	private void startBash(LivingEntity target) {
		moveTicks = 0;
		mob.setState(STATE_BASH);

		Vec3 direction = target.position().subtract(mob.position()).normalize();
		mob.setDeltaMovement(direction.x * BASH_SPEED, BASH_LIFT, direction.z * BASH_SPEED);
		mob.hasImpulse = true;
	}

	private void bashAI() {
		moveTicks += 2;

		AABB reach = mob.getBoundingBox().inflate(BASH_HIT_RADIUS);
		List<LivingEntity> hit = mob.level().getEntitiesOfClass(LivingEntity.class, reach);
		hit.remove(mob);

		for (LivingEntity victim : hit) {
			mob.doHurtTarget(victim);
		}

		if (moveTicks >= MAX_BASH_TICKS || (moveTicks > 6 && mob.onGround())) {
			mob.setState(STATE_IDLE);
			moveTicks = 0;
		}
	}
}
