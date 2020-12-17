package online.kingdomkeys.kingdomkeys.world.dimension;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraft.world.biome.provider.BiomeProvider;
import net.minecraft.world.chunk.IChunk;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.GenerationSettings;
import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.gen.WorldGenRegion;
import online.kingdomkeys.kingdomkeys.block.ModBlocks;
import online.kingdomkeys.kingdomkeys.block.MosaicStainedGlassBlock;
import online.kingdomkeys.kingdomkeys.block.SoAPlatformCoreBlock;
import online.kingdomkeys.kingdomkeys.entity.block.PedestalTileEntity;
import online.kingdomkeys.kingdomkeys.entity.block.SoAPlatformTileEntity;

public class DiveToTheHeartChunkGenerator extends ChunkGenerator<DiveToTheHeartChunkGenerator.Config> {

    private static final BlockPos SPAWN_POS = new BlockPos(0, 23, 0);
    private static final ChunkPos SPAWN_CHUNK_POS = new ChunkPos(SPAWN_POS);

    //x
    int width = 16;
    //y
    int height = 25;
    //z
    int depth = 16;

    String topOfPlatform =
                    "0000000000000000" +
                    "0000000300000000" +
                    "0000000000000000" +
                    "0000000000000000" +
                    "0000000000000000" +
                    "0000000000000000" +
                    "0000000000000000" +
                    "0000000000000000" +
                    "0000000000000000" +
                    "0000000000000000" +
                    "0000000000000000" +
                    "0004000000005000" +
                    "0000000000000000" +
                    "0000000000000000" +
                    "0000000000000000" +
                    "0000000000000000";

    String structureTop =
                    "0000001111000000" +
                    "0000111111110000" +
                    "0001111111111000" +
                    "0011111111111100" +
                    "0111111111111110" +
                    "0111111111111110" +
                    "1111111111111111" +
                    "1111111111111111" +
                    "1111111121111111" +
                    "1111111111111111" +
                    "0111111111111110" +
                    "0111111111111110" +
                    "0011111111111100" +
                    "0001111111111000" +
                    "0000111111110000" +
                    "0000001111000000";

    String structureMiddle =
                    "0000001111000000" +
                    "0000110000110000" +
                    "0001000000001000" +
                    "0010000000000100" +
                    "0100000000000010" +
                    "0100000000000010" +
                    "1000000000000001" +
                    "1000000000000001" +
                    "1000000000000001" +
                    "1000000000000001" +
                    "0100000000000010" +
                    "0100000000000010" +
                    "0010000000000100" +
                    "0001000000001000" +
                    "0000110000110000" +
                    "0000001111000000";

    String structureBottom =
                    "0000001111000000" +
                    "0000111111110000" +
                    "0001111111111000" +
                    "0011111111111100" +
                    "0111111111111110" +
                    "0111111111111110" +
                    "1111111111111111" +
                    "1111111111111111" +
                    "1111111111111111" +
                    "1111111111111111" +
                    "0111111111111110" +
                    "0111111111111110" +
                    "0011111111111100" +
                    "0001111111111000" +
                    "0000111111110000" +
                    "0000001111000000";

    public DiveToTheHeartChunkGenerator(IWorld worldIn, BiomeProvider biomeProviderIn) {
        super(worldIn, biomeProviderIn, Config.createDefault());
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
    public void makeBase(IWorld worldIn, IChunk chunkIn) {
        BlockPos.Mutable blockpos$mutable = new BlockPos.Mutable();
        if (distance(chunkIn.getPos().x, chunkIn.getPos().z, SPAWN_CHUNK_POS.x, SPAWN_CHUNK_POS.z) < 1) {
            int startZ = chunkIn.getPos().getZStart() - 8;
            int startX = chunkIn.getPos().getXStart() - 8;
            for (int y = 0; y < height; ++y) {
                for (int z = startZ; z <= chunkIn.getPos().getZEnd() - 8; ++z) {
                    for (int x = startX; x <= chunkIn.getPos().getXEnd() - 8; ++x) {
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
                createPedestal(world, pos, new ItemStack(Items.DIAMOND_SWORD));
                break;
            case '4':
                createPedestal(world, pos, new ItemStack(Items.SHIELD));
                break;
            case '5':
                createPedestal(world, pos, new ItemStack(Items.STICK));
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

    public static class Config extends GenerationSettings {

        public static Config createDefault() {
            Config config = new Config();
            config.setDefaultBlock(Blocks.AIR.getDefaultState());
            config.setDefaultBlock(Blocks.AIR.getDefaultState());
            return config;
        }

    }

}
