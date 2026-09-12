package online.kingdomkeys.kingdomkeys.menu;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.DataSlot;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.DyeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.SlotItemHandler;
import online.kingdomkeys.kingdomkeys.block.ModBlocks;
import online.kingdomkeys.kingdomkeys.entity.block.ApprenticeClothStationTileEntity;
import online.kingdomkeys.kingdomkeys.item.UnionApprenticeArmorItem;

public class ApprenticeClothStationMenu extends AbstractContainerMenu {

	public final ApprenticeClothStationTileEntity TE;
	private final ContainerLevelAccess access;

	// Slot indices in this menu, in the same order the tile entity holds them
	public static final int MENU_INPUT = ApprenticeClothStationTileEntity.INPUT_SLOT;
	public static final int MENU_PRIMARY_START = ApprenticeClothStationTileEntity.PRIMARY_DYE_START;
	public static final int MENU_SECONDARY_START = ApprenticeClothStationTileEntity.SECONDARY_DYE_START;
	public static final int MENU_OUTPUT = ApprenticeClothStationTileEntity.OUTPUT_SLOT;
	public static final int TE_SLOTS = ApprenticeClothStationTileEntity.SLOT_COUNT;

	private static final int DYE_ROWS = 4;
	private static final int DYE_TOP = 17, DYE_STEP = 18;
	private static final int PRIMARY_LEFT = 8, SECONDARY_LEFT = 134;

	public ApprenticeClothStationMenu(int windowId, Inventory playerInv, ApprenticeClothStationTileEntity te) {
		super(ModMenus.APPRENTICE_CLOTH_STATION.get(), windowId);
		this.TE = te;
		this.access = ContainerLevelAccess.create(te.getLevel(), te.getBlockPos());

		IItemHandler inv = te.getItemHandler();

		// Input armor (center-ish)
		addSlot(new SlotItemHandler(inv, ApprenticeClothStationTileEntity.INPUT_SLOT, 80, 35) {
			@Override
			public boolean mayPlace(ItemStack stack) {
				return stack.getItem() instanceof UnionApprenticeArmorItem;
			}

			@Override
			public int getMaxStackSize() {
				return 1;
			}
		});

		// Primary dyes, left
		addDyeBlock(inv, ApprenticeClothStationTileEntity.PRIMARY_DYE_START, ApprenticeClothStationTileEntity.PRIMARY_DYE_COUNT, PRIMARY_LEFT);

		// Secondary dyes, right
		addDyeBlock(inv, ApprenticeClothStationTileEntity.SECONDARY_DYE_START, ApprenticeClothStationTileEntity.SECONDARY_DYE_COUNT, SECONDARY_LEFT);

		// Output
		addSlot(new SlotItemHandler(inv, ApprenticeClothStationTileEntity.OUTPUT_SLOT, 80, 75) {
			@Override
			public boolean mayPlace(ItemStack stack) {
				return false;
			}

			@Override
			public void onTake(Player player, ItemStack stack) {
				TE.onTakeResult();
				super.onTake(player, stack);
			}
		});

		// Player inventory
		for (int row = 0; row < 3; ++row) {
			for (int col = 0; col < 9; ++col) {
				addSlot(new Slot(playerInv, col + row * 9 + 9, 8 + col * 18, 104 + row * 18));
			}
		}
		// Hotbar
		for (int col = 0; col < 9; ++col) {
			addSlot(new Slot(playerInv, col, 8 + col * 18, 162));
		}

		// Sync selected design
		addDataSlot(new DataSlot() {
			@Override
			public int get() {
				return TE.getSelectedDesign();
			}

			@Override
			public void set(int value) {
				TE.setSelectedDesign(value);
			}
		});

		TE.updateResult();
	}

	/** A block of dye slots filled column by column, so the first one you drop into is the top left. */
	private void addDyeBlock(IItemHandler inv, int start, int count, int left) {
		for (int i = 0; i < count; i++) {
			final int index = start + i;
			int x = left + (i / DYE_ROWS) * DYE_STEP;
			int y = DYE_TOP + (i % DYE_ROWS) * DYE_STEP;

			addSlot(new SlotItemHandler(inv, index, x, y) {
				@Override
				public boolean mayPlace(ItemStack stack) {
					return stack.getItem() instanceof DyeItem;
				}
			});
		}
	}

	public ApprenticeClothStationMenu(int windowId, Inventory playerInv, FriendlyByteBuf buf) {
		this(windowId, playerInv, getTileEntity(playerInv, buf));
	}

	private static ApprenticeClothStationTileEntity getTileEntity(Inventory playerInv, FriendlyByteBuf buf) {
		BlockEntity be = playerInv.player.level().getBlockEntity(buf.readBlockPos());
		if (be instanceof ApprenticeClothStationTileEntity te) {
			return te;
		}
		throw new IllegalStateException("Tile entity mismatch for ApprenticeClothStation");
	}

	public int getSelectedDesign() {
		return TE.getSelectedDesign();
	}

	/** Client calls this; value is written through DataSlot on next sync from server after packet. */
	public void setSelectedDesign(int design) {
		TE.setSelectedDesign(design);
	}

	@Override
	public boolean stillValid(Player player) {
		return stillValid(access, player, ModBlocks.apprenticeClothStation.get());
	}

	@Override
	public ItemStack quickMoveStack(Player player, int index) {
		ItemStack result = ItemStack.EMPTY;
		Slot slot = this.slots.get(index);
		if (slot == null || !slot.hasItem()) {
			return ItemStack.EMPTY;
		}

		ItemStack stack = slot.getItem();
		result = stack.copy();

		if (index < TE_SLOTS) {
			// TE -> player
			if (!this.moveItemStackTo(stack, TE_SLOTS, this.slots.size(), true)) {
				return ItemStack.EMPTY;
			}
		} else {
			// player -> TE
			if (stack.getItem() instanceof UnionApprenticeArmorItem) {
				if (!this.moveItemStackTo(stack, MENU_INPUT, MENU_INPUT + 1, false)) {
					return ItemStack.EMPTY;
				}
			} else if (stack.getItem() instanceof DyeItem) {
				// Try primary first, then secondary
				if (!this.moveItemStackTo(stack, MENU_PRIMARY_START, MENU_PRIMARY_START + ApprenticeClothStationTileEntity.PRIMARY_DYE_COUNT, false) && !this.moveItemStackTo(stack, MENU_SECONDARY_START, MENU_SECONDARY_START + ApprenticeClothStationTileEntity.SECONDARY_DYE_COUNT, false)) {
					return ItemStack.EMPTY;
				}
			} else {
				return ItemStack.EMPTY;
			}
		}

		if (stack.isEmpty()) {
			slot.set(ItemStack.EMPTY);
		} else {
			slot.setChanged();
		}
		return result;
	}
}
