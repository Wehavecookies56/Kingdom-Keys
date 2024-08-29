package online.kingdomkeys.kingdomkeys.datagen.init;

import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.recipes.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.neoforge.common.Tags;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.block.ModBlocks;
import online.kingdomkeys.kingdomkeys.item.ModItems;

public class Recipes extends RecipeProvider {
    DataGenerator dataGenerator;

    public Recipes(DataGenerator dataGenerator, CompletableFuture<HolderLookup.Provider> pRegistries) {
        super(dataGenerator.getPackOutput(), pRegistries);
        this.dataGenerator = dataGenerator;
    }

	@Override
	protected void buildRecipes(RecipeOutput consumer, HolderLookup.Provider holderLookup) {
    	//Terra
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ModItems.terra_Shoulder.get())
	        .requires(ModItems.terra_Helmet.get())
	        .requires(ModItems.terra_Chestplate.get())
	        .requires(ModItems.terra_Leggings.get())
	        .requires(ModItems.terra_Boots.get())
	        .unlockedBy("terra_shoulder", InventoryChangeTrigger.TriggerInstance.hasItems(Items.NETHERITE_INGOT))
	        .save(consumer);

    	SmithingTransformRecipeBuilder.smithing(Ingredient.of(Items.NETHERITE_UPGRADE_SMITHING_TEMPLATE), Ingredient.of(Items.NETHERITE_HELMET), Ingredient.of(Items.ORANGE_DYE), RecipeCategory.COMBAT, ModItems.terra_Helmet.get())
        .unlocks("has_keyblade_armor", InventoryChangeTrigger.TriggerInstance.hasItems(Items.NETHERITE_HELMET))
        .save(consumer, ResourceLocation.fromNamespaceAndPath(KingdomKeys.MODID, "keyblade_armor_terra_helmet_smithing"));

		SmithingTransformRecipeBuilder.smithing(Ingredient.of(Items.NETHERITE_UPGRADE_SMITHING_TEMPLATE), Ingredient.of(Items.NETHERITE_CHESTPLATE), Ingredient.of(Items.ORANGE_DYE), RecipeCategory.COMBAT, ModItems.terra_Chestplate.get())
        .unlocks("has_keyblade_armor", InventoryChangeTrigger.TriggerInstance.hasItems(Items.NETHERITE_CHESTPLATE))
        .save(consumer, ResourceLocation.fromNamespaceAndPath(KingdomKeys.MODID, "keyblade_armor_terra_chestplate_smithing"));

		SmithingTransformRecipeBuilder.smithing(Ingredient.of(Items.NETHERITE_UPGRADE_SMITHING_TEMPLATE), Ingredient.of(Items.NETHERITE_LEGGINGS), Ingredient.of(Items.ORANGE_DYE), RecipeCategory.COMBAT, ModItems.terra_Leggings.get())
        .unlocks("has_keyblade_armor", InventoryChangeTrigger.TriggerInstance.hasItems(Items.NETHERITE_LEGGINGS))
        .save(consumer, ResourceLocation.fromNamespaceAndPath(KingdomKeys.MODID, "keyblade_armor_terra_leggings_smithing"));

		SmithingTransformRecipeBuilder.smithing(Ingredient.of(Items.NETHERITE_UPGRADE_SMITHING_TEMPLATE), Ingredient.of(Items.NETHERITE_BOOTS), Ingredient.of(Items.ORANGE_DYE), RecipeCategory.COMBAT, ModItems.terra_Boots.get())
        .unlocks("has_keyblade_armor", InventoryChangeTrigger.TriggerInstance.hasItems(Items.NETHERITE_BOOTS))
        .save(consumer, ResourceLocation.fromNamespaceAndPath(KingdomKeys.MODID, "keyblade_armor_terra_boots_smithing"));
    	
