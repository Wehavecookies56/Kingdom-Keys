package online.kingdomkeys.kingdomkeys.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntitySize;
import net.minecraft.entity.EntityType;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.FMLPlayMessages;
import net.minecraftforge.fml.network.NetworkHooks;
import online.kingdomkeys.kingdomkeys.entity.mob.IMultiPartEntity;

public class EntityPart extends Entity {

	private IMultiPartEntity parent;
	private String partName;

	public EntityPart(EntityType<? extends Entity> entityTypeIn, World worldIn) {
		super(entityTypeIn, worldIn);
	}

	public EntityPart(FMLPlayMessages.SpawnEntity spawnEntity, World world) {
		super(ModEntities.TYPE_ENTITY_PART.get(), world);
	}

	public EntityPart(IMultiPartEntity parent, String partName) {
		super(ModEntities.TYPE_ENTITY_PART.get(), parent.getWorld());
		this.parent = parent;
		this.partName = partName;
	}

	public void setNewHitbox(double minX, double minY, double minZ, double maxX, double maxY, double maxZ) {
		this.setBoundingBox(new AxisAlignedBB(minX, minY, minZ, minX + maxX, minY + maxY, minZ + maxZ));
	}

	@Override
	public void tick() {
		//System.out.println(getPosition());
		super.tick();
	}
	
	@Override
	public boolean canBeCollidedWith() {
		// TODO Auto-generated method stub
		return true;
	}
	
	public String getPartName() {
		return this.partName;
	}

	public boolean attackEntityFrom(DamageSource source, float amount) {
		return this.isInvulnerableTo(source) ? false : this.parent.attackEntityFromPart(this, source, amount);
	}

	public boolean isEntityEqual(Entity entityIn) {
		return this == entityIn || this.parent == entityIn;
	}

	public IMultiPartEntity getParent() {
		return this.parent;
	}
	
	@Override
	protected void registerData() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void readAdditional(CompoundNBT compound) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void writeAdditional(CompoundNBT compound) {
		// TODO Auto-generated method stub

	}

	@Override
	public IPacket<?> createSpawnPacket() {
		return NetworkHooks.getEntitySpawningPacket(this);
	}

}
