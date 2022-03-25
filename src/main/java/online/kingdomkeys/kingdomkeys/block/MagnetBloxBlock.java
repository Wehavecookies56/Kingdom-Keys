package online.kingdomkeys.kingdomkeys.block;

import javax.annotation.Nullable;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.InteractionResult;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.Level;
import online.kingdomkeys.kingdomkeys.entity.ModEntities;
import online.kingdomkeys.kingdomkeys.entity.block.MagnetBloxTileEntity;
import online.kingdomkeys.kingdomkeys.util.Utils;

import net.minecraft.world.level.block.state.BlockBehaviour.Properties;

public class MagnetBloxBlock extends BaseBlock implements EntityBlock {

    private static int min = 1, max = 10;

    public static final DirectionProperty FACING = BlockStateProperties.FACING;
    public static final IntegerProperty RANGE = IntegerProperty.create("range", min, max);
    public static final BooleanProperty ATTRACT = BooleanProperty.create("attract");
    public static final BooleanProperty ACTIVE = BooleanProperty.create("active");

    public MagnetBloxBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.defaultBlockState().setValue(FACING, Direction.NORTH).setValue(RANGE, min).setValue(ATTRACT, true).setValue(ACTIVE, false));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(FACING, RANGE, ATTRACT, ACTIVE);
    }

    @Override
    public boolean canConnectRedstone(BlockState state, BlockGetter world, BlockPos pos, @Nullable Direction side) {
        return side != state.getValue(FACING);
    }

    @Override
    public void onPlace(BlockState state, Level world, BlockPos pos, BlockState oldState, boolean isMoving) {

    }

    @Override
    public void setPlacedBy(Level world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {

    }

    @Override
    public void neighborChanged(BlockState state, Level worldIn, BlockPos pos, Block blockIn, BlockPos fromPos, boolean isMoving) {
        worldIn.setBlockAndUpdate(pos, state.setValue(ACTIVE, worldIn.hasNeighborSignal(pos)));
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState().setValue(FACING, context.getNearestLookingDirection().getOpposite()).setValue(ATTRACT, true).setValue(RANGE, min).setValue(ACTIVE, context.getLevel().hasNeighborSignal(context.getClickedPos()));
    }

    @Override
    public boolean shouldCheckWeakPower(BlockState state, LevelReader world, BlockPos pos, Direction side) {
        return true;
    }

    @Override
    public InteractionResult use(BlockState state, Level worldIn, BlockPos pos, Player player, InteractionHand handIn, BlockHitResult hit) {
        if (player.isCrouching()) {
            worldIn.setBlockAndUpdate(pos, state.setValue(ATTRACT, !state.getValue(ATTRACT)));
            String message = state.getValue(ATTRACT) ? "message.magnet_blox.repel" : "message.magnet_blox.attract";
            message = Utils.translateToLocal(message);
            ChatFormatting formatting = state.getValue(ATTRACT) ? ChatFormatting.BLUE : ChatFormatting.RED;
            message = formatting + message;
            player.displayClientMessage(new TextComponent(message), true);
        } else {
            int newRange = state.getValue(RANGE) + 1;
            if (state.getValue(RANGE) == max) {
                newRange = min;
            }
            worldIn.setBlockAndUpdate(pos, state.setValue(RANGE, newRange));
            player.displayClientMessage(new TranslatableComponent("message.magnet_blox.range", newRange), true);
        }
        return InteractionResult.SUCCESS;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return ModEntities.TYPE_MAGNET_BLOX.get().create(pPos, pState);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level pLevel, BlockState pState, BlockEntityType<T> pBlockEntityType) {
        return pLevel.isClientSide ? null : (level, pos, state, blockEntity) -> ((MagnetBloxTileEntity)blockEntity).tick();
    }
}
