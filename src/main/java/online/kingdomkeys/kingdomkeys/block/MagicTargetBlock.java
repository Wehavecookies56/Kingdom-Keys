package online.kingdomkeys.kingdomkeys.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.BlockHitResult;
import online.kingdomkeys.kingdomkeys.entity.ModEntities;
import online.kingdomkeys.kingdomkeys.entity.block.MagicTargetBlockEntity;

public class MagicTargetBlock extends Block implements EntityBlock, INoDataGen {

    public static final MapCodec<MagicTargetBlock> CODEC = simpleCodec(MagicTargetBlock::new);
    public static final IntegerProperty OUTPUT_POWER = BlockStateProperties.POWER;
    public static final DirectionProperty FACING = BlockStateProperties.FACING;

    public MapCodec<MagicTargetBlock> codec() {
        return CODEC;
    }

    public MagicTargetBlock(BlockBehaviour.Properties properties) {
        super(properties);
        this.registerDefaultState((this.stateDefinition.any()).setValue(FACING, Direction.UP).setValue(OUTPUT_POWER, 0));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, OUTPUT_POWER);
    }

    private static void setOutputPower(LevelAccessor level, BlockState state, int power, BlockPos pos, int waitTime) {
        level.setBlock(pos, state.setValue(OUTPUT_POWER, power), 3);
        level.scheduleTick(pos, state.getBlock(), waitTime);
    }

    @Override
    protected void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        if (state.getValue(OUTPUT_POWER) != 0) {
            level.setBlock(pos, state.setValue(OUTPUT_POWER, 0), 3);
        }

    }

    @Override
    protected int getSignal(BlockState blockState, BlockGetter blockAccess, BlockPos pos, Direction side) {
        return blockState.getValue(OUTPUT_POWER);
    }

    @Override
    protected boolean isSignalSource(BlockState state) {
        return true;
    }


    @Override
    protected void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean isMoving) {
        if (!level.isClientSide() && !state.is(oldState.getBlock()) && state.getValue(OUTPUT_POWER) > 0 && !level.getBlockTicks().hasScheduledTick(pos, this)) {
            level.setBlock(pos, state.setValue(OUTPUT_POWER, 0), 18);
        }
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hit) {
        if (!level.isClientSide) {
            Direction next = getNextDirection(state.getValue(FACING));

            level.setBlock(pos, state.setValue(FACING, next), 3);

            BlockEntity be = level.getBlockEntity(pos);
            if (be instanceof MagicTargetBlockEntity target) {
                target.updateEntityPosition();
            }
        }

        return InteractionResult.SUCCESS;
    }

    private Direction getNextDirection(Direction dir) {
        return switch (dir) {
            case UP -> Direction.DOWN;
            case DOWN -> Direction.NORTH;
            case NORTH -> Direction.SOUTH;
            case SOUTH -> Direction.EAST;
            case EAST -> Direction.WEST;
            case WEST -> Direction.UP;
        };
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return ModEntities.TYPE_MAGIC_TARGET_TE.get().create(pPos, pState);
    }


    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        return level.isClientSide ? null : (lvl, pos, st, be) -> {
            if (be instanceof MagicTargetBlockEntity target) {
                MagicTargetBlockEntity.tick(lvl, pos, st, target);
            }
        };
    }
}