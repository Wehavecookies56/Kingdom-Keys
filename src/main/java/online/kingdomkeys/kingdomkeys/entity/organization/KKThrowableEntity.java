package online.kingdomkeys.kingdomkeys.entity.organization;

import java.util.List;
import java.util.UUID;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraftforge.network.NetworkHooks;
import net.minecraftforge.network.PlayMessages;
import online.kingdomkeys.kingdomkeys.entity.ModEntities;
import online.kingdomkeys.kingdomkeys.item.KeybladeItem;

public class KKThrowableEntity extends ThrowableItemProjectile {
	public static final EntityDataAccessor<ItemStack> ITEMSTACK = SynchedEntityData.defineId(KKThrowableEntity.class, EntityDataSerializers.ITEM_STACK);
	private static final EntityDataAccessor<Integer> ROTATION_POINT = SynchedEntityData.defineId(KKThrowableEntity.class, EntityDataSerializers.INT);

	public ItemStack originalItem;
	public int slot;
	public UUID ownerUUID;

	int maxTicks = 120;
	boolean returning = false;
	float dmg;
	Player owner;
	int rotationPoint;

	public KKThrowableEntity(PlayMessages.SpawnEntity spawnEntity, Level world) {
		super(ModEntities.TYPE_CHAKRAM.get(), world);
	}

	public KKThrowableEntity(Level world) {
		super(ModEntities.TYPE_CHAKRAM.get(), world);
		this.blocksBuilding = true;
	}
	
	public void setData(float damage, UUID ownerUUID, int slot, ItemStack stack) {
		this.ownerUUID = ownerUUID;
		this.originalItem = stack;
		this.slot = slot;
		this.dmg = damage;
	}

	public Player getProjOwner() {
		if (owner == null) {
			if (!level.isClientSide) {
				owner = (Player) ((ServerLevel) level).getEntity(ownerUUID);
			}
		}
		return owner;
	}

	@Override
	public Packet<?> getAddEntityPacket() {
		return NetworkHooks.getEntitySpawningPacket(this);
	}

	@Override
	protected float getGravity() {
		return 0F;
	}

	@Override
	public void tick() {
		super.tick();
		if(!level.isClientSide) {
			if (getProjOwner() == null) {
				this.remove(RemovalReason.KILLED);
				return;
			}
	
			if (this.tickCount > maxTicks) {
				this.remove(RemovalReason.KILLED);
			}

			if (tickCount > 30) {
				setReturn();
			}
	
			if (Math.max(Math.abs(getDeltaMovement().x), Math.max(Math.abs(getDeltaMovement().y), Math.abs(getDeltaMovement().z))) < 0.1) {
				setReturn();
			}
	
			if (returning) {
				List entityTagetList = this.level.getEntitiesOfClass(Entity.class, this.getBoundingBox().inflate(1.0D, 1.0D, 1.0D));
				for (int i = 0; i < entityTagetList.size(); i++) {
					Entity entityTarget = (Entity) entityTagetList.get(i);
					if (entityTarget != null && entityTarget instanceof Player) {
						Player owner = (Player) entityTarget;
						if (owner == getProjOwner()) {
							this.remove(RemovalReason.KILLED);
							if(!ItemStack.isSame(owner.getInventory().getItem(slot),originalItem)) {
								if(!ItemStack.isSame(owner.getInventory().getItem(slot), ItemStack.EMPTY)) {
									owner.addItem(originalItem);
								} else {
									owner.getInventory().add(slot, originalItem);
								}
							}
							owner.getCooldowns().addCooldown(originalItem.getItem(), 20);
						}
					}
				}
			}
		}
	}

	public void setReturn() {
		if(originalItem.getItem() instanceof KeybladeItem) {
			this.remove(RemovalReason.KILLED);
			return;
		} else {
			returning = true;
			if (getProjOwner() != null)
				shoot(this.getProjOwner().getX() - this.getX(), this.getProjOwner().getY() - this.getY() + 1.25, this.getProjOwner().getZ() - this.getZ(), 2f, 0);
		}
	}

	@Override
	protected void onHit(HitResult rtRes) {
		if (!level.isClientSide) {
			EntityHitResult ertResult = null;
			BlockHitResult brtResult = null;

			if (rtRes instanceof EntityHitResult) {
				ertResult = (EntityHitResult) rtRes;
			}

			if (rtRes instanceof BlockHitResult) {
				brtResult = (BlockHitResult) rtRes;
			}

			if (ertResult != null && ertResult.getEntity() != null && ertResult.getEntity() instanceof LivingEntity) {
				LivingEntity target = (LivingEntity) ertResult.getEntity();
				if (target != getProjOwner()) {
					// target.setFire(5);
					target.hurt(DamageSource.thrown(this, this.getProjOwner()), dmg < 4 ? 4 : dmg);
					// setReturn();
					setDeltaMovement(getDeltaMovement().scale(0.5));
					dmg *= 0.9;
				}
			} else { // Block (not ERTR)
				if (brtResult != null) {

					if (level.getBlockState(brtResult.getBlockPos()).getBlock() == Blocks.TALL_GRASS || level.getBlockState(brtResult.getBlockPos()).getBlock() == Blocks.GRASS || level.getBlockState(brtResult.getBlockPos()).getBlock() == Blocks.SUGAR_CANE || level.getBlockState(brtResult.getBlockPos()).getBlock() == Blocks.VINE) {

					} else {
						setReturn();
					}
				}
			}
		}

	}

	public int getMaxTicks() {
		return maxTicks;
	}

	public void setMaxTicks(int maxTicks) {
		this.maxTicks = maxTicks;
	}

	@Override
	public void onSyncedDataUpdated(EntityDataAccessor<?> key) {
		if (key.equals(ITEMSTACK)) {
			this.originalItem = this.entityData.get(ITEMSTACK);
		}
		if (key.equals(ROTATION_POINT)) {
			this.rotationPoint = this.entityData.get(ROTATION_POINT);
		}
	}

	@Override
	public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);

		compound.put("ogitem", originalItem.serializeNBT());
		if (ownerUUID != null) {
			compound.putUUID("ownerUUID", ownerUUID);
		}
		compound.putInt("Rotation", this.getRotationPoint());

		compound.putInt("slot", slot);
		compound.putFloat("damage", dmg);
	}

	@Override
	public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        
		if (compound.contains("ogitem")) {
			originalItem = ItemStack.of(compound.getCompound("ogitem"));
		}
		entityData.set(ITEMSTACK, originalItem);

		if (compound.contains("ownerUUID")) {
			ownerUUID = compound.getUUID("ownerUUID");
            owner = getProjOwner();
		}
		
		this.setRotationPoint(compound.getInt("Rotation"));

		slot = compound.getInt("slot");
		dmg = compound.getFloat("damage");

	}
	
	public int getRotationPoint() {
		return rotationPoint;
	}
	
	public void setRotationPoint(int rotations) {
		this.entityData.set(ROTATION_POINT, rotations);
		this.rotationPoint = rotations;
	}

	@Override
	protected void defineSynchedData() {
        super.defineSynchedData();
		entityData.define(ITEMSTACK, ItemStack.EMPTY);
		entityData.define(ROTATION_POINT, 0);

	}

	@Override
	public ItemStack getItem() {
		if (originalItem == null) {
			originalItem = entityData.get(ITEMSTACK);
		}
		return originalItem;
	}

	@Override
	protected Item getDefaultItem() {
		if (originalItem == null) {
			originalItem = entityData.get(ITEMSTACK);
		}
		return originalItem.getItem();
	}

}
