package online.kingdomkeys.kingdomkeys.lib;

public class PortalCoords {
    byte pID;
    double x,y,z;
    int dimID;

    public PortalCoords(byte pID, double x, double y, double z, int dimID) {
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
    public int getDimID() {
        return dimID;
    }
    public void setDimID(int dimID) {
        this.dimID = dimID;
    }

    public String getShortCoords() {
        //return x+", "+y+", "+z;
        return String.valueOf(pID+1);
    }


}
