package online.kingdomkeys.kingdomkeys.container;

import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.SlotItemHandler;
import online.kingdomkeys.kingdomkeys.item.SynthesisItem;

public class SynthesisBagSlot extends SlotItemHandler {

    public SynthesisBagSlot(IItemHandlerModifiable inv, int index, int x, int y) {
        super((IItemHandler) inv, index, x, y);
    }

    @Override
    public boolean mayPlace (ItemStack stack) {
        return stack.getItem() instanceof SynthesisItem;
    }
    
    @Override
    public void setChanged() {
        container.setChanged();
    }
}
