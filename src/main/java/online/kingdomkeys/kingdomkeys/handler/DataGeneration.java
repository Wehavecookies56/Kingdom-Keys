package online.kingdomkeys.kingdomkeys.handler;

import net.minecraft.data.DataGenerator;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.GatherDataEvent;
import online.kingdomkeys.kingdomkeys.datagen.Recipes;

@Mod.EventBusSubscriber(bus=Mod.EventBusSubscriber.Bus.MOD)
public class DataGeneration {

    @SubscribeEvent
    public static void data(GatherDataEvent event) {

        DataGenerator generator = event.getGenerator();
        generator.addProvider(new Recipes(generator));
    }
}
