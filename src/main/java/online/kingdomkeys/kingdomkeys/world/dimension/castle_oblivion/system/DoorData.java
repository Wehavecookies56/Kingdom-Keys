package online.kingdomkeys.kingdomkeys.world.dimension.castle_oblivion.system;

import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.util.INBTSerializable;

public class DoorData implements INBTSerializable<CompoundTag> {

    public static final DoorData NONE = null;

    String exitDestination;

    Type type;
    //conditions
    boolean open;

    public DoorData(Type type) {
        this.type = type;
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
        tag.putBoolean("open", this.open);
        return tag;
    }

    @Override
    public void deserializeNBT(CompoundTag tag) {
        this.type = Type.values()[tag.getInt("type")];
        this.open = tag.getBoolean("open");
    }

    public static DoorData deserialize(CompoundTag tag) {
        DoorData door = new DoorData(null);
        door.deserializeNBT(tag);
        return door;
    }

    public enum Type {
        NORMAL, EXIT
    }
}
