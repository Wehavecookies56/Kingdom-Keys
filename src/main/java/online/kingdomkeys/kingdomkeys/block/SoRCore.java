package online.kingdomkeys.kingdomkeys.block;

import javax.annotation.Nullable;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import online.kingdomkeys.kingdomkeys.entity.ModEntities;
import online.kingdomkeys.kingdomkeys.entity.block.SoRCoreTileEntity;

public class SoRCore extends BaseBlock {

	public SoRCore(Properties properties) {
		super(properties);
	}

	@Override
	public boolean hasTileEntity(BlockState state) {
		return true;
	}

	@Nullable
	@Override
	public BlockEntity createTileEntity(BlockState state, BlockGetter world) {
		return ModEntities.TYPE_SOR_CORE_TE.get().create();
	}

	
	@Override
	public void onRemove(BlockState state, Level worldIn, BlockPos pos, BlockState newState, boolean isMoving) {
		SoRCoreTileEntity te = (SoRCoreTileEntity) worldIn.getBlockEntity(pos);
		te.removeSoR();
		te.setRemoved();
	}


}
