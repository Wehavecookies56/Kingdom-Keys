package online.kingdomkeys.kingdomkeys.world.dimension.castle_oblivion.system.room;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.TagKey;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.neoforge.event.EventHooks;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.data.CastleOblivionData;
import online.kingdomkeys.kingdomkeys.data.GlobalData;
import online.kingdomkeys.kingdomkeys.entity.block.CardDoorTileEntity;
import online.kingdomkeys.kingdomkeys.lib.ModTags;
import online.kingdomkeys.kingdomkeys.util.Utils;
import online.kingdomkeys.kingdomkeys.world.dimension.castle_oblivion.CastleOblivionHandler;
import online.kingdomkeys.kingdomkeys.world.dimension.castle_oblivion.system.encounter.*;
import online.kingdomkeys.kingdomkeys.world.dimension.castle_oblivion.system.floor.Floor;
import online.kingdomkeys.kingdomkeys.world.dimension.castle_oblivion.system.registry.ModRoomStructures;
import online.kingdomkeys.kingdomkeys.world.dimension.castle_oblivion.system.registry.ModRoomTypes;

import javax.annotation.Nullable;
import java.io.IOException;
import java.util.*;

public class Room {
    RoomType type;
    RoomStructure structure;
    BlockPos position;
    int mobsRemaining, currentlySpawned;
    public Map<RoomDirection, Door> doors;
    public int parentFloor;
    int valueUsed;

    List<LivingEntity> cachedEntities = new ArrayList<>();

    List<BlockPos> spawnPoints;

    RoomPos roomPos;

    @Nullable EncounterInstance encounter;

