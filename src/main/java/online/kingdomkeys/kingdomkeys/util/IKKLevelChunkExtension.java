package online.kingdomkeys.kingdomkeys.util;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public interface IKKLevelChunkExtension {
    BlockState kingdom_Keys$setBlockState(BlockPos pos, BlockState state, boolean isMoving);
}
