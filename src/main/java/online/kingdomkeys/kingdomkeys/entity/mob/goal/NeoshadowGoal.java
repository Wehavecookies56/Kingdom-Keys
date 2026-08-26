package online.kingdomkeys.kingdomkeys.entity.mob.goal;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.target.TargetGoal;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import online.kingdomkeys.kingdomkeys.entity.mob.BaseKHEntity;

import java.util.List;

public class NeoshadowGoal extends TargetGoal {
	// 0-normal, 1-pouncing, 2-submerged
	public static final int STATE_IDLE = 0, STATE_POUNCE = 1, STATE_SUBMERGED = 2;

	private static final int MAX_POUNCE_TICKS = 24;
	private static final int POUNCE_COOLDOWN = 100;
	private static final double POUNCE_RANGE = 8;
	private static final double POUNCE_SPEED = 0.7;
	private static final double POUNCE_LIFT = 0.5;
	private static final double POUNCE_HIT_RADIUS = 1.4;

	private static final int TIME_ABOVE_GROUND = 140;
	private static final int TIME_SUBMERGED = 70;

	private final BaseKHEntity mob;

	private int pounceTicks;
	private int ticksToChooseAI = 20;
	private int shadowTicks = TIME_ABOVE_GROUND;
	private double originalAttackDamage;

	public NeoshadowGoal(PathfinderMob creature) {
		super(creature, true);
		this.mob = (BaseKHEntity) creature;
		this.originalAttackDamage = creature.getAttribute(Attributes.ATTACK_DAMAGE).getBaseValue();
	}

	@Override
	public boolean canUse() {
		return this.mob.getTarget() != null;
	}

	@Override
	public boolean canContinueToUse() {
		LivingEntity target = mob.getTarget();
		if (target == null) {
			surface();
			mob.setState(STATE_IDLE);
			return false;
		}

		if (isSubmerged()) {
			submergedAI();
		} else if (isPouncing()) {
			pounceAI();
		} else if (ticksToChooseAI <= 0 && mob.distanceTo(target) < POUNCE_RANGE) {
			startPounce(target);
			ticksToChooseAI = POUNCE_COOLDOWN;
		} else {
			ticksToChooseAI -= 2;

			// Only sinks from a standing start, never mid-leap.
			if (mob.onGround()) {
				shadowTicks -= 2;
				if (shadowTicks <= 0) {
					submerge();
				}
			}
		}
		return true;
	}

	@Override
	public void start() {
		surface();
		mob.setState(STATE_IDLE);
	}

	public boolean isSubmerged() {
		return mob.getState() == STATE_SUBMERGED;
	}

	private void submerge() {
		shadowTicks = 0;
		mob.setState(STATE_SUBMERGED);
		mob.setInvulnerable(true);
		originalAttackDamage = mob.getAttribute(Attributes.ATTACK_DAMAGE).getBaseValue();
		mob.getAttribute(Attributes.ATTACK_DAMAGE).setBaseValue(0.0D);
	}

	private void submergedAI() {
		shadowTicks += 2;
		if (shadowTicks >= TIME_SUBMERGED) {
			surface();
			mob.setState(STATE_IDLE);
		}
	}

	// Called from the entity too, when water drags it back out early.
	public void surface() {
		shadowTicks = TIME_ABOVE_GROUND;
		mob.setInvulnerable(false);
		if (originalAttackDamage > 0) {
			mob.getAttribute(Attributes.ATTACK_DAMAGE).setBaseValue(originalAttackDamage);
		}
	}

	private boolean isPouncing() {
		return mob.getState() == STATE_POUNCE;
	}

	private void startPounce(LivingEntity target) {
		pounceTicks = 0;
		mob.setState(STATE_POUNCE);

		Vec3 direction = target.position().subtract(mob.position()).normalize();
		mob.setDeltaMovement(direction.x * POUNCE_SPEED, POUNCE_LIFT, direction.z * POUNCE_SPEED);
		mob.hasImpulse = true;
	}

	private void pounceAI() {
		pounceTicks += 2;

		AABB reach = mob.getBoundingBox().inflate(POUNCE_HIT_RADIUS);
		List<LivingEntity> hit = mob.level().getEntitiesOfClass(LivingEntity.class, reach);
		hit.remove(mob);

		for (LivingEntity victim : hit) {
			mob.doHurtTarget(victim);
		}

		if (pounceTicks >= MAX_POUNCE_TICKS || (pounceTicks > 6 && mob.onGround())) {
			mob.setState(STATE_IDLE);
			pounceTicks = 0;
		}
	}
}
