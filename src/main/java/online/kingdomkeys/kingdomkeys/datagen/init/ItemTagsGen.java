package online.kingdomkeys.kingdomkeys.datagen.init;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.registries.DeferredHolder;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.item.*;
import online.kingdomkeys.kingdomkeys.item.organization.IOrgWeapon;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

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
	public static final TagKey<Item> SYNTHESIS_MATERIAL = bind(KingdomKeys.MODID+":synthesis_material");

	public static final TagKey<Item> GUMMI_BLOCK_CUBE = bind(KingdomKeys.MODID+":gummi_block_cube");
	public static final TagKey<Item> GUMMI_BLOCK_WEDGE = bind(KingdomKeys.MODID+":gummi_block_wedge");
	public static final TagKey<Item> GUMMI_BLOCK_PYRAMID = bind(KingdomKeys.MODID+":gummi_block_pyramid");
	public static final TagKey<Item> GUMMI_BLOCK_CYLINDER = bind(KingdomKeys.MODID+":gummi_block_cylinder");
	public static final TagKey<Item> GUMMI_BLOCK_PIE = bind(KingdomKeys.MODID+":gummi_block_pie");
	public static final TagKey<Item> GUMMI_BLOCK_ROUND_CORNER = bind(KingdomKeys.MODID+":gummi_block_round_corner");
	public static final TagKey<Item> GUMMI_BLOCK_CONE = bind(KingdomKeys.MODID+":gummi_block_cone");
	public static final TagKey<Item> GUMMI_BLOCK_DOME = bind(KingdomKeys.MODID+":gummi_block_dome");

    public static final TagKey<Item> GUMMI_SHELL_BLOCK_CUBE = bind(KingdomKeys.MODID+":gummi_shell_block_cube");
    public static final TagKey<Item> GUMMI_SHELL_BLOCK_WEDGE = bind(KingdomKeys.MODID+":gummi_shell_block_wedge");
    public static final TagKey<Item> GUMMI_SHELL_BLOCK_PYRAMID = bind(KingdomKeys.MODID+":gummi_shell_block_pyramid");
    public static final TagKey<Item> GUMMI_SHELL_BLOCK_CYLINDER = bind(KingdomKeys.MODID+":gummi_shell_block_cylinder");
    public static final TagKey<Item> GUMMI_SHELL_BLOCK_PIE = bind(KingdomKeys.MODID+":gummi_shell_block_pie");
    public static final TagKey<Item> GUMMI_SHELL_BLOCK_ROUND_CORNER = bind(KingdomKeys.MODID+":gummi_shell_block_round_corner");
    public static final TagKey<Item> GUMMI_SHELL_BLOCK_CONE = bind(KingdomKeys.MODID+":gummi_shell_block_cone");
    public static final TagKey<Item> GUMMI_SHELL_BLOCK_DOME = bind(KingdomKeys.MODID+":gummi_shell_block_dome");

    public static final TagKey<Item> GUMMI_DISPEL_BLOCK_CUBE = bind(KingdomKeys.MODID+":gummi_dispel_block_cube");
    public static final TagKey<Item> GUMMI_DISPEL_BLOCK_WEDGE = bind(KingdomKeys.MODID+":gummi_dispel_block_wedge");
    public static final TagKey<Item> GUMMI_DISPEL_BLOCK_PYRAMID = bind(KingdomKeys.MODID+":gummi_dispel_block_pyramid");
    public static final TagKey<Item> GUMMI_DISPEL_BLOCK_CYLINDER = bind(KingdomKeys.MODID+":gummi_dispel_block_cylinder");
    public static final TagKey<Item> GUMMI_DISPEL_BLOCK_PIE = bind(KingdomKeys.MODID+":gummi_dispel_block_pie");
    public static final TagKey<Item> GUMMI_DISPEL_BLOCK_ROUND_CORNER = bind(KingdomKeys.MODID+":gummi_dispel_block_round_corner");
    public static final TagKey<Item> GUMMI_DISPEL_BLOCK_CONE = bind(KingdomKeys.MODID+":gummi_dispel_block_cone");
    public static final TagKey<Item> GUMMI_DISPEL_BLOCK_DOME = bind(KingdomKeys.MODID+":gummi_dispel_block_dome");

    public static final TagKey<Item> GUMMI_BLOCK_BUBBLE = bind(KingdomKeys.MODID+":gummi_block_bubble");

    public static final TagKey<Item> GUMMI_BLOCK_AERO_TRIANGLE = bind(KingdomKeys.MODID+":gummi_block_aero_triangle");
    public static final TagKey<Item> GUMMI_BLOCK_AERO_SQUARE = bind(KingdomKeys.MODID+":gummi_block_aero_square");

	public static final List<TagKey<Item>> GUMMI_BLOCK_KEYS = List.of(GUMMI_BLOCK_CUBE, GUMMI_BLOCK_WEDGE, GUMMI_BLOCK_PYRAMID, GUMMI_BLOCK_CYLINDER, GUMMI_BLOCK_PIE, GUMMI_BLOCK_ROUND_CORNER, GUMMI_BLOCK_CONE, GUMMI_BLOCK_DOME, GUMMI_BLOCK_AERO_SQUARE, GUMMI_BLOCK_AERO_TRIANGLE);
    public static final List<TagKey<Item>> GUMMI_SHELL_BLOCK_KEYS = List.of(GUMMI_SHELL_BLOCK_CUBE, GUMMI_SHELL_BLOCK_WEDGE, GUMMI_SHELL_BLOCK_PYRAMID, GUMMI_SHELL_BLOCK_CYLINDER, GUMMI_SHELL_BLOCK_PIE, GUMMI_SHELL_BLOCK_ROUND_CORNER, GUMMI_SHELL_BLOCK_CONE, GUMMI_SHELL_BLOCK_DOME);
    public static final List<TagKey<Item>> GUMMI_DISPEL_BLOCK_KEYS = List.of(GUMMI_DISPEL_BLOCK_CUBE, GUMMI_DISPEL_BLOCK_WEDGE, GUMMI_DISPEL_BLOCK_PYRAMID, GUMMI_DISPEL_BLOCK_CYLINDER, GUMMI_DISPEL_BLOCK_PIE, GUMMI_DISPEL_BLOCK_ROUND_CORNER, GUMMI_DISPEL_BLOCK_CONE, GUMMI_DISPEL_BLOCK_DOME);

    public static final List<TagKey<Item>> GUMMI_DIFFERENT_BLOCK_KEYS = List.of(GUMMI_BLOCK_BUBBLE);

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


		}
	}

	public void add(TagKey<Item> branch, Item item) {
		this.tag(branch).add(item);
	}

	public void add(TagKey<Item> branch, Item... item) {
		this.tag(branch).add(item);
	}

	
	private static TagKey<Item> bind(String pName) {
		return TagKey.create(Registries.ITEM, ResourceLocation.parse(pName));
	}

}