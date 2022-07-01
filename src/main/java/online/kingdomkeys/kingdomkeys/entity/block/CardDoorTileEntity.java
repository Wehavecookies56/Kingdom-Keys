package online.kingdomkeys.kingdomkeys.entity.block;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import online.kingdomkeys.kingdomkeys.entity.ModEntities;
import online.kingdomkeys.kingdomkeys.world.dimension.castle_oblivion.system.Room;

import javax.annotation.Nullable;

public class CardDoorTileEntity extends BlockEntity {

    public CardDoorTileEntity(BlockPos pWorldPosition, BlockState pBlockState) {
        super(ModEntities.TYPE_CARD_DOOR.get(), pWorldPosition, pBlockState);
    }

    //public Room getDestination() {
    //    return destination;
    //}

    @Override
    public void load(CompoundTag pTag) {
        super.load(pTag);

    }

    @Override
    protected void saveAdditional(CompoundTag pTag) {
        super.saveAdditional(pTag);
        //if (destination != null) {
        //    pTag.put("destination", destination.serializeNBT());
        //}
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