    	//Aqua
    	ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ModItems.aqua_Shoulder.get())
        .requires(ModItems.aqua_Helmet.get())
        .requires(ModItems.aqua_Chestplate.get())
        .requires(ModItems.aqua_Leggings.get())
        .requires(ModItems.aqua_Boots.get())
        .unlockedBy("aqua_shoulder", InventoryChangeTrigger.TriggerInstance.hasItems(Items.NETHERITE_INGOT))
        .save(consumer);

		SmithingTransformRecipeBuilder.smithing(Ingredient.of(Items.NETHERITE_UPGRADE_SMITHING_TEMPLATE), Ingredient.of(Items.NETHERITE_HELMET), Ingredient.of(Items.BLUE_DYE), RecipeCategory.COMBAT, ModItems.aqua_Helmet.get())
        .unlocks("has_keyblade_armor", InventoryChangeTrigger.TriggerInstance.hasItems(Items.NETHERITE_HELMET))
        .save(consumer, ResourceLocation.fromNamespaceAndPath(KingdomKeys.MODID, "keyblade_armor_aqua_helmet_smithing"));

		SmithingTransformRecipeBuilder.smithing(Ingredient.of(Items.NETHERITE_UPGRADE_SMITHING_TEMPLATE), Ingredient.of(Items.NETHERITE_CHESTPLATE), Ingredient.of(Items.BLUE_DYE), RecipeCategory.COMBAT, ModItems.aqua_Chestplate.get())
        .unlocks("has_keyblade_armor", InventoryChangeTrigger.TriggerInstance.hasItems(Items.NETHERITE_CHESTPLATE))
        .save(consumer, ResourceLocation.fromNamespaceAndPath(KingdomKeys.MODID, "keyblade_armor_aqua_chestplate_smithing"));

		SmithingTransformRecipeBuilder.smithing(Ingredient.of(Items.NETHERITE_UPGRADE_SMITHING_TEMPLATE), Ingredient.of(Items.NETHERITE_LEGGINGS), Ingredient.of(Items.BLUE_DYE), RecipeCategory.COMBAT, ModItems.aqua_Leggings.get())
        .unlocks("has_keyblade_armor", InventoryChangeTrigger.TriggerInstance.hasItems(Items.NETHERITE_LEGGINGS))
        .save(consumer, ResourceLocation.fromNamespaceAndPath(KingdomKeys.MODID, "keyblade_armor_aqua_leggings_smithing"));

		SmithingTransformRecipeBuilder.smithing(Ingredient.of(Items.NETHERITE_UPGRADE_SMITHING_TEMPLATE), Ingredient.of(Items.NETHERITE_BOOTS), Ingredient.of(Items.BLUE_DYE), RecipeCategory.COMBAT, ModItems.aqua_Boots.get())
        .unlocks("has_keyblade_armor", InventoryChangeTrigger.TriggerInstance.hasItems(Items.NETHERITE_BOOTS))
        .save(consumer, ResourceLocation.fromNamespaceAndPath(KingdomKeys.MODID, "keyblade_armor_aqua_boots_smithing"));
    	
    	//Ventus
    	ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ModItems.ventus_Shoulder.get())
        .requires(ModItems.ventus_Helmet.get())
        .requires(ModItems.ventus_Chestplate.get())
        .requires(ModItems.ventus_Leggings.get())
        .requires(ModItems.ventus_Boots.get())
        .unlockedBy("ventus_shoulder", InventoryChangeTrigger.TriggerInstance.hasItems(Items.NETHERITE_INGOT))
        .save(consumer);

		SmithingTransformRecipeBuilder.smithing(Ingredient.of(Items.NETHERITE_UPGRADE_SMITHING_TEMPLATE), Ingredient.of(Items.NETHERITE_HELMET), Ingredient.of(Items.LIME_DYE), RecipeCategory.COMBAT, ModItems.ventus_Helmet.get())
        .unlocks("has_keyblade_armor", InventoryChangeTrigger.TriggerInstance.hasItems(Items.NETHERITE_HELMET))
        .save(consumer, ResourceLocation.fromNamespaceAndPath(KingdomKeys.MODID, "keyblade_armor_ventus_helmet_smithing"));

		SmithingTransformRecipeBuilder.smithing(Ingredient.of(Items.NETHERITE_UPGRADE_SMITHING_TEMPLATE), Ingredient.of(Items.NETHERITE_CHESTPLATE), Ingredient.of(Items.LIME_DYE), RecipeCategory.COMBAT, ModItems.ventus_Chestplate.get())
        .unlocks("has_keyblade_armor", InventoryChangeTrigger.TriggerInstance.hasItems(Items.NETHERITE_CHESTPLATE))
        .save(consumer, ResourceLocation.fromNamespaceAndPath(KingdomKeys.MODID, "keyblade_armor_ventus_chestplate_smithing"));

		SmithingTransformRecipeBuilder.smithing(Ingredient.of(Items.NETHERITE_UPGRADE_SMITHING_TEMPLATE), Ingredient.of(Items.NETHERITE_LEGGINGS), Ingredient.of(Items.LIME_DYE), RecipeCategory.COMBAT, ModItems.ventus_Leggings.get())
        .unlocks("has_keyblade_armor", InventoryChangeTrigger.TriggerInstance.hasItems(Items.NETHERITE_LEGGINGS))
        .save(consumer, ResourceLocation.fromNamespaceAndPath(KingdomKeys.MODID, "keyblade_armor_ventus_leggings_smithing"));

		SmithingTransformRecipeBuilder.smithing(Ingredient.of(Items.NETHERITE_UPGRADE_SMITHING_TEMPLATE), Ingredient.of(Items.NETHERITE_BOOTS), Ingredient.of(Items.LIME_DYE), RecipeCategory.COMBAT, ModItems.ventus_Boots.get())
        .unlocks("has_keyblade_armor", InventoryChangeTrigger.TriggerInstance.hasItems(Items.NETHERITE_BOOTS))
        .save(consumer, ResourceLocation.fromNamespaceAndPath(KingdomKeys.MODID, "keyblade_armor_ventus_boots_smithing"));
    	
    	//Nightmare Ventus
    	ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ModItems.nightmareVentus_Shoulder.get())
        .requires(ModItems.nightmareVentus_Helmet.get())
        .requires(ModItems.nightmareVentus_Chestplate.get())
        .requires(ModItems.nightmareVentus_Leggings.get())
        .requires(ModItems.nightmareVentus_Boots.get())
        .unlockedBy("nightmareverntus_shoulder", InventoryChangeTrigger.TriggerInstance.hasItems(Items.NETHERITE_INGOT))
        .save(consumer);

		SmithingTransformRecipeBuilder.smithing(Ingredient.of(Items.NETHERITE_UPGRADE_SMITHING_TEMPLATE), Ingredient.of(Items.NETHERITE_HELMET), Ingredient.of(Items.BLACK_DYE), RecipeCategory.COMBAT, ModItems.nightmareVentus_Helmet.get())
        .unlocks("has_keyblade_armor", InventoryChangeTrigger.TriggerInstance.hasItems(Items.NETHERITE_HELMET))
        .save(consumer, ResourceLocation.fromNamespaceAndPath(KingdomKeys.MODID, "keyblade_armor_nightmare_ventus_helmet_smithing"));

		SmithingTransformRecipeBuilder.smithing(Ingredient.of(Items.NETHERITE_UPGRADE_SMITHING_TEMPLATE), Ingredient.of(Items.NETHERITE_CHESTPLATE), Ingredient.of(Items.BLACK_DYE), RecipeCategory.COMBAT, ModItems.nightmareVentus_Chestplate.get())
        .unlocks("has_keyblade_armor", InventoryChangeTrigger.TriggerInstance.hasItems(Items.NETHERITE_CHESTPLATE))
        .save(consumer, ResourceLocation.fromNamespaceAndPath(KingdomKeys.MODID, "keyblade_armor_nightmare_ventus_chestplate_smithing"));

		SmithingTransformRecipeBuilder.smithing(Ingredient.of(Items.NETHERITE_UPGRADE_SMITHING_TEMPLATE), Ingredient.of(Items.NETHERITE_LEGGINGS), Ingredient.of(Items.BLACK_DYE), RecipeCategory.COMBAT, ModItems.nightmareVentus_Leggings.get())
        .unlocks("has_keyblade_armor", InventoryChangeTrigger.TriggerInstance.hasItems(Items.NETHERITE_LEGGINGS))
        .save(consumer, ResourceLocation.fromNamespaceAndPath(KingdomKeys.MODID, "keyblade_armor_nightmare_ventus_leggings_smithing"));

		SmithingTransformRecipeBuilder.smithing(Ingredient.of(Items.NETHERITE_UPGRADE_SMITHING_TEMPLATE), Ingredient.of(Items.NETHERITE_BOOTS), Ingredient.of(Items.BLACK_DYE), RecipeCategory.COMBAT, ModItems.nightmareVentus_Boots.get())
        .unlocks("has_keyblade_armor", InventoryChangeTrigger.TriggerInstance.hasItems(Items.NETHERITE_BOOTS))
        .save(consumer, ResourceLocation.fromNamespaceAndPath(KingdomKeys.MODID, "keyblade_armor_nightmare_ventus_boots_smithing"));
    	
    	//Eraqus
    	ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ModItems.eraqus_Shoulder.get())
        .requires(ModItems.eraqus_Helmet.get())
        .requires(ModItems.eraqus_Chestplate.get())
        .requires(ModItems.eraqus_Leggings.get())
        .requires(ModItems.eraqus_Boots.get())
        .unlockedBy("eraqus_shoulder", InventoryChangeTrigger.TriggerInstance.hasItems(Items.NETHERITE_INGOT))
        .save(consumer);

		SmithingTransformRecipeBuilder.smithing(Ingredient.of(Items.NETHERITE_UPGRADE_SMITHING_TEMPLATE), Ingredient.of(Items.NETHERITE_HELMET), Ingredient.of(Items.WHITE_DYE), RecipeCategory.COMBAT, ModItems.eraqus_Helmet.get())
        .unlocks("has_keyblade_armor", InventoryChangeTrigger.TriggerInstance.hasItems(Items.NETHERITE_HELMET))
        .save(consumer, ResourceLocation.fromNamespaceAndPath(KingdomKeys.MODID, "keyblade_armor_eraqus_helmet_smithing"));

		SmithingTransformRecipeBuilder.smithing(Ingredient.of(Items.NETHERITE_UPGRADE_SMITHING_TEMPLATE), Ingredient.of(Items.NETHERITE_CHESTPLATE), Ingredient.of(Items.WHITE_DYE), RecipeCategory.COMBAT, ModItems.eraqus_Chestplate.get())
        .unlocks("has_keyblade_armor", InventoryChangeTrigger.TriggerInstance.hasItems(Items.NETHERITE_CHESTPLATE))
        .save(consumer, ResourceLocation.fromNamespaceAndPath(KingdomKeys.MODID, "keyblade_armor_eraqus_chestplate_smithing"));

		SmithingTransformRecipeBuilder.smithing(Ingredient.of(Items.NETHERITE_UPGRADE_SMITHING_TEMPLATE), Ingredient.of(Items.NETHERITE_LEGGINGS), Ingredient.of(Items.WHITE_DYE), RecipeCategory.COMBAT, ModItems.eraqus_Leggings.get())
        .unlocks("has_keyblade_armor", InventoryChangeTrigger.TriggerInstance.hasItems(Items.NETHERITE_LEGGINGS))
        .save(consumer, ResourceLocation.fromNamespaceAndPath(KingdomKeys.MODID, "keyblade_armor_eraqus_leggings_smithing"));

		SmithingTransformRecipeBuilder.smithing(Ingredient.of(Items.NETHERITE_UPGRADE_SMITHING_TEMPLATE), Ingredient.of(Items.NETHERITE_BOOTS), Ingredient.of(Items.WHITE_DYE), RecipeCategory.COMBAT, ModItems.eraqus_Boots.get())
        .unlocks("has_keyblade_armor", InventoryChangeTrigger.TriggerInstance.hasItems(Items.NETHERITE_BOOTS))
        .save(consumer, ResourceLocation.fromNamespaceAndPath(KingdomKeys.MODID, "keyblade_armor_eraqus_boots_smithing"));
    	
    	//Xehanort
    	ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ModItems.xehanort_Shoulder.get())
        .requires(ModItems.xehanort_Helmet.get())
        .requires(ModItems.xehanort_Chestplate.get())
        .requires(ModItems.xehanort_Leggings.get())
        .requires(ModItems.xehanort_Boots.get())
        .unlockedBy("xehanort_shoulder", InventoryChangeTrigger.TriggerInstance.hasItems(Items.NETHERITE_INGOT))
        .save(consumer);

		SmithingTransformRecipeBuilder.smithing(Ingredient.of(Items.NETHERITE_UPGRADE_SMITHING_TEMPLATE), Ingredient.of(Items.NETHERITE_HELMET), Ingredient.of(Items.GRAY_DYE), RecipeCategory.COMBAT, ModItems.xehanort_Helmet.get())
        .unlocks("has_keyblade_armor", InventoryChangeTrigger.TriggerInstance.hasItems(Items.NETHERITE_HELMET))
        .save(consumer, ResourceLocation.fromNamespaceAndPath(KingdomKeys.MODID, "keyblade_armor_xehanort_helmet_smithing"));

		SmithingTransformRecipeBuilder.smithing(Ingredient.of(Items.NETHERITE_UPGRADE_SMITHING_TEMPLATE), Ingredient.of(Items.NETHERITE_CHESTPLATE), Ingredient.of(Items.GRAY_DYE), RecipeCategory.COMBAT, ModItems.xehanort_Chestplate.get())
        .unlocks("has_keyblade_armor", InventoryChangeTrigger.TriggerInstance.hasItems(Items.NETHERITE_CHESTPLATE))
        .save(consumer, ResourceLocation.fromNamespaceAndPath(KingdomKeys.MODID, "keyblade_armor_xehanort_chestplate_smithing"));

		SmithingTransformRecipeBuilder.smithing(Ingredient.of(Items.NETHERITE_UPGRADE_SMITHING_TEMPLATE), Ingredient.of(Items.NETHERITE_LEGGINGS), Ingredient.of(Items.GRAY_DYE), RecipeCategory.COMBAT, ModItems.xehanort_Leggings.get())
        .unlocks("has_keyblade_armor", InventoryChangeTrigger.TriggerInstance.hasItems(Items.NETHERITE_LEGGINGS))
        .save(consumer, ResourceLocation.fromNamespaceAndPath(KingdomKeys.MODID, "keyblade_armor_xehanort_leggings_smithing"));

		SmithingTransformRecipeBuilder.smithing(Ingredient.of(Items.NETHERITE_UPGRADE_SMITHING_TEMPLATE), Ingredient.of(Items.NETHERITE_BOOTS), Ingredient.of(Items.GRAY_DYE), RecipeCategory.COMBAT, ModItems.xehanort_Boots.get())
        .unlocks("has_keyblade_armor", InventoryChangeTrigger.TriggerInstance.hasItems(Items.NETHERITE_BOOTS))
        .save(consumer, ResourceLocation.fromNamespaceAndPath(KingdomKeys.MODID, "keyblade_armor_xehanort_boots_smithing"));
    	
        // blox
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, ModBlocks.normalBlox.get())
                .define('S', Tags.Items.STONES)
                .define('N', Items.DIRT)
                .pattern("NS")
                .pattern("SN")
                .group("kingdomkeys")
                .unlockedBy("stone", InventoryChangeTrigger.TriggerInstance.hasItems(Blocks.STONE))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, ModBlocks.dangerBlox.get())
                .pattern("NC")
                .pattern("CN")
                .define('C', Blocks.CACTUS)
                .define('N', ModBlocks.normalBlox.get())
                .group("kingdomkeys")
                .unlockedBy("normalblox", InventoryChangeTrigger.TriggerInstance.hasItems(ModBlocks.normalBlox.get()))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, ModBlocks.hardBlox.get())
                .pattern("NS")
                .pattern("SN")
                .define('S', Tags.Items.STONES)
                .define('N', ModBlocks.normalBlox.get())
                .group("kingdomkeys")
                .unlockedBy("normalblox", InventoryChangeTrigger.TriggerInstance.hasItems(ModBlocks.normalBlox.get()))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, ModBlocks.metalBlox.get())
                .pattern("HI")
                .pattern("IH")
                .define('I', Tags.Items.INGOTS_IRON)
                .define('H', ModBlocks.hardBlox.get())
                .group("kingdomkeys")
                .unlockedBy("hardblox", InventoryChangeTrigger.TriggerInstance.hasItems(ModBlocks.hardBlox.get()))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, ModBlocks.ghostBlox.get())
                .pattern("GNG")
                .pattern("GRG")
                .pattern("GNG")
                .define('G', Tags.Items.GLASS_BLOCKS)
                .define('N', ModBlocks.normalBlox.get())
                .define('R', Blocks.REDSTONE_BLOCK)
                .group("kingdomkeys")
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
                .group("kingdomkeys")
                .unlockedBy("normalblox", InventoryChangeTrigger.TriggerInstance.hasItems(ModBlocks.normalBlox.get()))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, ModBlocks.blastBlox.get())
                .pattern("NLN")
                .pattern("NTN")
                .pattern("NTN")
                .define('T', Blocks.TNT)
                .define('N', ModBlocks.normalBlox.get())
                .define('L', Items.LAVA_BUCKET)
                .group("kingdomkeys")
                .unlockedBy("normalblox", InventoryChangeTrigger.TriggerInstance.hasItems(ModBlocks.normalBlox.get()))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, ModBlocks.bounceBlox.get())
                .pattern("NNN")
                .pattern("NSN")
                .pattern("NNN")
                .define('S', Blocks.SLIME_BLOCK)
                .define('N', ModBlocks.normalBlox.get())
                .group("kingdomkeys")
                .unlockedBy("normalblox", InventoryChangeTrigger.TriggerInstance.hasItems(ModBlocks.normalBlox.get()))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, ModBlocks.mosaic_stained_glass.get(), 4)
                .pattern("DGD")
                .pattern("GIG")
                .pattern("DGD")
                .define('D', Tags.Items.DYES)
                .define('G', Blocks.GLASS)
                .define('I', Tags.Items.INGOTS_IRON)
                .group("kingdomkeys")
                .unlockedBy("glass", InventoryChangeTrigger.TriggerInstance.hasItems(Blocks.GLASS))
                .save(consumer);

		ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, ModBlocks.airstepTarget.get(), 1)
				.pattern(" G ")
				.pattern("GEG")
				.pattern(" G ")
				.define('E', Items.ENDER_PEARL)
				.define('G', Items.GLOWSTONE)
				.group("kingdomkeys")
				.unlockedBy("ender_pearl", InventoryChangeTrigger.TriggerInstance.hasItems(Items.ENDER_PEARL))
				.save(consumer);

		ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, ModBlocks.savepoint.get(), 1)
				.pattern("GEG")
				.pattern("EGE")
				.pattern("GEG")
				.define('E', Items.ENDER_PEARL)
				.define('G', Items.GLOWSTONE)
				.group("kingdomkeys")
				.unlockedBy("ender_pearl", InventoryChangeTrigger.TriggerInstance.hasItems(Items.ENDER_PEARL))
				.save(consumer);


        //Items
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, ModItems.wayfinder.get(), 1)
	        .pattern("GEG")
	        .pattern("ECE")
	        .pattern("GEG")
	        .define('E', Items.ENDER_PEARL)
	        .define('C', Items.COMPASS)
	        .define('G', ModBlocks.mosaic_stained_glass.get())
	        .group("kingdomkeys")
	        .unlockedBy("compass", InventoryChangeTrigger.TriggerInstance.hasItems(Items.COMPASS))
	        .save(consumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ModItems.struggleSword.get())
	        .pattern(" W ")
	        .pattern(" W ")
	        .pattern(" S ")
	        .define('S', ModItems.woodenStick.get())
	        .define('W', Blocks.BLUE_WOOL)
	        .group("kingdomkeys")
	        .unlockedBy("struggle_sword", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.woodenStick.get()))
	        .save(consumer);
        
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ModItems.struggleWand.get())
	        .pattern(" W ")
	        .pattern(" S ")
	        .pattern(" S ")
	        .define('S', ModItems.woodenStick.get())
	        .define('W', Blocks.BLUE_WOOL)
	        .group("kingdomkeys")
	        .unlockedBy("struggle_wand", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.woodenStick.get()))
	        .save(consumer);
        
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ModItems.struggleHammer.get())
	        .pattern(" W ")
	        .pattern(" WS")
	        .pattern(" S ")
	        .define('S', ModItems.woodenStick.get())
	        .define('W', Blocks.BLUE_WOOL)
	        .group("kingdomkeys")
	        .unlockedBy("struggle_hammer", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.woodenStick.get()))
	        .save(consumer);
	        
        //2 recipes upgrade
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ModItems.recipeC.get())
        .requires(ModItems.recipeD.get())
        .requires(ModItems.recipeD.get())
        .unlockedBy("recipe_d", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.recipeD.get()))
        .save(consumer);
        
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ModItems.recipeB.get())
        .requires(ModItems.recipeC.get())
        .requires(ModItems.recipeC.get())
        .unlockedBy("recipe_c", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.recipeC.get()))
        .save(consumer);
       
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ModItems.recipeA.get())
        .requires(ModItems.recipeB.get())
        .requires(ModItems.recipeB.get())
        .unlockedBy("recipe_b", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.recipeB.get()))
        .save(consumer);
      
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ModItems.recipeS.get())
        .requires(ModItems.recipeA.get())
        .requires(ModItems.recipeA.get())
        .unlockedBy("recipe_a", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.recipeA.get()))
        .save(consumer);
       
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ModItems.recipeSS.get())
        .requires(ModItems.recipeS.get())
        .requires(ModItems.recipeS.get())
        .unlockedBy("recipe_s", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.recipeS.get()))
        .save(consumer);
        
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ModItems.recipeSSS.get())
        .requires(ModItems.recipeSS.get())
        .requires(ModItems.recipeSS.get())
        .unlockedBy("recipe_ss", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.recipeSS.get()))
        .save(consumer);
      
        
        //4 recipes upgrade
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ModItems.recipeB.get())
        .requires(ModItems.recipeD.get())
        .requires(ModItems.recipeD.get())
        .requires(ModItems.recipeD.get())
        .requires(ModItems.recipeD.get())
        .unlockedBy("recipe_d", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.recipeD.get()))
        .save(consumer,ResourceLocation.fromNamespaceAndPath(KingdomKeys.MODID,"recipe_d2"));
       
	    ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ModItems.recipeA.get())
        .requires(ModItems.recipeC.get())
        .requires(ModItems.recipeC.get())
        .requires(ModItems.recipeC.get())
        .requires(ModItems.recipeC.get())
        .unlockedBy("recipe_c", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.recipeC.get()))
        .save(consumer,ResourceLocation.fromNamespaceAndPath(KingdomKeys.MODID,"recipe_c2"));
		
		ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ModItems.recipeS.get())
        .requires(ModItems.recipeB.get())
        .requires(ModItems.recipeB.get())
        .requires(ModItems.recipeB.get())
        .requires(ModItems.recipeB.get())
        .unlockedBy("recipe_b", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.recipeB.get()))
        .save(consumer,ResourceLocation.fromNamespaceAndPath(KingdomKeys.MODID,"recipe_b2"));
		
		ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ModItems.recipeSS.get())
        .requires(ModItems.recipeA.get())
        .requires(ModItems.recipeA.get())
        .requires(ModItems.recipeA.get())
        .requires(ModItems.recipeA.get())
        .unlockedBy("recipe_a", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.recipeA.get()))
        .save(consumer,ResourceLocation.fromNamespaceAndPath(KingdomKeys.MODID,"recipe_a2"));
		
		 ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ModItems.recipeSSS.get())
        .requires(ModItems.recipeS.get())
        .requires(ModItems.recipeS.get())
        .requires(ModItems.recipeS.get())
        .requires(ModItems.recipeS.get())
        .unlockedBy("recipe_s", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.recipeS.get()))
        .save(consumer,ResourceLocation.fromNamespaceAndPath(KingdomKeys.MODID,"recipe_s2"));
		 
		 
		 //1 recipe downgrade
		ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ModItems.recipeSS.get())
        .requires(ModItems.recipeSSS.get())
        .unlockedBy("recipe_sss", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.recipeSSS.get()))
        .save(consumer,ResourceLocation.fromNamespaceAndPath(KingdomKeys.MODID,"recipe_sss3"));
		
		ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ModItems.recipeS.get())
        .requires(ModItems.recipeSS.get())
        .unlockedBy("recipe_ss", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.recipeSS.get()))
        .save(consumer,ResourceLocation.fromNamespaceAndPath(KingdomKeys.MODID,"recipe_ss3"));
		
		ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ModItems.recipeA.get())
        .requires(ModItems.recipeS.get())
        .unlockedBy("recipe_s", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.recipeS.get()))
        .save(consumer,ResourceLocation.fromNamespaceAndPath(KingdomKeys.MODID,"recipe_s3"));
		
		ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ModItems.recipeB.get())
        .requires(ModItems.recipeA.get())
        .unlockedBy("recipe_a", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.recipeA.get()))
        .save(consumer,ResourceLocation.fromNamespaceAndPath(KingdomKeys.MODID,"recipe_a3"));
		
		ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ModItems.recipeC.get())
        .requires(ModItems.recipeB.get())
        .unlockedBy("recipe_b", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.recipeB.get()))
        .save(consumer,ResourceLocation.fromNamespaceAndPath(KingdomKeys.MODID,"recipe_b3"));
		
		ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ModItems.recipeD.get())
        .requires(ModItems.recipeC.get())
        .unlockedBy("recipe_c", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.recipeC.get()))
        .save(consumer,ResourceLocation.fromNamespaceAndPath(KingdomKeys.MODID,"recipe_c3"));
			
		
        ShapelessRecipeBuilder.shapeless(RecipeCategory.FOOD, ModItems.iceCream.get(), 3)
                .requires(Tags.Items.RODS_WOODEN)
                .requires(Items.SUGAR)
                .requires(Items.WATER_BUCKET)
                .requires(Blocks.ICE)
                .unlockedBy("ice", InventoryChangeTrigger.TriggerInstance.hasItems(Blocks.ICE))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, ModItems.synthesisBag.get())
                .pattern("LSL")
                .pattern("L L")
                .pattern("LLL")
                .define('S', Tags.Items.STRINGS)
                .define('L', Tags.Items.LEATHERS)
                .group("kingdomkeys")
                .unlockedBy("leather", InventoryChangeTrigger.TriggerInstance.hasItems(Items.LEATHER))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ModItems.woodenKeyblade.get())
                .pattern(" WS")
                .pattern(" WS")
                .pattern(" S ")
                .define('S', Items.STICK)
                .define('W', ItemTags.PLANKS)
                .group("kingdomkeys")
                .unlockedBy("stick", InventoryChangeTrigger.TriggerInstance.hasItems(Items.STICK))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.woodenStick.get())
                .pattern("S")
                .pattern("S")
                .pattern("S")
                .define('S', Items.STICK)
                .group("kingdomkeys")
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
                .group("kingdomkeys")
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
                .group("kingdomkeys")
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
                .group("kingdomkeys")
                .unlockedBy("ender_pearl", InventoryChangeTrigger.TriggerInstance.hasItems(Items.ENDER_PEARL))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ModItems.organizationRobe_Boots.get())
                .pattern("EBE")
                .pattern("LAL")
                .define('L', Tags.Items.LEATHERS)
                .define('A', Items.LEATHER_BOOTS)
                .define('E', Tags.Items.ENDER_PEARLS)
                .define('B', Tags.Items.DYES_BLACK)
                .group("kingdomkeys")
                .unlockedBy("ender_pearl", InventoryChangeTrigger.TriggerInstance.hasItems(Items.ENDER_PEARL))
                .save(consumer);

        

        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ModItems.xemnas_Helmet.get())
                .pattern("LWL")
                .pattern("EAE")
                .define('W', Tags.Items.DYES_WHITE)
                .define('E', Items.END_CRYSTAL)
                .define('A', ModItems.organizationRobe_Helmet.get())
                .define('L', Tags.Items.LEATHERS)
                .group("kingdomkeys")
                .unlockedBy("organization_helmet", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.organizationRobe_Helmet.get()))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ModItems.xemnas_Chestplate.get())
                .pattern("LAL")
                .pattern("EWE")
                .pattern("LLL")
                .define('L', Tags.Items.LEATHERS)
                .define('A', ModItems.organizationRobe_Chestplate.get())
                .define('W', Tags.Items.DYES_WHITE)
                .define('E', Items.END_CRYSTAL)
                .group("kingdomkeys")
                .unlockedBy("organization_chestplate", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.organizationRobe_Chestplate.get()))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ModItems.xemnas_Leggings.get())
                .pattern("LWL")
                .pattern("EAE")
                .pattern("L L")
                .define('L', Tags.Items.LEATHERS)
                .define('A', ModItems.organizationRobe_Leggings.get())
                .define('W', Tags.Items.DYES_WHITE)
                .define('E', Items.END_CRYSTAL)
                .group("kingdomkeys")
                .unlockedBy("organization_leggings", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.organizationRobe_Leggings.get()))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ModItems.xemnas_Boots.get())
                .pattern("EWE")
                .pattern("LAL")
                .define('L', Tags.Items.LEATHERS)
                .define('A', ModItems.organizationRobe_Boots.get())
                .define('W', Tags.Items.DYES_WHITE)
                .define('E', Items.END_CRYSTAL)
                .group("kingdomkeys")
                .unlockedBy("organization_boots", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.organizationRobe_Boots.get()))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ModItems.antiCoat_Helmet.get())
                .pattern("LPL")
                .pattern("EAE")
                .define('P', Tags.Items.DYES_PURPLE)
                .define('E', Items.END_CRYSTAL)
                .define('A', ModItems.organizationRobe_Helmet.get())
                .define('L', Tags.Items.LEATHERS)
                .group("kingdomkeys")
                .unlockedBy("organization_helmet", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.organizationRobe_Helmet.get()))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ModItems.antiCoat_Chestplate.get())
                .pattern("LAL")
                .pattern("EPE")
                .pattern("LLL")
                .define('L', Tags.Items.LEATHERS)
                .define('A', ModItems.organizationRobe_Chestplate.get())
                .define('P', Tags.Items.DYES_PURPLE)
                .define('E', Items.END_CRYSTAL)
                .group("kingdomkeys")
                .unlockedBy("organization_chestplate", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.organizationRobe_Chestplate.get()))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ModItems.antiCoat_Leggings.get())
                .pattern("LPL")
                .pattern("EAE")
                .pattern("L L")
                .define('L', Tags.Items.LEATHERS)
                .define('A', ModItems.organizationRobe_Leggings.get())
                .define('P', Tags.Items.DYES_PURPLE)
                .define('E', Items.END_CRYSTAL)
                .group("kingdomkeys")
                .unlockedBy("organization_leggings", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.organizationRobe_Leggings.get()))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ModItems.antiCoat_Boots.get())
                .pattern("EPE")
                .pattern("LAL")
                .define('L', Tags.Items.LEATHERS)
                .define('A', ModItems.organizationRobe_Boots.get())
                .define('P', Tags.Items.DYES_PURPLE)
                .define('E', Items.END_CRYSTAL)
                .group("kingdomkeys")
                .unlockedBy("organization_boots", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.organizationRobe_Boots.get()))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ModItems.vanitas_Helmet.get())
                .pattern("LBL")
                .pattern("EAE")
                .define('B', Tags.Items.DYES_BLACK)
                .define('E', Items.GHAST_TEAR)
                .define('A', Items.LEATHER_HELMET)
                .define('L', Tags.Items.DYES_RED)
                .group("kingdomkeys")
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
                .group("kingdomkeys")
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
                .group("kingdomkeys")
                .unlockedBy("ghast_tear", InventoryChangeTrigger.TriggerInstance.hasItems(Items.GHAST_TEAR))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ModItems.vanitas_Boots.get())
                .pattern("EBE")
                .pattern("LAL")
                .define('B', Tags.Items.DYES_BLACK)
                .define('E', Items.GHAST_TEAR)
                .define('A', Items.LEATHER_BOOTS)
                .define('L', Tags.Items.LEATHERS)
                .group("kingdomkeys")
                .unlockedBy("ghast_tear", InventoryChangeTrigger.TriggerInstance.hasItems(Items.GHAST_TEAR))
                .save(consumer);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.COMBAT, ModItems.vanitas_Remnant_Helmet.get())
		        .requires(ModItems.vanitas_Helmet.get())
		        .requires(Tags.Items.DYES_WHITE)
		        .group("kingdomkeys")
		        .unlockedBy("vanitas_helmet", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.vanitas_Helmet.get()))
		        .save(consumer);
		
		ShapelessRecipeBuilder.shapeless(RecipeCategory.COMBAT, ModItems.vanitas_Remnant_Chestplate.get())
		        .requires(ModItems.vanitas_Chestplate.get())
		        .requires(Tags.Items.DYES_WHITE)
		        .group("kingdomkeys")
		        .unlockedBy("vanitas_chestplate", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.vanitas_Chestplate.get()))
		        .save(consumer);
		
		ShapelessRecipeBuilder.shapeless(RecipeCategory.COMBAT, ModItems.vanitas_Remnant_Leggings.get())
		        .requires(ModItems.vanitas_Leggings.get())
		        .requires(Tags.Items.DYES_WHITE)
		        .group("kingdomkeys")
		        .unlockedBy("vanitas_leggings", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.vanitas_Leggings.get()))
		        .save(consumer);
		
		ShapelessRecipeBuilder.shapeless(RecipeCategory.COMBAT, ModItems.vanitas_Remnant_Boots.get())
		        .requires(ModItems.vanitas_Boots.get())
		        .requires(Tags.Items.DYES_WHITE)
		        .group("kingdomkeys")
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
		        .group("kingdomkeys")
		        .unlockedBy("phantom_membrane", InventoryChangeTrigger.TriggerInstance.hasItems(Items.PHANTOM_MEMBRANE))
		        .save(consumer);
		
		ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ModItems.dark_Riku_Leggings.get())
				.pattern("PBP")
		        .pattern("P P")
		        .pattern("C C")
		        .define('B', Tags.Items.DYES_BLACK)
		        .define('P', Items.PHANTOM_MEMBRANE)
		        .define('C', Blocks.CRYING_OBSIDIAN)
		        .group("kingdomkeys")
		        .unlockedBy("phantom_membrane", InventoryChangeTrigger.TriggerInstance.hasItems(Items.PHANTOM_MEMBRANE))
		        .save(consumer);
		
		ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ModItems.dark_Riku_Boots.get())
				.pattern("P P")
		        .pattern("C C")
		        .define('P', Items.PHANTOM_MEMBRANE)
		        .define('C', Blocks.CRYING_OBSIDIAN)
		        .group("kingdomkeys")
		        .unlockedBy("phantom_membrane", InventoryChangeTrigger.TriggerInstance.hasItems(Items.PHANTOM_MEMBRANE))
		        .save(consumer);

		
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ModItems.aced_Helmet.get())
                .pattern("LBL")
                .pattern("EAE")
                .define('B', Items.CLOCK)
                .define('E', Items.BOOK)
                .define('A', Items.LEATHER_HELMET)
                .define('L', Tags.Items.LEATHERS)
                .group("kingdomkeys")
                .unlockedBy("clock", InventoryChangeTrigger.TriggerInstance.hasItems(Items.CLOCK))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ModItems.aced_Chestplate.get())
                .pattern("LAL")
                .pattern("EBE")
                .pattern("LLL")
                .define('B', Items.CLOCK)
                .define('E', Items.BOOK)
                .define('A', Items.LEATHER_CHESTPLATE)
                .define('L', Tags.Items.LEATHERS)
                .group("kingdomkeys")
                .unlockedBy("clock", InventoryChangeTrigger.TriggerInstance.hasItems(Items.CLOCK))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ModItems.aced_Leggings.get())
                .pattern("LBL")
                .pattern("EAE")
                .pattern("L L")
                .define('B', Items.CLOCK)
                .define('E', Items.BOOK)
                .define('A', Items.LEATHER_LEGGINGS)
                .define('L', Tags.Items.LEATHERS)
                .group("kingdomkeys")
                .unlockedBy("clock", InventoryChangeTrigger.TriggerInstance.hasItems(Items.CLOCK))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ModItems.aced_Boots.get())
                .pattern("EBE")
                .pattern("LAL")
                .define('B', Items.CLOCK)
                .define('E', Items.BOOK)
                .define('A', Items.LEATHER_BOOTS)
                .define('L', Tags.Items.LEATHERS)
                .group("kingdomkeys")
                .unlockedBy("clock", InventoryChangeTrigger.TriggerInstance.hasItems(Items.CLOCK))
                .save(consumer);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.COMBAT, ModItems.aced_Helmet.get())
                .requires(ModItems.ira_Helmet.get())
                .group("kingdomkeys")
                .unlockedBy("ira_helmet", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.ira_Helmet.get()))
                .save(consumer, ResourceLocation.fromNamespaceAndPath(KingdomKeys.MODID, "aced_helmet_shapeless"));

        ShapelessRecipeBuilder.shapeless(RecipeCategory.COMBAT, ModItems.aced_Chestplate.get())
                .requires(ModItems.ira_Chestplate.get())
                .group("kingdomkeys")
                .unlockedBy("ira_chestplate", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.ira_Chestplate.get()))
                .save(consumer, ResourceLocation.fromNamespaceAndPath(KingdomKeys.MODID, "aced_chestplate_shapeless"));

        ShapelessRecipeBuilder.shapeless(RecipeCategory.COMBAT, ModItems.aced_Leggings.get())
                .requires(ModItems.ira_Leggings.get())
                .group("kingdomkeys")
                .unlockedBy("ira_leggings", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.ira_Leggings.get()))
                .save(consumer, ResourceLocation.fromNamespaceAndPath(KingdomKeys.MODID, "aced_leggings_shapeless"));

        ShapelessRecipeBuilder.shapeless(RecipeCategory.COMBAT, ModItems.aced_Boots.get())
                .requires(ModItems.ira_Boots.get())
                .group("kingdomkeys")
                .unlockedBy("ira_boots", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.ira_Boots.get()))
                .save(consumer, ResourceLocation.fromNamespaceAndPath(KingdomKeys.MODID, "aced_boots_shapeless"));

        ShapelessRecipeBuilder.shapeless(RecipeCategory.COMBAT, ModItems.ava_Helmet.get())
                .requires(ModItems.aced_Helmet.get())
                .group("kingdomkeys")
                .unlockedBy("aced_helmet", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.aced_Helmet.get()))
                .save(consumer);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.COMBAT, ModItems.ava_Chestplate.get())
                .requires(ModItems.aced_Chestplate.get())
                .group("kingdomkeys")
                .unlockedBy("aced_chestplate", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.aced_Chestplate.get()))
                .save(consumer);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.COMBAT, ModItems.ava_Leggings.get())
                .requires(ModItems.aced_Leggings.get())
                .group("kingdomkeys")
                .unlockedBy("aced_leggings", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.aced_Leggings.get()))
                .save(consumer);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.COMBAT, ModItems.ava_Boots.get())
                .requires(ModItems.aced_Boots.get())
                .group("kingdomkeys")
                .unlockedBy("aced_boots", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.aced_Boots.get()))
                .save(consumer);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.COMBAT, ModItems.gula_Helmet.get())
                .requires(ModItems.ava_Helmet.get())
                .group("kingdomkeys")
                .unlockedBy("ava_helmet", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.ava_Helmet.get()))
                .save(consumer);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.COMBAT, ModItems.gula_Chestplate.get())
                .requires(ModItems.ava_Chestplate.get())
                .group("kingdomkeys")
                .unlockedBy("ava_chestplate", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.ava_Chestplate.get()))
                .save(consumer);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.COMBAT, ModItems.gula_Leggings.get())
                .requires(ModItems.ava_Leggings.get())
                .group("kingdomkeys")
                .unlockedBy("ava_leggings", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.ava_Leggings.get()))
                .save(consumer);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.COMBAT, ModItems.gula_Boots.get())
                .requires(ModItems.ava_Boots.get())
                .group("kingdomkeys")
                .unlockedBy("ava_boots", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.ava_Boots.get()))
                .save(consumer);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.COMBAT, ModItems.invi_Helmet.get())
                .requires(ModItems.gula_Helmet.get())
                .group("kingdomkeys")
                .unlockedBy("gula_helmet", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.gula_Helmet.get()))
                .save(consumer);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.COMBAT, ModItems.invi_Chestplate.get())
                .requires(ModItems.gula_Chestplate.get())
                .group("kingdomkeys")
                .unlockedBy("gula_chestplate", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.gula_Chestplate.get()))
                .save(consumer);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.COMBAT, ModItems.invi_Leggings.get())
                .requires(ModItems.gula_Leggings.get())
                .group("kingdomkeys")
                .unlockedBy("gula_leggings", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.gula_Leggings.get()))
                .save(consumer);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.COMBAT, ModItems.invi_Boots.get())
                .requires(ModItems.gula_Boots.get())
                .group("kingdomkeys")
                .unlockedBy("gula_boots", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.gula_Boots.get()))
                .save(consumer);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.COMBAT, ModItems.ira_Helmet.get())
                .requires(ModItems.invi_Helmet.get())
                .group("kingdomkeys")
                .unlockedBy("invi_helmet", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.invi_Helmet.get()))
                .save(consumer);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.COMBAT, ModItems.ira_Chestplate.get())
                .requires(ModItems.invi_Chestplate.get())
                .group("kingdomkeys")
                .unlockedBy("invi_chestplate", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.invi_Chestplate.get()))
                .save(consumer);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.COMBAT, ModItems.ira_Leggings.get())
                .requires(ModItems.invi_Leggings.get())
                .group("kingdomkeys")
                .unlockedBy("invi_leggings", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.invi_Leggings.get()))
                .save(consumer);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.COMBAT, ModItems.ira_Boots.get())
                .requires(ModItems.invi_Boots.get())
                .group("kingdomkeys")
                .unlockedBy("invi_boots", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.invi_Boots.get()))
                .save(consumer);

        // util blocks
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, ModBlocks.magicalChest.get())
                .pattern("GNG")
                .pattern("GCG")
                .pattern("GNG")
                .define('G', Tags.Items.INGOTS_GOLD)
                .define('C', Tags.Items.CHESTS)
                .define('N', Blocks.NETHER_BRICKS)
                .group("kingdomkeys")
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
                .group("kingdomkeys")
                .unlockedBy("ender_eye", InventoryChangeTrigger.TriggerInstance.hasItems(Items.ENDER_EYE))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, ModBlocks.pedestal.get())
                .pattern(" I ")
                .pattern("MMM")
                .pattern("MBM")
                .define('M', ModBlocks.metalBlox.get())
                .define('B', Tags.Items.STORAGE_BLOCKS_IRON)
                .define('I', Items.ITEM_FRAME)
                .group("kingdomkeys")
                .unlockedBy("metalblox", InventoryChangeTrigger.TriggerInstance.hasItems(ModBlocks.metalBlox.get()))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, ModBlocks.station_of_awakening_core.get())
                .pattern("MMM")
                .pattern("MGM")
                .pattern("MMM")
                .define('M', ModBlocks.mosaic_stained_glass.get())
                .define('G', Blocks.GLOWSTONE)
                .group("kingdomkeys")
                .unlockedBy("mosaic_stained_glass", InventoryChangeTrigger.TriggerInstance.hasItems(ModBlocks.mosaic_stained_glass.get()))
                .save(consumer);
    }
}
