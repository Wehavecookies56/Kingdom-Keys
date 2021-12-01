package online.kingdomkeys.kingdomkeys.entity.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import online.kingdomkeys.kingdomkeys.entity.ModEntities;

public class MoogleProjectorTileEntity extends BlockEntity{
    public MoogleProjectorTileEntity(BlockPos blockPos, BlockState state) {
        super(ModEntities.TYPE_MOOGLE_PROJECTOR.get(),blockPos, state);
    }
}
