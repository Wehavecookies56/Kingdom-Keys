package online.kingdomkeys.kingdomkeys.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class GhostBloxBlock extends BaseBlock {

    public static final BooleanProperty VISIBLE = BooleanProperty.create("visible");

    public GhostBloxBlock(Properties properties) {
        super(properties);
        this.setDefaultState(this.getDefaultState().with(VISIBLE, true));
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        super.fillStateContainer(builder);
        builder.add(VISIBLE);
    }


    @SuppressWarnings("deprecation")
    @Override
    public void neighborChanged(BlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos, boolean b) {
        if (worldIn.getBlockState(fromPos).getBlock() == ModBlocks.ghostBlox.get()) {
            worldIn.setBlockState(pos, worldIn.getBlockState(fromPos));
            //Check for block power so block updates don't make them visible if there is still a powered block
            if (worldIn.isBlockPowered(pos)) {
                worldIn.setBlockState(pos, state.with(VISIBLE, false));
            }
        } else {
            worldIn.setBlockState(pos, state.with(VISIBLE, !worldIn.isBlockPowered(pos)));
        }
    }

    @Override
    public boolean shouldCheckWeakPower(BlockState state, IWorldReader world, BlockPos pos, Direction side) {
        return false;
    }

    @SuppressWarnings("deprecation")
    @Override
    public int getOpacity(BlockState state, IBlockReader worldIn, BlockPos pos) {
        return (state.get(VISIBLE)) ? super.getOpacity(state, worldIn, pos) : 0;
    }

    @SuppressWarnings("deprecation")
    @Override
    public boolean canProvidePower(BlockState state) {
        return false;
    }

    @Override
    public boolean canConnectRedstone(BlockState state, IBlockReader world, BlockPos pos, @Nullable Direction side) {
        return false;
    }

    @SuppressWarnings("deprecation")
    @Override
    public VoxelShape getCollisionShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
        return (state.get(VISIBLE)) ? super.getCollisionShape(state, worldIn, pos, context) : VoxelShapes.empty();
    }

    @SuppressWarnings("deprecation")
    @Override
    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
        return (state.get(VISIBLE)) ? super.getShape(state, worldIn, pos, context) : VoxelShapes.empty();
    }

    @SuppressWarnings("deprecation")
    @Override
    public void onBlockAdded(BlockState state, World worldIn, BlockPos pos, BlockState oldState, boolean b) {
        if (oldState.getBlock() != state.getBlock()) {
            worldIn.setBlockState(pos, state.with(VISIBLE, !worldIn.isBlockPowered(pos)));
        }
    }

    @Override
    public void onBlockPlacedBy(World worldIn, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
        if (!worldIn.isRemote && worldIn.getTileEntity(pos) == null) {
            worldIn.setBlockState(pos, state.with(VISIBLE, !worldIn.isBlockPowered(pos)));
        }
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context) {
        return this.getDefaultState().with(VISIBLE, true);
    }

    @SuppressWarnings("deprecation")
    @Override
    public boolean isNormalCube(BlockState state, IBlockReader reader, BlockPos pos) {
        return state.get(VISIBLE);
    }


}
