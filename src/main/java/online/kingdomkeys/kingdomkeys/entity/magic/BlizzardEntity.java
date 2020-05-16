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
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.RayTraceResult.Type;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.FMLPlayMessages;
import net.minecraftforge.fml.network.NetworkHooks;
import online.kingdomkeys.kingdomkeys.entity.ModEntities;

public class BlizzardEntity extends ThrowableEntity {

	int maxTicks = 100;

	public BlizzardEntity(EntityType<? extends ThrowableEntity> type, World world) {
		super(type, world);
		this.preventEntitySpawning = true;
	}

	public BlizzardEntity(FMLPlayMessages.SpawnEntity spawnEntity, World world) {
		super(ModEntities.TYPE_BLIZZARD.get(), world);
	}

	public BlizzardEntity(World world) {
		super(ModEntities.TYPE_BLIZZARD.get(), world);
		this.preventEntitySpawning = true;
	}

	public BlizzardEntity(World world, PlayerEntity player) {
		super(ModEntities.TYPE_BLIZZARD.get(), player, world);
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

		if (world.getBlockState(getPosition()).getBlockState() == Blocks.WATER.getDefaultState()) {
			world.setBlockState(getPosition(), Blocks.ICE.getDefaultState());
			remove();
		} else if(world.getBlockState(getPosition()).getBlockState() == Blocks.LAVA.getDefaultState()){
			world.setBlockState(getPosition(), Blocks.OBSIDIAN.getDefaultState());
			remove();
		}

		if (ticksExisted > 2)
			world.addParticle(ParticleTypes.CLOUD, getPosX(), getPosY(), getPosZ(), 0, 0, 0);

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
