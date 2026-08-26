package online.kingdomkeys.kingdomkeys.world.dimension.castle_oblivion.system.room;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;

import java.util.Objects;

public record RoomPos(int x, int y) {

    public static final Codec<RoomPos> CODEC = RecordCodecBuilder.create(roomPosInstance ->
            roomPosInstance.group(
                    Codec.INT.fieldOf("roompos_x").forGetter(RoomPos::x),
                    Codec.INT.fieldOf("roompos_y").forGetter(RoomPos::y)
            ).apply(roomPosInstance, RoomPos::new)
    );

    public static final RoomPos ZERO = new RoomPos(0, 0);

    public RoomPos(RoomPos pos) {
        this(pos.x, pos.y);
    }

    public static RoomPos inDirection(RoomPos prevPos, RoomDirection direction) {
        return new RoomPos(prevPos.add(direction));
    }

    public RoomPos add(RoomDirection direction) {
        return new RoomPos(this.x + direction.xDir, this.y + direction.yDir);
    }

    public static RoomPos deserializeNBT(CompoundTag tag) {
        return CODEC.parse(NbtOps.INSTANCE, tag).getOrThrow();
    }

    public CompoundTag serializeNBT() {
        return (CompoundTag) CODEC.encodeStart(NbtOps.INSTANCE, this).getOrThrow();
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