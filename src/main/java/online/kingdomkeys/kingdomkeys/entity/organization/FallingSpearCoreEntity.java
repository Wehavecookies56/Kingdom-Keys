package online.kingdomkeys.kingdomkeys.entity.organization;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ThrowableProjectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import online.kingdomkeys.kingdomkeys.damagesource.KKDamageTypes;
import online.kingdomkeys.kingdomkeys.entity.ModEntities;
import online.kingdomkeys.kingdomkeys.util.Utils;

import java.util.List;

public class FallingSpearCoreEntity extends ThrowableProjectile {

	private static final int TOTAL_JUMPS = 3;
	private static final float HEIGHT_ABOVE_TARGET = 6F;
	private static final float SEARCH_RADIUS = 12F;
	private static final float LANDING_HEIGHT_MARGIN = 1.3F; // how close to the target's Y counts as "landed"
	private static final int MAX_TICKS_PER_JUMP = 40; // safety timeout in case they never "land" (e.g. target died)
	private static final float SLAM_RADIUS = 2.5F;

	private float dmg;
	private int jumpsDone = 0;
	private int ticksThisJump = 0;
	private LivingEntity currentTarget = null;
	private boolean waitingToLand = false;

	public FallingSpearCoreEntity(EntityType<? extends ThrowableProjectile> type, Level world) {
		super(type, world);
		this.blocksBuilding = false;
		this.noPhysics = true;
	}

	public FallingSpearCoreEntity(Level world, Player caster, LivingEntity preferredTarget, float dmg) {
		super(ModEntities.TYPE_FALLING_SPEAR.get(), caster, world);
		this.dmg = dmg;
		this.setDeltaMovement(Vec3.ZERO);
		this.currentTarget = preferredTarget;
		this.setPos(caster.getX(), caster.getY(), caster.getZ());
	}

	@Override
	protected double getDefaultGravity() {
		return 0D;
	}

	@Override
	public void tick() {
		if (!(getOwner() instanceof Player caster) || !caster.isAlive()) {
			this.remove(RemovalReason.KILLED);
			return;
		}

		if (level().isClientSide) {
			super.tick();
			return;
		}

		if (jumpsDone >= TOTAL_JUMPS) {
			this.remove(RemovalReason.KILLED);
			return;
		}

		ticksThisJump++;

		if (!waitingToLand) {
			if (currentTarget == null || !currentTarget.isAlive()) {
				currentTarget = findNearestTarget(caster);
			}
			if (currentTarget == null) {
				// Nothing left to dive onto - end early rather than stall forever.
				this.remove(RemovalReason.KILLED);
				return;
			}

			caster.teleportTo(currentTarget.getX(), currentTarget.getY() + HEIGHT_ABOVE_TARGET, currentTarget.getZ());
			caster.setDeltaMovement(0, -0.15, 0);
			caster.fallDistance = 0;
			ticksThisJump = 0;
			waitingToLand = true;
			level().playSound(null, caster.blockPosition(), SoundEvents.ENDERMAN_TELEPORT, SoundSource.PLAYERS, 1F, 0.7F);
		} else {
			boolean targetGone = currentTarget == null || !currentTarget.isAlive();
			boolean landed = !targetGone && caster.getY() <= currentTarget.getY() + LANDING_HEIGHT_MARGIN;
			boolean timedOut = ticksThisJump > MAX_TICKS_PER_JUMP;

			if (targetGone || landed || timedOut) {
				if (!targetGone) {
					slam(caster, currentTarget);
				}
				waitingToLand = false;
				jumpsDone++;
				ticksThisJump = 0;
			}
		}

		this.setPos(caster.getX(), caster.getY(), caster.getZ());
		super.tick();
	}

	private LivingEntity findNearestTarget(Player caster) {
		List<LivingEntity> nearby = Utils.getLivingEntitiesInRadiusExcludingParty(caster, SEARCH_RADIUS);
		LivingEntity closest = null;
		double closestDistSqr = Double.MAX_VALUE;
		for (LivingEntity candidate : nearby) {
			double distSqr = candidate.distanceToSqr(caster);
			if (distSqr < closestDistSqr) {
				closest = candidate;
				closestDistSqr = distSqr;
			}
		}
		return closest;
	}

	private void slam(Player caster, LivingEntity primaryTarget) {
		List<LivingEntity> nearby = Utils.getLivingEntitiesInRadiusExcludingParty(caster, SLAM_RADIUS);
		for (LivingEntity target : nearby) {
			target.hurt(KKDamageTypes.getElementalDamage(KKDamageTypes.AIR, this, caster), dmg);
			target.invulnerableTime = 0;
		}

		if (level() instanceof ServerLevel serverLevel) {
			serverLevel.sendParticles(ParticleTypes.CLOUD, caster.getX(), caster.getY(), caster.getZ(), 20, 0.6, 0.1, 0.6, 0.05);
			serverLevel.sendParticles(ParticleTypes.CRIT, caster.getX(), caster.getY(), caster.getZ(), 15, 0.6, 0.2, 0.6, 0.1);
		}
		level().playSound(null, caster.blockPosition(), SoundEvents.GENERIC_EXPLODE.value(), SoundSource.PLAYERS, 0.6F, 1.5F);

		// Small upward bounce so the caster doesn't just stay stuck in the ground between jumps.
		caster.setDeltaMovement(caster.getDeltaMovement().x, 0.35D, caster.getDeltaMovement().z);
		caster.fallDistance = 0;
	}

	@Override
	protected void onHit(HitResult result) {}

	@Override
	protected void defineSynchedData(net.minecraft.network.syncher.SynchedEntityData.Builder pBuilder) {
	}
}
