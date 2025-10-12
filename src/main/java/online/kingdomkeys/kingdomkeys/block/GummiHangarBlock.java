package online.kingdomkeys.kingdomkeys.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.SignalGetter;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import net.neoforged.neoforge.items.IItemHandler;
import online.kingdomkeys.kingdomkeys.entity.block.GummiHangarTileEntity;
import online.kingdomkeys.kingdomkeys.util.Utils;

import javax.annotation.Nullable;

@EventBusSubscriber
public class GummiHangarBlock extends BaseEntityBlock implements EntityBlock, INoDataGen {
	public static final DirectionProperty FACING = BlockStateProperties.FACING;
	public static final BooleanProperty SHOW_LINES = BooleanProperty.create("show_lines");
	public static final IntegerProperty SIZE = IntegerProperty.create("size",5,11); //5 S, 7 M, 9 L, 11 XL

	int size = 7;

	public GummiHangarBlock(Properties properties) {
		super(properties);
	}

	public void setSize(int size) {
		this.size = size;
	}

	@Override
	protected MapCodec<? extends BaseEntityBlock> codec() {
		return simpleCodec(GummiHangarBlock::new);
	}

	@Nullable
	@Override
	public BlockState getStateForPlacement(BlockPlaceContext context) {
		return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite()).setValue(SHOW_LINES,false).setValue(SIZE,size);
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		super.createBlockStateDefinition(builder);
		builder.add(FACING);
		builder.add(SHOW_LINES);
		builder.add(SIZE);
	}

	@Override
	public ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
		if (level.isClientSide)
			return ItemInteractionResult.SUCCESS;

		MenuProvider namedContainerProvider = this.getMenuProvider(state, level, pos);
		if (namedContainerProvider != null) {
			if (!(player instanceof ServerPlayer serverPlayerEntity))
				return ItemInteractionResult.FAIL;
            if (state.hasBlockEntity() && level.getBlockEntity(pos) instanceof GummiHangarTileEntity te) {
                if (te != null) {
					serverPlayerEntity.openMenu(namedContainerProvider, (packetBuffer) -> {
						packetBuffer.writeBlockPos(pos);
					});
				}
			}
		}
		return ItemInteractionResult.SUCCESS;
	}

	public void onRemove(BlockState state, Level world, BlockPos pos, BlockState newState, boolean isMoving) {
		if (state.hasBlockEntity() && state.getBlock() != newState.getBlock()) {
			GummiHangarTileEntity TE = (GummiHangarTileEntity) world.getBlockEntity(pos);
			if (TE != null) {
				IItemHandler iItemHandler = TE.inventory.get();
				if (iItemHandler != null) {
					for (int i = 0; i < iItemHandler.getSlots(); i++) {
						popResource(world, pos, iItemHandler.getStackInSlot(i));
					}
				}
			}
			world.removeBlockEntity(pos);
			super.onRemove(state, world, pos, newState, isMoving); // call it last, because it removes the TileEntity
		}
	}

	@Override
	public void neighborChanged(BlockState state, Level worldIn, BlockPos pos, Block blockIn, BlockPos fromPos, boolean b) {
		worldIn.setBlockAndUpdate(pos, state.setValue(SHOW_LINES, worldIn.hasNeighborSignal(pos)));
	}

	@Override
	public boolean shouldCheckWeakPower(BlockState state, SignalGetter level, BlockPos pos, Direction side) {
		return true;
	}

	@SubscribeEvent
	public static void onRightClickBlock(PlayerInteractEvent.RightClickBlock event) {
		if(event.getEntity() instanceof Player player) {
			Level level = event.getLevel();
			ItemStack stack = event.getItemStack();

			if (stack.getItem() == ModBlocks.gummiHangar.get().asItem()) {
				BlockPlaceContext context = new BlockPlaceContext(player, event.getHand(), stack, event.getHitVec());
				BlockPos placePos = context.getClickedPos();

				if (Utils.hasBlocks(level, placePos, player.getDirection().getOpposite(), 7)) {
					event.setCanceled(true);
					player.displayClientMessage(Component.literal("You can't place the Gummi Hangar here"), true);
					event.setCancellationResult(InteractionResult.FAIL);
				}
			}
		}
	}


	@Override
	public void onPlace(BlockState state, Level worldIn, BlockPos pos, BlockState oldState, boolean b) {

		if (oldState.getBlock() != state.getBlock()) {
			worldIn.setBlockAndUpdate(pos, state.setValue(SHOW_LINES, worldIn.hasNeighborSignal(pos)));
		}
	}

	@Override
	public void setPlacedBy(Level worldIn, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
		if (!worldIn.isClientSide && worldIn.getBlockEntity(pos) == null) {
			worldIn.setBlockAndUpdate(pos, state.setValue(SHOW_LINES, worldIn.hasNeighborSignal(pos)));
		}
	}

	@Deprecated
	public RenderShape getRenderShape(BlockState state) {
		return RenderShape.MODEL;
	}
	
	@Nullable
	@Override
	public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
		return new GummiHangarTileEntity(pPos, pState);
	}
}
