package online.kingdomkeys.kingdomkeys.block.gummi;

import com.mojang.serialization.MapCodec;
import net.minecraft.ChatFormatting;
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
import net.minecraft.world.level.Level;
import net.minecraft.world.level.SignalGetter;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.*;
import net.minecraft.world.phys.BlockHitResult;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import net.neoforged.neoforge.items.IItemHandler;
import online.kingdomkeys.kingdomkeys.block.INoDataGen;
import online.kingdomkeys.kingdomkeys.block.ModBlocks;
import online.kingdomkeys.kingdomkeys.config.ModConfigs;
import online.kingdomkeys.kingdomkeys.entity.ModEntities;
import online.kingdomkeys.kingdomkeys.entity.block.GummiHangarTileEntity;
import online.kingdomkeys.kingdomkeys.item.ICreativeTab;
import online.kingdomkeys.kingdomkeys.item.ModComponents;
import online.kingdomkeys.kingdomkeys.lib.LineDisplay;
import online.kingdomkeys.kingdomkeys.util.Utils;

import javax.annotation.Nullable;
import java.util.List;

@EventBusSubscriber
public class GummiHangarBlock extends BaseEntityBlock implements EntityBlock, INoDataGen, ICreativeTab {
	public static final DirectionProperty FACING = BlockStateProperties.FACING;
    public static final EnumProperty<LineDisplay> SHOW_LINES = EnumProperty.create("show_lines", LineDisplay.class);
	public static final BooleanProperty DISPLAY_BLUEPRINT = BooleanProperty.create("display_blueprint");
	public static final IntegerProperty LEVEL = IntegerProperty.create("size",0,10); //5 XS (0), 7 S (1), 9 M (2), 11 L (3), 13 XL (4), (rest are for command only)
    public static final BooleanProperty ACTIVE = BooleanProperty.create("active");

	public GummiHangarBlock(Properties properties) {
		super(properties);
	}

	public static int getSize(int level) {
		return 5+(level*2);
	}

	@Override
	protected MapCodec<? extends BaseEntityBlock> codec() {
		return simpleCodec(GummiHangarBlock::new);
	}

	@Nullable
	@Override
	public BlockState getStateForPlacement(BlockPlaceContext context) {
		return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite()).setValue(ACTIVE,false).setValue(SHOW_LINES,LineDisplay.OFF).setValue(LEVEL,0).setValue(DISPLAY_BLUEPRINT,false);
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		super.createBlockStateDefinition(builder);
		builder.add(FACING);
		builder.add(SHOW_LINES);
		builder.add(LEVEL);
		builder.add(DISPLAY_BLUEPRINT);
        builder.add(ACTIVE);
	}

    @Override
    public void neighborChanged(BlockState state, Level worldIn, BlockPos pos, Block blockIn, BlockPos fromPos, boolean b) {
        worldIn.setBlockAndUpdate(pos, state.setValue(ACTIVE, worldIn.hasNeighborSignal(pos)));
    }

    @Override
    public void onPlace(BlockState state, Level worldIn, BlockPos pos, BlockState oldState, boolean b) {
        if (oldState.getBlock() != state.getBlock()) {
            worldIn.setBlockAndUpdate(pos, state.setValue(ACTIVE, worldIn.hasNeighborSignal(pos)));
        }
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

	@Override
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

                world.removeBlockEntity(pos);
                ItemStack stack = new ItemStack(this);
                stack.set(ModComponents.HANGAR_LEVEL, state.getValue(LEVEL));
                stack.set(ModComponents.HANGAR_FUEL, TE.energyStorage.getEnergyStored());
                popResource(world, pos, stack);
            }
			super.onRemove(state, world, pos, newState, isMoving);
		}
	}

	@Override
	public boolean shouldCheckWeakPower(BlockState state, SignalGetter level, BlockPos pos, Direction side) {
		return true;
	}

	@SubscribeEvent
	public static void onRightClickBlock(PlayerInteractEvent.RightClickBlock event) {
		if(!ModConfigs.allowBlocksInHangarArea){
			if(event.getEntity() instanceof Player player) {
				Level level = event.getLevel();
				ItemStack stack = event.getItemStack();

				if (stack.getItem() == ModBlocks.gummiHangar.get().asItem()) {
					BlockPlaceContext context = new BlockPlaceContext(player, event.getHand(), stack, event.getHitVec());
					BlockPos placePos = context.getClickedPos();

					if (Utils.hasBlocks(level, placePos, player.getDirection().getOpposite(), 11)) {
						event.setCanceled(true);
						player.displayClientMessage(Component.literal("You can't place the Gummi Hangar here"), true);
						event.setCancellationResult(InteractionResult.FAIL);
					}
				}
			}
		}
	}

	@Override
	public void setPlacedBy(Level worldIn, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
		if (!worldIn.isClientSide){
			if(worldIn.getBlockEntity(pos) != null) {
				//Give lvl to the block
                if (stack.get(ModComponents.HANGAR_LEVEL) != null) {
                    worldIn.setBlockAndUpdate(pos, state.setValue(ACTIVE, worldIn.hasNeighborSignal(pos)).setValue(LEVEL, stack.get(ModComponents.HANGAR_LEVEL)));
                    if(worldIn.getBlockEntity(pos) instanceof GummiHangarTileEntity TE){
                        TE.energyStorage = Utils.getEnergyStoragePerLevel(stack.get(ModComponents.HANGAR_LEVEL));
						if (stack.get(ModComponents.HANGAR_FUEL) != null) {
							TE.energyStorage.setEnergy(stack.get(ModComponents.HANGAR_FUEL));
						}
                    }
                } else {
                    worldIn.setBlockAndUpdate(pos, state.setValue(ACTIVE, worldIn.hasNeighborSignal(pos)));
                }
			}
		}
	}

    @OnlyIn(Dist.CLIENT)
    @Override
    public void appendHoverText(ItemStack stack, Item.TooltipContext pContext, List<Component> tooltip, TooltipFlag pTooltipFlag) {
        if (stack.has(ModComponents.HANGAR_LEVEL)) {
            int level = stack.get(ModComponents.HANGAR_LEVEL);
            tooltip.add(Component.translatable(ChatFormatting.GRAY+"Tier ").append(ChatFormatting.GRAY+Utils.getHangarSizeFromLevel(level)));
        }

        if (stack.has(ModComponents.HANGAR_FUEL)) {
            int fuel = stack.get(ModComponents.HANGAR_FUEL);
            tooltip.add(Component.translatable(ChatFormatting.GRAY+"Stored fuel: ").append(""+ChatFormatting.GRAY+fuel));
        }
        super.appendHoverText(stack, pContext, tooltip, pTooltipFlag);
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

    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        return type == ModEntities.TYPE_GUMMI_HANGAR.get() ? GummiHangarTileEntity::tick : null;
    }

	@Override
	public Tab getTab() {
		return Tab.GUMMI;
	}
}
