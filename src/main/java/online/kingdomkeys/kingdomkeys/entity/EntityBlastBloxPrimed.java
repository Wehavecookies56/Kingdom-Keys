package online.kingdomkeys.kingdomkeys.entity;

import javax.annotation.Nullable;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.MoverType;
import net.minecraft.init.Particles;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.world.World;

/**
 * Mostly a copy of {@link net.minecraft.entity.item.EntityTNTPrimed} with some small changes
 */
public class EntityBlastBloxPrimed extends Entity {

    private static final DataParameter<Integer> FUSE = EntityDataManager.createKey(EntityBlastBloxPrimed.class, DataSerializers.VARINT);
    private int fuse = 40;

    @Nullable
    private EntityLivingBase placedBy;

    public EntityBlastBloxPrimed(World world) {
        super(ModEntities.TYPE_BLAST_BLOX, world);
        this.preventEntitySpawning = true;
        this.isImmuneToFire = true;
        this.setSize(0.98F, 0.98F);
    }

    public EntityBlastBloxPrimed(World world, double x, double y, double z, @Nullable EntityLivingBase igniter) {
        this(world);
        this.setPosition(x, y, z);
        float f = (float)(Math.random() * (double)((float)Math.PI * 2F));
        this.motionX = (double)(-((float)Math.sin((double)f)) * 0.02F);
        this.motionY = (double)0.2F;
        this.motionZ = (double)(-((float)Math.cos((double)f)) * 0.02F);
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
        this.prevPosX = this.posX;
        this.prevPosY = this.posY;
        this.prevPosZ = this.posZ;
        if (!this.hasNoGravity()) {
            this.motionY -= (double)0.04F;
        }

        this.move(MoverType.SELF, this.motionX, this.motionY, this.motionZ);
        this.motionX *= (double)0.98F;
        this.motionY *= (double)0.98F;
        this.motionZ *= (double)0.98F;
        if (this.onGround) {
            this.motionX *= (double)0.7F;
            this.motionZ *= (double)0.7F;
            this.motionY *= -0.5D;
        }

        --this.fuse;
        if (this.fuse <= 0) {
            this.remove();
            if (!this.world.isRemote) {
                this.explode();
            }
        } else {
            this.handleWaterMovement();
            this.world.spawnParticle(Particles.FLAME, this.posX, this.posY + 0.5D, this.posZ, 0.0D, 0.0D, 0.0D);
        }
    }

    private void explode() {
        float explosionSize = 8.0F;
        this.world.createExplosion(this, this.posX, this.posY + (double)(this.height / 16.0F), this.posZ, explosionSize, true);
    }

    @Override
    protected void writeAdditional(NBTTagCompound compound) {
        compound.setShort("Fuse", (short)this.getFuse());
    }

    @Override
    protected void readAdditional(NBTTagCompound compound) {
        this.setFuse(compound.getShort("Fuse"));
    }

    @Nullable
    public EntityLivingBase getPlacedBy() {
        return this.placedBy;
    }

    @Override
    public float getEyeHeight() {
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
}
