package online.kingdomkeys.kingdomkeys.block;

import java.util.UUID;

import javax.annotation.Nullable;

import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import online.kingdomkeys.kingdomkeys.entity.ModEntities;
import online.kingdomkeys.kingdomkeys.entity.block.OrgPortalTileEntity;
import online.kingdomkeys.kingdomkeys.entity.block.SoRCoreTileEntity;

import net.minecraft.world.level.block.state.BlockBehaviour.Properties;

public class SoRCore extends BaseBlock implements EntityBlock {

	public SoRCore(Properties properties) {
		super(properties);
	}

	@Nullable
	@Override
	public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
		return ModEntities.TYPE_SOR_CORE_TE.get().create(pPos, pState);
	}

	@Override
	public void onRemove(BlockState state, Level worldIn, BlockPos pos, BlockState newState, boolean isMoving) {
		SoRCoreTileEntity te = (SoRCoreTileEntity) worldIn.getBlockEntity(pos);
		te.removeSoR();
		te.setRemoved();
	}


}
