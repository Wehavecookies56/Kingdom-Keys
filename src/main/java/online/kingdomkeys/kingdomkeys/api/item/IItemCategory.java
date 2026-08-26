package online.kingdomkeys.kingdomkeys.api.item;

import net.minecraft.world.item.ItemStack;
import online.kingdomkeys.kingdomkeys.util.Utils;

public interface IItemCategory {

    ItemCategory getCategory();

    default String getDescriptionKey(ItemStack stack) {
        return Utils.createDescriptionKey(stack);
    }

}
