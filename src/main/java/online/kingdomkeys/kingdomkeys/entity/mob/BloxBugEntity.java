package online.kingdomkeys.kingdomkeys.entity.mob;

import net.minecraft.commands.arguments.EntityAnchorArgument;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.damagesource.DamageSources;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.extensions.IForgeEntity;
import net.minecraftforge.entity.IEntityAdditionalSpawnData;
import net.minecraftforge.network.PlayMessages;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.entity.EntityHelper;
import online.kingdomkeys.kingdomkeys.entity.ModEntities;
import online.kingdomkeys.kingdomkeys.util.Utils;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.GeoAnimatable;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.*;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.EnumSet;

public class BloxBugEntity extends PathfinderMob implements GeoEntity, IKHMob, IEntityAdditionalSpawnData {

    public static final EntityDataAccessor<Integer> STATE = SynchedEntityData.defineId(BloxBugEntity.class, EntityDataSerializers.INT);

    boolean fromBlock;

    public static final int IDLE = 0,
                            SPAWN = 1,
                            SPAWN_BLOCK = 2,
                            WALKING = 3,
                            JUMPING = 4,
                            CHARGE = 5,
                            JUMP_ATTACK = 6,
                            COOLDOWN = 7;

    protected static final RawAnimation IDLE_ANIM = RawAnimation.begin().thenPlay("idle");
    protected static final RawAnimation SPAWN_ANIM = RawAnimation.begin().thenPlay("spawn");
    protected static final RawAnimation SPAWN_BLOCK_ANIM = RawAnimation.begin().thenPlay("spawn_block");
    protected static final RawAnimation WALK_ANIM = RawAnimation.begin().thenLoop("walk");
    protected static final RawAnimation CHARGE_READY_ANIM = RawAnimation.begin().thenPlayAndHold("charge_ready");
    protected static final RawAnimation CHARGE_LOOP_ANIM = RawAnimation.begin().thenLoop("charge_loop");
    protected static final RawAnimation CHARGE_STOP_ANIM = RawAnimation.begin().thenPlay("charge_stop");
    protected static final RawAnimation JUMP_ANIM = RawAnimation.begin().thenPlayAndHold("jump");
    protected static final RawAnimation JUMP_ATTACK_PREPARE_ANIM = RawAnimation.begin().thenPlayAndHold("jump_attack_prepare");
    protected static final RawAnimation JUMP_ATTACK_ANIM = RawAnimation.begin().thenLoop("jump_attack");


    private final AnimatableInstanceCache geoCache = GeckoLibUtil.createInstanceCache(this);

    public BloxBugEntity(EntityType<? extends PathfinderMob> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    public BloxBugEntity(PlayMessages.SpawnEntity spawnEntity, Level world) {
        this(ModEntities.TYPE_BLOX_BUG.get(), world);
    }

    public BloxBugEntity(Level world, boolean fromBlock) {
        this(ModEntities.TYPE_BLOX_BUG.get(), world);
        this.fromBlock = fromBlock;
        if (fromBlock) {
            this.setState(SPAWN_BLOCK);
        } else {
            this.setState(SPAWN);
        }
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<GeoAnimatable>(this, "main", 0, this::animController));
        controllers.add(new AnimationController<GeoAnimatable>(this, "anims", 0, state -> PlayState.STOP)
                .triggerableAnim("idle", IDLE_ANIM)
                .triggerableAnim("spawn", SPAWN_ANIM)
                .triggerableAnim("spawn_block", SPAWN_BLOCK_ANIM)
                .triggerableAnim("walk", WALK_ANIM)
                .triggerableAnim("charge_ready", CHARGE_READY_ANIM)
                .triggerableAnim("charge_loop", CHARGE_LOOP_ANIM)
                .triggerableAnim("charge_stop", CHARGE_STOP_ANIM)
                .triggerableAnim("jump", JUMP_ANIM)
                .triggerableAnim("jump_attack_prepare", JUMP_ATTACK_PREPARE_ANIM)
                .triggerableAnim("jump_attack", JUMP_ATTACK_ANIM)
        );
    }

