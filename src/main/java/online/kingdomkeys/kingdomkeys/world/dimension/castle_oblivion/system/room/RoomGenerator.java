package online.kingdomkeys.kingdomkeys.world.dimension.castle_oblivion.system.room;

import com.mojang.datafixers.util.Pair;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.StructureBlock;
import net.minecraft.world.level.block.entity.StructureBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.StructureMode;
import net.neoforged.neoforge.common.NeoForge;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.block.CardDoorBlock;
import online.kingdomkeys.kingdomkeys.block.ModBlocks;
import online.kingdomkeys.kingdomkeys.data.CastleOblivionData;
import online.kingdomkeys.kingdomkeys.entity.block.CardDoorTileEntity;
import online.kingdomkeys.kingdomkeys.network.stc.SCSyncCastleOblivionInteriorData;
import online.kingdomkeys.kingdomkeys.util.Utils;
import online.kingdomkeys.kingdomkeys.world.dimension.castle_oblivion.system.CastleOblivionEvent;
import online.kingdomkeys.kingdomkeys.world.dimension.castle_oblivion.system.floor.Floor;
import online.kingdomkeys.kingdomkeys.world.dimension.castle_oblivion.system.registry.ModRoomStructures;
import online.kingdomkeys.kingdomkeys.world.dimension.castle_oblivion.system.registry.ModRoomTypes;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Nullable;

public class RoomGenerator {

    public static final RoomGenerator INSTANCE = new RoomGenerator();

    private RoomGenerator() {}

    public Room generateNewFloor(ServerLevel level) {
        CastleOblivionData.InteriorData interiorData = CastleOblivionData.InteriorData.get(level);
        Floor currentFloor = new Floor(level);
        Room room = new Room(ModRoomTypes.ENTRANCE_HALL.get(), currentFloor.getFloorID(), RoomPos.ZERO);
        Floor prevFloor = interiorData.getFloorByID(currentFloor.getFloorID()-1);
        room.setPosition(prevFloor.getNorthernMostRoomPosition().relative(Direction.SOUTH, 512));
        return generateRoom(level, currentFloor.getRoom(RoomPos.ZERO), room, null);
    }

    public Room generateRoom(ServerLevel level, RoomData data, RoomType type, @Nullable Room currentRoom, @Nullable RoomDirection doorDirection) {
        if (type.equals(ModRoomTypes.ENTRANCE_HALL.get()) && data.pos.equals(RoomPos.ZERO)) {
            KingdomKeys.LOGGER.warn("Tried to generate room type {} at entrance hall position", type.getRegistryName().toString());
            return null;
        }
        CastleOblivionData.InteriorData interiorData = CastleOblivionData.InteriorData.get(level);
        Floor currentFloor = interiorData.getFloorByID(currentRoom.parentFloor);
        Room room = new Room(type, currentFloor.getFloorID(), data.pos);
        room.createRoomFromCard(type, level, currentRoom, doorDirection);

        return generateRoom(level, data, room, currentRoom);
    }

