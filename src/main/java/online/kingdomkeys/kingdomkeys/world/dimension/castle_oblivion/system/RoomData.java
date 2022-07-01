package online.kingdomkeys.kingdomkeys.world.dimension.castle_oblivion.system;

import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.util.INBTSerializable;

import java.util.HashMap;
import java.util.Map;

public class RoomData implements INBTSerializable<CompoundTag> {

    public Map<RoomUtils.Direction, DoorData> doors;
    RoomUtils.RoomPos pos;
    Floor parent;
    Room generatedRoom;

    public RoomData(RoomUtils.RoomPos pos) {
        this.pos = pos;
        doors = new HashMap<>();
    }

    public void setParent(Floor parent) {
        this.parent = parent;
    }

    public void setDoor(DoorData door, RoomUtils.Direction direction) {
        doors.put(direction, door);
    }

    public static RoomData inDirection(RoomData prevRoom, RoomUtils.Direction direction) {
        RoomData newRoom = new RoomData(RoomUtils.RoomPos.inDirection(prevRoom.pos, direction));
        newRoom.setDoor(new DoorData(DoorData.Type.NORMAL), RoomUtils.Direction.opposite(direction));
        return newRoom;
    }

    public void setGenerated(Room room) {
        this.generatedRoom = room;
    }

    public Room getGenerated() {
        return generatedRoom;
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag tag = new CompoundTag();
        tag.putInt("door_count", doors.size());
        CompoundTag doorDataTag = new CompoundTag();
        int i = 0;
        for (Map.Entry<RoomUtils.Direction, DoorData> doorPair : doors.entrySet()) {
            doorDataTag.putInt("door_direction_" + i, doorPair.getKey().ordinal());
            doorDataTag.put("door_data_" + i, doorPair.getValue().serializeNBT());
            i++;
        }
        tag.put("doors", doorDataTag);
        tag.put("roompos", RoomUtils.RoomPos.serialize(pos));
        tag.putBoolean("generated", generatedRoom != null);
        if (generatedRoom != null) {
            tag.put("generated_room", generatedRoom.serializeNBT());
        }
        return tag;
    }

    @Override
    public void deserializeNBT(CompoundTag tag) {
        int doorCount = tag.getInt("door_count");
        doors.clear();
        for (int i = 0; i < doorCount; i++) {
            int dir = tag.getInt("door_direction_" + i);
            doors.put(RoomUtils.Direction.values()[dir], DoorData.deserialize(tag));
        }
        if (tag.getBoolean("generated")) {
            generatedRoom = Room.deserialize(tag);
        }
    }

    public static RoomData deserialize(CompoundTag tag, RoomUtils.RoomPos pos) {
        RoomData room = new RoomData(pos);
        room.deserializeNBT(tag);
        return room;
    }

}
