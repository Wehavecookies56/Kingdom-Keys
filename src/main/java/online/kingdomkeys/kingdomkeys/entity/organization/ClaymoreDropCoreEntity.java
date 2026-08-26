package online.kingdomkeys.kingdomkeys.entity.organization;

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
import net.minecraft.world.phys.Vec3;
import online.kingdomkeys.kingdomkeys.damagesource.KKDamageTypes;
import online.kingdomkeys.kingdomkeys.entity.ModEntities;
import online.kingdomkeys.kingdomkeys.util.Utils;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class ClaymoreDropCoreEntity extends ThrowableProjectile {

	private static final float TRAVEL_SPEED = 2.2F; // blocks/tick while flying out
	private static final float TRAVEL_DISTANCE = 10F; // how far it flies before planting itself
	private static final int PULSE_INTERVAL_TICKS = 6;
	private static final int PULSE_COUNT = 5; // how many pulses once planted
	private static final float PULSE_RADIUS = 2.5F;

	private float dmg;
	private Vec3 startPos = Vec3.ZERO;
	private boolean planted = false;
	private int pulsesFired = 0;
	private final java.util.Set<UUID> hitDuringFlight = new java.util.HashSet<>();

	public ClaymoreDropCoreEntity(EntityType<? extends ThrowableProjectile> type, Level world) {
		super(type, world);
		this.blocksBuilding = false;
	}

	public ClaymoreDropCoreEntity(Level world, Player caster, float dmg) {
		super(ModEntities.TYPE_CLAYMORE_DROP.get(), caster, world);
		this.dmg = dmg;
		setCaster(caster.getUUID());

		this.startPos = caster.getEyePosition();
		this.setPos(startPos.x, startPos.y, startPos.z);
		Vec3 dir = caster.getLookAngle().normalize();
		this.setDeltaMovement(dir.scale(TRAVEL_SPEED));
	}

	@Override
	protected double getDefaultGravity() {
		return 0D;
	}

	@Override
	public void tick() {
		if (!(getOwner() instanceof Player caster) || !caster.isAlive()) {
			this.remove(RemovalReason.KILLED);
			return;
		}

		if (!level().isClientSide) {
			if (!planted) {
				if (tickCount == 1) {
					level().playSound(null, caster.blockPosition(), SoundEvents.FIRECHARGE_USE, SoundSource.PLAYERS, 1F, 0.7F);
				}

				damageAnythingInThePath(caster);

				if (position().distanceTo(startPos) >= TRAVEL_DISTANCE) {
					plant(caster);
				}
			} else {
				if (tickCount % PULSE_INTERVAL_TICKS == 0) {
					pulse(caster);
					pulsesFired++;
					if (pulsesFired >= PULSE_COUNT) {
						this.remove(RemovalReason.KILLED);
						return;
					}
				}
			}
		}

		super.tick();
	}

	/** Anything the claymore flies through on its way out takes a hit too, not just the planted pulses -
	 * but only once each, so it doesn't rack up several hits just from lingering in its path for a
	 * couple of ticks. */
	private void damageAnythingInThePath(Player caster) {
		List<LivingEntity> nearby = Utils.getLivingEntitiesInRadiusExcludingParty(caster, this, 1.2F, 1.2F, 1.2F);
		for (LivingEntity target : nearby) {
			if (hitDuringFlight.contains(target.getUUID())) continue;
			target.hurt(KKDamageTypes.getElementalDamage(KKDamageTypes.DARKNESS, this, caster), dmg);
			target.invulnerableTime = 0;
			hitDuringFlight.add(target.getUUID());
		}
	}

	private void plant(Player caster) {
		planted = true;
		this.setDeltaMovement(Vec3.ZERO);
		level().playSound(null, blockPosition(), SoundEvents.ANVIL_LAND, SoundSource.PLAYERS, 0.7F, 1.6F);
		spawnPlantParticles();
	}

	public boolean isPlanted() {
		return this.planted;
	}

	private void pulse(Player caster) {
		spawnPlantParticles();
		level().playSound(null, blockPosition(), SoundEvents.BEACON_AMBIENT, SoundSource.PLAYERS, 0.6F, 1.8F);

		List<LivingEntity> nearby = Utils.getLivingEntitiesInRadiusExcludingParty(caster, this, PULSE_RADIUS, PULSE_RADIUS, PULSE_RADIUS);
		for (LivingEntity target : nearby) {
			target.hurt(KKDamageTypes.getElementalDamage(KKDamageTypes.DARKNESS, this, caster), dmg);
			target.invulnerableTime = 0;
		}
	}

	private void spawnPlantParticles() {
		if (!(level() instanceof ServerLevel serverLevel)) return;
		serverLevel.sendParticles(ParticleTypes.SOUL, getX(), getY() + 0.2D, getZ(), 12, 0.6, 0.3, 0.6, 0.02);
		serverLevel.sendParticles(ParticleTypes.END_ROD, getX(), getY() + 0.2D, getZ(), 6, 0.4, 0.2, 0.4, 0.01);
	}

	@Override
	protected void onHit(HitResult result) {}

	private static final EntityDataAccessor<Optional<UUID>> OWNER = SynchedEntityData.defineId(ClaymoreDropCoreEntity.class, EntityDataSerializers.OPTIONAL_UUID);

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
		if (compound.contains("OwnerUUID")) {
			this.entityData.set(OWNER, Optional.of(UUID.fromString(compound.getString("OwnerUUID"))));
		}
	}

	public void setCaster(UUID uuid) {
		this.entityData.set(OWNER, Optional.of(uuid));
	}

	@Override
	protected void defineSynchedData(SynchedEntityData.Builder pBuilder) {
		pBuilder.define(OWNER, Optional.empty());
	}
}
