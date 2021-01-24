package online.kingdomkeys.kingdomkeys.entity;

import static online.kingdomkeys.kingdomkeys.entity.EntityHelper.MobType.HEARTLESS_EMBLEM;
import static online.kingdomkeys.kingdomkeys.entity.EntityHelper.MobType.HEARTLESS_PUREBLOOD;
import static online.kingdomkeys.kingdomkeys.entity.EntityHelper.MobType.NOBODY;

import java.awt.Color;
import java.util.HashSet;
import java.util.Set;
import java.util.function.BiFunction;

import net.minecraft.entity.CreatureEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntitySpawnPlacementRegistry;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.SpawnEggItem;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.world.World;
import net.minecraft.world.gen.Heightmap;
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
import online.kingdomkeys.kingdomkeys.client.render.block.BlastBloxRenderer;
import online.kingdomkeys.kingdomkeys.client.render.block.MoogleProjectorRenderer;
import online.kingdomkeys.kingdomkeys.client.render.block.PairBloxRenderer;
import online.kingdomkeys.kingdomkeys.client.render.block.PedestalRenderer;
import online.kingdomkeys.kingdomkeys.client.render.block.SoAPlatformRenderer;
import online.kingdomkeys.kingdomkeys.client.render.entity.ArrowgunShotEntityRenderer;
import online.kingdomkeys.kingdomkeys.client.render.entity.AssassinRenderer;
import online.kingdomkeys.kingdomkeys.client.render.entity.BombRenderer;
import online.kingdomkeys.kingdomkeys.client.render.entity.DarkballRenderer;
import online.kingdomkeys.kingdomkeys.client.render.entity.DirePlantRenderer;
import online.kingdomkeys.kingdomkeys.client.render.entity.DuskRenderer;
import online.kingdomkeys.kingdomkeys.client.render.entity.ElementalMusicalHeartlessRenderer;
import online.kingdomkeys.kingdomkeys.client.render.entity.GigaShadowRenderer;
import online.kingdomkeys.kingdomkeys.client.render.entity.LargeBodyRenderer;
import online.kingdomkeys.kingdomkeys.client.render.entity.MegaShadowRenderer;
import online.kingdomkeys.kingdomkeys.client.render.entity.MoogleRenderer;
import online.kingdomkeys.kingdomkeys.client.render.entity.NobodyCreeperRenderer;
import online.kingdomkeys.kingdomkeys.client.render.entity.OrgPortalEntityRenderer;
import online.kingdomkeys.kingdomkeys.client.render.entity.SeedBulletRenderer;
import online.kingdomkeys.kingdomkeys.client.render.entity.ShadowGlobRenderer;
import online.kingdomkeys.kingdomkeys.client.render.entity.ShadowRenderer;
import online.kingdomkeys.kingdomkeys.client.render.entity.drops.DriveOrbRenderer;
import online.kingdomkeys.kingdomkeys.client.render.entity.drops.HPOrbRenderer;
import online.kingdomkeys.kingdomkeys.client.render.entity.drops.MPOrbRenderer;
import online.kingdomkeys.kingdomkeys.client.render.entity.drops.MunnyRenderer;
import online.kingdomkeys.kingdomkeys.client.render.magic.BlizzardEntityRenderer;
import online.kingdomkeys.kingdomkeys.client.render.magic.FireEntityRenderer;
import online.kingdomkeys.kingdomkeys.client.render.magic.GravityEntityRenderer;
import online.kingdomkeys.kingdomkeys.client.render.magic.HeartEntityRenderer;
import online.kingdomkeys.kingdomkeys.client.render.magic.MagnetEntityRenderer;
import online.kingdomkeys.kingdomkeys.client.render.magic.ThunderBoltEntityRenderer;
import online.kingdomkeys.kingdomkeys.client.render.magic.ThunderEntityRenderer;
import online.kingdomkeys.kingdomkeys.client.render.magic.WaterEntityRenderer;
import online.kingdomkeys.kingdomkeys.client.render.org.ChakramEntityRenderer;
import online.kingdomkeys.kingdomkeys.client.render.org.LanceEntityRenderer;
import online.kingdomkeys.kingdomkeys.entity.EntityHelper.MobType;
import online.kingdomkeys.kingdomkeys.entity.block.BlastBloxEntity;
import online.kingdomkeys.kingdomkeys.entity.block.MagicalChestTileEntity;
import online.kingdomkeys.kingdomkeys.entity.block.MagnetBloxTileEntity;
import online.kingdomkeys.kingdomkeys.entity.block.MoogleProjectorTileEntity;
import online.kingdomkeys.kingdomkeys.entity.block.OrgPortalTileEntity;
import online.kingdomkeys.kingdomkeys.entity.block.PairBloxEntity;
import online.kingdomkeys.kingdomkeys.entity.block.PedestalTileEntity;
import online.kingdomkeys.kingdomkeys.entity.block.SoAPlatformTileEntity;
import online.kingdomkeys.kingdomkeys.entity.magic.BlizzardEntity;
import online.kingdomkeys.kingdomkeys.entity.magic.FireEntity;
import online.kingdomkeys.kingdomkeys.entity.magic.GravityEntity;
import online.kingdomkeys.kingdomkeys.entity.magic.MagnetEntity;
import online.kingdomkeys.kingdomkeys.entity.magic.ThunderBoltEntity;
import online.kingdomkeys.kingdomkeys.entity.magic.ThunderEntity;
import online.kingdomkeys.kingdomkeys.entity.magic.WaterEntity;
import online.kingdomkeys.kingdomkeys.entity.mob.AssassinEntity;
import online.kingdomkeys.kingdomkeys.entity.mob.BlueRhapsodyEntity;
import online.kingdomkeys.kingdomkeys.entity.mob.DarkballEntity;
import online.kingdomkeys.kingdomkeys.entity.mob.DetonatorEntity;
import online.kingdomkeys.kingdomkeys.entity.mob.DirePlantEntity;
import online.kingdomkeys.kingdomkeys.entity.mob.DuskEntity;
import online.kingdomkeys.kingdomkeys.entity.mob.GigaShadowEntity;
import online.kingdomkeys.kingdomkeys.entity.mob.GreenRequiemEntity;
import online.kingdomkeys.kingdomkeys.entity.mob.LargeBodyEntity;
import online.kingdomkeys.kingdomkeys.entity.mob.MegaShadowEntity;
import online.kingdomkeys.kingdomkeys.entity.mob.MinuteBombEntity;
import online.kingdomkeys.kingdomkeys.entity.mob.MoogleEntity;
import online.kingdomkeys.kingdomkeys.entity.mob.NobodyCreeperEntity;
import online.kingdomkeys.kingdomkeys.entity.mob.RedNocturneEntity;
import online.kingdomkeys.kingdomkeys.entity.mob.ShadowEntity;
import online.kingdomkeys.kingdomkeys.entity.mob.ShadowGlobEntity;
import online.kingdomkeys.kingdomkeys.entity.mob.SkaterBombEntity;
import online.kingdomkeys.kingdomkeys.entity.mob.StormBombEntity;
import online.kingdomkeys.kingdomkeys.entity.mob.YellowOperaEntity;
import online.kingdomkeys.kingdomkeys.entity.organization.ArrowgunShotEntity;
import online.kingdomkeys.kingdomkeys.entity.organization.ChakramEntity;
import online.kingdomkeys.kingdomkeys.entity.organization.LanceEntity;
import online.kingdomkeys.kingdomkeys.item.ModItems;
import online.kingdomkeys.kingdomkeys.proxy.ProxyClient;

