package online.kingdomkeys.kingdomkeys.world.dimension.station_of_sorrow;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.resources.RegistryOps;
import net.minecraft.world.level.*;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeManager;
import net.minecraft.world.level.biome.Climate;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.world.level.biome.BiomeSource;
import net.minecraft.world.level.border.WorldBorder;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.DensityFunctions;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.server.level.WorldGenRegion;
import net.minecraft.world.level.levelgen.blending.Blender;
import net.minecraft.world.level.levelgen.structure.StructureSet;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.world.dimension.dive_to_the_heart.DiveToTheHeartBiomeProvider;
import online.kingdomkeys.kingdomkeys.world.dimension.dive_to_the_heart.DiveToTheHeartChunkGenerator;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

public class StationOfSorrowChunkGenerator extends ChunkGenerator {

    public static void registerChunkGenerator() {
		Registry.register(Registry.CHUNK_GENERATOR, new ResourceLocation(KingdomKeys.MODID, "station_of_sorrow_generator"), StationOfSorrowChunkGenerator.CODEC);
	}

    public static final Codec<StationOfSorrowChunkGenerator> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    RegistryOps.retrieveRegistry(Registry.STRUCTURE_SET_REGISTRY).forGetter(StationOfSorrowChunkGenerator::getStructureSetRegistry),
                    RegistryOps.retrieveRegistry(Registry.BIOME_REGISTRY).forGetter(StationOfSorrowChunkGenerator::getBiomeRegistry)
            ).apply(instance, StationOfSorrowChunkGenerator::new));

    public Registry<Biome> getBiomeRegistry() {
        return ((StationOfSorrowBiomeProvider)biomeSource).getBiomeRegistry();
    }

    public Registry<StructureSet> getStructureSetRegistry() {
        return structureSets;
    }

   // private static final BlockPos SPAWN_POS = new BlockPos(0, 25, 0);
   // private static final ChunkPos SPAWN_CHUNK_POS = new ChunkPos(SPAWN_POS);

    //x
    int width = 25;
    //y
    int baseY = 25;
    //z
    int depth = 25;
    
    int colHeight = 6;

    String structureTop =
    		"0000000000111110000000000" +
    		"0000000011441441100000000" +
            "0030001114444444111000300" +
            "0000111114444444111110000" +
            "0001414111414141114141000" +
            "0001141111114111111411000" +
            "0011414114414144114141100" +
            "0011111144444444411111100" +
            "0111111414414144141111110" +
            "0144114441144411444114410" +
            "1444414441144411444144441" +
            "1444111414414144141114441" +
            "1144444444442444444444411" +
            "1444111414414144141114441" +
            "1444414441144411444144441" +
            "0144114441144411444114410" +
            "0111111414414144141111110" +
            "0011111144444444411111100" +
            "0011414114414144114141100" +
            "0001141111114111111411000" +
            "0001414111414141114141000" +
            "0000111114444444111110000" +
            "0030001114444444111000300" +
            "0000000011441441100000000" +
    		"0000000000111110000000000";

    public StationOfSorrowChunkGenerator(Registry<StructureSet> structureSetRegistry, Registry<Biome> registry) {
        super(structureSetRegistry, Optional.empty(), new DiveToTheHeartBiomeProvider(registry));
    }
    
    @Override
    protected Codec<? extends ChunkGenerator> codec() {
        return CODEC;
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public ChunkGenerator withSeed(long seed) {
        return new StationOfSorrowChunkGenerator(getStructureSetRegistry(), getBiomeRegistry());
    }

    @Override
    public Climate.Sampler climateSampler() {
        return new Climate.Sampler(DensityFunctions.constant(0.0), DensityFunctions.constant(0.0), DensityFunctions.constant(0.0), DensityFunctions.constant(0.0), DensityFunctions.constant(0.0), DensityFunctions.constant(0.0), Collections.emptyList());
    }

    @Override
    public void applyCarvers(WorldGenRegion pLevel, long pSeed, BiomeManager pBiomeManager, StructureFeatureManager pStructureFeatureManager, ChunkAccess pChunk, GenerationStep.Carving pStep) {

    }

    @Override
    public void buildSurface(WorldGenRegion pLevel, StructureFeatureManager pStructureFeatureManager, ChunkAccess pChunk) {

    }

    @Override
    public void spawnOriginalMobs(WorldGenRegion pLevel) {

    }

    @Override
    public int getGenDepth() {
        return 0;
    }

    @Override
    public CompletableFuture<ChunkAccess> fillFromNoise(Executor p_187748_, Blender p_187749_, StructureFeatureManager p_187750_, ChunkAccess p_187751_) {
        /* BlockPos.Mutable blockpos$mutable = new BlockPos.Mutable();
        if (distance(chunkIn.getPos().x, chunkIn.getPos().z, SPAWN_CHUNK_POS.x, SPAWN_CHUNK_POS.z) < 1) {
            int startZ = chunkIn.getPos().getZStart() - (depth/2);
            int startX = chunkIn.getPos().getXStart() - (width/2);
            for (int y = 0; y < baseY; ++y) {
                for (int z = startZ; z <= chunkIn.getPos().getZStart() + depth/2; ++z) {
                    for (int x = startX; x <= chunkIn.getPos().getXStart() + width/2; ++x) {
                        blockpos$mutable.setPos(x, SPAWN_POS.getY() - y, z);
                        int strucX = x - startX;
                        int strucZ = z - startZ;
                        if (y == 1) {
                            stateToPlace(structureTop.charAt(strucX + strucZ * width), worldIn, blockpos$mutable);
                        }
                    }
                }
            }
        }*/
        return CompletableFuture.completedFuture(p_187751_);
    }

    @Override
    public int getSeaLevel() {
        return 0;
    }

    @Override
    public int getMinY() {
        return 0;
    }

    @Override
    public int getBaseHeight(int pX, int pZ, Heightmap.Types pType, LevelHeightAccessor pLevel) {
        return 0;
    }

    @Override
    public NoiseColumn getBaseColumn(int pX, int pZ, LevelHeightAccessor pLevel) {
        return null;
    }

    @Override
    public void addDebugScreenInfo(List<String> p_208054_, BlockPos p_208055_) {

    }

    /**
     * Returns the Manhattan distance between the two points.
     */
    private static int distance(int firstX, int firstZ, int secondX, int secondZ) {
        return Math.max(Math.abs(firstX - secondX), Math.abs(firstZ - secondZ));
    }

    private void stateToPlace(char c, LevelAccessor world, BlockPos.MutableBlockPos pos) {
        switch (c) {
            case '0':
                return;
            case '1':
                world.setBlock(pos, Blocks.QUARTZ_BLOCK.defaultBlockState(),2);
                break;
            case '2':
                world.setBlock(pos, Blocks.QUARTZ_BRICKS.defaultBlockState(), 2);
                break;
            case '3':
            	for(int i=-1; i < colHeight; i++) {
            		world.setBlock(pos, Blocks.QUARTZ_PILLAR.defaultBlockState(), 2);
            		pos.setY(pos.getY()+1);
            	}
                break;
            case '4':
                world.setBlock(pos, Blocks.LIGHT_GRAY_CONCRETE.defaultBlockState(), 2);
                break;
        }
    }
}
