package online.kingdomkeys.kingdomkeys.entity;

import java.util.function.BiFunction;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.network.FMLPlayMessages;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.block.ModBlocks;
import online.kingdomkeys.kingdomkeys.client.render.BlastBloxRenderer;
import online.kingdomkeys.kingdomkeys.client.render.MunnyRenderer;
import online.kingdomkeys.kingdomkeys.client.render.PairBloxRenderer;
import online.kingdomkeys.kingdomkeys.client.render.TESRPedestal;
import online.kingdomkeys.kingdomkeys.client.render.entity.DarkballRenderer;
import online.kingdomkeys.kingdomkeys.client.render.entity.GigaShadowRenderer;
import online.kingdomkeys.kingdomkeys.client.render.entity.LargeBodyRenderer;
import online.kingdomkeys.kingdomkeys.client.render.entity.MegaShadowRenderer;
import online.kingdomkeys.kingdomkeys.client.render.entity.MoogleRenderer;
import online.kingdomkeys.kingdomkeys.client.render.entity.ShadowGlobRenderer;
import online.kingdomkeys.kingdomkeys.client.render.entity.ShadowRenderer;
import online.kingdomkeys.kingdomkeys.client.render.entity.drops.DriveOrbRenderer;
import online.kingdomkeys.kingdomkeys.client.render.entity.drops.HPOrbRenderer;
import online.kingdomkeys.kingdomkeys.client.render.entity.drops.MPOrbRenderer;
import online.kingdomkeys.kingdomkeys.client.render.magic.EntityBlizzardRenderer;
import online.kingdomkeys.kingdomkeys.client.render.magic.EntityFireRenderer;
import online.kingdomkeys.kingdomkeys.client.render.magic.EntityGravityRenderer;
import online.kingdomkeys.kingdomkeys.client.render.magic.EntityMagnetRenderer;
import online.kingdomkeys.kingdomkeys.client.render.magic.EntityThunderRenderer;
import online.kingdomkeys.kingdomkeys.client.render.magic.EntityWaterRenderer;
import online.kingdomkeys.kingdomkeys.entity.block.BlastBloxEntity;
import online.kingdomkeys.kingdomkeys.entity.block.MagnetBloxTileEntity;
import online.kingdomkeys.kingdomkeys.entity.block.PairBloxEntity;
import online.kingdomkeys.kingdomkeys.entity.block.PedestalTileEntity;
import online.kingdomkeys.kingdomkeys.entity.magic.BlizzardEntity;
import online.kingdomkeys.kingdomkeys.entity.magic.FireEntity;
import online.kingdomkeys.kingdomkeys.entity.magic.GravityEntity;
import online.kingdomkeys.kingdomkeys.entity.magic.MagnetEntity;
import online.kingdomkeys.kingdomkeys.entity.magic.ThunderEntity;
import online.kingdomkeys.kingdomkeys.entity.magic.WaterEntity;
import online.kingdomkeys.kingdomkeys.entity.mob.DarkballEntity;
import online.kingdomkeys.kingdomkeys.entity.mob.GigaShadowEntity;
import online.kingdomkeys.kingdomkeys.entity.mob.LargeBodyEntity;
import online.kingdomkeys.kingdomkeys.entity.mob.MegaShadowEntity;
import online.kingdomkeys.kingdomkeys.entity.mob.MoogleEntity;
import online.kingdomkeys.kingdomkeys.entity.mob.ShadowEntity;
import online.kingdomkeys.kingdomkeys.entity.mob.ShadowGlobEntity;
import online.kingdomkeys.kingdomkeys.proxy.ProxyClient;

public class ModEntities {
    public static final DeferredRegister<EntityType<?>> ENTITIES = new DeferredRegister<>(ForgeRegistries.ENTITIES, KingdomKeys.MODID);

    public static final RegistryObject<EntityType<BlastBloxEntity>> TYPE_BLAST_BLOX = createEntityType(BlastBloxEntity::new, BlastBloxEntity::new, EntityClassification.MISC,"blast_blox_primed", 0.98F, 0.98F);

