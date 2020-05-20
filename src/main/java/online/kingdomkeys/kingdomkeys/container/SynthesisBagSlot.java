package online.kingdomkeys.kingdomkeys.container;

import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.SlotItemHandler;
import online.kingdomkeys.kingdomkeys.synthesis.material.SynthesisItem;

public class SynthesisBagSlot extends SlotItemHandler {

    public SynthesisBagSlot(IItemHandlerModifiable inv, int index, int x, int y) {
        super((IItemHandler) inv, index, x, y);
    }

    @Override
    public boolean isItemValid (ItemStack stack) {
        return stack.getItem() instanceof SynthesisItem;
    }
    
    @Override
    public void onSlotChanged() {
        inventory.markDirty();
    }
}
