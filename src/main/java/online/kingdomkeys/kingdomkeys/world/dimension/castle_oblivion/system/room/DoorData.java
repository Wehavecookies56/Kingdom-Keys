package online.kingdomkeys.kingdomkeys.world.dimension.castle_oblivion.system.room;

import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.util.INBTSerializable;

public class DoorData implements INBTSerializable<CompoundTag> {

    RoomData parent;
    Type type;
    RoomDirection direction;

    public DoorData(RoomData parent, Type type, RoomDirection direction) {
        this.type = type;
        this.parent = parent;
        this.direction = direction;
    }

    public DoorData(CompoundTag tag) {
        this.deserializeNBT(tag);
    }

    public Type getType() {
        return type;
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
        this.direction = RoomDirection.values()[tag.getInt("direction")];
    }

    /**
     * NORMAL: door within a generated room that can be used to set the card for the room
     * ENTRANCE: door to go to previous floor or exit dimension on floor 1
     * EXIT: door to go to the next floor
     * FIXED: like NORMAL but cards cannot be used to generate a room
     * HALL: the door used to select a world card
     * NONE: not a door but rather to stop adjacent rooms connecting on the side of this "door"
     */
    public enum Type {
        NORMAL, ENTRANCE, EXIT, FIXED, HALL, NONE
    }
}
