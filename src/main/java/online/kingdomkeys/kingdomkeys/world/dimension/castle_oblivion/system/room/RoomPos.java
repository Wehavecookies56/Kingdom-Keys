package online.kingdomkeys.kingdomkeys.world.dimension.castle_oblivion.system.room;

import net.minecraft.nbt.CompoundTag;

import java.util.Objects;

public record RoomPos(int x, int y) {

    public static final RoomPos ZERO = new RoomPos(0, 0);

    public RoomPos(RoomPos pos) {
        this(pos.x, pos.y);
    }

    public RoomPos(CompoundTag tag) {
        this(tag.getInt("roompos_x"), tag.getInt("roompos_y"));
    }

    public static RoomPos inDirection(RoomPos prevPos, RoomDirection direction) {
        return new RoomPos(prevPos.add(direction));
    }

    public RoomPos add(RoomDirection direction) {
        return new RoomPos(this.x + direction.xDir, this.y + direction.yDir);
    }

    public CompoundTag serializeNBT() {
        CompoundTag tag = new CompoundTag();
        tag.putInt("roompos_x", x);
        tag.putInt("roompos_y", y);
        return tag;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof RoomPos other && this.x == other.x && this.y == other.y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

    @Override
    public String toString() {
        return "[" + x + " " + y + "]";
    }
}