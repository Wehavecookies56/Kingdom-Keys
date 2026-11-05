package online.kingdomkeys.kingdomkeys.block;

import com.mojang.serialization.MapCodec;
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
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.util.TriState;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import online.kingdomkeys.kingdomkeys.entity.ModEntities;
import online.kingdomkeys.kingdomkeys.entity.block.TreasureChestTileEntity;
import online.kingdomkeys.kingdomkeys.item.KeybladeItem;
import org.jetbrains.annotations.Nullable;

public class TreasureChestBlock extends BaseEntityBlock implements INoDataGen {

    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
    public static final BooleanProperty BIG = BooleanProperty.create("big");


    private static final VoxelShape collisionShapeEW = Block.box(2.0D, 0.0D, 1.0D, 14.0D, 12.0D, 15.0D);
    private static final VoxelShape collisionShapeNS = Block.box(1.0D, 0.0D, 2.0D, 15.0D, 12.0D, 14.0D);

    public TreasureChestBlock(Properties properties) {
        super(properties);
        registerDefaultState(this.defaultBlockState().setValue(FACING, Direction.NORTH).setValue(BIG, false));
    }

    @Override
    protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        if (!(level.getBlockEntity(pos) instanceof TreasureChestTileEntity te)) {
            return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
        }

        // Creative + sneaking: opens the editor to put the chest contents inside
        if (te.canEdit(player)) {
            if (!level.isClientSide()) {
                te.openEditor((ServerPlayer) player);
            }
            return ItemInteractionResult.sidedSuccess(level.isClientSide());
        }

        // Trapped chests keep working with any click, loot chests need a keyblade
        if (!te.isTrapped() && !(stack.getItem() instanceof KeybladeItem)) {
            return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
        }

        if (!level.isClientSide()) {
            if (te.open(player)) {
                return ItemInteractionResult.CONSUME;
            }
        }
        return ItemInteractionResult.sidedSuccess(true);
    }

    private static boolean canEdit(Player player, Level level, BlockPos pos) {
        return level.getBlockEntity(pos) instanceof TreasureChestTileEntity te && te.canEdit(player);
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return simpleCodec(TreasureChestBlock::new);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return ModEntities.TYPE_TREASURE_CHEST.get().create(blockPos, blockState);
    }

    @javax.annotation.Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite()).setValue(BIG, false);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(FACING, BIG);
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        return getShape(state, world, pos, context);
    }

    @Deprecated
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
        if (state.getValue(FACING) == Direction.NORTH || state.getValue(FACING) == Direction.SOUTH) {
            return collisionShapeNS;
        } else {
            return collisionShapeEW;
        }
    }

    @EventBusSubscriber
    public static class Events {
        @SubscribeEvent
        public static void onRightClickBlock(PlayerInteractEvent.RightClickBlock event) {
            if (event.getLevel().getBlockState(event.getPos()).getBlock() instanceof TreasureChestBlock && canEdit(event.getEntity(), event.getLevel(), event.getPos())) {
                event.setUseBlock(TriState.TRUE);
            }
        }
    }
}
