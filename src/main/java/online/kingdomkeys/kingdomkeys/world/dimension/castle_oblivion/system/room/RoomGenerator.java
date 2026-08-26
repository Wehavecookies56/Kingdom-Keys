package online.kingdomkeys.kingdomkeys.world.dimension.castle_oblivion.system.room;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.StructureBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.StructureBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.StructureMode;
import net.neoforged.neoforge.common.NeoForge;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.api.event.CastleOblivionEvent;
import online.kingdomkeys.kingdomkeys.block.CardDoorBlock;
import online.kingdomkeys.kingdomkeys.block.ModBlocks;
import online.kingdomkeys.kingdomkeys.block.StructureWallBlock;
import online.kingdomkeys.kingdomkeys.data.CastleOblivionData;
import online.kingdomkeys.kingdomkeys.entity.block.CardDoorTileEntity;
import online.kingdomkeys.kingdomkeys.entity.block.TreasureChestTileEntity;
import online.kingdomkeys.kingdomkeys.network.stc.SCSyncCastleOblivionInteriorData;
import online.kingdomkeys.kingdomkeys.util.Utils;
import online.kingdomkeys.kingdomkeys.world.dimension.castle_oblivion.system.floor.Floor;
import online.kingdomkeys.kingdomkeys.world.dimension.castle_oblivion.system.registry.ModRoomStructures;
import online.kingdomkeys.kingdomkeys.world.dimension.castle_oblivion.system.registry.ModRoomTypes;

import javax.annotation.Nullable;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

public class RoomGenerator {

    public static final RoomGenerator INSTANCE = new RoomGenerator();

    private RoomGenerator() {}

    public Room generateNewFloor(ServerLevel level) {
        CastleOblivionData.InteriorData interiorData = CastleOblivionData.InteriorData.get(level).orElseThrow();
        Floor currentFloor = new Floor(level);
        Room room = new Room(ModRoomTypes.ENTRANCE_HALL.get(), currentFloor.getFloorID(), RoomPos.ZERO, 0);
        Floor prevFloor = interiorData.getFloorByID(currentFloor.getFloorID()-1);
        room.setPosition(prevFloor.getNorthernMostRoomPosition().relative(Direction.SOUTH, 512));
        return generateRoom(level, currentFloor.getRoom(RoomPos.ZERO), room, null);
    }

    public Room generateRoom(ServerLevel level, RoomData data, RoomType type, Room currentRoom, RoomDirection doorDirection, int valueUsed) {
        if (type.equals(ModRoomTypes.ENTRANCE_HALL.get()) && data.pos.equals(RoomPos.ZERO)) {
            KingdomKeys.LOGGER.warn("Tried to generate room type {} at entrance hall position", type.getRegistryName().toString());
            return null;
        }
        CastleOblivionData.InteriorData interiorData = CastleOblivionData.InteriorData.get(level).orElseThrow();
        Floor currentFloor = interiorData.getFloorByID(currentRoom.parentFloor);
        Room room = new Room(type, currentFloor.getFloorID(), data.pos, valueUsed);
        room.createRoomFromCard(type, level, currentRoom, doorDirection);

        return generateRoom(level, data, room, currentRoom);
    }

