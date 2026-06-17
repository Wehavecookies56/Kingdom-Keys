package online.kingdomkeys.kingdomkeys.world.dimension.castle_oblivion.system.registry;

import net.minecraft.resources.ResourceLocation;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.world.dimension.castle_oblivion.system.floor.FloorType;
import online.kingdomkeys.kingdomkeys.world.dimension.castle_oblivion.system.room.RoomType;

import java.util.function.Supplier;

public class ModFloorTypes {

    public static Supplier<JsonRegistry<FloorType>> registry = ModJsonRegistries.FLOOR_TYPE;

    public static Supplier<FloorType>
            NONE = () -> registry.get().getValue(ResourceLocation.fromNamespaceAndPath(KingdomKeys.MODID, "none")),
            PLAINS = () -> registry.get().getValue(ResourceLocation.fromNamespaceAndPath(KingdomKeys.MODID, "plains")),
            THE_NETHER = () -> registry.get().getValue(ResourceLocation.fromNamespaceAndPath(KingdomKeys.MODID, "the_nether")),
            THE_END = () -> registry.get().getValue(ResourceLocation.fromNamespaceAndPath(KingdomKeys.MODID, "the_end")),
            OCEAN = () -> registry.get().getValue(ResourceLocation.fromNamespaceAndPath(KingdomKeys.MODID, "ocean")),
            JUNGLE = () -> registry.get().getValue(ResourceLocation.fromNamespaceAndPath(KingdomKeys.MODID, "jungle")),
            DESERT = () -> registry.get().getValue(ResourceLocation.fromNamespaceAndPath(KingdomKeys.MODID, "desert")),
            FOREST = () -> registry.get().getValue(ResourceLocation.fromNamespaceAndPath(KingdomKeys.MODID, "forest")),
            CAVE = () -> registry.get().getValue(ResourceLocation.fromNamespaceAndPath(KingdomKeys.MODID, "cave")),
            SWAMP = () -> registry.get().getValue(ResourceLocation.fromNamespaceAndPath(KingdomKeys.MODID, "swamp")),
            SNOWY = () -> registry.get().getValue(ResourceLocation.fromNamespaceAndPath(KingdomKeys.MODID, "snowy")),
            BADLANDS = () -> registry.get().getValue(ResourceLocation.fromNamespaceAndPath(KingdomKeys.MODID, "badlands")),
            MUSHROOM_FIELDS = () -> registry.get().getValue(ResourceLocation.fromNamespaceAndPath(KingdomKeys.MODID, "mushroom_fields")),
            CASTLE_OBLIVION = () -> registry.get().getValue(ResourceLocation.fromNamespaceAndPath(KingdomKeys.MODID, "castle_oblivion"));


    public static boolean isFloorCompatible(FloorType floor, RoomType room) {
        return !floor.getRoomBlacklist().contains(room) && room.isFloorCompatible(floor);
    }
}
