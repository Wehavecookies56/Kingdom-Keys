package online.kingdomkeys.kingdomkeys.entity.mob;

import net.minecraft.entity.CreatureEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.effect.LightningBoltEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.network.FMLPlayMessages;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.entity.EntityHelper;
import online.kingdomkeys.kingdomkeys.entity.ModEntities;

public class YellowOperaEntity extends BaseElementalMusicalHeartlessEntity {


    public YellowOperaEntity(EntityType<? extends CreatureEntity> type, World worldIn) {
        super(type, worldIn);
    }

    public YellowOperaEntity(FMLPlayMessages.SpawnEntity spawnEntity, World world) {
        super(ModEntities.TYPE_YELLOW_OPERA.get(), spawnEntity, world);
    }

    @Override
    protected Goal goalToUse() {
        return new YellowOperaGoal(this);
    }

    @Override
    protected double getMaxHelth() {
        return 40.0D;
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
        //TODO elemental weakness, aero
        //if(!this.world.isRemote) {
            //if(source.getImmediateSource() instanceof )
                //multiplier = 2;
        //}
        return super.attackEntityFrom(source, amount * multiplier);
    }

    class YellowOperaGoal extends Goal {
        private YellowOperaEntity theEntity;
        private boolean canUseAttack = true;
        private int attackTimer = 5, whileAttackTimer;
        private float initialHealth;

        public YellowOperaGoal(YellowOperaEntity e) {
            this.theEntity = e;
        }

        @Override
        public boolean shouldExecute() {
            if (theEntity.getAttackTarget() != null) {
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
            EntityHelper.setState(theEntity, 0);
            this.theEntity.getAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.20D);
            whileAttackTimer = 0;
            initialHealth = theEntity.getHealth();
        }

        @Override
        public void tick() {
            if (theEntity.getAttackTarget() != null && canUseAttack) {
                whileAttackTimer++;
                LivingEntity target = this.theEntity.getAttackTarget();

                if (EntityHelper.getState(theEntity) == 0) {
                    this.theEntity.getLookController().setLookPositionWithEntity(target, 30F, 30F);

                    if (world.rand.nextInt(100) + world.rand.nextDouble() <= 45) {
                        EntityHelper.setState(this.theEntity, 1);

                        this.theEntity.getAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.0D);
                        this.theEntity.getLookController().setLookPositionWithEntity(target, 0F, 0F);
                        if(!world.isRemote) {
	                       /* double d0 = this.theEntity.getDistanceSq(this.theEntity.getAttackTarget());
	                        float f = MathHelper.sqrt(MathHelper.sqrt(d0));
	                        double d1 = this.theEntity.getAttackTarget().getPosX() - this.theEntity.getPosX();
	                        double d2 = this.theEntity.getAttackTarget().getBoundingBox().minY + (double) (this.theEntity.getAttackTarget().getHeight() / 2.0F) - (this.theEntity.getPosY() + (double) (this.theEntity.getHeight() / 2.0F));
	                        double d3 = this.theEntity.getAttackTarget().getPosZ() - this.theEntity.getPosZ();*/

	                        LightningBoltEntity lightning = new LightningBoltEntity(this.theEntity.world, this.theEntity.getAttackTarget().getPosX(), this.theEntity.getAttackTarget().getPosY(), this.theEntity.getAttackTarget().getPosZ(), false);
	                        ((ServerWorld)world).addLightningBolt(lightning);
                        }
                    }
                    else {
                        if (world.rand.nextInt(100) + world.rand.nextDouble() <= 50) {
                            if (theEntity.getDistance(theEntity.getAttackTarget()) < 8) {
                                EntityHelper.setState(this.theEntity, 2);

                                this.theEntity.getAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.0D);

                                for (LivingEntity enemy : EntityHelper.getEntitiesNear(this.theEntity, 4))
                                    enemy.attackEntityFrom(DamageSource.causeMobDamage(this.theEntity), 4);
                            } else
                                return;
                        }
                        else {
                            EntityHelper.setState(this.theEntity, 3);

                            this.theEntity.getLookController().setLookPositionWithEntity(target, 30F, 30F);
                            this.theEntity.getNavigator().tryMoveToXYZ(target.getPosX(), target.getPosY(), target.getPosZ(), 3.0D);

                            for (LivingEntity enemy : EntityHelper.getEntitiesNear(this.theEntity, 3))
                                enemy.attackEntityFrom(DamageSource.causeMobDamage(this.theEntity), 4);
                        }
                    }

                }

                if (EntityHelper.getState(theEntity) == 3) {
                    if (whileAttackTimer > 50)
                        canUseAttack = false;

                    if (theEntity.getPosition().getX() == (int) target.getPosX() && theEntity.getPosition().getY() == (int) target.getPosY() && theEntity.getPosition().getZ() == (int) target.getPosZ())
                        canUseAttack = false;

                    if (theEntity.getDistanceSq(this.theEntity.getAttackTarget()) < 3)
                        canUseAttack = false;

                    if (initialHealth > theEntity.getHealth())
                        canUseAttack = false;
                }

                if (EntityHelper.getState(theEntity) == 2 && whileAttackTimer > 20) {
                    canUseAttack = false;
                    EntityHelper.setState(theEntity, 0);
                    this.theEntity.getAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.20D);
                }
                else if (EntityHelper.getState(theEntity) == 1 && whileAttackTimer > 50) {
                    canUseAttack = false;
                    EntityHelper.setState(theEntity, 0);
                    this.theEntity.getAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.20D);
                }
            }
        }

    }

}
