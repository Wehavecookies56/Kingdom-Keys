package online.kingdomkeys.kingdomkeys.block;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class BlockGhostBlox extends BlockBase {

    public static final BooleanProperty VISIBLE = BooleanProperty.create("visible");

    public BlockGhostBlox(String name, Properties properties) {
        super(name, properties);
        this.setDefaultState(this.getDefaultState().with(VISIBLE, true));
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, IBlockState> builder) {
        super.fillStateContainer(builder);
        builder.add(VISIBLE);
    }

    @SuppressWarnings("deprecation")
    @Override
    public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos) {
        if (worldIn.getBlockState(fromPos).getBlock() == ModBlocks.ghostBlox) {
            worldIn.setBlockState(pos, worldIn.getBlockState(fromPos));
            //Check for block power so block updates don't make them visible if there is still a powered block
            if (worldIn.isBlockPowered(pos)) {
                worldIn.setBlockState(pos, state.with(VISIBLE, false));
            }
        } else {
            worldIn.setBlockState(pos, state.with(VISIBLE, !worldIn.isBlockPowered(pos)));
        }
    }

    @SuppressWarnings("deprecation")
    @Override
    public boolean isSolid(IBlockState state) {
        return state.get(VISIBLE);
    }

    @SuppressWarnings("deprecation")
    @Override
    public int getOpacity(IBlockState state, IBlockReader worldIn, BlockPos pos) {
        return  (state.get(VISIBLE)) ? super.getOpacity(state, worldIn, pos) : 0;
    }

    @Override
    public BlockRenderLayer getRenderLayer() {
        return BlockRenderLayer.CUTOUT;
    }

    @SuppressWarnings("deprecation")
    @Override
    public boolean canProvidePower(IBlockState state) {
        return true;
    }

    @SuppressWarnings("deprecation")
    @Override
    public VoxelShape getCollisionShape(IBlockState state, IBlockReader worldIn, BlockPos pos) {
        return (state.get(VISIBLE)) ? super.getCollisionShape(state, worldIn, pos) : VoxelShapes.empty();
    }

    @SuppressWarnings("deprecation")
    @Override
    public VoxelShape getShape(IBlockState state, IBlockReader worldIn, BlockPos pos) {
        return (state.get(VISIBLE)) ? super.getShape(state, worldIn, pos) : VoxelShapes.empty();
    }

    @SuppressWarnings("deprecation")
    @Override
    public void onBlockAdded(IBlockState state, World worldIn, BlockPos pos, IBlockState oldState) {
        if (oldState.getBlock() != state.getBlock()) {
            worldIn.setBlockState(pos, state.with(VISIBLE, !worldIn.isBlockPowered(pos)));
        }
    }

    @Override
    public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, @Nullable EntityLivingBase placer, ItemStack stack) {
        if (!worldIn.isRemote && worldIn.getTileEntity(pos) == null) {
            worldIn.setBlockState(pos, state.with(VISIBLE, !worldIn.isBlockPowered(pos)));
        }
    }

    @Nullable
    @Override
    public IBlockState getStateForPlacement(BlockItemUseContext context) {
        return this.getDefaultState().with(VISIBLE, true);
    }

    @SuppressWarnings("deprecation")
    @Override
    public boolean isNormalCube(IBlockState state) {
        return state.get(VISIBLE);
    }

    @SuppressWarnings("deprecation")
    @Override
    public boolean isFullCube(IBlockState state) {
        return state.get(VISIBLE);
    }

}
