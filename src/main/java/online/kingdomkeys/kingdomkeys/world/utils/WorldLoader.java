package online.kingdomkeys.kingdomkeys.world.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtIo;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.minecraft.world.level.block.entity.StructureBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import online.kingdomkeys.kingdomkeys.KingdomKeys;

//Here for when we add worlds back
public class WorldLoader {

    public WorldLoader() {

    }

    public void processAndGenerateStructureFile(String file, ServerLevel world, BlockPos offset) {
        try {
            InputStream inputStream = getClass().getResourceAsStream("/assets/"+KingdomKeys.MODID+"/worlds/" + file + ".world");
            CompoundTag main = NbtIo.readCompressed(inputStream);

            ListTag palette = main.getList("palette", Constants.NBT.TAG_COMPOUND);

            ListTag blocks = main.getList("blocks", Constants.NBT.TAG_COMPOUND);

            List<BlockState> blockStates = new ArrayList<>();

            
            CompoundTag firstBlock = blocks.getCompound(0);
            BlockPos firstPos = new BlockPos(firstBlock.getList("pos", 3).getInt(0), firstBlock.getList("pos", 3).getInt(1), firstBlock.getList("pos", 3).getInt(2));
            

            for (int i = 0; i < palette.size(); i++) {
                CompoundTag block = palette.getCompound(i);
                blockStates.add(NbtUtils.readBlockState(block));
            }

            for (int i = 0; i < blocks.size(); i++) {
                CompoundTag block = blocks.getCompound(i);
                BlockPos blockpos = new BlockPos(block.getList("pos", 3).getInt(0)+offset.getX(), block.getList("pos", 3).getInt(1)+offset.getY(), block.getList("pos", 3).getInt(2)+offset.getZ());
                BlockState state = blockStates.get(block.getInt("state"));
                if (block.contains("nbt")) {
                    CompoundTag nbtData = block.getCompound("nbt");
                    StructureBlockEntity.loadStatic(blockpos, state, nbtData);
                }
                if (state.getBlock() == Blocks.OAK_DOOR) {
                    if (world.getBlockState(blockpos.below()).getBlock() == Blocks.AIR) {
                        world.setBlock(blockpos.below(), Blocks.DIRT.defaultBlockState(), 2);
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
                        
                        CompoundTag nbtData = block.getCompound("nbt");
                        world.setBlock(blockpos, state, 2);
                        ChestBlockEntity te = (ChestBlockEntity) ChestBlockEntity.loadStatic(blockpos, state, nbtData);
                        world.setBlockEntity(te);                        
                        if (nbtData.getString("id").equals("minecraft:chest")) {

                        }
                    }
                }
                else {
                    world.setBlock(blockpos, state, 2);
                }
                //world.setBlockState(blockpos, ModBlocks.KKChest.getDefaultState().withProperty(BlockKKChest.FACING, state.getValue(BlockChest.FACING)));
                
            }
        } catch (IOException e){
            e.printStackTrace();
        }
    }

}
