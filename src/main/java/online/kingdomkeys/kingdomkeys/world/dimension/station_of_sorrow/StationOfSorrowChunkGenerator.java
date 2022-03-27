package online.kingdomkeys.kingdomkeys.world.dimension.station_of_sorrow;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.block.Blocks;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.biome.provider.BiomeProvider;
import net.minecraft.world.chunk.IChunk;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.gen.WorldGenRegion;
import net.minecraft.world.gen.feature.structure.StructureManager;
import net.minecraft.world.gen.settings.DimensionStructuresSettings;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import online.kingdomkeys.kingdomkeys.KingdomKeys;

public class StationOfSorrowChunkGenerator extends ChunkGenerator {

    private DimensionStructuresSettings settings;

    public static void registerChunkGenerator() {
		Registry.register(Registry.CHUNK_GENERATOR_CODEC, new ResourceLocation(KingdomKeys.MODID, "station_of_sorrow_generator"), StationOfSorrowChunkGenerator.CODEC);
	}

	public static final Codec<StationOfSorrowChunkGenerator> CODEC = RecordCodecBuilder.create((instance) -> instance.group(BiomeProvider.CODEC.fieldOf("biome_source").forGetter((surfaceChunkGenerator) -> surfaceChunkGenerator.biomeProvider), DimensionStructuresSettings.field_236190_a_.fieldOf("structures").forGetter((ChunkGenerator::func_235957_b_))).apply(instance, instance.stable(StationOfSorrowChunkGenerator::new)));
    
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

    public StationOfSorrowChunkGenerator(BiomeProvider biomeSource, DimensionStructuresSettings dimensionStructuresSettings) {
        this(biomeSource, biomeSource, dimensionStructuresSettings);
    }

    private StationOfSorrowChunkGenerator(BiomeProvider biomeSource, BiomeProvider biomeSource2, DimensionStructuresSettings dimensionStructuresSettings) {
        super(biomeSource, biomeSource2, dimensionStructuresSettings, 0);
    }
    
    @Override
    protected Codec<? extends ChunkGenerator> func_230347_a_() {
        return CODEC;
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public ChunkGenerator func_230349_a_(long seed) {
        return new StationOfSorrowChunkGenerator(this.biomeProvider.getBiomeProvider(seed), this.settings);
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
    }

    private void stateToPlace(char c, IWorld world, BlockPos.Mutable pos) {
        switch (c) {
            case '0':
                return;
            case '1':
                world.setBlockState(pos, Blocks.QUARTZ_BLOCK.getDefaultState(),2);
                break;
            case '2':
                world.setBlockState(pos, Blocks.QUARTZ_BRICKS.getDefaultState(), 2);
                break;
            case '3':
            	for(int i=-1; i < colHeight; i++) {
            		world.setBlockState(pos, Blocks.QUARTZ_PILLAR.getDefaultState(), 2);
            		pos.setY(pos.getY()+1);
            	}
                break;
            case '4':
                world.setBlockState(pos, Blocks.LIGHT_GRAY_CONCRETE.getDefaultState(), 2);
                break;
        }
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