public class ModEntities {
    public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(ForgeRegistries.ENTITIES, KingdomKeys.MODID);

    public static Set<EntityType<? extends Entity>> pureblood = new HashSet<EntityType<?>>();
    public static Set<EntityType<? extends Entity>> emblem = new HashSet<EntityType<?>>();
    public static Set<EntityType<? extends Entity>> nobody = new HashSet<EntityType<?>>();
    public static Set<EntityType<? extends Entity>> npc = new HashSet<EntityType<?>>();

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
    public static final RegistryObject<EntityType<ThunderBoltEntity>> TYPE_THUNDERBOLT = createEntityType(ThunderBoltEntity::new, ThunderBoltEntity::new, EntityClassification.MISC,"entity_thunderbolt", 0.5F, 0.5F);
    public static final RegistryObject<EntityType<MagnetEntity>> TYPE_MAGNET = createEntityType(MagnetEntity::new, MagnetEntity::new, EntityClassification.MISC,"entity_magnet", 0.5F, 0.5F);
    public static final RegistryObject<EntityType<GravityEntity>> TYPE_GRAVITY = createEntityType(GravityEntity::new, GravityEntity::new, EntityClassification.MISC,"entity_gravity", 0.5F, 0.5F);

    public static final RegistryObject<EntityType<SeedBulletEntity>> TYPE_SEED_BULLET = createEntityType(SeedBulletEntity::new, SeedBulletEntity::new, EntityClassification.MISC,"seed_bullet", 0.5F, 0.5F);
    public static final RegistryObject<EntityType<ArrowgunShotEntity>> TYPE_ARROWGUN_SHOT = createEntityType(ArrowgunShotEntity::new, ArrowgunShotEntity::new, EntityClassification.MISC,"arrowgun_shot", 0.1F, 0.1F);

