package online.kingdomkeys.kingdomkeys.entity.organization;

import java.util.List;

import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ThrowableProjectile;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkHooks;
import net.minecraftforge.network.PlayMessages;
import online.kingdomkeys.kingdomkeys.entity.ModEntities;

public class ChakramEntity extends ThrowableProjectile{

	int maxTicks = 120;
	boolean returning = false;
	String model;
	int rotationPoint; //0 = x, 1 = y, 2 = z
	float dmg;

	public ChakramEntity(EntityType<? extends ThrowableProjectile> type, Level world) {
		super(type, world);
		this.blocksBuilding = true;
	}

	public ChakramEntity(PlayMessages.SpawnEntity spawnEntity, Level world) {
		super(ModEntities.TYPE_CHAKRAM.get(), world);
	}

	public ChakramEntity(Level world) {
		super(ModEntities.TYPE_CHAKRAM.get(), world);
		this.blocksBuilding = true;
	}

	public ChakramEntity(Level world, Player player, String model, float dmg) {
		super(ModEntities.TYPE_CHAKRAM.get(), player, world);
		setOwner(player);
		setModel(model);
		this.dmg = dmg;
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
		if (this.tickCount > maxTicks) {
			this.remove(RemovalReason.KILLED);
		}
		
		if(tickCount > maxTicks / 3) {
			setReturn();
		}

		if(Math.max(Math.abs(getDeltaMovement().x),Math.max(Math.abs(getDeltaMovement().y),Math.abs(getDeltaMovement().z))) < 0.1) {
			setReturn();
		}

		if (tickCount > 2)
			level.addParticle(ParticleTypes.FLAME, getX(), getY()+0.25, getZ(), 0, 0, 0);

		if (returning) {
			List entityTagetList = this.level.getEntitiesOfClass(Entity.class, this.getBoundingBox().inflate(1.0D, 1.0D, 1.0D));
			for (int i = 0; i < entityTagetList.size(); i++) {
				Entity entityTarget = (Entity) entityTagetList.get(i);
				if (entityTarget != null && entityTarget instanceof Player) {
					Player owner = (Player) entityTarget;
					if (owner == getOwner()) {
						this.remove(RemovalReason.KILLED);
					}
				}
			}
		}
		super.tick();
	}

	public void setReturn() {
		returning = true;
		if(getOwner() != null)
			shoot(this.getOwner().getX() - this.getX(), this.getOwner().getY() - this.getY() + 1.25, this.getOwner().getZ() - this.getZ(), 2f, 0);
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
				if (target != getOwner()) {
					//target.setFire(5);
					target.hurt(DamageSource.thrown(this, this.getOwner()), dmg < 4 ? 4 : dmg);
					//setReturn();
					setDeltaMovement(getDeltaMovement().scale(0.5));
					dmg *= 0.9;
				}
			} else { // Block (not ERTR)
				if(brtResult != null) {
					
					if(level.getBlockState(brtResult.getBlockPos()).getBlock() == Blocks.TALL_GRASS || level.getBlockState(brtResult.getBlockPos()).getBlock() == Blocks.GRASS || level.getBlockState(brtResult.getBlockPos()).getBlock() == Blocks.SUGAR_CANE || level.getBlockState(brtResult.getBlockPos()).getBlock() == Blocks.VINE) {
					
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
	
	private static final EntityDataAccessor<String> MODEL = SynchedEntityData.defineId(ChakramEntity.class, EntityDataSerializers.STRING);
	private static final EntityDataAccessor<Integer> ROTATION_POINT = SynchedEntityData.defineId(ChakramEntity.class, EntityDataSerializers.INT);
	
	public String getModel() {
		return model;
	}

	public void setModel(String name) {
		this.entityData.set(MODEL, name);
		this.model = name;
	}
	
	public int getRotationPoint() {
		return rotationPoint;
	}
	
	public void setRotationPoint(int rotations) {
		this.entityData.set(ROTATION_POINT, rotations);
		this.rotationPoint = rotations;
	}

	@Override
	public void onSyncedDataUpdated(EntityDataAccessor<?> key) {
		if (key.equals(MODEL)) {
			this.model = this.getModelDataManager();
		}
		if (key.equals(ROTATION_POINT)) {
			this.rotationPoint = this.getRotationPointDataManager();
		}
	}
	
	@Override
	public void addAdditionalSaveData(CompoundTag compound) {
		compound.putString("Model", this.getModel());
		compound.putInt("Rotation", this.getRotationPoint());
	}

	@Override
	public void readAdditionalSaveData(CompoundTag compound) {
		this.setModel(compound.getString("Model"));
		this.setRotationPoint(compound.getInt("Rotation"));
	}
	

	@Override
	protected void defineSynchedData() {
		this.entityData.define(MODEL, "");
		this.entityData.define(ROTATION_POINT, 0);
	}

	public String getModelDataManager() {
		return this.entityData.get(MODEL);
	}
	
	public int getRotationPointDataManager() {
		return this.entityData.get(ROTATION_POINT);
	}
	
}
