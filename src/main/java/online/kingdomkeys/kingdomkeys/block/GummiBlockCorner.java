package online.kingdomkeys.kingdomkeys.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.DyeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.Half;
import net.minecraft.world.phys.BlockHitResult;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.lib.Corner;
import online.kingdomkeys.kingdomkeys.lib.Quarter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Supplier;

public class GummiBlockCorner extends GummiBlockBase {

    public static final EnumProperty<Corner> CORNER = EnumProperty.create("corner", Corner.class);
    public static final EnumProperty<Half> HALF = BlockStateProperties.HALF;

    public GummiBlockCorner(Properties properties, int weight, int armour, DyeColor color, List<Supplier<Block>> blocks) {
        super(properties, weight, armour, color, blocks);
        registerDefaultState(defaultBlockState().setValue(CORNER, Corner.CORNER1).setValue(HALF, Half.BOTTOM));
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        Direction direction = context.getClickedFace();
        BlockPos blockpos = context.getClickedPos();

        double horizontalClickPos = direction != Direction.EAST && direction != Direction.WEST ? context.getClickLocation().x - (double) blockpos.getX() : context.getClickLocation().z - (double) blockpos.getZ();
        double verticalClickPos = direction == Direction.UP || direction == Direction.DOWN ? context.getClickLocation().z - (double) blockpos.getZ() : context.getClickLocation().y - (double) blockpos.getY();

        return this.defaultBlockState().setValue(CORNER, getCorner(direction, verticalClickPos, horizontalClickPos)).setValue(HALF, direction == Direction.DOWN || (direction != Direction.UP && verticalClickPos > 0.5) ? Half.TOP : Half.BOTTOM);
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
    protected BlockState rotate(BlockState state, Rotation rotation) {
        return switch (rotation) {
            case NONE -> state;
            case CLOCKWISE_90 -> state.setValue(CORNER, state.getValue(CORNER).next());
            case CLOCKWISE_180 -> state.setValue(CORNER, state.getValue(CORNER).opposite());
            case COUNTERCLOCKWISE_90 -> state.setValue(CORNER, state.getValue(CORNER).prev());
        };
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(CORNER);
        builder.add(HALF);
    }

    @Override
    protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        if (stack.getItem() instanceof DyeItem dyeItem) {
            DyeColor dyeColor = dyeItem.getDyeColor();
            Block b = blocks.get(dyeColor.getId()).get();
            level.setBlockAndUpdate(pos, b.defaultBlockState().setValue(CORNER, state.getValue(CORNER)).setValue(HALF, state.getValue(HALF)));
            player.swing(hand);
            if (!player.isCreative()) {
                stack.shrink(1);
            }
        }
        return super.useItemOn(stack, state, level, pos, player, hand, hitResult);
    }
}
