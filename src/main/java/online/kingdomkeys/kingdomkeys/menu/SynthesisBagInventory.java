package online.kingdomkeys.kingdomkeys.menu;

import javax.annotation.Nonnull;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemContainerContents;
import net.neoforged.neoforge.capabilities.ICapabilityProvider;
import net.neoforged.neoforge.common.MutableDataComponentHolder;
import net.neoforged.neoforge.common.util.Lazy;
import net.neoforged.neoforge.items.ComponentItemHandler;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.ItemStackHandler;
import online.kingdomkeys.kingdomkeys.item.ModComponents;
import online.kingdomkeys.kingdomkeys.item.SynthesisItem;
import org.jetbrains.annotations.UnknownNullability;

public class SynthesisBagInventory extends ComponentItemHandler {

	public SynthesisBagInventory(MutableDataComponentHolder parent) {
		super(parent, ModComponents.INVENTORY.get(), 72);
	}

	@Override
	public boolean isItemValid(int slot, ItemStack stack) {
		return !stack.isEmpty() && stack.getItem() instanceof SynthesisItem;
	}
}
