package online.kingdomkeys.kingdomkeys.world.dimension;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.registries.DeferredRegister;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.lib.Strings;
import online.kingdomkeys.kingdomkeys.world.dimension.castle_oblivion.CastleOblivionChunkGenerator;
import online.kingdomkeys.kingdomkeys.world.dimension.castle_oblivion.CastleOblivionInteriorChunkGenerator;
import online.kingdomkeys.kingdomkeys.world.dimension.dive_to_the_heart.DiveToTheHeartChunkGenerator;
import online.kingdomkeys.kingdomkeys.world.dimension.station_of_sorrow.StationOfSorrowChunkGenerator;

import java.util.function.Supplier;

@EventBusSubscriber
public class ModDimensions {
    public static final ResourceKey<Level> DIVE_TO_THE_HEART = ResourceKey.create(Registries.DIMENSION, ResourceLocation.fromNamespaceAndPath(KingdomKeys.MODID, Strings.diveToTheHeart));
    public static final ResourceKey<Level> STATION_OF_SORROW = ResourceKey.create(Registries.DIMENSION, ResourceLocation.fromNamespaceAndPath(KingdomKeys.MODID, Strings.stationOfSorrow));
    public static final ResourceKey<Level> CASTLE_OBLIVION = ResourceKey.create(Registries.DIMENSION, ResourceLocation.fromNamespaceAndPath(KingdomKeys.MODID, Strings.castleOblivion));

    public static final DeferredRegister<MapCodec<? extends ChunkGenerator>> CHUNK_GENERATORS = DeferredRegister.create(Registries.CHUNK_GENERATOR,  KingdomKeys.MODID);

    public static final Supplier<MapCodec<DiveToTheHeartChunkGenerator>> DIVE_TO_THE_HEART_GENERATOR = CHUNK_GENERATORS.register("dive_to_the_heart_generator", () -> DiveToTheHeartChunkGenerator.CODEC);
    public static final Supplier<MapCodec<StationOfSorrowChunkGenerator>> STATION_OF_SORROW_GENERATOR = CHUNK_GENERATORS.register("station_of_sorrow_generator", () -> StationOfSorrowChunkGenerator.CODEC);

    public static final Supplier<MapCodec<CastleOblivionChunkGenerator>> CASTLE_OBLIVION_GENERATOR = CHUNK_GENERATORS.register("castle_oblivion_generator", () -> CastleOblivionChunkGenerator.CODEC);
    public static final Supplier<MapCodec<CastleOblivionInteriorChunkGenerator>> CASTLE_OBLIVION_INTERIOR_GENERATOR = CHUNK_GENERATORS.register("castle_oblivion_interior_generator", () -> CastleOblivionInteriorChunkGenerator.CODEC);

}
