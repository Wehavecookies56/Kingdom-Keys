package online.kingdomkeys.kingdomkeys.world.dimension.castle_oblivion.system.floor;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.state.BlockState;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.block.CardDoorBlock;
import online.kingdomkeys.kingdomkeys.block.ModBlocks;
import online.kingdomkeys.kingdomkeys.data.CastleOblivionData;
import online.kingdomkeys.kingdomkeys.entity.block.CardDoorTileEntity;
import online.kingdomkeys.kingdomkeys.item.card.KeycardType;
import online.kingdomkeys.kingdomkeys.item.card.WorldCardItem;
import online.kingdomkeys.kingdomkeys.util.Utils;
import online.kingdomkeys.kingdomkeys.world.dimension.castle_oblivion.system.registry.ModFloorTypes;
import online.kingdomkeys.kingdomkeys.world.dimension.castle_oblivion.system.registry.ModJsonRegistries;
import online.kingdomkeys.kingdomkeys.world.dimension.castle_oblivion.system.registry.ModRoomStructures;
import online.kingdomkeys.kingdomkeys.world.dimension.castle_oblivion.system.registry.ModRoomTypes;
import online.kingdomkeys.kingdomkeys.world.dimension.castle_oblivion.system.room.*;

import javax.annotation.Nullable;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class Floor {


    FloorType type = ModFloorTypes.NONE.get();
    Map<RoomPos, RoomData> rooms = new HashMap<>();
    int floorID;
    RoomPos exitRoom;

    public Floor(CompoundTag tag) {
        deserializeNBT(tag);
    }

    public Floor(ServerLevel level) {
        CastleOblivionData.InteriorData interiorData = CastleOblivionData.InteriorData.get(level).orElseThrow();
        floorID = interiorData.getFloors().size();
        interiorData.addFloor(this);
        RoomData entranceHall = new RoomData(RoomPos.ZERO, RoomData.Type.ENTRANCE);
        entranceHall.setDoor(DoorData.Type.ENTRANCE, RoomDirection.SOUTH);
        entranceHall.setDoor(DoorData.Type.HALL, RoomDirection.NORTH);
        entranceHall.setRemainingDoors(DoorData.Type.NONE);
        entranceHall.setParent(this);
        rooms.put(entranceHall.pos, entranceHall);
    }

    public static Floor getOrCreateFirstFloor(ServerLevel level) {
        CastleOblivionData.InteriorData capability = CastleOblivionData.InteriorData.get(level).orElseThrow();
        //Only do this if there are no floors
        if (capability.getFloors().isEmpty()) {
            Floor floor = new Floor(level);
            RoomData data = floor.getRoom(RoomPos.ZERO);
            Room room = new Room(ModRoomTypes.ENTRANCE_HALL.get(), floor.getFloorID(), RoomPos.ZERO, 0);
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
            capability.setDirty();
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

    public boolean hasWorldCard() {
        return type != ModFloorTypes.NONE.get();
    }

    public boolean inFloor(BlockPos pos) {
        if (!rooms.isEmpty()) {
            if (rooms.get(RoomPos.ZERO).getGenerated().isPresent()) {
                Room entrance = rooms.get(RoomPos.ZERO).getGenerated().get();
                int maxX = entrance.getPosition().getX() + entrance.getStructure().getWidth();
                int minX = entrance.getPosition().getX();
                int maxZ = entrance.getPosition().getZ() + entrance.getStructure().getDepth();
                int minZ = entrance.getPosition().getZ();
                for (Map.Entry<RoomPos, RoomData> roomData : rooms.entrySet()) {
                    Room room = roomData.getValue().getGenerated().get();
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
        return getRoom(RoomPos.ZERO).getGenerated().map(Room::getPosition).orElse(null);
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
        KingdomKeys.LOGGER.debug("Generating crit path");
        for (int i = 0; i < type.getCritPathLength(); i++) {
            KingdomKeys.LOGGER.debug("Crit path room no. {}, {}", i, currentRoom.pos);
            EnumMap<RoomDirection, RoomData> adjRooms = getAdjacentRooms(currentRoom);
            List<RoomDirection> directions = new ArrayList<>(List.of(RoomDirection.values()));
            //prevent rooms going into the hall
            if (currentRoom.pos.y() == 1) {
                directions.remove(RoomDirection.SOUTH);
            }
            //remove directions that have a room already in that direction
            for (RoomDirection direction : adjRooms.keySet()) {
                directions.remove(direction);
                KingdomKeys.LOGGER.debug("Removing adjacent room in dir {}", direction);
            }
            //No more possible directions to continue so should intersect
            if (directions.isEmpty()) {
                KingdomKeys.LOGGER.debug("No free directions, starting intersection");
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
                //No possible directions to go so stop
                if (deadEnd) {
                    if (!currentRoom.getDoors().containsKey(nextDir)) {
                        currentRoom.finalizeType(RoomData.Type.NORMAL);
                        KingdomKeys.LOGGER.debug("Reached dead end");
                    }
                    //Otherwise go through intersecting rooms and add needed doors
                } else {
                    RoomPos pos = currentRoom.pos.add(nextDir);
                    for (int j = 0; j < intersections; j++) {
                        RoomData intersectedRoom = rooms.get(pos);
                        if (intersectedRoom != null) {
                            intersectedRoom.addDoor(DoorData.Type.NORMAL, nextDir);
                            intersectedRoom.addDoor(DoorData.Type.NORMAL, nextDir.opposite());
                            KingdomKeys.LOGGER.debug("Intersection happened!");
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
                            //last room does not need next door
                            currentRoom.finalizeType(RoomData.Type.NORMAL);
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
                directions.forEach(roomDirection -> KingdomKeys.LOGGER.debug("Possible direction: {}", roomDirection));
                int rand = Utils.randomWithRange(0, directions.size() - 1);
                RoomDirection nextDir = directions.get(rand);
                prevDir = nextDir.opposite();
                //create door for next room
                currentRoom.setDoor(DoorData.Type.NORMAL, nextDir);
                //create next room in direction with door at opposite direction
                if (i == type.getCritPathLength() - 1) {
                    currentRoom = createRoomInDirection(currentRoom, nextDir);
                    //last room does not need next door
                    currentRoom.finalizeType(RoomData.Type.NORMAL);
                } else {
                    currentRoom.finalizeType(RoomData.Type.NORMAL);
                    currentRoom = createRoomInDirection(currentRoom, nextDir);
                }
            }
            currentRoom.setParent(this);
            rooms.put(currentRoom.pos, currentRoom);
        }
        //create special encounter rooms for the key cards

        List<RoomData> possibleRoomsForKeyRooms = roomsWithRemainingDoors();

        KingdomKeys.LOGGER.debug("Generating Key rooms, {} rooms have space for doors", possibleRoomsForKeyRooms.size());
        //check if there are 4 rooms for the key rooms, first one gives key of beginnings, second one gives key of guidance, third one gives key to truth, fourth one leads to exit room
        //need to find rooms that have at least one spare door for these rooms
        if (possibleRoomsForKeyRooms.size() < 4) {
            //probably could happen in rare circumstances probably can handle it by forcefully extending the crit path
            KingdomKeys.LOGGER.error("No rooms suitable for key rooms making floor impossible to complete");
        } else {
            //TODO maybe let floor type configure how many key rooms there are?
            for (int i = 0; i < 3; i++) {
                KingdomKeys.LOGGER.debug("Generating Key room no. {}", i);
                int index = Utils.randomWithRange(0, possibleRoomsForKeyRooms.size() - 1);
                RoomData room = possibleRoomsForKeyRooms.get(index);
                KeycardType keycardType = KeycardType.values()[i];;
                RoomDirection direction = setRandomFreeDoor(room, DoorData.Type.KEY, keycardType);
                if (direction != null) {
                    RoomData newRoom = new RoomData(room.pos.add(direction), RoomData.Type.ENCOUNTER);
                    //create door that goes back, fixed type as the only way to get in the room is by opening the door on the other side
                    newRoom.setDoor(DoorData.Type.FIXED, direction.opposite());
                    if (rooms.containsKey(newRoom.pos)) {
                        KingdomKeys.LOGGER.debug("Tried to create key room where room already exists {} trying again...", newRoom.pos);
                        //try again (potentially problematic...)
                        possibleRoomsForKeyRooms.remove(index);
                        i--;
                        continue;
                    }

                    //room of truth to conqueror's respite
                    if (i == 2) {
                        EnumMap<RoomDirection, RoomData> adjacentRooms = getAdjacentRooms(newRoom);
                        if (adjacentRooms.size() == 4) {
                            //worst case scenario probably need to come up with a way to prevent this from happening
                            KingdomKeys.LOGGER.error("Room of Truth generated in a position that does not allow conquerer's respite to generate");
                        } else {
                            KingdomKeys.LOGGER.debug("Generating conqueror's respite room");
                            for (RoomDirection dir : RoomDirection.values()) {
                                if (!adjacentRooms.containsKey(dir)) {
                                    RoomData exitRoom = new RoomData(newRoom.pos.add(dir), RoomData.Type.EXIT);
                                    exitRoom.setFixedType(ModRoomTypes.CONQUERORS_RESPITE.get());
                                    exitRoom.setDoor(DoorData.Type.FIXED, dir.opposite());
                                    exitRoom.setDoor(DoorData.Type.EXIT, dir);
                                    exitRoom.setRemainingDoors(DoorData.Type.NONE);
                                    newRoom.setDoor(DoorData.Type.FIXED, dir, true);
                                    rooms.put(exitRoom.pos, exitRoom);
                                    this.exitRoom = exitRoom.pos;
                                    KingdomKeys.LOGGER.debug("Generated exit room at {}", exitRoom.pos);
                                    break;
                                }
                            }
                        }
                    }
                    //set the rest of the doors to none so there is only one entrance to the room
                    newRoom.setRemainingDoors(DoorData.Type.NONE);
                    rooms.put(newRoom.pos, newRoom);
                    KingdomKeys.LOGGER.debug("Finished generating key room no. {} at {}", i, newRoom.pos);
                    //check if room no longer has any remaining doors and remove it if so, as it should be possible to have more than one key room connected to the same room
                    if (!room.hasRemaningDoors()) {
                        possibleRoomsForKeyRooms.remove(index);
                    }
                } else {
                    KingdomKeys.LOGGER.error("No free doors somehow, retrying key door {}", i);
                    possibleRoomsForKeyRooms.remove(index);
                    i--;
                }
            }
            KingdomKeys.LOGGER.debug("Generated key rooms");
        }

        //todo bonus rooms
        for (int i = 0; i < type.getBonusRooms().count(); i++) {

        }

        KingdomKeys.LOGGER.debug("Finished generating layout");
        KingdomKeys.LOGGER.debug("Total rooms: {}", rooms.size());
    }

    public List<RoomData> roomsWithRemainingDoors() {
        List<RoomData> list = new ArrayList<>();
        rooms.values().forEach(room -> {
            //first check if there are remaining doors
            AtomicInteger freeCount = new AtomicInteger();
                room.getRemainingDirections().forEach(roomDirection -> {
                    if (getAdjacentRoom(room, roomDirection).isEmpty()) {
                        freeCount.incrementAndGet();
                    }
                });

                if (freeCount.get() > 0) {
                    list.add(room);
                }
        });
        return list;
    }

    //Set random remaining door and return the direction it was set for.
    public RoomDirection setRandomFreeDoor(RoomData room, DoorData.Type doorType, @Nullable KeycardType keycardType) {
        List<RoomDirection> remainingDirs = room.getRemainingDirections().stream().filter(roomDirection -> getAdjacentRoom(room, roomDirection).isEmpty()).toList();
        if (!remainingDirs.isEmpty()) {
            RoomDirection dir = remainingDirs.get(Utils.randomWithRange(0, remainingDirs.size() - 1));
            if (keycardType != null) {
                room.getDoors().put(dir, new DoorData(room, doorType, dir, keycardType));
            } else {
                room.getDoors().put(dir, new DoorData(room, doorType, dir));
            }
            return dir;
        } else {
            return null;
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
        return rooms.values().stream().filter(roomData -> roomData.getGenerated().isPresent()).toList();
    }

    public RoomData getRoom(RoomPos pos) {
        return rooms.get(pos);
    }

    public Optional<RoomData> getAdjacentRoom(RoomData room, RoomDirection direction) {
        RoomPos adjPos = room.pos.add(direction);
        if (rooms.containsKey(adjPos)) {
            return Optional.ofNullable(rooms.get(adjPos));
        }
        return Optional.empty();
    }

    public EnumMap<RoomDirection, RoomData> getAdjacentRooms(RoomData room) {
        EnumMap<RoomDirection, RoomData> rooms = new EnumMap<>(RoomDirection.class);
        for (RoomDirection dir : RoomDirection.values()) {
            getAdjacentRoom(room, dir).ifPresent(adjRoom -> rooms.put(dir, adjRoom));
        }
        return rooms;
    }

    public boolean shouldTick() {
        return false;
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

    public CompoundTag serializeNBT() {
        CompoundTag tag = new CompoundTag();
        tag.putInt("id", floorID);
        tag.putString("floor_type", type.getRegistryName().toString());
        tag.putInt("rooms_size", rooms.size());
        CompoundTag roomsTag = new CompoundTag();
        List<RoomData> roomList = rooms.values().stream().toList();
        for (int i = 0; i < rooms.size(); i++) {
            roomsTag.put("rooms_roomdata_" + i, roomList.get(i).serializeNBT());
        }
        tag.put("rooms", roomsTag);
        if (exitRoom != null) {
            tag.put("exit", exitRoom.serializeNBT());
        }
        return tag;
    }

    public void deserializeNBT(CompoundTag tag) {
        floorID = tag.getInt("id");
        type = ModJsonRegistries.FLOOR_TYPE.get().getValue(ResourceLocation.parse(tag.getString("floor_type")));
        rooms.clear();
        int roomssize = tag.getInt("rooms_size");
        CompoundTag roomsTag = tag.getCompound("rooms");
        for (int i = 0; i < roomssize; i++) {
            RoomData data = new RoomData(roomsTag.getCompound("rooms_roomdata_" + i));
            rooms.put(data.pos, data);
        }
        if (tag.contains("exit")) {
            exitRoom = RoomPos.deserializeNBT(tag.getCompound("exit"));
        }
    }
}
