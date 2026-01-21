package online.kingdomkeys.kingdomkeys.client.gui.elements.buttons;

import net.minecraft.client.Minecraft;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.ItemStack;
import online.kingdomkeys.kingdomkeys.client.gui.elements.MenuFilterableIndexed;

public class MenuStockItemIndexed extends MenuStockItem {

    int index;

    public MenuStockItemIndexed(MenuFilterableIndexed parent, int index, ItemStack stack, int x, int y, int width, boolean showAmount) {
        super(parent, BuiltInRegistries.ITEM.getKey(stack.getItem()), stack, x, y, width, showAmount, b -> {
            parent.action(index);
        });
        this.index = index;
        this.stack = Minecraft.getInstance().player.getInventory().getItem(index);
        this.rl = BuiltInRegistries.ITEM.getKey(stack.getItem());
    }
}
