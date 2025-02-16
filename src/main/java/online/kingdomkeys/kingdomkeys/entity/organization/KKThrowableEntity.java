package online.kingdomkeys.kingdomkeys.entity.organization;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import online.kingdomkeys.kingdomkeys.entity.ModEntities;
import online.kingdomkeys.kingdomkeys.item.KeybladeItem;
import online.kingdomkeys.kingdomkeys.item.organization.CardItem;
import online.kingdomkeys.kingdomkeys.item.organization.ChakramItem;
import online.kingdomkeys.kingdomkeys.item.organization.KnifeItem;
import online.kingdomkeys.kingdomkeys.item.organization.ScytheItem;

public class KKThrowableEntity extends ThrowableItemProjectile {
	private static final EntityDataAccessor<Integer> ROTATION_POINT = SynchedEntityData.defineId(KKThrowableEntity.class, EntityDataSerializers.INT);

	Set<LivingEntity> hitSet = new HashSet<>();
	
	public int slot;
	public UUID ownerUUID;

	int maxTicks = 120;
	boolean returning = false;
	float dmg;
	Player owner;
	int rotationPoint;

	public KKThrowableEntity(Level world) {
		super(ModEntities.TYPE_KK_THROWABLE.get(), world);
		this.blocksBuilding = true;
	}

	public KKThrowableEntity(EntityType<? extends ThrowableItemProjectile> type, Level world) {
		super(type, world);
		this.blocksBuilding = true;
	}

	public void setData(float damage, UUID ownerUUID, int slot, ItemStack stack) {
		this.dmg = damage;
		this.ownerUUID = ownerUUID;
		this.slot = slot;
		this.setItem(stack);
	}

	public Player getProjOwner() {
		if (owner == null) {
			if (!level().isClientSide) {
				owner = (Player) ((ServerLevel) level()).getEntity(ownerUUID);
			}
		}
		return owner;
	}

	@Override
	protected double getDefaultGravity() {
		return 0D;
	}

	@Override
	public void tick() {
		super.tick();
		if(!level().isClientSide) {
			if (getProjOwner() == null) {
				this.remove(RemovalReason.KILLED);
				return;
			}
	
			if (this.tickCount > maxTicks) {
				this.remove(RemovalReason.KILLED);
			}

			if (getItem().getItem() instanceof KnifeItem){
				((ServerLevel) level()).sendParticles(ParticleTypes.ELECTRIC_SPARK,getX(),getY()+0.3F,getZ(),1, 0,0,0,0);
			}

			if (tickCount > 30) {
				setReturn();
			}
	
			if (Math.max(Math.abs(getDeltaMovement().x), Math.max(Math.abs(getDeltaMovement().y), Math.abs(getDeltaMovement().z))) < 0.1) {
				setReturn();
			}
	
			if (returning) {
				List<Entity> entityTagetList = this.level().getEntitiesOfClass(Entity.class, this.getBoundingBox().inflate(1.0D, 1.0D, 1.0D));
                for (Entity entity : entityTagetList) {
                    if (entity != null && entity instanceof Player player) {
                        if (player == getProjOwner()) {
                            this.remove(RemovalReason.KILLED);
                            returnItemToPlayer();
							player.getCooldowns().addCooldown(getItem().getItem(), 20);
                        }
                    }
                }
			}
		}
	}

	public void setReturn() {
		hitSet.clear();
		if(getItem().getItem() instanceof KeybladeItem) {
			this.remove(RemovalReason.KILLED);
		} else if(getItem().getItem() instanceof ChakramItem) {
			returning = true;
			if (getProjOwner() != null)
				shoot(this.getProjOwner().getX() - this.getX(), this.getProjOwner().getY() - this.getY() + 1.25, this.getProjOwner().getZ() - this.getZ(), 2f, 0);
		} else if(getItem().getItem() instanceof ScytheItem) {
			this.remove(RemovalReason.KILLED);
			returnItemToPlayer();
		} else if (getItem().getItem() instanceof KnifeItem) {
			this.remove(RemovalReason.KILLED);
			returnItemToPlayer();
		} else if (getItem().getItem() instanceof CardItem) {
			this.remove(RemovalReason.KILLED);
			returnItemToPlayer();
		}
	}

	private void returnItemToPlayer() {
		if(owner == null)
			return;
		if(!ItemStack.isSameItem(owner.getInventory().getItem(slot),getItem())) {
			if(!ItemStack.isSameItem(owner.getInventory().getItem(slot), ItemStack.EMPTY)) {
				owner.addItem(getItem());
			} else {
				owner.getInventory().add(slot, getItem());
			}
		}		
	}

	@Override
	protected void onHit(HitResult rtRes) {
		if (!level().isClientSide) {
			EntityHitResult ertResult = null;
			BlockHitResult brtResult = null;

			if (rtRes instanceof EntityHitResult) {
				ertResult = (EntityHitResult) rtRes;
			}

			if (rtRes instanceof BlockHitResult) {
				brtResult = (BlockHitResult) rtRes;
			}

			if (ertResult != null && ertResult.getEntity() != null && ertResult.getEntity() instanceof LivingEntity target) {

                if (target != getProjOwner() && !hitSet.contains(target)) { // prevent hitting entities twice before it's returning since it removes invulnerable ticks from hit entities
					//TODO card absorbing entity?
					hitSet.add(target);
					target.invulnerableTime = 0;
					target.hurt(target.damageSources().thrown(this, this.getProjOwner()), dmg < 4 ? 4 : dmg);
					setDeltaMovement(getDeltaMovement().scale(0.8));
					dmg *= 1.2F;
					
				}
			} else { // Block (not ERTR)
				if (brtResult != null) {
					if (level().getBlockState(brtResult.getBlockPos()).getBlock() == Blocks.TALL_GRASS || level().getBlockState(brtResult.getBlockPos()).getBlock() == Blocks.GRASS_BLOCK || level().getBlockState(brtResult.getBlockPos()).getBlock() == Blocks.SUGAR_CANE || level().getBlockState(brtResult.getBlockPos()).getBlock() == Blocks.VINE) {
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
		super.onSyncedDataUpdated(key);
		if (key.equals(ROTATION_POINT)) {
			this.rotationPoint = this.entityData.get(ROTATION_POINT);
		}
	}

	@Override
	public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
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
	protected void defineSynchedData(SynchedEntityData.Builder pBuilder) {
		super.defineSynchedData(pBuilder);
		pBuilder.define(ROTATION_POINT, 0);
	}

	/*@Override
	public ItemStack getItem() {
		if (originalItem == null) {
			originalItem = entityData.get(ITEMSTACK);
		}
		return originalItem;
	}*/

	@Override
	protected Item getDefaultItem() {
		return Items.EGG;
	}

}
