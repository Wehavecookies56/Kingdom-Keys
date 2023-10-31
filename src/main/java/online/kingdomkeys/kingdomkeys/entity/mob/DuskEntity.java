package online.kingdomkeys.kingdomkeys.entity.mob;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.MoveTowardsRestrictionGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraftforge.network.PlayMessages;
import online.kingdomkeys.kingdomkeys.capability.ModCapabilities;
import online.kingdomkeys.kingdomkeys.entity.EntityHelper;
import online.kingdomkeys.kingdomkeys.entity.ModEntities;

public class DuskEntity extends BaseKHEntity {

	public DuskEntity(EntityType<? extends Monster> type, Level worldIn) {
		super(type, worldIn);
	}

	public DuskEntity(PlayMessages.SpawnEntity spawnEntity, Level world) {
		super(ModEntities.TYPE_DUSK.get(), world);
	}

	@Override
    public boolean checkSpawnRules(LevelAccessor worldIn, MobSpawnType spawnReasonIn) {
    	return worldIn == null ? false : ModCapabilities.getWorld((Level)worldIn).getHeartlessSpawnLevel() > 0;
    }
	
	@Override
	protected void registerGoals() {
		this.goalSelector.addGoal(0, new MeleeAttackGoal(this, 1.0D, false));
		this.goalSelector.addGoal(0, new CoilGoal(this));
		this.goalSelector.addGoal(1, new MoveTowardsRestrictionGoal(this, 1.0D));
		this.goalSelector.addGoal(2, new RandomStrollGoal(this, 1.0D));
		this.goalSelector.addGoal(3, new LookAtPlayerGoal(this, Player.class, 8.0F));
		this.goalSelector.addGoal(4, new RandomLookAroundGoal(this));
		this.targetSelector.addGoal(0, new HurtByTargetGoal(this));
		this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, Player.class, true));
	}

	public static AttributeSupplier.Builder registerAttributes() {
		return Mob.createLivingAttributes()
				.add(Attributes.FOLLOW_RANGE, 35.0D)
				.add(Attributes.MOVEMENT_SPEED, 0.2D)
				.add(Attributes.MAX_HEALTH, 40.0D)
				.add(Attributes.KNOCKBACK_RESISTANCE, 1000.0D)
				.add(Attributes.ATTACK_DAMAGE, 4.0D)
				.add(Attributes.ATTACK_KNOCKBACK, 1.0D)
				;
	}

	@Override
	public int getMaxSpawnClusterSize() {
		return 4;
	}

	@Override
	protected void defineSynchedData() {
		super.defineSynchedData();
		this.entityData.define(EntityHelper.STATE, 0);
	}

	@Override
	public EntityHelper.MobType getKHMobType() {
		return EntityHelper.MobType.NOBODY;
	}

	@Override
	public boolean causeFallDamage(float pFallDistance, float pMultiplier, DamageSource pSource) {
		return false;
	}

	class CoilGoal extends Goal {
		private DuskEntity theEntity;
		private boolean canUseAttack = true;
		private int attackTimer = 30;
		private double[] posToCharge;

		public CoilGoal(DuskEntity e) {
			this.theEntity = e;
		}

		@Override
		public boolean canUse() {
			if (theEntity.getTarget() != null) {
				if (!canUseAttack) {
					if (attackTimer > 0) {
						attackTimer-=2;
						return false;
					} else
						return true;
				} else
					return true;
			} else
				return false;
		}

		@Override
		public boolean canContinueToUse() {
			boolean flag = canUseAttack;

			return flag;
		}

		@Override
		public void start() {
			canUseAttack = true;
			attackTimer = 40 + level().random.nextInt(10);
			EntityHelper.setState(theEntity, 0);
			LivingEntity target = this.theEntity.getTarget();

			if (target != null)
				posToCharge = new double[] { target.getX(), target.getY(), target.getZ() };
		}

		@Override
		public void tick() {
			if (theEntity.getTarget() != null && canUseAttack) {
				EntityHelper.setState(theEntity, 1);
				LivingEntity target = this.theEntity.getTarget();
				this.theEntity.getLookControl().setLookAt(target, 30F, 30F);
				this.theEntity.getNavigation().moveTo(posToCharge[0], posToCharge[1], posToCharge[2], 10.0D);
				canUseAttack = false;
			}
		}

	}
}
