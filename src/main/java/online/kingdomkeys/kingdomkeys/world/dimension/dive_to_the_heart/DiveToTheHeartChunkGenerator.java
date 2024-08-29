package online.kingdomkeys.kingdomkeys.world.dimension.dive_to_the_heart;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.WorldGenRegion;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.LevelHeightAccessor;
import net.minecraft.world.level.NoiseColumn;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.biome.BiomeManager;
import net.minecraft.world.level.biome.BiomeSource;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.RandomState;
import net.minecraft.world.level.levelgen.blending.Blender;
import online.kingdomkeys.kingdomkeys.block.ModBlocks;
import online.kingdomkeys.kingdomkeys.block.MosaicStainedGlassBlock;
import online.kingdomkeys.kingdomkeys.block.SoAPlatformCoreBlock;
import online.kingdomkeys.kingdomkeys.entity.block.PedestalTileEntity;
import online.kingdomkeys.kingdomkeys.entity.block.SoAPlatformTileEntity;
import online.kingdomkeys.kingdomkeys.item.ModItems;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

public class DiveToTheHeartChunkGenerator extends ChunkGenerator {

    public DiveToTheHeartChunkGenerator(BiomeSource biomeSource) {
        super(biomeSource);
        this.biomeSource = biomeSource;
    }

    BiomeSource biomeSource;

	public static final MapCodec<DiveToTheHeartChunkGenerator> CODEC = RecordCodecBuilder.mapCodec(instance ->
            instance.group(BiomeSource.CODEC.fieldOf("biome_source").forGetter((inst) -> inst.biomeSource))
                    .apply(instance, instance.stable(DiveToTheHeartChunkGenerator::new)));

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
    protected MapCodec<? extends ChunkGenerator> codec() {
        return CODEC;
    }

    @Override
    public void applyCarvers(WorldGenRegion pLevel, long pSeed, RandomState pRandom, BiomeManager pBiomeManager, StructureManager pStructureManager, ChunkAccess pChunk, GenerationStep.Carving pStep) {

    }

    @Override
    public void spawnOriginalMobs(WorldGenRegion pLevel) { }

    @Override
    public int getGenDepth() {
        return 0;
    }

    @Override
    public CompletableFuture<ChunkAccess> fillFromNoise(Blender blender, RandomState randomState, StructureManager structureManager, ChunkAccess chunk) {
        return CompletableFuture.completedFuture(chunk);
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
    
}
