package online.kingdomkeys.kingdomkeys.entity.organization;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
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
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import online.kingdomkeys.kingdomkeys.damagesource.KKDamageTypes;
import online.kingdomkeys.kingdomkeys.data.WorldData;
import online.kingdomkeys.kingdomkeys.entity.ModEntities;
import online.kingdomkeys.kingdomkeys.lib.Party;
import online.kingdomkeys.kingdomkeys.util.Utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class IcePillarsCoreEntity extends ThrowableProjectile {

	private static final int PILLAR_COUNT = 8;
	private static final float RADIUS = 5F;
	private static final float PILLAR_HEIGHT = 4F;
	private static final int PILLAR_INTERVAL_TICKS = 6; // delay between each pillar erupting, one at a time
	private static final int LIFETIME_TICKS = PILLAR_COUNT * PILLAR_INTERVAL_TICKS + 20;
	private static final float HIT_RADIUS = 1.1F;
	private static final float LAUNCH_STRENGTH = 1.1F;

	private float dmg;
	private int pillarsErupted = 0;
	private final List<Vec3> pillarBases = new ArrayList<>();

	public IcePillarsCoreEntity(EntityType<? extends ThrowableProjectile> type, Level world) {
		super(type, world);
		this.blocksBuilding = false;
	}

	public IcePillarsCoreEntity(Level world, Player caster, float dmg) {
		super(ModEntities.TYPE_ICE_PILLARS.get(), caster, world);
		this.dmg = dmg;
		this.setDeltaMovement(Vec3.ZERO);
		setCaster(caster.getUUID());
		this.setPos(caster.getX(), caster.getY(), caster.getZ());
	}

	@Override
	protected double getDefaultGravity() {
		return 0D;
	}

	protected BlockState pillarMaterial(ServerLevel level, BlockPos groundPos) {
		return Blocks.PACKED_ICE.defaultBlockState();
	}

	@Override
	public void tick() {
		if (!(getOwner() instanceof Player caster) || !caster.isAlive()) {
			this.remove(RemovalReason.KILLED);
			return;
		}

		if (!level().isClientSide) {
			if (pillarsErupted < PILLAR_COUNT) {
				if (tickCount % PILLAR_INTERVAL_TICKS == 0) {
					raisePillar(caster);
					pillarsErupted++;
				}
			} else if (tickCount % 6 == 0) {
				spawnLingeringParticles();
			}

			if (tickCount >= LIFETIME_TICKS) {
				this.remove(RemovalReason.KILLED);
				return;
			}
		}

		super.tick();
	}

	private void raisePillar(Player caster) {
		if (!(level() instanceof ServerLevel serverLevel))
			return;

		double angle = level().random.nextDouble() * 2 * Math.PI;
		double r = RADIUS * Math.sqrt(level().random.nextDouble());
		int px = (int) Math.floor(caster.getX() + r * Math.cos(angle));
		int pz = (int) Math.floor(caster.getZ() + r * Math.sin(angle));

		BlockPos ground = findGround(serverLevel, px, (int) caster.getY() + 2, pz);
		if (ground == null) return;

		Vec3 base = Vec3.atBottomCenterOf(ground);
		pillarBases.add(base);

		BlockState material = pillarMaterial(serverLevel, ground);

		PillarEntity pillar = new PillarEntity(ModEntities.TYPE_PILLAR.get(), level());
		pillar.setPos(base.x, base.y, base.z);
		pillar.setup(material, PILLAR_HEIGHT, 0.6F, LIFETIME_TICKS - tickCount + 10);
		level().addFreshEntity(pillar);

		serverLevel.sendParticles(ParticleTypes.SNOWFLAKE, base.x, base.y + PILLAR_HEIGHT, base.z, 20, 0.3, 0.5, 0.3, 0.02);

		level().playSound(null, ground, SoundEvents.GLASS_BREAK, SoundSource.PLAYERS, 1F, 0.6F + level().random.nextFloat() * 0.2F);
		launchNearby(caster, base);
	}

	private void spawnLingeringParticles() {
		if (!(level() instanceof ServerLevel serverLevel)) return;
		for (Vec3 base : pillarBases) {
			serverLevel.sendParticles(ParticleTypes.SNOWFLAKE, base.x, base.y + PILLAR_HEIGHT * 0.5, base.z, 3, 0.2, 0.6, 0.2, 0.01);
		}
	}

	/** Scans down (then up as a fallback) from the given column to find solid ground to erupt from. */
	private BlockPos findGround(ServerLevel level, int x, int startY, int z) {
		BlockPos pos = new BlockPos(x, startY, z);
		for (int i = 0; i < 10; i++) {
			if (!level.getBlockState(pos).isAir()) return pos;
			pos = pos.below();
		}
		return null;
	}

	private void launchNearby(Player caster, Vec3 base) {
		Party casterParty = Utils.getParty(caster);

		AABB box = new AABB(base, base).inflate(HIT_RADIUS, PILLAR_HEIGHT, HIT_RADIUS).move(0, PILLAR_HEIGHT * 0.5, 0);
		List<Entity> nearby = level().getEntities(this, box, Entity::isAlive);

		for (Entity entity : nearby) {
			if (!(entity instanceof LivingEntity target))
				continue;
			if (target == caster)
				continue;
			if (!Utils.canHarm(casterParty, target))
				continue;

			target.hurt(KKDamageTypes.getElementalDamage(KKDamageTypes.ICE, this, caster), dmg);
			target.invulnerableTime = 0;
			target.setDeltaMovement(target.getDeltaMovement().x, LAUNCH_STRENGTH, target.getDeltaMovement().z);
			target.hurtMarked = true;
		}
	}

	@Override
	protected void onHit(HitResult result) {}

	private static final EntityDataAccessor<Optional<UUID>> OWNER = SynchedEntityData.defineId(IcePillarsCoreEntity.class, EntityDataSerializers.OPTIONAL_UUID);

	public void setCaster(UUID uuid) {
		this.entityData.set(OWNER, Optional.of(uuid));
	}

	@Override
	protected void defineSynchedData(SynchedEntityData.Builder pBuilder) {
		pBuilder.define(OWNER, Optional.empty());
	}
}
