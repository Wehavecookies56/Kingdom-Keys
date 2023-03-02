package online.kingdomkeys.kingdomkeys.entity;

import java.util.List;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.entity.IEntityAdditionalSpawnData;
import net.minecraftforge.network.NetworkHooks;
import net.minecraftforge.network.PlayMessages;
import online.kingdomkeys.kingdomkeys.network.PacketHandler;
import online.kingdomkeys.kingdomkeys.network.cts.CSOrgPortalTPPacket;

public class OrgPortalEntity extends Entity implements IEntityAdditionalSpawnData {

	int maxTicks = 100;
	float radius = 0.5F;
	
	BlockPos destinationPos;
    ResourceKey<Level> destinationDim;
    boolean shouldTeleport;

	public OrgPortalEntity(EntityType<? extends Entity> type, Level world) {
		super(type, world);
		this.blocksBuilding = true;
	}

	public OrgPortalEntity(PlayMessages.SpawnEntity spawnEntity, Level world) {
		super(ModEntities.TYPE_ORG_PORTAL.get(), world);
	}

	public OrgPortalEntity(Level world) {
		super(ModEntities.TYPE_ORG_PORTAL.get(), world);
		this.blocksBuilding = true;
	}

	public OrgPortalEntity(Level world, Player player, BlockPos spawnPos, BlockPos destinationPos, ResourceKey<Level> destinationDim, boolean shouldTP) {
		super(ModEntities.TYPE_ORG_PORTAL.get(), world);
		this.setPos(spawnPos.getX()+0.5,spawnPos.getY(), spawnPos.getZ()+0.5);
        this.destinationPos = destinationPos;
        this.destinationDim = destinationDim;
        this.shouldTeleport = shouldTP;
	}

	@Override
	public Packet<ClientGamePacketListener> getAddEntityPacket() {
		return NetworkHooks.getEntitySpawningPacket(this);
	}

	@Override
	public void tick() {
		if (this.tickCount > maxTicks) {
			this.remove(RemovalReason.KILLED);
		}
		level.addParticle(ParticleTypes.DRAGON_BREATH, getX() - 1 + random.nextDouble() * 2, getY() + random.nextDouble() * 4, getZ() - 1 + random.nextDouble() * 2, 0.0D, 0.0D, 0.0D);

		List<Entity> tempList = level.getEntities(this, getBoundingBox().inflate(radius, radius, radius));
		for (Entity t : tempList) {
			if(shouldTeleport && !(t instanceof OrgPortalEntity)) {
		        if(!this.isAlive())
		            return;
		        if(t != null){
		            if (destinationPos != null) {
		                if(destinationPos.getX()!=0 && destinationPos.getY()!=0 && destinationPos.getZ()!=0){
		                	double yOffset = t.getY() - this.getY();
		                	t.setPos(destinationPos.getX()+0.5, destinationPos.getY()+1 + yOffset, destinationPos.getZ()+0.5);
		                	if(t instanceof Player && level.isClientSide)
		                		PacketHandler.sendToServer(new CSOrgPortalTPPacket(this.destinationDim,destinationPos.getX()+0.5, destinationPos.getY()+1 + yOffset, destinationPos.getZ()+0.5));
		                }
		            }
		        }
	        }
		}
    	super.tick();
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
		/*if(destinationPos == null)
            return;
    	
        compound.putInt("x",destinationPos.getX());
        compound.putInt("y",destinationPos.getY());
        compound.putInt("z",destinationPos.getZ());
        compound.putInt("dim",destinationDim);
        compound.putBoolean("tp",shouldTeleport);*/
	}

	@Override
	public void readAdditionalSaveData(CompoundTag compound) {
		// this.setLvl(compound.getInt("lvl"));
	/*	destinationPos = new BlockPos(compound.getInt("x"),compound.getInt("y"),compound.getInt("z"));
    	destinationDim = compound.getInt("dim");
    	shouldTeleport = compound.getBoolean("tp");*/
	}

	@Override
	protected void defineSynchedData() {
		// TODO Auto-generated method stub

	}

	@Override
	public void writeSpawnData(FriendlyByteBuf buffer) {
		if(destinationPos == null)
            return;
    	
        buffer.writeBlockPos(new BlockPos(destinationPos.getX(),destinationPos.getY(),destinationPos.getZ()));
        buffer.writeResourceLocation(destinationDim.location());
        buffer.writeBoolean(shouldTeleport);	}

	@Override
	public void readSpawnData(FriendlyByteBuf additionalData) {
		destinationPos = additionalData.readBlockPos();
    	destinationDim = ResourceKey.create(Registries.DIMENSION, additionalData.readResourceLocation());
    	shouldTeleport = additionalData.readBoolean();
    }
}
