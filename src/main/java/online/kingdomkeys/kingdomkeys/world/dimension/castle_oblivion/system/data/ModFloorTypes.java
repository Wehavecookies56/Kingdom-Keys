package online.kingdomkeys.kingdomkeys.world.dimension.castle_oblivion.system.data;

import net.minecraft.resources.ResourceLocation;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.world.dimension.castle_oblivion.system.FloorType;
import online.kingdomkeys.kingdomkeys.world.dimension.castle_oblivion.system.RoomType;

import java.util.function.Supplier;

public class ModFloorTypes {

    public static Supplier<FloorType>
        NONE = () -> ModJsonRegistries.FLOOR_TYPE.get().getValue(new ResourceLocation(KingdomKeys.MODID, "none")),
        PLAINS = () -> ModJsonRegistries.FLOOR_TYPE.get().getValue(new ResourceLocation(KingdomKeys.MODID, "plains")),
        NETHER = () -> ModJsonRegistries.FLOOR_TYPE.get().getValue(new ResourceLocation(KingdomKeys.MODID, "nether"));

    public static boolean isFloorCompatible(FloorType floor, RoomType room) {
        return !floor.getRoomBlacklist().contains(room) && room.isFloorCompatible(floor);
    }

}
