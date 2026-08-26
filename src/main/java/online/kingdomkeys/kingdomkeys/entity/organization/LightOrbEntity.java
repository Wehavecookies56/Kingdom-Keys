package online.kingdomkeys.kingdomkeys.entity.organization;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ThrowableProjectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import online.kingdomkeys.kingdomkeys.damagesource.KKDamageTypes;
import online.kingdomkeys.kingdomkeys.entity.ModEntities;

public class LightOrbEntity extends ThrowableProjectile {

	private static final int MAX_TICKS = 200;

	private float dmg;

	public LightOrbEntity(EntityType<? extends ThrowableProjectile> type, Level world) {
		super(type, world);
		this.blocksBuilding = false;
	}

	public LightOrbEntity(Level world, Player caster, float dmg) {
		super(ModEntities.TYPE_LIGHT_ORB.get(), caster, world);
		this.dmg = dmg;
	}

	@Override
	protected double getDefaultGravity() {
		return 0D;
	}

	@Override
	public void tick() {
		if (this.tickCount > MAX_TICKS) {
			this.remove(RemovalReason.KILLED);
			return;
		}

		if (level() instanceof ServerLevel serverLevel && tickCount > 2) {
			serverLevel.sendParticles(ParticleTypes.END_ROD, getX(), getY(), getZ(), 2, 0.03, 0.03, 0.03, 0.005);
		}

		super.tick();
	}

	@Override
	protected void onHit(HitResult result) {
		if (level().isClientSide)
			return;

		if (result instanceof EntityHitResult entityHit && entityHit.getEntity() instanceof LivingEntity target && getOwner() instanceof Player caster) {
			if (target != caster) {
				target.hurt(KKDamageTypes.getElementalDamage(KKDamageTypes.LIGHT, this, caster), dmg);
				target.invulnerableTime = 0;
			}
		}

		if (level() instanceof ServerLevel serverLevel) {
			serverLevel.sendParticles(ParticleTypes.FLASH, getX(), getY(), getZ(), 1, 0, 0, 0, 0);
			serverLevel.sendParticles(ParticleTypes.END_ROD, getX(), getY(), getZ(), 10, 0.1, 0.1, 0.1, 0.05);
		}

		this.remove(RemovalReason.KILLED);
	}

	@Override
	protected void defineSynchedData(net.minecraft.network.syncher.SynchedEntityData.Builder pBuilder) {
	}
}
