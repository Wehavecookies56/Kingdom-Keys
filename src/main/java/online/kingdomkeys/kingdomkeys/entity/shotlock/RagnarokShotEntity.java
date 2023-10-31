package online.kingdomkeys.kingdomkeys.entity.shotlock;

import java.awt.Color;

import org.joml.Vector3f;

import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.world.damagesource.DamageSource;
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

	public RagnarokShotEntity(Level world) {
		super(ModEntities.TYPE_RAGNAROK_SHOTLOCK_SHOT.get(), world);
		this.blocksBuilding = true;
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
			//world.addParticle(ParticleTypes.ENTITY_EFFECT, getPosX(), getPosY(), getPosZ(), color.getRed()/255F, color.getGreen()/255F, color.getBlue()/255F);
		}
		
		/*if(ticksExisted < 20) {
			//Open
			double X = getPosX();
			double Y = getPosY();
			double Z = getPosZ();
			
			float r = 2;
			double alpha = Math.toRadians(getCaster().rotationYaw);						
			double theta = 2 * Math.PI / getTargets().size();
			double x = X + r * ((Math.cos(i * theta) + Math.sin(alpha) * Math.sin(alpha) * (1 - Math.cos(i * theta))) * Math.cos(alpha) + (-Math.cos(alpha) * Math.sin(alpha) * (1 - Math.cos(i * theta))) * Math.sin(alpha));
			double y = Y + r * ((Math.cos(alpha) * Math.sin(i * theta)) * Math.cos(alpha) + Math.sin(alpha) * Math.sin(i * theta) * Math.sin(alpha));
			double z = Z + r * (-Math.cos(alpha) * Math.sin(alpha) * (1 - Math.cos(i * theta)) * Math.cos(alpha) + (Math.cos(i * theta) + Math.cos(alpha) * Math.cos(alpha) * (1 - Math.cos(i * theta))) * Math.sin(alpha));*			double x = getCaster().getPosX() + X;
			double y = getCaster().getPosY() + Y;
			double z = getCaster().getPosZ() + Z;
			
			this.shoot(x,y,z,0.3F,0);
			
			//this.setPosition(x,y,z);
			//this.setMaxTicks(maxTicks + 20);
		}*/
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

			EntityHitResult ertResult = null;
			BlockHitResult brtResult = null;

			if (rtRes instanceof EntityHitResult) {
				ertResult = (EntityHitResult) rtRes;
			}

			if (rtRes instanceof BlockHitResult) {
				brtResult = (BlockHitResult) rtRes;
			}

			if (ertResult != null && ertResult.getEntity() instanceof LivingEntity) {
				LivingEntity target = (LivingEntity) ertResult.getEntity();
				if (target != getOwner()) {
	            	target.hurt(target.damageSources().thrown(this, this.getOwner()), dmg);
					super.remove(RemovalReason.KILLED);
				}
			}
			remove(RemovalReason.KILLED);
		}
	}
	
	@Override
	public void remove(RemovalReason reason) {
		if(tickCount > 40) {
			super.remove(RemovalReason.KILLED);
		}
	}

}