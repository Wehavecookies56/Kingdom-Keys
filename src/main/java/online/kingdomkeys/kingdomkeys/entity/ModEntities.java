package online.kingdomkeys.kingdomkeys.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.world.World;

import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod;

import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.client.render.RenderEntityBlastBloxPrimed;
import online.kingdomkeys.kingdomkeys.entity.mobs.heartless.EntityShadow;

import java.util.function.Function;

public class ModEntities {

    public static EntityType<EntityBlastBloxPrimed> TYPE_BLAST_BLOX = createEntityType(EntityBlastBloxPrimed.class, EntityBlastBloxPrimed::new, "blast_blox_primed");
   // public static EntityType<EntityShadow> TYPE_HEARTLESS_SHADOW = createEntityType(EntityShadow.class, EntityShadow::new, "shadow");

    /**
     * Helper method to create a new EntityType and set the registry name
     * @param entityClassIn The entity class
     * @param factoryIn The render factory
     * @param name The registry name of the entity
     * @param <T> The entity type
     * @return The EntityType created
     */
    public static <T extends Entity>EntityType<T> createEntityType(Class<? extends T> entityClassIn, Function<? super World, ? extends T> factoryIn, String name) {
        EntityType<T> type = EntityType.Builder.create(entityClassIn, factoryIn).tracker(100, 1, true).build(name);
        type.setRegistryName(KingdomKeys.MODID, name);
        return type;
    }

    /**
     * Register the render classes for the entities, called in {@link online.kingdomkeys.kingdomkeys.proxy.ClientProxy#registerModels(ModelRegistryEvent)}  }
     */
    public static void registerModels() {
        RenderingRegistry.registerEntityRenderingHandler(EntityBlastBloxPrimed.class, RenderEntityBlastBloxPrimed.FACTORY);
    }

    @Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class Registry {

        @SubscribeEvent
        public static void registerEntities(RegistryEvent.Register<EntityType<?>> event) {
            event.getRegistry().register(TYPE_BLAST_BLOX);
          //  event.getRegistry().register(TYPE_HEARTLESS_SHADOW);
        }

    }

}
