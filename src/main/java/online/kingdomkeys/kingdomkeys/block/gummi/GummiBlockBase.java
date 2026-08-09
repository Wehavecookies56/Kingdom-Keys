package online.kingdomkeys.kingdomkeys.block.gummi;

import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.*;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import online.kingdomkeys.kingdomkeys.block.BaseBlock;
import online.kingdomkeys.kingdomkeys.item.ICreativeTab;
import online.kingdomkeys.kingdomkeys.lib.Corner;
import online.kingdomkeys.kingdomkeys.lib.Quarter;

import javax.annotation.Nullable;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

public class GummiBlockBase extends BaseBlock implements ICreativeTab {

    DyeColor color;
    List<Supplier<Block>> blocks;
    int armour, weight, cost;
    GummiPlacementType placementType;
    GummiBlockProperties.Shape shape;
    boolean isMultiBlock;

    // Aero fins thickness
    private static final double AERO_MIN = 0.4, AERO_MAX = 0.6;
    // Built once per shape, facing and quarter rather than per query
    private static final Map<GummiBlockProperties.Shape, Map<Direction, Map<Quarter, VoxelShape>>> SHAPES = new EnumMap<>(GummiBlockProperties.Shape.class);
    // Face placed shapes key off FACING alone, including up and down, so they need their own table
    private static final Map<Direction, VoxelShape> SLAB_SHAPES = new EnumMap<>(Direction.class);
    // Corner placed shapes key off CORNER and HALF instead
    private static final Map<GummiBlockProperties.Shape, Map<Corner, Map<Half, VoxelShape>>> CORNER_SHAPES = new EnumMap<>(GummiBlockProperties.Shape.class);

    static {
        List<double[]> wedge = List.of(
                new double[]{0, 0, 0, 0.5, 1, 1},
                new double[]{0.5, 0, 0, 1, 0.5, 1}
        );

        register(GummiBlockProperties.Shape.WEDGE, wedge);

        register(GummiBlockProperties.Shape.PIE, wedge);

        register(GummiBlockProperties.Shape.AERO_WEDGE, List.of(
                new double[]{0, 0, AERO_MIN, 0.5, 1, AERO_MAX},
                new double[]{0.5, 0, AERO_MIN, 1, 0.5, AERO_MAX}
        ));

        register(GummiBlockProperties.Shape.AERO_PLATE, List.of(new double[]{0, 0, AERO_MIN, 1, 1, AERO_MAX}));

        List<double[]> slab = List.of(new double[]{0, 0, 0, 1, 0.5, 1});

        for (Direction facing : Direction.values()) {
            SLAB_SHAPES.put(facing, buildRotated(slab, slabXRotation(facing), slabYRotation(facing)));
        }

        registerCorner(GummiBlockProperties.Shape.PYRAMID, List.of(
                new double[]{0, 0, 0, 1, 0.5, 1},
                new double[]{0, 0.5, 0.5, 0.5, 1, 1}
        ));

        registerCorner(GummiBlockProperties.Shape.ROUND_CORNER, List.of(
                new double[]{0, 0, 0, 1, 0.5, 1},
                new double[]{0, 0.5, 0.5, 0.5, 1, 1}
        ));
    }

    private static void registerCorner(GummiBlockProperties.Shape shape, List<double[]> base) {
        Map<Corner, Map<Half, VoxelShape>> byCorner = new EnumMap<>(Corner.class);

        for (Corner corner : Corner.values()) {
            Map<Half, VoxelShape> byHalf = new EnumMap<>(Half.class);

            for (Half half : Half.values()) {
                byHalf.put(half, buildRotated(base, half == Half.TOP ? 180 : 0, cornerYRotation(corner, half)));
            }

            byCorner.put(corner, byHalf);
        }

        CORNER_SHAPES.put(shape, byCorner);
    }

