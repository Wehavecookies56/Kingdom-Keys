package online.kingdomkeys.kingdomkeys.block;

import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.Block;

import java.util.List;
import java.util.function.Supplier;

public class GummiAeroBlock extends GummiBlockEdge{
    int mobility;
    public GummiAeroBlock(Properties properties, int weight, int armour, DyeColor color, List<Supplier<Block>> blocks, int mobility) {
        super(properties, weight, armour, color, blocks);
        this.mobility = mobility;
    }

    public int getMobility() {
        return mobility;
    }

    public void setMobility(int mobility) {
        this.mobility = mobility;
    }
}
