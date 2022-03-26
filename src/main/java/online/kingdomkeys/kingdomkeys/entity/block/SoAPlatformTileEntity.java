package online.kingdomkeys.kingdomkeys.entity.block;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import online.kingdomkeys.kingdomkeys.entity.ModEntities;

public class SoAPlatformTileEntity extends BlockEntity {

    private boolean multiblockFormed = false;

    public List<BlockPos> structureBlockPosCache = new ArrayList<>();

    public void setMultiblockFormed(boolean formed) {
        this.multiblockFormed = formed;
        setChanged();
    }

    public boolean isMultiblockFormed() {
        return multiblockFormed;
    }

    public SoAPlatformTileEntity(BlockPos pos, BlockState state) {
        super(ModEntities.TYPE_SOA_PLATFORM.get(), pos, state);
    }

    public void clearPositions() {
        structureBlockPosCache.clear();
        setChanged();
    }

    public void addPos(BlockPos pos) {
        structureBlockPosCache.add(pos);
        setChanged();
    }

    @Override
    public void load(CompoundTag compound) {
        super.load(compound);
        multiblockFormed = compound.getBoolean("formed");
        CompoundTag structureCompound = compound.getCompound("structure");
        int size = structureCompound.getInt("size");
        structureBlockPosCache.clear();
        for (int i = 0; i < size; i++) {
            structureBlockPosCache.add(NbtUtils.readBlockPos(structureCompound.getCompound("pos"+i)));
        }
    }

    @Override
    protected void saveAdditional(CompoundTag compound) {
        super.saveAdditional(compound);
        compound.putBoolean("formed", multiblockFormed);
        CompoundTag structureCompound = new CompoundTag();
        structureCompound.putInt("size", structureBlockPosCache.size());
        for (int i = 0; i < structureBlockPosCache.size(); i++) {
            structureCompound.put("pos" + i, NbtUtils.writeBlockPos(structureBlockPosCache.get(i)));
        }
        compound.put("structure", structureCompound);
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

    @Override
    public AABB getRenderBoundingBox() {
        return INFINITE_EXTENT_AABB;
    }
}
