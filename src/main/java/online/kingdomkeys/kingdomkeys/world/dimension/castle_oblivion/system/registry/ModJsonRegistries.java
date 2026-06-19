package online.kingdomkeys.kingdomkeys.world.dimension.castle_oblivion.system.registry;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.registries.DeferredRegister;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.world.dimension.castle_oblivion.system.floor.FloorType;
import online.kingdomkeys.kingdomkeys.world.dimension.castle_oblivion.system.room.RoomStructure;
import online.kingdomkeys.kingdomkeys.world.dimension.castle_oblivion.system.room.RoomType;

import java.util.function.Supplier;

public class ModJsonRegistries {

    public static DeferredRegister<JsonRegistry<?>> JSON_REGISTRIES = DeferredRegister.create(ResourceLocation.fromNamespaceAndPath(KingdomKeys.MODID, "registries"), KingdomKeys.MODID);
    public static Registry<JsonRegistry<?>> registry = JSON_REGISTRIES.makeRegistry(builder -> builder.sync(true));

    public static Supplier<JsonRegistry<FloorType>> FLOOR_TYPE = JSON_REGISTRIES.register("floor_type", () -> new JsonRegistry<>(ResourceLocation.fromNamespaceAndPath(KingdomKeys.MODID, "floor_type"), "castle_oblivion/floor_type", FloorType.CODEC, FloorType.class));
    public static Supplier<JsonRegistry<RoomStructure>> ROOM_STRUCTURE = JSON_REGISTRIES.register("room_structure", () -> new JsonRegistry<>(ResourceLocation.fromNamespaceAndPath(KingdomKeys.MODID, "room_structure"), "castle_oblivion/room_structure", RoomStructure.CODEC, RoomStructure.class));
    public static Supplier<JsonRegistry<RoomType>> ROOM_TYPE = JSON_REGISTRIES.register("room_type", () -> new JsonRegistry<>(ResourceLocation.fromNamespaceAndPath(KingdomKeys.MODID, "room_type"), "castle_oblivion/room_type", RoomType.CODEC, RoomType.class));

}
