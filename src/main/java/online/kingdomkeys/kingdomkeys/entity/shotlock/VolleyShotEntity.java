package online.kingdomkeys.kingdomkeys.entity.shotlock;

import java.awt.Color;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.ThrowableEntity;
import net.minecraft.particles.RedstoneParticleData;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.FMLPlayMessages;
import online.kingdomkeys.kingdomkeys.entity.ModEntities;

public class VolleyShotEntity extends BaseShotlockShotEntity {
	
	public VolleyShotEntity(EntityType<? extends ThrowableEntity> type, World world) {
		super(type, world);
		this.preventEntitySpawning = true;
	}

	public VolleyShotEntity(FMLPlayMessages.SpawnEntity spawnEntity, World world) {
		super(ModEntities.TYPE_VOLLEY_SHOTLOCK_SHOT.get(), world);
	}

	public VolleyShotEntity(World world) {
		super(ModEntities.TYPE_VOLLEY_SHOTLOCK_SHOT.get(), world);
		this.preventEntitySpawning = true;
	}

	public VolleyShotEntity(World world, LivingEntity player, Entity target, double dmg) {
		super(ModEntities.TYPE_VOLLEY_SHOTLOCK_SHOT.get(), world, player, target, dmg);
	}

	@Override
	public void tick() {
		if (this.ticksExisted > maxTicks) {
			this.remove();
		}
		
		if(ticksExisted > 1) {
			Color color = new Color(getColor());
			world.addParticle(new RedstoneParticleData(color.getRed()/255F, color.getGreen()/255F, color.getBlue()/255F, 1F), getPosX(), getPosY(), getPosZ(), 1,1,1);
			//world.addParticle(ParticleTypes.DRAGON_BREATH, getPosX(), getPosY(), getPosZ(), 0, 0, 0);
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