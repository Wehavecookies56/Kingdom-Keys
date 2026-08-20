package online.kingdomkeys.kingdomkeys.entity.magic;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ThrowableProjectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import online.kingdomkeys.kingdomkeys.client.sound.ModSounds;
import online.kingdomkeys.kingdomkeys.damagesource.KKDamageTypes;
import online.kingdomkeys.kingdomkeys.entity.ModEntities;
import online.kingdomkeys.kingdomkeys.util.Utils;

import java.util.List;

public class AeroTornadoEntity extends BaseMagicProjectile {
	private static final int LIFETIME = 70;

	private static final int RELEASE = 55;

	// How long the column takes to be fully casted
	private static final int SPIN_UP = 10;

	private static final float SWIRL = 0.55F;

	//Pulling strength
	private static final double PULL = 0.18;

	// Lift per tick
	private static final double LIFT = 0.14;
	private static final double LIFT_CAP = 0.85;

	// Push at the end
	private static final double THROW_OUT = 0.95;
	private static final double THROW_UP = 0.55;

	private float radius = 3F;
	private float height = 5F;

	private Vec3 anchor;

	public AeroTornadoEntity(EntityType<? extends ThrowableProjectile> type, Level world) {
		super(type, world);
		setMaxTicks(LIFETIME);
		this.noPhysics = true;
	}

	public AeroTornadoEntity(Level world, LivingEntity caster, float dmgMult, float radius, float height) {
		super(ModEntities.TYPE_AERO_TORNADO.get(), caster, world);
		this.dmgMult = dmgMult;
		this.radius = radius;
		this.height = height;
		setDamageType(KKDamageTypes.AIR);
		setMaxTicks(LIFETIME);
		this.noPhysics = true;
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

		float opening = Math.min(1F, (float) tickCount / SPIN_UP);
		float reach = radius * opening;

		if (level().isClientSide) {
			spin(reach);
			return;
		}

		if (tickCount == RELEASE) {
			release(reach);
			return;
		}

		if (tickCount > RELEASE) {
			return;
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

			double rise = caught.getY() < getY() + height ? LIFT : 0;
			double y = Math.min(caught.getDeltaMovement().y + rise, LIFT_CAP);

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
			server.sendParticles(ParticleTypes.CLOUD, getX(), getY() + height * 0.4, getZ(), 60, reach * 0.6, height * 0.3, reach * 0.6, 0.25);
		}
	}

	// Everything the tornado holds except owner and party
	private List<Entity> caught(float reach) {
		List<Entity> list = level().getEntities(getOwner(), getBoundingBox().inflate(reach, height, reach));
		return getOwner() instanceof Player player ? Utils.removePartyMembersFromList(player, list) : list;
	}

	//Particles
	private void spin(float reach) {
		int arms = 3;
		float turn = (tickCount * SWIRL) % (float) (Math.PI * 2);

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

}
