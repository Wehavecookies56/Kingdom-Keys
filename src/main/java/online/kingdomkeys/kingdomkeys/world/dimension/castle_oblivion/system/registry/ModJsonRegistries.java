package online.kingdomkeys.kingdomkeys.world.dimension.castle_oblivion.system.registry;

import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryBuilder;
import net.minecraftforge.registries.RegistryObject;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.world.dimension.castle_oblivion.system.floor.FloorType;
import online.kingdomkeys.kingdomkeys.world.dimension.castle_oblivion.system.room.RoomStructure;
import online.kingdomkeys.kingdomkeys.world.dimension.castle_oblivion.system.room.RoomType;

import java.util.function.Supplier;

public class ModJsonRegistries {

    public static DeferredRegister<JsonRegistry<?>> JSON_REGISTRIES = DeferredRegister.create(new ResourceLocation(KingdomKeys.MODID, "registries"), KingdomKeys.MODID);
    public static Supplier<IForgeRegistry<JsonRegistry<?>>> registry = JSON_REGISTRIES.makeRegistry(RegistryBuilder::new);

    public static RegistryObject<JsonRegistry<FloorType>> FLOOR_TYPE = JSON_REGISTRIES.register("floor_type", () -> new JsonRegistry<>(new ResourceLocation(KingdomKeys.MODID, "floor_type"), "castle_oblivion/floor_type", (json, typeOfT, context) -> new FloorType(json), FloorType.class));
    public static RegistryObject<JsonRegistry<RoomStructure>> ROOM_STRUCTURE = JSON_REGISTRIES.register("room_structure", () -> new JsonRegistry<>(new ResourceLocation(KingdomKeys.MODID, "room_structure"), "castle_oblivion/room_structure", (json, typeOfT, context) -> new RoomStructure(json), RoomStructure.class));
    public static RegistryObject<JsonRegistry<RoomType>> ROOM_TYPE = JSON_REGISTRIES.register("room_type", () -> new JsonRegistry<>(new ResourceLocation(KingdomKeys.MODID, "room_type"), "castle_oblivion/room_type", (json, typeOfT, context) -> new RoomType(json), RoomType.class));

}
