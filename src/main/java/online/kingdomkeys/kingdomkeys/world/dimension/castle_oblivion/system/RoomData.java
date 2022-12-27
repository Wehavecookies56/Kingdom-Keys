package online.kingdomkeys.kingdomkeys.world.dimension.castle_oblivion.system;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.util.INBTSerializable;
import online.kingdomkeys.kingdomkeys.capability.ModCapabilities;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class RoomData implements INBTSerializable<CompoundTag> {

    public Map<RoomUtils.Direction, DoorData> doors;
    public final RoomUtils.RoomPos pos;
    UUID parent;
    Room generatedRoom;

    public RoomData(RoomUtils.RoomPos pos) {
        this.pos = pos;
        doors = new HashMap<>();
    }

    public Floor getParentFloor(Level level) {
        return ModCapabilities.getCastleOblivionInterior(level).getFloorByID(parent);
    }

    public void setParent(Floor parent) {
        this.parent = parent.getFloorID();
    }

    public void setDoor(DoorData door, RoomUtils.Direction direction) {
        doors.put(direction, door);
    }

    public static RoomData inDirection(RoomData prevRoom, RoomUtils.Direction direction) {
        RoomData newRoom = new RoomData(RoomUtils.RoomPos.inDirection(prevRoom.pos, direction));
        newRoom.setDoor(new DoorData(DoorData.Type.NORMAL), direction.opposite());
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
        tag.putUUID("parent", parent);
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
        parent = tag.getUUID("parent");
        int doorCount = tag.getInt("door_count");
        doors.clear();
        for (int i = 0; i < doorCount; i++) {
            int dir = tag.getInt("door_direction_" + i);
            doors.put(RoomUtils.Direction.values()[dir], DoorData.deserialize(tag.getCompound("door_data_" + i)));
        }
        if (tag.getBoolean("generated")) {
            generatedRoom = Room.deserialize(tag.getCompound("generated_room"));
        }
    }

    public static RoomData deserialize(CompoundTag tag) {
        RoomData room = new RoomData(RoomUtils.RoomPos.deserialize(tag.getCompound("roompos")));
        room.deserializeNBT(tag);
        return room;
    }

}
