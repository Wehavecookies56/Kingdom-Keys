package online.kingdomkeys.kingdomkeys.entity.mob.goal;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.target.TargetGoal;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import online.kingdomkeys.kingdomkeys.entity.mob.BaseKHEntity;

import java.util.List;

public class AirSoldierGoal extends TargetGoal {
	// 0-normal, 1-climbing, 2-diving
	public static final int STATE_IDLE = 0, STATE_CLIMB = 1, STATE_DIVE = 2;

	private static final int DIVE_COOLDOWN = 120;
	private static final double DIVE_RANGE = 14;

	private static final double APEX_HEIGHT = 9;
	private static final int MAX_CLIMB_TICKS = 50;
	private static final double CLIMB_SPEED = 0.4;
	// Drift over the target while climbing, so the dive starts from above them rather than beside.
	private static final double CLIMB_DRIFT = 0.12;

	private static final int MAX_DIVE_TICKS = 30;
	private static final double DIVE_SPEED = 1.0;
	private static final double DIVE_DROP = 0.9;
	private static final double DIVE_HIT_RADIUS = 1.6;

	private final BaseKHEntity mob;

	private int stateTicks;
	private int ticksToChooseAI = 20;

	public AirSoldierGoal(PathfinderMob creature) {
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
			case STATE_CLIMB -> climbAI(target);
			case STATE_DIVE -> diveAI();
			default -> {
				if (ticksToChooseAI <= 0 && mob.distanceTo(target) < DIVE_RANGE) {
					startClimb();
					ticksToChooseAI = DIVE_COOLDOWN;
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

	private void startClimb() {
		stateTicks = 0;
		mob.setState(STATE_CLIMB);
	}

	// Straight up, drifting over the target as it goes.
	private void climbAI(LivingEntity target) {
		stateTicks += 2;

		mob.getNavigation().stop();
		mob.setNoGravity(true);
		mob.getLookControl().setLookAt(target, 30F, 30F);

		Vec3 toTarget = new Vec3(target.getX() - mob.getX(), 0, target.getZ() - mob.getZ());
		Vec3 drift = toTarget.lengthSqr() > 1.0E-4 ? toTarget.normalize().scale(CLIMB_DRIFT) : Vec3.ZERO;
		mob.setDeltaMovement(drift.x, CLIMB_SPEED, drift.z);

		if (mob.getY() >= target.getY() + APEX_HEIGHT || stateTicks >= MAX_CLIMB_TICKS) {
			startDive(target);
		}
	}

	private void startDive(LivingEntity target) {
		stateTicks = 0;
		mob.setState(STATE_DIVE);

		// Aimed where the target is at the moment it commits, which is what makes the dive dodgeable.
		Vec3 direction = target.position().subtract(mob.position()).normalize();
		mob.setNoGravity(false);
		mob.setDeltaMovement(direction.x * DIVE_SPEED, -DIVE_DROP, direction.z * DIVE_SPEED);
		mob.hasImpulse = true;
	}

	private void diveAI() {
		stateTicks += 2;

		AABB reach = mob.getBoundingBox().inflate(DIVE_HIT_RADIUS);
		List<LivingEntity> hit = mob.level().getEntitiesOfClass(LivingEntity.class, reach);
		hit.remove(mob);

		for (LivingEntity victim : hit) {
			mob.doHurtTarget(victim);
		}

		if (stateTicks >= MAX_DIVE_TICKS || (stateTicks > 6 && mob.onGround())) {
			mob.setState(STATE_IDLE);
			stateTicks = 0;
		}
	}
}
