package online.kingdomkeys.kingdomkeys.entity.magic;

import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ThrowableProjectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import online.kingdomkeys.kingdomkeys.ability.ModAbilities;
import online.kingdomkeys.kingdomkeys.client.sound.ModSounds;
import online.kingdomkeys.kingdomkeys.damagesource.KKDamageTypes;
import online.kingdomkeys.kingdomkeys.data.PlayerData;
import online.kingdomkeys.kingdomkeys.entity.ModEntities;
import online.kingdomkeys.kingdomkeys.network.PacketHandler;
import online.kingdomkeys.kingdomkeys.util.Utils;
import org.joml.Vector3f;

import java.util.List;

public class AeroTornadoEntity extends BaseMagicProjectile {
	private static final int LIFETIME = 70;

	private static final int RELEASE = 55;

	// How long the column takes to be fully casted
	private static final int SPIN_UP = 10;

	private static final float SWIRL = 0.55F;

	//Pulling strength
	private static final double PULL = 0.18;

	// Rise per tick
	private static final double RISE = 0.22;

	// Keeps entities within the tornado
	private static final double SINK = 0.25;

	private static final double APPROACH = 0.2;

	//How far under the top entities stay
	private static final double LID = 0.6;

	// Push at the end
	private static final double THROW_OUT = 0.95;
	private static final double THROW_UP = 0.55;

	private static final double FLOWMOTION_UP = 1.35;

	private static final DustParticleOptions WIND_PARTICLES = new DustParticleOptions(new Vector3f(0.15F, 0.95F, 0.75F), 1.1F);

	// Synced data
	private static final EntityDataAccessor<Float> RADIUS = SynchedEntityData.defineId(AeroTornadoEntity.class, EntityDataSerializers.FLOAT);
	private static final EntityDataAccessor<Float> HEIGHT = SynchedEntityData.defineId(AeroTornadoEntity.class, EntityDataSerializers.FLOAT);

	private Vec3 anchor;

	public AeroTornadoEntity(EntityType<? extends ThrowableProjectile> type, Level world) {
		super(type, world);
		setMaxTicks(LIFETIME);
		this.noPhysics = true;
	}

	public AeroTornadoEntity(Level world, LivingEntity caster, float dmgMult, float radius, float height) {
		super(ModEntities.TYPE_AERO_TORNADO.get(), caster, world);
		this.dmgMult = dmgMult;
		this.entityData.set(RADIUS, radius);
		this.entityData.set(HEIGHT, height);
		setDamageType(KKDamageTypes.AIR);
		setMaxTicks(LIFETIME);
		this.noPhysics = true;
	}

	@Override
	protected void defineSynchedData(SynchedEntityData.Builder builder) {
		super.defineSynchedData(builder);
		builder.define(RADIUS, 3F);
		builder.define(HEIGHT, 5F);
	}

	public float getRadius() {
		return this.entityData.get(RADIUS);
	}

	public float getHeight() {
		return this.entityData.get(HEIGHT);
	}

	public float getReach() {
		return getRadius() * Math.min(1F, (float) tickCount / SPIN_UP);
	}

	@Override
	protected double getDefaultGravity() {
		return 0;
	}

	@Override
	public void tick() {
		if (anchor == null) {
			anchor = position();
		}

		setDeltaMovement(Vec3.ZERO);

		super.tick();

		setPos(anchor.x, anchor.y, anchor.z);
		setDeltaMovement(Vec3.ZERO);

		if (getOwner() == null) {
			return;
		}

		float reach = getReach();

		if (level().isClientSide) {
			smoke(reach);
			return;
		}

		if (tickCount == RELEASE) {
			release(reach);
			return;
		}

		if (tickCount > RELEASE) {
			return;
		}

		for (Player friend : getPartyMembersFlowmotion(reach)) {
			launchFlowmotion(friend);
		}

		for (Entity caught : caught(reach)) {
			double dx = caught.getX() - getX();
			double dz = caught.getZ() - getZ();
			double distance = Math.sqrt(dx * dx + dz * dz);

			if (distance < 0.05) {
				dx = 0.05;
				distance = 0.05;
			}

			// Round the axis: the pull's own vector turned a quarter turn, which is what a circle is made of
			double swirlX = -dz / distance * SWIRL;
			double swirlZ = dx / distance * SWIRL;

			double inX = -dx / distance * PULL;
			double inZ = -dz / distance * PULL;

			double gap = (getY() + getHeight() - LID) - caught.getY();
			double y = Mth.clamp(gap * APPROACH, -SINK, RISE);

			caught.setDeltaMovement(swirlX + inX, y, swirlZ + inZ);
			caught.hurtMarked = true;
			caught.fallDistance = 0;

			// Once per second, so it wears them down over the spin rather than shredding them on contact
			if (caught instanceof LivingEntity living && tickCount % 20 == 0) {
				damageEntity(living);
			}
		}
	}

