package online.kingdomkeys.kingdomkeys.item.card;

import net.minecraft.world.item.Item;
import online.kingdomkeys.kingdomkeys.item.ICreativeTab;
import online.kingdomkeys.kingdomkeys.world.dimension.castle_oblivion.system.floor.FloorType;

import java.util.function.Supplier;

public class WorldCardItem extends Item implements ICreativeTab {

    private final Supplier<FloorType> floorType;

    public WorldCardItem(Supplier<FloorType> floorType) {
        super(new Properties());
        this.floorType = floorType;
    }

    public FloorType getFloorType() {
        return floorType.get();
    }

    @Override
    public Tab getTab() {
        return Tab.CARDS;
    }
}
