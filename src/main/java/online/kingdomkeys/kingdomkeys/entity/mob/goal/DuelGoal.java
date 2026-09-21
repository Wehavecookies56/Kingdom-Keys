package online.kingdomkeys.kingdomkeys.entity.mob.goal;

import net.minecraft.commands.arguments.EntityAnchorArgument;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.util.DefaultRandomPos;
import net.minecraft.world.phys.Vec3;
import online.kingdomkeys.kingdomkeys.entity.mob.Dueller;
import online.kingdomkeys.kingdomkeys.lib.KKSupplier;
import online.kingdomkeys.kingdomkeys.magic.Magic;
import online.kingdomkeys.kingdomkeys.magic.ModMagic;

import java.util.EnumSet;

public class DuelGoal<T extends PathfinderMob & Dueller> extends Goal {
	private static final int IDLE = 0, COMBO = 1, LUNGE = 2, CAST = 3;

	private static final double MELEE_REACH = 3.2D;
	private static final double LUNGE_FROM = 4.5D, LUNGE_TO = 14.0D;
	private static final double CAST_FROM = 6.0D;

	private static final int SWING_INTERVAL_BASE = 14, SWING_INTERVAL_MIN = 7;
	private static final int SWING_INTERVAL_PER_LEVEL = 12;

	private static final int COMBO_HITS_BASE = 3, COMBO_HITS_MAX = 7;
	private static final int COMBO_HITS_PER_LEVEL = 20;

	private static final int RECOVERY_BASE = 45, RECOVERY_MIN = 10;
	private static final int RECOVERY_PER_LEVEL = 2;

	private static final int LUNGE_TICKS = 14;
	private static final double LUNGE_SPEED = 1.15D, LUNGE_LIFT = 0.22D;

	private static final int CAST_TELEGRAPH = 16;

	private static final int REPATH_INTERVAL = 5;

	/** For whoever turns up to a duel without a keyblade yet: an arm is shorter and slower than one */
	private static final double FIST_REACH = 1.9D;
	private static final int FIST_INTERVAL = 20;
	private static final double FLEE_SPEED = 1.25D;
	private static final int FLEE_RADIUS = 10, FLEE_HEIGHT = 4;

	private final T fighter;

	private int state = IDLE;
	private int stateTicks;
	private int recovery;
	private int hitsLeft;
	private boolean lungeConnected;
	private int repath;

	public DuelGoal(T fighter) {
		this.fighter = fighter;
		setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
	}

	@Override
	public boolean canUse() {
		LivingEntity target = fighter.getTarget();
		return fighter.isDuelling() && target != null && target.isAlive() && !fighter.isDeadOrDying();
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
		state = IDLE;
		stateTicks = 0;
		recovery = 0;

		fighter.startCallingKeyblade();
	}

	@Override
	public void stop() {
		state = IDLE;
		fighter.getNavigation().stop();
	}

	@Override
	public void tick() {
		LivingEntity target = fighter.getTarget();
		if (target == null) {
			return;
		}

		fighter.getLookControl().setLookAt(target, 30F, 30F);

		if (!fighter.hasKeyblade()) {
			if (fighter.holdsGroundUnarmed()) {
				punch(target);
			} else {
				backAway(target);
			}
			return;
		}

		stateTicks++;

		switch (state) {
			case COMBO -> tickCombo(target);
			case LUNGE -> tickLunge(target);
			case CAST -> tickCast(target);
			default -> tickIdle(target);
		}
	}

	private void tickIdle(LivingEntity target) {
		chase(target, 1.0D);

		if (recovery > 0) {
			recovery--;
			return;
		}

		double distance = fighter.distanceTo(target);

		if (distance <= MELEE_REACH) {
			enter(COMBO);
			hitsLeft = comboHits();
			return;
		}

		if (canCast() && distance >= CAST_FROM) {
			enter(CAST);
			return;
		}

		if (distance >= LUNGE_FROM && distance <= LUNGE_TO && fighter.hasLineOfSight(target)) {
			enter(LUNGE);
			lungeConnected = false;
			fighter.getNavigation().stop();
			Vec3 at = target.position().subtract(fighter.position()).normalize();
			fighter.setDeltaMovement(at.x * LUNGE_SPEED, LUNGE_LIFT, at.z * LUNGE_SPEED);
			fighter.hasImpulse = true;
		}
	}

