package online.kingdomkeys.kingdomkeys.entity.magic;

import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.projectile.ThrowableProjectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.HitResult;
import online.kingdomkeys.kingdomkeys.damagesource.KKDamageTypes;
import online.kingdomkeys.kingdomkeys.data.WorldData;
import online.kingdomkeys.kingdomkeys.entity.ModEntities;
import online.kingdomkeys.kingdomkeys.lib.Party;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class FaithBeamEntity extends BaseMagicProjectile {
	private static final int EXPANSION_DURATION = 20;
	private static final double ROTATION_SPEED = 0.25D;
	private final Set<UUID> hitTargets = new HashSet<>();
	private double baseAngle;
	private double centerX;
	private double centerZ;
	private double orbitRadius = 2D;
	private boolean expanding = false;
	private int expandingTicks;

	public FaithBeamEntity(EntityType<? extends ThrowableProjectile> type, Level world) {
		super(type, world);
		this.blocksBuilding = true;
	}

	public FaithBeamEntity(Level world, LivingEntity player, float damage, double x, double y, double z) {
		this(world, player, damage);
		this.setPos(x, y, z);
	}

	public FaithBeamEntity(Level world, LivingEntity player, float damage) {
		super(ModEntities.TYPE_FAITHBEAM.get(), player, world);
		this.dmgMult = damage;
		this.damageType = KKDamageTypes.LIGHT;
	}

	@Override
	protected double getDefaultGravity() {
		return 0;
	}

	@Override
	public void tick() {
		if (expandingTicks >= EXPANSION_DURATION) {
			discard();
			return;
		}

		if (expanding) {
			expandingTicks++;

			orbitRadius += 0.8D + expandingTicks * 0.01D;
			baseAngle += ROTATION_SPEED;

			double targetX = centerX + Math.cos(baseAngle) * orbitRadius;
			double targetZ = centerZ + Math.sin(baseAngle) * orbitRadius;

			int groundY = level().getHeight(Heightmap.Types.WORLD_SURFACE, (int) Math.floor(targetX), (int) Math.floor(targetZ));

			double targetY = groundY + 0.1D;

			//float EXPANSION_SPEED = 0.5F;
			//orbitRadius += EXPANSION_SPEED;

			double dx = targetX - getX();
			double dz = targetZ - getZ();

			setDeltaMovement(dx * 0.4D, targetY, dz * 0.4D);
			move(MoverType.SELF, getDeltaMovement());

			damageNearbyEntities();
		}

		super.tick();
	}

	public void setBaseAngle(double angle) {
		this.baseAngle = angle;
	}

	public void startExpanding(double centerX, double centerZ) {
		this.centerX = centerX;
		this.centerZ = centerZ;

		double dx = getX() - centerX;
		double dz = getZ() - centerZ;

		this.orbitRadius = Math.sqrt(dx * dx + dz * dz);
		this.expanding = true;
	}

	private void damageNearbyEntities() {
		if (getOwner() == null || level().isClientSide) {
			return;
		}
		AABB area = getBoundingBox().inflate(1.0D);
		Party party = WorldData.get(getOwner().getServer()).getPartyFromMember(getOwner().getUUID());

		for (LivingEntity target : level().getEntitiesOfClass(LivingEntity.class, area, e -> e.isAlive() && e != getOwner())) {
			if (party != null && party.getMember(target.getUUID()) != null && !party.getFriendlyFire()) {
				continue;
			}

			if (hitTargets.contains(target.getUUID())) {
				continue;
			}

			damageEntity(target);
			hitTargets.add(target.getUUID());

			target.invulnerableTime = 10;
		}
	}

	@Override
	protected void onHit(HitResult rtRes) {

	}

	@Override
	protected void defineSynchedData(SynchedEntityData.Builder builder) {

	}
}
