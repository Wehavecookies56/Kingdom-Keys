package online.kingdomkeys.kingdomkeys.entity.mob;

import net.minecraft.entity.CreatureEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.FMLPlayMessages;
import online.kingdomkeys.kingdomkeys.capability.ModCapabilities;
import online.kingdomkeys.kingdomkeys.entity.EntityHelper;
import online.kingdomkeys.kingdomkeys.entity.ModEntities;
import online.kingdomkeys.kingdomkeys.entity.SeedBulletEntity;

//TODO fix seed bullet
public class DirePlantEntity extends MonsterEntity implements IKHMob {

    public DirePlantEntity(EntityType<? extends MonsterEntity> type, World worldIn) {
        super(type, worldIn);
    }

    public DirePlantEntity(FMLPlayMessages.SpawnEntity spawnEntity, World world) {
        super(ModEntities.TYPE_DIRE_PLANT.get(), world);
    }
    
    @Override
    public boolean canSpawn(IWorld worldIn, SpawnReason spawnReasonIn) {
    	return ModCapabilities.getWorld((World)worldIn).getHeartlessSpawnLevel() > 0;
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new MeleeAttackGoal(this, 1.0D, false));
        this.goalSelector.addGoal(0, new SeedGoal(this));
        this.goalSelector.addGoal(1, new LookRandomlyGoal(this));
        this.targetSelector.addGoal(0, new HurtByTargetGoal(this));
        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, PlayerEntity.class, true));
    }

    @Override
    protected void registerAttributes() {
        super.registerAttributes();
        this.getAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(35.0D);
        this.getAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.0D);
        this.getAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(40.0D);
        this.getAttribute(SharedMonsterAttributes.KNOCKBACK_RESISTANCE).setBaseValue(1000.0D);
        this.getAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(2.0D);
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
        return EntityHelper.MobType.HEARTLESS_EMBLEM;
    }

    class SeedGoal extends TargetGoal {
        private boolean canUseAttack = true;
        private int attackTimer = 30;

        public SeedGoal(DirePlantEntity e) {
        	super(e, true);
        }

        @Override
        public boolean shouldExecute() {
            if(goalOwner.getAttackTarget() != null) {
                if(!canUseAttack) {
                    if(attackTimer > 0) {
                        attackTimer--;
                        return false;
                    }
                    else return true;
                }
                else return true;
            }
            else return false;
        }

        @Override
        public boolean shouldContinueExecuting() {
            boolean flag = canUseAttack;

            return flag;
        }

        @Override
        public void startExecuting() {
            canUseAttack = true;
            attackTimer = 30;
            EntityHelper.setState(goalOwner, 0);
        }

        @Override
        public void tick() {
            if(goalOwner.getAttackTarget() != null && canUseAttack	) {
                EntityHelper.setState(goalOwner, 1);
                LivingEntity target = this.goalOwner.getAttackTarget();

                this.goalOwner.getLookController().setLookPositionWithEntity(target, 30F, 30F);
                double d1 = this.goalOwner.getAttackTarget().getPosX() - this.goalOwner.getPosX();
                double d2 = this.goalOwner.getAttackTarget().getPosY() - this.goalOwner.getPosY();//getBoundingBox().minY + (double)(this.goalOwner.getAttackTarget().getHeight() / 2.0F) - (this.goalOwner.getPosY() + (double)(this.goalOwner.getHeight() / 2.0F));
                double d3 = this.goalOwner.getAttackTarget().getPosZ() - this.goalOwner.getPosZ();
                if(world.rand.nextInt(100) + 1 > EntityHelper.percentage(25, 100)) {

                    SeedBulletEntity seed = new SeedBulletEntity(this.goalOwner, this.goalOwner.world);
                    seed.shoot(d1, d2, d3, 1.2F, 0);
                    seed.setPosition(seed.getPosX(), this.goalOwner.getPosY() + (double)(this.goalOwner.getHeight() / 2.0F) + 0.3D, seed.getPosZ());
                    this.goalOwner.world.addEntity(seed);
                }
                else {
                    SeedBulletEntity seed = new SeedBulletEntity(this.goalOwner, this.goalOwner.world);
                    seed.shoot(d1, d2, d3, 1.2F, 0);
                    seed.setPosition(seed.getPosX(), this.goalOwner.getPosY() + (double)(this.goalOwner.getHeight() / 2.0F) + 0.3D, seed.getPosZ());
                    this.goalOwner.world.addEntity(seed);

                    seed = new SeedBulletEntity(this.goalOwner, this.goalOwner.world);
                    seed.shoot(d1, d2, d3, 0.7F, 0);
                    seed.setPosition(seed.getPosX(), this.goalOwner.getPosY() + (double)(this.goalOwner.getHeight() / 2.0F) + 0.3D, seed.getPosZ());
                    this.goalOwner.world.addEntity(seed);

                    seed = new SeedBulletEntity(this.goalOwner, this.goalOwner.world);
                    seed.shoot(d1, d2, d3, 0.5F, 0);
                    seed.setPosition(seed.getPosX(), this.goalOwner.getPosY() + (double)(this.goalOwner.getHeight() / 2.0F) + 0.3D, seed.getPosZ());
                    this.goalOwner.world.addEntity(seed);
                }

                canUseAttack = false;
            }
        }

    }
}
