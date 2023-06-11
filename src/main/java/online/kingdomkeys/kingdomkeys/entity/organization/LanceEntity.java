package online.kingdomkeys.kingdomkeys.entity.organization;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ProjectileUtil;
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

public class LanceEntity extends KKThrowableEntity{

	int maxTicks = 100;
	boolean stopped = false;
	
	public LanceEntity(EntityType<? extends KKThrowableEntity> type, Level world) {
		super(world);
		this.blocksBuilding = true;
	}

	public LanceEntity(PlayMessages.SpawnEntity spawnEntity, Level world) {
		super(world);
	}

	public LanceEntity(Level world) {
		super(world);
		this.blocksBuilding = true;
	}

	public LanceEntity(Level world, Player player, float dmg) {
		super(world);
		setOwner(player);
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
		this.setDeltaMovement(this.getDeltaMovement().scale(0.001));
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
					target.hurt(DamageSource.thrown(this, this.getOwner()), dmg < 4 ? 4 : dmg);
					dmg *= 0.8F;
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
	
	private static final EntityDataAccessor<Boolean> STOPPED = SynchedEntityData.defineId(LanceEntity.class, EntityDataSerializers.BOOLEAN);

	public boolean isStopped() {
		return stopped;
	}

	public void setStopped(boolean stopped) {
		this.entityData.set(STOPPED, stopped);
		this.stopped = stopped;
	}
	
	@Override
	public void onSyncedDataUpdated(EntityDataAccessor<?> key) {
		if (key.equals(STOPPED)) {
			this.stopped = this.getStoppedDataManager();
		}
		super.onSyncedDataUpdated(key);
	}
	
	@Override
	public void addAdditionalSaveData(CompoundTag compound) {
		compound.putBoolean("Stopped", this.isStopped());
		super.addAdditionalSaveData(compound);
	}

	@Override
	public void readAdditionalSaveData(CompoundTag compound) {
		this.setStopped(compound.getBoolean("Stopped"));
		this.readAdditionalSaveData(compound);
	}	

	@Override
	protected void defineSynchedData() {
		this.entityData.define(STOPPED, false);
		super.defineSynchedData();
	}

	
	public boolean getStoppedDataManager() {
		return this.entityData.get(STOPPED);
	}
	
	
}
