package online.kingdomkeys.kingdomkeys.world.dimension.castle_oblivion.system;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.google.common.collect.ImmutableMap;
import com.mojang.datafixers.util.Pair;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.util.INBTSerializable;
import online.kingdomkeys.kingdomkeys.item.card.WorldCardItem;
import online.kingdomkeys.kingdomkeys.util.Utils;

public class Floor implements INBTSerializable<CompoundTag> {


    BlockPos lobbyPosition;
    FloorType type = ModFloorTypes.NONE.get();
    Map<UUID, RoomUtils.RoomPos> players;
    Map<RoomUtils.RoomPos, RoomData> rooms;
    UUID floorID;

    public Floor() {
        rooms = new HashMap<>();
        players = new HashMap<>();
        floorID = UUID.randomUUID();
        RoomData lobby = new RoomData(new RoomUtils.RoomPos(0, 0));
        lobby.setDoor(new DoorData(DoorData.Type.EXIT), RoomUtils.Direction.SOUTH);
        lobby.setDoor(new DoorData(DoorData.Type.NORMAL), RoomUtils.Direction.NORTH);
        lobby.setParent(this);
        rooms.put(lobby.pos, lobby);
    }

    public void setFloorID(UUID id) {
        floorID = id;
    }

    public UUID getFloorID() {
        return floorID;
    }

    public Map<UUID, RoomUtils.RoomPos> getPlayers() {
        return ImmutableMap.<UUID, RoomUtils.RoomPos>builder().putAll(players).build();
    }

    public boolean hasWorldCard() {
        return type != ModFloorTypes.NONE.get();
    }

    public void floorEntered(Player player) {

    }

    public void floorExited(Player player) {
        players.remove(player.getGameProfile().getId());
    }

    public void createLobby(BlockPos pos) {
        lobbyPosition = pos;
    }

    public void setWorldCard(WorldCardItem card) {
        type = card.getFloorType();
        generateLayout();
    }

    public FloorType getType() {
        return type;
    }

    public void generateLayout() {
        RoomData entrance = new RoomData(new RoomUtils.RoomPos(0, 1));
        entrance.setDoor(new DoorData(DoorData.Type.NORMAL), RoomUtils.Direction.SOUTH);
        entrance.setParent(this);
        RoomData currentRoom = entrance;
        rooms.put(entrance.pos, entrance);
        for (int i = 0; i < type.critPathLength; i++) {
            Map<RoomData, RoomUtils.Direction> adjRooms = getAdjacentRooms(currentRoom);
            List<RoomUtils.Direction> directions = new ArrayList<>(List.of(RoomUtils.Direction.values()));
            //prevent rooms going further south
            if (currentRoom.pos.y == 1) {
                directions.remove(RoomUtils.Direction.SOUTH);
            }
            //remove directions that have a room already in that direction
            for (RoomUtils.Direction direction : adjRooms.values()) {
                directions.remove(direction);
            }
            //No more possible directions to continue so exit is created
            if (directions.size() == 0) {
                boolean exitCreated = false;
                for (RoomUtils.Direction dir : Arrays.stream(RoomUtils.Direction.values()).toList()) {
                    if (!exitCreated) {
                        if (!currentRoom.doors.containsKey(dir)) {
                            currentRoom.setDoor(new DoorData(DoorData.Type.EXIT), dir);
                        }
                    }
                }
            } else {
                int rand = Utils.randomWithRange(0, directions.size() - 1);
                RoomUtils.Direction nextDir = directions.get(rand);
                //create door for next room
                currentRoom.setDoor(new DoorData(DoorData.Type.NORMAL), nextDir);
                //create next room in direction with door at opposite direction
                currentRoom = RoomData.inDirection(currentRoom, nextDir);
                if (i == type.critPathLength - 1) {
                    //final room needs extra door
                    currentRoom.setDoor(new DoorData(DoorData.Type.EXIT), nextDir);
                }
            }
            currentRoom.setParent(this);
            rooms.put(currentRoom.pos, currentRoom);
        }
        //todo bonus rooms
        for (int i = 0; i < type.bonusRoomCount; i++) {

        }
    }

    public List<RoomData> getRooms() {
        return rooms.values().stream().toList();
    }

    public List<RoomData> getGeneratedRooms() {
        return rooms.values().stream().filter(roomData -> roomData.generatedRoom != null).toList();
    }

    public RoomData getRoom(RoomUtils.RoomPos pos) {
        return rooms.get(pos);
    }

    public Pair<RoomData, RoomUtils.Direction> getAdjacentRoom(RoomData room, RoomUtils.Direction direction) {
        RoomUtils.RoomPos adjPos = room.pos.add(direction);
        if (rooms.containsKey(adjPos)) {
            return Pair.of(rooms.get(adjPos), direction);
        }
        return null;
    }

    public Map<RoomData, RoomUtils.Direction> getAdjacentRooms(RoomData room) {
        Map<RoomData, RoomUtils.Direction> rooms = new HashMap<>();
        for (int i = 0; i < RoomUtils.Direction.values().length; i++) {
            Pair<RoomData, RoomUtils.Direction> roomDirectionPair = getAdjacentRoom(room, RoomUtils.Direction.values()[i]);
            if (roomDirectionPair != null) {
                rooms.put(roomDirectionPair.getFirst(), roomDirectionPair.getSecond());
            }
        }
        return rooms;
    }

    public boolean shouldTick() {
        return players.size() > 0;
    }

    public boolean shouldRoomTick(Room room) {
        return players.containsValue(room);
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag tag = new CompoundTag();
        tag.putUUID("id", floorID);
        tag.put("lobby_pos", NbtUtils.writeBlockPos(lobbyPosition));
        tag.putString("floor_type", type.getRegistryName().toString());
        tag.putInt("players_size", players.size());
        CompoundTag playersTag = new CompoundTag();
        for (int i = 0; i < players.size(); i++) {
            List<UUID> uuids = players.keySet().stream().toList();
            List<RoomUtils.RoomPos> rooms = players.values().stream().toList();
            playersTag.putUUID("players_uuid_" + i, uuids.get(i));
            playersTag.put("players_room_" + i, RoomUtils.RoomPos.serialize(rooms.get(i)));
        }
        tag.put("players", playersTag);
        tag.putInt("rooms_size", rooms.size());
        CompoundTag roomsTag = new CompoundTag();
        for (int i = 0; i < rooms.size(); i++) {
            List<RoomData> roomList = rooms.values().stream().toList();
            roomsTag.put("rooms_roomdata_" + i, roomList.get(i).serializeNBT());
        }
        tag.put("rooms", roomsTag);
        return tag;
    }

    @Override
    public void deserializeNBT(CompoundTag tag) {
        floorID = tag.getUUID("id");
        lobbyPosition = NbtUtils.readBlockPos(tag.getCompound("lobby_pos"));
        type = ModFloorTypes.registry.get().getValue(new ResourceLocation(tag.getString("floor_type")));
        players.clear();
        int playerssize = tag.getInt("players_size");
        CompoundTag playersTag = tag.getCompound("players");
        //todo players
        rooms.clear();
        int roomssize = tag.getInt("rooms_size");
        CompoundTag roomsTag = tag.getCompound("rooms");
        for (int i = 0; i < roomssize; i++) {
            RoomData data = RoomData.deserialize(roomsTag.getCompound("rooms_roomdata_" + i));
            rooms.put(data.pos, data);
        }
    }

    public static Floor deserialize(CompoundTag tag) {
        Floor floor = new Floor();
        floor.deserializeNBT(tag);
        return floor;
    }


}
