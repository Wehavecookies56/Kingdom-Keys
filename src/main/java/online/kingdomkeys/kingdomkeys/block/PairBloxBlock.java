package online.kingdomkeys.kingdomkeys.block;

import java.util.Random;

import javax.annotation.Nullable;

import net.minecraft.block.Block;
import net.minecraft.block.Block.Properties;
import net.minecraft.block.material.Material;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.FallingBlock;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.FallingBlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.IntegerProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import online.kingdomkeys.kingdomkeys.entity.block.PairBloxEntity;

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
		if (!worldIn.isRemote) {
			PairBloxEntity pairEntity = new PairBloxEntity(worldIn, pos.getX(), pos.getY(), pos.getZ(), state.get(PAIR));
			float velocity = 0.5F;
			switch (MathHelper.floor(player.rotationYaw * 8.0F / 360.0F + 0.5D) & 7) { // Get direction
			case 0:// S
				pairEntity.setMotion(0, 0, velocity);
				break;
			case 1:// SW
				pairEntity.setMotion(-velocity, 0, velocity);
				break;
			case 2:// W
				pairEntity.setMotion(-velocity, 0, 0);
				break;
			case 3:// NW
				pairEntity.setMotion(-velocity, 0, -velocity);
				break;
			case 4:// N
				pairEntity.setMotion(0, 0, -velocity);
				break;
			case 5:// NE
				pairEntity.setMotion(velocity, 0, -velocity);
				break;
			case 6:// E
				pairEntity.setMotion(velocity, 0, 0);
				break;
			case 7:// SE
				pairEntity.setMotion(velocity, 0, velocity);
				break;

			}

			// System.out.println(getDefaultState().get(PAIR));
			worldIn.setBlockState(pos, Blocks.AIR.getDefaultState());
			worldIn.addEntity(pairEntity);
		}
		super.onBlockClicked(state, worldIn, pos, player);
	}

	@Override
	public void tick(BlockState state, ServerWorld worldIn, BlockPos pos, Random rand) {
		System.out.println("a");
		if (worldIn.isAirBlock(pos.down()) || canFallThrough(worldIn.getBlockState(pos.down())) && pos.getY() >= 0) {
			worldIn.setBlockState(pos, Blocks.AIR.getDefaultState());
			PairBloxEntity pairEntity = new PairBloxEntity(worldIn, pos.getX(), pos.getY(), pos.getZ(), state.get(PAIR));
			pairEntity.setMotion(0, -1, 0);
			worldIn.addEntity(pairEntity);
		}
	}

	public static boolean canFallThrough(BlockState state) {
		Block block = state.getBlock();
		Material material = state.getMaterial();
		return state.isAir() || block == Blocks.FIRE || material.isLiquid() || material.isReplaceable();
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

		// Check if both blox are different but not the final result one
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
