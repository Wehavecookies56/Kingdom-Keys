package online.kingdomkeys.kingdomkeys.container;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IWorldPosCallable;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.SlotItemHandler;
import online.kingdomkeys.kingdomkeys.block.ModBlocks;
import online.kingdomkeys.kingdomkeys.entity.block.GummiEditorTileEntity;

public class GummiEditorContainer extends Container {

	public final GummiEditorTileEntity TE;
	private final IWorldPosCallable canInteractWith;

	private static final int HOTBAR_SLOT_COUNT = 9;
	private static final int PLAYER_INVENTORY_ROW_COUNT = 3;
	private static final int PLAYER_INVENTORY_COLUMN_COUNT = 9;
	private static final int PLAYER_INVENTORY_SLOT_COUNT = PLAYER_INVENTORY_COLUMN_COUNT * PLAYER_INVENTORY_ROW_COUNT;
	private static final int VANILLA_SLOT_COUNT = HOTBAR_SLOT_COUNT + PLAYER_INVENTORY_SLOT_COUNT;

	private static final int VANILLA_FIRST_SLOT_INDEX = 0;
	private static final int TE_INVENTORY_FIRST_SLOT_INDEX = VANILLA_FIRST_SLOT_INDEX + VANILLA_SLOT_COUNT;
	private static final int GUMMI_EDITOR_SLOTS = GummiEditorTileEntity.NUMBER_OF_SLOTS; // must match TileEntityInventoryBasic.NUMBER_OF_SLOTS

	public static final int TILE_INVENTORY_YPOS = 20; // the ContainerScreenBasic needs to know these so it can tell where to draw the
														// Titles
	public static final int PLAYER_INVENTORY_YPOS = 51;

	public GummiEditorContainer(final int windowID, final PlayerInventory playerInventory, final GummiEditorTileEntity tileEntity) {
		super(ModContainers.GUMMI_EDITOR.get(), windowID);
		TE = tileEntity;
		canInteractWith = IWorldPosCallable.of(TE.getWorld(), TE.getPos());

		int i,j;
		//Gummi Ship slot
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

	private static GummiEditorTileEntity getTileEntity(final PlayerInventory playerInventory, final PacketBuffer buf) {
		final TileEntity te = playerInventory.player.world.getTileEntity(buf.readBlockPos());
		if (te instanceof GummiEditorTileEntity) {
			return (GummiEditorTileEntity) te;
		}
		throw new IllegalStateException("Tile Entity mismatch with container");
	}


	public GummiEditorContainer(final int windowId, final PlayerInventory playerInventory, final PacketBuffer buf) {
		this(windowId, playerInventory, getTileEntity(playerInventory, buf));
	}

	@Override
	public boolean canInteractWith(PlayerEntity playerIn) {
		return isWithinUsableDistance(canInteractWith, playerIn, ModBlocks.gummiEditor.get());
	}

	@Override
	public ItemStack transferStackInSlot(PlayerEntity playerIn, int index) {
		ItemStack itemstack = ItemStack.EMPTY;
		Slot slot = this.inventorySlots.get(index);
		if (slot != null && slot.getHasStack()) {
			ItemStack itemstack1 = slot.getStack();
			itemstack = itemstack1.copy();
			if (index < GUMMI_EDITOR_SLOTS) {
				if (!this.mergeItemStack(itemstack1, GUMMI_EDITOR_SLOTS, this.inventorySlots.size(), true)) {
					return ItemStack.EMPTY;
				}
			} else if (!this.mergeItemStack(itemstack1, 0, GUMMI_EDITOR_SLOTS, false)) {
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
