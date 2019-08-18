package online.kingdomkeys.kingdomkeys.util;

import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;

/**
 * Enum for flags used by {@link net.minecraft.world.World#setBlockState(BlockPos, BlockState, int)}
 * Values are based on the comment for that method
 */
public enum SetBlockStateFlags {

    BLOCK_UPDATE(1 << 0),
    SEND_TO_CLIENT(1 << 1),
    DONT_RERENDER(1 << 2),
    RERENDER_ON_MAIN(1 << 3),
    PREVENT_NEIGHBOR_REACTIONS(1 << 4),
    PREVENT_NEIGHBOR_DROPS(1 << 5),
    MOVED(1 << 6);

    private int value;

    SetBlockStateFlags(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
