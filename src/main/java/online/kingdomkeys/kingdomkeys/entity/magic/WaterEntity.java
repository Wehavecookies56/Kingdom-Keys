package online.kingdomkeys.kingdomkeys.entity.magic;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ThrowableProjectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.HitResult;
import online.kingdomkeys.kingdomkeys.damagesource.KKDamageTypes;
import online.kingdomkeys.kingdomkeys.entity.ModEntities;
import online.kingdomkeys.kingdomkeys.entity.ProjectileHits;

import java.util.function.Supplier;

// Water, Watera, Waterga and Waterza: a shield spun around the caster that is then thrown, the higher tiers bursting where it lands
public class WaterEntity extends BaseMagicProjectile {

	public enum Tier {
		WATER(ModEntities.TYPE_WATER::get, 50, 1F, 40, 1F, 0.2F, 0, 1),
		WATERA(ModEntities.TYPE_WATERA::get, 35, 1.2F, 50, 1.25F, 0.3F, 1, 1),
		WATERGA(ModEntities.TYPE_WATERGA::get, 25, 1.4F, 100, 1.75F, 0.4F, 2, 1),
		WATERZA(ModEntities.TYPE_WATERZA::get, 20, 1.6F, 100, 2F, 0.5F, 6, 2);

		final Supplier<EntityType<WaterEntity>> type;
		// How long the shield spins before it is thrown, how wide and how fast
		final int shieldTicks;
		final float shieldRadius;
		final int spin;
		final float speed, trailRadius;
		// The burst where it lands, none for plain Water. The step spaces out the burst's rings of droplets
		final float splashRadius;
		final int splashStep;

		Tier(Supplier<EntityType<WaterEntity>> type, int shieldTicks, float shieldRadius, int spin, float speed, float trailRadius, float splashRadius, int splashStep) {
			this.type = type;
			this.shieldTicks = shieldTicks;
			this.shieldRadius = shieldRadius;
			this.spin = spin;
			this.speed = speed;
			this.trailRadius = trailRadius;
			this.splashRadius = splashRadius;
			this.splashStep = splashStep;
		}

		static Tier of(EntityType<?> type) {
			for (Tier tier : values()) {
				if (tier.type.get() == type) {
					return tier;
				}
			}
			return WATER;
		}
	}

	final Tier tier;

	public WaterEntity(EntityType<? extends ThrowableProjectile> type, Level world) {
		super(type, world);
		this.tier = Tier.of(type);
	}

	public WaterEntity(Level world, LivingEntity player, float dmgMult, Tier tier) {
		super(tier.type.get(), player, world);
		this.tier = tier;
		this.dmgMult = dmgMult;
		setDamageType(KKDamageTypes.WATER);
	}

	double a = 0;

	@Override
	public void tick() {
		if(getOwner() == null)
			return;

		if(tickCount <= 1) {
			this.setDeltaMovement(0, 0, 0);

		} else if (tickCount < tier.shieldTicks) { //Shield
			setPos(getOwner().getX(), getY(), getOwner().getZ());
			double radius = tier.shieldRadius;
			double cx = getX();
			double cy = getY();
			double cz = getZ();

			a += tier.spin; //Speed and distance between particles
			double radA = Math.toRadians(a);
			double cosA = Math.cos(radA);
			double sinA = Math.sin(radA);
			double x = cx + (radius * cosA);
			double z = cz + (radius * sinA);

			double x2 = cx + (radius * cosA);
			double z2 = cz + (radius * -sinA);

			if(!level().isClientSide) {
				((ServerLevel) level()).sendParticles(ParticleTypes.DRIPPING_WATER, x,  (cy+0.5) - a / 1080D, z, 1, 0,0,0, 0.5);
				((ServerLevel) level()).sendParticles(ParticleTypes.DOLPHIN, x2, (cy+0.5) - a / 1080D, z2, 1, 0,0,0, 0.5);
			}

			for (LivingEntity e : ProjectileHits.around(getOwner(), getOwner(), radius, null)) {
				damageEntity(e);
			}

		} else { //Projectile
			shootFromRotation(getOwner(), getOwner().getXRot(), getOwner().getYRot(), 0, tier.speed, 0);
			getOwner().level().playSound(null, getOwner().blockPosition(), SoundEvents.PLAYER_SWIM, SoundSource.PLAYERS, 1F, 1F);

			hurtMarked = true;
			float radius = tier.trailRadius;
			for (int t = 1; t < 360; t += 30) {
				double radT = Math.toRadians(t);
				double sinT = Math.sin(radT);
				double y = getY() + (radius * Math.cos(radT));
				for (int s = 1; s < 360 ; s += 30) {
					double radS = Math.toRadians(s);
					double x = getX() + (radius * Math.cos(radS) * sinT);
					double z = getZ() + (radius * Math.sin(radS) * sinT);
					if(!level().isClientSide)
						((ServerLevel) level()).sendParticles(ParticleTypes.DOLPHIN, x, y, z, 1, 0,0,0, 0.5);
				}
			}

		}

		super.tick();
	}

	@Override
	protected void onImpact(HitResult hit) {
		LivingEntity struck = struck(hit);

		if (tier.splashRadius <= 0) {
			// It puts out whoever is burning and carries on, it only splashes on someone it may hurt or on the ground
			if (struck == null) {
				remove(RemovalReason.KILLED);
			} else if (struck.isOnFire()) {
				struck.clearFire();
			} else if (canHarm(struck)) {
				damageEntity(struck);
				remove(RemovalReason.KILLED);
			}

			interactWithBlocks(hit, 0);
			return;
		}

		if (struck != null) {
			if (struck.isOnFire()) {
				struck.clearFire();
			} else if (canHarm(struck)) {
				damageEntity(struck);
			}
		}

		float radius = tier.splashRadius;

		interactWithBlocks(hit, radius);

		for (int t = 1; t < 360; t += 10) {
			double radT = Math.toRadians(t);
			double sinT = Math.sin(radT);
			double cosT = Math.cos(radT);
			for (int s = 1; s < 360 ; s += 10) {
				double radS = Math.toRadians(s);
				double cosS = Math.cos(radS);
				double sinS = Math.sin(radS);
				for(int r = tier.splashStep; r <= radius; r += tier.splashStep) {
					double x = getX() + (r * cosS * sinT);
					double z = getZ() + (r * sinS * sinT);
					double y = getY() + (r * cosT);
					((ServerLevel) level()).sendParticles(ParticleTypes.DRIPPING_WATER, x, y, z, 1, Math.random() - 0.5D, Math.random() - 0.5D, Math.random() - 0.5D, 0.5);
				}
			}
		}

		// Fire around is put out whoever it is on, the splash itself only hurts those the caster may hurt
		for (LivingEntity e : everyoneAround(radius, struck)) {
			if (e.isOnFire()) {
				e.clearFire();
			} else if (canHarm(e)) {
				damageEntity(e);
			}
		}

		remove(RemovalReason.KILLED);
	}
}
