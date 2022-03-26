package online.kingdomkeys.kingdomkeys.datagen.init;

import java.util.function.Consumer;

import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.data.recipes.ShapelessRecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.common.Tags;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.block.ModBlocks;
import online.kingdomkeys.kingdomkeys.item.ModItems;

public class Recipes extends RecipeProvider {
    DataGenerator dataGenerator;
    public Recipes(DataGenerator generatorIn) {
        super(generatorIn);
        this.dataGenerator = generatorIn;
    }

    @Override
    protected void buildCraftingRecipes(Consumer<FinishedRecipe> consumer) {

        // blox
        ShapedRecipeBuilder.shaped(ModBlocks.normalBlox.get())
                .define('S', Tags.Items.STONE)
                .define('N', Items.DIRT)
                .pattern("NS")
                .pattern("SN")
                .group("kingdomkeys")
                .unlockedBy("stone", InventoryChangeTrigger.TriggerInstance.hasItems(Blocks.STONE))
                .save(consumer);

        ShapedRecipeBuilder.shaped(ModBlocks.dangerBlox.get())
                .pattern("NC")
                .pattern("CN")
                .define('C', Blocks.CACTUS)
                .define('N', ModBlocks.normalBlox.get())
                .group("kingdomkeys")
                .unlockedBy("normalblox", InventoryChangeTrigger.TriggerInstance.hasItems(ModBlocks.normalBlox.get()))
                .save(consumer);

        ShapedRecipeBuilder.shaped(ModBlocks.hardBlox.get())
                .pattern("NS")
                .pattern("SN")
                .define('S', Tags.Items.STONE)
                .define('N', ModBlocks.normalBlox.get())
                .group("kingdomkeys")
                .unlockedBy("normalblox", InventoryChangeTrigger.TriggerInstance.hasItems(ModBlocks.normalBlox.get()))
                .save(consumer);

        ShapedRecipeBuilder.shaped(ModBlocks.metalBlox.get())
                .pattern("HI")
                .pattern("IH")
                .define('I', Tags.Items.INGOTS_IRON)
                .define('H', ModBlocks.hardBlox.get())
                .group("kingdomkeys")
                .unlockedBy("hardblox", InventoryChangeTrigger.TriggerInstance.hasItems(ModBlocks.hardBlox.get()))
                .save(consumer);

        ShapedRecipeBuilder.shaped(ModBlocks.ghostBlox.get())
                .pattern("GNG")
                .pattern("GRG")
                .pattern("GNG")
                .define('G', Tags.Items.GLASS)
                .define('N', ModBlocks.normalBlox.get())
                .define('R', Blocks.REDSTONE_BLOCK)
                .group("kingdomkeys")
                .unlockedBy("normalblox", InventoryChangeTrigger.TriggerInstance.hasItems(ModBlocks.normalBlox.get()))
                .save(consumer);

        ShapedRecipeBuilder.shaped(ModBlocks.magnetBlox.get())
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

        ShapedRecipeBuilder.shaped(ModBlocks.blastBlox.get())
                .pattern("NLN")
                .pattern("NTN")
                .pattern("NTN")
                .define('T', Blocks.TNT)
                .define('N', ModBlocks.normalBlox.get())
                .define('L', Items.LAVA_BUCKET)
                .group("kingdomkeys")
                .unlockedBy("normalblox", InventoryChangeTrigger.TriggerInstance.hasItems(ModBlocks.normalBlox.get()))
                .save(consumer);

        ShapedRecipeBuilder.shaped(ModBlocks.bounceBlox.get())
                .pattern("NNN")
                .pattern("NSN")
                .pattern("NNN")
                .define('S', Blocks.SLIME_BLOCK)
                .define('N', ModBlocks.normalBlox.get())
                .group("kingdomkeys")
                .unlockedBy("normalblox", InventoryChangeTrigger.TriggerInstance.hasItems(ModBlocks.normalBlox.get()))
                .save(consumer);

        ShapedRecipeBuilder.shaped(ModBlocks.mosaic_stained_glass.get(), 4)
                .pattern("DGD")
                .pattern("GIG")
                .pattern("DGD")
                .define('D', Tags.Items.DYES)
                .define('G', Blocks.GLASS)
                .define('I', Tags.Items.INGOTS_IRON)
                .group("kingdomkeys")
                .unlockedBy("glass", InventoryChangeTrigger.TriggerInstance.hasItems(Blocks.GLASS))
                .save(consumer);

        //Items
        ShapelessRecipeBuilder.shapeless(ModItems.iceCream.get())
                .requires(Tags.Items.RODS_WOODEN)
                .requires(Items.SUGAR)
                .requires(Items.WATER_BUCKET)
                .requires(Blocks.ICE)
                .unlockedBy("ice", InventoryChangeTrigger.TriggerInstance.hasItems(Blocks.ICE))
                .save(consumer);

        /*ShapedRecipeBuilder.shapedRecipe(ModItems.emptyBottle.get())
		        .patternLine("G G")
		        .patternLine("GBG")
		        .patternLine("GGG")
                .key('G', Tags.Items.GLASS).key('B', Items.GLASS_BOTTLE)
                .setGroup("kingdomkeys")
                .addCriterion("glass_bottle", InventoryChangeTrigger.Instance.forItems(Items.GLASS_BOTTLE))
                .build(consumer);*/

        ShapedRecipeBuilder.shaped(ModItems.synthesisBag.get())
                .pattern("LSL")
                .pattern("L L")
                .pattern("LLL")
                .define('S', Tags.Items.STRING)
                .define('L', Tags.Items.LEATHER)
                .group("kingdomkeys")
                .unlockedBy("leather", InventoryChangeTrigger.TriggerInstance.hasItems(Items.LEATHER))
                .save(consumer);

        ShapedRecipeBuilder.shaped(ModItems.woodenKeyblade.get())
                .pattern(" WS")
                .pattern(" WS")
                .pattern(" S ")
                .define('S', Items.STICK)
                .define('W', ItemTags.PLANKS)
                .group("kingdomkeys")
                .unlockedBy("stick", InventoryChangeTrigger.TriggerInstance.hasItems(Items.STICK))
                .save(consumer);

        ShapedRecipeBuilder.shaped(ModItems.woodenStick.get())
                .pattern("S")
                .pattern("S")
                .pattern("S")
                .define('S', Items.STICK)
                .group("kingdomkeys")
                .unlockedBy("stick", InventoryChangeTrigger.TriggerInstance.hasItems(Items.STICK))
                .save(consumer);

        //Armour TODO add some items specifically for crafting these so the recipes make a bit more sense
        ShapedRecipeBuilder.shaped(ModItems.organizationRobe_Helmet.get())
                .pattern("LBL")
                .pattern("EAE")
                .define('B', Tags.Items.DYES_BLACK)
                .define('E', Tags.Items.ENDER_PEARLS)
                .define('A', Items.LEATHER_HELMET)
                .define('L', Tags.Items.LEATHER)
                .group("kingdomkeys")
                .unlockedBy("ender_pearl", InventoryChangeTrigger.TriggerInstance.hasItems(Items.ENDER_PEARL))
                .save(consumer);

        ShapedRecipeBuilder.shaped(ModItems.organizationRobe_Chestplate.get())
                .pattern("LAL")
                .pattern("EBE")
                .pattern("LLL")
                .define('L', Tags.Items.LEATHER)
                .define('A', Items.LEATHER_CHESTPLATE)
                .define('E', Tags.Items.ENDER_PEARLS)
                .define('B', Tags.Items.DYES_BLACK)
                .group("kingdomkeys")
                .unlockedBy("ender_pearl", InventoryChangeTrigger.TriggerInstance.hasItems(Items.ENDER_PEARL))
                .save(consumer);

        ShapedRecipeBuilder.shaped(ModItems.organizationRobe_Leggings.get())
                .pattern("LBL")
                .pattern("EAE")
                .pattern("L L")
                .define('L', Tags.Items.LEATHER)
                .define('A', Items.LEATHER_LEGGINGS)
                .define('E', Tags.Items.ENDER_PEARLS)
                .define('B', Tags.Items.DYES_BLACK)
                .group("kingdomkeys")
                .unlockedBy("ender_pearl", InventoryChangeTrigger.TriggerInstance.hasItems(Items.ENDER_PEARL))
                .save(consumer);

        ShapedRecipeBuilder.shaped(ModItems.organizationRobe_Boots.get())
                .pattern("EBE")
                .pattern("LAL")
                .define('L', Tags.Items.LEATHER)
                .define('A', Items.LEATHER_BOOTS)
                .define('E', Tags.Items.ENDER_PEARLS)
                .define('B', Tags.Items.DYES_BLACK)
                .group("kingdomkeys")
                .unlockedBy("ender_pearl", InventoryChangeTrigger.TriggerInstance.hasItems(Items.ENDER_PEARL))
                .save(consumer);

        ShapedRecipeBuilder.shaped(ModItems.terra_Helmet.get())
                .pattern("LOL")
                .pattern("DAD")
                .define('L', Items.LAVA_BUCKET)
                .define('A', Items.DIAMOND_HELMET)
                .define('D', Items.DRAGON_BREATH)
                .define('O', Items.OBSIDIAN)
                .group("kingdomkeys")
                .unlockedBy("diamond_helmet", InventoryChangeTrigger.TriggerInstance.hasItems(Items.DIAMOND_HELMET))
                .save(consumer);

        ShapedRecipeBuilder.shaped(ModItems.terra_Chestplate.get())
                .pattern("LAL")
                .pattern("DOD")
                .pattern("ODO")
                .define('L', Items.LAVA_BUCKET)
                .define('A', Items.DIAMOND_CHESTPLATE)
                .define('D', Items.DRAGON_BREATH)
                .define('O', Items.OBSIDIAN)
                .group("kingdomkeys")
                .unlockedBy("diamond_chestplate", InventoryChangeTrigger.TriggerInstance.hasItems(Items.DIAMOND_CHESTPLATE))
                .save(consumer);

        ShapedRecipeBuilder.shaped(ModItems.terra_Leggings.get())
                .pattern("LDL")
                .pattern("DAD")
                .pattern("O O")
                .define('L', Items.LAVA_BUCKET)
                .define('A', Items.DIAMOND_LEGGINGS)
                .define('D', Items.DRAGON_BREATH)
                .define('O', Items.OBSIDIAN)
                .group("kingdomkeys")
                .unlockedBy("diamond_leggings", InventoryChangeTrigger.TriggerInstance.hasItems(Items.DIAMOND_LEGGINGS))
                .save(consumer);

        ShapedRecipeBuilder.shaped(ModItems.terra_Boots.get())
                .pattern("D D")
                .pattern("LAL")
                .define('L', Items.LAVA_BUCKET)
                .define('A', Items.DIAMOND_BOOTS)
                .define('D', Items.DRAGON_BREATH)
                .group("kingdomkeys")
                .unlockedBy("diamond_boots", InventoryChangeTrigger.TriggerInstance.hasItems(Items.DIAMOND_BOOTS))
                .save(consumer);

        ShapedRecipeBuilder.shaped(ModItems.aqua_Helmet.get())
                .pattern("WPW")
                .pattern("DAD")
                .define('W', Items.WATER_BUCKET)
                .define('A', Items.DIAMOND_HELMET)
                .define('D', Items.DRAGON_BREATH)
                .define('P', Items.PRISMARINE_CRYSTALS)
                .group("kingdomkeys")
                .unlockedBy("diamond_helmet", InventoryChangeTrigger.TriggerInstance.hasItems(Items.DIAMOND_HELMET))
                .save(consumer);

        ShapedRecipeBuilder.shaped(ModItems.aqua_Chestplate.get())
                .pattern("WAW")
                .pattern("DHD")
                .pattern("PDP")
                .define('W', Items.WATER_BUCKET)
                .define('A', Items.DIAMOND_CHESTPLATE)
                .define('D', Items.DRAGON_BREATH)
                .define('P', Items.PRISMARINE_CRYSTALS)
                .define('H', Items.HEART_OF_THE_SEA)
                .group("kingdomkeys")
                .unlockedBy("diamond_chestplate", InventoryChangeTrigger.TriggerInstance.hasItems(Items.DIAMOND_CHESTPLATE))
                .save(consumer);

        ShapedRecipeBuilder.shaped(ModItems.aqua_Leggings.get())
                .pattern("WDW")
                .pattern("DAD")
                .pattern("P P")
                .define('W', Items.WATER_BUCKET)
                .define('A', Items.DIAMOND_LEGGINGS)
                .define('D', Items.DRAGON_BREATH)
                .define('P', Items.PRISMARINE_CRYSTALS)
                .group("kingdomkeys")
                .unlockedBy("diamond_leggings", InventoryChangeTrigger.TriggerInstance.hasItems(Items.DIAMOND_LEGGINGS))
                .save(consumer);

        ShapedRecipeBuilder.shaped(ModItems.aqua_Boots.get())
                .pattern("D D")
                .pattern("WAW")
                .define('W', Items.WATER_BUCKET)
                .define('A', Items.DIAMOND_BOOTS)
                .define('D', Items.DRAGON_BREATH)
                .group("kingdomkeys")
                .unlockedBy("diamond_boots", InventoryChangeTrigger.TriggerInstance.hasItems(Items.DIAMOND_BOOTS))
                .save(consumer);

        ShapedRecipeBuilder.shaped(ModItems.ventus_Helmet.get())
                .pattern("EAE")
                .pattern("D D")
                .define('E', Blocks.EMERALD_BLOCK)
                .define('A', Items.DIAMOND_HELMET)
                .define('D', Items.DRAGON_BREATH)
                .group("kingdomkeys")
                .unlockedBy("diamond_helmet", InventoryChangeTrigger.TriggerInstance.hasItems(Items.DIAMOND_HELMET))
                .save(consumer);

        ShapedRecipeBuilder.shaped(ModItems.ventus_Chestplate.get())
                .pattern("E E")
                .pattern("QAQ")
                .pattern("DQD")
                .define('E', Items.EMERALD_BLOCK)
                .define('A', Items.DIAMOND_CHESTPLATE)
                .define('D', Items.DRAGON_BREATH)
                .define('Q', Items.QUARTZ_BLOCK)
                .group("kingdomkeys")
                .unlockedBy("diamond_chestplate", InventoryChangeTrigger.TriggerInstance.hasItems(Items.DIAMOND_CHESTPLATE))
                .save(consumer);

        ShapedRecipeBuilder.shaped(ModItems.ventus_Leggings.get())
                .pattern("EDE")
                .pattern("DAD")
                .pattern("Q Q")
                .define('E', Items.EMERALD_BLOCK)
                .define('A', Items.DIAMOND_LEGGINGS)
                .define('D', Items.DRAGON_BREATH)
                .define('Q', Items.QUARTZ_BLOCK)
                .group("kingdomkeys")
                .unlockedBy("diamond_leggings", InventoryChangeTrigger.TriggerInstance.hasItems(Items.DIAMOND_LEGGINGS))
                .save(consumer);

        ShapedRecipeBuilder.shaped(ModItems.ventus_Boots.get())
                .pattern("D D")
                .pattern("EAE")
                .define('E', Items.EMERALD_BLOCK)
                .define('A', Items.DIAMOND_BOOTS)
                .define('D', Items.DRAGON_BREATH)
                .group("kingdomkeys")
                .unlockedBy("diamond_boots", InventoryChangeTrigger.TriggerInstance.hasItems(Items.DIAMOND_BOOTS))
                .save(consumer);

        ShapedRecipeBuilder.shaped(ModItems.nightmareVentus_Helmet.get())
                .pattern("OAO")
                .pattern("O O")
                .define('O', Tags.Items.OBSIDIAN)
                .define('A', ModItems.ventus_Helmet.get())
                .group("kingdomkeys")
                .unlockedBy("ventus_helmet", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.ventus_Helmet.get()))
                .save(consumer);

