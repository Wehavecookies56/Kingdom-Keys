package online.kingdomkeys.kingdomkeys.menu;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.items.IItemHandlerModifiable;

public class PauldronMenu extends AbstractContainerMenu {

    public static PauldronMenu fromNetwork(int windowId, Inventory inv, FriendlyByteBuf buf) {
        InteractionHand hand = buf.readBoolean() ? InteractionHand.MAIN_HAND : InteractionHand.OFF_HAND;
        return new PauldronMenu(windowId, inv, inv.player.getItemInHand(hand));
    }

    public final ItemStack pauldron;
    public final IItemHandlerModifiable pauldronInv;

    public PauldronMenu(int windowId, Inventory playerInv, ItemStack pauldron) {
        super(ModMenus.PAULDRON.get(), windowId);
        this.pauldron = pauldron;
        int i, j;

        pauldronInv = (IItemHandlerModifiable) pauldron.getCapability(Capabilities.ItemHandler.ITEM);

        for (i = 0; i < pauldronInv.getSlots(); ++i) {
            addSlot(new PauldronSlot(pauldronInv, i, 8,8+i * 18));
        }

        for (i = 0; i < 3; ++i) {
            for (j = 0; j < 9; ++j) {
                addSlot(new Slot(playerInv, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
            }
        }

        for (i = 0; i < 9; ++i) {
            addSlot(new Slot(playerInv, i, 8 + i * 18, 142));
        }
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);
        if (slot != null && slot.hasItem()) {
            ItemStack itemstack1 = slot.getItem();
            itemstack = itemstack1.copy();
            if (index < 4) {
                if (!this.moveItemStackTo(itemstack1, 4, this.slots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.moveItemStackTo(itemstack1, 0, 4, false)) {
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

    @Override
    public boolean stillValid(Player player) {
        return player.getInventory().contains(pauldron);
    }
}
