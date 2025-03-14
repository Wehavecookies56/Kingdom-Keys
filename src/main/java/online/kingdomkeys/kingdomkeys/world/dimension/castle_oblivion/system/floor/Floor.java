package online.kingdomkeys.kingdomkeys.world.dimension.castle_oblivion.system.floor;

import com.google.common.collect.ImmutableMap;
import com.mojang.datafixers.util.Pair;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.util.INBTSerializable;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.block.CardDoorBlock;
import online.kingdomkeys.kingdomkeys.block.ModBlocks;
import online.kingdomkeys.kingdomkeys.capability.CastleOblivionCapabilities;
import online.kingdomkeys.kingdomkeys.capability.ModCapabilities;
import online.kingdomkeys.kingdomkeys.entity.block.CardDoorTileEntity;
import online.kingdomkeys.kingdomkeys.item.card.WorldCardItem;
import online.kingdomkeys.kingdomkeys.util.Utils;
import online.kingdomkeys.kingdomkeys.world.dimension.castle_oblivion.system.registry.ModRoomStructures;
import online.kingdomkeys.kingdomkeys.world.dimension.castle_oblivion.system.registry.ModRoomTypes;
import online.kingdomkeys.kingdomkeys.world.dimension.castle_oblivion.system.room.*;
import online.kingdomkeys.kingdomkeys.world.dimension.castle_oblivion.system.registry.ModFloorTypes;
import online.kingdomkeys.kingdomkeys.world.dimension.castle_oblivion.system.registry.ModJsonRegistries;

import javax.annotation.Nullable;
import java.util.*;

public class Floor implements INBTSerializable<CompoundTag> {

    FloorType type = ModFloorTypes.NONE.get();
    Map<UUID, Room> players = new HashMap<>();
    Map<RoomPos, RoomData> rooms = new HashMap<>();
    int floorID;
    RoomPos exitRoom;

    public Floor(CompoundTag tag) {
        deserializeNBT(tag);
    }

    public Floor(Level level) {
        CastleOblivionCapabilities.ICastleOblivionInteriorCapability capability = ModCapabilities.getCastleOblivionInterior(level);
        floorID = capability.getFloors().size();
        capability.addFloor(this);
        RoomData entranceHall = new RoomData(RoomPos.ZERO, RoomData.Type.ENTRANCE);
        entranceHall.setDoor(DoorData.Type.ENTRANCE, RoomDirection.SOUTH);
        entranceHall.setDoor(DoorData.Type.HALL, RoomDirection.NORTH);
        entranceHall.setRemainingDoors(DoorData.Type.NONE);
        entranceHall.setParent(this);
        rooms.put(entranceHall.pos, entranceHall);
    }


    public static Floor getOrCreateFirstFloor(Level level) {
        CastleOblivionCapabilities.ICastleOblivionInteriorCapability capability = ModCapabilities.getCastleOblivionInterior(level);
        //Only do this if there are no floors
        if (capability.getFloors().isEmpty()) {
            Floor floor = new Floor(level);
            RoomData data = floor.getRoom(RoomPos.ZERO);
            Room room = new Room(ModRoomTypes.ENTRANCE_HALL.get(), floor.getFloorID(), RoomPos.ZERO);
            room.setPosition(new BlockPos(0, 59, 0));
            BlockPos southDoor = new BlockPos(16, 60, 1); //door to exit CO
            BlockPos northDoor = new BlockPos(16, 63, 67); //door to first room
            room.doors.put(RoomDirection.NORTH, new Room.Door(data.getDoor(RoomDirection.NORTH), northDoor));
            room.doors.put(RoomDirection.SOUTH, new Room.Door(data.getDoor(RoomDirection.SOUTH), southDoor));
            room.setStructure(ModRoomStructures.ENTRANCE_HALL_1F.get());
            data.setGenerated(room);
            BlockState northState = ModBlocks.cardDoor.get().defaultBlockState().setValue(CardDoorBlock.FACING, Direction.NORTH).setValue(CardDoorBlock.GENERATED, true).setValue(CardDoorBlock.OPEN, false);
            BlockState southState = ModBlocks.cardDoor.get().defaultBlockState().setValue(CardDoorBlock.FACING, Direction.SOUTH).setValue(CardDoorBlock.GENERATED, true).setValue(CardDoorBlock.OPEN, true);
            //replace structure blocks with doors
            level.setBlock(northDoor, northState, 2);
            level.setBlock(southDoor, southState, 2);
            CardDoorTileEntity northTE = new CardDoorTileEntity(northDoor, northState);
            northTE.setParent(data);
            northTE.setDirection(RoomDirection.NORTH);
            northTE.setData(data.getDoor(RoomDirection.NORTH));
            CardDoorTileEntity southTE = new CardDoorTileEntity(southDoor, southState);
            southTE.setParent(data);
            southTE.setDirection(RoomDirection.SOUTH);
            southTE.setData(data.getDoor(RoomDirection.SOUTH));
            southTE.openDoor(false);
            level.setBlockEntity(northTE);
            level.setBlockEntity(southTE);
            return floor;
        }
        return capability.getFloors().get(0);
    }

