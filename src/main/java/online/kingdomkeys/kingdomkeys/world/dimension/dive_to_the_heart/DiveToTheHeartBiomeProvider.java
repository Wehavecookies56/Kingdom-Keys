package online.kingdomkeys.kingdomkeys.world.dimension.dive_to_the_heart;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import com.mojang.serialization.Codec;

import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.RegistryOps;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeSource;
import net.minecraft.world.level.biome.Climate;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.lib.Strings;

public class DiveToTheHeartBiomeProvider extends BiomeSource {
    public static void registerBiomeProvider() {
        Registry.register(Registries.BIOME_SOURCE, new ResourceLocation(KingdomKeys.MODID, "dive_to_the_heart_biome_source"), DiveToTheHeartBiomeProvider.CODEC);
    }

    public static final Codec<DiveToTheHeartBiomeProvider> CODEC = RegistryOps.retrieveRegistry(Registries.BIOME).xmap(DiveToTheHeartBiomeProvider::new, DiveToTheHeartBiomeProvider::getBiomeRegistry).codec();

    private final Registry<Biome> BIOME_REGISTRY;
    private final Holder<Biome> BIOME;

    public static ResourceKey<Biome> BIOME_LOCATION = ResourceKey.create(Registries.BIOME, new ResourceLocation(KingdomKeys.MODID, Strings.diveToTheHeart));

    private static final List<ResourceKey<Biome>> SPAWN = Collections.singletonList(BIOME_LOCATION);

    public DiveToTheHeartBiomeProvider(Registry<Biome> biomeRegistry) {
        super(SPAWN.stream().map(s -> biomeRegistry.getHolderOrThrow(ResourceKey.create(Registries.BIOME, s.location()))).collect(Collectors.toList()));

        this.BIOME_REGISTRY = biomeRegistry;
        this.BIOME = biomeRegistry.getHolderOrThrow(BIOME_LOCATION);
    }

    @Override
    public Holder<Biome> getNoiseBiome(int x, int y, int z, Climate.Sampler sampler) {
        return BIOME;
    }

    public Registry<Biome> getBiomeRegistry() {
        return BIOME_REGISTRY;
    }

    @Override
    protected Codec<? extends BiomeSource> codec() {
        return CODEC;
    }
}