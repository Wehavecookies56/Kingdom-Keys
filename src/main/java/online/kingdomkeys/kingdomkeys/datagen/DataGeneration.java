package online.kingdomkeys.kingdomkeys.datagen;

import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.GatherDataEvent;
import online.kingdomkeys.kingdomkeys.datagen.init.*;
import online.kingdomkeys.kingdomkeys.datagen.provider.KKAdvancementProvider;

@Mod.EventBusSubscriber(bus=Mod.EventBusSubscriber.Bus.MOD)
public class DataGeneration {

    @SubscribeEvent
    public static void gatherData(GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        final ExistingFileHelper existingFileHelper = event.getExistingFileHelper();

        //tags
        BlockTagsGen blockTags = new BlockTagsGen(generator, existingFileHelper);
        generator.addProvider(blockTags);
        generator.addProvider(new ItemTagsGen(generator, blockTags, existingFileHelper));

        generator.addProvider(new Recipes(generator));
        generator.addProvider(new BlockStates(generator, existingFileHelper));
        generator.addProvider(new ItemModels(generator, existingFileHelper));
        generator.addProvider(new KeybladeStats(generator, existingFileHelper));
        generator.addProvider(new LootTables(generator));
        generator.addProvider(new SynthesisRecipe(generator, existingFileHelper));
        generator.addProvider(new KKAdvancementProvider(generator));
    }
}
