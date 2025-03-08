package online.kingdomkeys.kingdomkeys.entity.block;

import javax.annotation.Nullable;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import online.kingdomkeys.kingdomkeys.block.CardDoorBlock;
import online.kingdomkeys.kingdomkeys.entity.ModEntities;
import online.kingdomkeys.kingdomkeys.world.dimension.castle_oblivion.system.floor.Floor;
import online.kingdomkeys.kingdomkeys.world.dimension.castle_oblivion.system.room.RoomDirection;
import online.kingdomkeys.kingdomkeys.world.dimension.castle_oblivion.system.room.DoorData;
import online.kingdomkeys.kingdomkeys.world.dimension.castle_oblivion.system.room.RoomData;

public class CardDoorTileEntity extends BlockEntity {

    public CardDoorTileEntity(BlockPos pWorldPosition, BlockState pBlockState) {
        super(ModEntities.TYPE_CARD_DOOR.get(), pWorldPosition, pBlockState);
    }

    boolean open = false;
    BlockPos destination;
    RoomData parent;
    RoomData destinationRoom;
    RoomDirection direction;
    DoorData data;


    public void openDoor(boolean setBlock) {
        open = true;
        if (setBlock) {
            level.setBlock(this.getBlockPos(), getBlockState().setValue(CardDoorBlock.OPEN, true), 2);
        }
    }

    public boolean isOpen() {
        return open;
    }

    public void setParent(RoomData room) {
        parent = room;
    }

    public RoomData getParentRoom() {
        return parent;
    }

    public RoomData getDestinationRoom() {
        return destinationRoom;
    }

    public void setDestinationRoom(RoomData destinationRoom) {
        this.destinationRoom = destinationRoom;
    }

    public void setDirection(RoomDirection direction) {
        this.direction = direction;
    }

    public RoomDirection getDirection() {
        return direction;
    }

    public DoorData getData() {
        return data;
    }

    public void setData(DoorData data) {
        this.data = data;
    }

    @Override
    public void load(CompoundTag pTag) {
        super.load(pTag);
        if (pTag.contains("parent")) {
            parent = new RoomData(pTag.getCompound("parent"));
            direction = RoomDirection.values()[pTag.getInt("direction")];
        }
        if (pTag.contains("destination_room")) {
            destinationRoom = new RoomData(pTag.getCompound("destination_room"));
        }
        open = pTag.getBoolean("open");
        if (open && pTag.contains("destination")) {
            destination = NbtUtils.readBlockPos(pTag.getCompound("destination"));
        } else {
            destination = null;
        }
        if (pTag.contains("door_data")) {
            data = new DoorData(pTag.getCompound("door_data"));
        }
    }

    @Override
    protected void saveAdditional(CompoundTag pTag) {
        super.saveAdditional(pTag);
        if (parent != null) {
            pTag.put("parent", parent.serializeNBT());
            pTag.putInt("direction", direction.ordinal());
        }
        if (destinationRoom != null) {
            pTag.put("destination_room", destinationRoom.serializeNBT());
        }
        pTag.putBoolean("open", open);
        if (open && destination != null) {
            pTag.put("destination", NbtUtils.writeBlockPos(destination));
        }
        if (data != null) {
            pTag.put("door_data", data.serializeNBT());
        }
    }

    @Nullable
    @Override
    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt) {
        load(pkt.getTag());
    }

    @Override
    public CompoundTag getUpdateTag() {
        return serializeNBT();
    }

    @Override
    public void handleUpdateTag(CompoundTag tag) {
        this.load(tag);
    }

}