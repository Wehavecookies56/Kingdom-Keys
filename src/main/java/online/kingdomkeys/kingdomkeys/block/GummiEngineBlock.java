package online.kingdomkeys.kingdomkeys.block;

import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.Block;

import java.util.List;
import java.util.function.Supplier;

public class GummiEngineBlock extends GummiBlockEnd {
    int speed;
    public GummiEngineBlock(Properties properties, int weight, int armour, int speed) {
        super(properties, weight, armour, null, null);
        this.speed = speed;
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int mobility) {
        this.speed = mobility;
    }
}
