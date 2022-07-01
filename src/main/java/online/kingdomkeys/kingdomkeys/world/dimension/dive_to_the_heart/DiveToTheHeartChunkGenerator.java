package online.kingdomkeys.kingdomkeys.world.dimension.dive_to_the_heart;

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
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.LevelHeightAccessor;
import net.minecraft.world.level.NoiseColumn;
import net.minecraft.world.level.StructureFeatureManager;
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
import net.minecraft.world.level.levelgen.blending.Blender;
import net.minecraft.world.level.levelgen.structure.StructureSet;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.block.ModBlocks;
import online.kingdomkeys.kingdomkeys.block.MosaicStainedGlassBlock;
import online.kingdomkeys.kingdomkeys.block.SoAPlatformCoreBlock;
import online.kingdomkeys.kingdomkeys.entity.block.PedestalTileEntity;
import online.kingdomkeys.kingdomkeys.entity.block.SoAPlatformTileEntity;
import online.kingdomkeys.kingdomkeys.item.ModItems;

public class DiveToTheHeartChunkGenerator extends ChunkGenerator {

    public DiveToTheHeartChunkGenerator(Registry<StructureSet> structureSetRegistry, Registry<Biome> registry) {
        super(structureSetRegistry, Optional.empty(), new DiveToTheHeartBiomeProvider(registry));
    }

    public static void registerChunkGenerator() {
		Registry.register(Registry.CHUNK_GENERATOR, new ResourceLocation(KingdomKeys.MODID, "dive_to_the_heart_generator"), DiveToTheHeartChunkGenerator.CODEC);
	}