    /**
     * Matches the y rotation the generated blockstate gives each corner. Flipping to the top half also
     * shifts the corner round by one quarter turn, which is why the two halves need separate tables.
     */
    private static int cornerYRotation(Corner corner, Half half) {
        int quarter = switch (corner) {
            case CORNER1 -> 1;
            case CORNER2 -> 2;
            case CORNER3 -> 3;
            case CORNER4 -> 0;
        };

        return ((half == Half.TOP ? quarter + 3 : quarter) % 4) * 90;
    }

    /** Matches the rotations the generated blockstate gives each facing for face placed blocks */
    private static int slabXRotation(Direction facing) {
        return switch (facing) {
            case UP -> 0;
            case DOWN -> 180;
            default -> 90;
        };
    }

    private static int slabYRotation(Direction facing) {
        return switch (facing) {
            case EAST -> 90;
            case SOUTH -> 180;
            case WEST -> 270;
            default -> 0;
        };
    }

    private static void register(GummiBlockProperties.Shape shape, List<double[]> base) {
        Map<Direction, Map<Quarter, VoxelShape>> byFacing = new EnumMap<>(Direction.class);

        for (Direction facing : Direction.Plane.HORIZONTAL) {
            Map<Quarter, VoxelShape> byQuarter = new EnumMap<>(Quarter.class);

            for (Quarter quarter : Quarter.values()) {
                byQuarter.put(quarter, buildRotated(base, xRotation(quarter), yRotation(facing)));
            }

            byFacing.put(facing, byQuarter);
        }

        SHAPES.put(shape, byFacing);
    }

