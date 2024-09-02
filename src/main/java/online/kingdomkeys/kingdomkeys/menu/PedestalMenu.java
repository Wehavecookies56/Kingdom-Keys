package online.kingdomkeys.kingdomkeys.menu;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.SlotItemHandler;
import online.kingdomkeys.kingdomkeys.block.ModBlocks;
import online.kingdomkeys.kingdomkeys.entity.block.PedestalTileEntity;

public class PedestalMenu extends AbstractContainerMenu {

	public final PedestalTileEntity TE;
	private final ContainerLevelAccess canInteractWith;

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

	public PedestalMenu(final int windowID, final Inventory playerInventory, final PedestalTileEntity tileEntity) {
		super(ModMenus.PEDESTAL.get(), windowID);
		TE = tileEntity;
		canInteractWith = ContainerLevelAccess.create(TE.getLevel(), TE.getBlockPos());

		int i,j;
		//Pedestal slot
		IItemHandler iih = TE.inventory.get();
		addSlot(new SlotItemHandler(iih, 0, 152, 9) {
			@Override
			public boolean mayPlace(ItemStack stack) {
				return true; //stack.getItem() instanceof KeybladeItem;
			}
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

	private static PedestalTileEntity getTileEntity(final Inventory playerInventory, final FriendlyByteBuf buf) {
		final BlockEntity te = playerInventory.player.level().getBlockEntity(buf.readBlockPos());
		if (te instanceof PedestalTileEntity) {
			return (PedestalTileEntity) te;
		}
		throw new IllegalStateException("Tile Entity mismatch with container");
	}


	public PedestalMenu(final int windowId, final Inventory playerInventory, final FriendlyByteBuf buf) {
		this(windowId, playerInventory, getTileEntity(playerInventory, buf));
	}

	@Override
	public boolean stillValid(Player playerIn) {
		return stillValid(canInteractWith, playerIn, ModBlocks.pedestal.get());
	}

	@Override
	public ItemStack quickMoveStack(Player playerIn, int index) {
		ItemStack itemstack = ItemStack.EMPTY;
		Slot slot = this.slots.get(index);
		if (slot != null && slot.hasItem()) {
			ItemStack itemstack1 = slot.getItem();
			itemstack = itemstack1.copy();
			if (index < PEDESTAL_SLOTS) {
				if (!this.moveItemStackTo(itemstack1, PEDESTAL_SLOTS, this.slots.size(), true)) {
					return ItemStack.EMPTY;
				}
			} else if (!this.moveItemStackTo(itemstack1, 0, PEDESTAL_SLOTS, false)) {
				return ItemStack.EMPTY;
			}

			if (itemstack1.isEmpty()) {
				slot.set(ItemStack.EMPTY);
			} else {
				slot.setChanged();
			}
		}
		return itemstack;
	}
}
