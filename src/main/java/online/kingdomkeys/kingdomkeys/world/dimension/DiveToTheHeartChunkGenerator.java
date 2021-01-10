package online.kingdomkeys.kingdomkeys.world.dimension;

import com.mojang.serialization.Codec;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraft.world.biome.provider.BiomeProvider;
import net.minecraft.world.chunk.IChunk;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.gen.WorldGenRegion;
import net.minecraft.world.gen.feature.structure.StructureManager;
import net.minecraft.world.gen.settings.DimensionStructuresSettings;
import online.kingdomkeys.kingdomkeys.block.ModBlocks;
import online.kingdomkeys.kingdomkeys.block.MosaicStainedGlassBlock;
import online.kingdomkeys.kingdomkeys.block.SoAPlatformCoreBlock;
import online.kingdomkeys.kingdomkeys.entity.block.PedestalTileEntity;
import online.kingdomkeys.kingdomkeys.entity.block.SoAPlatformTileEntity;
import online.kingdomkeys.kingdomkeys.item.ModItems;

public class DiveToTheHeartChunkGenerator extends ChunkGenerator {

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

    public DiveToTheHeartChunkGenerator(BiomeProvider p_i231888_1_, DimensionStructuresSettings p_i231888_2_) {
        super(p_i231888_1_, p_i231888_2_);
    }

    public DiveToTheHeartChunkGenerator(BiomeProvider p_i231887_1_, BiomeProvider p_i231887_2_, DimensionStructuresSettings p_i231887_3_, long p_i231887_4_) {
        super(p_i231887_1_, p_i231887_2_, p_i231887_3_, p_i231887_4_);
    }

    @Override
    protected Codec<? extends ChunkGenerator> func_230347_a_() {
        return null;
    }

    @Override
    public ChunkGenerator func_230349_a_(long p_230349_1_) {
        return null;
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
