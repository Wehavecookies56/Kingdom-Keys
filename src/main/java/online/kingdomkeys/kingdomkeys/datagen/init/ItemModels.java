package online.kingdomkeys.kingdomkeys.datagen.init;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.DataGenerator;
import net.minecraft.world.item.*;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.registries.DeferredHolder;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.block.*;
import online.kingdomkeys.kingdomkeys.item.*;

public class ItemModels extends ItemModelProvider {

	public ItemModels(DataGenerator generator, ExistingFileHelper existingFileHelper) {
		super(generator.getPackOutput(), KingdomKeys.MODID, existingFileHelper);
	}

    @Override
    protected void registerModels() {
		for (DeferredHolder<Item, ? extends Item> itemRegistryObject : ModItems.ITEMS.getEntries()){

			//item Name
			final Item item = itemRegistryObject.get();
			final String path = BuiltInRegistries.ITEM.getKey(item).getPath();

			if (item instanceof BaseArmorItem) {
				standardArmor(path);
			} else if (item instanceof KeychainItem) {
				standardKeychain(path);
			} else if (item instanceof SynthesisItem) {
				standardMaterial(path);
			} else if (item instanceof BlockItem) {
				blockLogic((BlockItem) item, path);
			} else if (item instanceof KKRecordItem) {
				standardDisc(path);
			} else if (item instanceof KKArmorItem){
				standardKKArmor(path);
			} else if (item instanceof KKAccessoryItem){
				standardKKAccessory(path);
			} else if (item instanceof KeybladeItem) {
				// Keyblades already have models set up
			} else if (item instanceof ShieldItem) {
				// shields already have models set up
			} else if (item instanceof SwordItem) {
				// Wooden Keyblade/Stick
			} else if (item instanceof SpawnEggItem) {
				// Spawn Egg
				// This
				standardSpawnEggItem(path);
			} else if (item instanceof RecipeItem && !path.equals("recipe")) {
				standardRecipe(path);
			} else {
				standardItem(path);
			}
		}
    }

	private void blockLogic(BlockItem item, String path) {
		final Block block = item.getBlock();
		if (block instanceof GhostBloxBlock) {
			// generated as part of blockstates provider
		} else if (block instanceof PairBloxBlock) {
			// generated as part of blockstates provider
		} else if (block instanceof MagnetBloxBlock) {
			// manually generated version exists in main/resources
			standardBlockItem("magnet_blox_on");
			standardBlockItem("magnet_blox_off");
		} else if (block instanceof OrgPortalBlock) {
			// Custom Model
			// manually generated version exists in main/resources
		} else if (block instanceof SavePointBlock) {
			// Custom Model
			// manually generated version exists in main/resources
		} else if (block instanceof SoRCore) {
			// skip - no texture/special block
		} else if (block instanceof SoAPlatformCoreBlock) {
			// skip - no texture/special block?
		} else if (block instanceof DataPortalBlock) {
			// manually generated version exists in main/resources
		} else if (block instanceof MagicalChestBlock) {
			// manually generated version exists in main/resources
			getBuilder(path).parent(new ModelFile.UncheckedModelFile(KingdomKeys.MODID + ":block/" + path)).transforms().transform(ItemDisplayContext.GUI).rotation(0, 0, 0).translation(-0.25F, 1, 0).scale(1, 1, 1).end();
		} else {
			// fallback in case block item could not be generated as part of blockstates
			standardBlockItem(path);
		}
	}

    void standardMaterial(String name) {
		standardItem(name, "synthesis/");
	}

    void standardDisc(String name) {
		standardItem(name, "discs/");
	}

    void standardArmor(String name) {
		standardItem(name, "armor/");
	}

    void standardKeychain(String name) {
		standardItem(name, "keychains/");
	}
    
    void standardKKArmor(String name) {
		standardItem(name, "kkarmors/");
	}
    void standardKKAccessory(String name) {
		standardItem(name, "kkaccessories/");
	}

	void standardRecipe(String name) {
		getBuilder(name).parent(new ModelFile.UncheckedModelFile("item/generated")).texture("layer0","item/"+ name + "_tier");
	}

	void standardBlockItem(String name) {
		getBuilder(name).parent(new ModelFile.UncheckedModelFile(KingdomKeys.MODID + ":block/" + name));
	}

    void standardItem(String name) {
		standardItem(name, "");
	}

    void standardItem(String name, String path) {
		getBuilder(name).parent(new ModelFile.UncheckedModelFile("item/generated")).texture("layer0","item/"+ path + name);
	}

    void standardSpawnEggItem(String name) {
		getBuilder(name).parent(new ModelFile.UncheckedModelFile("item/template_spawn_egg"));
    }
    
    @Override
    public String getName() {
        return "Item Models";
    }
}
