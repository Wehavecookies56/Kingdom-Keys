package online.kingdomkeys.kingdomkeys.entity.organization;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.ProjectileHelper;
import net.minecraft.entity.projectile.ThrowableEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.tileentity.EndGatewayTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.FMLPlayMessages;
import net.minecraftforge.fml.network.NetworkHooks;
import online.kingdomkeys.kingdomkeys.entity.ModEntities;

public class LaserDomeShotEntity extends ThrowableEntity {

	int maxTicks = 220;
	float dmg;
	
	public LaserDomeShotEntity(EntityType<? extends ThrowableEntity> type, World world) {
		super(type, world);
		this.preventEntitySpawning = true;
	}

	public LaserDomeShotEntity(FMLPlayMessages.SpawnEntity spawnEntity, World world) {
		super(ModEntities.TYPE_LASER_SHOT.get(), world);
	}

	public LaserDomeShotEntity(World world) {
		super(ModEntities.TYPE_LASER_SHOT.get(), world);
		this.preventEntitySpawning = true;
	}

	public LaserDomeShotEntity(World world, LivingEntity player, double dmg) {
		super(ModEntities.TYPE_LASER_SHOT.get(), player, world);
		this.dmg = (float)dmg;
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

		if (!this.world.isRemote) {
			this.setFlag(6, this.isGlowing());
		}

		this.baseTick();

		RayTraceResult raytraceresult = ProjectileHelper.func_234618_a_(this, this::func_230298_a_);
		boolean flag = false;
		if (raytraceresult.getType() == RayTraceResult.Type.BLOCK) {
			BlockPos blockpos = ((BlockRayTraceResult) raytraceresult).getPos();
			BlockState blockstate = this.world.getBlockState(blockpos);
			if (blockstate.matchesBlock(Blocks.NETHER_PORTAL)) {
				this.setPortal(blockpos);
				flag = true;
			} else if (blockstate.matchesBlock(Blocks.END_GATEWAY)) {
				TileEntity tileentity = this.world.getTileEntity(blockpos);
				if (tileentity instanceof EndGatewayTileEntity && EndGatewayTileEntity.func_242690_a(this)) {
					((EndGatewayTileEntity) tileentity).teleportEntity(this);
				}

				flag = true;
			}
		}

		if (raytraceresult.getType() != RayTraceResult.Type.MISS && !flag && !net.minecraftforge.event.ForgeEventFactory.onProjectileImpact(this, raytraceresult)) {
			this.onImpact(raytraceresult);
		}

		this.doBlockCollisions();
		Vector3d vector3d = this.getMotion();
		double d2 = this.getPosX() + vector3d.x;
		double d0 = this.getPosY() + vector3d.y;
		double d1 = this.getPosZ() + vector3d.z;
		this.updatePitchAndYaw();
		if (!this.hasNoGravity()) {
			Vector3d vector3d1 = this.getMotion();
			this.setMotion(vector3d1.x, vector3d1.y - (double) this.getGravityVelocity(), vector3d1.z);
		}

		this.setPosition(d2, d0, d1);
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
					remove();
				}
			}
			remove();
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