    public static final RegistryObject<EntityType<OrgPortalEntity>> TYPE_ORG_PORTAL = createEntityType(OrgPortalEntity::new, OrgPortalEntity::new, EntityClassification.MISC,"entity_org_portal", 0.5F, 0.5F);
    
    public static final RegistryObject<EntityType<ChakramEntity>> TYPE_CHAKRAM = createEntityType(ChakramEntity::new, ChakramEntity::new, EntityClassification.MISC,"entity_chakram", 1F, 0.5F);
    public static final RegistryObject<EntityType<LanceEntity>> TYPE_LANCE = createEntityType(LanceEntity::new, LanceEntity::new, EntityClassification.MISC,"entity_lance", 1F, 0.5F);
    
    public static final RegistryObject<EntityType<HeartEntity>> TYPE_HEART = createEntityType(HeartEntity::new, HeartEntity::new, EntityClassification.MISC, "heart", 1F, 1F);

    //Mobs
    public static final RegistryObject<EntityType<MoogleEntity>> TYPE_MOOGLE = createEntityType(MoogleEntity::new, MoogleEntity::new, EntityClassification.AMBIENT, "moogle", 0.6F, 1.5F, MobType.NPC, 0xDACAB0, 0xC50033);
    
    public static final RegistryObject<EntityType<ShadowEntity>> TYPE_SHADOW = createEntityType(ShadowEntity::new, ShadowEntity::new, EntityClassification.MONSTER, "shadow", 0.5F, 0.7F, HEARTLESS_PUREBLOOD, 0x000000, 0xFFFF00);
    public static final RegistryObject<EntityType<MegaShadowEntity>> TYPE_MEGA_SHADOW = createEntityType(MegaShadowEntity::new, MegaShadowEntity::new, EntityClassification.MONSTER, "mega_shadow", 1.5F, 1.7F, HEARTLESS_PUREBLOOD, 0x000000, 0xAAAA00);
    public static final RegistryObject<EntityType<GigaShadowEntity>> TYPE_GIGA_SHADOW = createEntityType(GigaShadowEntity::new, GigaShadowEntity::new, EntityClassification.MONSTER, "giga_shadow", 2.5F, 2.7F, HEARTLESS_PUREBLOOD, 0x000000, 0x666600);
    public static final RegistryObject<EntityType<DarkballEntity>> TYPE_DARKBALL = createEntityType(DarkballEntity::new, DarkballEntity::new, EntityClassification.MONSTER, "darkball", 1.5F, 2F, HEARTLESS_PUREBLOOD, 0x000000, 0x6600FF);
    public static final RegistryObject<EntityType<ShadowGlobEntity>> TYPE_SHADOW_GLOB = createEntityType(ShadowGlobEntity::new, ShadowGlobEntity::new, EntityClassification.MONSTER, "shadow_glob", 1F, 1F, HEARTLESS_PUREBLOOD, Color.BLACK.getRGB(), 0x312ba0);
    
