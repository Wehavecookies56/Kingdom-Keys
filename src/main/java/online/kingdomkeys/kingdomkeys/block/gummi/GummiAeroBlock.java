package online.kingdomkeys.kingdomkeys.block.gummi;

public class GummiAeroBlock extends GummiBlockBase {
    int mobility;
    public GummiAeroBlock(GummiBlockProperties gummiProperties, int mobility) {
        super(gummiProperties);
        this.mobility = mobility;
    }

    public int getMobility() {
        return mobility;
    }

    public void setMobility(int mobility) {
        this.mobility = mobility;
    }
}
