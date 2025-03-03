package online.kingdomkeys.kingdomkeys.world.dimension.castle_oblivion.system;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryBuilder;
import net.minecraftforge.registries.RegistryObject;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.client.sound.ModSounds;

import java.awt.*;
import java.util.function.Supplier;

public class ModFloorTypes {

    public static DeferredRegister<FloorType> FLOOR_TYPES = DeferredRegister.create(new ResourceLocation(KingdomKeys.MODID, "floors"), KingdomKeys.MODID);

    public static Supplier<IForgeRegistry<FloorType>> registry = FLOOR_TYPES.makeRegistry(RegistryBuilder::new);

    public static RegistryObject<FloorType>
        NONE = FLOOR_TYPES.register("none", () -> new FloorType("none", 0, 0, 0, 0, Color.BLACK, ModSounds.Music_Castle_Oblivion).setRegistryName(new ResourceLocation(KingdomKeys.MODID, "none"))),
        PLAINS = FLOOR_TYPES.register("plains", () -> new FloorType("plains", 7, 0, 0, 0, new Color(123, 169, 255), SoundEvents.MUSIC_GAME).setRegistryName(new ResourceLocation(KingdomKeys.MODID, "plains"))),
        NETHER = FLOOR_TYPES.register("nether", () -> new FloorType("nether", 7, 0, 0, 0, new Color(35, 5, 6), SoundEvents.MUSIC_BIOME_NETHER_WASTES).setRegistryName(new ResourceLocation(KingdomKeys.MODID, "nether")));

    public static boolean isFloorCompatible(FloorType floor, RoomType room) {
        return !floor.roomBlacklist.contains(room) && room.getProperties().isFloorCompatible(floor);
    }
}