    public RoomData getExitRoom() {
        return rooms.get(exitRoom);
    }

    public int getFloorID() {
        return floorID;
    }

    public Map<UUID, Room> getPlayers() {
        return ImmutableMap.<UUID, Room>builder().putAll(players).build();
    }

    public boolean hasWorldCard() {
        return type != ModFloorTypes.NONE.get();
    }

    public void floorEntered(Player player) {
        players.put(player.getGameProfile().getId(), getEntranceHall().getGenerated());
    }

    public void floorExited(Player player) {
        players.remove(player.getGameProfile().getId());
    }

    public boolean inFloor(BlockPos pos) {
        if (!rooms.isEmpty()) {
            Room entrance = rooms.get(RoomPos.ZERO).getGenerated();
            if (entrance != null) {
                int maxX = entrance.getPosition().getX() + entrance.getStructure().getWidth();
                int minX = entrance.getPosition().getX();
                int maxZ = entrance.getPosition().getZ() + entrance.getStructure().getDepth();
                int minZ = entrance.getPosition().getZ();
                for (Map.Entry<RoomPos, RoomData> roomData : rooms.entrySet()) {
                    Room room = roomData.getValue().getGenerated();
                    int roomWidth = room.getStructure().getWidth();
                    int roomDepth = room.getStructure().getDepth();
                    BlockPos roomPos = room.getPosition();
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

    public void setWorldCard(WorldCardItem card) {
        type = card.getFloorType();
        generateLayout();
    }

    public FloorType getType() {
        return type;
    }

    public BlockPos getEntranceHallPosition() {
        return getRoom(RoomPos.ZERO).getGenerated().getPosition();
    }

    public RoomData getEntranceHall() {
        //entrance hall room is always generated at RoomPos 0,0
        return getRoom(RoomPos.ZERO);
    }

    public void generateLayout() {
        RoomData entrance = new RoomData(new RoomPos(0, 1), RoomData.Type.NORMAL);
        entrance.setDoor(DoorData.Type.FIXED, RoomDirection.SOUTH);
        entrance.setParent(this);
        RoomData currentRoom = entrance;
        rooms.put(entrance.pos, entrance);
        RoomDirection prevDir = RoomDirection.SOUTH;
        for (int i = 0; i < type.getCritPathLength(); i++) {
            Map<RoomData, RoomDirection> adjRooms = getAdjacentRooms(currentRoom);
            List<RoomDirection> directions = new ArrayList<>(List.of(RoomDirection.values()));
            //prevent rooms going into the hall
            if (currentRoom.pos.y() == 1) {
                directions.remove(RoomDirection.SOUTH);
            }
            //remove directions that have a room already in that direction
            for (RoomDirection direction : adjRooms.values()) {
                directions.remove(direction);
            }
            //No more possible directions to continue so should intersect
            if (directions.isEmpty()) {
                boolean deadEnd = true;
                RoomDirection nextDir = null;
                int intersections = 0;
                boolean foundNextSpace = false;
                for (RoomDirection dir : Arrays.stream(RoomDirection.values()).toList()) {
                    if (!foundNextSpace) {
                        boolean noPossible = false;
                        RoomPos searchPos = currentRoom.pos.add(dir);
                        intersections = 0;
                        while (!foundNextSpace && !noPossible) {
                            if (rooms.containsKey(searchPos)) {
                                RoomData searchRoom = rooms.get(searchPos);
                                if (searchRoom.getType() == RoomData.Type.NORMAL) {
                                    searchPos = searchPos.add(dir);
                                    intersections++;
                                } else {
                                    nextDir = dir;
                                    noPossible = true;
                                }
                            } else {
                                nextDir = dir;
                                deadEnd = false;
                                foundNextSpace = true;
                            }
                        }
                    } else {
                        break;
                    }
                }
                //No possible directions to go so make this the exit room
                if (deadEnd) {
                    if (!currentRoom.getDoors().containsKey(nextDir)) {
                        currentRoom.setDoor(DoorData.Type.EXIT, nextDir);
                        currentRoom.setRemainingDoors(DoorData.Type.NONE);
                        currentRoom.finalizeType(RoomData.Type.EXIT);
                        exitRoom = currentRoom.pos;
                    }
                //Otherwise go through intersecting rooms and add needed doors
                } else {
                    RoomPos pos = currentRoom.pos.add(nextDir);
                    for (int j = 0; j < intersections; j++) {
                        RoomData intersectedRoom = rooms.get(pos);
                        if (intersectedRoom != null) {
                            intersectedRoom.addDoor(DoorData.Type.NORMAL, nextDir);
                            intersectedRoom.addDoor(DoorData.Type.NORMAL, nextDir.opposite());
                        }
                        pos = pos.add(nextDir);
                    }
                    if (!rooms.containsKey(pos)) {
                        //create room after intersection
                        //create door for next room
                        currentRoom.setDoor(DoorData.Type.NORMAL, nextDir);
                        //create next room in direction with door at opposite direction
                        if (i == type.getCritPathLength() - 1) {
                            RoomData newRoom = new RoomData(pos);
                            newRoom.setDoor(DoorData.Type.NORMAL, nextDir.opposite());
                            currentRoom.finalizeType(RoomData.Type.NORMAL);
                            currentRoom = newRoom;
                            //final room needs extra door
                            currentRoom.setDoor(DoorData.Type.EXIT, nextDir);
                            currentRoom.setRemainingDoors(DoorData.Type.NONE);
                            currentRoom.finalizeType(RoomData.Type.EXIT);
                            exitRoom = currentRoom.pos;
                        } else {
                            RoomData newRoom = new RoomData(pos);
                            newRoom.setDoor(DoorData.Type.NORMAL, nextDir.opposite());
                            currentRoom.finalizeType(RoomData.Type.NORMAL);
                            currentRoom = newRoom;
                        }
                    } else {
                        KingdomKeys.LOGGER.error("Room pos after intersection at pos {} already contains a room this should not happen", pos.toString());
                    }
                }
            } else {
                int rand = Utils.randomWithRange(0, directions.size() - 1);
                RoomDirection nextDir = directions.get(rand);
                prevDir = nextDir.opposite();
                //create door for next room
                currentRoom.setDoor(DoorData.Type.NORMAL, nextDir);
                //create next room in direction with door at opposite direction
                if (i == type.getCritPathLength() - 1) {
                    currentRoom = createRoomInDirection(currentRoom, nextDir);
                    //final room needs extra door
                    currentRoom.setDoor(DoorData.Type.EXIT, nextDir);
                    currentRoom.setRemainingDoors(DoorData.Type.NONE);
                    currentRoom.finalizeType(RoomData.Type.EXIT);
                    exitRoom = currentRoom.pos;
                } else {
                    currentRoom.finalizeType(RoomData.Type.NORMAL);
                    currentRoom = createRoomInDirection(currentRoom, nextDir);
                }
            }
            currentRoom.setParent(this);
            rooms.put(currentRoom.pos, currentRoom);
        }

        currentRoom = entrance;
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
    public RoomData getAdjacentRoom(RoomData room, RoomDirection direction) {
        RoomPos adjPos = room.pos.add(direction);
        if (rooms.containsKey(adjPos)) {
            return rooms.get(adjPos);
        }
        return null;
    }

    public Map<RoomData, RoomDirection> getAdjacentRooms(RoomData room) {
        Map<RoomData, RoomDirection> rooms = new HashMap<>();
        for (int i = 0; i < RoomDirection.values().length; i++) {
            RoomData roomData = getAdjacentRoom(room, RoomDirection.values()[i]);
            if (roomData != null) {
                rooms.put(roomData, RoomDirection.values()[i]);
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

    public BlockPos getNorthernMostRoomPosition() {
        BlockPos startPos = getEntranceHallPosition();
        RoomPos found = RoomPos.ZERO;
        for (RoomPos pos : rooms.keySet()) {
            if (pos.y() > found.y()) {
                found = pos;
            }
        }
        return new BlockPos(startPos.getX(), startPos.getY(), startPos.getZ() + (128 * found.y()));
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag tag = new CompoundTag();
        tag.putInt("id", floorID);
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
        if (exitRoom != null) {
            tag.put("exit", exitRoom.serializeNBT());
        }
        return tag;
    }

    @Override
    public void deserializeNBT(CompoundTag tag) {
        floorID = tag.getInt("id");
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
        if (tag.contains("exit")) {
            exitRoom = new RoomPos(tag.getCompound("exit"));
        }
    }
}