    private Room generateRoom(ServerLevel level, RoomData data, Room newRoom, @Nullable Room currentRoom) {
        try {
            CastleOblivionData.InteriorData interiorData = CastleOblivionData.InteriorData.get(level).orElseThrow();
            Floor currentFloor = interiorData.getFloorByID(newRoom.parentFloor);
            BlockPos pos = newRoom.position;
            KingdomKeys.LOGGER.debug("Finding compatible structures for {}", newRoom.getType());
            List<RoomStructure> possibleRooms = ModRoomStructures.getCompatibleStructures(level, currentFloor.getType(), newRoom.type);
            if (possibleRooms.isEmpty()) {
                KingdomKeys.LOGGER.warn("No compatible room structure files found for {}, using fallback room", newRoom.type.getRegistryName());
                possibleRooms = ModRoomStructures.getFallbacks();
            }
            RoomStructure structureToGenerate = possibleRooms.get(Utils.randomWithRange(0, possibleRooms.size()-1));
            KingdomKeys.LOGGER.debug("Found {} compatible structures, {} selected", possibleRooms.size(), structureToGenerate.getRegistryName());
            Resource resource = structureToGenerate.getStructureFile(level, currentFloor.getType()).orElseThrow(IOException::new);
            KingdomKeys.LOGGER.debug("Generating structure file {}:{}/{}.nbt", currentFloor.getType().getRegistryName().getNamespace(), structureToGenerate.useFloorSpecificStructure() ? currentFloor.getType().getRegistryName().getPath() : "all", structureToGenerate.getPath());
            CompoundTag main = NbtIo.readCompressed(resource.open(), NbtAccounter.unlimitedHeap());
            newRoom.setStructure(level, structureToGenerate);

            ListTag size = main.getList("size", Tag.TAG_INT);
            ListTag palette = main.getList("palette", Tag.TAG_COMPOUND);
            ListTag blocks = main.getList("blocks", Tag.TAG_COMPOUND);
            List<BlockState> blockStates = new ArrayList<>();

            if (!newRoom.getType().isEntranceHall()) {
                //Create room walls
                int width = size.getInt(0);
                int height = size.getInt(1);
                int depth = size.getInt(2);

                List<Wall> walls = List.of(
                        new Wall(pos.north(), pos.north().offset(width -1, height -1, 0), Direction.SOUTH),
                        new Wall(pos.south(depth), pos.south(depth).offset(width -1, height -1, 0), Direction.NORTH),
                        new Wall(pos.west(), pos.west().offset(0, height -1, depth - 1), Direction.EAST),
                        new Wall(pos.east(width), pos.east(width).offset(0, height -1, depth - 1), Direction.WEST),
                        new Wall(pos.below(), pos.below().offset(width -1, 0, depth -1), Direction.UP),
                        new Wall(pos.above(height), pos.above(height).offset(width -1, 0, depth - 1), Direction.DOWN)
                );

                walls.forEach(wall -> fillWall(level, wall.start, wall.end, wall.facing));
                KingdomKeys.LOGGER.debug("Generated walls");
            }

            CompoundTag block = blocks.getCompound(0);
            BlockPos.MutableBlockPos blockpos = new BlockPos.MutableBlockPos(block.getList("pos", 3).getInt(0), block.getList("pos", 3).getInt(1), block.getList("pos", 3).getInt(2));
            BlockState state;

            for (int i = 0; i < palette.size(); i++) {
                block = palette.getCompound(i);
                blockStates.add(NbtUtils.readBlockState(level.holderLookup(Registries.BLOCK),block));
            }
            KingdomKeys.LOGGER.debug("Read block palette");

            RoomType.Treasure treasure = newRoom.getType().getTreasure().orElse(null);

            for (int i = 0; i < blocks.size(); i++) {
                block = blocks.getCompound(i);
                blockpos.set(block.getList("pos", 3).getInt(0) + pos.getX(), block.getList("pos", 3).getInt(1) + pos.getY(), block.getList("pos", 3).getInt(2) + pos.getZ());
                state = blockStates.get(block.getInt("state"));
                if (state.getBlock() == ModBlocks.treasureChest.get()) {
                    newRoom.addTreasurePoint(blockpos.immutable(), state);
                } else if (state.getBlock() == Blocks.STRUCTURE_BLOCK) {
                    if (state.getValue(StructureBlock.MODE).equals(StructureMode.DATA)) {
                        //Replace data mode structure blocks with card doors
                        StructureBlockEntity be = new StructureBlockEntity(blockpos, state);
                        be.loadCustomOnly(block.getCompound("nbt"), level.registryAccess());
                        if (be.getMetaData().equals("spawn")) {
                            newRoom.addSpawnPoint(blockpos.immutable());
                        } else {
                            BlockState cardDoorState = ModBlocks.cardDoor.get().defaultBlockState().setValue(CardDoorBlock.GENERATED, true);
                            RoomDirection facing = switch (be.getMetaData()) {
                                case "north" -> RoomDirection.SOUTH;
                                case "west" -> RoomDirection.EAST;
                                case "east" -> RoomDirection.WEST;
                                case "south" -> RoomDirection.NORTH;
                                default -> null;
                            };
                            if (facing != null) {
                                KingdomKeys.LOGGER.debug("Generating card door facing {}", facing);
                                cardDoorState = cardDoorState.setValue(CardDoorBlock.FACING, facing.toMCDirection().getOpposite());
                                DoorData doorData = data.getDoor(facing);
                                if (doorData != null && doorData.getType() != DoorData.Type.NONE) {
                                    newRoom.doors.put(facing, new Room.Door(doorData, blockpos.immutable()));
                                    //exit and entrance doors don't have adjacent rooms so no need to check
                                    if (doorData.getType() == DoorData.Type.EXIT || doorData.getType() == DoorData.Type.ENTRANCE) {
                                        cardDoorState = cardDoorState.setValue(CardDoorBlock.OPEN, true);
                                        new CardDoorTileEntityBuilder(blockpos, cardDoorState, data, facing, doorData).openDoor(false).build(level);
                                        KingdomKeys.LOGGER.debug("Placed open {} card door", doorData.getType());
                                    } else if (doorData.getType() == DoorData.Type.HALL) {
                                        new CardDoorTileEntityBuilder(blockpos, cardDoorState, data, facing, doorData).build(level);
                                        KingdomKeys.LOGGER.debug("Placed entrance hall door");
                                    } else {
                                        //check for adjacent rooms for non EXIT or ENTRANCE doors
                                        Optional<RoomData> adjacentRoom = currentFloor.getAdjacentRoom(data, facing);
                                        if (adjacentRoom.isPresent()) {
                                            if (adjacentRoom.get().getGenerated().isPresent()) {
                                                BlockPos adjacentDoorPos = adjacentRoom.get().getGenerated().get().doors.get(facing.opposite()).pos();
                                                CardDoorTileEntity adjacentDoorTE = (CardDoorTileEntity) level.getBlockEntity(adjacentDoorPos);
                                                if (adjacentDoorTE != null && adjacentDoorTE.isOpen()) {
                                                    cardDoorState = cardDoorState.setValue(CardDoorBlock.OPEN, true);
                                                    adjacentDoorTE.setDestinationRoom(data);
                                                }
                                            }
                                            if (adjacentRoom.get().getDoors().get(facing.opposite()) != null) {
                                                new CardDoorTileEntityBuilder(blockpos, cardDoorState, data, facing, doorData).destination(adjacentRoom.get()).openDoor(false).generateCardCriteria(newRoom).build(level);
                                                KingdomKeys.LOGGER.debug("Placed normal card door");
                                            }
                                        }
                                    }
                                } else {
                                    level.setBlock(blockpos, Blocks.AIR.defaultBlockState(), 2);
                                }
                            }
                        }
                    }
                } else {
                    level.setBlock(blockpos, state, 2);
                    //create block entity and load nbt
                    if (block.contains("nbt")) {
                        CompoundTag nbtData = block.getCompound("nbt");
                        ResourceLocation blockEntityID = KingdomKeys.rl(nbtData.getString("id"));
                        BlockEntityType<?> type = BuiltInRegistries.BLOCK_ENTITY_TYPE.get(blockEntityID);
                        if (type != null) {
                            BlockEntity blockEntity = type.create(blockpos, state);
                            if (blockEntity != null) {
                                blockEntity.loadCustomOnly(nbtData, level.registryAccess());
                                level.setBlockEntity(blockEntity);
                            }
                        }
                    }
                }
            }
            Collections.shuffle(newRoom.spawnPoints);
            Collections.shuffle(newRoom.treasurePoints);
            if (newRoom.spawnPoints.isEmpty() && newRoom.getType().getEnemies() != RoomEnemies.NONE) {
                KingdomKeys.LOGGER.warn("Room Structure contains no spawn points for Room Type that contains enemies");
            }
            if (newRoom.treasurePoints.isEmpty() && treasure != null) {
                KingdomKeys.LOGGER.warn("Room Structure contains no treasure chests for Room Type that contains treasures");
            } else {
                if (treasure != null) {
                    final int totalChestsToCreate = treasure.count() + treasure.trappedCount();
                    AtomicInteger chestsCreated = new AtomicInteger();
                    newRoom.treasurePoints.forEach(treasurePoint -> {
                        if (chestsCreated.get() < totalChestsToCreate) {
                            level.setBlock(treasurePoint.pos(), treasurePoint.state(), 2);
                            TreasureChestTileEntity.create(level, treasurePoint.pos(), treasurePoint.state(), treasure, chestsCreated.get() >= treasure.count());
                            chestsCreated.getAndIncrement();
                        }
                    });
                }
            }
            data.setGenerated(newRoom);
            newRoom.modifierOnGenerate(level);
            CastleOblivionData.InteriorData.get(level).orElseThrow().setDirty();
            SCSyncCastleOblivionInteriorData.syncClients(level);
            KingdomKeys.LOGGER.info("Generated room:{} at {}", newRoom.type.getRegistryName().toString(), pos);
            KingdomKeys.LOGGER.info("Room has {} spawn points, {} treasure points", newRoom.spawnPoints.size(), newRoom.treasurePoints.size());
            NeoForge.EVENT_BUS.post(new CastleOblivionEvent.RoomGeneratedEvent(level, data, currentRoom));
            return newRoom;
        } catch (IOException e){
            e.printStackTrace();
            return null;
        }
    }

