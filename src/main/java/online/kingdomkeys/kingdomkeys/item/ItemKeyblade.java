package online.kingdomkeys.kingdomkeys.item;

import net.minecraft.item.Item;
import net.minecraft.item.ItemSword;

import online.kingdomkeys.kingdomkeys.KingdomKeys;

public class ItemKeyblade extends ItemSword {

    //TODO implement stats
    int strength, magic;

    public ItemKeyblade(String name, int attackDamageIn, float attackSpeedIn) {
        super(new ItemTierKeyblade(attackDamageIn), attackDamageIn, attackSpeedIn, new Item.Properties().group(KingdomKeys.keybladesGroup).maxStackSize(1));
        setRegistryName(KingdomKeys.MODID, name);
    }
}
