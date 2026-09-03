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
import net.minecraft.world.phys.Vec3;
import online.kingdomkeys.kingdomkeys.block.ModBlocks;
import online.kingdomkeys.kingdomkeys.block.MosaicStainedGlassBlock;
import online.kingdomkeys.kingdomkeys.block.SoAPlatformCoreBlock;
import online.kingdomkeys.kingdomkeys.entity.block.PedestalTileEntity;
import online.kingdomkeys.kingdomkeys.entity.block.SoAPlatformTileEntity;
import online.kingdomkeys.kingdomkeys.item.ModItems;

import java.util.List;
import java.util.concurrent.CompletableFuture;

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

    //x z
    int width = 17;
    //y
    int height = 25;

    public static final int PLATFORM_RADIUS = 8;

    /** The layer the player stands on top of. The masks hang downwards from SPAWN_POS.getY(). */
    public static final int FLOOR_Y = SPAWN_POS.getY() - 1;

    /** Platform of pedestals: where the weapon choice happens, and where it has always been. */
    public static final int PEDESTAL_CX = 0, PEDESTAL_CZ = 0;

    public static final int UNION_CX = 0, UNION_CZ = 48;

    /** Walkway joining the two, running north along the z axis between the two edges. */
    public static final int BRIDGE_HALF_WIDTH = 1;
    public static final int BRIDGE_Z_MIN = PEDESTAL_CZ + PLATFORM_RADIUS + 1;
    public static final int BRIDGE_Z_MAX = UNION_CZ - PLATFORM_RADIUS - 1;

    public static final double UNION_CENTRE_X = UNION_CX + 0.5D;
    public static final double UNION_CENTRE_Z = UNION_CZ + 0.5D;

    public static final double FORETELLERS_RADIUS = 5.0D;

    /**
     * Where the first Foreteller stands, in degrees: 90 is due south, the far end from
     * the walkway, so index 0 faces the player head on as they arrive. Turn this to
     * rotate the whole pentagon.
     */
    public static final int FORETELLERS_START_ANGLE = 90;

    /** Degrees between one Foreteller and the next. Positive runs clockwise seen from above. */
    public static final int FORETELLERS_STEP = 72;

    // "Pentagon" the foretellers form
    public static Vec3 foretellerPos(int index) {
        double angle = Math.toRadians(FORETELLERS_START_ANGLE + index * FORETELLERS_STEP);
        return new Vec3(UNION_CENTRE_X + FORETELLERS_RADIUS * Math.cos(angle), SPAWN_POS.getY(), UNION_CENTRE_Z + FORETELLERS_RADIUS * Math.sin(angle));
    }

    public static BlockPos spawnFor(boolean hasUnion) {
        return hasUnion ? new BlockPos(PEDESTAL_CX, SPAWN_POS.getY(), PEDESTAL_CZ) : new BlockPos(UNION_CX, SPAWN_POS.getY(), UNION_CZ);
    }

    /** True while the player is still stood on the platform they are meant to be on. */
    public static boolean onUnionPlatform(double x, double z) {
        return Math.abs(x - UNION_CX) <= PLATFORM_RADIUS + 1 && Math.abs(z - UNION_CZ) <= PLATFORM_RADIUS + 1;
    }

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

    /** The union platform carries no pedestals: the Foretellers standing on it are entities. */
    String topOfUnionPlatform =
            "00000000000000000" +
            "00000000000000000" +
            "00000000000000000" +
            "00000000000000000" +
            "00000000000000000" +
            "00000000000000000" +
            "00000000000000000" +
            "00000000000000000" +
            "00000000000000000" +
            "00000000000000000" +
            "00000000000000000" +
            "00000000000000000" +
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

    @Override
    public void buildSurface(WorldGenRegion pLevel, StructureManager pStructureManager, RandomState pRandom, ChunkAccess pChunk) {
        BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos();
        ChunkPos cPos = pChunk.getPos();

        generatePlatform(pLevel, cPos, pos, PEDESTAL_CX, PEDESTAL_CZ, topOfPlatform, false);
        generatePlatform(pLevel, cPos, pos, UNION_CX, UNION_CZ, topOfUnionPlatform, true);
        generateBridge(pLevel, cPos, pos);
    }

    public void generatePlatform(WorldGenRegion level, ChunkPos cPos, BlockPos.MutableBlockPos pos, int centreX, int centreZ, String top, boolean fate) {
        int fromX = Math.max(cPos.getMinBlockX(), centreX - PLATFORM_RADIUS);
        int toX = Math.min(cPos.getMaxBlockX(), centreX + PLATFORM_RADIUS);
        int fromZ = Math.max(cPos.getMinBlockZ(), centreZ - PLATFORM_RADIUS);
        int toZ = Math.min(cPos.getMaxBlockZ(), centreZ + PLATFORM_RADIUS);
        if (fromX > toX || fromZ > toZ)
            return;

        for (int y = 0; y < height; ++y) {
            String layer;
            if (y == 0) {
                layer = top;
            } else if (y == 1) {
                layer = structureTop;
            } else if (y == height - 1) {
                layer = structureBottom;
            } else {
                layer = structureMiddle;
            }
            for (int worldZ = fromZ; worldZ <= toZ; ++worldZ) {
                for (int worldX = fromX; worldX <= toX; ++worldX) {
                    pos.set(worldX, SPAWN_POS.getY() - y, worldZ);
                    int maskX = worldX - centreX + PLATFORM_RADIUS;
                    int maskZ = worldZ - centreZ + PLATFORM_RADIUS;
                    stateToPlace(layer.charAt(maskX + (maskZ * width)), level, pos, fate);
                }
            }
        }
    }

    public void generateBridge(WorldGenRegion level, ChunkPos cPos, BlockPos.MutableBlockPos pos) {
        int fromX = Math.max(cPos.getMinBlockX(), UNION_CX - BRIDGE_HALF_WIDTH);
        int toX = Math.min(cPos.getMaxBlockX(), UNION_CX + BRIDGE_HALF_WIDTH);
        int fromZ = Math.max(cPos.getMinBlockZ(), BRIDGE_Z_MIN);
        int toZ = Math.min(cPos.getMaxBlockZ(), BRIDGE_Z_MAX);
        if (fromX > toX || fromZ > toZ)
            return;

        for (int worldZ = fromZ; worldZ <= toZ; ++worldZ) {
            for (int worldX = fromX; worldX <= toX; ++worldX) {
                pos.set(worldX, FLOOR_Y, worldZ);
                stateToPlace('1', level, pos, false);
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

    private void stateToPlace(char c, WorldGenRegion pLevel, BlockPos.MutableBlockPos pos, boolean fate) {
    	 switch (c) {
         case '0':
             return;
         case '1':
        	 pLevel.setBlock(pos, ModBlocks.mosaic_stained_glass.get().defaultBlockState().setValue(MosaicStainedGlassBlock.STRUCTURE, true), 2);
             break;
         case '2':
        	 pLevel.setBlock(pos, ModBlocks.station_of_awakening_core.get().defaultBlockState().setValue(SoAPlatformCoreBlock.STRUCTURE, true), 2);
             SoAPlatformTileEntity core = (SoAPlatformTileEntity) pLevel.getBlockEntity(pos);
             core.setMultiblockFormed(true);
             core.setFate(fate);
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
