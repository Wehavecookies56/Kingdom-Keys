package online.kingdomkeys.kingdomkeys.entity.mob;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.PlayMessages;
import online.kingdomkeys.kingdomkeys.capability.ModCapabilities;
import online.kingdomkeys.kingdomkeys.client.sound.ModSounds;
import online.kingdomkeys.kingdomkeys.entity.EntityHelper;
import online.kingdomkeys.kingdomkeys.entity.ModEntities;

public class LargeBodyEntity extends BaseKHEntity {

    enum SpecialAttack {
        WAIT,
        CHARGE,
        MOWDOWN,
        SHOCKWAVE;
    }

    private SpecialAttack currentAttack, previousAttack;
    private int timeForNextAI = 80;
    private boolean isAngry = false;

    protected final int
            DAMAGE_HIT = 0,
            DAMAGE_CHARGE = 6,
            DAMAGE_MOWDOWN = 5,
            DAMAGE_SHOCKWAVE = 4;

    public LargeBodyEntity(EntityType<? extends Monster> type, Level worldIn) {
        super(type, worldIn);
    }

    public LargeBodyEntity(PlayMessages.SpawnEntity spawnEntity, Level world) {
        super(ModEntities.TYPE_LARGE_BODY.get(), world);
    }
    
    @Override
    public boolean checkSpawnRules(LevelAccessor worldIn, MobSpawnType spawnReasonIn) {
    	return ModCapabilities.getWorld((Level)worldIn).getHeartlessSpawnLevel() > 0;
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new MeleeAttackGoal(this, 1.0D, false));
        this.goalSelector.addGoal(0, new ChargeGoal(this));
        this.goalSelector.addGoal(1, new MowdownGoal(this));
        this.goalSelector.addGoal(1, new FloatGoal(this));
        this.goalSelector.addGoal(3, new RandomStrollGoal(this, 1.0D));
        this.goalSelector.addGoal(4, new RandomLookAroundGoal(this));
        this.targetSelector.addGoal(0, new HurtByTargetGoal(this));
        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, Player.class, true));
		this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Villager.class, true));

    }

    public static AttributeSupplier.Builder registerAttributes() {
        return Mob.createLivingAttributes()
                .add(Attributes.MAX_HEALTH, 100.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.15D)
                .add(Attributes.FOLLOW_RANGE, 35.0D)
                .add(Attributes.ATTACK_DAMAGE, 5.0D)
				.add(Attributes.ATTACK_KNOCKBACK, 1.0D)

                ;
    }

    @Override
    public EntityHelper.MobType getKHMobType() {
        return EntityHelper.MobType.HEARTLESS_EMBLEM;
    }

    @Override
    public void tick() {
        super.tick();

        int rotation = Mth.floor(this.getYHeadRot() * 4.0F / 360.0F + 0.5D) & 3;

        if(this.getHealth() < this.getMaxHealth()/3)
            this.isAngry = true;

        if(this.getCurrentAttackState() == SpecialAttack.MOWDOWN)
            this.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(0);

        if(this.getPreviousAttackState() != SpecialAttack.WAIT && timeForNextAI > 0) {
            this.setCurrentAttackState(SpecialAttack.WAIT);
            this.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(0);
            this.setTarget(null);
            EntityHelper.setState(this, 10);
            timeForNextAI--;
        }
        else if(timeForNextAI <= 0) {
            this.setPreviousAttackState(SpecialAttack.WAIT);
            this.setCurrentAttackState(null);
            if(this.isAngry)
                this.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(0.20);
            else
                this.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(0.15);
            EntityHelper.setState(this, 0);
            this.setTarget(null);
            timeForNextAI = 80;
        }

        if (this.isAngry) {
            //TODO particles
            //KingdomKeys.proxy.spawnDarkSmokeParticle(world, getPosX(), getPosY() + 1, getPosZ(), 0, 0.01D, 0, 0.8F);
        }

    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
    	if(source.getEntity() instanceof LivingEntity) {
    		Entity attacker = source.getDirectEntity();
    		double d1 = attacker.getX() - this.getX();
            double d0 = attacker.getZ() - this.getZ();
            float attackYaw = (float)Math.toDegrees((Mth.atan2(d0, d1)));// Global degree the attack is coming from
            float diff = Mth.wrapDegrees(attackYaw-getYRot());

    		if(diff > 30 && diff < 150) {
    			if(attacker instanceof LivingEntity) {
	                ((LivingEntity) attacker).knockback(0.8F, -d1, -d0);
					level.playSound(null, blockPosition(), ModSounds.invincible_hit.get(), SoundSource.PLAYERS, 1F, 1F);
	                attacker.setDeltaMovement(attacker.getDeltaMovement().x, 0.5F, attacker.getDeltaMovement().z);
    			}
                return false;    		
    		}
    	}
    	return super.hurt(source, amount);
    }

    
    /*public boolean attackEntityAsMob(Entity ent) {
        int i = 0;
        float j = 1;

        if(EntityHelper.getState(this) == 0) i = DAMAGE_HIT;
        else if(EntityHelper.getState(this) == 1) i = DAMAGE_CHARGE;
        else if(EntityHelper.getState(this) == 2) i = DAMAGE_MOWDOWN;
        else if(EntityHelper.getState(this) == 3) i = DAMAGE_SHOCKWAVE;

        if(this.isAngry)
            j = 1.5F;

        return ent.attackEntityFrom(DamageSource.causeMobDamage(this), i * j);
    }*/

    public Level getWorld() {
        return this.level;
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


    @Override
    public float getScale() {
    	return 2;
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

    class MowdownGoal extends Goal {
        private LargeBodyEntity theEntity;
        private boolean canUseAttack = true;
        private final int ATTACK_MAX_TIMER = 50;
        private int attackTimer = ATTACK_MAX_TIMER, whileAttackTimer;
        private double[] posToCharge;
        private float initialHealth;

        public MowdownGoal(LargeBodyEntity e)
        {
            this.theEntity = e;
        }

        @Override
        public boolean canUse() {
            if(theEntity.getTarget() != null && this.theEntity.getCurrentAttackState() == null && theEntity.distanceTo(theEntity.getTarget()) < 5) {
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
            if(this.theEntity.getPreviousAttackState() == SpecialAttack.MOWDOWN) {
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

            return flag; //(theEntity.getAttackTarget() != null && theEntity.getDistanceSqToEntity(this.theEntity.getAttackTarget()) < 400) ||
        }

        @Override
        public void start() {
            canUseAttack = true;
            attackTimer = ATTACK_MAX_TIMER;
            whileAttackTimer = 0;
            this.theEntity.setCurrentAttackState(SpecialAttack.MOWDOWN);
            EntityHelper.setState(theEntity, 2);
            initialHealth = theEntity.getHealth();
        }

        @Override
        public void tick() {
            if(theEntity.getTarget() != null && canUseAttack) {
                whileAttackTimer++;

                for(Entity t : EntityHelper.getEntitiesNear(this.theEntity, 0.2)) {
                	theEntity.doHurtTarget(t);
                    //t.attackEntityFrom(DamageSource.causeMobDamage(this.theEntity), theEntity.isAngry ? this.theEntity.DAMAGE_MOWDOWN * 1.5f : this.theEntity.DAMAGE_MOWDOWN);
                }

                if(whileAttackTimer > 40)
                    canUseAttack = false;

                if(initialHealth > theEntity.getHealth())
                    canUseAttack = false;
            }
        }
    }

    class ChargeGoal extends Goal {
        private LargeBodyEntity theEntity;
        private boolean canUseAttack = true;
        private final int ATTACK_MAX_TIMER = 70;
        private int attackTimer = ATTACK_MAX_TIMER, whileAttackTimer;
        private double[] posToCharge;
        private float initialHealth;

        public ChargeGoal(LargeBodyEntity e) {
            this.theEntity = e;
        }

        @Override
        public boolean canUse() {
            if(theEntity.getTarget() != null && this.theEntity.getCurrentAttackState() == null && theEntity.distanceToSqr(theEntity.getTarget()) > 4) {
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
                if(theEntity.random.nextFloat() <= 0.2f) return true;
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
            attackTimer = ATTACK_MAX_TIMER;
            whileAttackTimer = 0;
            this.theEntity.setCurrentAttackState(SpecialAttack.CHARGE);
            EntityHelper.setState(theEntity, 1);
            LivingEntity target = this.theEntity.getTarget();
            initialHealth = theEntity.getHealth();

            if(target != null)
                posToCharge = new double[] {target.getX(), target.getY(), target.getZ()};
        }

        @Override
        public void tick() {
            if(theEntity.getTarget() != null && canUseAttack) {
                whileAttackTimer++;
                LivingEntity target = this.theEntity.getTarget();
                this.theEntity.getNavigation().moveTo(posToCharge[0], posToCharge[1], posToCharge[2], theEntity.isAngry ? 2.3D : 2.0D);

                if(whileAttackTimer > 70)
                    canUseAttack = false;

                if((theEntity.blockPosition().getX() == (int)posToCharge[0] && theEntity.blockPosition().getY() == (int)posToCharge[1] && theEntity.blockPosition().getZ() == (int)posToCharge[2])
                        || (theEntity.blockPosition().getX() == (int)posToCharge[0] + 1 && theEntity.blockPosition().getY() == (int)posToCharge[1] && theEntity.blockPosition().getZ() == (int)posToCharge[2] + 1)
                        || (theEntity.blockPosition().getX() == (int)posToCharge[0] - 1 && theEntity.blockPosition().getY() == (int)posToCharge[1] && theEntity.blockPosition().getZ() == (int)posToCharge[2] - 1))
                    canUseAttack = false;

                if(theEntity.distanceToSqr(this.theEntity.getTarget()) < 2)
                    canUseAttack = false;

                if(initialHealth > theEntity.getHealth())
                    canUseAttack = false;
            }
        }
    }

}
