package online.kingdomkeys.kingdomkeys.entity.magic;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ThrowableProjectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraftforge.fmllegacy.network.FMLPlayMessages;
import net.minecraftforge.fmllegacy.network.NetworkHooks;
import online.kingdomkeys.kingdomkeys.entity.ModEntities;

public class CureEntity extends ThrowableProjectile {

	int maxTicks = 100;

	public CureEntity(EntityType<? extends ThrowableProjectile> type, Level world) {
		super(type, world);
		this.blocksBuilding = true;
	}

	public CureEntity(FMLPlayMessages.SpawnEntity spawnEntity, Level world) {
		super(ModEntities.TYPE_FIRE.get(), world);
	}

	public CureEntity(Level world) {
		super(ModEntities.TYPE_FIRE.get(), world);
		this.blocksBuilding = true;
	}

	public CureEntity(Level world, Player player) {
		super(ModEntities.TYPE_FIRE.get(), player, world);
	}

	@Override
	public Packet<?> getAddEntityPacket() {
		return NetworkHooks.getEntitySpawningPacket(this);
	}

	@Override
	protected float getGravity() {
		return 0F;
	}

	@Override
	public void tick() {
		if (this.tickCount > maxTicks) {
			this.remove(false);
		}

		//world.addParticle(ParticleTypes.ENTITY_EFFECT, getPosX(), getPosY(), getPosZ(), 1, 1, 0);
		if(tickCount > 2)
			level.addParticle(ParticleTypes.FLAME, getX(), getY(), getZ(), 0, 0, 0);
		
		super.tick();
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

			if (ertResult != null && ertResult.getEntity() != null && ertResult.getEntity() instanceof LivingEntity) {

				LivingEntity target = (LivingEntity) ertResult.getEntity();
				if (target != getOwner()) {
					target.setSecondsOnFire(10);
					target.hurt(DamageSource.thrown(this, this.getOwner()), 10);
					this.remove(false);
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
				 * -getMotion().y, getMotion().z); } } } else { this.remove(false); }
				 */
				this.remove(false);
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
	public void addAdditionalSaveData(CompoundTag compound) {
		// compound.putInt("lvl", this.getLvl());
	}

	@Override
	public void readAdditionalSaveData(CompoundTag compound) {
		// this.setLvl(compound.getInt("lvl"));
	}

	@Override
	protected void defineSynchedData() {
		// TODO Auto-generated method stub

	}
}
