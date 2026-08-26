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
import online.kingdomkeys.kingdomkeys.entity.EntityHelper;

public abstract class BaseElementalMusicalHeartlessEntity extends BaseKHEntity {

    public enum Element {
        FIRE, BLIZZARD, THUNDER, AERO, CURE
    }

    private static final double FLIGHT_HEIGHT = 2.5;
    private static final double CHARGING_ATTACK_HEIGHT = 0.05;

    private static final int GROUND_SCAN = 24;
    private static final double CLIMB_RATE = 0.06;
    private static final double DESCEND_RATE = 0.35;

    private static final double VERTICAL_DAMPING = 0.85;
    private static final double HORIZONTAL_DAMPING = 0.6;

    protected BaseElementalMusicalHeartlessEntity(EntityType<? extends Monster> type, Level worldIn) {
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

    public boolean isChargingAttack() {
        return getTarget() != null && getAttributeValue(Attributes.MOVEMENT_SPEED) <= 0.001;
    }

    @Override
    protected void customServerAiStep() {
        super.customServerAiStep();

        if (isChargingAttack()) {
            charging();
        } else {
            hover();
        }
    }

    private void charging() {
        getNavigation().stop();
        setNoGravity(true);

        double target = groundBelow() + CHARGING_ATTACK_HEIGHT;
        double difference = target - getY();
        double rise = Mth.clamp(difference, -DESCEND_RATE, DESCEND_RATE);

        setDeltaMovement(getDeltaMovement().x * HORIZONTAL_DAMPING, rise, getDeltaMovement().z * HORIZONTAL_DAMPING);
    }

    private void hover() {
        setNoGravity(true);

        double target = groundBelow() + FLIGHT_HEIGHT;
        double difference = target - getY();

        Vec3 movement = getDeltaMovement();
        if (Math.abs(difference) > 0.25) {
            movement = movement.add(0, Mth.clamp(difference * 0.05, -CLIMB_RATE, CLIMB_RATE), 0);
        }
        setDeltaMovement(movement.x, movement.y * VERTICAL_DAMPING, movement.z);
    }

    private double groundBelow() {
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

    @Override
    public boolean causeFallDamage(float distance, float multiplier, DamageSource source) {
        return false;
    }

    protected abstract Goal goalToUse();

    @OnlyIn(Dist.CLIENT)
    public abstract ResourceLocation getTexture();

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, goalToUse());
        this.goalSelector.addGoal(1, new MeleeAttackGoal(this, 1.5D, true));
        this.goalSelector.addGoal(1, new MoveTowardsRestrictionGoal(this, 1.0D));
        this.goalSelector.addGoal(2, new WaterAvoidingRandomFlyingGoal(this, 1.0D));
        this.goalSelector.addGoal(3, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(4, new RandomLookAroundGoal(this));
        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, Player.class, true));
		this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Villager.class, true));
    }

    public static AttributeSupplier.Builder registerAttributes() {
        return Mob.createLivingAttributes()
            .add(Attributes.FOLLOW_RANGE, 35.0D)
            .add(Attributes.MOVEMENT_SPEED, 0.2D)
            .add(Attributes.FLYING_SPEED, 0.5D)
            .add(Attributes.ATTACK_DAMAGE, 0.0D)
			.add(Attributes.ATTACK_KNOCKBACK, 1.0D)
            ;
    }

    public abstract Element getElementToUse();

    @Override
    public EntityHelper.MobType getKHMobType() {
        return EntityHelper.MobType.HEARTLESS_EMBLEM;
    }
}
