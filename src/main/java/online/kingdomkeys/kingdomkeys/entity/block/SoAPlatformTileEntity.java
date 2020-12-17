package online.kingdomkeys.kingdomkeys.entity.block;

import net.minecraft.block.Block;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import online.kingdomkeys.kingdomkeys.entity.ModEntities;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

public class SoAPlatformTileEntity extends TileEntity {

    private boolean multiblockFormed = false;

    public List<BlockPos> structureBlockPosCache = new ArrayList<>();

    public void setMultiblockFormed(boolean formed) {
        this.multiblockFormed = formed;
        markDirty();
    }

    public boolean isMultiblockFormed() {
        return multiblockFormed;
    }

    public SoAPlatformTileEntity() {
        super(ModEntities.TYPE_SOA_PLATFORM.get());
    }

    public void clearPositions() {
        structureBlockPosCache.clear();
        markDirty();
    }

    public void addPos(BlockPos pos) {
        structureBlockPosCache.add(pos);
        markDirty();
    }

    @Override
    public void read(CompoundNBT compound) {
        super.read(compound);
        multiblockFormed = compound.getBoolean("formed");
        CompoundNBT structureCompound = compound.getCompound("structure");
        int size = structureCompound.getInt("size");
        structureBlockPosCache.clear();
        for (int i = 0; i < size; i++) {
            structureBlockPosCache.add(NBTUtil.readBlockPos(structureCompound.getCompound("pos"+i)));
        }
    }

    @Override
    public CompoundNBT write(CompoundNBT compound) {
        compound.putBoolean("formed", multiblockFormed);
        CompoundNBT structureCompound = new CompoundNBT();
        structureCompound.putInt("size", structureBlockPosCache.size());
        for (int i = 0; i < structureBlockPosCache.size(); i++) {
            structureCompound.put("pos" + i, NBTUtil.writeBlockPos(structureBlockPosCache.get(i)));
        }
        compound.put("structure", structureCompound);
        return super.write(compound);
    }

    @Nullable
    @Override
    public SUpdateTileEntityPacket getUpdatePacket() {
        CompoundNBT nbt = new CompoundNBT();
        this.write(nbt);
        return new SUpdateTileEntityPacket(this.getPos(), 1, nbt);
    }

    @Override
    public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket pkt) {
        this.read(pkt.getNbtCompound());
    }

    @Override
    public CompoundNBT getUpdateTag() {
        return this.write(new CompoundNBT());
    }

    @Override
    public void handleUpdateTag(CompoundNBT tag) {
        this.read(tag);
    }


    @Override
    public AxisAlignedBB getRenderBoundingBox() {
        return INFINITE_EXTENT_AABB;
    }
}
