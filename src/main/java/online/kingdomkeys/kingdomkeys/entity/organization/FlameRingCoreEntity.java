package online.kingdomkeys.kingdomkeys.entity.organization;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ThrowableProjectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import online.kingdomkeys.kingdomkeys.client.sound.ModSounds;
import online.kingdomkeys.kingdomkeys.damagesource.KKDamageTypes;
import online.kingdomkeys.kingdomkeys.entity.ModEntities;
import online.kingdomkeys.kingdomkeys.util.Utils;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class FlameRingCoreEntity extends ThrowableProjectile {
	private static final int PULSE_INTERVAL_TICKS = 5;
	private static final int MAX_TICKS = 30; // ~2.5s of pulses
	private static final float RING_BAND = 0.75F; // how thick the damage band around the current radius is
	private static final float MAX_RADIUS = 5F;
	private static final int FIRE_TICKS_APPLIED = 40; // 2s on fire

	private float dmg;

	public FlameRingCoreEntity(EntityType<? extends ThrowableProjectile> type, Level world) {
		super(type, world);
		this.blocksBuilding = false;
	}

	public FlameRingCoreEntity(Level world, Player caster, float dmg) {
		super(ModEntities.TYPE_FLAME_RING.get(), caster, world);
		this.dmg = dmg;
		this.setDeltaMovement(Vec3.ZERO);
		setCaster(caster.getUUID());
	}

	@Override
	protected double getDefaultGravity() {
		return 0D;
	}

	@Override
	public void tick() {
		if (this.tickCount > MAX_TICKS || !(getOwner() instanceof Player caster) || !caster.isAlive()) {
			this.remove(RemovalReason.KILLED);
			return;
		}

		if (!level().isClientSide) {
			if (tickCount % PULSE_INTERVAL_TICKS == 0) {
				// Ring grows outward over the duration of the limit
				float progress = (float) tickCount / MAX_TICKS;
				float radius = 0.5F + progress * (MAX_RADIUS - 0.5F);

				spawnRingParticles(radius);
				damageEntitiesInBand(caster, radius);

				level().playSound(null, caster.blockPosition(), ModSounds.fire.get(), SoundSource.PLAYERS, 0.6F, 1.1F);
			}
		}

		super.tick();
	}

	private void spawnRingParticles(float radius) {
		if (!(level() instanceof ServerLevel serverLevel)) return;
		int points = 16;
		for (int i = 0; i < points; i++) {
			double angle = (2 * Math.PI / points) * i;
			double x = getX() + radius * Math.cos(angle);
			double z = getZ() + radius * Math.sin(angle);
			serverLevel.sendParticles(ParticleTypes.FLAME, x, getY() + 0.1D, z, 1, 0, 0.02, 0, 0.01);
		}
	}

	private void damageEntitiesInBand(Player caster, float radius) {
		List<LivingEntity> nearby = Utils.getLivingEntitiesInRadiusExcludingParty(caster, radius + RING_BAND);
		for (LivingEntity target : nearby) {
			double distance = target.distanceTo(caster);
			// Only hit things currently within the moving ring band, not everything inside the max radius
			if (distance >= radius - RING_BAND && distance <= radius + RING_BAND) {
				target.hurt(KKDamageTypes.getElementalDamage(KKDamageTypes.FIRE, this, caster), dmg);
				target.setRemainingFireTicks(Math.max(target.getRemainingFireTicks(), FIRE_TICKS_APPLIED));
				target.invulnerableTime = 0;
			}
		}
	}

	@Override
	protected void onHit(HitResult result) {}

	private static final EntityDataAccessor<Optional<UUID>> OWNER = SynchedEntityData.defineId(FlameRingCoreEntity.class, EntityDataSerializers.OPTIONAL_UUID);

	@Override
	public void addAdditionalSaveData(CompoundTag compound) {
		super.addAdditionalSaveData(compound);
		if (this.entityData.get(OWNER).isPresent()) {
			compound.putString("OwnerUUID", this.entityData.get(OWNER).get().toString());
		}
	}

	@Override
	public void readAdditionalSaveData(CompoundTag compound) {
		super.readAdditionalSaveData(compound);
		this.entityData.set(OWNER, Optional.of(UUID.fromString(compound.getString("OwnerUUID"))));
	}

	public Player getCaster() {
		return this.getEntityData().get(OWNER).isPresent() ? this.level().getPlayerByUUID(this.getEntityData().get(OWNER).get()) : null;
	}

	public void setCaster(UUID uuid) {
		this.entityData.set(OWNER, Optional.of(uuid));
	}

	@Override
	protected void defineSynchedData(SynchedEntityData.Builder pBuilder) {
		pBuilder.define(OWNER, Optional.empty());
	}
}
