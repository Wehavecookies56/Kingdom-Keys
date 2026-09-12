package online.kingdomkeys.kingdomkeys.entity.mob.goal;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import online.kingdomkeys.kingdomkeys.entity.mob.ApprenticeEntity;

import java.util.EnumSet;

public class ApprenticeCombatGoal extends Goal {
	private static final double REACH = 3.2D;

	private static final double CHASE_SPEED = 1.15D;

	private static final int SWING_INTERVAL = 12;
	private static final int COMBO_HITS = 3;
	private static final int RECOVERY = 26;

	private static final int REPATH_INTERVAL = 5;

	private final ApprenticeEntity apprentice;

	private int cooldown;
	private int hitsLeft;
	private int repath;

	public ApprenticeCombatGoal(ApprenticeEntity apprentice) {
		this.apprentice = apprentice;
		setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
	}

	@Override
	public boolean canUse() {
		if (apprentice.isSparring()) {
			return false;
		}

		LivingEntity target = apprentice.getTarget();
		return target != null && target.isAlive();
	}

	@Override
	public boolean canContinueToUse() {
		return canUse();
	}

	@Override
	public boolean requiresUpdateEveryTick() {
		return true;
	}

	@Override
	public void start() {
		cooldown = 0;
		hitsLeft = COMBO_HITS;
		repath = 0;
	}

	@Override
	public void stop() {
		apprentice.getNavigation().stop();
		apprentice.setAggressive(false);
	}

	@Override
	public void tick() {
		LivingEntity target = apprentice.getTarget();

		if (target == null) {
			return;
		}

		apprentice.getLookControl().setLookAt(target, 30F, 30F);

		double distance = apprentice.distanceTo(target);

		// Walk them down rather than rooting to the spot, but stop short so the swing has room
		if (distance > REACH * 0.85D) {
			if (--repath <= 0) {
				repath = REPATH_INTERVAL;
				apprentice.getNavigation().moveTo(target, CHASE_SPEED);
			}
		} else {
			apprentice.getNavigation().stop();
		}

		if (cooldown > 0) {
			cooldown--;
			return;
		}

		if (distance > REACH || !apprentice.getSensing().hasLineOfSight(target)) {
			return;
		}

		apprentice.setAggressive(true);
		apprentice.swing(InteractionHand.MAIN_HAND);
		apprentice.doHurtTarget(target);

		// Three blows then a breather, so a fight reads as a fight and not a metronome
		if (--hitsLeft > 0) {
			cooldown = SWING_INTERVAL;
		} else {
			hitsLeft = COMBO_HITS;
			cooldown = RECOVERY;
		}
	}
}
