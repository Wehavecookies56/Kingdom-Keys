package online.kingdomkeys.kingdomkeys.entity.mob;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.target.TargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.network.PlayMessages;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.damagesource.IceDamageSource;
import online.kingdomkeys.kingdomkeys.damagesource.MagicDamageSource;
import online.kingdomkeys.kingdomkeys.entity.EntityHelper;
import online.kingdomkeys.kingdomkeys.entity.ModEntities;

public class GreenRequiemEntity extends BaseElementalMusicalHeartlessEntity {

    public GreenRequiemEntity(EntityType<? extends Monster> type, Level worldIn) {
        super(type, worldIn);
    }

    public GreenRequiemEntity(PlayMessages.SpawnEntity spawnEntity, Level world) {
        super(ModEntities.TYPE_GREEN_REQUIEM.get(), spawnEntity, world);
    }

    @Override
    protected Goal goalToUse() {
        return new GreenRequiemGoal(this);
    }

    public static AttributeSupplier.Builder registerAttributes() {
        return BaseElementalMusicalHeartlessEntity.registerAttributes()
        		.add(Attributes.MAX_HEALTH, 20.0D)
                .add(Attributes.ATTACK_DAMAGE, 2.0D);
    }

    @Override
    public Element getElementToUse() {
        return Element.CURE;
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public ResourceLocation getTexture() {
        return new ResourceLocation(KingdomKeys.MODID, "textures/entity/mob/green_requiem.png");
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        if(!this.level.isClientSide()) {
            if(source instanceof MagicDamageSource)
            	return false;
        }
        return super.hurt(source, amount);
    }

    class GreenRequiemGoal extends TargetGoal {
        private boolean canUseAttack = true;
        private int attackTimer = 5, whileAttackTimer;
        private float initialHealth;

        public GreenRequiemGoal(GreenRequiemEntity e) {
        	super(e, true);
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
            boolean flag = canUseAttack;

            return flag;
        }

        @Override
        public void start() {
            canUseAttack = true;
            attackTimer = 25 + level.random.nextInt(5);
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

                    if (level.random.nextInt(100) + level.random.nextDouble() <= 20) {
                        EntityHelper.setState(this.mob, 1);
						((ServerLevel) level).sendParticles(ParticleTypes.HAPPY_VILLAGER.getType(), mob.getX(), mob.getY()+mob.getEyeHeight(), mob.getZ(), 1, 0D, 1D, 0D, 1D);

                        this.mob.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(0.0D);

                        if(EntityHelper.getEntitiesNear(this.mob, 10).size() > 0) {
                            for (LivingEntity heartless : EntityHelper.getEntitiesNear(this.mob, 10)) {
                                if (heartless instanceof IKHMob && ((IKHMob)heartless).getKHMobType() != EntityHelper.MobType.NPC) {
                                    if (heartless.getHealth() < heartless.getMaxHealth()) {
                                        heartless.heal((float) this.mob.getAttribute(Attributes.ATTACK_DAMAGE).getBaseValue()/4);
										((ServerLevel) level).sendParticles(ParticleTypes.HAPPY_VILLAGER.getType(), heartless.getX(), heartless.getY()+heartless.getEyeHeight(), heartless.getZ()-0.5F, 1, 0D, 1D, 0D, 1D);
										((ServerLevel) level).sendParticles(ParticleTypes.HAPPY_VILLAGER.getType(), heartless.getX(), heartless.getY()+heartless.getEyeHeight(), heartless.getZ()+0.5F, 1, 0D, 1D, 0D, 1D);
										((ServerLevel) level).sendParticles(ParticleTypes.HAPPY_VILLAGER.getType(), heartless.getX(), heartless.getY()+heartless.getEyeHeight(), heartless.getZ(), 1, 0D, 1D, 0D, 1D);
										((ServerLevel) level).sendParticles(ParticleTypes.HAPPY_VILLAGER.getType(), heartless.getX()+0.5F, heartless.getY()+heartless.getEyeHeight(), heartless.getZ(), 1, 0D, 1D, 0D, 1D);
										((ServerLevel) level).sendParticles(ParticleTypes.HAPPY_VILLAGER.getType(), heartless.getX()-0.5F, heartless.getY()+heartless.getEyeHeight(), heartless.getZ(), 1, 0D, 1D, 0D, 1D);
                                    }
                                }
                            }
                        }
                    }

                }

                if (EntityHelper.getState(mob) == 1 && whileAttackTimer > 50) {
                    canUseAttack = false;
                    EntityHelper.setState(mob, 0);
                    this.mob.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(0.20D);
                }
            }
        }

    }

}
