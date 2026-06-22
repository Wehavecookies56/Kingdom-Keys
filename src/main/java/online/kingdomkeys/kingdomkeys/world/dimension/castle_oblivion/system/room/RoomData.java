package online.kingdomkeys.kingdomkeys.world.dimension.castle_oblivion.system.room;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.Item;
import online.kingdomkeys.kingdomkeys.data.CastleOblivionData;
import online.kingdomkeys.kingdomkeys.item.card.KeycardType;
import online.kingdomkeys.kingdomkeys.util.Utils;
import online.kingdomkeys.kingdomkeys.world.dimension.castle_oblivion.system.floor.Floor;

import java.util.*;

public class RoomData {

    Map<RoomDirection, DoorData> doors;
    public final RoomPos pos;
    int parent;
    Room generatedRoom;
    Type type;

    public RoomData(RoomPos pos, Type type) {
        this(pos);
        this.type = type;
    }

    public RoomData(RoomPos pos) {
        this.pos = pos;
        doors = new HashMap<>(4);
    }

    public RoomData(CompoundTag tag) {
        this(RoomPos.CODEC.parse(NbtOps.INSTANCE, tag.getCompound("roompos")).getOrThrow());
        deserializeNBT(tag);
    }

    public Type getType() {
        return type;
    }

    //set type only if it hasn't been set yet intended to be used in floor generation
    public void finalizeType(Type type) {
        if (this.type == null) {
            this.type = type;
        }
    }

    public int getParentID() {
        return parent;
    }

    public Floor getParentFloor(ServerLevel level) {
        return CastleOblivionData.InteriorData.get(level).orElseThrow().getFloorByID(parent);
    }

    public void setParent(Floor parent) {
        this.parent = parent.getFloorID();
    }

    public void addDoor(DoorData.Type doorType, RoomDirection direction) {
        if (!doors.containsKey(direction)) {
            setDoor(doorType, direction);
        }
    }

    public void setDoor(DoorData.Type doorType, RoomDirection direction) {
        doors.put(direction, new DoorData(this, doorType, direction));
    }

    public void setDoor(DoorData.Type doorType, RoomDirection direction, KeycardType keycardType) {
        doors.put(direction, new DoorData(this, doorType, direction, keycardType));
    }

    public void setRemainingDoors(DoorData.Type doorType) {
        for (RoomDirection dir : RoomDirection.values()) {
            if (!doors.containsKey(dir)) {
                doors.put(dir, new DoorData(this, doorType, dir));
            }
        }
    }

    public boolean hasRemaningDoors() {
        return doors.size() < 4;
    }

    public List<RoomDirection> getRemainingDirections() {
        if (hasRemaningDoors()) {
            List<RoomDirection> remainingDirs = new ArrayList<>();
            for (RoomDirection dir : RoomDirection.values()) {
                if (!doors.containsKey(dir)) {
                    remainingDirs.add(dir);
                }
            }
            return remainingDirs;
        }
        return List.of();
    }

    public DoorData getDoor(RoomDirection direction) {
        return doors.get(direction);
    }

    public Map<RoomDirection, DoorData> getDoors() {
        return doors;
    }

    public void setGenerated(Room room) {
        this.generatedRoom = room;
    }

    public Optional<Room> getGenerated() {
        return Optional.ofNullable(generatedRoom);
    }

    public CompoundTag serializeNBT() {
        CompoundTag tag = new CompoundTag();
        tag.putInt("parent", parent);
        tag.putInt("door_count", doors.size());
        CompoundTag doorDataTag = new CompoundTag();
        int i = 0;
        for (Map.Entry<RoomDirection, DoorData> doorPair : doors.entrySet()) {
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
        if (type != null) {
            tag.putInt("type", type.ordinal());
        }
        return tag;
    }

    public void deserializeNBT(CompoundTag tag) {
        parent = tag.getInt("parent");
        int doorCount = tag.getInt("door_count");
        doors.clear();
        CompoundTag doorDataTag = tag.getCompound("doors");

        for (int i = 0; i < doorCount; i++) {
            int dir = doorDataTag.getInt("door_direction_" + i);
            doors.put(RoomDirection.values()[dir], new DoorData(doorDataTag.getCompound("door_data_" + i)));
        }
        if (tag.getBoolean("generated")) {
            generatedRoom = new Room(tag.getCompound("generated_room"));
        }
        if (tag.contains("type")) {
            type = Type.values()[tag.getInt("type")];
        }
    }

    public enum Type {
        ENTRANCE, EXIT, BOSS, NORMAL, ENCOUNTER
    }

    public static final StreamCodec<FriendlyByteBuf, RoomData> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.COMPOUND_TAG,
            RoomData::serializeNBT,
            RoomData::new
    );

}
