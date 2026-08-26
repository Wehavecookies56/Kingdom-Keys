package online.kingdomkeys.kingdomkeys.item;

import net.minecraft.world.item.ItemStack;

public interface ILevelableItem {
	int getLocalLevel(ItemStack stack);

	int getLocalExp(ItemStack stack);

	int getLocalMaxExp();

	float getLocalPercent(ItemStack stack);

	boolean isMaxed(ItemStack stack);
}
