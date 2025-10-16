package online.kingdomkeys.kingdomkeys.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.DyeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.Half;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.lib.Quarter;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Supplier;

public class RotatableGummiBlock extends GummiBlockBase {

    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
    public static final EnumProperty<Quarter> QUARTER = EnumProperty.create("quarter", Quarter.class);

    public RotatableGummiBlock(Properties properties, int armour, DyeColor color, List<Supplier<Block>> blocks) {
        super(properties, armour, color, blocks);
        registerDefaultState(defaultBlockState().setValue(FACING, Direction.NORTH).setValue(QUARTER, Quarter.BOTTOM));
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        Direction direction = context.getClickedFace();
        BlockPos blockpos = context.getClickedPos();
        if (direction == Direction.DOWN || direction == Direction.UP) {
            return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection()).setValue(QUARTER, direction == Direction.DOWN ? Quarter.TOP : Quarter.BOTTOM);
        } else {
            double horizontalClickPos = direction != Direction.EAST && direction != Direction.WEST ? context.getClickLocation().x - (double) blockpos.getX() : context.getClickLocation().z - (double) blockpos.getZ();
            double verticalClickPos = context.getClickLocation().y - (double) blockpos.getY();
            Quarter quarter = getQuarter(horizontalClickPos, verticalClickPos);
            if ((quarter == Quarter.LEFT || quarter == Quarter.RIGHT) && (direction == Direction.WEST || direction == Direction.SOUTH)) {
                quarter = quarter.opposite();
            }
            return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection()).setValue(QUARTER, quarter);
        }
    }

    private static Quarter getQuarter(double horizontalClickPos, double verticalClickPos) {
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

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(FACING);
        builder.add(QUARTER);
    }

    @Override
    protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        if (stack.getItem() instanceof DyeItem dyeItem) {
            DyeColor dyeColor = dyeItem.getDyeColor();
            Block b = blocks.get(dyeColor.getId()).get();
            level.setBlockAndUpdate(pos, b.defaultBlockState().setValue(FACING, state.getValue(FACING)).setValue(QUARTER, state.getValue(QUARTER)));
            player.swing(hand);
            if (!player.isCreative()) {
                stack.shrink(1);
            }
        }
        return super.useItemOn(stack, state, level, pos, player, hand, hitResult);
    }
}
