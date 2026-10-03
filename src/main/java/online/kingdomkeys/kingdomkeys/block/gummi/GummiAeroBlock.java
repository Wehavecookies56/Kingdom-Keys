package online.kingdomkeys.kingdomkeys.block.gummi;

import net.minecraft.network.chat.Component;

import java.util.List;

public class GummiAeroBlock extends GummiBlockBase {

    public GummiAeroBlock(GummiBlockProperties gummiProperties) {
        super(gummiProperties);
    }

    @Override
    protected void appendStats(List<Component> tooltip) {
        tooltip.add(stat("mobility", getMobility()));
    }

    public int getMobility() {
        GummiStats stats = stats();
        return stats != null ? stats.mobility() : 0;
    }
}