    //Emblems
    public static final RegistryObject<EntityType<MinuteBombEntity>> TYPE_MINUTE_BOMB = createEntityType(MinuteBombEntity::new, MinuteBombEntity::new, EntityClassification.MONSTER, "minute_bomb", 0.6F, 1.3F, HEARTLESS_EMBLEM, 0x020030, 0x8B4513);
    public static final RegistryObject<EntityType<SkaterBombEntity>> TYPE_SKATER_BOMB = createEntityType(SkaterBombEntity::new, SkaterBombEntity::new, EntityClassification.MONSTER, "skater_bomb", 0.6F, 1.3F, HEARTLESS_EMBLEM, 0x020030, 0xAAAAFF);
    public static final RegistryObject<EntityType<StormBombEntity>> TYPE_STORM_BOMB = createEntityType(StormBombEntity::new, StormBombEntity::new, EntityClassification.MONSTER, "storm_bomb", 0.6F, 1.3F, HEARTLESS_EMBLEM, 0x020030, Color.CYAN.getRGB());
    public static final RegistryObject<EntityType<DetonatorEntity>> TYPE_DETONATOR = createEntityType(DetonatorEntity::new, DetonatorEntity::new, EntityClassification.MONSTER, "detonator", 0.6F, 1.3F, HEARTLESS_EMBLEM, 0x020030, Color.RED.getRGB());
    public static final RegistryObject<EntityType<RedNocturneEntity>> TYPE_RED_NOCTURNE = createEntityTypeImmuneToFire(RedNocturneEntity::new, RedNocturneEntity::new, EntityClassification.MONSTER, "red_nocturne", 1.0F,  2.0F, HEARTLESS_EMBLEM, Color.RED.getRGB(), Color.YELLOW.getRGB());
    public static final RegistryObject<EntityType<BlueRhapsodyEntity>> TYPE_BLUE_RHAPSODY = createEntityType(BlueRhapsodyEntity::new, BlueRhapsodyEntity::new, EntityClassification.MONSTER, "blue_rhapsody", 1.0F, 2.0F, HEARTLESS_EMBLEM, Color.BLUE.getRGB(), Color.YELLOW.getRGB());
    public static final RegistryObject<EntityType<YellowOperaEntity>> TYPE_YELLOW_OPERA = createEntityType(YellowOperaEntity::new, YellowOperaEntity::new, EntityClassification.MONSTER, "yellow_opera", 1.0F, 2.0F, HEARTLESS_EMBLEM, Color.YELLOW.getRGB(), Color.YELLOW.getRGB());
    public static final RegistryObject<EntityType<GreenRequiemEntity>> TYPE_GREEN_REQUIEM = createEntityType(GreenRequiemEntity::new, GreenRequiemEntity::new, EntityClassification.MONSTER, "green_requiem", 1.0F, 2.0F, HEARTLESS_EMBLEM, Color.GREEN.getRGB(), Color.YELLOW.getRGB());

    //TODO update textures to work with newer model, make magic for
    /*
    public static final RegistryObject<EntityType<ShadowEntity>> TYPE_SILVER_ROCK = createEntityType(ShadowEntity::new, ShadowEntity::new, EntityClassification.MONSTER, "silver_rock", 1.0F, 1.0F);
    public static final RegistryObject<EntityType<ShadowEntity>> TYPE_CRIMSON_JAZZ = createEntityType(ShadowEntity::new, ShadowEntity::new, EntityClassification.MONSTER, "crimson_jazz", 1.0F, 1.0F);
    public static final RegistryObject<EntityType<ShadowEntity>> TYPE_EMERALD_BLUES = createEntityType(ShadowEntity::new, ShadowEntity::new, EntityClassification.MONSTER, "emerald_blues", 1.0F, 1.0F);
    */

