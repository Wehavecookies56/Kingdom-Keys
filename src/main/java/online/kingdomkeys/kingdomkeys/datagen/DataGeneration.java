package online.kingdomkeys.kingdomkeys.datagen;

import java.util.Collections;
import java.util.Set;

import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.DatapackBuiltinEntriesProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.damagesource.KKDamageTypes;
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

@Mod.EventBusSubscriber(bus=Mod.EventBusSubscriber.Bus.MOD)
public class DataGeneration {

	 private static final RegistrySetBuilder BUILDER = new RegistrySetBuilder()
	            .add(Registries.DAMAGE_TYPE, KKDamageTypes::bootstrap);
	 
    @SubscribeEvent
    public static void gatherData(GatherDataEvent event) {
    	DataGenerator generator = event.getGenerator();
        PackOutput output = generator.getPackOutput();

        final ExistingFileHelper existingFileHelper = event.getExistingFileHelper();

        //Datapack Registry providers
        generator.addProvider(event.includeServer(), new DatapackBuiltinEntriesProvider(output, event.getLookupProvider(), BUILDER, Set.of(KingdomKeys.MODID)));
       
        //tags
        BlockTagsGen blockTags = new BlockTagsGen(generator, event.getLookupProvider(), existingFileHelper);
        generator.addProvider(event.includeServer(), blockTags);
        generator.addProvider(event.includeServer(), new ItemTagsGen(generator.getPackOutput(), event.getLookupProvider(), blockTags.contentsGetter(), existingFileHelper));
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
        
       // generator.addProvider(event.includeServer(), new DatapackBuiltinEntriesProvider(output, event.getLookupProvider(), BUILDER, Set.of(KingdomKeys.MODID)));

    }
}
