package online.kingdomkeys.kingdomkeys.integration.kubejs;

import dev.latvian.mods.kubejs.item.ItemBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Item;
import online.kingdomkeys.kingdomkeys.item.KeychainItem;
import online.kingdomkeys.kingdomkeys.lib.ModTags;

public class KeychainItemBuilder extends ItemBuilder {

    public static final ResourceLocation[] KEYCHAIN_TAGS = {
            ModTags.KEYCHAINS.location(), ItemTags.SWORD_ENCHANTABLE.location(), ItemTags.SHARP_WEAPON_ENCHANTABLE.location()
    };

    public KeychainItemBuilder(ResourceLocation i) {
        super(i);
        tag(KEYCHAIN_TAGS);
    }

    @Override
    public Item createObject() {
        return new KeychainItem();
    }
}
