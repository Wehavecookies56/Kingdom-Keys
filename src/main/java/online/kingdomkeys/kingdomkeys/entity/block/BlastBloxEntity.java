package online.kingdomkeys.kingdomkeys.entity.block;

import javax.annotation.Nullable;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.Level.ExplosionInteraction;
import net.minecraftforge.network.NetworkHooks;
import net.minecraftforge.network.PlayMessages;
import online.kingdomkeys.kingdomkeys.entity.ModEntities;

/**
 * Mostly a copy of {@link net.minecraft.world.entity.item.PrimedTnt} with some small changes
 */
public class BlastBloxEntity extends Entity {

    private static final EntityDataAccessor<Integer> FUSE = SynchedEntityData.defineId(BlastBloxEntity.class, EntityDataSerializers.INT);
    private int fuse = 40;

    @Nullable
    private LivingEntity placedBy;

    public BlastBloxEntity(EntityType<? extends Entity> type, Level world) {
        super(type, world);
        this.blocksBuilding = true;
    }

    public static BlastBloxEntity create(EntityType<? extends Entity> type, Level world) {
        return new BlastBloxEntity(type, world);
    }


    public BlastBloxEntity(PlayMessages.SpawnEntity spawnEntity, Level world) {
        super(ModEntities.TYPE_BLAST_BLOX.get(), world);
    }

    public BlastBloxEntity(EntityType type, Level world, double x, double y, double z, @Nullable LivingEntity igniter) {
        this(type, world);
        this.setPos(x, y, z);
        double random = world.random.nextDouble() * 6.2831854820251465D;
        this.setDeltaMovement(-Math.sin(random) * 0.02D, 0.20000000298023224D, -Math.cos(random) * 0.02D);
        this.setFuse(40);
        this.xo = x;
        this.yo = y;
        this.zo = z;
        this.placedBy = igniter;
    }

    @Override
    protected void defineSynchedData() {
        this.entityData.define(FUSE, 40);
    }

    protected Entity.MovementEmission getMovementEmission() {
        return Entity.MovementEmission.NONE;
    }

    @Override
    public boolean isPickable() {
        return this.isAlive();
    }

    @Override
    public void tick() {
        this.xo = this.blockPosition().getX();
        this.yo = this.blockPosition().getY();
        this.zo = this.blockPosition().getZ();
        if (!this.isNoGravity()) {
            this.setDeltaMovement(this.getDeltaMovement().add(0.0D, -0.04D, 0.0D));
        }

        this.move(MoverType.SELF, this.getDeltaMovement());
        this.setDeltaMovement(this.getDeltaMovement().scale(0.98D));
        if (this.onGround) {
            this.setDeltaMovement(this.getDeltaMovement().multiply(0.7D, -0.5D, 0.7D));
        }

        --this.fuse;
        if (this.fuse <= 0) {
            this.remove(RemovalReason.KILLED);
            if (!this.level.isClientSide) {
                this.explode();
            }
        } else {
            //this.handleWaterMovement();
            this.level.addParticle(ParticleTypes.FLAME, this.blockPosition().getX(), this.blockPosition().getY() + 0.5D, this.blockPosition().getZ(), 0.0D, 0.0D, 0.0D);
        }
    }

    private void explode() {
        float explosionSize = 4.0F;
        this.level.explode(this, this.blockPosition().getX(), this.blockPosition().getY() + (double)(this.getBbHeight() / 16.0F), this.blockPosition().getZ(), explosionSize, true, ExplosionInteraction.TNT);
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag compound) {
        compound.putShort("Fuse", (short)this.getFuse());
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag compound) {
        this.setFuse(compound.getShort("Fuse"));
    }

    @Nullable
    public LivingEntity getPlacedBy() {
        return this.placedBy;
    }

    @Override
    protected float getEyeHeight(Pose pose, EntityDimensions entitySize) {
        return 0.0F;
    }

    public void setFuse(int fuse) {
        this.entityData.set(FUSE, fuse);
        this.fuse = fuse;
    }

    @Override
    public void onSyncedDataUpdated(EntityDataAccessor<?> key) {
        if (FUSE.equals(key)) {
            this.fuse = this.getFuseDataManager();
        }
    }

    public int getFuseDataManager() {
        return this.entityData.get(FUSE);
    }

    public int getFuse() {
        return this.fuse;
    }

    @Override
    public Packet<ClientGamePacketListener> getAddEntityPacket() {
        return (Packet<ClientGamePacketListener>) NetworkHooks.getEntitySpawningPacket(this);
    }
}
