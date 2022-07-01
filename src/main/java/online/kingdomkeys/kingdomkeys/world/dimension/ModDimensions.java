package online.kingdomkeys.kingdomkeys.world.dimension;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraftforge.fml.common.Mod;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.lib.Strings;
import online.kingdomkeys.kingdomkeys.world.dimension.castle_oblivion.CastleOblivionBiomeProvider;
import online.kingdomkeys.kingdomkeys.world.dimension.castle_oblivion.CastleOblivionChunkGenerator;
import online.kingdomkeys.kingdomkeys.world.dimension.castle_oblivion.CastleOblivionInteriorBiomeProvider;
import online.kingdomkeys.kingdomkeys.world.dimension.castle_oblivion.CastleOblivionInteriorChunkGenerator;
import online.kingdomkeys.kingdomkeys.world.dimension.dive_to_the_heart.DiveToTheHeartBiomeProvider;
import online.kingdomkeys.kingdomkeys.world.dimension.dive_to_the_heart.DiveToTheHeartChunkGenerator;
import online.kingdomkeys.kingdomkeys.world.dimension.station_of_sorrow.StationOfSorrowBiomeProvider;
import online.kingdomkeys.kingdomkeys.world.dimension.station_of_sorrow.StationOfSorrowChunkGenerator;

@Mod.EventBusSubscriber
public class ModDimensions {
    public static final ResourceKey<Level> DIVE_TO_THE_HEART = ResourceKey.create(Registry.DIMENSION_REGISTRY, new ResourceLocation(KingdomKeys.MODID, Strings.diveToTheHeart));
    public static final ResourceKey<Level> STATION_OF_SORROW = ResourceKey.create(Registry.DIMENSION_REGISTRY, new ResourceLocation(KingdomKeys.MODID, Strings.stationOfSorrow));
    public static final ResourceKey<Level> CASTLE_OBLIVION = ResourceKey.create(Registry.DIMENSION_REGISTRY, new ResourceLocation(KingdomKeys.MODID, Strings.castleOblivion));

    public static void setupDimension() {
        DiveToTheHeartChunkGenerator.registerChunkGenerator();
        DiveToTheHeartBiomeProvider.registerBiomeProvider();
        
        StationOfSorrowChunkGenerator.registerChunkGenerator();
        StationOfSorrowBiomeProvider.registerBiomeProvider();

        CastleOblivionChunkGenerator.registerChunkGenerator();
        CastleOblivionBiomeProvider.registerBiomeProvider();

        CastleOblivionInteriorChunkGenerator.registerChunkGenerator();
        CastleOblivionInteriorBiomeProvider.registerBiomeProvider();
    }
}
