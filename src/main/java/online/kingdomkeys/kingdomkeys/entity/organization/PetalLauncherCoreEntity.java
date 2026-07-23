package online.kingdomkeys.kingdomkeys.entity.organization;

import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ThrowableProjectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import online.kingdomkeys.kingdomkeys.damagesource.KKDamageTypes;
import online.kingdomkeys.kingdomkeys.data.WorldData;
import online.kingdomkeys.kingdomkeys.entity.ModEntities;
import online.kingdomkeys.kingdomkeys.lib.DamageCalculation;
import online.kingdomkeys.kingdomkeys.lib.Party;
import online.kingdomkeys.kingdomkeys.util.Utils;
import org.joml.Vector3f;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public class PetalLauncherCoreEntity extends ThrowableProjectile {

	private static final int LIFETIME_TICKS = 200; // ~10s
	private static final float LAUNCH_STRENGTH = 1.3F;
	private static final int TRIGGER_COOLDOWN_TICKS = 20; // don't re-launch every tick while someone's standing on it
	private static final float DEFAULT_RADIUS = 1.0F;

	private final Map<UUID, Integer> lastTriggeredTick = new HashMap<>();

	private float dmgMult;

	public PetalLauncherCoreEntity(EntityType<? extends ThrowableProjectile> type, Level world) {
		super(type, world);
		this.blocksBuilding = false;
		this.noPhysics = true;
	}

	public PetalLauncherCoreEntity(Level world, Player caster, float dmg) {
		super(ModEntities.TYPE_PETAL_LAUNCHER.get(), caster, world);
		this.setDeltaMovement(Vec3.ZERO);
		setCaster(caster.getUUID());
		this.dmgMult = dmg;
	}

	/** How big the disc is - both what you see and the area that actually triggers the launch. */
	public void setRadius(float radius) {
		this.entityData.set(RADIUS, radius);
	}

	public float getRadius() {
		return this.entityData.get(RADIUS);
	}

	@Override
	protected double getDefaultGravity() {
		return 0D;
	}

	@Override
	public void tick() {
		if (!level().isClientSide) {
			if (!(getOwner() instanceof Player caster) || !caster.isAlive() || this.tickCount > LIFETIME_TICKS) {
				this.remove(RemovalReason.KILLED);
				return;
			}

			if (tickCount % 3 == 0) {
				spawnDiscParticles();
			}
			checkForSteppers(caster);
		}

		super.tick();
	}

	private void spawnDiscParticles() {
		if (!(level() instanceof ServerLevel serverLevel)) return;
		float radius = getRadius();

		// Dark center
		serverLevel.sendParticles(new DustParticleOptions(new Vector3f(0.05F, 0.02F, 0.08F), 1.3F), getX(), getY() + 0.05D, getZ(), (int) (6 * radius), 0.4 * radius, 0.02, 0.4 * radius, 0.0);
		// Pink rim
		int points = 14;
		for (int i = 0; i < points; i++) {
			double angle = (2 * Math.PI / points) * i;
			double x = getX() + radius * 0.85 * Math.cos(angle);
			double z = getZ() + radius * 0.85 * Math.sin(angle);
			serverLevel.sendParticles(new DustParticleOptions(new Vector3f(1.0F, 0.45F, 0.75F), 1.1F), x, getY() + 0.05D, z, 1, 0, 0, 0, 0.0);
		}
	}

	private void checkForSteppers(Player caster) {
		float radius = getRadius();
		AABB box = new AABB(getX() - radius, getY() - 0.5D, getZ() - radius, getX() + radius, getY() + 1.0D, getZ() + radius);
		Party party = WorldData.get(caster.getServer()).getPartyFromMember(caster.getUUID());

		for (Entity entity : level().getEntities(this, box, Entity::isAlive)) {
			if (!(entity instanceof LivingEntity target))
				continue;
			if (target == caster)
				continue;

			Integer last = lastTriggeredTick.get(target.getUUID());
			if (last != null && tickCount - last < TRIGGER_COOLDOWN_TICKS)
				continue;

			if (party != null && Utils.isEntityInParty(party, entity)) {
				continue;
			}

			target.setDeltaMovement(target.getDeltaMovement().x, LAUNCH_STRENGTH, target.getDeltaMovement().z);
			target.hurtMarked = true;
			lastTriggeredTick.put(target.getUUID(), tickCount);

			PetalAscendTrailEntity ascendTrail = new PetalAscendTrailEntity(ModEntities.TYPE_PETAL_ASCEND_TRAIL.get(), level(), target);
			level().addFreshEntity(ascendTrail);

			level().playSound(null, target.blockPosition(), SoundEvents.SLIME_JUMP, SoundSource.PLAYERS, 1F, 1.6F);
			target.hurt(KKDamageTypes.getElementalDamage(KKDamageTypes.DARKNESS, this, this.getOwner()), DamageCalculation.getMagicDamage(caster) * dmgMult);
		}
	}

	@Override
	protected void onHit(HitResult result) {}

	private static final EntityDataAccessor<Optional<UUID>> OWNER = SynchedEntityData.defineId(PetalLauncherCoreEntity.class, EntityDataSerializers.OPTIONAL_UUID);
	private static final EntityDataAccessor<Float> RADIUS = SynchedEntityData.defineId(PetalLauncherCoreEntity.class, EntityDataSerializers.FLOAT);

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
		pBuilder.define(RADIUS, DEFAULT_RADIUS);
	}
}