	private void tickCombo(LivingEntity target) {
		int interval = swingInterval();

		// Walks the target down between blows rather than rooting himself to the spot
		if (fighter.distanceTo(target) > MELEE_REACH * 0.8D) {
			chase(target, 1.1D);
		}

		if (stateTicks % interval != 0) {
			return;
		}

		fighter.swing(InteractionHand.MAIN_HAND);
		if (fighter.distanceTo(target) <= MELEE_REACH) {
			fighter.doHurtTarget(target);
		}

		if (--hitsLeft <= 0) {
			rest();
		}
	}

	private void tickLunge(LivingEntity target) {
		if (!lungeConnected && fighter.distanceTo(target) <= MELEE_REACH) {
			fighter.swing(InteractionHand.MAIN_HAND);
			fighter.doHurtTarget(target);
			lungeConnected = true;

			// Landing a lunge flows straight into a combo, which is where the pressure comes from
			enter(COMBO);
			hitsLeft = comboHits();
			return;
		}

		if (stateTicks >= LUNGE_TICKS) {
			rest();
		}
	}

	private void tickCast(LivingEntity target) {
		fighter.getNavigation().stop();

		if (stateTicks < CAST_TELEGRAPH) {
			if (fighter.level() instanceof ServerLevel level) {
				level.sendParticles(ParticleTypes.END_ROD, fighter.getX(), fighter.getY() + fighter.getBbHeight() * 0.8D, fighter.getZ(), 3, 0.25D, 0.25D, 0.25D, 0.01D);
			}
			return;
		}

		throwSpell(target);
		rest();
	}

	private void throwSpell(LivingEntity target) {
		// He aims by facing, so the look has to be on the target before the spell leaves him
		fighter.lookAt(EntityAnchorArgument.Anchor.EYES, target.getEyePosition());
		fighter.setYBodyRot(fighter.getYRot());
		fighter.setYHeadRot(fighter.getYRot());

		spellFor(fighter.getDuelLevel(), fighter.getRandom().nextBoolean()).get().castFromMob(fighter, target);
	}

	/** Use stronger magic spells */
	private KKSupplier<Magic> spellFor(int level, boolean fire) {
		if (level >= Dueller.THIRD_GRADE_FROM) {
			return fire ? ModMagic.FIRAGA : ModMagic.THUNDAGA;
		}

		if (level >= Dueller.SECOND_GRADE_FROM) {
			return fire ? ModMagic.FIRA : ModMagic.THUNDARA;
		}

		return fire ? ModMagic.FIRE : ModMagic.THUNDER;
	}

	private void chase(LivingEntity target, double speed) {
		if (--repath > 0) {
			return;
		}
		repath = REPATH_INTERVAL;
		fighter.getNavigation().moveTo(target, speed);
	}

	private void punch(LivingEntity target) {
		chase(target, 1.0D);

		if (recovery > 0) {
			recovery--;
			return;
		}

		if (fighter.distanceTo(target) > FIST_REACH || !fighter.getSensing().hasLineOfSight(target)) {
			return;
		}

		fighter.swing(InteractionHand.MAIN_HAND);
		fighter.doHurtTarget(target);
		recovery = FIST_INTERVAL;
	}

	private void backAway(LivingEntity target) {
		if (--repath > 0) {
			return;
		}

		repath = REPATH_INTERVAL;

		Vec3 away = DefaultRandomPos.getPosAway(fighter, FLEE_RADIUS, FLEE_HEIGHT, target.position());
		if (away != null) {
			fighter.getNavigation().moveTo(away.x, away.y, away.z, FLEE_SPEED);
		}
	}

	private void enter(int next) {
		state = next;
		stateTicks = 0;
	}

	private void rest() {
		enter(IDLE);
		recovery = recovery();
	}

	private boolean canCast() {
		return fighter.getDuelLevel() >= Dueller.MAGIC_FROM;
	}

	private int comboHits() {
		return Math.min(COMBO_HITS_MAX, COMBO_HITS_BASE + fighter.getDuelLevel() / COMBO_HITS_PER_LEVEL);
	}

	private int swingInterval() {
		return Math.max(SWING_INTERVAL_MIN, SWING_INTERVAL_BASE - fighter.getDuelLevel() / SWING_INTERVAL_PER_LEVEL);
	}

	private int recovery() {
		int base = Math.max(RECOVERY_MIN, RECOVERY_BASE - fighter.getDuelLevel() / RECOVERY_PER_LEVEL);
		return base + Mth.nextInt(fighter.getRandom(), 0, Math.max(1, base / 4));
	}
}
