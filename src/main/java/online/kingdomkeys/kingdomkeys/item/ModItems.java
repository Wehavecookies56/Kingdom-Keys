package online.kingdomkeys.kingdomkeys.item;

import net.minecraft.item.Item;

import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import online.kingdomkeys.kingdomkeys.KingdomKeys;

public class ModItems {

    public static Item kingdomKey, kingdomKeyD;

    @Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class Registry {

        @SubscribeEvent
        public static void registerItems(final RegistryEvent.Register<Item> event) {
            //Generic property that will cover most standard items
            Item.Properties genericItemProperties = new Item.Properties().group(KingdomKeys.miscGroup);

            //Register and set item references here, no array needed here as only 1 thing needs to be registered
            event.getRegistry().registerAll(
                    kingdomKey = new ItemKeyblade("kingdom_key", 3, 1.0F),
                    kingdomKeyD = new ItemKeyblade("kingdom_key_d", 3, 1.0F)
            );
        }

        //Helper method to create item with the properties and registry name
        public static Item createNewItem(String name, Item.Properties properties) {
            return new Item(properties).setRegistryName(KingdomKeys.MODID, name);
        }

    }
}
