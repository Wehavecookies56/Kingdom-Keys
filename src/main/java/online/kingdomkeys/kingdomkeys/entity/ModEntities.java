package online.kingdomkeys.kingdomkeys.entity;

import static online.kingdomkeys.kingdomkeys.entity.EntityHelper.MobType.*;

import java.awt.Color;
import java.util.*;
import java.util.Map.Entry;
import java.util.function.BiFunction;
import java.util.function.Supplier;

import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.levelgen.Heightmap;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.common.DeferredSpawnEggItem;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;
import net.neoforged.neoforge.event.entity.RegisterSpawnPlacementsEvent;
import net.neoforged.neoforge.registries.DeferredRegister;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.block.ModBlocks;
import online.kingdomkeys.kingdomkeys.client.ClientSetup;
import online.kingdomkeys.kingdomkeys.client.model.BlizzardModel;
import online.kingdomkeys.kingdomkeys.client.model.FireModel;
import online.kingdomkeys.kingdomkeys.client.model.armor.*;
import online.kingdomkeys.kingdomkeys.client.model.entity.*;
import online.kingdomkeys.kingdomkeys.client.render.block.*;
import online.kingdomkeys.kingdomkeys.client.render.entity.*;
import online.kingdomkeys.kingdomkeys.client.render.entity.drops.*;
import online.kingdomkeys.kingdomkeys.client.render.magic.HeartEntityRenderer;
import online.kingdomkeys.kingdomkeys.client.render.magic.InvisibleEntityRenderer;
import online.kingdomkeys.kingdomkeys.client.render.magic.MagnetEntityRenderer;
import online.kingdomkeys.kingdomkeys.client.render.magic.ThunderBoltEntityRenderer;
import online.kingdomkeys.kingdomkeys.client.render.org.*;
import online.kingdomkeys.kingdomkeys.client.render.shotlock.UltimaCannonShotlockShotEntityRenderer;
import online.kingdomkeys.kingdomkeys.client.render.shotlock.VolleyShotlockShotEntityRenderer;
import online.kingdomkeys.kingdomkeys.config.ModConfigs;
import online.kingdomkeys.kingdomkeys.entity.EntityHelper.MobType;
import online.kingdomkeys.kingdomkeys.entity.block.*;
import online.kingdomkeys.kingdomkeys.entity.magic.*;
import online.kingdomkeys.kingdomkeys.entity.mob.*;
import online.kingdomkeys.kingdomkeys.entity.organization.*;
import online.kingdomkeys.kingdomkeys.entity.shotlock.*;
import online.kingdomkeys.kingdomkeys.item.ModItems;

@EventBusSubscriber(modid = KingdomKeys.MODID, bus = EventBusSubscriber.Bus.MOD)
public class ModEntities {
    public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(Registries.ENTITY_TYPE, KingdomKeys.MODID);

    public static HashMap<EntityType<? extends Entity>,Integer> pureblood = new HashMap<>();
    public static HashMap<EntityType<? extends Entity>,Integer> emblem = new HashMap<>();
    public static HashMap<EntityType<? extends Entity>,Integer> nobody = new HashMap<>();
    public static Set<EntityType<? extends Entity>> npc = new HashSet<>();

    public static final Supplier<EntityType<BlastBloxEntity>> TYPE_BLAST_BLOX = createEntityType(BlastBloxEntity::new, MobCategory.MISC,"blast_blox_primed", 0.98F, 0.98F);
    public static final Supplier<EntityType<PairBloxEntity>> TYPE_PAIR_BLOX = createEntityType(PairBloxEntity::new, MobCategory.MISC,"pair_blox", 1F, 1F);
    public static final Supplier<EntityType<MunnyEntity>> TYPE_MUNNY = createEntityType(MunnyEntity::new, MobCategory.MISC,"entity_munny", 0.25F, 0.25F);
    public static final Supplier<EntityType<HPOrbEntity>> TYPE_HPORB = createEntityType(HPOrbEntity::new, MobCategory.MISC,"entity_hp_orb", 0.25F, 0.25F);
    public static final Supplier<EntityType<MPOrbEntity>> TYPE_MPORB = createEntityType(MPOrbEntity::new, MobCategory.MISC,"entity_mp_orb", 0.25F, 0.25F);
    public static final Supplier<EntityType<DriveOrbEntity>> TYPE_DRIVEORB = createEntityType(DriveOrbEntity::new, MobCategory.MISC,"entity_drive_orb", 0.25F, 0.25F);
    public static final Supplier<EntityType<FocusOrbEntity>> TYPE_FOCUSORB = createEntityType(FocusOrbEntity::new, MobCategory.MISC,"entity_focus_orb", 0.25F, 0.25F);

    public static final Supplier<EntityType<FireEntity>> TYPE_FIRE = createEntityType(FireEntity::new, MobCategory.MISC,"entity_fire", 0.5F, 0.5F);
    public static final Supplier<EntityType<FiraEntity>> TYPE_FIRA = createEntityType(FiraEntity::new, MobCategory.MISC,"entity_fira", 0.8F, 0.8F);
    public static final Supplier<EntityType<FiragaEntity>> TYPE_FIRAGA = createEntityType(FiragaEntity::new, MobCategory.MISC,"entity_firaga", 1.2F, 1.2F);
    public static final Supplier<EntityType<FirazaEntity>> TYPE_FIRAZA = createEntityType(FirazaEntity::new, MobCategory.MISC,"entity_firaza", 1.2F, 1.2F);
    
    public static final Supplier<EntityType<BlizzardEntity>> TYPE_BLIZZARD = createEntityType(BlizzardEntity::new, MobCategory.MISC,"entity_blizzard", 0.5F, 0.5F);
    public static final Supplier<EntityType<BlizzazaEntity>> TYPE_BLIZZAZA = createEntityType(BlizzazaEntity::new, MobCategory.MISC,"entity_blizzaza", 0.5F, 0.5F);

    public static final Supplier<EntityType<WaterEntity>> TYPE_WATER = createEntityType(WaterEntity::new, MobCategory.MISC,"entity_water", 0.5F, 0.5F);
    public static final Supplier<EntityType<WateraEntity>> TYPE_WATERA = createEntityType(WateraEntity::new, MobCategory.MISC,"entity_watera", 0.8F, 0.8F);
    public static final Supplier<EntityType<WatergaEntity>> TYPE_WATERGA = createEntityType(WatergaEntity::new, MobCategory.MISC,"entity_waterga", 1F, 1F);
    public static final Supplier<EntityType<WaterzaEntity>> TYPE_WATERZA = createEntityType(WaterzaEntity::new, MobCategory.MISC,"entity_waterza", 1F, 1F);
    
    public static final Supplier<EntityType<ThunderEntity>> TYPE_THUNDER = createEntityType(ThunderEntity::new, MobCategory.MISC,"entity_thunder", 0.5F, 0.5F);
    public static final Supplier<EntityType<ThundaraEntity>> TYPE_THUNDARA = createEntityType(ThundaraEntity::new, MobCategory.MISC,"entity_thundara", 0.5F, 0.5F);
    public static final Supplier<EntityType<ThundagaEntity>> TYPE_THUNDAGA = createEntityType(ThundagaEntity::new, MobCategory.MISC,"entity_thundaga", 0.5F, 0.5F);
    public static final Supplier<EntityType<ThundazaEntity>> TYPE_THUNDAZA = createEntityType(ThundazaEntity::new, MobCategory.MISC,"entity_thundaza", 0.5F, 0.5F);
    public static final Supplier<EntityType<ThunderBoltEntity>> TYPE_THUNDERBOLT = createEntityType(ThunderBoltEntity::new, MobCategory.MISC,"entity_thunderbolt", 0.5F, 0.5F);
    
