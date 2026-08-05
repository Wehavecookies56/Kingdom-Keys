package online.kingdomkeys.kingdomkeys.entity.mob;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.FlyingMoveControl;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.navigation.FlyingPathNavigation;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.entity.EntityHelper;
import online.kingdomkeys.kingdomkeys.entity.mob.goal.AirSoldierGoal;

public class AirSoldierEntity extends BaseKHEntity {
	private static final double FLIGHT_HEIGHT = 4.0;
	private static final int GROUND_SCAN = 24;
	private static final double CLIMB_RATE = 0.08;
	private static final double VERTICAL_DAMPING = 0.85;

	public AirSoldierEntity(EntityType<? extends Monster> type, Level worldIn) {
		super(type, worldIn);
		xpReward = 8;
		this.moveControl = new FlyingMoveControl(this, 20, true);
	}

	@Override
	protected PathNavigation createNavigation(Level level) {
		FlyingPathNavigation navigation = new FlyingPathNavigation(this, level);
		navigation.setCanOpenDoors(false);
		navigation.setCanFloat(true);
		navigation.setCanPassDoors(true);
		return navigation;
	}

	// The goal drives its own height while climbing and diving, so the cruise only applies at rest.
	@Override
	protected void customServerAiStep() {
		super.customServerAiStep();

		if (getState() != AirSoldierGoal.STATE_IDLE) {
			return;
		}

		setNoGravity(true);

		double target = groundBelow() + FLIGHT_HEIGHT;
		double difference = target - getY();

		Vec3 movement = getDeltaMovement();
		if (Math.abs(difference) > 0.25) {
			movement = movement.add(0, Mth.clamp(difference * 0.05, -CLIMB_RATE, CLIMB_RATE), 0);
		}
		setDeltaMovement(movement.x, movement.y * VERTICAL_DAMPING, movement.z);
	}

	public double groundBelow() {
		BlockPos.MutableBlockPos pos = blockPosition().mutable();
		int startY = blockPosition().getY();

		for (int i = 0; i <= GROUND_SCAN; i++) {
			pos.setY(startY - i);
			if (!level().getBlockState(pos).getCollisionShape(level(), pos).isEmpty()) {
				return pos.getY() + 1;
			}
		}
		return getY() - GROUND_SCAN;
	}

	@OnlyIn(Dist.CLIENT)
	public ResourceLocation getTexture() {
		return KingdomKeys.rl("textures/entity/mob/air_soldier.png");
	}

	@Override
	protected void registerGoals() {
		this.goalSelector.addGoal(1, new MeleeAttackGoal(this, 1.0D, true));
		this.goalSelector.addGoal(5, new MoveTowardsRestrictionGoal(this, 1.0D));
		this.goalSelector.addGoal(7, new WaterAvoidingRandomFlyingGoal(this, 1.0D));
		this.goalSelector.addGoal(8, new LookAtPlayerGoal(this, Player.class, 8.0F));
		this.goalSelector.addGoal(8, new RandomLookAroundGoal(this));
		this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, Player.class, true));
		this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Villager.class, true));
		this.targetSelector.addGoal(4, new AirSoldierGoal(this));
	}

	public static AttributeSupplier.Builder registerAttributes() {
		return Mob.createLivingAttributes()
				.add(Attributes.FOLLOW_RANGE, 20.0D)
				.add(Attributes.MOVEMENT_SPEED, 0.32D)
				.add(Attributes.FLYING_SPEED, 0.55D)
				.add(Attributes.MAX_HEALTH, 60.0D)
				.add(Attributes.ATTACK_DAMAGE, 4.0D)
				.add(Attributes.ATTACK_KNOCKBACK, 1.0D);
	}

	@Override
	public boolean causeFallDamage(float distance, float multiplier, DamageSource source) {
		return false;
	}

	@Override
	public int getMaxSpawnClusterSize() {
		return 3;
	}

	@Override
	public EntityHelper.MobType getKHMobType() {
		return EntityHelper.MobType.HEARTLESS_EMBLEM;
	}
}
