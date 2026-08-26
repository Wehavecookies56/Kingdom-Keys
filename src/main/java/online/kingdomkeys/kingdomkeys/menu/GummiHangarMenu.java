package online.kingdomkeys.kingdomkeys.menu;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.SlotItemHandler;
import online.kingdomkeys.kingdomkeys.block.ModBlocks;
import online.kingdomkeys.kingdomkeys.entity.block.GummiHangarTileEntity;
import online.kingdomkeys.kingdomkeys.item.GummiShipBlueprintItem;

public class GummiHangarMenu extends AbstractContainerMenu {

	public final GummiHangarTileEntity TE;
	private final ContainerLevelAccess canInteractWith;

	private static final int HOTBAR_SLOT_COUNT = 9;
	private static final int PLAYER_INVENTORY_ROW_COUNT = 3;
	private static final int PLAYER_INVENTORY_COLUMN_COUNT = 9;
	private static final int PLAYER_INVENTORY_SLOT_COUNT = PLAYER_INVENTORY_COLUMN_COUNT * PLAYER_INVENTORY_ROW_COUNT;
	private static final int VANILLA_SLOT_COUNT = HOTBAR_SLOT_COUNT + PLAYER_INVENTORY_SLOT_COUNT;

	private static final int VANILLA_FIRST_SLOT_INDEX = 0;
	private static final int TE_INVENTORY_FIRST_SLOT_INDEX = VANILLA_FIRST_SLOT_INDEX + VANILLA_SLOT_COUNT;
	private static final int GUMMI_HANGAR_SLOTS = GummiHangarTileEntity.NUMBER_OF_SLOTS; // must match TileEntityInventoryBasic.NUMBER_OF_SLOTS

	public static final int TILE_INVENTORY_YPOS = 20; // the ContainerScreenBasic needs to know these so it can tell where to draw the
														// Titles
	public static final int PLAYER_INVENTORY_YPOS = 51;

	public GummiHangarMenu(final int windowID, final Inventory playerInventory, final GummiHangarTileEntity tileEntity) {
		super(ModMenus.GUMMI_HANGAR.get(), windowID);
		TE = tileEntity;
		canInteractWith = ContainerLevelAccess.create(TE.getLevel(), TE.getBlockPos());
        this.data = new ContainerData() {
            @Override
            public int get(int index) {
                return switch (index) {
                    case 0 -> TE.burnTime;
                    case 1 -> TE.maxBurnTime;
                    case 2 -> TE.energyStorage.getEnergyStored();
                    case 3 -> TE.getMaxEnergy();
                    default -> 0;
                };
            }

            @Override
            public void set(int index, int value) {
                switch (index) {
                    case 0 -> TE.burnTime = value;
                    case 1 -> TE.maxBurnTime = value;
                    case 2 -> TE.energyStorage.setEnergy(value);
                    case 3 -> TE.energyStorage.setCapacity(value);
                }
            }

            @Override
            public int getCount() {
                return 4;
            }
        };

        addDataSlots(data);
        int i,j;
		//Gummi Ship slot
		IItemHandler iih = TE.inventory.get();
		addSlot(new SlotItemHandler(iih, 0, 152, 18) {
			@Override
			public boolean mayPlace(ItemStack stack) {
				return GummiShipBlueprintItem.isBlueprint(stack);
			}
		});

        //Fuel slot
        addSlot(new SlotItemHandler(iih, 1, 152, 58) {
            @Override
            public boolean mayPlace(ItemStack stack) {
                return stack.getBurnTime(RecipeType.SMELTING) > 0;
            }
        });

		//Player Inventory slots
		for (i = 0; i < 3; ++i) {
			for (j = 0; j < 9; ++j) {
				addSlot(new Slot(playerInventory, j + i * 9 + 9, 8 + j * 18, 41 + 17 + (i + 4) * 18));
			}
		}

		//Player hotbar slots
		for (i = 0; i < 9; ++i) {
			addSlot(new Slot(playerInventory, i, 8 + i * 18, 45 + 17 + (3 + 4) * 18));
		}
	}

	private static GummiHangarTileEntity getTileEntity(final Inventory playerInventory, final FriendlyByteBuf buf) {
		final BlockEntity te = playerInventory.player.level().getBlockEntity(buf.readBlockPos());
		if (te instanceof GummiHangarTileEntity) {
			return (GummiHangarTileEntity) te;
		}
		throw new IllegalStateException("Tile Entity mismatch with container");
	}


	public GummiHangarMenu(final int windowId, final Inventory playerInventory, final FriendlyByteBuf buf) {
		this(windowId, playerInventory, getTileEntity(playerInventory, buf));
	}

	@Override
	public boolean stillValid(Player playerIn) {
		return stillValid(canInteractWith, playerIn, ModBlocks.gummiHangar.get());
	}

	@Override
	public ItemStack quickMoveStack(Player playerIn, int index) {
		ItemStack itemstack = ItemStack.EMPTY;
		Slot slot = this.slots.get(index);
		if (slot != null && slot.hasItem()) {
			ItemStack itemstack1 = slot.getItem();
			itemstack = itemstack1.copy();
			if (index < GUMMI_HANGAR_SLOTS) {
				if (!this.moveItemStackTo(itemstack1, GUMMI_HANGAR_SLOTS, this.slots.size(), true)) {
					return ItemStack.EMPTY;
				}
			} else if (!this.moveItemStackTo(itemstack1, 0, GUMMI_HANGAR_SLOTS, false)) {
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

    private final ContainerData data;

    public int getBurnTime() {
        return data.get(0);
    }

    public int getMaxBurnTime() {
        return data.get(1);
    }

    public int getEnergy() {
        return data.get(2);
    }

    public int getMaxEnergy() {
        return data.get(3);
    }

}
