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

    public static final RegistryObject<RoomStructure>
        LOBBY = ROOM_STRUCTURES.register("lobby", () -> new RoomStructure("lobby", null, RoomProperties.RoomSize.SPECIAL, List.of(RoomProperties.RoomCategory.SPECIAL), new Size2i(33, 69))),
        TEST_ROOM = ROOM_STRUCTURES.register("test_room", () -> new RoomStructure("test_room", null, RoomProperties.RoomSize.M, List.of(RoomProperties.RoomCategory.ENEMY, RoomProperties.RoomCategory.STATUS, RoomProperties.RoomCategory.BOUNTY), new Size2i(32, 32)));

    public static List<RoomStructure> getCompatibleStructures(RoomType type) {
        return registry.get().getValues().stream().filter(s -> isStructureCompatible(s, type)).toList();
    }

    public static boolean isStructureCompatible(RoomStructure structure, RoomType type) {
        return type.getProperties().getSize() == structure.size && structure.categories.contains(type.getProperties().getCategory()) && (structure.floor == null || type.getProperties().isFloorCompatible(structure.floor));
    }
}
