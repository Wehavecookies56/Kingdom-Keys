package online.kingdomkeys.kingdomkeys.world.dimension.castle_oblivion.system.registry;

import net.minecraft.resources.ResourceLocation;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.world.dimension.castle_oblivion.system.floor.FloorType;
import online.kingdomkeys.kingdomkeys.world.dimension.castle_oblivion.system.room.RoomStructure;
import online.kingdomkeys.kingdomkeys.world.dimension.castle_oblivion.system.room.RoomType;

import java.util.List;
import java.util.function.Supplier;

public class ModRoomStructures {

    public static Supplier<JsonRegistry<RoomStructure>> registry = ModJsonRegistries.ROOM_STRUCTURE;

    public static final Supplier<RoomStructure>
            //all
            ENTRANCE_HALL_1F = () -> registry.get().getValue(ResourceLocation.fromNamespaceAndPath(KingdomKeys.MODID, "entrance_hall_1f")),
            ENTRANCE_HALL = () -> registry.get().getValue(ResourceLocation.fromNamespaceAndPath(KingdomKeys.MODID, "entrance_hall")),
            BOTTOMLESS_DARKNESS = () -> registry.get().getValue(ResourceLocation.fromNamespaceAndPath(KingdomKeys.MODID, "bottomless_darkness")),
            FALLBACK = () -> registry.get().getValue(ResourceLocation.fromNamespaceAndPath(KingdomKeys.MODID, "fallback")),

            //floor specific
            SMALL_1 = () -> registry.get().getValue(ResourceLocation.fromNamespaceAndPath(KingdomKeys.MODID, "small_1")),
            SMALL_2 = () -> registry.get().getValue(ResourceLocation.fromNamespaceAndPath(KingdomKeys.MODID, "small_2")),
            SMALL_3 = () -> registry.get().getValue(ResourceLocation.fromNamespaceAndPath(KingdomKeys.MODID, "small_3")),
            MEDIUM_1 = () -> registry.get().getValue(ResourceLocation.fromNamespaceAndPath(KingdomKeys.MODID, "medium_1")),
            MEDIUM_2 = () -> registry.get().getValue(ResourceLocation.fromNamespaceAndPath(KingdomKeys.MODID, "medium_2")),
            MEDIUM_3 = () -> registry.get().getValue(ResourceLocation.fromNamespaceAndPath(KingdomKeys.MODID, "medium_3")),
            LARGE_1 = () -> registry.get().getValue(ResourceLocation.fromNamespaceAndPath(KingdomKeys.MODID, "large_1")),
            LARGE_2 = () -> registry.get().getValue(ResourceLocation.fromNamespaceAndPath(KingdomKeys.MODID, "large_2")),
            LARGE_3 = () -> registry.get().getValue(ResourceLocation.fromNamespaceAndPath(KingdomKeys.MODID, "large_3")),

            MOOGLE_ROOM = () -> registry.get().getValue(ResourceLocation.fromNamespaceAndPath(KingdomKeys.MODID, "moogle_room")),
            MOMENTS_REPRIEVE = () -> registry.get().getValue(ResourceLocation.fromNamespaceAndPath(KingdomKeys.MODID, "moments_reprieve"));




    public static List<RoomStructure> getCompatibleStructures(FloorType floor, RoomType room) {
        if (room.getFixedRoom().isPresent()) {
            return List.of(room.getFixedRoom().get());
        } else {
            for (RoomStructure rs : registry.get().getValues()) {
                KingdomKeys.LOGGER.debug("{}: compatible? {}", rs.registryName, isStructureCompatible(floor, rs, room));
            }
            return registry.get().getValues().stream().filter(s -> isStructureCompatible(floor, s, room) && !s.isFallback()).toList();
        }
    }

    public static List<RoomStructure> getFallbacks() {
        return registry.get().getValues().stream().filter(RoomStructure::isFallback).toList();
    }

    public static boolean isStructureCompatible(FloorType floor, RoomStructure structure, RoomType type) {
        if (structure.isFallback()) {
            return false;
        }
        if (!ModFloorTypes.isFloorCompatible(floor, type)) {
            KingdomKeys.LOGGER.debug("Floor was not compatible");
            return false;
        }
        if (structure.getRoomWhitelist().isEmpty()) {
            KingdomKeys.LOGGER.debug("Size {}, Category {}", type.getSize() == structure.getSize(), structure.getCategories().contains(type.getCategory()));
            return type.getSize() == structure.getSize() && structure.getCategories().contains(type.getCategory());
        } else {
            return structure.getRoomWhitelist().contains(type);
        }
    }
}
