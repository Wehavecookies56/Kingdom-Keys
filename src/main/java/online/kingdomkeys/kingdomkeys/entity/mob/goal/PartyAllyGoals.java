package online.kingdomkeys.kingdomkeys.entity.mob.goal;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.target.TargetGoal;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.PathType;
import net.minecraft.world.level.pathfinder.WalkNodeEvaluator;
import online.kingdomkeys.kingdomkeys.lib.Party;
import online.kingdomkeys.kingdomkeys.util.Utils;

import javax.annotation.Nullable;
import java.util.EnumSet;

public final class PartyAllyGoals {

	private PartyAllyGoals() {}

	/**
	 * Priorities, low number wins.
	 */
	public static final int FOLLOW_PRIORITY = 4;
	public static final int TARGET_PRIORITY = 1;

	/** Vanilla's wolf numbers: starts walking at 10 blocks, settles at 2, and teleports past 12 */
	private static final float START_DISTANCE = 10F;
	private static final float STOP_DISTANCE = 2F;
	private static final double TELEPORT_DISTANCE_SQR = 144D;

	private static final double FOLLOW_SPEED = 1D;

	@Nullable
	static Player leader(Mob mob) {
		Party party = Utils.getParty(mob);

		if (party == null) {
			return null;
		}

		for (Party.Member member : party.getMembers()) {
			if (!member.isLeader()) {
				continue;
			}

			if (Utils.getPartyEntity(mob.level(), member.getUUID()) instanceof Player player && player.isAlive()) {
				return player;
			}
		}

		return null;
	}

	public static boolean mayTarget(Mob ally, @Nullable LivingEntity target) {
		if (target == null || !Utils.canHarm(ally, target)) {
			return false;
		}

		if (!(target instanceof Player)) {
			return true;
		}

		if (ally.getLastHurtByMob() == target) {
			return true;
		}

		Player leader = leader(ally);

		return leader != null && (leader.getLastHurtByMob() == target || leader.getLastHurtMob() == target);
	}

	public static void applyAI(Mob mob) {
		removeAI(mob);

		mob.goalSelector.addGoal(FOLLOW_PRIORITY, new FollowLeader(mob));
		mob.targetSelector.addGoal(TARGET_PRIORITY, new LeaderHurtBy(mob));
		mob.targetSelector.addGoal(TARGET_PRIORITY, new LeaderHurt(mob));

		mob.setPersistenceRequired();
	}

	public static void removeAI(Mob mob) {
		mob.goalSelector.removeAllGoals(goal -> goal instanceof FollowLeader);
		mob.targetSelector.removeAllGoals(goal -> goal instanceof LeaderHurtBy || goal instanceof LeaderHurt);
	}

	public static boolean isApplied(Mob mob) {
		return mob.goalSelector.getAvailableGoals().stream().anyMatch(wrapped -> wrapped.getGoal() instanceof FollowLeader);
	}


	public static class FollowLeader extends Goal {

		private final Mob mob;
		private final PathNavigation navigation;

		@Nullable
		private Player following;
		private int timeToRecalcPath;
		private float oldWaterCost;

		public FollowLeader(Mob mob) {
			this.mob = mob;
			this.navigation = mob.getNavigation();
			this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
		}

		@Override
		public boolean canUse() {
			Player leader = leader(mob);

			if (leader == null || leader.isSpectator() || mob.isPassenger()) {
				return false;
			}

			if (mob.distanceToSqr(leader) < START_DISTANCE * START_DISTANCE) {
				return false;
			}

			this.following = leader;
			return true;
		}

		@Override
		public boolean canContinueToUse() {
			if (following == null || navigation.isDone() || mob.isPassenger()) {
				return false;
			}

			return mob.distanceToSqr(following) > STOP_DISTANCE * STOP_DISTANCE;
		}

		@Override
		public void start() {
			this.timeToRecalcPath = 0;
			this.oldWaterCost = mob.getPathfindingMalus(PathType.WATER);
			mob.setPathfindingMalus(PathType.WATER, 0F);
		}

