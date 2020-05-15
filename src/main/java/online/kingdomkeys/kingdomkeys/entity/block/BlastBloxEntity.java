package online.kingdomkeys.kingdomkeys.entity.block;

import net.minecraft.entity.*;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.FMLPlayMessages;
import net.minecraftforge.fml.network.NetworkHooks;
import online.kingdomkeys.kingdomkeys.entity.ModEntities;

import javax.annotation.Nullable;

/**
 * Mostly a copy of {@link net.minecraft.entity.item.TNTEntity} with some small changes
 */
public class BlastBloxEntity extends Entity {

    private static final DataParameter<Integer> FUSE = EntityDataManager.createKey(BlastBloxEntity.class, DataSerializers.VARINT);
    private int fuse = 40;

    @Nullable
    private LivingEntity placedBy;

    public BlastBloxEntity(EntityType<? extends Entity> type, World world) {
        super(type, world);
        this.preventEntitySpawning = true;
    }

    public BlastBloxEntity(FMLPlayMessages.SpawnEntity spawnEntity, World world) {
        super(ModEntities.TYPE_BLAST_BLOX, world);
    }

    public BlastBloxEntity(EntityType type, World world, double x, double y, double z, @Nullable LivingEntity igniter) {
        this(type, world);
        this.setPosition(x, y, z);
        double random = world.rand.nextDouble() * 6.2831854820251465D;
        this.setMotion(-Math.sin(random) * 0.02D, 0.20000000298023224D, -Math.cos(random) * 0.02D);
        this.setFuse(40);
        this.prevPosX = x;
        this.prevPosY = y;
        this.prevPosZ = z;
        this.placedBy = igniter;
    }

    @Override
    protected void registerData() {
        this.dataManager.register(FUSE, 40);
    }

    @Override
    protected boolean canTriggerWalking() {
        return true;
    }

    @Override
    public boolean canBeCollidedWith() {
        return this.isAlive();
    }

    @Override
    public void tick() {
        this.prevPosX = this.getPosition().getX();
        this.prevPosY = this.getPosition().getY();
        this.prevPosZ = this.getPosition().getZ();
        if (!this.hasNoGravity()) {
            this.setMotion(this.getMotion().add(0.0D, -0.04D, 0.0D));
        }

        this.move(MoverType.SELF, this.getMotion());
        this.setMotion(this.getMotion().scale(0.98D));
        if (this.onGround) {
            this.setMotion(this.getMotion().mul(0.7D, -0.5D, 0.7D));
        }

        --this.fuse;
        if (this.fuse <= 0) {
            this.remove();
            if (!this.world.isRemote) {
                this.explode();
            }
        } else {
            this.handleWaterMovement();
            this.world.addParticle(ParticleTypes.FLAME, this.getPosition().getX(), this.getPosition().getY() + 0.5D, this.getPosition().getZ(), 0.0D, 0.0D, 0.0D);
        }
    }

    private void explode() {
        float explosionSize = 8.0F;
        this.world.createExplosion(this, this.getPosition().getX(), this.getPosition().getY() + (double)(this.getHeight() / 16.0F), this.getPosition().getZ(), explosionSize, true, Explosion.Mode.BREAK);
    }

    @Override
    protected void writeAdditional(CompoundNBT compound) {
        compound.putShort("Fuse", (short)this.getFuse());
    }

    @Override
    protected void readAdditional(CompoundNBT compound) {
        this.setFuse(compound.getShort("Fuse"));
    }

    @Nullable
    public LivingEntity getPlacedBy() {
        return this.placedBy;
    }

    @Override
    protected float getEyeHeight(Pose pose, EntitySize entitySize) {
        return 0.0F;
    }

    public void setFuse(int fuse) {
        this.dataManager.set(FUSE, fuse);
        this.fuse = fuse;
    }

    @Override
    public void notifyDataManagerChange(DataParameter<?> key) {
        if (FUSE.equals(key)) {
            this.fuse = this.getFuseDataManager();
        }
    }

    public int getFuseDataManager() {
        return this.dataManager.get(FUSE);
    }

    public int getFuse() {
        return this.fuse;
    }

    @Override
    public IPacket<?> createSpawnPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }
}