    public static final Supplier<EntityType<MagnetEntity>> TYPE_MAGNET = createEntityType(MagnetEntity::new, MobCategory.MISC,"entity_magnet", 1F, 1F);
    public static final Supplier<EntityType<MagneraEntity>> TYPE_MAGNERA = createEntityType(MagneraEntity::new, MobCategory.MISC,"entity_magnera", 1.5F, 1.5F);
    public static final Supplier<EntityType<MagnegaEntity>> TYPE_MAGNEGA = createEntityType(MagnegaEntity::new, MobCategory.MISC,"entity_magnega", 2F, 2F);
    
    public static final Supplier<EntityType<GravityEntity>> TYPE_GRAVITY = createEntityType(GravityEntity::new, MobCategory.MISC,"entity_gravity", 0.5F, 0.5F);
    public static final Supplier<EntityType<GraviraEntity>> TYPE_GRAVIRA = createEntityType(GraviraEntity::new, MobCategory.MISC,"entity_gravira", 0.5F, 0.5F);
    public static final Supplier<EntityType<GravigaEntity>> TYPE_GRAVIGA = createEntityType(GravigaEntity::new, MobCategory.MISC,"entity_graviga", 0.5F, 0.5F);

    public static final Supplier<EntityType<SeedBulletEntity>> TYPE_SEED_BULLET = createEntityType(SeedBulletEntity::new, MobCategory.MISC,"seed_bullet", 0.5F, 0.5F);
    public static final Supplier<EntityType<ArrowgunShotEntity>> TYPE_ARROWGUN_SHOT = createEntityType(ArrowgunShotEntity::new, MobCategory.MISC,"arrowgun_shot", 0.1F, 0.1F);

    public static final Supplier<EntityType<SaixShockwave>> TYPE_SAIX_SHOCKWAVE = createEntityType(SaixShockwave::new, MobCategory.MISC,"saix_shockwave", 1.5F,3.5F);

    public static final Supplier<EntityType<OrgPortalEntity>> TYPE_ORG_PORTAL = createEntityType(OrgPortalEntity::new, MobCategory.MISC,"entity_org_portal", 1F, 3.5F);
    
   // public static final Supplier<EntityType<ChakramEntity>> TYPE_CHAKRAM = createEntityType(ChakramEntity::new, ChakramEntity::new, MobCategory.MISC,"entity_chakram", 1.3F, 0.5F);
    public static final Supplier<EntityType<KKThrowableEntity>> TYPE_KK_THROWABLE = ENTITIES.register("entity_chakram",
    		() -> EntityType.Builder.<KKThrowableEntity>of((e,w)->new KKThrowableEntity(w), MobCategory.MISC).sized(2.5F, 0.75F).clientTrackingRange(10)
                    .build("entity_chakram"));

    public static final Supplier<EntityType<LanceEntity>> TYPE_LANCE = createEntityType(LanceEntity::new, MobCategory.MISC,"entity_lance", 0.5F, 0.5F);
    
    public static final Supplier<EntityType<HeartEntity>> TYPE_HEART = createEntityType(HeartEntity::new, MobCategory.MISC, "heart", 1F, 1F);
    public static final Supplier<EntityType<XPEntity>> TYPE_XP = createEntityType(XPEntity::new, MobCategory.MISC, "xp", 1F, 1F);

    //Mobs
    public static final Item.Properties PROPERTIES = new Item.Properties();

    public static final Supplier<EntityType<MoogleEntity>> TYPE_MOOGLE = createEntityType(MoogleEntity::new, MobCategory.AMBIENT, "moogle", 0.6F, 1.5F);
    public static final Supplier<Item> MOOGLE_EGG = ModItems.ITEMS.register("moogle_spawn_egg", () -> new DeferredSpawnEggItem(TYPE_MOOGLE, 0xDACAB0, 0xC50033, PROPERTIES));

    public static final Supplier<EntityType<ShadowEntity>> TYPE_SHADOW = createEntityType(ShadowEntity::new, MobCategory.MONSTER, "shadow", 0.5F, 0.7F);
    public static final Supplier<Item> SHADOW_EGG = ModItems.ITEMS.register("shadow_spawn_egg", () -> new DeferredSpawnEggItem(TYPE_SHADOW, 0x000000, 0xFFFF00, PROPERTIES));
    public static final Supplier<EntityType<MegaShadowEntity>> TYPE_MEGA_SHADOW = createEntityType(MegaShadowEntity::new, MobCategory.MONSTER, "mega_shadow", 1.5F, 1.7F);
    public static final Supplier<Item> MEGA_SHADOW_EGG = ModItems.ITEMS.register("mega_shadow_spawn_egg", () -> new DeferredSpawnEggItem(TYPE_MEGA_SHADOW, 0x000000, 0xAAAA00, PROPERTIES));
    public static final Supplier<EntityType<GigaShadowEntity>> TYPE_GIGA_SHADOW = createEntityType(GigaShadowEntity::new, MobCategory.MONSTER, "giga_shadow", 2.5F, 2.7F);
    public static final Supplier<Item> GIGA_SHADOW_EGG = ModItems.ITEMS.register("giga_shadow_spawn_egg", () -> new DeferredSpawnEggItem(TYPE_GIGA_SHADOW, 0x000000, 0x666600, PROPERTIES));
    public static final Supplier<EntityType<DarkballEntity>> TYPE_DARKBALL = createEntityType(DarkballEntity::new, MobCategory.MONSTER, "darkball", 1.5F, 2F);
    public static final Supplier<Item> DARKBALL_EGG = ModItems.ITEMS.register("darkball_spawn_egg", () -> new DeferredSpawnEggItem(TYPE_DARKBALL, 0x000000, 0x6600FF, PROPERTIES));
    public static final Supplier<EntityType<ShadowGlobEntity>> TYPE_SHADOW_GLOB = createEntityType(ShadowGlobEntity::new, MobCategory.MONSTER, "shadow_glob", 1F, 1F);
    public static final Supplier<Item> SHADOW_GLOB_EGG = ModItems.ITEMS.register("shadow_glob_spawn_egg", () -> new DeferredSpawnEggItem(TYPE_SHADOW_GLOB, Color.BLACK.getRGB(), 0x312ba0, PROPERTIES));

