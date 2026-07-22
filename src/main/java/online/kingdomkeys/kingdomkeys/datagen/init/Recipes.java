package online.kingdomkeys.kingdomkeys.datagen.init;

import com.mojang.authlib.properties.PropertyMap;
import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponents;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.recipes.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.ResolvableProfile;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.neoforge.common.Tags;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.block.ModBlocks;
import online.kingdomkeys.kingdomkeys.item.ModComponents;
import online.kingdomkeys.kingdomkeys.item.ModItems;
import online.kingdomkeys.kingdomkeys.item.card.WorldCardItem;
import online.kingdomkeys.kingdomkeys.lib.Constants;
import online.kingdomkeys.kingdomkeys.lib.ModTags;
import online.kingdomkeys.kingdomkeys.util.Utils;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

public class Recipes extends RecipeProvider {
    DataGenerator dataGenerator;

    //Stonecutter recipes
	public static final List<List<Supplier<Block>>> gummiBlocks = List.of(ModBlocks.gummiCubes, ModBlocks.gummiWedges, ModBlocks.gummiPyramids, ModBlocks.gummiCylinders, ModBlocks.gummiPies, ModBlocks.gummiRoundCorners, ModBlocks.gummiCones, ModBlocks.gummiDomes, ModBlocks.gummiAeroSquares, ModBlocks.gummiAeroTriangles);
    public static final List<List<Supplier<Block>>> gummiShellBlocks = List.of(ModBlocks.gummiShellCubes, ModBlocks.gummiShellWedges, ModBlocks.gummiShellPyramids, ModBlocks.gummiShellCylinders, ModBlocks.gummiShellPies, ModBlocks.gummiShellRoundCorners, ModBlocks.gummiShellCones, ModBlocks.gummiShellDomes);
    public static final List<List<Supplier<Block>>> gummiDispelBlocks = List.of(ModBlocks.gummiDispelCubes, ModBlocks.gummiDispelWedges, ModBlocks.gummiDispelPyramids, ModBlocks.gummiDispelCylinders, ModBlocks.gummiDispelPies, ModBlocks.gummiDispelRoundCorners, ModBlocks.gummiDispelCones, ModBlocks.gummiDispelDomes);

    //Other blocks that shouldn't be stonecutted alongside normal blocks
    public static final List<List<Supplier<Block>>> gummiDifferentBlocks = List.of(ModBlocks.gummiBubbleHelms);
    public Recipes(DataGenerator dataGenerator, CompletableFuture<HolderLookup.Provider> pRegistries) {
        super(dataGenerator.getPackOutput(), pRegistries);
        this.dataGenerator = dataGenerator;
    }

	private ItemStack createWorldCard(Item item, String floorType) {
		ItemStack out = new ItemStack(item);
		out.set(ModComponents.WORLD_CARD, new WorldCardItem.WorldCard(KingdomKeys.rl(floorType)));
		return out;
	}

	private ItemStack createDevSkull(String username) {
		ItemStack out = new ItemStack(Items.PLAYER_HEAD);
		out.set(DataComponents.PROFILE, new ResolvableProfile(Optional.of(username), Optional.empty(), new PropertyMap()));
		return out;
	}