    private static void fillWall(Level level, BlockPos start, BlockPos end, Direction facing) {
        BlockState state = ModBlocks.structureWall.get().defaultBlockState().setValue(StructureWallBlock.FACING, facing);
        BlockPos.betweenClosedStream(start, end).forEach(pos -> level.setBlock(pos, state, Block.UPDATE_CLIENTS));
    }

    record Wall(BlockPos start, BlockPos end, Direction facing) {}

    private static class CardDoorTileEntityBuilder {
        final CardDoorTileEntity cardDoorTileEntity;

        public CardDoorTileEntityBuilder (BlockPos pos, BlockState state, RoomData parent, RoomDirection facing, DoorData data) {
            cardDoorTileEntity = new CardDoorTileEntity(pos, state);
            cardDoorTileEntity.setParent(parent);
            cardDoorTileEntity.setDirection(facing);
            cardDoorTileEntity.setData(data);
        }

        public CardDoorTileEntityBuilder destination(RoomData destination) {
            cardDoorTileEntity.setDestinationRoom(destination);
            return this;
        }

        public CardDoorTileEntityBuilder openDoor(boolean setBlock) {
            cardDoorTileEntity.openDoor(setBlock);
            return this;
        }

        public CardDoorTileEntityBuilder generateCardCriteria(Room newRoom) {
            cardDoorTileEntity.getData().generateCardCriteria(newRoom.valueUsed);
            cardDoorTileEntity.setCurrentCriteria(cardDoorTileEntity.getData().getCardCriteria());
            return this;
        }

        public CardDoorTileEntity build(Level level) {
            level.setBlock(cardDoorTileEntity.getBlockPos(), cardDoorTileEntity.getBlockState(), Block.UPDATE_CLIENTS);
            level.setBlockEntity(cardDoorTileEntity);
            level.sendBlockUpdated(cardDoorTileEntity.getBlockPos(), cardDoorTileEntity.getBlockState(), cardDoorTileEntity.getBlockState(), Block.UPDATE_CLIENTS);
            return cardDoorTileEntity;
        }
    }

}
