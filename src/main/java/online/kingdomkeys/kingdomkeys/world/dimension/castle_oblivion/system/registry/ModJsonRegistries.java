package online.kingdomkeys.kingdomkeys.world.dimension.castle_oblivion.system.registry;

import net.minecraft.core.Registry;
import net.neoforged.neoforge.registries.DeferredRegister;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.world.dimension.castle_oblivion.system.encounter.RoomEncounter;
import online.kingdomkeys.kingdomkeys.world.dimension.castle_oblivion.system.floor.FloorType;
import online.kingdomkeys.kingdomkeys.world.dimension.castle_oblivion.system.room.RoomStructure;
import online.kingdomkeys.kingdomkeys.world.dimension.castle_oblivion.system.room.RoomType;

import java.util.function.Supplier;

public class ModJsonRegistries {

    public static DeferredRegister<JsonRegistry<?>> JSON_REGISTRIES = DeferredRegister.create(KingdomKeys.rl("registries"), KingdomKeys.MODID);
    public static Registry<JsonRegistry<?>> registry = JSON_REGISTRIES.makeRegistry(builder -> builder.sync(true));

    public static Supplier<JsonRegistry<FloorType>> FLOOR_TYPE = JSON_REGISTRIES.register("floor_type", () -> new JsonRegistry<>(KingdomKeys.rl("floor_type"), "castle_oblivion/floor_type", FloorType.CODEC));
    public static Supplier<JsonRegistry<RoomStructure>> ROOM_STRUCTURE = JSON_REGISTRIES.register("room_structure", () -> new JsonRegistry<>(KingdomKeys.rl("room_structure"), "castle_oblivion/room_structure", RoomStructure.CODEC));
    public static Supplier<JsonRegistry<RoomType>> ROOM_TYPE = JSON_REGISTRIES.register("room_type", () -> new JsonRegistry<>(KingdomKeys.rl("room_type"), "castle_oblivion/room_type", RoomType.CODEC));
    public static Supplier<JsonRegistry<RoomEncounter>> ROOM_ENCOUNTER = JSON_REGISTRIES.register("room_encounter", () -> new JsonRegistry<>(KingdomKeys.rl("room_encounter"), "castle_oblivion/room_encounter", RoomEncounter.CODEC));
}
