package online.kingdomkeys.kingdomkeys.entity.organization;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.BlockParticleOption;
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
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import online.kingdomkeys.kingdomkeys.damagesource.KKDamageTypes;
import online.kingdomkeys.kingdomkeys.data.WorldData;
import online.kingdomkeys.kingdomkeys.entity.ModEntities;
import online.kingdomkeys.kingdomkeys.lib.Party;
import online.kingdomkeys.kingdomkeys.util.Utils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Zexion's Limit: a zone of pure darkness settles over an area - anyone caught inside keeps taking
 * darkness damage for as long as they stay in it, while meteors of shadow keep crashing down at random
 * points within the same radius. Purely visual/entity-based like the Pillars Limits - no real blocks
 * are ever placed, meteors are tracked internally and rendered with particles only.
 */
public class IllusoryMeteorCoreEntity extends ThrowableProjectile {

	private static final int DURATION_TICKS = 100; // ~5s total
	private static final int ZONE_DAMAGE_INTERVAL_TICKS = 20; // darkness zone tick, once a second
	private static final int METEOR_INTERVAL_TICKS = 15; // a new meteor starts falling roughly every 0.75s
	private static final int METEOR_FALL_TICKS = 12; // how long a meteor takes to fall once it starts
	private static final float RADIUS = 5F;
	private static final float METEOR_START_HEIGHT = 8F; // how high above its landing point a meteor starts
	private static final float METEOR_IMPACT_RADIUS = 1.6F;

	private float dmg;
	private Vec3 center = Vec3.ZERO;
	private final List<double[]> fallingMeteors = new ArrayList<>(); // {targetX, targetZ, groundY, startTick}

	public IllusoryMeteorCoreEntity(EntityType<? extends ThrowableProjectile> type, Level world) {
		super(type, world);
		this.blocksBuilding = false;
	}

	public IllusoryMeteorCoreEntity(Level world, Player caster, Vec3 center, float dmg) {
		super(ModEntities.TYPE_ILLUSORY_METEOR.get(), caster, world);
		this.dmg = dmg;
		this.center = center;
		this.setDeltaMovement(Vec3.ZERO);
		setCaster(caster.getUUID());
		this.setPos(center.x, center.y, center.z);
	}

	@Override
	protected double getDefaultGravity() {
		return 0D;
	}

	@Override
	public void tick() {
		if (!(getOwner() instanceof Player caster) || !caster.isAlive() || this.tickCount > DURATION_TICKS) {
			this.remove(RemovalReason.KILLED);
			return;
		}

		if (!level().isClientSide) {
			if (tickCount % ZONE_DAMAGE_INTERVAL_TICKS == 0) {
				damageZone(caster);
			}
			if (tickCount % METEOR_INTERVAL_TICKS == 0 && tickCount < DURATION_TICKS - METEOR_FALL_TICKS) {
				startMeteor();
			}
			tickMeteors(caster);
			if (tickCount % 5 == 0) {
				spawnZoneParticles();
			}
		}

		super.tick();
	}

	private void damageZone(Player caster) {
		Party casterParty = WorldData.get(caster.getServer()).getPartyFromMember(caster.getUUID());
		AABB box = new AABB(center.x - RADIUS, center.y - 2D, center.z - RADIUS, center.x + RADIUS, center.y + 4D, center.z + RADIUS);
		List<Entity> nearby = level().getEntities(this, box, Entity::isAlive);

		for (Entity entity : nearby) {
			if (!(entity instanceof LivingEntity target))
				continue;
			if (target == caster)
				continue;
			if (casterParty != null && !casterParty.getFriendlyFire() && Utils.isEntityInParty(casterParty, entity))
				continue;

			double dx = target.getX() - center.x;
			double dz = target.getZ() - center.z;
			if (dx * dx + dz * dz > RADIUS * RADIUS)
				continue;

			target.hurt(KKDamageTypes.getElementalDamage(KKDamageTypes.DARKNESS, this, caster), dmg * 0.4F);
			target.invulnerableTime = 0;
		}
	}

