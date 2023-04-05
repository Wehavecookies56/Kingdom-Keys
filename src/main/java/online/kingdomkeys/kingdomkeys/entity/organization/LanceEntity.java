package online.kingdomkeys.kingdomkeys.entity.organization;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.entity.projectile.ThrowableProjectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.TheEndGatewayBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.NetworkHooks;
import net.minecraftforge.network.PlayMessages;
import online.kingdomkeys.kingdomkeys.entity.ModEntities;

public class LanceEntity extends ThrowableProjectile{

	int maxTicks = 100;
	String model;
	boolean stopped = false;
	int rotationPoint; //0 = x, 1 = y, 2 = z
	float dmg = 0;
	
	public LanceEntity(EntityType<? extends ThrowableProjectile> type, Level world) {
		super(type, world);
		this.blocksBuilding = true;
	}

	public LanceEntity(PlayMessages.SpawnEntity spawnEntity, Level world) {
		super(ModEntities.TYPE_LANCE.get(), world);
	}

	public LanceEntity(Level world) {
		super(ModEntities.TYPE_LANCE.get(), world);
		this.blocksBuilding = true;
	}

	public LanceEntity(Level world, Player player, String model, float dmg) {
		super(ModEntities.TYPE_LANCE.get(), player, world);
		setOwner(player);
		setModel(model);
		this.dmg = dmg;
	}

	@Override
	public Packet<ClientGamePacketListener> getAddEntityPacket() {
		return (Packet<ClientGamePacketListener>) NetworkHooks.getEntitySpawningPacket(this);
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

		if (!isStopped()) {
			if (tickCount > 2)
				level.addParticle(ParticleTypes.CRIT, getX(), getY(), getZ(), 0, 0, 0);

			if (this.isOnGround()) {
				this.setOnGround(false);
				this.setDeltaMovement(this.getDeltaMovement().multiply((double) (this.random.nextFloat() * 0.2F), (double) (this.random.nextFloat() * 0.2F), (double) (this.random.nextFloat() * 0.2F)));
			}

			HitResult raytraceresult = ProjectileUtil.getHitResult(this, this::canHitEntity);
			boolean flag = false;
			if (raytraceresult.getType() == HitResult.Type.BLOCK) {
				BlockPos blockpos = ((BlockHitResult) raytraceresult).getBlockPos();
				BlockState blockstate = this.level.getBlockState(blockpos);
				if (blockstate.is(Blocks.NETHER_PORTAL)) {
					this.handleInsidePortal(blockpos);
					flag = true;
				} else if (blockstate.is(Blocks.END_GATEWAY)) {
					BlockEntity tileentity = this.level.getBlockEntity(blockpos);
					if (tileentity instanceof TheEndGatewayBlockEntity && TheEndGatewayBlockEntity.canEntityTeleport(this)) {
						((TheEndGatewayBlockEntity) tileentity).teleportEntity(this.level, blockpos, blockstate, this, (TheEndGatewayBlockEntity) tileentity);
					}

					flag = true;
				} else {
					setStopped(true);
				}
			}

			if (raytraceresult.getType() != HitResult.Type.MISS && !flag && !net.minecraftforge.event.ForgeEventFactory.onProjectileImpact(this, raytraceresult)) {
				this.onHit(raytraceresult);
			}

			if (!stopped) {
				Vec3 vec3d = this.getDeltaMovement();
				double d0 = this.getX() + vec3d.x;
				double d1 = this.getY() + vec3d.y;
				double d2 = this.getZ() + vec3d.z;
				float f1;
				if (this.isInWater()) {
					for (int i = 0; i < 4; ++i) {
						this.level.addParticle(ParticleTypes.BUBBLE, d0 - vec3d.x * 0.25D, d1 - vec3d.y * 0.25D, d2 - vec3d.z * 0.25D, vec3d.x, vec3d.y, vec3d.z);
					}

					f1 = 0.8F;
				} else {
					f1 = 0.99F;
				}

				this.setDeltaMovement(vec3d.scale((double) f1));
				if (!this.isNoGravity()) {
					Vec3 vec3d1 = this.getDeltaMovement();
					this.setDeltaMovement(vec3d1.x, vec3d1.y - (double) this.getGravity(), vec3d1.z);
				}

				this.setPos(d0, d1, d2);
			}
		}

	}
	
	public void stopLance(){
		setStopped(true);
		this.setDeltaMovement(0, 0, 0);
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
	            	target.hurt(target.damageSources().thrown(this, this.getOwner()), dmg < 4 ? 4 : dmg);
					dmg *= 0.8F;
					//stopLance();
				}
			} else { // Block (not ERTR)
				if(brtResult != null) {
					
					if(level.getBlockState(brtResult.getBlockPos()).getBlock() == Blocks.TALL_GRASS || level.getBlockState(brtResult.getBlockPos()).getBlock() == Blocks.GRASS || level.getBlockState(brtResult.getBlockPos()).getBlock() == Blocks.SUGAR_CANE) {
					
					} else {
						stopLance();	
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
	
	private static final EntityDataAccessor<String> MODEL = SynchedEntityData.defineId(LanceEntity.class, EntityDataSerializers.STRING);
	private static final EntityDataAccessor<Boolean> STOPPED = SynchedEntityData.defineId(LanceEntity.class, EntityDataSerializers.BOOLEAN);
	private static final EntityDataAccessor<Integer> ROTATION_POINT = SynchedEntityData.defineId(LanceEntity.class, EntityDataSerializers.INT);
	
	public String getModel() {
		return model;
	}

	public void setModel(String name) {
		this.entityData.set(MODEL, name);
		this.model = name;
	}

	public boolean isStopped() {
		return stopped;
	}

	public void setStopped(boolean stopped) {
		this.entityData.set(STOPPED, stopped);
		this.stopped = stopped;
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
		if (key.equals(STOPPED)) {
			this.stopped = this.getStoppedDataManager();
		}
		if (key.equals(ROTATION_POINT)) {
			this.rotationPoint = this.getRotationPointDataManager();
		}
	}
	
	@Override
	public void addAdditionalSaveData(CompoundTag compound) {
		compound.putString("Model", this.getModel());
		compound.putBoolean("Stopped", this.isStopped());
		compound.putInt("Rotation", this.getRotationPoint());

	}

	@Override
	public void readAdditionalSaveData(CompoundTag compound) {
		this.setModel(compound.getString("Model"));
		this.setStopped(compound.getBoolean("Stopped"));
		this.setRotationPoint(compound.getInt("Rotation"));
	}
	

	@Override
	protected void defineSynchedData() {
		this.entityData.define(MODEL, "");
		this.entityData.define(STOPPED, false);
		this.entityData.define(ROTATION_POINT, 0);
	}

	public String getModelDataManager() {
		return this.entityData.get(MODEL);
	}
	public boolean getStoppedDataManager() {
		return this.entityData.get(STOPPED);
	}
	public int getRotationPointDataManager() {
		return this.entityData.get(ROTATION_POINT);
	}
	
}