    //Emblems
    public static final Supplier<EntityType<MinuteBombEntity>> TYPE_MINUTE_BOMB = createEntityType(MinuteBombEntity::new, MobCategory.MONSTER, "minute_bomb", 0.6F, 1.3F);
    public static final Supplier<Item> MINUTE_BOMB_EGG = ModItems.ITEMS.register("minute_bomb_spawn_egg", () -> new DeferredSpawnEggItem(TYPE_MINUTE_BOMB, 0x020030, 0x8B4513, PROPERTIES));
    public static final Supplier<EntityType<SkaterBombEntity>> TYPE_SKATER_BOMB = createEntityType(SkaterBombEntity::new, MobCategory.MONSTER, "skater_bomb", 0.6F, 1.3F);
    public static final Supplier<Item> SKATER_BOMB_EGG = ModItems.ITEMS.register("skater_bomb_spawn_egg", () -> new DeferredSpawnEggItem(TYPE_SKATER_BOMB, 0x020030, 0xAAAAFF, PROPERTIES));
    public static final Supplier<EntityType<StormBombEntity>> TYPE_STORM_BOMB = createEntityType(StormBombEntity::new, MobCategory.MONSTER, "storm_bomb", 0.6F, 1.3F);
    public static final Supplier<Item> STORM_BOMB_EGG = ModItems.ITEMS.register("storm_bomb_spawn_egg", () -> new DeferredSpawnEggItem(TYPE_STORM_BOMB, 0x020030, Color.CYAN.getRGB(), PROPERTIES));
    public static final Supplier<EntityType<DetonatorEntity>> TYPE_DETONATOR = createEntityType(DetonatorEntity::new, MobCategory.MONSTER, "detonator", 0.6F, 1.3F);
    public static final Supplier<Item> DETONATOR_EGG = ModItems.ITEMS.register("detonator_spawn_egg", () -> new DeferredSpawnEggItem(TYPE_DETONATOR, 0x020030, Color.RED.getRGB(), PROPERTIES));
    public static final Supplier<EntityType<RedNocturneEntity>> TYPE_RED_NOCTURNE = createEntityTypeImmuneToFire(RedNocturneEntity::new, MobCategory.MONSTER, "red_nocturne", 1.0F,  2.0F);
    public static final Supplier<Item> RED_NOCTURNE_EGG = ModItems.ITEMS.register("red_nocturne_spawn_egg", () -> new DeferredSpawnEggItem(TYPE_RED_NOCTURNE, Color.RED.getRGB(), Color.YELLOW.getRGB(), PROPERTIES));
    public static final Supplier<EntityType<BlueRhapsodyEntity>> TYPE_BLUE_RHAPSODY = createEntityType(BlueRhapsodyEntity::new, MobCategory.MONSTER, "blue_rhapsody", 1.0F, 2.0F);
    public static final Supplier<Item> BLUE_RHAPSODY_EGG = ModItems.ITEMS.register("blue_rhapsody_spawn_egg", () -> new DeferredSpawnEggItem(TYPE_BLUE_RHAPSODY, Color.BLUE.getRGB(), Color.YELLOW.getRGB(), PROPERTIES));
    public static final Supplier<EntityType<YellowOperaEntity>> TYPE_YELLOW_OPERA = createEntityType(YellowOperaEntity::new, MobCategory.MONSTER, "yellow_opera", 1.0F, 2.0F);
    public static final Supplier<Item> YELLOW_OPERA_EGG = ModItems.ITEMS.register("yellow_opera_spawn_egg", () -> new DeferredSpawnEggItem(TYPE_YELLOW_OPERA, Color.YELLOW.getRGB(), Color.YELLOW.getRGB(), PROPERTIES));
    public static final Supplier<EntityType<GreenRequiemEntity>> TYPE_GREEN_REQUIEM = createEntityType(GreenRequiemEntity::new, MobCategory.MONSTER, "green_requiem", 1.0F, 2.0F);
    public static final Supplier<Item> GREEN_REQUIEM_EGG = ModItems.ITEMS.register("green_requiem_spawn_egg", () -> new DeferredSpawnEggItem(TYPE_GREEN_REQUIEM, Color.GREEN.getRGB(), Color.YELLOW.getRGB(), PROPERTIES));
    public static final Supplier<EntityType<GummiShipEntity>> TYPE_GUMMI_SHIP = createEntityType(GummiShipEntity::new, MobCategory.MISC, "gummi_ship", 5.0F, 5.0F);
    public static final Supplier<EntityType<SpawningOrbEntity>> TYPE_SPAWNING_ORB = createEntityType(SpawningOrbEntity::new, MobCategory.MONSTER, "spawning_orb", 1.5F,  1.5F);

    public static final Supplier<EntityType<SoldierEntity>> TYPE_SOLDIER = createEntityType(SoldierEntity::new, MobCategory.MONSTER, "soldier", 0.8F, 1.6F);
    public static final Supplier<Item> SOLDIER_EGG = ModItems.ITEMS.register("soldier_spawn_egg", () -> new DeferredSpawnEggItem(TYPE_SOLDIER, Color.BLUE.getRGB(), Color.RED.getRGB(), PROPERTIES));

    public static final Supplier<EntityType<WhiteMushroomEntity>> TYPE_WHITE_MUSHROOM = createEntityType(WhiteMushroomEntity::new, MobCategory.MONSTER, "white_mushroom", 0.6F, 1.1F);
    public static final Supplier<Item> WHITE_MUSHROOM_EGG = ModItems.ITEMS.register("white_mushroom_spawn_egg", () -> new DeferredSpawnEggItem(TYPE_WHITE_MUSHROOM, 0xFAF3B9, Color.RED.getRGB(), PROPERTIES));

    public static final Supplier<EntityType<BlackFungusEntity>> TYPE_BLACK_FUNGUS = createEntityType(BlackFungusEntity::new, MobCategory.MONSTER, "black_fungus", 0.6F, 1.1F);
    public static final Supplier<Item> BLACK_FUNGUS_EGG = ModItems.ITEMS.register("black_fungus_spawn_egg", () -> new DeferredSpawnEggItem(TYPE_BLACK_FUNGUS, Color.DARK_GRAY.getRGB(), Color.MAGENTA.getRGB(), PROPERTIES));


    //TODO update textures to work with newer model, make magic for
    /*
    public static final Supplier<EntityType<ShadowEntity>> TYPE_SILVER_ROCK = createEntityType(ShadowEntity::new, ShadowEntity::new, EntityClassification.MONSTER, "silver_rock", 1.0F, 1.0F);
    public static final Supplier<EntityType<ShadowEntity>> TYPE_CRIMSON_JAZZ = createEntityType(ShadowEntity::new, ShadowEntity::new, EntityClassification.MONSTER, "crimson_jazz", 1.0F, 1.0F);
    */
    public static final Supplier<EntityType<EmeraldBluesEntity>> TYPE_EMERALD_BLUES = createEntityType(EmeraldBluesEntity::new, MobCategory.MONSTER, "emerald_blues", 0.8F, 1.6F);
    public static final Supplier<Item> EMERALD_BLUES_EGG = ModItems.ITEMS.register("emerald_blues_spawn_egg", () -> new DeferredSpawnEggItem(TYPE_EMERALD_BLUES, Color.CYAN.getRGB(), Color.GREEN.getRGB(), PROPERTIES));


    public static final Supplier<EntityType<LargeBodyEntity>> TYPE_LARGE_BODY = createEntityType(LargeBodyEntity::new, MobCategory.MONSTER, "large_body", 1.3F, 1.6F);
    public static final Supplier<Item> LARGE_BODY_EGG = ModItems.ITEMS.register("large_body_spawn_egg", () -> new DeferredSpawnEggItem(TYPE_LARGE_BODY, 0x4d177c, 0x29014c, PROPERTIES));
    //TODO make AI
    public static final Supplier<EntityType<DirePlantEntity>> TYPE_DIRE_PLANT = createEntityType(DirePlantEntity::new, MobCategory.MONSTER, "dire_plant", 0.75F, 1.5F);
    public static final Supplier<Item> DIRE_PLANT_EGG = ModItems.ITEMS.register("dire_plant_spawn_egg", () -> new DeferredSpawnEggItem(TYPE_DIRE_PLANT, 0x4ba04e, 0xedc2c2, PROPERTIES));

