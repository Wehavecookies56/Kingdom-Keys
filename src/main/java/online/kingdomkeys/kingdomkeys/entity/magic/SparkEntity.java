package online.kingdomkeys.kingdomkeys.entity.magic;

import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ThrowableProjectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import online.kingdomkeys.kingdomkeys.damagesource.KKDamageTypes;
import online.kingdomkeys.kingdomkeys.data.PlayerData;
import online.kingdomkeys.kingdomkeys.data.WorldData;
import online.kingdomkeys.kingdomkeys.entity.ModEntities;
import online.kingdomkeys.kingdomkeys.lib.Party;
import online.kingdomkeys.kingdomkeys.lib.Strings;
import org.joml.Vector3f;

import java.util.List;

public class SparkEntity extends ThrowableProjectile {

	private static final EntityDataAccessor<String> CASTER = SynchedEntityData.defineId(SparkEntity.class, EntityDataSerializers.STRING);

	// config / state
	private String casterName = "";
	private float dmgMult;
	private int index = 0;

	// orbit params (tweak these)
	private double verticalOffset = 0.0;
	private double angleOffset = 0.0;   // radians initial phase
	private double orbitRadius = 1.0;
	private double orbitSpeed = 0.08;   // small = slower, large = faster
	private int direction = 1;          // +1 clockwise, -1 counterclockwise

	// cached owner reference
	private Player ownerPlayer = null;

	// lifetime
	private int maxTicks = 60;

	public SparkEntity(EntityType<? extends ThrowableProjectile> type, Level world) {
		super(type, world);
		this.blocksBuilding = true;
	}

	public SparkEntity(Level world) {
		super(ModEntities.TYPE_SPARK.get(), world);
		this.blocksBuilding = true;
	}

	// used when spawning from magic: (world, player, index, dmgMult)
	public SparkEntity(Level world, LivingEntity player, int index, float dmgMult) {
		super(ModEntities.TYPE_SPARK.get(), player, world);
		this.blocksBuilding = true;
		this.index = index;
		this.dmgMult = dmgMult;
		// default angleOffset; caller should setAngleOffset(...) for specific placement
		this.angleOffset = 0.0;
	}

	private static float[] hsbToRgb(float hue, float saturation, float brightness) {
		int h_i = (int) Math.floor(hue * 6);
		float f = hue * 6 - h_i;
		float p = brightness * (1 - saturation);
		float q = brightness * (1 - f * saturation);
		float t = brightness * (1 - (1 - f) * saturation);
		float r = 0, g = 0, b = 0;
		switch (h_i % 6) {
			case 0 -> {
				r = brightness;
				g = t;
				b = p;
			}
			case 1 -> {
				r = q;
				g = brightness;
				b = p;
			}
			case 2 -> {
				r = p;
				g = brightness;
				b = t;
			}
			case 3 -> {
				r = p;
				g = q;
				b = brightness;
			}
			case 4 -> {
				r = t;
				g = p;
				b = brightness;
			}
			case 5 -> {
				r = brightness;
				g = p;
				b = q;
			}
		}
		return new float[]{r, g, b};
	}

	@Override
	protected double getDefaultGravity() {
		return 0.0;
	}

	@Override
	protected void defineSynchedData(SynchedEntityData.Builder builder) {
		builder.define(CASTER, "");
	}

	@Override
	public void addAdditionalSaveData(CompoundTag compound) {
		compound.putString("caster", this.casterName);
		compound.putInt("index", this.index);
		compound.putDouble("angleOffset", this.angleOffset);
		compound.putDouble("verticalOffset", this.verticalOffset);
		compound.putDouble("orbitRadius", this.orbitRadius);
		compound.putDouble("orbitSpeed", this.orbitSpeed);
		compound.putInt("direction", this.direction);
		compound.putFloat("dmgMult", this.dmgMult);
		compound.putInt("maxTicks", this.maxTicks);
	}

	@Override
	public void readAdditionalSaveData(CompoundTag compound) {
		if (compound.contains("caster")) this.setCaster(compound.getString("caster"));
		if (compound.contains("index")) this.index = compound.getInt("index");
		if (compound.contains("angleOffset")) this.angleOffset = compound.getDouble("angleOffset");
		if (compound.contains("verticalOffset")) this.verticalOffset = compound.getDouble("verticalOffset");
		if (compound.contains("orbitRadius")) this.orbitRadius = compound.getDouble("orbitRadius");
		if (compound.contains("orbitSpeed")) this.orbitSpeed = compound.getDouble("orbitSpeed");
		if (compound.contains("direction")) this.direction = compound.getInt("direction");
		if (compound.contains("dmgMult")) this.dmgMult = compound.getFloat("dmgMult");
		if (compound.contains("maxTicks")) this.maxTicks = compound.getInt("maxTicks");
	}

