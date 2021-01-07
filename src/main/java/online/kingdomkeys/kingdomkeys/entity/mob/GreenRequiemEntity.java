package online.kingdomkeys.kingdomkeys.entity.mob;

import net.minecraft.entity.CreatureEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.particles.ParticleTypes;
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

public class GreenRequiemEntity extends BaseElementalMusicalHeartlessEntity {

    public GreenRequiemEntity(EntityType<? extends CreatureEntity> type, World worldIn) {
        super(type, worldIn);
    }

    public GreenRequiemEntity(FMLPlayMessages.SpawnEntity spawnEntity, World world) {
        super(ModEntities.TYPE_GREEN_REQUIEM.get(), spawnEntity, world);
    }

    @Override
    protected Goal goalToUse() {
        return new GreenRequiemGoal(this);
    }

    @Override
    protected double getMaxHP() {
        return 40.0D;
    }

    @Override
    public Element getElementToUse() {
        return Element.BLIZZARD;
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

    class GreenRequiemGoal extends Goal {
        private GreenRequiemEntity theEntity;
        private boolean canUseAttack = true;
        private int attackTimer = 5, whileAttackTimer;
        private float initialHealth;

        public GreenRequiemGoal(GreenRequiemEntity e) {
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

                    if (world.rand.nextInt(100) + world.rand.nextDouble() <= 20) {
                        EntityHelper.setState(this.theEntity, 1);

                        this.theEntity.getAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.0D);

                        if(EntityHelper.getEntitiesNear(this.theEntity, 10).size() > 0) {
                            for (LivingEntity heartless : EntityHelper.getEntitiesNear(this.theEntity, 10)) {
                                if (heartless instanceof IKHMob && ((IKHMob)heartless).getMobType() != EntityHelper.MobType.NPC) {
                                    if (heartless.getHealth() < heartless.getMaxHealth() - 10)
                                        heartless.setHealth(heartless.getHealth() + 10);
                                    else if(heartless.getHealth() > heartless.getMaxHealth() - 10)
                                        heartless.setHealth(heartless.getMaxHealth());

                                    if(!this.theEntity.world.isRemote) {
                                        this.theEntity.world.addParticle(ParticleTypes.HAPPY_VILLAGER, heartless.getPosX(), heartless.getPosY(), heartless.getPosZ(), 0.0D, 1.0D, 0.0D);
                                        this.theEntity.world.addParticle(ParticleTypes.HAPPY_VILLAGER, heartless.getPosX(), heartless.getPosY() , heartless.getPosZ(), 0.0D, 1.0D, 0.0D);
                                        this.theEntity.world.addParticle(ParticleTypes.HAPPY_VILLAGER, heartless.getPosX(), heartless.getPosY(), heartless.getPosZ(), 0.0D, 1.0D, 0.0D);
                                        this.theEntity.world.addParticle(ParticleTypes.HAPPY_VILLAGER, heartless.getPosX() + 0.3, heartless.getPosY(), heartless.getPosZ(), 0.0D, 1.0D, 0.0D);
                                        this.theEntity.world.addParticle(ParticleTypes.HAPPY_VILLAGER, heartless.getPosX() - 0.3, heartless.getPosY(), heartless.getPosZ(), 0.0D, 1.0D, 0.0D);
                                    }
                                }
                            }
                        }
                    }

                }

                if (EntityHelper.getState(theEntity) == 1 && whileAttackTimer > 50) {
                    canUseAttack = false;
                    EntityHelper.setState(theEntity, 0);
                    this.theEntity.getAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.20D);
                }
            }
        }

    }

}
