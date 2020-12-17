package online.kingdomkeys.kingdomkeys.world.dimension;

import net.minecraft.block.BlockState;
import net.minecraft.entity.EntityClassification;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.Biomes;
import net.minecraft.world.biome.provider.BiomeProvider;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraftforge.registries.ForgeRegistries;
import online.kingdomkeys.kingdomkeys.world.biome.ModBiomes;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class DiveToTheHeartBiomeProvider extends BiomeProvider {

    private final Biome biome;
    public static final List<Biome> BIOME_LIST = Collections.singletonList(ModBiomes.diveToTheHeart.get());

    protected DiveToTheHeartBiomeProvider() {
        super(Collections.singleton(ModBiomes.diveToTheHeart.get()));
        biome = ModBiomes.diveToTheHeart.get();
    }

    @Override
    public List<Biome> getBiomesToSpawnIn() {
        return BIOME_LIST;
    }

    @Override
    public Set<Biome> getBiomes(int xIn, int yIn, int zIn, int radius) {
        return biomes;
    }

    @Override
    public Biome getNoiseBiome(int x, int y, int z) {
        return biome;
    }
}
