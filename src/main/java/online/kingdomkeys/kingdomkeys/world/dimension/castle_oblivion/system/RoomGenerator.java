package online.kingdomkeys.kingdomkeys.world.dimension.castle_oblivion.system;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.mojang.datafixers.util.Pair;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.StructureBlock;
import net.minecraft.world.level.block.entity.StructureBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.StructureMode;
import net.minecraftforge.common.MinecraftForge;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.block.CardDoorBlock;
import online.kingdomkeys.kingdomkeys.block.ModBlocks;
import online.kingdomkeys.kingdomkeys.capability.ModCapabilities;
import online.kingdomkeys.kingdomkeys.entity.block.CardDoorTileEntity;
import online.kingdomkeys.kingdomkeys.network.stc.SCSyncCastleOblivionInteriorCapability;
import online.kingdomkeys.kingdomkeys.util.Utils;

public class RoomGenerator {

    public static final RoomGenerator INSTANCE = new RoomGenerator();

    private RoomGenerator() {}

    public Room generateRoom(RoomData data, RoomType type, Player player, Room currentRoom, RoomUtils.Direction doorDirection, boolean newFloor) {
        try {
            //todo new floor handling
            Room room = new Room(currentRoom.parentFloor, data.pos);
            room.createRoomFromCard(type, player.level(), currentRoom, doorDirection);
            BlockPos pos = room.position;
            Level level = player.level();
            List<RoomStructure> possibleRooms = ModRoomStructures.getCompatibleStructures(type);
            if (possibleRooms.isEmpty()) {
                throw new IOException(String.format("No compatible room structure files found for %s", type.registryName));
            }
            RoomStructure structureToGenerate = possibleRooms.get(Utils.randomWithRange(0, possibleRooms.size()-1));
            String floorFolder = structureToGenerate.floor == null ? "all" : structureToGenerate.floor.name;
            Resource resource = level.getServer().getResourceManager().getResource(new ResourceLocation(KingdomKeys.MODID, "structures/castle_oblivion/rooms/" + floorFolder + "/" + structureToGenerate.path + ".nbt")).get();
            CompoundTag main = NbtIo.readCompressed(resource.open());

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
                        be.load(block.getCompound("nbt"));

                        RoomUtils.Direction facing = RoomUtils.Direction.NORTH;
                        BlockState cardDoorState = ModBlocks.cardDoor.get().defaultBlockState().setValue(CardDoorBlock.GENERATED, true).setValue(CardDoorBlock.TYPE, false);
                        if (be.getMetaData().contains("north")) {
                            if (data.getDoor(RoomUtils.Direction.NORTH) != null && data.getDoor(RoomUtils.Direction.NORTH).open) {
                                cardDoorState = cardDoorState.setValue(CardDoorBlock.OPEN, true);
                            }
                            cardDoorState = cardDoorState.setValue(CardDoorBlock.FACING, Direction.NORTH.getOpposite());
                            room.doorPositions.put(RoomUtils.Direction.NORTH, blockpos.immutable());
                            facing = RoomUtils.Direction.NORTH;
                        } else if (be.getMetaData().contains("west")) {
                            if (data.getDoor(RoomUtils.Direction.WEST) != null && data.getDoor(RoomUtils.Direction.WEST).open) {
                                cardDoorState = cardDoorState.setValue(CardDoorBlock.OPEN, true);
                            }
                            cardDoorState = cardDoorState.setValue(CardDoorBlock.FACING, Direction.WEST.getOpposite());
                            room.doorPositions.put(RoomUtils.Direction.WEST, blockpos.immutable());
                            facing = RoomUtils.Direction.WEST;
                        } else if (be.getMetaData().contains("east")) {
                            if (data.getDoor(RoomUtils.Direction.EAST) != null && data.getDoor(RoomUtils.Direction.EAST).open) {
                                cardDoorState = cardDoorState.setValue(CardDoorBlock.OPEN, true);
                            }
                            cardDoorState = cardDoorState.setValue(CardDoorBlock.FACING, Direction.EAST.getOpposite());
                            room.doorPositions.put(RoomUtils.Direction.EAST, blockpos.immutable());
                            facing = RoomUtils.Direction.EAST;
                        } else if (be.getMetaData().contains("south")) {
                            if (data.getDoor(RoomUtils.Direction.SOUTH) != null && data.getDoor(RoomUtils.Direction.SOUTH).open) {
                                cardDoorState = cardDoorState.setValue(CardDoorBlock.OPEN, true);
                            }
                            cardDoorState = cardDoorState.setValue(CardDoorBlock.FACING, Direction.SOUTH.getOpposite());
                            room.doorPositions.put(RoomUtils.Direction.SOUTH, blockpos.immutable());
                            facing = RoomUtils.Direction.SOUTH;
                        }
                        Pair<RoomData, RoomUtils.Direction> adjacentRoom = ModCapabilities.getCastleOblivionInterior(player.level()).getFloorByID(currentRoom.parentFloor).getAdjacentRoom(data, facing.opposite());
                        if (adjacentRoom != null) {
                            if (adjacentRoom.getFirst().doors.get(adjacentRoom.getSecond().opposite()) != null){
                                level.setBlock(blockpos, cardDoorState, 2);
                                CardDoorTileEntity cardDoorTileEntity = new CardDoorTileEntity(blockpos, cardDoorState);
                                cardDoorTileEntity.setParent(data);
                                cardDoorTileEntity.setDirection(facing);
                                cardDoorTileEntity.setDestinationRoom(adjacentRoom.getFirst());
                                level.setBlockEntity(cardDoorTileEntity);
                            }
                        }
                    }
                } else {
                    level.setBlock(blockpos, state, 2);
                }
            }
            data.setGenerated(room);
            SCSyncCastleOblivionInteriorCapability.syncClients(level);
            KingdomKeys.LOGGER.info("Generated room:{} at {}", type.registryName.toString(), pos);
            MinecraftForge.EVENT_BUS.post(new CastleOblivionEvent.RoomGeneratedEvent(player, data, currentRoom));
            return room;
        } catch (IOException e){
            e.printStackTrace();
            return null;
        }
    }

}