	public static final Codec<DiveToTheHeartChunkGenerator> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    RegistryOps.retrieveRegistry(Registry.STRUCTURE_SET_REGISTRY).forGetter(DiveToTheHeartChunkGenerator::getStructureSetRegistry),
                    RegistryOps.retrieveRegistry(Registry.BIOME_REGISTRY).forGetter(DiveToTheHeartChunkGenerator::getBiomeRegistry)
            ).apply(instance, DiveToTheHeartChunkGenerator::new));

    public Registry<Biome> getBiomeRegistry() {
        return ((DiveToTheHeartBiomeProvider)biomeSource).getBiomeRegistry();
    }

    public Registry<StructureSet> getStructureSetRegistry() {
        return structureSets;
    }

    private static final BlockPos SPAWN_POS = new BlockPos(0, 25, 0);

    //x
    int width = 17;
    //y
    int height = 25;
    //z
    int depth = 17;

    int leftXSize = 8;
    int rightXSize = 9;
    int topZSize = 8;
    int bottomZSize = 9;


    String topOfPlatform =
			"00000000000000000" +
			"00000000300000000" +
            "00000000000000000" +
            "00000000000000000" +
            "00000000000000000" +
            "00000000000000000" +
            "00000000000000000" +
            "00000000000000000" +
            "00000000000000000" +
            "00000000000000000" +
            "00000000000000000" +
            "00400000000000500" +
            "00000000000000000" +
            "00000000000000000" +
            "00000000000000000" +
            "00000000000000000" +
            "00000000000000000";

    String structureTop =
            "00000111111100000" +
            "00011111111111000" +
            "00111111111111100" +
            "01111111111111110" +
            "01111111111111110" +
            "11111111111111111" +
            "11111111111111111" +
            "11111111111111111" +
            "11111111211111111" +
            "11111111111111111" +
            "11111111111111111" +
            "11111111111111111" +
            "01111111111111110" +
            "01111111111111110" +
            "00111111111111100" +
            "00011111111111000" +
            "00000111111100000";

    String structureMiddle =
            "00000111111100000" +
            "00011000000011000" +
            "00100000000000100" +
            "01000000000000010" +
            "01000000000000010" +
            "10000000000000001" +
            "10000000000000001" +
            "10000000000000001" +
            "10000000000000001" +
            "10000000000000001" +
            "10000000000000001" +
            "10000000000000001" +
            "01000000000000010" +
            "01000000000000010" +
            "00100000000000100" +
            "00011000000011000" +
            "00000111111100000";

    String structureBottom =
            "00000111111100000" +
            "00011111111111000" +
            "00111111111111100" +
            "01111111111111110" +
            "01111111111111110" +
            "11111111111111111" +
            "11111111111111111" +
            "11111111111111111" +
            "11111111111111111" +
            "11111111111111111" +
            "11111111111111111" +
            "11111111111111111" +
            "01111111111111110" +
            "01111111111111110" +
            "00111111111111100" +
            "00011111111111000" +
            "00000111111100000";

    
    @Override
    protected Codec<? extends ChunkGenerator> codec() {
        return CODEC;
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public ChunkGenerator withSeed(long seed) {
        return new DiveToTheHeartChunkGenerator(getStructureSetRegistry(), getBiomeRegistry());
    }

    @Override
    public Climate.Sampler climateSampler() {
        return new Climate.Sampler(DensityFunctions.constant(0.0), DensityFunctions.constant(0.0), DensityFunctions.constant(0.0), DensityFunctions.constant(0.0), DensityFunctions.constant(0.0), DensityFunctions.constant(0.0), Collections.emptyList());
    }

    @Override
    public void applyCarvers(WorldGenRegion pLevel, long pSeed, BiomeManager pBiomeManager, StructureFeatureManager pStructureFeatureManager, ChunkAccess pChunk, GenerationStep.Carving pStep) { }

    @Override
    public void spawnOriginalMobs(WorldGenRegion pLevel) { }

    @Override
    public int getGenDepth() {
        return 0;
    }

    enum Corner { TL, TR, BL, BR }

    @Override
    public void buildSurface(WorldGenRegion pLevel, StructureFeatureManager pStructureFeatureManager, ChunkAccess chunkIn) {
        BlockPos.MutableBlockPos blockpos$mutable = new BlockPos.MutableBlockPos();
        ChunkPos cPos = chunkIn.getPos();
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
            xOffset = 8;
            startZ = cPos.getMinBlockZ() + zOffset;
            startX = cPos.getMinBlockX() + xOffset;
            generateCorner(pLevel, cPos, blockpos$mutable, startX, startZ, leftXSize, bottomZSize, Corner.BL);
        }
        //Top right
        if (cPos.equals(new ChunkPos(0, -1))) {
            zOffset = 8;
            startZ = cPos.getMinBlockZ() + zOffset;
            startX = cPos.getMinBlockX() + xOffset;
            generateCorner(pLevel, cPos, blockpos$mutable, startX, startZ, rightXSize, topZSize, Corner.TR);
        }
        //Top left
        if (cPos.equals(new ChunkPos(-1, -1))) {
            zOffset = 8;
            xOffset = 8;
            startZ = cPos.getMinBlockZ() + zOffset;
            startX = cPos.getMinBlockX() + xOffset;
            generateCorner(pLevel, cPos, blockpos$mutable, startX, startZ, leftXSize, topZSize, Corner.TL);
        }
    }

    public void generateCorner(WorldGenRegion level, ChunkPos cPos, BlockPos.MutableBlockPos pos, int startX, int startZ, int xSize, int zSize, Corner corner) {
        for (int y = 0; y < height; ++y) {
            for (int z = 0; z < zSize; ++z) {
                for (int x = 0; x < xSize; ++x) {
                    pos.set(x + startX, SPAWN_POS.getY() - y, z + startZ);
                    int strucX = x;
                    int strucZ = z;
                    switch (corner) {
                        case BL:
                            strucZ += 8;
                            break;
                        case BR:
                            strucX += 8;
                            strucZ += 8;
                            break;
                        case TL:
                            //no change
                            break;
                        case TR:
                            strucX += 8;
                            break;
                    }
                    if (y == 0) {
                        stateToPlace(topOfPlatform.charAt(strucX + (strucZ * width)), level, pos);
                    } else if (y == 1) {
                        stateToPlace(structureTop.charAt(strucX + (strucZ * width)), level, pos);
                    } else if (y == height - 1) {
                        stateToPlace(structureBottom.charAt(strucX + (strucZ * width)), level, pos);
                    } else {
                        stateToPlace(structureMiddle.charAt(strucX + (strucZ * width)), level, pos);
                    }
                }
            }
        }
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
    public NoiseColumn getBaseColumn(int x, int z, LevelHeightAccessor levelHeightAccessor) {
        return new NoiseColumn(0, new BlockState[0]);
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

    private void stateToPlace(char c, WorldGenRegion pLevel, BlockPos.MutableBlockPos pos) {
    	 switch (c) {
         case '0':
             return;
         case '1':
        	 pLevel.setBlock(pos, ModBlocks.mosaic_stained_glass.get().defaultBlockState().setValue(MosaicStainedGlassBlock.STRUCTURE, true), 2);
             break;
         case '2':
        	 pLevel.setBlock(pos, ModBlocks.station_of_awakening_core.get().defaultBlockState().setValue(SoAPlatformCoreBlock.STRUCTURE, true), 2);
             ((SoAPlatformTileEntity) pLevel.getBlockEntity(pos)).setMultiblockFormed(true);
             break;
         case '3':
             createPedestal(pLevel, pos, new ItemStack(ModItems.dreamSword.get()));
             break;
         case '4':
             createPedestal(pLevel, pos, new ItemStack(ModItems.dreamShield.get()));
             break;
         case '5':
             createPedestal(pLevel, pos, new ItemStack(ModItems.dreamStaff.get()));
             break;
     }
    }

    private void createPedestal(WorldGenRegion pLevel, BlockPos.MutableBlockPos pos, ItemStack toDisplay) {
    	pLevel.setBlock(pos, ModBlocks.pedestal.get().defaultBlockState(), 2);
        PedestalTileEntity te = ((PedestalTileEntity) pLevel.getBlockEntity(pos));
        te.setStationOfAwakeningMarker(true);
        te.setDisplayStack(toDisplay);
    }

	@Override
	public CompletableFuture<ChunkAccess> fillFromNoise(Executor p_187748_, Blender p_187749_, StructureFeatureManager p_187750_, ChunkAccess chunkIn) {
		return CompletableFuture.completedFuture(chunkIn);
	}
    
}
