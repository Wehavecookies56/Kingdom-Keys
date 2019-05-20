package online.kingdomkeys.kingdomkeys.synthesis.material;

import net.minecraft.init.Items;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryBuilder;
import online.kingdomkeys.kingdomkeys.lib.Reference;

public class ModMaterials {

    //TODO should be an API thing
    public static IForgeRegistry<Material> registry;

    @Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class Registry {

        @SubscribeEvent
        public static void registerMaterialRegistry(RegistryEvent.NewRegistry event) {
            //Create material registry
            registry = new RegistryBuilder<Material>().setName(new ResourceLocation(Reference.MODID, "materials")).setType(Material.class).create();
        }

        @SubscribeEvent
        public static void registerMaterials(RegistryEvent.Register<Material> event) {
            //PLACEHOLDER MATERIAL
            event.getRegistry().register(new Material("kingdomkeys:material_apple", Items.APPLE));
        }

    }

}
