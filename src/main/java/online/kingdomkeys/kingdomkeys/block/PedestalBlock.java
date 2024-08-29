package online.kingdomkeys.kingdomkeys.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.network.NetworkHooks;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.items.IItemHandler;
import online.kingdomkeys.kingdomkeys.data.ModData;
import online.kingdomkeys.kingdomkeys.entity.ModEntities;
import online.kingdomkeys.kingdomkeys.entity.block.PedestalTileEntity;
import online.kingdomkeys.kingdomkeys.lib.SoAState;
import online.kingdomkeys.kingdomkeys.network.PacketHandler;
import online.kingdomkeys.kingdomkeys.network.stc.SCOpenChoiceScreen;

import javax.annotation.Nullable;

public class PedestalBlock extends BaseEntityBlock implements INoDataGen {

	public PedestalBlock(Properties properties) {
		super(properties);
	}

	@Override
	protected MapCodec<? extends BaseEntityBlock> codec() {
		return simpleCodec(PedestalBlock::new);
	}

	private static final VoxelShape collision = Block.box(0.0D, 0.0D, 0.0D, 16.0D, 14.0D, 16.0D);

	@Override
	public VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
		return collision;
	}

	@Override
	public VoxelShape getCollisionShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
		return getShape(state, worldIn, pos, context);
	}

	@Override
	public ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level worldIn, BlockPos pos, Player player, InteractionHand hand, BlockHitResult rayTraceResult) {
		if (worldIn.isClientSide)
			return ItemInteractionResult.SUCCESS;

		MenuProvider namedContainerProvider = this.getMenuProvider(state, worldIn, pos);
		if (namedContainerProvider != null) {
			if (!(player instanceof ServerPlayer))
				return ItemInteractionResult.FAIL;
			ServerPlayer serverPlayerEntity = (ServerPlayer) player;
			if (state.hasBlockEntity() && worldIn.getBlockEntity(pos) instanceof PedestalTileEntity) {
				PedestalTileEntity te = (PedestalTileEntity) worldIn.getBlockEntity(pos);
				if (te != null) {
					if (te.isStationOfAwakeningMarker()) {
						IPlayerData playerData = ModData.getPlayer(player);
						SoAState soAState = playerData.getSoAState();
						if (soAState == SoAState.CHOICE || (soAState == SoAState.SACRIFICE && (!playerData.getChoicePedestal().equals(pos)))) {
							PacketHandler.sendTo(new SCOpenChoiceScreen(te.getDisplayStack(), soAState, pos), serverPlayerEntity);
						} else {
							return ItemInteractionResult.FAIL;
						}
					} else {
						serverPlayerEntity.openMenu(namedContainerProvider, (packetBuffer) -> {
							packetBuffer.writeBlockPos(pos);
						});
					}
				}
			}
		}
		return ItemInteractionResult.SUCCESS;
	}

	public void onRemove(BlockState state, Level world, BlockPos pos, BlockState newState, boolean isMoving) {
		if (state.hasBlockEntity() && state.getBlock() != newState.getBlock()) {
			IItemHandler itemHandler = world.getCapability(Capabilities.ItemHandler.BLOCK, pos, null);
			if (itemHandler != null) {
				for (int i = 0; i < itemHandler.getSlots(); i++) {
					popResource(world, pos, itemHandler.getStackInSlot(i));
				}
			}
			world.removeBlockEntity(pos);
			super.onRemove(state, world, pos, newState, isMoving); // call it last, because it removes the TileEntity
		}
	}

	@Deprecated
	public RenderShape getRenderShape(BlockState state) {
		return RenderShape.MODEL;
	}
	
	@Override
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
		return type == ModEntities.TYPE_PEDESTAL.get() ? PedestalTileEntity::tick : null;//EntityBlock.super.getTicker(pLevel, pState, pBlockEntityType);
	}

	@Nullable
	@Override
	public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
		return ModEntities.TYPE_PEDESTAL.get().create(pPos, pState);
	}
}