    public static final RegistryObject<EntityType<LargeBodyEntity>> TYPE_LARGE_BODY = createEntityType(LargeBodyEntity::new, LargeBodyEntity::new, EntityClassification.MONSTER, "large_body", 1.3F, 1.6F, HEARTLESS_EMBLEM, 0x4d177c, 0x29014c);
    //TODO make AI
    //public static final RegistryObject<EntityType<ShadowEntity>> TYPE_WHITE_MUSHROOM = createEntityType(ShadowEntity::new, ShadowEntity::new, EntityClassification.MONSTER, "white_mushroom", 0.5F, 0.5F, HEARTLESS_EMBLEM, 0xe3e5e8, 0xffffff);
    public static final RegistryObject<EntityType<DirePlantEntity>> TYPE_DIRE_PLANT = createEntityType(DirePlantEntity::new, DirePlantEntity::new, EntityClassification.MONSTER, "dire_plant", 0.75F, 1.5F, HEARTLESS_EMBLEM, 0x4ba04e, 0xedc2c2);

    //Nobodies
    public static final RegistryObject<EntityType<NobodyCreeperEntity>> TYPE_NOBODY_CREEPER = createEntityType(NobodyCreeperEntity::new, NobodyCreeperEntity::new, EntityClassification.MONSTER, "nobody_creeper", 1F, 1.5F, NOBODY, 0xb8bdc4, 0xfcfcfc);
    public static final RegistryObject<EntityType<DuskEntity>> TYPE_DUSK = createEntityType(DuskEntity::new, DuskEntity::new, EntityClassification.MONSTER, "dusk", 1F, 1.5F, NOBODY, 0xb8bdc4, 0xfcfcfc);
    public static final RegistryObject<EntityType<AssassinEntity>> TYPE_ASSASSIN = createEntityType(AssassinEntity::new, AssassinEntity::new, EntityClassification.MONSTER, "assassin", 1F, 1.5F, NOBODY, 0xc9c9c9, 0xd4ccff);

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
    public static <T extends Entity, M extends EntityType<T>>RegistryObject<EntityType<T>> createEntityType(EntityType.IFactory<T> factory, BiFunction<FMLPlayMessages.SpawnEntity, World, T> clientFactory, EntityClassification classification, String name, float sizeX, float sizeY, EntityHelper.MobType group, int color1, int color2) {
        EntityType<T> type = EntityType.Builder.create(factory, classification)
                .setCustomClientFactory(clientFactory)
                .setShouldReceiveVelocityUpdates(true)
                .setUpdateInterval(1)
                .setTrackingRange(128)
                .size(sizeX, sizeY)
                .build(name);
        if (group != null) {
        	addToGroup(group, type);
            ModItems.ITEMS.register(name+"_spawn_egg", () -> new SpawnEggItem(type, color1, color2, new Item.Properties().group(ItemGroup.MISC)));
        }
        
        return ENTITIES.register(name, () -> type);
    }
    
    public static <T extends Entity, M extends EntityType<T>>RegistryObject<EntityType<T>> createEntityType(EntityType.IFactory<T> factory, BiFunction<FMLPlayMessages.SpawnEntity, World, T> clientFactory, EntityClassification classification, String name, float sizeX, float sizeY) {
        return createEntityType(factory,clientFactory,classification,name,sizeX,sizeY, null, 0,0);
    }

    public static <T extends Entity, M extends EntityType<T>>RegistryObject<EntityType<T>> createEntityTypeImmuneToFire(EntityType.IFactory<T> factory, BiFunction<FMLPlayMessages.SpawnEntity, World, T> clientFactory, EntityClassification classification, String name, float sizeX, float sizeY, EntityHelper.MobType group, int color1, int color2) {
        EntityType<T> type = EntityType.Builder.create(factory, classification)
                .setCustomClientFactory(clientFactory)
                .setShouldReceiveVelocityUpdates(true)
                .setUpdateInterval(1)
                .setTrackingRange(128)
                .size(sizeX, sizeY)
                .immuneToFire()
                .build(name);
        if (group != null) {
        	addToGroup(group, type);
            ModItems.ITEMS.register(name+"_spawn_egg", () -> new SpawnEggItem(type, color1, color2, new Item.Properties().group(ItemGroup.MISC)));
        }

        return ENTITIES.register(name, () -> type);
    }
    
