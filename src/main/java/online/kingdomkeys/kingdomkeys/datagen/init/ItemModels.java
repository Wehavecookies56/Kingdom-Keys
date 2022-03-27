package online.kingdomkeys.kingdomkeys.datagen.init;

import net.minecraft.block.Block;
import net.minecraft.data.DataGenerator;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ShieldItem;
import net.minecraft.item.SpawnEggItem;
import net.minecraft.item.SwordItem;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.fml.RegistryObject;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.block.DataPortalBlock;
import online.kingdomkeys.kingdomkeys.block.GhostBloxBlock;
import online.kingdomkeys.kingdomkeys.block.MagnetBloxBlock;
import online.kingdomkeys.kingdomkeys.block.OrgPortalBlock;
import online.kingdomkeys.kingdomkeys.block.PairBloxBlock;
import online.kingdomkeys.kingdomkeys.block.SavePointBlock;
import online.kingdomkeys.kingdomkeys.block.SoADoorBlock;
import online.kingdomkeys.kingdomkeys.block.SoAPlatformCoreBlock;
import online.kingdomkeys.kingdomkeys.block.SoRCore;
import online.kingdomkeys.kingdomkeys.item.BaseArmorItem;
import online.kingdomkeys.kingdomkeys.item.KKRecordItem;
import online.kingdomkeys.kingdomkeys.item.KeybladeItem;
import online.kingdomkeys.kingdomkeys.item.KeychainItem;
import online.kingdomkeys.kingdomkeys.item.ModItems;
import online.kingdomkeys.kingdomkeys.item.SynthesisItem;

public class ItemModels extends ItemModelProvider {

	public ItemModels(DataGenerator generator, ExistingFileHelper existingFileHelper) {
		super(generator, KingdomKeys.MODID, existingFileHelper);
	}

    @Override
    protected void registerModels() {
		for (RegistryObject<Item> itemRegistryObject : ModItems.ITEMS.getEntries()){

			//item Name
			final Item item = itemRegistryObject.get();
			final String path = item.getRegistryName().getPath();

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
			// skip - no texture/special block
		} else if (block instanceof SoADoorBlock) {
			// skip - no texture/special block?
		} else if (block instanceof DataPortalBlock) {
			// skip - no texture/special block?
		} else {
			// fallback incase block item could not be generated as part of blockstates
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
