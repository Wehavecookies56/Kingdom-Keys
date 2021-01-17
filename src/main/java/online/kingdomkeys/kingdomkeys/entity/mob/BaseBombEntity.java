package online.kingdomkeys.kingdomkeys.entity.mob;

import net.minecraft.entity.CreatureEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.IPacket;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.Explosion;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.fml.common.registry.IEntityAdditionalSpawnData;
import net.minecraftforge.fml.network.FMLPlayMessages;
import net.minecraftforge.fml.network.NetworkHooks;
import online.kingdomkeys.kingdomkeys.capability.ModCapabilities;
import online.kingdomkeys.kingdomkeys.entity.EntityHelper;
import online.kingdomkeys.kingdomkeys.entity.magic.FireEntity;

public abstract class BaseBombEntity extends MonsterEntity implements IKHMob, IEntityAdditionalSpawnData {

    public int ticksToExplode;

    protected BaseBombEntity(EntityType<? extends MonsterEntity> type, World worldIn) {
        super(type, worldIn);
        this.ticksToExplode = 100;
    }

    public BaseBombEntity(EntityType<? extends MonsterEntity> type, FMLPlayMessages.SpawnEntity spawnEntity, World world) {
        super(type, world);
    }
    
    @Override
    public boolean canSpawn(IWorld worldIn, SpawnReason spawnReasonIn) {
    	return ModCapabilities.getWorld((World)worldIn).getHeartlessSpawnLevel() > 0;
    }

    @OnlyIn(Dist.CLIENT)
    public abstract ResourceLocation getTexture();

    public abstract float getExplosionStength();

    @Override
    protected void registerAttributes() {
        super.registerAttributes();
        this.getAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(35.0D);
        this.getAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.4D);
        this.getAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(1.0D);
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(0, new BombGoal(this));
        this.goalSelector.addGoal(0, new SwimGoal(this));
        this.goalSelector.addGoal(3, new LeapAtTargetGoal(this, 0.4F));
        this.goalSelector.addGoal(4, new MeleeAttackGoal(this, 1.0D, true));
        this.goalSelector.addGoal(7, new WaterAvoidingRandomWalkingGoal(this, 1.0D));
        this.goalSelector.addGoal(8, new LookAtGoal(this, PlayerEntity.class, 8.0F));
        this.goalSelector.addGoal(8, new LookRandomlyGoal(this));
        this.targetSelector.addGoal(0, new HurtByTargetGoal(this));
        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, PlayerEntity.class, true));
    }

    @Override
    public void writeSpawnData(PacketBuffer buffer) {
        buffer.writeInt(ticksToExplode);
    }

    @Override
    public void readSpawnData(PacketBuffer additionalData) {
        ticksToExplode = additionalData.readInt();
    }

    @Override
    public IPacket<?> createSpawnPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    @Override
    public boolean attackEntityFrom(DamageSource source, float amount) {
        if(!this.world.isRemote) {
            if (isBurning() || source.getImmediateSource() instanceof FireEntity) {
                explode();
            }
        }
        return super.attackEntityFrom(source, amount);
    }

    @Override
    public void tick() {
        if (EntityHelper.getState(this) == 1) 
        	ticksToExplode--;
        super.tick();
    }

    public void explode() {
        Explosion.Mode explosion$mode = ForgeEventFactory.getMobGriefingEvent(this.world, this) ? Explosion.Mode.DESTROY : Explosion.Mode.NONE;
        this.world.createExplosion(this, this.getPosX(), this.getPosY(), this.getPosZ(), getExplosionStength(), false, explosion$mode);
        this.remove();
    }

    @Override
    public EntityHelper.MobType getMobType() {
        return EntityHelper.MobType.HEARTLESS_EMBLEM;
    }

    @Override
    protected void registerData() {
        super.registerData();
        this.dataManager.register(EntityHelper.STATE, 0);
    }

    class BombGoal extends Goal {
        private BaseBombEntity bomb;

        public BombGoal(BaseBombEntity bomb) {
            this.bomb = bomb;
        }

        @Override
        public boolean shouldExecute() {
            return bomb.getAttackTarget() != null && bomb.getDistanceSq(bomb.getAttackTarget()) < 64 && bomb.getHealth() < bomb.getMaxHealth();
        }

        @Override
        public boolean shouldContinueExecuting() {
            if (shouldExecute()) {
                EntityHelper.setState(bomb, 1);
                bomb.getAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.8D);
                if (bomb.ticksToExplode <= 0) {
                    bomb.explode();
                }
            }
            return false;
        }

    }
}
