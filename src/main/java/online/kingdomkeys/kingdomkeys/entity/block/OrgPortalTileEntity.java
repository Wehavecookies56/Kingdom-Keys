package online.kingdomkeys.kingdomkeys.entity.block;

import java.util.UUID;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import online.kingdomkeys.kingdomkeys.entity.ModEntities;

public class OrgPortalTileEntity extends TileEntity {
	UUID ownerName;

	public OrgPortalTileEntity() {
		super(ModEntities.TYPE_ORG_PORTAL_TE.get());
	}

	@Override
	public CompoundNBT write(CompoundNBT parentNBTTagCompound) {
		super.write(parentNBTTagCompound);
		if (ownerName != null)
			parentNBTTagCompound.putUniqueId("owner", ownerName);
		return parentNBTTagCompound;
	}

	@Override
	public void read(CompoundNBT parentNBTTagCompound) {
		super.read(parentNBTTagCompound);
		ownerName = parentNBTTagCompound.getUniqueId("owner");
	}

	public UUID getOwner() {
		return ownerName;
	}

	public void setOwner(PlayerEntity player) {
		this.ownerName = player.getUniqueID();
	}
	
}