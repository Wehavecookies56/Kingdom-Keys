package online.kingdomkeys.kingdomkeys.lib;

import java.util.UUID;

import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;

public class PortalData {
	UUID uuid, ownerID;
    String name;
    BlockPos pos;
    ResourceKey<Level> dimKey;

    public PortalData(UUID id, String name, double x, double y, double z, ResourceKey<Level> dimID, UUID ownerID) {
    	this.uuid = id;
    	this.name = name;
    	this.pos = new BlockPos(x,y,z);
        this.dimKey = dimID;
        this.ownerID = ownerID;
    }

    
    public UUID getUUID() {
    	return uuid;
    }
    
    public void setUUID(UUID id) {
    	this.uuid = id;
    }
    
    public String getName() {
    	return name;
    }
    
    public void setName(String name) {
    	this.name = name;
    }
   
    public BlockPos getPos() {
        return pos;
    }
    public void setPos(BlockPos pos) {
        this.pos = pos;
    }
   
    public ResourceKey<Level> getDimID() {
        return dimKey;
    }
    public void setDimID(ResourceKey<Level> dimID) {
        this.dimKey = dimID;
    }
    
    public UUID getOwnerID() {
        return ownerID;
    }
    public void setOwnerID(UUID id) {
        this.ownerID = id;
    } 

    public String getShortCoords() {
        //return x+", "+y+", "+z;
        return name;
    }
    
    public CompoundTag write() {
		CompoundTag portalNBT = new CompoundTag();
		portalNBT.putUUID("uuid", this.uuid);
		portalNBT.putString("name", this.getName());
		portalNBT.putDouble("x", this.pos.getX());
		portalNBT.putDouble("y", this.pos.getY());
		portalNBT.putDouble("z", this.pos.getZ());
		portalNBT.putString("dim", this.dimKey.location().toString());
		portalNBT.putUUID("owner", this.ownerID);
		return portalNBT;
	}

	public void read(CompoundTag nbt) {
		this.setUUID(nbt.getUUID("uuid"));
		this.setName(nbt.getString("name"));
		this.setPos(new BlockPos(nbt.getDouble("x"),nbt.getDouble("y"), nbt.getDouble("z")));
		ResourceLocation rl = new ResourceLocation(nbt.getString("dim"));
		this.setDimID(ResourceKey.create(Registries.DIMENSION,rl));
		this.setOwnerID(nbt.getUUID("owner"));
	}

}
