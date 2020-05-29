package online.kingdomkeys.kingdomkeys.entity.block;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import online.kingdomkeys.kingdomkeys.entity.ModEntities;

public class KKChestTileEntity extends TileEntity {
	private String keybladeName;

	public KKChestTileEntity() {
		super(ModEntities.TYPE_KKCHEST.get());
	}

	// set by the block upon creation
	public void setKeyblade(ItemStack keyblade) {
		keybladeName = keyblade.getItem().toString();
	}
	
	public String getKeybladeName() {
		return keybladeName;
	}

	/*@Override
	@Nullable
	public SUpdateTileEntityPacket getUpdatePacket() {
		CompoundNBT nbtTagCompound = new CompoundNBT();
		write(nbtTagCompound);
		int tileEntityType = 42; // arbitrary number; only used for vanilla TileEntities. You can use it, or not, as you want.
		return new SUpdateTileEntityPacket(this.pos, tileEntityType, nbtTagCompound);
	}

	@Override
	public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket pkt) {
		read(pkt.getNbtCompound());
	}

	/*
	 * Creates a tag containing all of the TileEntity information, used by vanilla
	 * to transmit from server to client
	 */
	/*@Override
	public CompoundNBT getUpdateTag() {
		CompoundNBT nbtTagCompound = new CompoundNBT();
		write(nbtTagCompound);
		return nbtTagCompound;
	}*/

	/*
	 * Populates this TileEntity with information from the tag, used by vanilla to
	 * transmit from server to client
	 */
	/*@Override
	public void handleUpdateTag(CompoundNBT tag) {
		this.read(tag);
	}*/

	public CompoundNBT write(CompoundNBT parentNBTTagCompound) {
        super.write(parentNBTTagCompound);
        if (keybladeName != null)
            parentNBTTagCompound.putString("keyblade", keybladeName);
        return parentNBTTagCompound;
    }

    @Override
    public void read(CompoundNBT parentNBTTagCompound) {
        super.read(parentNBTTagCompound);
        keybladeName = parentNBTTagCompound.getString("keyblade");
    }
    
}