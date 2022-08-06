package online.kingdomkeys.kingdomkeys.world.dimension.castle_oblivion.system;

import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryBuilder;
import net.minecraftforge.registries.RegistryObject;
import online.kingdomkeys.kingdomkeys.KingdomKeys;

import java.awt.*;
import java.util.function.Supplier;

public class ModFloorTypes {

    public static DeferredRegister<FloorType> FLOOR_TYPES = DeferredRegister.create(new ResourceLocation(KingdomKeys.MODID, "floors"), KingdomKeys.MODID);

    public static Supplier<IForgeRegistry<FloorType>> registry = FLOOR_TYPES.makeRegistry(FloorType.class, RegistryBuilder::new);

    public static RegistryObject<FloorType>
        NONE = FLOOR_TYPES.register("none", () -> new FloorType("none", 0, 0, 0, 0, Color.BLACK)),
        PLAINS = FLOOR_TYPES.register("plains", () -> new FloorType("plains", 7, 0, 0, 0, new Color(123, 169, 255)));

    public static boolean isFloorCompatible(FloorType floor, RoomType room) {
        return !floor.roomBlacklist.contains(room) && room.getProperties().isFloorCompatible(floor);
    }
}
