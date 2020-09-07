package online.kingdomkeys.kingdomkeys.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.IWaterLoggable;
import net.minecraft.entity.LivingEntity;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.Fluids;
import net.minecraft.fluid.IFluidState;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class GhostBloxBlock extends BaseBlock implements IWaterLoggable {

    public static final BooleanProperty VISIBLE = BooleanProperty.create("visible");

    public GhostBloxBlock(Properties properties) {
        super(properties);
        this.setDefaultState(this.getDefaultState().with(VISIBLE, true).with(BlockStateProperties.WATERLOGGED, false));
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        super.fillStateContainer(builder);
        builder.add(VISIBLE);
        builder.add(BlockStateProperties.WATERLOGGED);
    }

    @Override
    public IFluidState getFluidState(BlockState state) {
        return state.get(BlockStateProperties.WATERLOGGED) && !state.get(VISIBLE) ? Fluids.WATER.getStillFluidState(false) : super.getFluidState(state);
    }

    @Override
    public boolean canContainFluid(IBlockReader worldIn, BlockPos pos, BlockState state, Fluid fluidIn) {
        return !state.get(VISIBLE) && (!state.get(BlockStateProperties.WATERLOGGED) && fluidIn == Fluids.WATER);
    }

    @Override
    public boolean receiveFluid(IWorld worldIn, BlockPos pos, BlockState state, IFluidState fluidStateIn) {
        if (!state.get(VISIBLE) && (!state.get(BlockStateProperties.WATERLOGGED) && fluidStateIn.getFluid() == Fluids.WATER)) {
            if (!worldIn.isRemote()) {
                worldIn.setBlockState(pos, state.with(BlockStateProperties.WATERLOGGED, Boolean.valueOf(true)), 3);
                worldIn.getPendingFluidTicks().scheduleTick(pos, fluidStateIn.getFluid(), fluidStateIn.getFluid().getTickRate(worldIn));
            }

            return true;
        } else {
            return false;
        }
    }

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
        if (worldIn.getBlockState(pos).get(VISIBLE)) {
            worldIn.setBlockState(pos, worldIn.getBlockState(pos).with(BlockStateProperties.WATERLOGGED, false));
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
        return this.getDefaultState().with(VISIBLE, true).with(BlockStateProperties.WATERLOGGED, false);
    }

    @SuppressWarnings("deprecation")
    @Override
    public boolean isNormalCube(BlockState state, IBlockReader reader, BlockPos pos) {
        return state.get(VISIBLE);
    }




}
