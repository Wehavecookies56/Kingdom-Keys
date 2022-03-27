package online.kingdomkeys.kingdomkeys.datagen.init;

import javax.annotation.Nullable;

import net.minecraft.data.BlockTagsProvider;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.ItemTagsProvider;
import net.minecraft.item.Item;
import net.minecraft.item.MusicDiscItem;
import net.minecraft.tags.ITag;
import net.minecraft.tags.ItemTags;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.fml.RegistryObject;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.item.ModItems;

public class ItemTagsGen extends ItemTagsProvider
{
    public ItemTagsGen(DataGenerator dataGenerator, BlockTagsProvider blockTagsProvider, @Nullable ExistingFileHelper existingFileHelper)
    {
        super(dataGenerator, blockTagsProvider, KingdomKeys.MODID, existingFileHelper);
    }

    @Override
    protected void registerTags()
    {
        for (RegistryObject<Item> itemRegistryObject : ModItems.ITEMS.getEntries())
        {
            final Item item = itemRegistryObject.get();
            if (item instanceof MusicDiscItem)
            {
                add(ItemTags.MUSIC_DISCS, item);
                add(ItemTags.CREEPER_DROP_MUSIC_DISCS, item);
            }
        }
    }

    public void add(ITag.INamedTag<Item> branch, Item item)
    {
        this.getOrCreateBuilder(branch).add(item);
    }

    public void add(ITag.INamedTag<Item> branch, Item... item)
    {
        this.getOrCreateBuilder(branch).add(item);
    }


}