    public static final RegistryObject<EntityType<PairBloxEntity>> TYPE_PAIR_BLOX = createEntityType(PairBloxEntity::new, PairBloxEntity::new, EntityClassification.MISC,"pair_blox", 1F, 1F);
    public static final RegistryObject<EntityType<MunnyEntity>> TYPE_MUNNY = createEntityType(MunnyEntity::new, MunnyEntity::new, EntityClassification.MISC,"entity_munny", 0.25F, 0.25F);
    public static final RegistryObject<EntityType<HPOrbEntity>> TYPE_HPORB = createEntityType(HPOrbEntity::new, HPOrbEntity::new, EntityClassification.MISC,"entity_hp_orb", 0.25F, 0.25F);
    public static final RegistryObject<EntityType<MPOrbEntity>> TYPE_MPORB = createEntityType(MPOrbEntity::new, MPOrbEntity::new, EntityClassification.MISC,"entity_mp_orb", 0.25F, 0.25F);
    public static final RegistryObject<EntityType<DriveOrbEntity>> TYPE_DRIVEORB = createEntityType(DriveOrbEntity::new, DriveOrbEntity::new, EntityClassification.MISC,"entity_drive_orb", 0.25F, 0.25F);

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
    
    //Pureblood
    public static final RegistryObject<EntityType<ShadowEntity>> TYPE_SHADOW = createEntityType(ShadowEntity::new, ShadowEntity::new, EntityClassification.CREATURE, "shadow", 0.5F, 0.7F);
    public static final RegistryObject<EntityType<MegaShadowEntity>> TYPE_MEGA_SHADOW = createEntityType(MegaShadowEntity::new, MegaShadowEntity::new, EntityClassification.CREATURE, "mega_shadow", 1.5F, 1.7F);
    public static final RegistryObject<EntityType<GigaShadowEntity>> TYPE_GIGA_SHADOW = createEntityType(GigaShadowEntity::new, GigaShadowEntity::new, EntityClassification.CREATURE, "giga_shadow", 2.5F, 2.7F);
    public static final RegistryObject<EntityType<DarkballEntity>> TYPE_DARKBALL = createEntityType(DarkballEntity::new, DarkballEntity::new, EntityClassification.CREATURE, "darkball", 1.3F, 1.7F);
    public static final RegistryObject<EntityType<ShadowGlobEntity>> TYPE_SHADOW_GLOB = createEntityType(ShadowGlobEntity::new, ShadowGlobEntity::new, EntityClassification.CREATURE, "shadow_glob", 1.5F, 1F);
    
    //Emblems
   /* public static final RegistryObject<EntityType<ShadowEntity>> TYPE_RED_NOCTURNE = createEntityType(ShadowEntity::new, ShadowEntity::new, EntityClassification.CREATURE, "red_nocturne", 0.5F, 0.5F);
    public static final RegistryObject<EntityType<ShadowEntity>> TYPE_BLUE_RHAPSODY = createEntityType(ShadowEntity::new, ShadowEntity::new, EntityClassification.CREATURE, "blue_rhapsody", 0.5F, 0.5F);
    public static final RegistryObject<EntityType<ShadowEntity>> TYPE_YELLOW_OPERA = createEntityType(ShadowEntity::new, ShadowEntity::new, EntityClassification.CREATURE, "yellow_opera", 0.5F, 0.5F);
    public static final RegistryObject<EntityType<ShadowEntity>> TYPE_GREEN_REQUIEM= createEntityType(ShadowEntity::new, ShadowEntity::new, EntityClassification.CREATURE, "green_requiem", 0.5F, 0.5F);
    public static final RegistryObject<EntityType<ShadowEntity>> TYPE_SILVER_ROCK = createEntityType(ShadowEntity::new, ShadowEntity::new, EntityClassification.CREATURE, "silver_rock", 0.5F, 0.5F);
    public static final RegistryObject<EntityType<ShadowEntity>> TYPE_CRIMSON_JAZZ = createEntityType(ShadowEntity::new, ShadowEntity::new, EntityClassification.CREATURE, "crimson_jazz", 0.5F, 0.5F);
    public static final RegistryObject<EntityType<ShadowEntity>> TYPE_EMERALD_BLUES = createEntityType(ShadowEntity::new, ShadowEntity::new, EntityClassification.CREATURE, "emerald_blues", 0.5F, 0.5F);
    public static final RegistryObject<EntityType<ShadowEntity>> TYPE_MINUTE_BOMB = createEntityType(ShadowEntity::new, ShadowEntity::new, EntityClassification.CREATURE, "minute_bomb", 0.5F, 0.5F);
    public static final RegistryObject<EntityType<ShadowEntity>> TYPE_SKATER_BOMB = createEntityType(ShadowEntity::new, ShadowEntity::new, EntityClassification.CREATURE, "skater_bomb", 0.5F, 0.5F);
    public static final RegistryObject<EntityType<ShadowEntity>> TYPE_STORM_BOMB = createEntityType(ShadowEntity::new, ShadowEntity::new, EntityClassification.CREATURE, "storm_bomb", 0.5F, 0.5F);
    public static final RegistryObject<EntityType<ShadowEntity>> TYPE_DETONATOR = createEntityType(ShadowEntity::new, ShadowEntity::new, EntityClassification.CREATURE, "detonator", 0.5F, 0.5F);*/
    public static final RegistryObject<EntityType<LargeBodyEntity>> TYPE_LARGE_BODY = createEntityType(LargeBodyEntity::new, LargeBodyEntity::new, EntityClassification.CREATURE, "large_body", 1F, 1.5F);
   /* public static final RegistryObject<EntityType<ShadowEntity>> TYPE_WHITE_MUSHROOM = createEntityType(ShadowEntity::new, ShadowEntity::new, EntityClassification.CREATURE, "white_mushroom", 0.5F, 0.5F);
    public static final RegistryObject<EntityType<ShadowEntity>> TYPE_DIRE_PLANT = createEntityType(ShadowEntity::new, ShadowEntity::new, EntityClassification.CREATURE, "dire_plant", 0.5F, 0.5F);*/

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

