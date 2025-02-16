package online.kingdomkeys.kingdomkeys.menu;

import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.common.MutableDataComponentHolder;
import net.neoforged.neoforge.items.ComponentItemHandler;
import online.kingdomkeys.kingdomkeys.item.ModComponents;
import online.kingdomkeys.kingdomkeys.item.SynthesisItem;

public class SynthesisBagInventory extends ComponentItemHandler {

	public SynthesisBagInventory(MutableDataComponentHolder parent) {
		super(parent, ModComponents.INVENTORY.get(), 72);
	}

	@Override
	public boolean isItemValid(int slot, ItemStack stack) {
		return stack.isEmpty() || stack.getItem() instanceof SynthesisItem;
	}
}
