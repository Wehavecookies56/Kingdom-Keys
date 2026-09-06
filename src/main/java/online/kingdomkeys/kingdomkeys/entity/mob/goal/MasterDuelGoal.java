package online.kingdomkeys.kingdomkeys.entity.mob.goal;

import net.minecraft.commands.arguments.EntityAnchorArgument;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.phys.Vec3;
import online.kingdomkeys.kingdomkeys.entity.mob.MasterDuelEntity;
import online.kingdomkeys.kingdomkeys.lib.KKSupplier;
import online.kingdomkeys.kingdomkeys.magic.Magic;
import online.kingdomkeys.kingdomkeys.magic.ModMagic;

import java.util.EnumSet;

public class MasterDuelGoal extends Goal {
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

	private final MasterDuelEntity master;

	private int state = IDLE;
	private int stateTicks;
	private int recovery;
	private int hitsLeft;
	private boolean lungeConnected;
	private int repath;

	public MasterDuelGoal(MasterDuelEntity master) {
		this.master = master;
		setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
	}

	@Override
	public boolean canUse() {
		LivingEntity target = master.getTarget();
		return !master.isSettled() && target != null && target.isAlive() && !master.isDeadOrDying();
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
	}

	@Override
	public void stop() {
		state = IDLE;
		master.getNavigation().stop();
	}

	@Override
	public void tick() {
		LivingEntity target = master.getTarget();
		if (target == null) {
			return;
		}

		master.getLookControl().setLookAt(target, 30F, 30F);
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

		double distance = master.distanceTo(target);

		if (distance <= MELEE_REACH) {
			enter(COMBO);
			hitsLeft = comboHits();
			return;
		}

		if (canCast() && distance >= CAST_FROM) {
			enter(CAST);
			return;
		}

		if (distance >= LUNGE_FROM && distance <= LUNGE_TO && master.hasLineOfSight(target)) {
			enter(LUNGE);
			lungeConnected = false;
			master.getNavigation().stop();
			Vec3 at = target.position().subtract(master.position()).normalize();
			master.setDeltaMovement(at.x * LUNGE_SPEED, LUNGE_LIFT, at.z * LUNGE_SPEED);
			master.hasImpulse = true;
		}
	}

	private void tickCombo(LivingEntity target) {
		int interval = swingInterval();

		// Walks the target down between blows rather than rooting himself to the spot
		if (master.distanceTo(target) > MELEE_REACH * 0.8D) {
			chase(target, 1.1D);
		}

		if (stateTicks % interval != 0) {
			return;
		}

		master.swing(InteractionHand.MAIN_HAND);
		if (master.distanceTo(target) <= MELEE_REACH) {
			master.doHurtTarget(target);
		}

		if (--hitsLeft <= 0) {
			rest();
		}
	}

	private void tickLunge(LivingEntity target) {
		if (!lungeConnected && master.distanceTo(target) <= MELEE_REACH) {
			master.swing(InteractionHand.MAIN_HAND);
			master.doHurtTarget(target);
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
		master.getNavigation().stop();

		if (stateTicks < CAST_TELEGRAPH) {
			if (master.level() instanceof ServerLevel level) {
				level.sendParticles(ParticleTypes.END_ROD, master.getX(), master.getY() + master.getBbHeight() * 0.8D, master.getZ(), 3, 0.25D, 0.25D, 0.25D, 0.01D);
			}
			return;
		}

		throwSpell(target);
		rest();
	}

	private void throwSpell(LivingEntity target) {
		// He aims by facing, so the look has to be on the target before the spell leaves him
		master.lookAt(EntityAnchorArgument.Anchor.EYES, target.getEyePosition());
		master.setYBodyRot(master.getYRot());
		master.setYHeadRot(master.getYRot());

		spellFor(master.getDuelLevel(), master.getRandom().nextBoolean()).get().castFromMob(master, target);
	}

	/** Use stronger magic spells */
	private KKSupplier<Magic> spellFor(int level, boolean fire) {
		if (level >= MasterDuelEntity.THIRD_GRADE_FROM) {
			return fire ? ModMagic.FIRAGA : ModMagic.THUNDAGA;
		}

		if (level >= MasterDuelEntity.SECOND_GRADE_FROM) {
			return fire ? ModMagic.FIRA : ModMagic.THUNDARA;
		}

		return fire ? ModMagic.FIRE : ModMagic.THUNDER;
	}

	private void chase(LivingEntity target, double speed) {
		if (--repath > 0) {
			return;
		}
		repath = REPATH_INTERVAL;
		master.getNavigation().moveTo(target, speed);
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
		return master.getDuelLevel() >= MasterDuelEntity.MAGIC_FROM;
	}

	private int comboHits() {
		return Math.min(COMBO_HITS_MAX, COMBO_HITS_BASE + master.getDuelLevel() / COMBO_HITS_PER_LEVEL);
	}

	private int swingInterval() {
		return Math.max(SWING_INTERVAL_MIN, SWING_INTERVAL_BASE - master.getDuelLevel() / SWING_INTERVAL_PER_LEVEL);
	}

	private int recovery() {
		int base = Math.max(RECOVERY_MIN, RECOVERY_BASE - master.getDuelLevel() / RECOVERY_PER_LEVEL);
		return base + Mth.nextInt(master.getRandom(), 0, Math.max(1, base / 4));
	}
}
