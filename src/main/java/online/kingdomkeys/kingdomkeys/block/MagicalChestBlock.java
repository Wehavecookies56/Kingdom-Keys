package online.kingdomkeys.kingdomkeys.block;

import java.util.List;
import java.util.UUID;

import javax.annotation.Nullable;

import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.ContainerBlock;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.network.NetworkHooks;
import net.minecraftforge.items.CapabilityItemHandler;
import online.kingdomkeys.kingdomkeys.entity.ModEntities;
import online.kingdomkeys.kingdomkeys.entity.block.MagicalChestTileEntity;
import online.kingdomkeys.kingdomkeys.item.KeybladeItem;
import online.kingdomkeys.kingdomkeys.util.Utils;

//TODO localisable messages
public class MagicalChestBlock extends ContainerBlock {
	public static final DirectionProperty FACING = BlockStateProperties.FACING;
	public static final BooleanProperty BIG = BooleanProperty.create("big");

	private static final VoxelShape collisionShapeEW = Block.makeCuboidShape(2.0D, 0.0D, 1.0D, 14.0D, 12.0D, 15.0D);
	private static final VoxelShape collisionShapeNS = Block.makeCuboidShape(1.0D, 0.0D, 2.0D, 15.0D, 12.0D, 14.0D);

	public MagicalChestBlock(Properties properties) {
		super(properties);
		this.setDefaultState(this.getDefaultState().with(BIG, false));
	}

	@OnlyIn(Dist.CLIENT)
	@Override
	public void addInformation(ItemStack stack, @Nullable IBlockReader worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
		tooltip.add(new TranslationTextComponent("Can be locked with a keyblade"));
		super.addInformation(stack, worldIn, tooltip, flagIn);
	}

	@Nullable
	@Override
	public BlockState getStateForPlacement(BlockItemUseContext context) {
		return this.getDefaultState().with(FACING, context.getPlacementHorizontalFacing().getOpposite()).with(BIG, false);
	}

