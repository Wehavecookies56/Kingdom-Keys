package online.kingdomkeys.kingdomkeys.world.dimension;

import java.util.stream.IntStream;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.EntityType;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SharedSeedRandom;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.biome.MobSpawnInfo;
import net.minecraft.world.biome.provider.BiomeProvider;
import net.minecraft.world.chunk.IChunk;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.gen.INoiseGenerator;
import net.minecraft.world.gen.OctavesNoiseGenerator;
import net.minecraft.world.gen.PerlinNoiseGenerator;
import net.minecraft.world.gen.WorldGenRegion;
import net.minecraft.world.gen.feature.structure.StructureManager;
import net.minecraft.world.gen.settings.DimensionStructuresSettings;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.block.ModBlocks;
import online.kingdomkeys.kingdomkeys.block.MosaicStainedGlassBlock;
import online.kingdomkeys.kingdomkeys.block.SoAPlatformCoreBlock;
import online.kingdomkeys.kingdomkeys.entity.block.PedestalTileEntity;
import online.kingdomkeys.kingdomkeys.entity.block.SoAPlatformTileEntity;
import online.kingdomkeys.kingdomkeys.item.ModItems;

public class DTTHChunkGenerator extends ChunkGenerator {

	public static void registerChunkGenerator() {
		Registry.register(Registry.CHUNK_GENERATOR_CODEC, new ResourceLocation(KingdomKeys.MODID, "chunk_generator"), DTTHChunkGenerator.CODEC);
	}

	public static final Codec<DTTHChunkGenerator> CODEC = RecordCodecBuilder.create((instance) -> instance.group(BiomeProvider.CODEC.fieldOf("biome_source").forGetter((surfaceChunkGenerator) -> surfaceChunkGenerator.biomeProvider), DimensionStructuresSettings.field_236190_a_.fieldOf("structures").forGetter((ChunkGenerator::func_235957_b_))).apply(instance, instance.stable(DTTHChunkGenerator::new)));
	    
    private static final MobSpawnInfo.Spawners INITIAL_BEE_ENTRY = new MobSpawnInfo.Spawners(EntityType.BEE, 1, 4, 4);
    private static final BlockState CAVE_AIR = Blocks.CAVE_AIR.getDefaultState();
    protected final BlockState defaultBlock;
    protected final BlockState defaultFluid;
    private final int verticalNoiseResolution;
    private final int horizontalNoiseResolution;
    private final int noiseSizeX;
    private final int noiseSizeY;
    private final int noiseSizeZ;
    protected final SharedSeedRandom random;
    private final OctavesNoiseGenerator lowerInterpolatedNoise;
    private final OctavesNoiseGenerator upperInterpolatedNoise;
    private final OctavesNoiseGenerator interpolationNoise;
    private final INoiseGenerator surfaceDepthNoise;
    private final OctavesNoiseGenerator field_24776;
    private final DimensionStructuresSettings dimensionStructuresSettings;
    private final int height2;
    
    private static final BlockPos SPAWN_POS = new BlockPos(0, 25, 0);
    private static final ChunkPos SPAWN_CHUNK_POS = new ChunkPos(SPAWN_POS);

    //x
    int width = 17;
    //y
    int height = 25;
    //z
    int depth = 17;

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
            "11000000000000011" +
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

    public DTTHChunkGenerator(BiomeProvider biomeSource, DimensionStructuresSettings dimensionStructuresSettings) {
        this(biomeSource, biomeSource, dimensionStructuresSettings);
    }

    private DTTHChunkGenerator(BiomeProvider biomeSource, BiomeProvider biomeSource2, DimensionStructuresSettings dimensionStructuresSettings) {
        super(biomeSource, biomeSource2, dimensionStructuresSettings, 0);

        // we need the seed passed into here
        this.random = new SharedSeedRandom(0);

        this.dimensionStructuresSettings = dimensionStructuresSettings;
        this.height2 = 256;
        this.verticalNoiseResolution = 8;
        this.horizontalNoiseResolution = 4;
        this.defaultBlock = Blocks.HONEYCOMB_BLOCK.getDefaultState();
        this.defaultFluid = Blocks.WATER.getDefaultState();
        this.noiseSizeX = 16 / this.horizontalNoiseResolution;
        this.noiseSizeY = this.height2 / this.verticalNoiseResolution;
        this.noiseSizeZ = 16 / this.horizontalNoiseResolution;
        this.lowerInterpolatedNoise = new OctavesNoiseGenerator(this.random, IntStream.rangeClosed(-15, 0));
        this.upperInterpolatedNoise = new OctavesNoiseGenerator(this.random, IntStream.rangeClosed(-15, 0));
        this.interpolationNoise = new OctavesNoiseGenerator(this.random, IntStream.rangeClosed(-7, 0));
        this.surfaceDepthNoise = new PerlinNoiseGenerator(this.random, IntStream.rangeClosed(-3, 0));
        this.random.skip(2620);
        this.field_24776 = new OctavesNoiseGenerator(this.random, IntStream.rangeClosed(-15, 0));
    }
    
