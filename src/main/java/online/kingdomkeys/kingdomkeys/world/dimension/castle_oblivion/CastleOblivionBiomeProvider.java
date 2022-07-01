package online.kingdomkeys.kingdomkeys.world.dimension.castle_oblivion;

import com.mojang.serialization.Codec;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.resources.RegistryOps;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeSource;
import net.minecraft.world.level.biome.Climate;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.lib.Strings;
import online.kingdomkeys.kingdomkeys.world.dimension.dive_to_the_heart.DiveToTheHeartBiomeProvider;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class CastleOblivionBiomeProvider extends BiomeSource {
    public static void registerBiomeProvider() {
        Registry.register(Registry.BIOME_SOURCE, new ResourceLocation(KingdomKeys.MODID, "castle_oblivion_biome_source"), CastleOblivionBiomeProvider.CODEC);
    }

    public static final Codec<CastleOblivionBiomeProvider> CODEC = RegistryOps.retrieveRegistry(Registry.BIOME_REGISTRY).xmap(CastleOblivionBiomeProvider::new, CastleOblivionBiomeProvider::getBiomeRegistry).codec();

    private final Registry<Biome> BIOME_REGISTRY;
    private final Holder<Biome> BIOME;

    public static ResourceKey<Biome> BIOME_LOCATION = ResourceKey.create(Registry.BIOME_REGISTRY, new ResourceLocation(KingdomKeys.MODID, Strings.castleOblivion));

    private static final List<ResourceKey<Biome>> SPAWN = Collections.singletonList(BIOME_LOCATION);

    public CastleOblivionBiomeProvider(Registry<Biome> biomeRegistry) {
        super(SPAWN.stream().map(s -> biomeRegistry.getHolderOrThrow(ResourceKey.create(Registry.BIOME_REGISTRY, s.location()))).collect(Collectors.toList()));

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

    @Override
    @OnlyIn(Dist.CLIENT)
    public BiomeSource withSeed(long seed) {
        return this;
    }
}