	@Override
	protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
		super.fillStateContainer(builder);
		builder.add(FACING, BIG);
	}

	@Override
	public VoxelShape getCollisionShape(BlockState state, IBlockReader world, BlockPos pos, ISelectionContext context) {
		return getShape(state, world, pos, context);
	}

	@Override
	public void onBlockPlacedBy(World worldIn, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
		if (state.hasTileEntity() && worldIn.getTileEntity(pos) instanceof MagicalChestTileEntity) {
			MagicalChestTileEntity te = (MagicalChestTileEntity) worldIn.getTileEntity(pos);
			if (te != null) {
				PlayerEntity player = (PlayerEntity) placer;
				te.setOwner(player.getGameProfile().getId());
				player.sendStatusMessage(new TranslationTextComponent("message.chest.lock"), true);
			}
		}
		super.onBlockPlacedBy(worldIn, pos, state, placer, stack);
	}

	@Override
	public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit) {
		if (worldIn.isRemote)
			return ActionResultType.SUCCESS;

		INamedContainerProvider namedContainerProvider = this.getContainer(state, worldIn, pos);
		if (namedContainerProvider != null) {
			if (!(player instanceof ServerPlayerEntity))
				return ActionResultType.FAIL;
			ServerPlayerEntity serverPlayerEntity = (ServerPlayerEntity) player;
			if (state.hasTileEntity() && worldIn.getTileEntity(pos) instanceof MagicalChestTileEntity) {
				MagicalChestTileEntity te = (MagicalChestTileEntity) worldIn.getTileEntity(pos);
				if (te != null) {
					UUID keyblade = te.getKeyblade();
					ItemStack held = player.getHeldItem(handIn);
					if (held.getItem() instanceof KeybladeItem) {
						UUID heldID = Utils.getID(held);
						if (heldID != null) {
							if (keyblade != null) {
								if (heldID.equals(keyblade)) {
									NetworkHooks.openGui(serverPlayerEntity, namedContainerProvider, buf -> {
										buf.writeBlockPos(pos);
									});
								} else {
									player.sendStatusMessage(new TranslationTextComponent("message.chest.locked"), true);
									//you can't open it with that keyblade message
								}
							} else {
								//Set the keyblade ID to unlock
								te.setKeyblade(heldID);
								player.sendStatusMessage(new TranslationTextComponent("message.chest.keyblade_set"), true);
							}
						} else if (keyblade == null) {
							//Chest is not locked and keyblade has no ID
							NetworkHooks.openGui(serverPlayerEntity, namedContainerProvider, buf -> {
								buf.writeBlockPos(pos);
							});
						}
					} else if (keyblade == null) {
						//Chest is not locked so just open it
						NetworkHooks.openGui(serverPlayerEntity, namedContainerProvider, buf -> {
							buf.writeBlockPos(pos);
						});
						return ActionResultType.SUCCESS;
					} else {
						player.sendStatusMessage(new TranslationTextComponent("message.chest.locked"), true);
					}
				}
			}

		}
		return super.onBlockActivated(state, worldIn, pos, player, handIn, hit);
	}

	@Deprecated
	public BlockRenderType getRenderType(BlockState state) {
		return BlockRenderType.MODEL;
	}

	@Override
	public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
		if (state.get(FACING) == Direction.NORTH || state.get(FACING) == Direction.SOUTH) {
			return collisionShapeNS;
		} else {
			return collisionShapeEW;
		}
	}

	public void onReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean isMoving) {
		if (state.hasTileEntity() && state.getBlock() != newState.getBlock()) {
			world.getTileEntity(pos).getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent(inv -> {
				for (int i = 0; i < inv.getSlots(); i++) {
					spawnAsEntity(world, pos, inv.getStackInSlot(i));
				}
			});
			world.removeTileEntity(pos);
			super.onReplaced(state, world, pos, newState, isMoving); // call it last, because it removes the TileEntity
		}
	}

	@Override
	public boolean hasTileEntity(BlockState state) {
		return true;
	}

	@Nullable
	@Override
	public TileEntity createTileEntity(BlockState state, IBlockReader world) {
		return createNewTileEntity(world);
	}

	@Nullable
	@Override
	public TileEntity createNewTileEntity(IBlockReader worldIn) {
		return ModEntities.TYPE_MAGICAL_CHEST.get().create();
	}

	//Prevent block from breaking if player is not owner
	@Mod.EventBusSubscriber
	public static class Events {
		@SubscribeEvent
		public static void onBlockBreak(BlockEvent.BreakEvent event) {
			if (event.getState().getBlock() == ModBlocks.magicalChest.get()) {
				if (event.getState().hasTileEntity()) {
					MagicalChestTileEntity te = (MagicalChestTileEntity) event.getWorld().getTileEntity(event.getPos());
					if (te != null) {
						//If player is not the same as the owner AND the chest has any keyblade assigned AND the player is in survival
						if (!te.getOwner().equals(event.getPlayer().getGameProfile().getId()) && te.getKeyblade() != null && (event.getPlayer() != null && !event.getPlayer().isCreative())) {
							event.setCanceled(true);
						}
					}
				}
			}
		}

		//For sneaking interaction
		@SubscribeEvent
		public static void onBlockRightClick(PlayerInteractEvent.RightClickBlock event) {
			PlayerEntity player = event.getPlayer();
			if (player.isCrouching()) {
				ItemStack held = player.getHeldItem(event.getHand());
				if (held.getItem() instanceof KeybladeItem) {
					BlockPos pos = event.getPos();
					World world = event.getWorld();
					BlockState state = world.getBlockState(pos);
					if (state.getBlock() == ModBlocks.magicalChest.get()) {
						MagicalChestTileEntity te = (MagicalChestTileEntity) world.getTileEntity(pos);
						if (te != null) {
							if (te.getKeyblade() != null) {
								UUID heldID = Utils.getID(held);
								if (heldID.equals(te.getKeyblade())) {
									te.setKeyblade(null);
									player.sendStatusMessage(new TranslationTextComponent("message.chest.unlocked"), true);
								}
							}
						}
					}
				}
			}
		}

	}
}
