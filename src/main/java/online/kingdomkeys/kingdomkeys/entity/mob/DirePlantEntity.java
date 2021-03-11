package online.kingdomkeys.kingdomkeys.entity.mob;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.goal.HurtByTargetGoal;
import net.minecraft.entity.ai.goal.LookRandomlyGoal;
import net.minecraft.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.entity.ai.goal.NearestAttackableTargetGoal;
import net.minecraft.entity.ai.goal.TargetGoal;
import net.minecraft.entity.merchant.villager.VillagerEntity;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.entity.player.PlayerEntity;
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
        super.registerGoals();
       // this.goalSelector.addGoal(0, new MeleeAttackGoal(this, 1.0D, false));
        this.goalSelector.addGoal(0, new SeedGoal(this));
        this.goalSelector.addGoal(1, new LookRandomlyGoal(this));
        this.targetSelector.addGoal(0, new HurtByTargetGoal(this));
        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, PlayerEntity.class, true));
		this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, VillagerEntity.class, true));

    }
    
    public static AttributeModifierMap.MutableAttribute registerAttributes() {
        return MobEntity.registerAttributes()
                .createMutableAttribute(Attributes.FOLLOW_RANGE, 35.0D)
                .createMutableAttribute(Attributes.MOVEMENT_SPEED, 0.0D)
                .createMutableAttribute(Attributes.MAX_HEALTH, 40.0D)
                .createMutableAttribute(Attributes.KNOCKBACK_RESISTANCE, 1000.0D)
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
            return canUseAttack;
        }

        @Override
        public void startExecuting() {
            canUseAttack = true;
            attackTimer = 30;
            EntityHelper.setState(goalOwner, 0);
        }

        @Override
        public void tick() {
            if(goalOwner.getAttackTarget() != null && canUseAttack) {
                EntityHelper.setState(goalOwner, 1);
                LivingEntity target = this.goalOwner.getAttackTarget();
                this.goalOwner.getLookController().setLookPositionWithEntity(target, 30F, 30F);
                double d1 = this.goalOwner.getAttackTarget().getPosX() - this.goalOwner.getPosX();
                double d2 = this.goalOwner.getAttackTarget().getPosY() - this.goalOwner.getPosY();//getBoundingBox().minY + (double)(this.goalOwner.getAttackTarget().getHeight() / 2.0F) - (this.goalOwner.getPosY() + (double)(this.goalOwner.getHeight() / 2.0F));
                double d3 = this.goalOwner.getAttackTarget().getPosZ() - this.goalOwner.getPosZ();
                System.out.println("attack");
                int num = world.rand.nextInt(100) + 1;
                System.out.println(num);
                if(num < 30) { //Single
                    SeedBulletEntity seed = new SeedBulletEntity(this.goalOwner, this.goalOwner.world);
                    seed.setPosition(seed.getPosX(), this.goalOwner.getPosY() + (double)(this.goalOwner.getHeight() / 2.0F) + 0.3D, seed.getPosZ());
                    seed.shoot(d1, d2, d3, 1.2F, 0);
                    world.addEntity(seed);
                    System.out.println("Single");
                } else if(num < 60) { //Vertical
                	SeedBulletEntity seed = new SeedBulletEntity(this.goalOwner, this.goalOwner.world);
                    seed.shoot(d1, d2+1, d3, 1F, 0);
                    seed.setPosition(seed.getPosX(), this.goalOwner.getPosY() + (double)(this.goalOwner.getHeight() / 2.0F) + 0.3D, seed.getPosZ());
                    goalOwner.world.addEntity(seed);

                    seed = new SeedBulletEntity(this.goalOwner, this.goalOwner.world);
                    seed.shoot(d1, d2+2, d3, 1F, 0);
                    seed.setPosition(seed.getPosX(), this.goalOwner.getPosY() + (double)(this.goalOwner.getHeight() / 2.0F) + 0.3D, seed.getPosZ());
                    goalOwner.world.addEntity(seed);

                    seed = new SeedBulletEntity(this.goalOwner, this.goalOwner.world);
                    seed.shoot(d1, d2, d3, 1F, 0);
                    seed.setPosition(seed.getPosX(), this.goalOwner.getPosY() + (double)(this.goalOwner.getHeight() / 2.0F) + 0.3D, seed.getPosZ());
                    goalOwner.world.addEntity(seed);
                    System.out.println("Vert");
                } else { //Triple
                    SeedBulletEntity seed = new SeedBulletEntity(this.goalOwner, this.goalOwner.world);
                    seed.shoot(d1, d2, d3, 1.2F, 0);
                    seed.setPosition(seed.getPosX(), this.goalOwner.getPosY() + (double)(this.goalOwner.getHeight() / 2.0F) + 0.3D, seed.getPosZ());
                    goalOwner.world.addEntity(seed);

                    seed = new SeedBulletEntity(this.goalOwner, this.goalOwner.world);
                    seed.shoot(d1, d2, d3, 0.7F, 0);
                    seed.setPosition(seed.getPosX(), this.goalOwner.getPosY() + (double)(this.goalOwner.getHeight() / 2.0F) + 0.3D, seed.getPosZ());
                    goalOwner.world.addEntity(seed);

                    seed = new SeedBulletEntity(this.goalOwner, this.goalOwner.world);
                    seed.shoot(d1, d2, d3, 0.5F, 0);
                    seed.setPosition(seed.getPosX(), this.goalOwner.getPosY() + (double)(this.goalOwner.getHeight() / 2.0F) + 0.3D, seed.getPosZ());
                    goalOwner.world.addEntity(seed);
                    System.out.println("Triple");

                }
                System.out.println("USed attack");
                canUseAttack = false;
            } else {
            	System.out.println("I would've crashed");
            }
            
        }

    }
}
