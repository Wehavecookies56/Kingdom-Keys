package online.kingdomkeys.kingdomkeys.world.dimension.castle_oblivion.system;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.RegistryBuilder;
import online.kingdomkeys.kingdomkeys.KingdomKeys;

import java.awt.*;
import java.util.function.Supplier;

public class ModFloorTypes {

    public static DeferredRegister<FloorType> FLOOR_TYPES = DeferredRegister.create(ResourceLocation.fromNamespaceAndPath(KingdomKeys.MODID, "floors"), KingdomKeys.MODID);

    public static final ResourceKey<Registry<FloorType>> FLOOR_TYPES_KEY = ResourceKey.createRegistryKey(ResourceLocation.fromNamespaceAndPath(KingdomKeys.MODID, "floors"));
    public static Registry<FloorType> registry = new RegistryBuilder<>(FLOOR_TYPES_KEY).sync(true).defaultKey(ResourceLocation.fromNamespaceAndPath(KingdomKeys.MODID, "empty")).create();

    public static Supplier<FloorType>
        NONE = FLOOR_TYPES.register("none", () -> new FloorType("none", 0, 0, 0, 0, Color.BLACK).setRegistryName(ResourceLocation.fromNamespaceAndPath(KingdomKeys.MODID, "none"))),
        PLAINS = FLOOR_TYPES.register("plains", () -> new FloorType("plains", 7, 0, 0, 0, new Color(123, 169, 255)).setRegistryName(ResourceLocation.fromNamespaceAndPath(KingdomKeys.MODID, "plains"))),
        NETHER = FLOOR_TYPES.register("nether", () -> new FloorType("nether", 7, 0, 0, 0, new Color(35, 5, 6)).setRegistryName(ResourceLocation.fromNamespaceAndPath(KingdomKeys.MODID, "nether")));

    public static boolean isFloorCompatible(FloorType floor, RoomType room) {
        return !floor.roomBlacklist.contains(room) && room.getProperties().isFloorCompatible(floor);
    }
}
