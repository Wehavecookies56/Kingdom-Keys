package online.kingdomkeys.kingdomkeys.menu;

import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.IItemHandlerModifiable;
import net.neoforged.neoforge.items.SlotItemHandler;
import online.kingdomkeys.kingdomkeys.item.SynthesisItem;

public class SynthesisBagSlot extends SlotItemHandler {

    public SynthesisBagSlot(IItemHandlerModifiable inv, int index, int x, int y) {
        super(inv, index, x, y);
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
