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

public class FlameWallCoreEntity extends ThrowableProjectile {
	private static final int MAX_TICKS = 120;
	private static final int PARTICLE_INTERVAL_TICKS = 4;
	private static final int DAMAGE_INTERVAL_TICKS = 10;
	private static final float RADIUS = 4.0F;
	private static final float HALF_HEIGHT = 4.0F; // cylinder extends this far up/down from the caster
	private static final float EDGE_BAND = 1.0F; // how close to the wall counts as "trying to escape"
	private static final float PUSHBACK_STRENGTH = 0.35F;
	private static final int FIRE_TICKS_APPLIED = 20;

	private float dmg;

	public FlameWallCoreEntity(EntityType<? extends ThrowableProjectile> type, Level world) {
		super(type, world);
		this.blocksBuilding = false;
	}

	public FlameWallCoreEntity(Level world, Player caster, float dmg) {
		super(ModEntities.TYPE_FLAME_WALL.get(), caster, world);
		this.dmg = dmg;
		this.setDeltaMovement(Vec3.ZERO);
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
			if (tickCount % PARTICLE_INTERVAL_TICKS == 0) {
				spawnWallParticles();
			}
			containEntities(caster, tickCount % DAMAGE_INTERVAL_TICKS == 0);
		}

		super.tick();
	}

	private void spawnWallParticles() {
		if (!(level() instanceof ServerLevel serverLevel))
			return;

		int points = 40;
		for (int i = 0; i < points; i++) {
			double angle = (2 * Math.PI / points) * i;
			double x = getX() + RADIUS * Math.cos(angle);
			double z = getZ() + RADIUS * Math.sin(angle);
			for (double dy = 0.1D; dy <= HALF_HEIGHT * 1.5D; dy += 1.25D) {
				serverLevel.sendParticles(ParticleTypes.FLAME, x, getY() + dy, z, 1, 0, 0.02, 0, 0.01);
			}
		}
	}

	private void containEntities(Player caster, boolean doDamagePulse) {
		List<LivingEntity> nearby = Utils.getLivingEntitiesInRadiusExcludingParty(caster, this, RADIUS + 2F, HALF_HEIGHT, RADIUS + 2F);

		for (LivingEntity target : nearby) {
			double dx = target.getX() - getX();
			double dz = target.getZ() - getZ();
			double horizontalDist = Math.sqrt(dx * dx + dz * dz);

			if (horizontalDist >= RADIUS - EDGE_BAND) {
				// Push back toward the center (horizontal only) so it stays a clean cylinder wall
				double inwardX = horizontalDist > 0.0001 ? -dx / horizontalDist : 0;
				double inwardZ = horizontalDist > 0.0001 ? -dz / horizontalDist : 0;

				target.setDeltaMovement(target.getDeltaMovement().add(inwardX * PUSHBACK_STRENGTH, 0.05D, inwardZ * PUSHBACK_STRENGTH));
				target.hurtMarked = true;

				if (doDamagePulse) {
					target.hurt(KKDamageTypes.getElementalDamage(KKDamageTypes.FIRE, this, caster), dmg);
					target.setRemainingFireTicks(Math.max(target.getRemainingFireTicks(), FIRE_TICKS_APPLIED));
					target.invulnerableTime = 0;
				}
			}
		}

		if (doDamagePulse) {
			level().playSound(null, caster.blockPosition(), ModSounds.fire.get(), SoundSource.PLAYERS, 0.5F, 0.9F);
		}
	}

	@Override
	protected void onHit(HitResult result) {}


	private static final EntityDataAccessor<Optional<UUID>> OWNER = SynchedEntityData.defineId(FlameWallCoreEntity.class, EntityDataSerializers.OPTIONAL_UUID);

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
