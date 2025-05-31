package online.kingdomkeys.kingdomkeys.entity.organization;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.entity.projectile.ThrowableProjectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.event.EventHooks;
import online.kingdomkeys.kingdomkeys.entity.ModEntities;

public class ArrowgunShotEntity extends ThrowableProjectile {

	int maxTicks = 120;
	float dmg;

	public ArrowgunShotEntity(EntityType<? extends ThrowableProjectile> type, Level world) {
		super(type, world);
		this.blocksBuilding = true;
	}

	public ArrowgunShotEntity(Level world, LivingEntity player,float damage, double x, double y, double z) {
		this(world, player,  damage);
		this.setPos(x,y,z);
	}

	public ArrowgunShotEntity(Level world, LivingEntity player, float damage) {
		super(ModEntities.TYPE_ARROWGUN_SHOT.get(), player, world);
		this.dmg = damage;
	}

	@Override
	protected double getDefaultGravity() {
		return 0D;
	}

	@Override
	public void tick() {
		if (this.tickCount > maxTicks) {
			this.remove(RemovalReason.KILLED);
		}

		if(tickCount > 1 && getShotType() == 1)
			level().addParticle(ParticleTypes.ENCHANTED_HIT, getX(), getY(), getZ(), 0, 0, 0);


		HitResult hitresult = ProjectileUtil.getHitResultOnMoveVector(this, this::canHitEntity);
		if (hitresult.getType() != HitResult.Type.MISS && !EventHooks.onProjectileImpact(this, hitresult)) {
			this.hitTargetOrDeflectSelf(hitresult);
		}

		this.checkInsideBlocks();
		Vec3 vec3 = this.getDeltaMovement();
		double d0 = this.getX() + vec3.x;
		double d1 = this.getY() + vec3.y;
		double d2 = this.getZ() + vec3.z;
		//this.updateRotation();
		float f;
		if (this.isInWater()) {
			for(int i = 0; i < 4; ++i) {
				this.level().addParticle(ParticleTypes.BUBBLE, d0 - vec3.x * (double)0.25F, d1 - vec3.y * (double)0.25F, d2 - vec3.z * (double)0.25F, vec3.x, vec3.y, vec3.z);
			}

			f = 0.8F;
		} else {
			f = 0.99F;
		}

		this.setDeltaMovement(vec3.scale(f));
		this.applyGravity();
		this.setPos(d0, d1, d2);



	}

	@Override
	protected void onHit(HitResult rtRes) {
		if (!level().isClientSide) {

			EntityHitResult ertResult = null;
			BlockHitResult brtResult = null;

			if (rtRes instanceof EntityHitResult) {
				ertResult = (EntityHitResult) rtRes;
			}

			if (rtRes instanceof BlockHitResult) {
				brtResult = (BlockHitResult) rtRes;
			}

			if (ertResult != null && ertResult.getEntity() instanceof LivingEntity target) {

                if (target != getOwner()) {
					target.invulnerableTime = 0;
					target.hurt(target.damageSources().thrown(this, this.getOwner()), dmg);
					remove(RemovalReason.KILLED);
				}
			} else { // Block (not ERTR)
				remove(RemovalReason.KILLED);
			}
		}
	}

	public int getMaxTicks() {
		return maxTicks;
	}

	public void setMaxTicks(int maxTicks) {
		this.maxTicks = maxTicks;
	}

	private static final EntityDataAccessor<Integer> TYPE = SynchedEntityData.defineId(ArrowgunShotEntity.class, EntityDataSerializers.INT);
	int type = 0;
	
	@Override
	public void onSyncedDataUpdated(EntityDataAccessor<?> key) {
		if (key.equals(TYPE)) {
			this.type = this.entityData.get(TYPE);
		}
	}

	@Override
	public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
		compound.putInt("Type", this.getShotType());
	}

	@Override
	public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
		this.setShotType(compound.getInt("Type"));
	}
	
	public int getShotType() {
		return type;
	}
	
	public void setShotType(int type) {
		this.entityData.set(TYPE, type);
		this.type = type;
	}

	@Override
	protected void defineSynchedData(SynchedEntityData.Builder pBuilder) {
		pBuilder.define(TYPE, 0);
	}
}