    @Override
    protected Codec<? extends ChunkGenerator> func_230347_a_() {
        return CODEC;
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public ChunkGenerator func_230349_a_(long seed) {
        return new DTTHChunkGenerator(this.biomeProvider.getBiomeProvider(seed), dimensionStructuresSettings);
    }

    @Override
    public void generateSurface(WorldGenRegion p_225551_1_, IChunk p_225551_2_) {

    }

    /**
     * Returns the Manhattan distance between the two points.
     */
    private static int distance(int firstX, int firstZ, int secondX, int secondZ) {
        return Math.max(Math.abs(firstX - secondX), Math.abs(firstZ - secondZ));
    }

    @Override
    public int getGroundHeight() {
        return 0;
    }

    @Override
    public void func_230352_b_(IWorld worldIn, StructureManager structureManagerIn, IChunk chunkIn) {
        BlockPos.Mutable blockpos$mutable = new BlockPos.Mutable();
        if (distance(chunkIn.getPos().x, chunkIn.getPos().z, SPAWN_CHUNK_POS.x, SPAWN_CHUNK_POS.z) < 1) {
            int startZ = chunkIn.getPos().getZStart() - (depth/2);
            int startX = chunkIn.getPos().getXStart() - (width/2);

            for (int y = 0; y < height; ++y) {
                for (int z = startZ; z <= chunkIn.getPos().getZStart() + depth/2; ++z) {
                    for (int x = startX; x <= chunkIn.getPos().getXStart() + width/2; ++x) {
                        blockpos$mutable.setPos(x, SPAWN_POS.getY() - y, z);
                        int strucX = x - startX;
                        int strucZ = z - startZ;
                        if (y == 0) {
                            stateToPlace(topOfPlatform.charAt(strucX + strucZ * width), worldIn, blockpos$mutable);
                        } else if (y == 1) {
                            stateToPlace(structureTop.charAt(strucX + strucZ * width), worldIn, blockpos$mutable);
                        } else if (y == height - 1) {
                            stateToPlace(structureBottom.charAt(strucX + strucZ * width), worldIn, blockpos$mutable);
                        } else {
                            stateToPlace(structureMiddle.charAt(strucX + strucZ * width), worldIn, blockpos$mutable);
                        }
                    }
                }
            }
        }
    }

    private void stateToPlace(char c, IWorld world, BlockPos.Mutable pos) {
        switch (c) {
            case '0':
                return;
            case '1':
                world.setBlockState(pos, ModBlocks.mosaic_stained_glass.get().getDefaultState().with(MosaicStainedGlassBlock.STRUCTURE, true), 2);
                break;
            case '2':
                world.setBlockState(pos, ModBlocks.station_of_awakening_core.get().getDefaultState().with(SoAPlatformCoreBlock.STRUCTURE, true), 2);
                ((SoAPlatformTileEntity) world.getTileEntity(pos)).setMultiblockFormed(true);
                break;
            case '3':
                createPedestal(world, pos, new ItemStack(ModItems.dreamSword.get()));
                break;
            case '4':
                createPedestal(world, pos, new ItemStack(ModItems.dreamShield.get()));
                break;
            case '5':
                createPedestal(world, pos, new ItemStack(ModItems.dreamStaff.get()));
                break;
        }
    }

    private void createPedestal(IWorld world, BlockPos.Mutable pos, ItemStack toDisplay) {
        world.setBlockState(pos, ModBlocks.pedestal.get().getDefaultState(), 2);
        PedestalTileEntity te = ((PedestalTileEntity) world.getTileEntity(pos));
        te.setStationOfAwakeningMarker(true);
        te.setDisplayStack(toDisplay);
    }

    @Override
    public int getHeight(int x, int z, Heightmap.Type heightmapType) {
        return 0;
    }

    @Override
    public IBlockReader func_230348_a_(int p_230348_1_, int p_230348_2_) {
        return null;
    }
    
}