    @Override
    protected VoxelShape getCollisionShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        VoxelShape custom = customShape(state);
        return custom != null ? custom : super.getCollisionShape(state, level, pos, context);
    }

    @Nullable
    private VoxelShape customShape(BlockState state) {
        Map<Corner, Map<Half, VoxelShape>> byCorner = CORNER_SHAPES.get(shape);

        if (byCorner != null && state.hasProperty(CORNER) && state.hasProperty(HALF)) {
            return byCorner.get(state.getValue(CORNER)).get(state.getValue(HALF));
        }

        if (shape == GummiBlockProperties.Shape.SLAB) {
            return state.hasProperty(FACING) ? SLAB_SHAPES.get(state.getValue(FACING)) : SLAB_SHAPES.get(Direction.UP);
        }

        Map<Direction, Map<Quarter, VoxelShape>> byFacing = SHAPES.get(shape);

        if (byFacing != null && state.hasProperty(HORIZONTAL_FACING) && state.hasProperty(QUARTER)) {
            return byFacing.get(state.getValue(HORIZONTAL_FACING)).get(state.getValue(QUARTER));
        }

        return null;
    }

    /*@Nullable
    public VoxelShape debugCollisionShape(BlockState state) {
        return customShape(state);
    }*/

    /** Matches the x rotation the generated blockstate gives each quarter */
    private static int xRotation(Quarter quarter) {
        return switch (quarter) {
            case BOTTOM -> 0;
            case RIGHT -> 90;
            case TOP -> 180;
            case LEFT -> 270;
        };
    }

    /** Matches the y rotation the generated blockstate gives each facing */
    private static int yRotation(Direction facing) {
        return switch (facing) {
            case NORTH -> 90;
            case EAST -> 180;
            case SOUTH -> 270;
            default -> 0;
        };
    }

    private static VoxelShape buildRotated(List<double[]> boxes, int xRot, int yRot) {
        VoxelShape shape = Shapes.empty();

        for (double[] box : boxes) {
            double[] rotated = box;

            // Same order the model rotation uses: about X first, then about Y
            for (int turn = 0; turn < xRot / 90; turn++) {
                rotated = new double[]{rotated[0], rotated[2], 1 - rotated[4], rotated[3], rotated[5], 1 - rotated[1]};
            }
            for (int turn = 0; turn < yRot / 90; turn++) {
                rotated = new double[]{1 - rotated[5], rotated[1], rotated[0], 1 - rotated[2], rotated[4], rotated[3]};
            }

            shape = Shapes.or(shape, Shapes.box(rotated[0], rotated[1], rotated[2], rotated[3], rotated[4], rotated[5]));
        }

        return shape;
    }

    public static final EnumProperty<Corner> CORNER = EnumProperty.create("corner", Corner.class);
    public static final EnumProperty<Half> HALF = BlockStateProperties.HALF;
    public static final EnumProperty<Quarter> QUARTER = EnumProperty.create("quarter", Quarter.class);
    public static final EnumProperty<Direction.Axis> AXIS = BlockStateProperties.AXIS;
    public static final EnumProperty<Direction> FACING = BlockStateProperties.FACING;
    public static final DirectionProperty HORIZONTAL_FACING = BlockStateProperties.HORIZONTAL_FACING;
    public static final IntegerProperty X = IntegerProperty.create("x", 0, 1);
    public static final IntegerProperty Y = IntegerProperty.create("y", 0, 1);
    public static final IntegerProperty Z = IntegerProperty.create("z", 0, 1);

    final List<Property<?>> properties;

    public GummiBlockBase(GummiBlockProperties gummiProperties) {
        super(gummiProperties.properties);
        this.weight = gummiProperties.weight;
        this.armour = gummiProperties.armour;
        this.cost = gummiProperties.cost;
        this.placementType = gummiProperties.placementType;
        this.shape = gummiProperties.shape;
        properties = switch (placementType) {
            case STANDARD -> List.of();
            case EDGE -> List.of(HORIZONTAL_FACING, QUARTER);
            case CORNER -> List.of(HALF, CORNER);
            case END -> List.of(FACING);
            case PILLAR -> List.of(AXIS);
            case MULTIBLOCK2D -> List.of(QUARTER, HORIZONTAL_FACING, X, Z);
            case MULTIBLOCK3D -> List.of(HORIZONTAL_FACING, X, Y, Z);
        };
        if (gummiProperties.tinted) {
            this.color = gummiProperties.colour;
            this.blocks = gummiProperties.blocks;
        }
        isMultiBlock = gummiProperties.isMultiBlock;

        StateDefinition.Builder<Block, BlockState> builder = new StateDefinition.Builder<>(this);
        this.createBlockStateDefinition(builder);
        this.stateDefinition = builder.create(Block::defaultBlockState, BlockState::new);
        this.registerDefaultState(this.stateDefinition.any());
    }

    public int getArmour() {
        return armour;
    }

    public int getWeight() {
        return weight;
    }

    public int getCost() {
        return cost;
    }

    public GummiPlacementType getPlacementType() {
        return placementType;
    }

    public boolean isTinted() {
        return blocks != null && color != null;
    }

    @Nullable
    public DyeColor getColor() {
        return this.color;
    }

    @Override
    protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        if (isTinted() && stack.getItem() instanceof DyeItem dyeItem) {
            DyeColor dyeColor = dyeItem.getDyeColor();
            Block b = blocks.get(dyeColor.getId()).get();
            BlockState newState = b.defaultBlockState();
            //TODO handle multiblocks (need to set all the blocks to change
            newState = switch (placementType) {
                case END -> newState.setValue(FACING, state.getValue(FACING));
                case STANDARD -> newState;
                case EDGE -> newState.setValue(HORIZONTAL_FACING, state.getValue(HORIZONTAL_FACING)).setValue(QUARTER, state.getValue(QUARTER));
                case CORNER -> newState.setValue(CORNER, state.getValue(CORNER)).setValue(HALF, state.getValue(HALF));
                case PILLAR -> newState.setValue(AXIS, state.getValue(AXIS));
                case MULTIBLOCK2D -> state;
                case MULTIBLOCK3D -> state;
            };
            level.setBlockAndUpdate(pos, newState);
            player.swing(hand);
            if (!player.isCreative()) {
                stack.shrink(1);
            }
        }
        return super.useItemOn(stack, state, level, pos, player, hand, hitResult);
    }

    @Override
    public Tab getTab() {
        return Tab.GUMMI;
    }

    public boolean isMultiBlock() {
        return isMultiBlock;
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        Level level = context.getLevel();
        Direction direction = context.getClickedFace();
        BlockPos blockpos = context.getClickedPos();
        Direction facing = context.getHorizontalDirection();
        return switch (placementType) {
            case STANDARD -> super.getStateForPlacement(context);
            case EDGE -> {
                if (direction == Direction.DOWN || direction == Direction.UP) {
                    yield this.defaultBlockState().setValue(HORIZONTAL_FACING, context.getHorizontalDirection()).setValue(QUARTER, direction == Direction.DOWN ? Quarter.TOP : Quarter.BOTTOM);
                } else {
                    Quarter quarter = getQuarter(context);
                    if ((quarter == Quarter.LEFT || quarter == Quarter.RIGHT) && (direction == Direction.WEST || direction == Direction.SOUTH)) {
                        quarter = quarter.opposite();
                    }
                    yield this.defaultBlockState().setValue(HORIZONTAL_FACING, context.getHorizontalDirection()).setValue(QUARTER, quarter);
                }
            }
            case CORNER -> {
                double horizontalClickPos = direction != Direction.EAST && direction != Direction.WEST ? context.getClickLocation().x - (double) blockpos.getX() : context.getClickLocation().z - (double) blockpos.getZ();
                double verticalClickPos = direction == Direction.UP || direction == Direction.DOWN ? context.getClickLocation().z - (double) blockpos.getZ() : context.getClickLocation().y - (double) blockpos.getY();
                yield this.defaultBlockState().setValue(CORNER, getCorner(direction, verticalClickPos, horizontalClickPos)).setValue(HALF, direction == Direction.DOWN || (direction != Direction.UP && verticalClickPos > 0.5) ? Half.TOP : Half.BOTTOM);
            }
            case END -> this.defaultBlockState().setValue(FACING, direction);
            case PILLAR -> this.defaultBlockState().setValue(AXIS, direction.getAxis());
            case MULTIBLOCK2D -> {
                Direction zDir = direction == Direction.UP || direction == Direction.DOWN ? facing.getClockWise() : Direction.UP;
                if (direction == Direction.DOWN) {
                    zDir = zDir.getOpposite();
                }
                Quarter quarter = Quarter.BOTTOM;
                if (direction == Direction.DOWN) {
                    quarter = Quarter.TOP;
                } else if (direction == Direction.NORTH || direction == Direction.EAST) {
                    quarter = Quarter.RIGHT;
                } else if (direction == Direction.SOUTH || direction == Direction.WEST) {
                    quarter = Quarter.LEFT;
                }
                if ((quarter != Quarter.TOP && quarter != Quarter.BOTTOM) && (facing == Direction.EAST || facing == Direction.SOUTH)) {
                    quarter = quarter.opposite();
                }
                if (quarter == Quarter.RIGHT) {
                    zDir = zDir.getOpposite();
                }
                BlockPos pos1 = blockpos.relative(facing);
                BlockPos pos2 = blockpos.relative(zDir);
                BlockPos pos3 = blockpos.relative(facing).relative(zDir);
                if (level.getBlockState(pos1).getBlock() == Blocks.AIR && level.getBlockState(pos2).getBlock() == Blocks.AIR && level.getBlockState(pos3).getBlock() == Blocks.AIR) {
                    yield defaultBlockState().setValue(HORIZONTAL_FACING, facing).setValue(QUARTER, quarter);
                } else {
                    yield null;
                }
            }
            case MULTIBLOCK3D -> {
                BlockPos pos1 = blockpos.relative(facing);
                BlockPos pos2 = blockpos.relative(facing.getClockWise());
                BlockPos pos3 = blockpos.relative(facing).relative(facing.getClockWise());
                if (level.getBlockState(pos1).getBlock() == Blocks.AIR && level.getBlockState(pos2).getBlock() == Blocks.AIR && level.getBlockState(pos3).getBlock() == Blocks.AIR && level.getBlockState(pos1.relative(Direction.UP)).getBlock() == Blocks.AIR && level.getBlockState(pos2.relative(Direction.UP)).getBlock() == Blocks.AIR && level.getBlockState(pos3.relative(Direction.UP)).getBlock() == Blocks.AIR) {
                    yield defaultBlockState().setValue(HORIZONTAL_FACING, facing);
                } else {
                    yield null;
                }
            }
        };
    }

    @Override
    protected void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean movedByPiston) {
        super.onRemove(state, level, pos, newState, movedByPiston);
        if (placementType == GummiPlacementType.MULTIBLOCK3D) {
            if (newState.getBlock() == Blocks.AIR) {
                Direction facing = state.getValue(HORIZONTAL_FACING);
                Direction xDir = state.getValue(X) == 0 ? facing : facing.getOpposite();
                Direction yDir = state.getValue(Y) == 0 ? Direction.UP : Direction.DOWN;
                Direction zDir = state.getValue(Z) == 0 ? facing.getClockWise() : facing.getCounterClockWise();
                BlockPos pos1 = pos.relative(xDir);
                BlockPos pos2 = pos.relative(zDir);
                BlockPos pos3 = pos.relative(xDir).relative(zDir);
                if (level.getBlockState(pos1).getBlock() instanceof GummiBlockBase) {
                    level.setBlock(pos1, Blocks.AIR.defaultBlockState(), 3);
                }
                if (level.getBlockState(pos2).getBlock() instanceof GummiBlockBase) {
                    level.setBlock(pos2, Blocks.AIR.defaultBlockState(), 3);
                }
                if (level.getBlockState(pos3).getBlock() instanceof GummiBlockBase) {
                    level.setBlock(pos3, Blocks.AIR.defaultBlockState(), 3);
                }
                if (level.getBlockState(pos1.relative(yDir)).getBlock() instanceof GummiBlockBase) {
                    level.setBlock(pos1.relative(yDir), Blocks.AIR.defaultBlockState(), 3);
                }
                if (level.getBlockState(pos2.relative(yDir)).getBlock() instanceof GummiBlockBase) {
                    level.setBlock(pos2.relative(yDir), Blocks.AIR.defaultBlockState(), 3);
                }
                if (level.getBlockState(pos3.relative(yDir)).getBlock() instanceof GummiBlockBase) {
                    level.setBlock(pos3.relative(yDir), Blocks.AIR.defaultBlockState(), 3);
                }
            }
        } else if (placementType == GummiPlacementType.MULTIBLOCK2D) {
            if (newState.getBlock() == Blocks.AIR) {
                Quarter quarter = state.getValue(QUARTER);
                Direction facing = state.getValue(HORIZONTAL_FACING);
                Direction xDir = state.getValue(X) == 0 ? facing : facing.getOpposite();
                Direction zDir = quarter == Quarter.BOTTOM || quarter == Quarter.TOP ? state.getValue(Z) == 0 ? facing.getClockWise() : facing.getCounterClockWise() : state.getValue(Z) == 0 ? Direction.UP : Direction.DOWN;
                if (quarter == Quarter.TOP || quarter == Quarter.RIGHT) {
                    zDir = zDir.getOpposite();
                }
                BlockPos pos1 = pos.relative(xDir);
                BlockPos pos2 = pos.relative(zDir);
                BlockPos pos3 = pos.relative(xDir).relative(zDir);
                if (level.getBlockState(pos1).getBlock() instanceof GummiBlockBase) {
                    level.setBlock(pos1, Blocks.AIR.defaultBlockState(), 3);
                }
                if (level.getBlockState(pos2).getBlock() instanceof GummiBlockBase) {
                    level.setBlock(pos2, Blocks.AIR.defaultBlockState(), 3);
                }
                if (level.getBlockState(pos3).getBlock() instanceof GummiBlockBase) {
                    level.setBlock(pos3, Blocks.AIR.defaultBlockState(), 3);
                }
            }
        }
    }

    @Override
    public void setPlacedBy(Level level, BlockPos pos, BlockState state, @org.jetbrains.annotations.Nullable LivingEntity placer, ItemStack stack) {
        if (placementType == GummiPlacementType.MULTIBLOCK2D) {
            Quarter quarter = state.getValue(QUARTER);
            Direction facing = state.getValue(HORIZONTAL_FACING);
            Direction zDir = quarter == Quarter.BOTTOM || quarter == Quarter.TOP ? facing.getClockWise() : quarter == Quarter.RIGHT ? Direction.DOWN : Direction.UP;
            if (quarter == Quarter.TOP) {
                zDir = zDir.getOpposite();
            }
            BlockPos pos1 = pos.relative(facing);
            BlockPos pos2 = pos.relative(zDir);
            BlockPos pos3 = pos.relative(facing).relative(zDir);
            if (level.getBlockState(pos1).getBlock() == Blocks.AIR && level.getBlockState(pos2).getBlock() == Blocks.AIR && level.getBlockState(pos3).getBlock() == Blocks.AIR) {
                super.setPlacedBy(level, pos, state, placer, stack);
                level.setBlock(pos1, state.setValue(X, 1), 3);
                level.setBlock(pos2, state.setValue(Z, 1), 3);
                level.setBlock(pos3, state.setValue(X, 1).setValue(Z, 1), 3);
            }
        } else if (placementType == GummiPlacementType.MULTIBLOCK3D) {
            Direction facing = state.getValue(HORIZONTAL_FACING);
            BlockPos pos1 = pos.relative(facing);
            BlockPos pos2 = pos.relative(facing.getClockWise());
            BlockPos pos3 = pos.relative(facing).relative(facing.getClockWise());
            if (level.getBlockState(pos1).getBlock() == Blocks.AIR && level.getBlockState(pos2).getBlock() == Blocks.AIR && level.getBlockState(pos3).getBlock() == Blocks.AIR) {
                super.setPlacedBy(level, pos, state, placer, stack);
                level.setBlock(pos1, state.setValue(X, 1), 3);
                level.setBlock(pos2, state.setValue(Z, 1), 3);
                level.setBlock(pos3, state.setValue(X, 1).setValue(Z, 1), 3);
                level.setBlock(pos.relative(Direction.UP), state.setValue(Y, 1), 3);
                level.setBlock(pos1.relative(Direction.UP), state.setValue(X, 1).setValue(Y, 1), 3);
                level.setBlock(pos2.relative(Direction.UP), state.setValue(Z, 1).setValue(Y, 1), 3);
                level.setBlock(pos3.relative(Direction.UP), state.setValue(X, 1).setValue(Z, 1).setValue(Y, 1), 3);
            }
        } else {
            super.setPlacedBy(level, pos, state, placer, stack);
        }
    }

    private Quarter getQuarter(BlockPlaceContext context) {
        Direction direction = context.getClickedFace();
        BlockPos blockpos = context.getClickedPos();
        double horizontalClickPos = direction != Direction.EAST && direction != Direction.WEST ? context.getClickLocation().x - (double) blockpos.getX() : context.getClickLocation().z - (double) blockpos.getZ();
        double verticalClickPos = context.getClickLocation().y - (double) blockpos.getY();
        if (verticalClickPos <= 0.5 && horizontalClickPos <= 0.5) {
            return verticalClickPos < horizontalClickPos ? Quarter.BOTTOM : Quarter.LEFT;
        } else if (verticalClickPos <= 0.5 && horizontalClickPos > 0.5) {
            return verticalClickPos < 1 - horizontalClickPos ? Quarter.BOTTOM : Quarter.RIGHT;
        } else if (verticalClickPos > 0.5 && horizontalClickPos <= 0.5) {
            return 1 - verticalClickPos < horizontalClickPos ? Quarter.TOP : Quarter.LEFT;
        } else {
            return verticalClickPos > horizontalClickPos ? Quarter.TOP : Quarter.RIGHT;
        }
    }

    private static Corner getCorner(Direction direction, double verticalClickPos, double horizontalClickPos) {
        boolean bottom = verticalClickPos < 0.5;
        boolean left = horizontalClickPos < 0.5;
        if (direction == Direction.UP || direction == Direction.DOWN) {
            if (bottom) {
                return left ? Corner.CORNER1 : Corner.CORNER2;
            } else {
                return left ? Corner.CORNER4 : Corner.CORNER3;
            }
        } else {
            return switch (direction) {
                case NORTH -> left ? Corner.CORNER4 : Corner.CORNER3;
                case SOUTH -> left ? Corner.CORNER1 : Corner.CORNER2;
                case WEST -> left ? Corner.CORNER2 : Corner.CORNER3;
                case EAST -> left ? Corner.CORNER1 : Corner.CORNER4;
                default -> Corner.CORNER1;
            };
        }
    }

    @Override
    protected RenderShape getRenderShape(BlockState state) {
        if (placementType == GummiPlacementType.MULTIBLOCK2D) {
            if (state.getValue(X) == 0 && state.getValue(Z) == 0) {
                return RenderShape.MODEL;
            } else {
                return RenderShape.INVISIBLE;
            }
        } else if (placementType == GummiPlacementType.MULTIBLOCK3D) {
            if (state.getValue(X) == 0 && state.getValue(Y) == 0 && state.getValue(Z) == 0) {
                return RenderShape.MODEL;
            } else {
                return RenderShape.INVISIBLE;
            }
        } else {
            return super.getRenderShape(state);
        }
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        if (properties != null) {
            properties.forEach(builder::add);
        }
    }

    @Override
    protected BlockState rotate(BlockState state, Rotation rotation) {
        switch (placementType) {
            case CORNER -> {
                return switch (rotation) {
                    case NONE -> state;
                    case CLOCKWISE_90 -> state.setValue(CORNER, state.getValue(CORNER).next());
                    case CLOCKWISE_180 -> state.setValue(CORNER, state.getValue(CORNER).opposite());
                    case COUNTERCLOCKWISE_90 -> state.setValue(CORNER, state.getValue(CORNER).prev());
                };
            }
            case END -> {
                return state.setValue(FACING, rotation.rotate(state.getValue(FACING)));
            }
            case PILLAR -> {
                return rotatePillar(state, rotation);
            }
        }
        return super.rotate(state, rotation);
    }

    private static BlockState rotatePillar(BlockState state, Rotation rotation) {
        switch (rotation) {
            case COUNTERCLOCKWISE_90:
            case CLOCKWISE_90:
                switch (state.getValue(AXIS)) {
                    case X -> {
                        return state.setValue(AXIS, Direction.Axis.Z);
                    }
                    case Z -> {
                        return state.setValue(AXIS, Direction.Axis.X);
                    }
                    default -> {
                        return state;
                    }
                }
            default:
                return state;
        }
    }

    @Override
    protected BlockState mirror(BlockState state, Mirror mirror) {
        if (placementType == GummiPlacementType.END) {
            return state.setValue(FACING, mirror.mirror(state.getValue(FACING)));
        }
        return super.mirror(state, mirror);
    }

    @Override
    public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        //TODO more multiblock sizes
        if (placementType == GummiPlacementType.MULTIBLOCK2D || placementType == GummiPlacementType.MULTIBLOCK3D) {
            if (placementType == GummiPlacementType.MULTIBLOCK2D) {
                tooltipComponents.add(Component.translatable("kingdomkeys.gummi.block.shape_size_2x1x2").withStyle(ChatFormatting.GRAY));
            } else {
                tooltipComponents.add(Component.translatable("kingdomkeys.gummi.block.shape_size_2x2x2").withStyle(ChatFormatting.GRAY));
            }
            tooltipComponents.add(Component.translatable("kingdomkeys.gummi.block.place_corner").withStyle(ChatFormatting.GRAY));
        }
        super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
    }
}
