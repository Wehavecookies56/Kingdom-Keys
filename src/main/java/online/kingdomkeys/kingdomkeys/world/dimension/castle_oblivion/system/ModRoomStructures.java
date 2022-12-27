package online.kingdomkeys.kingdomkeys.world.dimension.castle_oblivion.system;

import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.util.Size2i;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryBuilder;
import net.minecraftforge.registries.RegistryObject;
import online.kingdomkeys.kingdomkeys.KingdomKeys;

import java.util.List;
import java.util.function.Supplier;

public class ModRoomStructures {

    public static DeferredRegister<RoomStructure> ROOM_STRUCTURES = DeferredRegister.create(new ResourceLocation(KingdomKeys.MODID, "roomstructures"), KingdomKeys.MODID);

    public static Supplier<IForgeRegistry<RoomStructure>> registry = ROOM_STRUCTURES.makeRegistry(RoomStructure.class, RegistryBuilder::new);

    @SuppressWarnings("unchecked")
	public static final RegistryObject<RoomStructure>
        LOBBY = ROOM_STRUCTURES.register("lobby", () -> new RoomStructure("lobby", null, RoomProperties.RoomSize.SPECIAL, List.of(RoomProperties.RoomCategory.SPECIAL), new Size2i(33, 69))),
        TEST_ROOM = ROOM_STRUCTURES.register("test_room", () -> new RoomStructure("test_room", null, RoomProperties.RoomSize.M, List.of(RoomProperties.RoomCategory.ENEMY, RoomProperties.RoomCategory.STATUS, RoomProperties.RoomCategory.BOUNTY), new Size2i(32, 32))),
        PLAINS_S_1 = ROOM_STRUCTURES.register("plains_s_1", () -> new RoomStructure("plains_s_1", ModFloorTypes.PLAINS.get(), RoomProperties.RoomSize.S, List.of(RoomProperties.RoomCategory.ENEMY, RoomProperties.RoomCategory.STATUS, RoomProperties.RoomCategory.BOUNTY), new Size2i(32, 32))),
        PLAINS_S_2 = ROOM_STRUCTURES.register("plains_s_2", () -> new RoomStructure("plains_s_2", ModFloorTypes.PLAINS.get(), RoomProperties.RoomSize.S, List.of(RoomProperties.RoomCategory.ENEMY, RoomProperties.RoomCategory.STATUS, RoomProperties.RoomCategory.BOUNTY), new Size2i(32, 32))),
        BOTTOMLESS_DARKNESS = ROOM_STRUCTURES.register("bottomless_darkness", () -> new RoomStructure("bottomless_darkness", null, RoomProperties.RoomSize.L, List.of(RoomProperties.RoomCategory.ENEMY), new Size2i(64, 64), ModRoomTypes.BOTTOMLESS_DARKNESS))
    ;

    public static List<RoomStructure> getCompatibleStructures(RoomType type) {
        return registry.get().getValues().stream().filter(s -> isStructureCompatible(s, type)).toList();
    }

    public static boolean isStructureCompatible(RoomStructure structure, RoomType type) {
        if (structure.roomWhitelist == null || structure.roomWhitelist.isEmpty()) {
            return type.getProperties().getSize() == structure.size && structure.categories.contains(type.getProperties().getCategory()) && (structure.floor == null || type.getProperties().isFloorCompatible(structure.floor));
        } else {
            return structure.getRoomWhitelist().contains(type);
        }
    }
}
