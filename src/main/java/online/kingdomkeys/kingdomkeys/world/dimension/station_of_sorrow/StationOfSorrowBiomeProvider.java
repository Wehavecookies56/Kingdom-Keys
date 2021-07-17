package online.kingdomkeys.kingdomkeys.world.dimension.station_of_sorrow;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryLookupCodec;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.provider.BiomeProvider;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.lib.Strings;

public class StationOfSorrowBiomeProvider extends BiomeProvider {
    public static void registerBiomeProvider() {
        Registry.register(Registry.BIOME_PROVIDER_CODEC, new ResourceLocation(KingdomKeys.MODID, "station_of_remembrance_biome_source"), StationOfSorrowBiomeProvider.CODEC);
    }

    public static final Codec<StationOfSorrowBiomeProvider> CODEC =
            RecordCodecBuilder.create((instance) -> instance.group(
                    RegistryLookupCodec.getLookUpCodec(Registry.BIOME_KEY).forGetter((vanillaLayeredBiomeSource) -> vanillaLayeredBiomeSource.BIOME_REGISTRY))
            .apply(instance, instance.stable(StationOfSorrowBiomeProvider::new)));

    private final Registry<Biome> BIOME_REGISTRY;
    public static List<Biome> NONSTANDARD_BIOME = new ArrayList<>();

    public static ResourceLocation BIOME = new ResourceLocation(KingdomKeys.MODID, Strings.stationOfSorrow);

    public StationOfSorrowBiomeProvider(Registry<Biome> biomeRegistry) {
        super(biomeRegistry.getEntries().stream()
                .filter(entry -> entry.getKey().getLocation().getNamespace().equals(KingdomKeys.MODID))
                .map(Map.Entry::getValue)
                .collect(Collectors.toList()));

        NONSTANDARD_BIOME = this.biomes.stream()
                .filter(biome -> biomeRegistry.getKey(biome) != BIOME)
                .collect(Collectors.toList());

        this.BIOME_REGISTRY = biomeRegistry;
    }

    @Override
    public Biome getNoiseBiome(int x, int y, int z) {
        return biomes.get(0);
    }

    @Override
    protected Codec<? extends BiomeProvider> getBiomeProviderCodec() {
        return CODEC;
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public BiomeProvider getBiomeProvider(long seed) {
        return new StationOfSorrowBiomeProvider(this.BIOME_REGISTRY);
    }
}