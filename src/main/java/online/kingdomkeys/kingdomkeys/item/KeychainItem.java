package online.kingdomkeys.kingdomkeys.item;

import net.minecraft.item.Item;
import net.minecraft.item.SwordItem;
import online.kingdomkeys.kingdomkeys.KingdomKeys;

public class KeychainItem extends SwordItem {

    public KeychainItem() {
        super(new KeybladeItemTier(0), 0, 0, new Item.Properties().group(KingdomKeys.keybladesGroup).maxStackSize(1));
    }
}
