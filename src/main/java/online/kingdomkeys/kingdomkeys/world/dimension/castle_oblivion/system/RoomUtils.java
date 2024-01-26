package online.kingdomkeys.kingdomkeys.world.dimension.castle_oblivion.system;

import java.util.Objects;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;

public class RoomUtils {

    public static final RoomPos ZERO = new RoomPos(0, 0);

    public static final int SPACING = 1024;
    public static class FloorPos {
        int x, y, z;

        public FloorPos(int x, int y, int z) {
            this.x = x;
            this.y = y;
            this.z = z;
        }

        public FloorPos(BlockPos pos) {
            x = pos.getX() / SPACING;
            y = pos.getY();
            z = pos.getZ();
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

    public static class RoomPos {
        final int x, y;

        public RoomPos(int x, int y) {
            this.x = x;
            this.y = y;
        }

        public RoomPos(RoomPos pos) {
            this.x = pos.x;
            this.y = pos.y;
        }

        public static RoomPos inDirection(RoomPos prevPos, Direction direction) {
            return new RoomPos(prevPos.add(direction));
        }

        public RoomPos add(Direction direction) {
            return new RoomPos(this.x + direction.xDir, this.y + direction.yDir);
        }

        public static CompoundTag serialize(RoomPos pos) {
            CompoundTag tag = new CompoundTag();
            tag.putInt("roompos_x", pos.x);
            tag.putInt("roompos_y", pos.y);
            return tag;
        }

        public static RoomPos deserialize(CompoundTag tag) {
            return new RoomPos(tag.getInt("roompos_x"), tag.getInt("roompos_y"));
        }

        @Override
        public boolean equals(Object obj) {
            return obj instanceof RoomPos other && this.x == other.x && this.y == other.y;
        }

        @Override
        public int hashCode() {
            return Objects.hash(x, y);
        }
    }


    public enum Direction {
        NORTH(0, 1),
        WEST(1, 0),
        EAST(-1, 0),
        SOUTH(0, -1);

        final int xDir, yDir;

        Direction(int xDir, int yDir) {
            this.xDir = xDir;
            this.yDir = yDir;
        }

        public Direction opposite() {
            return switch (this) {
                case EAST -> WEST;
                case WEST -> EAST;
                case NORTH -> SOUTH;
                case SOUTH -> NORTH;
            };
        }

        public net.minecraft.core.Direction toMCDirection() {
            return switch (this) {
                case NORTH -> net.minecraft.core.Direction.SOUTH;
                case SOUTH -> net.minecraft.core.Direction.NORTH;
                case WEST -> net.minecraft.core.Direction.EAST;
                case EAST -> net.minecraft.core.Direction.WEST;
            };
        }
    }

}
