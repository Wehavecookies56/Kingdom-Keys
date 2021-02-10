package online.kingdomkeys.kingdomkeys.entity;

import java.util.List;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.network.PacketBuffer;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.fml.common.registry.IEntityAdditionalSpawnData;
import net.minecraftforge.fml.network.FMLPlayMessages;
import net.minecraftforge.fml.network.NetworkHooks;
import online.kingdomkeys.kingdomkeys.network.PacketHandler;
import online.kingdomkeys.kingdomkeys.network.cts.CSOrgPortalTPPacket;

public class OrgPortalEntity extends Entity implements IEntityAdditionalSpawnData{

	int maxTicks = 100;
	float radius = 0.5F;
	
	BlockPos destinationPos;
    RegistryKey<World> destinationDim;
    boolean shouldTeleport;

	public OrgPortalEntity(EntityType<? extends Entity> type, World world) {
		super(type, world);
		this.preventEntitySpawning = true;
	}

	public OrgPortalEntity(FMLPlayMessages.SpawnEntity spawnEntity, World world) {
		super(ModEntities.TYPE_ORG_PORTAL.get(), world);
	}

	public OrgPortalEntity(World world) {
		super(ModEntities.TYPE_ORG_PORTAL.get(), world);
		this.preventEntitySpawning = true;
	}

	public OrgPortalEntity(World world, PlayerEntity player, BlockPos spawnPos, BlockPos destinationPos, RegistryKey<World> destinationDim, boolean shouldTP) {
		super(ModEntities.TYPE_ORG_PORTAL.get(), world);
		this.setPosition(spawnPos.getX()+0.5,spawnPos.getY(), spawnPos.getZ()+0.5);
        this.destinationPos = destinationPos;
        this.destinationDim = destinationDim;
        this.shouldTeleport = shouldTP;
	}

	@Override
	public IPacket<?> createSpawnPacket() {
		return NetworkHooks.getEntitySpawningPacket(this);
	}

	@Override
	public void tick() {
		if (this.ticksExisted > maxTicks) {
			this.remove();
		}
        world.addParticle(ParticleTypes.DRAGON_BREATH, getPosX()-1+rand.nextDouble()*2, getPosY() + rand.nextDouble()*4, getPosZ()-1+rand.nextDouble()*2, 0.0D, 0.0D, 0.0D);

		List<Entity> tempList = world.getEntitiesWithinAABBExcludingEntity(this, getBoundingBox().grow(radius, radius, radius));
		for (Entity t : tempList) {
			if(shouldTeleport) {
		        if(!this.isAlive())
		            return;
		        if(t != null){
		            if (destinationPos != null) {
		                if(destinationPos.getX()!=0 && destinationPos.getY()!=0 && destinationPos.getZ()!=0){
		                	t.setPosition(destinationPos.getX()+0.5, destinationPos.getY()+1, destinationPos.getZ()+0.5);
		                	if(t instanceof PlayerEntity && world.isRemote)
		                		PacketHandler.sendToServer(new CSOrgPortalTPPacket(this.destinationDim,destinationPos.getX()+0.5, destinationPos.getY()+1, destinationPos.getZ()+0.5));
		                }
		            }
		        }
	        }
		}
    	

		super.tick();
	}
	
	 @Override
	    public void onCollideWithPlayer(PlayerEntity player) {
	        /*if(shouldTeleport) {
		        if(!this.isAlive())
		            return;
		        if(player != null){
		            if (destinationPos != null) {
		                if(destinationPos.getX()!=0 && destinationPos.getY()!=0 && destinationPos.getZ()!=0){
		                	player.setPosition(destinationPos.getX(), destinationPos.getY(), destinationPos.getZ());
		                	if(player.world.isRemote)
		                		PacketHandler.sendToServer(new CSOrgPortalTPPacket(this.destinationDim,destinationPos.getX()+0.5, destinationPos.getY()+1, destinationPos.getZ()+0.5));
		                }
		            }
		        }
	        }*/

	        super.onCollideWithPlayer(player);
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
		/*if(destinationPos == null)
            return;
    	
        compound.putInt("x",destinationPos.getX());
        compound.putInt("y",destinationPos.getY());
        compound.putInt("z",destinationPos.getZ());
        compound.putInt("dim",destinationDim);
        compound.putBoolean("tp",shouldTeleport);*/
	}

	@Override
	public void readAdditional(CompoundNBT compound) {
		// this.setLvl(compound.getInt("lvl"));
	/*	destinationPos = new BlockPos(compound.getInt("x"),compound.getInt("y"),compound.getInt("z"));
    	destinationDim = compound.getInt("dim");
    	shouldTeleport = compound.getBoolean("tp");*/
	}

	@Override
	protected void registerData() {
		// TODO Auto-generated method stub

	}

	@Override
	public void writeSpawnData(PacketBuffer buffer) {
		if(destinationPos == null)
            return;
    	
        buffer.writeBlockPos(new BlockPos(destinationPos.getX(),destinationPos.getY(),destinationPos.getZ()));
        buffer.writeResourceLocation(destinationDim.getLocation());
        buffer.writeBoolean(shouldTeleport);	}

	@Override
	public void readSpawnData(PacketBuffer additionalData) {
		destinationPos = additionalData.readBlockPos();
    	destinationDim = RegistryKey.getOrCreateKey(Registry.WORLD_KEY, additionalData.readResourceLocation());
    	shouldTeleport = additionalData.readBoolean();
    }
}
