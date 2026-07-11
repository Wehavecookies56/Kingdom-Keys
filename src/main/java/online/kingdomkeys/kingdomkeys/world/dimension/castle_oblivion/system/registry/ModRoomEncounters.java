package online.kingdomkeys.kingdomkeys.world.dimension.castle_oblivion.system.registry;

import net.minecraft.resources.ResourceLocation;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.world.dimension.castle_oblivion.system.encounter.RoomEncounter;

import java.util.function.Supplier;

public class ModRoomEncounters {

    public static final Supplier<JsonRegistry<RoomEncounter>> registry = ModJsonRegistries.ROOM_ENCOUNTER;

    public static final Supplier<RoomEncounter>
        ROOM_OF_BEGINNINGS = () -> registry.get().getValue(ResourceLocation.fromNamespaceAndPath(KingdomKeys.MODID, "room_of_beginnings"));

}