    //Nobodies
    public static final Supplier<EntityType<NobodyCreeperEntity>> TYPE_NOBODY_CREEPER = createEntityType(NobodyCreeperEntity::new, MobCategory.MONSTER, "nobody_creeper", 1F, 1.5F);
    public static final Supplier<Item> NOBODY_CREEPER_EGG = ModItems.ITEMS.register("nobody_creeper_spawn_egg", () -> new DeferredSpawnEggItem(TYPE_NOBODY_CREEPER, 0xb8bdc4, 0xfcfcfc, PROPERTIES));
    public static final Supplier<EntityType<DuskEntity>> TYPE_DUSK = createEntityType(DuskEntity::new, MobCategory.MONSTER, "dusk", 1F, 1.8F);
    public static final Supplier<Item> DUSK_EGG = ModItems.ITEMS.register("dusk_spawn_egg", () -> new DeferredSpawnEggItem(TYPE_DUSK, 0xb8bdc4, 0xfcfcfc, PROPERTIES));
    public static final Supplier<EntityType<AssassinEntity>> TYPE_ASSASSIN = createEntityType(AssassinEntity::new, MobCategory.MONSTER, "assassin", 1.2F, 2F);
    public static final Supplier<Item> ASSASSIN_EGG = ModItems.ITEMS.register("assassin_spawn_egg", () -> new DeferredSpawnEggItem(TYPE_ASSASSIN, 0xc9c9c9, 0xd4ccff, PROPERTIES));
    public static final Supplier<EntityType<DragoonEntity>> TYPE_DRAGOON = createEntityType(DragoonEntity::new, MobCategory.MONSTER, "dragoon", 1F, 2F);
    public static final Supplier<Item> DRAGOON_EGG = ModItems.ITEMS.register("dragoon_spawn_egg", () -> new DeferredSpawnEggItem(TYPE_DRAGOON, 0xc9c9c9, 0xc2387f, PROPERTIES));

    //Bosses
    public static final Supplier<EntityType<MarluxiaEntity>> TYPE_MARLUXIA = createEntityTypeImmuneToFire(MarluxiaEntity::new, MobCategory.MONSTER, "marluxia", 1F, 2F);
    public static final Supplier<Item> MARLUXIA_EGG = ModItems.ITEMS.register("marluxia_spawn_egg", () -> new DeferredSpawnEggItem(TYPE_MARLUXIA, 0xc9c9c9, 0xFF00FF, PROPERTIES));
    
    //Limits
    public static final Supplier<EntityType<LaserCircleCoreEntity>> TYPE_LASER_CIRCLE = createEntityType(LaserCircleCoreEntity::new, MobCategory.MISC,"entity_laser_circle_core", 0.5F, 0.5F);
    public static final Supplier<EntityType<LaserDomeCoreEntity>> TYPE_LASER_DOME = createEntityType(LaserDomeCoreEntity::new, MobCategory.MISC,"entity_laser_dome_core", 0.5F, 0.5F);
    public static final Supplier<EntityType<LaserDomeShotEntity>> TYPE_LASER_SHOT = createEntityType(LaserDomeShotEntity::new, MobCategory.MISC,"entity_laser_dome_shot", 0.5F, 0.5F);
    public static final Supplier<EntityType<ArrowRainCoreEntity>> TYPE_ARROW_RAIN = createEntityType(ArrowRainCoreEntity::new, MobCategory.MISC,"entity_arrow_rain_core", 0.5F, 0.5F);
    public static final Supplier<EntityType<ThunderTrailCoreEntity>> TYPE_THUNDER_TRAIL = createEntityType(ThunderTrailCoreEntity::new, MobCategory.MISC,"entity_thunder_trail_core", 0.5F, 0.5F);
    
	public static final Supplier<EntityType<DarkVolleyCoreEntity>> TYPE_SHOTLOCK_DARK_VOLLEY = createEntityType(DarkVolleyCoreEntity::new, MobCategory.MISC, "entity_shotlock_volley_core", 0.5F, 0.5F);
	public static final Supplier<EntityType<RagnarokCoreEntity>> TYPE_SHOTLOCK_CIRCULAR = createEntityType(RagnarokCoreEntity::new, MobCategory.MISC, "entity_shotlock_circular_core", 0.5F, 0.5F);
	public static final Supplier<EntityType<SonicBladeCoreEntity>> TYPE_SHOTLOCK_SONIC_BLADE = createEntityType(SonicBladeCoreEntity::new, MobCategory.MISC, "entity_shotlock_sonic_blade_core", 0.5F, 0.5F);
	public static final Supplier<EntityType<PrismRainCoreEntity>> TYPE_PRISM_RAIN = createEntityType(PrismRainCoreEntity::new, MobCategory.MISC, "entity_shotlock_prism_rain", 0.5F, 0.5F);

	public static final Supplier<EntityType<BaseShotlockShotEntity>> TYPE_VOLLEY_SHOTLOCK_SHOT = createEntityType(VolleyShotEntity::new, MobCategory.MISC, "entity_volley_shotlock_shot", 0.5F, 0.5F);
	public static final Supplier<EntityType<BaseShotlockShotEntity>> TYPE_RAGNAROK_SHOTLOCK_SHOT = createEntityType(RagnarokShotEntity::new, MobCategory.MISC,"entity_ragnarok_shotlock_shot", 0.5F, 0.5F);
    public static final Supplier<EntityType<BaseShotlockShotEntity>> TYPE_ULTIMA_CANNON_SHOT = createEntityType(UltimaCannonShotEntity::new, MobCategory.MISC,"entity_ultima_cannon_shotlock_shot", 0.5F, 0.5F);

    /**
     * Helper method to create a new EntityType and set the registry name
     * @param factory The entity type factory
     * @param classification The classification of the entity
     * @param name The registry name of the entity
     * @param sizeX The X size of the entity
     * @param sizeY The Y size of the entity
     * @param <T> The entity type
     * @return The EntityType created
     */
    public static <T extends Entity, M extends EntityType<T>>Supplier<EntityType<T>> createEntityType(EntityType.EntityFactory<T> factory, MobCategory classification, String name, float sizeX, float sizeY) {
        return ENTITIES.register(name, () -> EntityType.Builder.of(factory, classification)
                .setShouldReceiveVelocityUpdates(false)
                .setUpdateInterval(1)
                .setTrackingRange(8)
                .sized(sizeX, sizeY)
                .build(name));
    }

    public static <T extends Entity, M extends EntityType<T>>Supplier<EntityType<T>> createEntityTypeImmuneToFire(EntityType.EntityFactory<T> factory, MobCategory classification, String name, float sizeX, float sizeY) {
        return ENTITIES.register(name, () -> EntityType.Builder.of(factory, classification)
                .setShouldReceiveVelocityUpdates(true)
                .setUpdateInterval(1)
                .setTrackingRange(128)
                .sized(sizeX, sizeY)
                .fireImmune()
                .build(name));
    }



    public static void addToGroup(EntityHelper.MobType group, EntityType<?> type, int level) {
        switch (group) {
            case HEARTLESS_PUREBLOOD -> pureblood.put(type, level);
            case HEARTLESS_EMBLEM -> emblem.put(type, level);
            case NOBODY -> nobody.put(type, level);
            case NPC -> npc.add(type);
        }
    }

    /**
     * Register the render classes for the entities, called in {@link ClientSetup#registerRenderers(EntityRenderersEvent.RegisterRenderers)}
     */
    @OnlyIn(Dist.CLIENT)
    public static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
        //Entities
        
        event.registerEntityRenderer(TYPE_BLAST_BLOX.get(), BlastBloxRenderer::new);

