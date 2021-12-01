package online.kingdomkeys.kingdomkeys.block;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class GhostBloxBlock extends BaseBlock implements SimpleWaterloggedBlock {

    public static final BooleanProperty VISIBLE = BooleanProperty.create("visible");

    public GhostBloxBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.defaultBlockState().setValue(VISIBLE, true).setValue(BlockStateProperties.WATERLOGGED, false));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(VISIBLE);
        builder.add(BlockStateProperties.WATERLOGGED);
    }

    @Nonnull
    @Override
    public FluidState getFluidState(BlockState state) {
        return state.getValue(BlockStateProperties.WATERLOGGED) && !state.getValue(VISIBLE) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
    }

    @Override
    public boolean canPlaceLiquid(BlockGetter worldIn, BlockPos pos, BlockState state, Fluid fluidIn) {
        return !state.getValue(VISIBLE) && (!state.getValue(BlockStateProperties.WATERLOGGED) && fluidIn == Fluids.WATER);
    }

    @Override
    public boolean placeLiquid(LevelAccessor worldIn, BlockPos pos, BlockState state, FluidState fluidStateIn) {
        if (!state.getValue(VISIBLE) && (!state.getValue(BlockStateProperties.WATERLOGGED) && fluidStateIn.getType() == Fluids.WATER)) {
            if (!worldIn.isClientSide()) {
                worldIn.setBlock(pos, state.setValue(BlockStateProperties.WATERLOGGED, Boolean.valueOf(true)), 3);
                worldIn.getFluidTicks().scheduleTick(pos, fluidStateIn.getType(), fluidStateIn.getType().getTickDelay(worldIn));
            }

            return true;
        } else {
            return false;
        }
    }

    @Override
    public void neighborChanged(BlockState state, Level worldIn, BlockPos pos, Block blockIn, BlockPos fromPos, boolean b) {
        if (worldIn.getBlockState(fromPos).getBlock() == ModBlocks.ghostBlox.get()) {
            worldIn.setBlockAndUpdate(pos, worldIn.getBlockState(fromPos));
            //Check for block power so block updates don't make them visible if there is still a powered block
            if (worldIn.hasNeighborSignal(pos)) {
                worldIn.setBlockAndUpdate(pos, state.setValue(VISIBLE, false));
            }
        } else {
            worldIn.setBlockAndUpdate(pos, state.setValue(VISIBLE, !worldIn.hasNeighborSignal(pos)));
        }
        if (worldIn.getBlockState(pos).getValue(VISIBLE)) {
            worldIn.setBlockAndUpdate(pos, worldIn.getBlockState(pos).setValue(BlockStateProperties.WATERLOGGED, false));
        }
    }

    @Override
    public boolean shouldCheckWeakPower(BlockState state, LevelReader world, BlockPos pos, Direction side) {
        return false;
    }

    @SuppressWarnings("deprecation")
    @Override
    public int getLightBlock(BlockState state, BlockGetter worldIn, BlockPos pos) {
        return (state.getValue(VISIBLE)) ? super.getLightBlock(state, worldIn, pos) : 0;
    }

    @Override
    public boolean isSignalSource(BlockState state) {
        return false;
    }
    
    @Override
    public boolean canConnectRedstone(BlockState state, BlockGetter world, BlockPos pos, @Nullable Direction side) {
        return false;
    }

    @SuppressWarnings("deprecation")
    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
        return (state.getValue(VISIBLE)) ? super.getCollisionShape(state, worldIn, pos, context) : Shapes.empty();
    }

    @SuppressWarnings("deprecation")
    @Override
    public VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
        return (state.getValue(VISIBLE)) ? super.getShape(state, worldIn, pos, context) : Shapes.empty();
    }

    @Override
    public void onPlace(BlockState state, Level worldIn, BlockPos pos, BlockState oldState, boolean b) {
        if (oldState.getBlock() != state.getBlock()) {
            worldIn.setBlockAndUpdate(pos, state.setValue(VISIBLE, !worldIn.hasNeighborSignal(pos)));
        }
    }

    @Override
    public void setPlacedBy(Level worldIn, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
        if (!worldIn.isClientSide && worldIn.getBlockEntity(pos) == null) {
            worldIn.setBlockAndUpdate(pos, state.setValue(VISIBLE, !worldIn.hasNeighborSignal(pos)));
        }
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState().setValue(VISIBLE, true).setValue(BlockStateProperties.WATERLOGGED, false);
    }


}
