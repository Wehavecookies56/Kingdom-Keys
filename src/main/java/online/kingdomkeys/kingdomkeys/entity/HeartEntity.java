package online.kingdomkeys.kingdomkeys.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.FMLPlayMessages;
import net.minecraftforge.fml.network.NetworkHooks;

public class HeartEntity extends Entity {

	public final static int MAX_TICKS = 30;
	
	public HeartEntity(EntityType<? extends Entity> type, World world) {
		super(type, world);
		this.preventEntitySpawning = true;
	}

	public HeartEntity(FMLPlayMessages.SpawnEntity spawnEntity, World world) {
		super(ModEntities.TYPE_HEART.get(), world);
	}

	public HeartEntity(World world) {
		super(ModEntities.TYPE_HEART.get(), world);
		this.preventEntitySpawning = true;
	}

	/*public HeartEntity(World world, PlayerEntity player, BlockPos spawnPos, BlockPos destinationPos, int destinationDim, boolean shouldTP) {
		super(ModEntities.TYPE_HEART.get(), world);
		this.setPosition(spawnPos.getX()+0.5,spawnPos.getY(), spawnPos.getZ()+0.5);
        this.destinationPos = destinationPos;
        this.destinationDim = destinationDim;
        this.shouldTeleport = shouldTP;
	}*/

	@Override
	public void tick(){
		if(this.ticksExisted < MAX_TICKS - 10) {
			this.setPosition(getPosX(), getPosY() + 0.15, getPosZ());
			//this.posY += 0.15;
		} else {
			//KingdomKeys.proxy.spawnDarkSmokeParticle(world, getPosX(), getPosY(), getPosZ(), 0, 0, 0, 0.1F);
			world.addParticle(ParticleTypes.DRAGON_BREATH, getPosX(), getPosY(), getPosZ(), 0, 0, 0);
		}
		
		if(this.ticksExisted >= MAX_TICKS) {
			this.remove();
		}
		
		super.tick();
	}

	@Override
	protected void registerData() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void readAdditional(CompoundNBT compound) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void writeAdditional(CompoundNBT compound) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public IPacket<?> createSpawnPacket() {
		return NetworkHooks.getEntitySpawningPacket(this);
	}


}
