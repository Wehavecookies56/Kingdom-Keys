package online.kingdomkeys.kingdomkeys.world.dimension.station_of_sorrow;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.WorldGenRegion;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.StructureFeatureManager;
import net.minecraft.world.level.biome.BiomeSource;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.StructureSettings;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import online.kingdomkeys.kingdomkeys.KingdomKeys;

public class StationOfSorrowChunkGenerator extends ChunkGenerator {

    private StructureSettings settings;

    public static void registerChunkGenerator() {
		Registry.register(Registry.CHUNK_GENERATOR, new ResourceLocation(KingdomKeys.MODID, "station_of_sorrow_generator"), StationOfSorrowChunkGenerator.CODEC);
	}

	public static final Codec<StationOfSorrowChunkGenerator> CODEC = RecordCodecBuilder.create((instance) -> instance.group(BiomeSource.CODEC.fieldOf("biome_source").forGetter((surfaceChunkGenerator) -> surfaceChunkGenerator.biomeSource), StructureSettings.CODEC.fieldOf("structures").forGetter((ChunkGenerator::getSettings))).apply(instance, instance.stable(StationOfSorrowChunkGenerator::new)));
    
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

    public StationOfSorrowChunkGenerator(BiomeSource biomeSource, StructureSettings dimensionStructuresSettings) {
        this(biomeSource, biomeSource, dimensionStructuresSettings);
    }

    private StationOfSorrowChunkGenerator(BiomeSource biomeSource, BiomeSource biomeSource2, StructureSettings dimensionStructuresSettings) {
        super(biomeSource, biomeSource2, dimensionStructuresSettings, 0);
    }
    
    @Override
    protected Codec<? extends ChunkGenerator> codec() {
        return CODEC;
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public ChunkGenerator withSeed(long seed) {
        return new StationOfSorrowChunkGenerator(this.biomeSource.withSeed(seed), this.settings);
    }

    @Override
    public void buildSurfaceAndBedrock(WorldGenRegion p_225551_1_, ChunkAccess p_225551_2_) {

    }

    /**
     * Returns the Manhattan distance between the two points.
     */
    private static int distance(int firstX, int firstZ, int secondX, int secondZ) {
        return Math.max(Math.abs(firstX - secondX), Math.abs(firstZ - secondZ));
    }

    @Override
    public int getSpawnHeight() {
        return 0;
    }

    @Override
    public void fillFromNoise(LevelAccessor worldIn, StructureFeatureManager structureManagerIn, ChunkAccess chunkIn) {
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

    @Override
    public int getBaseHeight(int x, int z, Heightmap.Types heightmapType) {
        return 0;
    }

    @Override
    public BlockGetter getBaseColumn(int p_230348_1_, int p_230348_2_) {
        return null;
    }
    
}
