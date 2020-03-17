package online.kingdomkeys.kingdomkeys.datagen;

import net.minecraft.block.Blocks;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.IFinishedRecipe;
import net.minecraft.data.RecipeProvider;
import net.minecraft.data.ShapedRecipeBuilder;
import net.minecraft.item.Items;
import net.minecraftforge.common.Tags;
import online.kingdomkeys.kingdomkeys.block.ModBlocks;
import online.kingdomkeys.kingdomkeys.handler.DataGeneration;

import java.util.function.Consumer;

public class Recipes extends RecipeProvider {
    DataGenerator dataGenerator;
    public Recipes(DataGenerator generatorIn) {
        super(generatorIn);
        this.dataGenerator = generatorIn;
    }

    @Override
    protected void registerRecipes(Consumer<IFinishedRecipe> consumer) {
        ShapedRecipeBuilder.shapedRecipe(ModBlocks.normalBlox).key('x', Tags.Items.STONE)
                .key('#', Items.DIRT).patternLine("#x").patternLine("x#").build(consumer);
        ShapedRecipeBuilder.shapedRecipe(ModBlocks.hardBlox).patternLine("#x").patternLine("x#")
                .key('x', Tags.Items.STONE).key('#', ModBlocks.normalBlox).build(consumer);
        ShapedRecipeBuilder.shapedRecipe(ModBlocks.metalBlox).patternLine("#x").patternLine("x#")
                .key('x', Tags.Items.INGOTS_IRON).key('#', ModBlocks.hardBlox).build(consumer);
        ShapedRecipeBuilder.shapedRecipe(ModBlocks.ghostBlox).patternLine("GNG").patternLine("GRG").patternLine("GNG")
                .key('G', Tags.Items.GLASS).key('N', ModBlocks.normalBlox)
                .key('R', Blocks.REDSTONE_BLOCK).build(consumer);
        ShapedRecipeBuilder.shapedRecipe(ModBlocks.ghostBlox).patternLine("NLN").patternLine("NTN").patternLine("NTN")
                .key('T', Blocks.TNT).key('N', ModBlocks.normalBlox)
                .key('L', Items.LAVA_BUCKET).build(consumer);
        ShapedRecipeBuilder.shapedRecipe(ModBlocks.ghostBlox).patternLine("NNN").patternLine("NSN").patternLine("NNN")
                .key('S', Blocks.SLIME_BLOCK).key('N', ModBlocks.normalBlox).build(consumer);
    }
}
