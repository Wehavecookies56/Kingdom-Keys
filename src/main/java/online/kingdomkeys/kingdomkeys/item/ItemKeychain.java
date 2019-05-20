package online.kingdomkeys.kingdomkeys.item;

import net.minecraft.item.Item;
import net.minecraft.item.ItemSword;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.lib.Reference;

public class ItemKeychain extends ItemSword {

    public ItemKeychain(String name) {
        super(new ItemTierKeyblade(0), 0, 0, new Item.Properties().group(KingdomKeys.keybladesGroup).maxStackSize(1));
        setRegistryName(Reference.MODID, name);
    }
}
