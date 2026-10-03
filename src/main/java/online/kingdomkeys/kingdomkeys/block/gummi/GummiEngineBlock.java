package online.kingdomkeys.kingdomkeys.block.gummi;

import net.minecraft.network.chat.Component;

import java.util.List;

public class GummiEngineBlock extends GummiBlockBase {

    public GummiEngineBlock(GummiBlockProperties gummiProperties) {
        super(gummiProperties);
    }

    @Override
    protected void appendStats(List<Component> tooltip) {
        tooltip.add(stat("top_speed", getTopSpeed()));
        tooltip.add(stat("low_speed", getLowSpeed()));
        tooltip.add(stat("horsepower", getHorsepower()));
    }

    public int getTopSpeed() {
        GummiStats stats = stats();
        return stats != null ? stats.topSpeed() : 0;
    }

    public int getLowSpeed() {
        GummiStats stats = stats();
        return stats != null ? stats.lowSpeed() : 0;
    }

    public int getHorsepower() {
        GummiStats stats = stats();
        return stats != null ? stats.horsepower() : 0;
    }
}
