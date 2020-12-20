package online.kingdomkeys.kingdomkeys.entity.mob;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntitySize;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.Pose;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.World;
import online.kingdomkeys.kingdomkeys.entity.ModEntities;

public class PartEntity extends Entity {

    public final IMultiPartEntity parent;
    public final String partName;

    public EntitySize size;

    public PartEntity(EntityType<?> type, IMultiPartEntity parent, String partName) {
        super(type, parent.getWorld());
        this.parent = parent;
        this.partName = partName;
    }

    public void setNewHitbox(double minX, double minY, double minZ, double maxX, double maxY, double maxZ) {
        this.setBoundingBox(new AxisAlignedBB(minX, minY, minZ, minX + maxX, minY + maxY, minZ + maxZ));
    }

    @Override
    protected void registerData() { }

    @Override
    public boolean canBeCollidedWith() {
        return true;
    }

    @Override
    protected void readAdditional(CompoundNBT compound) { }

    @Override
    protected void writeAdditional(CompoundNBT compound) { }

    public String getPartName() { return this.partName; }

    @Override
    public boolean attackEntityFrom(DamageSource source, float amount)
    {
        return !this.isInvulnerableTo(source) && this.parent.attackEntityFromPart(this, source, amount);
    }

    @Override
    public boolean isEntityEqual(Entity entityIn)
    {
        return this == entityIn || this.parent == entityIn;
    }

    @Override
    public IPacket<?> createSpawnPacket() {
        return null;
    }

    public IMultiPartEntity getParent() {
        return this.parent;
    }

    @Override
    public EntitySize getSize(Pose poseIn) {
        return size;
    }
}
