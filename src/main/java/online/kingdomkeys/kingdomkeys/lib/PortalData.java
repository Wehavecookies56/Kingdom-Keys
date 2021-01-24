package online.kingdomkeys.kingdomkeys.lib;

import java.util.UUID;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;

public class PortalData {
	UUID uuid, ownerID;
    String name;
    BlockPos pos;
    RegistryKey<World> dimKey;

    public PortalData(UUID id, String name, double x, double y, double z, RegistryKey<World> dimID, UUID ownerID) {
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
   
    public RegistryKey<World> getDimID() {
        return dimKey;
    }
    public void setDimID(RegistryKey<World> dimID) {
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
    
    public CompoundNBT write() {
		CompoundNBT portalNBT = new CompoundNBT();
		portalNBT.putUniqueId("uuid", this.uuid);
		portalNBT.putString("name", this.getName());
		portalNBT.putDouble("x", this.pos.getX());
		portalNBT.putDouble("y", this.pos.getY());
		portalNBT.putDouble("z", this.pos.getZ());
		portalNBT.putString("dim", this.dimKey.getLocation().toString());
		portalNBT.putUniqueId("owner", this.ownerID);
		return portalNBT;
	}

	public void read(CompoundNBT nbt) {
		this.setUUID(nbt.getUniqueId("uuid"));
		this.setName(nbt.getString("name"));
		this.setPos(new BlockPos(nbt.getDouble("x"),nbt.getDouble("y"), nbt.getDouble("z")));
		ResourceLocation rl = new ResourceLocation(nbt.getString("dim"));
		this.setDimID(RegistryKey.getOrCreateKey(Registry.WORLD_KEY,rl));
		this.setOwnerID(nbt.getUniqueId("owner"));
	}

}
