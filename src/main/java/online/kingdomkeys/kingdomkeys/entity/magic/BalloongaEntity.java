package online.kingdomkeys.kingdomkeys.entity.magic;

import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ThrowableProjectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import online.kingdomkeys.kingdomkeys.client.sound.ModSounds;
import online.kingdomkeys.kingdomkeys.damagesource.KKDamageTypes;
import online.kingdomkeys.kingdomkeys.data.WorldData;
import online.kingdomkeys.kingdomkeys.entity.ModEntities;
import online.kingdomkeys.kingdomkeys.lib.DamageCalculation;
import online.kingdomkeys.kingdomkeys.util.Utils;

import java.util.List;

public class BalloongaEntity extends ThrowableProjectile {
	// Start
	int maxTicks = 100;
	float dmgMult = 1;

	public BalloongaEntity(EntityType<? extends ThrowableProjectile> type, Level world) {
		super(type, world);
		this.blocksBuilding = true;
	}

	public BalloongaEntity(Level world) {
		super(ModEntities.TYPE_BALLOONGA.get(), world);
		this.blocksBuilding = true;
	}

	public BalloongaEntity(Level world, LivingEntity player, float dmgMult) {
		super(ModEntities.TYPE_BALLOONGA.get(), player, world);
		this.dmgMult = dmgMult;
	}


	private void balloonBurst(){
		float explosionSize = 2.0F;
		this.level().explode(this, this.blockPosition().getX(), this.blockPosition().getY() + (double)(this.getBbHeight() / 1.0F), this.blockPosition().getZ(), explosionSize, false, Level.ExplosionInteraction.NONE);
	}

	@Override
	protected double getDefaultGravity() {
		return 0.125;
	}

	@Override
	public void tick() {
		if (this.tickCount > maxTicks) {
			if (!level().isClientSide && getOwner() != null) {
				explodeBalloonga();
			}
		}

		//world.addParticle(ParticleTypes.ENTITY_EFFECT, getPosX(), getPosY(), getPosZ(), 1, 1, 0);
		if(tickCount > 2)
			level().addParticle(ParticleTypes.GLOW_SQUID_INK, getX(), getY(), getZ(), 0, 0, 0);

		super.tick();
	}

	@Override
	protected void onHit(HitResult rtRes) {
		if (!level().isClientSide && getOwner() != null) {
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
					if (Utils.canHarm(getOwner(), target)) {
						float dmg = this.getOwner() instanceof Player ? DamageCalculation.getMagicDamage((Player) this.getOwner()) / 2.5F : 2;
						target.hurt(KKDamageTypes.getElementalDamage(KKDamageTypes.WATER,this, this.getOwner()), dmg * dmgMult);
						target.invulnerableTime = 0;
						explodeBalloonga();
					}
				}
			}

			if (brtResult != null && rtRes.getType() == HitResult.Type.BLOCK) {

				// Bounce

				Vec3 mot = getDeltaMovement();

				double x = mot.x();
				double y = mot.y();
				double z = mot.z();

				LivingEntity target = this.tickCount > 20 ? getNearbyEntity(WorldData.get(level().getServer())) : null;
				if(brtResult.getDirection() == Direction.UP || brtResult.getDirection() == Direction.DOWN){
					if(target != null) {
						//this.shoot(target.getX() - this.getX(), -y, target.getZ() - this.getZ(), 0.5f, 0);
						Vec3 vec3 = new Vec3(target.getX() - this.getX(), -y, target.getZ() - this.getZ()).normalize();
						this.setDeltaMovement(vec3);
						double d0 = vec3.horizontalDistance();
						this.setXRot((float)(Mth.atan2(vec3.y, d0) * (double)(180F / (float)Math.PI)));
						this.xRotO = this.getXRot();
					} else {
						this.setDeltaMovement(x,-y,z);
					}
					this.markHurt();

				} else if (brtResult.getDirection() == Direction.EAST || brtResult.getDirection() == Direction.WEST){
					this.setDeltaMovement(-x,y,z);
					this.markHurt();
				}else if (brtResult.getDirection() == Direction.NORTH || brtResult.getDirection() == Direction.SOUTH){
					this.setDeltaMovement(x,y,-z);
					this.markHurt();
				}
				playSound(ModSounds.balloonBounce.get(),1F,1F);

			}
		}

	}

	private void explodeBalloonga() {
		playSound(ModSounds.balloonBounce.get(),1F,1F);
		// The Dumb part
		for(int i = 0; i < 360; i+=45) {
			ThrowableProjectile balloon = new BalloonEntity(this.level(), (LivingEntity) getOwner(), dmgMult);
			balloon.setPos(new Vec3(this.getX(), this.getY(), this.getZ()));
			balloon.shootFromRotation(this, this.getXRot(), this.getYRot()+i, 0, 0.5F, 0);
			level().addFreshEntity(balloon);
			this.remove(RemovalReason.KILLED);
		}
	}

	private LivingEntity getNearbyEntity(WorldData worldData) {
		List<Entity> list = level().getEntities(getOwner(), getBoundingBox().inflate(3));
		if(worldData == null)
			return null;
		Utils.removeAllies(getOwner(), list);

		if (!list.isEmpty()) {
			for (Entity entity : list) {
				if(entity instanceof LivingEntity le) {
					return le;
				}
			}
		}
		return null;
	}
	public int getMaxTicks() {
		return maxTicks;
	}

	public void setMaxTicks(int maxTicks) {
		this.maxTicks = maxTicks;
	}



	@Override
	protected void defineSynchedData(SynchedEntityData.Builder builder) {

	}
}
