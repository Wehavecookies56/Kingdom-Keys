package online.kingdomkeys.kingdomkeys.entity.mob;

import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.goal.target.TargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.PlayMessages;
import online.kingdomkeys.kingdomkeys.entity.EntityHelper;
import online.kingdomkeys.kingdomkeys.entity.ModEntities;
import online.kingdomkeys.kingdomkeys.entity.SeedBulletEntity;

//TODO fix seed bullet
public class DirePlantEntity extends BaseKHEntity {

    public DirePlantEntity(EntityType<? extends Monster> type, Level worldIn) {
        super(type, worldIn);
        xpReward = 6;
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
       // this.goalSelector.addGoal(0, new MeleeAttackGoal(this, 1.0D, false));
        this.goalSelector.addGoal(0, new SeedGoal(this));
        this.goalSelector.addGoal(1, new RandomLookAroundGoal(this));
        this.targetSelector.addGoal(0, new HurtByTargetGoal(this));
        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, Player.class, true));
		this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Villager.class, true));

    }
    
    public static AttributeSupplier.Builder registerAttributes() {
        return Mob.createLivingAttributes()
                .add(Attributes.FOLLOW_RANGE, 35.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.0D)
                .add(Attributes.MAX_HEALTH, 30.0D)
                .add(Attributes.KNOCKBACK_RESISTANCE, 1000.0D)
                .add(Attributes.ATTACK_DAMAGE, 1.0D)
				.add(Attributes.ATTACK_KNOCKBACK, 1.0D)
                ;
    }

    @Override
    public int getMaxSpawnClusterSize() {
        return 4;
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder pBuilder) {
        pBuilder.define(EntityHelper.STATE, 0);
    }

    @Override
    public EntityHelper.MobType getKHMobType() {
        return EntityHelper.MobType.HEARTLESS_EMBLEM;
    }

    class SeedGoal extends TargetGoal {
        private boolean canUseAttack = true;
        private int attackTimer = 30;

        public SeedGoal(DirePlantEntity e) {
        	super(e, true);
        }

        @Override
        public boolean canUse() {
            if(mob.getTarget() != null) {
                if(!canUseAttack) {
                    if(attackTimer > 0) {
                        attackTimer-=2;
                        return false;
                    }
                    else return true;
                }
                else return true;
            }
            else return false;
        }

        @Override
        public boolean canContinueToUse() {
            return canUseAttack;
        }

        @Override
        public void start() {
            canUseAttack = true;
            attackTimer = 30;
            EntityHelper.setState(mob, 0);
        }

        @Override
        public void tick() {
            if(mob.getTarget() != null && canUseAttack) {
                EntityHelper.setState(mob, 1);
                LivingEntity target = this.mob.getTarget();
                this.mob.getLookControl().setLookAt(target, 30F, 30F);
                double d1 = this.mob.getTarget().getX() - this.mob.getX();
                double d2 = this.mob.getTarget().getY() - this.mob.getY();//getBoundingBox().minY + (double)(this.goalOwner.getAttackTarget().getHeight() / 2.0F) - (this.goalOwner.getPosY() + (double)(this.goalOwner.getHeight() / 2.0F));
                double d3 = this.mob.getTarget().getZ() - this.mob.getZ();
                
                int num = level().random.nextInt(100) + 1;
                
                if(num < 30) { //Single
                    SeedBulletEntity seed = new SeedBulletEntity(this.mob, this.mob.level());
                    seed.setPos(seed.getX(), this.mob.getY() + (double)(this.mob.getBbHeight() / 2.0F) + 0.3D, seed.getZ());
                    seed.shoot(d1, d2, d3, 1.2F, 0);
                    level().addFreshEntity(seed);
                    
                } else if(num < 60) { //Vertical
                	SeedBulletEntity seed = new SeedBulletEntity(this.mob, this.mob.level());
                    seed.shoot(d1, d2+1, d3, 1F, 0);
                    seed.setPos(seed.getX(), this.mob.getY() + (double)(this.mob.getBbHeight() / 2.0F) + 0.3D, seed.getZ());
                    mob.level().addFreshEntity(seed);

                    seed = new SeedBulletEntity(this.mob, this.mob.level());
                    seed.shoot(d1, d2+2, d3, 1F, 0);
                    seed.setPos(seed.getX(), this.mob.getY() + (double)(this.mob.getBbHeight() / 2.0F) + 0.3D, seed.getZ());
                    mob.level().addFreshEntity(seed);

                    seed = new SeedBulletEntity(this.mob, this.mob.level());
                    seed.shoot(d1, d2, d3, 1F, 0);
                    seed.setPos(seed.getX(), this.mob.getY() + (double)(this.mob.getBbHeight() / 2.0F) + 0.3D, seed.getZ());
                    mob.level().addFreshEntity(seed);
                    
                } else { //Triple
                    SeedBulletEntity seed = new SeedBulletEntity(this.mob, this.mob.level());
                    seed.shoot(d1, d2, d3, 1.2F, 0);
                    seed.setPos(seed.getX(), this.mob.getY() + (double)(this.mob.getBbHeight() / 2.0F) + 0.3D, seed.getZ());
                    mob.level().addFreshEntity(seed);

                    seed = new SeedBulletEntity(this.mob, this.mob.level());
                    seed.shoot(d1, d2, d3, 0.7F, 0);
                    seed.setPos(seed.getX(), this.mob.getY() + (double)(this.mob.getBbHeight() / 2.0F) + 0.3D, seed.getZ());
                    mob.level().addFreshEntity(seed);

                    seed = new SeedBulletEntity(this.mob, this.mob.level());
                    seed.shoot(d1, d2, d3, 0.5F, 0);
                    seed.setPos(seed.getX(), this.mob.getY() + (double)(this.mob.getBbHeight() / 2.0F) + 0.3D, seed.getZ());
                    mob.level().addFreshEntity(seed);
                    

                }
                
                canUseAttack = false;
            } else {
            	
            }
            
        }

    }
}
