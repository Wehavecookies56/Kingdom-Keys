package online.kingdomkeys.kingdomkeys.item.card;

import net.minecraft.world.item.Item;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.world.dimension.castle_oblivion.system.FloorType;

import java.util.function.Supplier;

public class WorldCardItem extends Item {

    private final Supplier<FloorType> floorType;

    public WorldCardItem(Supplier<FloorType> floorType) {
        super(new Properties().tab(KingdomKeys.miscGroup));
        this.floorType = floorType;
    }

    public FloorType getFloorType() {
        return floorType.get();
    }
}
