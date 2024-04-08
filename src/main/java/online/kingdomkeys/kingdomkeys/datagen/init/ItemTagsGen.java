package online.kingdomkeys.kingdomkeys.datagen.init;

import java.util.concurrent.CompletableFuture;

import org.jetbrains.annotations.Nullable;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.RecordItem;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.RegistryObject;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.item.*;
import online.kingdomkeys.kingdomkeys.item.organization.IOrgWeapon;

public class ItemTagsGen extends ItemTagsProvider {
	public static final TagKey<Item> KEYBLADES = bind(KingdomKeys.MODID+":keyblades");
	public static final TagKey<Item> KEYCHAINS = bind(KingdomKeys.MODID+":keychains");
	public static final TagKey<Item> MAGICS = bind(KingdomKeys.MODID+":magics");
	public static final TagKey<Item> DRIVES = bind(KingdomKeys.MODID+":drives");
	public static final TagKey<Item> ORG = bind(KingdomKeys.MODID+":org_weapons");
	public static final TagKey<Item> PAULDRONS = bind(KingdomKeys.MODID+":pauldrons");
	public static final TagKey<Item> ACCESSORIES = bind(KingdomKeys.MODID+":accessories");
	public static final TagKey<Item> ARMORS = bind(KingdomKeys.MODID+":armors");
	public static final TagKey<Item> MUSIC_DISCS = bind(KingdomKeys.MODID+":music_discs");
	
	public ItemTagsGen(PackOutput p_255871_, CompletableFuture<HolderLookup.Provider> p_256035_, CompletableFuture<TagLookup<Block>> p_256467_, @Nullable ExistingFileHelper existingFileHelper) {
		super(p_255871_, p_256035_, p_256467_, KingdomKeys.MODID, existingFileHelper);
	}

	@Override
	protected void addTags(HolderLookup.Provider pProvider) {
		for (RegistryObject<Item> itemRegistryObject : ModItems.ITEMS.getEntries()) {
			final Item item = itemRegistryObject.get();
			if (item instanceof RecordItem) {
				add(ItemTags.MUSIC_DISCS, item);
				add(ItemTags.CREEPER_DROP_MUSIC_DISCS, item);
			}
			if(item instanceof MagicSpellItem) {
				add(MAGICS,item);
			}
			if(item instanceof DriveFormOrbItem) {
				add(DRIVES,item);
			}
			if(item instanceof KeychainItem) {
				add(KEYCHAINS,item);
			}
			if(item instanceof KeybladeItem) {
				add(KEYBLADES,item);
			}
			if(item instanceof IOrgWeapon) {
				add(ORG,item);
			}
			if(item instanceof PauldronItem) {
				add(PAULDRONS,item);
			}
			if(item instanceof KKAccessoryItem) {
				add(ACCESSORIES,item);
			}
			if(item instanceof KKArmorItem) {
				add(ARMORS,item);
			}
			if(item instanceof KKRecordItem) {
				add(MUSIC_DISCS,item);
			}
		}
	}

	public void add(TagKey<Item> branch, Item item) {
		this.tag(branch).add(item);
	}

	public void add(TagKey<Item> branch, Item... item) {
		this.tag(branch).add(item);
	}

	
	private static TagKey<Item> bind(String pName) {
		return TagKey.create(Registries.ITEM, new ResourceLocation(pName));
	}

}