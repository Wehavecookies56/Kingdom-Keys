package online.kingdomkeys.kingdomkeys.block;

import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import online.kingdomkeys.kingdomkeys.lib.Quarter;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class GummiWeaponMultiBlock extends GummiWeaponBlock {

    public static final IntegerProperty X = IntegerProperty.create("x", 0, 1);
    public static final IntegerProperty Z = IntegerProperty.create("z", 0, 1);

    public GummiWeaponMultiBlock(Properties properties, ShotType shotType, int weight, int armour, int firepower, int fuelPerShot) {
        super(properties.pushReaction(PushReaction.IGNORE), shotType, weight, armour, firepower, fuelPerShot);
    }

    //TODO change for a dynamic number
    public float[] getOffsetToCannon(){
        return new float[]{0.5F,0,0.5F};
    }

    @Override
    protected void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean movedByPiston) {
        super.onRemove(state, level, pos, newState, movedByPiston);
        if (newState.getBlock() == Blocks.AIR) {
            Quarter quarter = state.getValue(QUARTER);
            Direction facing = state.getValue(FACING);
            Direction xDir = state.getValue(X) == 0 ? facing : facing.getOpposite();
            Direction zDir = quarter == Quarter.BOTTOM || quarter == Quarter.TOP ? state.getValue(Z) == 0 ? facing.getClockWise() : facing.getCounterClockWise() : state.getValue(Z) == 0 ? Direction.UP : Direction.DOWN;
            if (quarter == Quarter.TOP || quarter == Quarter.RIGHT) {
                zDir = zDir.getOpposite();
            }
            BlockPos pos1 = pos.relative(xDir);
            BlockPos pos2 = pos.relative(zDir);
            BlockPos pos3 = pos.relative(xDir).relative(zDir);
            if (level.getBlockState(pos1).getBlock() instanceof GummiWeaponMultiBlock) {
                level.setBlock(pos1, Blocks.AIR.defaultBlockState(), 3);
            }
            if (level.getBlockState(pos2).getBlock() instanceof GummiWeaponMultiBlock) {
                level.setBlock(pos2, Blocks.AIR.defaultBlockState(), 3);
            }
            if (level.getBlockState(pos3).getBlock() instanceof GummiWeaponMultiBlock) {
                level.setBlock(pos3, Blocks.AIR.defaultBlockState(), 3);
            }
        }
    }

    @Override
    public void setPlacedBy(Level level, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
        Quarter quarter = state.getValue(QUARTER);
        Direction facing = state.getValue(FACING);
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
    }

    @Override
    public @Nullable BlockState getStateForPlacement(BlockPlaceContext context) {
        Level level = context.getLevel();
        BlockPos pos = context.getClickedPos();
        Direction face = context.getClickedFace();
        Direction facing = context.getHorizontalDirection();
        Direction zDir = face == Direction.UP || face == Direction.DOWN ? facing.getClockWise() : Direction.UP;
        if (face == Direction.DOWN) {
            zDir = zDir.getOpposite();
        }
        Quarter quarter = Quarter.BOTTOM;
        if (face == Direction.DOWN) {
            quarter = Quarter.TOP;
        } else if (face == Direction.NORTH || face == Direction.EAST) {
            quarter = Quarter.RIGHT;
        } else if (face == Direction.SOUTH || face == Direction.WEST) {
            quarter = Quarter.LEFT;
        }
        if ((quarter != Quarter.TOP && quarter != Quarter.BOTTOM) && (facing == Direction.EAST || facing == Direction.SOUTH)) {
            quarter = quarter.opposite();
        }
        if (quarter == Quarter.RIGHT) {
            zDir = zDir.getOpposite();
        }
        BlockPos pos1 = pos.relative(facing, 1);
        BlockPos pos2 = pos.relative(zDir, 1);
        BlockPos pos3 = pos.relative(facing, 1).relative(zDir, 1);
        if (level.getBlockState(pos1).getBlock() == Blocks.AIR && level.getBlockState(pos2).getBlock() == Blocks.AIR && level.getBlockState(pos3).getBlock() == Blocks.AIR) {
            return defaultBlockState().setValue(FACING, facing).setValue(QUARTER, quarter);
        } else {
            return null;
        }
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(X);
        builder.add(Z);
    }

    @Override
    protected RenderShape getRenderShape(BlockState state) {
        if (state.getValue(X) == 0 && state.getValue(Z) == 0) {
            return RenderShape.MODEL;
        } else {
            return RenderShape.INVISIBLE;
        }
    }

    @Override
    public void neighborChanged(BlockState state, Level level, BlockPos pos, Block blockIn, BlockPos fromPos, boolean b) {
        if (canConnectRedstone(state, level, pos, null)) {
            boolean powered = level.hasNeighborSignal(pos);
            boolean oldPowered = state.getValue(ACTIVE);
            level.setBlockAndUpdate(pos, state.setValue(ACTIVE, powered));
            if (!level.isClientSide() && !oldPowered && powered) {
                shoot(null, level, null, new Vec3(pos.getX(), pos.getY(), pos.getZ()), null);
            }
        }
    }

    @Override
    public boolean canConnectRedstone(BlockState state, BlockGetter level, BlockPos pos, @Nullable Direction direction) {
        return state.getValue(X) == 0 && state.getValue(Z) == 0;
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void appendHoverText(ItemStack stack, Item.TooltipContext tooltipContext, List<Component> tooltip, TooltipFlag flagIn) {
        tooltip.add(Component.translatable(ChatFormatting.GRAY+"Shape size: 2x1x2"));
        super.appendHoverText(stack, tooltipContext, tooltip, flagIn);
    }
}
