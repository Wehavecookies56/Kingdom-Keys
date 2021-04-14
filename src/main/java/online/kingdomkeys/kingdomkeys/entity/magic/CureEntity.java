package online.kingdomkeys.kingdomkeys.entity.magic;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ThrowableEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.FMLPlayMessages;
import net.minecraftforge.fml.network.NetworkHooks;
import online.kingdomkeys.kingdomkeys.entity.ModEntities;

public class CureEntity extends ThrowableEntity {

	int maxTicks = 100;

	public CureEntity(EntityType<? extends ThrowableEntity> type, World world) {
		super(type, world);
		this.preventEntitySpawning = true;
	}

	public CureEntity(FMLPlayMessages.SpawnEntity spawnEntity, World world) {
		super(ModEntities.TYPE_FIRE.get(), world);
	}

	public CureEntity(World world) {
		super(ModEntities.TYPE_FIRE.get(), world);
		this.preventEntitySpawning = true;
	}

	public CureEntity(World world, PlayerEntity player) {
		super(ModEntities.TYPE_FIRE.get(), player, world);
	}

	@Override
	public IPacket<?> createSpawnPacket() {
		return NetworkHooks.getEntitySpawningPacket(this);
	}

	@Override
	protected float getGravityVelocity() {
		return 0F;
	}

	@Override
	public void tick() {
		if (this.ticksExisted > maxTicks) {
			this.remove();
		}

		//world.addParticle(ParticleTypes.ENTITY_EFFECT, getPosX(), getPosY(), getPosZ(), 1, 1, 0);
		if(ticksExisted > 2)
			world.addParticle(ParticleTypes.FLAME, getPosX(), getPosY(), getPosZ(), 0, 0, 0);
		
		super.tick();
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

			if (ertResult != null && ertResult.getEntity() != null && ertResult.getEntity() instanceof LivingEntity) {

				LivingEntity target = (LivingEntity) ertResult.getEntity();
				if (target != getShooter()) {
					target.setFire(10);
					target.attackEntityFrom(DamageSource.causeThrownDamage(this, this.getShooter()), 10);
					remove();
				}
			} else { // Block (not ERTR)
				/*
				 * if (brtResult != null && rtRes.getType() == Type.BLOCK) {
				 * 
				 * } else { world.playSound(null, getPosition(), ModSounds.fistBounce,
				 * SoundCategory.MASTER, 1F, 1F);
				 * 
				 * bounces++; if (brtResult.getFace() == Direction.NORTH || brtResult.getFace()
				 * == Direction.SOUTH) { this.setMotion(getMotion().x, getMotion().y,
				 * -getMotion().z); } else if (brtResult.getFace() == Direction.EAST ||
				 * brtResult.getFace() == Direction.WEST) { this.setMotion(-getMotion().x,
				 * getMotion().y, getMotion().z); } else if (brtResult.getFace() == Direction.UP
				 * || brtResult.getFace() == Direction.DOWN) { this.setMotion(getMotion().x,
				 * -getMotion().y, getMotion().z); } } } else { remove(); }
				 */
				remove();
			}
		}
	}

	public int getMaxTicks() {
		return maxTicks;
	}

	public void setMaxTicks(int maxTicks) {
		this.maxTicks = maxTicks;
	}

	@Override
	public void writeAdditional(CompoundNBT compound) {
		// compound.putInt("lvl", this.getLvl());
	}

	@Override
	public void readAdditional(CompoundNBT compound) {
		// this.setLvl(compound.getInt("lvl"));
	}

	@Override
	protected void registerData() {
		// TODO Auto-generated method stub

	}
}