    public static <T extends Entity, M extends EntityType<T>>RegistryObject<EntityType<T>> createEntityTypeImmuneToFire(EntityType.IFactory<T> factory, BiFunction<FMLPlayMessages.SpawnEntity, World, T> clientFactory, EntityClassification classification, String name, float sizeX, float sizeY) {
    	RegistryObject<EntityType<T>> entity = createEntityTypeImmuneToFire(factory,clientFactory,classification,name,sizeX,sizeY, null, 0, 0);
        return entity;
    }

    public static void addToGroup(EntityHelper.MobType group, EntityType<?> type) {
        switch(group) {
            case HEARTLESS_PUREBLOOD:
                pureblood.add(type);
                break;
            case HEARTLESS_EMBLEM:
                emblem.add(type);
                break;
            case NOBODY:
                nobody.add(type);
                break;
            case NPC:
            	npc.add(type);
        }
    }
    /**
     * Register the render classes for the entities, called in {@link ProxyClient#registerModels(ModelRegistryEvent)}  }
     */
    @OnlyIn(Dist.CLIENT)
    public static void registerModels() {
        //Entities
        RenderingRegistry.registerEntityRenderingHandler(TYPE_BLAST_BLOX.get(), BlastBloxRenderer.FACTORY);

        RenderingRegistry.registerEntityRenderingHandler(TYPE_PAIR_BLOX.get(), PairBloxRenderer.FACTORY);
        RenderingRegistry.registerEntityRenderingHandler(TYPE_MUNNY.get(), MunnyRenderer.FACTORY);
        RenderingRegistry.registerEntityRenderingHandler(TYPE_HPORB.get(), HPOrbRenderer.FACTORY);
        RenderingRegistry.registerEntityRenderingHandler(TYPE_MPORB.get(), MPOrbRenderer.FACTORY);
        RenderingRegistry.registerEntityRenderingHandler(TYPE_DRIVEORB.get(), DriveOrbRenderer.FACTORY);
        
        RenderingRegistry.registerEntityRenderingHandler(TYPE_FIRE.get(), FireEntityRenderer.FACTORY);
        RenderingRegistry.registerEntityRenderingHandler(TYPE_BLIZZARD.get(), BlizzardEntityRenderer.FACTORY);
        RenderingRegistry.registerEntityRenderingHandler(TYPE_THUNDER.get(), ThunderEntityRenderer.FACTORY);
        RenderingRegistry.registerEntityRenderingHandler(TYPE_THUNDERBOLT.get(), ThunderBoltEntityRenderer.FACTORY);
        RenderingRegistry.registerEntityRenderingHandler(TYPE_MAGNET.get(), MagnetEntityRenderer.FACTORY);
        RenderingRegistry.registerEntityRenderingHandler(TYPE_WATER.get(), WaterEntityRenderer.FACTORY);
        RenderingRegistry.registerEntityRenderingHandler(TYPE_GRAVITY.get(), GravityEntityRenderer.FACTORY);
        
        RenderingRegistry.registerEntityRenderingHandler(TYPE_MOOGLE.get(), MoogleRenderer.FACTORY);
        RenderingRegistry.registerEntityRenderingHandler(TYPE_SHADOW.get(), ShadowRenderer.FACTORY);
        RenderingRegistry.registerEntityRenderingHandler(TYPE_MEGA_SHADOW.get(), MegaShadowRenderer.FACTORY);
        RenderingRegistry.registerEntityRenderingHandler(TYPE_GIGA_SHADOW.get(), GigaShadowRenderer.FACTORY);
        RenderingRegistry.registerEntityRenderingHandler(TYPE_DARKBALL.get(), DarkballRenderer.FACTORY);
        RenderingRegistry.registerEntityRenderingHandler(TYPE_SHADOW_GLOB.get(), ShadowGlobRenderer.FACTORY);
        RenderingRegistry.registerEntityRenderingHandler(TYPE_LARGE_BODY.get(), LargeBodyRenderer.FACTORY);
        RenderingRegistry.registerEntityRenderingHandler(TYPE_MINUTE_BOMB.get(), BombRenderer.FACTORY);
        RenderingRegistry.registerEntityRenderingHandler(TYPE_SKATER_BOMB.get(), BombRenderer.FACTORY);
        RenderingRegistry.registerEntityRenderingHandler(TYPE_STORM_BOMB.get(), BombRenderer.FACTORY);
        RenderingRegistry.registerEntityRenderingHandler(TYPE_DETONATOR.get(), BombRenderer.FACTORY);
        RenderingRegistry.registerEntityRenderingHandler(TYPE_NOBODY_CREEPER.get(), NobodyCreeperRenderer.FACTORY);
        RenderingRegistry.registerEntityRenderingHandler(TYPE_RED_NOCTURNE.get(), ElementalMusicalHeartlessRenderer.FACTORY);
        RenderingRegistry.registerEntityRenderingHandler(TYPE_BLUE_RHAPSODY.get(), ElementalMusicalHeartlessRenderer.FACTORY);
        RenderingRegistry.registerEntityRenderingHandler(TYPE_YELLOW_OPERA.get(), ElementalMusicalHeartlessRenderer.FACTORY);
        RenderingRegistry.registerEntityRenderingHandler(TYPE_GREEN_REQUIEM.get(), ElementalMusicalHeartlessRenderer.FACTORY);
        RenderingRegistry.registerEntityRenderingHandler(TYPE_DUSK.get(), DuskRenderer.FACTORY);
        RenderingRegistry.registerEntityRenderingHandler(TYPE_ASSASSIN.get(), AssassinRenderer.FACTORY);
        RenderingRegistry.registerEntityRenderingHandler(TYPE_DIRE_PLANT.get(), DirePlantRenderer.FACTORY);

        RenderingRegistry.registerEntityRenderingHandler(TYPE_ORG_PORTAL.get(), OrgPortalEntityRenderer.FACTORY);
        RenderingRegistry.registerEntityRenderingHandler(TYPE_HEART.get(), HeartEntityRenderer.FACTORY);

        RenderingRegistry.registerEntityRenderingHandler(TYPE_CHAKRAM.get(), ChakramEntityRenderer.FACTORY);
        RenderingRegistry.registerEntityRenderingHandler(TYPE_LANCE.get(), LanceEntityRenderer.FACTORY);

        RenderingRegistry.registerEntityRenderingHandler(TYPE_SEED_BULLET.get(), SeedBulletRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(TYPE_ARROWGUN_SHOT.get(), ArrowgunShotEntityRenderer.FACTORY);

        
        //Tile Entities
        
        ClientRegistry.bindTileEntityRenderer(TYPE_PEDESTAL.get(), PedestalRenderer::new);
        ClientRegistry.bindTileEntityRenderer(TYPE_MOOGLE_PROJECTOR.get(), MoogleProjectorRenderer::new);
        ClientRegistry.bindTileEntityRenderer(TYPE_SOA_PLATFORM.get(), SoAPlatformRenderer::new);

    }

    public static void registerPlacements() {
        EntitySpawnPlacementRegistry.register(TYPE_ASSASSIN.get(), EntitySpawnPlacementRegistry.PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, MonsterEntity::canMonsterSpawn);
        EntitySpawnPlacementRegistry.register(TYPE_BLUE_RHAPSODY.get(), EntitySpawnPlacementRegistry.PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, MonsterEntity::canMonsterSpawn);
        EntitySpawnPlacementRegistry.register(TYPE_DARKBALL.get(), EntitySpawnPlacementRegistry.PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, MonsterEntity::canMonsterSpawn);
        EntitySpawnPlacementRegistry.register(TYPE_DETONATOR.get(), EntitySpawnPlacementRegistry.PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, MonsterEntity::canMonsterSpawn);
        EntitySpawnPlacementRegistry.register(TYPE_DIRE_PLANT.get(), EntitySpawnPlacementRegistry.PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, MonsterEntity::canMonsterSpawn);
        EntitySpawnPlacementRegistry.register(TYPE_DUSK.get(), EntitySpawnPlacementRegistry.PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, MonsterEntity::canMonsterSpawn);
        EntitySpawnPlacementRegistry.register(TYPE_GIGA_SHADOW.get(), EntitySpawnPlacementRegistry.PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, MonsterEntity::canMonsterSpawn);
        EntitySpawnPlacementRegistry.register(TYPE_GREEN_REQUIEM.get(), EntitySpawnPlacementRegistry.PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, MonsterEntity::canMonsterSpawn);
        EntitySpawnPlacementRegistry.register(TYPE_LARGE_BODY.get(), EntitySpawnPlacementRegistry.PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, MonsterEntity::canMonsterSpawn);
        EntitySpawnPlacementRegistry.register(TYPE_MEGA_SHADOW.get(), EntitySpawnPlacementRegistry.PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, MonsterEntity::canMonsterSpawn);
        EntitySpawnPlacementRegistry.register(TYPE_MINUTE_BOMB.get(), EntitySpawnPlacementRegistry.PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, MonsterEntity::canMonsterSpawn);
        EntitySpawnPlacementRegistry.register(TYPE_MOOGLE.get(), EntitySpawnPlacementRegistry.PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, CreatureEntity::canSpawnOn);
        EntitySpawnPlacementRegistry.register(TYPE_NOBODY_CREEPER.get(), EntitySpawnPlacementRegistry.PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, MonsterEntity::canMonsterSpawn);
        EntitySpawnPlacementRegistry.register(TYPE_RED_NOCTURNE.get(), EntitySpawnPlacementRegistry.PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, MonsterEntity::canMonsterSpawn);
        EntitySpawnPlacementRegistry.register(TYPE_SHADOW.get(), EntitySpawnPlacementRegistry.PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, MonsterEntity::canMonsterSpawn);
        EntitySpawnPlacementRegistry.register(TYPE_SHADOW_GLOB.get(), EntitySpawnPlacementRegistry.PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, MonsterEntity::canMonsterSpawn);
        EntitySpawnPlacementRegistry.register(TYPE_SKATER_BOMB.get(), EntitySpawnPlacementRegistry.PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, MonsterEntity::canMonsterSpawn);
        EntitySpawnPlacementRegistry.register(TYPE_STORM_BOMB.get(), EntitySpawnPlacementRegistry.PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, MonsterEntity::canMonsterSpawn);
        EntitySpawnPlacementRegistry.register(TYPE_YELLOW_OPERA.get(), EntitySpawnPlacementRegistry.PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, MonsterEntity::canMonsterSpawn);
    }

    public static final DeferredRegister<TileEntityType<?>> TILE_ENTITIES = DeferredRegister.create(ForgeRegistries.TILE_ENTITIES, KingdomKeys.MODID);

    public static final RegistryObject<TileEntityType<MagnetBloxTileEntity>> TYPE_MAGNET_BLOX = TILE_ENTITIES.register("magnet_blox", () -> TileEntityType.Builder.create(MagnetBloxTileEntity::new, ModBlocks.magnetBlox.get()).build(null));
    public static final RegistryObject<TileEntityType<PedestalTileEntity>> TYPE_PEDESTAL = TILE_ENTITIES.register("pedestal", () -> TileEntityType.Builder.create(PedestalTileEntity::new, ModBlocks.pedestal.get()).build(null));
    public static final RegistryObject<TileEntityType<MagicalChestTileEntity>> TYPE_MAGICAL_CHEST = TILE_ENTITIES.register("magical_chest", () -> TileEntityType.Builder.create(MagicalChestTileEntity::new, ModBlocks.magicalChest.get()).build(null));
    public static final RegistryObject<TileEntityType<OrgPortalTileEntity>> TYPE_ORG_PORTAL_TE = TILE_ENTITIES.register("org_portal", () -> TileEntityType.Builder.create(OrgPortalTileEntity::new, ModBlocks.orgPortal.get()).build(null));
    public static final RegistryObject<TileEntityType<MoogleProjectorTileEntity>> TYPE_MOOGLE_PROJECTOR = TILE_ENTITIES.register("moogle_projector", () -> TileEntityType.Builder.create(MoogleProjectorTileEntity::new, ModBlocks.moogleProjector.get()).build(null));
    public static final RegistryObject<TileEntityType<SoAPlatformTileEntity>> TYPE_SOA_PLATFORM = TILE_ENTITIES.register("soa_platform", () -> TileEntityType.Builder.create(SoAPlatformTileEntity::new, ModBlocks.station_of_awakening_core.get()).build(null));
}
