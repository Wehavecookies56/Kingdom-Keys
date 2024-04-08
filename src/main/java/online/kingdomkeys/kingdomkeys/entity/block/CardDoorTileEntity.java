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
import online.kingdomkeys.kingdomkeys.world.dimension.castle_oblivion.system.RoomData;
import online.kingdomkeys.kingdomkeys.world.dimension.castle_oblivion.system.RoomUtils;

public class CardDoorTileEntity extends BlockEntity {

    public CardDoorTileEntity(BlockPos pWorldPosition, BlockState pBlockState) {
        super(ModEntities.TYPE_CARD_DOOR.get(), pWorldPosition, pBlockState);
    }

    boolean open = false;
    BlockPos destination;
    RoomData parent;
    RoomData destinationRoom;
    RoomUtils.Direction direction;
    public void openDoor(RoomUtils.Direction directionFrom) {
        open = true;
        if (directionFrom != null) {
            parent.getDoor(directionFrom.opposite()).open();
        }
        level.setBlock(this.getBlockPos(), getBlockState().setValue(CardDoorBlock.OPEN, true), 2);
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

    public void setDirection(RoomUtils.Direction direction) {
        this.direction = direction;
    }

    public RoomUtils.Direction getDirection() {
        return direction;
    }

    @Override
    public void load(CompoundTag pTag) {
        super.load(pTag);
        if (pTag.contains("parent")) {
            parent = RoomData.deserialize(pTag.getCompound("parent"));
        }
        direction = RoomUtils.Direction.values()[pTag.getInt("direction")];
        if (pTag.contains("destination_room")) {
            destinationRoom = RoomData.deserialize(pTag.getCompound("destination_room"));
        }
        open = pTag.getBoolean("open");
        if (open && pTag.getCompound("destination") != null) {
            destination = NbtUtils.readBlockPos(pTag.getCompound("destination"));
        } else {
            destination = null;
        }
    }

    @Override
    protected void saveAdditional(CompoundTag pTag) {
        super.saveAdditional(pTag);
        if (parent != null) {
            pTag.put("parent", parent.serializeNBT());
            pTag.putInt("direction", direction.ordinal());
            pTag.put("destination_room", destinationRoom.serializeNBT());
        }
        pTag.putBoolean("open", open);
        if (open && destination != null) {
            pTag.put("destination", NbtUtils.writeBlockPos(destination));
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