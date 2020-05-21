package online.kingdomkeys.kingdomkeys.entity.block;

import java.util.Arrays;
import java.util.Random;

import javax.annotation.Nullable;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.SaplingBlock;
import net.minecraft.block.TNTBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.DoubleNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import online.kingdomkeys.kingdomkeys.entity.ModEntities;

public class KKChestTileEntity extends TileEntity {
	private ItemStack keybladeLocked = ItemStack.EMPTY;

	public KKChestTileEntity() {
		super(ModEntities.TYPE_KKCHEST.get());
	}

	// set by the block upon creation
	public void setKeyblade(ItemStack keyblade) {
		keybladeLocked = keyblade;
	}
	
	public ItemStack getKeyblade() {
		return keybladeLocked;
	}

	@Override
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
	@Override
	public CompoundNBT getUpdateTag() {
		CompoundNBT nbtTagCompound = new CompoundNBT();
		write(nbtTagCompound);
		return nbtTagCompound;
	}

	/*
	 * Populates this TileEntity with information from the tag, used by vanilla to
	 * transmit from server to client
	 */
	@Override
	public void handleUpdateTag(CompoundNBT tag) {
		this.read(tag);
	}

	@Override
	public CompoundNBT write(CompoundNBT parentNBTTagCompound) {
		super.write(parentNBTTagCompound); // The super call is required to save the tile's location
		// ItemStack
		CompoundNBT itemStackNBT = new CompoundNBT();
		keybladeLocked.write(itemStackNBT); // make sure testItemStack is not null first!
		parentNBTTagCompound.put("keyblade", itemStackNBT);

		return parentNBTTagCompound;
	}

	// This is where you load the data that you saved in writeToNBT
	@Override
	public void read(CompoundNBT parentNBTTagCompound) {
		super.read(parentNBTTagCompound); // The super call is required to load the tiles location

		// ItemStack
		CompoundNBT itemStackNBT = parentNBTTagCompound.getCompound("keyblade");
		ItemStack readItemStack = ItemStack.read(itemStackNBT);
		if (!ItemStack.areItemStacksEqual(keybladeLocked, readItemStack)) {
			System.err.println("keyblade mismatch:" + readItemStack);
		}

	}
}