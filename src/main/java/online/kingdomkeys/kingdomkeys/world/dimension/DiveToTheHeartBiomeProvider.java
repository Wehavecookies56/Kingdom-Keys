package online.kingdomkeys.kingdomkeys.world.dimension;

import com.mojang.serialization.Codec;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.Biomes;
import net.minecraft.world.biome.provider.BiomeProvider;
import net.minecraftforge.registries.ForgeRegistries;
import online.kingdomkeys.kingdomkeys.world.biome.ModBiomes;

import java.util.Collections;
import java.util.List;

public class DiveToTheHeartBiomeProvider extends BiomeProvider {

    protected DiveToTheHeartBiomeProvider() {
        super(Collections.singletonList(ForgeRegistries.BIOMES.getValue(ModBiomes.diveToTheHeart.getRegistryName())));
    }

    @Override
    protected Codec<? extends BiomeProvider> getBiomeProviderCodec() {
        return null;
    }

    @Override
    public BiomeProvider getBiomeProvider(long seed) {
        return null;
    }


    @Override
    public Biome getNoiseBiome(int x, int y, int z) {
        return biomes.get(0);
    }

}