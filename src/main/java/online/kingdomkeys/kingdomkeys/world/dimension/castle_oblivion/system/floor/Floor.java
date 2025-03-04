package online.kingdomkeys.kingdomkeys.world.dimension.castle_oblivion.system.floor;

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
import online.kingdomkeys.kingdomkeys.world.dimension.castle_oblivion.system.room.RoomDirection;
import online.kingdomkeys.kingdomkeys.world.dimension.castle_oblivion.system.registry.ModFloorTypes;
import online.kingdomkeys.kingdomkeys.world.dimension.castle_oblivion.system.registry.ModJsonRegistries;
import online.kingdomkeys.kingdomkeys.world.dimension.castle_oblivion.system.room.DoorData;
import online.kingdomkeys.kingdomkeys.world.dimension.castle_oblivion.system.room.RoomData;
import online.kingdomkeys.kingdomkeys.world.dimension.castle_oblivion.system.room.Room;
import online.kingdomkeys.kingdomkeys.world.dimension.castle_oblivion.system.room.RoomPos;

import javax.annotation.Nullable;
import java.util.*;

public class Floor implements INBTSerializable<CompoundTag> {


    BlockPos lobbyPosition;
    FloorType type = ModFloorTypes.NONE.get();
    Map<UUID, Room> players;
    Map<RoomPos, RoomData> rooms;
    UUID floorID;

    public Floor() {
        rooms = new HashMap<>();
        players = new HashMap<>();
        floorID = UUID.randomUUID();
        RoomData lobby = new RoomData(RoomPos.ZERO);
        lobby.setDoor(DoorData.Type.EXIT, RoomDirection.SOUTH);
        lobby.setDoor(DoorData.Type.NORMAL, RoomDirection.NORTH);
        lobby.setParent(this);
        rooms.put(lobby.pos, lobby);
    }

    public void setFloorID(UUID id) {
        floorID = id;
    }

    public UUID getFloorID() {
        return floorID;
    }

    public Map<UUID, Room> getPlayers() {
        return ImmutableMap.<UUID, Room>builder().putAll(players).build();
    }

    public boolean hasWorldCard() {
        return type != ModFloorTypes.NONE.get();
    }

    public void floorEntered(Player player) {

    }

    public void floorExited(Player player) {
        players.remove(player.getGameProfile().getId());
    }

    //calculate the length of the room based on the furthest room from the lobby
    public int calculateFloorLength() {
        int furthestRoomPos = 0;
        for (RoomPos pos : rooms.keySet()) {
            furthestRoomPos = Math.max(furthestRoomPos, pos.y());
        }
        return (furthestRoomPos + 1) * 128;
    }