	private void release(float reach) {
		for (Entity caught : caught(reach)) {
			double dx = caught.getX() - getX();
			double dz = caught.getZ() - getZ();
			double distance = Math.max(Math.sqrt(dx * dx + dz * dz), 0.05);

			caught.setDeltaMovement(dx / distance * THROW_OUT, THROW_UP, dz / distance * THROW_OUT);
			caught.hurtMarked = true;
			caught.fallDistance = 0;
		}

		level().playSound(null, getX(), getY(), getZ(), ModSounds.aero2.get(), SoundSource.PLAYERS, 1F, 0.8F);

		if (level() instanceof ServerLevel server) {
			server.sendParticles(WIND_PARTICLES, getX(), getY() + getHeight() * 0.4, getZ(), 60, reach * 0.6, getHeight() * 0.3, reach * 0.6, 0.25);
		}
	}

	private void smoke(float reach) {
		int arms = 3;
		float turn = (tickCount * SWIRL) % (float) (Math.PI * 2);
		float height = getHeight();

		for (int arm = 0; arm < arms; arm++) {
			for (int step = 0; step < 8; step++) {
				double up = height * (step / 8D);
				// Wider as it rises, which is the silhouette a funnel has
				double r = reach * (0.35 + 0.65 * (step / 8D));
				double angle = turn + arm * (Math.PI * 2 / arms) + step * 0.6;

				level().addParticle(ParticleTypes.CLOUD, getX() + Math.cos(angle) * r, getY() + up, getZ() + Math.sin(angle) * r, -Math.sin(angle) * 0.2, 0.05, Math.cos(angle) * 0.2);
			}
		}
	}

	// Height from the base which launches via flowmotion
	private static final double BASE_BAND = 1.5;

	private List<Player> getPartyMembersFlowmotion(float reach) {
		return level().getEntitiesOfClass(Player.class, getBoundingBox().inflate(reach, BASE_BAND, reach), player -> {
			if (!player.isAlive() || player.isSpectator() || !canRide(player)) {
				return false;
			}

			// A box is square and the base is round, so the corners have to go
			double dx = player.getX() - getX();
			double dz = player.getZ() - getZ();

			return dx * dx + dz * dz <= reach * reach && player.getY() >= getY() - BASE_BAND && player.getY() <= getY() + BASE_BAND;
		});
	}

	private static boolean canRide(Player player) {
		PlayerData data = PlayerData.get(player);
		return data != null && data.isAbilityEquipped(ModAbilities.WALL_KICK) && !data.inFlowmotion();
	}

	private void launchFlowmotion(Player player) {
		PlayerData data = PlayerData.get(player);

		if (data == null) {
			return;
		}

		data.setFlowmotion(true);
		data.setAirDashed(false);
		data.setBounced(false);

		Vec3 going = player.getDeltaMovement();
		player.setDeltaMovement(going.x * 0.4, FLOWMOTION_UP, going.z * 0.4);
		player.hurtMarked = true;
		player.fallDistance = 0;

		PacketHandler.syncToAllAround(player, data);
		level().playSound(null, player.getX(), player.getY(), player.getZ(), ModSounds.wall_grab.get(), SoundSource.PLAYERS, 1F, 1.2F);

		if (level() instanceof ServerLevel server) {
			server.sendParticles(WIND_PARTICLES, player.getX(), player.getY() + 1, player.getZ(), 25, 0.4, 0.6, 0.4, 0.05);
		}
	}

	// Everything the tornado holds except owner and party
	private List<Entity> caught(float reach) {
		List<Entity> list = level().getEntities(getOwner(), getBoundingBox().inflate(reach, getHeight(), reach));
		return getOwner() instanceof Player player ? Utils.removePartyMembersFromList(player, list) : list;
	}
}