	@Override
	protected void buildRecipes(RecipeOutput consumer, HolderLookup.Provider holderLookup) {
		ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.emptyCard.get())
				.define('P', Items.PAPER)
				.define('E', Items.ENDER_EYE)
				.pattern("PPP")
				.pattern("PEP")
				.pattern("PPP")
				.group(KingdomKeys.MODID)
				.unlockedBy("empty_card", InventoryChangeTrigger.TriggerInstance.hasItems(Items.ENDER_EYE))
				.save(consumer);

		ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, createWorldCard(ModItems.plainsCard.get(), "plains"))
				.requires(ModItems.emptyCard.get())
				.requires(ModItems.plainsMemory.get())
				.unlockedBy("plains_card", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.plainsMemory.get()))
				.save(consumer);

		ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, createWorldCard(ModItems.desertCard.get(), "desert"))
				.requires(ModItems.emptyCard.get())
				.requires(ModItems.desertMemory.get())
				.unlockedBy("desert_card", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.desertMemory.get()))
				.save(consumer);

		for (Constants.DevRecipe dev : Constants.devRecipes) {
			ShapelessRecipeBuilder.shapeless(RecipeCategory.DECORATIONS, createDevSkull(dev.name()))
					.requires(Items.WITHER_SKELETON_SKULL)
					.requires(dev.material1())
					.requires(dev.material2())
					.unlockedBy("wither_skeleton_skull", InventoryChangeTrigger.TriggerInstance.hasItems(Items.WITHER_SKELETON_SKULL))
					.save(consumer, KingdomKeys.rl(dev.name().toLowerCase() + "_skull"));
		}

		ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModBlocks.struggleBoard.get())
				.define('W', ItemTags.PLANKS)
				.define('D', ModItems.trainingDummy.get())
				.pattern("WWW")
				.pattern("WDW")
				.pattern("WWW")
				.group(KingdomKeys.MODID)
				.unlockedBy("struggle_board", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.trainingDummy.get()))
				.save(consumer);


		ShapelessRecipeBuilder.shapeless(RecipeCategory.COMBAT, ModBlocks.magicTarget.get())
				.requires(Blocks.TARGET)
				.requires(Blocks.REDSTONE_BLOCK)
				.unlockedBy("magic_target", InventoryChangeTrigger.TriggerInstance.hasItems(Blocks.TARGET))
				.save(consumer);

		ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.terra_Shoulder.get())
				.define('B', ItemTags.BUTTONS)
				.define('I', Items.IRON_INGOT)
				.define('D', Items.ORANGE_DYE)
				.define('G', Items.GOLD_INGOT)
				.pattern("DGD")
				.pattern("IBI")
				.pattern("DGD")
				.group(KingdomKeys.MODID)
				.unlockedBy("terra_shoulder", InventoryChangeTrigger.TriggerInstance.hasItems(Blocks.STONE))
				.save(consumer);

    	SmithingTransformRecipeBuilder.smithing(Ingredient.of(Items.NETHERITE_UPGRADE_SMITHING_TEMPLATE), Ingredient.of(Items.NETHERITE_HELMET), Ingredient.of(Items.ORANGE_DYE), RecipeCategory.COMBAT, ModItems.terra_Helmet.get())
        .unlocks("has_keyblade_armor", InventoryChangeTrigger.TriggerInstance.hasItems(Items.NETHERITE_HELMET))
        .save(consumer, KingdomKeys.rl("keyblade_armor_terra_helmet_smithing"));

		SmithingTransformRecipeBuilder.smithing(Ingredient.of(Items.NETHERITE_UPGRADE_SMITHING_TEMPLATE), Ingredient.of(Items.NETHERITE_CHESTPLATE), Ingredient.of(Items.ORANGE_DYE), RecipeCategory.COMBAT, ModItems.terra_Chestplate.get())
        .unlocks("has_keyblade_armor", InventoryChangeTrigger.TriggerInstance.hasItems(Items.NETHERITE_CHESTPLATE))
        .save(consumer, KingdomKeys.rl("keyblade_armor_terra_chestplate_smithing"));

		SmithingTransformRecipeBuilder.smithing(Ingredient.of(Items.NETHERITE_UPGRADE_SMITHING_TEMPLATE), Ingredient.of(Items.NETHERITE_LEGGINGS), Ingredient.of(Items.ORANGE_DYE), RecipeCategory.COMBAT, ModItems.terra_Leggings.get())
        .unlocks("has_keyblade_armor", InventoryChangeTrigger.TriggerInstance.hasItems(Items.NETHERITE_LEGGINGS))
        .save(consumer, KingdomKeys.rl("keyblade_armor_terra_leggings_smithing"));

		SmithingTransformRecipeBuilder.smithing(Ingredient.of(Items.NETHERITE_UPGRADE_SMITHING_TEMPLATE), Ingredient.of(Items.NETHERITE_BOOTS), Ingredient.of(Items.ORANGE_DYE), RecipeCategory.COMBAT, ModItems.terra_Boots.get())
        .unlocks("has_keyblade_armor", InventoryChangeTrigger.TriggerInstance.hasItems(Items.NETHERITE_BOOTS))
        .save(consumer, KingdomKeys.rl("keyblade_armor_terra_boots_smithing"));

    	//Aqua
		ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.aqua_Shoulder.get())
				.define('B', ItemTags.BUTTONS)
				.define('I', Items.IRON_INGOT)
				.define('D', Items.BLUE_DYE)
				.define('G', Items.GOLD_INGOT)
				.pattern("DGD")
				.pattern("IBI")
				.pattern("DGD")
				.group(KingdomKeys.MODID)
				.unlockedBy("aqua_shoulder", InventoryChangeTrigger.TriggerInstance.hasItems(Blocks.STONE))
				.save(consumer);

		SmithingTransformRecipeBuilder.smithing(Ingredient.of(Items.NETHERITE_UPGRADE_SMITHING_TEMPLATE), Ingredient.of(Items.NETHERITE_HELMET), Ingredient.of(Items.BLUE_DYE), RecipeCategory.COMBAT, ModItems.aqua_Helmet.get())
        .unlocks("has_keyblade_armor", InventoryChangeTrigger.TriggerInstance.hasItems(Items.NETHERITE_HELMET))
        .save(consumer, KingdomKeys.rl("keyblade_armor_aqua_helmet_smithing"));

		SmithingTransformRecipeBuilder.smithing(Ingredient.of(Items.NETHERITE_UPGRADE_SMITHING_TEMPLATE), Ingredient.of(Items.NETHERITE_CHESTPLATE), Ingredient.of(Items.BLUE_DYE), RecipeCategory.COMBAT, ModItems.aqua_Chestplate.get())
        .unlocks("has_keyblade_armor", InventoryChangeTrigger.TriggerInstance.hasItems(Items.NETHERITE_CHESTPLATE))
        .save(consumer, KingdomKeys.rl("keyblade_armor_aqua_chestplate_smithing"));

		SmithingTransformRecipeBuilder.smithing(Ingredient.of(Items.NETHERITE_UPGRADE_SMITHING_TEMPLATE), Ingredient.of(Items.NETHERITE_LEGGINGS), Ingredient.of(Items.BLUE_DYE), RecipeCategory.COMBAT, ModItems.aqua_Leggings.get())
        .unlocks("has_keyblade_armor", InventoryChangeTrigger.TriggerInstance.hasItems(Items.NETHERITE_LEGGINGS))
        .save(consumer, KingdomKeys.rl("keyblade_armor_aqua_leggings_smithing"));

		SmithingTransformRecipeBuilder.smithing(Ingredient.of(Items.NETHERITE_UPGRADE_SMITHING_TEMPLATE), Ingredient.of(Items.NETHERITE_BOOTS), Ingredient.of(Items.BLUE_DYE), RecipeCategory.COMBAT, ModItems.aqua_Boots.get())
        .unlocks("has_keyblade_armor", InventoryChangeTrigger.TriggerInstance.hasItems(Items.NETHERITE_BOOTS))
        .save(consumer, KingdomKeys.rl("keyblade_armor_aqua_boots_smithing"));

    	//Ventus
		ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.ventus_Shoulder.get())
				.define('B', ItemTags.BUTTONS)
				.define('I', Items.IRON_INGOT)
				.define('D', Items.LIME_DYE)
				.define('G', Items.GOLD_INGOT)
				.pattern("DGD")
				.pattern("IBI")
				.pattern("DGD")
				.group(KingdomKeys.MODID)
				.unlockedBy("ventus_shoulder", InventoryChangeTrigger.TriggerInstance.hasItems(Blocks.STONE))
				.save(consumer);

		SmithingTransformRecipeBuilder.smithing(Ingredient.of(Items.NETHERITE_UPGRADE_SMITHING_TEMPLATE), Ingredient.of(Items.NETHERITE_HELMET), Ingredient.of(Items.LIME_DYE), RecipeCategory.COMBAT, ModItems.ventus_Helmet.get())
        .unlocks("has_keyblade_armor", InventoryChangeTrigger.TriggerInstance.hasItems(Items.NETHERITE_HELMET))
        .save(consumer, KingdomKeys.rl("keyblade_armor_ventus_helmet_smithing"));

		SmithingTransformRecipeBuilder.smithing(Ingredient.of(Items.NETHERITE_UPGRADE_SMITHING_TEMPLATE), Ingredient.of(Items.NETHERITE_CHESTPLATE), Ingredient.of(Items.LIME_DYE), RecipeCategory.COMBAT, ModItems.ventus_Chestplate.get())
        .unlocks("has_keyblade_armor", InventoryChangeTrigger.TriggerInstance.hasItems(Items.NETHERITE_CHESTPLATE))
        .save(consumer, KingdomKeys.rl("keyblade_armor_ventus_chestplate_smithing"));

		SmithingTransformRecipeBuilder.smithing(Ingredient.of(Items.NETHERITE_UPGRADE_SMITHING_TEMPLATE), Ingredient.of(Items.NETHERITE_LEGGINGS), Ingredient.of(Items.LIME_DYE), RecipeCategory.COMBAT, ModItems.ventus_Leggings.get())
        .unlocks("has_keyblade_armor", InventoryChangeTrigger.TriggerInstance.hasItems(Items.NETHERITE_LEGGINGS))
        .save(consumer, KingdomKeys.rl("keyblade_armor_ventus_leggings_smithing"));

		SmithingTransformRecipeBuilder.smithing(Ingredient.of(Items.NETHERITE_UPGRADE_SMITHING_TEMPLATE), Ingredient.of(Items.NETHERITE_BOOTS), Ingredient.of(Items.LIME_DYE), RecipeCategory.COMBAT, ModItems.ventus_Boots.get())
        .unlocks("has_keyblade_armor", InventoryChangeTrigger.TriggerInstance.hasItems(Items.NETHERITE_BOOTS))
        .save(consumer, KingdomKeys.rl("keyblade_armor_ventus_boots_smithing"));

    	//Nightmare Ventus
		ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.nightmareVentus_Shoulder.get())
				.define('B', ItemTags.BUTTONS)
				.define('I', Items.IRON_INGOT)
				.define('D', Items.BLACK_DYE)
				.define('G', Items.GOLD_INGOT)
				.pattern("DGD")
				.pattern("IBI")
				.pattern("DGD")
				.group(KingdomKeys.MODID)
				.unlockedBy("nightmareventus_shoulder", InventoryChangeTrigger.TriggerInstance.hasItems(Blocks.STONE))
				.save(consumer);

		SmithingTransformRecipeBuilder.smithing(Ingredient.of(Items.NETHERITE_UPGRADE_SMITHING_TEMPLATE), Ingredient.of(Items.NETHERITE_HELMET), Ingredient.of(Items.BLACK_DYE), RecipeCategory.COMBAT, ModItems.nightmareVentus_Helmet.get())
        .unlocks("has_keyblade_armor", InventoryChangeTrigger.TriggerInstance.hasItems(Items.NETHERITE_HELMET))
        .save(consumer, KingdomKeys.rl("keyblade_armor_nightmare_ventus_helmet_smithing"));

		SmithingTransformRecipeBuilder.smithing(Ingredient.of(Items.NETHERITE_UPGRADE_SMITHING_TEMPLATE), Ingredient.of(Items.NETHERITE_CHESTPLATE), Ingredient.of(Items.BLACK_DYE), RecipeCategory.COMBAT, ModItems.nightmareVentus_Chestplate.get())
        .unlocks("has_keyblade_armor", InventoryChangeTrigger.TriggerInstance.hasItems(Items.NETHERITE_CHESTPLATE))
        .save(consumer, KingdomKeys.rl("keyblade_armor_nightmare_ventus_chestplate_smithing"));

		SmithingTransformRecipeBuilder.smithing(Ingredient.of(Items.NETHERITE_UPGRADE_SMITHING_TEMPLATE), Ingredient.of(Items.NETHERITE_LEGGINGS), Ingredient.of(Items.BLACK_DYE), RecipeCategory.COMBAT, ModItems.nightmareVentus_Leggings.get())
        .unlocks("has_keyblade_armor", InventoryChangeTrigger.TriggerInstance.hasItems(Items.NETHERITE_LEGGINGS))
        .save(consumer, KingdomKeys.rl("keyblade_armor_nightmare_ventus_leggings_smithing"));

		SmithingTransformRecipeBuilder.smithing(Ingredient.of(Items.NETHERITE_UPGRADE_SMITHING_TEMPLATE), Ingredient.of(Items.NETHERITE_BOOTS), Ingredient.of(Items.BLACK_DYE), RecipeCategory.COMBAT, ModItems.nightmareVentus_Boots.get())
        .unlocks("has_keyblade_armor", InventoryChangeTrigger.TriggerInstance.hasItems(Items.NETHERITE_BOOTS))
        .save(consumer, KingdomKeys.rl("keyblade_armor_nightmare_ventus_boots_smithing"));

    	//Eraqus
		ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.eraqus_Shoulder.get())
				.define('B', ItemTags.BUTTONS)
				.define('I', Items.IRON_INGOT)
				.define('D', Items.WHITE_DYE)
				.define('G', Items.GOLD_INGOT)
				.pattern("DGD")
				.pattern("IBI")
				.pattern("DGD")
				.group(KingdomKeys.MODID)
				.unlockedBy("eraqus_shoulder", InventoryChangeTrigger.TriggerInstance.hasItems(Blocks.STONE))
				.save(consumer);

		SmithingTransformRecipeBuilder.smithing(Ingredient.of(Items.NETHERITE_UPGRADE_SMITHING_TEMPLATE), Ingredient.of(Items.NETHERITE_HELMET), Ingredient.of(Items.WHITE_DYE), RecipeCategory.COMBAT, ModItems.eraqus_Helmet.get())
        .unlocks("has_keyblade_armor", InventoryChangeTrigger.TriggerInstance.hasItems(Items.NETHERITE_HELMET))
        .save(consumer, KingdomKeys.rl("keyblade_armor_eraqus_helmet_smithing"));

		SmithingTransformRecipeBuilder.smithing(Ingredient.of(Items.NETHERITE_UPGRADE_SMITHING_TEMPLATE), Ingredient.of(Items.NETHERITE_CHESTPLATE), Ingredient.of(Items.WHITE_DYE), RecipeCategory.COMBAT, ModItems.eraqus_Chestplate.get())
        .unlocks("has_keyblade_armor", InventoryChangeTrigger.TriggerInstance.hasItems(Items.NETHERITE_CHESTPLATE))
        .save(consumer, KingdomKeys.rl("keyblade_armor_eraqus_chestplate_smithing"));

		SmithingTransformRecipeBuilder.smithing(Ingredient.of(Items.NETHERITE_UPGRADE_SMITHING_TEMPLATE), Ingredient.of(Items.NETHERITE_LEGGINGS), Ingredient.of(Items.WHITE_DYE), RecipeCategory.COMBAT, ModItems.eraqus_Leggings.get())
        .unlocks("has_keyblade_armor", InventoryChangeTrigger.TriggerInstance.hasItems(Items.NETHERITE_LEGGINGS))
        .save(consumer, KingdomKeys.rl("keyblade_armor_eraqus_leggings_smithing"));

		SmithingTransformRecipeBuilder.smithing(Ingredient.of(Items.NETHERITE_UPGRADE_SMITHING_TEMPLATE), Ingredient.of(Items.NETHERITE_BOOTS), Ingredient.of(Items.WHITE_DYE), RecipeCategory.COMBAT, ModItems.eraqus_Boots.get())
        .unlocks("has_keyblade_armor", InventoryChangeTrigger.TriggerInstance.hasItems(Items.NETHERITE_BOOTS))
        .save(consumer, KingdomKeys.rl("keyblade_armor_eraqus_boots_smithing"));

    	//Xehanort
		ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.xehanort_Shoulder.get())
				.define('B', ItemTags.BUTTONS)
				.define('I', Items.IRON_INGOT)
				.define('D', Items.GRAY_DYE)
				.define('G', Items.GOLD_INGOT)
				.pattern("DGD")
				.pattern("IBI")
				.pattern("DGD")
				.group(KingdomKeys.MODID)
				.unlockedBy("xehanort_shoulder", InventoryChangeTrigger.TriggerInstance.hasItems(Blocks.STONE))
				.save(consumer);

		SmithingTransformRecipeBuilder.smithing(Ingredient.of(Items.NETHERITE_UPGRADE_SMITHING_TEMPLATE), Ingredient.of(Items.NETHERITE_HELMET), Ingredient.of(Items.GRAY_DYE), RecipeCategory.COMBAT, ModItems.xehanort_Helmet.get())
        .unlocks("has_keyblade_armor", InventoryChangeTrigger.TriggerInstance.hasItems(Items.NETHERITE_HELMET))
        .save(consumer, KingdomKeys.rl("keyblade_armor_xehanort_helmet_smithing"));

		SmithingTransformRecipeBuilder.smithing(Ingredient.of(Items.NETHERITE_UPGRADE_SMITHING_TEMPLATE), Ingredient.of(Items.NETHERITE_CHESTPLATE), Ingredient.of(Items.GRAY_DYE), RecipeCategory.COMBAT, ModItems.xehanort_Chestplate.get())
        .unlocks("has_keyblade_armor", InventoryChangeTrigger.TriggerInstance.hasItems(Items.NETHERITE_CHESTPLATE))
        .save(consumer, KingdomKeys.rl("keyblade_armor_xehanort_chestplate_smithing"));

		SmithingTransformRecipeBuilder.smithing(Ingredient.of(Items.NETHERITE_UPGRADE_SMITHING_TEMPLATE), Ingredient.of(Items.NETHERITE_LEGGINGS), Ingredient.of(Items.GRAY_DYE), RecipeCategory.COMBAT, ModItems.xehanort_Leggings.get())
        .unlocks("has_keyblade_armor", InventoryChangeTrigger.TriggerInstance.hasItems(Items.NETHERITE_LEGGINGS))
        .save(consumer, KingdomKeys.rl("keyblade_armor_xehanort_leggings_smithing"));

		SmithingTransformRecipeBuilder.smithing(Ingredient.of(Items.NETHERITE_UPGRADE_SMITHING_TEMPLATE), Ingredient.of(Items.NETHERITE_BOOTS), Ingredient.of(Items.GRAY_DYE), RecipeCategory.COMBAT, ModItems.xehanort_Boots.get())
        .unlocks("has_keyblade_armor", InventoryChangeTrigger.TriggerInstance.hasItems(Items.NETHERITE_BOOTS))
        .save(consumer, KingdomKeys.rl("keyblade_armor_xehanort_boots_smithing"));

		/*ShapelessRecipeBuilder.shapeless(RecipeCategory.COMBAT, ModItems.firaSpell.get())
				.requires(ModItems.fireSpell.get(), 2)
				.unlockedBy("fira_spell", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.fireSpell.get()))
				.save(consumer);

		ShapelessRecipeBuilder.shapeless(RecipeCategory.COMBAT, ModItems.firagaSpell.get())
				.requires(ModItems.firaSpell.get(), 2)
				.unlockedBy("fira_spell", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.firaSpell.get()))
				.save(consumer);

		ShapelessRecipeBuilder.shapeless(RecipeCategory.COMBAT, ModItems.blizzaraSpell.get())
				.requires(ModItems.blizzardSpell.get(), 2)
				.unlockedBy("blizzara_spell", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.blizzardSpell.get()))
				.save(consumer);

		ShapelessRecipeBuilder.shapeless(RecipeCategory.COMBAT, ModItems.blizzagaSpell.get())
				.requires(ModItems.blizzaraSpell.get(), 2)
				.unlockedBy("blizzaga_spell", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.blizzaraSpell.get()))
				.save(consumer);

		ShapelessRecipeBuilder.shapeless(RecipeCategory.COMBAT, ModItems.wateraSpell.get())
				.requires(ModItems.waterSpell.get(), 2)
				.unlockedBy("watera_spell", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.waterSpell.get()))
				.save(consumer);

		ShapelessRecipeBuilder.shapeless(RecipeCategory.COMBAT, ModItems.watergaSpell.get())
				.requires(ModItems.wateraSpell.get(), 2)
				.unlockedBy("waterga_spell", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.wateraSpell.get()))
				.save(consumer);

		ShapelessRecipeBuilder.shapeless(RecipeCategory.COMBAT, ModItems.thundaraSpell.get())
				.requires(ModItems.thunderSpell.get(), 2)
				.unlockedBy("thundara_spell", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.thunderSpell.get()))
				.save(consumer);

		ShapelessRecipeBuilder.shapeless(RecipeCategory.COMBAT, ModItems.thundagaSpell.get())
				.requires(ModItems.thundaraSpell.get(), 2)
				.unlockedBy("thundaga_spell", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.thundaraSpell.get()))
				.save(consumer);

		ShapelessRecipeBuilder.shapeless(RecipeCategory.COMBAT, ModItems.curaSpell.get())
				.requires(ModItems.cureSpell.get(), 2)
				.unlockedBy("cura_spell", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.cureSpell.get()))
				.save(consumer);

		ShapelessRecipeBuilder.shapeless(RecipeCategory.COMBAT, ModItems.curagaSpell.get())
				.requires(ModItems.curaSpell.get(), 2)
				.unlockedBy("curaga_spell", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.curaSpell.get()))
				.save(consumer);

		ShapelessRecipeBuilder.shapeless(RecipeCategory.COMBAT, ModItems.aeroraSpell.get())
				.requires(ModItems.aeroSpell.get(), 2)
				.unlockedBy("aerora_spell", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.aeroSpell.get()))
				.save(consumer);

		ShapelessRecipeBuilder.shapeless(RecipeCategory.COMBAT, ModItems.aerogaSpell.get())
				.requires(ModItems.aeroraSpell.get(), 2)
				.unlockedBy("aeroga_spell", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.aeroraSpell.get()))
				.save(consumer);

		ShapelessRecipeBuilder.shapeless(RecipeCategory.COMBAT, ModItems.magneraSpell.get())
				.requires(ModItems.magnetSpell.get(), 2)
				.unlockedBy("magnera_spell", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.magnetSpell.get()))
				.save(consumer);

		ShapelessRecipeBuilder.shapeless(RecipeCategory.COMBAT, ModItems.magnegaSpell.get())
				.requires(ModItems.magneraSpell.get(), 2)
				.unlockedBy("magnega_spell", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.magneraSpell.get()))
				.save(consumer);

		ShapelessRecipeBuilder.shapeless(RecipeCategory.COMBAT, ModItems.refleraSpell.get())
				.requires(ModItems.reflectSpell.get(), 2)
				.unlockedBy("reflera_spell", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.reflectSpell.get()))
				.save(consumer);

		ShapelessRecipeBuilder.shapeless(RecipeCategory.COMBAT, ModItems.reflegaSpell.get())
				.requires(ModItems.refleraSpell.get(), 2)
				.unlockedBy("reflega_spell", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.refleraSpell.get()))
				.save(consumer);

		ShapelessRecipeBuilder.shapeless(RecipeCategory.COMBAT, ModItems.graviraSpell.get())
				.requires(ModItems.gravitySpell.get(), 2)
				.unlockedBy("gravira_spell", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.gravitySpell.get()))
				.save(consumer);

		ShapelessRecipeBuilder.shapeless(RecipeCategory.COMBAT, ModItems.gravigaSpell.get())
				.requires(ModItems.graviraSpell.get(), 2)
				.unlockedBy("graviga_spell", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.graviraSpell.get()))
				.save(consumer);

		ShapelessRecipeBuilder.shapeless(RecipeCategory.COMBAT, ModItems.stopraSpell.get())
				.requires(ModItems.stopSpell.get(), 2)
				.unlockedBy("stopra_spell", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.stopSpell.get()))
				.save(consumer);

		ShapelessRecipeBuilder.shapeless(RecipeCategory.COMBAT, ModItems.stopgaSpell.get())
				.requires(ModItems.stopraSpell.get(), 2)
				.unlockedBy("stopga_spell", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.stopraSpell.get()))
				.save(consumer);
*/

        // blox
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, ModBlocks.normalBlox.get())
                .define('S', Tags.Items.STONES)
                .define('N', Items.DIRT)
                .pattern("NS")
                .pattern("SN")
                .group(KingdomKeys.MODID)
                .unlockedBy("stone", InventoryChangeTrigger.TriggerInstance.hasItems(Blocks.STONE))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, ModBlocks.dangerBlox.get())
                .pattern("NC")
                .pattern("CN")
                .define('C', Blocks.CACTUS)
                .define('N', ModBlocks.normalBlox.get())
                .group(KingdomKeys.MODID)
                .unlockedBy("normalblox", InventoryChangeTrigger.TriggerInstance.hasItems(ModBlocks.normalBlox.get()))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, ModBlocks.hardBlox.get())
                .pattern("NS")
                .pattern("SN")
                .define('S', Tags.Items.STONES)
                .define('N', ModBlocks.normalBlox.get())
                .group(KingdomKeys.MODID)
                .unlockedBy("normalblox", InventoryChangeTrigger.TriggerInstance.hasItems(ModBlocks.normalBlox.get()))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, ModBlocks.metalBlox.get())
                .pattern("HI")
                .pattern("IH")
                .define('I', Tags.Items.INGOTS_IRON)
                .define('H', ModBlocks.hardBlox.get())
                .group(KingdomKeys.MODID)
                .unlockedBy("hardblox", InventoryChangeTrigger.TriggerInstance.hasItems(ModBlocks.hardBlox.get()))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, ModBlocks.ghostBlox.get())
                .pattern("GNG")
                .pattern("GRG")
                .pattern("GNG")
                .define('G', Tags.Items.GLASS_BLOCKS)
                .define('N', ModBlocks.normalBlox.get())
                .define('R', Blocks.REDSTONE_BLOCK)
                .group(KingdomKeys.MODID)
                .unlockedBy("normalblox", InventoryChangeTrigger.TriggerInstance.hasItems(ModBlocks.normalBlox.get()))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, ModBlocks.magnetBlox.get())
                .pattern("RIR")
                .pattern("GBG")
                .pattern("RIR")
                .define('I', Tags.Items.INGOTS_IRON)
                .define('B', ModBlocks.normalBlox.get())
                .define('R', Tags.Items.DUSTS_REDSTONE)
                .define('G', Tags.Items.INGOTS_GOLD)
                .group(KingdomKeys.MODID)
                .unlockedBy("normalblox", InventoryChangeTrigger.TriggerInstance.hasItems(ModBlocks.normalBlox.get()))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, ModBlocks.blastBlox.get())
                .pattern("NLN")
                .pattern("NTN")
                .pattern("NTN")
                .define('T', Blocks.TNT)
                .define('N', ModBlocks.normalBlox.get())
                .define('L', Items.LAVA_BUCKET)
                .group(KingdomKeys.MODID)
                .unlockedBy("normalblox", InventoryChangeTrigger.TriggerInstance.hasItems(ModBlocks.normalBlox.get()))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, ModBlocks.bounceBlox.get())
                .pattern("NNN")
                .pattern("NSN")
                .pattern("NNN")
                .define('S', Blocks.SLIME_BLOCK)
                .define('N', ModBlocks.normalBlox.get())
                .group(KingdomKeys.MODID)
                .unlockedBy("normalblox", InventoryChangeTrigger.TriggerInstance.hasItems(ModBlocks.normalBlox.get()))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, ModBlocks.mosaic_stained_glass.get(), 4)
                .pattern("DGD")
                .pattern("GIG")
                .pattern("DGD")
                .define('D', Tags.Items.DYES)
                .define('G', Blocks.GLASS)
                .define('I', Tags.Items.INGOTS_IRON)
                .group(KingdomKeys.MODID)
                .unlockedBy("glass", InventoryChangeTrigger.TriggerInstance.hasItems(Blocks.GLASS))
                .save(consumer);

		ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, ModBlocks.airstepTarget.get(), 1)
				.pattern(" G ")
				.pattern("GEG")
				.pattern(" G ")
				.define('E', Items.ENDER_PEARL)
				.define('G', Items.GLOWSTONE)
				.group(KingdomKeys.MODID)
				.unlockedBy("ender_pearl", InventoryChangeTrigger.TriggerInstance.hasItems(Items.ENDER_PEARL))
				.save(consumer);

		ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, ModBlocks.savepoint.get(), 1)
				.pattern("GEG")
				.pattern("EGE")
				.pattern("GEG")
				.define('E', Items.ENDER_PEARL)
				.define('G', Items.GLOWSTONE)
				.group(KingdomKeys.MODID)
				.unlockedBy("ender_pearl", InventoryChangeTrigger.TriggerInstance.hasItems(Items.ENDER_PEARL))
				.save(consumer);

		ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, ModBlocks.gummiHangar.get())
				.define('I', Tags.Items.INGOTS_IRON)
				.define('Q', Items.QUARTZ)
				.define('R', Blocks.REDSTONE_BLOCK)
				.define('E', Items.ENDER_EYE)
				.define('C', Blocks.CRAFTER)
				.pattern("IEI")
				.pattern("QCQ")
				.pattern("IRI")
				.group(KingdomKeys.MODID)
				.unlockedBy("crafter", InventoryChangeTrigger.TriggerInstance.hasItems(Blocks.CRAFTER))
				.save(consumer);

		ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, ModItems.gummiShipBlueprint.get())
				.define('G', Blocks.GLASS)
				.define('F', ModItems.gummiMeteorFragment.get())
				.define('R', Blocks.REDSTONE_BLOCK)
				.pattern("GGG")
				.pattern("FRF")
				.pattern("GGG")
				.group(KingdomKeys.MODID)
				.unlockedBy("fragment", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.gummiMeteorFragment.get()))
				.save(consumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, ModBlocks.gummiCore.get())
                .define('B', Items.BLAZE_POWDER)
                .define('G', ModItems.gummiMeteorFragment.get())
                .define('D', Blocks.DIAMOND_BLOCK)
                .define('E', Items.ENDER_EYE)
                .pattern("EBE")
                .pattern("GDG")
                .pattern("BGB")
                .group(KingdomKeys.MODID)
                .unlockedBy("ender_eye", InventoryChangeTrigger.TriggerInstance.hasItems(Items.ENDER_EYE))
                .save(consumer);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.BUILDING_BLOCKS, ModBlocks.gummiFire.get())
                .requires(Blocks.DISPENSER)
                .requires(ModItems.gummiMeteorFragment.get())
                .requires(ModItems.fireSpell.get())
                .unlockedBy("gummi_fire", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.fireSpell.get()))
                .save(consumer);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.BUILDING_BLOCKS, ModBlocks.gummiFira.get())
                .requires(Blocks.DISPENSER)
                .requires(ModItems.gummiMeteorFragment.get())
                .requires(ModItems.fireSpell.get(), 2)
                .unlockedBy("gummi_fira", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.fireSpell.get()))
                .save(consumer);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.BUILDING_BLOCKS, ModBlocks.gummiBlizzard.get())
                .requires(Blocks.DISPENSER)
                .requires(ModItems.gummiMeteorFragment.get())
                .requires(ModItems.blizzardSpell.get())
                .unlockedBy("gummi_blizzard", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.blizzardSpell.get()))
                .save(consumer);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.BUILDING_BLOCKS, ModBlocks.gummiBlizzara.get())
                .requires(Blocks.DISPENSER)
                .requires(ModItems.gummiMeteorFragment.get())
                .requires(ModItems.blizzardSpell.get(), 2)
                .unlockedBy("gummi_blizzara", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.blizzardSpell.get()))
                .save(consumer);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.BUILDING_BLOCKS, ModBlocks.gummiGravity.get())
                .requires(Blocks.DISPENSER)
                .requires(ModItems.gummiMeteorFragment.get())
                .requires(ModItems.gravitySpell.get())
                .unlockedBy("gummi_gravity", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.gravitySpell.get()))
                .save(consumer);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.BUILDING_BLOCKS, ModBlocks.gummiGravira.get())
                .requires(Blocks.DISPENSER)
                .requires(ModItems.gummiMeteorFragment.get())
                .requires(ModItems.gravitySpell.get(), 2)
                .unlockedBy("gummi_gravira", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.gravitySpell.get()))
                .save(consumer);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.BUILDING_BLOCKS, ModBlocks.gummiWater.get())
                .requires(Blocks.DISPENSER)
                .requires(ModItems.gummiMeteorFragment.get())
                .requires(ModItems.waterSpell.get())
                .unlockedBy("gummi_water", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.waterSpell.get()))
                .save(consumer);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.BUILDING_BLOCKS, ModBlocks.gummiWatera.get())
                .requires(Blocks.DISPENSER)
                .requires(ModItems.gummiMeteorFragment.get())
                .requires(ModItems.waterSpell.get(), 2)
                .unlockedBy("gummi_watera", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.waterSpell.get()))
                .save(consumer);

        //Engines
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, ModBlocks.gummiVernier.get())
                .pattern("P")
                .pattern("F")
                .pattern("G")
                .define('F',Blocks.FURNACE)
                .define('G',ModItems.gummiMeteorFragment.get())
                .define('P', Blocks.PISTON)
                .unlockedBy("gummi_vernier", InventoryChangeTrigger.TriggerInstance.hasItems(Blocks.FURNACE))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, ModBlocks.gummiThruster.get())
                .pattern("PP")
                .pattern("FF")
                .pattern("GG")
                .define('F',Blocks.FURNACE)
                .define('G',ModItems.gummiMeteorFragment.get())
                .define('P', Blocks.PISTON)
                .unlockedBy("gummi_thruster", InventoryChangeTrigger.TriggerInstance.hasItems(Blocks.FURNACE))
                .save(consumer);


        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, ModBlocks.gummiBubbleHelms.getFirst().get())
                .pattern("GGG")
                .pattern("GCG")
                .pattern("FFF")
                .define('F', ModItems.gummiMeteorFragment.get())
                .define('C', ModItems.cureSpell.get())
                .define('G', Blocks.GLASS)
                .group(KingdomKeys.MODID)
                .unlockedBy("gummi_cure", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.cureSpell.get()))
                .save(consumer);

        //TODO rest of gummi weapons

        //Items
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, ModItems.wayfinder.get(), 1)
	        .pattern("GEG")
	        .pattern("ECE")
	        .pattern("GEG")
	        .define('E', Items.ENDER_PEARL)
	        .define('C', Items.COMPASS)
	        .define('G', ModBlocks.mosaic_stained_glass.get())
	        .group(KingdomKeys.MODID)
	        .unlockedBy("compass", InventoryChangeTrigger.TriggerInstance.hasItems(Items.COMPASS))
	        .save(consumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ModItems.struggleSword.get())
	        .pattern(" W ")
	        .pattern(" W ")
	        .pattern(" S ")
	        .define('S', ModItems.woodenStick.get())
	        .define('W', Blocks.BLUE_WOOL)
	        .group(KingdomKeys.MODID)
	        .unlockedBy("struggle_sword", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.woodenStick.get()))
	        .save(consumer);
        
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ModItems.struggleWand.get())
	        .pattern(" W ")
	        .pattern(" S ")
	        .pattern(" S ")
	        .define('S', ModItems.woodenStick.get())
	        .define('W', Blocks.BLUE_WOOL)
	        .group(KingdomKeys.MODID)
	        .unlockedBy("struggle_wand", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.woodenStick.get()))
	        .save(consumer);
        
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ModItems.struggleHammer.get())
	        .pattern(" W ")
	        .pattern(" WS")
	        .pattern(" S ")
	        .define('S', ModItems.woodenStick.get())
	        .define('W', Blocks.BLUE_WOOL)
	        .group(KingdomKeys.MODID)
	        .unlockedBy("struggle_hammer", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.woodenStick.get()))
	        .save(consumer);

		ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ModItems.trainingDummy.get())
			.pattern(" W ")
			.pattern("SHS")
			.pattern(" A ")
			.define('S', ModItems.woodenStick.get())
			.define('W', Items.WHEAT)
			.define('A', Items.ARMOR_STAND)
			.define('H', Blocks.HAY_BLOCK)
			.group(KingdomKeys.MODID)
			.unlockedBy("training_dummy", InventoryChangeTrigger.TriggerInstance.hasItems(Blocks.HAY_BLOCK))
			.save(consumer);


		addRecipeUpgrade(consumer, ModItems.recipeD.get(), ModItems.recipeC.get(), 2, "recipe_d");
		addRecipeUpgrade(consumer, ModItems.recipeD.get(), ModItems.recipeB.get(), 4, "recipe_d");
		addRecipeUpgrade(consumer, ModItems.recipeD.get(), ModItems.recipeA.get(), 8, "recipe_d");

		addRecipeUpgrade(consumer, ModItems.recipeC.get(), ModItems.recipeB.get(), 2, "recipe_c");
		addRecipeUpgrade(consumer, ModItems.recipeC.get(), ModItems.recipeA.get(), 4, "recipe_c");
		addRecipeUpgrade(consumer, ModItems.recipeC.get(), ModItems.recipeS.get(), 8, "recipe_c");

		addRecipeUpgrade(consumer, ModItems.recipeB.get(), ModItems.recipeA.get(), 2, "recipe_b");
		addRecipeUpgrade(consumer, ModItems.recipeB.get(), ModItems.recipeS.get(), 4, "recipe_b");
		addRecipeUpgrade(consumer, ModItems.recipeB.get(), ModItems.recipeSS.get(), 8, "recipe_b");

		addRecipeUpgrade(consumer, ModItems.recipeA.get(), ModItems.recipeS.get(), 2, "recipe_a");
		addRecipeUpgrade(consumer, ModItems.recipeA.get(), ModItems.recipeSS.get(), 4, "recipe_a");
		addRecipeUpgrade(consumer, ModItems.recipeA.get(), ModItems.recipeSSS.get(), 8, "recipe_a");

		addRecipeUpgrade(consumer, ModItems.recipeS.get(), ModItems.recipeSS.get(), 2, "recipe_s");
		addRecipeUpgrade(consumer, ModItems.recipeS.get(), ModItems.recipeSSS.get(), 4, "recipe_s");

		addRecipeUpgrade(consumer, ModItems.recipeSS.get(), ModItems.recipeSSS.get(), 2, "recipe_ss");

		//Downgrades
		addRecipeUpgrade(consumer, ModItems.recipeSSS.get(), ModItems.recipeSS.get(), 1, "recipe_sss");
		addRecipeUpgrade(consumer, ModItems.recipeSS.get(), ModItems.recipeS.get(), 1, "recipe_ss");
		addRecipeUpgrade(consumer, ModItems.recipeS.get(), ModItems.recipeA.get(), 1, "recipe_s");
		addRecipeUpgrade(consumer, ModItems.recipeA.get(), ModItems.recipeB.get(), 1, "recipe_a");
		addRecipeUpgrade(consumer, ModItems.recipeB.get(), ModItems.recipeC.get(), 1, "recipe_b");
		addRecipeUpgrade(consumer, ModItems.recipeC.get(), ModItems.recipeD.get(), 1, "recipe_c");
		
        ShapelessRecipeBuilder.shapeless(RecipeCategory.FOOD, ModItems.iceCream.get(), 3)
                .requires(Tags.Items.RODS_WOODEN)
                .requires(Items.SUGAR)
                .requires(Items.WATER_BUCKET)
                .requires(Blocks.ICE)
                .unlockedBy("ice", InventoryChangeTrigger.TriggerInstance.hasItems(Blocks.ICE))
                .save(consumer);

		ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, ModItems.synthesisBag.get())
				.pattern("LSL")
				.pattern("LDL")
				.pattern("LLL")
				.define('S', Tags.Items.STRINGS)
				.define('L', Tags.Items.LEATHERS)
				.define('D', Items.ORANGE_DYE)
				.group(KingdomKeys.MODID)
				.unlockedBy("leather", InventoryChangeTrigger.TriggerInstance.hasItems(Items.LEATHER))
				.save(consumer);

		ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, ModItems.magicsBag.get())
				.pattern("LSL")
				.pattern("LDL")
				.pattern("LLL")
				.define('S', Tags.Items.STRINGS)
				.define('L', Tags.Items.LEATHERS)
				.define('D', Items.PURPLE_DYE)
				.group(KingdomKeys.MODID)
				.unlockedBy("leather", InventoryChangeTrigger.TriggerInstance.hasItems(Items.LEATHER))
				.save(consumer);

		ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, ModItems.cardsBag.get())
				.pattern("LSL")
				.pattern("LRL")
				.pattern("LLL")
				.define('S', Tags.Items.STRINGS)
				.define('L', Tags.Items.LEATHERS)
				.define('R', Items.RED_DYE)
				.group(KingdomKeys.MODID)
				.unlockedBy("leather", InventoryChangeTrigger.TriggerInstance.hasItems(Items.LEATHER))
				.save(consumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ModItems.woodenKeyblade.get())
                .pattern(" WS")
                .pattern(" WS")
                .pattern(" S ")
                .define('S', Items.STICK)
                .define('W', ItemTags.PLANKS)
                .group(KingdomKeys.MODID)
                .unlockedBy("stick", InventoryChangeTrigger.TriggerInstance.hasItems(Items.STICK))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.woodenStick.get())
                .pattern("S")
                .pattern("S")
                .pattern("S")
                .define('S', Items.STICK)
                .group(KingdomKeys.MODID)
                .unlockedBy("stick", InventoryChangeTrigger.TriggerInstance.hasItems(Items.STICK))
                .save(consumer);

        //Armour TODO add some items specifically for crafting these so the recipes make a bit more sense
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ModItems.organizationRobe_Helmet.get())
                .pattern("LBL")
                .pattern("EAE")
                .define('B', Tags.Items.DYES_BLACK)
                .define('E', Tags.Items.ENDER_PEARLS)
                .define('A', Items.LEATHER_HELMET)
                .define('L', Tags.Items.LEATHERS)
                .group(KingdomKeys.MODID)
                .unlockedBy("ender_pearl", InventoryChangeTrigger.TriggerInstance.hasItems(Items.ENDER_PEARL))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ModItems.organizationRobe_Chestplate.get())
                .pattern("LAL")
                .pattern("EBE")
                .pattern("LLL")
                .define('L', Tags.Items.LEATHERS)
                .define('A', Items.LEATHER_CHESTPLATE)
                .define('E', Tags.Items.ENDER_PEARLS)
                .define('B', Tags.Items.DYES_BLACK)
                .group(KingdomKeys.MODID)
                .unlockedBy("ender_pearl", InventoryChangeTrigger.TriggerInstance.hasItems(Items.ENDER_PEARL))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ModItems.organizationRobe_Leggings.get())
                .pattern("LBL")
                .pattern("EAE")
                .pattern("L L")
                .define('L', Tags.Items.LEATHERS)
                .define('A', Items.LEATHER_LEGGINGS)
                .define('E', Tags.Items.ENDER_PEARLS)
                .define('B', Tags.Items.DYES_BLACK)
                .group(KingdomKeys.MODID)
                .unlockedBy("ender_pearl", InventoryChangeTrigger.TriggerInstance.hasItems(Items.ENDER_PEARL))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ModItems.organizationRobe_Boots.get())
                .pattern("EBE")
                .pattern("LAL")
                .define('L', Tags.Items.LEATHERS)
                .define('A', Items.LEATHER_BOOTS)
                .define('E', Tags.Items.ENDER_PEARLS)
                .define('B', Tags.Items.DYES_BLACK)
                .group(KingdomKeys.MODID)
                .unlockedBy("ender_pearl", InventoryChangeTrigger.TriggerInstance.hasItems(Items.ENDER_PEARL))
                .save(consumer);

        

        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ModItems.xemnas_Helmet.get())
                .pattern("WAW")
                .pattern("B B")
                .define('W', Tags.Items.DYES_WHITE)
                .define('B', Tags.Items.DYES_BLACK)
                .define('A', ModItems.organizationRobe_Helmet.get())
                .group(KingdomKeys.MODID)
                .unlockedBy("organization_helmet", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.organizationRobe_Helmet.get()))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ModItems.xemnas_Chestplate.get())
                .pattern("B B")
                .pattern("WAW")
                .pattern("BWB")
                .define('A', ModItems.organizationRobe_Chestplate.get())
                .define('W', Tags.Items.DYES_WHITE)
                .define('B', Tags.Items.DYES_BLACK)
                .group(KingdomKeys.MODID)
                .unlockedBy("organization_chestplate", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.organizationRobe_Chestplate.get()))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ModItems.xemnas_Leggings.get())
                .pattern("WAW")
                .pattern("B B")
                .pattern("W W")
                .define('A', ModItems.organizationRobe_Leggings.get())
                .define('W', Tags.Items.DYES_WHITE)
                .define('B', Tags.Items.DYES_BLACK)
                .group(KingdomKeys.MODID)
                .unlockedBy("organization_leggings", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.organizationRobe_Leggings.get()))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ModItems.xemnas_Boots.get())
                .pattern("B B")
                .pattern("WAW")
                .define('A', ModItems.organizationRobe_Boots.get())
                .define('W', Tags.Items.DYES_WHITE)
                .define('B', Tags.Items.DYES_BLACK)
                .group(KingdomKeys.MODID)
                .unlockedBy("organization_boots", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.organizationRobe_Boots.get()))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ModItems.antiCoat_Helmet.get())
                .pattern("WAW")
                .pattern("B B")
                .define('W', Tags.Items.DYES_PURPLE)
                .define('B', Tags.Items.DYES_BLACK)
                .define('A', ModItems.organizationRobe_Helmet.get())
                .group(KingdomKeys.MODID)
                .unlockedBy("organization_helmet", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.organizationRobe_Helmet.get()))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ModItems.antiCoat_Chestplate.get())
                .pattern("B B")
                .pattern("WAW")
                .pattern("BWB")
                .define('A', ModItems.organizationRobe_Chestplate.get())
                .define('W', Tags.Items.DYES_PURPLE)
                .define('B', Tags.Items.DYES_BLACK)
                .group(KingdomKeys.MODID)
                .unlockedBy("organization_chestplate", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.organizationRobe_Chestplate.get()))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ModItems.antiCoat_Leggings.get())
                .pattern("WAW")
                .pattern("B B")
                .pattern("W W")
                .define('A', ModItems.organizationRobe_Leggings.get())
                .define('W', Tags.Items.DYES_PURPLE)
                .define('B', Tags.Items.DYES_BLACK)
                .group(KingdomKeys.MODID)
                .unlockedBy("organization_leggings", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.organizationRobe_Leggings.get()))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ModItems.antiCoat_Boots.get())
                .pattern("B B")
                .pattern("WAW")
                .define('A', ModItems.organizationRobe_Boots.get())
                .define('W', Tags.Items.DYES_PURPLE)
                .define('B', Tags.Items.DYES_BLACK)
                .group(KingdomKeys.MODID)
                .unlockedBy("organization_boots", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.organizationRobe_Boots.get()))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ModItems.vanitas_Helmet.get())
                .pattern("LBL")
                .pattern("EAE")
                .define('B', Tags.Items.DYES_BLACK)
                .define('E', Items.GHAST_TEAR)
                .define('A', Items.LEATHER_HELMET)
                .define('L', Tags.Items.DYES_RED)
                .group(KingdomKeys.MODID)
                .unlockedBy("ghast_tear", InventoryChangeTrigger.TriggerInstance.hasItems(Items.GHAST_TEAR))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ModItems.vanitas_Chestplate.get())
                .pattern("LAL")
                .pattern("EBE")
                .pattern("LLL")
                .define('B', Tags.Items.DYES_BLACK)
                .define('E', Items.GHAST_TEAR)
                .define('A', Items.LEATHER_CHESTPLATE)
                .define('L', Tags.Items.DYES_RED)
                .group(KingdomKeys.MODID)
                .unlockedBy("ghast_tear", InventoryChangeTrigger.TriggerInstance.hasItems(Items.GHAST_TEAR))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ModItems.vanitas_Leggings.get())
                .pattern("LBL")
                .pattern("EAE")
                .pattern("L L")
                .define('B', Tags.Items.DYES_BLACK)
                .define('E', Items.GHAST_TEAR)
                .define('A', Items.LEATHER_LEGGINGS)
                .define('L', Tags.Items.DYES_RED)
                .group(KingdomKeys.MODID)
                .unlockedBy("ghast_tear", InventoryChangeTrigger.TriggerInstance.hasItems(Items.GHAST_TEAR))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ModItems.vanitas_Boots.get())
                .pattern("EBE")
                .pattern("LAL")
                .define('B', Tags.Items.DYES_BLACK)
                .define('E', Items.GHAST_TEAR)
                .define('A', Items.LEATHER_BOOTS)
                .define('L', Tags.Items.LEATHERS)
                .group(KingdomKeys.MODID)
                .unlockedBy("ghast_tear", InventoryChangeTrigger.TriggerInstance.hasItems(Items.GHAST_TEAR))
                .save(consumer);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.COMBAT, ModItems.vanitas_Remnant_Helmet.get())
		        .requires(ModItems.vanitas_Helmet.get())
		        .requires(Tags.Items.DYES_WHITE)
		        .group(KingdomKeys.MODID)
		        .unlockedBy("vanitas_helmet", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.vanitas_Helmet.get()))
		        .save(consumer);
		
		ShapelessRecipeBuilder.shapeless(RecipeCategory.COMBAT, ModItems.vanitas_Remnant_Chestplate.get())
		        .requires(ModItems.vanitas_Chestplate.get())
		        .requires(Tags.Items.DYES_WHITE)
		        .group(KingdomKeys.MODID)
		        .unlockedBy("vanitas_chestplate", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.vanitas_Chestplate.get()))
		        .save(consumer);
		
		ShapelessRecipeBuilder.shapeless(RecipeCategory.COMBAT, ModItems.vanitas_Remnant_Leggings.get())
		        .requires(ModItems.vanitas_Leggings.get())
		        .requires(Tags.Items.DYES_WHITE)
		        .group(KingdomKeys.MODID)
		        .unlockedBy("vanitas_leggings", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.vanitas_Leggings.get()))
		        .save(consumer);
		
		ShapelessRecipeBuilder.shapeless(RecipeCategory.COMBAT, ModItems.vanitas_Remnant_Boots.get())
		        .requires(ModItems.vanitas_Boots.get())
		        .requires(Tags.Items.DYES_WHITE)
		        .group(KingdomKeys.MODID)
		        .unlockedBy("vanitas_boots", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.vanitas_Boots.get()))
		        .save(consumer);
        		
		ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ModItems.dark_Riku_Chestplate.get())
		        .pattern("P P")
		        .pattern("BCB")
		        .pattern("PDP")
		        .define('B', Tags.Items.DYES_BLACK)
		        .define('P', Items.PHANTOM_MEMBRANE)
		        .define('C', Blocks.CRYING_OBSIDIAN)
		        .define('D', Tags.Items.DYES_BLUE)
		        .group(KingdomKeys.MODID)
		        .unlockedBy("phantom_membrane", InventoryChangeTrigger.TriggerInstance.hasItems(Items.PHANTOM_MEMBRANE))
		        .save(consumer);
		
		ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ModItems.dark_Riku_Leggings.get())
				.pattern("PBP")
		        .pattern("P P")
		        .pattern("C C")
		        .define('B', Tags.Items.DYES_BLACK)
		        .define('P', Items.PHANTOM_MEMBRANE)
		        .define('C', Blocks.CRYING_OBSIDIAN)
		        .group(KingdomKeys.MODID)
		        .unlockedBy("phantom_membrane", InventoryChangeTrigger.TriggerInstance.hasItems(Items.PHANTOM_MEMBRANE))
		        .save(consumer);
		
		ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ModItems.dark_Riku_Boots.get())
				.pattern("P P")
		        .pattern("C C")
		        .define('P', Items.PHANTOM_MEMBRANE)
		        .define('C', Blocks.CRYING_OBSIDIAN)
		        .group(KingdomKeys.MODID)
		        .unlockedBy("phantom_membrane", InventoryChangeTrigger.TriggerInstance.hasItems(Items.PHANTOM_MEMBRANE))
		        .save(consumer);




		ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ModItems.aced_Helmet.get())
				.pattern("DLD")
				.pattern("LHL")
				.define('L', Items.LEATHER)
				.define('H', Items.LEATHER_HELMET)
				.define('D', Items.BROWN_DYE)
				.group(KingdomKeys.MODID)
				.unlockedBy("leather_helmet", InventoryChangeTrigger.TriggerInstance.hasItems(Items.LEATHER_HELMET))
				.save(consumer);

		ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ModItems.aced_Chestplate.get())
				.pattern("D D")
				.pattern("LHL")
				.pattern("DLD")
				.define('L', Items.LEATHER)
				.define('H', Items.LEATHER_CHESTPLATE)
				.define('D', Items.BROWN_DYE)
				.group(KingdomKeys.MODID)
				.unlockedBy("leather_chestplate", InventoryChangeTrigger.TriggerInstance.hasItems(Items.LEATHER_CHESTPLATE))
				.save(consumer);

		ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ModItems.aced_Leggings.get())
				.pattern("DLD")
				.pattern("LHL")
				.pattern("D D")
				.define('L', Items.LEATHER)
				.define('H', Items.LEATHER_LEGGINGS)
				.define('D', Items.BROWN_DYE)
				.group(KingdomKeys.MODID)
				.unlockedBy("leather_leggings", InventoryChangeTrigger.TriggerInstance.hasItems(Items.LEATHER_LEGGINGS))
				.save(consumer);

		ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ModItems.aced_Boots.get())
				.pattern("D D")
				.pattern("LHL")
				.define('D', Items.BROWN_DYE)
				.define('H', Items.LEATHER_BOOTS)
				.define('L', Items.LEATHER)
				.group(KingdomKeys.MODID)
				.unlockedBy("leather_boots", InventoryChangeTrigger.TriggerInstance.hasItems(Items.LEATHER_BOOTS))
				.save(consumer);


		ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ModItems.ira_Helmet.get())
				.pattern("DLD")
				.pattern("LHL")
				.define('L', Items.LEATHER)
				.define('H', Items.LEATHER_HELMET)
				.define('D', Items.WHITE_DYE)
				.group(KingdomKeys.MODID)
				.unlockedBy("leather_helmet", InventoryChangeTrigger.TriggerInstance.hasItems(Items.LEATHER_HELMET))
				.save(consumer);

		ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ModItems.ira_Chestplate.get())
				.pattern("D D")
				.pattern("LHL")
				.pattern("DLD")
				.define('L', Items.LEATHER)
				.define('H', Items.LEATHER_CHESTPLATE)
				.define('D', Items.WHITE_DYE)
				.group(KingdomKeys.MODID)
				.unlockedBy("leather_chestplate", InventoryChangeTrigger.TriggerInstance.hasItems(Items.LEATHER_CHESTPLATE))
				.save(consumer);

		ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ModItems.ira_Leggings.get())
				.pattern("DLD")
				.pattern("LHL")
				.pattern("D D")
				.define('L', Items.LEATHER)
				.define('H', Items.LEATHER_LEGGINGS)
				.define('D', Items.WHITE_DYE)
				.group(KingdomKeys.MODID)
				.unlockedBy("leather_leggings", InventoryChangeTrigger.TriggerInstance.hasItems(Items.LEATHER_LEGGINGS))
				.save(consumer);

		ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ModItems.ira_Boots.get())
				.pattern("D D")
				.pattern("LHL")
				.define('D', Items.WHITE_DYE)
				.define('H', Items.LEATHER_BOOTS)
				.define('L', Items.LEATHER)
				.group(KingdomKeys.MODID)
				.unlockedBy("leather_boots", InventoryChangeTrigger.TriggerInstance.hasItems(Items.LEATHER_BOOTS))
				.save(consumer);


		ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ModItems.ava_Helmet.get())
				.pattern("DLD")
				.pattern("LHL")
				.define('L', Items.LEATHER)
				.define('H', Items.LEATHER_HELMET)
				.define('D', Items.PINK_DYE)
				.group(KingdomKeys.MODID)
				.unlockedBy("leather_helmet", InventoryChangeTrigger.TriggerInstance.hasItems(Items.LEATHER_HELMET))
				.save(consumer);

		ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ModItems.ava_Chestplate.get())
				.pattern("D D")
				.pattern("LHL")
				.pattern("DLD")
				.define('L', Items.LEATHER)
				.define('H', Items.LEATHER_CHESTPLATE)
				.define('D', Items.PINK_DYE)
				.group(KingdomKeys.MODID)
				.unlockedBy("leather_chestplate", InventoryChangeTrigger.TriggerInstance.hasItems(Items.LEATHER_CHESTPLATE))
				.save(consumer);

		ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ModItems.ava_Leggings.get())
				.pattern("DLD")
				.pattern("LHL")
				.pattern("D D")
				.define('L', Items.LEATHER)
				.define('H', Items.LEATHER_LEGGINGS)
				.define('D', Items.PINK_DYE)
				.group(KingdomKeys.MODID)
				.unlockedBy("leather_leggings", InventoryChangeTrigger.TriggerInstance.hasItems(Items.LEATHER_LEGGINGS))
				.save(consumer);

		ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ModItems.ava_Boots.get())
				.pattern("D D")
				.pattern("LHL")
				.define('D', Items.PINK_DYE)
				.define('H', Items.LEATHER_BOOTS)
				.define('L', Items.LEATHER)
				.group(KingdomKeys.MODID)
				.unlockedBy("leather_boots", InventoryChangeTrigger.TriggerInstance.hasItems(Items.LEATHER_BOOTS))
				.save(consumer);


		ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ModItems.gula_Helmet.get())
				.pattern("DLD")
				.pattern("LHL")
				.define('L', Items.LEATHER)
				.define('H', Items.LEATHER_HELMET)
				.define('D', Items.YELLOW_DYE)
				.group(KingdomKeys.MODID)
				.unlockedBy("leather_helmet", InventoryChangeTrigger.TriggerInstance.hasItems(Items.LEATHER_HELMET))
				.save(consumer);

		ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ModItems.gula_Chestplate.get())
				.pattern("D D")
				.pattern("LHL")
				.pattern("DLD")
				.define('L', Items.LEATHER)
				.define('H', Items.LEATHER_CHESTPLATE)
				.define('D', Items.YELLOW_DYE)
				.group(KingdomKeys.MODID)
				.unlockedBy("leather_chestplate", InventoryChangeTrigger.TriggerInstance.hasItems(Items.LEATHER_CHESTPLATE))
				.save(consumer);

		ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ModItems.gula_Leggings.get())
				.pattern("DLD")
				.pattern("LHL")
				.pattern("D D")
				.define('L', Items.LEATHER)
				.define('H', Items.LEATHER_LEGGINGS)
				.define('D', Items.YELLOW_DYE)
				.group(KingdomKeys.MODID)
				.unlockedBy("leather_leggings", InventoryChangeTrigger.TriggerInstance.hasItems(Items.LEATHER_LEGGINGS))
				.save(consumer);

		ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ModItems.gula_Boots.get())
				.pattern("D D")
				.pattern("LHL")
				.define('D', Items.YELLOW_DYE)
				.define('H', Items.LEATHER_BOOTS)
				.define('L', Items.LEATHER)
				.group(KingdomKeys.MODID)
				.unlockedBy("leather_boots", InventoryChangeTrigger.TriggerInstance.hasItems(Items.LEATHER_BOOTS))
				.save(consumer);

		ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ModItems.invi_Helmet.get())
				.pattern("DLD")
				.pattern("LHL")
				.define('L', Items.LEATHER)
				.define('H', Items.LEATHER_HELMET)
				.define('D', Items.BLUE_DYE)
				.group(KingdomKeys.MODID)
				.unlockedBy("leather_helmet", InventoryChangeTrigger.TriggerInstance.hasItems(Items.LEATHER_HELMET))
				.save(consumer);

		ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ModItems.invi_Chestplate.get())
				.pattern("D D")
				.pattern("LHL")
				.pattern("DLD")
				.define('L', Items.LEATHER)
				.define('H', Items.LEATHER_CHESTPLATE)
				.define('D', Items.BLUE_DYE)
				.group(KingdomKeys.MODID)
				.unlockedBy("leather_chestplate", InventoryChangeTrigger.TriggerInstance.hasItems(Items.LEATHER_CHESTPLATE))
				.save(consumer);

		ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ModItems.invi_Leggings.get())
				.pattern("DLD")
				.pattern("LHL")
				.pattern("D D")
				.define('L', Items.LEATHER)
				.define('H', Items.LEATHER_LEGGINGS)
				.define('D', Items.BLUE_DYE)
				.group(KingdomKeys.MODID)
				.unlockedBy("leather_leggings", InventoryChangeTrigger.TriggerInstance.hasItems(Items.LEATHER_LEGGINGS))
				.save(consumer);

		ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ModItems.invi_Boots.get())
				.pattern("D D")
				.pattern("LHL")
				.define('D', Items.BLUE_DYE)
				.define('H', Items.LEATHER_BOOTS)
				.define('L', Items.LEATHER)
				.group(KingdomKeys.MODID)
				.unlockedBy("leather_boots", InventoryChangeTrigger.TriggerInstance.hasItems(Items.LEATHER_BOOTS))
				.save(consumer);

		// util blocks
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, ModBlocks.magicalChest.get())
                .pattern("GNG")
                .pattern("GCG")
                .pattern("GNG")
                .define('G', Tags.Items.INGOTS_GOLD)
                .define('C', Tags.Items.CHESTS)
                .define('N', Blocks.NETHER_BRICKS)
                .group(KingdomKeys.MODID)
                .unlockedBy("chest", InventoryChangeTrigger.TriggerInstance.hasItems(Blocks.CHEST))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, ModBlocks.orgPortal.get())
                .pattern("OPO")
                .pattern("CEC")
                .pattern("OPO")
                .define('O', Tags.Items.OBSIDIANS)
                .define('P', Tags.Items.ENDER_PEARLS)
                .define('E', Items.ENDER_EYE)
                .define('C', Items.CHORUS_FRUIT)
                .group(KingdomKeys.MODID)
                .unlockedBy("ender_eye", InventoryChangeTrigger.TriggerInstance.hasItems(Items.ENDER_EYE))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, ModBlocks.pedestal.get())
                .pattern(" I ")
                .pattern("MMM")
                .pattern("MBM")
                .define('M', ModBlocks.metalBlox.get())
                .define('B', Tags.Items.STORAGE_BLOCKS_IRON)
                .define('I', Items.ITEM_FRAME)
                .group(KingdomKeys.MODID)
                .unlockedBy("metalblox", InventoryChangeTrigger.TriggerInstance.hasItems(ModBlocks.metalBlox.get()))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, ModBlocks.station_of_awakening_core.get())
                .pattern("MMM")
                .pattern("MGM")
                .pattern("MMM")
                .define('M', ModBlocks.mosaic_stained_glass.get())
                .define('G', Blocks.GLOWSTONE)
                .group(KingdomKeys.MODID)
                .unlockedBy("mosaic_stained_glass", InventoryChangeTrigger.TriggerInstance.hasItems(ModBlocks.mosaic_stained_glass.get()))
                .save(consumer);

		ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, ModItems.gummiPhone.get())
				.pattern("G G")
				.pattern("RWR")
				.pattern("RWR")
				.define('R', Items.REDSTONE)
				.define('G', Items.GOLD_INGOT)
				.define('W', ModBlocks.gummiCubes.get(15).get())
				.group(KingdomKeys.MODID)
				.unlockedBy("redstone", InventoryChangeTrigger.TriggerInstance.hasItems(Items.REDSTONE))
				.save(consumer);

		List<Item> dyes = List.of(
				Items.WHITE_DYE,
				Items.ORANGE_DYE,
				Items.MAGENTA_DYE,
				Items.LIGHT_BLUE_DYE,
				Items.YELLOW_DYE,
				Items.LIME_DYE,
				Items.PINK_DYE,
				Items.GRAY_DYE,
				Items.LIGHT_GRAY_DYE,
				Items.CYAN_DYE,
				Items.PURPLE_DYE,
				Items.BLUE_DYE,
				Items.BROWN_DYE,
				Items.GREEN_DYE,
				Items.RED_DYE,
				Items.BLACK_DYE
		);

		//Stonecutter
		for (int i = 0; i < gummiBlocks.size(); i++) {
			for (int j = 0; j < gummiBlocks.size(); j++) {
				if (i != j) {
					for (int k = 0; k < dyes.size(); k++) {
						SingleItemRecipeBuilder.stonecutting(Ingredient.of(gummiBlocks.get(i).get(k).get()), RecipeCategory.BUILDING_BLOCKS, gummiBlocks.get(j).get(k).get())
							.group(KingdomKeys.MODID + "_gummi_blocks")
							.unlockedBy("has_" + Utils.getBlockRegistryName(gummiBlocks.get(i).get(k).get()).getPath(), InventoryChangeTrigger.TriggerInstance.hasItems(gummiBlocks.get(i).get(k).get()))
							.save(consumer, KingdomKeys.rl(Utils.getBlockRegistryName(gummiBlocks.get(j).get(k).get()).getPath() + "_from_" + Utils.getBlockRegistryName(gummiBlocks.get(i).get(k).get()).getPath().replace("_" + DyeColor.values()[k].getName(), "").replace("gummi_", "")));
					}
				}
			}
		}
        for (int i = 0; i < gummiShellBlocks.size(); i++) {
            for (int j = 0; j < gummiShellBlocks.size(); j++) {
                if (i != j) {
                    for (int k = 0; k < dyes.size(); k++) {
                        SingleItemRecipeBuilder.stonecutting(Ingredient.of(gummiShellBlocks.get(i).get(k).get()), RecipeCategory.BUILDING_BLOCKS, gummiShellBlocks.get(j).get(k).get())
                                .group(KingdomKeys.MODID + "_gummi_blocks")
                                .unlockedBy("has_" + Utils.getBlockRegistryName(gummiShellBlocks.get(i).get(k).get()).getPath(), InventoryChangeTrigger.TriggerInstance.hasItems(gummiShellBlocks.get(i).get(k).get()))
                                .save(consumer, KingdomKeys.rl(Utils.getBlockRegistryName(gummiShellBlocks.get(j).get(k).get()).getPath() + "_from_" + Utils.getBlockRegistryName(gummiShellBlocks.get(i).get(k).get()).getPath().replace("_" + DyeColor.values()[k].getName(), "").replace("gummi_", "")));
                    }
                }
            }
        }
        for (int i = 0; i < gummiDispelBlocks.size(); i++) {
            for (int j = 0; j < gummiDispelBlocks.size(); j++) {
                if (i != j) {
                    for (int k = 0; k < dyes.size(); k++) {
                        SingleItemRecipeBuilder.stonecutting(Ingredient.of(gummiDispelBlocks.get(i).get(k).get()), RecipeCategory.BUILDING_BLOCKS, gummiDispelBlocks.get(j).get(k).get())
                                .group(KingdomKeys.MODID + "_gummi_blocks")
                                .unlockedBy("has_" + Utils.getBlockRegistryName(gummiDispelBlocks.get(i).get(k).get()).getPath(), InventoryChangeTrigger.TriggerInstance.hasItems(gummiDispelBlocks.get(i).get(k).get()))
                                .save(consumer, KingdomKeys.rl(Utils.getBlockRegistryName(gummiDispelBlocks.get(j).get(k).get()).getPath() + "_from_" + Utils.getBlockRegistryName(gummiDispelBlocks.get(i).get(k).get()).getPath().replace("_" + DyeColor.values()[k].getName(), "").replace("gummi_", "")));
                    }
                }
            }
        }

		//Meteor fragment to cube
		ShapelessRecipeBuilder.shapeless(RecipeCategory.BUILDING_BLOCKS, new ItemStack(ModBlocks.gummiCubes.getFirst().get()))
			.requires(ModItems.gummiMeteorFragment.get())
			.group(KingdomKeys.MODID + "_gummi_blocks")
			.unlockedBy("fragment", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.gummiMeteorFragment.get()))
			.save(consumer);

		//Meteor to shell
		ShapelessRecipeBuilder.shapeless(RecipeCategory.BUILDING_BLOCKS, new ItemStack(ModBlocks.gummiShellCubes.getFirst().get()))
			.requires(ModItems.gummiMeteorFragment.get(),4)
			.group(KingdomKeys.MODID + "_gummi_blocks")
			.unlockedBy("fragment", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.gummiMeteorFragment.get()))
			.save(consumer);

		//Meteor to dispel
		ShapelessRecipeBuilder.shapeless(RecipeCategory.BUILDING_BLOCKS, new ItemStack(ModBlocks.gummiDispelCubes.getFirst().get()))
			.requires(ModItems.gummiMeteorFragment.get(),9)
			.group(KingdomKeys.MODID + "_gummi_blocks")
			.unlockedBy("fragment", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.gummiMeteorFragment.get()))
			.save(consumer);


		for (int i = 0; i < dyes.size(); i++) {
			//Meteor + dye to cube
			if(i > 0) { //Avoid white dye since they are white by default
				ShapelessRecipeBuilder.shapeless(RecipeCategory.BUILDING_BLOCKS, new ItemStack(ModBlocks.gummiCubes.get(i).get()))
					.requires(ModItems.gummiMeteorFragment.get())
					.requires(dyes.get(i))
					.group(KingdomKeys.MODID + "_gummi_blocks")
					.unlockedBy("fragment", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.gummiMeteorFragment.get()))
					.save(consumer);
			}

			// Shape + dye to dyed shape
			for (int shape=0; shape<gummiBlocks.size();shape++) {
				List<Supplier<Block>> blocks = gummiBlocks.get(shape);
				ShapelessRecipeBuilder.shapeless(RecipeCategory.BUILDING_BLOCKS, new ItemStack(blocks.get(i).get()))
					.requires(ModTags.GUMMI_BLOCK_KEYS.get(shape))
					.requires(dyes.get(i))
					.group(KingdomKeys.MODID + "_gummi_blocks")
					.unlockedBy("has_" + Utils.getBlockRegistryName(blocks.get(i).get()).getPath(), InventoryChangeTrigger.TriggerInstance.hasItems(blocks.get(i).get()))
					.save(consumer, KingdomKeys.rl(Utils.getBlockRegistryName(blocks.get(i).get()).getPath() + "_from_dye"));
			}

            //Shell block + dye ? shell dyed
            for (int shape=0; shape<gummiShellBlocks.size();shape++) {
                List<Supplier<Block>> blocks = gummiShellBlocks.get(shape);
                ShapelessRecipeBuilder.shapeless(RecipeCategory.BUILDING_BLOCKS, new ItemStack(blocks.get(i).get()))
                        .requires(ModTags.GUMMI_SHELL_BLOCK_KEYS.get(shape))
                        .requires(dyes.get(i))
                        .group(KingdomKeys.MODID + "_gummi_blocks")
                        .unlockedBy("has_" + Utils.getBlockRegistryName(blocks.get(i).get()).getPath(), InventoryChangeTrigger.TriggerInstance.hasItems(blocks.get(i).get()))
                        .save(consumer, KingdomKeys.rl(Utils.getBlockRegistryName(blocks.get(i).get()).getPath() + "_from_dye"));
            }

            //Dispel block + dye = dispel dyed
            for (int shape=0; shape<gummiDispelBlocks.size();shape++) {
                List<Supplier<Block>> blocks = gummiDispelBlocks.get(shape);
                ShapelessRecipeBuilder.shapeless(RecipeCategory.BUILDING_BLOCKS, new ItemStack(blocks.get(i).get()))
                        .requires(ModTags.GUMMI_DISPEL_BLOCK_KEYS.get(shape))
                        .requires(dyes.get(i))
                        .group(KingdomKeys.MODID + "_gummi_blocks")
                        .unlockedBy("has_" + Utils.getBlockRegistryName(blocks.get(i).get()).getPath(), InventoryChangeTrigger.TriggerInstance.hasItems(blocks.get(i).get()))
                        .save(consumer, KingdomKeys.rl(Utils.getBlockRegistryName(blocks.get(i).get()).getPath() + "_from_dye"));
            }

            for (int shape=0; shape<gummiDifferentBlocks.size();shape++) {
                List<Supplier<Block>> blocks = gummiDifferentBlocks.get(shape);
                ShapelessRecipeBuilder.shapeless(RecipeCategory.BUILDING_BLOCKS, new ItemStack(blocks.get(i).get()))
                        .requires(ModTags.GUMMI_DIFFERENT_BLOCK_KEYS.get(shape))
                        .requires(dyes.get(i))
                        .group(KingdomKeys.MODID + "_gummi_blocks")
                        .unlockedBy("has_" + Utils.getBlockRegistryName(blocks.get(i).get()).getPath(), InventoryChangeTrigger.TriggerInstance.hasItems(blocks.get(i).get()))
                        .save(consumer, KingdomKeys.rl(Utils.getBlockRegistryName(blocks.get(i).get()).getPath() + "_from_dye"));
            }
		}
    }

	private void addRecipeUpgrade(RecipeOutput consumer, ItemLike input, ItemLike output, int amount, String id) {
		ShapelessRecipeBuilder builder = ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, output);
		builder.requires(input, amount);
		builder.unlockedBy(id, InventoryChangeTrigger.TriggerInstance.hasItems(input)).save(consumer, KingdomKeys.rl(id + amount));
	}
}
