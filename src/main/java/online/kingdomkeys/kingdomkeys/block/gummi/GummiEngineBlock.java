package online.kingdomkeys.kingdomkeys.block.gummi;

public class GummiEngineBlock extends GummiBlockBase {
    int speed;
    public GummiEngineBlock(GummiBlockProperties gummiProperties, int speed) {
        super(gummiProperties);
        this.speed = speed;
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }
}