    protected <E extends BloxBugEntity> PlayState animController(final AnimationState<GeoAnimatable> state) {
        switch (getState()) {
            case SPAWN_BLOCK -> {
                state.getController().setAnimation(SPAWN_BLOCK_ANIM);
                return PlayState.CONTINUE;
            }
            case WALKING -> {
                //KingdomKeys.LOGGER.debug("WALK");
                state.getController().setAnimation(WALK_ANIM);
                return PlayState.CONTINUE;
            }
            case CHARGE -> {
                //KingdomKeys.LOGGER.debug("CHARGE");
                state.getController().setAnimation(CHARGE_LOOP_ANIM);
                return PlayState.CONTINUE;
            }
            case JUMP_ATTACK -> {
                //KingdomKeys.LOGGER.debug("JUMP ATTACK");
                state.getController().setAnimation(JUMP_ATTACK_ANIM);
                return PlayState.CONTINUE;
            }
            case JUMPING -> {
                //KingdomKeys.LOGGER.debug("JUMP");
                state.getController().setAnimation(JUMP_ANIM);
                return PlayState.CONTINUE;
            }
            case IDLE -> {
                //KingdomKeys.LOGGER.debug("IDLE");
                state.getController().setAnimation(IDLE_ANIM);
                return PlayState.CONTINUE;
            }
        }
        //KingdomKeys.LOGGER.debug(getState());
        return PlayState.STOP;
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.geoCache;
    }

