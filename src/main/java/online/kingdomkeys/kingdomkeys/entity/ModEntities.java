package online.kingdomkeys.kingdomkeys.entity;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.world.World;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.network.FMLPlayMessages;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.block.ModBlocks;
import online.kingdomkeys.kingdomkeys.client.render.BlastBloxRenderer;
import online.kingdomkeys.kingdomkeys.client.render.EntityMunnyRenderer;
import online.kingdomkeys.kingdomkeys.client.render.magics.*;
import online.kingdomkeys.kingdomkeys.entity.block.BlastBloxEntity;
import online.kingdomkeys.kingdomkeys.entity.block.MagnetBloxTileEntity;
import online.kingdomkeys.kingdomkeys.entity.magic.*;
import online.kingdomkeys.kingdomkeys.proxy.ProxyClient;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Supplier;

public class ModEntities {

    private static List<EntityType> ENTITIES = new ArrayList<>();

    public static EntityType<BlastBloxEntity> TYPE_BLAST_BLOX = createEntityType(BlastBloxEntity::new, BlastBloxEntity::new, EntityClassification.MISC,"blast_blox_primed", 0.98F, 0.98F);
    
    public static EntityType<EntityItemDrop> TYPE_MUNNY = createEntityType(EntityMunny::new, EntityMunny::new, EntityClassification.MISC,"entity_munny", 0.5F, 0.5F);
    public static EntityType<EntityItemDrop> TYPE_HPORB = createEntityType(EntityHPOrb::new, EntityHPOrb::new, EntityClassification.MISC,"entity_hp_orb", 0.5F, 0.5F);

    //Magic
    public static EntityType<EntityFire> TYPE_FIRE = createEntityType(EntityFire::new, EntityFire::new, EntityClassification.MISC,"entity_fire", 0.5F, 0.5F);
    public static EntityType<EntityBlizzard> TYPE_BLIZZARD = createEntityType(EntityBlizzard::new, EntityBlizzard::new, EntityClassification.MISC,"entity_blizzard", 0.5F, 0.5F);
    public static EntityType<EntityWater> TYPE_WATER = createEntityType(EntityWater::new, EntityWater::new, EntityClassification.MISC,"entity_water", 0.5F, 0.5F);
    public static EntityType<EntityThunder> TYPE_THUNDER = createEntityType(EntityThunder::new, EntityThunder::new, EntityClassification.MISC,"entity_thunder", 0.5F, 0.5F);
    //public static EntityType<EntityBlizzard> TYPE_CURE = createEntityType(EntityCure::new, EntityCure::new, EntityClassification.MISC,"entity_thunder", 0.5F, 0.5F);
    public static EntityType<EntityMagnet> TYPE_MAGNET = createEntityType(EntityMagnet::new, EntityMagnet::new, EntityClassification.MISC,"entity_magnet", 0.5F, 0.5F);
    //public static EntityType<EntityReflect> TYPE_REFLECT = createEntityType(EntityReflect::new, EntityReflect::new, EntityClassification.MISC,"entity_reflect", 4F, 3F);
    public static EntityType<EntityGravity> TYPE_GRAVITY = createEntityType(EntityGravity::new, EntityGravity::new, EntityClassification.MISC,"entity_gravity", 0.5F, 0.5F);
    //public static EntityType<EntityStop> TYPE_STOP = createEntityType(EntityStop::new, EntityStop::new, EntityClassification.MISC,"entity_stop", 0.5F, 0.5F);
    
    //Mobs
    // public static EntityType<EntityShadow> TYPE_HEARTLESS_SHADOW = createEntityType(EntityShadow.class, EntityShadow::new, "shadow");

    /**
     * Helper method to create a new EntityType and set the registry name
     * @param factory The entity type factory
     * @param clientFactory The client factory
     * @param classification The classification of the entity
     * @param name The registry name of the entity
     * @param sizeX The X size of the entity
     * @param sizeY The Y size of the entity
     * @param <T> The entity type
     * @return The EntityType created
     */
    public static <T extends Entity>EntityType<T> createEntityType(EntityType.IFactory<T> factory, BiFunction<FMLPlayMessages.SpawnEntity, World, T> clientFactory, EntityClassification classification, String name, float sizeX, float sizeY) {
        EntityType<T> type = EntityType.Builder.create(factory, classification)
                .setCustomClientFactory(clientFactory)
                .setShouldReceiveVelocityUpdates(true)
                .setUpdateInterval(1)
                .setTrackingRange(128)
                .size(sizeX, sizeY)
                .build(name);
        type.setRegistryName(KingdomKeys.MODID, name);
        ENTITIES.add(type);
        return type;
    }

    /**
     * Register the render classes for the entities, called in {@link ProxyClient#registerModels(ModelRegistryEvent)}  }
     */
    public static void registerModels() {
        RenderingRegistry.registerEntityRenderingHandler(TYPE_BLAST_BLOX, BlastBloxRenderer.FACTORY);
        
        RenderingRegistry.registerEntityRenderingHandler(TYPE_MUNNY, EntityMunnyRenderer.FACTORY);
        RenderingRegistry.registerEntityRenderingHandler(TYPE_HPORB, EntityMunnyRenderer.FACTORY);

        
        RenderingRegistry.registerEntityRenderingHandler(TYPE_FIRE, EntityFireRenderer.FACTORY);
        RenderingRegistry.registerEntityRenderingHandler(TYPE_BLIZZARD, EntityBlizzardRenderer.FACTORY);
        RenderingRegistry.registerEntityRenderingHandler(TYPE_THUNDER, EntityThunderRenderer.FACTORY);
        RenderingRegistry.registerEntityRenderingHandler(TYPE_MAGNET, EntityMagnetRenderer.FACTORY);
        RenderingRegistry.registerEntityRenderingHandler(TYPE_WATER, EntityWaterRenderer.FACTORY);
        RenderingRegistry.registerEntityRenderingHandler(TYPE_GRAVITY, EntityGravityRenderer.FACTORY);
    }

    private static List<TileEntityType> TILE_ENTITIES = new ArrayList<>();

    public static TileEntityType<MagnetBloxTileEntity> TYPE_MAGNET_BLOX = createTileEntityType("magnet_blox", MagnetBloxTileEntity::new, ModBlocks.magnetBlox);

    public static <T extends TileEntity>TileEntityType<T> createTileEntityType(String registryName, Supplier<T> factory, Block... blocks) {
        TileEntityType<T> type = TileEntityType.Builder.create(factory, blocks).build(null);
        type.setRegistryName(KingdomKeys.MODID, registryName);
        TILE_ENTITIES.add(type);
        return type;
    }

    @Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class Registry {

        @SubscribeEvent
        public static void registerEntities(RegistryEvent.Register<EntityType<?>> event) {
            ENTITIES.forEach((entityType)->event.getRegistry().register(entityType));
        }

        @SubscribeEvent
        public static void registerTileEntities(RegistryEvent.Register<TileEntityType<?>> event) {
            TILE_ENTITIES.forEach((tileEntityType->event.getRegistry().register(tileEntityType)));
        }

    }

}
