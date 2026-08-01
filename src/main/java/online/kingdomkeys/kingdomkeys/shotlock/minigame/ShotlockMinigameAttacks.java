package online.kingdomkeys.kingdomkeys.shotlock.minigame;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import online.kingdomkeys.kingdomkeys.client.sound.ModSounds;
import online.kingdomkeys.kingdomkeys.damagesource.KKDamageTypes;
import online.kingdomkeys.kingdomkeys.effects.ModMobEffects;
import online.kingdomkeys.kingdomkeys.entity.organization.FlameRingCoreEntity;
import online.kingdomkeys.kingdomkeys.entity.organization.IcePillarsCoreEntity;
import online.kingdomkeys.kingdomkeys.entity.organization.LightBarrageCoreEntity;
import online.kingdomkeys.kingdomkeys.entity.organization.RockyPillarsCoreEntity;
import online.kingdomkeys.kingdomkeys.entity.organization.WaterWallCoreEntity;
import online.kingdomkeys.kingdomkeys.entity.shotlock.UltimaCannonShotEntity;

import java.util.List;

public final class ShotlockMinigameAttacks {

	private ShotlockMinigameAttacks() {
	}

	// Radius of the landing shockwave.
	private static final double IMPACT_RADIUS = 3.5D;
	// How wide the dashing mash hit sweeps as the caster charges through.
	private static final double DASH_RADIUS = 2.0D;

	// Teleports the caster above the target, drops them onto it and detonates the elemental impact.
	// power: scales the damage - a missed prompt still lands, just weakly
	public static void slam(Player player, Entity target, ResourceKey<DamageType> element, float damage, float power) {
		if (player.level().isClientSide || target == null || !target.isAlive()) {
			return;
		}

		Vec3 landing = target.position();
		player.teleportTo(landing.x, landing.y + 2.5D, landing.z);
		player.setDeltaMovement(0, -1.2D, 0);
		player.hurtMarked = true;
		player.resetFallDistance();

		float dealt = damage * power;

		AABB area = new AABB(landing, landing).inflate(IMPACT_RADIUS, IMPACT_RADIUS * 0.8D, IMPACT_RADIUS);
		List<LivingEntity> hit = player.level().getEntitiesOfClass(LivingEntity.class, area, e -> e.isAlive() && e != player);

		for (LivingEntity victim : hit) {
			victim.invulnerableTime = 0;
			victim.hurt(buildDamageSource(player, victim, element), dealt);
			applyElementalRider(victim, element);
		}

		spawnFlavour(player, element, dealt);
		spawnImpactParticles(player, landing, element);
	}

	public static void dash(Player player, Entity target, ResourceKey<DamageType> element, float damage) {
		if (player.level().isClientSide || target == null || !target.isAlive()) {
			return;
		}

		// Same speed factor the Sonic Blade core uses per hop, so the two read as the same move.
		double speedFactor = 0.4D;
		player.setDeltaMovement(
				(target.getX() - player.getX()) * speedFactor,
				(target.getY() + 1 - player.getY()) * speedFactor,
				(target.getZ() - player.getZ()) * speedFactor);
		player.hurtMarked = true;
		player.resetFallDistance();

		if (player.getVehicle() != null) {
			player.getVehicle().onPassengerTurned(player);
		}

		AABB area = player.getBoundingBox().inflate(DASH_RADIUS, 1.0D, DASH_RADIUS);
		List<LivingEntity> hit = player.level().getEntitiesOfClass(LivingEntity.class, area, e -> e.isAlive() && e != player);

		for (LivingEntity victim : hit) {
			victim.invulnerableTime = 0;
			victim.hurt(buildDamageSource(player, victim, element), damage);
			applyElementalRider(victim, element);
		}

		// The target itself always eats the hit even if the lunge hasn't closed the gap yet.
		if (!hit.contains(target) && target instanceof LivingEntity living) {
			living.invulnerableTime = 0;
			living.hurt(buildDamageSource(player, living, element), damage);
			applyElementalRider(living, element);
		}

		spawnImpactParticles(player, player.position(), element);
	}

	private static DamageSource buildDamageSource(Player player, LivingEntity victim, ResourceKey<DamageType> element) {
		if (element != null) {
			return KKDamageTypes.getElementalDamage(element, player, player);
		}
		return victim.damageSources().playerAttack(player);
	}

