package online.kingdomkeys.kingdomkeys.integration.kubejs;

import dev.latvian.mods.kubejs.item.ItemBuilder;
import dev.latvian.mods.kubejs.item.custom.HandheldItemBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Item;
import online.kingdomkeys.kingdomkeys.item.KeybladeItem;
import online.kingdomkeys.kingdomkeys.lib.ModTags;

public class KeybladeItemBuilder extends ItemBuilder {

    public static final ResourceLocation[] KEYBLADE_TAGS = {
            ModTags.KEYBLADES.location(), ItemTags.SWORD_ENCHANTABLE.location(), ItemTags.SHARP_WEAPON_ENCHANTABLE.location()
    };

    public KeybladeItemBuilder(ResourceLocation resourceLocation) {
        super(resourceLocation);
        tag(KEYBLADE_TAGS);
    }

    //TODO add to creative tab now that items are added in an event

    @Override
    public Item createObject() {
        return new KeybladeItem(new Item.Properties().stacksTo(1));
    }
}