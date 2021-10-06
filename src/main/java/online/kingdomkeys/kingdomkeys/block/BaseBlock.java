package online.kingdomkeys.kingdomkeys.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nullable;

/**
 * Base class for other blocks for anything that is shared across every block
 */
public abstract class BaseBlock extends Block {

    public BaseBlock(Properties properties) {
        super(properties);
    }

    public abstract boolean canConnectRedstone(BlockState state, BlockGetter world, BlockPos pos, @Nullable Direction side);
}
