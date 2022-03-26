package online.kingdomkeys.kingdomkeys.entity.shotlock;

import java.awt.Color;

import com.mojang.math.Vector3f;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.util.Mth;
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

public class SonicBladeEntity extends BaseShotlockShotEntity{
	public SonicBladeEntity(EntityType<? extends ThrowableProjectile> type, Level world) {
		super(type, world);
		this.blocksBuilding = true;
	}

	public SonicBladeEntity(PlayMessages.SpawnEntity spawnEntity, Level world) {
		super(ModEntities.TYPE_VOLLEY_SHOTLOCK_SHOT.get(), world);
	}

	public SonicBladeEntity(Level world) {
		super(ModEntities.TYPE_VOLLEY_SHOTLOCK_SHOT.get(), world);
		this.blocksBuilding = true;
	}

	public SonicBladeEntity(Level world, LivingEntity player, Entity target, double dmg) {
		super(ModEntities.TYPE_VOLLEY_SHOTLOCK_SHOT.get(), world, player, target, dmg);
		setCaster(player.getUUID());
	}

	@Override
	public void tick() {
		if (getCaster() != null && getTarget() != null) {
			BlockPos pos = getTarget().blockPosition();
			float speedFactor = 0.5F;
			getCaster().setDeltaMovement((pos.getX() - getCaster().getX()) * speedFactor, (pos.getY() - getCaster().getY())  * speedFactor, (pos.getZ() - getCaster().getZ()) * speedFactor);

			if (level.isClientSide) {
				getCaster().hurtMarked = true;
			}
				
			double dx = getCaster().getX() - getTarget().getX();
            double dz = getCaster().getZ() - getTarget().getZ();
            double dy = getCaster().getY() - (getTarget().getY() + (getTarget().getBbHeight() / 2.0F)-getCaster().getBbHeight());
            double angle = Math.atan2(dz, dx) * 180 / Math.PI;
            double pitch = Math.atan2(dy, Math.sqrt(dx * dx + dz * dz)) * 180 / Math.PI;
            double distance = getCaster().distanceTo(getTarget());
            
            float rYaw = (float) Mth.wrapDegrees(angle - getCaster().getYRot()) + 90;
            float rPitch = (float) pitch - (float) (10.0F / Math.sqrt(distance)) + (float) (distance * Math.PI / 90);
            
            float f = getCaster().getXRot();
            float f1 = getCaster().getYRot();
            
            getCaster().setYRot((float)((double)getCaster().getYRot() + (double)rYaw * 0.15D));
            getCaster().setXRot((float)((double)getCaster().getXRot() - (double)-(rPitch - getCaster().getXRot()) * 0.15D));
            getCaster().xRotO = getCaster().getXRot() - f;
            getCaster().yRotO += getCaster().getYRot() - f1;

            if (getCaster().getVehicle() != null) {
            	getCaster().getVehicle().onPassengerTurned(getCaster());
            }
			
			
			
		}
		
		if (this.tickCount > maxTicks) {
			this.remove(RemovalReason.KILLED);
		}
		
		if(tickCount > 1) {
			Color color = new Color(getColor());
			level.addParticle(new DustParticleOptions(new Vector3f(color.getRed()/255F, color.getGreen()/255F, color.getBlue()/255F), 1F), getX(), getY(), getZ(), 1,1,1);
		}
		
		if(tickCount % 10 == 0) {
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
		if (!level.isClientSide) {
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
					target.hurt(DamageSource.thrown(this, this.getOwner()), dmg);
					super.remove(RemovalReason.KILLED);
				}
			}
			remove(RemovalReason.KILLED);
		}
	}
	
}
