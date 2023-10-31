package online.kingdomkeys.kingdomkeys.container;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.items.SlotItemHandler;
import online.kingdomkeys.kingdomkeys.block.ModBlocks;
import online.kingdomkeys.kingdomkeys.entity.block.MagicalChestTileEntity;

public class MagicalChestContainer extends AbstractContainerMenu {

    public final MagicalChestTileEntity TE;

    private final ContainerLevelAccess canInteractWith;

    private static final int CHEST_SLOTS = MagicalChestTileEntity.NUMBER_OF_SLOTS;

    public MagicalChestContainer(final int windowID, final Inventory playerInventory, final MagicalChestTileEntity tileEntity) {
        super(ModContainers.MAGICAL_CHEST.get(), windowID);
        TE = tileEntity;
        canInteractWith = ContainerLevelAccess.create(TE.getLevel(), TE.getBlockPos());

        //Chest slots
        TE.getCapability(ForgeCapabilities.ITEM_HANDLER).ifPresent(iih -> {
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

    private static MagicalChestTileEntity getTileEntity(final Inventory playerInventory, final FriendlyByteBuf buf) {
        final BlockEntity te = playerInventory.player.level().getBlockEntity(buf.readBlockPos());
        if (te instanceof MagicalChestTileEntity) {
            return (MagicalChestTileEntity) te;
        }
        throw new IllegalStateException("Tile Entity mismatch with container");
    }

    public MagicalChestContainer(final int windowId, final Inventory playerInventory, final FriendlyByteBuf buf) {
        this(windowId, playerInventory, getTileEntity(playerInventory, buf));
    }

    @Override
    public boolean stillValid(Player playerIn) {
        return stillValid(canInteractWith, playerIn, ModBlocks.magicalChest.get());
    }

    @Override
    public ItemStack quickMoveStack(Player playerIn, int index) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);
        if (slot != null && slot.hasItem()) {
            ItemStack itemstack1 = slot.getItem();
            itemstack = itemstack1.copy();
            if (index < CHEST_SLOTS) {
                if (!this.moveItemStackTo(itemstack1, CHEST_SLOTS, this.slots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.moveItemStackTo(itemstack1, 0, CHEST_SLOTS, false)) {
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
