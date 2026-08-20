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
import online.kingdomkeys.kingdomkeys.block.gummi.GummiBlockBase;
import online.kingdomkeys.kingdomkeys.block.gummi.GummiHangarBlock;
import online.kingdomkeys.kingdomkeys.item.*;
import online.kingdomkeys.kingdomkeys.item.card.BiomeMemoryItem;
import online.kingdomkeys.kingdomkeys.item.card.MapCardItem;
import online.kingdomkeys.kingdomkeys.item.card.RouletteBonusItem;
import online.kingdomkeys.kingdomkeys.item.card.WorldCardItem;
import online.kingdomkeys.kingdomkeys.util.Utils;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

public class ItemModels extends ItemModelProvider {
	private static final Map<Item, String> SPELL_TEXTURES = Map.ofEntries(
			// Fire
			Map.entry(ModItems.fireSpell.get(), "fire"),
			Map.entry(ModItems.firaSpell.get(), "fire"),
			Map.entry(ModItems.firagaSpell.get(), "fire"),
			Map.entry(ModItems.darkFiragaSpell.get(), "fire"),
			Map.entry(ModItems.tripleFiragaSpell.get(), "fire"),
			Map.entry(ModItems.crawlingFiragaSpell.get(), "fire"),
			Map.entry(ModItems.fissionFiragaSpell.get(), "fire"),
			Map.entry(ModItems.firagaBurstSpell.get(), "fire"),
			Map.entry(ModItems.igniteSpell.get(), "fire"),

			// Blizzard
			Map.entry(ModItems.blizzardSpell.get(), "blizzard"),
			Map.entry(ModItems.blizzaraSpell.get(), "blizzard"),
			Map.entry(ModItems.blizzagaSpell.get(), "blizzard"),
			Map.entry(ModItems.tripleBlizzagaSpell.get(), "blizzard"),
			Map.entry(ModItems.deepFreezeSpell.get(), "blizzard"),
			Map.entry(ModItems.glacierSpell.get(), "blizzard"),
			Map.entry(ModItems.iceBarrageSpell.get(), "blizzard"),

			// Water
			Map.entry(ModItems.waterSpell.get(), "water"),
			Map.entry(ModItems.wateraSpell.get(), "water"),
			Map.entry(ModItems.watergaSpell.get(), "water"),

			// Thunder
			Map.entry(ModItems.thunderSpell.get(), "thunder"),
			Map.entry(ModItems.thundaraSpell.get(), "thunder"),
			Map.entry(ModItems.thundagaSpell.get(), "thunder"),
			Map.entry(ModItems.thundagaShotSpell.get(), "thunder"),
			Map.entry(ModItems.triplePlasmaSpell.get(), "thunder"),
			Map.entry(ModItems.sparkSpell.get(), "thunder"),
			Map.entry(ModItems.sparkraSpell.get(), "thunder"),
			Map.entry(ModItems.sparkgaSpell.get(), "thunder"),

			// Aero
			Map.entry(ModItems.aeroSpell.get(), "aero"),
			Map.entry(ModItems.aeroraSpell.get(), "aero"),
			Map.entry(ModItems.aerogaSpell.get(), "aero"),
			Map.entry(ModItems.aeroShieldSpell.get(), "aero"),
			Map.entry(ModItems.aeroraShieldSpell.get(), "aero"),
			Map.entry(ModItems.aerogaShieldSpell.get(), "aero"),

			// Cure
			Map.entry(ModItems.cureSpell.get(), "cure"),
			Map.entry(ModItems.curaSpell.get(), "cure"),
			Map.entry(ModItems.curagaSpell.get(), "cure"),
			Map.entry(ModItems.esunaSpell.get(), "cure"),
			Map.entry(ModItems.faithSpell.get(), "cure"),

			// Gravity
			Map.entry(ModItems.gravitySpell.get(), "gravity"),
			Map.entry(ModItems.graviraSpell.get(), "gravity"),
			Map.entry(ModItems.gravigaSpell.get(), "gravity"),
			Map.entry(ModItems.zeroGravitySpell.get(), "gravity"),
			Map.entry(ModItems.zeroGraviraSpell.get(), "gravity"),
			Map.entry(ModItems.zeroGravigaSpell.get(), "gravity"),

			// Magnet
			Map.entry(ModItems.magnetSpell.get(), "magnet"),
			Map.entry(ModItems.magneraSpell.get(), "magnet"),
			Map.entry(ModItems.magnegaSpell.get(), "magnet"),

			// Reflect
			Map.entry(ModItems.reflectSpell.get(), "reflect"),
			Map.entry(ModItems.refleraSpell.get(), "reflect"),
			Map.entry(ModItems.reflegaSpell.get(), "reflect"),

			// Stop
			Map.entry(ModItems.stopSpell.get(), "stop"),
			Map.entry(ModItems.stopraSpell.get(), "stop"),
			Map.entry(ModItems.stopgaSpell.get(), "stop"),
			Map.entry(ModItems.slowSpell.get(), "stop")
	);

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
			} else if (item instanceof MagicSpellItem) {
				String texture = SPELL_TEXTURES.getOrDefault(item, "generic");
				standardMagic(path, texture);
			} else if (item instanceof ShotlockItem) {
				standardShotlockItem(path);
			} else if (item instanceof KeybladeItem) {
				// Keyblades already have models set up
			} else if (item instanceof ShieldItem) {
				// shields already have models set up
			} else if (item instanceof SwordItem) {
				// Wooden Keyblade/Stick
			} else if (item instanceof SpawnEggItem) {
				// Spawn Egg
				standardSpawnEggItem(path);
			} else if (item instanceof RecipeItem && !path.equals("recipe")) {
				standardRecipe(path);
			}   else if(item instanceof WorldCardItem || item instanceof MapCardItem || item instanceof BiomeMemoryItem || item instanceof CardPackItem || item instanceof RouletteBonusItem){
				standardCard(path);
			} else {
				standardItem(path);
			}
		}
	}

	private void blockLogic(BlockItem item, String path) {
		final Block block = item.getBlock();
		String blockName = Utils.getBlockRegistryName(block).getPath();
		List<String> colours = Arrays.stream(DyeColor.values()).map(DyeColor::toString).sorted(Comparator.comparingInt(String::length).reversed()).toList();
		for (String colour : colours) {
			String suffix = "_" + colour;
			if (blockName.endsWith(suffix)) {
				blockName = blockName.substring(0, blockName.length() - suffix.length());
			}
		}

		switch (block) {
			case GummiBlockBase gummiBlockBase -> {
				if (!blockName.contains("gummi_cube")) {
					gummiBlockItem(path, blockName);
				} else {
					standardBlockItem(path);
				}
			}
			case GhostBloxBlock ghostBloxBlock -> {
				// generated as part of blockstates provider
			}
			case PairBloxBlock pairBloxBlock -> {
				// generated as part of blockstates provider
			}
			case MagnetBloxBlock magnetBloxBlock -> {
				// manually generated version exists in main/resources
				standardBlockItem("magnet_blox_on");
				standardBlockItem("magnet_blox_off");
			}
			case OrgPortalBlock orgPortalBlock -> {
				// Custom Model
				// manually generated version exists in main/resources
			}
			case SavePointBlock savePointBlock -> {
				// Custom Model
				// manually generated version exists in main/resources
			}
			case SoRCore soRCore -> {
				// skip - no texture/special block
			}
			case SoAPlatformCoreBlock soAPlatformCoreBlock -> {
				// skip - no texture/special block?
			}
			case DataPortalBlock dataPortalBlock -> {
				// manually generated version exists in main/resources
			}
			case MagicalChestBlock magicalChestBlock ->
				// manually generated version exists in main/resources
					getBuilder(path).parent(new ModelFile.UncheckedModelFile(KingdomKeys.MODID + ":block/" + path)).transforms().transform(ItemDisplayContext.GUI).rotation(0, 0, 0).translation(-0.25F, 1, 0).scale(1, 1, 1).end();
			case GummiHangarBlock gummiHangarBlock -> {
				// skip - no texture/special block
			}
			case MagicTargetBlock magicTargetBlock -> {
				// manually generated version exists in main/resources
			}
			case SoADoorBlock door -> {
				// manually generated version exists in main/resources
			}
			case CardDoorBlock door -> {
				// manually generated version exists in main/resources
			}

			case TreasureChestBlock treasureChestBlock -> {
				getBuilder("treasure_chest").parent(new ModelFile.UncheckedModelFile(KingdomKeys.MODID + ":block/magical_chest"));
			}

			case FlowmotionRailBlock rail -> {
				// flat sprite, generated as part of the blockstates provider
			}

			default ->
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

	void standardMagic(String name, String element) {
		standardMagicItem(name, "magic/", element);
	}

	void standardShotlockItem(String name) {
		getBuilder(name).parent(new ModelFile.UncheckedModelFile("item/generated")).texture("layer0", "item/shotlock_orb");
	}

	void standardCard(String name) {
		standardItem(name, "cards/");
	}

	void standardRecipe(String name) {
		getBuilder(name).parent(new ModelFile.UncheckedModelFile("item/generated")).texture("layer0","item/"+ name + "_tier");
	}

	void standardBlockItem(String name) {
		getBuilder(name).parent(new ModelFile.UncheckedModelFile(KingdomKeys.MODID + ":block/" + name));
	}

	void gummiBlockItem(String name, String type) {
		if (type.equals("gummi_bubble_helm")) {
			getBuilder(name).parent(new ModelFile.UncheckedModelFile(KingdomKeys.MODID + ":block/gummi/" + type)).transforms()
					.transform(ItemDisplayContext.GROUND).scale(0.25F, 0.25F, 0.25F).translation(0, 3F, 0).end()
					.transform(ItemDisplayContext.GUI).scale(0.3F, 0.3F, 0.3F).rotation(45, -135, 0).translation(-3, 0, 0).end()
					.transform(ItemDisplayContext.FIRST_PERSON_LEFT_HAND).scale(0.375F, 0.375F, 0.375F).rotation(0, -135, 0).end()
					.transform(ItemDisplayContext.FIRST_PERSON_RIGHT_HAND).scale(0.375F, 0.375F, 0.375F).rotation(0, -135, 0).end()
					.transform(ItemDisplayContext.THIRD_PERSON_LEFT_HAND).scale(0.375F, 0.375F, 0.375F).rotation(70, -135, 0).translation(0, 3, 0).end()
					.transform(ItemDisplayContext.THIRD_PERSON_RIGHT_HAND).scale(0.375F, 0.375F, 0.375F).rotation(70, -135, 0).translation(0, 3, 0).end()
					.transform(ItemDisplayContext.FIXED).scale(0.5F, 0.5F, 0.5F).end()
					.transform(ItemDisplayContext.HEAD).scale(0.5F, 0.5F, 0.5F).end()
					.end();
		} else if (type.equals("gummi_fira") || type.equals("gummi_blizzara") || type.equals("gummi_gravira") || type.equals("gummi_watera") || type.equals("gummi_firaga") || type.equals("gummi_blizzaga") || type.equals("gummi_graviga") || type.equals("gummi_waterga")) {
			getBuilder(name).parent(new ModelFile.UncheckedModelFile(KingdomKeys.MODID + ":block/gummi/" + type)).transforms()
					.transform(ItemDisplayContext.GROUND).scale(0.25F, 0.25F, 0.25F).translation(0, 3F, 0).end()
					.transform(ItemDisplayContext.GUI).scale(0.5F, 0.5F, 0.5F).rotation(45, -135, 0).translation(-5, 0, 0).end()
					.transform(ItemDisplayContext.FIRST_PERSON_LEFT_HAND).scale(0.375F, 0.375F, 0.375F).rotation(0, -135, 0).end()
					.transform(ItemDisplayContext.FIRST_PERSON_RIGHT_HAND).scale(0.375F, 0.375F, 0.375F).rotation(0, -135, 0).end()
					.transform(ItemDisplayContext.THIRD_PERSON_LEFT_HAND).scale(0.375F, 0.375F, 0.375F).rotation(70, -135, 0).translation(0, 3, 0).end()
					.transform(ItemDisplayContext.THIRD_PERSON_RIGHT_HAND).scale(0.375F, 0.375F, 0.375F).rotation(70, -135, 0).translation(0, 3, 0).end()
					.transform(ItemDisplayContext.FIXED).scale(0.5F, 0.5F, 0.5F).end()
					.transform(ItemDisplayContext.HEAD).scale(0.5F, 0.5F, 0.5F).end()
					.end();
		} else if (type.contains("gummi_pyramid")) {
			getBuilder(name).parent(new ModelFile.UncheckedModelFile(KingdomKeys.MODID + ":block/gummi/" + type)).transforms()
					.transform(ItemDisplayContext.GROUND).scale(0.25F, 0.25F, 0.25F).translation(0, 3F, 0).end()
					.transform(ItemDisplayContext.GUI).scale(0.7F, 0.7F, 0.7F).rotation(45, 180, 0).end()
					.transform(ItemDisplayContext.FIRST_PERSON_LEFT_HAND).scale(0.375F, 0.375F, 0.375F).rotation(0, -135, 0).end()
					.transform(ItemDisplayContext.FIRST_PERSON_RIGHT_HAND).scale(0.375F, 0.375F, 0.375F).rotation(0, -135, 0).end()
					.transform(ItemDisplayContext.THIRD_PERSON_LEFT_HAND).scale(0.375F, 0.375F, 0.375F).rotation(70, -135, 0).translation(0, 3, 0).end()
					.transform(ItemDisplayContext.THIRD_PERSON_RIGHT_HAND).scale(0.375F, 0.375F, 0.375F).rotation(70, -135, 0).translation(0, 3, 0).end()
					.transform(ItemDisplayContext.FIXED).scale(0.5F, 0.5F, 0.5F).end()
					.transform(ItemDisplayContext.HEAD).scale(0.5F, 0.5F, 0.5F).end()
					.end();
		} else {
			getBuilder(name).parent(new ModelFile.UncheckedModelFile(KingdomKeys.MODID + ":block/gummi/" + type)).transforms()
					.transform(ItemDisplayContext.GROUND).scale(0.25F, 0.25F, 0.25F).translation(0, 3F, 0).end()
					.transform(ItemDisplayContext.GUI).scale(0.6F, 0.6F, 0.6F).rotation(45, -135, 0).end()
					.transform(ItemDisplayContext.FIRST_PERSON_LEFT_HAND).scale(0.375F, 0.375F, 0.375F).rotation(0, -135, 0).end()
					.transform(ItemDisplayContext.FIRST_PERSON_RIGHT_HAND).scale(0.375F, 0.375F, 0.375F).rotation(0, -135, 0).end()
					.transform(ItemDisplayContext.THIRD_PERSON_LEFT_HAND).scale(0.375F, 0.375F, 0.375F).rotation(70, -135, 0).translation(0, 3, 0).end()
					.transform(ItemDisplayContext.THIRD_PERSON_RIGHT_HAND).scale(0.375F, 0.375F, 0.375F).rotation(70, -135, 0).translation(0, 3, 0).end()
					.transform(ItemDisplayContext.FIXED).scale(0.5F, 0.5F, 0.5F).end()
					.transform(ItemDisplayContext.HEAD).scale(0.5F, 0.5F, 0.5F).end()
					.end();
		}
	}

	void standardItem(String name) {
		standardItem(name, "");
	}

	void standardMagicItem(String name, String path, String element) {
		getBuilder(name).parent(new ModelFile.UncheckedModelFile("item/generated")).texture("layer0","item/"+ path + element);
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