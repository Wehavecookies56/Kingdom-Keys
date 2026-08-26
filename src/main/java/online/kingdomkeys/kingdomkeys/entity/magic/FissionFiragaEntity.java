package online.kingdomkeys.kingdomkeys.entity.magic;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ThrowableProjectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import online.kingdomkeys.kingdomkeys.effects.ModMobEffects;
import online.kingdomkeys.kingdomkeys.entity.ModEntities;
import online.kingdomkeys.kingdomkeys.util.Utils;

import java.util.List;

public class FissionFiragaEntity extends FiragaEntity {

	public FissionFiragaEntity(EntityType<? extends ThrowableProjectile> type, Level world) {
		super(type, world);
		this.blocksBuilding = true;
	}

	public FissionFiragaEntity(Level world, LivingEntity player, float dmgMult, LivingEntity lockOnEntity) {
		super(ModEntities.TYPE_FISSIONFIRAGA.get(), world, player, dmgMult, lockOnEntity);
	}

	public List<SimpleParticleType> getParticles() {
		return List.of(ParticleTypes.FLAME);
	}

	@Override
	protected double getDefaultGravity() {
		return 0;
	}

	@Override
	protected void onHit(HitResult rtRes) {
		//super.onHit(rtRes);
		if (!level().isClientSide && getOwner() != null) {
			EntityHitResult ertResult = null;
			BlockHitResult brtResult = null;

			if (rtRes instanceof EntityHitResult) {
				ertResult = (EntityHitResult) rtRes;
			}

			if (rtRes instanceof BlockHitResult) {
				brtResult = (BlockHitResult) rtRes;
			}

			LivingEntity target = null;
			if (ertResult != null && ertResult.getEntity() instanceof LivingEntity t) {
				target = t;
			}

			if (target != null) {
				if (target != getOwner()) {
					if (target.getEffect(ModMobEffects.FREEZE) != null) {
						target.removeEffect(ModMobEffects.FREEZE);
					}
					if (Utils.canHarm(getOwner(), target)) {
						target.setRemainingFireTicks(15);
						damageEntity(target);
						target.invulnerableTime = 0;
					}
				}
			}

			float radius = 2.5F;

			interactWithBlocks(rtRes, radius);

			List<Entity> list = level().getEntities(getOwner(), getBoundingBox().inflate(radius));
			list = Utils.removePartyMembersFromList((Player) getOwner(), list);
			if (target != null) { //If was direct impact remove the target from the explosion damage
				list.remove(target);
			}

			for (SimpleParticleType p : getParticles()) {
				((ServerLevel) level()).sendParticles(p, getX(), getY(), getZ(), 100 / getParticles().size(), Math.random() - 0.5D, Math.random() - 0.5D, Math.random() - 0.5D, 0.1);
			}

			radius -= 1.5F;
			for (int a = 0; a < 360; a += 5) {
				double angle = Math.toRadians(a);
				double dirX = Math.sin(angle);
				double dirZ = Math.cos(angle);

				double x = getX() + radius * dirX;
				double z = getZ() + radius * dirZ;

				((ServerLevel) level()).sendParticles(ParticleTypes.FLAME, x, getY(), z, 0, dirX * 0.15, 0, dirZ * 0.15, 1);
			}

			if (!list.isEmpty()) {
				for (Entity e : list) {
					if (e instanceof LivingEntity ent) {
						e.setRemainingFireTicks(15);
						damageEntity(ent);

						if (ent.getEffect(ModMobEffects.FREEZE) != null) {
							ent.removeEffect(ModMobEffects.FREEZE);
						}
					}
				}
			}

			remove(RemovalReason.KILLED);
		}
	}
}
