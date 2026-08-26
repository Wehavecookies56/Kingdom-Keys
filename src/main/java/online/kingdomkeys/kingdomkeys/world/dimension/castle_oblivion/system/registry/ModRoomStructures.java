package online.kingdomkeys.kingdomkeys.world.dimension.castle_oblivion.system.registry;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.packs.resources.Resource;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.world.dimension.castle_oblivion.system.floor.FloorType;
import online.kingdomkeys.kingdomkeys.world.dimension.castle_oblivion.system.room.RoomStructure;
import online.kingdomkeys.kingdomkeys.world.dimension.castle_oblivion.system.room.RoomType;

import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

public class ModRoomStructures {

    public static Supplier<JsonRegistry<RoomStructure>> registry = ModJsonRegistries.ROOM_STRUCTURE;

    public static final Supplier<RoomStructure>
            //all
            ENTRANCE_HALL_1F = () -> registry.get().getValue(KingdomKeys.rl("entrance_hall_1f")),
            ENTRANCE_HALL = () -> registry.get().getValue(KingdomKeys.rl("entrance_hall")),
            BOTTOMLESS_DARKNESS = () -> registry.get().getValue(KingdomKeys.rl("bottomless_darkness")),
            FALLBACK = () -> registry.get().getValue(KingdomKeys.rl("fallback")),

            //floor specific
            SMALL_1 = () -> registry.get().getValue(KingdomKeys.rl("small_1")),
            SMALL_2 = () -> registry.get().getValue(KingdomKeys.rl("small_2")),
            SMALL_3 = () -> registry.get().getValue(KingdomKeys.rl("small_3")),
            MEDIUM_1 = () -> registry.get().getValue(KingdomKeys.rl("medium_1")),
            MEDIUM_2 = () -> registry.get().getValue(KingdomKeys.rl("medium_2")),
            MEDIUM_3 = () -> registry.get().getValue(KingdomKeys.rl("medium_3")),
            LARGE_1 = () -> registry.get().getValue(KingdomKeys.rl("large_1")),
            LARGE_2 = () -> registry.get().getValue(KingdomKeys.rl("large_2")),
            LARGE_3 = () -> registry.get().getValue(KingdomKeys.rl("large_3")),

            MOOGLE_ROOM = () -> registry.get().getValue(KingdomKeys.rl("moogle_room")),
            MOMENTS_REPRIEVE = () -> registry.get().getValue(KingdomKeys.rl("moments_reprieve"));




    public static List<RoomStructure> getCompatibleStructures(ServerLevel level, FloorType floor, RoomType room) {
        if (room.getFixedRoom().isPresent()) {
            return List.of(room.getFixedRoom().get());
        } else {
            for (RoomStructure rs : registry.get().getValues()) {
                KingdomKeys.LOGGER.debug("{}: compatible? {}", rs.registryName, isStructureCompatible(level, floor, rs, room));
            }
            return registry.get().getValues().stream().filter(s -> isStructureCompatible(level, floor, s, room) && !s.isFallback()).toList();
        }
    }

    public static List<RoomStructure> getFallbacks() {
        return registry.get().getValues().stream().filter(RoomStructure::isFallback).toList();
    }

    public static boolean isStructureCompatible(ServerLevel level, FloorType floor, RoomStructure structure, RoomType type) {
        if (structure.isFallback()) {
            return false;
        }
        if (!ModFloorTypes.isFloorCompatible(floor, type)) {
            KingdomKeys.LOGGER.debug("Floor was not compatible");
            return false;
        }
        Optional<Resource> structureFile = structure.getStructureFile(level, floor);
        if (structureFile.isEmpty()) {
            return false;
        }
        if (structure.getDimensionsCache().isPresent()) {
            RoomStructure.RoomDimensions dimensions = structure.getDimensionsCache().get();
            //Max size is 126 as rooms are 128 blocks apart and walls need to fit so 126+2 would mean the rooms are touching
            if (dimensions.width() > 126 || dimensions.depth() > 126) {
                KingdomKeys.LOGGER.error("Structure exceeds maximum height/depth of 126 so it will never be generated");
                return false;
            }
        }
        if (structure.getRoomWhitelist().isEmpty()) {
            return type.getSize() == structure.getSize() && structure.getCategories().contains(type.getCategory());
        } else {
            return structure.getRoomWhitelist().contains(type);
        }
    }
}
