package online.kingdomkeys.kingdomkeys.block;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.SimpleMapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.items.IItemHandler;
import online.kingdomkeys.kingdomkeys.entity.ModEntities;
import online.kingdomkeys.kingdomkeys.entity.block.GummiEditorTileEntity;

import javax.annotation.Nullable;

public class GummiEditorBlock extends BaseEntityBlock implements EntityBlock, INoDataGen {
	public static final DirectionProperty FACING = BlockStateProperties.FACING;

	public GummiEditorBlock(Properties properties) {
		super(properties);
	}

	@Override
	protected MapCodec<? extends BaseEntityBlock> codec() {
		return simpleCodec(GummiEditorBlock::new);
	}

	private static final VoxelShape collision = Block.box(0.0D, 0.0D, 0.0D, 16.0D, 14.0D, 16.0D);

	@Nullable
	@Override
	public BlockState getStateForPlacement(BlockPlaceContext context) {
		return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite());
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		super.createBlockStateDefinition(builder);
		builder.add(FACING);
	}
	
	@Override
	public VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
		return collision;
	}

	@Override
	public VoxelShape getCollisionShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
		return getShape(state, worldIn, pos, context);
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
			if (state.hasBlockEntity() && level.getBlockEntity(pos) instanceof GummiEditorTileEntity) {
				GummiEditorTileEntity te = (GummiEditorTileEntity) level.getBlockEntity(pos);
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
			IItemHandler iItemHandler = world.getCapability(Capabilities.ItemHandler.BLOCK, pos, null);
			if (iItemHandler != null) {
				for (int i = 0; i < iItemHandler.getSlots(); i++) {
					popResource(world, pos, iItemHandler.getStackInSlot(i));
				}
			};
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
		return type == ModEntities.TYPE_GUMMI_EDITOR.get() ? GummiEditorTileEntity::tick : null;//EntityBlock.super.getTicker(pLevel, pState, pBlockEntityType);
	}
	
	@Nullable
	@Override
	public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
		return new GummiEditorTileEntity(pPos, pState);
	}
}
