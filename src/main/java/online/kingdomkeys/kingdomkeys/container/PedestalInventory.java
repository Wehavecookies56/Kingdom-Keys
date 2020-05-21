package online.kingdomkeys.kingdomkeys.container;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.items.ItemStackHandler;

import java.util.Set;
import java.util.function.Predicate;

public class PedestalInventory implements IInventory {

	public static PedestalInventory createForTileEntity(int size, Predicate<PlayerEntity> canPlayerAccessInventoryLambda, Notify markDirtyNotificationLambda) {
		return new PedestalInventory(size, canPlayerAccessInventoryLambda, markDirtyNotificationLambda);
	}

	public static PedestalInventory createForClientSideContainer(int size) {
		return new PedestalInventory(size);
	}

	// ----Methods used to load / save the contents to NBT

	public CompoundNBT serializeNBT() {
		return chestContents.serializeNBT();
	}

	public void deserializeNBT(CompoundNBT nbt) {
		chestContents.deserializeNBT(nbt);
	}

	/**
	 * sets the function that the container should call in order to decide if the
	 * given player can access the container's contents not. The lambda function is
	 * only used on the server side
	 */
	public void setCanPlayerAccessInventoryLambda(Predicate<PlayerEntity> canPlayerAccessInventoryLambda) {
		this.canPlayerAccessInventoryLambda = canPlayerAccessInventoryLambda;
	}

	// the function that the container should call in order to tell the parent
	// TileEntity that the
	// contents of its inventory have been changed.
	// default is "do nothing"
	public void setMarkDirtyNotificationLambda(Notify markDirtyNotificationLambda) {
		this.markDirtyNotificationLambda = markDirtyNotificationLambda;
	}

	// the function that the container should call in order to tell the parent
	// TileEntity that the
	// container has been opened by a player (eg so that the chest can animate its
	// lid being opened)
	// default is "do nothing"
	public void setOpenInventoryNotificationLambda(Notify openInventoryNotificationLambda) {
		this.openInventoryNotificationLambda = openInventoryNotificationLambda;
	}

	// the function that the container should call in order to tell the parent
	// TileEntity that the
	// container has been closed by a player
	// default is "do nothing"
	public void setCloseInventoryNotificationLambda(Notify closeInventoryNotificationLambda) {
		this.closeInventoryNotificationLambda = closeInventoryNotificationLambda;
	}

	// ---------- These methods are used by the container to ask whether certain
	// actions are permitted
	// If you need special behaviour (eg a chest can only be used by a particular
	// player) then either modify this method
	// or ask the parent TileEntity.

	@Override
	public boolean isUsableByPlayer(PlayerEntity player) {
		return canPlayerAccessInventoryLambda.test(player); // on the client, this does nothing. on the server, ask our parent TileEntity.
	}

	@Override
	public boolean isItemValidForSlot(int index, ItemStack stack) {
		return chestContents.isItemValid(index, stack);
	}

	// ----- Methods used to inform the parent tile entity that something has
	// happened to the contents
	// you can make direct calls to the parent if you like, I've used lambdas
	// because I think it shows the separation
	// of responsibilities more clearly.

	@FunctionalInterface
	public interface Notify { // Some folks use Runnable, but I prefer not to use it for non-thread-related tasks
		void invoke();
	}

	@Override
	public void markDirty() {
		markDirtyNotificationLambda.invoke();
	}

	@Override
	public void openInventory(PlayerEntity player) {
		openInventoryNotificationLambda.invoke();
	}

	@Override
	public void closeInventory(PlayerEntity player) {
		closeInventoryNotificationLambda.invoke();
	}

	// ---------These following methods are called by Vanilla container methods to
	// manipulate the inventory contents ---

	@Override
	public int getSizeInventory() {
		return chestContents.getSlots();
	}

	@Override
	public boolean isEmpty() {
		for (int i = 0; i < chestContents.getSlots(); ++i) {
			if (!chestContents.getStackInSlot(i).isEmpty())
				return false;
		}
		return true;
	}

	@Override
	public ItemStack getStackInSlot(int index) {
		return chestContents.getStackInSlot(index);
	}

	@Override
	public ItemStack decrStackSize(int index, int count) {
		return chestContents.extractItem(index, count, false);
	}

	@Override
	public ItemStack removeStackFromSlot(int index) {
		int maxPossibleItemStackSize = chestContents.getSlotLimit(index);
		return chestContents.extractItem(index, maxPossibleItemStackSize, false);
	}

	@Override
	public void setInventorySlotContents(int index, ItemStack stack) {
		chestContents.setStackInSlot(index, stack);
	}

	@Override
	public void clear() {
		for (int i = 0; i < chestContents.getSlots(); ++i) {
			chestContents.setStackInSlot(i, ItemStack.EMPTY);
		}
	}

	// ---------

	private PedestalInventory(int size) {
		this.chestContents = new ItemStackHandler(size);
	}

	private PedestalInventory(int size, Predicate<PlayerEntity> canPlayerAccessInventoryLambda, Notify markDirtyNotificationLambda) {
		this.chestContents = new ItemStackHandler(size);
		this.canPlayerAccessInventoryLambda = canPlayerAccessInventoryLambda;
		this.markDirtyNotificationLambda = markDirtyNotificationLambda;
	}

	// the function that the container should call in order to decide if the
	// given player can access the container's Inventory or not. Only valid server
	// side
	// default is "true".
	private Predicate<PlayerEntity> canPlayerAccessInventoryLambda = x -> true;

	// the function that the container should call in order to tell the parent
	// TileEntity that the
	// contents of its inventory have been changed.
	// default is "do nothing"
	private Notify markDirtyNotificationLambda = () -> {
	};

	// the function that the container should call in order to tell the parent
	// TileEntity that the
	// container has been opened by a player (eg so that the chest can animate its
	// lid being opened)
	// default is "do nothing"
	private Notify openInventoryNotificationLambda = () -> {
	};

	// the function that the container should call in order to tell the parent
	// TileEntity that the
	// container has been closed by a player
	// default is "do nothing"
	private Notify closeInventoryNotificationLambda = () -> {
	};

	private final ItemStackHandler chestContents;
}