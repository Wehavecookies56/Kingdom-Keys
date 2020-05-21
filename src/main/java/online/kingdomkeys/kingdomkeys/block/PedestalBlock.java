package online.kingdomkeys.kingdomkeys.block;

import javax.annotation.Nullable;

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
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;
import online.kingdomkeys.kingdomkeys.entity.ModEntities;
import online.kingdomkeys.kingdomkeys.entity.block.PedestalTileEntity;

public class PedestalBlock extends ContainerBlock {

	public PedestalBlock(Properties properties) {
		super(properties);
	}

	@Override
	public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult rayTraceResult) {
		if (worldIn.isRemote)
			return ActionResultType.SUCCESS; // on client side, don't do anything

		INamedContainerProvider namedContainerProvider = this.getContainer(state, worldIn, pos);
		if (namedContainerProvider != null) {
			if (!(player instanceof ServerPlayerEntity))
				return ActionResultType.FAIL; // should always be true, but just in case...
			ServerPlayerEntity serverPlayerEntity = (ServerPlayerEntity) player;
			NetworkHooks.openGui(serverPlayerEntity, namedContainerProvider, (packetBuffer) -> {
			});
			// (packetBuffer)->{} is just a do-nothing because we have no extra data to send
		}
		return ActionResultType.SUCCESS;
	}

	public void onReplaced(BlockState state, World world, BlockPos blockPos, BlockState newState, boolean isMoving) {
		if (state.getBlock() != newState.getBlock()) {
			TileEntity tileentity = world.getTileEntity(blockPos);
			if (tileentity instanceof PedestalTileEntity) {
				PedestalTileEntity tileEntityInventoryBasic = (PedestalTileEntity) tileentity;
				//tileEntityInventoryBasic.dropAllContents(world, blockPos);
			}
//		      worldIn.updateComparatorOutputLevel(pos, this);  if the inventory is used to set redstone power for comparators
			super.onReplaced(state, world, blockPos, newState, isMoving); // call it last, because it removes the TileEntity
		}
	}

	@Deprecated
	public BlockRenderType getRenderType(BlockState state) {
		return BlockRenderType.MODEL;
	}

	@Override
	public boolean isNormalCube(BlockState p_220081_1_, IBlockReader p_220081_2_, BlockPos p_220081_3_) {
		return false;
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
