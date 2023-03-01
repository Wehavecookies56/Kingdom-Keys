package online.kingdomkeys.kingdomkeys.datagen.init;

import javax.annotation.Nullable;

import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.RecordItem;
import net.minecraftforge.common.data.BlockTagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.RegistryObject;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.item.ModItems;

public class ItemTagsGen extends ItemTagsProvider {
	public ItemTagsGen(PackOutput dataGenerator, BlockTagsProvider blockTagsProvider, @Nullable ExistingFileHelper existingFileHelper) {
		super(dataGenerator, lookupProvider, blockTagsProvider, KingdomKeys.MODID, existingFileHelper);
	}

	@Override
	protected void addTags(Provider provider) {
		for (RegistryObject<Item> itemRegistryObject : ModItems.ITEMS.getEntries()) {
			final Item item = itemRegistryObject.get();
			if (item instanceof RecordItem) {
				add(ItemTags.MUSIC_DISCS, item);
				add(ItemTags.CREEPER_DROP_MUSIC_DISCS, item);
			}
		}
	}

	public void add(TagKey<Item> branch, Item item) {
		this.tag(branch).add(item);
	}

	public void add(TagKey<Item> branch, Item... item) {
		this.tag(branch).add(item);
	}

}