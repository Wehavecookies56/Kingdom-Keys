package online.kingdomkeys.kingdomkeys.block;

import javax.annotation.Nullable;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.BlockHitResult;
import online.kingdomkeys.kingdomkeys.entity.block.PairBloxEntity;

public class PairBloxBlock extends BaseBlock {

	public static final IntegerProperty PAIR = IntegerProperty.create("pair", 0, 2);

	public PairBloxBlock(Properties properties) {
		super(properties);
		this.registerDefaultState(this.defaultBlockState().setValue(PAIR, 0));
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		super.createBlockStateDefinition(builder);
		builder.add(PAIR);
	}

	@Override
	public void attack(BlockState state, Level worldIn, BlockPos pos, Player player) {
		if (!worldIn.isClientSide) {
			PairBloxEntity pairEntity = new PairBloxEntity(worldIn, pos.getX(), pos.getY(), pos.getZ(), state.getValue(PAIR));
			float velocity = 0.5F;
			switch (Mth.floor(player.getYRot() * 8.0F / 360.0F + 0.5D) & 7) { // Get direction
			case 0:// S
				pairEntity.setDeltaMovement(0, 0, velocity);
				break;
			case 1:// SW
				pairEntity.setDeltaMovement(-velocity, 0, velocity);
				break;
			case 2:// W
				pairEntity.setDeltaMovement(-velocity, 0, 0);
				break;
			case 3:// NW
				pairEntity.setDeltaMovement(-velocity, 0, -velocity);
				break;
			case 4:// N
				pairEntity.setDeltaMovement(0, 0, -velocity);
				break;
			case 5:// NE
				pairEntity.setDeltaMovement(velocity, 0, -velocity);
				break;
			case 6:// E
				pairEntity.setDeltaMovement(velocity, 0, 0);
				break;
			case 7:// SE
				pairEntity.setDeltaMovement(velocity, 0, velocity);
				break;

			}

			// System.out.println(getDefaultState().get(PAIR));
			worldIn.setBlockAndUpdate(pos, Blocks.AIR.defaultBlockState());
			worldIn.addFreshEntity(pairEntity);
		}
		super.attack(state, worldIn, pos, player);
	}

	@Override
	public void tick(BlockState state, ServerLevel worldIn, BlockPos pos, RandomSource rand) {
		if (worldIn.isEmptyBlock(pos.below()) || canFallThrough(worldIn.getBlockState(pos.below())) && pos.getY() >= 0) {
			worldIn.setBlockAndUpdate(pos, Blocks.AIR.defaultBlockState());
			PairBloxEntity pairEntity = new PairBloxEntity(worldIn, pos.getX(), pos.getY(), pos.getZ(), state.getValue(PAIR));
			pairEntity.setDeltaMovement(0, -1, 0);
			worldIn.addFreshEntity(pairEntity);
		}
	}

	public static boolean canFallThrough(BlockState state) {
		Block block = state.getBlock();
		return state.isAir() || block == Blocks.FIRE || state.liquid() || state.canBeReplaced();
	}

	@Override
	public void onPlace(BlockState state, Level world, BlockPos pos, BlockState oldState, boolean isMoving) {
		BlockState other = null;
		BlockPos[] positions = { pos.north(), pos.east(), pos.south(), pos.west() };
		int i = 0;
		for (i = 0; i < positions.length; i++) {
			if (world.getBlockState(positions[i]).getBlock() == ModBlocks.pairBlox.get().defaultBlockState().getBlock()) {
				other = world.getBlockState(positions[i]);
				break;
			}
		}

		// Check if both blox are different but not the final result one
		if (other != null && state.getValue(PAIR) < 2 && other.getValue(PAIR) < 2 && state.getValue(PAIR) != other.getValue(PAIR)) {
			//System.out.println("MERGE");
			world.setBlockAndUpdate(pos, Blocks.AIR.defaultBlockState());
			world.setBlockAndUpdate(positions[i], Blocks.AIR.defaultBlockState());
			world.setBlockAndUpdate(positions[i], ModBlocks.pairBlox.get().defaultBlockState().setValue(PAIR, 2));
		}

	}

	@Override
	public void setPlacedBy(Level world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {

	}

	@Nullable
	@Override
	public BlockState getStateForPlacement(BlockPlaceContext context) {
		return this.defaultBlockState().setValue(PAIR, 0);
	}

	@Override
	public InteractionResult use(BlockState state, Level worldIn, BlockPos pos, Player player, InteractionHand handIn, BlockHitResult hit) {
		if (state.getValue(PAIR) < 2) {
			int newState = state.getValue(PAIR) == 0 ? 1 : 0;
			worldIn.setBlockAndUpdate(pos, state.setValue(PAIR, newState));
			return InteractionResult.SUCCESS;
		} else {
			return InteractionResult.FAIL;
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
