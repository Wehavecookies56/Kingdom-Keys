package online.kingdomkeys.kingdomkeys.entity.block;

import java.util.UUID;

import javax.annotation.Nullable;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import online.kingdomkeys.kingdomkeys.entity.ModEntities;

public class OrgPortalTileEntity extends BlockEntity {
	UUID uuid;

	public OrgPortalTileEntity() {
		super(ModEntities.TYPE_ORG_PORTAL_TE.get());
	}

	@Override
	public CompoundTag save(CompoundTag parentNBTTagCompound) {
		super.save(parentNBTTagCompound);
		if (uuid != null)
			parentNBTTagCompound.putUUID("uuid", uuid);
		return parentNBTTagCompound;
	}

	@Override
	public void load(BlockState state, CompoundTag nbt) {
		super.load(state, nbt);
		if(nbt.hasUUID("uuid"))
			uuid = nbt.getUUID("uuid");
	}

	public UUID getUUID() {
		return uuid;
	}

	public void setUUID(UUID uuid) {
		this.uuid = uuid;
		setChanged();
	}
	
	@Nullable
	@Override
	public ClientboundBlockEntityDataPacket getUpdatePacket() {
		CompoundTag nbt = new CompoundTag();
		this.save(nbt);
		return new ClientboundBlockEntityDataPacket(this.getBlockPos(), 1, nbt);
	}

	@Override
	public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt) {
		this.load(level.getBlockState(pkt.getPos()), pkt.getTag());
	}

	@Override
	public CompoundTag getUpdateTag() {
		return this.save(new CompoundTag());
	}

	@Override
	public void handleUpdateTag(BlockState state, CompoundTag tag) {
		this.load(state, tag);
	}
	
}