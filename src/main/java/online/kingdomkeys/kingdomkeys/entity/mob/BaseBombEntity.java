package online.kingdomkeys.kingdomkeys.entity.mob;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
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
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.entity.IEntityWithComplexSpawn;
import net.neoforged.neoforge.event.entity.EntityMobGriefingEvent;
import online.kingdomkeys.kingdomkeys.config.ModConfigs;
import online.kingdomkeys.kingdomkeys.entity.EntityHelper;
import online.kingdomkeys.kingdomkeys.item.KKResistanceType;

public abstract class BaseBombEntity extends BaseKHEntity implements IEntityWithComplexSpawn {

    public int ticksToExplode;

    protected BaseBombEntity(EntityType<? extends Monster> type, Level worldIn) {
        super(type, worldIn);
        this.ticksToExplode = 100;
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
    public void writeSpawnData(RegistryFriendlyByteBuf buffer) {
        buffer.writeInt(ticksToExplode);
    }

    @Override
    public void readSpawnData(RegistryFriendlyByteBuf additionalData) {
        ticksToExplode = additionalData.readInt();
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        if(!this.level().isClientSide) {
            if (ModConfigs.bombExplodeWithfire && (isOnFire() || source.getMsgId().equals(KKResistanceType.fire.toString()))) {
                explode();
            }
        }
        return super.hurt(source, amount);
    }

    @Override
    public void tick() {
        if (EntityHelper.getState(this) == 1) 
        	ticksToExplode--;
        super.tick();
    }

    boolean hasExploded = false;

    public void explode() {
        if (!hasExploded) {
            hasExploded = true;
            ExplosionInteraction explosion$mode = NeoForge.EVENT_BUS.post(new EntityMobGriefingEvent(this.level(), this)).canGrief() ? ExplosionInteraction.MOB : ExplosionInteraction.NONE;
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
    protected void defineSynchedData(SynchedEntityData.Builder pBuilder) {
        pBuilder.define(EntityHelper.STATE, 0);
    }

    class BombGoal extends Goal {
        private BaseBombEntity bomb;

        public BombGoal(BaseBombEntity bomb) {
            this.bomb = bomb;
        }

        @Override
        public boolean canUse() {
            return bomb.getTarget() != null && bomb.distanceToSqr(bomb.getTarget()) < 64 && bomb.getHealth() < bomb.getMaxHealth();
        }

        @Override
        public boolean canContinueToUse() {
            if (canUse()) {
                EntityHelper.setState(bomb, 1);
                bomb.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(0.8D);
                if (bomb.ticksToExplode <= 0) {
                    bomb.explode();
                }
            }
            return false;
        }

    }
}
