package online.kingdomkeys.kingdomkeys.entity.organization;

import net.minecraft.core.particles.ParticleTypes;
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
import online.kingdomkeys.kingdomkeys.lib.Party;
import online.kingdomkeys.kingdomkeys.util.Utils;

import java.util.*;

public class LanceStormCoreEntity extends ThrowableProjectile {

	public static final int LANCE_COUNT = 6;
	public static final int TELEGRAPH_TICKS = 12; // hovering in formation before the thrust
	public static final int THRUST_TICKS = 14; // how long the forward stab itself takes
	private static final float FRONT_DISTANCE = 2.5F; // how far in front of the caster the hexagon forms
	public static final float HEX_RADIUS = 1.1F;
	public static final float THRUST_DISTANCE = 6.5F; // how far the whole formation travels once thrusting
	private static final float HIT_RADIUS = 0.9F; // how close something needs to be to a lance point to get hit

	private float dmg;
	private Vec3 forward = Vec3.ZERO;
	private Vec3 right = Vec3.ZERO;
	private Vec3 up = Vec3.ZERO;
	private Vec3 center = Vec3.ZERO;
	private final Set<UUID> alreadyHit = new HashSet<>();
	private static final EntityDataAccessor<Float> FROZEN_YAW = SynchedEntityData.defineId(LanceStormCoreEntity.class, EntityDataSerializers.FLOAT);
	private static final EntityDataAccessor<Float> FROZEN_PITCH = SynchedEntityData.defineId(LanceStormCoreEntity.class, EntityDataSerializers.FLOAT);

	public LanceStormCoreEntity(EntityType<? extends ThrowableProjectile> type, Level world) {
		super(type, world);
		this.blocksBuilding = false;
	}

	public LanceStormCoreEntity(Level world, Player caster, float dmg) {
		super(ModEntities.TYPE_LANCE_STORM.get(), caster, world);
		this.dmg = dmg;
		this.setDeltaMovement(Vec3.ZERO);
		setCaster(caster.getUUID());

		this.forward = caster.getLookAngle().normalize();
		Vec3 upRef = Math.abs(forward.y) > 0.95 ? new Vec3(1, 0, 0) : new Vec3(0, 1, 0);
		this.right = forward.cross(upRef).normalize();
		this.up = right.cross(forward).normalize();
		this.center = caster.getEyePosition().add(forward.scale(FRONT_DISTANCE));
		this.setPos(center.x, center.y, center.z);
		this.entityData.set(FROZEN_YAW, caster.getYRot());
		this.entityData.set(FROZEN_PITCH, caster.getXRot());
	}

	/** For the renderer, so it can rebuild the exact same forward/right/up the hit detection uses. */
	public float getFrozenYaw() {
		return this.entityData.get(FROZEN_YAW);
	}

	public float getFrozenPitch() {
		return this.entityData.get(FROZEN_PITCH);
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
			if (tickCount <= TELEGRAPH_TICKS) {
				if (tickCount == 1) {
					level().playSound(null, caster.blockPosition(), SoundEvents.TRIDENT_THROW.value(), SoundSource.PLAYERS, 1F, 0.6F);
				}
				if (tickCount % 3 == 0) {
					spawnFormationParticles(0F);
				}
			} else {
				int thrustTick = tickCount - TELEGRAPH_TICKS;
				if (thrustTick == 1) {
					level().playSound(null, caster.blockPosition(), SoundEvents.TRIDENT_THROW.value(), SoundSource.PLAYERS, 1F, 1.4F);
				}

				float progress = Math.min(1F, (float) thrustTick / THRUST_TICKS);
				float travelled = progress * THRUST_DISTANCE;
				spawnFormationParticles(travelled);
				damageAlongFormation(caster, travelled);

				if (thrustTick >= THRUST_TICKS) {
					this.remove(RemovalReason.KILLED);
					return;
				}
			}
		}

		super.tick();
	}

	private Vec3 hexPoint(int index, float travelled) {
		double angle = (2 * Math.PI / LANCE_COUNT) * index;
		Vec3 ringOffset = right.scale(Math.cos(angle) * HEX_RADIUS).add(up.scale(Math.sin(angle) * HEX_RADIUS));
		return center.add(forward.scale(travelled)).add(ringOffset);
	}

	private void spawnFormationParticles(float travelled) {
		if (!(level() instanceof ServerLevel serverLevel)) return;
		for (int i = 0; i < LANCE_COUNT; i++) {
			Vec3 p = hexPoint(i, travelled);
			serverLevel.sendParticles(ParticleTypes.CRIT, p.x, p.y, p.z, 2, 0.05, 0.05, 0.05, 0.01);
		}
	}

	private void damageAlongFormation(Player caster, float travelled) {
		Party casterParty = Utils.getParty(caster);

		for (int i = 0; i < LANCE_COUNT; i++) {
			Vec3 p = hexPoint(i, travelled);
			AABB box = new AABB(p.x - HIT_RADIUS, p.y - HIT_RADIUS, p.z - HIT_RADIUS, p.x + HIT_RADIUS, p.y + HIT_RADIUS, p.z + HIT_RADIUS);
			List<Entity> nearby = level().getEntities(this, box, Entity::isAlive);

			for (Entity entity : nearby) {
				if (!(entity instanceof LivingEntity target))
					continue;
				if (target == caster)
					continue;
				if (alreadyHit.contains(target.getUUID()))
					continue;
				if (!Utils.canHarm(casterParty, target))
					continue;

				target.hurt(KKDamageTypes.getElementalDamage(KKDamageTypes.AIR, this, caster), dmg);
				target.invulnerableTime = 0;
				alreadyHit.add(target.getUUID());
			}
		}
	}


	@Override
	protected void onHit(HitResult result) {}

	private static final EntityDataAccessor<Optional<UUID>> OWNER = SynchedEntityData.defineId(LanceStormCoreEntity.class, EntityDataSerializers.OPTIONAL_UUID);

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
		pBuilder.define(FROZEN_YAW, 0F);
		pBuilder.define(FROZEN_PITCH, 0F);
	}
}
