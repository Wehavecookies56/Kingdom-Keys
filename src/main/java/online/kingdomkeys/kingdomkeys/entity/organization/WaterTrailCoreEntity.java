package online.kingdomkeys.kingdomkeys.entity.organization;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ThrowableProjectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.HitResult;
import online.kingdomkeys.kingdomkeys.damagesource.KKDamageTypes;
import online.kingdomkeys.kingdomkeys.entity.ModEntities;
import online.kingdomkeys.kingdomkeys.util.Utils;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class WaterTrailCoreEntity extends ThrowableProjectile {
	int maxTicks = 240;
	float dmg;

	public WaterTrailCoreEntity(EntityType<? extends ThrowableProjectile> type, Level world) {
		super(type, world);
		this.blocksBuilding = true;
	}

	public WaterTrailCoreEntity(Level world, Player player, LivingEntity target, float dmg) {
		super(ModEntities.TYPE_WATER_TRAIL.get(), player, world);
		setCaster(player.getUUID());
		setTarget(target.getUUID());
		this.dmg = dmg;
		setOgPos(player.blockPosition());
	}

	@Override
	protected double getDefaultGravity() {
		return 0D;
	}

	@Override
	public void tick() {
		if (getOgPos() == null) {
			return;
		}
		if (this.tickCount > maxTicks || getCaster() == null || this.distanceToSqr(getOgPos().getX(), getOgPos().getY(), getOgPos().getZ()) > 30 * 30) {
			this.remove(RemovalReason.KILLED);
		}

		if (getCaster() != null && !level().isClientSide) {
			if (tickCount % 3 == 0) {
				spawnWaterColumn();
				damageNearby(getCaster());
			}
		}
		super.tick();
	}

	private void spawnWaterColumn() {
		if (!(level() instanceof ServerLevel serverLevel)) return;
		serverLevel.sendParticles(ParticleTypes.SPLASH, getX(), getY(), getZ(), 15, 0.3, 0.05, 0.3, 0.05);
		for (double dy = 0D; dy <= 2.5D; dy += 0.5D) {
			serverLevel.sendParticles(ParticleTypes.SPLASH, getX(), getY() + dy, getZ(), 2, 0.15, 0.02, 0.15, 0.01);
			serverLevel.sendParticles(ParticleTypes.BUBBLE_COLUMN_UP, getX(), getY() + dy, getZ(), 2, 0.15, 0.02, 0.15, 0.01);
		}
		level().playSound(null, blockPosition(), SoundEvents.GENERIC_SPLASH, SoundSource.PLAYERS, 0.6F, 1.1F);
	}

	private void damageNearby(Player caster) {
		List<LivingEntity> nearby = Utils.getLivingEntitiesInRadiusExcludingParty(caster, this, 1.1F, 1.6F, 1.1F);
		for (LivingEntity target : nearby) {
			target.hurt(KKDamageTypes.getElementalDamage(KKDamageTypes.WATER, this, caster), dmg);
			target.invulnerableTime = 0;
		}
	}

	@Override
	protected void onHit(HitResult rtRes) {}

	public int getMaxTicks() {
		return maxTicks;
	}

	public void setMaxTicks(int maxTicks) {
		this.maxTicks = maxTicks;
	}

	@Override
	public void addAdditionalSaveData(CompoundTag compound) {
		super.addAdditionalSaveData(compound);
		if (this.entityData.get(OWNER).isPresent()) {
			compound.putString("OwnerUUID", this.entityData.get(OWNER).get().toString());
			compound.putString("TargetUUID", this.entityData.get(TARGET).get().toString());
			int[] intArray = new int[]{this.entityData.get(OGPOS).getX(), this.entityData.get(OGPOS).getY(), this.entityData.get(OGPOS).getZ()};
			compound.putIntArray("OgPos", intArray);
		}
	}

	@Override
	public void readAdditionalSaveData(CompoundTag compound) {
		super.readAdditionalSaveData(compound);
		if (compound.contains("OwnerUUID")) {
			this.entityData.set(OWNER, Optional.of(UUID.fromString(compound.getString("OwnerUUID"))));
		}
		if (compound.contains("TargetUUID")) {
			this.entityData.set(TARGET, Optional.of(UUID.fromString(compound.getString("TargetUUID"))));
		}
		if (compound.contains("OgPos")) {
			int[] coords = compound.getIntArray("OgPos");
			this.entityData.set(OGPOS, new BlockPos(coords[0], coords[1], coords[2]));
		}
	}

	private static final EntityDataAccessor<Optional<UUID>> OWNER = SynchedEntityData.defineId(WaterTrailCoreEntity.class, EntityDataSerializers.OPTIONAL_UUID);
	private static final EntityDataAccessor<Optional<UUID>> TARGET = SynchedEntityData.defineId(WaterTrailCoreEntity.class, EntityDataSerializers.OPTIONAL_UUID);
	private static final EntityDataAccessor<BlockPos> OGPOS = SynchedEntityData.defineId(WaterTrailCoreEntity.class, EntityDataSerializers.BLOCK_POS);

	public Player getCaster() {
		return this.getEntityData().get(OWNER).isPresent() ? this.level().getPlayerByUUID(this.getEntityData().get(OWNER).get()) : null;
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

	public void setTarget(UUID uuid) {
		this.entityData.set(TARGET, Optional.of(uuid));
	}

	@Override
	protected void defineSynchedData(SynchedEntityData.Builder pBuilder) {
		pBuilder.define(OWNER, Optional.of(new UUID(0L, 0L)));
		pBuilder.define(TARGET, Optional.of(new UUID(0L, 0L)));
		pBuilder.define(OGPOS, new BlockPos(0, 0, 0));
	}
}
