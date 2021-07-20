package online.kingdomkeys.kingdomkeys.entity.shotlock;

import java.util.Optional;
import java.util.UUID;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ThrowableEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.FMLPlayMessages;
import net.minecraftforge.fml.network.NetworkHooks;
import online.kingdomkeys.kingdomkeys.entity.ModEntities;

public class BaseShotlockShotEntity extends ThrowableEntity{
	
	int maxTicks = 100;
	public float dmg;
	Entity target;

	public BaseShotlockShotEntity(EntityType<? extends ThrowableEntity> type, World world) {
		super(type, world);
		this.preventEntitySpawning = true;
	}

	public BaseShotlockShotEntity(FMLPlayMessages.SpawnEntity spawnEntity, World world) {
		super(ModEntities.TYPE_VOLLEY_SHOTLOCK_SHOT.get(), world);
	}

	public BaseShotlockShotEntity(World world) {
		super(ModEntities.TYPE_VOLLEY_SHOTLOCK_SHOT.get(), world);
		this.preventEntitySpawning = true;
	}

	public BaseShotlockShotEntity(EntityType<? extends ThrowableEntity> type, World world, LivingEntity player, Entity target, double dmg) {
		super(type, player, world);
		this.dmg = (float)dmg;
		setTarget(target.getEntityId());
	}

	@Override
	public IPacket<?> createSpawnPacket() {
		return NetworkHooks.getEntitySpawningPacket(this);
	}

	@Override
	protected float getGravityVelocity() {
		return 0F;
	}

	@Override
	public void remove() {
		if(ticksExisted > 20) {
			super.remove();
		}
	}

	public int getMaxTicks() {
		return maxTicks;
	}

	public void setMaxTicks(int maxTicks) {
		this.maxTicks = maxTicks;
	}

	@Override
	public void writeAdditional(CompoundNBT compound) {
		super.writeAdditional(compound);
		if (this.dataManager.get(OWNER) != null) {
			compound.putString("OwnerUUID", this.dataManager.get(OWNER).get().toString());
			compound.putInt("TargetUUID", this.dataManager.get(TARGET));
			compound.putInt("Color", this.dataManager.get(COLOR));
		}
	}

	@Override
	public void readAdditional(CompoundNBT compound) {
		super.readAdditional(compound);
		this.dataManager.set(OWNER, Optional.of(UUID.fromString(compound.getString("OwnerUUID"))));
		this.dataManager.set(TARGET, compound.getInt("TargetUUID"));
		this.dataManager.set(COLOR, compound.getInt("Color"));
	}

	private static final DataParameter<Optional<UUID>> OWNER = EntityDataManager.createKey(DarkVolleyCoreEntity.class, DataSerializers.OPTIONAL_UNIQUE_ID);
	private static final DataParameter<Integer> TARGET = EntityDataManager.createKey(DarkVolleyCoreEntity.class, DataSerializers.VARINT);
	private static final DataParameter<Integer> COLOR = EntityDataManager.createKey(DarkVolleyCoreEntity.class, DataSerializers.VARINT);

	public PlayerEntity getCaster() {
		return this.getDataManager().get(OWNER).isPresent() ? this.world.getPlayerByUuid(this.getDataManager().get(OWNER).get()) : null;
	}

	public void setCaster(UUID uuid) {
		this.dataManager.set(OWNER, Optional.of(uuid));
	}

	public Entity getTarget() {
		return this.world.getEntityByID(this.getDataManager().get(TARGET));
	}

	public void setTarget(int i) {
		this.dataManager.set(TARGET, i);
	}

	public int getColor() {
		return this.getDataManager().get(COLOR);
	}
	
	public void setColor(int color) {
		this.dataManager.set(COLOR, color);
	}
	
	@Override
	protected void registerData() {
		this.dataManager.register(OWNER, Optional.of(new UUID(0L, 0L)));
		this.dataManager.register(TARGET, 0);
		this.dataManager.register(COLOR, 0);
	}

}
