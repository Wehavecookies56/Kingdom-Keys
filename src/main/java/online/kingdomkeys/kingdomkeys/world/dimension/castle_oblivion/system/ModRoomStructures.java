package online.kingdomkeys.kingdomkeys.world.dimension.castle_oblivion.system;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.common.util.Size2i;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.RegistryBuilder;
import online.kingdomkeys.kingdomkeys.KingdomKeys;

import java.util.List;
import java.util.function.Supplier;

public class ModRoomStructures {

    public static DeferredRegister<RoomStructure> ROOM_STRUCTURES = DeferredRegister.create(ResourceLocation.fromNamespaceAndPath(KingdomKeys.MODID, "roomstructures"), KingdomKeys.MODID);

    public static final ResourceKey<Registry<RoomStructure>> ROOM_STRUCTURES_KEY = ResourceKey.createRegistryKey(ResourceLocation.fromNamespaceAndPath(KingdomKeys.MODID, "roomstructures"));
    public static Registry<RoomStructure> registry = new RegistryBuilder<>(ROOM_STRUCTURES_KEY).sync(true).defaultKey(ResourceLocation.fromNamespaceAndPath(KingdomKeys.MODID, "empty")).create();

    @SuppressWarnings("unchecked")
	public static final Supplier<RoomStructure>
        LOBBY = ROOM_STRUCTURES.register("lobby", () -> new RoomStructure("lobby", null, RoomProperties.RoomSize.SPECIAL, List.of(RoomProperties.RoomCategory.SPECIAL), new Size2i(33, 69)).setRegistryName(ResourceLocation.fromNamespaceAndPath(KingdomKeys.MODID, "lobby"))),
        TEST_ROOM = ROOM_STRUCTURES.register("test_room", () -> new RoomStructure("test_room", null, RoomProperties.RoomSize.M, List.of(RoomProperties.RoomCategory.ENEMY, RoomProperties.RoomCategory.STATUS, RoomProperties.RoomCategory.BOUNTY), new Size2i(32, 32)).setRegistryName(ResourceLocation.fromNamespaceAndPath(KingdomKeys.MODID, "test_room"))),
        PLAINS_S_1 = ROOM_STRUCTURES.register("plains_s_1", () -> new RoomStructure("plains_s_1", ModFloorTypes.PLAINS.get(), RoomProperties.RoomSize.S, List.of(RoomProperties.RoomCategory.ENEMY, RoomProperties.RoomCategory.STATUS, RoomProperties.RoomCategory.BOUNTY), new Size2i(32, 32)).setRegistryName(ResourceLocation.fromNamespaceAndPath(KingdomKeys.MODID, "plains_s_1"))),
        PLAINS_S_2 = ROOM_STRUCTURES.register("plains_s_2", () -> new RoomStructure("plains_s_2", ModFloorTypes.PLAINS.get(), RoomProperties.RoomSize.S, List.of(RoomProperties.RoomCategory.ENEMY, RoomProperties.RoomCategory.STATUS, RoomProperties.RoomCategory.BOUNTY), new Size2i(32, 32)).setRegistryName(ResourceLocation.fromNamespaceAndPath(KingdomKeys.MODID, "plains_s_2"))),
        BOTTOMLESS_DARKNESS = ROOM_STRUCTURES.register("bottomless_darkness", () -> new RoomStructure("bottomless_darkness", null, RoomProperties.RoomSize.L, List.of(RoomProperties.RoomCategory.ENEMY), new Size2i(64, 64), ModRoomTypes.BOTTOMLESS_DARKNESS).setRegistryName(ResourceLocation.fromNamespaceAndPath(KingdomKeys.MODID, "bottomless_darkness"))),
        NETHER_S_1 = ROOM_STRUCTURES.register("nether_s_1", () -> new RoomStructure("nether_s_1", ModFloorTypes.NETHER.get(), RoomProperties.RoomSize.S, List.of(RoomProperties.RoomCategory.ENEMY, RoomProperties.RoomCategory.STATUS, RoomProperties.RoomCategory.BOUNTY), new Size2i(32, 32)).setRegistryName(ResourceLocation.fromNamespaceAndPath(KingdomKeys.MODID, "nether_s_1")))
    ;

    public static List<RoomStructure> getCompatibleStructures(RoomType type) {
        return registry.stream().filter(s -> isStructureCompatible(s, type)).toList();
    }

    public static boolean isStructureCompatible(RoomStructure structure, RoomType type) {
        if (structure.roomWhitelist == null || structure.roomWhitelist.isEmpty()) {
            return type.getProperties().getSize() == structure.size && structure.categories.contains(type.getProperties().getCategory()) && (structure.floor == null || type.getProperties().isFloorCompatible(structure.floor));
        } else {
            return structure.getRoomWhitelist().contains(type);
        }
    }
}
