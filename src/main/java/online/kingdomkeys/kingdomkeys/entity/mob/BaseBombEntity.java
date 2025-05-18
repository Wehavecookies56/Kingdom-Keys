package online.kingdomkeys.kingdomkeys.entity.mob;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.Level.ExplosionInteraction;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.network.NetworkHooks;
import net.minecraftforge.network.PlayMessages;
import online.kingdomkeys.kingdomkeys.config.ModConfigs;
import online.kingdomkeys.kingdomkeys.damagesource.KKDamageTypes;
import online.kingdomkeys.kingdomkeys.entity.EntityHelper;

public abstract class BaseBombEntity extends BaseKHEntity {

    protected BaseBombEntity(EntityType<? extends Monster> type, Level worldIn) {
        super(type, worldIn);
        this.setTicks(100);
    }

    public BaseBombEntity(EntityType<? extends Monster> type, PlayMessages.SpawnEntity spawnEntity, Level world) {
        this(type, world);
    }

    @OnlyIn(Dist.CLIENT)
    public abstract ResourceLocation getTexture();

    public abstract float getExplosionStength();

    public static AttributeSupplier.Builder registerAttributes() {
        return Mob.createLivingAttributes()
                .add(Attributes.FOLLOW_RANGE, 35.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.4D)
                .add(Attributes.ATTACK_DAMAGE, 1.0D)
				.add(Attributes.ATTACK_KNOCKBACK, 1.0D)
                ;
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(0, new BombGoal(this));
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(3, new LeapAtTargetGoal(this, 0.4F));
        this.goalSelector.addGoal(4, new MeleeAttackGoal(this, 1.0D, true));
        this.goalSelector.addGoal(7, new WaterAvoidingRandomStrollGoal(this, 1.0D));
        this.goalSelector.addGoal(8, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(8, new RandomLookAroundGoal(this));
        this.targetSelector.addGoal(0, new HurtByTargetGoal(this));
        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, Player.class, true));
		this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Villager.class, true));

    }

    @Override
    public Packet<ClientGamePacketListener> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        if(!this.level().isClientSide) {
            if (ModConfigs.bombExplodeWithfire && (isOnFire() || source.is(KKDamageTypes.FIRE))) {
                explode();
            }
        }
        return super.hurt(source, amount);
    }


    private static final EntityDataAccessor<Integer> TICKS = SynchedEntityData.defineId(BaseBombEntity.class, EntityDataSerializers.INT);

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        compound.putInt("ticks", this.entityData.get(TICKS));
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        this.entityData.set(TICKS, compound.getInt("ticks"));
    }

    public int getTicks() {
        return this.getEntityData().get(TICKS);
    }

    public void setTicks(int ticks) {
        this.entityData.set(TICKS, ticks);
    }
    boolean hasExploded = false;

    public void explode() {
        if (!hasExploded) {
            hasExploded = true;
            ExplosionInteraction explosion$mode = ForgeEventFactory.getMobGriefingEvent(this.level(), this) ? ExplosionInteraction.MOB : ExplosionInteraction.NONE;
            this.level().explode(this, this.getX(), this.getY(), this.getZ(), getExplosionStength(), false, explosion$mode);
            for (LivingEntity enemy : EntityHelper.getEntitiesNear(this, getExplosionStength()+1))
                this.doHurtTarget(enemy);
            this.remove(RemovalReason.KILLED);
        }
    }

    @Override
    public EntityHelper.MobType getKHMobType() {
        return EntityHelper.MobType.HEARTLESS_EMBLEM;
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(EntityHelper.STATE, 0);
        this.entityData.define(TICKS, 0);
    }

    class BombGoal extends Goal {
        private BaseBombEntity bomb;

        public BombGoal(BaseBombEntity bomb) {
            this.bomb = bomb;
        }

        @Override
        public boolean canUse() {
            return EntityHelper.getState(bomb) == 1 || bomb.getTarget() != null && bomb.distanceToSqr(bomb.getTarget()) < 64 && bomb.getHealth() < bomb.getMaxHealth();
        }

        @Override
        public boolean canContinueToUse() {
            if (canUse()) {
                EntityHelper.setState(bomb, 1);
                bomb.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(0.8D);

                bomb.setTicks(bomb.getTicks()-2);

                if (bomb.getTicks() <= 0) {
                    bomb.explode();
                }
            }
            return false;
        }

    }
}