        RenderingRegistry.registerEntityRenderingHandler(TYPE_PAIR_BLOX.get(), PairBloxRenderer.FACTORY);
        RenderingRegistry.registerEntityRenderingHandler(TYPE_MUNNY.get(), MunnyRenderer.FACTORY);
        RenderingRegistry.registerEntityRenderingHandler(TYPE_HPORB.get(), HPOrbRenderer.FACTORY);
        RenderingRegistry.registerEntityRenderingHandler(TYPE_MPORB.get(), MPOrbRenderer.FACTORY);
        RenderingRegistry.registerEntityRenderingHandler(TYPE_DRIVEORB.get(), DriveOrbRenderer.FACTORY);
        
        RenderingRegistry.registerEntityRenderingHandler(TYPE_FIRE.get(), EntityFireRenderer.FACTORY);
        RenderingRegistry.registerEntityRenderingHandler(TYPE_BLIZZARD.get(), EntityBlizzardRenderer.FACTORY);
        RenderingRegistry.registerEntityRenderingHandler(TYPE_THUNDER.get(), EntityThunderRenderer.FACTORY);
        RenderingRegistry.registerEntityRenderingHandler(TYPE_MAGNET.get(), EntityMagnetRenderer.FACTORY);
        RenderingRegistry.registerEntityRenderingHandler(TYPE_WATER.get(), EntityWaterRenderer.FACTORY);
        RenderingRegistry.registerEntityRenderingHandler(TYPE_GRAVITY.get(), EntityGravityRenderer.FACTORY);
        
        RenderingRegistry.registerEntityRenderingHandler(TYPE_MOOGLE.get(), MoogleRenderer.FACTORY);
        RenderingRegistry.registerEntityRenderingHandler(TYPE_SHADOW.get(), ShadowRenderer.FACTORY);
        RenderingRegistry.registerEntityRenderingHandler(TYPE_MEGA_SHADOW.get(), MegaShadowRenderer.FACTORY);
        RenderingRegistry.registerEntityRenderingHandler(TYPE_GIGA_SHADOW.get(), GigaShadowRenderer.FACTORY);
        RenderingRegistry.registerEntityRenderingHandler(TYPE_DARKBALL.get(), DarkballRenderer.FACTORY);
        RenderingRegistry.registerEntityRenderingHandler(TYPE_SHADOW_GLOB.get(), ShadowGlobRenderer.FACTORY);
        
        RenderingRegistry.registerEntityRenderingHandler(TYPE_LARGE_BODY.get(), LargeBodyRenderer.FACTORY);
        
        ClientRegistry.bindTileEntityRenderer(TYPE_PEDESTAL.get(), TESRPedestal::new);
    }

    public static final DeferredRegister<TileEntityType<?>> TILE_ENTITIES = new DeferredRegister<>(ForgeRegistries.TILE_ENTITIES, KingdomKeys.MODID);

    //public static final RegistryObject<TileEntityType<MagnetBloxTileEntity>> TYPE_MAGNET_BLOX = createTileEntityType("magnet_blox", MagnetBloxTileEntity::new, ModBlocks.magnetBlox);
    public static final RegistryObject<TileEntityType<MagnetBloxTileEntity>> TYPE_MAGNET_BLOX = TILE_ENTITIES.register("magnet_blox", () -> TileEntityType.Builder.create(MagnetBloxTileEntity::new, ModBlocks.magnetBlox.get()).build(null));
    public static final RegistryObject<TileEntityType<PedestalTileEntity>> TYPE_PEDESTAL = TILE_ENTITIES.register("pedestal", () -> TileEntityType.Builder.create(PedestalTileEntity::new, ModBlocks.pedestal.get()).build(null));
}