    @Override
    protected void registerGoals() {
        MoveTowardsTargetGoal moveTowardsTargetGoal = new MoveTowardsTargetGoal(this, 1, 35);
        WaterAvoidingRandomStrollGoal waterAvoidingRandomStrollGoal = new WaterAvoidingRandomStrollGoal(this, 1.0D);
        BloxBugGoal bloxBugGoal = new BloxBugGoal(this);
        this.goalSelector.addGoal(7, moveTowardsTargetGoal);
        this.goalSelector.addGoal(9, waterAvoidingRandomStrollGoal);
        this.goalSelector.addGoal(8, new RandomLookAroundGoal(this));
        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, Player.class, false));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Villager.class, true));
        this.goalSelector.addGoal(3, bloxBugGoal);
        waterAvoidingRandomStrollGoal.setFlags(EnumSet.of(Goal.Flag.MOVE));
        bloxBugGoal.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.JUMP));
    }

    public static AttributeSupplier.Builder registerAttributes() {
        return PathfinderMob.createLivingAttributes()
                .add(Attributes.FOLLOW_RANGE, 35)
                .add(Attributes.MOVEMENT_SPEED, 0.4D)
                .add(Attributes.MAX_HEALTH, 60)
                .add(Attributes.ATTACK_DAMAGE, 3)
                .add(Attributes.ATTACK_KNOCKBACK, 3);

    }

    int spawnBlockTicks = 0;

    @Override
    public void tick() {
        super.tick();
        if (getState() == SPAWN_BLOCK) {
            spawnBlockTicks++;
        }
        if (spawnBlockTicks > 31) {
            spawnBlockTicks = 0;
            setState(IDLE);
        }
        if (getState() == WALKING || getState() == IDLE) {
            if ((xOld != getX() || zOld != getZ()) && onGround()) {
                setState(WALKING);
            } else {
                setState(IDLE);
            }
        }
        if (getState() == JUMPING && !jumping) {
            setState(WALKING);
        }

        if (jumping) {
            setState(JUMPING);
        }
        if (getTarget() == null) {
            if (getState() == CHARGE || getState() == JUMP_ATTACK) {
                setState(IDLE);
                stopTriggeredAnimation("anims", "charge_loop");
            }
            if (getState() == COOLDOWN) {
                //setState(IDLE);
                //stopTriggeredAnimation("anims", "charge_stop");
            }
        }
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(STATE, IDLE);
    }

    @Override
    public EntityHelper.MobType getKHMobType() {
        return EntityHelper.MobType.HEARTLESS_EMBLEM;
    }

    @Override
    public int getDefense() {
        return 0;
    }

    public void setState(int i) {
        entityData.set(STATE, i);
    }

    public int getState() {
        return entityData.get(STATE);
    }

    @Override
    public void writeSpawnData(FriendlyByteBuf friendlyByteBuf) {
        friendlyByteBuf.writeBoolean(fromBlock);
    }

    @Override
    public void readSpawnData(FriendlyByteBuf friendlyByteBuf) {
        fromBlock = friendlyByteBuf.readBoolean();
    }

    public static class BloxBugGoal extends Goal {

        BloxBugEntity instance;

        public BloxBugGoal(BloxBugEntity instance) {
            this.instance = instance;
        }

        @Override
        public boolean canUse() {
            return true;
        }

        Vec3 targetPosCache, targetDirection;
        int attackTicks;
        int cooldownTicks;
        int prepareTicks;
        final int attackDelay = 60;
        final int attackDelayVariance = 40; //+ or - attackDelay
        final int chargeDistance = 10;
        final int cooldownLength = 10;
        final int prepareLength = 10;
        float startDist;
        int nextAttack = attackDelay + Utils.randomWithRange(-attackDelayVariance, attackDelayVariance);

        @Override
        public void start() {
            super.start();
        }

        @Override
        public boolean requiresUpdateEveryTick() {
            return true;
        }

        @Override
        public boolean canContinueToUse() {
            LivingEntity target = instance.getTarget();
            switch (instance.getState()) {
                case WALKING, IDLE -> {
                    if (target != null) {
                        attackTicks++;
                        if (attackTicks > nextAttack) {
                            targetDirection = new Vec3((float) (target.position().x() - instance.position().x()), (float) (target.position().y() - instance.position().y()), (float) (target.position().z() - instance.position().z())).normalize();
                            //higher = jump attack
                            if (target.position().y > instance.position().y || !instance.isFree(targetDirection.x, targetDirection.y, targetDirection.z)) {
                                instance.setState(JUMP_ATTACK);
                                instance.triggerAnim("anims", "jump");
                                attackTicks = 0;
                                prepareTicks = 8;
                                nextAttack = attackDelay + Utils.randomWithRange(-attackDelayVariance, attackDelayVariance);
                                //lower or same level = charge attack
                            } else if (target.position().y <= instance.position().y && instance.isFree(targetDirection.x, targetDirection.y, targetDirection.z) && target.distanceTo(instance) < chargeDistance * 2) {
                                instance.setState(CHARGE);
                                instance.triggerAnim("anims", "charge_ready");
                                attackTicks = 0;
                                prepareTicks = prepareLength;
                                instance.lookAt(EntityAnchorArgument.Anchor.EYES, target.position());
                                nextAttack = attackDelay + Utils.randomWithRange(-attackDelayVariance, attackDelayVariance);
                            }
                        } else {
                            if (target.closerThan(instance, 2)) {
                                target.hurt(instance.damageSources().mobAttack(instance), (float) instance.getAttributeValue(Attributes.ATTACK_DAMAGE));
                                instance.triggerAnim("anims", "charge_loop");
                            } else {
                                instance.stopTriggeredAnimation("anims", "charge_loop");
                            }
                        }
                    }
                }
                case CHARGE -> {
                    if (prepareTicks <= 0) {
                        if (instance.position().y < targetPosCache.y || instance.walkDist - startDist >= chargeDistance || !instance.isFree(targetDirection.x, targetDirection.y, targetDirection.z)) {
                            //stop at target
                            instance.setState(COOLDOWN);
                            cooldownTicks = cooldownLength;
                            instance.stopTriggeredAnimation("anims", "charge_loop");
                            instance.triggerAnim("anims", "charge_stop");
                            targetPosCache = null;
                        } else {
                            instance.triggerAnim("anims", "charge_loop");
                            instance.setDeltaMovement(new Vec3(targetDirection.x, instance.getDeltaMovement().y, targetDirection.z).scale(2));
                            instance.level().getEntities(instance, instance.getBoundingBox().inflate(2)).forEach(entity ->  {
                                entity.hurt(instance.damageSources().mobAttack(instance), (float) instance.getAttributeValue(Attributes.ATTACK_DAMAGE) + 4);
                                entity.push(targetDirection.x * 7, 0.3, targetDirection.z * 7);
                            });
                        }
                    } else {
                        if (target != null) {
                            targetPosCache = target.position();
                        }
                        if (targetPosCache != null) {
                            targetDirection = new Vec3((float) (targetPosCache.x() - instance.position().x()), 0, (float) (targetPosCache.z() - instance.position().z())).normalize();
                            if (!instance.isFree(targetDirection.x, targetDirection.y, targetDirection.z) && targetPosCache.distanceTo(instance.position()) > chargeDistance*2) {
                                instance.setState(IDLE);
                                instance.stopTriggeredAnimation("anims", "charge_ready");
                            }
                            prepareTicks--;
                            instance.lookAt(EntityAnchorArgument.Anchor.EYES, targetPosCache);
                            if (prepareTicks <= 0) {
                                startDist = instance.walkDist;
                            }
                        }
                    }
                }
                case JUMP_ATTACK -> {
                    if (prepareTicks <= 0) {
                        if (!instance.isFree(targetDirection.x, targetDirection.y, targetDirection.z)) {
                            //stop at target
                            cooldownTicks = 4;
                            instance.setState(COOLDOWN);
                            instance.resetFallDistance();
                            targetPosCache = null;
                            instance.setNoGravity(false);
                            instance.level().getEntities(instance, instance.getBoundingBox().inflate(3)).forEach(entity -> entity.hurt(instance.damageSources().mobAttack(instance), (float) instance.getAttributeValue(Attributes.ATTACK_DAMAGE) + 8));
                        } else {
                            instance.setNoGravity(false);
                            instance.setDeltaMovement(new Vec3(targetDirection.x, targetDirection.y > 1 ? 1 : targetDirection.y, targetDirection.z).scale(2));
                            instance.lookAt(EntityAnchorArgument.Anchor.EYES, targetPosCache);
                            instance.resetFallDistance();
                            instance.level().getEntities(instance, instance.getBoundingBox().inflate(2)).forEach(entity -> entity.hurt(instance.damageSources().mobAttack(instance), (float) (instance.getAttributeValue(Attributes.ATTACK_DAMAGE) + 8)));
                        }
                    } else {
                        if (target != null) {
                            targetPosCache = target.position();
                        }
                        targetDirection = new Vec3((float) (targetPosCache.x() - instance.position().x()), (float) (targetPosCache.y() - instance.position().y()), (float) (targetPosCache.z() - instance.position().z())).normalize();
                        prepareTicks--;
                        instance.setNoGravity(true);
                        instance.lookAt(EntityAnchorArgument.Anchor.EYES, targetPosCache);
                        instance.lookControl.setLookAt(targetPosCache);
                        if (prepareTicks >= 4) {
                            instance.setDeltaMovement(new Vec3(0, 1, 0));
                        }
                        if (prepareTicks < 4) {
                            instance.triggerAnim("anims", "jump_attack_prepare");
                            instance.setDeltaMovement(new Vec3(0, 0, 0));
                        }
                        if (prepareTicks <= 0) {;
                            instance.triggerAnim("anims", "jump_attack");
                        }
                    }
                }
                case COOLDOWN -> {
                    cooldownTicks--;
                    if (cooldownTicks <= 0) {
                        instance.setState(IDLE);
                        instance.stopTriggeredAnimation("anims", "jump_attack");
                        instance.stopTriggeredAnimation("anims", "charge_stop");
                    }
                }
            }

            if (target != null) {
                if (instance.getState() != IDLE && instance.getState() != WALKING) {
                    instance.goalSelector.getAvailableGoals().forEach(wrappedGoal -> {
                        if (wrappedGoal.getGoal().getClass() == WaterAvoidingRandomStrollGoal.class || wrappedGoal.getGoal().getClass() == RandomLookAroundGoal.class || wrappedGoal.getGoal().getClass() == MoveTowardsTargetGoal.class) {
                            wrappedGoal.stop();
                            instance.getNavigation().stop();
                        }
                    });
                } else {
                    instance.goalSelector.getAvailableGoals().forEach(wrappedGoal -> {
                        if (wrappedGoal.getGoal().getClass() == WaterAvoidingRandomStrollGoal.class || wrappedGoal.getGoal().getClass() == RandomLookAroundGoal.class || wrappedGoal.getGoal().getClass() == MoveTowardsTargetGoal.class) {
                            if (wrappedGoal.canUse()) {
                                wrappedGoal.start();
                            }
                        }
                    });
                }
            }
            if (targetPosCache == null && target == null) {
                instance.setNoGravity(false);
                instance.setState(IDLE);
                instance.goalSelector.getAvailableGoals().forEach(wrappedGoal -> {
                    if (wrappedGoal.getGoal().getClass() == WaterAvoidingRandomStrollGoal.class || wrappedGoal.getGoal().getClass() == RandomLookAroundGoal.class) {
                        if (wrappedGoal.canUse()) {
                            wrappedGoal.start();
                        }
                    }
                });
            }
            return true;
        }
    }
}
