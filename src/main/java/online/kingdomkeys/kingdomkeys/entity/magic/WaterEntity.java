package online.kingdomkeys.kingdomkeys.entity.magic;

import net.minecraft.block.Blocks;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ThrowableEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.*;
import net.minecraft.util.math.RayTraceResult.Type;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.FMLPlayMessages;
import net.minecraftforge.fml.network.NetworkHooks;
import online.kingdomkeys.kingdomkeys.entity.ModEntities;

public class WaterEntity extends ThrowableEntity {

	int maxTicks = 100;
	Vec3d motion;
	PlayerEntity player;

	public WaterEntity(EntityType<? extends ThrowableEntity> type, World world) {
		super(type, world);
		this.preventEntitySpawning = true;
	}

	public WaterEntity(FMLPlayMessages.SpawnEntity spawnEntity, World world) {
		super(ModEntities.TYPE_WATER.get(), world);
	}

	public WaterEntity(World world) {
		super(ModEntities.TYPE_WATER.get(), world);
		this.preventEntitySpawning = true;
	}

	public WaterEntity(World world, PlayerEntity player) {
		super(ModEntities.TYPE_WATER.get(), player, world);
		this.player = player;
	}

	@Override
	public IPacket<?> createSpawnPacket() {
		return NetworkHooks.getEntitySpawningPacket(this);
	}

	@Override
	protected float getGravityVelocity() {
		return 0F;
	}
	
	double a = 0;

	@Override
	public void tick() {
		if (this.ticksExisted > maxTicks) {
			this.remove();
		}

		if(ticksExisted <= 1) {
			motion = getMotion();
			this.setMotion(0, 0, 0);
			this.velocityChanged = true;
			
		} else if (ticksExisted < 50) { //Shield
			double r = 1D;
			double cx = getPosX();
			double cy = getPosY();
			double cz = getPosZ();

			a+=30; //Speed and distance between particles
			double x = cx + (r * Math.cos(Math.toRadians(a)));
			double z = cz + (r * Math.sin(Math.toRadians(a)));

			double x2 = cx + (r * Math.cos(Math.toRadians(-a)));
			double z2 = cz + (r * Math.sin(Math.toRadians(-a)));

		//	System.out.println(a / 180 / 2);
			world.addParticle(ParticleTypes.DRIPPING_WATER, x2, (cy+0.5) - a / 1080D, z2, 0.0D, 0.0D, 0.0D);
			world.addParticle(ParticleTypes.DRIPPING_WATER, x2, (cy+0.5) - a / 1080D, z2, 0.0D, 0.0D, 0.0D);

		} else { //Projectile
			this.setMotion(motion);
			velocityChanged = true;
			for(double px = -0.5;px < 0.5;px++) {
				for(double pz = -0.5;pz < 0.5;pz++) {
					world.addParticle(ParticleTypes.DRIPPING_WATER, getPosX()+px, getPosY(), getPosZ()+pz, 0, 0, 0);
				}
			}

			world.addParticle(ParticleTypes.DRIPPING_WATER, getPosX(), getPosY(), getPosZ(), 0, 0, 0);
		}

		super.tick();
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
					target.attackEntityFrom(DamageSource.causeThrownDamage(this, this.getThrower()), 10);
					remove();
				}
			} else { // Block (not ERTR)

				if (brtResult != null && rtRes.getType() == Type.BLOCK) {
					BlockPos hitPos = brtResult.getPos();
					System.out.println(world.getBlockState(hitPos).getBlockState());
					if (world.getBlockState(hitPos).getBlockState() == Blocks.WATER.getDefaultState()) {
						System.out.println("water");
					}
				} else {
					// world.playSound(null, getPosition(), ModSounds.fistBounce,
					// SoundCategory.MASTER, 1F, 1F);
				}

				remove();
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
