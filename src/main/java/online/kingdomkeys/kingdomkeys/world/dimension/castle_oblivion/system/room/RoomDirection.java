package online.kingdomkeys.kingdomkeys.world.dimension.castle_oblivion.system.room;

import net.minecraft.core.Direction;

public enum RoomDirection {
    NORTH(0, 1),
    WEST(1, 0),
    EAST(-1, 0),
    SOUTH(0, -1);

    public final int xDir, yDir;

    RoomDirection(int xDir, int yDir) {
        this.xDir = xDir;
        this.yDir = yDir;
    }

    public RoomDirection opposite() {
        return switch (this) {
            case EAST -> WEST;
            case WEST -> EAST;
            case NORTH -> SOUTH;
            case SOUTH -> NORTH;
        };
    }

    public Direction toMCDirection() {
        return switch (this) {
            case NORTH -> Direction.SOUTH;
            case SOUTH -> Direction.NORTH;
            case WEST -> Direction.EAST;
            case EAST -> Direction.WEST;
        };
    }
}