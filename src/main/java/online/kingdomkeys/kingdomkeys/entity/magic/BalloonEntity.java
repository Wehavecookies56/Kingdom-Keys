package online.kingdomkeys.kingdomkeys.entity.magic;

import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ThrowableProjectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import online.kingdomkeys.kingdomkeys.client.sound.ModSounds;
import online.kingdomkeys.kingdomkeys.damagesource.KKDamageTypes;
import online.kingdomkeys.kingdomkeys.data.WorldData;
import online.kingdomkeys.kingdomkeys.entity.ModEntities;
import online.kingdomkeys.kingdomkeys.entity.ProjectileHits;
import online.kingdomkeys.kingdomkeys.lib.DamageCalculation;

import java.util.List;

public class BalloonEntity extends ThrowableProjectile {
	// Start
	int maxTicks = 100;
	float dmgMult = 1;

	public BalloonEntity(EntityType<? extends ThrowableProjectile> type, Level world) {
		super(type, world);
		this.blocksBuilding = true;
	}

	public BalloonEntity(Level world) {
		super(ModEntities.TYPE_BALLOON.get(), world);
		this.blocksBuilding = true;
	}

	public BalloonEntity(Level world, LivingEntity player, float dmgMult) {
		super(ModEntities.TYPE_BALLOON.get(), player, world);
		this.dmgMult = dmgMult;
	}

	@Override
	protected double getDefaultGravity() {
		return 0.125;
	}

	@Override
	public void tick() {
		if (this.tickCount > maxTicks) {
			this.remove(RemovalReason.KILLED);
		}

		if(tickCount > 2)
			level().addParticle(ParticleTypes.GLOW_SQUID_INK, getX(), getY(), getZ(), 0, 0, 0);

		super.tick();
	}

	@Override
	protected void onHit(HitResult hit) {
		if (level().isClientSide || getOwner() == null || ProjectileHits.hitOwner(hit, getOwner())) {
			return;
		}

		LivingEntity target = ProjectileHits.target(hit, getOwner());
		if (target != null) {
			float dmg = this.getOwner() instanceof LivingEntity magicCaster ? DamageCalculation.getMagicDamage(magicCaster) / 2F : 2;

			target.hurt(KKDamageTypes.getElementalDamage(KKDamageTypes.WATER,this, this.getOwner()), dmg * dmgMult);
			target.invulnerableTime = 0;
			playSound(ModSounds.balloonBounce.get(),1F,1F);
			this.remove(RemovalReason.KILLED);
		}

		if (hit instanceof BlockHitResult block) { //Bounce
			Vec3 mot = getDeltaMovement();

			double x = mot.x();
			double y = mot.y();
			double z = mot.z();

			LivingEntity nearby = this.tickCount > 30 ? getNearbyEntity(WorldData.get(level().getServer())) : null;
			if(block.getDirection() == Direction.UP || block.getDirection() == Direction.DOWN){
				if(nearby != null) {
					this.shoot(nearby.getX() - this.getX(), -y, nearby.getZ() - this.getZ(), 0.5f, 0);
				} else {
					this.setDeltaMovement(x,-y,z);
				}
				this.markHurt();
			} else if (block.getDirection() == Direction.EAST || block.getDirection() == Direction.WEST){
				this.setDeltaMovement(-x,y,z);
				this.markHurt();
			}else if (block.getDirection() == Direction.NORTH || block.getDirection() == Direction.SOUTH){
				this.setDeltaMovement(x,y,-z);
				this.markHurt();
			}
			playSound(ModSounds.balloonBounce.get(),1F,1F);

		}
	}

	private LivingEntity getNearbyEntity(WorldData worldData) {
		if(worldData == null)
			return null;
		List<LivingEntity> list = ProjectileHits.around(this, getOwner(), 3, null);
		return list.isEmpty() ? null : list.getFirst();
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