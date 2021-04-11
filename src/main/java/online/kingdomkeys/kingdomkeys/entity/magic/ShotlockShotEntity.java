package online.kingdomkeys.kingdomkeys.entity.magic;

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
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.FMLPlayMessages;
import net.minecraftforge.fml.network.NetworkHooks;
import online.kingdomkeys.kingdomkeys.entity.ModEntities;

public class ShotlockShotEntity extends ThrowableEntity {

	int maxTicks = 220;
	float dmg;
	Entity target;
	
	public ShotlockShotEntity(EntityType<? extends ThrowableEntity> type, World world) {
		super(type, world);
		this.preventEntitySpawning = true;
	}

	public ShotlockShotEntity(FMLPlayMessages.SpawnEntity spawnEntity, World world) {
		super(ModEntities.TYPE_SHOTLOCK_SHOT.get(), world);
	}

	public ShotlockShotEntity(World world) {
		super(ModEntities.TYPE_SHOTLOCK_SHOT.get(), world);
		this.preventEntitySpawning = true;
	}

	public ShotlockShotEntity(World world, LivingEntity player, Entity target, double dmg) {
		super(ModEntities.TYPE_SHOTLOCK_SHOT.get(), player, world);
		this.dmg = (float)dmg;
		setTarget(target.getUniqueID());
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
	public void tick() {
		if (this.ticksExisted > maxTicks) {
			this.remove();
		}
		
		if(ticksExisted > 1)
			world.addParticle(ParticleTypes.ENTITY_EFFECT, getPosX(), getPosY(), getPosZ(), 1, 0, 0);
		if(ticksExisted % 4 == 0) {
			updateMovement();
			//System.out.println(getTarget());
		}
		
		super.tick();
	}

	private void updateMovement() {
		//this.shoot(this.target.getPosX(), this.target.getPosY(), this.target.getPosZ(), 1, 0);
	}

	@Override
	protected void onImpact(RayTraceResult rtRes) {
		if (!world.isRemote) {

			EntityRayTraceResult ertResult = null;
			BlockRayTraceResult brtResult = null;

			if (rtRes instanceof EntityRayTraceResult) {
				ertResult = (EntityRayTraceResult) rtRes;
			}

			if (rtRes instanceof BlockRayTraceResult) {
				brtResult = (BlockRayTraceResult) rtRes;
			}

			if (ertResult != null && ertResult.getEntity() instanceof LivingEntity) {
				LivingEntity target = (LivingEntity) ertResult.getEntity();
				if (target != func_234616_v_()) {
					target.attackEntityFrom(DamageSource.causeThrownDamage(this, this.func_234616_v_()), dmg);
					remove();
				}
			}
			remove();
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
			compound.putString("TargetUUID", this.dataManager.get(TARGET).get().toString());
		}
	}

	@Override
	public void readAdditional(CompoundNBT compound) {
		super.readAdditional(compound);
		this.dataManager.set(OWNER, Optional.of(UUID.fromString(compound.getString("OwnerUUID"))));
		this.dataManager.set(TARGET, Optional.of(UUID.fromString(compound.getString("TargetUUID"))));
	}

	private static final DataParameter<Optional<UUID>> OWNER = EntityDataManager.createKey(ShotlockCoreEntity.class, DataSerializers.OPTIONAL_UNIQUE_ID);
	private static final DataParameter<Optional<UUID>> TARGET = EntityDataManager.createKey(ShotlockCoreEntity.class, DataSerializers.OPTIONAL_UNIQUE_ID);

	public PlayerEntity getCaster() {
		return this.getDataManager().get(OWNER).isPresent() ? this.world.getPlayerByUuid(this.getDataManager().get(OWNER).get()) : null;
	}

	public void setCaster(UUID uuid) {
		this.dataManager.set(OWNER, Optional.of(uuid));
	}

	public PlayerEntity getTarget() {
		return this.getDataManager().get(TARGET).isPresent() ? this.world.getPlayerByUuid(this.getDataManager().get(TARGET).get()) : null;
	}

	public void setTarget(UUID uuid) {
		this.dataManager.set(TARGET, Optional.of(uuid));
	}

	@Override
	protected void registerData() {
		this.dataManager.register(OWNER, Optional.of(new UUID(0L, 0L)));
		this.dataManager.register(TARGET, Optional.of(new UUID(0L, 0L)));
	}
}