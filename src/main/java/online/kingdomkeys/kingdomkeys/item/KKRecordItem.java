package online.kingdomkeys.kingdomkeys.item;

import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.JukeboxSong;
import online.kingdomkeys.kingdomkeys.api.item.IItemCategory;
import online.kingdomkeys.kingdomkeys.api.item.ItemCategory;

public class KKRecordItem extends Item implements IItemCategory {


    public KKRecordItem(ResourceKey<JukeboxSong> song) {
        super(new Properties().stacksTo(1).jukeboxPlayable(song));
    }

    @Override
    public ItemCategory getCategory() {
        return ItemCategory.MISC;
    }
}
