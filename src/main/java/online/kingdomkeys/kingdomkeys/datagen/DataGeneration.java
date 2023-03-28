package online.kingdomkeys.kingdomkeys.datagen;

import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import online.kingdomkeys.kingdomkeys.datagen.init.BlockModels;
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

import java.util.Collections;

@Mod.EventBusSubscriber(bus=Mod.EventBusSubscriber.Bus.MOD)
public class DataGeneration {

    @SubscribeEvent
    public static void gatherData(GatherDataEvent event) {
    	DataGenerator generator = event.getGenerator();
        final ExistingFileHelper existingFileHelper = event.getExistingFileHelper();

        //tags
        BlockTagsGen blockTags = new BlockTagsGen(generator, event.getLookupProvider(), existingFileHelper);
        generator.addProvider(event.includeServer(), blockTags);
        generator.addProvider(event.includeServer(), new ItemTagsGen(generator.getPackOutput(), event.getLookupProvider(), blockTags, existingFileHelper));
        generator.addProvider(event.includeServer(), new Recipes(generator));
        generator.addProvider(event.includeClient(), new BlockStates(generator, existingFileHelper));
        generator.addProvider(event.includeClient(), new ItemModels(generator, existingFileHelper));
        generator.addProvider(event.includeClient(), new BlockModels(generator, existingFileHelper));
        generator.addProvider(event.includeServer(), new KeybladeStats(generator, existingFileHelper));
        generator.addProvider(event.includeServer(), new LootTables(generator, Collections.emptySet(), Collections.emptyList()));
        generator.addProvider(event.includeServer(), new SynthesisRecipe(generator, existingFileHelper));
        //probably should use the forge provider generator.addProvider(event.includeServer(), new KKAdvancementProvider(generator.getPackOutput(), event.getLookupProvider(), ));
        generator.addProvider(event.includeClient(), new LanguageENUS(generator));
        generator.addProvider(event.includeClient(), new LanguageESES(generator));
        generator.addProvider(event.includeClient(), new LanguageENGB(generator));
        generator.addProvider(event.includeClient(), new Sounds(generator, existingFileHelper));
    }
}
