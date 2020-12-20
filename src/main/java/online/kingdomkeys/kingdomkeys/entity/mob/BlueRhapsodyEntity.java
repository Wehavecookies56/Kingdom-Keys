package online.kingdomkeys.kingdomkeys.entity.mob;

import net.minecraft.entity.CreatureEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.network.FMLPlayMessages;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.entity.EntityHelper;
import online.kingdomkeys.kingdomkeys.entity.ModEntities;
import online.kingdomkeys.kingdomkeys.entity.magic.BlizzardEntity;
import online.kingdomkeys.kingdomkeys.entity.magic.FireEntity;

public class BlueRhapsodyEntity extends BaseElementalMusicalHeartlessEntity {


    public BlueRhapsodyEntity(EntityType<? extends CreatureEntity> type, World worldIn) {
        super(type, worldIn);
    }

    public BlueRhapsodyEntity(FMLPlayMessages.SpawnEntity spawnEntity, World world) {
        super(ModEntities.TYPE_BLUE_RHAPSODY.get(), spawnEntity, world);
    }

    @Override
    protected Goal goalToUse() {
        return new BlueRhapsodyGoal(this);
    }

    @Override
    protected double getMaxHelth() {
        return 40.0D;
    }

    @Override
    public Element getElementToUse() {
        return Element.BLIZZARD;
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public ResourceLocation getTexture() {
        return new ResourceLocation(KingdomKeys.MODID, "textures/entity/mob/blue_rhapsody.png");
    }

    @Override
    public boolean attackEntityFrom(DamageSource source, float amount) {
        float multiplier = 1;
        if(!this.world.isRemote) {
            if(source.getImmediateSource() instanceof FireEntity)
                multiplier = 2;
        }
        return super.attackEntityFrom(source, amount * multiplier);
    }

    class BlueRhapsodyGoal extends Goal {
        private BlueRhapsodyEntity theEntity;
        private boolean canUseAttack = true;
        private int attackTimer = 5, whileAttackTimer;

        public BlueRhapsodyGoal(BlueRhapsodyEntity e) {
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
            attackTimer = 20 + world.rand.nextInt(5);
            EntityHelper.setState(theEntity, 0);
            this.theEntity.getAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.20D);
            whileAttackTimer = 0;
        }

        @Override
        public void tick() {
            if (theEntity.getAttackTarget() != null && canUseAttack) {
                whileAttackTimer++;
                LivingEntity target = this.theEntity.getAttackTarget();

                if (EntityHelper.getState(theEntity) == 0) {
                    this.theEntity.getLookController().setLookPositionWithEntity(target, 30F, 30F);

                    if (world.rand.nextInt(100) + world.rand.nextDouble() <= 75) {
                        EntityHelper.setState(this.theEntity, 1);

                        this.theEntity.getAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.0D);
                        this.theEntity.getLookController().setLookPositionWithEntity(target, 0F, 0F);

                        double d0 = this.theEntity.getDistanceSq(this.theEntity.getAttackTarget());
                        float f = MathHelper.sqrt(MathHelper.sqrt(d0));
                        double d1 = this.theEntity.getAttackTarget().getPosX() - this.theEntity.getPosX();
                        double d2 = this.theEntity.getAttackTarget().getBoundingBox().minY + (double) (this.theEntity.getAttackTarget().getHeight() / 2.0F) - (this.theEntity.getPosY() + (double) (this.theEntity.getHeight() / 2.0F));
                        double d3 = this.theEntity.getAttackTarget().getPosZ() - this.theEntity.getPosZ();
                        BlizzardEntity esfb = new BlizzardEntity(this.theEntity.world);
                        esfb.shoot(d1, d2, d3, 1, 0);
                        esfb.setPosition(esfb.getPosX(), this.theEntity.getPosY() + (double) (this.theEntity.getHeight() / 2.0F) + 0.5D, esfb.getPosZ());
                        this.theEntity.world.addEntity(esfb);
                    }
                    else {
                        if (theEntity.getDistance(theEntity.getAttackTarget()) < 8) {
                            EntityHelper.setState(this.theEntity, 2);

                            this.theEntity.getAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.0D);

                            for (LivingEntity enemy : EntityHelper.getEntitiesNear(this.theEntity, 4))
                                enemy.attackEntityFrom(DamageSource.causeMobDamage(this.theEntity), 4);
                        }
                        else
                            return;
                    }

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
