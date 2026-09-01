package online.kingdomkeys.kingdomkeys.menu;

import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.common.MutableDataComponentHolder;
import net.neoforged.neoforge.items.ComponentItemHandler;
import online.kingdomkeys.kingdomkeys.item.KeychainItem;
import online.kingdomkeys.kingdomkeys.item.ModComponents;
import java.util.function.Predicate;

public class BagInventory extends ComponentItemHandler {

	private final Predicate<ItemStack> validator;

	public BagInventory(MutableDataComponentHolder parent, Predicate<ItemStack> validator) {
		super(parent, ModComponents.INVENTORY.get(), 72);
		this.validator = validator;
	}

	@Override
	public boolean isItemValid(int slot, ItemStack stack) {
		return stack.isEmpty() || validator.test(stack);
	}

	@Override
	public void setStackInSlot(int slot, ItemStack stack) {
		KeychainItem.ensureID(stack);
		super.setStackInSlot(slot, stack);
	}

	@Override
	public ItemStack insertItem(int slot, ItemStack toInsert, boolean simulate) {
		if (!simulate) {
			KeychainItem.ensureID(toInsert);
		}

		return super.insertItem(slot, toInsert, simulate);
	}
}