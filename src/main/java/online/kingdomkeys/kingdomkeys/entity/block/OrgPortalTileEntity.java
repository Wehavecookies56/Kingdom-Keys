package online.kingdomkeys.kingdomkeys.entity.block;

import java.util.UUID;

import javax.annotation.Nullable;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.TileEntity;
import online.kingdomkeys.kingdomkeys.entity.ModEntities;

public class OrgPortalTileEntity extends TileEntity {
	UUID uuid;

	public OrgPortalTileEntity() {
		super(ModEntities.TYPE_ORG_PORTAL_TE.get());
	}

	@Override
	public CompoundNBT write(CompoundNBT parentNBTTagCompound) {
		super.write(parentNBTTagCompound);
		if (uuid != null)
			parentNBTTagCompound.putUniqueId("uuid", uuid);
		return parentNBTTagCompound;
	}

	@Override
	public void read(CompoundNBT parentNBTTagCompound) {
		super.read(parentNBTTagCompound);
		if(parentNBTTagCompound.hasUniqueId("uuid"))
			uuid = parentNBTTagCompound.getUniqueId("uuid");
	}

	public UUID getUUID() {
		return uuid;
	}

	public void setUUID(UUID uuid) {
		this.uuid = uuid;
		markDirty();
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
}