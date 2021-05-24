package online.kingdomkeys.kingdomkeys.entity.mob;

import java.util.Random;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.ai.goal.HurtByTargetGoal;
import net.minecraft.entity.ai.goal.LookRandomlyGoal;
import net.minecraft.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.entity.ai.goal.NearestAttackableTargetGoal;
import net.minecraft.entity.ai.goal.RandomWalkingGoal;
import net.minecraft.entity.ai.goal.SwimGoal;
import net.minecraft.entity.merchant.villager.VillagerEntity;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.FMLPlayMessages;
import online.kingdomkeys.kingdomkeys.capability.ModCapabilities;
import online.kingdomkeys.kingdomkeys.entity.EntityHelper;
import online.kingdomkeys.kingdomkeys.entity.ModEntities;

public class DarkballEntity extends MonsterEntity implements IKHMob {

    enum SpecialAttack {
        CHARGE,
        BERSERK,
        DARKCLOUD;
    }

    private SpecialAttack currentAttack, previousAttack;

    protected final int
            DAMAGE_HIT = 0,
            DAMAGE_CHARGE = 6,
            DAMAGE_BERSERK = 5,
            DAMAGE_DARKCLOUD = 4;

    public DarkballEntity(EntityType<? extends MonsterEntity> type, World worldIn) {
        super(type, worldIn);
    }

    public DarkballEntity(FMLPlayMessages.SpawnEntity spawnEntity, World world) {
        super(ModEntities.TYPE_DARKBALL.get(), world);
    }

