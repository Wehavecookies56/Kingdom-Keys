package online.kingdomkeys.kingdomkeys.world.dimension.castle_oblivion.system;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.util.INBTSerializable;
import online.kingdomkeys.kingdomkeys.capability.ModCapabilities;
import online.kingdomkeys.kingdomkeys.util.Utils;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class RoomData implements INBTSerializable<CompoundTag> {

    Map<RoomUtils.Direction, DoorData> doors;
    public final RoomUtils.RoomPos pos;
    UUID parent;
    Room generatedRoom;

    int cardCost;

    public RoomData(RoomUtils.RoomPos pos) {
        this.pos = pos;
        doors = new HashMap<>();
        this.cardCost = Utils.randomWithRange(0, 9);
    }

    public RoomData(CompoundTag tag) {
        this(new RoomUtils.RoomPos(tag.getCompound("roompos")));
        deserializeNBT(tag);
    }

    public Floor getParentFloor(Level level) {
        return ModCapabilities.getCastleOblivionInterior(level).getFloorByID(parent);
    }

    public void setParent(Floor parent) {
        this.parent = parent.getFloorID();
    }

    public void setDoor(DoorData.Type doorType, RoomUtils.Direction direction) {
        doors.put(direction, new DoorData(this, doorType, direction));
    }

    public DoorData getDoor(RoomUtils.Direction direction) {
        return doors.get(direction);
    }

    public Map<RoomUtils.Direction, DoorData> getDoors() {
        return doors;
    }

    public int getCardCost() {
        return cardCost;
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
        tag.put("roompos", pos.serializeNBT());
        tag.putBoolean("generated", generatedRoom != null);
        if (generatedRoom != null) {
            tag.put("generated_room", generatedRoom.serializeNBT());
        }
        tag.putInt("card_cost", cardCost);
        return tag;
    }

    @Override
    public void deserializeNBT(CompoundTag tag) {
        parent = tag.getUUID("parent");
        int doorCount = tag.getInt("door_count");
        doors.clear();
        CompoundTag doorDataTag = tag.getCompound("doors");
        
        for (int i = 0; i < doorCount; i++) {
            int dir = doorDataTag.getInt("door_direction_" + i);
            doors.put(RoomUtils.Direction.values()[dir], new DoorData(doorDataTag.getCompound("door_data_" + i)));
        }
        if (tag.getBoolean("generated")) {
            generatedRoom = new Room(tag.getCompound("generated_room"));
        }
        cardCost = tag.getInt("card_cost");
    }

}
