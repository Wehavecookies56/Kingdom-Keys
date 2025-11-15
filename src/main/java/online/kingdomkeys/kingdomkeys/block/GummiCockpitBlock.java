package online.kingdomkeys.kingdomkeys.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.DyeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import online.kingdomkeys.kingdomkeys.lib.Quarter;
import org.jetbrains.annotations.Nullable;
import yesman.epicfight.api.utils.math.Vec3f;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class GummiCockpitBlock extends GummiBlockBase {

    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
    public static final IntegerProperty X = IntegerProperty.create("x", 0, 1);
    public static final IntegerProperty Y = IntegerProperty.create("y", 0, 1);
    public static final IntegerProperty Z = IntegerProperty.create("z", 0, 1);

    List<Vec3> seats;

    public GummiCockpitBlock(Properties properties, int weight, int armour, DyeColor color, List<Supplier<Block>> blocks, List<Vec3> seats) {
        super(properties.pushReaction(PushReaction.IGNORE), weight, armour, color, blocks);
        this.seats = seats;
    }

    public List<Vec3> getSeats(){
        return seats;
    }

    public Vec3 getSeat(int i){
        return seats.get(i);
    }

    public int getMaxSeats(){
        return seats.size();
    }

    @Override
    protected void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean movedByPiston) {
        super.onRemove(state, level, pos, newState, movedByPiston);
        if (newState.getBlock() == Blocks.AIR) {
            Direction facing = state.getValue(FACING);
            Direction xDir = state.getValue(X) == 0 ? facing : facing.getOpposite();
            Direction yDir = state.getValue(Y) == 0 ? Direction.UP : Direction.DOWN;
            Direction zDir = state.getValue(Z) == 0 ? facing.getClockWise() : facing.getCounterClockWise();
            BlockPos pos1 = pos.relative(xDir);
            BlockPos pos2 = pos.relative(zDir);
            BlockPos pos3 = pos.relative(xDir).relative(zDir);
            if (level.getBlockState(pos1).getBlock() instanceof GummiCockpitBlock) {
                level.setBlock(pos1, Blocks.AIR.defaultBlockState(), 3);
            }
            if (level.getBlockState(pos2).getBlock() instanceof GummiCockpitBlock) {
                level.setBlock(pos2, Blocks.AIR.defaultBlockState(), 3);
            }
            if (level.getBlockState(pos3).getBlock() instanceof GummiCockpitBlock) {
                level.setBlock(pos3, Blocks.AIR.defaultBlockState(), 3);
            }
            if (level.getBlockState(pos1.relative(yDir)).getBlock() instanceof GummiCockpitBlock) {
                level.setBlock(pos1.relative(yDir), Blocks.AIR.defaultBlockState(), 3);
            }
            if (level.getBlockState(pos2.relative(yDir)).getBlock() instanceof GummiCockpitBlock) {
                level.setBlock(pos2.relative(yDir), Blocks.AIR.defaultBlockState(), 3);
            }
            if (level.getBlockState(pos3.relative(yDir)).getBlock() instanceof GummiCockpitBlock) {
                level.setBlock(pos3.relative(yDir), Blocks.AIR.defaultBlockState(), 3);
            }
        }
    }

    @Override
    public void setPlacedBy(Level level, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
        Direction facing = state.getValue(FACING);
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
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        Level level = context.getLevel();
        Direction facing = context.getHorizontalDirection();
        BlockPos pos = context.getClickedPos();
        BlockPos pos1 = pos.relative(facing);
        BlockPos pos2 = pos.relative(facing.getClockWise());
        BlockPos pos3 = pos.relative(facing).relative(facing.getClockWise());
        if (level.getBlockState(pos1).getBlock() == Blocks.AIR && level.getBlockState(pos2).getBlock() == Blocks.AIR && level.getBlockState(pos3).getBlock() == Blocks.AIR && level.getBlockState(pos1.relative(Direction.UP)).getBlock() == Blocks.AIR && level.getBlockState(pos2.relative(Direction.UP)).getBlock() == Blocks.AIR && level.getBlockState(pos3.relative(Direction.UP)).getBlock() == Blocks.AIR) {
            return defaultBlockState().setValue(FACING, facing);
        } else {
            return null;
        }
    }

    @Override
    protected RenderShape getRenderShape(BlockState state) {
        if (state.getValue(X) == 0 && state.getValue(Y) == 0 && state.getValue(Z) == 0) {
            return RenderShape.MODEL;
        } else {
            return RenderShape.INVISIBLE;
        }
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(FACING);
        builder.add(X);
        builder.add(Y);
        builder.add(Z);
    }

    @Override
    protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        if (blocks != null && stack.getItem() instanceof DyeItem dyeItem) {
            DyeColor dyeColor = dyeItem.getDyeColor();
            Block b = blocks.get(dyeColor.getId()).get();
            level.setBlockAndUpdate(pos, b.defaultBlockState().setValue(FACING,  state.getValue(FACING)).setValue(X, state.getValue(X)).setValue(Y, state.getValue(Y)).setValue(Z, state.getValue(Z)));
            player.swing(hand);
            if (!player.isCreative()) {
                stack.shrink(1);
            }
        }
        return super.useItemOn(stack, state, level, pos, player, hand, hitResult);
    }
}
