package online.kingdomkeys.kingdomkeys.block.gummi;

import net.minecraft.network.chat.Component;

import java.util.List;

public class GummiAeroBlock extends GummiBlockBase {
    int mobility;
    public GummiAeroBlock(GummiBlockProperties gummiProperties, int mobility) {
        super(gummiProperties);
        this.mobility = mobility;
    }

    @Override
    protected void appendStats(List<Component> tooltip) {
        tooltip.add(stat("mobility", mobility));
    }

    public int getMobility() {
        return mobility;
    }

    public void setMobility(int mobility) {
        this.mobility = mobility;
    }
}
