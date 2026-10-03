package online.kingdomkeys.kingdomkeys.block.gummi;

import net.minecraft.network.chat.Component;

import java.util.List;

public class GummiEngineBlock extends GummiBlockBase {
    private final int topSpeed, lowSpeed, horsepower;

    public GummiEngineBlock(GummiBlockProperties gummiProperties, int topSpeed, int lowSpeed, int horsepower) {
        super(gummiProperties);
        this.topSpeed = topSpeed;
        this.lowSpeed = lowSpeed;
        this.horsepower = horsepower;
    }

    @Override
    protected void appendStats(List<Component> tooltip) {
        tooltip.add(stat("top_speed", topSpeed));
        tooltip.add(stat("low_speed", lowSpeed));
        tooltip.add(stat("horsepower", horsepower));
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
