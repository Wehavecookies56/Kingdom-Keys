package online.kingdomkeys.kingdomkeys.entity.organization;

import java.util.Optional;
import java.util.UUID;

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
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkHooks;
import net.minecraftforge.network.PlayMessages;
import online.kingdomkeys.kingdomkeys.client.sound.ModSounds;
import online.kingdomkeys.kingdomkeys.config.ModConfigs;
import online.kingdomkeys.kingdomkeys.entity.ModEntities;

public class ArrowRainCoreEntity extends ThrowableProjectile {

	int maxTicks = 240;
	float dmg;

	double dmgMult;
	float radius;
	float space;

	public ArrowRainCoreEntity(EntityType<? extends ThrowableProjectile> type, Level world) {
		super(type, world);
		this.blocksBuilding = true;
	}

	public ArrowRainCoreEntity(PlayMessages.SpawnEntity spawnEntity, Level world) {
		super(ModEntities.TYPE_ARROW_RAIN.get(), world);
	}

	public ArrowRainCoreEntity(Level world) {
		super(ModEntities.TYPE_ARROW_RAIN.get(), world);
		this.blocksBuilding = true;
	}

	public ArrowRainCoreEntity(Level world, Player player, LivingEntity target, float dmg) {
		super(ModEntities.TYPE_ARROW_RAIN.get(), player, world);
		setCaster(player.getUUID());
		setTarget(target.getUUID());
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
		if (this.tickCount > maxTicks || getCaster() == null) {
			this.remove(RemovalReason.KILLED);
		}
		
		this.setDeltaMovement(0, 0, 0);
		this.hurtMarked = true;

		this.dmgMult = ModConfigs.limitArrowRainMult;

		// world.addParticle(ParticleTypes.ENTITY_EFFECT, getPosX(), getPosY(),
		// getPosZ(), 1, 1, 0);
		level.addParticle(ParticleTypes.BUBBLE, getX(), getY(), getZ(), 0, 0, 0);

		double X = getX();
		double Y = getY();
		double Z = getZ();

		if (getCaster() != null) {
			if (tickCount == 1) {
				LaserDomeShotEntity bullet = new LaserDomeShotEntity(level, getCaster(), dmg * dmgMult);
				bullet.setPos(X, Y, Z);
				bullet.setMaxTicks(30);
				bullet.shoot(0, 255, 0, 1f, 0);
				level.addFreshEntity(bullet);
				level.playSound(getCaster(), getCaster().blockPosition(), ModSounds.sharpshooterbullet.get(), SoundSource.PLAYERS, 1F, 0.6F);

			} else if (tickCount > 40 && tickCount % 2 == 0) { // Get all targets right before starting to shoot
				radius = Math.min((tickCount-34) / 10F, 20);
				space = 20 + 6 - radius;
				for (int s = 1; s < 360; s += space) {
					double x = X + (radius * Math.cos(Math.toRadians(s)));
					double z = Z + (radius * Math.sin(Math.toRadians(s)));
					LaserDomeShotEntity bullet = new LaserDomeShotEntity(level, getCaster(), dmg * dmgMult);
					bullet.setPos(X, Y + 27, Z);
					bullet.setMaxTicks(20);
					bullet.shoot(x - bullet.getX(), this.getY() - bullet.getY()+1, z - bullet.getZ(), 2.5f, 0);
					//list.add(bullet);
					level.addFreshEntity(bullet);
				}
				level.playSound(getCaster(), getCaster().blockPosition(), ModSounds.sharpshooterbullet.get(), SoundSource.PLAYERS, 1F, 1F);	
			}
		}
		super.tick();
	}

	@Override
	protected void onHit(HitResult rtRes) {
		remove(RemovalReason.KILLED);
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
		}
	}

	@Override
	public void readAdditionalSaveData(CompoundTag compound) {
		super.readAdditionalSaveData(compound);
		this.entityData.set(OWNER, Optional.of(UUID.fromString(compound.getString("OwnerUUID"))));
		this.entityData.set(TARGET, Optional.of(UUID.fromString(compound.getString("TargetUUID"))));
	}

	private static final EntityDataAccessor<Optional<UUID>> OWNER = SynchedEntityData.defineId(ArrowRainCoreEntity.class, EntityDataSerializers.OPTIONAL_UUID);
	private static final EntityDataAccessor<Optional<UUID>> TARGET = SynchedEntityData.defineId(ArrowRainCoreEntity.class, EntityDataSerializers.OPTIONAL_UUID);

	public Player getCaster() {
		return this.getEntityData().get(OWNER).isPresent() ? this.level.getPlayerByUUID(this.getEntityData().get(OWNER).get()) : null;
	}

	public void setCaster(UUID uuid) {
		this.entityData.set(OWNER, Optional.of(uuid));
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
	}
}
