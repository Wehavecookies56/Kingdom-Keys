package online.kingdomkeys.kingdomkeys.world.dimension;

import net.minecraft.util.RegistryKey;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.Mod;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.world.dimension.dive_to_the_heart.DiveToTheHeartBiomeProvider;
import online.kingdomkeys.kingdomkeys.world.dimension.dive_to_the_heart.DiveToTheHeartChunkGenerator;
import online.kingdomkeys.kingdomkeys.world.dimension.station_of_remembrance.StationOfRemembranceBiomeProvider;
import online.kingdomkeys.kingdomkeys.world.dimension.station_of_remembrance.StationOfRemembranceChunkGenerator;

@Mod.EventBusSubscriber
public class ModDimensions {
    public static final RegistryKey<World> DIVE_TO_THE_HEART = RegistryKey.getOrCreateKey(Registry.WORLD_KEY, new ResourceLocation(KingdomKeys.MODID, Strings.diveToTheHeart));
    public static final RegistryKey<World> STATION_OF_REMEMBRANCE = RegistryKey.getOrCreateKey(Registry.WORLD_KEY, new ResourceLocation(KingdomKeys.MODID, Strings.stationOfRemembrance));

    public static void setupDimension() {
        DiveToTheHeartChunkGenerator.registerChunkGenerator();
        DiveToTheHeartBiomeProvider.registerBiomeProvider();
        
        StationOfRemembranceChunkGenerator.registerChunkGenerator();
        StationOfRemembranceBiomeProvider.registerBiomeProvider();
    }
}
