package online.kingdomkeys.kingdomkeys.container;

import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IWorldPosCallable;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.SlotItemHandler;
import online.kingdomkeys.kingdomkeys.block.ModBlocks;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.wrapper.PlayerInvWrapper;
import online.kingdomkeys.kingdomkeys.entity.block.PedestalTileEntity;
import online.kingdomkeys.kingdomkeys.item.KeybladeItem;

import javax.annotation.Nonnull;
import java.util.Objects;

public class PedestalContainer extends Container {

	public final PedestalTileEntity TE;
	private final IWorldPosCallable canInteractWith;

	private static final int HOTBAR_SLOT_COUNT = 9;
	private static final int PLAYER_INVENTORY_ROW_COUNT = 3;
	private static final int PLAYER_INVENTORY_COLUMN_COUNT = 9;
	private static final int PLAYER_INVENTORY_SLOT_COUNT = PLAYER_INVENTORY_COLUMN_COUNT * PLAYER_INVENTORY_ROW_COUNT;
	private static final int VANILLA_SLOT_COUNT = HOTBAR_SLOT_COUNT + PLAYER_INVENTORY_SLOT_COUNT;

	private static final int VANILLA_FIRST_SLOT_INDEX = 0;
	private static final int TE_INVENTORY_FIRST_SLOT_INDEX = VANILLA_FIRST_SLOT_INDEX + VANILLA_SLOT_COUNT;
	private static final int PEDESTAL_SLOTS = PedestalTileEntity.NUMBER_OF_SLOTS; // must match TileEntityInventoryBasic.NUMBER_OF_SLOTS

	public static final int TILE_INVENTORY_YPOS = 20; // the ContainerScreenBasic needs to know these so it can tell where to draw the
														// Titles
	public static final int PLAYER_INVENTORY_YPOS = 51;

	public PedestalContainer(final int windowID, final PlayerInventory playerInventory, final PedestalTileEntity tileEntity) {
		super(ModContainers.PEDESTAL.get(), windowID);
		TE = tileEntity;
		canInteractWith = IWorldPosCallable.of(TE.getWorld(), TE.getPos());

		int i,j;
		//Pedestal slot
		TE.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent(iih -> {
			addSlot(new SlotItemHandler(iih, 0, 152, 9) {
				@Override
				public boolean isItemValid(ItemStack stack) {
					return true; //stack.getItem() instanceof KeybladeItem;
				}
			});
		});


		//Player inventory
		for (i = 0; i < 3; ++i) {
			for (j = 0; j < 9; ++j) {
				addSlot(new Slot(playerInventory, j + i * 9 + 9, 8 + j * 18, 105 + i * 18));
			}
		}

		//Hotbar
		for (i = 0; i < 9; ++i) {
			addSlot(new Slot(playerInventory, i, 8 + i * 18, 163));
		}
	}

	private static PedestalTileEntity getTileEntity(final PlayerInventory playerInventory, final PacketBuffer buf) {
		final TileEntity te = playerInventory.player.world.getTileEntity(buf.readBlockPos());
		if (te instanceof PedestalTileEntity) {
			return (PedestalTileEntity) te;
		}
		throw new IllegalStateException("Tile Entity mismatch with container");
	}


	public PedestalContainer(final int windowId, final PlayerInventory playerInventory, final PacketBuffer buf) {
		this(windowId, playerInventory, getTileEntity(playerInventory, buf));
	}

	@Override
	public boolean canInteractWith(PlayerEntity playerIn) {
		return isWithinUsableDistance(canInteractWith, playerIn, ModBlocks.pedestal.get());
	}

	@Override
	public ItemStack transferStackInSlot(PlayerEntity playerIn, int index) {
		ItemStack itemstack = ItemStack.EMPTY;
		Slot slot = this.inventorySlots.get(index);
		if (slot != null && slot.getHasStack()) {
			ItemStack itemstack1 = slot.getStack();
			itemstack = itemstack1.copy();
			if (index < PEDESTAL_SLOTS) {
				if (!this.mergeItemStack(itemstack1, PEDESTAL_SLOTS, this.inventorySlots.size(), true)) {
					return ItemStack.EMPTY;
				}
			} else if (!this.mergeItemStack(itemstack1, 0, PEDESTAL_SLOTS, false)) {
				return ItemStack.EMPTY;
			}

			if (itemstack1.isEmpty()) {
				slot.putStack(ItemStack.EMPTY);
			} else {
				slot.onSlotChanged();
			}
		}
		return itemstack;
	}
}
