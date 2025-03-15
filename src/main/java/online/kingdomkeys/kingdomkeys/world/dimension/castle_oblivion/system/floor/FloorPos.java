package online.kingdomkeys.kingdomkeys.world.dimension.castle_oblivion.system.floor;

import net.minecraft.core.BlockPos;

public record FloorPos(int x, int y, int z) {
    public static final int SPACING = 1024;

    public FloorPos(BlockPos pos) {
        this(pos.getX() / SPACING, pos.getY(), pos.getZ());
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getZ() {
        return z;
    }

    public BlockPos toBlockPos() {
        return new BlockPos(x * SPACING, y, z);
    }
}