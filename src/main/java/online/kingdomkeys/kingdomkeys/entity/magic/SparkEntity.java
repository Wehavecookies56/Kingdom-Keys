package online.kingdomkeys.kingdomkeys.entity.magic;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ThrowableProjectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import online.kingdomkeys.kingdomkeys.data.WorldData;
import online.kingdomkeys.kingdomkeys.entity.ModEntities;
import online.kingdomkeys.kingdomkeys.lib.Party;

public class SparkEntity extends BaseMagicProjectile {

	private static final EntityDataAccessor<Integer> INDEX = SynchedEntityData.defineId(SparkEntity.class, EntityDataSerializers.INT);

	public final Vec3[] trailPositions = new Vec3[20];

	private double angleOffset;
	private double orbitRadius = 1.0D;
	private double orbitSpeed = 0.08D;
	private double verticalOffset;
	private int direction = 1;
	private int maxTicks = 60;

	public SparkEntity(EntityType<? extends ThrowableProjectile> type, Level level) {
		super(type, level);
		this.blocksBuilding = true;
	}

	public SparkEntity(Level level, LivingEntity owner, int index, float dmgMult) {
		super(ModEntities.TYPE_SPARK.get(), owner, level);
		this.dmgMult = dmgMult;
		this.blocksBuilding = true;
		setIndex(index);
	}

	@Override
	protected double getDefaultGravity() {
		return 0.0D;
	}

	@Override
	protected void defineSynchedData(SynchedEntityData.Builder builder) {
		builder.define(INDEX, 0);
	}

	public int getIndex() {
		return entityData.get(INDEX);
	}

	public void setIndex(int index) {
		entityData.set(INDEX, index);
	}

	public void setAngleOffset(double angleOffset) {
		this.angleOffset = angleOffset;
	}

	public void setOrbitRadius(double orbitRadius) {
		this.orbitRadius = orbitRadius;
	}

	public void setOrbitSpeed(double orbitSpeed) {
		this.orbitSpeed = orbitSpeed;
	}

	public void setVerticalOffset(double verticalOffset) {
		this.verticalOffset = verticalOffset;
	}

	public void setDirection(int direction) {
		this.direction = direction >= 0 ? 1 : -1;
	}

	@Override
	public void addAdditionalSaveData(CompoundTag tag) {
		tag.putInt("Index", getIndex());
		tag.putDouble("AngleOffset", angleOffset);
		tag.putDouble("OrbitRadius", orbitRadius);
		tag.putDouble("OrbitSpeed", orbitSpeed);
		tag.putDouble("VerticalOffset", verticalOffset);
		tag.putInt("Direction", direction);
		tag.putInt("MaxTicks", maxTicks);
	}

	@Override
	public void readAdditionalSaveData(CompoundTag tag) {
		setIndex(tag.getInt("Index"));
		angleOffset = tag.getDouble("AngleOffset");
		orbitRadius = tag.getDouble("OrbitRadius");
		orbitSpeed = tag.getDouble("OrbitSpeed");
		verticalOffset = tag.getDouble("VerticalOffset");
		direction = tag.getInt("Direction");
		maxTicks = tag.getInt("MaxTicks");
	}

	@Override
	public void tick() {
		super.tick();

		if (!level().isClientSide) {
			if (getOwner() == null) {
				if (tickCount > 40) {
					discard();
				}
				return;
			}

			if (tickCount > maxTicks) {
				discard();
				return;
			}

			updateOrbit();
			damageNearbyEntities();
		}

		updateTrail();
	}

	@Override
	public void lerpTo(double x, double y, double z, float yRot, float xRot, int steps) {
		setPosRaw(x, y, z);
	}

	private void updateTrail() {
		System.arraycopy(trailPositions, 0, trailPositions, 1, trailPositions.length - 1);
		trailPositions[0] = position();
	}


	private void updateOrbit() {
		setDeltaMovement(Vec3.ZERO);
		double angle = angleOffset + direction * tickCount * orbitSpeed;
		setPos(getOwner().getX() + Math.cos(angle) * orbitRadius, getOwner().getY() + 1.0D + verticalOffset, getOwner().getZ() + Math.sin(angle) * orbitRadius);
	}

	private void damageNearbyEntities() {
		AABB area = getBoundingBox().inflate(0.5D);
		Party party = WorldData.get(getOwner().getServer()).getPartyFromMember(getOwner().getUUID());

		for (LivingEntity target : level().getEntitiesOfClass(LivingEntity.class, area, e -> e != getOwner() && e.isAlive())) {
			if (party != null && party.getMember(target.getUUID()) != null && !party.getFriendlyFire()) {
				continue;
			}
			damageEntity(target);

			target.invulnerableTime = 11;
		}
	}
}