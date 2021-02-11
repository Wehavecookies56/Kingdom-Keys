package online.kingdomkeys.kingdomkeys.entity.mob;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.ai.goal.HurtByTargetGoal;
import net.minecraft.entity.ai.goal.LookAtGoal;
import net.minecraft.entity.ai.goal.LookRandomlyGoal;
import net.minecraft.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.entity.ai.goal.MoveTowardsRestrictionGoal;
import net.minecraft.entity.ai.goal.NearestAttackableTargetGoal;
import net.minecraft.entity.ai.goal.RandomWalkingGoal;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.FMLPlayMessages;
import online.kingdomkeys.kingdomkeys.capability.ModCapabilities;
import online.kingdomkeys.kingdomkeys.entity.EntityHelper;
import online.kingdomkeys.kingdomkeys.entity.ModEntities;

public class DuskEntity extends MonsterEntity implements IKHMob {

	public DuskEntity(EntityType<? extends MonsterEntity> type, World worldIn) {
		super(type, worldIn);
	}

	public DuskEntity(FMLPlayMessages.SpawnEntity spawnEntity, World world) {
		super(ModEntities.TYPE_DUSK.get(), world);
	}

	@Override
    public boolean canSpawn(IWorld worldIn, SpawnReason spawnReasonIn) {
    	return ModCapabilities.getWorld((World)worldIn).getHeartlessSpawnLevel() > 0;
    }
	
	@Override
	protected void registerGoals() {
		this.goalSelector.addGoal(0, new MeleeAttackGoal(this, 1.0D, false));
		this.goalSelector.addGoal(0, new CoilGoal(this));
		this.goalSelector.addGoal(1, new MoveTowardsRestrictionGoal(this, 1.0D));
		this.goalSelector.addGoal(2, new RandomWalkingGoal(this, 1.0D));
		this.goalSelector.addGoal(3, new LookAtGoal(this, PlayerEntity.class, 8.0F));
		this.goalSelector.addGoal(4, new LookRandomlyGoal(this));
		this.targetSelector.addGoal(0, new HurtByTargetGoal(this));
		this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, PlayerEntity.class, true));
	}

	public static AttributeModifierMap.MutableAttribute registerAttributes() {
		return MobEntity.registerAttributes()
				.createMutableAttribute(Attributes.FOLLOW_RANGE, 35.0D)
				.createMutableAttribute(Attributes.MOVEMENT_SPEED, 0.2D)
				.createMutableAttribute(Attributes.MAX_HEALTH, 50.0D)
				.createMutableAttribute(Attributes.KNOCKBACK_RESISTANCE, 1000.0D)
				.createMutableAttribute(Attributes.ATTACK_DAMAGE, 4.0D)
				.createMutableAttribute(Attributes.ATTACK_KNOCKBACK, 1.0D)
				;
	}

	@Override
	public int getMaxSpawnedInChunk() {
		return 4;
	}

	@Override
	protected void registerData() {
		super.registerData();
		this.dataManager.register(EntityHelper.STATE, 0);
	}

	@Override
	public EntityHelper.MobType getMobType() {
		return EntityHelper.MobType.NOBODY;
	}

	@Override
	public boolean onLivingFall(float distance, float damageMultiplier) {
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
		public boolean shouldExecute() {
			if (theEntity.getAttackTarget() != null) {
				if (!canUseAttack) {
					if (attackTimer > 0) {
						attackTimer--;
						return false;
					} else
						return true;
				} else
					return true;
			} else
				return false;
		}

		@Override
		public boolean shouldContinueExecuting() {
			boolean flag = canUseAttack;

			return flag;
		}

		@Override
		public void startExecuting() {
			canUseAttack = true;
			attackTimer = 40 + world.rand.nextInt(10);
			EntityHelper.setState(theEntity, 0);
			LivingEntity target = this.theEntity.getAttackTarget();

			if (target != null)
				posToCharge = new double[] { target.getPosX(), target.getPosY(), target.getPosZ() };
		}

		@Override
		public void tick() {
			if (theEntity.getAttackTarget() != null && canUseAttack) {
				EntityHelper.setState(theEntity, 1);
				LivingEntity target = this.theEntity.getAttackTarget();
				this.theEntity.getLookController().setLookPositionWithEntity(target, 30F, 30F);
				this.theEntity.getNavigator().tryMoveToXYZ(posToCharge[0], posToCharge[1], posToCharge[2], 10.0D);
				canUseAttack = false;
			}
		}

	}
}
