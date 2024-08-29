package online.kingdomkeys.kingdomkeys.entity.mob;

import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.target.TargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.Level;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.damagesource.LightningDamageSource;
import online.kingdomkeys.kingdomkeys.entity.EntityHelper;
import online.kingdomkeys.kingdomkeys.entity.ModEntities;
import online.kingdomkeys.kingdomkeys.item.KKResistanceType;
import org.joml.Vector3f;

public class YellowOperaEntity extends BaseElementalMusicalHeartlessEntity {


    public YellowOperaEntity(EntityType<? extends Monster> type, Level worldIn) {
        super(type, worldIn);
        xpReward = 6;
    }

    @Override
    protected Goal goalToUse() {
        return new YellowOperaGoal(this);
    }

    public static AttributeSupplier.Builder registerAttributes() {
        return BaseElementalMusicalHeartlessEntity.registerAttributes()
        		.add(Attributes.MAX_HEALTH, 20.0D)
                .add(Attributes.ATTACK_DAMAGE, 2.0D);
        		
    }

    @Override
    public Element getElementToUse() {
        return Element.THUNDER;
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public ResourceLocation getTexture() {
        return ResourceLocation.fromNamespaceAndPath(KingdomKeys.MODID, "textures/entity/mob/yellow_opera.png");
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        if(!this.level().isClientSide) {
            if(source.getMsgId().equals(KKResistanceType.lightning.toString())){
                extinguishFire();
                ((ServerLevel)this.level()).sendParticles(new DustParticleOptions(new Vector3f(1,1,0),1F), this.getX(), this.getY()+1, this.getZ(), 10, random.nextDouble()-0.5F,random.nextDouble()-0.5F,random.nextDouble()-0.5F, 0.0);
                return false;
            }
        }
        return super.hurt(source, amount);
    }

    class YellowOperaGoal extends TargetGoal {
        private boolean canUseAttack = true;
        private int attackTimer = 5, whileAttackTimer, shotChargeTimer = 50;
        private float initialHealth;

        public YellowOperaGoal(YellowOperaEntity e) {
        	super(e,true);
        }

        @Override
        public boolean canUse() {
            if (mob.getTarget() != null) {
                if (!canUseAttack) {
                    if (attackTimer > 0) {
                        attackTimer-=2;
                        return false;
                    } else
                        return true;
                } else
                    return true;
            } else
                return false;
        }

        @Override
        public boolean canContinueToUse() {
            return canUseAttack;
        }

        @Override
        public void start() {
            canUseAttack = true;
            attackTimer = 25 + level().random.nextInt(5);
            EntityHelper.setState(mob, 0);
            this.mob.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(0.20D);
            whileAttackTimer = 0;
            initialHealth = mob.getHealth();
        }

        @Override
        public void tick() {
            if (mob.getTarget() != null && canUseAttack) {
                whileAttackTimer+=2;
                LivingEntity target = this.mob.getTarget();

                if (EntityHelper.getState(mob) == 0) {
                    this.mob.getLookControl().setLookAt(target, 30F, 30F);
                    if (level().random.nextInt(100) <= 35 && this.mob.distanceTo(target) > 6) {
                        EntityHelper.setState(this.mob, 1);
                    } else {
                        if (level().random.nextInt(100) + level().random.nextDouble() <= 50) {
                            if (mob.distanceTo(mob.getTarget()) < 8) {
                                EntityHelper.setState(this.mob, 2);

                                this.mob.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(0.0D);

                                for (LivingEntity enemy : EntityHelper.getEntitiesNear(this.mob, 4))
                                	mob.doHurtTarget(enemy);
                            } else
                                return;
                        } else {
                            EntityHelper.setState(this.mob, 3);

                            this.mob.getLookControl().setLookAt(target, 30F, 30F);
                            this.mob.getNavigation().moveTo(target.getX(), target.getY(), target.getZ(), 3.0D);

                            for (LivingEntity enemy : EntityHelper.getEntitiesNear(this.mob, 3))
                                this.mob.doHurtTarget(enemy);
                        }
                    }

                }

                if (EntityHelper.getState(mob) == 3) {
                    if (whileAttackTimer > 50)
                        canUseAttack = false;

                    if (mob.blockPosition().getX() == (int) target.getX() && mob.blockPosition().getY() == (int) target.getY() && mob.blockPosition().getZ() == (int) target.getZ())
                        canUseAttack = false;

                    if (mob.distanceToSqr(this.mob.getTarget()) < 3)
                        canUseAttack = false;

                    if (initialHealth > mob.getHealth())
                        canUseAttack = false;
                }

                if (EntityHelper.getState(mob) == 2 && whileAttackTimer > 20) {
                    canUseAttack = false;
                    EntityHelper.setState(mob, 0);
                    this.mob.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(0.20D);
                }
                else if (EntityHelper.getState(mob) == 1) {
                    if(shotChargeTimer > 0){
                        shotChargeTimer--;
                        this.mob.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(0.0D);
                        ((ServerLevel)this.mob.level()).sendParticles(new DustParticleOptions(new Vector3f(1,1,0),1F), this.mob.getX(), this.mob.getY()+2.5, this.mob.getZ(), 1, 0,0,0, 0.0);
                    } else {
                        this.mob.getLookControl().setLookAt(target, 0F, 0F);
                        this.mob.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(0.20D);
                        if(!level().isClientSide) {
                            LightningBolt lightningboltentity = EntityType.LIGHTNING_BOLT.create(level());
                            lightningboltentity.moveTo(target.position());
                            level().addFreshEntity(lightningboltentity);
                            this.mob.doHurtTarget(target);
                        }
                        if(whileAttackTimer > 50) {
                            shotChargeTimer = 50;
                            canUseAttack = false;
                            EntityHelper.setState(mob, 0);
                            this.mob.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(0.20D);
                        }
                    }

                }
            }
        }

    }

}
