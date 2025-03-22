package online.kingdomkeys.kingdomkeys.world.dimension.castle_oblivion.system.registry;

import net.minecraft.resources.ResourceLocation;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.world.dimension.castle_oblivion.system.room.RoomType;
import online.kingdomkeys.kingdomkeys.world.dimension.castle_oblivion.system.floor.FloorType;

import java.util.function.Supplier;

public class ModFloorTypes {

    public static Supplier<JsonRegistry<FloorType>> registry = ModJsonRegistries.FLOOR_TYPE;

    public static Supplier<FloorType>
            NONE = () -> registry.get().getValue(ResourceLocation.fromNamespaceAndPath(KingdomKeys.MODID, "none")),
            PLAINS = () -> registry.get().getValue(ResourceLocation.fromNamespaceAndPath(KingdomKeys.MODID, "plains")),
            NETHER = () -> registry.get().getValue(ResourceLocation.fromNamespaceAndPath(KingdomKeys.MODID, "nether"));

    public static boolean isFloorCompatible(FloorType floor, RoomType room) {
        return !floor.getRoomBlacklist().contains(room) && room.isFloorCompatible(floor);
    }
}
