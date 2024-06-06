package online.kingdomkeys.kingdomkeys.entity.shotlock;

import java.awt.Color;

import org.joml.Vector3f;

import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ThrowableProjectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraftforge.network.PlayMessages;
import online.kingdomkeys.kingdomkeys.entity.ModEntities;

public class RagnarokShotEntity extends BaseShotlockShotEntity {
	
	public RagnarokShotEntity(EntityType<? extends ThrowableProjectile> type, Level world) {
		super(type, world);
		this.blocksBuilding = true;
	}

	public RagnarokShotEntity(PlayMessages.SpawnEntity spawnEntity, Level world) {
		super(ModEntities.TYPE_RAGNAROK_SHOTLOCK_SHOT.get(), world);
	}

	public RagnarokShotEntity(Level world, LivingEntity player, Entity target, double dmg) {
		super(ModEntities.TYPE_RAGNAROK_SHOTLOCK_SHOT.get(), world, player, target, dmg);
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

		if(tickCount % 10 == 0 && tickCount - 10 >= 6) {
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

		if (!level().isClientSide) {
			if (rtRes instanceof EntityHitResult ertResult) {
				if (ertResult.getEntity() instanceof LivingEntity target) {
					if (target != getOwner()) {
						target.invulnerableTime = 0;
						target.hurt(target.damageSources().thrown(this, this.getOwner()), dmg);
						super.remove(RemovalReason.KILLED);
					}
				}

			}
		}
		remove(RemovalReason.KILLED);

	}
	
	@Override
	public void remove(RemovalReason reason) {
		if(tickCount > 40) {
			super.remove(RemovalReason.KILLED);
		}
	}

}