package online.kingdomkeys.kingdomkeys.entity.mob;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.ai.goal.TargetGoal;
import net.minecraft.entity.effect.LightningBoltEntity;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.network.FMLPlayMessages;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.entity.EntityHelper;
import online.kingdomkeys.kingdomkeys.entity.ModEntities;
import online.kingdomkeys.kingdomkeys.entity.magic.ThunderBoltEntity;

public class YellowOperaEntity extends BaseElementalMusicalHeartlessEntity {


    public YellowOperaEntity(EntityType<? extends MonsterEntity> type, World worldIn) {
        super(type, worldIn);
    }

    public YellowOperaEntity(FMLPlayMessages.SpawnEntity spawnEntity, World world) {
        super(ModEntities.TYPE_YELLOW_OPERA.get(), spawnEntity, world);
    }

    @Override
    protected Goal goalToUse() {
        return new YellowOperaGoal(this);
    }

    public static AttributeModifierMap.MutableAttribute registerAttributes() {
        return BaseElementalMusicalHeartlessEntity.registerAttributes().createMutableAttribute(Attributes.MAX_HEALTH, 40.0D);
    }

    @Override
    public Element getElementToUse() {
        return Element.THUNDER;
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public ResourceLocation getTexture() {
        return new ResourceLocation(KingdomKeys.MODID, "textures/entity/mob/yellow_opera.png");
    }

    @Override
    public boolean attackEntityFrom(DamageSource source, float amount) {
    	float multiplier = 1;
        if(!this.world.isRemote) {
            //if(source.getImmediateSource() instanceof BlizzardEntity)
              //  multiplier = 2;
            if(source.getImmediateSource() instanceof ThunderBoltEntity)
            	return false;
        }
        return super.attackEntityFrom(source, amount * multiplier);
    }

    class YellowOperaGoal extends TargetGoal {
        private boolean canUseAttack = true;
        private int attackTimer = 5, whileAttackTimer;
        private float initialHealth;

        public YellowOperaGoal(YellowOperaEntity e) {
        	super(e,true);
        }

        @Override
        public boolean shouldExecute() {
            if (goalOwner.getAttackTarget() != null) {
                if (!canUseAttack) {
                    if (attackTimer > 0) {
                        attackTimer--;
                        return false;
                    } else
                        return true;
                } else
                    return true;
            } else
                return false;
        }

        @Override
        public boolean shouldContinueExecuting() {
            boolean flag = canUseAttack;

            return flag;
        }

        @Override
        public void startExecuting() {
            canUseAttack = true;
            attackTimer = 25 + world.rand.nextInt(5);
            EntityHelper.setState(goalOwner, 0);
            this.goalOwner.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(0.20D);
            whileAttackTimer = 0;
            initialHealth = goalOwner.getHealth();
        }

        @Override
        public void tick() {
            if (goalOwner.getAttackTarget() != null && canUseAttack) {
                whileAttackTimer++;
                LivingEntity target = this.goalOwner.getAttackTarget();

                if (EntityHelper.getState(goalOwner) == 0) {
                    this.goalOwner.getLookController().setLookPositionWithEntity(target, 30F, 30F);
                    if (world.rand.nextInt(100) + world.rand.nextDouble() <= 45 && this.goalOwner.getDistance(target) < 10) {
                        EntityHelper.setState(this.goalOwner, 1);

                        this.goalOwner.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(0.0D);
                        this.goalOwner.getLookController().setLookPositionWithEntity(target, 0F, 0F);
                        if(!world.isRemote) {
                            LightningBoltEntity lightningboltentity = EntityType.LIGHTNING_BOLT.create(world);
                            lightningboltentity.moveForced(target.getPositionVec());
                            world.addEntity(lightningboltentity);
                        }
                    } else {
                        if (world.rand.nextInt(100) + world.rand.nextDouble() <= 50) {
                            if (goalOwner.getDistance(goalOwner.getAttackTarget()) < 8) {
                                EntityHelper.setState(this.goalOwner, 2);

                                this.goalOwner.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(0.0D);

                                for (LivingEntity enemy : EntityHelper.getEntitiesNear(this.goalOwner, 4))
                                    enemy.attackEntityFrom(DamageSource.causeMobDamage(this.goalOwner), 4);
                            } else
                                return;
                        } else {
                            EntityHelper.setState(this.goalOwner, 3);

                            this.goalOwner.getLookController().setLookPositionWithEntity(target, 30F, 30F);
                            this.goalOwner.getNavigator().tryMoveToXYZ(target.getPosX(), target.getPosY(), target.getPosZ(), 3.0D);

                            for (LivingEntity enemy : EntityHelper.getEntitiesNear(this.goalOwner, 3))
                                enemy.attackEntityFrom(DamageSource.causeMobDamage(this.goalOwner), 4);
                        }
                    }

                }

                if (EntityHelper.getState(goalOwner) == 3) {
                    if (whileAttackTimer > 50)
                        canUseAttack = false;

                    if (goalOwner.getPosition().getX() == (int) target.getPosX() && goalOwner.getPosition().getY() == (int) target.getPosY() && goalOwner.getPosition().getZ() == (int) target.getPosZ())
                        canUseAttack = false;

                    if (goalOwner.getDistanceSq(this.goalOwner.getAttackTarget()) < 3)
                        canUseAttack = false;

                    if (initialHealth > goalOwner.getHealth())
                        canUseAttack = false;
                }

                if (EntityHelper.getState(goalOwner) == 2 && whileAttackTimer > 20) {
                    canUseAttack = false;
                    EntityHelper.setState(goalOwner, 0);
                    this.goalOwner.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(0.20D);
                }
                else if (EntityHelper.getState(goalOwner) == 1 && whileAttackTimer > 50) {
                    canUseAttack = false;
                    EntityHelper.setState(goalOwner, 0);
                    this.goalOwner.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(0.20D);
                }
            }
        }

    }

}
