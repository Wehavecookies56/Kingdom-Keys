package online.kingdomkeys.kingdomkeys.entity.magic;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ThrowableProjectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import online.kingdomkeys.kingdomkeys.damagesource.KKDamageTypes;
import online.kingdomkeys.kingdomkeys.effects.ModMobEffects;
import online.kingdomkeys.kingdomkeys.entity.ModEntities;

import javax.annotation.Nullable;
import java.util.List;
import java.util.function.Supplier;

// Fire, Fira, Firaga and Firaza: a homing fireball that sets its target alight, Firaga and Firaza also blowing up around it
public class FireEntity extends BaseMagicProjectile {

	public enum Tier {
		FIRE(ModEntities.TYPE_FIRE::get, 40F, 0.2F, -1, 5, false, 0, 0, 30, 0.08F, 0.5F),
		FIRA(ModEntities.TYPE_FIRA::get, 32F, 0.4F, 2, 10, false, 0, 0, 90, 0.1F, 0.8F),
		FIRAGA(ModEntities.TYPE_FIRAGA::get, 26F, 0.6F, 2, 15, true, 1.5F, 15, 200, 0.1F, 1.2F),
		FIRAZA(ModEntities.TYPE_FIRAZA::get, 20F, 1F, 2, 30, false, 6F, 25, 1000, 0.3F, 2F);

		final Supplier<EntityType<FireEntity>> type;
		// The higher, the slower it turns towards its target
		final float trackingSpeed;
		// The trail of flames, from this tick on
		final float trailRadius;
		final int trailAfter;
		// What it does to whoever it hits
		final int fireTicks;
		final boolean resetsInvulnerability;
		// The blast where it lands, none for Fire and Fira, whose flames only show when they hit a block
		final float splashRadius;
		final int splashFireTicks;
		final int particles;
		final float particleSpeed;
		public final float renderScale;

		Tier(Supplier<EntityType<FireEntity>> type, float trackingSpeed, float trailRadius, int trailAfter, int fireTicks, boolean resetsInvulnerability, float splashRadius, int splashFireTicks, int particles, float particleSpeed, float renderScale) {
			this.type = type;
			this.trackingSpeed = trackingSpeed;
			this.trailRadius = trailRadius;
			this.trailAfter = trailAfter;
			this.fireTicks = fireTicks;
			this.resetsInvulnerability = resetsInvulnerability;
			this.splashRadius = splashRadius;
			this.splashFireTicks = splashFireTicks;
			this.particles = particles;
			this.particleSpeed = particleSpeed;
			this.renderScale = renderScale;
		}

		// Crawling, Fission and Dark Firaga have types of their own and are all Firagas underneath
		static Tier of(EntityType<?> type) {
			for (Tier tier : values()) {
				if (tier.type.get() == type) {
					return tier;
				}
			}
			return FIRAGA;
		}
	}

	public final Tier tier;

	public FireEntity(EntityType<? extends ThrowableProjectile> type, Level world) {
		super(type, world);
		this.tier = Tier.of(type);
	}

	public FireEntity(Level world, LivingEntity player, float dmgMult, LivingEntity lockOnEntity, Tier tier) {
		this(tier.type.get(), world, player, dmgMult, lockOnEntity, tier);
	}

	protected FireEntity(EntityType<? extends FireEntity> type, Level world, LivingEntity player, float dmgMult, LivingEntity lockOnEntity, Tier tier) {
		super(type, player, world);
		this.tier = tier;
		this.dmgMult = dmgMult;
		this.lockOnEntity = lockOnEntity;
		setDamageType(KKDamageTypes.FIRE);
	}

	public List<SimpleParticleType> getParticles(){
		return List.of(ParticleTypes.FLAME);
	}

	@Override
	public void tick() {
		if(this.lockOnEntity != null && tickCount > 0) {
			double x = (this.lockOnEntity.getX() - this.getX());
			double y = (this.lockOnEntity.getY() - this.getY());
			double z = (this.lockOnEntity.getZ() - this.getZ());
			float trackingSpeed = tier.trackingSpeed;
			shoot(getDeltaMovement().x + x / trackingSpeed, getDeltaMovement().y + y / trackingSpeed, getDeltaMovement().z + z / trackingSpeed, 2F, 0);
		}

		if(tickCount > tier.trailAfter) {
			float radius = tier.trailRadius;
			double t = Math.random() * 360;
			double s = Math.random() * 360;
			double radT = Math.toRadians(t);
			double sinT = Math.sin(radT);
			double radS = Math.toRadians(s);
			double x = getX() + (radius * Math.cos(radS) * sinT);
			double z = getZ() + (radius * Math.sin(radS) * sinT);
			double y = getY() + (radius * Math.cos(radT));
			for (SimpleParticleType p : getParticles()) {
				level().addParticle(p, x, y, z, 0, 0, 0);
			}
		}
		super.tick();
	}

	@Override
	protected void onImpact(HitResult hit) {
		LivingEntity struck = struck(hit);
		if (struck != null) {
			struck.removeEffect(ModMobEffects.FREEZE);
			if (canHarm(struck)) {
				struck.setRemainingFireTicks(tier.fireTicks);
				damageEntity(struck);
				if (tier.resetsInvulnerability) {
					struck.invulnerableTime = 0;
				}
			}
		}

		burst(hit, struck, tier.splashRadius, tier.particles);
		remove(RemovalReason.KILLED);
	}

	// The blast where it lands, sparing whoever it already hit directly
	protected void burst(HitResult hit, @Nullable LivingEntity struck, float radius, int particles) {
		interactWithBlocks(hit, radius);

		if (radius > 0) {
			for (LivingEntity e : targetsAround(radius, struck)) {
				e.setRemainingFireTicks(tier.splashFireTicks);
				damageEntity(e);
				e.removeEffect(ModMobEffects.FREEZE);
			}
		} else if (!(hit instanceof BlockHitResult)) {
			return;
		}

		for(SimpleParticleType p : getParticles()) {
			((ServerLevel)level()).sendParticles(p, getX(), getY(), getZ(), particles/getParticles().size(), Math.random() - 0.5D, Math.random() - 0.5D, Math.random() - 0.5D, tier.particleSpeed);
		}
	}
}
