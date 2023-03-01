package online.kingdomkeys.kingdomkeys.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import online.kingdomkeys.kingdomkeys.entity.ModEntities;
import online.kingdomkeys.kingdomkeys.entity.block.SoRCoreTileEntity;

import javax.annotation.Nullable;

import net.minecraft.world.level.block.state.BlockBehaviour.Properties;

public class SoRCore extends BaseBlock implements EntityBlock {

	public SoRCore(Properties properties) {
		super(properties);
	}

	@Override
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
		return type == ModEntities.TYPE_SOR_CORE_TE.get() ? SoRCoreTileEntity::tick : null;//EntityBlock.super.getTicker(pLevel, pState, pBlockEntityType);
	}
	
	@Nullable
	@Override
	public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
		return ModEntities.TYPE_SOR_CORE_TE.get().create(pPos, pState);
	}

	@Override
	public void onRemove(BlockState state, Level worldIn, BlockPos pos, BlockState newState, boolean isMoving) {
		SoRCoreTileEntity te = (SoRCoreTileEntity) worldIn.getBlockEntity(pos);
		//TODO te.removeSoR();
		te.setRemoved();
	}


}
