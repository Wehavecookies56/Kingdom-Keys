package online.kingdomkeys.kingdomkeys.world.dimension.castle_oblivion.system.data;

import net.minecraft.resources.ResourceLocation;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.world.dimension.castle_oblivion.system.RoomStructure;
import online.kingdomkeys.kingdomkeys.world.dimension.castle_oblivion.system.RoomType;

import java.util.List;
import java.util.function.Supplier;

public class ModRoomStructures {

	public static final Supplier<RoomStructure>
        LOBBY = () -> ModJsonRegistries.ROOM_STRUCTURE.get().getValue(new ResourceLocation(KingdomKeys.MODID, "lobby")),
        TEST_ROOM = () -> ModJsonRegistries.ROOM_STRUCTURE.get().getValue(new ResourceLocation(KingdomKeys.MODID, "test_room")),
        PLAINS_S_1 = () -> ModJsonRegistries.ROOM_STRUCTURE.get().getValue(new ResourceLocation(KingdomKeys.MODID, "plains_s_1")),
        PLAINS_S_2 = () -> ModJsonRegistries.ROOM_STRUCTURE.get().getValue(new ResourceLocation(KingdomKeys.MODID, "plains_s_2")),
        BOTTOMLESS_DARKNESS = () -> ModJsonRegistries.ROOM_STRUCTURE.get().getValue(new ResourceLocation(KingdomKeys.MODID, "bottomless_darkness")),
        NETHER_S_1 = () -> ModJsonRegistries.ROOM_STRUCTURE.get().getValue(new ResourceLocation(KingdomKeys.MODID, "nether_s_1"));

    public static List<RoomStructure> getCompatibleStructures(RoomType type) {
        return ModJsonRegistries.ROOM_STRUCTURE.get().getValues().stream().filter(s -> isStructureCompatible(s, type)).toList();
    }

    public static boolean isStructureCompatible(RoomStructure structure, RoomType type) {
        if (structure.getRoomWhitelist().isEmpty()) {
            return type.getSize() == structure.getSize() && structure.getCategories().contains(type.getCategory()) && (structure.getFloor() == null || type.isFloorCompatible(structure.getFloor()));
        } else {
            return structure.getRoomWhitelist().contains(type);
        }
    }
}
