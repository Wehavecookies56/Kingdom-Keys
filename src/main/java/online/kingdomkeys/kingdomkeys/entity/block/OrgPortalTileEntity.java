package online.kingdomkeys.kingdomkeys.entity.block;

import java.util.UUID;

import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
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
	public void read(BlockState state, CompoundNBT nbt) {
		super.read(state, nbt);
		uuid = nbt.getUniqueId("uuid");
	}

	public UUID getUUID() {
		return uuid;
	}

	public void setUUID(UUID uuid) {
		this.uuid = uuid;
	}

	

	
	
}