package online.kingdomkeys.kingdomkeys.entity.shotlock;

import com.mojang.math.Vector3f;

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
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.fmllegacy.network.FMLPlayMessages;
import online.kingdomkeys.kingdomkeys.entity.ModEntities;

public class VolleyShotEntity extends BaseShotlockShotEntity {
	
	public VolleyShotEntity(EntityType<? extends ThrowableProjectile> type, Level world) {
		super(type, world);
		this.blocksBuilding = true;
	}

	public VolleyShotEntity(FMLPlayMessages.SpawnEntity spawnEntity, Level world) {
		super(ModEntities.TYPE_VOLLEY_SHOTLOCK_SHOT.get(), world);
	}

	public VolleyShotEntity(Level world) {
		super(ModEntities.TYPE_VOLLEY_SHOTLOCK_SHOT.get(), world);
		this.blocksBuilding = true;
	}

	public VolleyShotEntity(Level world, LivingEntity player, Entity target, double dmg) {
		super(ModEntities.TYPE_VOLLEY_SHOTLOCK_SHOT.get(), world, player, target, dmg);
	}

	@Override
	public void tick() {
		if (this.tickCount > maxTicks) {
			this.remove(false);
		}
		
		if(tickCount > 1) {
			Vector3f col = new Vector3f(Vec3.fromRGB24(getColor()));
			level.addParticle(new DustParticleOptions(col, 1F), getX(), getY(), getZ(), 1,1,1);
			//world.addParticle(ParticleTypes.DRAGON_BREATH, getPosX(), getPosY(), getPosZ(), 0, 0, 0);
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
					super.remove(false);
				}
			}
			this.remove(false);
		}
	}
	
	
}