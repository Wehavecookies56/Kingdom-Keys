package online.kingdomkeys.kingdomkeys.datagen.init;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.registries.DeferredHolder;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.item.*;
import online.kingdomkeys.kingdomkeys.item.card.CardCategory;
import online.kingdomkeys.kingdomkeys.item.card.MapCardItem;
import online.kingdomkeys.kingdomkeys.item.organization.IOrgWeapon;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

import static online.kingdomkeys.kingdomkeys.lib.ModTags.*;

public class ItemTagsGen extends ItemTagsProvider {

	public ItemTagsGen(PackOutput p_255871_, CompletableFuture<HolderLookup.Provider> p_256035_, CompletableFuture<TagLookup<Block>> p_256467_, @Nullable ExistingFileHelper existingFileHelper) {
		super(p_255871_, p_256035_, p_256467_, KingdomKeys.MODID, existingFileHelper);
	}

	@Override
	protected void addTags(HolderLookup.Provider pProvider) {
		for(int shape = 0; shape < Recipes.gummiBlocks.size();shape++){
			List<Supplier<Block>> suppliers = Recipes.gummiBlocks.get(shape);
			List<Item> items = suppliers.stream().map(blockSupplier -> blockSupplier.get().asItem()).toList();

            for (Item item : items) {
                add(GUMMI_BLOCK_KEYS.get(shape), item);
            }
		}

        for(int shape = 0; shape < Recipes.gummiShellBlocks.size();shape++){
            List<Supplier<Block>> suppliers = Recipes.gummiShellBlocks.get(shape);
            List<Item> items = suppliers.stream().map(blockSupplier -> blockSupplier.get().asItem()).toList();

            for (Item item : items) {
                add(GUMMI_SHELL_BLOCK_KEYS.get(shape), item);
            }
        }

        for(int shape = 0; shape < Recipes.gummiDispelBlocks.size();shape++){
            List<Supplier<Block>> suppliers = Recipes.gummiDispelBlocks.get(shape);
            List<Item> items = suppliers.stream().map(blockSupplier -> blockSupplier.get().asItem()).toList();

            for (Item item : items) {
                add(GUMMI_DISPEL_BLOCK_KEYS.get(shape), item);
            }
        }

        for(int shape = 0; shape < Recipes.gummiDifferentBlocks.size();shape++){
            List<Supplier<Block>> suppliers = Recipes.gummiDifferentBlocks.get(shape);
            List<Item> items = suppliers.stream().map(blockSupplier -> blockSupplier.get().asItem()).toList();

            for (Item item : items) {
                add(GUMMI_DIFFERENT_BLOCK_KEYS.get(shape), item);
            }
        }

		/*if(ModBlocks.gummiCubes.contains(block)){
			add(GUMMI_BLOCK, block);
			add(GUMMI_BLOCK_CUBE, block);
		} else if(ModBlocks.gummiWedges.contains(block)){
			add(GUMMI_BLOCK, block);
			add(GUMMI_BLOCK_WEDGE, block);
		} else if(ModBlocks.gummiPyramids.contains(block)){
			add(GUMMI_BLOCK, block);
			add(GUMMI_BLOCK_PYRAMID, block);
		} else if(ModBlocks.gummiCylinders.contains(block)){
			add(GUMMI_BLOCK, block);
			add(GUMMI_BLOCK_CYLINDER, block);
		} else if(ModBlocks.gummiPies.contains(block)){
			add(GUMMI_BLOCK, block);
			add(GUMMI_BLOCK_PIE, block);
		} else if(ModBlocks.gummiRoundCorners.contains(block)){
			add(GUMMI_BLOCK, block);
			add(GUMMI_BLOCK_ROUND_CORNER, block);
		} else if(ModBlocks.gummiCones.contains(block)){
			add(GUMMI_BLOCK, block);
			add(GUMMI_BLOCK_CONE, block);
		} else if(ModBlocks.gummiDomes.contains(block)){
			add(GUMMI_BLOCK, block);
			add(GUMMI_BLOCK_DOME, block);

		}*/

		for (DeferredHolder<Item, ? extends Item> itemRegistryObject : ModItems.ITEMS.getEntries()) {
			final Item item = itemRegistryObject.get();

			if(item instanceof MagicSpellItem) {
				add(MAGICS,item);
			}
			if(item instanceof DriveFormOrbItem) {
				add(DRIVES,item);
			}
			if(item instanceof BaseArmorItem armor) {
				switch(armor.getEquipmentSlot()) {
					case HEAD -> add(ItemTags.HEAD_ARMOR, armor);
					case CHEST -> add(ItemTags.CHEST_ARMOR, armor);
					case LEGS -> add(ItemTags.LEG_ARMOR, armor);
					case FEET -> add(ItemTags.FOOT_ARMOR, armor);
				}
			}
			if(item instanceof KeychainItem) {
				add(KEYCHAINS,item);
				add(ItemTags.SWORD_ENCHANTABLE, item);
				add(ItemTags.SHARP_WEAPON_ENCHANTABLE, item);
			}
			if(item instanceof KeybladeItem) {
				add(KEYBLADES,item);
				add(ItemTags.SWORD_ENCHANTABLE, item);
				add(ItemTags.SHARP_WEAPON_ENCHANTABLE, item);
			}
			if(item instanceof IOrgWeapon) {
				add(ORG,item);
				add(ItemTags.SWORD_ENCHANTABLE, item);
				add(ItemTags.SHARP_WEAPON_ENCHANTABLE, item);
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
				add(ItemTags.CREEPER_DROP_MUSIC_DISCS, item);
			}
			if(item instanceof SynthesisItem){
				add(SYNTHESIS_MATERIAL,item);
			}

			if (item instanceof MapCardItem mapCardItem) {
				if (mapCardItem.getCategory() != CardCategory.YELLOW) {
					add(MAP_CARD, item);
				} else {
					add(KEY_CARD, item);
				}
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