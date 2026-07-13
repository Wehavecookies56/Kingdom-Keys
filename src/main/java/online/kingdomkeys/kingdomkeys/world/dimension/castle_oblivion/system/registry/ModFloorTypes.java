package online.kingdomkeys.kingdomkeys.world.dimension.castle_oblivion.system.registry;

import net.minecraft.resources.ResourceLocation;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.world.dimension.castle_oblivion.system.floor.FloorType;
import online.kingdomkeys.kingdomkeys.world.dimension.castle_oblivion.system.room.RoomType;

import java.util.function.Supplier;

public class ModFloorTypes {

    public static Supplier<JsonRegistry<FloorType>> registry = ModJsonRegistries.FLOOR_TYPE;

    public static Supplier<FloorType>
            NONE = () -> registry.get().getValue(KingdomKeys.rl("none")),
            PLAINS = () -> registry.get().getValue(KingdomKeys.rl("plains")),
            THE_NETHER = () -> registry.get().getValue(KingdomKeys.rl("the_nether")),
            THE_END = () -> registry.get().getValue(KingdomKeys.rl("the_end")),
            OCEAN = () -> registry.get().getValue(KingdomKeys.rl("ocean")),
            JUNGLE = () -> registry.get().getValue(KingdomKeys.rl("jungle")),
            DESERT = () -> registry.get().getValue(KingdomKeys.rl("desert")),
            FOREST = () -> registry.get().getValue(KingdomKeys.rl("forest")),
            CAVE = () -> registry.get().getValue(KingdomKeys.rl("cave")),
            SWAMP = () -> registry.get().getValue(KingdomKeys.rl("swamp")),
            SNOWY = () -> registry.get().getValue(KingdomKeys.rl("snowy")),
            BADLANDS = () -> registry.get().getValue(KingdomKeys.rl("badlands")),
            MUSHROOM_FIELDS = () -> registry.get().getValue(KingdomKeys.rl("mushroom_fields")),
            CASTLE_OBLIVION = () -> registry.get().getValue(KingdomKeys.rl("castle_oblivion"));


    public static boolean isFloorCompatible(FloorType floor, RoomType room) {
        return !floor.getRoomBlacklist().contains(room) && room.isFloorCompatible(floor);
    }
}
