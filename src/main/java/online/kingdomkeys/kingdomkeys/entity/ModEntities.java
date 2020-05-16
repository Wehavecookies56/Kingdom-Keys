package online.kingdomkeys.kingdomkeys.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.network.FMLPlayMessages;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.block.ModBlocks;
import online.kingdomkeys.kingdomkeys.client.render.BlastBloxRenderer;
import online.kingdomkeys.kingdomkeys.client.render.EntityMunnyRenderer;
import online.kingdomkeys.kingdomkeys.client.render.entity.MoogleRenderer;
import online.kingdomkeys.kingdomkeys.client.render.magic.*;
import online.kingdomkeys.kingdomkeys.entity.block.BlastBloxEntity;
import online.kingdomkeys.kingdomkeys.entity.block.MagnetBloxTileEntity;
import online.kingdomkeys.kingdomkeys.entity.magic.*;
import online.kingdomkeys.kingdomkeys.entity.mob.MoogleEntity;
import online.kingdomkeys.kingdomkeys.proxy.ProxyClient;

import java.util.function.BiFunction;

public class ModEntities {
    public static final DeferredRegister<EntityType<?>> ENTITIES = new DeferredRegister<>(ForgeRegistries.ENTITIES, KingdomKeys.MODID);

    public static final RegistryObject<EntityType<BlastBloxEntity>> TYPE_BLAST_BLOX = createEntityType(BlastBloxEntity::new, BlastBloxEntity::new, EntityClassification.MISC,"blast_blox_primed", 0.98F, 0.98F);
    public static final RegistryObject<EntityType<EntityMunny>> TYPE_MUNNY = createEntityType(EntityMunny::new, EntityMunny::new, EntityClassification.MISC,"entity_munny", 0.5F, 0.5F);
    public static final RegistryObject<EntityType<EntityHPOrb>> TYPE_HPORB = createEntityType(EntityHPOrb::new, EntityHPOrb::new, EntityClassification.MISC,"entity_hp_orb", 0.5F, 0.5F);
    
    public static final RegistryObject<EntityType<FireEntity>> TYPE_FIRE = createEntityType(FireEntity::new, FireEntity::new, EntityClassification.MISC,"entity_fire", 0.5F, 0.5F);
    public static final RegistryObject<EntityType<BlizzardEntity>> TYPE_BLIZZARD = createEntityType(BlizzardEntity::new, BlizzardEntity::new, EntityClassification.MISC,"entity_blizzard", 0.5F, 0.5F);
    public static final RegistryObject<EntityType<WaterEntity>> TYPE_WATER = createEntityType(WaterEntity::new, WaterEntity::new, EntityClassification.MISC,"entity_water", 0.5F, 0.5F);
    public static final RegistryObject<EntityType<ThunderEntity>> TYPE_THUNDER = createEntityType(ThunderEntity::new, ThunderEntity::new, EntityClassification.MISC,"entity_thunder", 0.5F, 0.5F);
    //public static final RegistryObject<EntityType<CureEntity>> TYPE_CURE = createEntityType(CureEntity::new, CureEntity::new, EntityClassification.MISC,"entity_thunder", 0.5F, 0.5F);
    public static final RegistryObject<EntityType<MagnetEntity>> TYPE_MAGNET = createEntityType(MagnetEntity::new, MagnetEntity::new, EntityClassification.MISC,"entity_magnet", 0.5F, 0.5F);
    //public static final RegistryObject<EntityType<ReflectEntity>> TYPE_REFLECT = createEntityType(ReflectEntity::new, ReflectEntity::new, EntityClassification.MISC,"entity_reflect", 4F, 3F),
    public static final RegistryObject<EntityType<GravityEntity>> TYPE_GRAVITY = createEntityType(GravityEntity::new, GravityEntity::new, EntityClassification.MISC,"entity_gravity", 0.5F, 0.5F);
    //public static final RegistryObject<EntityType<StopEntity>> TYPE_STOP = createEntityType(StopEntity::new, StopEntity::new, EntityClassification.MISC,"entity_stop", 0.5F, 0.5F);
    
    //Mobs
    // public static EntityType<EntityShadow> TYPE_HEARTLESS_SHADOW = createEntityType(EntityShadow.class, EntityShadow::new, "shadow");
    public static final RegistryObject<EntityType<MoogleEntity>> TYPE_MOOGLE = createEntityType(MoogleEntity::new, MoogleEntity::new, EntityClassification.CREATURE, "moogle", 0.6F, 1.5F);

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
    public static <T extends Entity, M extends EntityType<T>>RegistryObject<EntityType<T>> createEntityType(EntityType.IFactory<T> factory, BiFunction<FMLPlayMessages.SpawnEntity, World, T> clientFactory, EntityClassification classification, String name, float sizeX, float sizeY) {
        EntityType<T> type = EntityType.Builder.create(factory, classification)
                .setCustomClientFactory(clientFactory)
                .setShouldReceiveVelocityUpdates(true)
                .setUpdateInterval(1)
                .setTrackingRange(128)
                .size(sizeX, sizeY)
                .build(name);
        return ENTITIES.register(name, () -> type);
    }

    /**
     * Register the render classes for the entities, called in {@link ProxyClient#registerModels(ModelRegistryEvent)}  }
     */
    @OnlyIn(Dist.CLIENT)
    public static void registerModels() {
        RenderingRegistry.registerEntityRenderingHandler(TYPE_BLAST_BLOX.get(), BlastBloxRenderer.FACTORY);
        RenderingRegistry.registerEntityRenderingHandler(TYPE_MUNNY.get(), EntityMunnyRenderer.FACTORY);
        RenderingRegistry.registerEntityRenderingHandler(TYPE_HPORB.get(), EntityMunnyRenderer.FACTORY);
        
        RenderingRegistry.registerEntityRenderingHandler(TYPE_FIRE.get(), EntityFireRenderer.FACTORY);
        RenderingRegistry.registerEntityRenderingHandler(TYPE_BLIZZARD.get(), EntityBlizzardRenderer.FACTORY);
        RenderingRegistry.registerEntityRenderingHandler(TYPE_THUNDER.get(), EntityThunderRenderer.FACTORY);
        RenderingRegistry.registerEntityRenderingHandler(TYPE_MAGNET.get(), EntityMagnetRenderer.FACTORY);
        RenderingRegistry.registerEntityRenderingHandler(TYPE_WATER.get(), EntityWaterRenderer.FACTORY);
        RenderingRegistry.registerEntityRenderingHandler(TYPE_GRAVITY.get(), EntityGravityRenderer.FACTORY);
        RenderingRegistry.registerEntityRenderingHandler(TYPE_MOOGLE.get(), MoogleRenderer.FACTORY);
    }

    public static final DeferredRegister<TileEntityType<?>> TILE_ENTITIES = new DeferredRegister<>(ForgeRegistries.TILE_ENTITIES, KingdomKeys.MODID);

    //public static final RegistryObject<TileEntityType<MagnetBloxTileEntity>> TYPE_MAGNET_BLOX = createTileEntityType("magnet_blox", MagnetBloxTileEntity::new, ModBlocks.magnetBlox);
    public static final RegistryObject<TileEntityType<MagnetBloxTileEntity>> TYPE_MAGNET_BLOX = TILE_ENTITIES.register("magnet_blox", () -> TileEntityType.Builder.create(MagnetBloxTileEntity::new, ModBlocks.magnetBlox.get()).build(null));

}
