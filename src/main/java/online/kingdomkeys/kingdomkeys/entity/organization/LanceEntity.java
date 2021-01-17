package online.kingdomkeys.kingdomkeys.entity.organization;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileHelper;
import net.minecraft.entity.projectile.ThrowableEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.tileentity.EndGatewayTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.RayTraceContext;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.FMLPlayMessages;
import net.minecraftforge.fml.network.NetworkHooks;
import online.kingdomkeys.kingdomkeys.entity.ModEntities;

public class LanceEntity extends ThrowableEntity{

	int maxTicks = 100;
	String model;
	boolean stopped = false;
	
	public LanceEntity(EntityType<? extends ThrowableEntity> type, World world) {
		super(type, world);
		this.preventEntitySpawning = true;
	}

	public LanceEntity(FMLPlayMessages.SpawnEntity spawnEntity, World world) {
		super(ModEntities.TYPE_LANCE.get(), world);
	}

	public LanceEntity(World world) {
		super(ModEntities.TYPE_LANCE.get(), world);
		this.preventEntitySpawning = true;
	}

	public LanceEntity(World world, PlayerEntity player, String model) {
		super(ModEntities.TYPE_LANCE.get(), player, world);
		setShooter(player);
		setModel(model);
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


		if (!isStopped()) {
			if (ticksExisted > 2)
				world.addParticle(ParticleTypes.CRIT, getPosX(), getPosY(), getPosZ(), 0, 0, 0);

			if (this.isOnGround()) {
				this.setOnGround(false);
				this.setMotion(this.getMotion().mul((double) (this.rand.nextFloat() * 0.2F), (double) (this.rand.nextFloat() * 0.2F), (double) (this.rand.nextFloat() * 0.2F)));
			}

			RayTraceResult raytraceresult = ProjectileHelper.func_234618_a_(this, this::func_230298_a_);
		      boolean flag = false;
		      if (raytraceresult.getType() == RayTraceResult.Type.BLOCK) {
		         BlockPos blockpos = ((BlockRayTraceResult)raytraceresult).getPos();
		         BlockState blockstate = this.world.getBlockState(blockpos);
		         if (blockstate.isIn(Blocks.NETHER_PORTAL)) {
		            this.setPortal(blockpos);
		            flag = true;
		         } else if (blockstate.isIn(Blocks.END_GATEWAY)) {
		            TileEntity tileentity = this.world.getTileEntity(blockpos);
		            if (tileentity instanceof EndGatewayTileEntity && EndGatewayTileEntity.func_242690_a(this)) {
		               ((EndGatewayTileEntity)tileentity).teleportEntity(this);
		            }

		            flag = true;
		         }
		      }

		      if (raytraceresult.getType() != RayTraceResult.Type.MISS && !flag && !net.minecraftforge.event.ForgeEventFactory.onProjectileImpact(this, raytraceresult)) {
		         this.onImpact(raytraceresult);
		      }

			Vector3d vec3d = this.getMotion();
			double d0 = this.getPosX() + vec3d.x;
			double d1 = this.getPosY() + vec3d.y;
			double d2 = this.getPosZ() + vec3d.z;
			float f1;
			if (this.isInWater()) {
				for (int i = 0; i < 4; ++i) {
					this.world.addParticle(ParticleTypes.BUBBLE, d0 - vec3d.x * 0.25D, d1 - vec3d.y * 0.25D, d2 - vec3d.z * 0.25D, vec3d.x, vec3d.y, vec3d.z);
				}

				f1 = 0.8F;
			} else {
				f1 = 0.99F;
			}

			this.setMotion(vec3d.scale((double) f1));
			if (!this.hasNoGravity()) {
				Vector3d vec3d1 = this.getMotion();
				this.setMotion(vec3d1.x, vec3d1.y - (double) this.getGravityVelocity(), vec3d1.z);
			}

			this.setPosition(d0, d1, d2);
		}
		//super.tick();


	}
	
	public void stopLance(){
		setStopped(true);
		this.setMotion(0, 0, 0);
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
				if (target != func_234616_v_()) {
					//target.setFire(5);
					target.attackEntityFrom(DamageSource.causeThrownDamage(this, this.func_234616_v_()), 10);
					stopLance();
				}
			} else { // Block (not ERTR)
				if(brtResult != null) {
					//System.out.println(world.getBlockState(brtResult.getPos()).getBlockState());
					if(world.getBlockState(brtResult.getPos()).getBlock() == Blocks.TALL_GRASS || world.getBlockState(brtResult.getPos()).getBlock() == Blocks.SUGAR_CANE) {
					//System.out.println("goin through");	
					} else {
						stopLance();	
					}
				}
			}
		}


	}

	public int getMaxTicks() {
		return maxTicks;
	}

	public void setMaxTicks(int maxTicks) {
		this.maxTicks = maxTicks;
	}
	
	private static final DataParameter<String> MODEL = EntityDataManager.createKey(LanceEntity.class, DataSerializers.STRING);
	private static final DataParameter<Boolean> STOPPED = EntityDataManager.createKey(LanceEntity.class, DataSerializers.BOOLEAN);
	
	public String getModel() {
		return model;
	}

	public void setModel(String name) {
		this.dataManager.set(MODEL, name);
		this.model = name;
	}

	public boolean isStopped() {
		return stopped;
	}

	public void setStopped(boolean stopped) {
		this.dataManager.set(STOPPED, stopped);
		this.stopped = stopped;
	}

	
	@Override
	public void notifyDataManagerChange(DataParameter<?> key) {
		if (key.equals(MODEL)) {
			this.model = this.getModelDataManager();
		}
		if (key.equals(STOPPED)) {
			this.stopped = this.getStoppedDataManager();
		}
	}
	
	@Override
	public void writeAdditional(CompoundNBT compound) {
		compound.putString("Model", this.getModel());
		compound.putBoolean("Stopped", this.isStopped());
	}

	@Override
	public void readAdditional(CompoundNBT compound) {
		this.setModel(compound.getString("Model"));
		this.setStopped(compound.getBoolean("Stopped"));
	}
	

	@Override
	protected void registerData() {
		this.dataManager.register(MODEL, "");
		this.dataManager.register(STOPPED, false);
	}

	public String getModelDataManager() {
		return this.dataManager.get(MODEL);
	}
	public boolean getStoppedDataManager() {
		return this.dataManager.get(STOPPED);
	}
	
}