        event.registerEntityRenderer(TYPE_PAIR_BLOX.get(), PairBloxRenderer::new);
        event.registerEntityRenderer(TYPE_MUNNY.get(), MunnyRenderer::new);
        event.registerEntityRenderer(TYPE_HPORB.get(), HPOrbRenderer::new);
        event.registerEntityRenderer(TYPE_MPORB.get(), MPOrbRenderer::new);
        event.registerEntityRenderer(TYPE_DRIVEORB.get(), DriveOrbRenderer::new);
        event.registerEntityRenderer(TYPE_FOCUSORB.get(), FocusOrbRenderer::new);
        
        event.registerEntityRenderer(TYPE_FIRE.get(), InvisibleEntityRenderer::new);
        event.registerEntityRenderer(TYPE_FIRA.get(), InvisibleEntityRenderer::new);
        event.registerEntityRenderer(TYPE_FIRAGA.get(), InvisibleEntityRenderer::new);
        event.registerEntityRenderer(TYPE_FIRAZA.get(), InvisibleEntityRenderer::new);
        event.registerEntityRenderer(TYPE_BLIZZARD.get(), InvisibleEntityRenderer::new);
        event.registerEntityRenderer(TYPE_BLIZZAZA.get(), InvisibleEntityRenderer::new);
        event.registerEntityRenderer(TYPE_THUNDER.get(), InvisibleEntityRenderer::new);
        event.registerEntityRenderer(TYPE_THUNDARA.get(), InvisibleEntityRenderer::new);
        event.registerEntityRenderer(TYPE_THUNDAGA.get(), InvisibleEntityRenderer::new);
        event.registerEntityRenderer(TYPE_THUNDAZA.get(), InvisibleEntityRenderer::new);
        event.registerEntityRenderer(TYPE_THUNDERBOLT.get(), ThunderBoltEntityRenderer::new);
        event.registerEntityRenderer(TYPE_MAGNET.get(), MagnetEntityRenderer::new);
        event.registerEntityRenderer(TYPE_MAGNERA.get(), MagnetEntityRenderer::new);
        event.registerEntityRenderer(TYPE_MAGNEGA.get(), MagnetEntityRenderer::new);
        event.registerEntityRenderer(TYPE_WATER.get(), InvisibleEntityRenderer::new);
        event.registerEntityRenderer(TYPE_WATERA.get(), InvisibleEntityRenderer::new);
        event.registerEntityRenderer(TYPE_WATERGA.get(), InvisibleEntityRenderer::new);
        event.registerEntityRenderer(TYPE_WATERZA.get(), InvisibleEntityRenderer::new);
        event.registerEntityRenderer(TYPE_GRAVITY.get(), InvisibleEntityRenderer::new);
        event.registerEntityRenderer(TYPE_GRAVIRA.get(), InvisibleEntityRenderer::new);
        event.registerEntityRenderer(TYPE_GRAVIGA.get(), InvisibleEntityRenderer::new);

        event.registerEntityRenderer(TYPE_SAIX_SHOCKWAVE.get(),InvisibleEntityRenderer::new);
        
        event.registerEntityRenderer(TYPE_SPAWNING_ORB.get(), SpawningOrbRenderer::new);
        
        event.registerEntityRenderer(TYPE_MOOGLE.get(), MoogleRenderer::new);
        event.registerEntityRenderer(TYPE_SHADOW.get(), ShadowRenderer::new);
        event.registerEntityRenderer(TYPE_MEGA_SHADOW.get(), MegaShadowRenderer::new);
        event.registerEntityRenderer(TYPE_GIGA_SHADOW.get(), GigaShadowRenderer::new);
        event.registerEntityRenderer(TYPE_DARKBALL.get(), DarkballRenderer::new);
        event.registerEntityRenderer(TYPE_SHADOW_GLOB.get(), ShadowGlobRenderer::new);
        event.registerEntityRenderer(TYPE_LARGE_BODY.get(), LargeBodyRenderer::new);
        event.registerEntityRenderer(TYPE_MINUTE_BOMB.get(), BombRenderer::new);
        event.registerEntityRenderer(TYPE_SKATER_BOMB.get(), BombRenderer::new);
        event.registerEntityRenderer(TYPE_STORM_BOMB.get(), BombRenderer::new);
        event.registerEntityRenderer(TYPE_DETONATOR.get(), BombRenderer::new);
        event.registerEntityRenderer(TYPE_NOBODY_CREEPER.get(), NobodyCreeperRenderer::new);
        event.registerEntityRenderer(TYPE_RED_NOCTURNE.get(), ElementalMusicalHeartlessRenderer::new);
        event.registerEntityRenderer(TYPE_BLUE_RHAPSODY.get(), ElementalMusicalHeartlessRenderer::new);
        event.registerEntityRenderer(TYPE_YELLOW_OPERA.get(), ElementalMusicalHeartlessRenderer::new);
        event.registerEntityRenderer(TYPE_GREEN_REQUIEM.get(), ElementalMusicalHeartlessRenderer::new);
        event.registerEntityRenderer(TYPE_EMERALD_BLUES.get(), ElementalMusicalHeartlessRenderer::new);
        event.registerEntityRenderer(TYPE_DUSK.get(), DuskRenderer::new);
        event.registerEntityRenderer(TYPE_ASSASSIN.get(), AssassinRenderer::new);
        event.registerEntityRenderer(TYPE_DIRE_PLANT.get(), DirePlantRenderer::new);
        event.registerEntityRenderer(TYPE_SOLDIER.get(), SoldierRenderer::new);
        event.registerEntityRenderer(TYPE_DRAGOON.get(), DragoonRenderer::new);
        event.registerEntityRenderer(TYPE_WHITE_MUSHROOM.get(), WhiteMushroomRenderer::new);
        event.registerEntityRenderer(TYPE_BLACK_FUNGUS.get(), BlackFungusRenderer::new);


        event.registerEntityRenderer(TYPE_ORG_PORTAL.get(), OrgPortalEntityRenderer::new);
        event.registerEntityRenderer(TYPE_HEART.get(), HeartEntityRenderer::new);
        event.registerEntityRenderer(TYPE_XP.get(), XPEntityRenderer::new);

        //event.registerEntityRenderer(TYPE_CHAKRAM.get(), ChakramEntityRenderer::new);
        EntityRenderers.register(TYPE_KK_THROWABLE.get(), KKThrowableEntityRenderer::new);

        event.registerEntityRenderer(TYPE_LANCE.get(), KKThrowableEntityRenderer::new);

        event.registerEntityRenderer(TYPE_SEED_BULLET.get(), SeedBulletRenderer::new);
        event.registerEntityRenderer(TYPE_ARROWGUN_SHOT.get(), ArrowgunShotEntityRenderer::new);

        event.registerEntityRenderer(TYPE_LASER_CIRCLE.get(), LaserCircleEntityRenderer::new);
        event.registerEntityRenderer(TYPE_LASER_DOME.get(), LaserDomeEntityRenderer::new);
        event.registerEntityRenderer(TYPE_LASER_SHOT.get(), LaserDomeShotEntityRenderer::new);
        event.registerEntityRenderer(TYPE_ARROW_RAIN.get(), ArrowRainCoreEntityRenderer::new);
        event.registerEntityRenderer(TYPE_THUNDER_TRAIL.get(), InvisibleEntityRenderer::new);
        event.registerEntityRenderer(TYPE_SHOTLOCK_DARK_VOLLEY.get(), InvisibleEntityRenderer::new);
        event.registerEntityRenderer(TYPE_SHOTLOCK_CIRCULAR.get(), InvisibleEntityRenderer::new);
        event.registerEntityRenderer(TYPE_SHOTLOCK_SONIC_BLADE.get(), InvisibleEntityRenderer::new);
        event.registerEntityRenderer(TYPE_PRISM_RAIN.get(), InvisibleEntityRenderer::new);
        event.registerEntityRenderer(TYPE_VOLLEY_SHOTLOCK_SHOT.get(), VolleyShotlockShotEntityRenderer::new);
        event.registerEntityRenderer(TYPE_ULTIMA_CANNON_SHOT.get(), UltimaCannonShotlockShotEntityRenderer::new);
        event.registerEntityRenderer(TYPE_RAGNAROK_SHOTLOCK_SHOT.get(), VolleyShotlockShotEntityRenderer::new);
        
