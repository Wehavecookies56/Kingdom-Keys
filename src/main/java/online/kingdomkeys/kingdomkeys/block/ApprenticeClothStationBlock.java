package online.kingdomkeys.kingdomkeys.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.MenuProvider;
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
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.neoforged.neoforge.items.IItemHandler;
import online.kingdomkeys.kingdomkeys.entity.block.ApprenticeClothStationTileEntity;
import org.jetbrains.annotations.Nullable;

public class ApprenticeClothStationBlock extends BaseEntityBlock implements INoDataGen {
	public static final MapCodec<ApprenticeClothStationBlock> CODEC = simpleCodec(ApprenticeClothStationBlock::new);
	public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;

	private static final VoxelShape SHAPE = Block.box(0.0D, 0.0D, 0.0D, 16.0D, 16.0D, 16.0D);

	public ApprenticeClothStationBlock(Properties properties) {
		super(properties);
		this.registerDefaultState(this.defaultBlockState().setValue(FACING, Direction.NORTH));
	}

	@Override
	protected MapCodec<? extends BaseEntityBlock> codec() {
		return CODEC;
	}

	@Override
	public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
		return SHAPE;
	}

	@Override
	public RenderShape getRenderShape(BlockState state) {
		return RenderShape.MODEL;
	}

	@Nullable
	@Override
	public BlockState getStateForPlacement(BlockPlaceContext context) {
		return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite());
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(FACING);
	}

	@Nullable
	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return new ApprenticeClothStationTileEntity(pos, state);
	}

	@Override
	public ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
		if (level.isClientSide) {
			return ItemInteractionResult.SUCCESS;
		}
		MenuProvider provider = this.getMenuProvider(state, level, pos);
		if (provider != null && player instanceof ServerPlayer serverPlayer) {
			serverPlayer.openMenu(provider, buf -> buf.writeBlockPos(pos));
		}
		return ItemInteractionResult.SUCCESS;
	}

	@Override
	public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving) {
		if (state.hasBlockEntity() && state.getBlock() != newState.getBlock()) {
			BlockEntity be = level.getBlockEntity(pos);
			if (be instanceof ApprenticeClothStationTileEntity station) {
				IItemHandler inv = station.inventory.get();
				if (inv != null) {
					for (int i = 0; i < inv.getSlots(); i++) {
						// Do not drop the computed output slot (last) to avoid dupes
						if (i == ApprenticeClothStationTileEntity.OUTPUT_SLOT) {
							continue;
						}
						popResource(level, pos, inv.getStackInSlot(i));
					}
				}
			}
			level.removeBlockEntity(pos);
		}
		super.onRemove(state, level, pos, newState, isMoving);
	}
}
