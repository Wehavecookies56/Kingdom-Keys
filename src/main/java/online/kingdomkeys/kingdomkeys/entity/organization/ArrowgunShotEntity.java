package online.kingdomkeys.kingdomkeys.entity.organization;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ThrowableProjectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraftforge.network.NetworkHooks;
import net.minecraftforge.network.PlayMessages;
import online.kingdomkeys.kingdomkeys.entity.ModEntities;

public class ArrowgunShotEntity extends ThrowableProjectile {

	int maxTicks = 120;
	float dmg;

	public ArrowgunShotEntity(EntityType<? extends ThrowableProjectile> type, Level world) {
		super(type, world);
		this.blocksBuilding = true;
	}

	public ArrowgunShotEntity(PlayMessages.SpawnEntity spawnEntity, Level world) {
		super(ModEntities.TYPE_ARROWGUN_SHOT.get(), world);
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
	public Packet<ClientGamePacketListener> getAddEntityPacket() {
		return NetworkHooks.getEntitySpawningPacket(this);
	}

	@Override
	protected float getGravity() {
		return 0F;
	}

	@Override
	public void tick() {
		if (this.tickCount > maxTicks) {
			this.remove(RemovalReason.KILLED);
		}

		if(tickCount > 1 && getShotType() == 1)
			level().addParticle(ParticleTypes.ENCHANTED_HIT, getX(), getY(), getZ(), 0, 0, 0);

		super.tick();
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

			if (ertResult != null && ertResult.getEntity() instanceof LivingEntity) {

				LivingEntity target = (LivingEntity) ertResult.getEntity();

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

	private static final EntityDataAccessor<Integer> TYPE = SynchedEntityData.defineId(LaserDomeShotEntity.class, EntityDataSerializers.INT);
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
	protected void defineSynchedData() {
		entityData.define(TYPE, 0);
	}
}
