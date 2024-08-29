package online.kingdomkeys.kingdomkeys.world.dimension.castle_oblivion.system;

import net.minecraft.nbt.CompoundTag;

public class DoorData {

    public static final DoorData NONE = null;

    RoomType roomType;

    String exitDestination;

    Type type;

    boolean open;

    public DoorData(Type type) {
        this.type = type;
    }

    public DoorData(CompoundTag tag) {
        this.deserializeNBT(tag);
    }

    public void setExitDestination(String destination) {
        if (type == Type.EXIT) {
            exitDestination = destination;
        }
    }

    public void open() {
        this.open = true;
    }

    public boolean isOpen() {
        return open;
    }

    public CompoundTag serializeNBT() {
        CompoundTag tag = new CompoundTag();
        tag.putInt("type", this.type.ordinal());
        tag.putBoolean("open", this.open);
        return tag;
    }

    public void deserializeNBT(CompoundTag tag) {
        this.type = Type.values()[tag.getInt("type")];
        this.open = tag.getBoolean("open");
    }

    public static DoorData deserialize(CompoundTag tag) {
        return new DoorData(tag);
    }

    public enum Type {
        NORMAL, EXIT
    }
}
