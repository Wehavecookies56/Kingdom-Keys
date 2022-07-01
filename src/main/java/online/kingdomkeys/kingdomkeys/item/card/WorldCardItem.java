package online.kingdomkeys.kingdomkeys.item.card;

import net.minecraft.world.item.Item;
import online.kingdomkeys.kingdomkeys.KingdomKeys;

public class WorldCardItem /* extends Item */ {

    private final String floorType;
    public int critPathLength;
    public int bonusRoomCount;
    public int branchCount;
    public int bonusRoomChance;

    public WorldCardItem(String floorType) {
        //super(new Properties().tab(KingdomKeys.miscGroup));
        this.floorType = floorType;
    }

    public String getFloorType() {
        return floorType;
    }
}
