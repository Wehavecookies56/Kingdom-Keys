package online.kingdomkeys.kingdomkeys.entity.organization;

import java.util.Optional;
import java.util.UUID;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ThrowableProjectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.NetworkHooks;
import net.minecraftforge.network.PlayMessages;
import online.kingdomkeys.kingdomkeys.entity.ModEntities;
import online.kingdomkeys.kingdomkeys.entity.magic.ThunderBoltEntity;

public class ThunderTrailCoreEntity extends ThrowableProjectile {

	int maxTicks = 240;
	float dmg;
	
	BlockPos ogPos;

	public ThunderTrailCoreEntity(EntityType<? extends ThrowableProjectile> type, Level world) {
		super(type, world);
		this.blocksBuilding = true;
	}

	public ThunderTrailCoreEntity(PlayMessages.SpawnEntity spawnEntity, Level world) {
		super(ModEntities.TYPE_THUNDER_TRAIL.get(), world);
	}

	public ThunderTrailCoreEntity(Level world) {
		super(ModEntities.TYPE_THUNDER_TRAIL.get(), world);
		this.blocksBuilding = true;
	}

	public ThunderTrailCoreEntity(Level world, Player player, LivingEntity target, float dmg) {
		super(ModEntities.TYPE_THUNDER_TRAIL.get(), player, world);
		setCaster(player.getUUID());
		setTarget(target.getUUID());
		this.dmg = dmg;
		setOgPos(player.blockPosition());
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
		if(getOgPos() == null) {
			return;
		}
		if (this.tickCount > maxTicks || getCaster() == null || this.distanceToSqr(getOgPos().getX(),getOgPos().getY(),getOgPos().getZ()) > 30*30) {
			this.remove(RemovalReason.KILLED);
		}
		
		if (getCaster() != null) {
			if(tickCount % 3 == 0) {
				ThunderBoltEntity shot = new ThunderBoltEntity(getCaster().level, getCaster(), getX(), getY()-1, getZ(), dmg);
				shot.setCaster(getCaster().getUUID());
				level.addFreshEntity(shot);

				LightningBolt lightningBoltEntity = EntityType.LIGHTNING_BOLT.create(this.level);
				lightningBoltEntity.setVisualOnly(true);
				lightningBoltEntity.moveTo(Vec3.atBottomCenterOf(blockPosition().below()));
				lightningBoltEntity.setCause(getCaster() instanceof ServerPlayer ? (ServerPlayer) getCaster() : null);
				this.level.addFreshEntity(lightningBoltEntity);
			}
		}
		super.tick();
	}

	@Override
	protected void onHit(HitResult rtRes) {
		//remove(RemovalReason.KILLED);
	}

	public int getMaxTicks() {
		return maxTicks;
	}

	public void setMaxTicks(int maxTicks) {
		this.maxTicks = maxTicks;
	}

	@Override
	public void addAdditionalSaveData(CompoundTag compound) {
		super.addAdditionalSaveData(compound);
		if (this.entityData.get(OWNER) != null) {
			compound.putString("OwnerUUID", this.entityData.get(OWNER).get().toString());
			compound.putString("TargetUUID", this.entityData.get(TARGET).get().toString());
			int[] intArray = new int[] {this.entityData.get(OGPOS).getX(),this.entityData.get(OGPOS).getY(),this.entityData.get(OGPOS).getZ()};
			compound.putIntArray("OgPos", intArray);
		}
	}

	@Override
	public void readAdditionalSaveData(CompoundTag compound) {
		super.readAdditionalSaveData(compound);
		this.entityData.set(OWNER, Optional.of(UUID.fromString(compound.getString("OwnerUUID"))));
		this.entityData.set(TARGET, Optional.of(UUID.fromString(compound.getString("TargetUUID"))));
		int[] coords = compound.getIntArray("OgPos");
		BlockPos blockpos = new BlockPos(coords[0],coords[1],coords[2]);
		this.entityData.set(OGPOS, blockpos);
	}

	private static final EntityDataAccessor<Optional<UUID>> OWNER = SynchedEntityData.defineId(ThunderTrailCoreEntity.class, EntityDataSerializers.OPTIONAL_UUID);
	private static final EntityDataAccessor<Optional<UUID>> TARGET = SynchedEntityData.defineId(ThunderTrailCoreEntity.class, EntityDataSerializers.OPTIONAL_UUID);
	private static final EntityDataAccessor<BlockPos> OGPOS = SynchedEntityData.defineId(ThunderTrailCoreEntity.class, EntityDataSerializers.BLOCK_POS);

	public Player getCaster() {
		return this.getEntityData().get(OWNER).isPresent() ? this.level.getPlayerByUUID(this.getEntityData().get(OWNER).get()) : null;
	}

	public void setCaster(UUID uuid) {
		this.entityData.set(OWNER, Optional.of(uuid));
	}

	public BlockPos getOgPos() {
		return this.getEntityData().get(OGPOS);
	}

	public void setOgPos(BlockPos blockpos) {
		this.entityData.set(OGPOS, blockpos);
	}
	
	public Player getTarget() {
		return this.getEntityData().get(TARGET).isPresent() ? this.level.getPlayerByUUID(this.getEntityData().get(TARGET).get()) : null;
	}

	public void setTarget(UUID uuid) {
		this.entityData.set(TARGET, Optional.of(uuid));
	}

	@Override
	protected void defineSynchedData() {
		this.entityData.define(OWNER, Optional.of(new UUID(0L, 0L)));
		this.entityData.define(TARGET, Optional.of(new UUID(0L, 0L)));
		this.entityData.define(OGPOS, new BlockPos(0,0,0));
	}
}