    @Override
    public boolean canSpawn(IWorld worldIn, SpawnReason spawnReasonIn) {
    	return ModCapabilities.getWorld((World)worldIn).getHeartlessSpawnLevel() > 0;
    }
    
    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new MeleeAttackGoal(this, 1.0D, false));
        this.goalSelector.addGoal(0, new DarkCloudGoal(this));
        this.goalSelector.addGoal(1, new ChargeGoal(this));
        this.goalSelector.addGoal(1, new BerserkGoal(this));
        this.goalSelector.addGoal(1, new SwimGoal(this));
        this.goalSelector.addGoal(3, new RandomWalkingGoal(this, 1.0D));
        this.goalSelector.addGoal(4, new LookRandomlyGoal(this));
        this.targetSelector.addGoal(0, new HurtByTargetGoal(this));
		this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, PlayerEntity.class, true));
		this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, VillagerEntity.class, true));

    }

    public static AttributeModifierMap.MutableAttribute registerAttributes() {
        return MobEntity.registerAttributes()
                .createMutableAttribute(Attributes.FOLLOW_RANGE, 35.0D)
                .createMutableAttribute(Attributes.MOVEMENT_SPEED, 0.22D)
                .createMutableAttribute(Attributes.MAX_HEALTH, 90.0D)
                .createMutableAttribute(Attributes.ATTACK_DAMAGE, 1.0D)
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
        return EntityHelper.MobType.HEARTLESS_PUREBLOOD;
    }

    @Override
    public boolean doesEntityNotTriggerPressurePlate() {
        return true;
    }

    @Override
    public boolean onLivingFall(float distance, float damageMultiplier) {
        return false;
    }

    protected void updateAITasks() {
        LivingEntity target = this.getAttackTarget();

        if(EntityHelper.getState(this) == 3)
            this.setInvulnerable(true);
        else
            this.setInvulnerable(false);

        super.updateAITasks();
    }

    public boolean attackEntityAsMob(Entity ent) {
        int i;
        if(EntityHelper.getState(this) == 0) i = DAMAGE_HIT;
        else if(EntityHelper.getState(this) == 1) i = DAMAGE_CHARGE;
        else if(EntityHelper.getState(this) == 2) i = DAMAGE_BERSERK;
        else if(EntityHelper.getState(this) == 3) i = DAMAGE_HIT;
        else i = 0;
        return ent.attackEntityFrom(DamageSource.causeMobDamage(this), i);
    }

    public void setCurrentAttackState(SpecialAttack state) {
        this.currentAttack = state;
    }

    public SpecialAttack getCurrentAttackState() {
        return this.currentAttack;
    }

    public void setPreviousAttackState(SpecialAttack state) {
        this.previousAttack = state;
    }

    public SpecialAttack getPreviousAttackState() {
        return this.previousAttack;
    }

    class DarkCloudGoal extends Goal {
        private DarkballEntity theEntity;
        private boolean canUseAttack = true;
        private int attackTimer = 50, whileAttackTimer;
        private double[] pivotPosToMove;

        public DarkCloudGoal(DarkballEntity e) {
            this.theEntity = e;
        }

        @Override
        public boolean shouldExecute() {
            if(theEntity.getAttackTarget() != null && this.theEntity.getCurrentAttackState() == null) {
                if(!canUseAttack) {
                    if(attackTimer > 0) {
                        attackTimer--;
                        return false;
                    }
                    else return prevAttackCalc();
                }
                else return prevAttackCalc();
            }
            else return false;
        }

        public boolean prevAttackCalc() {
            if(this.theEntity.getPreviousAttackState() == SpecialAttack.DARKCLOUD) {
                if(theEntity.rand.nextFloat() <= 0.3f) return true;
                else return false;
            }
            return true;
        }

        @Override
        public boolean shouldContinueExecuting() {
            boolean flag = canUseAttack;

            if(!flag) {
                this.theEntity.setPreviousAttackState(this.theEntity.getCurrentAttackState());
                this.theEntity.setCurrentAttackState(null);
                EntityHelper.setState(theEntity, 0);
                for(Entity p : EntityHelper.getEntitiesNear(theEntity, 1.4)) {
                    p.attackEntityFrom(DamageSource.causeMobDamage(theEntity), theEntity.DAMAGE_DARKCLOUD);
                }
            }

            return flag;
        }

        @Override
        public void startExecuting() {
            canUseAttack = true;
            attackTimer = 50;
            whileAttackTimer = 0;
            this.theEntity.setCurrentAttackState(SpecialAttack.DARKCLOUD);
            EntityHelper.setState(theEntity, 0);
        }

        @Override
        public void tick() {
            if(theEntity.getAttackTarget() != null && canUseAttack) {
                whileAttackTimer++;
                
                EntityHelper.setState(theEntity, 3);
                LivingEntity target = this.theEntity.getAttackTarget();
                for (int i = 0; i < 20; i++) {
                    double offsetX = (new Random().nextInt(5) + 1.0D + 5.0D) - 5.0D; //3
                    double offsetY = (new Random().nextInt(5) + 1.0D + 5.0D) - 5.0D;
                    double offsetZ = (new Random().nextInt(5) + 1.0D + 5.0D) - 5.0D;

                    this.theEntity.getNavigator().tryMoveToXYZ(target.getPosition().getX(), target.getPosition().getY(), target.getPosition().getZ(), 1.5D);
                    //TODO particles
                    //KingdomKeys.proxy.spawnDarkSmokeParticle(world, getPosX(), getPosY() + 1, getPosZ(), 0, 0.01D, 0, 0.8F);
                }

                for(Entity p : EntityHelper.getEntitiesNear(theEntity, 1))
                    if(p == target)
                        canUseAttack = false;

                if(whileAttackTimer > 80)
                    canUseAttack = false;
            }
        }

    }


    class BerserkGoal extends Goal {
        private DarkballEntity theEntity;
        private boolean canUseAttack = true;
        private int attackTimer = 70, whileAttackTimer;
        private double[] pivotPosToBerserk;

        public BerserkGoal(DarkballEntity e) {
            this.theEntity = e;
        }

        @Override
        public boolean shouldExecute() {
            if(theEntity.getAttackTarget() != null && this.theEntity.getCurrentAttackState() == null && theEntity.getDistanceSq(theEntity.getAttackTarget()) < 15) {
                if(!canUseAttack) {
                    if(attackTimer > 0) {
                        attackTimer--;
                        return false;
                    }
                    else return prevAttackCalc();
                }
                else return prevAttackCalc();
            }
            else return false;
        }

        public boolean prevAttackCalc() {
            if(this.theEntity.getPreviousAttackState() == SpecialAttack.BERSERK) {
                if(theEntity.rand.nextFloat() <= 0.5f) return true;
                else return false;
            }
            return true;
        }

        @Override
        public boolean shouldContinueExecuting() {
            boolean flag = canUseAttack;

            if(!flag) {
                this.theEntity.setPreviousAttackState(this.theEntity.getCurrentAttackState());
                this.theEntity.setCurrentAttackState(null);
                EntityHelper.setState(theEntity, 0);
            }

            return flag;
        }

        @Override
        public void startExecuting() {
            canUseAttack = true;
            attackTimer = 70;
            whileAttackTimer = 0;
            this.theEntity.setCurrentAttackState(SpecialAttack.BERSERK);
            EntityHelper.setState(theEntity, 0);
            pivotPosToBerserk = new double[] {theEntity.getPosition().getX(), theEntity.getPosition().getY(), theEntity.getPosition().getZ()};
        }

        @Override
        public void tick() {
            if(theEntity.getAttackTarget() != null && canUseAttack) {
                whileAttackTimer++;
                
                EntityHelper.setState(theEntity, 2);
                for (int i = 0; i < 20; i++) {
                    double offsetX = (new Random().nextInt(5) + 1.0D + 5.0D) - 5.0D; //3
                    double offsetY = (new Random().nextInt(5) + 1.0D + 5.0D) - 5.0D;
                    double offsetZ = (new Random().nextInt(5) + 1.0D + 5.0D) - 5.0D;

                    LivingEntity target = this.theEntity.getAttackTarget();
                    this.theEntity.getNavigator().tryMoveToXYZ(pivotPosToBerserk[0] + offsetX, pivotPosToBerserk[1] + offsetY, pivotPosToBerserk[2] + offsetZ, 5.0D);
                    //TODO particles
                    //KingdomKeys.proxy.spawnDarkSmokeParticle(world, getPosX(), getPosY() + 1, getPosZ(), 0, 0.01D, 0, 0.5F);
                }

                if(whileAttackTimer > 40)
                    canUseAttack = false;
            }
        }

    }

    class ChargeGoal extends Goal {
        private DarkballEntity theEntity;
        private boolean canUseAttack = true;
        private int attackTimer = 50, whileAttackTimer;
        private double[] posToCharge;
        private float initialHealth;

        public ChargeGoal(DarkballEntity e) {
            this.theEntity = e;
        }

        @Override
        public boolean shouldExecute() {
            if(theEntity.getAttackTarget() != null && this.theEntity.getCurrentAttackState() == null && theEntity.getDistanceSq(theEntity.getAttackTarget()) > 4) {
                if(!canUseAttack) {
                    if(attackTimer > 0) {
                        attackTimer--;
                        return false;
                    }
                    else return prevAttackCalc();
                }
                else return prevAttackCalc();
            }
            else return false;
        }

        public boolean prevAttackCalc() {
            if(this.theEntity.getPreviousAttackState() == SpecialAttack.CHARGE) {
                if(theEntity.rand.nextFloat() <= 0.1f) return true;
                else return false;
            }
            return true;
        }

        @Override
        public boolean shouldContinueExecuting() {
            boolean flag = canUseAttack;

            if(!flag) {
                this.theEntity.setPreviousAttackState(this.theEntity.getCurrentAttackState());
                this.theEntity.setCurrentAttackState(null);
                EntityHelper.setState(theEntity, 0);
            }

            return flag; //(theEntity.getAttackTarget() != null && theEntity.getDistanceSqToEntity(this.theEntity.getAttackTarget()) < 400) ||
        }

        @Override
        public void startExecuting() {
            canUseAttack = true;
            attackTimer = 50;
            whileAttackTimer = 0;
            this.theEntity.setCurrentAttackState(SpecialAttack.CHARGE);
            EntityHelper.setState(theEntity, 0);
            LivingEntity target = this.theEntity.getAttackTarget();
            initialHealth = theEntity.getHealth();

            if(target != null)
                posToCharge = new double[] { target.getPosX(), target.getPosY(), target.getPosZ() };
        }

        @Override
        public void tick() {
            if(theEntity.getAttackTarget() != null && canUseAttack) {
                whileAttackTimer++;
                
                EntityHelper.setState(theEntity, 1);
                LivingEntity target = this.theEntity.getAttackTarget();
                this.theEntity.getNavigator().tryMoveToXYZ(posToCharge[0], posToCharge[1], posToCharge[2], 3.0D);

                if(whileAttackTimer > 100)
                    canUseAttack = false;

                if(theEntity.getPosition().getX() == (int)posToCharge[0] && theEntity.getPosition().getY() == (int)posToCharge[1] && theEntity.getPosition().getZ() == (int)posToCharge[2])
                    canUseAttack = false;

                if(theEntity.getDistanceSq(this.theEntity.getAttackTarget()) < 3)
                    canUseAttack = false;

                if(initialHealth > theEntity.getHealth())
                    canUseAttack = false;
            }
        }
    }
}
