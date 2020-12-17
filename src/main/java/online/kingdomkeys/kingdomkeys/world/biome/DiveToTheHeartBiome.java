package online.kingdomkeys.kingdomkeys.world.biome;

import net.minecraft.block.BlockState;
import net.minecraft.entity.EntityClassification;
import net.minecraft.util.SharedSeedRandom;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.DefaultBiomeFeatures;
import net.minecraft.world.chunk.IChunk;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.GenerationSettings;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.IFeatureConfig;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraft.world.gen.placement.IPlacementConfig;
import net.minecraft.world.gen.placement.Placement;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import online.kingdomkeys.kingdomkeys.world.features.ModFeatures;

import java.util.Collections;
import java.util.List;
import java.util.Random;

public class DiveToTheHeartBiome extends Biome {
    protected DiveToTheHeartBiome(Builder biomeBuilder) {
        super(biomeBuilder);
    }

    @Override
    protected void addSpawn(EntityClassification type, SpawnListEntry spawnListEntry) { }

    @Override
    public List<SpawnListEntry> getSpawns(EntityClassification creatureType) {
        return Collections.emptyList();
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public int getSkyColor() {
        return 0;
    }

}
