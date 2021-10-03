package online.kingdomkeys.kingdomkeys.entity.shotlock;

import java.awt.Color;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.ThrowableEntity;
import net.minecraft.particles.RedstoneParticleData;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.FMLPlayMessages;
import online.kingdomkeys.kingdomkeys.entity.ModEntities;

public class SonicBladeEntity extends BaseShotlockShotEntity{
	public SonicBladeEntity(EntityType<? extends ThrowableEntity> type, World world) {
		super(type, world);
		this.preventEntitySpawning = true;
	}

	public SonicBladeEntity(FMLPlayMessages.SpawnEntity spawnEntity, World world) {
		super(ModEntities.TYPE_VOLLEY_SHOTLOCK_SHOT.get(), world);
	}

	public SonicBladeEntity(World world) {
		super(ModEntities.TYPE_VOLLEY_SHOTLOCK_SHOT.get(), world);
		this.preventEntitySpawning = true;
	}

	public SonicBladeEntity(World world, LivingEntity player, Entity target, double dmg) {
		super(ModEntities.TYPE_VOLLEY_SHOTLOCK_SHOT.get(), world, player, target, dmg);
		setCaster(player.getUniqueID());
	}

	@Override
	public void tick() {
		if (getCaster() != null && getTarget() != null) {
			BlockPos pos = getTarget().getPosition();
			float speedFactor = 0.5F;
			getCaster().setMotion((pos.getX() - getCaster().getPosX()) * speedFactor, (pos.getY() - getCaster().getPosY())  * speedFactor, (pos.getZ() - getCaster().getPosZ()) * speedFactor);

			if (world.isRemote) {
				getCaster().velocityChanged = true;
			}
				
			double dx = getCaster().getPosX() - getTarget().getPosX();
            double dz = getCaster().getPosZ() - getTarget().getPosZ();
            double dy = getCaster().getPosY() - (getTarget().getPosY() + (getTarget().getHeight() / 2.0F)-getCaster().getHeight());
            double angle = Math.atan2(dz, dx) * 180 / Math.PI;
            double pitch = Math.atan2(dy, Math.sqrt(dx * dx + dz * dz)) * 180 / Math.PI;
            double distance = getCaster().getDistance(getTarget());
            
            float rYaw = (float) MathHelper.wrapDegrees(angle - getCaster().rotationYaw) + 90;
            float rPitch = (float) pitch - (float) (10.0F / Math.sqrt(distance)) + (float) (distance * Math.PI / 90);
            
            float f = getCaster().rotationPitch;
            float f1 = getCaster().rotationYaw;
            
            getCaster().rotationYaw = (float)((double)getCaster().rotationYaw + (double)rYaw * 0.15D);
            getCaster().rotationPitch = (float)((double)getCaster().rotationPitch - (double)-(rPitch - getCaster().rotationPitch) * 0.15D);
            getCaster().prevRotationPitch = getCaster().rotationPitch - f;
            getCaster().prevRotationYaw += getCaster().rotationYaw - f1;

            if (getCaster().getRidingEntity() != null) {
            	getCaster().getRidingEntity().applyOrientationToEntity(getCaster());
            }
			
			
			
		}
		
		if (this.ticksExisted > maxTicks) {
			this.remove();
		}
		
		if(ticksExisted > 1) {
			Color color = new Color(getColor());
			world.addParticle(new RedstoneParticleData(color.getRed()/255F, color.getGreen()/255F, color.getBlue()/255F, 1F), getPosX(), getPosY(), getPosZ(), 1,1,1);
		}
		
		if(ticksExisted % 10 == 0) {
			updateMovement();
		}
		
		super.tick();
	}

	private void updateMovement() {
		if(getTarget() != null) {
			if(getTarget().isAlive()) {
				this.shoot(getTarget().getPosX() - this.getPosX(), (getTarget().getPosY() + (getTarget().getHeight() / 2.0F) - this.getHeight()) - getPosY() + 0.5, getTarget().getPosZ() - this.getPosZ(), 1, 0);
			} else {
				if(getShooter() != null)
					this.setDirectionAndMovement(this, getShooter().rotationPitch, getShooter().rotationYaw, 0, 1, 0); // Work in progress
			}
		}
	}

	@Override
	protected void onImpact(RayTraceResult rtRes) {
		if (!world.isRemote) {
			EntityRayTraceResult ertResult = null;
			BlockRayTraceResult brtResult = null;

			if (rtRes instanceof EntityRayTraceResult) {
				ertResult = (EntityRayTraceResult) rtRes;
			}

			if (rtRes instanceof BlockRayTraceResult) {
				brtResult = (BlockRayTraceResult) rtRes;
			}

			if (ertResult != null && ertResult.getEntity() instanceof LivingEntity) {
				LivingEntity target = (LivingEntity) ertResult.getEntity();
				if (target != getShooter()) {
					target.attackEntityFrom(DamageSource.causeThrownDamage(this, this.getShooter()), dmg);
					super.remove();
				}
			}
			remove();
		}
	}
	
}
