package online.kingdomkeys.kingdomkeys.entity.mob;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import online.kingdomkeys.kingdomkeys.entity.EntityHelper;

import java.util.EnumSet;

public abstract class TrainingOrbEntity extends BaseKHEntity {
	private static final EntityDataAccessor<Boolean> CHARGING = SynchedEntityData.defineId(TrainingOrbEntity.class, EntityDataSerializers.BOOLEAN);

	private BlockPos spawnPoint;
	private int cdTicks;

	protected TrainingOrbEntity(EntityType<? extends Monster> type, Level level) {
		super(type, level);
		this.moveControl = new OrbMoveControl(this);
		this.setNoGravity(true);
		this.xpReward = 3;
	}

	protected abstract ParticleOptions trail();

	protected abstract int cdTicks();

	@Override
	protected void registerGoals() {
		this.goalSelector.addGoal(1, new ChargeGoal());
		this.goalSelector.addGoal(4, new DriftGoal());
		this.goalSelector.addGoal(8, new LookAtPlayerGoal(this, Player.class, 8.0F));
		this.targetSelector.addGoal(0, new HurtByTargetGoal(this));
		this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, Player.class, true));
	}

	@Override
	protected void defineSynchedData(SynchedEntityData.Builder builder) {
		super.defineSynchedData(builder);
		builder.define(CHARGING, false);
	}

	public boolean isCharging() {
		return getEntityData().get(CHARGING);
	}

	public void setCharging(boolean charging) {
		getEntityData().set(CHARGING, charging);
	}

	public BlockPos getSpawnPoint() {
		return spawnPoint == null ? blockPosition() : spawnPoint;
	}

	public void setSpawnPoint(BlockPos spawnPoint) {
		this.spawnPoint = spawnPoint;
	}

	@Override
	public EntityHelper.MobType getKHMobType() {
		return EntityHelper.MobType.NPC;
	}

	@Override
	public void tick() {
		super.tick();
		this.setNoGravity(true);

		if (cdTicks > 0) {
			cdTicks--;
		}

		if (!level().isClientSide) {
			if (spawnPoint == null) {
				spawnPoint = blockPosition();
			}

			if (tickCount % 10 == 0 && !level().noCollision(this, getBoundingBox().deflate(0.1D))) {
				setDeltaMovement(getDeltaMovement().add(0.0D, 0.14D, 0.0D));
			}
		}

		if (level().isClientSide && random.nextInt(isCharging() ? 2 : 6) == 0) {
			double spread = getBbWidth() * 0.6;
			level().addParticle(trail(), getX() + (random.nextDouble() - 0.5) * spread, getY() + getBbHeight() * 0.5 + (random.nextDouble() - 0.5) * spread, getZ() + (random.nextDouble() - 0.5) * spread, 0, 0, 0);
		}
	}

	public float hoverPhase(float partialTick) {
		return (tickCount + partialTick) * 0.1F;
	}

	@Override
	public boolean doHurtTarget(net.minecraft.world.entity.Entity target) {
		boolean hit = super.doHurtTarget(target);
		if (hit) {
			cdTicks = cdTicks();
			Vec3 away = position().subtract(target.position()).normalize().scale(0.4).add(0, 0.15, 0);
			setDeltaMovement(away);
			hasImpulse = true;
		}
		return hit;
	}

	@Override
	public boolean isPushable() {
		return false;
	}

	@Override
	public boolean causeFallDamage(float distance, float multiplier, DamageSource source) {
		return false;
	}

	@Override
	public boolean isInWall() {
		return false;
	}

	@Override
	public boolean isIgnoringBlockTriggers() {
		return true;
	}

	@Override
	public float getLightLevelDependentMagicValue() {
		return 1.0F;
	}

	@Override
	protected SoundEvent getHurtSound(DamageSource source) {
		return SoundEvents.AMETHYST_BLOCK_HIT;
	}

	@Override
	protected SoundEvent getDeathSound() {
		return SoundEvents.AMETHYST_BLOCK_BREAK;
	}

	@Override
	public void addAdditionalSaveData(CompoundTag tag) {
		super.addAdditionalSaveData(tag);
		if (spawnPoint != null) {
			tag.putInt("AnchorX", spawnPoint.getX());
			tag.putInt("AnchorY", spawnPoint.getY());
			tag.putInt("AnchorZ", spawnPoint.getZ());
		}
	}

	@Override
	public void readAdditionalSaveData(CompoundTag tag) {
		super.readAdditionalSaveData(tag);
		if (tag.contains("AnchorX")) {
			spawnPoint = new BlockPos(tag.getInt("AnchorX"), tag.getInt("AnchorY"), tag.getInt("AnchorZ"));
		}
	}

	static class OrbMoveControl extends MoveControl {
		OrbMoveControl(TrainingOrbEntity orb) {
			super(orb);
		}

		@Override
		public void tick() {
			if (this.operation != MoveControl.Operation.MOVE_TO) {
				return;
			}

			Vec3 toWanted = new Vec3(this.wantedX - mob.getX(), this.wantedY - mob.getY(), this.wantedZ - mob.getZ());
			double distance = toWanted.length();

			if (distance < mob.getBoundingBox().getSize()) {
				this.operation = MoveControl.Operation.WAIT;
				mob.setDeltaMovement(mob.getDeltaMovement().scale(0.5));
				return;
			}

			mob.setDeltaMovement(mob.getDeltaMovement().add(toWanted.scale(this.speedModifier * 0.05 / distance)));

			LivingEntity target = mob.getTarget();
			double facingX = target != null ? target.getX() - mob.getX() : mob.getDeltaMovement().x;
			double facingZ = target != null ? target.getZ() - mob.getZ() : mob.getDeltaMovement().z;
			mob.setYRot(-((float) Mth.atan2(facingX, facingZ)) * (180F / (float) Math.PI));
			mob.yBodyRot = mob.getYRot();
		}
	}

	// Charges at their enemy
	class ChargeGoal extends Goal {
		ChargeGoal() {
			setFlags(EnumSet.of(Goal.Flag.MOVE));
		}

		@Override
		public boolean canUse() {
			LivingEntity target = getTarget();
			return target != null && target.isAlive() && cdTicks <= 0 && !getMoveControl().hasWanted() && random.nextInt(reducedTickDelay(10)) == 0 && distanceToSqr(target) > 4.0D;
		}

		@Override
		public boolean canContinueToUse() {
			LivingEntity target = getTarget();
			return getMoveControl().hasWanted() && isCharging() && target != null && target.isAlive();
		}

		@Override
		public void start() {
			LivingEntity target = getTarget();
			if (target != null) {
				Vec3 eyes = target.getEyePosition();
				moveControl.setWantedPosition(eyes.x, eyes.y, eyes.z, 1.0D);
			}
			setCharging(true);
			playSound(SoundEvents.AMETHYST_BLOCK_CHIME, 0.8F, 1.4F);
		}

		@Override
		public void stop() {
			setCharging(false);
		}

		@Override
		public boolean requiresUpdateEveryTick() {
			return true;
		}

		@Override
		public void tick() {
			LivingEntity target = getTarget();
			if (target == null) {
				return;
			}

			if (getBoundingBox().intersects(target.getBoundingBox())) {
				doHurtTarget(target);
				setCharging(false);
				return;
			}

			// Keeps correcting once it is close, so a sidestep actually dodges it
			if (distanceToSqr(target) < 9.0D) {
				Vec3 eyes = target.getEyePosition();
				moveControl.setWantedPosition(eyes.x, eyes.y, eyes.z, 1.0D);
			}
		}
	}

	class DriftGoal extends Goal {
		DriftGoal() {
			setFlags(EnumSet.of(Goal.Flag.MOVE));
		}

		@Override
		public boolean canUse() {
			return !getMoveControl().hasWanted() && random.nextInt(reducedTickDelay(8)) == 0;
		}

		@Override
		public boolean canContinueToUse() {
			return false;
		}

		@Override
		public void tick() {
			BlockPos around = getSpawnPoint();
			for (int attempt = 0; attempt < 5; attempt++) {
				BlockPos spot = around.offset(random.nextInt(11) - 5, random.nextInt(4), random.nextInt(11) - 5);
				if (!level().isEmptyBlock(spot) || !level().isEmptyBlock(spot.above())) {
					continue;
				}

				moveControl.setWantedPosition(spot.getX() + 0.5D, spot.getY() + 0.5D, spot.getZ() + 0.5D, 0.3D);
				if (getTarget() == null) {
					getLookControl().setLookAt(spot.getX() + 0.5D, spot.getY() + 0.5D, spot.getZ() + 0.5D, 180F, 20F);
				}
				return;
			}
		}
	}
}
