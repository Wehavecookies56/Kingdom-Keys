package online.kingdomkeys.kingdomkeys.entity.shotlock;

import java.awt.Color;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.ThrowableEntity;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.particles.RedstoneParticleData;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.FMLPlayMessages;
import online.kingdomkeys.kingdomkeys.entity.ModEntities;

public class RagnarokShotEntity extends BaseShotlockShotEntity {
	
	public RagnarokShotEntity(EntityType<? extends ThrowableEntity> type, World world) {
		super(type, world);
		this.preventEntitySpawning = true;
	}

	public RagnarokShotEntity(FMLPlayMessages.SpawnEntity spawnEntity, World world) {
		super(ModEntities.TYPE_RAGNAROK_SHOTLOCK_SHOT.get(), world);
	}

	public RagnarokShotEntity(World world) {
		super(ModEntities.TYPE_RAGNAROK_SHOTLOCK_SHOT.get(), world);
		this.preventEntitySpawning = true;
	}

	public RagnarokShotEntity(World world, LivingEntity player, Entity target, double dmg) {
		super(ModEntities.TYPE_RAGNAROK_SHOTLOCK_SHOT.get(), world, player, target, dmg);
	}

	@Override
	public void tick() {
		if (this.ticksExisted > maxTicks) {
			this.remove();
		}
		
		if(ticksExisted > 1) {
			Color color = new Color(getColor());
			world.addParticle(new RedstoneParticleData(color.getRed()/255F, color.getGreen()/255F, color.getBlue()/255F, 1F), getPosX(), getPosY(), getPosZ(), 1,1,1);
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
		if(ticksExisted % 10 == 0 && ticksExisted - 10 >= 6) {
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
	
	@Override
	public void remove() {
		if(ticksExisted > 40) {
			super.remove();
		}
	}

}