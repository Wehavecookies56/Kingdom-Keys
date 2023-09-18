package online.kingdomkeys.kingdomkeys.integration.kubejs;

import dev.latvian.mods.kubejs.item.custom.HandheldItemBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.item.KeybladeItem;

public class KeybladeItemBuilder extends HandheldItemBuilder {

    public KeybladeItemBuilder(ResourceLocation resourceLocation) {
        super(resourceLocation, 3F, -2.4F);
    }

    @Override
    public Item createObject() {
        return new KeybladeItem(new Item.Properties().tab(KingdomKeys.keybladesGroup).stacksTo(1));
    }
}