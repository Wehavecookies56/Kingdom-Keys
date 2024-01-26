package online.kingdomkeys.kingdomkeys.world.dimension.castle_oblivion.system;

import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.util.INBTSerializable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DoorData implements INBTSerializable<CompoundTag> {

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

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag tag = new CompoundTag();
        tag.putInt("type", this.type.ordinal());
        tag.putBoolean("open", this.open);
        return tag;
    }

    @Override
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
