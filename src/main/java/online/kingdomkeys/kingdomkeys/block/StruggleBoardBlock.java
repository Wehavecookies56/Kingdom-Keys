package online.kingdomkeys.kingdomkeys.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import online.kingdomkeys.kingdomkeys.data.WorldData;
import online.kingdomkeys.kingdomkeys.lib.Struggle;
import online.kingdomkeys.kingdomkeys.network.PacketHandler;
import online.kingdomkeys.kingdomkeys.network.stc.SCOpenStruggleMenu;
import online.kingdomkeys.kingdomkeys.network.stc.SCSyncWorldData;

import javax.annotation.Nullable;
import java.util.EnumMap;
import java.util.Map;

public class StruggleBoardBlock extends BaseBlock {

	public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;

	private static final double THICKNESS = 2;
	private static final Map<Direction, VoxelShape> SHAPES = new EnumMap<>(Direction.class);

	static {
		SHAPES.put(Direction.NORTH, box(0, 0, 16 - THICKNESS, 16, 16, 16));
		SHAPES.put(Direction.SOUTH, box(0, 0, 0, 16, 16, THICKNESS));
		SHAPES.put(Direction.WEST, box(16 - THICKNESS, 0, 0, 16, 16, 16));
		SHAPES.put(Direction.EAST, box(0, 0, 0, THICKNESS, 16, 16));
	}

	public StruggleBoardBlock(Properties properties) {
		super(properties);
		registerDefaultState(stateDefinition.any().setValue(FACING, Direction.NORTH));
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(FACING);
	}

	@Override
	protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
		return SHAPES.get(state.getValue(FACING));
	}

	@Override
	protected VoxelShape getCollisionShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
		return Shapes.empty();
	}

	@Nullable
	@Override
	public BlockState getStateForPlacement(BlockPlaceContext context) {
		Direction face = context.getClickedFace();

		Direction facing = face.getAxis().isHorizontal() ? face : context.getHorizontalDirection().getOpposite();
		BlockState state = defaultBlockState().setValue(FACING, facing);

		return state.canSurvive(context.getLevel(), context.getClickedPos()) ? state : null;
	}

	@Override
	protected boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
		Direction behind = state.getValue(FACING).getOpposite();
		BlockPos support = pos.relative(behind);

		return level.getBlockState(support).isFaceSturdy(level, support, state.getValue(FACING));
	}

	@Override
	protected BlockState updateShape(BlockState state, Direction direction, BlockState neighbour, LevelAccessor level, BlockPos pos, BlockPos neighbourPos) {
		if (direction == state.getValue(FACING).getOpposite() && !state.canSurvive(level, pos)) {
			return Blocks.AIR.defaultBlockState();
		}

		return super.updateShape(state, direction, neighbour, level, pos, neighbourPos);
	}

	@Override
	protected BlockState rotate(BlockState state, Rotation rotation) {
		return state.setValue(FACING, rotation.rotate(state.getValue(FACING)));
	}

	@Override
	protected BlockState mirror(BlockState state, Mirror mirror) {
		return state.rotate(mirror.getRotation(state.getValue(FACING)));
	}

	@Override
	public ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
		if (!level.isClientSide) {
			PacketHandler.sendTo(new SCOpenStruggleMenu(pos), (ServerPlayer) player);
		}
		return ItemInteractionResult.SUCCESS;
	}

	@Override
	public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving) {
		if (!level.isClientSide && !state.is(newState.getBlock())) {
			WorldData worldData = WorldData.get(level.getServer());
			Struggle struggle = worldData.getStruggleFromBlockPos(pos);
			if (struggle != null) {
				worldData.removeStruggle(struggle);
				worldData.setDirty();
				PacketHandler.sendToAll(new SCSyncWorldData(level.getServer()));
			}
		}
		super.onRemove(state, level, pos, newState, isMoving);
	}
}
