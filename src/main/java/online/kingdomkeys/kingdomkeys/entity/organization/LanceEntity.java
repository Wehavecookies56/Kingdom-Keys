package online.kingdomkeys.kingdomkeys.entity.organization;

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
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceContext;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.FMLPlayMessages;
import net.minecraftforge.fml.network.NetworkHooks;
import online.kingdomkeys.kingdomkeys.entity.ModEntities;

public class LanceEntity extends ThrowableEntity{

	int maxTicks = 100;
	String model;
	boolean stopped = false;
	int rotationPoint; //0 = x, 1 = y, 2 = z
	float dmg;
	
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

	public LanceEntity(World world, PlayerEntity player, String model, float dmg) {
		super(ModEntities.TYPE_LANCE.get(), player, world);
		owner = player;
		setModel(model);
		this.dmg = dmg;
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

		if (ticksExisted > 2)
			world.addParticle(ParticleTypes.CRIT, getPosX(), getPosY(), getPosZ(), 0, 0, 0);

		if(!stopped) {
			//super.tick();
			if (this.throwableShake > 0) {
		         --this.throwableShake;
		      }

		      if (this.inGround) {
		        // this.inGround = false;
		         this.setMotion(this.getMotion().mul((double)(this.rand.nextFloat() * 0.2F), (double)(this.rand.nextFloat() * 0.2F), (double)(this.rand.nextFloat() * 0.2F)));
		      }

		      AxisAlignedBB axisalignedbb = this.getBoundingBox().expand(this.getMotion()).grow(1.0D);

		      for(Entity entity : this.world.getEntitiesInAABBexcluding(this, axisalignedbb, (p_213881_0_) -> {
		         return !p_213881_0_.isSpectator() && p_213881_0_.canBeCollidedWith();
		      })) {
		        
		      }

		      RayTraceResult raytraceresult = ProjectileHelper.rayTrace(this, axisalignedbb, (p_213880_1_) -> {
		         return !p_213880_1_.isSpectator() && p_213880_1_.canBeCollidedWith();
		      }, RayTraceContext.BlockMode.OUTLINE, true);
		      

		      if (raytraceresult.getType() != RayTraceResult.Type.MISS) {
		         if (raytraceresult.getType() == RayTraceResult.Type.BLOCK) {
		        	if(this.world.getBlockState(((BlockRayTraceResult)raytraceresult).getPos()).getBlock() == Blocks.NETHER_PORTAL) {
		        		this.setPortal(((BlockRayTraceResult)raytraceresult).getPos());
		        	} else {
		        		//System.out.println(this.world.getBlockState(((BlockRayTraceResult)raytraceresult).getPos()).getBlock());
		        		stopped = true;
		        	}
		         } else if (!net.minecraftforge.event.ForgeEventFactory.onProjectileImpact(this, raytraceresult)){
		            this.onImpact(raytraceresult);
		         }
		      }

		      if(!stopped) {
			      Vec3d vec3d = this.getMotion();
			      double d0 = this.getPosX() + vec3d.x;
			      double d1 = this.getPosY() + vec3d.y;
			      double d2 = this.getPosZ() + vec3d.z;
			      float f1;
			      if (this.isInWater()) {
			         for(int i = 0; i < 4; ++i) {
			            this.world.addParticle(ParticleTypes.BUBBLE, d0 - vec3d.x * 0.25D, d1 - vec3d.y * 0.25D, d2 - vec3d.z * 0.25D, vec3d.x, vec3d.y, vec3d.z);
			         }
	
			         f1 = 0.8F;
			      } else {
			         f1 = 0.99F;
			      }
	
	
			      this.setMotion(vec3d.scale((double)f1));
			      if (!this.hasNoGravity()) {
			         Vec3d vec3d1 = this.getMotion();
			         this.setMotion(vec3d1.x, vec3d1.y - (double)this.getGravityVelocity(), vec3d1.z);
			      }
	
			      this.setPosition(d0, d1, d2);
		      }
		}
	}
	
	public void stopLance(){
		stopped = true;
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
				if (target != getThrower()) {
					target.attackEntityFrom(DamageSource.causeThrownDamage(this, this.getThrower()), dmg < 4 ? 4 : dmg);
					dmg *= 0.8F;
					//stopLance();
				}
			} else { // Block (not ERTR)				
				if(brtResult != null) {
					//System.out.println(world.getBlockState(brtResult.getPos()).getBlockState());
					if(world.getBlockState(brtResult.getPos()).getBlock() == Blocks.TALL_GRASS || world.getBlockState(brtResult.getPos()).getBlock() == Blocks.GRASS || world.getBlockState(brtResult.getPos()).getBlock() == Blocks.SUGAR_CANE) {
					//	System.out.println("goin through");	
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
	
	private static final DataParameter<String> MODEL = EntityDataManager.createKey(ChakramEntity.class, DataSerializers.STRING);
	private static final DataParameter<Integer> ROTATION_POINT = EntityDataManager.createKey(ChakramEntity.class, DataSerializers.VARINT);
	
	public String getModel() {
		return model;
	}

	public void setModel(String name) {
		this.dataManager.set(MODEL, name);
		this.model = name;
	}
	
	public int getRotationPoint() {
		return rotationPoint;
	}
	
	public void setRotationPoint(int rotations) {
		this.dataManager.set(ROTATION_POINT, rotations);
		this.rotationPoint = rotations;
	}

	@Override
	public void notifyDataManagerChange(DataParameter<?> key) {
		if (key.equals(MODEL)) {
			this.model = this.getModelDataManager();
		}
		if (key.equals(ROTATION_POINT)) {
			this.rotationPoint = this.getRotationPointDataManager();
		}
		
	}
	
	@Override
	public void writeAdditional(CompoundNBT compound) {
		compound.putString("Model", this.getModel());
		compound.putInt("Rotation", this.getRotationPoint());
	}

	@Override
	public void readAdditional(CompoundNBT compound) {
		this.setModel(compound.getString("Model"));
		this.setRotationPoint(compound.getInt("Rotation"));
	}
	

	@Override
	protected void registerData() {
		this.dataManager.register(MODEL, "");
		this.dataManager.register(ROTATION_POINT, 0);
	}

	public String getModelDataManager() {
		return this.dataManager.get(MODEL);
	}
	
	public int getRotationPointDataManager() {
		return this.dataManager.get(ROTATION_POINT);
	}
}
