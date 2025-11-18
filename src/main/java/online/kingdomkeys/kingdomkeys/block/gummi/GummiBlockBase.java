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
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.*;
import net.minecraft.world.phys.BlockHitResult;
import online.kingdomkeys.kingdomkeys.block.BaseBlock;
import online.kingdomkeys.kingdomkeys.item.ICreativeTab;
import online.kingdomkeys.kingdomkeys.lib.Corner;
import online.kingdomkeys.kingdomkeys.lib.Quarter;

import javax.annotation.Nullable;
import java.util.*;
import java.util.function.Supplier;

public class GummiBlockBase extends BaseBlock implements ICreativeTab {

    DyeColor color;
    List<Supplier<Block>> blocks;
    int armour, weight, cost;
    GummiPlacementType placementType;
    boolean isMultiBlock;

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
                case STANDARD -> state;
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
                tooltipComponents.add(Component.translatable(ChatFormatting.GRAY + "Shape size: 2x1x2"));
            } else {
                tooltipComponents.add(Component.translatable(ChatFormatting.GRAY + "Shape size: 2x2x2"));
            }
            tooltipComponents.add(Component.translatable(ChatFormatting.GRAY+"Place in the bottom-left corner of the area for correct orientation"));
        }
        super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
    }
}
