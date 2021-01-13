package online.kingdomkeys.kingdomkeys.entity.block;

import java.util.UUID;

import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import online.kingdomkeys.kingdomkeys.entity.ModEntities;

public class OrgPortalTileEntity extends TileEntity {
	UUID ownerID;

	public OrgPortalTileEntity() {
		super(ModEntities.TYPE_ORG_PORTAL_TE.get());
	}

	@Override
	public CompoundNBT write(CompoundNBT parentNBTTagCompound) {
		super.write(parentNBTTagCompound);
		if (ownerID != null)
			parentNBTTagCompound.putUniqueId("owner", ownerID);
		return parentNBTTagCompound;
	}

	@Override
	public void read(BlockState state, CompoundNBT nbt) {
		super.read(state, nbt);
		ownerID = nbt.getUniqueId("owner");
	}

	public UUID getOwner() {
		return ownerID;
	}

	public void setOwner(PlayerEntity player) {
		this.ownerID = player.getUniqueID();
	}
	
}