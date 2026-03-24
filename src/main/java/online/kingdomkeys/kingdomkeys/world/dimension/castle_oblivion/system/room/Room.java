package online.kingdomkeys.kingdomkeys.world.dimension.castle_oblivion.system.room;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import online.kingdomkeys.kingdomkeys.data.CastleOblivionData;
import online.kingdomkeys.kingdomkeys.entity.block.CardDoorTileEntity;
import online.kingdomkeys.kingdomkeys.world.dimension.castle_oblivion.system.floor.Floor;
import online.kingdomkeys.kingdomkeys.world.dimension.castle_oblivion.system.registry.ModRoomStructures;
import online.kingdomkeys.kingdomkeys.world.dimension.castle_oblivion.system.registry.ModRoomTypes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Room {
    RoomType type;
    RoomStructure structure;
    BlockPos position;
    int mobsRemaining;
    public Map<RoomDirection, Door> doors;
    public int parentFloor;

    RoomPos roomPos;

    //If the structure has been generated in the world
    boolean generated;


    //Constructor used when generating a room
    public Room(RoomType type, int parentFloor, RoomPos roomPos) {
        this.type = type;
        this.parentFloor = parentFloor;
        this.doors = new HashMap<>();
        this.roomPos = roomPos;
    }

    //Deserialization constructor
    public Room(CompoundTag tag) {
        this(ModRoomTypes.registry.get().getValue(ResourceLocation.parse(tag.getString("type"))), tag.getInt("parent"), new RoomPos(tag.getCompound("room_pos")));
        deserializeNBT(tag);
    }

    public RoomStructure getStructure() {
        return structure;
    }

    public void setStructure(RoomStructure structure) {
        this.structure = structure;
    }

    //Clear room if needed, set type and position
    public void createRoomFromCard(RoomType type, ServerLevel level, Room currentRoom, RoomDirection doorDirection) {
        this.type = type;
        if (generated) {
            clearRoom(level);
        }
        Direction dir = doorDirection.toMCDirection();
        position = currentRoom.position.relative(dir, 128);
    }

    public BlockPos getPosition() {
        return position;
    }

    public void setPosition(BlockPos position) {
        this.position = position;
    }

    public BlockPos getExitDoor() {
        for (Door door : doors.values()) {
            if (door.data.getType() == DoorData.Type.EXIT) {
                return door.pos;
            }
        }
        return null;
    }

    public CardDoorTileEntity getDoorTE(Level level, RoomDirection direction) {
        if (doors.containsKey(direction)) {
            BlockPos pos = doors.get(direction).pos();
            if (pos != null) {
                return (CardDoorTileEntity) level.getBlockEntity(pos);
            }
        }
        return null;
    }

    public RoomType getType() {
        return type;
    }

    public RoomPos getRoomPos() {
        return roomPos;
    }

    public RoomData getRoomData(CastleOblivionData.InteriorData data) {
        return getParent(data).getRoom(getRoomPos());
    }

    public Floor getParent(CastleOblivionData.InteriorData data) {
        return data.getFloorByID(parentFloor);
    }

    public void tick(MinecraftServer server) {
        List<Player> players = getPlayersInRoom(server, this);
        if (shouldRoomTick(players)) {
            type.getModifiers().forEach(roomModifier -> roomModifier.tick(this, players));
        }
    }

    public boolean shouldRoomTick(List<Player> players) {
        return !players.isEmpty();
    }

    public boolean inRoom(BlockPos pos) {
        return pos.getX() >= position.getX() - 1 && pos.getX() <= position.getX() + structure.getWidth() && pos.getZ() >= position.getZ() - 1 && pos.getZ() <= position.getZ() + structure.getDepth();
    }

    public boolean clearRoom(ServerLevel level) {
        Floor parent = getParent(CastleOblivionData.InteriorData.get(level));
        if (parent != null) {
            if (!shouldRoomTick(getPlayersInRoom(level.getServer(), this))) {
                BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos(position.getX(), position.getY(), position.getZ());
                for (int z = 0; z < structure.getDepth(); z++) {
                    for (int y = 0; y < 128; y++) {
                        for (int x = 0; x < structure.getWidth(); x++) {
                            pos.set(x, y, z);
                            level.setBlock(pos, Blocks.AIR.defaultBlockState(), 2);
                        }
                    }
                }
                generated = false;
                return true;
            } else {
                return false;
            }
        } return false;
    }

    public CompoundTag serializeNBT() {
        CompoundTag tag = new CompoundTag();
        tag.putInt("parent", parentFloor);
        tag.put("room_pos", roomPos.serializeNBT());
        tag.putString("type", type.getRegistryName().toString());
        tag.put("position", NbtUtils.writeBlockPos(position));
        tag.putInt("mobs", mobsRemaining);
        tag.putBoolean("generated", generated);
        tag.putInt("door_positions_size", doors.size());
        CompoundTag doorPosTag = new CompoundTag();
        int i = 0;
        for (Map.Entry<RoomDirection, Door> doorPos : doors.entrySet()) {
            doorPosTag.putInt("direction_" + i, doorPos.getKey().ordinal());
            doorPosTag.put("door_" + i, doorPos.getValue().serializeNBT());
            i++;
        }
        tag.put("door_positions", doorPosTag);
        tag.putString("structure", structure.getRegistryName().toString());

        return tag;
    }

    public void deserializeNBT(CompoundTag tag) {
        position = NbtUtils.readBlockPos(tag, "position").get();
        mobsRemaining = tag.getInt("mobs");
        generated = tag.getBoolean("generated");
        int doorPosSize = tag.getInt("door_positions_size");
        CompoundTag doorPosTag = tag.getCompound("door_positions");
        for (int i = 0; i < doorPosSize; i++) {
            doors.put(RoomDirection.values()[doorPosTag.getInt("direction_" + i)], new Door(doorPosTag.getCompound("door_" + i)));
        }
        structure = ModRoomStructures.registry.get().getValue(ResourceLocation.parse(tag.getString("structure")));
    }

    public record Door(DoorData data, BlockPos pos) {
        public Door(CompoundTag tag) {
            this(new DoorData(tag.getCompound("data")), NbtUtils.readBlockPos(tag, "pos").get());
        }

        public CompoundTag serializeNBT() {
            CompoundTag tag = new CompoundTag();
            tag.put("data", data.serializeNBT());
            tag.put("pos", NbtUtils.writeBlockPos(pos));
            return tag;
        }
    }


    public static List<Player> getPlayersInRoom(MinecraftServer server, Room room) {
        List<Player> players = new ArrayList<>();
        server.getPlayerList().getPlayers().forEach(serverPlayer -> {
            if (room.inRoom(serverPlayer.blockPosition())) {
                players.add(serverPlayer);
            }
        });
        return players;
    }
}
