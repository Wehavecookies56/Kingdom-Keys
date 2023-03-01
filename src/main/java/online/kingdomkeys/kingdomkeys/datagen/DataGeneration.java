package online.kingdomkeys.kingdomkeys.datagen;

import java.util.concurrent.CompletableFuture;

import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import online.kingdomkeys.kingdomkeys.datagen.init.BlockStates;
import online.kingdomkeys.kingdomkeys.datagen.init.BlockTagsGen;
import online.kingdomkeys.kingdomkeys.datagen.init.ItemModels;
import online.kingdomkeys.kingdomkeys.datagen.init.ItemTagsGen;
import online.kingdomkeys.kingdomkeys.datagen.init.KeybladeStats;
import online.kingdomkeys.kingdomkeys.datagen.init.LanguageENGB;
import online.kingdomkeys.kingdomkeys.datagen.init.LanguageENUS;
import online.kingdomkeys.kingdomkeys.datagen.init.LanguageESES;
import online.kingdomkeys.kingdomkeys.datagen.init.LootTables;
import online.kingdomkeys.kingdomkeys.datagen.init.Recipes;
import online.kingdomkeys.kingdomkeys.datagen.init.Sounds;
import online.kingdomkeys.kingdomkeys.datagen.init.SynthesisRecipe;
import online.kingdomkeys.kingdomkeys.datagen.provider.KKAdvancementProvider;

@Mod.EventBusSubscriber(bus=Mod.EventBusSubscriber.Bus.MOD)
public class DataGeneration {

    @SubscribeEvent
    public static void gatherData(GatherDataEvent event) {
    	DataGenerator generator = event.getGenerator();
        CompletableFuture<Provider> lookupProvider = event.getLookupProvider();
        final ExistingFileHelper existingFileHelper = event.getExistingFileHelper();

        //tags
        BlockTagsGen blockTags = new BlockTagsGen(generator.getPackOutput(), existingFileHelper);
        generator.addProvider(event.includeServer(), blockTags);
        generator.addProvider(event.includeServer(), new ItemTagsGen(generator.getPackOutput(), blockTags, existingFileHelper));

        generator.addProvider(new Recipes(generator.getPackOutput()));
        generator.addProvider(event.includeClient(), new BlockStates(generator.getPackOutput(), existingFileHelper));
        generator.addProvider(event.includeClient(), new ItemModels(generator.getPackOutput(), existingFileHelper));
        generator.addProvider(new KeybladeStats(generator.getPackOutput(), existingFileHelper));
        generator.addProvider(new LootTables(generator.getPackOutput()));
        generator.addProvider(new SynthesisRecipe(generator.getPackOutput(), existingFileHelper));
        generator.addProvider(new KKAdvancementProvider(generator.getPackOutput()));
        generator.addProvider(event.includeClient(),new LanguageENUS(generator));
        generator.addProvider(event.includeClient(),new LanguageESES(generator));
        generator.addProvider(event.includeClient(),new LanguageENGB(generator));
        generator.addProvider(event.includeClient(), new Sounds(generator.getPackOutput(), existingFileHelper));
    }
}
