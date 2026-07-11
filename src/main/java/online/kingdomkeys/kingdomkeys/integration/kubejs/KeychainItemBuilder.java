package online.kingdomkeys.kingdomkeys.integration.kubejs;

import dev.latvian.mods.kubejs.item.ItemBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import online.kingdomkeys.kingdomkeys.item.KeychainItem;

public class KeychainItemBuilder extends ItemBuilder {

    public KeychainItemBuilder(ResourceLocation i) {
        super(i);
    }

    @Override
    public Item createObject() {
        return new KeychainItem();
    }
}
