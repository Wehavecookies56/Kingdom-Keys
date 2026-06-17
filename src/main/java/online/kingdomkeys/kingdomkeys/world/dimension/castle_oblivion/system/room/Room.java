package online.kingdomkeys.kingdomkeys.world.dimension.castle_oblivion.system.room;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.data.CastleOblivionData;
import online.kingdomkeys.kingdomkeys.data.GlobalData;
import online.kingdomkeys.kingdomkeys.entity.block.CardDoorTileEntity;
import online.kingdomkeys.kingdomkeys.lib.ModTags;
import online.kingdomkeys.kingdomkeys.util.Utils;
import online.kingdomkeys.kingdomkeys.world.dimension.castle_oblivion.CastleOblivionHandler;
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
    int mobsRemaining, currentlySpawned;
    public Map<RoomDirection, Door> doors;
    public int parentFloor;
    int valueUsed;

    Level level;

    List<BlockPos> spawnPoints;

    RoomPos roomPos;

    //Constructor used when generating a room
    public Room(RoomType type, int parentFloor, RoomPos roomPos, int valueUsed) {
        this.type = type;
        this.parentFloor = parentFloor;
        this.doors = new HashMap<>();
        this.roomPos = roomPos;
        this.spawnPoints = new ArrayList<>();
        this.valueUsed = valueUsed;
        this.mobsRemaining = type.getNumberOfEnemies();
    }

    //Deserialization constructor
    public Room(CompoundTag tag) {
        this(ModRoomTypes.registry.get().getValue(ResourceLocation.parse(tag.getString("type"))), tag.getInt("parent"), new RoomPos(tag.getCompound("room_pos")), tag.getInt("value_used"));
        deserializeNBT(tag);
    }

    public void removeCurrentSpawn() {
        currentlySpawned--;
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
        RoomData oldRoomData = getParent(CastleOblivionData.InteriorData.get(level).orElseThrow()).getRoom(roomPos);
        oldRoomData.getGenerated().ifPresent(room -> room.clearRoom(level));
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

    public int getValueUsed() {
        return valueUsed;
    }

    public RoomData getRoomData(CastleOblivionData.InteriorData data) {
        return getParent(data).getRoom(getRoomPos());
    }

    public Floor getParent(CastleOblivionData.InteriorData data) {
        return data.getFloorByID(parentFloor);
    }

    public void addSpawnPoint(BlockPos pos) {
        this.spawnPoints.add(pos);
        KingdomKeys.LOGGER.debug("Found spawn point #{} [{}]", spawnPoints.size(), pos.toShortString());
    }

    long ticksSinceLastSpawn;

    public void tick(ServerLevel level) {
        List<Player> players = getPlayersInRoom(level.getServer(), this);
        if (shouldRoomTick(players)) {
            type.getModifiers().forEach(roomModifier -> roomModifier.tick(this, players));

            if (mobsRemaining > 0 && !spawnPoints.isEmpty()) {
                if (currentlySpawned != type.getSimultaneousEnemies()) {
                    ticksSinceLastSpawn++;
                    if (ticksSinceLastSpawn > 100) {
                        int spawnIndex = Utils.randomWithRange(0, spawnPoints.size()-1);
                        BlockPos spawnPoint = spawnPoints.get(spawnIndex);
                        TagKey<EntityType<?>> tag = getParent(CastleOblivionData.InteriorData.get(level).orElseThrow()).getType().getRegularEnemies();
                        if (type.getRegularEnemies() != null) {
                            tag = type.getRegularEnemies();
                        }
                        List<? extends EntityType<?>> entities = ModTags.getEntitiesInTag(level, tag);
                        int toSpawn = Utils.randomWithRange(0, entities.size()-1);
                        LivingEntity spawned = (LivingEntity) entities.get(toSpawn).spawn(level, spawnPoint, MobSpawnType.SPAWNER);
                        GlobalData.get(spawned).setCastleOblivionMarker(true);
                        mobsRemaining--;
                        currentlySpawned++;
                        ticksSinceLastSpawn = 0;
                        KingdomKeys.LOGGER.debug("Spawned {}", spawned.toString());
                    }
                }
            }
        }
    }

    public boolean shouldRoomTick(List<Player> players) {
        return !players.isEmpty();
    }

    public boolean inRoom(BlockPos pos) {
        return pos.getX() >= position.getX() - 1 && pos.getX() <= position.getX() + structure.getWidth() && pos.getZ() >= position.getZ() - 1 && pos.getZ() <= position.getZ() + structure.getDepth();
    }

    public boolean clearRoom(ServerLevel level) {
        Floor parent = getParent(CastleOblivionData.InteriorData.get(level).orElseThrow());
        if (parent != null) {
            if (!shouldRoomTick(getPlayersInRoom(level.getServer(), this))) {
                BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos(position.getX(), position.getY(), position.getZ());
                //TODO kill entities
                KingdomKeys.LOGGER.debug(pos);
                for (int z = 0; z < structure.getWidth()+1; z++) {
                    for (int y = 0; y < 128; y++) {
                        for (int x = 0; x < structure.getDepth()+1; x++) {
                            pos.set(position.getX() + x, y, position.getZ() + z);
                            level.setBlock(pos, Blocks.AIR.defaultBlockState(), 2);
                        }
                    }
                }
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
        tag.putInt("current_mobs", currentlySpawned);
        tag.putInt("door_positions_size", doors.size());
        CompoundTag doorPosTag = new CompoundTag();
        int i = 0;
        for (Map.Entry<RoomDirection, Door> doorPos : doors.entrySet()) {
            doorPosTag.putInt("direction_" + i, doorPos.getKey().ordinal());
            doorPosTag.put("door_" + i, doorPos.getValue().serializeNBT());
            i++;
        }
        tag.put("door_positions", doorPosTag);
        tag.putInt("spawn_points_size", spawnPoints.size());
        tag.putString("structure", structure.getRegistryName().toString());
        CompoundTag spawnPointsTag = new CompoundTag();
        for (int s = 0; s < spawnPoints.size(); s++) {
            spawnPointsTag.put("spawn_point_" + s, NbtUtils.writeBlockPos(spawnPoints.get(s)));
        }
        tag.put("spawn_points", spawnPointsTag);
        tag.putInt("value_used", valueUsed);
        return tag;
    }

    public void deserializeNBT(CompoundTag tag) {
        position = NbtUtils.readBlockPos(tag, "position").get();
        mobsRemaining = tag.getInt("mobs");
        currentlySpawned = tag.getInt("current_mobs");
        int doorPosSize = tag.getInt("door_positions_size");
        CompoundTag doorPosTag = tag.getCompound("door_positions");
        for (int i = 0; i < doorPosSize; i++) {
            doors.put(RoomDirection.values()[doorPosTag.getInt("direction_" + i)], new Door(doorPosTag.getCompound("door_" + i)));
        }
        structure = ModRoomStructures.registry.get().getValue(ResourceLocation.parse(tag.getString("structure")));
        CompoundTag spawnPointsTag = tag.getCompound("spawn_points");
        int spawnPointsSize = tag.getInt("spawn_points_size");
        for (int i = 0; i < spawnPointsSize; i++) {
            spawnPoints.add(NbtUtils.readBlockPos(spawnPointsTag,"spawn_point_" + i).get());
        }
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
            if (CastleOblivionHandler.inInterior(serverPlayer)) {
                if (room.inRoom(serverPlayer.blockPosition())) {
                    players.add(serverPlayer);
                }
            }
        });
        return players;
    }

    @Override
    public String toString() {
        return type.toString();
    }
}
