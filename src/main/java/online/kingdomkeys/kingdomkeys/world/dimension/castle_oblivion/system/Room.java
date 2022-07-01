package online.kingdomkeys.kingdomkeys.world.dimension.castle_oblivion.system;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.StructureBlock;
import net.minecraft.world.level.block.entity.StructureBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.StructureMode;
import net.minecraftforge.common.util.INBTSerializable;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.block.CardDoorBlock;
import online.kingdomkeys.kingdomkeys.block.ModBlocks;
import online.kingdomkeys.kingdomkeys.capability.ModCapabilities;
import org.apache.commons.lang3.tuple.Pair;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

public class Room implements INBTSerializable<CompoundTag> {
    RoomType type;
    public BlockPos position;
    int mobsRemaining;
    public Map<RoomUtils.Direction, BlockPos> doorPositions;
    UUID parentFloor;

    //If the structure has been generated in the world
    boolean generated;

    public Room(UUID parentFloor) {
        this.parentFloor = parentFloor;
        this.doorPositions = new HashMap<>();
    }

    //Clear room if needed, set type and position
    public void createRoomFromCard(RoomType type, Level level, Room currentRoom, RoomUtils.Direction doorDirection) {
        this.type = type;
        if (generated) {
            clearRoom(level);
        }
        Direction dir = doorDirection.toMCDirection();
        position = currentRoom.position.relative(dir, 256);
    }

    //Used for the first lobby that generates when the dimension is created
    public static void createDefaultLobby(Room room) {
        room.type = ModRoomTypes.LOBBY.get();
        room.position = new BlockPos(0, 59, 0);
        room.doorPositions.put(RoomUtils.Direction.SOUTH, new BlockPos(16, 60, 1));
        room.doorPositions.put(RoomUtils.Direction.NORTH, new BlockPos(16, 63, 67));
    }

    public RoomType getType() {
        return type;
    }

    public Floor getParent(Level level) {
        return ModCapabilities.getCastleOblivionInterior(level).getFloorByID(parentFloor);
    }

    public void tick() {
        type.getProperties().getModifiers().forEach(RoomModifier::tick);
    }

    public boolean inRoom(BlockPos pos) {
        return pos.getX() >= position.getX() && pos.getX() <= position.getX() + type.getProperties().getDimensions().width && pos.getZ() >= position.getZ() && pos.getZ() <= position.getZ() + type.getProperties().getDimensions().height;
    }

    public boolean clearRoom(Level level) {
        Floor parent = getParent(level);
        if (parent != null) {
            if (!parent.shouldRoomTick(this)) {
                BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos(position.getX(), position.getY(), position.getZ());
                for (int z = 0; z < type.getProperties().getDimensions().height; z++) {
                    for (int y = 0; y < 128; y++) {
                        for (int x = 0; x < type.getProperties().getDimensions().width; x++) {
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

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag tag = new CompoundTag();
        tag.putUUID("parent", parentFloor);
        tag.putString("type", type.registryName.toString());
        tag.put("position", NbtUtils.writeBlockPos(position));
        tag.putInt("mobs", mobsRemaining);
        tag.putBoolean("generated", generated);
        tag.putInt("door_positions_size", doorPositions.size());
        CompoundTag doorPosTag = new CompoundTag();
        int i = 0;
        for (Map.Entry<RoomUtils.Direction, BlockPos> doorPos : doorPositions.entrySet()) {
            doorPosTag.putInt("direction_" + i, doorPos.getKey().ordinal());
            doorPosTag.put("position_" + i, NbtUtils.writeBlockPos(doorPos.getValue()));
            i++;
        }
        tag.put("door_positions", doorPosTag);
        return tag;
    }

    @Override
    public void deserializeNBT(CompoundTag tag) {
        parentFloor = tag.getUUID("parent");
        type = ModRoomTypes.registry.get().getValue(new ResourceLocation(tag.getString("type")));
        position = NbtUtils.readBlockPos(tag.getCompound("position"));
        mobsRemaining = tag.getInt("mobs");
        generated = tag.getBoolean("generated");
        int doorPosSize = tag.getInt("door_positions_size");
        CompoundTag doorPosTag = tag.getCompound("door_positions");
        for (int i = 0; i < doorPosSize; i++) {
            doorPositions.put(RoomUtils.Direction.values()[doorPosTag.getInt("direction_" + i)], NbtUtils.readBlockPos(doorPosTag.getCompound("position_" + i)));
        }
    }

    public static Room deserialize(CompoundTag tag) {
        Room room = new Room(null);
        room.deserializeNBT(tag);
        return room;
    }
}
