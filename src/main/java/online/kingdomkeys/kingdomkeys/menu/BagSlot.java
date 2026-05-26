package online.kingdomkeys.kingdomkeys.menu;

import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.SlotItemHandler;
import java.util.function.Predicate;

public class BagSlot extends SlotItemHandler {

	private final Predicate<ItemStack> validator;

	public BagSlot(IItemHandler handler, int index, int x, int y, Predicate<ItemStack> validator) {

		super(handler, index, x, y);
		this.validator = validator;
	}

	@Override
	public boolean mayPlace(ItemStack stack) {
		return validator.test(stack);
	}

	@Override
	public void setChanged() {
		container.setChanged();
	}
}