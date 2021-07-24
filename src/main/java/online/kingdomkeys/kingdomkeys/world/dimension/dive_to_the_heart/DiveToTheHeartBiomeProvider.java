package online.kingdomkeys.kingdomkeys.world.dimension.dive_to_the_heart;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.core.Registry;
import net.minecraft.resources.RegistryLookupCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeSource;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.lib.Strings;

public class DiveToTheHeartBiomeProvider extends BiomeSource {
    public static void registerBiomeProvider() {
        Registry.register(Registry.BIOME_SOURCE, new ResourceLocation(KingdomKeys.MODID, "dive_to_the_heart_biome_source"), DiveToTheHeartBiomeProvider.CODEC);
    }

    public static final Codec<DiveToTheHeartBiomeProvider> CODEC =
            RecordCodecBuilder.create((instance) -> instance.group(
                    RegistryLookupCodec.create(Registry.BIOME_REGISTRY).forGetter((vanillaLayeredBiomeSource) -> vanillaLayeredBiomeSource.BIOME_REGISTRY))
            .apply(instance, instance.stable(DiveToTheHeartBiomeProvider::new)));

    private final Registry<Biome> BIOME_REGISTRY;
    public static List<Biome> NONSTANDARD_BIOME = new ArrayList<>();

    public static ResourceLocation BIOME = new ResourceLocation(KingdomKeys.MODID, Strings.diveToTheHeart);

    public DiveToTheHeartBiomeProvider(Registry<Biome> biomeRegistry) {
        super(biomeRegistry.entrySet().stream()
                .filter(entry -> entry.getKey().location().getNamespace().equals(KingdomKeys.MODID))
                .map(Map.Entry::getValue)
                .collect(Collectors.toList()));

        NONSTANDARD_BIOME = this.possibleBiomes.stream()
                .filter(biome -> biomeRegistry.getKey(biome) != BIOME)
                .collect(Collectors.toList());

        this.BIOME_REGISTRY = biomeRegistry;
    }

    @Override
    public Biome getNoiseBiome(int x, int y, int z) {
        return possibleBiomes.get(0);
    }

    @Override
    protected Codec<? extends BiomeSource> codec() {
        return CODEC;
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public BiomeSource withSeed(long seed) {
        return new DiveToTheHeartBiomeProvider(this.BIOME_REGISTRY);
    }
}