package online.kingdomkeys.kingdomkeys.menu;

import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.common.MutableDataComponentHolder;
import net.neoforged.neoforge.items.ComponentItemHandler;
import online.kingdomkeys.kingdomkeys.item.ModComponents;

public class PauldronInventory extends ComponentItemHandler {

    public PauldronInventory(MutableDataComponentHolder parent) {
        super(parent, ModComponents.INVENTORY.get(), 4);
    }

    @Override
    public boolean isItemValid(int slot, ItemStack stack) {
        if (stack.getItem() instanceof ArmorItem armorItem) {
            return switch (slot) {
                case 0 -> armorItem.getType().equals(ArmorItem.Type.HELMET);
                case 1 -> armorItem.getType().equals(ArmorItem.Type.CHESTPLATE);
                case 2 -> armorItem.getType().equals(ArmorItem.Type.LEGGINGS);
                case 3 -> armorItem.getType().equals(ArmorItem.Type.BOOTS);
                default -> false;
            };
        }
        return stack.getItem() instanceof ArmorItem || stack.isEmpty();
    }

}
