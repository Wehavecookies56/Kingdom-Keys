package online.kingdomkeys.kingdomkeys.entity.mob;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.ai.goal.TargetGoal;
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
import online.kingdomkeys.kingdomkeys.entity.magic.BlizzardEntity;
import online.kingdomkeys.kingdomkeys.entity.magic.FireEntity;

public class RedNocturneEntity extends BaseElementalMusicalHeartlessEntity {


    public RedNocturneEntity(EntityType<? extends MonsterEntity> type, World worldIn) {
        super(type, worldIn);
    }

    public RedNocturneEntity(FMLPlayMessages.SpawnEntity spawnEntity, World world) {
        super(ModEntities.TYPE_RED_NOCTURNE.get(), spawnEntity, world);
    }

    @Override
    protected Goal goalToUse() {
        return new RedNocturneGoal(this);
    }

    public static AttributeModifierMap.MutableAttribute registerAttributes() {
        return BaseElementalMusicalHeartlessEntity.registerAttributes().createMutableAttribute(Attributes.MAX_HEALTH, 40.0D);
    }

    @Override
    public Element getElementToUse() {
        return Element.FIRE;
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public ResourceLocation getTexture() {
        return new ResourceLocation(KingdomKeys.MODID, "textures/entity/mob/red_nocturne.png");
    }

    @Override
    public boolean attackEntityFrom(DamageSource source, float amount) {
        float multiplier = 1;
        if(!this.world.isRemote) {
            if(source.getImmediateSource() instanceof BlizzardEntity)
                multiplier = 2;
            if(source.getImmediateSource() instanceof FireEntity)
            	return false;
        }
        return super.attackEntityFrom(source, amount * multiplier);
    }

    class RedNocturneGoal extends TargetGoal {
        private boolean canUseAttack = true;
        private int attackTimer = 5, whileAttackTimer;

        public RedNocturneGoal(RedNocturneEntity e) {
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
            attackTimer = 20 + world.rand.nextInt(5);
            EntityHelper.setState(goalOwner, 0);
            this.goalOwner.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(0.20D);
            whileAttackTimer = 0;
        }

        @Override
        public void tick() {
            if (goalOwner.getAttackTarget() != null && canUseAttack) {
                whileAttackTimer++;
                LivingEntity target = this.goalOwner.getAttackTarget();

                if (EntityHelper.getState(goalOwner) == 0) {
                    this.goalOwner.getLookController().setLookPositionWithEntity(target, 30F, 30F);

                    if (world.rand.nextInt(100) + world.rand.nextDouble() <= 75) {
                        EntityHelper.setState(this.goalOwner, 1);

                        this.goalOwner.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(0.0D);
                        this.goalOwner.getLookController().setLookPositionWithEntity(target, 0F, 0F);

                        double d1 = this.goalOwner.getAttackTarget().getPosX() - this.goalOwner.getPosX();
                        double d2 = this.goalOwner.getAttackTarget().getBoundingBox().minY + (double) (this.goalOwner.getAttackTarget().getHeight() / 2.0F) - (this.goalOwner.getPosY() + (double) (this.goalOwner.getHeight() / 2.0F));
                        double d3 = this.goalOwner.getAttackTarget().getPosZ() - this.goalOwner.getPosZ();
                        FireEntity esfb = new FireEntity(this.goalOwner.world, this.goalOwner, 1F);
                        esfb.shoot(d1, d2, d3, 1, 0);
                        esfb.setPosition(esfb.getPosX(), this.goalOwner.getPosY() + (double) (this.goalOwner.getHeight() / 2.0F) + 0.5D, esfb.getPosZ());
                        this.goalOwner.world.addEntity(esfb);
                    }
                    else {
                        if (goalOwner.getDistance(goalOwner.getAttackTarget()) < 8) {
                            EntityHelper.setState(this.goalOwner, 2);

                            this.goalOwner.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(0.0D);

                            for (LivingEntity enemy : EntityHelper.getEntitiesNear(this.goalOwner, 4))
                                enemy.attackEntityFrom(DamageSource.causeMobDamage(this.goalOwner), 4);
                        }
                        else
                            return;
                    }

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