        event.registerEntityRenderer(TYPE_GUMMI_SHIP.get(), GummiShipEntityRenderer::new);
        
        event.registerEntityRenderer(TYPE_MARLUXIA.get(), MarluxiaRenderer::new);
        
        //Tile Entities

        event.registerBlockEntityRenderer(TYPE_PEDESTAL.get(), PedestalRenderer::new);
        event.registerBlockEntityRenderer(TYPE_MOOGLE_PROJECTOR.get(), MoogleProjectorRenderer::new);
        event.registerBlockEntityRenderer(TYPE_SOA_PLATFORM.get(), SoAPlatformRenderer::new);
        event.registerBlockEntityRenderer(TYPE_PEDESTAL.get(), PedestalRenderer::new);
        event.registerBlockEntityRenderer(TYPE_AIRSTEP_TARGET_TE.get(), AirstepTargetRenderer::new);
    }

    @OnlyIn(Dist.CLIENT)
    public static void registerLayers(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(AssassinModel.LAYER_LOCATION, AssassinModel::createBodyLayer);
        event.registerLayerDefinition(BombModel.LAYER_LOCATION, BombModel::createBodyLayer);
        event.registerLayerDefinition(CubeModel.LAYER_LOCATION, CubeModel::createBodyLayer);
        event.registerLayerDefinition(DarkballModel.LAYER_LOCATION, DarkballModel::createBodyLayer);
        event.registerLayerDefinition(DirePlantModel.LAYER_LOCATION, DirePlantModel::createBodyLayer);
        event.registerLayerDefinition(SoldierModel.LAYER_LOCATION, SoldierModel::createBodyLayer);
        event.registerLayerDefinition(WhiteMushroomModel.LAYER_LOCATION, WhiteMushroomModel::createBodyLayer);
        //event.registerLayerDefinition(.LAYER_LOCATION, WhiteMushroomModel::createBodyLayer);
        event.registerLayerDefinition(DragoonModel.LAYER_LOCATION, DragoonModel::createBodyLayer);
        event.registerLayerDefinition(DuskModel.LAYER_LOCATION, DuskModel::createBodyLayer);
        event.registerLayerDefinition(ElementalMusicalHeartlessModel.LAYER_LOCATION, ElementalMusicalHeartlessModel::createBodyLayer);
        event.registerLayerDefinition(LargeBodyModel.LAYER_LOCATION, LargeBodyModel::createBodyLayer);
        event.registerLayerDefinition(MarluxiaModel.LAYER_LOCATION, MarluxiaModel::createBodyLayer);
        event.registerLayerDefinition(MoogleModel.LAYER_LOCATION, MoogleModel::createBodyLayer);
        event.registerLayerDefinition(NobodyCreeperModel.LAYER_LOCATION, NobodyCreeperModel::createBodyLayer);
        event.registerLayerDefinition(ShadowGlobModel.LAYER_LOCATION, ShadowGlobModel::createBodyLayer);
        event.registerLayerDefinition(ShadowModel.LAYER_LOCATION, ShadowModel::createBodyLayer);
        event.registerLayerDefinition(BlizzardModel.LAYER_LOCATION, BlizzardModel::createBodyLayer);
        event.registerLayerDefinition(FireModel.LAYER_LOCATION, FireModel::createBodyLayer);
        event.registerLayerDefinition(ArmorModel.LAYER_LOCATION_TOP, () -> ArmorModel.createBodyLayer(new CubeDeformation(0.5F)));
        event.registerLayerDefinition(ArmorModel.LAYER_LOCATION_BOTTOM, () -> ArmorModel.createBodyLayer(new CubeDeformation(0.25F)));
        event.registerLayerDefinition(VentusModel.LAYER_LOCATION_TOP, () -> VentusModel.createBodyLayer(new CubeDeformation(0.5F)));
        event.registerLayerDefinition(VentusModel.LAYER_LOCATION_BOTTOM, () -> VentusModel.createBodyLayer(new CubeDeformation(0.25F)));
        event.registerLayerDefinition(TerraModel.LAYER_LOCATION_TOP, () -> TerraModel.createBodyLayer(new CubeDeformation(0.5F)));
        event.registerLayerDefinition(TerraModel.LAYER_LOCATION_BOTTOM, () -> TerraModel.createBodyLayer(new CubeDeformation(0.25F)));
        event.registerLayerDefinition(AquaModel.LAYER_LOCATION_TOP, () -> AquaModel.createBodyLayer(new CubeDeformation(0.5F)));
        event.registerLayerDefinition(AquaModel.LAYER_LOCATION_BOTTOM, () -> AquaModel.createBodyLayer(new CubeDeformation(0.25F)));
        event.registerLayerDefinition(EraqusModel.LAYER_LOCATION_TOP, () -> EraqusModel.createBodyLayer(new CubeDeformation(0.5F)));
        event.registerLayerDefinition(EraqusModel.LAYER_LOCATION_BOTTOM, () -> EraqusModel.createBodyLayer(new CubeDeformation(0.25F)));
        event.registerLayerDefinition(XehanortModel.LAYER_LOCATION_TOP, () -> XehanortModel.createBodyLayer(new CubeDeformation(0.5F)));
        event.registerLayerDefinition(XehanortModel.LAYER_LOCATION_BOTTOM, () -> XehanortModel.createBodyLayer(new CubeDeformation(0.25F)));

        event.registerLayerDefinition(StopModel.LAYER_LOCATION, StopModel::createBodyLayer);
        event.registerLayerDefinition(MagnetModel.LAYER_LOCATION, MagnetModel::createBodyLayer);
        event.registerLayerDefinition(TerraShoulderModel.LAYER_LOCATION, () -> TerraShoulderModel.createBodyLayer(new CubeDeformation(0)));
        event.registerLayerDefinition(AquaShoulderModel.LAYER_LOCATION, () -> AquaShoulderModel.createBodyLayer(new CubeDeformation(0)));
        event.registerLayerDefinition(VentusShoulderModel.LAYER_LOCATION, () -> VentusShoulderModel.createBodyLayer(new CubeDeformation(0)));
        event.registerLayerDefinition(EraqusShoulderModel.LAYER_LOCATION, () -> EraqusShoulderModel.createBodyLayer(new CubeDeformation(0)));
        event.registerLayerDefinition(XehanortShoulderModel.LAYER_LOCATION, () -> XehanortShoulderModel.createBodyLayer(new CubeDeformation(0)));
        event.registerLayerDefinition(UXArmorModel.LAYER_LOCATION_TOP, () -> UXArmorModel.createBodyLayer(new CubeDeformation(0), false));
        event.registerLayerDefinition(UXArmorModel.LAYER_LOCATION_BOTTOM, () -> UXArmorModel.createBodyLayer(new CubeDeformation(0), false));
        event.registerLayerDefinition(UXArmorModel.SLIM_LAYER_LOCATION_TOP, () -> UXArmorModel.createBodyLayer(new CubeDeformation(0), true));
        event.registerLayerDefinition(UXArmorModel.SLIM_LAYER_LOCATION_BOTTOM, () -> UXArmorModel.createBodyLayer(new CubeDeformation(0), true));
    }

    @SubscribeEvent
    public static void registerAttributes(EntityAttributeCreationEvent event) {
        event.put(TYPE_ASSASSIN.get(), AssassinEntity.registerAttributes().build());
        event.put(TYPE_BLUE_RHAPSODY.get(), BlueRhapsodyEntity.registerAttributes().build());
        event.put(TYPE_DARKBALL.get(), DarkballEntity.registerAttributes().build());
        event.put(TYPE_DETONATOR.get(), DetonatorEntity.registerAttributes().build());
        event.put(TYPE_DIRE_PLANT.get(), DirePlantEntity.registerAttributes().build());
        event.put(TYPE_DUSK.get(), DuskEntity.registerAttributes().build());
        event.put(TYPE_GIGA_SHADOW.get(), GigaShadowEntity.registerAttributes().build());
        event.put(TYPE_GREEN_REQUIEM.get(), GreenRequiemEntity.registerAttributes().build());
        event.put(TYPE_EMERALD_BLUES.get(), EmeraldBluesEntity.registerAttributes().build());
        event.put(TYPE_LARGE_BODY.get(), LargeBodyEntity.registerAttributes().build());
        event.put(TYPE_MEGA_SHADOW.get(), MegaShadowEntity.registerAttributes().build());
        event.put(TYPE_MINUTE_BOMB.get(), MinuteBombEntity.registerAttributes().build());
        event.put(TYPE_MOOGLE.get(), MoogleEntity.registerAttributes().build());
        event.put(TYPE_NOBODY_CREEPER.get(), NobodyCreeperEntity.registerAttributes().build());
        event.put(TYPE_RED_NOCTURNE.get(), RedNocturneEntity.registerAttributes().build());
        event.put(TYPE_SHADOW.get(), ShadowEntity.registerAttributes().build());
        event.put(TYPE_SHADOW_GLOB.get(), ShadowGlobEntity.registerAttributes().build());
        event.put(TYPE_SKATER_BOMB.get(), SkaterBombEntity.registerAttributes().build());
        event.put(TYPE_STORM_BOMB.get(), StormBombEntity.registerAttributes().build());
        event.put(TYPE_YELLOW_OPERA.get(), YellowOperaEntity.registerAttributes().build());
        event.put(TYPE_SOLDIER.get(), SoldierEntity.registerAttributes().build());
        event.put(TYPE_WHITE_MUSHROOM.get(), WhiteMushroomEntity.registerAttributes().build());
        event.put(TYPE_BLACK_FUNGUS.get(), BlackFungusEntity.registerAttributes().build());
        event.put(TYPE_DRAGOON.get(), DragoonEntity.registerAttributes().build());
        
        //GlobalEntityTypeAttributes.put(TYPE_GUMMI_SHIP.get(), GummiShipEntity.registerAttributes().create());
        event.put(TYPE_SPAWNING_ORB.get(), SpawningOrbEntity.registerAttributes().build());
        
        event.put(TYPE_MARLUXIA.get(), MarluxiaEntity.registerAttributes().build());
    }
    
    public static Monster getRandomEnemy(int level, Level world) {
        addToGroup(MobType.NPC, TYPE_MOOGLE.get(), 0);
        
        addToGroup(HEARTLESS_PUREBLOOD, TYPE_SHADOW.get(), 0);
        addToGroup(HEARTLESS_PUREBLOOD, TYPE_MEGA_SHADOW.get(), 10);
        addToGroup(HEARTLESS_PUREBLOOD, TYPE_GIGA_SHADOW.get(), 20);
        addToGroup(HEARTLESS_PUREBLOOD, TYPE_DARKBALL.get(), 15);
        addToGroup(HEARTLESS_PUREBLOOD, TYPE_SHADOW_GLOB.get(), 5);
        
        addToGroup(HEARTLESS_EMBLEM, TYPE_MINUTE_BOMB.get(), 4);
        addToGroup(HEARTLESS_EMBLEM, TYPE_SKATER_BOMB.get(), 8);
        addToGroup(HEARTLESS_EMBLEM, TYPE_STORM_BOMB.get(), 12);
        addToGroup(HEARTLESS_EMBLEM, TYPE_DETONATOR.get(), 16);
        addToGroup(HEARTLESS_EMBLEM, TYPE_RED_NOCTURNE.get(), 6);
        addToGroup(HEARTLESS_EMBLEM, TYPE_BLUE_RHAPSODY.get(), 6);
        addToGroup(HEARTLESS_EMBLEM, TYPE_YELLOW_OPERA.get(), 6);
        addToGroup(HEARTLESS_EMBLEM, TYPE_GREEN_REQUIEM.get(), 6);
        addToGroup(HEARTLESS_EMBLEM, TYPE_EMERALD_BLUES.get(), 8);
        addToGroup(HEARTLESS_EMBLEM, TYPE_LARGE_BODY.get(), 8);
        addToGroup(HEARTLESS_EMBLEM, TYPE_DIRE_PLANT.get(), 0);
        addToGroup(HEARTLESS_EMBLEM, TYPE_SOLDIER.get(), 3);
        addToGroup(HEARTLESS_EMBLEM, TYPE_WHITE_MUSHROOM.get(), 20);
        addToGroup(HEARTLESS_EMBLEM, TYPE_BLACK_FUNGUS.get(), 25);


        addToGroup(NOBODY, TYPE_NOBODY_CREEPER.get(), 4);
        addToGroup(NOBODY, TYPE_DUSK.get(), 0);
        addToGroup(NOBODY, TYPE_ASSASSIN.get(), 15);
        addToGroup(NOBODY, TYPE_DRAGOON.get(), 10);

        int purebloodChance = Integer.parseInt(ModConfigs.mobSpawnRate.get(0).split(",")[1]);
		int emblemChance = Integer.parseInt(ModConfigs.mobSpawnRate.get(1).split(",")[1]);
		int nobodyChance = Integer.parseInt(ModConfigs.mobSpawnRate.get(2).split(",")[1]);
		int num = world.random.nextInt(100);
		List<Monster> mobs = new ArrayList<Monster>();

		if(num <= purebloodChance) {
			for(Entry<EntityType<? extends Entity>, Integer> entry : pureblood.entrySet()) {
				if(entry.getValue() <= level) {
					mobs.add((Monster) entry.getKey().create(world));
				}
			}
		} else if(num > purebloodChance && num <= purebloodChance + emblemChance) {
			for(Entry<EntityType<? extends Entity>, Integer> entry : emblem.entrySet()) {
				if(entry.getValue() <= level) {
					mobs.add((Monster) entry.getKey().create(world));
				}
			}
		} else if(num > purebloodChance + emblemChance && num <= purebloodChance + emblemChance + nobodyChance){
			for(Entry<EntityType<? extends Entity>, Integer> entry : nobody.entrySet()) {
				if(entry.getValue() <= level) {
					mobs.add((Monster) entry.getKey().create(world));
				}
			}
		} else {
			KingdomKeys.LOGGER.error("No spawning, config is not adding up to 100 percent");
		}
    	
		return mobs.get(world.random.nextInt(mobs.size()));		 
	}

    @SubscribeEvent
    public static void registerPlacements(RegisterSpawnPlacementsEvent event) {
    	event.register(TYPE_ASSASSIN.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Monster::checkAnyLightMonsterSpawnRules, RegisterSpawnPlacementsEvent.Operation.AND);
        event.register(TYPE_BLUE_RHAPSODY.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Monster::checkMonsterSpawnRules, RegisterSpawnPlacementsEvent.Operation.AND);
        event.register(TYPE_DARKBALL.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Monster::checkMonsterSpawnRules, RegisterSpawnPlacementsEvent.Operation.AND);
        event.register(TYPE_DETONATOR.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Monster::checkMonsterSpawnRules, RegisterSpawnPlacementsEvent.Operation.AND);
        event.register(TYPE_DIRE_PLANT.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Monster::checkMonsterSpawnRules, RegisterSpawnPlacementsEvent.Operation.AND);
        event.register(TYPE_DUSK.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Monster::checkAnyLightMonsterSpawnRules, RegisterSpawnPlacementsEvent.Operation.AND);
        event.register(TYPE_GIGA_SHADOW.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Monster::checkMonsterSpawnRules, RegisterSpawnPlacementsEvent.Operation.AND);
        event.register(TYPE_GREEN_REQUIEM.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Monster::checkMonsterSpawnRules, RegisterSpawnPlacementsEvent.Operation.AND);
        event.register(TYPE_EMERALD_BLUES.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Monster::checkMonsterSpawnRules, RegisterSpawnPlacementsEvent.Operation.AND);
        event.register(TYPE_LARGE_BODY.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Monster::checkMonsterSpawnRules, RegisterSpawnPlacementsEvent.Operation.AND);
        event.register(TYPE_MEGA_SHADOW.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Monster::checkMonsterSpawnRules, RegisterSpawnPlacementsEvent.Operation.AND);
        event.register(TYPE_MINUTE_BOMB.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Monster::checkMonsterSpawnRules, RegisterSpawnPlacementsEvent.Operation.AND);
        event.register(TYPE_MOOGLE.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, PathfinderMob::checkMobSpawnRules, RegisterSpawnPlacementsEvent.Operation.AND);
        event.register(TYPE_NOBODY_CREEPER.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Monster::checkAnyLightMonsterSpawnRules, RegisterSpawnPlacementsEvent.Operation.AND);
        event.register(TYPE_RED_NOCTURNE.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Monster::checkMonsterSpawnRules, RegisterSpawnPlacementsEvent.Operation.AND);
        event.register(TYPE_SHADOW.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Monster::checkMonsterSpawnRules, RegisterSpawnPlacementsEvent.Operation.AND);
        event.register(TYPE_SHADOW_GLOB.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Monster::checkMonsterSpawnRules, RegisterSpawnPlacementsEvent.Operation.AND);
        event.register(TYPE_SKATER_BOMB.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Monster::checkMonsterSpawnRules, RegisterSpawnPlacementsEvent.Operation.AND);
        event.register(TYPE_STORM_BOMB.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Monster::checkMonsterSpawnRules, RegisterSpawnPlacementsEvent.Operation.AND);
        event.register(TYPE_YELLOW_OPERA.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Monster::checkMonsterSpawnRules, RegisterSpawnPlacementsEvent.Operation.AND);
        event.register(TYPE_SPAWNING_ORB.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Monster::checkMonsterSpawnRules, RegisterSpawnPlacementsEvent.Operation.AND);
        event.register(TYPE_SOLDIER.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Monster::checkMonsterSpawnRules, RegisterSpawnPlacementsEvent.Operation.AND);
        event.register(TYPE_WHITE_MUSHROOM.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Monster::checkMonsterSpawnRules, RegisterSpawnPlacementsEvent.Operation.AND);
        event.register(TYPE_BLACK_FUNGUS.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Monster::checkMonsterSpawnRules, RegisterSpawnPlacementsEvent.Operation.AND);
        event.register(TYPE_DRAGOON.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Monster::checkMonsterSpawnRules, RegisterSpawnPlacementsEvent.Operation.AND);
        
        event.register(TYPE_MARLUXIA.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Monster::checkMonsterSpawnRules, RegisterSpawnPlacementsEvent.Operation.AND);
    }

    public static final DeferredRegister<BlockEntityType<?>> TILE_ENTITIES = DeferredRegister.create(BuiltInRegistries.BLOCK_ENTITY_TYPE, KingdomKeys.MODID);

    public static final Supplier<BlockEntityType<MagnetBloxTileEntity>> TYPE_MAGNET_BLOX = TILE_ENTITIES.register("magnet_blox", () -> BlockEntityType.Builder.of(MagnetBloxTileEntity::new, ModBlocks.magnetBlox.get()).build(null));
    public static final Supplier<BlockEntityType<SavepointTileEntity>> TYPE_SAVEPOINT = TILE_ENTITIES.register("savepoint", () -> BlockEntityType.Builder.of(SavepointTileEntity::new, ModBlocks.savepoint.get()).build(null));
    public static final Supplier<BlockEntityType<PedestalTileEntity>> TYPE_PEDESTAL = TILE_ENTITIES.register("pedestal", () -> BlockEntityType.Builder.of(PedestalTileEntity::new, ModBlocks.pedestal.get()).build(null));
    public static final Supplier<BlockEntityType<MagicalChestTileEntity>> TYPE_MAGICAL_CHEST = TILE_ENTITIES.register("magical_chest", () -> BlockEntityType.Builder.of(MagicalChestTileEntity::new, ModBlocks.magicalChest.get()).build(null));
    public static final Supplier<BlockEntityType<OrgPortalTileEntity>> TYPE_ORG_PORTAL_TE = TILE_ENTITIES.register("org_portal", () -> BlockEntityType.Builder.of(OrgPortalTileEntity::new, ModBlocks.orgPortal.get()).build(null));
    public static final Supplier<BlockEntityType<MoogleProjectorTileEntity>> TYPE_MOOGLE_PROJECTOR = TILE_ENTITIES.register("moogle_projector", () -> BlockEntityType.Builder.of(MoogleProjectorTileEntity::new, ModBlocks.moogleProjector.get()).build(null));
    public static final Supplier<BlockEntityType<SoAPlatformTileEntity>> TYPE_SOA_PLATFORM = TILE_ENTITIES.register("soa_platform", () -> BlockEntityType.Builder.of(SoAPlatformTileEntity::new, ModBlocks.station_of_awakening_core.get()).build(null));
    public static final Supplier<BlockEntityType<GummiEditorTileEntity>> TYPE_GUMMI_EDITOR = TILE_ENTITIES.register("gummi_editor", () -> BlockEntityType.Builder.of(GummiEditorTileEntity::new, ModBlocks.gummiEditor.get()).build(null));
    public static final Supplier<BlockEntityType<SoRCoreTileEntity>> TYPE_SOR_CORE_TE = TILE_ENTITIES.register("sor_core", () -> BlockEntityType.Builder.of(SoRCoreTileEntity::new, ModBlocks.sorCore.get()).build(null));
    public static final Supplier<BlockEntityType<CardDoorTileEntity>> TYPE_CARD_DOOR = TILE_ENTITIES.register("card_door", () -> BlockEntityType.Builder.of(CardDoorTileEntity::new, ModBlocks.cardDoor.get()).build(null));
    public static final Supplier<BlockEntityType<AirStepTargetEntity>> TYPE_AIRSTEP_TARGET_TE = TILE_ENTITIES.register("airstep_target", () -> BlockEntityType.Builder.of(AirStepTargetEntity::new, ModBlocks.airstepTarget.get()).build(null));
}
