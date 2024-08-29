package online.kingdomkeys.kingdomkeys.entity.shotlock;

import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ThrowableProjectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import online.kingdomkeys.kingdomkeys.entity.ModEntities;
import online.kingdomkeys.kingdomkeys.util.Utils;
import org.joml.Vector3f;

import java.awt.*;
import java.util.List;

public class UltimaCannonShotEntity extends BaseShotlockShotEntity {

	public UltimaCannonShotEntity(EntityType<? extends ThrowableProjectile> type, Level world) {
		super(type, world);
		this.blocksBuilding = true;
	}

	public UltimaCannonShotEntity(Level world, LivingEntity player, Entity target, double dmg) {
		super(ModEntities.TYPE_ULTIMA_CANNON_SHOT.get(), world, player, target, dmg * 15);
	}

	@Override
	public void tick() {
		if (this.tickCount > maxTicks) {
			this.remove(RemovalReason.KILLED);
		}
		
		if(tickCount > 1) {
			Color color = new Color(getColor());
			level().addParticle(new DustParticleOptions(new Vector3f(color.getRed()/255F, color.getGreen()/255F, color.getBlue()/255F), 1F), getX(), getY(), getZ(), 1,1,1);
		}
		
		if(tickCount > 30 && tickCount % 10 == 0) {
			updateMovement();
		}
		
		super.tick();
	}

	private void updateMovement() {
		if(getTarget() != null) {
			if(getTarget().isAlive()) {
				this.shoot(getTarget().getX() - this.getX(), (getTarget().getY() + (getTarget().getBbHeight() / 2.0F) - this.getBbHeight()) - getY() + 0.5, getTarget().getZ() - this.getZ(), 1, 0);
			} else {
				if(getOwner() != null)
					this.shootFromRotation(this, getOwner().getXRot(), getOwner().getYRot(), 0, 1, 0); // Work in progress
			}
		}
	}

	@Override
	protected void onHit(HitResult rtRes) {
		super.onHit(rtRes);
		if (!level().isClientSide && getOwner() != null) {
			if (rtRes instanceof EntityHitResult ertResult) {
				if (ertResult.getEntity() instanceof LivingEntity target) {
					if (target != getOwner()) {
						target.hurt(target.damageSources().thrown(this, this.getOwner()), dmg);
					}
				}
			}
			for(int i = 0; i < 6; i++) {
				((ServerLevel) level()).sendParticles(ParticleTypes.END_ROD, getX(), getY(), getZ(), 500, Math.random()*5 - 2.5F, Math.random()*5 - 2.5F, Math.random()*5 - 2.5F, 0.1);
			}

			List<Entity> list = level().getEntities(getOwner(), getBoundingBox().inflate(8));
			list = Utils.removePartyMembersFromList((Player) getOwner(), list);

			if (!list.isEmpty()) {
                for (Entity e : list) {
                    if (e instanceof LivingEntity) { //If distance is 3 or less do full damage otherwise scale it down
						e.hurt(e.damageSources().thrown(this, this.getOwner()), dmg / e.distanceTo(this) > 5 ? e.distanceTo(this):1F);
                    }
                }
			}

		}
		this.level().explode(this.getOwner(), this.blockPosition().getX(), this.blockPosition().getY(), this.blockPosition().getZ(), 5, false, Level.ExplosionInteraction.NONE);
		remove(RemovalReason.KILLED);
	}
}