        ShapedRecipeBuilder.shaped(ModItems.nightmareVentus_Chestplate.get())
                .pattern("O O")
                .pattern("OAO")
                .pattern("OOO")
                .define('O', Tags.Items.OBSIDIAN)
                .define('A', ModItems.ventus_Chestplate.get())
                .group("kingdomkeys")
                .unlockedBy("ventus_chestplate", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.ventus_Chestplate.get()))
                .save(consumer);

        ShapedRecipeBuilder.shaped(ModItems.nightmareVentus_Leggings.get())
                .pattern("OAO")
                .pattern("O O")
                .pattern("O O")
                .define('O', Tags.Items.OBSIDIAN)
                .define('A', ModItems.ventus_Leggings.get())
                .group("kingdomkeys")
                .unlockedBy("ventus_leggings", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.ventus_Leggings.get()))
                .save(consumer);

        ShapedRecipeBuilder.shaped(ModItems.nightmareVentus_Boots.get())
                .pattern("O O")
                .pattern("OAO")
                .define('O', Tags.Items.OBSIDIAN)
                .define('A', ModItems.ventus_Boots.get())
                .group("kingdomkeys")
                .unlockedBy("ventus_boots", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.ventus_Boots.get()))
                .save(consumer);

        ShapedRecipeBuilder.shaped(ModItems.xemnas_Helmet.get())
                .pattern("LWL")
                .pattern("EAE")
                .define('W', Tags.Items.DYES_WHITE)
                .define('E', Items.END_CRYSTAL)
                .define('A', ModItems.organizationRobe_Helmet.get())
                .define('L', Tags.Items.LEATHER)
                .group("kingdomkeys")
                .unlockedBy("organization_helmet", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.organizationRobe_Helmet.get()))
                .save(consumer);

        ShapedRecipeBuilder.shaped(ModItems.xemnas_Chestplate.get())
                .pattern("LAL")
                .pattern("EWE")
                .pattern("LLL")
                .define('L', Tags.Items.LEATHER)
                .define('A', ModItems.organizationRobe_Chestplate.get())
                .define('W', Tags.Items.DYES_WHITE)
                .define('E', Items.END_CRYSTAL)
                .group("kingdomkeys")
                .unlockedBy("organization_chestplate", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.organizationRobe_Chestplate.get()))
                .save(consumer);

        ShapedRecipeBuilder.shaped(ModItems.xemnas_Leggings.get())
                .pattern("LWL")
                .pattern("EAE")
                .pattern("L L")
                .define('L', Tags.Items.LEATHER)
                .define('A', ModItems.organizationRobe_Leggings.get())
                .define('W', Tags.Items.DYES_WHITE)
                .define('E', Items.END_CRYSTAL)
                .group("kingdomkeys")
                .unlockedBy("organization_leggings", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.organizationRobe_Leggings.get()))
                .save(consumer);

        ShapedRecipeBuilder.shaped(ModItems.xemnas_Boots.get())
                .pattern("EWE")
                .pattern("LAL")
                .define('L', Tags.Items.LEATHER)
                .define('A', ModItems.organizationRobe_Boots.get())
                .define('W', Tags.Items.DYES_WHITE)
                .define('E', Items.END_CRYSTAL)
                .group("kingdomkeys")
                .unlockedBy("organization_boots", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.organizationRobe_Boots.get()))
                .save(consumer);

        ShapedRecipeBuilder.shaped(ModItems.antiCoat_Helmet.get())
                .pattern("LPL")
                .pattern("EAE")
                .define('P', Tags.Items.DYES_PURPLE)
                .define('E', Items.END_CRYSTAL)
                .define('A', ModItems.organizationRobe_Helmet.get())
                .define('L', Tags.Items.LEATHER)
                .group("kingdomkeys")
                .unlockedBy("organization_helmet", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.organizationRobe_Helmet.get()))
                .save(consumer);

        ShapedRecipeBuilder.shaped(ModItems.antiCoat_Chestplate.get())
                .pattern("LAL")
                .pattern("EPE")
                .pattern("LLL")
                .define('L', Tags.Items.LEATHER)
                .define('A', ModItems.organizationRobe_Chestplate.get())
                .define('P', Tags.Items.DYES_PURPLE)
                .define('E', Items.END_CRYSTAL)
                .group("kingdomkeys")
                .unlockedBy("organization_chestplate", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.organizationRobe_Chestplate.get()))
                .save(consumer);

        ShapedRecipeBuilder.shaped(ModItems.antiCoat_Leggings.get())
                .pattern("LPL")
                .pattern("EAE")
                .pattern("L L")
                .define('L', Tags.Items.LEATHER)
                .define('A', ModItems.organizationRobe_Leggings.get())
                .define('P', Tags.Items.DYES_PURPLE)
                .define('E', Items.END_CRYSTAL)
                .group("kingdomkeys")
                .unlockedBy("organization_leggings", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.organizationRobe_Leggings.get()))
                .save(consumer);

        ShapedRecipeBuilder.shaped(ModItems.antiCoat_Boots.get())
                .pattern("EPE")
                .pattern("LAL")
                .define('L', Tags.Items.LEATHER)
                .define('A', ModItems.organizationRobe_Boots.get())
                .define('P', Tags.Items.DYES_PURPLE)
                .define('E', Items.END_CRYSTAL)
                .group("kingdomkeys")
                .unlockedBy("organization_boots", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.organizationRobe_Boots.get()))
                .save(consumer);

        ShapedRecipeBuilder.shaped(ModItems.eraqus_Helmet.get())
                .pattern("LOL")
                .pattern("DAD")
                .define('L', Tags.Items.STORAGE_BLOCKS_IRON)
                .define('A', Items.DIAMOND_HELMET)
                .define('D', Items.DRAGON_BREATH)
                .define('O', Tags.Items.OBSIDIAN)
                .group("kingdomkeys")
                .unlockedBy("diamond_helmet", InventoryChangeTrigger.TriggerInstance.hasItems(Items.DIAMOND_HELMET))
                .save(consumer);

        ShapedRecipeBuilder.shaped(ModItems.eraqus_Chestplate.get())
                .pattern("LAL")
                .pattern("DOD")
                .pattern("ODO")
                .define('L', Tags.Items.STORAGE_BLOCKS_IRON)
                .define('A', Items.DIAMOND_CHESTPLATE)
                .define('D', Items.DRAGON_BREATH)
                .define('O', Tags.Items.OBSIDIAN)
                .group("kingdomkeys")
                .unlockedBy("diamond_chestplate", InventoryChangeTrigger.TriggerInstance.hasItems(Items.DIAMOND_CHESTPLATE))
                .save(consumer);

        ShapedRecipeBuilder.shaped(ModItems.eraqus_Leggings.get())
                .pattern("LDL")
                .pattern("DAD")
                .pattern("O O")
                .define('L', Items.LAVA_BUCKET)
                .define('A', Items.DIAMOND_LEGGINGS)
                .define('D', Items.DRAGON_BREATH)
                .define('O', Tags.Items.OBSIDIAN)
                .group("kingdomkeys")
                .unlockedBy("diamond_leggings", InventoryChangeTrigger.TriggerInstance.hasItems(Items.DIAMOND_LEGGINGS))
                .save(consumer);

        ShapedRecipeBuilder.shaped(ModItems.eraqus_Boots.get())
                .pattern("DTD")
                .pattern("LAL")
                .define('L', Tags.Items.STORAGE_BLOCKS_IRON)
                .define('A', Items.DIAMOND_BOOTS)
                .define('D', Items.DRAGON_BREATH)
                .define('T', Items.TOTEM_OF_UNDYING)
                .group("kingdomkeys")
                .unlockedBy("diamond_boots", InventoryChangeTrigger.TriggerInstance.hasItems(Items.DIAMOND_BOOTS))
                .save(consumer);

        ShapedRecipeBuilder.shaped(ModItems.vanitas_Helmet.get())
                .pattern("LBL")
                .pattern("EAE")
                .define('B', Tags.Items.DYES_BLACK)
                .define('E', Items.GHAST_TEAR)
                .define('A', Items.LEATHER_HELMET)
                .define('L', Tags.Items.DYES_RED)
                .group("kingdomkeys")
                .unlockedBy("ghast_tear", InventoryChangeTrigger.TriggerInstance.hasItems(Items.GHAST_TEAR))
                .save(consumer);

        ShapedRecipeBuilder.shaped(ModItems.vanitas_Chestplate.get())
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

        ShapedRecipeBuilder.shaped(ModItems.vanitas_Leggings.get())
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

        ShapedRecipeBuilder.shaped(ModItems.vanitas_Boots.get())
                .pattern("EBE")
                .pattern("LAL")
                .define('B', Tags.Items.DYES_BLACK)
                .define('E', Items.GHAST_TEAR)
                .define('A', Items.LEATHER_BOOTS)
                .define('L', Tags.Items.LEATHER)
                .group("kingdomkeys")
                .unlockedBy("ghast_tear", InventoryChangeTrigger.TriggerInstance.hasItems(Items.GHAST_TEAR))
                .save(consumer);

        ShapedRecipeBuilder.shaped(ModItems.aced_Helmet.get())
                .pattern("LBL")
                .pattern("EAE")
                .define('B', Items.CLOCK)
                .define('E', Items.BOOK)
                .define('A', Items.LEATHER_HELMET)
                .define('L', Tags.Items.LEATHER)
                .group("kingdomkeys")
                .unlockedBy("clock", InventoryChangeTrigger.TriggerInstance.hasItems(Items.CLOCK))
                .save(consumer);

        ShapedRecipeBuilder.shaped(ModItems.aced_Chestplate.get())
                .pattern("LAL")
                .pattern("EBE")
                .pattern("LLL")
                .define('B', Items.CLOCK)
                .define('E', Items.BOOK)
                .define('A', Items.LEATHER_CHESTPLATE)
                .define('L', Tags.Items.LEATHER)
                .group("kingdomkeys")
                .unlockedBy("clock", InventoryChangeTrigger.TriggerInstance.hasItems(Items.CLOCK))
                .save(consumer);

        ShapedRecipeBuilder.shaped(ModItems.aced_Leggings.get())
                .pattern("LBL")
                .pattern("EAE")
                .pattern("L L")
                .define('B', Items.CLOCK)
                .define('E', Items.BOOK)
                .define('A', Items.LEATHER_LEGGINGS)
                .define('L', Tags.Items.LEATHER)
                .group("kingdomkeys")
                .unlockedBy("clock", InventoryChangeTrigger.TriggerInstance.hasItems(Items.CLOCK))
                .save(consumer);

        ShapedRecipeBuilder.shaped(ModItems.aced_Boots.get())
                .pattern("EBE")
                .pattern("LAL")
                .define('B', Items.CLOCK)
                .define('E', Items.BOOK)
                .define('A', Items.LEATHER_BOOTS)
                .define('L', Tags.Items.LEATHER)
                .group("kingdomkeys")
                .unlockedBy("clock", InventoryChangeTrigger.TriggerInstance.hasItems(Items.CLOCK))
                .save(consumer);

        ShapelessRecipeBuilder.shapeless(ModItems.aced_Helmet.get())
                .requires(ModItems.ira_Helmet.get())
                .group("kingdomkeys")
                .unlockedBy("ira_helmet", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.ira_Helmet.get()))
                .save(consumer, new ResourceLocation(KingdomKeys.MODID, "aced_helmet_shapeless"));

        ShapelessRecipeBuilder.shapeless(ModItems.aced_Chestplate.get())
                .requires(ModItems.ira_Chestplate.get())
                .group("kingdomkeys")
                .unlockedBy("ira_chestplate", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.ira_Chestplate.get()))
                .save(consumer, new ResourceLocation(KingdomKeys.MODID, "aced_chestplate_shapeless"));

        ShapelessRecipeBuilder.shapeless(ModItems.aced_Leggings.get())
                .requires(ModItems.ira_Leggings.get())
                .group("kingdomkeys")
                .unlockedBy("ira_leggings", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.ira_Leggings.get()))
                .save(consumer, new ResourceLocation(KingdomKeys.MODID, "aced_leggings_shapeless"));

        ShapelessRecipeBuilder.shapeless(ModItems.aced_Boots.get())
                .requires(ModItems.ira_Boots.get())
                .group("kingdomkeys")
                .unlockedBy("ira_boots", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.ira_Boots.get()))
                .save(consumer, new ResourceLocation(KingdomKeys.MODID, "aced_boots_shapeless"));

        ShapelessRecipeBuilder.shapeless(ModItems.ava_Helmet.get())
                .requires(ModItems.aced_Helmet.get())
                .group("kingdomkeys")
                .unlockedBy("aced_helmet", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.aced_Helmet.get()))
                .save(consumer);

        ShapelessRecipeBuilder.shapeless(ModItems.ava_Chestplate.get())
                .requires(ModItems.aced_Chestplate.get())
                .group("kingdomkeys")
                .unlockedBy("aced_chestplate", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.aced_Chestplate.get()))
                .save(consumer);

        ShapelessRecipeBuilder.shapeless(ModItems.ava_Leggings.get())
                .requires(ModItems.aced_Leggings.get())
                .group("kingdomkeys")
                .unlockedBy("aced_leggings", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.aced_Leggings.get()))
                .save(consumer);

        ShapelessRecipeBuilder.shapeless(ModItems.ava_Boots.get())
                .requires(ModItems.aced_Boots.get())
                .group("kingdomkeys")
                .unlockedBy("aced_boots", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.aced_Boots.get()))
                .save(consumer);

        ShapelessRecipeBuilder.shapeless(ModItems.gula_Helmet.get())
                .requires(ModItems.ava_Helmet.get())
                .group("kingdomkeys")
                .unlockedBy("ava_helmet", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.ava_Helmet.get()))
                .save(consumer);

        ShapelessRecipeBuilder.shapeless(ModItems.gula_Chestplate.get())
                .requires(ModItems.ava_Chestplate.get())
                .group("kingdomkeys")
                .unlockedBy("ava_chestplate", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.ava_Chestplate.get()))
                .save(consumer);

        ShapelessRecipeBuilder.shapeless(ModItems.gula_Leggings.get())
                .requires(ModItems.ava_Leggings.get())
                .group("kingdomkeys")
                .unlockedBy("ava_leggings", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.ava_Leggings.get()))
                .save(consumer);

        ShapelessRecipeBuilder.shapeless(ModItems.gula_Boots.get())
                .requires(ModItems.ava_Boots.get())
                .group("kingdomkeys")
                .unlockedBy("ava_boots", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.ava_Boots.get()))
                .save(consumer);

        ShapelessRecipeBuilder.shapeless(ModItems.invi_Helmet.get())
                .requires(ModItems.gula_Helmet.get())
                .group("kingdomkeys")
                .unlockedBy("gula_helmet", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.gula_Helmet.get()))
                .save(consumer);

        ShapelessRecipeBuilder.shapeless(ModItems.invi_Chestplate.get())
                .requires(ModItems.gula_Chestplate.get())
                .group("kingdomkeys")
                .unlockedBy("gula_chestplate", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.gula_Chestplate.get()))
                .save(consumer);

        ShapelessRecipeBuilder.shapeless(ModItems.invi_Leggings.get())
                .requires(ModItems.gula_Leggings.get())
                .group("kingdomkeys")
                .unlockedBy("gula_leggings", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.gula_Leggings.get()))
                .save(consumer);

        ShapelessRecipeBuilder.shapeless(ModItems.invi_Boots.get())
                .requires(ModItems.gula_Boots.get())
                .group("kingdomkeys")
                .unlockedBy("gula_boots", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.gula_Boots.get()))
                .save(consumer);

        ShapelessRecipeBuilder.shapeless(ModItems.ira_Helmet.get())
                .requires(ModItems.invi_Helmet.get())
                .group("kingdomkeys")
                .unlockedBy("invi_helmet", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.invi_Helmet.get()))
                .save(consumer);

        ShapelessRecipeBuilder.shapeless(ModItems.ira_Chestplate.get())
                .requires(ModItems.invi_Chestplate.get())
                .group("kingdomkeys")
                .unlockedBy("invi_chestplate", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.invi_Chestplate.get()))
                .save(consumer);

        ShapelessRecipeBuilder.shapeless(ModItems.ira_Leggings.get())
                .requires(ModItems.invi_Leggings.get())
                .group("kingdomkeys")
                .unlockedBy("invi_leggings", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.invi_Leggings.get()))
                .save(consumer);

        ShapelessRecipeBuilder.shapeless(ModItems.ira_Boots.get())
                .requires(ModItems.invi_Boots.get())
                .group("kingdomkeys")
                .unlockedBy("invi_boots", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.invi_Boots.get()))
                .save(consumer);

        // util blocks
        ShapedRecipeBuilder.shaped(ModBlocks.magicalChest.get())
                .pattern("GNG")
                .pattern("GCG")
                .pattern("GNG")
                .define('G', Tags.Items.INGOTS_GOLD)
                .define('C', Tags.Items.CHESTS)
                .define('N', Blocks.NETHER_BRICKS)
                .group("kingdomkeys")
                .unlockedBy("chest", InventoryChangeTrigger.TriggerInstance.hasItems(Blocks.CHEST))
                .save(consumer);

        ShapedRecipeBuilder.shaped(ModBlocks.orgPortal.get())
                .pattern("OPO")
                .pattern("CEC")
                .pattern("OPO")
                .define('O', Tags.Items.OBSIDIAN)
                .define('P', Tags.Items.ENDER_PEARLS)
                .define('E', Items.ENDER_EYE)
                .define('C', Items.CHORUS_FRUIT)
                .group("kingdomkeys")
                .unlockedBy("ender_eye", InventoryChangeTrigger.TriggerInstance.hasItems(Items.ENDER_EYE))
                .save(consumer);

        ShapedRecipeBuilder.shaped(ModBlocks.pedestal.get())
                .pattern(" I ")
                .pattern("MMM")
                .pattern("MBM")
                .define('M', ModBlocks.metalBlox.get())
                .define('B', Tags.Items.STORAGE_BLOCKS_IRON)
                .define('I', Items.ITEM_FRAME)
                .group("kingdomkeys")
                .unlockedBy("metalblox", InventoryChangeTrigger.TriggerInstance.hasItems(ModBlocks.metalBlox.get()))
                .save(consumer);

        ShapedRecipeBuilder.shaped(ModBlocks.station_of_awakening_core.get())
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
