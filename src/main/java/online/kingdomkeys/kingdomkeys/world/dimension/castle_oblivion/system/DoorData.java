package online.kingdomkeys.kingdomkeys.world.dimension.castle_oblivion.system;

import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.util.INBTSerializable;

public class DoorData implements INBTSerializable<CompoundTag> {

    public static final DoorData NONE = null;

    String exitDestination;

    RoomData parent;
    Type type;
    RoomUtils.Direction direction;

    public DoorData(RoomData parent, Type type, RoomUtils.Direction direction) {
        this.type = type;
        this.parent = parent;
        this.direction = direction;
    }

    public DoorData(CompoundTag tag) {
        this.deserializeNBT(tag);
    }

    public void setExitDestination(String destination) {
        if (type == Type.EXIT) {
            exitDestination = destination;
        }
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag tag = new CompoundTag();
        tag.putInt("type", this.type.ordinal());
        tag.putInt("direction", this.direction.ordinal());
        return tag;
    }

    @Override
    public void deserializeNBT(CompoundTag tag) {
        this.type = Type.values()[tag.getInt("type")];
        this.direction = RoomUtils.Direction.values()[tag.getInt("direction")];
    }

    public enum Type {
        NORMAL, EXIT, FIXED
    }
}
