package online.kingdomkeys.kingdomkeys.lib;

import java.util.UUID;

import net.minecraft.nbt.CompoundNBT;

public class PortalData {
	UUID uuid;
    String name;
    double x,y,z;
    int dimID;

    public PortalData(UUID id, String name, double x, double y, double z, int dimID) {
    	this.uuid = id;
    	this.name = name;
        this.x = x;
        this.y = y;
        this.z = z;
        this.dimID = dimID;
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
   
    public double getX() {
        return x;
    }
    public void setX(double x) {
        this.x = x;
    }
    public double getY() {
        return y;
    }
    public void setY(double y) {
        this.y = y;
    }
    public double getZ() {
        return z;
    }
    public void setZ(double z) {
        this.z = z;
    }
    public int getDimID() {
        return dimID;
    }
    public void setDimID(int dimID) {
        this.dimID = dimID;
    }

    public String getShortCoords() {
        //return x+", "+y+", "+z;
        return name;
    }
    
    public CompoundNBT write() {
		CompoundNBT portalNBT = new CompoundNBT();
		portalNBT.putUniqueId("uuid", this.uuid);
		portalNBT.putString("name", this.getName());
		portalNBT.putDouble("x", this.x);
		portalNBT.putDouble("y", this.y);
		portalNBT.putDouble("z", this.z);
		portalNBT.putInt("dim", this.dimID);
		return portalNBT;
	}

	public void read(CompoundNBT nbt) {
		this.setUUID(nbt.getUniqueId("uuid"));
		this.setName(nbt.getString("name"));
		this.setX(nbt.getDouble("x"));
		this.setY(nbt.getDouble("y"));
		this.setZ(nbt.getDouble("z"));
		this.setDimID(nbt.getInt("dim"));
	}
}
