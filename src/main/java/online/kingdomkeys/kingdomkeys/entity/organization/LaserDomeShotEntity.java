package online.kingdomkeys.kingdomkeys.entity.organization;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.entity.projectile.ThrowableProjectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.TheEndGatewayBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.NetworkHooks;
import net.minecraftforge.network.PlayMessages;
import online.kingdomkeys.kingdomkeys.entity.ModEntities;

public class LaserDomeShotEntity extends ThrowableProjectile {

	int maxTicks = 220;
	float dmg;
	
	public LaserDomeShotEntity(EntityType<? extends ThrowableProjectile> type, Level world) {
		super(type, world);
		this.blocksBuilding = true;
	}

	public LaserDomeShotEntity(PlayMessages.SpawnEntity spawnEntity, Level world) {
		super(ModEntities.TYPE_LASER_SHOT.get(), world);
	}

	public LaserDomeShotEntity(Level world) {
		super(ModEntities.TYPE_LASER_SHOT.get(), world);
		this.blocksBuilding = true;
	}

	public LaserDomeShotEntity(Level world, LivingEntity player, double dmg) {
		super(ModEntities.TYPE_LASER_SHOT.get(), player, world);
		this.dmg = (float)dmg;
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

		if (!this.level.isClientSide) {
			this.setSharedFlag(6, this.isCurrentlyGlowing());
		}

		this.baseTick();

		HitResult raytraceresult = ProjectileUtil.getHitResult(this, this::canHitEntity);
		boolean flag = false;
		if (raytraceresult.getType() == HitResult.Type.BLOCK) {
			BlockPos blockpos = ((BlockHitResult) raytraceresult).getBlockPos();
			BlockState blockstate = this.level.getBlockState(blockpos);
			if (blockstate.is(Blocks.NETHER_PORTAL)) {
				this.handleInsidePortal(blockpos);
				flag = true;
			} else if (blockstate.is(Blocks.END_GATEWAY)) {
				BlockEntity tileentity = this.level.getBlockEntity(blockpos);
				if (tileentity instanceof TheEndGatewayBlockEntity && TheEndGatewayBlockEntity.canEntityTeleport(this)) {
					((TheEndGatewayBlockEntity) tileentity).teleportEntity(level, blockpos, blockstate, this, (TheEndGatewayBlockEntity) tileentity);
				}

				flag = true;
			}
		}

		if (raytraceresult.getType() != HitResult.Type.MISS && !flag && !net.minecraftforge.event.ForgeEventFactory.onProjectileImpact(this, raytraceresult)) {
			this.onHit(raytraceresult);
		}

		this.checkInsideBlocks();
		Vec3 vector3d = this.getDeltaMovement();
		double d2 = this.getX() + vector3d.x;
		double d0 = this.getY() + vector3d.y;
		double d1 = this.getZ() + vector3d.z;
		this.updateRotation();
		if (!this.isNoGravity()) {
			Vec3 vector3d1 = this.getDeltaMovement();
			this.setDeltaMovement(vector3d1.x, vector3d1.y - (double) this.getGravity(), vector3d1.z);
		}

		this.setPos(d2, d0, d1);
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
					remove(RemovalReason.KILLED);
				}
			}
			remove(RemovalReason.KILLED);
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