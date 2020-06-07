package online.kingdomkeys.kingdomkeys.world;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.ListNBT;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.tileentity.ChestTileEntity;
import net.minecraft.tileentity.StructureBlockTileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.util.Constants;
import online.kingdomkeys.kingdomkeys.KingdomKeys;

public class WorldLoader {

    public WorldLoader() {

    }

    public void processAndGenerateStructureFile(String file, ServerWorld world, BlockPos offset) {
        try {
            InputStream inputStream = getClass().getResourceAsStream("/assets/"+KingdomKeys.MODID+"/worlds/" + file + ".world");
            CompoundNBT main = CompressedStreamTools.readCompressed(inputStream);

            ListNBT palette = main.getList("palette", Constants.NBT.TAG_COMPOUND);

            ListNBT blocks = main.getList("blocks", Constants.NBT.TAG_COMPOUND);

            List<BlockState> blockStates = new ArrayList<>();

            //System.out.println("Generating World with " + blocks.tagCount() + " blocks");
            CompoundNBT firstBlock = blocks.getCompound(0);
            BlockPos firstPos = new BlockPos(firstBlock.getList("pos", 3).getInt(0), firstBlock.getList("pos", 3).getInt(1), firstBlock.getList("pos", 3).getInt(2));
            //System.out.println("Starting with position " + firstPos.getX()+offset.getX() + " " + firstPos.getY()+offset.getY() + " " + firstPos.getZ()+offset.getZ());

            for (int i = 0; i < palette.size(); i++) {
                CompoundNBT block = palette.getCompound(i);
                blockStates.add(NBTUtil.readBlockState(block));
            }

            for (int i = 0; i < blocks.size(); i++) {
                CompoundNBT block = blocks.getCompound(i);
                BlockPos blockpos = new BlockPos(block.getList("pos", 3).getInt(0)+offset.getX(), block.getList("pos", 3).getInt(1)+offset.getY(), block.getList("pos", 3).getInt(2)+offset.getZ());
                BlockState state = blockStates.get(block.getInt("state"));
                if (block.contains("nbt")) {
                    CompoundNBT nbtData = block.getCompound("nbt");
                    StructureBlockTileEntity.create(nbtData);
                }
                if (state.getBlock() == Blocks.OAK_DOOR) {
                    if (world.getBlockState(blockpos.down()).getBlock() == Blocks.AIR) {
                        world.setBlockState(blockpos.down(), Blocks.DIRT.getDefaultState(), 2);
                    }
                }
                /*if (state.getBlock() == Blocks.TORCH && state.getValue(TorchBlock.FACING) == Direction.WEST)
                    if (world.getBlockState(blockpos.east()).getBlock() == Blocks.AIR)
                        world.setBlockState(blockpos.east(), Blocks.DIRT.getDefaultState(), 2);
                if (state.getBlock() == Blocks.TORCH && state.getValue(BlockTorch.FACING) == Direction.EAST)
                    if (world.getBlockState(blockpos.west()).getBlock() == Blocks.AIR)
                        world.setBlockState(blockpos.west(), Blocks.DIRT.getDefaultState(), 2);
                if (state.getBlock() == Blocks.TORCH && state.getValue(BlockTorch.FACING) == Direction.NORTH)
                    if (world.getBlockState(blockpos.south()).getBlock() == Blocks.AIR)
                        world.setBlockState(blockpos.south(), Blocks.DIRT.getDefaultState(), 2);
                if (state.getBlock() == Blocks.TORCH && state.getValue(BlockTorch.FACING) == Direction.SOUTH)
                    if (world.getBlockState(blockpos.north()).getBlock() == Blocks.AIR)
                        world.setBlockState(blockpos.north(), Blocks.DIRT.getDefaultState(), 2);
                if (state.getBlock() == Blocks.TORCH && state.getValue(TorchBlock.FACING) == Direction.UP)
                    if (world.getBlockState(blockpos.down()).getBlock() == Blocks.AIR)
                        world.setBlockState(blockpos.down(), Blocks.DIRT.getDefaultState(), 2);*/
                if (state.getBlock() == Blocks.CHEST) {
                    if (block.contains("nbt")) {
                        //System.out.println("Chest NBT");
                        CompoundNBT nbtData = block.getCompound("nbt");
                        world.setBlockState(blockpos, state, 2);
                        ChestTileEntity te = (ChestTileEntity) ChestTileEntity.create(nbtData);
                        world.setTileEntity(blockpos, te);
                        //System.out.println(world.getTileEntity(blockpos));
                        if (nbtData.getString("id").equals("minecraft:chest")) {

                        }
                    }
                }
                else {
                    world.setBlockState(blockpos, state, 2);
                }
                //world.setBlockState(blockpos, ModBlocks.KKChest.getDefaultState().withProperty(BlockKKChest.FACING, state.getValue(BlockChest.FACING)));
                //System.out.println(i + ":" + state.getBlock() + " " + blockpos.getY());
            }
        } catch (IOException e){
            e.printStackTrace();
        }
    }

}
