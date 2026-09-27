package online.kingdomkeys.kingdomkeys.block.gummi;

public class GummiEngineBlock extends GummiBlockBase {
    private final int topSpeed, lowSpeed, horsepower;

    public GummiEngineBlock(GummiBlockProperties gummiProperties, int topSpeed, int lowSpeed, int horsepower) {
        super(gummiProperties);
        this.topSpeed = topSpeed;
        this.lowSpeed = lowSpeed;
        this.horsepower = horsepower;
    }

    public int getTopSpeed() {
        return topSpeed;
    }

    public int getLowSpeed() {
        return lowSpeed;
    }

    public int getHorsepower() {
        return horsepower;
    }
}
