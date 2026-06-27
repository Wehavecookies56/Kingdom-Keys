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
            ENTRANCE_HALL_1F = () -> registry.get().getValue(ResourceLocation.fromNamespaceAndPath(KingdomKeys.MODID, "entrance_hall_1f")),
            ENTRANCE_HALL = () -> registry.get().getValue(ResourceLocation.fromNamespaceAndPath(KingdomKeys.MODID, "entrance_hall")),
            TEST_ROOM = () -> registry.get().getValue(ResourceLocation.fromNamespaceAndPath(KingdomKeys.MODID, "test_room")),
            PLAINS_S_1 = () -> registry.get().getValue(ResourceLocation.fromNamespaceAndPath(KingdomKeys.MODID, "plains_s_1")),
            PLAINS_S_2 = () -> registry.get().getValue(ResourceLocation.fromNamespaceAndPath(KingdomKeys.MODID, "plains_s_2")),
            PLAINS_S_3 = () -> registry.get().getValue(ResourceLocation.fromNamespaceAndPath(KingdomKeys.MODID, "plains_s_3")),
            PLAINS_M_1 = () -> registry.get().getValue(ResourceLocation.fromNamespaceAndPath(KingdomKeys.MODID, "plains_m_1")),
            PLAINS_M_2 = () -> registry.get().getValue(ResourceLocation.fromNamespaceAndPath(KingdomKeys.MODID, "plains_m_2")),
            PLAINS_M_3 = () -> registry.get().getValue(ResourceLocation.fromNamespaceAndPath(KingdomKeys.MODID, "plains_m_3")),
            PLAINS_L_1 = () -> registry.get().getValue(ResourceLocation.fromNamespaceAndPath(KingdomKeys.MODID, "plains_l_1")),

            BOTTOMLESS_DARKNESS = () -> registry.get().getValue(ResourceLocation.fromNamespaceAndPath(KingdomKeys.MODID, "bottomless_darkness")),
            NETHER_S_1 = () -> registry.get().getValue(ResourceLocation.fromNamespaceAndPath(KingdomKeys.MODID, "nether_s_1"));

    public static List<RoomStructure> getCompatibleStructures(FloorType floor, RoomType room) {
        if (room.getFixedRoom().isPresent()) {
            return List.of(room.getFixedRoom().get());
        } else {
            for (RoomStructure rs : registry.get().getValues()) {
                KingdomKeys.LOGGER.debug("{}: compatible? {}", rs.registryName, isStructureCompatible(floor, rs, room));
            }
            return registry.get().getValues().stream().filter(s -> isStructureCompatible(floor, s, room)).toList();
        }
    }

    public static boolean isStructureCompatible(FloorType floor, RoomStructure structure, RoomType type) {
        if (structure.getFloor() != null) {
            if (!ModFloorTypes.isFloorCompatible(floor, type) || !floor.registryName.equals(structure.getFloor().registryName)) {
                KingdomKeys.LOGGER.debug("Floor was not compatible");
                return false;
            }
        }
        if (structure.getRoomWhitelist().isEmpty()) {
            KingdomKeys.LOGGER.debug("Size {}, Category {}, Floor {}", type.getSize() == structure.getSize(), structure.getCategories().contains(type.getCategory()), (structure.getFloor() == null || type.isFloorCompatible(structure.getFloor())));
            return type.getSize() == structure.getSize() && structure.getCategories().contains(type.getCategory()) && (structure.getFloor() == null || type.isFloorCompatible(structure.getFloor()));
        } else {
            return structure.getRoomWhitelist().contains(type);
        }
    }
}
