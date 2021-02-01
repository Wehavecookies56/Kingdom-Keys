package online.kingdomkeys.kingdomkeys.entity.mob;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntitySize;
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
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.FMLPlayMessages;
import online.kingdomkeys.kingdomkeys.capability.ModCapabilities;
import online.kingdomkeys.kingdomkeys.entity.EntityHelper;
import online.kingdomkeys.kingdomkeys.entity.ModEntities;

public class LargeBodyEntity extends MonsterEntity implements IKHMob {

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

    public LargeBodyEntity(EntityType<? extends MonsterEntity> type, World worldIn) {
        super(type, worldIn);
    }

    public LargeBodyEntity(FMLPlayMessages.SpawnEntity spawnEntity, World world) {
        super(ModEntities.TYPE_LARGE_BODY.get(), world);
    }
    
    @Override
    public boolean canSpawn(IWorld worldIn, SpawnReason spawnReasonIn) {
    	return ModCapabilities.getWorld((World)worldIn).getHeartlessSpawnLevel() > 0;
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new MeleeAttackGoal(this, 1.0D, false));
        this.goalSelector.addGoal(0, new ChargeGoal(this));
        this.goalSelector.addGoal(1, new MowdownGoal(this));
        this.goalSelector.addGoal(1, new SwimGoal(this));
        this.goalSelector.addGoal(3, new RandomWalkingGoal(this, 1.0D));
        this.goalSelector.addGoal(4, new LookRandomlyGoal(this));
        this.targetSelector.addGoal(0, new HurtByTargetGoal(this));
        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, PlayerEntity.class, true));
    }

    public static AttributeModifierMap.MutableAttribute registerAttributes() {
        return MobEntity.registerAttributes()
                .createMutableAttribute(Attributes.MAX_HEALTH, 100.0D)
                .createMutableAttribute(Attributes.MOVEMENT_SPEED, 0.15D)
                .createMutableAttribute(Attributes.FOLLOW_RANGE, 35.0D)
                .createMutableAttribute(Attributes.ATTACK_DAMAGE, 1.0D)
				.createMutableAttribute(Attributes.ATTACK_KNOCKBACK, 1.0D)

                ;
    }

    @Override
    public EntityHelper.MobType getMobType() {
        return EntityHelper.MobType.HEARTLESS_EMBLEM;
    }

    @Override
    public void tick() {
        super.tick();

        int rotation = MathHelper.floor(this.getRotationYawHead() * 4.0F / 360.0F + 0.5D) & 3;

        if(this.getHealth() < this.getMaxHealth()/3)
            this.isAngry = true;

        if(this.getCurrentAttackState() == SpecialAttack.MOWDOWN)
            this.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(0);

        if(this.getPreviousAttackState() != SpecialAttack.WAIT && timeForNextAI > 0) {
            this.setCurrentAttackState(SpecialAttack.WAIT);
            this.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(0);
            this.setAttackTarget(null);
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
            this.setAttackTarget(null);
            timeForNextAI = 80;
        }

        if (this.isAngry) {
            //TODO particles
            //KingdomKeys.proxy.spawnDarkSmokeParticle(world, getPosX(), getPosY() + 1, getPosZ(), 0, 0.01D, 0, 0.8F);
        }

    }

    @Override
    public boolean attackEntityFrom(DamageSource source, float amount) {
    	if(source.getTrueSource() instanceof LivingEntity) {
    		Entity attacker = source.getImmediateSource();
    		double d1 = attacker.getPosX() - this.getPosX();
            double d0 = attacker.getPosZ() - this.getPosZ();
            float attackYaw = (float)Math.toDegrees((MathHelper.atan2(d0, d1)));// Global degree the attack is coming from
            float diff = MathHelper.wrapDegrees(attackYaw-rotationYaw);

    		if(diff > 30 && diff < 150) {
    			if(attacker instanceof LivingEntity) {
	                ((LivingEntity) attacker).applyKnockback(0.8F, -d1, -d0);
	                attacker.setMotion(attacker.getMotion().x, 0.5F, attacker.getMotion().z);
    			}
                return false;    		
    		}
    	}
    	return super.attackEntityFrom(source, amount);
    }

    
    public boolean attackEntityAsMob(Entity ent) {
        int i = 0;
        float j = 1;

        if(EntityHelper.getState(this) == 0) i = DAMAGE_HIT;
        else if(EntityHelper.getState(this) == 1) i = DAMAGE_CHARGE;
        else if(EntityHelper.getState(this) == 2) i = DAMAGE_MOWDOWN;
        else if(EntityHelper.getState(this) == 3) i = DAMAGE_SHOCKWAVE;

        if(this.isAngry)
            j = 1.5F;

        return ent.attackEntityFrom(DamageSource.causeMobDamage(this), i * j);
    }

    public World getWorld() {
        return this.world;
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
    public float getRenderScale() {
    	return 2;
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
        public boolean shouldExecute() {
            if(theEntity.getAttackTarget() != null && this.theEntity.getCurrentAttackState() == null && theEntity.getDistance(theEntity.getAttackTarget()) < 5) {
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

            return flag; //(theEntity.getAttackTarget() != null && theEntity.getDistanceSqToEntity(this.theEntity.getAttackTarget()) < 400) ||
        }

        @Override
        public void startExecuting() {
            canUseAttack = true;
            attackTimer = ATTACK_MAX_TIMER;
            whileAttackTimer = 0;
            this.theEntity.setCurrentAttackState(SpecialAttack.MOWDOWN);
            EntityHelper.setState(theEntity, 2);
            initialHealth = theEntity.getHealth();
        }

        @Override
        public void tick() {
            if(theEntity.getAttackTarget() != null && canUseAttack) {
                whileAttackTimer++;

                for(Entity t : EntityHelper.getEntitiesNear(this.theEntity, 0.2)) {
                    t.attackEntityFrom(DamageSource.causeMobDamage(this.theEntity), theEntity.isAngry ? this.theEntity.DAMAGE_MOWDOWN * 1.5f : this.theEntity.DAMAGE_MOWDOWN);
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
                if(theEntity.rand.nextFloat() <= 0.2f) return true;
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
            attackTimer = ATTACK_MAX_TIMER;
            whileAttackTimer = 0;
            this.theEntity.setCurrentAttackState(SpecialAttack.CHARGE);
            EntityHelper.setState(theEntity, 1);
            LivingEntity target = this.theEntity.getAttackTarget();
            initialHealth = theEntity.getHealth();

            if(target != null)
                posToCharge = new double[] {target.getPosX(), target.getPosY(), target.getPosZ()};
        }

        @Override
        public void tick() {
            if(theEntity.getAttackTarget() != null && canUseAttack) {
                whileAttackTimer++;
                LivingEntity target = this.theEntity.getAttackTarget();
                this.theEntity.getNavigator().tryMoveToXYZ(posToCharge[0], posToCharge[1], posToCharge[2], theEntity.isAngry ? 2.3D : 2.0D);

                if(whileAttackTimer > 70)
                    canUseAttack = false;

                if((theEntity.getPosition().getX() == (int)posToCharge[0] && theEntity.getPosition().getY() == (int)posToCharge[1] && theEntity.getPosition().getZ() == (int)posToCharge[2])
                        || (theEntity.getPosition().getX() == (int)posToCharge[0] + 1 && theEntity.getPosition().getY() == (int)posToCharge[1] && theEntity.getPosition().getZ() == (int)posToCharge[2] + 1)
                        || (theEntity.getPosition().getX() == (int)posToCharge[0] - 1 && theEntity.getPosition().getY() == (int)posToCharge[1] && theEntity.getPosition().getZ() == (int)posToCharge[2] - 1))
                    canUseAttack = false;

                if(theEntity.getDistanceSq(this.theEntity.getAttackTarget()) < 2)
                    canUseAttack = false;

                if(initialHealth > theEntity.getHealth())
                    canUseAttack = false;
            }
        }
    }

}
