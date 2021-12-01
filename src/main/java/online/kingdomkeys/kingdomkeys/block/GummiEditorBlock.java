package online.kingdomkeys.kingdomkeys.block;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.network.NetworkHooks;
import online.kingdomkeys.kingdomkeys.entity.block.GummiEditorTileEntity;

public class GummiEditorBlock extends BaseEntityBlock {

	public GummiEditorBlock(Properties properties) {
		super(properties);
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
	public InteractionResult use(BlockState state, Level worldIn, BlockPos pos, Player player, InteractionHand hand, BlockHitResult rayTraceResult) {
		if (worldIn.isClientSide)
			return InteractionResult.SUCCESS;

		MenuProvider namedContainerProvider = this.getMenuProvider(state, worldIn, pos);
		if (namedContainerProvider != null) {
			if (!(player instanceof ServerPlayer))
				return InteractionResult.FAIL;
			ServerPlayer serverPlayerEntity = (ServerPlayer) player;
			if (state.hasBlockEntity() && worldIn.getBlockEntity(pos) instanceof GummiEditorTileEntity) {
				GummiEditorTileEntity te = (GummiEditorTileEntity) worldIn.getBlockEntity(pos);
				if (te != null) {
					NetworkHooks.openGui(serverPlayerEntity, namedContainerProvider, (packetBuffer) -> {
						packetBuffer.writeBlockPos(pos);
					});
				}
			}
		}
		return InteractionResult.SUCCESS;
	}

	public void onRemove(BlockState state, Level world, BlockPos pos, BlockState newState, boolean isMoving) {
		if (state.hasBlockEntity() && state.getBlock() != newState.getBlock()) {
			world.getBlockEntity(pos).getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent(inv -> {
				for (int i = 0; i < inv.getSlots(); i++) {
					popResource(world, pos, inv.getStackInSlot(i));
				}
			});
			world.removeBlockEntity(pos);
			super.onRemove(state, world, pos, newState, isMoving); // call it last, because it removes the TileEntity
		}
	}

	@Deprecated
	public RenderShape getRenderShape(BlockState state) {
		return RenderShape.MODEL;
	}


	@Override
	public BlockEntity newBlockEntity( BlockPos blockPos, BlockState blockState) {
		return new GummiEditorTileEntity(blockPos, blockState);
	}
}
