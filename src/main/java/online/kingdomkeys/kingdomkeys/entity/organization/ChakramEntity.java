package online.kingdomkeys.kingdomkeys.entity.organization;

import java.util.List;

import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ThrowableEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.FMLPlayMessages;
import net.minecraftforge.fml.network.NetworkHooks;
import online.kingdomkeys.kingdomkeys.entity.ModEntities;

public class ChakramEntity extends ThrowableEntity{

	int maxTicks = 120;
	boolean returning = false;
	String model;
	int rotationPoint; //0 = x, 1 = y, 2 = z
	float dmg;

	public ChakramEntity(EntityType<? extends ThrowableEntity> type, World world) {
		super(type, world);
		this.preventEntitySpawning = true;
	}

	public ChakramEntity(FMLPlayMessages.SpawnEntity spawnEntity, World world) {
		super(ModEntities.TYPE_CHAKRAM.get(), world);
	}

	public ChakramEntity(World world) {
		super(ModEntities.TYPE_CHAKRAM.get(), world);
		this.preventEntitySpawning = true;
	}

	public ChakramEntity(World world, PlayerEntity player, String model, float dmg) {
		super(ModEntities.TYPE_CHAKRAM.get(), player, world);
		setShooter(player);
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
		
		if(ticksExisted > maxTicks / 3) {
			setReturn();
		}

		if(Math.max(Math.abs(getMotion().x),Math.max(Math.abs(getMotion().y),Math.abs(getMotion().z))) < 0.1) {
			setReturn();
		}

		if (ticksExisted > 2)
			world.addParticle(ParticleTypes.FLAME, getPosX(), getPosY()+0.25, getPosZ(), 0, 0, 0);

		if (returning) {
			List entityTagetList = this.world.getEntitiesWithinAABB(Entity.class, this.getBoundingBox().grow(1.0D, 1.0D, 1.0D));
			for (int i = 0; i < entityTagetList.size(); i++) {
				Entity entityTarget = (Entity) entityTagetList.get(i);
				if (entityTarget != null && entityTarget instanceof PlayerEntity) {
					PlayerEntity owner = (PlayerEntity) entityTarget;
					if (owner == getShooter()) {
						this.remove();
					}
				}
			}
		}
		super.tick();
	}

	public void setReturn() {
		returning = true;
		if(getShooter() != null)
			shoot(this.getShooter().getPosX() - this.getPosX(), this.getShooter().getPosY() - this.getPosY() + 1.25, this.getShooter().getPosZ() - this.getPosZ(), 2f, 0);
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
					//target.setFire(5);
					target.attackEntityFrom(DamageSource.causeThrownDamage(this, this.getShooter()), dmg < 4 ? 4 : dmg);
					//setReturn();
					setMotion(getMotion().scale(0.5));
					dmg *= 0.9;
				}
			} else { // Block (not ERTR)
				if(brtResult != null) {
					
					if(world.getBlockState(brtResult.getPos()).getBlock() == Blocks.TALL_GRASS || world.getBlockState(brtResult.getPos()).getBlock() == Blocks.GRASS || world.getBlockState(brtResult.getPos()).getBlock() == Blocks.SUGAR_CANE || world.getBlockState(brtResult.getPos()).getBlock() == Blocks.VINE) {
					
					} else {
						setReturn();	
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
