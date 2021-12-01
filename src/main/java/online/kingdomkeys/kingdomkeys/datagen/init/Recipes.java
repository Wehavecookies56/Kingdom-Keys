package online.kingdomkeys.kingdomkeys.datagen.init;

import java.util.function.Consumer;

import net.minecraft.advancements.criterion.InventoryChangeTrigger;
import net.minecraft.block.Blocks;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.IFinishedRecipe;
import net.minecraft.data.RecipeProvider;
import net.minecraft.data.ShapedRecipeBuilder;
import net.minecraft.data.ShapelessRecipeBuilder;
import net.minecraft.item.Items;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.ResourceLocation;
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
    protected void registerRecipes(Consumer<IFinishedRecipe> consumer) {

        // blox
        ShapedRecipeBuilder.shapedRecipe(ModBlocks.normalBlox.get())
                .key('S', Tags.Items.STONE)
                .key('N', Items.DIRT)
                .patternLine("NS")
                .patternLine("SN")
                .setGroup("kingdomkeys")
                .addCriterion("stone", InventoryChangeTrigger.Instance.forItems(Blocks.STONE))
                .build(consumer);

        ShapedRecipeBuilder.shapedRecipe(ModBlocks.dangerBlox.get())
                .patternLine("NC")
                .patternLine("CN")
                .key('C', Blocks.CACTUS)
                .key('N', ModBlocks.normalBlox.get())
                .setGroup("kingdomkeys")
                .addCriterion("normalblox", InventoryChangeTrigger.Instance.forItems(ModBlocks.normalBlox.get()))
                .build(consumer);

        ShapedRecipeBuilder.shapedRecipe(ModBlocks.hardBlox.get())
                .patternLine("NS")
                .patternLine("SN")
                .key('S', Tags.Items.STONE)
                .key('N', ModBlocks.normalBlox.get())
                .setGroup("kingdomkeys")
                .addCriterion("normalblox", InventoryChangeTrigger.Instance.forItems(ModBlocks.normalBlox.get()))
                .build(consumer);

        ShapedRecipeBuilder.shapedRecipe(ModBlocks.metalBlox.get())
                .patternLine("HI")
                .patternLine("IH")
                .key('I', Tags.Items.INGOTS_IRON)
                .key('H', ModBlocks.hardBlox.get())
                .setGroup("kingdomkeys")
                .addCriterion("hardblox", InventoryChangeTrigger.Instance.forItems(ModBlocks.hardBlox.get()))
                .build(consumer);

        ShapedRecipeBuilder.shapedRecipe(ModBlocks.ghostBlox.get())
                .patternLine("GNG")
                .patternLine("GRG")
                .patternLine("GNG")
                .key('G', Tags.Items.GLASS)
                .key('N', ModBlocks.normalBlox.get())
                .key('R', Blocks.REDSTONE_BLOCK)
                .setGroup("kingdomkeys")
                .addCriterion("normalblox", InventoryChangeTrigger.Instance.forItems(ModBlocks.normalBlox.get()))
                .build(consumer);

        ShapedRecipeBuilder.shapedRecipe(ModBlocks.magnetBlox.get())
                .patternLine("RIR")
                .patternLine("GBG")
                .patternLine("RIR")
                .key('I', Tags.Items.INGOTS_IRON)
                .key('B', ModBlocks.normalBlox.get())
                .key('R', Tags.Items.DUSTS_REDSTONE)
                .key('G', Tags.Items.INGOTS_GOLD)
                .setGroup("kingdomkeys")
                .addCriterion("normalblox", InventoryChangeTrigger.Instance.forItems(ModBlocks.normalBlox.get()))
                .build(consumer);

        ShapedRecipeBuilder.shapedRecipe(ModBlocks.blastBlox.get())
                .patternLine("NLN")
                .patternLine("NTN")
                .patternLine("NTN")
                .key('T', Blocks.TNT)
                .key('N', ModBlocks.normalBlox.get())
                .key('L', Items.LAVA_BUCKET)
                .setGroup("kingdomkeys")
                .addCriterion("normalblox", InventoryChangeTrigger.Instance.forItems(ModBlocks.normalBlox.get()))
                .build(consumer);

        ShapedRecipeBuilder.shapedRecipe(ModBlocks.bounceBlox.get())
                .patternLine("NNN")
                .patternLine("NSN")
                .patternLine("NNN")
                .key('S', Blocks.SLIME_BLOCK)
                .key('N', ModBlocks.normalBlox.get())
                .setGroup("kingdomkeys")
                .addCriterion("normalblox", InventoryChangeTrigger.Instance.forItems(ModBlocks.normalBlox.get()))
                .build(consumer);

        ShapedRecipeBuilder.shapedRecipe(ModBlocks.mosaic_stained_glass.get(), 4)
                .patternLine("DGD")
                .patternLine("GIG")
                .patternLine("DGD")
                .key('D', Tags.Items.DYES)
                .key('G', Blocks.GLASS)
                .key('I', Tags.Items.INGOTS_IRON)
                .setGroup("kingdomkeys")
                .addCriterion("glass", InventoryChangeTrigger.Instance.forItems(Blocks.GLASS))
                .build(consumer);

        //Items
        ShapelessRecipeBuilder.shapelessRecipe(ModItems.iceCream.get())
                .addIngredient(Tags.Items.RODS_WOODEN)
                .addIngredient(Items.SUGAR)
                .addIngredient(Items.WATER_BUCKET)
                .addIngredient(Blocks.ICE)
                .addCriterion("ice", InventoryChangeTrigger.Instance.forItems(Blocks.ICE))
                .build(consumer);

        /*ShapedRecipeBuilder.shapedRecipe(ModItems.emptyBottle.get())
		        .patternLine("G G")
		        .patternLine("GBG")
		        .patternLine("GGG")
                .key('G', Tags.Items.GLASS).key('B', Items.GLASS_BOTTLE)
                .setGroup("kingdomkeys")
                .addCriterion("glass_bottle", InventoryChangeTrigger.Instance.forItems(Items.GLASS_BOTTLE))
                .build(consumer);*/

        ShapedRecipeBuilder.shapedRecipe(ModItems.synthesisBag.get())
                .patternLine("LSL")
                .patternLine("L L")
                .patternLine("LLL")
                .key('S', Items.STRING)
                .key('L', Items.LEATHER)
                .setGroup("kingdomkeys")
                .addCriterion("leather", InventoryChangeTrigger.Instance.forItems(Items.LEATHER))
                .build(consumer);

        ShapedRecipeBuilder.shapedRecipe(ModItems.woodenKeyblade.get())
                .patternLine(" WS")
                .patternLine(" WS")
                .patternLine(" S ")
                .key('S', Items.STICK)
                .key('W', ItemTags.PLANKS)
                .setGroup("kingdomkeys")
                .addCriterion("stick", InventoryChangeTrigger.Instance.forItems(Items.STICK))
                .build(consumer);

        ShapedRecipeBuilder.shapedRecipe(ModItems.woodenStick.get())
                .patternLine("S")
                .patternLine("S")
                .patternLine("S")
                .key('S', Items.STICK)
                .setGroup("kingdomkeys")
                .addCriterion("stick", InventoryChangeTrigger.Instance.forItems(Items.STICK))
                .build(consumer);

        //Armour TODO add some items specifically for crafting these so the recipes make a bit more sense
        ShapedRecipeBuilder.shapedRecipe(ModItems.organizationRobe_Helmet.get())
                .patternLine("LBL")
                .patternLine("EAE")
                .key('B', Items.BLACK_DYE)
                .key('E', Items.ENDER_PEARL)
                .key('A', Items.LEATHER_HELMET)
                .key('L', Items.LEATHER)
                .setGroup("kingdomkeys")
                .addCriterion("ender_pearl", InventoryChangeTrigger.Instance.forItems(Items.ENDER_PEARL))
                .build(consumer);

        ShapedRecipeBuilder.shapedRecipe(ModItems.organizationRobe_Chestplate.get())
                .patternLine("LAL")
                .patternLine("EBE")
                .patternLine("LLL")
                .key('L', Items.LEATHER)
                .key('A', Items.LEATHER_CHESTPLATE)
                .key('E', Items.ENDER_PEARL)
                .key('B', Items.BLACK_DYE)
                .setGroup("kingdomkeys")
                .addCriterion("ender_pearl", InventoryChangeTrigger.Instance.forItems(Items.ENDER_PEARL))
                .build(consumer);

        ShapedRecipeBuilder.shapedRecipe(ModItems.organizationRobe_Leggings.get())
                .patternLine("LBL")
                .patternLine("EAE")
                .patternLine("L L")
                .key('L', Items.LEATHER)
                .key('A', Items.LEATHER_LEGGINGS)
                .key('E', Items.ENDER_PEARL)
                .key('B', Items.BLACK_DYE)
                .setGroup("kingdomkeys")
                .addCriterion("ender_pearl", InventoryChangeTrigger.Instance.forItems(Items.ENDER_PEARL))
                .build(consumer);

        ShapedRecipeBuilder.shapedRecipe(ModItems.organizationRobe_Boots.get())
                .patternLine("EBE")
                .patternLine("LAL")
                .key('L', Items.LEATHER)
                .key('A', Items.LEATHER_BOOTS)
                .key('E', Items.ENDER_PEARL)
                .key('B', Items.BLACK_DYE)
                .setGroup("kingdomkeys")
                .addCriterion("ender_pearl", InventoryChangeTrigger.Instance.forItems(Items.ENDER_PEARL))
                .build(consumer);

        ShapedRecipeBuilder.shapedRecipe(ModItems.terra_Helmet.get())
                .patternLine("LOL")
                .patternLine("DAD")
                .key('L', Items.LAVA_BUCKET)
                .key('A', Items.DIAMOND_HELMET)
                .key('D', Items.DRAGON_BREATH)
                .key('O', Items.OBSIDIAN)
                .setGroup("kingdomkeys")
                .addCriterion("diamond_helmet", InventoryChangeTrigger.Instance.forItems(Items.DIAMOND_HELMET))
                .build(consumer);

        ShapedRecipeBuilder.shapedRecipe(ModItems.terra_Chestplate.get())
                .patternLine("LAL")
                .patternLine("DOD")
                .patternLine("ODO")
                .key('L', Items.LAVA_BUCKET)
                .key('A', Items.DIAMOND_CHESTPLATE)
                .key('D', Items.DRAGON_BREATH)
                .key('O', Items.OBSIDIAN)
                .setGroup("kingdomkeys")
                .addCriterion("diamond_chestplate", InventoryChangeTrigger.Instance.forItems(Items.DIAMOND_CHESTPLATE))
                .build(consumer);

        ShapedRecipeBuilder.shapedRecipe(ModItems.terra_Leggings.get())
                .patternLine("LDL")
                .patternLine("DAD")
                .patternLine("O O")
                .key('L', Items.LAVA_BUCKET)
                .key('A', Items.DIAMOND_LEGGINGS)
                .key('D', Items.DRAGON_BREATH)
                .key('O', Items.OBSIDIAN)
                .setGroup("kingdomkeys")
                .addCriterion("diamond_leggings", InventoryChangeTrigger.Instance.forItems(Items.DIAMOND_LEGGINGS))
                .build(consumer);

        ShapedRecipeBuilder.shapedRecipe(ModItems.terra_Boots.get())
                .patternLine("D D")
                .patternLine("LAL")
                .key('L', Items.LAVA_BUCKET)
                .key('A', Items.DIAMOND_BOOTS)
                .key('D', Items.DRAGON_BREATH)
                .setGroup("kingdomkeys")
                .addCriterion("diamond_boots", InventoryChangeTrigger.Instance.forItems(Items.DIAMOND_BOOTS))
                .build(consumer);

        ShapedRecipeBuilder.shapedRecipe(ModItems.aqua_Helmet.get())
                .patternLine("WPW")
                .patternLine("DAD")
                .key('W', Items.WATER_BUCKET)
                .key('A', Items.DIAMOND_HELMET)
                .key('D', Items.DRAGON_BREATH)
                .key('P', Items.PRISMARINE_CRYSTALS)
                .setGroup("kingdomkeys")
                .addCriterion("diamond_helmet", InventoryChangeTrigger.Instance.forItems(Items.DIAMOND_HELMET))
                .build(consumer);

        ShapedRecipeBuilder.shapedRecipe(ModItems.aqua_Chestplate.get())
                .patternLine("WAW")
                .patternLine("DHD")
                .patternLine("PDP")
                .key('W', Items.WATER_BUCKET)
                .key('A', Items.DIAMOND_CHESTPLATE)
                .key('D', Items.DRAGON_BREATH)
                .key('P', Items.PRISMARINE_CRYSTALS)
                .key('H', Items.HEART_OF_THE_SEA)
                .setGroup("kingdomkeys")
                .addCriterion("diamond_chestplate", InventoryChangeTrigger.Instance.forItems(Items.DIAMOND_CHESTPLATE))
                .build(consumer);

        ShapedRecipeBuilder.shapedRecipe(ModItems.aqua_Leggings.get())
                .patternLine("WDW")
                .patternLine("DAD")
                .patternLine("P P")
                .key('W', Items.WATER_BUCKET)
                .key('A', Items.DIAMOND_LEGGINGS)
                .key('D', Items.DRAGON_BREATH)
                .key('P', Items.PRISMARINE_CRYSTALS)
                .setGroup("kingdomkeys")
                .addCriterion("diamond_leggings", InventoryChangeTrigger.Instance.forItems(Items.DIAMOND_LEGGINGS))
                .build(consumer);

        ShapedRecipeBuilder.shapedRecipe(ModItems.aqua_Boots.get())
                .patternLine("D D")
                .patternLine("WAW")
                .key('W', Items.WATER_BUCKET)
                .key('A', Items.DIAMOND_BOOTS)
                .key('D', Items.DRAGON_BREATH)
                .setGroup("kingdomkeys")
                .addCriterion("diamond_boots", InventoryChangeTrigger.Instance.forItems(Items.DIAMOND_BOOTS))
                .build(consumer);

        ShapedRecipeBuilder.shapedRecipe(ModItems.ventus_Helmet.get())
                .patternLine("EAE")
                .patternLine("D D")
                .key('E', Blocks.EMERALD_BLOCK)
                .key('A', Items.DIAMOND_HELMET)
                .key('D', Items.DRAGON_BREATH)
                .setGroup("kingdomkeys")
                .addCriterion("diamond_helmet", InventoryChangeTrigger.Instance.forItems(Items.DIAMOND_HELMET))
                .build(consumer);

        ShapedRecipeBuilder.shapedRecipe(ModItems.ventus_Chestplate.get())
                .patternLine("E E")
                .patternLine("QAQ")
                .patternLine("DQD")
                .key('E', Items.EMERALD_BLOCK)
                .key('A', Items.DIAMOND_CHESTPLATE)
                .key('D', Items.DRAGON_BREATH)
                .key('Q', Items.QUARTZ_BLOCK)
                .setGroup("kingdomkeys")
                .addCriterion("diamond_chestplate", InventoryChangeTrigger.Instance.forItems(Items.DIAMOND_CHESTPLATE))
                .build(consumer);

        ShapedRecipeBuilder.shapedRecipe(ModItems.ventus_Leggings.get())
                .patternLine("EDE")
                .patternLine("DAD")
                .patternLine("Q Q")
                .key('E', Items.EMERALD_BLOCK)
                .key('A', Items.DIAMOND_LEGGINGS)
                .key('D', Items.DRAGON_BREATH)
                .key('Q', Items.QUARTZ_BLOCK)
                .setGroup("kingdomkeys")
                .addCriterion("diamond_leggings", InventoryChangeTrigger.Instance.forItems(Items.DIAMOND_LEGGINGS))
                .build(consumer);

        ShapedRecipeBuilder.shapedRecipe(ModItems.ventus_Boots.get())
                .patternLine("D D")
                .patternLine("EAE")
                .key('E', Items.EMERALD_BLOCK)
                .key('A', Items.DIAMOND_BOOTS)
                .key('D', Items.DRAGON_BREATH)
                .setGroup("kingdomkeys")
                .addCriterion("diamond_boots", InventoryChangeTrigger.Instance.forItems(Items.DIAMOND_BOOTS))
                .build(consumer);

        ShapedRecipeBuilder.shapedRecipe(ModItems.nightmareVentus_Helmet.get())
                .patternLine("OAO")
                .patternLine("O O")
                .key('O', Blocks.OBSIDIAN)
                .key('A', ModItems.ventus_Helmet.get())
                .setGroup("kingdomkeys")
                .addCriterion("ventus_helmet", InventoryChangeTrigger.Instance.forItems(ModItems.ventus_Helmet.get()))
                .build(consumer);

        ShapedRecipeBuilder.shapedRecipe(ModItems.nightmareVentus_Chestplate.get())
                .patternLine("O O")
                .patternLine("OAO")
                .patternLine("OOO")
                .key('O', Blocks.OBSIDIAN)
                .key('A', ModItems.ventus_Chestplate.get())
                .setGroup("kingdomkeys")
                .addCriterion("ventus_chestplate", InventoryChangeTrigger.Instance.forItems(ModItems.ventus_Chestplate.get()))
                .build(consumer);

        ShapedRecipeBuilder.shapedRecipe(ModItems.nightmareVentus_Leggings.get())
                .patternLine("OAO")
                .patternLine("O O")
                .patternLine("O O")
                .key('O', Blocks.OBSIDIAN)
                .key('A', ModItems.ventus_Leggings.get())
                .setGroup("kingdomkeys")
                .addCriterion("ventus_leggings", InventoryChangeTrigger.Instance.forItems(ModItems.ventus_Leggings.get()))
                .build(consumer);

        ShapedRecipeBuilder.shapedRecipe(ModItems.nightmareVentus_Boots.get())
                .patternLine("O O")
                .patternLine("OAO")
                .key('O', Blocks.OBSIDIAN)
                .key('A', ModItems.ventus_Boots.get())
                .setGroup("kingdomkeys")
                .addCriterion("ventus_boots", InventoryChangeTrigger.Instance.forItems(ModItems.ventus_Boots.get()))
                .build(consumer);

        ShapedRecipeBuilder.shapedRecipe(ModItems.xemnas_Helmet.get())
                .patternLine("LWL")
                .patternLine("EAE")
                .key('W', Items.WHITE_DYE)
                .key('E', Items.END_CRYSTAL)
                .key('A', ModItems.organizationRobe_Helmet.get())
                .key('L', Items.LEATHER)
                .setGroup("kingdomkeys")
                .addCriterion("organization_helmet", InventoryChangeTrigger.Instance.forItems(ModItems.organizationRobe_Helmet.get()))
                .build(consumer);

        ShapedRecipeBuilder.shapedRecipe(ModItems.xemnas_Chestplate.get())
                .patternLine("LAL")
                .patternLine("EWE")
                .patternLine("LLL")
                .key('L', Items.LEATHER)
                .key('A', ModItems.organizationRobe_Chestplate.get())
                .key('W', Items.WHITE_DYE)
                .key('E', Items.END_CRYSTAL)
                .setGroup("kingdomkeys")
                .addCriterion("organization_chestplate", InventoryChangeTrigger.Instance.forItems(ModItems.organizationRobe_Chestplate.get()))
                .build(consumer);

        ShapedRecipeBuilder.shapedRecipe(ModItems.xemnas_Leggings.get())
                .patternLine("LWL")
                .patternLine("EAE")
                .patternLine("L L")
                .key('L', Items.LEATHER)
                .key('A', ModItems.organizationRobe_Leggings.get())
                .key('W', Items.WHITE_DYE)
                .key('E', Items.END_CRYSTAL)
                .setGroup("kingdomkeys")
                .addCriterion("organization_leggings", InventoryChangeTrigger.Instance.forItems(ModItems.organizationRobe_Leggings.get()))
                .build(consumer);

        ShapedRecipeBuilder.shapedRecipe(ModItems.xemnas_Boots.get())
                .patternLine("EWE")
                .patternLine("LAL")
                .key('L', Items.LEATHER)
                .key('A', ModItems.organizationRobe_Boots.get())
                .key('W', Items.WHITE_DYE)
                .key('E', Items.END_CRYSTAL)
                .setGroup("kingdomkeys")
                .addCriterion("organization_boots", InventoryChangeTrigger.Instance.forItems(ModItems.organizationRobe_Boots.get()))
                .build(consumer);

        ShapedRecipeBuilder.shapedRecipe(ModItems.antiCoat_Helmet.get())
                .patternLine("LPL")
                .patternLine("EAE")
                .key('P', Items.PURPLE_DYE)
                .key('E', Items.END_CRYSTAL)
                .key('A', ModItems.organizationRobe_Helmet.get())
                .key('L', Items.LEATHER)
                .setGroup("kingdomkeys")
                .addCriterion("organization_helmet", InventoryChangeTrigger.Instance.forItems(ModItems.organizationRobe_Helmet.get()))
                .build(consumer);

        ShapedRecipeBuilder.shapedRecipe(ModItems.antiCoat_Chestplate.get())
                .patternLine("LAL")
                .patternLine("EPE")
                .patternLine("LLL")
                .key('L', Items.LEATHER)
                .key('A', ModItems.organizationRobe_Chestplate.get())
                .key('P', Items.PURPLE_DYE)
                .key('E', Items.END_CRYSTAL)
                .setGroup("kingdomkeys")
                .addCriterion("organization_chestplate", InventoryChangeTrigger.Instance.forItems(ModItems.organizationRobe_Chestplate.get()))
                .build(consumer);

        ShapedRecipeBuilder.shapedRecipe(ModItems.antiCoat_Leggings.get())
                .patternLine("LPL")
                .patternLine("EAE")
                .patternLine("L L")
                .key('L', Items.LEATHER)
                .key('A', ModItems.organizationRobe_Leggings.get())
                .key('P', Items.PURPLE_DYE)
                .key('E', Items.END_CRYSTAL)
                .setGroup("kingdomkeys")
                .addCriterion("organization_leggings", InventoryChangeTrigger.Instance.forItems(ModItems.organizationRobe_Leggings.get()))
                .build(consumer);

        ShapedRecipeBuilder.shapedRecipe(ModItems.antiCoat_Boots.get())
                .patternLine("EPE")
                .patternLine("LAL")
                .key('L', Items.LEATHER)
                .key('A', ModItems.organizationRobe_Boots.get())
                .key('P', Items.PURPLE_DYE)
                .key('E', Items.END_CRYSTAL)
                .setGroup("kingdomkeys")
                .addCriterion("organization_boots", InventoryChangeTrigger.Instance.forItems(ModItems.organizationRobe_Boots.get()))
                .build(consumer);

        ShapedRecipeBuilder.shapedRecipe(ModItems.eraqus_Helmet.get())
                .patternLine("LOL")
                .patternLine("DAD")
                .key('L', Blocks.IRON_BLOCK)
                .key('A', Items.DIAMOND_HELMET)
                .key('D', Items.DRAGON_BREATH)
                .key('O', Items.OBSIDIAN)
                .setGroup("kingdomkeys")
                .addCriterion("diamond_helmet", InventoryChangeTrigger.Instance.forItems(Items.DIAMOND_HELMET))
                .build(consumer);

        ShapedRecipeBuilder.shapedRecipe(ModItems.eraqus_Chestplate.get())
                .patternLine("LAL")
                .patternLine("DOD")
                .patternLine("ODO")
                .key('L', Blocks.IRON_BLOCK)
                .key('A', Items.DIAMOND_CHESTPLATE)
                .key('D', Items.DRAGON_BREATH)
                .key('O', Items.OBSIDIAN)
                .setGroup("kingdomkeys")
                .addCriterion("diamond_chestplate", InventoryChangeTrigger.Instance.forItems(Items.DIAMOND_CHESTPLATE))
                .build(consumer);

        ShapedRecipeBuilder.shapedRecipe(ModItems.eraqus_Leggings.get())
                .patternLine("LDL")
                .patternLine("DAD")
                .patternLine("O O")
                .key('L', Items.LAVA_BUCKET)
                .key('A', Items.DIAMOND_LEGGINGS)
                .key('D', Items.DRAGON_BREATH)
                .key('O', Items.OBSIDIAN)
                .setGroup("kingdomkeys")
                .addCriterion("diamond_leggings", InventoryChangeTrigger.Instance.forItems(Items.DIAMOND_LEGGINGS))
                .build(consumer);

        ShapedRecipeBuilder.shapedRecipe(ModItems.eraqus_Boots.get())
                .patternLine("DTD")
                .patternLine("LAL")
                .key('L', Blocks.IRON_BLOCK)
                .key('A', Items.DIAMOND_BOOTS)
                .key('D', Items.DRAGON_BREATH)
                .key('T', Items.TOTEM_OF_UNDYING)
                .setGroup("kingdomkeys")
                .addCriterion("diamond_boots", InventoryChangeTrigger.Instance.forItems(Items.DIAMOND_BOOTS))
                .build(consumer);

        ShapedRecipeBuilder.shapedRecipe(ModItems.vanitas_Helmet.get())
                .patternLine("LBL")
                .patternLine("EAE")
                .key('B', Items.BLACK_DYE)
                .key('E', Items.GHAST_TEAR)
                .key('A', Items.LEATHER_HELMET)
                .key('L', Items.RED_DYE)
                .setGroup("kingdomkeys")
                .addCriterion("ghast_tear", InventoryChangeTrigger.Instance.forItems(Items.GHAST_TEAR))
                .build(consumer);

        ShapedRecipeBuilder.shapedRecipe(ModItems.vanitas_Chestplate.get())
                .patternLine("LAL")
                .patternLine("EBE")
                .patternLine("LLL")
                .key('B', Items.BLACK_DYE)
                .key('E', Items.GHAST_TEAR)
                .key('A', Items.LEATHER_CHESTPLATE)
                .key('L', Items.RED_DYE)
                .setGroup("kingdomkeys")
                .addCriterion("ghast_tear", InventoryChangeTrigger.Instance.forItems(Items.GHAST_TEAR))
                .build(consumer);

        ShapedRecipeBuilder.shapedRecipe(ModItems.vanitas_Leggings.get())
                .patternLine("LBL")
                .patternLine("EAE")
                .patternLine("L L")
                .key('B', Items.BLACK_DYE)
                .key('E', Items.GHAST_TEAR)
                .key('A', Items.LEATHER_LEGGINGS)
                .key('L', Items.RED_DYE)
                .setGroup("kingdomkeys")
                .addCriterion("ghast_tear", InventoryChangeTrigger.Instance.forItems(Items.GHAST_TEAR))
                .build(consumer);

        ShapedRecipeBuilder.shapedRecipe(ModItems.vanitas_Boots.get())
                .patternLine("EBE")
                .patternLine("LAL")
                .key('B', Items.BLACK_DYE)
                .key('E', Items.GHAST_TEAR)
                .key('A', Items.LEATHER_BOOTS)
                .key('L', Items.LEATHER)
                .setGroup("kingdomkeys")
                .addCriterion("ghast_tear", InventoryChangeTrigger.Instance.forItems(Items.GHAST_TEAR))
                .build(consumer);

        ShapedRecipeBuilder.shapedRecipe(ModItems.aced_Helmet.get())
                .patternLine("LBL")
                .patternLine("EAE")
                .key('B', Items.CLOCK)
                .key('E', Items.ENCHANTED_BOOK)
                .key('A', Items.LEATHER_HELMET)
                .key('L', Items.LEATHER)
                .setGroup("kingdomkeys")
                .addCriterion("clock", InventoryChangeTrigger.Instance.forItems(Items.CLOCK))
                .build(consumer);

        ShapedRecipeBuilder.shapedRecipe(ModItems.aced_Chestplate.get())
                .patternLine("LAL")
                .patternLine("EBE")
                .patternLine("LLL")
                .key('B', Items.CLOCK)
                .key('E', Items.ENCHANTED_BOOK)
                .key('A', Items.LEATHER_CHESTPLATE)
                .key('L', Items.LEATHER)
                .setGroup("kingdomkeys")
                .addCriterion("clock", InventoryChangeTrigger.Instance.forItems(Items.CLOCK))
                .build(consumer);

        ShapedRecipeBuilder.shapedRecipe(ModItems.aced_Leggings.get())
                .patternLine("LBL")
                .patternLine("EAE")
                .patternLine("L L")
                .key('B', Items.CLOCK)
                .key('E', Items.ENCHANTED_BOOK)
                .key('A', Items.LEATHER_LEGGINGS)
                .key('L', Items.LEATHER)
                .setGroup("kingdomkeys")
                .addCriterion("clock", InventoryChangeTrigger.Instance.forItems(Items.CLOCK))
                .build(consumer);

        ShapedRecipeBuilder.shapedRecipe(ModItems.aced_Boots.get())
                .patternLine("EBE")
                .patternLine("LAL")
                .key('B', Items.CLOCK)
                .key('E', Items.ENCHANTED_BOOK)
                .key('A', Items.LEATHER_BOOTS)
                .key('L', Items.LEATHER)
                .setGroup("kingdomkeys")
                .addCriterion("clock", InventoryChangeTrigger.Instance.forItems(Items.CLOCK))
                .build(consumer);

        ShapelessRecipeBuilder.shapelessRecipe(ModItems.aced_Helmet.get())
                .addIngredient(ModItems.ira_Helmet.get())
                .setGroup("kingdomkeys")
                .addCriterion("ira_helmet", InventoryChangeTrigger.Instance.forItems(ModItems.ira_Helmet.get()))
                .build(consumer, new ResourceLocation(KingdomKeys.MODID, "aced_helmet_shapeless"));

        ShapelessRecipeBuilder.shapelessRecipe(ModItems.aced_Chestplate.get())
                .addIngredient(ModItems.ira_Chestplate.get())
                .setGroup("kingdomkeys")
                .addCriterion("ira_chestplate", InventoryChangeTrigger.Instance.forItems(ModItems.ira_Chestplate.get()))
                .build(consumer, new ResourceLocation(KingdomKeys.MODID, "aced_chestplate_shapeless"));

        ShapelessRecipeBuilder.shapelessRecipe(ModItems.aced_Leggings.get())
                .addIngredient(ModItems.ira_Leggings.get())
                .setGroup("kingdomkeys")
                .addCriterion("ira_leggings", InventoryChangeTrigger.Instance.forItems(ModItems.ira_Leggings.get()))
                .build(consumer, new ResourceLocation(KingdomKeys.MODID, "aced_leggings_shapeless"));

        ShapelessRecipeBuilder.shapelessRecipe(ModItems.aced_Boots.get())
                .addIngredient(ModItems.ira_Boots.get())
                .setGroup("kingdomkeys")
                .addCriterion("ira_boots", InventoryChangeTrigger.Instance.forItems(ModItems.ira_Boots.get()))
                .build(consumer, new ResourceLocation(KingdomKeys.MODID, "aced_boots_shapeless"));

        ShapelessRecipeBuilder.shapelessRecipe(ModItems.ava_Helmet.get())
                .addIngredient(ModItems.aced_Helmet.get())
                .setGroup("kingdomkeys")
                .addCriterion("aced_helmet", InventoryChangeTrigger.Instance.forItems(ModItems.aced_Helmet.get()))
                .build(consumer);

        ShapelessRecipeBuilder.shapelessRecipe(ModItems.ava_Chestplate.get())
                .addIngredient(ModItems.aced_Chestplate.get())
                .setGroup("kingdomkeys")
                .addCriterion("aced_chestplate", InventoryChangeTrigger.Instance.forItems(ModItems.aced_Chestplate.get()))
                .build(consumer);

        ShapelessRecipeBuilder.shapelessRecipe(ModItems.ava_Leggings.get())
                .addIngredient(ModItems.aced_Leggings.get())
                .setGroup("kingdomkeys")
                .addCriterion("aced_leggings", InventoryChangeTrigger.Instance.forItems(ModItems.aced_Leggings.get()))
                .build(consumer);

        ShapelessRecipeBuilder.shapelessRecipe(ModItems.ava_Boots.get())
                .addIngredient(ModItems.aced_Boots.get())
                .setGroup("kingdomkeys")
                .addCriterion("aced_boots", InventoryChangeTrigger.Instance.forItems(ModItems.aced_Boots.get()))
                .build(consumer);

        ShapelessRecipeBuilder.shapelessRecipe(ModItems.gula_Helmet.get())
                .addIngredient(ModItems.ava_Helmet.get())
                .setGroup("kingdomkeys")
                .addCriterion("ava_helmet", InventoryChangeTrigger.Instance.forItems(ModItems.ava_Helmet.get()))
                .build(consumer);

        ShapelessRecipeBuilder.shapelessRecipe(ModItems.gula_Chestplate.get())
                .addIngredient(ModItems.ava_Chestplate.get())
                .setGroup("kingdomkeys")
                .addCriterion("ava_chestplate", InventoryChangeTrigger.Instance.forItems(ModItems.ava_Chestplate.get()))
                .build(consumer);

        ShapelessRecipeBuilder.shapelessRecipe(ModItems.gula_Leggings.get())
                .addIngredient(ModItems.ava_Leggings.get())
                .setGroup("kingdomkeys")
                .addCriterion("ava_leggings", InventoryChangeTrigger.Instance.forItems(ModItems.ava_Leggings.get()))
                .build(consumer);

        ShapelessRecipeBuilder.shapelessRecipe(ModItems.gula_Boots.get())
                .addIngredient(ModItems.ava_Boots.get())
                .setGroup("kingdomkeys")
                .addCriterion("ava_boots", InventoryChangeTrigger.Instance.forItems(ModItems.ava_Boots.get()))
                .build(consumer);

        ShapelessRecipeBuilder.shapelessRecipe(ModItems.invi_Helmet.get())
                .addIngredient(ModItems.gula_Helmet.get())
                .setGroup("kingdomkeys")
                .addCriterion("gula_helmet", InventoryChangeTrigger.Instance.forItems(ModItems.gula_Helmet.get()))
                .build(consumer);

        ShapelessRecipeBuilder.shapelessRecipe(ModItems.invi_Chestplate.get())
                .addIngredient(ModItems.gula_Chestplate.get())
                .setGroup("kingdomkeys")
                .addCriterion("gula_chestplate", InventoryChangeTrigger.Instance.forItems(ModItems.gula_Chestplate.get()))
                .build(consumer);

        ShapelessRecipeBuilder.shapelessRecipe(ModItems.invi_Leggings.get())
                .addIngredient(ModItems.gula_Leggings.get())
                .setGroup("kingdomkeys")
                .addCriterion("gula_leggings", InventoryChangeTrigger.Instance.forItems(ModItems.gula_Leggings.get()))
                .build(consumer);

        ShapelessRecipeBuilder.shapelessRecipe(ModItems.invi_Boots.get())
                .addIngredient(ModItems.gula_Boots.get())
                .setGroup("kingdomkeys")
                .addCriterion("gula_boots", InventoryChangeTrigger.Instance.forItems(ModItems.gula_Boots.get()))
                .build(consumer);

        ShapelessRecipeBuilder.shapelessRecipe(ModItems.ira_Helmet.get())
                .addIngredient(ModItems.invi_Helmet.get())
                .setGroup("kingdomkeys")
                .addCriterion("invi_helmet", InventoryChangeTrigger.Instance.forItems(ModItems.invi_Helmet.get()))
                .build(consumer);

        ShapelessRecipeBuilder.shapelessRecipe(ModItems.ira_Chestplate.get())
                .addIngredient(ModItems.invi_Chestplate.get())
                .setGroup("kingdomkeys")
                .addCriterion("invi_chestplate", InventoryChangeTrigger.Instance.forItems(ModItems.invi_Chestplate.get()))
                .build(consumer);

        ShapelessRecipeBuilder.shapelessRecipe(ModItems.ira_Leggings.get())
                .addIngredient(ModItems.invi_Leggings.get())
                .setGroup("kingdomkeys")
                .addCriterion("invi_leggings", InventoryChangeTrigger.Instance.forItems(ModItems.invi_Leggings.get()))
                .build(consumer);

        ShapelessRecipeBuilder.shapelessRecipe(ModItems.ira_Boots.get())
                .addIngredient(ModItems.invi_Boots.get())
                .setGroup("kingdomkeys")
                .addCriterion("invi_boots", InventoryChangeTrigger.Instance.forItems(ModItems.invi_Boots.get()))
                .build(consumer);

        // util blocks
        ShapedRecipeBuilder.shapedRecipe(ModBlocks.magicalChest.get())
                .patternLine("GNG")
                .patternLine("GCG")
                .patternLine("GNG")
                .key('G', Tags.Items.INGOTS_GOLD)
                .key('C', Tags.Items.CHESTS)
                .key('N', Blocks.NETHER_BRICKS)
                .setGroup("kingdomkeys")
                .addCriterion("chest", InventoryChangeTrigger.Instance.forItems(Blocks.CHEST))
                .build(consumer);

        ShapedRecipeBuilder.shapedRecipe(ModBlocks.orgPortal.get())
                .patternLine("OPO")
                .patternLine("CEC")
                .patternLine("OPO")
                .key('O', Tags.Items.OBSIDIAN)
                .key('P', Items.ENDER_PEARL)
                .key('E', Items.ENDER_EYE)
                .key('C', Items.CHORUS_FRUIT)
                .setGroup("kingdomkeys")
                .addCriterion("ender_eye", InventoryChangeTrigger.Instance.forItems(Items.ENDER_EYE))
                .build(consumer);

        ShapedRecipeBuilder.shapedRecipe(ModBlocks.pedestal.get())
                .patternLine(" I ")
                .patternLine("MMM")
                .patternLine("MBM")
                .key('M', ModBlocks.metalBlox.get())
                .key('B', Blocks.IRON_BLOCK)
                .key('I', Items.ITEM_FRAME)
                .setGroup("kingdomkeys")
                .addCriterion("metalblox", InventoryChangeTrigger.Instance.forItems(ModBlocks.metalBlox.get()))
                .build(consumer);

        ShapedRecipeBuilder.shapedRecipe(ModBlocks.station_of_awakening_core.get())
                .patternLine("MMM")
                .patternLine("MGM")
                .patternLine("MMM")
                .key('M', ModBlocks.mosaic_stained_glass.get())
                .key('G', Blocks.GLOWSTONE)
                .setGroup("kingdomkeys")
                .addCriterion("mosaic_stained_glass", InventoryChangeTrigger.Instance.forItems(ModBlocks.mosaic_stained_glass.get()))
                .build(consumer);
    }
}