	public void setCaster(String name) {
		this.entityData.set(CASTER, name == null ? "" : name);
		this.casterName = name == null ? "" : name;
	}

	public String getCasterDataManager() {
		return this.entityData.get(CASTER);
	}

	@Override
	public void onSyncedDataUpdated(EntityDataAccessor<?> key) {
		if (key.equals(CASTER)) {
			this.casterName = this.getCasterDataManager();
		}
		super.onSyncedDataUpdated(key);
	}

	// setters for magicSpark to control orientation/speed/direction
	public void setVerticalOffset(double v) {
		this.verticalOffset = v;
	}

	public void setAngleOffset(double a) {
		this.angleOffset = a;
	}

	public void setOrbitRadius(double r) {
		this.orbitRadius = r;
	}

	public void setOrbitSpeed(double s) {
		this.orbitSpeed = s;
	}

	public void setDirection(int dir) {
		this.direction = dir >= 0 ? 1 : -1;
	}

	@Override
	public void tick() {
		super.tick();

		// --- SERVER SIDE LOGIC ---
		if (!level().isClientSide) {
			// resolve owner if not yet cached
			if (this.ownerPlayer == null) {
				if (this.getOwner() instanceof Player p) {
					this.ownerPlayer = p;
				} else if (this.casterName != null && !this.casterName.isEmpty()) {
					for (Player p : level().players()) {
						if (p.getDisplayName().getString().equals(this.casterName)) {
							this.ownerPlayer = p;
							break;
						}
					}
				}
			}

			// if no owner after a while → discard
			if (this.ownerPlayer == null) {
				if (this.tickCount > 40) this.discard();
				return;
			}

			// lifetime check
			if (this.tickCount > this.maxTicks) {
				this.discard();
				return;
			}

			// keep entity frozen
			this.setDeltaMovement(0, 0, 0);

			// use tickCount for deterministic orbiting
			double time = this.tickCount;
			double angle = this.angleOffset + this.direction * (time * this.orbitSpeed);

			double cx = ownerPlayer.getX();
			double cy = ownerPlayer.getY() + 1.0 + this.verticalOffset;
			double cz = ownerPlayer.getZ();

			double nx = cx + Math.cos(angle) * this.orbitRadius;
			double nz = cz + Math.sin(angle) * this.orbitRadius;

			// server sets authoritative position
			this.setPos(nx, cy, nz);
		}

		if (!level().isClientSide && ownerPlayer != null) {
			ownerPlayer.setDeltaMovement(0, 0, 0);
			double damageRange = 0.5; // radius around orb to hit entities
			AABB box = this.getBoundingBox().inflate(damageRange, damageRange, damageRange);

			List<LivingEntity> targets = level().getEntitiesOfClass(LivingEntity.class, box, e -> e != ownerPlayer && e.isAlive());

			for (LivingEntity target : targets) {
				Party p = null;
				if (ownerPlayer instanceof Player owner) p = WorldData.get(owner.getServer()).getPartyFromMember(owner.getUUID());
				if (p == null || (p.getMember(target.getUUID()) == null || p.getFriendlyFire())) {
					PlayerData playerData = PlayerData.get(ownerPlayer);
					float dmg = (float) playerData.getMagic(true) * 0.005f;
					dmgMult = (playerData.getNumberOfAbilitiesEquipped(Strings.thunderBoost) * 0.5f);
					dmg += (dmg * dmgMult);

					target.hurt(KKDamageTypes.getElementalDamage(KKDamageTypes.LIGHT, this, ownerPlayer), dmg);
					target.invulnerableTime = 11; // reset invulnerability so multiple hits possible
				}
			}
		}

		// --- CLIENT SIDE VISUALS ---
		if (this.level().isClientSide) {
			spawnTrailParticles();
		}
	}

	@Override
	public void lerpTo(double x, double y, double z, float yRot, float xRot, int steps) {
		this.setPosRaw(x, y, z);
	}

	private void spawnTrailParticles() {
		// small colorful dust particles, ephemeral; tweak size/saturation as you like
		int count = 2;
		for (int i = 0; i < count; i++) {
			float hue = (float) (((this.level().getGameTime() * 6L + this.index * 30L + i * 10L) % 360L) / 360.0F);
			float[] rgb = hsbToRgb(hue, 1.0F, 1.0F);
			DustParticleOptions dust = new DustParticleOptions(new Vector3f(rgb[0], rgb[1], rgb[2]), 1F);
			this.level().addParticle(dust, this.getX() + (this.random.nextDouble() - 0.5) * 0.12, this.getY() + ((this.random.nextDouble() - 0.5) * 0.12) + 0.6, this.getZ() + (this.random.nextDouble() - 0.5) * 0.12, 0.0, 0.0, 0.0);
		}
	}
}