		@Override
		public void stop() {
			this.following = null;
			navigation.stop();
			mob.setPathfindingMalus(PathType.WATER, oldWaterCost);
		}

		@Override
		public void tick() {
			if (following == null) {
				return;
			}

			boolean tooFar = mob.distanceToSqr(following) >= TELEPORT_DISTANCE_SQR;

			if (!tooFar) {
				mob.getLookControl().setLookAt(following, 10F, mob.getMaxHeadXRot());
			}

			if (--timeToRecalcPath > 0) {
				return;
			}

			this.timeToRecalcPath = adjustedTickDelay(10);

			if (tooFar) {
				teleportNear(following.blockPosition());
			} else {
				navigation.moveTo(following, FOLLOW_SPEED);
			}
		}

		// Straight from TamableAnimal: ten tries in a small box around the leader, never right on top of them
		private void teleportNear(BlockPos pos) {
			for (int i = 0; i < 10; i++) {
				int x = mob.getRandom().nextIntBetweenInclusive(-3, 3);
				int z = mob.getRandom().nextIntBetweenInclusive(-3, 3);

				if (Math.abs(x) < 2 && Math.abs(z) < 2) {
					continue;
				}

				int y = mob.getRandom().nextIntBetweenInclusive(-1, 1);

				if (teleportTo(pos.getX() + x, pos.getY() + y, pos.getZ() + z)) {
					return;
				}
			}
		}

		private boolean teleportTo(int x, int y, int z) {
			BlockPos to = new BlockPos(x, y, z);

			if (WalkNodeEvaluator.getPathTypeStatic(mob, to) != PathType.WALKABLE) {
				return false;
			}

			BlockState below = mob.level().getBlockState(to.below());

			if (below.getBlock() instanceof LeavesBlock) {
				return false;
			}

			if (!mob.level().noCollision(mob, mob.getBoundingBox().move(to.subtract(mob.blockPosition())))) {
				return false;
			}

			mob.moveTo(x + 0.5, y, z + 0.5, mob.getYRot(), mob.getXRot());
			navigation.stop();
			return true;
		}
	}

	public static class LeaderHurtBy extends TargetGoal {
		private final Mob ally;
		@Nullable
		private LivingEntity leaderLastHurtBy;
		private int timestamp;

		public LeaderHurtBy(Mob mob) {
			super(mob, false);
			this.ally = mob;
			this.setFlags(EnumSet.of(Flag.TARGET));
		}

		@Override
		public boolean canUse() {
			Player leader = leader(ally);

			if (leader == null) {
				return false;
			}

			this.leaderLastHurtBy = leader.getLastHurtByMob();
			return leader.getLastHurtByMobTimestamp() != timestamp && canAttack(leaderLastHurtBy, TargetingConditions.DEFAULT) && Utils.canHarm(ally, leaderLastHurtBy);
		}

		@Override
		public void start() {
			ally.setTarget(leaderLastHurtBy);

			Player leader = leader(ally);

			if (leader != null) {
				this.timestamp = leader.getLastHurtByMobTimestamp();
			}

			super.start();
		}
	}

	public static class LeaderHurt extends TargetGoal {
		private final Mob ally;
		@Nullable
		private LivingEntity leaderLastHurt;
		private int timestamp;

		public LeaderHurt(Mob mob) {
			super(mob, false);
			this.ally = mob;
			this.setFlags(EnumSet.of(Flag.TARGET));
		}

		@Override
		public boolean canUse() {
			Player leader = leader(ally);

			if (leader == null) {
				return false;
			}

			this.leaderLastHurt = leader.getLastHurtMob();
			return leader.getLastHurtMobTimestamp() != timestamp && canAttack(leaderLastHurt, TargetingConditions.DEFAULT) && Utils.canHarm(ally, leaderLastHurt);
		}

		@Override
		public void start() {
			ally.setTarget(leaderLastHurt);

			Player leader = leader(ally);

			if (leader != null) {
				this.timestamp = leader.getLastHurtMobTimestamp();
			}

			super.start();
		}
	}
}
