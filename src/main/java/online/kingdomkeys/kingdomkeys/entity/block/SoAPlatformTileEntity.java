package online.kingdomkeys.kingdomkeys.entity.block;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import online.kingdomkeys.kingdomkeys.entity.ModEntities;
import org.jetbrains.annotations.Nullable;

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
    public void loadAdditional(CompoundTag compound, HolderLookup.Provider registries) {
        super.loadAdditional(compound, registries);
        multiblockFormed = compound.getBoolean("formed");
        CompoundTag structureCompound = compound.getCompound("structure");
        int size = structureCompound.getInt("size");
        structureBlockPosCache.clear();
        for (int i = 0; i < size; i++) {
            structureBlockPosCache.add(NbtUtils.readBlockPos(structureCompound.getCompound("pos"+i), "pos"+i).get());
        }
    }

    @Override
    protected void saveAdditional(CompoundTag compound, HolderLookup.Provider registries) {
        super.saveAdditional(compound, registries);
        compound.putBoolean("formed", multiblockFormed);
        CompoundTag structureCompound = new CompoundTag();
        structureCompound.putInt("size", structureBlockPosCache.size());
        for (int i = 0; i < structureBlockPosCache.size(); i++) {
            structureCompound.put("pos" + i, NbtUtils.writeBlockPos(structureBlockPosCache.get(i)));
        }
        compound.put("structure", structureCompound);
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider pRegistries) {
        CompoundTag tag = new CompoundTag();
        saveAdditional(tag, pRegistries);
        return tag;
    }

    @Override
    public void handleUpdateTag(CompoundTag tag, HolderLookup.Provider lookupProvider) {
        loadAdditional(tag, lookupProvider);
    }

    @Nullable
    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt, HolderLookup.Provider lookupProvider) {
        super.onDataPacket(net, pkt, lookupProvider);
    }
}
