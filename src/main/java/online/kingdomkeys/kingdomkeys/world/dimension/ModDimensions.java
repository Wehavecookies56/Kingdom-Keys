package online.kingdomkeys.kingdomkeys.world.dimension;

import com.mojang.serialization.Codec;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.lib.Strings;
import online.kingdomkeys.kingdomkeys.world.dimension.dive_to_the_heart.DiveToTheHeartChunkGenerator;
import online.kingdomkeys.kingdomkeys.world.dimension.station_of_sorrow.StationOfSorrowChunkGenerator;

@Mod.EventBusSubscriber
public class ModDimensions {
    public static final ResourceKey<Level> DIVE_TO_THE_HEART = ResourceKey.create(Registries.DIMENSION, new ResourceLocation(KingdomKeys.MODID, Strings.diveToTheHeart));
    public static final ResourceKey<Level> STATION_OF_SORROW = ResourceKey.create(Registries.DIMENSION, new ResourceLocation(KingdomKeys.MODID, Strings.stationOfSorrow));

    public static final DeferredRegister<Codec<? extends ChunkGenerator>> CHUNK_GENERATORS = DeferredRegister.create(Registries.CHUNK_GENERATOR,  KingdomKeys.MODID);

    public static final RegistryObject<Codec<DiveToTheHeartChunkGenerator>> DIVE_TO_THE_HEART_GENERATOR = CHUNK_GENERATORS.register("dive_to_the_heart_generator", () -> DiveToTheHeartChunkGenerator.CODEC);
    public static final RegistryObject<Codec<StationOfSorrowChunkGenerator>> STATION_OF_SORROW_GENERATOR = CHUNK_GENERATORS.register("station_of_sorrow_generator", () -> StationOfSorrowChunkGenerator.CODEC);

}
