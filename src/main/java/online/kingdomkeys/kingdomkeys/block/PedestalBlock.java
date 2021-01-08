package online.kingdomkeys.kingdomkeys.block;

import javax.annotation.Nullable;

import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.ContainerBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;
import net.minecraftforge.items.CapabilityItemHandler;
import online.kingdomkeys.kingdomkeys.capability.IPlayerCapabilities;
import online.kingdomkeys.kingdomkeys.capability.ModCapabilities;
import online.kingdomkeys.kingdomkeys.entity.block.PedestalTileEntity;
import online.kingdomkeys.kingdomkeys.lib.SoAState;
import online.kingdomkeys.kingdomkeys.network.PacketHandler;
import online.kingdomkeys.kingdomkeys.network.stc.SCOpenChoiceScreen;

public class PedestalBlock extends ContainerBlock {

	public PedestalBlock(Properties properties) {
		super(properties);
	}

	private static final VoxelShape collision = Block.makeCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 14.0D, 16.0D);

	@Override
	public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
		return collision;
	}

	@Override
	public VoxelShape getCollisionShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
		return getShape(state, worldIn, pos, context);
	}

	@Override
	public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult rayTraceResult) {
		if (worldIn.isRemote)
			return ActionResultType.SUCCESS;

		INamedContainerProvider namedContainerProvider = this.getContainer(state, worldIn, pos);
		if (namedContainerProvider != null) {
			if (!(player instanceof ServerPlayerEntity))
				return ActionResultType.FAIL;
			ServerPlayerEntity serverPlayerEntity = (ServerPlayerEntity) player;
			if (state.hasTileEntity() && worldIn.getTileEntity(pos) instanceof PedestalTileEntity) {
				PedestalTileEntity te = (PedestalTileEntity) worldIn.getTileEntity(pos);
				if (te != null) {
					if (te.isStationOfAwakeningMarker()) {
						IPlayerCapabilities playerData = ModCapabilities.getPlayer(player);
						SoAState soAState = playerData.getSoAState();
						if (soAState == SoAState.CHOICE || (soAState == SoAState.SACRIFICE && (!playerData.getChoicePedestal().equals(pos)))) {
							PacketHandler.sendTo(new SCOpenChoiceScreen(te.getDisplayStack(), soAState, pos), serverPlayerEntity);
						} else {
							return ActionResultType.FAIL;
						}
					} else {
						NetworkHooks.openGui(serverPlayerEntity, namedContainerProvider, (packetBuffer) -> {
							packetBuffer.writeBlockPos(pos);
						});
					}
				}
			}
		}
		return ActionResultType.SUCCESS;
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

	@Deprecated
	public BlockRenderType getRenderType(BlockState state) {
		return BlockRenderType.MODEL;
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

	@Override
	public TileEntity createNewTileEntity(IBlockReader worldIn) {
		return new PedestalTileEntity();
	}
}
