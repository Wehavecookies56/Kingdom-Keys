package online.kingdomkeys.kingdomkeys.entity.magic;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ThrowableProjectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.HitResult;
import online.kingdomkeys.kingdomkeys.effects.ModMobEffects;
import online.kingdomkeys.kingdomkeys.entity.ModEntities;

import java.util.List;

public class FissionFiragaEntity extends FireEntity {

	public FissionFiragaEntity(EntityType<? extends ThrowableProjectile> type, Level world) {
		super(type, world);
		this.blocksBuilding = true;
	}

	public FissionFiragaEntity(Level world, LivingEntity player, float dmgMult, LivingEntity lockOnEntity) {
		super(ModEntities.TYPE_FISSIONFIRAGA.get(), world, player, dmgMult, lockOnEntity, Tier.FIRAGA);
	}

	public List<SimpleParticleType> getParticles() {
		return List.of(ParticleTypes.FLAME);
	}

	@Override
	protected double getDefaultGravity() {
		return 0;
	}

	@Override
	protected void onImpact(HitResult hit) {
		LivingEntity struck = struck(hit);
		if (struck != null) {
			struck.removeEffect(ModMobEffects.FREEZE);
			if (canHarm(struck)) {
				struck.setRemainingFireTicks(15);
				damageEntity(struck);
				struck.invulnerableTime = 0;
			}
		}

		float radius = 2.5F;
		burst(hit, struck, radius, 100);

		radius -= 1.5F;
		for (int a = 0; a < 360; a += 5) {
			double angle = Math.toRadians(a);
			double dirX = Math.sin(angle);
			double dirZ = Math.cos(angle);

			double x = getX() + radius * dirX;
			double z = getZ() + radius * dirZ;

			((ServerLevel) level()).sendParticles(ParticleTypes.FLAME, x, getY(), z, 0, dirX * 0.15, 0, dirZ * 0.15, 1);
		}

		remove(RemovalReason.KILLED);
	}
}
