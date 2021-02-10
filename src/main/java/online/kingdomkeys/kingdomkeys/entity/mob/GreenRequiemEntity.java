package online.kingdomkeys.kingdomkeys.entity.mob;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.ai.goal.TargetGoal;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.network.FMLPlayMessages;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.entity.EntityHelper;
import online.kingdomkeys.kingdomkeys.entity.ModEntities;

public class GreenRequiemEntity extends BaseElementalMusicalHeartlessEntity {

    public GreenRequiemEntity(EntityType<? extends MonsterEntity> type, World worldIn) {
        super(type, worldIn);
    }

    public GreenRequiemEntity(FMLPlayMessages.SpawnEntity spawnEntity, World world) {
        super(ModEntities.TYPE_GREEN_REQUIEM.get(), spawnEntity, world);
    }

    @Override
    protected Goal goalToUse() {
        return new GreenRequiemGoal(this);
    }

    public static AttributeModifierMap.MutableAttribute registerAttributes() {
        return BaseElementalMusicalHeartlessEntity.registerAttributes().createMutableAttribute(Attributes.MAX_HEALTH, 40.0D);
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
    public boolean attackEntityFrom(DamageSource source, float amount) {
        float multiplier = 1;
        //TODO elemental weakness, ? not sure yet
        //if(!this.world.isRemote) {
        //    if(source.getImmediateSource() instanceof FireEntity)
        //        multiplier = 2;
        //}
        return super.attackEntityFrom(source, amount * multiplier);
    }

    class GreenRequiemGoal extends TargetGoal {
        private boolean canUseAttack = true;
        private int attackTimer = 5, whileAttackTimer;
        private float initialHealth;

        public GreenRequiemGoal(GreenRequiemEntity e) {
        	super(e, true);
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

                    if (world.rand.nextInt(100) + world.rand.nextDouble() <= 20) {
                        EntityHelper.setState(this.goalOwner, 1);
						((ServerWorld) world).spawnParticle(ParticleTypes.HAPPY_VILLAGER.getType(), goalOwner.getPosX(), goalOwner.getPosY()+goalOwner.getEyeHeight(), goalOwner.getPosZ(), 1, 0D, 1D, 0D, 1D);

                        this.goalOwner.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(0.0D);

                        if(EntityHelper.getEntitiesNear(this.goalOwner, 10).size() > 0) {
                            for (LivingEntity heartless : EntityHelper.getEntitiesNear(this.goalOwner, 10)) {
                                if (heartless instanceof IKHMob && ((IKHMob)heartless).getMobType() != EntityHelper.MobType.NPC) {
                                    if (heartless.getHealth() < heartless.getMaxHealth()) {
                                        heartless.heal(10);
										((ServerWorld) world).spawnParticle(ParticleTypes.HAPPY_VILLAGER.getType(), heartless.getPosX(), heartless.getPosY()+heartless.getEyeHeight(), heartless.getPosZ()-0.5F, 1, 0D, 1D, 0D, 1D);
										((ServerWorld) world).spawnParticle(ParticleTypes.HAPPY_VILLAGER.getType(), heartless.getPosX(), heartless.getPosY()+heartless.getEyeHeight(), heartless.getPosZ()+0.5F, 1, 0D, 1D, 0D, 1D);
										((ServerWorld) world).spawnParticle(ParticleTypes.HAPPY_VILLAGER.getType(), heartless.getPosX(), heartless.getPosY()+heartless.getEyeHeight(), heartless.getPosZ(), 1, 0D, 1D, 0D, 1D);
										((ServerWorld) world).spawnParticle(ParticleTypes.HAPPY_VILLAGER.getType(), heartless.getPosX()+0.5F, heartless.getPosY()+heartless.getEyeHeight(), heartless.getPosZ(), 1, 0D, 1D, 0D, 1D);
										((ServerWorld) world).spawnParticle(ParticleTypes.HAPPY_VILLAGER.getType(), heartless.getPosX()-0.5F, heartless.getPosY()+heartless.getEyeHeight(), heartless.getPosZ(), 1, 0D, 1D, 0D, 1D);
                                    }
                                }
                            }
                        }
                    }

                }

                if (EntityHelper.getState(goalOwner) == 1 && whileAttackTimer > 50) {
                    canUseAttack = false;
                    EntityHelper.setState(goalOwner, 0);
                    this.goalOwner.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(0.20D);
                }
            }
        }

    }

}