    private Room generateRoom(ServerLevel level, RoomData data, Room newRoom, @Nullable Room currentRoom) {
        try {
            CastleOblivionData.InteriorData interiorData = CastleOblivionData.InteriorData.get(level);
            Floor currentFloor = interiorData.getFloorByID(newRoom.parentFloor);
            BlockPos pos = newRoom.position;
            List<RoomStructure> possibleRooms = ModRoomStructures.getCompatibleStructures(currentFloor.getType(), newRoom.type);
            if (possibleRooms.isEmpty()) {
                throw new IOException(String.format("No compatible room structure files found for %s", newRoom.type.getRegistryName()));
            }
            RoomStructure structureToGenerate = possibleRooms.get(Utils.randomWithRange(0, possibleRooms.size()-1));
            String floorFolder = structureToGenerate.getFloor() == null ? "all" : structureToGenerate.getFloor().getRegistryName().getPath();
            Resource resource = level.getServer().getResourceManager().getResource(ResourceLocation.fromNamespaceAndPath(KingdomKeys.MODID, "structure/castle_oblivion/rooms/" + floorFolder + "/" + structureToGenerate.getPath() + ".nbt")).get();
            CompoundTag main = NbtIo.readCompressed(resource.open(), NbtAccounter.unlimitedHeap());
            newRoom.setStructure(structureToGenerate);

            ListTag palette = main.getList("palette", Tag.TAG_COMPOUND);

            ListTag blocks = main.getList("blocks", Tag.TAG_COMPOUND);

            List<BlockState> blockStates = new ArrayList<>();


            CompoundTag block = blocks.getCompound(0);
            BlockPos.MutableBlockPos blockpos = new BlockPos.MutableBlockPos(block.getList("pos", 3).getInt(0), block.getList("pos", 3).getInt(1), block.getList("pos", 3).getInt(2));
            BlockState state;

            for (int i = 0; i < palette.size(); i++) {
                block = palette.getCompound(i);
                blockStates.add(NbtUtils.readBlockState(level.holderLookup(Registries.BLOCK),block));
            }

            for (int i = 0; i < blocks.size(); i++) {
                block = blocks.getCompound(i);
                blockpos.set(block.getList("pos", 3).getInt(0) + pos.getX(), block.getList("pos", 3).getInt(1) + pos.getY(), block.getList("pos", 3).getInt(2) + pos.getZ());
                state = blockStates.get(block.getInt("state"));
                if (state.getBlock() == Blocks.STRUCTURE_BLOCK) {
                    if (state.getValue(StructureBlock.MODE).equals(StructureMode.DATA)) {
                        //Replace data mode structure blocks with card doors
                        StructureBlockEntity be = new StructureBlockEntity(blockpos, state);
                        be.loadCustomOnly(block.getCompound("nbt"), level.registryAccess());

                        BlockState cardDoorState = ModBlocks.cardDoor.get().defaultBlockState().setValue(CardDoorBlock.GENERATED, true);
                        RoomDirection facing = switch (be.getMetaData()) {
                            case "north" -> RoomDirection.SOUTH;
                            case "west" -> RoomDirection.EAST;
                            case "east" -> RoomDirection.WEST;
                            case "south" -> RoomDirection.NORTH;
                            default -> null;
                        };
                        if (facing != null) {
                            cardDoorState = cardDoorState.setValue(CardDoorBlock.FACING, facing.toMCDirection().getOpposite());
                            DoorData doorData = data.getDoor(facing);
                            if (doorData != null && doorData.getType() != DoorData.Type.NONE) {
                                newRoom.doors.put(facing, new Room.Door(doorData, blockpos.immutable()));
                                //exit and entrance doors don't have adjacent rooms so no need to check
                                if (doorData.getType() == DoorData.Type.EXIT || doorData.getType() == DoorData.Type.ENTRANCE) {
                                    cardDoorState = cardDoorState.setValue(CardDoorBlock.OPEN, true);
                                    level.setBlock(blockpos, cardDoorState, 2);
                                    CardDoorTileEntity cardDoorTileEntity = new CardDoorTileEntity(blockpos, cardDoorState);
                                    cardDoorTileEntity.setParent(data);
                                    cardDoorTileEntity.setDirection(facing);
                                    cardDoorTileEntity.setData(doorData);
                                    cardDoorTileEntity.openDoor(false);
                                    level.setBlockEntity(cardDoorTileEntity);
                                } else if (doorData.getType() == DoorData.Type.HALL) {
                                    level.setBlock(blockpos, cardDoorState, 2);
                                    CardDoorTileEntity cardDoorTileEntity = new CardDoorTileEntity(blockpos, cardDoorState);
                                    cardDoorTileEntity.setParent(data);
                                    cardDoorTileEntity.setDirection(facing);
                                    cardDoorTileEntity.setData(doorData);
                                    level.setBlockEntity(cardDoorTileEntity);
                                } else {
                                    //check for adjacent rooms for non EXIT or ENTRANCE doors
                                    RoomData adjacentRoom = currentFloor.getAdjacentRoom(data, facing);
                                    if (adjacentRoom != null) {
                                        if (adjacentRoom.getGenerated() != null) {
                                            BlockPos adjacentDoorPos = adjacentRoom.getGenerated().doors.get(facing.opposite()).pos();
                                            CardDoorTileEntity adjacentDoorTE = (CardDoorTileEntity) level.getBlockEntity(adjacentDoorPos);
                                            if (adjacentDoorTE != null && adjacentDoorTE.isOpen()) {
                                                cardDoorState = cardDoorState.setValue(CardDoorBlock.OPEN, true);
                                                adjacentDoorTE.setDestinationRoom(data);
                                            }
                                        }
                                        if (adjacentRoom.getDoors().get(facing.opposite()) != null) {
                                            level.setBlock(blockpos, cardDoorState, 2);
                                            CardDoorTileEntity cardDoorTileEntity = new CardDoorTileEntity(blockpos, cardDoorState);
                                            cardDoorTileEntity.setParent(data);
                                            cardDoorTileEntity.setDirection(facing);
                                            cardDoorTileEntity.setDestinationRoom(adjacentRoom);
                                            cardDoorTileEntity.setData(doorData);
                                            cardDoorTileEntity.openDoor(false);
                                            level.setBlockEntity(cardDoorTileEntity);
                                        }
                                    }
                                }
                            } else {
                                level.setBlock(blockpos, Blocks.AIR.defaultBlockState(), 2);
                            }
                        }
                    }
                } else {
                    level.setBlock(blockpos, state, 2);
                }
            }
            data.setGenerated(newRoom);
            SCSyncCastleOblivionInteriorData.syncClients(level);
            KingdomKeys.LOGGER.info("Generated room:{} at {}", newRoom.type.getRegistryName().toString(), pos);
            NeoForge.EVENT_BUS.post(new CastleOblivionEvent.RoomGeneratedEvent(level, data, currentRoom));
            return newRoom;
        } catch (IOException e){
            e.printStackTrace();
            return null;
        }
    }

}