	private static void applyElementalRider(LivingEntity victim, ResourceKey<DamageType> element) {
		if (element == null) {
			return;
		}

		if (KKDamageTypes.ICE.equals(element)) {
			victim.addEffect(new MobEffectInstance(ModMobEffects.FREEZE, 60, 0, false, true, true));
		} else if (KKDamageTypes.FIRE.equals(element)) {
			victim.igniteForSeconds(4);
		} else if (KKDamageTypes.DARKNESS.equals(element)) {
			victim.addEffect(new MobEffectInstance(MobEffects.WITHER, 60, 0, false, true, true));
		} else if (KKDamageTypes.LIGHTNING.equals(element)) {
			victim.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 40, 2, false, true, true));
		} else if (KKDamageTypes.AIR.equals(element)) {
			victim.setDeltaMovement(victim.getDeltaMovement().add(0, 0.7D, 0));
			victim.hurtMarked = true;
		}
	}

	// Reuses the existing limit cores for the landing effect - they all spawn around the caster, and
	// the caster is stood on top of the target by the time this runs.
	private static void spawnFlavour(Player player, ResourceKey<DamageType> element, float damage) {
		Entity core = null;

		if (element == null) {
			core = new RockyPillarsCoreEntity(player.level(), player, damage);
		} else if (KKDamageTypes.ICE.equals(element)) {
			core = new IcePillarsCoreEntity(player.level(), player, damage);
		} else if (KKDamageTypes.FIRE.equals(element)) {
			core = new FlameRingCoreEntity(player.level(), player, damage);
		} else if (KKDamageTypes.WATER.equals(element)) {
			core = new WaterWallCoreEntity(player.level(), player, damage);
		} else if (KKDamageTypes.LIGHT.equals(element)) {
			core = new LightBarrageCoreEntity(player.level(), player, damage);
		} else if (KKDamageTypes.LIGHTNING.equals(element)) {
			if (player.level() instanceof ServerLevel serverLevel) {
				serverLevel.sendParticles(ParticleTypes.ELECTRIC_SPARK, player.getX(), player.getY() + 1, player.getZ(), 60, 1.5, 1.0, 1.5, 0.3);
			}
			player.level().playSound(null, player.blockPosition(), ModSounds.thundagaShot.get(), SoundSource.PLAYERS, 1F, 1F);
		} else {
			core = new RockyPillarsCoreEntity(player.level(), player, damage);
		}

		if (core != null) {
			player.level().addFreshEntity(core);
		}
	}

	private static void spawnImpactParticles(Player player, Vec3 landing, ResourceKey<DamageType> element) {
		if (!(player.level() instanceof ServerLevel serverLevel)) {
			return;
		}

		serverLevel.sendParticles(ParticleTypes.EXPLOSION, landing.x, landing.y + 0.5D, landing.z, 3, 0.6, 0.3, 0.6, 0);

		if (element == null) {
			serverLevel.sendParticles(ParticleTypes.CRIT, landing.x, landing.y + 0.5D, landing.z, 30, 1.0, 0.4, 1.0, 0.15);
		} else if (KKDamageTypes.ICE.equals(element)) {
			serverLevel.sendParticles(ParticleTypes.SNOWFLAKE, landing.x, landing.y + 0.5D, landing.z, 40, 1.2, 0.5, 1.2, 0.1);
		} else if (KKDamageTypes.FIRE.equals(element)) {
			serverLevel.sendParticles(ParticleTypes.FLAME, landing.x, landing.y + 0.5D, landing.z, 40, 1.2, 0.5, 1.2, 0.1);
		} else if (KKDamageTypes.WATER.equals(element)) {
			serverLevel.sendParticles(ParticleTypes.SPLASH, landing.x, landing.y + 0.5D, landing.z, 50, 1.2, 0.5, 1.2, 0.15);
		} else if (KKDamageTypes.DARKNESS.equals(element)) {
			serverLevel.sendParticles(ParticleTypes.SQUID_INK, landing.x, landing.y + 0.5D, landing.z, 30, 1.2, 0.5, 1.2, 0.05);
		} else if (KKDamageTypes.LIGHT.equals(element)) {
			serverLevel.sendParticles(ParticleTypes.END_ROD, landing.x, landing.y + 0.5D, landing.z, 40, 1.2, 0.5, 1.2, 0.1);
		} else if (KKDamageTypes.AIR.equals(element)) {
			serverLevel.sendParticles(ParticleTypes.CLOUD, landing.x, landing.y + 0.5D, landing.z, 40, 1.2, 0.5, 1.2, 0.15);
		}
	}

	// The shot colour the minigame projectiles use, picked off the Shotlock's element.
	public static void cannonBlast(Player player, Entity target, ResourceKey<DamageType> element, float damage, int colour) {
		if (player.level().isClientSide || target == null || !target.isAlive()) {
			return;
		}

		UltimaCannonShotEntity shot = new UltimaCannonShotEntity(player.level(), player, target, damage);
		shot.setElement(element);
		shot.setColor(colour);

		Vec3 forward = player.getLookAngle().normalize();
		Vec3 spawn = player.position().add(forward.scale(2D));
		shot.setPos(spawn.x, spawn.y + player.getEyeHeight(), spawn.z);

		player.level().addFreshEntity(shot);
	}

	public static int shotColour(ResourceKey<DamageType> element) {
		if (element == null) {
			return 0xFFD75A;
		}
		if (KKDamageTypes.ICE.equals(element)) {
			return 0xB4E1FF;
		}
		if (KKDamageTypes.FIRE.equals(element)) {
			return 0xFF7A2A;
		}
		if (KKDamageTypes.WATER.equals(element)) {
			return 0x4FA8FF;
		}
		if (KKDamageTypes.LIGHTNING.equals(element)) {
			return 0xFFF14F;
		}
		if (KKDamageTypes.DARKNESS.equals(element)) {
			return 0x8C4FD1;
		}
		if (KKDamageTypes.LIGHT.equals(element)) {
			return 0xFFFFFF;
		}
		if (KKDamageTypes.AIR.equals(element)) {
			return 0xB6FFC8;
		}
		return 0xFFD75A;
	}
}
