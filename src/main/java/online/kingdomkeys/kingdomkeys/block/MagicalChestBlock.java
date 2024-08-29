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
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
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
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import net.neoforged.neoforge.event.level.BlockEvent;
import net.neoforged.neoforge.items.IItemHandler;
import online.kingdomkeys.kingdomkeys.entity.ModEntities;
import online.kingdomkeys.kingdomkeys.entity.block.MagicalChestTileEntity;
import online.kingdomkeys.kingdomkeys.item.KeybladeItem;
import online.kingdomkeys.kingdomkeys.util.Utils;

import javax.annotation.Nullable;
import java.util.List;
import java.util.UUID;

public class MagicalChestBlock extends BaseEntityBlock implements INoDataGen {
	public static final DirectionProperty FACING = BlockStateProperties.FACING;
	public static final BooleanProperty BIG = BooleanProperty.create("big");

	private static final VoxelShape collisionShapeEW = Block.box(2.0D, 0.0D, 1.0D, 14.0D, 12.0D, 15.0D);
	private static final VoxelShape collisionShapeNS = Block.box(1.0D, 0.0D, 2.0D, 15.0D, 12.0D, 14.0D);

	public MagicalChestBlock(Properties properties) {
		super(properties);
		this.registerDefaultState(this.defaultBlockState().setValue(BIG, false));
	}

	@Override
	protected MapCodec<? extends BaseEntityBlock> codec() {
		return simpleCodec(MagicalChestBlock::new);
	}

	@OnlyIn(Dist.CLIENT)
	@Override
	public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
		tooltipComponents.add(Component.translatable("message.chest.can_be_locked"));
		super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
	}

	@Nullable
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

	@Override
	public void setPlacedBy(Level worldIn, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
		if (state.hasBlockEntity() && worldIn.getBlockEntity(pos) instanceof MagicalChestTileEntity) {
			MagicalChestTileEntity te = (MagicalChestTileEntity) worldIn.getBlockEntity(pos);
			if (te != null) {
				Player player = (Player) placer;
				te.setOwner(player.getGameProfile().getId());
				player.displayClientMessage(Component.translatable("message.chest.lock"), true);
			}
		}
		super.setPlacedBy(worldIn, pos, state, placer, stack);
	}

	@Override
	public ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
		if (level.isClientSide)
			return ItemInteractionResult.SUCCESS;

		MenuProvider namedContainerProvider = this.getMenuProvider(state, level, pos);
		if (namedContainerProvider != null) {
			if (!(player instanceof ServerPlayer))
				return ItemInteractionResult.FAIL;
			ServerPlayer serverPlayerEntity = (ServerPlayer) player;
			if (state.hasBlockEntity() && level.getBlockEntity(pos) instanceof MagicalChestTileEntity) {
				MagicalChestTileEntity te = (MagicalChestTileEntity) level.getBlockEntity(pos);
				if (te != null) {
					UUID keyblade = te.getKeyblade();
					ItemStack held = player.getItemInHand(hand);
					if (held.getItem() instanceof KeybladeItem) {
						UUID heldID = Utils.getKeybladeID(held);
						if (heldID != null) {
							if (keyblade != null) {
								if (heldID.equals(keyblade)) {
									serverPlayerEntity.openMenu(namedContainerProvider, buf -> {
										buf.writeBlockPos(pos);
									});
								} else {
									player.displayClientMessage(Component.translatable("message.chest.locked"), true);
									//you can't open it with that keyblade message
								}
							} else {
								//Set the keyblade ID to unlock
								te.setKeyblade(heldID);
								player.displayClientMessage(Component.translatable("message.chest.keyblade_set"), true);
							}
						} else if (keyblade == null) {
							//Chest is not locked and keyblade has no ID
							serverPlayerEntity.openMenu(namedContainerProvider, buf -> {
								buf.writeBlockPos(pos);
							});
						}
					} else if (keyblade == null) {
						//Chest is not locked so just open it
						serverPlayerEntity.openMenu(namedContainerProvider, buf -> {
							buf.writeBlockPos(pos);
						});
						return ItemInteractionResult.SUCCESS;
					} else {
						player.displayClientMessage(Component.translatable("message.chest.locked"), true);
					}
				}
			}

		}
		return super.useItemOn(stack, state, level, pos, player, hand, hitResult);
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

	public void onRemove(BlockState state, Level world, BlockPos pos, BlockState newState, boolean isMoving) {
		if (state.hasBlockEntity() && state.getBlock() != newState.getBlock()) {
			IItemHandler itemHandler = world.getCapability(Capabilities.ItemHandler.BLOCK, pos, null);
			if(itemHandler != null) {
				for (int i = 0; i < itemHandler.getSlots(); i++) {
					popResource(world, pos, itemHandler.getStackInSlot(i));
				}
			}
			world.removeBlockEntity(pos);
			super.onRemove(state, world, pos, newState, isMoving); // call it last, because it removes the TileEntity
		}
	}
	@Nullable
	@Override
	public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
		return ModEntities.TYPE_MAGICAL_CHEST.get().create(pPos, pState);
	}


	//Prevent block from breaking if player is not owner
	@EventBusSubscriber
	public static class Events {
		@SubscribeEvent
		public static void onBlockBreak(BlockEvent.BreakEvent event) {
			if (event.getState().getBlock() == ModBlocks.magicalChest.get()) {
				if (event.getState().hasBlockEntity()) {
					MagicalChestTileEntity te = (MagicalChestTileEntity) event.getLevel().getBlockEntity(event.getPos());
					if (te != null) {
						//If player is not the same as the owner AND the chest has any keyblade assigned AND the player is in survival
						if (te.getOwner() != null && !te.getOwner().equals(event.getPlayer().getGameProfile().getId()) && te.getKeyblade() != null && (event.getPlayer() != null && !event.getPlayer().isCreative())) {
							event.setCanceled(true);
						}
					}
				}
			}
		}

		//For sneaking interaction
		@SubscribeEvent
		public static void onBlockRightClick(PlayerInteractEvent.RightClickBlock event) {
			Player player = event.getEntity();
			if (player.isCrouching()) {
				ItemStack held = player.getItemInHand(event.getHand());
				if (held.getItem() instanceof KeybladeItem) {
					BlockPos pos = event.getPos();
					Level world = event.getLevel();
					BlockState state = world.getBlockState(pos);
					if (state.getBlock() == ModBlocks.magicalChest.get()) {
						MagicalChestTileEntity te = (MagicalChestTileEntity) world.getBlockEntity(pos);
						if (te != null) {
							if (te.getKeyblade() != null) {
								UUID heldID = Utils.getKeybladeID(held);
								if (heldID.equals(te.getKeyblade())) {
									te.setKeyblade(null);
									player.displayClientMessage(Component.translatable("message.chest.unlocked"), true);
								}
							}
						}
					}
				}
			}
		}

	}
}
