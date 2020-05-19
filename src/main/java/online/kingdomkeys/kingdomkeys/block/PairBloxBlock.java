package online.kingdomkeys.kingdomkeys.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.Block.Properties;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.IntegerProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import online.kingdomkeys.kingdomkeys.entity.ModEntities;
import online.kingdomkeys.kingdomkeys.entity.EntityHelper.Dir;
import online.kingdomkeys.kingdomkeys.entity.block.PairBloxEntity;

import javax.annotation.Nullable;

public class PairBloxBlock extends BaseBlock {

	public static final IntegerProperty PAIR = IntegerProperty.create("pair", 0, 2);

	public PairBloxBlock(Properties properties) {
		super(properties);
		this.setDefaultState(this.getDefaultState().with(PAIR, 0));
	}

	@Override
	protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
		super.fillStateContainer(builder);
		builder.add(PAIR);
	}

	@Override
	public void onBlockClicked(BlockState state, World worldIn, BlockPos pos, PlayerEntity player) {
		PairBloxEntity pairEntity = new PairBloxEntity(worldIn, pos.getX(), pos.getY(), pos.getZ(), state.get(PAIR));
		float velocity = 0.5F;
		switch (MathHelper.floor(player.rotationYaw * 4.0F / 360.0F + 0.5D) & 3) { // Get direction
		case 0:
			pairEntity.setMotion(0, -velocity, velocity);
			break;
		case 1:
			pairEntity.setMotion(-velocity, -velocity, 0);
			break;
		case 2:
			pairEntity.setMotion(0, -velocity, -velocity);
			break;
		case 3:
			pairEntity.setMotion(velocity, -velocity, 0);
			break;

		}

		// System.out.println(getDefaultState().get(PAIR));
		worldIn.setBlockState(pos, Blocks.AIR.getDefaultState());
		worldIn.addEntity(pairEntity);
		super.onBlockClicked(state, worldIn, pos, player);
	}

	@Override
	public void onBlockAdded(BlockState state, World world, BlockPos pos, BlockState oldState, boolean isMoving) {
		BlockState other = null;
		BlockPos[] positions = { pos.north(), pos.east(), pos.south(), pos.west() };
		int i = 0;
		for (i = 0; i < positions.length; i++) {
			if (world.getBlockState(positions[i]).getBlock() == ModBlocks.pairBlox.get().getDefaultState().getBlock()) {
				other = world.getBlockState(positions[i]);
				break;
			}
		}
		
		//Check if both blox are different but not the final result one
		if (other != null && state.get(PAIR) < 2 && other.get(PAIR) < 2 && state.get(PAIR) != other.get(PAIR)) {
			System.out.println("MERGE");
			world.setBlockState(pos, Blocks.AIR.getDefaultState());
			world.setBlockState(positions[i], Blocks.AIR.getDefaultState());
			world.setBlockState(positions[i], ModBlocks.pairBlox.get().getDefaultState().with(PAIR, 2));
		}

	}

	@Override
	public void onBlockPlacedBy(World world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {

	}

	/*
	 * @Override public void neighborChanged(BlockState state, World worldIn,
	 * BlockPos pos, Block blockIn, BlockPos fromPos, boolean isMoving) {
	 * worldIn.setBlockState(pos, state.with(ACTIVE, worldIn.isBlockPowered(pos)));
	 * }
	 */

	@Nullable
	@Override
	public BlockState getStateForPlacement(BlockItemUseContext context) {
		return this.getDefaultState().with(PAIR, 0);
	}

	@Override
	public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit) {
		if (state.get(PAIR) < 2) {
			int newState = state.get(PAIR) == 0 ? 1 : 0;
			worldIn.setBlockState(pos, state.with(PAIR, newState));
			return ActionResultType.SUCCESS;
		} else {
			return ActionResultType.FAIL;
		}
	}

	/*
	 * @Override public boolean hasTileEntity(BlockState state) { return true; }
	 * 
	 * @Nullable
	 * 
	 * @Override public TileEntity createTileEntity(BlockState state, IBlockReader
	 * world) { return ModEntities.TYPE_MAGNET_BLOX.get().create(); }
	 */

}