	private void startMeteor() {
		if (!(level() instanceof ServerLevel serverLevel))
			return;

		double angle = level().random.nextDouble() * 2 * Math.PI;
		double r = RADIUS * Math.sqrt(level().random.nextDouble());
		double targetX = center.x + r * Math.cos(angle);
		double targetZ = center.z + r * Math.sin(angle);

		BlockPos ground = findGround(serverLevel, (int) Math.floor(targetX), (int) center.y + 2, (int) Math.floor(targetZ));
		double groundY = ground != null ? ground.getY() + 1D : center.y;

		fallingMeteors.add(new double[]{targetX, targetZ, groundY, tickCount});
		level().playSound(null, targetX, groundY, targetZ, SoundEvents.FIRECHARGE_USE, SoundSource.PLAYERS, 0.8F, 0.6F);
	}

	private BlockPos findGround(ServerLevel level, int x, int startY, int z) {
		BlockPos pos = new BlockPos(x, startY, z);
		for (int i = 0; i < 10; i++) {
			if (!level.getBlockState(pos).isAir()) return pos;
			pos = pos.below();
		}
		return null;
	}

	private void tickMeteors(Player caster) {
		if (!(level() instanceof ServerLevel serverLevel)) return;

		Iterator<double[]> it = fallingMeteors.iterator();
		while (it.hasNext()) {
			double[] meteor = it.next();
			double targetX = meteor[0];
			double targetZ = meteor[1];
			double groundY = meteor[2];
			double startTick = meteor[3];

			double elapsed = tickCount - startTick;
			float progress = net.minecraft.util.Mth.clamp((float) (elapsed / METEOR_FALL_TICKS), 0F, 1F);
			double currentY = groundY + METEOR_START_HEIGHT * (1F - progress);

			serverLevel.sendParticles(new BlockParticleOption(ParticleTypes.BLOCK, Blocks.BLACKSTONE.defaultBlockState()), targetX, currentY, targetZ, 3, 0.15, 0.15, 0.15, 0.02);
			serverLevel.sendParticles(ParticleTypes.SMOKE, targetX, currentY, targetZ, 2, 0.1, 0.1, 0.1, 0.01);

			if (progress >= 1F) {
				impact(caster, targetX, groundY, targetZ);
				it.remove();
			}
		}
	}

	private void impact(Player caster, double x, double y, double z) {
		if (!(level() instanceof ServerLevel serverLevel)) return;

		Party casterParty = WorldData.get(caster.getServer()).getPartyFromMember(caster.getUUID());
		AABB box = new AABB(x - METEOR_IMPACT_RADIUS, y - 1D, z - METEOR_IMPACT_RADIUS, x + METEOR_IMPACT_RADIUS, y + 2D, z + METEOR_IMPACT_RADIUS);
		List<Entity> nearby = level().getEntities(this, box, Entity::isAlive);

		for (Entity entity : nearby) {
			if (!(entity instanceof LivingEntity target)) continue;
			if (target == caster) continue;
			if (casterParty != null && !casterParty.getFriendlyFire() && Utils.isEntityInParty(casterParty, entity)) continue;

			target.hurt(KKDamageTypes.getElementalDamage(KKDamageTypes.DARKNESS, this, caster), dmg);
			target.invulnerableTime = 0;
			target.setDeltaMovement(target.getDeltaMovement().x, 0.25D, target.getDeltaMovement().z);
		}

		serverLevel.sendParticles(ParticleTypes.EXPLOSION, x, y, z, 1, 0, 0, 0, 0);
		serverLevel.sendParticles(new BlockParticleOption(ParticleTypes.BLOCK, Blocks.BLACKSTONE.defaultBlockState()), x, y, z, 20, 0.5, 0.3, 0.5, 0.15);
		level().playSound(null, x, y, z, SoundEvents.GENERIC_EXPLODE, SoundSource.PLAYERS, 0.7F, 0.7F);
	}

	private void spawnZoneParticles() {
		if (!(level() instanceof ServerLevel serverLevel)) return;
		int points = 20;
		for (int i = 0; i < points; i++) {
			double angle = (2 * Math.PI / points) * i;
			double x = center.x + RADIUS * Math.cos(angle);
			double z = center.z + RADIUS * Math.sin(angle);
			serverLevel.sendParticles(ParticleTypes.SQUID_INK, x, center.y + 0.1D, z, 1, 0, 0.05, 0, 0.0);
		}
	}

	private static final EntityDataAccessor<Optional<UUID>> OWNER = SynchedEntityData.defineId(IllusoryMeteorCoreEntity.class, EntityDataSerializers.OPTIONAL_UUID);

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
