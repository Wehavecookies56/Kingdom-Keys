package online.kingdomkeys.kingdomkeys.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RailBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.RailShape;
import net.minecraft.world.phys.Vec3;

import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.context.BlockPlaceContext;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Nullable;

public class FlowmotionRailBlock extends RailBlock {
	public static final MapCodec<RailBlock> CODEC = simpleCodec(properties -> new FlowmotionRailBlock(properties, DyeColor.WHITE));

	private final DyeColor colour;

	public FlowmotionRailBlock(Properties properties, DyeColor colour) {
		super(properties);
		this.colour = colour;
	}

	public DyeColor getColour() {
		return colour;
	}

	@Override
	public MapCodec<RailBlock> codec() {
		return CODEC;
	}

	// Allows it to stay in the air without a block below it
	@Override
	protected boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
		return true;
	}

	@Override
	protected void neighborChanged(BlockState state, Level level, BlockPos pos, Block block, BlockPos fromPos, boolean isMoving) {
		if (!level.isClientSide && level.getBlockState(pos).is(this)) {
			level.setBlock(pos, shapeFor(level, pos, state), Block.UPDATE_CLIENTS);
		}
	}

	@Override
	public BlockState getStateForPlacement(BlockPlaceContext context) {
		BlockState placed = super.getStateForPlacement(context);
		return placed == null ? null : shapeFor(context.getLevel(), context.getClickedPos(), placed);
	}

	@Override
	protected void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean isMoving) {
		if (!level.isClientSide && !oldState.is(state.getBlock())) {
			level.setBlock(pos, shapeFor(level, pos, state), Block.UPDATE_CLIENTS);
			// Neighbours of the same colour may want to curve into the new piece
			for (Direction side : Direction.Plane.HORIZONTAL) {
				for (BlockPos neighbour : new BlockPos[]{pos.relative(side), pos.relative(side).above(), pos.relative(side).below()}) {
					if (level.getBlockState(neighbour).getBlock() == this) {
						level.setBlock(neighbour, shapeFor(level, neighbour, level.getBlockState(neighbour)), Block.UPDATE_CLIENTS);
					}
				}
			}
		}
	}

	// Only same color rails can connect
	private BlockState shapeFor(LevelReader level, BlockPos pos, BlockState state) {
		List<Direction> joined = new ArrayList<>();
		Map<Direction, Boolean> rises = new EnumMap<>(Direction.class);

		for (Direction side : Direction.Plane.HORIZONTAL) {
			BlockPos beside = pos.relative(side);

			if (sameLine(level, beside)) {
				joined.add(side);
				rises.put(side, false);
			} else if (sameLine(level, beside.above())) {
				joined.add(side);
				rises.put(side, true);
			} else if (sameLine(level, beside.below())) {
				joined.add(side);
				rises.put(side, false);
			}
		}

		RailShape shape = shapeFrom(joined, rises, state.getValue(getShapeProperty()));
		return state.setValue(getShapeProperty(), shape);
	}

	private boolean sameLine(LevelReader level, BlockPos pos) {
		return level.getBlockState(pos).getBlock() == this;
	}

	private RailShape shapeFrom(List<Direction> joined, Map<Direction, Boolean> rises, RailShape current) {
		if (joined.size() < 2) {
			if (joined.size() == 1) {
				Direction side = joined.getFirst();
				return straight(side, rises.getOrDefault(side, false), side);
			}

			return current;
		}

		Direction first = joined.get(0);
		Direction second = joined.get(1);

		if (first.getOpposite() == second) {
			// Climbing is drawn towards whichever end is a block higher, and only one end can be
			return straight(first, rises.getOrDefault(first, false), rises.getOrDefault(second, false) ? second : first);
		}

		return corner(first, second);
	}

	private RailShape straight(Direction side, boolean risesHere, Direction climbTowards) {
		boolean climbs = risesHere || climbTowards != side;
		Direction axis = side.getAxis() == Direction.Axis.Z ? Direction.NORTH : Direction.EAST;

		if (!climbs) {
			return axis == Direction.NORTH ? RailShape.NORTH_SOUTH : RailShape.EAST_WEST;
		}

		return switch (climbTowards) {
			case NORTH -> RailShape.ASCENDING_NORTH;
			case SOUTH -> RailShape.ASCENDING_SOUTH;
			case EAST -> RailShape.ASCENDING_EAST;
			default -> RailShape.ASCENDING_WEST;
		};
	}

	private RailShape corner(Direction a, Direction b) {
		boolean north = a == Direction.NORTH || b == Direction.NORTH;
		boolean south = a == Direction.SOUTH || b == Direction.SOUTH;
		boolean east = a == Direction.EAST || b == Direction.EAST;

		if (north) {
			return east ? RailShape.NORTH_EAST : RailShape.NORTH_WEST;
		}

		return south && east ? RailShape.SOUTH_EAST : RailShape.SOUTH_WEST;
	}

	/** The two directions a shape joins up with. Ascending shapes climb towards the first of the pair. */
	public static Direction[] connections(RailShape shape) {
		return switch (shape) {
			case NORTH_SOUTH, ASCENDING_NORTH -> new Direction[]{Direction.NORTH, Direction.SOUTH};
			case ASCENDING_SOUTH -> new Direction[]{Direction.SOUTH, Direction.NORTH};
			case EAST_WEST, ASCENDING_EAST -> new Direction[]{Direction.EAST, Direction.WEST};
			case ASCENDING_WEST -> new Direction[]{Direction.WEST, Direction.EAST};
			case SOUTH_EAST -> new Direction[]{Direction.SOUTH, Direction.EAST};
			case SOUTH_WEST -> new Direction[]{Direction.SOUTH, Direction.WEST};
			case NORTH_WEST -> new Direction[]{Direction.NORTH, Direction.WEST};
			case NORTH_EAST -> new Direction[]{Direction.NORTH, Direction.EAST};
		};
	}

	/** The other end of the piece, which is both where you came in and where you go if you turn around */
	public static Direction other(RailShape shape, Direction direction) {
		Direction[] ends = connections(shape);
		return ends[0] == direction ? ends[1] : ends[0];
	}

	public static boolean climbs(RailShape shape, Direction direction) {
		return shape.isAscending() && connections(shape)[0] == direction;
	}

	// Points that form a curve
	private static final int CURVE_SAMPLES = 4;

	// Player height when flowmotioning a rail
	private static final double RIDE_HEIGHT = 0.2D;

	/** Where a piece wants to be ridden through: the middle of a flat one, halfway up a sloped one */
	public static Vec3 centre(BlockPos pos, RailShape shape) {
		return new Vec3(pos.getX() + 0.5D, pos.getY() + RIDE_HEIGHT + (shape.isAscending() ? 0.5D : 0.0D), pos.getZ() + 0.5D);
	}

	/** The middle of the edge a piece joins its neighbour by, a block higher on the climbing side */
	private static Vec3 edge(BlockPos pos, RailShape shape, Direction side) {
		double y = pos.getY() + RIDE_HEIGHT + (climbs(shape, side) ? 1.0D : 0.0D);
		return new Vec3(pos.getX() + 0.5D + side.getStepX() * 0.5D, y, pos.getZ() + 0.5D + side.getStepZ() * 0.5D);
	}

	/**
	 * The line to ride through a piece, ending on the edge it hands over at.
	 */
	public static Vec3[] path(BlockPos pos, RailShape shape, Direction exit) {
		Direction entry = other(shape, exit);
		Vec3 leaving = edge(pos, shape, exit);

		if (entry == exit.getOpposite()) {
			return new Vec3[]{leaving};
		}

		Vec3 entering = edge(pos, shape, entry);
		Vec3 corner = centre(pos, shape);
		Vec3[] arc = new Vec3[CURVE_SAMPLES];

		for (int i = 1; i <= CURVE_SAMPLES; i++) {
			arc[i - 1] = quadratic(entering, corner, leaving, i / (double) CURVE_SAMPLES);
		}

		return arc;
	}

	private static Vec3 quadratic(Vec3 from, Vec3 control, Vec3 to, double t) {
		double u = 1 - t;
		return from.scale(u * u).add(control.scale(2 * u * t)).add(to.scale(t * t));
	}

	/** Whether something is standing on or passing through a rail, feet or the block below */
	public static boolean isOn(Entity entity) {
		BlockPos at = entity.blockPosition();
		return shapeAt(entity.level(), at) != null || shapeAt(entity.level(), at.below()) != null;
	}

	@Nullable
	public static RailShape shapeAt(BlockGetter level, BlockPos pos) {
		BlockState state = level.getBlockState(pos);
		return state.getBlock() instanceof FlowmotionRailBlock rail ? state.getValue(rail.getShapeProperty()) : null;
	}

	/**
	 * Next position the player is riding towards
	 */
	@Nullable
	public static BlockPos next(BlockGetter level, BlockPos pos, Direction direction) {
		RailShape shape = shapeAt(level, pos);
		if (shape == null) {
			return null;
		}

		BlockPos ahead = pos.relative(direction);

		// Leaving by the climbing end means the next piece sits a block higher, and a piece one block down is how a descent continues
		if (climbs(shape, direction)) {
			ahead = ahead.above();
		}

		if (shapeAt(level, ahead) != null) {
			return ahead;
		}

		BlockPos below = ahead.below();
		return shapeAt(level, below) != null ? below : null;
	}

	/**
	 * Which way to travel after arriving at a piece.
	 *
	 * @param arrivedFrom the direction of travel that brought the rider here
	 * @return the new direction of travel, following the curve, or null if the piece doesn't join up
	 */
	@Nullable
	public static Direction travel(RailShape shape, Direction arrivedFrom) {
		Direction entry = arrivedFrom.getOpposite();
		Direction[] ends = connections(shape);

		if (ends[0] == entry) {
			return ends[1];
		}

		if (ends[1] == entry) {
			return ends[0];
		}

		// Arrived at an angle the piece doesn't accept, which happens where a curve meets a straight sideways rail
		for (Direction end : ends) {
			if (end == arrivedFrom) {
				return end;
			}
		}

		return null;
	}
}
