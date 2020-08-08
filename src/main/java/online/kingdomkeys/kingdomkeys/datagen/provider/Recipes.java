package online.kingdomkeys.kingdomkeys.datagen.provider;

import net.minecraft.advancements.criterion.InventoryChangeTrigger;
import net.minecraft.block.Blocks;
import net.minecraft.data.*;
import net.minecraft.item.Items;
import net.minecraftforge.common.Tags;
import online.kingdomkeys.kingdomkeys.block.ModBlocks;
import online.kingdomkeys.kingdomkeys.item.ModItems;

import java.util.function.Consumer;

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
		        .patternLine(" G ")
		        .patternLine("GDG")
		        .patternLine(" G ")
		        .key('D', Tags.Items.DYES)
		        .key('G', Blocks.GLASS)
		        .setGroup("kingdomkeys")
		        .addCriterion("glass", InventoryChangeTrigger.Instance.forItems(Blocks.GLASS))
		        .build(consumer);


        //items
        ShapelessRecipeBuilder.shapelessRecipe(ModItems.iceCream.get())
        		.addIngredient(Tags.Items.RODS_WOODEN)
        		.addIngredient(Items.SUGAR)
                .addIngredient(Items.WATER_BUCKET)
                .addIngredient(Blocks.ICE)
                .addCriterion("ice", InventoryChangeTrigger.Instance.forItems(Blocks.ICE))
                .build(consumer);

        ShapedRecipeBuilder.shapedRecipe(ModItems.emptyBottle.get())
		        .patternLine("G G")
		        .patternLine("GBG")
		        .patternLine("GGG")
                .key('G', Tags.Items.GLASS).key('B', Items.GLASS_BOTTLE)
                .setGroup("kingdomkeys")
                .addCriterion("glass_bottle", InventoryChangeTrigger.Instance.forItems(Items.GLASS_BOTTLE))
                .build(consumer);

        /*// bags
        ShapedRecipeBuilder.shapedRecipe(ModItems.synthesisBagS).patternLine("LSL").patternLine("L L").patternLine("LLL")
                .key('S', Items.STRING).key('L', Items.LEATHER).setGroup("kingdomkeys")
                .addCriterion("leather", InventoryChangeTrigger.Instance.forItems(Items.LEATHER))
                .build(consumer);

        ShapedRecipeBuilder.shapedRecipe(ModItems.synthesisBagM).patternLine("LSL").patternLine("LBL").patternLine("LLL")
                .key('S', Items.STRING).key('L', Items.LEATHER).key('B', ModItems.synthesisBagS)
                .setGroup("kingdomkeys").addCriterion("bag", InventoryChangeTrigger.Instance.forItems(ModItems.synthesisBagS))
                .build(consumer);

        ShapedRecipeBuilder.shapedRecipe(ModItems.synthesisBagL).patternLine("LSL").patternLine("LBL").patternLine("LLL")
                .key('S', Items.STRING).key('L', Items.LEATHER).key('B', ModItems.synthesisBagM)
                .setGroup("kingdomkeys").addCriterion("bag", InventoryChangeTrigger.Instance.forItems(ModItems.synthesisBagM))
                .build(consumer);

        // util blocks
        ShapedRecipeBuilder.shapedRecipe(ModBlocks.kkChest).patternLine("HGH").patternLine("GCG").patternLine("HGH")
                .key('G', Tags.Items.INGOTS_GOLD).key('C', Tags.Items.CHESTS).key('H', ModItems.heart)
                .setGroup("kingdomkeys").addCriterion("chest", InventoryChangeTrigger.Instance.forItems(Blocks.CHEST))
                .build(consumer);

        ShapedRecipeBuilder.shapedRecipe(ModBlocks.orgPortal).patternLine(" D ").patternLine("DKD").patternLine("OOO")
                .key('K', ModItems.kingdomHearts).key('D', ModItems.darkHeart).key('O', Tags.Items.OBSIDIAN)
                .setGroup("kingdomkeys").addCriterion("obsidian", InventoryChangeTrigger.Instance.forItems(Blocks.OBSIDIAN))
                .build(consumer);

        ShapedRecipeBuilder.shapedRecipe(ModBlocks.pedestal).patternLine(" H ").patternLine("HBH").patternLine("BBB")
                .key('B', ModBlocks.metalBlox).key('H', ModItems.heart)
                .setGroup("kingdomkeys").addCriterion("metalblox", InventoryChangeTrigger.Instance.forItems(ModBlocks.metalBlox))
                .build(consumer);*/
    }
}