    RoomStructure.RoomDimensions dimensions;

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
        this(ModRoomTypes.registry.get().getValue(ResourceLocation.parse(tag.getString("type"))), tag.getInt("parent"), RoomPos.deserializeNBT(tag.getCompound("room_pos")), tag.getInt("value_used"));
        deserializeNBT(tag);
    }

    public void roomEntered(@Nullable Room previousRoom, ServerPlayer player) {
        cachedEntities = getEntitiesInRoom((ServerLevel) player.level(), this);
        if (currentlySpawned != cachedEntities.size()) {
            currentlySpawned = cachedEntities.size();
        }
        if (previousRoom != null) {
            previousRoom.getType().getModifiers().forEach(roomModifier -> roomModifier.onExit(previousRoom, player));
            CastleOblivionData.InteriorData.get((ServerLevel) player.level()).ifPresent(interiorData -> {
                Floor floor = interiorData.getFloorByID(previousRoom.parentFloor);
                floor.getType().getGlobalModifiers().forEach(roomModifier -> roomModifier.onExit(previousRoom, player));
            });
        }
        getType().getModifiers().forEach(roomModifier -> roomModifier.onEnter(this, player));
        if (!getType().isEntranceHall()) {
            Floor floor = CastleOblivionData.InteriorData.get((ServerLevel) player.level()).orElseThrow().getFloorByID(parentFloor);
            floor.getType().getGlobalModifiers().forEach(roomModifier -> roomModifier.onEnter(this, player));
        }
        type.getEncounter().ifPresent(roomEncounter -> {
            //check if encounter is either not complete and start it again or if the encounter has not been started yet and start it
            if ((getEncounter().isPresent() && !getEncounter().get().isComplete()) || getEncounter().isEmpty()) {
                if (Room.getPlayersInRoom(player.server, this).size() == 1) {
                    encounter = roomEncounter.getEncounter().type().createInstance(roomEncounter);
                    encounter.start(this, (ServerLevel) player.level());
                }
            }
        });
    }

    public void removeCurrentSpawn() {
        currentlySpawned--;
    }

    public RoomStructure getStructure() {
        return structure;
    }

    public void setStructure(ServerLevel level, RoomStructure structure) {
        this.structure = structure;
        readDimensionsFromStructure(level);
    }

    public Optional<EncounterInstance> getEncounter() {
        return Optional.ofNullable(encounter);
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

    public int getMobsRemaining() {
        return mobsRemaining;
    }

    public int getCurrentlySpawned() {
        return currentlySpawned;
    }

    public void setMobsRemaining(int mobsRemaining) {
        this.mobsRemaining = mobsRemaining;
    }

    public void spawnMobs(int toSpawn) {
        if (mobsRemaining > 0) {
            currentlySpawned += Math.min(toSpawn, mobsRemaining);
            mobsRemaining -= currentlySpawned;
        }
    }

    public List<BlockPos> getSpawnPoints() {
        return spawnPoints;
    }

    long ticksSinceLastSpawn;

    public void addEntityToCache(LivingEntity entity) {
        this.cachedEntities.add(entity);
    }

    public void removeEntityFromCache(LivingEntity entity) {
        this.cachedEntities.remove(entity);
    }

    public void tick(ServerLevel level) {
        List<Player> players = getPlayersInRoom(level.getServer(), this);
        if (shouldRoomTick(players)) {
            if (ticksSinceLastSpawn > 100) {
                boolean invalidCache = false;
                for (LivingEntity entity : cachedEntities) {
                    if (level.getEntity(entity.getId()) == null) {
                        invalidCache = true;
                    }
                }
                if (invalidCache || currentlySpawned != cachedEntities.size()) {
                    cachedEntities = getEntitiesInRoom(level, this);
                    currentlySpawned = cachedEntities.size();
                    ticksSinceLastSpawn = 0;
                }
            }
            modifierOnTick(players);
            if (getEncounter().isPresent()) {
                EncounterInstance encounterInstance = getEncounter().get();
                if (!encounterInstance.isComplete()) {
                    ticksSinceLastSpawn++;
                    RoomEncounter roomEncounter = encounterInstance.getEncounter();
                    EncounterHandler<Encounter, EncounterState> handler = getEncounter().get().getEncounter().getHandler();
                    handler.tick(roomEncounter.getEncounter(), encounter.getState(), encounterInstance, this, level);
                    encounterInstance.tick(this, level);
                }
            } else {
                if (mobsRemaining > 0 && !spawnPoints.isEmpty()) {
                    if (currentlySpawned != type.getSimultaneousEnemies()) {
                        ticksSinceLastSpawn++;
                        if (ticksSinceLastSpawn > 100) {
                            int spawnIndex = Utils.randomWithRange(0, spawnPoints.size() - 1);
                            BlockPos spawnPoint = spawnPoints.get(spawnIndex);
                            TagKey<EntityType<?>> tag = getParent(CastleOblivionData.InteriorData.get(level).orElseThrow()).getType().getRegularEnemies();
                            if (type.getRegularEnemies() != null) {
                                tag = type.getRegularEnemies();
                            }
                            List<? extends EntityType<?>> entities = ModTags.getEntitiesInTag(level, tag);
                            int toSpawn = Utils.randomWithRange(0, entities.size() - 1);
                            LivingEntity spawned = (LivingEntity) entities.get(toSpawn).create(level);
                            if (spawned != null) {
                                cachedEntities.add(spawned);
                                GlobalData globalData = GlobalData.get(spawned);
                                globalData.setCastleOblivionMarker(true);
                                globalData.setLevel(((parentFloor + 1) * 10) + Utils.randomWithRange(-3, 3));
                                mobsRemaining--;
                                currentlySpawned++;
                                ticksSinceLastSpawn = 0;
                                modifierOnSpawn(spawned);
                                spawned.moveTo((double) spawnPoint.getX() + 0.5, spawnPoint.getY(), (double) spawnPoint.getZ() + 0.5, Mth.wrapDegrees(level.random.nextFloat() * 360.0F), 0.0F);
                                level.addFreshEntityWithPassengers(spawned);
                                if (spawned instanceof Mob spawnedMob) {
                                    EventHooks.finalizeMobSpawn(spawnedMob, level, level.getCurrentDifficultyAt(spawned.blockPosition()), MobSpawnType.TRIAL_SPAWNER, null);
                                }
                                KingdomKeys.LOGGER.debug("Spawned {}", spawned.toString());
                            }
                        }
                    }
                }
            }
        }
    }

    public boolean shouldRoomTick(List<Player> players) {
        return !players.isEmpty();
    }

    public boolean inRoom(BlockPos pos) {
        return pos.getX() >= position.getX() - 1 && pos.getX() <= position.getX() + getWidth() && pos.getZ() >= position.getZ() - 1 && pos.getZ() <= position.getZ() + getDepth();
    }

    public boolean clearRoom(ServerLevel level) {
        Floor parent = getParent(CastleOblivionData.InteriorData.get(level).orElseThrow());
        if (parent != null) {
            if (!shouldRoomTick(getPlayersInRoom(level.getServer(), this))) {
                BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos(position.getX(), position.getY(), position.getZ());
                getEntitiesInRoom(level, this).forEach(LivingEntity::kill);
                for (int z = 0; z < getWidth()+1; z++) {
                    for (int y = 0; y < 256; y++) {
                        for (int x = 0; x < getDepth()+1; x++) {
                            pos.set(position.getX() + x, position.getY() + y, position.getZ() + z);
                            Utils.setBlockWithoutUpdate(level, pos, Blocks.AIR.defaultBlockState());
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
        if (encounter != null) {
            tag.put("encounter", encounter.serializeNBT());
        }
        if (dimensions != null) {
            RoomStructure.RoomDimensions.CODEC.encodeStart(NbtOps.INSTANCE, dimensions).resultOrPartial(KingdomKeys.LOGGER::error).ifPresent(dimensionsTag -> {
                tag.put("structure_dimensions", dimensionsTag);
            });
        }
        return tag;
    }

    public void deserializeNBT(CompoundTag tag) {
        position = NbtUtils.readBlockPos(tag, "position").orElseThrow();
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
            spawnPoints.add(NbtUtils.readBlockPos(spawnPointsTag,"spawn_point_" + i).orElseThrow());
        }
        if (tag.contains("encounter")) {
            encounter = new EncounterInstance(tag.getCompound("encounter"));
        }
        if (tag.contains("structure_dimensions")) {
            dimensions = RoomStructure.RoomDimensions.CODEC.parse(NbtOps.INSTANCE, tag.get("structure_dimensions")).getPartialOrThrow(NbtException::new);
        }
    }

    public void setDoorLocks(ServerLevel level, boolean lock) {
        doors.values().forEach(door -> door.setLock(level, lock));
        KingdomKeys.LOGGER.debug("Doors locked: {}", lock);
    }

    public record Door(DoorData data, BlockPos pos) {
        public Door(CompoundTag tag) {
            this(new DoorData(tag.getCompound("data")), NbtUtils.readBlockPos(tag, "pos").orElseThrow());
        }

        public CompoundTag serializeNBT() {
            CompoundTag tag = new CompoundTag();
            tag.put("data", data.serializeNBT());
            tag.put("pos", NbtUtils.writeBlockPos(pos));
            return tag;
        }

        public void setLock(ServerLevel level, boolean lock) {
            if (level.getBlockEntity(pos) instanceof CardDoorTileEntity cardDoorTileEntity) {
                if (cardDoorTileEntity.isLocked() != lock) {
                    cardDoorTileEntity.toggleDoorLock();
                }
            }
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

    public static List<LivingEntity> getEntitiesInRoom(ServerLevel level, Room room) {
        List<LivingEntity> entities = new ArrayList<>();
        level.getAllEntities().forEach(entity -> {
            if (entity instanceof LivingEntity livingEntity) {
                if (!(entity instanceof Player)) {
                    if (GlobalData.get(livingEntity).getCastleOblivionMarker()) {
                        if (room.inRoom(livingEntity.blockPosition())) {
                            entities.add(livingEntity);
                        }
                    }
                }
            }
        });
        return entities;
    }

    public void readDimensionsFromStructure(ServerLevel level) {
        if (structure.getDimensionsCache().isEmpty()) {
            Floor parent = getParent(CastleOblivionData.InteriorData.get(level).orElseThrow());
            try {
                structure.getStructureFile(level, parent.getType()).orElseThrow(IOException::new);
                dimensions = structure.getDimensionsCache().orElseThrow();
            } catch (IOException e) {
                KingdomKeys.LOGGER.error("Failed to read structure file", e.fillInStackTrace());
            }
        } else {
            KingdomKeys.LOGGER.debug("Dimensions were cached, nice!");
            dimensions = structure.getDimensionsCache().get();
        }
    }

    public int getWidth() {
        return dimensions.width();
    }

    public int getHeight() {
        return dimensions.height();

    }

    public int getDepth() {
        return dimensions.depth();
    }

    public Optional<RoomStructure.RoomDimensions> getDimensions() {
        return Optional.ofNullable(dimensions);
    }

    //Modifier shortcut methods

    public void modifierOnTick(List<Player> players) {
        getType().getModifiers().forEach(roomModifier -> roomModifier.tick(this, players));
    }

    public void modifierOnGenerate(ServerLevel level) {
        getType().getModifiers().forEach(roomModifier -> roomModifier.onGenerate(this, level));
    }

    public void modifierOnSpawn(LivingEntity spawned) {
        getType().getModifiers().forEach(roomModifier -> roomModifier.onSpawn(this, spawned));
    }

    public void modifierOnEnter(Player player) {
        getType().getModifiers().forEach(roomModifier -> roomModifier.onEnter(this, player));
    }

    public void modifierOnExit(Player player) {
        getType().getModifiers().forEach(roomModifier -> roomModifier.onExit(this, player));
    }

    @Override
    public String toString() {
        return type.toString();
    }
}
