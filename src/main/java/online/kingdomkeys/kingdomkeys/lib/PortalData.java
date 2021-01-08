package online.kingdomkeys.kingdomkeys.lib;

import net.minecraft.util.RegistryKey;
import net.minecraft.world.World;

public class PortalData {
    byte pID;
    double x,y,z;
    RegistryKey<World> dimID;

    public PortalData(byte pID, double x, double y, double z, RegistryKey<World> dimID) {
        this.pID = pID;
        this.x = x;
        this.y = y;
        this.z = z;
        this.dimID = dimID;
    }

    public byte getPID() {
        return pID;
    }
    public void setPID(byte pID) {
        this.pID = pID;
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
    public RegistryKey<World> getDimID() {
        return dimID;
    }
    public void setDimID(RegistryKey<World> dimID) {
        this.dimID = dimID;
    }

    public String getShortCoords() {
        //return x+", "+y+", "+z;
        return String.valueOf(pID+1);
    }


}
