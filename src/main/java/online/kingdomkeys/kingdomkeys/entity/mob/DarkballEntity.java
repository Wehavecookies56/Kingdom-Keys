package online.kingdomkeys.kingdomkeys.entity.mob;

import java.util.Random;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraftforge.network.PlayMessages;
import online.kingdomkeys.kingdomkeys.capability.ModCapabilities;
import online.kingdomkeys.kingdomkeys.entity.EntityHelper;
import online.kingdomkeys.kingdomkeys.entity.ModEntities;

public class DarkballEntity extends BaseKHEntity {


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

    public DarkballEntity(EntityType<? extends Monster> type, Level worldIn) {
        super(type, worldIn);
    }

    public DarkballEntity(PlayMessages.SpawnEntity spawnEntity, Level world) {
        super(ModEntities.TYPE_DARKBALL.get(), world);
    }

    @Override
    public boolean checkSpawnRules(LevelAccessor worldIn, MobSpawnType spawnReasonIn) {
    	return worldIn == null ? false : ModCapabilities.getWorld((Level)worldIn).getHeartlessSpawnLevel() > 0;
    }
    
    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new MeleeAttackGoal(this, 1.0D, false));
        this.goalSelector.addGoal(0, new DarkCloudGoal(this));
        this.goalSelector.addGoal(1, new ChargeGoal(this));
        this.goalSelector.addGoal(1, new BerserkGoal(this));
        this.goalSelector.addGoal(1, new FloatGoal(this));
        this.goalSelector.addGoal(3, new RandomStrollGoal(this, 1.0D));
        this.goalSelector.addGoal(4, new RandomLookAroundGoal(this));
        this.targetSelector.addGoal(0, new HurtByTargetGoal(this));
		this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, Player.class, true));
		this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Villager.class, true));

    }

    public static AttributeSupplier.Builder registerAttributes() {
        return Mob.createLivingAttributes()
                .add(Attributes.FOLLOW_RANGE, 35.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.22D)
                .add(Attributes.MAX_HEALTH, 70.0D)
                .add(Attributes.ATTACK_DAMAGE, 1.0D)
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
        return EntityHelper.MobType.HEARTLESS_PUREBLOOD;
    }

    @Override
    public boolean isIgnoringBlockTriggers() {
        return true;
    }

    @Override
    public boolean causeFallDamage(float pFallDistance, float pMultiplier, DamageSource pSource) {
        return false;
    }

    protected void customServerAiStep() {
       // LivingEntity target = this.getAttackTarget();

        this.setInvulnerable(false);
        super.customServerAiStep();
    }

    /*public boolean attackEntityAsMob(Entity ent) {
        int i;
        if(EntityHelper.getState(this) == 0)
        	i = DAMAGE_HIT;
        else if(EntityHelper.getState(this) == 1) 
        	i = DAMAGE_CHARGE;
        else if(EntityHelper.getState(this) == 2) 
        	i = DAMAGE_BERSERK;
        else if(EntityHelper.getState(this) == 3)
        	i = DAMAGE_HIT;
        else 
        	i = 0;
        return ent.attackEntityFrom(DamageSource.causeMobDamage(this), i);
    }*/

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
        public boolean canUse() {
            if(theEntity.getTarget() != null && this.theEntity.getCurrentAttackState() == null) {
                if(!canUseAttack) {
                    if(attackTimer > 0) {
                        attackTimer-=2;
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
                if(theEntity.random.nextFloat() <= 0.3f) return true;
                else return false;
            }
            return true;
        }

        @Override
        public boolean canContinueToUse() {
            boolean flag = canUseAttack;

            if(!flag) {
                this.theEntity.setPreviousAttackState(this.theEntity.getCurrentAttackState());
                this.theEntity.setCurrentAttackState(null);
                EntityHelper.setState(theEntity, 0);
                for(Entity p : EntityHelper.getEntitiesNear(theEntity, 1.4)) {
                	theEntity.doHurtTarget(p);
                   // p.attackEntityFrom(DamageSource.causeMobDamage(theEntity), theEntity.DAMAGE_DARKCLOUD);
                }
            }

            return flag;
        }

        @Override
        public void start() {
            canUseAttack = true;
            attackTimer = 50;
            whileAttackTimer = 0;
            this.theEntity.setCurrentAttackState(SpecialAttack.DARKCLOUD);
            EntityHelper.setState(theEntity, 0);
        }

        @Override
        public void tick() {
            if(theEntity.getTarget() != null && canUseAttack) {
                whileAttackTimer+=2;
                
                EntityHelper.setState(theEntity, 3);
                LivingEntity target = this.theEntity.getTarget();
                for (int i = 0; i < 20; i++) {
                    double offsetX = (new Random().nextInt(5) + 1.0D + 5.0D) - 5.0D; //3
                    double offsetY = (new Random().nextInt(5) + 1.0D + 5.0D) - 5.0D;
                    double offsetZ = (new Random().nextInt(5) + 1.0D + 5.0D) - 5.0D;

                    this.theEntity.getNavigation().moveTo(target.blockPosition().getX(), target.blockPosition().getY(), target.blockPosition().getZ(), 1.5D);
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
        public boolean canUse() {
            if(theEntity.getTarget() != null && this.theEntity.getCurrentAttackState() == null && theEntity.distanceToSqr(theEntity.getTarget()) < 15) {
                if(!canUseAttack) {
                    if(attackTimer > 0) {
                        attackTimer-=2;
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
                if(theEntity.random.nextFloat() <= 0.5f) return true;
                else return false;
            }
            return true;
        }

        @Override
        public boolean canContinueToUse() {
            boolean flag = canUseAttack;

            if(!flag) {
                this.theEntity.setPreviousAttackState(this.theEntity.getCurrentAttackState());
                this.theEntity.setCurrentAttackState(null);
                EntityHelper.setState(theEntity, 0);
            }

            return flag;
        }

        @Override
        public void start() {
            canUseAttack = true;
            attackTimer = 70;
            whileAttackTimer = 0;
            this.theEntity.setCurrentAttackState(SpecialAttack.BERSERK);
            EntityHelper.setState(theEntity, 0);
            pivotPosToBerserk = new double[] {theEntity.blockPosition().getX(), theEntity.blockPosition().getY(), theEntity.blockPosition().getZ()};
        }

        @Override
        public void tick() {
            if(theEntity.getTarget() != null && canUseAttack) {
                whileAttackTimer+=2;
                
                EntityHelper.setState(theEntity, 2);
                for (int i = 0; i < 20; i++) {
                    double offsetX = (new Random().nextInt(5) + 1.0D + 5.0D) - 5.0D; //3
                    double offsetY = (new Random().nextInt(5) + 1.0D + 5.0D) - 5.0D;
                    double offsetZ = (new Random().nextInt(5) + 1.0D + 5.0D) - 5.0D;

                    LivingEntity target = this.theEntity.getTarget();
                    this.theEntity.getNavigation().moveTo(pivotPosToBerserk[0] + offsetX, pivotPosToBerserk[1] + offsetY, pivotPosToBerserk[2] + offsetZ, 5.0D);
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
        public boolean canUse() {
            if(theEntity.getTarget() != null && this.theEntity.getCurrentAttackState() == null && theEntity.distanceToSqr(theEntity.getTarget()) > 4) {
                if(!canUseAttack) {
                    if(attackTimer > 0) {
                        attackTimer-=2;
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
                if(theEntity.random.nextFloat() <= 0.1f) return true;
                else return false;
            }
            return true;
        }

        @Override
        public boolean canContinueToUse() {
            boolean flag = canUseAttack;

            if(!flag) {
                this.theEntity.setPreviousAttackState(this.theEntity.getCurrentAttackState());
                this.theEntity.setCurrentAttackState(null);
                EntityHelper.setState(theEntity, 0);
            }

            return flag; //(theEntity.getAttackTarget() != null && theEntity.getDistanceSqToEntity(this.theEntity.getAttackTarget()) < 400) ||
        }

        @Override
        public void start() {
            canUseAttack = true;
            attackTimer = 50;
            whileAttackTimer = 0;
            this.theEntity.setCurrentAttackState(SpecialAttack.CHARGE);
            EntityHelper.setState(theEntity, 0);
            LivingEntity target = this.theEntity.getTarget();
            initialHealth = theEntity.getHealth();

            if(target != null)
                posToCharge = new double[] { target.getX(), target.getY(), target.getZ() };
        }

        @Override
        public void tick() {
            if(theEntity.getTarget() != null && canUseAttack) {
                whileAttackTimer+=2;
                
                EntityHelper.setState(theEntity, 1);
                LivingEntity target = this.theEntity.getTarget();
                this.theEntity.getNavigation().moveTo(posToCharge[0], posToCharge[1], posToCharge[2], 3.0D);

                if(whileAttackTimer > 100)
                    canUseAttack = false;

                if(theEntity.blockPosition().getX() == (int)posToCharge[0] && theEntity.blockPosition().getY() == (int)posToCharge[1] && theEntity.blockPosition().getZ() == (int)posToCharge[2])
                    canUseAttack = false;

                if(theEntity.distanceToSqr(this.theEntity.getTarget()) < 3)
                    canUseAttack = false;

                if(initialHealth > theEntity.getHealth())
                    canUseAttack = false;
            }
        }
    }
}
