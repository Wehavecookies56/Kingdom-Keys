package online.kingdomkeys.kingdomkeys.container;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IWorldPosCallable;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.SlotItemHandler;
import online.kingdomkeys.kingdomkeys.block.ModBlocks;
import online.kingdomkeys.kingdomkeys.entity.block.MagicalChestTileEntity;

import javax.annotation.Nullable;

public class MagicalChestContainer extends Container {

    public final MagicalChestTileEntity TE;

    private final IWorldPosCallable canInteractWith;

    private static final int CHEST_SLOTS = MagicalChestTileEntity.NUMBER_OF_SLOTS;

    public MagicalChestContainer(final int windowID, final PlayerInventory playerInventory, final MagicalChestTileEntity tileEntity) {
        super(ModContainers.MAGICAL_CHEST.get(), windowID);
        TE = tileEntity;
        canInteractWith = IWorldPosCallable.of(TE.getWorld(), TE.getPos());

        //Chest slots
        TE.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent(iih -> {
            for (int i = 0; i < 4; i++) {
                for (int j = 0; j < 9; j++) {
                    addSlot(new SlotItemHandler(iih, j + i * 9, 8 + j * 18, 20 + i * 18));
                }
            }
        });

        int i,j;

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

    private static MagicalChestTileEntity getTileEntity(final PlayerInventory playerInventory, final PacketBuffer buf) {
        final TileEntity te = playerInventory.player.world.getTileEntity(buf.readBlockPos());
        if (te instanceof MagicalChestTileEntity) {
            return (MagicalChestTileEntity) te;
        }
        throw new IllegalStateException("Tile Entity mismatch with container");
    }

    public MagicalChestContainer(final int windowId, final PlayerInventory playerInventory, final PacketBuffer buf) {
        this(windowId, playerInventory, getTileEntity(playerInventory, buf));
    }

    @Override
    public boolean canInteractWith(PlayerEntity playerIn) {
        return isWithinUsableDistance(canInteractWith, playerIn, ModBlocks.magicalChest.get());
    }

    @Override
    public ItemStack transferStackInSlot(PlayerEntity playerIn, int index) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.inventorySlots.get(index);
        if (slot != null && slot.getHasStack()) {
            ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();
            if (index < CHEST_SLOTS) {
                if (!this.mergeItemStack(itemstack1, CHEST_SLOTS, this.inventorySlots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.mergeItemStack(itemstack1, 0, CHEST_SLOTS, false)) {
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