    public boolean inFloor(BlockPos pos) {
        if (!rooms.isEmpty()) {
            Room lobby = rooms.get(RoomPos.ZERO).getGenerated();
            if (lobby != null) {
                int maxX = lobby.position.getX() + lobby.getStructure().getWidth();
                int minX = lobby.position.getX();
                int maxZ = lobby.position.getZ() + lobby.getStructure().getDepth();
                int minZ = lobby.position.getZ();
                for (Map.Entry<RoomPos, RoomData> roomData : rooms.entrySet()) {
                    Room room = roomData.getValue().getGenerated();
                    int roomWidth = room.getStructure().getWidth();
                    int roomDepth = room.getStructure().getDepth();
                    BlockPos roomPos = room.position;
                    minX = Math.min(minX, roomPos.getX());
                    maxX = Math.max(maxX, roomPos.getX() + roomWidth);
                    minZ = Math.min(minZ, roomPos.getZ());
                    maxZ = Math.max(maxZ, roomPos.getZ() + roomDepth);
                }
                return pos.getX() >= minX && pos.getX() <= maxX && pos.getZ() >= minZ && pos.getZ() <= maxZ;
            }
        }
        return false;
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

    public BlockPos getLobbyPosition() {
        return lobbyPosition;
    }

    public Room getLobbyRoom() {
        //lobby room is always generated at RoomPos 0,0
        return rooms.get(RoomPos.ZERO).getGenerated();
    }

    public void generateLayout() {
        RoomData entrance = new RoomData(new RoomPos(0, 1));
        entrance.setDoor(DoorData.Type.NORMAL, RoomDirection.SOUTH);
        entrance.setParent(this);
        RoomData currentRoom = entrance;
        rooms.put(entrance.pos, entrance);
        for (int i = 0; i < type.getCritPathLength(); i++) {
            Map<RoomData, RoomDirection> adjRooms = getAdjacentRooms(currentRoom);
            List<RoomDirection> directions = new ArrayList<>(List.of(RoomDirection.values()));
            //prevent rooms going further south
            if (currentRoom.pos.y() == 1) {
                directions.remove(RoomDirection.SOUTH);
            }
            //remove directions that have a room already in that direction
            for (RoomDirection direction : adjRooms.values()) {
                directions.remove(direction);
            }
            //No more possible directions to continue so exit is created
            if (directions.isEmpty()) {
                boolean exitCreated = false;
                for (RoomDirection dir : Arrays.stream(RoomDirection.values()).toList()) {
                    if (!exitCreated) {
                        if (!currentRoom.getDoors().containsKey(dir)) {
                            currentRoom.setDoor(DoorData.Type.EXIT, dir);
                            exitCreated = true;
                        }
                    }
                }
            } else {
                int rand = Utils.randomWithRange(0, directions.size() - 1);
                RoomDirection nextDir = directions.get(rand);
                //create door for next room
                currentRoom.setDoor(DoorData.Type.NORMAL, nextDir);
                //create next room in direction with door at opposite direction
                currentRoom = createRoomInDirection(currentRoom, nextDir);
                if (i == type.getCritPathLength() - 1) {
                    //final room needs extra door
                    currentRoom.setDoor(DoorData.Type.EXIT, nextDir);
                }
            }
            currentRoom.setParent(this);
            rooms.put(currentRoom.pos, currentRoom);
        }
        //todo bonus rooms
        for (int i = 0; i < type.getBonusRoomCount(); i++) {

        }
    }

    public RoomData createRoomInDirection(RoomData prevRoom, RoomDirection direction) {
        RoomData newRoom = new RoomData(RoomPos.inDirection(prevRoom.pos, direction));
        newRoom.setDoor(DoorData.Type.NORMAL, direction.opposite());
        return newRoom;
    }

    public List<RoomData> getRooms() {
        return rooms.values().stream().toList();
    }

    public List<RoomData> getGeneratedRooms() {
        return rooms.values().stream().filter(roomData -> roomData.getGenerated() != null).toList();
    }

    public RoomData getRoom(RoomPos pos) {
        return rooms.get(pos);
    }

    @Nullable
    public Pair<RoomData, RoomDirection> getAdjacentRoom(RoomData room, RoomDirection direction) {
        RoomPos adjPos = room.pos.add(direction);
        if (rooms.containsKey(adjPos)) {
            return Pair.of(rooms.get(adjPos), direction);
        }
        return null;
    }

    public Map<RoomData, RoomDirection> getAdjacentRooms(RoomData room) {
        Map<RoomData, RoomDirection> rooms = new HashMap<>();
        for (int i = 0; i < RoomDirection.values().length; i++) {
            Pair<RoomData, RoomDirection> roomDirectionPair = getAdjacentRoom(room, RoomDirection.values()[i]);
            if (roomDirectionPair != null) {
                rooms.put(roomDirectionPair.getFirst(), roomDirectionPair.getSecond());
            }
        }
        return rooms;
    }

    public boolean shouldTick() {
        return !players.isEmpty();
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
            List<Room> rooms = players.values().stream().toList();
            playersTag.putUUID("players_uuid_" + i, uuids.get(i));
            playersTag.put("players_room_" + i, rooms.get(i).serializeNBT());
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
        type = ModJsonRegistries.FLOOR_TYPE.get().getValue(new ResourceLocation(tag.getString("floor_type")));
        players.clear();
        int playerssize = tag.getInt("players_size");
        CompoundTag playersTag = tag.getCompound("players");
        //todo players
        rooms.clear();
        int roomssize = tag.getInt("rooms_size");
        CompoundTag roomsTag = tag.getCompound("rooms");
        for (int i = 0; i < roomssize; i++) {
            RoomData data = new RoomData(roomsTag.getCompound("rooms_roomdata_" + i));
            rooms.put(data.pos, data);
        }
    }

    public static Floor deserialize(CompoundTag tag) {
        Floor floor = new Floor();
        floor.deserializeNBT(tag);
        return floor;
    }


}
