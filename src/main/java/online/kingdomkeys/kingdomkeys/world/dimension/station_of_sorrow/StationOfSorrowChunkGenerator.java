package online.kingdomkeys.kingdomkeys.world.dimension.station_of_sorrow;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.resources.RegistryOps;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.WorldGenRegion;
import net.minecraft.world.level.*;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeManager;
import net.minecraft.world.level.biome.Climate;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.DensityFunctions;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.RandomState;
import net.minecraft.world.level.levelgen.blending.Blender;
import net.minecraft.world.level.levelgen.structure.StructureSet;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import online.kingdomkeys.kingdomkeys.KingdomKeys;

public class StationOfSorrowChunkGenerator extends ChunkGenerator {
	
	public StationOfSorrowChunkGenerator(Registry<StructureSet> structureSetRegistry, Registry<Biome> registry) {
		super(structureSetRegistry, Optional.empty(), new StationOfSorrowBiomeProvider(registry));
	}

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

    private static final BlockPos SPAWN_POS = new BlockPos(0, 25, 0);

    //x
    int width = 25;
    //y
    int baseY = 25;
    //z
    int depth = 25;
    
    int colHeight = 6;

    int leftXSize = 12;
    int rightXSize = 13;
    int topZSize = 12;
    int bottomZSize = 13;

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

   
    @Override
    protected Codec<? extends ChunkGenerator> codec() {
        return CODEC;
    }

    @Override
    public void applyCarvers(WorldGenRegion pLevel, long pSeed, RandomState pRandom, BiomeManager pBiomeManager, StructureManager pStructureManager, ChunkAccess pChunk, GenerationStep.Carving pStep) {

    }

    enum Corner { TL, TR, BL, BR }

    @Override
    public void buildSurface(WorldGenRegion pLevel, StructureManager pStructureManager, RandomState pRandom, ChunkAccess pChunk) {
        BlockPos.MutableBlockPos blockpos$mutable = new BlockPos.MutableBlockPos();
        ChunkPos cPos = pChunk.getPos();
        int xOffset = 0;
        int zOffset = 0;
        int startZ;
        int startX;
        //Bottom right
        if (cPos.equals(new ChunkPos(0, 0))) {
            startZ = cPos.getMinBlockZ() + zOffset;
            startX = cPos.getMinBlockX() + xOffset;
            generateCorner(pLevel, cPos, blockpos$mutable, startX, startZ, rightXSize, bottomZSize, Corner.BR);
        }
        //Bottom left
        if (cPos.equals(new ChunkPos(-1, 0))) {
            xOffset = 4;
            startZ = cPos.getMinBlockZ() + zOffset;
            startX = cPos.getMinBlockX() + xOffset;
            generateCorner(pLevel, cPos, blockpos$mutable, startX, startZ, leftXSize, bottomZSize, Corner.BL);
        }
        //Top right
        if (cPos.equals(new ChunkPos(0, -1))) {
            zOffset = 4;
            startZ = cPos.getMinBlockZ() + zOffset;
            startX = cPos.getMinBlockX() + xOffset;
            generateCorner(pLevel, cPos, blockpos$mutable, startX, startZ, rightXSize, topZSize, Corner.TR);
        }
        //Top left
        if (cPos.equals(new ChunkPos(-1, -1))) {
            zOffset = 4;
            xOffset = 4;
            startZ = cPos.getMinBlockZ() + zOffset;
            startX = cPos.getMinBlockX() + xOffset;
            generateCorner(pLevel, cPos, blockpos$mutable, startX, startZ, leftXSize, topZSize, Corner.TL);
        }
    }

    public void generateCorner(WorldGenRegion level, ChunkPos cPos, BlockPos.MutableBlockPos pos, int startX, int startZ, int xSize, int zSize, Corner corner) {
        for (int y = 0; y < baseY; ++y) {
            for (int z = 0; z < zSize; ++z) {
                for (int x = 0; x < xSize; ++x) {
                    pos.set(x + startX, SPAWN_POS.getY() - y, z + startZ);
                    int strucX = x;
                    int strucZ = z;
                    switch (corner) {
                        case BL:
                            strucZ += 12;
                            break;
                        case BR:
                            strucX += 12;
                            strucZ += 12;
                            break;
                        case TL:
                            //no change
                            break;
                        case TR:
                            strucX += 12;
                            break;
                    }
                    if (y == 1) {
                        stateToPlace(structureTop.charAt(strucX + (strucZ * width)), level, pos);
                    }
                }
            }
        }
    }

    @Override
    public void spawnOriginalMobs(WorldGenRegion pLevel) {

    }

    @Override
    public int getGenDepth() {
        return 0;
    }

    @Override
    public CompletableFuture<ChunkAccess> fillFromNoise(Executor pExecutor, Blender pBlender, RandomState pRandom, StructureManager pStructureManager, ChunkAccess pChunk) {
        return CompletableFuture.completedFuture(pChunk);
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
    public int getBaseHeight(int pX, int pZ, Heightmap.Types pType, LevelHeightAccessor pLevel, RandomState pRandom) {
        return 0;
    }

    @Override
    public NoiseColumn getBaseColumn(int pX, int pZ, LevelHeightAccessor pHeight, RandomState pRandom) {
        return new NoiseColumn(0, new BlockState[0]);
    }

    @Override
    public void addDebugScreenInfo(List<String> pInfo, RandomState pRandom, BlockPos pPos) {

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
